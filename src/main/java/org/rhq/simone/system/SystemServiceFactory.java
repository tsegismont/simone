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

import static jnr.ffi.Platform.getNativePlatform;

import jnr.ffi.Platform;
import jnr.posix.POSIX;
import jnr.posix.POSIXFactory;
import org.rhq.simone.system.linux.LinuxSystemService;
import org.rhq.simone.util.log.Log;
import org.rhq.simone.util.log.LogFactory;

/**
 * @author Thomas Segismont
 */
public class SystemServiceFactory {

  private static final Log LOG = LogFactory.getLog(SystemServiceFactory.class);

  private SystemServiceFactory() {
    // Factory
  }

  public static SystemService createSystemService() {
    return createSystemService(POSIXFactory.getPOSIX());
  }

  public static SystemService createSystemService(POSIX posix) {
    try {
      Platform.OS os = getNativePlatform().getOS();
      switch (os) {
        case LINUX:
          return createLinuxSystemService(posix);
        default:
          return null;
      }
    } catch (Throwable t) {
      LOG.error("Failed to create SystemService", t);
      return null;
    }
  }

  public static SystemService createLinuxSystemService(POSIX posix) {
    return new LinuxSystemService(posix);
  }
}
