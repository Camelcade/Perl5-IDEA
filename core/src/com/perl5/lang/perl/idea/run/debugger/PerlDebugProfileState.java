/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.run.debugger;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.execution.PortMapping;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * For execution and debugging
 */
public class PerlDebugProfileState extends PerlDebugProfileStateBase {
  public static final String DEBUG_PACKAGE = "Devel::Camelcadedb";
  public static final String DEBUG_ARGUMENT = "-d:Camelcadedb";
  public static final String PERL5_DEBUG_HOST = "PERL5_DEBUG_HOST";
  public static final String PERL5_DEBUG_PORT = "PERL5_DEBUG_PORT";
  public static final String PERL5_DEBUG_ROLE = "PERL5_DEBUG_ROLE";
  @Nullable
  private PerlHostData myHostData;

  public PerlDebugProfileState(ExecutionEnvironment environment) {
    super(environment);
  }

  @NotNull
  private PerlHostData getHostData() {
    return Objects.requireNonNull(myHostData);
  }

  @NotNull
  @Override
  protected ProcessHandler startProcess() throws ExecutionException {
    ProcessHandler process = super.startProcess();
    RunProfile runProfile = getEnvironment().getRunProfile();
    if (runProfile instanceof GenericPerlRunConfiguration) {
      myHostData = PerlHostData.notNullFrom(((GenericPerlRunConfiguration)runProfile).getEffectiveSdk());
    }
    else {
      myHostData = PerlHostHandler.getDefaultHandler().createData();
    }
    return process;
  }

  @NotNull
  @Override
  public List<String> getAdditionalPerlParameters(@NotNull GenericPerlRunConfiguration perlRunConfiguration) {
    return Collections.singletonList(DEBUG_ARGUMENT);
  }

  @NotNull
  @Override
  protected PerlCommandLine createCommandLine() throws ExecutionException {
    return super.createCommandLine().withPortMappings(PortMapping.create(getDebugPort()));
  }

  @Override
  public Map<String, String> getAdditionalEnvironmentVariables() throws ExecutionException {
    Map<String, String> stringStringMap = new HashMap<>();
    PerlDebugOptions debugOptions = getDebugOptions();
    stringStringMap.put(PERL5_DEBUG_ROLE, debugOptions.getPerlRole());
    stringStringMap.put(PERL5_DEBUG_HOST, debugOptions.getDebugHost());
    stringStringMap.put(PERL5_DEBUG_PORT, String.valueOf(getDebugPort()));
    return stringStringMap;
  }

  public String mapPathToRemote(@NotNull String localPath) {
    String remotePath = getHostData().getRemotePath(localPath);
    return remotePath == null ? localPath : remotePath;
  }

  @NotNull
  public String mapPathToLocal(@NotNull String remotePath) {
    String localPath = getHostData().getLocalPath(remotePath);
    return localPath == null ? remotePath : localPath;
  }
}
