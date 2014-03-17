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

import java.util.StringTokenizer;

/**
 * @author Thomas Segismont
 */
public class TokenizerUtil {

  private TokenizerUtil() {
    // Util
  }

  public static boolean skip1Token(StringTokenizer tokenizer) {
    if (tokenizer == null) {
      return false;
    }
    if (tokenizer.hasMoreTokens()) {
      tokenizer.nextToken();
      return true;
    }
    return false;
  }

  public static boolean skipNTokens(StringTokenizer tokenizer, int n) {
    if (tokenizer == null || n < 1) {
      return false;
    }
    int skipped = 0;
    for (; skipped < n && tokenizer.hasMoreTokens(); skipped++) {
      tokenizer.nextToken();
    }
    return skipped == n;
  }
}
