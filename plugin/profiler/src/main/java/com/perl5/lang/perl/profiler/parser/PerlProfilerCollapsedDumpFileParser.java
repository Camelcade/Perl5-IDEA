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
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.profiler.api.*;
import com.intellij.profiler.ui.NativeCallStackElementRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * Parser for backed up files with serialized collapsed dump
 */
public class PerlProfilerCollapsedDumpFileParser implements ProfilerDumpFileParser {
  private static final Logger LOG = Logger.getInstance(PerlProfilerCollapsedDumpFileParser.class);
  private final @NotNull Project myProject;

  public PerlProfilerCollapsedDumpFileParser(Project project) {
    myProject = project;
  }

  @Override
  public @Nullable String getHelpId() {
    return null;
  }

  @Override
  public @NotNull ProfilerDumpFileParsingResult parse(@NotNull File file, @NotNull ProgressIndicator indicator) {
    var dumpParser = new PerlCollapsedDumpParser();

    try (var fileInputStream = new FileInputStream(file);
         var gzipInputStream = new GZIPInputStream(fileInputStream, 1024 * 1024)
    ) {
      dumpParser.readFromStream(gzipInputStream, indicator);
    }
    catch (IOException e) {
      LOG.warn("Error reading collapsed dump: " + e.getMessage());
      return new Failure(e.getMessage());
    }
    return new Success(
      new NewCallTreeOnlyProfilerData(dumpParser.getCallTreeBuilder(), NativeCallStackElementRenderer.Companion.getINSTANCE()));
  }
}
