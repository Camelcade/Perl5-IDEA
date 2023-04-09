/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

import com.intellij.execution.ExecutionException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.perl5.lang.perl.idea.run.PerlRunProfileState;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.profiler.configuration.PerlProfilerConfigurationState;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PerlProfilerRunProfileState extends PerlRunProfileState {
  private final @NotNull PerlProfilerConfigurationState myProfilerConfigurationState;
  public static final String BASE_DUMP_NAME = "nytprof.out";

  public PerlProfilerRunProfileState(@NotNull ExecutionEnvironment environment,
                                     @NotNull PerlProfilerConfigurationState profilerConfigurationState) {
    super(environment);
    myProfilerConfigurationState = profilerConfigurationState;
  }

  public @NotNull PerlProfilerConfigurationState getProfilerConfigurationState() {
    return myProfilerConfigurationState;
  }

  public final @NotNull Path getProfilingResultsPath() {
    var project = getEnvironment().getProject();
    var perlSdk = PerlProjectManager.getSdk(project);
    return Paths.get(PathManager.getSystemPath(),
                     "profiling",
                     FileUtil.sanitizeFileName(
                       String.join("_", project.getName(),
                                   perlSdk == null ? "null" : perlSdk.getName(),
                                   StringUtil.notNullize(myProfilerConfigurationState.getDisplayName(), "unnamed"),
                                   getEnvironment().getRunProfile().getName()
                       )));
  }

  public @NotNull File getDumpFile() {
    var profileResultsPath = getProfilingResultsPath();
    profileResultsPath.toFile().mkdirs();
    return profileResultsPath.resolve(BASE_DUMP_NAME).toFile();
  }

  @Override
  public @NotNull List<String> getAdditionalPerlArguments(@NotNull GenericPerlRunConfiguration perlRunConfiguration)
    throws ExecutionException {
    return Collections.singletonList("-d:NYTProf");
  }

  @Override
  public Map<String, String> getAdditionalEnvironmentVariables() throws ExecutionException {
    Sdk effectiveSdk = ((GenericPerlRunConfiguration)getEnvironment().getRunProfile()).getEffectiveSdk();
    PerlHostData<?, ?> hostData = PerlHostData.notNullFrom(effectiveSdk);
    var dumpLocalPath = getDumpFile().getAbsolutePath();
    var remotePath = hostData.getRemotePath(dumpLocalPath);
    if (StringUtil.isEmpty(remotePath)) {
      throw new ExecutionException("Unable to compute remote path for: " + dumpLocalPath);
    }
    var nytProfOptions = "stmts=0:calls=2:savesrc=0:slowops=1:sigexit=1:addpid=1" +
                         ":file=" + StringUtil.escapeChar(FileUtil.toSystemIndependentName(remotePath), ':') +
                         ":start=" + myProfilerConfigurationState.getStartupMode().getProfilerCommand();
    if (myProfilerConfigurationState.isOptimizerDisabled()) {
      nytProfOptions += ":optimize=0";
    }

    return Map.of("NYTPROF", nytProfOptions);
  }
}
