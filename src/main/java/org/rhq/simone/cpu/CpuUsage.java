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
 * Milliseconds spent by the system, since it started, in various states.
 *
 * @author Thomas Segismont
 */
public class CpuUsage {

  private final long user;
  private final long nice;
  private final long system;
  private final long idle;
  private final long ioWait;
  private final long irq;
  private final long softIrq;
  private final long steal;
  private final long guest;
  private final long guestNice;

  public CpuUsage(long user, long nice, long system, long idle, long ioWait, long irq, long softIrq, long steal,
                  long guest, long guestNice) {
    this.user = user;
    this.nice = nice;
    this.system = system;
    this.idle = idle;
    this.ioWait = ioWait;
    this.irq = irq;
    this.softIrq = softIrq;
    this.steal = steal;
    this.guest = guest;
    this.guestNice = guestNice;
  }

  public long getUser() {
    return user;
  }

  public long getNice() {
    return nice;
  }

  public long getSystem() {
    return system;
  }

  public long getIdle() {
    return idle;
  }

  public long getIoWait() {
    return ioWait;
  }

  public long getIrq() {
    return irq;
  }

  public long getSoftIrq() {
    return softIrq;
  }

  public long getSteal() {
    return steal;
  }

  public long getGuest() {
    return guest;
  }

  public long getGuestNice() {
    return guestNice;
  }

  public long getTotal() {
    return user + nice + system + idle + ioWait + irq + softIrq + steal + guest + guestNice;
  }

  @Override
  public String toString() {
    return "CpuUsage(milliseconds)[" +
           "user=" + user +
           ", nice=" + nice +
           ", system=" + system +
           ", idle=" + idle +
           ", ioWait=" + ioWait +
           ", irq=" + irq +
           ", softIrq=" + softIrq +
           ", steal=" + steal +
           ", guest=" + guest +
           ", guestNice=" + guestNice +
           ']';
  }

}
