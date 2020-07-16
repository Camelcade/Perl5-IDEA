/*
 * Copyright 2015-2020 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.perl5.lang.perl.util;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

public class PerlTimeLogger {
  private final Logger myLogger;
  long myLastTime = System.currentTimeMillis();

  public PerlTimeLogger(@NotNull Class<?> clazz) {
    myLogger = Logger.getInstance(clazz);
  }

  public void debug(@NotNull String text) {
    long newLastTime = System.currentTimeMillis();
    myLogger.debug("+", (newLastTime - myLastTime), "ms. ", text);
    myLastTime = newLastTime;
  }
}
