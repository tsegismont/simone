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

package org.rhq.simone.system.linux;

import static java.lang.Float.parseFloat;
import static java.lang.Long.parseLong;
import static java.math.RoundingMode.HALF_EVEN;
import static org.rhq.simone.util.IOUtil.readFirstLine;
import static org.rhq.simone.util.IOUtil.readLines;
import static org.rhq.simone.util.SizeUnit.BYTES;
import static org.rhq.simone.util.SizeUnit.KILOBYTES;
import static org.rhq.simone.util.SizeUnit.MEGABYTES;

import jnr.posix.POSIX;
import jnr.posix.POSIXFactory;
import org.rhq.simone.system.SystemLoad;
import org.rhq.simone.system.SystemMemoryUsage;
import org.rhq.simone.system.SystemService;
import org.rhq.simone.util.SizeUnit;
import org.rhq.simone.util.log.Log;
import org.rhq.simone.util.log.LogFactory;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Thomas Segismont
 */
public class LinuxSystemService implements SystemService {

  private static final Log LOG = LogFactory.getLog(LinuxSystemService.class);

  public LinuxSystemService(POSIX posix) {
  }

  @Override
  public SystemLoad getSystemLoad() {
    String uptimeLine = readFirstLine(new File("/proc/uptime"));
    StringTokenizer uptimeTokenizer = new StringTokenizer(uptimeLine);
    long upTime = new BigDecimal(uptimeTokenizer.nextToken()).setScale(0, HALF_EVEN).longValue();
    long idleTime = new BigDecimal(uptimeTokenizer.nextToken()).setScale(0, HALF_EVEN).longValue();
    String loadLine = readFirstLine(new File("/proc/loadavg"));
    StringTokenizer loadLineTokenizer = new StringTokenizer(loadLine);
    float load1Minute = parseFloat(loadLineTokenizer.nextToken());
    float load5Minutes = parseFloat(loadLineTokenizer.nextToken());
    float load15Minutes = parseFloat(loadLineTokenizer.nextToken());
    return new SystemLoad(upTime, idleTime, load1Minute, load5Minutes, load15Minutes);
  }

  @Override
  public SystemMemoryUsage getSystemMemoryUsage() {
    Map<String, String> details = new HashMap<String, String>();
    for (String line : readLines(new File("/proc/meminfo"))) {
      int separatorIndex = line.indexOf(":");
      if (separatorIndex <= 0 || separatorIndex == line.length() - 1) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Invalid detail line format: " + line);
        }
        continue;
      }
      details.put(line.substring(0, separatorIndex).trim(), line.substring(separatorIndex + 1).trim());
    }
    long total = readSizeFromDetail(details.get("MemTotal"), KILOBYTES, KILOBYTES);
    long free = readSizeFromDetail(details.get("MemFree"), KILOBYTES, KILOBYTES);
    long active = readSizeFromDetail(details.get("Active"), KILOBYTES, KILOBYTES);
    long inactive = readSizeFromDetail(details.get("Inactive"), KILOBYTES, KILOBYTES);
    long swapTotal = readSizeFromDetail(details.get("SwapTotal"), KILOBYTES, KILOBYTES);
    long swapFree = readSizeFromDetail(details.get("SwapFree"), KILOBYTES, KILOBYTES);
    return new SystemMemoryUsage(total, free, active, inactive, swapTotal, swapFree);
  }

  private long readSizeFromDetail(String detail, SizeUnit defaultUnit, SizeUnit targetUnit) {
    StringTokenizer tokenizer = new StringTokenizer(detail);
    long value = parseLong(tokenizer.nextToken());
    SizeUnit unit = tokenizer.hasMoreTokens() ? getSizeUnitFromString(tokenizer.nextToken()) : defaultUnit;
    return targetUnit.convert(value, unit);
  }

  private SizeUnit getSizeUnitFromString(String unitString) {
    if ("kb".equalsIgnoreCase(unitString)) {
      return KILOBYTES;
    }
    if ("mb".equalsIgnoreCase(unitString)) {
      return MEGABYTES;
    }
    return BYTES;
  }

  public static void main(String[] args) {
    LinuxSystemService service = new LinuxSystemService(POSIXFactory.getPOSIX());
    System.out.println(service.getSystemLoad());
    System.out.println(service.getSystemMemoryUsage().toStringRatio());
  }
}
