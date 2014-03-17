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

import static org.rhq.simone.process.ProcessDetails.State.STOPPED;
import static org.rhq.simone.process.ProcessDetails.State.UNKNOWN;
import static org.rhq.simone.process.ProcessDetails.State.ZOMBIE;

/**
 * @author Thomas Segismont
 */
public class ProcessDetails {

  public enum State {
    RUNNING, SLEEPING, UNINTERRUPTIBLE_WAIT, STOPPED, ZOMBIE, PAGING, UNKNOWN;
  }

  private final int pid;
  private final int parentPid;
  private final String executableName;
  private final State state;
  private final int threadCount;
  private final long startTime;

  public ProcessDetails(int pid, int parentPid, String executableName, State state, int threadCount, long startTime) {
    this.pid = pid;
    this.executableName = executableName;
    this.state = state;
    this.parentPid = parentPid;
    this.threadCount = threadCount;
    this.startTime = startTime;
  }

  public int getPid() {
    return pid;
  }

  public int getParentPid() {
    return parentPid;
  }

  public String getExecutableName() {
    return executableName;
  }

  public State getState() {
    return state;
  }

  public int getThreadCount() {
    return threadCount;
  }

  /**
   * @return the time the process started after system boot, in seconds
   */
  public long getStartTime() {
    return startTime;
  }

  public boolean isRunning() {
    return state != STOPPED && state != ZOMBIE && state != UNKNOWN;
  }

  @Override
  public String toString() {
    return "ProcessDetails[" +
           "pid=" + pid +
           ", parentPid=" + parentPid +
           ", executableName='" + executableName + '\'' +
           ", state=" + state +
           ", threadCount=" + threadCount +
           ", startTime(seconds)=" + startTime +
           ']';
  }
}
