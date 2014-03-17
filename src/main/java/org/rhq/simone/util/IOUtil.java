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

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Thomas Segismont
 */
public class IOUtil {

  private static final AcceptAllLinesFilter ACCEPT_ALL_LINES_FILTER = new AcceptAllLinesFilter();

  public static final String EMPTY_STRING = "";

  public interface LineFilter {

    boolean accept(String line);
  }

  private IOUtil() {
    // Utility
  }

  public static String readFirstLine(File file) {
    if (file == null || !file.canRead()) {
      return EMPTY_STRING;
    }
    FileReader fileReader = null;
    BufferedReader bufferedReader = null;
    try {
      fileReader = new FileReader(file);
      bufferedReader = new BufferedReader(fileReader);
      return bufferedReader.readLine();
    } catch (Exception e) {
      return EMPTY_STRING;
    } finally {
      closeQuietly(bufferedReader);
      closeQuietly(fileReader);
    }
  }

  public static List<String> readLines(File file) {
    return readLines(file, ACCEPT_ALL_LINES_FILTER);
  }

  public static List<String> readLines(File file, LineFilter lineFilter) {
    if (file == null || !file.canRead()) {
      return new ArrayList<String>();
    }
    FileReader fileReader = null;
    BufferedReader bufferedReader = null;
    try {
      List<String> result = new ArrayList<String>();
      fileReader = new FileReader(file);
      bufferedReader = new BufferedReader(fileReader);
      for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
        if (lineFilter.accept(line)) {
          result.add(line);
        }
      }
      return result;
    } catch (Exception e) {
      return new ArrayList<String>();
    } finally {
      closeQuietly(bufferedReader);
      closeQuietly(fileReader);
    }
  }

  public static void closeQuietly(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException ignore) {
      }
    }
  }

  private static class AcceptAllLinesFilter implements LineFilter {

    @Override
    public boolean accept(String line) {
      return true;
    }
  }
}
