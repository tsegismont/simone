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

import java.util.Date;

/**
 * Milliseconds spent by the process, since it started, in various states.
 *
 * @author Thomas Segismont
 */
public class ProcessCpuUsage {

  private final long user;
  private final long system;
  private final long timestamp;

  public ProcessCpuUsage(long user, long system, long timestamp) {
    this.user = user;
    this.system = system;
    this.timestamp = timestamp;
  }

  /**
   * @return time that this process has been scheduled in user mode, in milliseconds
   */
  public long getUser() {
    return user;
  }

  /**
   * @return time that this process has been scheduled in kernel mode, in milliseconds
   */
  public long getSystem() {
    return system;
  }

  /**
   * @return timestamp of this snapshot, in milliseconds since epoch
   */
  public long getTimestamp() {
    return timestamp;
  }

  @Override
  public String toString() {
    return "ProcessCpuUsage[" +
           "user(milliseconds)=" + user +
           ", system(milliseconds)=" + system +
           ", timestamp=" + new Date(timestamp) +
           ']';
  }
}
