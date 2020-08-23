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
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.profiler.CollapsedDumpParser;
import com.intellij.profiler.DummyCallTreeBuilder;
import com.intellij.profiler.api.*;
import com.intellij.profiler.model.NoThreadInfoInProfilerData;
import com.intellij.profiler.ui.NativeCallStackElementRenderer;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collections;

import static com.perl5.lang.perl.profiler.PerlProfilerBundle.DEVEL_NYTPROF;

public class PerlProfilerDumpFileParser implements ProfilerDumpFileParser {
  private static final Logger LOG = Logger.getInstance(PerlProfilerDumpFileParser.class);
  private final @NotNull Project myProject;
  @NonNls private static final String NYTPROFCALLS = "nytprofcalls";

  public PerlProfilerDumpFileParser(Project project) {
    myProject = project;
  }

  @Override
  public @Nullable String getHelpId() {
    return null;
  }

  @Override
  public @NotNull ProfilerDumpFileParsingResult parse(@NotNull File file, @NotNull ProgressIndicator indicator) {
    var perlSdk = PerlProjectManager.getSdk(myProject);
    if (perlSdk == null) {
      return new Failure("Perl sdk is not set for the project " + myProject.getName());
    }

    var nytprofcalls = ReadAction.compute(() -> PerlRunUtil.findScript(myProject, NYTPROFCALLS));
    if (nytprofcalls == null) {
      LOG.warn("Unable to find `" + NYTPROFCALLS + "` script in " + myProject);
      PerlRunUtil.showMissingLibraryNotification(myProject, perlSdk, Collections.singletonList(DEVEL_NYTPROF));
      return new Failure("Unable to find `nytprofcalls` script. Make sure that " +
                         DEVEL_NYTPROF + " " +
                         "is installed in the " +
                         perlSdk.getName());
    }

    var perlCommandLine = PerlRunUtil.getPerlCommandLine(myProject, nytprofcalls);
    if (perlCommandLine == null) {
      LOG.warn("Unable to create command line for parsing results in " + myProject);
      return new Failure("Failed to create command line for parsing profiling results");
    }

    PerlHostData<?, ?> hostData = PerlHostData.notNullFrom(perlSdk);
    var resultsDirectory = file.getParent();
    try {
      hostData.fixPermissionsRecursively(resultsDirectory);
    }
    catch (ExecutionException e) {
      LOG.warn("Failed to fix permissions for " + resultsDirectory, e);
      return new Failure("Failed to set permissions for the " + resultsDirectory);
    }

    var localPath = file.getAbsolutePath();
    var remotePath = hostData.getRemotePath(localPath);
    if (remotePath == null) {
      var reason = "Unable to map local path to remote: " + localPath + " with perl " + perlSdk.getName();
      LOG.warn(reason);
      return new Failure(reason);
    }
    perlCommandLine.withParameters(remotePath);

    var callTreeBuilder = new DummyCallTreeBuilder<BaseCallStackElement>();
    var dumpParser = new CollapsedDumpParser<>(
      callTreeBuilder,
      it -> NoThreadInfoInProfilerData.INSTANCE,
      PerlCallStackElement::new,
      it -> false);

    try {
      BaseProcessHandler<?> processHandler = PerlHostData.createProcessHandler(perlCommandLine);
      var processInput = processHandler.getProcess().getInputStream();
      dumpParser.readFromStream(processInput, indicator);
      return  new Success(new NewCallTreeOnlyProfilerData(callTreeBuilder, NativeCallStackElementRenderer.Companion.getINSTANCE()));
    }
    catch (ExecutionException e) {
      LOG.warn("Error parsing results: " + e.getMessage());
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
