/*
 * Copyright 2015-2025 Alexandr Evstigneev
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
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PerlTimeLogger {
  private static final PerlTimeLogger EMPTY_LOGGER = new PerlTimeLogger("") {
    @Override
    public void debug(@NotNull Object... data) {
    }

    @Override
    public @NotNull Counter getCounter(@NotNull String name) {
      return Counter.EMPTY_COUNTER;
    }
  };

  private final Logger myLogger;
  long myLastTime = System.currentTimeMillis();
  private Map<String, Counter> myCounters;

  public PerlTimeLogger(@NotNull Class<?> clazz) {
    myLogger = Logger.getInstance(clazz);
  }

  public PerlTimeLogger(@NotNull String id) {
    myLogger = Logger.getInstance(id);
  }

  public PerlTimeLogger(@NotNull Logger logger) {
    myLogger = logger;
  }

  public void debug(@NotNull Object... data) {
    if (myLogger.isDebugEnabled()) {
      long newLastTime = System.currentTimeMillis();
      //noinspection LoggingSimilarMessage
      myLogger.debug((newLastTime - myLastTime) + " ms. ", data);
      myLastTime = newLastTime;
    }
  }

  public void trace(@NotNull Object... data) {
    if (myLogger.isTraceEnabled()) {
      long newLastTime = System.currentTimeMillis();
      //noinspection LoggingSimilarMessage
      myLogger.debug((newLastTime - myLastTime) + " ms. ", data);
      myLastTime = newLastTime;
    }
  }

  public @NotNull Counter getCounter(@NotNull String name) {
    if (myCounters == null) {
      myCounters = new HashMap<>();
    }
    return myCounters.computeIfAbsent(name, it -> new Counter());
  }

  public static class Counter {
    private static final Counter EMPTY_COUNTER = new Counter() {
      @Override
      public void inc() {
      }

      @Override
      public int get() {
        return 0;
      }
    };

    private int myCounter = 0;

    public void inc() {
      myCounter++;
    }

    public int get() {
      return myCounter;
    }
  }

  public static @NotNull String kb(int bytes) {
    return String.format("%.2f", (float)bytes / 1024);
  }

  public static @NotNull PerlTimeLogger create(@Nullable Logger logger) {
    return logger != null && logger.isDebugEnabled() ? new PerlTimeLogger(logger) : EMPTY_LOGGER;
  }
}
