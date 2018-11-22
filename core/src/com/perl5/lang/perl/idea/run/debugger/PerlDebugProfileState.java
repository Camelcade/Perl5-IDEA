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
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.projectRoots.Sdk;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.execution.PortMapping;
import com.perl5.lang.perl.idea.run.PerlRunConfiguration;
import com.perl5.lang.perl.idea.run.PerlRunProfileState;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 04.05.2016.
 */
public class PerlDebugProfileState extends PerlRunProfileState {
  private static final String DEBUG_ARGUMENT = "-d:Camelcadedb";
  private Integer myDebugPort;
  @NotNull
  private final PerlHostData myHostData;

  public PerlDebugProfileState(ExecutionEnvironment environment) {
    super(environment);
    RunProfile runProfile = environment.getRunProfile();
    if (runProfile instanceof PerlRunConfiguration) {
      try {
        Sdk effectiveSdk = ((PerlRunConfiguration)runProfile).getEffectiveSdk();
        myHostData = PerlHostData.notNullFrom(effectiveSdk);
      }
      catch (ExecutionException e) {
        throw new RuntimeException(e);
      }
    }
    else {
      myHostData = PerlHostHandler.getDefaultHandler().createData();
    }
  }

  @NotNull
  @Override
  protected List<String> getPerlParameters(PerlRunConfiguration runProfile) throws ExecutionException {
    List<String> result = new ArrayList<>(super.getPerlParameters(runProfile));
    result.add(0, DEBUG_ARGUMENT);
    return result;
  }

  @NotNull
  @Override
  protected PerlCommandLine customizeCommandLine(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return super.customizeCommandLine(commandLine).withPortMappings(PortMapping.create(getDebugPort()));
  }

  @Override
  protected Map<String, String> calcEnv(PerlRunConfiguration runProfile) throws ExecutionException {
    Map<String, String> stringStringMap = new HashMap<>(super.calcEnv(runProfile));
    PerlDebugOptions debugOptions = getDebugOptions();
    stringStringMap.put("PERL5_DEBUG_ROLE", debugOptions.getPerlRole());
    stringStringMap.put("PERL5_DEBUG_HOST", debugOptions.getDebugHost());
    stringStringMap.put("PERL5_DEBUG_PORT", String.valueOf(getDebugPort()));
    return stringStringMap;
  }

  public PerlDebugOptions getDebugOptions() {
    return (PerlDebugOptions)getEnvironment().getRunProfile();
  }

  public String mapPathToRemote(String localPath) {
    String remotePath = myHostData.getRemotePath(localPath);
    return remotePath == null ? localPath : remotePath;
  }

  @NotNull
  public String mapPathToLocal(String remotePath) {
    String localPath = myHostData.getLocalPath(remotePath);
    return localPath == null ? remotePath : localPath;
  }

  public Integer getDebugPort() throws ExecutionException {
    if (myDebugPort == null) {
      myDebugPort = getDebugOptions().getDebugPort();
    }
    return myDebugPort;
  }
}
