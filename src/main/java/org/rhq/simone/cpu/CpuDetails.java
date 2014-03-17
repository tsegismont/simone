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

package org.rhq.simone.cpu;

/**
 * @author Thomas Segismont
 */
public class CpuDetails {

  private final String vendorId;
  private final String modelName;
  private final int cpuFrequency;
  private final int cacheSize;

  public CpuDetails(String vendorId, String modelName, int cpuFrequency, int cacheSize) {
    this.vendorId = vendorId;
    this.modelName = modelName;
    this.cpuFrequency = cpuFrequency;
    this.cacheSize = cacheSize;
  }

  public String getVendorId() {
    return vendorId;
  }

  public String getModelName() {
    return modelName;
  }

  /**
   * @return frequency in MHz
   */
  public int getCpuFrequency() {
    return cpuFrequency;
  }

  /**
   * @return cache size in kilobytes
   */
  public int getCacheSize() {
    return cacheSize;
  }

  @Override
  public String toString() {
    return "CpuDetails[" +
           "vendorId='" + vendorId + '\'' +
           ", modelName='" + modelName + '\'' +
           ", cpuFrequency(MHz)=" + cpuFrequency +
           ", cacheSize(KB)=" + cacheSize +
           ']';
  }
}
