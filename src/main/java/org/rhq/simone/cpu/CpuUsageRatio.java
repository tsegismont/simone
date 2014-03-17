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

import java.text.NumberFormat;

/**
 * Ratio of time spent by the system, between two measures, in various states.
 *
 * @author Thomas Segismont
 */
public class CpuUsageRatio {

  private final float user;
  private final float nice;
  private final float system;
  private final float idle;
  private final float ioWait;
  private final float irq;
  private final float softIrq;
  private final float steal;
  private final float guest;
  private final float guestNice;
  private final long sampleTime;

  public CpuUsageRatio(CpuUsage start, CpuUsage end) {
    sampleTime = end.getTotal() - start.getTotal();
    if (sampleTime <= 0) {
      user = nice = system = idle = ioWait = irq = softIrq = steal = guest = guestNice = 0f;
    } else {
      user = getRatio(start.getUser(), end.getUser(), sampleTime);
      nice = getRatio(start.getNice(), end.getNice(), sampleTime);
      system = getRatio(start.getSystem(), end.getSystem(), sampleTime);
      idle = getRatio(start.getIdle(), end.getIdle(), sampleTime);
      ioWait = getRatio(start.getIoWait(), end.getIoWait(), sampleTime);
      irq = getRatio(start.getIrq(), end.getIrq(), sampleTime);
      softIrq = getRatio(start.getSoftIrq(), end.getSoftIrq(), sampleTime);
      steal = getRatio(start.getSteal(), end.getSteal(), sampleTime);
      guest = getRatio(start.getGuest(), end.getGuest(), sampleTime);
      guestNice = getRatio(start.getGuestNice(), end.getGuestNice(), sampleTime);
    }
  }

  private float getRatio(long start, long end, long totalDifference) {
    long difference = end - start;
    if (difference > totalDifference) {
      return 0f;
    }
    return (float) (difference / (double) totalDifference);
  }

  public float getUser() {
    return user;
  }

  public float getNice() {
    return nice;
  }

  public float getSystem() {
    return system;
  }

  public float getIdle() {
    return idle;
  }

  public float getIoWait() {
    return ioWait;
  }

  public float getIrq() {
    return irq;
  }

  public float getSoftIrq() {
    return softIrq;
  }

  public float getSteal() {
    return steal;
  }

  public float getGuest() {
    return guest;
  }

  public float getGuestNice() {
    return guestNice;
  }

  /**
   * @return milliseconds between measures
   */
  public long getSampleTime() {
    return sampleTime;
  }

  @Override
  public String toString() {
    NumberFormat formatter = NumberFormat.getPercentInstance();
    formatter.setMinimumFractionDigits(3);
    formatter.setMaximumFractionDigits(3);
    return "CpuUsageRatio[" +
           "user=" + formatter.format(user) +
           ", nice=" + formatter.format(nice) +
           ", system=" + formatter.format(system) +
           ", idle=" + formatter.format(idle) +
           ", ioWait=" + formatter.format(ioWait) +
           ", irq=" + formatter.format(irq) +
           ", softIrq=" + formatter.format(softIrq) +
           ", steal=" + formatter.format(steal) +
           ", guest=" + formatter.format(guest) +
           ", guestNice=" + formatter.format(guestNice) +
           ", sampleTime(milliseconds)=" + sampleTime +
           ']';
  }
}
