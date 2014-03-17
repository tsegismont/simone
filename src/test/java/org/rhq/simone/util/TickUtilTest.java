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

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static junit.framework.Assert.assertEquals;
import static org.rhq.simone.util.TickUtil.ticksToTimeUnit;

import org.junit.Test;

/**
 * @author Thomas Segismont
 */
public class TickUtilTest {

  @Test
  public void testTicksToTimeUnit() throws Exception {
    long ticksPerSecond = 87, clockTicks = ticksPerSecond * DAYS.toSeconds(1);
    assertEquals(1, ticksToTimeUnit(clockTicks, ticksPerSecond, DAYS));
    assertEquals(DAYS.toHours(1), ticksToTimeUnit(clockTicks, ticksPerSecond, HOURS));
    assertEquals(DAYS.toMinutes(1), ticksToTimeUnit(clockTicks, ticksPerSecond, MINUTES));
    assertEquals(DAYS.toSeconds(1), ticksToTimeUnit(clockTicks, ticksPerSecond, SECONDS));
    assertEquals(DAYS.toMillis(1), ticksToTimeUnit(clockTicks, ticksPerSecond, MILLISECONDS));
    assertEquals(DAYS.toMicros(1), ticksToTimeUnit(clockTicks, ticksPerSecond, MICROSECONDS));
    assertEquals(DAYS.toNanos(1), ticksToTimeUnit(clockTicks, ticksPerSecond, NANOSECONDS));
  }
}
