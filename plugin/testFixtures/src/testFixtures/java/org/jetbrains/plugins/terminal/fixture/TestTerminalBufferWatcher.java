// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.jetbrains.plugins.terminal.fixture;

import com.intellij.openapi.diagnostic.Logger;
import com.jediterm.terminal.model.TerminalModelListener;
import com.jediterm.terminal.model.TerminalTextBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

public class TestTerminalBufferWatcher {
  private static final Logger LOG = Logger.getInstance(TestTerminalBufferWatcher.class);
  private final TerminalTextBuffer myBuffer;
  TestTerminalBufferWatcher(@NotNull TerminalTextBuffer buffer) {
    myBuffer = buffer;
  }

  public void awaitBuffer(@NotNull BooleanSupplier awaitCondition, long timeoutMillis) {
    CountDownLatch latch = new CountDownLatch(1);
    AtomicBoolean ok = new AtomicBoolean(false);
    TerminalModelListener listener = () -> {
      if (awaitCondition.getAsBoolean()) {
        ok.set(true);
        latch.countDown();
      }
    };
    myBuffer.addModelListener(listener);
    if (awaitCondition.getAsBoolean()) {
      myBuffer.removeModelListener(listener);
      return;
    }
    try {
      latch.await(timeoutMillis, TimeUnit.MILLISECONDS);
    }
    catch (InterruptedException e) {
      LOG.debug("Could not get response in " + timeoutMillis + "ms. Terminal screen lines are: " + getScreenLines());
      throw new AssertionError(e);
    }
    finally {
      myBuffer.removeModelListener(listener);
    }
    ok.get();
  }

  public String getScreenLines() {
    return myBuffer.getScreenLines();
  }
}
