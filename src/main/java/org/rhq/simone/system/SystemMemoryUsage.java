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

import java.text.NumberFormat;

/**
 * @author Thomas Segismont
 */
public class SystemMemoryUsage {

  private final long total;
  private final long free;
  private final long active;
  private final long inactive;
  private final long swapTotal;
  private final long swapFree;

  public SystemMemoryUsage(long total, long free, long active, long inactive, long swapTotal, long swapFree) {
    this.total = total;
    this.free = free;
    this.active = active;
    this.inactive = inactive;
    this.swapTotal = swapTotal;
    this.swapFree = swapFree;
  }

  /**
   * @return total size in kilobytes
   */
  public long getTotal() {
    return total;
  }

  /**
   * @return free memory in kilobytes
   */
  public long getFree() {
    return free;
  }

  public float getFreeRatio() {
    return (float) (free / (double) total);
  }

  /**
   * @return used memory in kilobytes
   */
  public long getUsed() {
    return total - free;
  }

  public float getUsedRatio() {
    return (float) (getUsed() / (double) total);
  }

  /**
   * @return active memory in kilobytes
   */
  public long getActive() {
    return active;
  }

  public float getActiveRatio() {
    return (float) (active / (double) total);
  }

  /**
   * @return inactive memory in kilobytes
   */
  public long getInactive() {
    return inactive;
  }

  /**
   * @return total swap memory in kilobytes
   */
  public long getSwapTotal() {
    return swapTotal;
  }

  /**
   * @return free swap in kilobytes
   */
  public long getSwapFree() {
    return swapFree;
  }

  /**
   * @return used swap in kilobytes
   */
  public long getSwapUsed() {
    return swapTotal - swapFree;
  }

  public float getSwapUsedRatio() {
    return (float) (getSwapUsed() / (double) swapTotal);
  }

  @Override
  public String toString() {
    return "SystemMemoryUsage(KB)[" +
           "total=" + total +
           ", free=" + free +
           ", active=" + active +
           ", inactive=" + inactive +
           ", swapTotal=" + swapTotal +
           ", swapFree=" + swapFree +
           ']';
  }

  public String toStringRatio() {
    NumberFormat formatter = NumberFormat.getPercentInstance();
    formatter.setMinimumFractionDigits(3);
    formatter.setMaximumFractionDigits(3);
    return "SystemMemoryUsage[" +
           "used=" + formatter.format(getUsedRatio()) +
           ", free=" + formatter.format(getFreeRatio()) +
           ", active=" + formatter.format(getActiveRatio()) +
           ", swapUsed=" + formatter.format(getSwapUsedRatio()) +
           ']';
  }
}
