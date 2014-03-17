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

package org.rhq.simone.process;

/**
 * @author Thomas Segismont
 */
public class ProcessMemoryUsage {

  private final long size;
  private final long resident;
  private final long shared;

  public ProcessMemoryUsage(long size, long resident, long shared) {
    this.size = size;
    this.resident = resident;
    this.shared = shared;
  }

  /**
   * @return total program size in kilobytes
   */
  public long getSize() {
    return size;
  }

  /**
   * @return resident set size in kilobytes
   */
  public long getResident() {
    return resident;
  }

  /**
   * @return shared size in kilobytes
   */
  public long getShared() {
    return shared;
  }

  @Override
  public String toString() {
    return "ProcessMemoryUsage(KB)[" +
           "size=" + size +
           ", resident=" + resident +
           ", shared=" + shared +
           ']';
  }
}
