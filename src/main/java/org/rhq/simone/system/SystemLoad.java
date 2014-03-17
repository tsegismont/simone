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

package org.rhq.simone.system;

/**
 * @author Thomas Segismont
 */
public class SystemLoad {

  private final long upTime;
  private final long idleTime;
  private final float load1Minute;
  private final float load5Minutes;
  private final float load15Minutes;

  public SystemLoad(long upTime, long idleTime, float load1Minute, float load5Minutes, float load15Minutes) {
    this.upTime = upTime;
    this.idleTime = idleTime;
    this.load1Minute = load1Minute;
    this.load5Minutes = load5Minutes;
    this.load15Minutes = load15Minutes;
  }

  /**
   * @return uptime of the system in seconds
   */
  public long getUpTime() {
    return upTime;
  }

  /**
   * @return idle time of the system in seconds
   */
  public long getIdleTime() {
    return idleTime;
  }

  /**
   * @return average number of processes that are either in a runnable or uninterruptable state during the last minute
   */
  public float getLoad1Minute() {
    return load1Minute;
  }

  /**
   * @return average number of processes that are either in a runnable or uninterruptable state during the last 5
   *         minutes
   */
  public float getLoad5Minutes() {
    return load5Minutes;
  }

  /**
   * @return average number of processes that are either in a runnable or uninterruptable state during the last 15
   *         minutes
   */
  public float getLoad15Minutes() {
    return load15Minutes;
  }

  @Override
  public String toString() {
    return "SystemLoad[" +
           "upTime(seconds)=" + upTime +
           ", idleTime(seconds)=" + idleTime +
           ", load1Minute=" + load1Minute +
           ", load5Minutes=" + load5Minutes +
           ", load15Minutes=" + load15Minutes +
           ']';
  }
}
