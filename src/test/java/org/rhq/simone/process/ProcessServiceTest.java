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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

import jnr.posix.POSIXFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Thomas Segismont
 */
public class ProcessServiceTest {

  private static final int OWN_PID = POSIXFactory.getPOSIX().getpid();

  private ProcessService processService;

  @Before
  public void setUp() throws Exception {
    processService = ProcessServiceFactory.createProcessService();
    assumeFalse("No ProcessService in this environment", processService == null);
  }

  @Test
  public void testGetPid() throws Exception {
    assertTrue(processService.getPid() > 0);
  }

  @Test
  public void testGetAllPids() throws Exception {
    int[] pids = processService.getAllPids();
    assertNotNull(pids);
    assertFalse(pids.length == 0);
    Arrays.sort(pids);
    assertTrue(Arrays.binarySearch(pids, OWN_PID) >= 0);
  }

  @Test
  public void testCurrentProcessDetails() throws Exception {
    ProcessDetails details = processService.getProcessDetails(OWN_PID);
    assertNotNull(details);
    assertTrue(details.isRunning());
  }

  @Test
  public void testCurrentProcessExecutionContext() throws Exception {
    ProcessExecutionContext context = processService.getProcessExecutionContext(OWN_PID);
    assertNotNull(context);
    assertEquals(System.getProperty("user.name"), context.getUser());
    assertTrue(System.getenv().entrySet().containsAll(context.getEnvironment().entrySet()));
  }
}
