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

import static jnr.ffi.Platform.getNativePlatform;

import jnr.ffi.Platform;
import jnr.posix.POSIX;
import jnr.posix.POSIXFactory;
import org.rhq.simone.process.linux.LinuxProcessService;
import org.rhq.simone.util.log.Log;
import org.rhq.simone.util.log.LogFactory;

/**
 * @author Thomas Segismont
 */
public class ProcessServiceFactory {

  private static final Log LOG = LogFactory.getLog(ProcessServiceFactory.class);

  private ProcessServiceFactory() {
    // Factory
  }

  public static ProcessService createProcessService() {
    return createProcessService(POSIXFactory.getPOSIX());
  }

  public static ProcessService createProcessService(POSIX posix) {
    try {
      Platform.OS os = getNativePlatform().getOS();
      switch (os) {
        case LINUX:
          return createLinuxProcessService(posix);
        default:
          return null;
      }
    } catch (Throwable t) {
      LOG.error("Failed to create ProcessService", t);
      return null;
    }
  }

  public static ProcessService createLinuxProcessService(POSIX posix) {
    return new LinuxProcessService(posix);
  }
}
