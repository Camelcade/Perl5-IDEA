/*
 * Copyright 2015-2021 Alexandr Evstigneev
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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.profiler.DummyCallTreeBuilder;
import com.intellij.profiler.LineByLineParser;
import com.intellij.profiler.api.BaseCallStackElement;
import com.intellij.profiler.model.NoThreadInfoInProfilerData;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.profiler.parser.frames.PerlCallStackElement;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PerlCollapsedDumpParser extends LineByLineParser {
  private static final Logger LOG = Logger.getInstance(PerlCollapsedDumpParser.class);
  private final DummyCallTreeBuilder<BaseCallStackElement> myCallTreeBuilder = new DummyCallTreeBuilder<>();
  private final Map<String, PerlCallStackElement> myCachedFrames = new ConcurrentHashMap<>();

  public @NotNull DummyCallTreeBuilder<BaseCallStackElement> getCallTreeBuilder() {
    return myCallTreeBuilder;
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
      ContainerUtil.map(frames, it -> myCachedFrames.computeIfAbsent(it, PerlCallStackElement::create)),
      count);
  }
}
