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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Thomas Segismont
 */
public class SystemServiceTest {

  private SystemService systemService;

  @Before
  public void setUp() throws Exception {
    systemService = SystemServiceFactory.createSystemService();
    assumeFalse("No SystemService in this environment", systemService == null);
  }

  @Test
  public void testGetSystemLoad() throws Exception {
    SystemLoad systemLoad = systemService.getSystemLoad();
    assertNotNull(systemLoad);
    assertTrue(systemLoad.getUpTime() > 0);
  }

  @Test
  public void testGetSystemMemoryUsage() throws Exception {
    SystemMemoryUsage systemMemoryUsage = systemService.getSystemMemoryUsage();
    assertNotNull(systemMemoryUsage);
    assertTrue(systemMemoryUsage.getTotal() > 0);
  }
}
