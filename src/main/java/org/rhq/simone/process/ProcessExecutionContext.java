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

import java.util.Map;

/**
 * @author Thomas Segismont
 */
public class ProcessExecutionContext {

  private final int pid;
  private final String executablePath;
  private final String currentWorkingDirectory;
  private final Map<String, String> environment;
  private final String user;
  private final String group;

  public ProcessExecutionContext(int pid, String executablePath, String currentWorkingDirectory,
                                 Map<String, String> environment, String user, String group) {
    this.pid = pid;
    this.executablePath = executablePath;
    this.currentWorkingDirectory = currentWorkingDirectory;
    this.environment = environment;
    this.user = user;
    this.group = group;
  }

  public int getPid() {
    return pid;
  }

  public String getExecutablePath() {
    return executablePath;
  }

  public String getCurrentWorkingDirectory() {
    return currentWorkingDirectory;
  }

  public Map<String, String> getEnvironment() {
    return environment;
  }

  public String getUser() {
    return user;
  }

  public String getGroup() {
    return group;
  }

  @Override
  public String toString() {
    return "ProcessExecutionContext[" +
           "pid=" + pid +
           ", executablePath='" + executablePath + '\'' +
           ", currentWorkingDirectory='" + currentWorkingDirectory + '\'' +
           ", user='" + user + '\'' +
           ", group='" + group + '\'' +
           ", environment=" + environment +
           ']';
  }
}
