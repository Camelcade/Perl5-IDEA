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

import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.BaseProcessHandler;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.profiler.CollapsedDumpParser;
import com.intellij.profiler.DummyCallTreeBuilder;
import com.intellij.profiler.api.*;
import com.intellij.profiler.model.NoThreadInfoInProfilerData;
import com.intellij.profiler.ui.NativeCallStackElementRenderer;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class PerlProfilerDumpFileParser implements ProfilerDumpFileParser {
  private final @NotNull Project myProject;

  public PerlProfilerDumpFileParser(Project project) {
    myProject = project;
  }

  @Override
  public @Nullable String getHelpId() {
    return null;
  }

  @Override
  public @NotNull ProfilerDumpFileParsingResult parse(@NotNull File file, @NotNull ProgressIndicator indicator) {
    var callTreeBuilder = new DummyCallTreeBuilder<BaseCallStackElement>();
    var dumpParser = new CollapsedDumpParser<>(
      callTreeBuilder,
      it -> NoThreadInfoInProfilerData.INSTANCE,
      PerlCallStackElement::new,
      it -> false);

    var nytprofcalls = ReadAction.compute(() -> PerlRunUtil.findScript(myProject, "nytprofcalls"));
    var absoluteFile = file.getAbsoluteFile();
    var perlCommandLine = PerlRunUtil.getPerlCommandLine(myProject, nytprofcalls)
      .withParameters(absoluteFile.getPath())
      .withWorkDirectory(absoluteFile.getParent());
    try {
      BaseProcessHandler<?> processHandler = PerlHostData.createProcessHandler(perlCommandLine);
      var processInput = processHandler.getProcess().getInputStream();
      dumpParser.readFromStream(processInput, indicator);
      return  new Success(new NewCallTreeOnlyProfilerData(callTreeBuilder, NativeCallStackElementRenderer.Companion.getINSTANCE()));
    }
    catch (ExecutionException e) {
      return new Failure(e.getMessage());
    }
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
