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

import static java.lang.Integer.parseInt;
import static java.math.RoundingMode.HALF_EVEN;
import static org.rhq.simone.util.SizeUnit.MEGABYTES;

import org.rhq.simone.cpu.CpuDetails;
import org.rhq.simone.util.log.Log;
import org.rhq.simone.util.log.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * @author Thomas Segismont
 */
class CpuDetailsIterator implements Iterator<CpuDetails> {

  private static final Log LOG = LogFactory.getLog(CpuDetailsIterator.class);

  private final BufferedReader bufferedReader;

  private CpuDetails next;

  public CpuDetailsIterator(BufferedReader bufferedReader) {
    this.bufferedReader = bufferedReader;
    next = null;
  }

  @Override
  public boolean hasNext() {
    if (next == null) {
      try {
        next = readNextCpuDetails();
      } catch (IOException ignore) {
      }
    }
    return next != null;
  }

  @Override
  public CpuDetails next() {
    if (hasNext()) {
      CpuDetails cpuDetails = next;
      next = null;
      return cpuDetails;
    }
    throw new NoSuchElementException();
  }

  private CpuDetails readNextCpuDetails() throws IOException {
    Map<String, String> details = new HashMap<String, String>();
    for (String line; ; ) {
      line = bufferedReader.readLine();
      if (line == null || line.isEmpty()) {
        break;
      }
      if (LOG.isTraceEnabled()) {
        LOG.trace("Detail line: " + line);
      }
      int separatorIndex = line.indexOf(":");
      if (separatorIndex <= 0 || separatorIndex == line.length() - 1) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Invalid detail line format: " + line);
        }
        continue;
      }
      details.put(line.substring(0, separatorIndex).trim(), line.substring(separatorIndex + 1).trim());
    }
    if (details.isEmpty()) {
      return null;
    }
    return createCpuFromDetails(details);
  }

  private CpuDetails createCpuFromDetails(Map<String, String> details) {
    String vendorId = details.get("vendor_id");
    String modelName = details.get("model name");
    int cpuMhz = new BigDecimal(details.get("cpu MHz")).setScale(0, HALF_EVEN).intValue();
    String cacheSizeWithUnit = details.get("cache size");
    StringTokenizer tokenizer = new StringTokenizer(cacheSizeWithUnit);
    int cacheSize = parseInt(tokenizer.nextToken());
    if (tokenizer.hasMoreTokens()) {
      String cacheSizeUnit = tokenizer.nextToken();
      if ("MB".equalsIgnoreCase(cacheSizeUnit)) {
        cacheSize = (int) MEGABYTES.toKiloBytes(cacheSize);
      }
    }
    return new CpuDetails(vendorId, modelName, cpuMhz, cacheSize);
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }
}
