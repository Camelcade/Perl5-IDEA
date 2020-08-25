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

package com.perl5.lang.perl.profiler.parser;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.profiler.DummyCallTreeBuilder;
import com.intellij.profiler.LineByLineParser;
import com.intellij.profiler.api.BaseCallStackElement;
import com.intellij.profiler.model.NoThreadInfoInProfilerData;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PerlCollapsedDumpParser extends LineByLineParser {
  private static final Logger LOG = Logger.getInstance(PerlCollapsedDumpParser.class);
  private final DummyCallTreeBuilder<BaseCallStackElement> myCallTreeBuilder = new DummyCallTreeBuilder<>();
  private final Map<String, PerlCallStackElement> myCachedFrames = new ConcurrentHashMap<>();

  public @NotNull DummyCallTreeBuilder<BaseCallStackElement> getCallTreeBuilder() {
    return myCallTreeBuilder;
  }

  /**
   * @deprecated this fixes a reading bug, going to be fixed in platform in 2020.3
   */
  @ApiStatus.ScheduledForRemoval(inVersion = "2020.3")
  @Deprecated
  void readFromStreamFixed(InputStream input, ProgressIndicator indicator) {
    final var startTime = System.currentTimeMillis();
    int linesCounter = 0;
    try (
      var streamReader = new InputStreamReader(input);
      var bufferedReader = new BufferedReader(streamReader, 1024 * 1024 * 2)) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        if (++linesCounter % 10_000 == 0) {
          indicator.checkCanceled();
        }
        consumeLine(line);
      }
    }
    catch (IOException e) {
      LOG.warn("Problem while reading results: ", e);
    }
    LOG.info(
      "Dump read in " + (System.currentTimeMillis() - startTime) + " ms; lines read: " + linesCounter + "; bad lines: " + getBadLines());
  }

  @Override
  public void consumeLine(@NotNull String line) {
    var timeDelimiterIndex = line.lastIndexOf(" ");
    var framesString = timeDelimiterIndex < 0 ? line : line.substring(0, timeDelimiterIndex);
    var countString = timeDelimiterIndex < 0 ? null : line.substring(timeDelimiterIndex + 1);
    Long count;
    try {
      count = countString == null ? null : Long.parseLong(countString);
    }
    catch (NumberFormatException e) {
      count = null;
    }

    //pretty common situation, just ignore
    if (count == null || framesString.isEmpty()) {
      setBadLines(getBadLines() + 1);
      return;
    }
    var frames = StringUtil.split(framesString, ";");
    myCallTreeBuilder.addStack(
      NoThreadInfoInProfilerData.INSTANCE,
      ContainerUtil.map(frames, it -> myCachedFrames.computeIfAbsent(it, PerlCallStackElement::new)),
      count);
  }

  private static class PerlCallStackElement extends BaseCallStackElement {
    private final @NotNull String myName;

    public PerlCallStackElement(@NotNull String name) {
      myName = name;
    }

    @Override
    public @NotNull String fullName() {
      return myName;
    }
  }
}
