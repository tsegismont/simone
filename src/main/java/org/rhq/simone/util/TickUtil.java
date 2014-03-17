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

package org.rhq.simone.util;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.TimeUnit;

/**
 * @author Thomas Segismont
 */
public class TickUtil {

  private TickUtil() {
    // Util
  }

  /**
   * @param clockTicks     count of clock ticks
   * @param ticksPerSecond the number of clock ticks per second
   * @param unit           the target {@link TimeUnit} of the result
   * @return time corresponding to <code>clockTicks</code> in the given <code>unit</code>
   */
  public static long ticksToTimeUnit(long clockTicks, long ticksPerSecond, TimeUnit unit) {
    switch (unit) {
      case DAYS:
      case HOURS:
      case MINUTES:
        return unit.convert(clockTicks / ticksPerSecond, SECONDS);
      case SECONDS:
        return clockTicks / ticksPerSecond;
      case MILLISECONDS:
      case MICROSECONDS:
      case NANOSECONDS:
        return unit.convert(1, SECONDS) * clockTicks / ticksPerSecond;
      default:
        throw new UnsupportedOperationException(String.valueOf(unit));
    }
  }
}
