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

package com.perl5.lang.perl.profiler.run;

import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.profiler.CallTreeBuilder;
import com.intellij.profiler.FileBasedProfilerProcess;
import com.intellij.profiler.api.*;
import com.perl5.lang.perl.profiler.configuration.PerlProfilerConfigurationState;
import com.perl5.lang.perl.profiler.parser.PerlProfilerDumpFileParser;
import com.perl5.lang.perl.profiler.parser.PerlProfilerDumpWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.io.File;
import java.util.Objects;

public class PerlProfilerProcess extends FileBasedProfilerProcess<PerlTargetProcess> {
  private static final Logger LOG = Logger.getInstance(PerlProfilerProcess.class);

  private final @NotNull ExecutionEnvironment myExecutionEnvironment;
  private final @NotNull PerlProfilerRunProfileState myPerlProfilerRunProfileState;
  private final long myStartTime = System.currentTimeMillis();

  public PerlProfilerProcess(@NotNull ExecutionEnvironment executionEnvironment,
                             @NotNull RunContentDescriptor runContentDescriptor,
                             @NotNull PerlProfilerRunProfileState profilerRunProfileState) {
    super(executionEnvironment.getProject(), new PerlTargetProcess(runContentDescriptor), profilerRunProfileState.getDumpFile());
    myPerlProfilerRunProfileState = profilerRunProfileState;
    myExecutionEnvironment = executionEnvironment;
    initTargetProcessLifecycleListener(Objects.requireNonNull(runContentDescriptor.getProcessHandler()));
  }

  @Override
  protected @NotNull ProfilerState readPreparedDump(@NotNull File file, @NotNull ProgressIndicator indicator)
    throws ProcessCanceledException {
    var dumpParser = new PerlProfilerDumpFileParser(myExecutionEnvironment.getProject());
    var dumpFile = myPerlProfilerRunProfileState.getDumpFile();
    var parsingResult = dumpParser.parse(dumpFile.getParentFile(), indicator);
    return asProfilerState(parsingResult, PerlProfilerDumpWriter.create(dumpFile, parsingResult));
  }

  @Override
  protected @NotNull Logger getLOG() {
    return LOG;
  }

  @Override
  public long getAttachedTimestamp() {
    return myStartTime;
  }

  @Override
  public @Nullable String getHelpId() {
    return null;
  }

  @Override
  public @NotNull PerlProfilerConfigurationState getProfilerConfiguration() {
    return myPerlProfilerRunProfileState.getProfilerConfigurationState();
  }

  @TestOnly
  public @NotNull RunProfile getRunProfile() {
    return myExecutionEnvironment.getRunProfile();
  }
}
