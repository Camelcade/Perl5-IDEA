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

package org.jetbrains.plugins.terminal.fixture;

import com.intellij.openapi.diagnostic.Logger;
import com.jediterm.terminal.Terminal;
import com.jediterm.terminal.model.TerminalLine;
import com.jediterm.terminal.model.TerminalModelListener;
import com.jediterm.terminal.model.TerminalTextBuffer;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

/***
 * Copy-paste of org.jetbrains.plugins.terminal.classic.fixture.ClassicTerminalTestBufferWatcher
 * See: https://youtrack.jetbrains.com/issue/IJPL-107274/
 */
public class TestTerminalBufferWatcher {
  private static final Logger LOG = Logger.getInstance(TestTerminalBufferWatcher.class);
  private final TerminalTextBuffer myBuffer;
  private final Terminal myTerminal;

  public TestTerminalBufferWatcher(@NotNull TerminalTextBuffer buffer, @NotNull Terminal terminal) {
    myBuffer = buffer;
    myTerminal = terminal;
  }

  @SuppressWarnings("SameParameterValue")
  private @NotNull List<String> getScreenLines(boolean aboveCursorLine) {
    List<String> screenLines = new ArrayList<>();
    myBuffer.lock();
    try {
      int cursorLineInd = myTerminal.getCursorY() - 1;
      for (int row = 0; row < myBuffer.getHeight(); row++) {
        if (!aboveCursorLine || row < cursorLineInd) {
          TerminalLine line = myBuffer.getLine(row);
          screenLines.add(line.getText());
        }
      }
    }
    finally {
      myBuffer.unlock();
    }
    return screenLines;
  }

  public void awaitScreenLinesAre(@NotNull List<String> expectedScreenLines, long timeoutMillis) {
    boolean ok = awaitBuffer(() -> expectedScreenLines.equals(getScreenLines(true)), timeoutMillis);
    if (!ok) {
      Assert.assertEquals(expectedScreenLines, getScreenLines(true));
      Assert.fail("Unexpected failure");
    }
  }

  public void awaitScreenLinesEndWith(@NotNull List<String> expectedScreenLines, long timeoutMillis) {
    boolean ok = awaitBuffer(() -> checkScreenLinesEndWith(expectedScreenLines), timeoutMillis);
    if (!ok) {
      Assert.assertEquals(expectedScreenLines, getScreenLines(true));
      Assert.fail("Unexpected failure");
    }
  }

  public boolean awaitBuffer(@NotNull BooleanSupplier awaitCondition, long timeoutMillis) {
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
      return true;
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
    return ok.get();
  }

  public boolean checkScreenLinesEndWith(@NotNull List<String> expectedScreenLines) {
    List<String> actualLines = getScreenLines(true);
    if (actualLines.size() < expectedScreenLines.size()) {
      return false;
    }
    List<String> lastActualLines = actualLines.subList(actualLines.size() - expectedScreenLines.size(), actualLines.size());
    return expectedScreenLines.equals(lastActualLines);
  }

  public String getScreenLines() {
    return myBuffer.getScreenLines();
  }
}
