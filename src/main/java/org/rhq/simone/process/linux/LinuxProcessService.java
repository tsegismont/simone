/*
 * Copyright 2014 Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rhq.simone.process.linux;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.rhq.simone.process.ProcessDetails.State.PAGING;
import static org.rhq.simone.process.ProcessDetails.State.RUNNING;
import static org.rhq.simone.process.ProcessDetails.State.SLEEPING;
import static org.rhq.simone.process.ProcessDetails.State.STOPPED;
import static org.rhq.simone.process.ProcessDetails.State.UNINTERRUPTIBLE_WAIT;
import static org.rhq.simone.process.ProcessDetails.State.UNKNOWN;
import static org.rhq.simone.process.ProcessDetails.State.ZOMBIE;
import static org.rhq.simone.util.IOUtil.LineFilter;
import static org.rhq.simone.util.IOUtil.readFirstLine;
import static org.rhq.simone.util.IOUtil.readLines;
import static org.rhq.simone.util.SizeUnit.BYTES;
import static org.rhq.simone.util.TickUtil.ticksToTimeUnit;
import static org.rhq.simone.util.TokenizerUtil.skip1Token;
import static org.rhq.simone.util.TokenizerUtil.skipNTokens;

import jnr.constants.platform.Sysconf;
import jnr.posix.POSIX;
import jnr.posix.POSIXFactory;
import org.rhq.simone.process.ProcessCpuUsage;
import org.rhq.simone.process.ProcessDetails;
import org.rhq.simone.process.ProcessExecutionContext;
import org.rhq.simone.process.ProcessMemoryUsage;
import org.rhq.simone.process.ProcessService;
import org.rhq.simone.util.log.Log;
import org.rhq.simone.util.log.LogFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * @author Thomas Segismont
 */
public class LinuxProcessService implements ProcessService {

  private static final Log LOG = LogFactory.getLog(LinuxProcessService.class);

  private static final ProcessFileFilter PROCESS_FILE_FILTER = new ProcessFileFilter();
  private static final int[] EMPTY_INT_ARRAY = new int[0];
  private static final String ENVIRON_DELIM = Character.toString((char) 0);
  static final String UID_PREFIX = "Uid:";
  static final String GID_PREFIX = "Gid:";
  private static final LineFilter STATUS_FILE_UID_GID_FILTER = new StatusFileUidGidFilter();

  private final POSIX posix;
  private final long pageSize;
  private final long ticksPerSecond;

  public LinuxProcessService(POSIX posix) {
    this.posix = posix;
    pageSize = posix.sysconf(Sysconf._SC_PAGESIZE);
    ticksPerSecond = posix.sysconf(Sysconf._SC_CLK_TCK);
  }

  @Override
  public int getPid() {
    return posix.getpid();
  }

  @Override
  public int[] getAllPids() {
    File[] processFiles = new File("/proc").listFiles(PROCESS_FILE_FILTER);
    if (processFiles == null || processFiles.length == 0) {
      return EMPTY_INT_ARRAY;
    }
    int[] pids = new int[processFiles.length];
    for (int i = 0; i < processFiles.length; i++) {
      pids[i] = parseInt(processFiles[i].getName());
    }
    return pids;
  }

  @Override
  public ProcessDetails getProcessDetails(int pid) {
    String line = readFirstLine(new File("/proc/" + pid + "/stat"));
    StringTokenizer tokenizer = new StringTokenizer(line);
    skip1Token(tokenizer);
    String executableName = tokenizer.nextToken();
    executableName = executableName.substring(1, executableName.length() - 1); // remove parentheses
    ProcessDetails.State state;
    String stateString = tokenizer.nextToken();
    switch (stateString.charAt(0)) {
      case 'R':
        state = RUNNING;
        break;
      case 'S':
        state = SLEEPING;
        break;
      case 'D':
        state = UNINTERRUPTIBLE_WAIT;
        break;
      case 'Z':
        state = ZOMBIE;
        break;
      case 'T':
        state = STOPPED;
        break;
      case 'W':
        state = PAGING;
        break;
      default:
        if (LOG.isDebugEnabled()) {
          LOG.debug("Unknown state '" + stateString + "' for process '" + pid + "'");
        }
        state = UNKNOWN;
        break;
    }
    int parentPid = parseInt(tokenizer.nextToken());
    skipNTokens(tokenizer, 15);
    int threadCount = parseInt(tokenizer.nextToken());
    skip1Token(tokenizer);
    long startTime = ticksToTimeUnit(parseLong(tokenizer.nextToken()), ticksPerSecond, SECONDS);
    return new ProcessDetails(pid, parentPid, executableName, state, threadCount, startTime);
  }

  @Override
  public ProcessMemoryUsage getProcessMemoryUsage(int pid) {
    String line = readFirstLine(new File("/proc/" + pid + "/statm"));
    StringTokenizer tokenizer = new StringTokenizer(line);
    long size = BYTES.toKiloBytes(parseLong(tokenizer.nextToken()) * pageSize);
    long resident = BYTES.toKiloBytes(parseLong(tokenizer.nextToken()) * pageSize);
    long shared = BYTES.toKiloBytes(parseLong(tokenizer.nextToken()) * pageSize);
    return new ProcessMemoryUsage(size, resident, shared);
  }

  @Override
  public ProcessCpuUsage getProcessCpuUsage(int pid) {
    String line = readFirstLine(new File("/proc/" + pid + "/stat"));
    StringTokenizer tokenizer = new StringTokenizer(line);
    skipNTokens(tokenizer, 13);
    long user = convertNextTokenFromTicksToMillis(tokenizer);
    long system = convertNextTokenFromTicksToMillis(tokenizer);
    return new ProcessCpuUsage(user, system, System.currentTimeMillis());
  }

  private long convertNextTokenFromTicksToMillis(StringTokenizer tokenizer) {
    return ticksToTimeUnit(parseLong(tokenizer.nextToken()), ticksPerSecond, MILLISECONDS);
  }

  @Override
  public ProcessExecutionContext getProcessExecutionContext(int pid) {
    String executablePath = null;
    try {
      executablePath = new File("/proc/" + pid + "/exe").getCanonicalPath();
    } catch (IOException e) {
    }
    String currentWorkingDirectory = null;
    try {
      currentWorkingDirectory = new File("/proc/" + pid + "/cwd").getCanonicalPath();
    } catch (IOException e) {
    }
    Map<String, String> environment = new HashMap<String, String>();
    String environ = readFirstLine(new File("/proc/" + pid + "/environ"));
    for (StringTokenizer environTokenizer = new StringTokenizer(environ, ENVIRON_DELIM);
         environTokenizer.hasMoreTokens(); ) {
      String environEntry = environTokenizer.nextToken();
      if (environEntry.isEmpty()) {
        continue;
      }
      int delimIndex = environEntry.indexOf("=");
      if (delimIndex <= 0 || delimIndex == environEntry.length() - 1) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Invalid environ entry format: " + environEntry);
        }
        continue;
      }
      environment.put(environEntry.substring(0, delimIndex).trim(), environEntry.substring(delimIndex + 1).trim());
    }
    String user = null, group = null;
    for (String line : readLines(new File("/proc/" + pid + "/status"), STATUS_FILE_UID_GID_FILTER)) {
      StringTokenizer tokenizer = new StringTokenizer(line);
      skip1Token(tokenizer);
      int which = parseInt(tokenizer.nextToken());
      if (line.startsWith(UID_PREFIX)) {
        user = posix.getpwuid(which).getLoginName();
      } else {
        group = posix.getgrgid(which).getName();
      }
    }
    return new ProcessExecutionContext(pid, executablePath, currentWorkingDirectory, environment, user, group);
  }

  private static class ProcessFileFilter implements FileFilter {

    static final Pattern PID_PATTERN = Pattern.compile("[1-9][0-9]*");

    @Override
    public boolean accept(File file) {
      return file.isDirectory() && isPid(file.getName());
    }

    private boolean isPid(String fileName) {
      return PID_PATTERN.matcher(fileName).matches();
    }
  }

  private static class StatusFileUidGidFilter implements LineFilter {

    @Override
    public boolean accept(String line) {
      return line.startsWith(UID_PREFIX) || line.startsWith(GID_PREFIX);
    }
  }

  public static void main(String[] args) {
    POSIX posix = POSIXFactory.getPOSIX();
    LinuxProcessService service = new LinuxProcessService(posix);
    int[] allPids = service.getAllPids();
    System.out.println("allPids.length = " + allPids.length);
    int pid = posix.getpid();
    System.out.println(service.getProcessDetails(pid));
    System.out.println(service.getProcessMemoryUsage(pid));
    System.out.println(service.getProcessExecutionContext(pid));
    System.out.println(service.getProcessCpuUsage(pid));
  }
}
