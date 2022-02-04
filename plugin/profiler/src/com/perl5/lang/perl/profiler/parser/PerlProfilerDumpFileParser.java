/*
 * Copyright 2015-2022 Alexandr Evstigneev
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
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessOutputType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Ref;
import com.intellij.profiler.api.*;
import com.intellij.profiler.ui.NativeCallStackElementRenderer;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.util.PerlPluginUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Parser for nytprof dump
 */
public class PerlProfilerDumpFileParser implements ProfilerDumpFileParser {
  private static final Logger LOG = Logger.getInstance(PerlProfilerDumpFileParser.class);
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
    var perlSdk = PerlProjectManager.getSdk(myProject);
    if (perlSdk == null) {
      return new Failure("Perl sdk is not set for the project " + myProject.getName());
    }

    var nytprofUtilPath = PerlPluginUtil.getHelperPath("nytprofcalls.pl");
    var perlCommandLine = PerlRunUtil.getPerlCommandLine(myProject, nytprofUtilPath);
    if (perlCommandLine == null) {
      LOG.warn("Unable to create command line for parsing results in " + myProject);
      return new Failure("Failed to create command line for parsing profiling results");
    }

    PerlHostData<?, ?> hostData = PerlHostData.notNullFrom(perlSdk);
    try {
      hostData.fixPermissionsRecursively(file.getAbsolutePath(), myProject);
    }
    catch (ExecutionException e) {
      LOG.warn("Failed to fix permissions for " + file, e);
      return new Failure("Failed to set permissions for the " + file);
    }

    var localPath = file.getAbsolutePath();
    var remotePath = hostData.getRemotePath(localPath);
    if (remotePath == null) {
      var reason = "Unable to map local path to remote: " + localPath + " with perl " + perlSdk.getName();
      LOG.warn(reason);
      return new Failure(reason);
    }

    perlCommandLine.withParameters(remotePath);

    var dumpParser = new PerlCollapsedDumpParser();

    try {
      BaseProcessHandler<?> processHandler = PerlHostData.createProcessHandler(perlCommandLine);
      Ref<String> errorRef = Ref.create();
      processHandler.addProcessListener(new ProcessAdapter() {
        @Override
        public void processTerminated(@NotNull ProcessEvent event) {
          if (event.getExitCode() != 0) {
            var message = "Error parsing NYTProf output. Exit code: " + event.getExitCode() + ". Message: " + event.getText();
            errorRef.set(message);
            LOG.warn(message);
          }
        }

        @Override
        public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
          if (ProcessOutputType.isStderr(outputType)) {
            LOG.warn("Error output: " + event.getText());
          }
          else if (ProcessOutputType.isStdout(outputType)) {
            dumpParser.consumeText(event.getText(), indicator);
          }
        }
      });
      processHandler.startNotify();
      while (!processHandler.waitFor(10)) {
        indicator.checkCanceled();
      }
      if (errorRef.isNull()) {
        return new Success(
          new NewCallTreeOnlyProfilerData(dumpParser.getCallTreeBuilder(), NativeCallStackElementRenderer.Companion.getINSTANCE()));
      }
      return new Failure(errorRef.get());
    }
    catch (ExecutionException e) {
      LOG.warn("Error parsing results: " + e.getMessage());
      return new Failure(e.getMessage());
    }
  }
}
