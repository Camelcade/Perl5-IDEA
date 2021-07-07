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

package com.perl5.lang.perl.debugger.run.run.debugger;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.execution.PortMapping;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugOptions;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * For execution and debugging
 */
public class PerlDebugProfileState extends PerlDebugProfileStateBase {
  private static final Logger LOG = Logger.getInstance(PerlDebugProfileState.class);
  private static final String PROCESS_START_MARKER_TEXT = "Listening for the IDE connection at";
  public static final String DEBUG_PACKAGE = "Devel::Camelcadedb";
  public static final String DEBUG_ARGUMENT = "-d:Camelcadedb";
  public static final String PERL5_DEBUG_HOST = "PERL5_DEBUG_HOST";
  public static final String PERL5_DEBUG_PORT = "PERL5_DEBUG_PORT";
  public static final String PERL5_DEBUG_ROLE = "PERL5_DEBUG_ROLE";
  private static final ProcessAdapter READY_PROCESS_MARKER = new ProcessAdapter() {
    @Override
    public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
      String eventText = event.getText();
      if (LOG.isDebugEnabled()) {
        LOG.debug(outputType + ": " + eventText);
      }
      if (StringUtil.startsWith(eventText, PROCESS_START_MARKER_TEXT) ||
          StringUtil.startsWith(eventText, "##teamcity") && StringUtil.contains(eventText, PROCESS_START_MARKER_TEXT)) {
        LOG.debug("Marking as ready and removing listener");
        ProcessHandler processHandler = event.getProcessHandler();
        markAsReady(processHandler);
        processHandler.removeProcessListener(READY_PROCESS_MARKER);
      }
    }
  };

  private @Nullable PerlHostData<?, ?> myHostData;

  public PerlDebugProfileState(ExecutionEnvironment environment) {
    super(environment);
  }

  private @NotNull PerlHostData<?, ?> getHostData() {
    return Objects.requireNonNull(myHostData);
  }

  @Override
  protected @NotNull ProcessHandler startProcess() throws ExecutionException {
    ProcessHandler processHandler = super.startProcess();
    RunProfile runProfile = getEnvironment().getRunProfile();
    if (runProfile instanceof GenericPerlRunConfiguration) {
      myHostData = PerlHostData.notNullFrom(((GenericPerlRunConfiguration)runProfile).getEffectiveSdk());
    }
    else {
      myHostData = PerlHostHandler.getDefaultHandler().createData();
    }
    processHandler.addProcessListener(READY_PROCESS_MARKER);
    return processHandler;
  }

  @Override
  public @NotNull List<String> getAdditionalPerlParameters(@NotNull GenericPerlRunConfiguration perlRunConfiguration) {
    return Collections.singletonList(DEBUG_ARGUMENT);
  }

  @Override
  protected @NotNull PerlCommandLine createCommandLine() throws ExecutionException {
    return super.createCommandLine().withPortMappings(PortMapping.create(getDebugPort()));
  }

  @Override
  public Map<String, String> getAdditionalEnvironmentVariables() throws ExecutionException {
    Map<String, String> stringStringMap = new HashMap<>();
    PerlDebugOptions debugOptions = getDebugOptions();
    stringStringMap.put(PERL5_DEBUG_ROLE, debugOptions.getPerlRole());
    stringStringMap.put(PERL5_DEBUG_HOST, debugOptions.getHostToBind());
    stringStringMap.put(PERL5_DEBUG_PORT, String.valueOf(getDebugPort()));
    return stringStringMap;
  }

  @Override
  public String mapPathToRemote(@NotNull String localPath) {
    String remotePath = getHostData().getRemotePath(localPath);
    return remotePath == null ? localPath : remotePath;
  }

  @Override
  public @NotNull String mapPathToLocal(@NotNull String remotePath) {
    String localPath = getHostData().getLocalPath(remotePath);
    return localPath == null ? remotePath : localPath;
  }
}
