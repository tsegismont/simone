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

package org.rhq.simone.cpu.linux;

import static java.lang.Long.parseLong;
import static org.rhq.simone.util.IOUtil.closeQuietly;
import static org.rhq.simone.util.IOUtil.readLines;

import jnr.constants.platform.Sysconf;
import jnr.posix.POSIX;
import jnr.posix.POSIXFactory;
import org.rhq.simone.cpu.CpuDetails;
import org.rhq.simone.cpu.CpuService;
import org.rhq.simone.cpu.CpuUsage;
import org.rhq.simone.cpu.CpuUsageRatio;
import org.rhq.simone.util.IOUtil;
import org.rhq.simone.util.log.Log;
import org.rhq.simone.util.log.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Thomas Segismont
 */
public class LinuxCpuService implements CpuService {

  private static final Log LOG = LogFactory.getLog(LinuxCpuService.class);

  private static final CpuUsageLineFilter CPU_USAGE_LINE_FILTER = new CpuUsageLineFilter();

  private final POSIX posix;
  private final int cpuCount;
  private final long ticks;

  public LinuxCpuService(POSIX posix) {
    this.posix = posix;
    cpuCount = (int) posix.sysconf(Sysconf._SC_NPROCESSORS_CONF);
    ticks = posix.sysconf(Sysconf._SC_CLK_TCK);
  }

  @Override
  public int getCpuCount() {
    return cpuCount;
  }

  @Override
  public List<CpuDetails> getAllCpuDetails() {
    List<CpuDetails> result = new ArrayList<CpuDetails>();
    FileReader fileReader = null;
    BufferedReader bufferedReader = null;
    CpuDetailsParser cpuDetailsParser = null;
    try {
      fileReader = new FileReader("/proc/cpuinfo");
      bufferedReader = new BufferedReader(fileReader);
      cpuDetailsParser = new CpuDetailsParser(bufferedReader);
      for (CpuDetails cpuDetails : cpuDetailsParser) {
        result.add(cpuDetails);
      }
    } catch (FileNotFoundException e) {
      return result;
    } finally {
      closeQuietly(cpuDetailsParser);
      closeQuietly(bufferedReader);
      closeQuietly(fileReader);
    }
    return result;
  }

  @Override
  public List<CpuUsage> getAllCpuUsage() {
    List<String> cpuLines = readLines(new File("/proc/stat"), CPU_USAGE_LINE_FILTER);
    List<String> cpuLinesView;
    if (cpuCount == 1) {
      cpuLinesView = cpuLines;
    } else {
      cpuLinesView = cpuLines.subList(1, cpuLines.size());
    }
    List<CpuUsage> result = new ArrayList<CpuUsage>(cpuLinesView.size());
    for (String line : cpuLinesView) {
      try {
        result.add(parseCpuUsageLine(line));
      } catch (IOException ignore) {
      }
    }
    return result;
  }

  private CpuUsage parseCpuUsageLine(String line) throws IOException {
    if (LOG.isTraceEnabled()) {
      LOG.trace("Detail line: " + line);
    }
    StringTokenizer tokenizer = new StringTokenizer(line);
    tokenizer.nextToken(); // Advance past 'cpu' token
    long user = (1000 * parseLong(tokenizer.nextToken())) / ticks;
    long nice = (1000 * parseLong(tokenizer.nextToken())) / ticks;
    long system = (1000 * parseLong(tokenizer.nextToken())) / ticks;
    long idle = (1000 * parseLong(tokenizer.nextToken())) / ticks;
    long ioWait = 0, irq = 0, softIrq = 0, steal = 0, guest = 0, guestNice = 0;
    if (tokenizer.hasMoreTokens()) {
      ioWait = (1000 * parseLong(tokenizer.nextToken())) / ticks;
    }
    if (tokenizer.hasMoreTokens()) {
      irq = (1000 * parseLong(tokenizer.nextToken())) / ticks;
    }
    if (tokenizer.hasMoreTokens()) {
      softIrq = (1000 * parseLong(tokenizer.nextToken())) / ticks;
    }
    if (tokenizer.hasMoreTokens()) {
      steal = (1000 * parseLong(tokenizer.nextToken())) / ticks;
    }
    if (tokenizer.hasMoreTokens()) {
      guest = (1000 * parseLong(tokenizer.nextToken())) / ticks;
    }
    if (tokenizer.hasMoreTokens()) {
      guestNice = (1000 * parseLong(tokenizer.nextToken())) / ticks;
    }
    return new CpuUsage(user, nice, system, idle, ioWait, irq, softIrq, steal, guest, guestNice);
  }

  private static class CpuUsageLineFilter implements IOUtil.LineFilter {

    @Override
    public boolean accept(String line) {
      return line.startsWith("cpu");
    }
  }

  public static void main(String[] args) throws Exception {
    LinuxCpuService linuxCpuService = new LinuxCpuService(POSIXFactory.getPOSIX());
    List<CpuDetails> allCpuDetails = linuxCpuService.getAllCpuDetails();
    for (CpuDetails cpuDetails : allCpuDetails) {
      System.out.println("cpuDetails = " + cpuDetails);
    }
    List<CpuUsage> allCpuUsage1 = linuxCpuService.getAllCpuUsage();
    for (CpuUsage cpuUsage : allCpuUsage1) {
      System.out.println("cpuUsage = " + cpuUsage);
    }
    Thread.sleep(1 * 1000l);
    List<CpuUsage> allCpuUsage2 = linuxCpuService.getAllCpuUsage();
    for (int i = 0; i < allCpuUsage2.size(); i++) {
      CpuUsageRatio cpuUsageRatio = new CpuUsageRatio(allCpuUsage1.get(i), allCpuUsage2.get(i));
      System.out.println("cpuUsageRatio = " + cpuUsageRatio);
    }
  }
}
