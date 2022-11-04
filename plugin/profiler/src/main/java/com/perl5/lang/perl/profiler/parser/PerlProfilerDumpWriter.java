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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Iterables;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.profiler.CallTreeBuilder;
import com.intellij.profiler.Stack;
import com.intellij.profiler.api.*;
import com.intellij.util.containers.ContainerUtil;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPOutputStream;

@SuppressWarnings("UnstableApiUsage")
public class PerlProfilerDumpWriter implements ProfilerDumpWriter {
  private static final Logger LOG = Logger.getInstance(PerlProfilerDumpWriter.class);
  private final @NotNull File myOriginalFile;
  private final @NotNull CallTreeBuilder<BaseCallStackElement> myBuilder;

  @VisibleForTesting
  public PerlProfilerDumpWriter(@NotNull File originalFile,
                                @NotNull CallTreeBuilder<BaseCallStackElement> builder) {
    myOriginalFile = originalFile;
    myBuilder = builder;
  }

  @Override
  public @NotNull String getDumpFileName() {
    return myOriginalFile.getName() + ".gz";
  }

  @Override
  public void writeDump(@NotNull File file, @NotNull ProgressIndicator indicator) {
    try (var fileOutputStream = new FileOutputStream(file);
         var gzipOutputStream = new GZIPOutputStream(fileOutputStream, 1024 * 1024);
         var writer = new OutputStreamWriter(gzipOutputStream, Charsets.UTF_8)) {
      var allStacks = myBuilder.getAllStacks();
      var stacksNumber = (double)Iterables.size(allStacks);
      double stackCounter = 0;
      for (Stack<BaseCallStackElement> stack : allStacks) {
        indicator.checkCanceled();
        indicator.setFraction(stackCounter++ / stacksNumber);
        writer.append(String.join(";", ContainerUtil.map(stack.getFrames(), BaseCallStackElement::toString)));
        writer.append(" ");
        writer.append(Long.toString(stack.getValue()));
        writer.append("\n");
      }
    }
    catch (IOException e) {
      LOG.warn(e);
    }
  }

  public static @Nullable ProfilerDumpWriter create(@NotNull File originalFile,
                                                    @NotNull ProfilerDumpFileParsingResult parsingResult) {
    if (!(parsingResult instanceof Success)) {
      return null;
    }

    var profilerData = ((Success)parsingResult).getData();
    if (!(profilerData instanceof NewCallTreeOnlyProfilerData)) {
      return null;
    }

    var treeBuilder = ((NewCallTreeOnlyProfilerData)profilerData).getBuilder();
    return new PerlProfilerDumpWriter(originalFile, treeBuilder);
  }
}
