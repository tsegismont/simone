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

package org.rhq.simone.util.log;

import static org.rhq.simone.util.ThrowableUtil.throwableToString;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Thomas Segismont
 */
public class ConsoleLog implements Log {

  private static final Level LEVEL;

  static {
    String requiredLevel = System.getProperty("simone.console.log.level");
    Level levelToSet = Level.WARN;
    for (Level level : Level.values()) {
      if (level.toString().equalsIgnoreCase(requiredLevel)) {
        levelToSet = level;
        break;
      }
    }
    LEVEL = levelToSet;
  }

  private enum Level {
    TRACE, DEBUG, INFO, WARN, ERROR;
  }

  private final String className;

  public ConsoleLog(Class clazz) {
    this.className = clazz.getName();
  }

  @Override
  public boolean isTraceEnabled() {
    return LEVEL == Level.TRACE;
  }

  @Override
  public void trace(String message) {
    println(System.out, Level.TRACE, message);
  }

  @Override
  public void trace(String message, Throwable t) {
    println(System.out, Level.TRACE, message, t);
  }

  @Override
  public boolean isDebugEnabled() {
    return LEVEL == Level.TRACE || LEVEL == Level.DEBUG;
  }

  @Override
  public void debug(String message) {
    println(System.out, Level.DEBUG, message);
  }

  @Override
  public void debug(String message, Throwable t) {
    println(System.out, Level.DEBUG, message, t);
  }

  @Override
  public boolean isInfoEnabled() {
    return LEVEL != Level.WARN && LEVEL != Level.ERROR;
  }

  @Override
  public void info(String message) {
    println(System.out, Level.INFO, message);
  }

  @Override
  public void info(String message, Throwable t) {
    println(System.out, Level.INFO, message, t);
  }

  @Override
  public boolean isWarnEnabled() {
    return LEVEL != Level.ERROR;
  }

  @Override
  public void warn(String message) {
    println(System.out, Level.WARN, message);
  }

  @Override
  public void warn(String message, Throwable t) {
    println(System.out, Level.WARN, message, t);
  }

  @Override
  public void error(String message) {
    println(System.err, Level.ERROR, message);
  }

  @Override
  public void error(String message, Throwable t) {
    println(System.err, Level.ERROR, message, t);
  }

  private void println(PrintStream stream, Level level, String message) {
    println(stream, level, message, null);
  }

  private void println(PrintStream stream, Level level, String message, Throwable t) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS zzz");
    StringBuilder
        sb =
        new StringBuilder(sdf.format(new Date())).append(" [").append(level).append("] [").append(className)
            .append("] ").append(message);
    if (t != null) {
      sb.append(System.getProperty("line.separator"));
      sb.append(throwableToString(t));
    }
    stream.println(sb.toString());
  }
}
