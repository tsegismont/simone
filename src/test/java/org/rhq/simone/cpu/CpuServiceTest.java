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

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeFalse;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Thomas Segismont
 */
public class CpuServiceTest {

  private CpuService cpuService;

  @Before
  public void setUp() throws Exception {
    cpuService = CpuServiceFactory.createCpuService();
    assumeFalse("No CpuService in this environment", cpuService == null);
  }

  @Test
  public void testCpuCount() throws Exception {
    assertEquals(Runtime.getRuntime().availableProcessors(), cpuService.getCpuCount());
    assertEquals(Runtime.getRuntime().availableProcessors(), cpuService.getAllCpuDetails().size());
    assertEquals(Runtime.getRuntime().availableProcessors(), cpuService.getAllCpuUsage().size());
  }
}
