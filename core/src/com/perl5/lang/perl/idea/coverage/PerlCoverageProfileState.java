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

package com.perl5.lang.perl.idea.coverage;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.coverage.CoverageEnabledConfiguration;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.projectRoots.Sdk;
import com.perl5.lang.perl.idea.run.PerlRunConfiguration;
import com.perl5.lang.perl.idea.run.PerlRunProfileState;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PerlCoverageProfileState extends PerlRunProfileState {
  private static final Logger LOG = Logger.getInstance(PerlCoverageProfileState.class);
  public PerlCoverageProfileState(ExecutionEnvironment environment) {
    super(environment);
  }

  @NotNull
  @Override
  protected List<String> getPerlParameters(PerlRunConfiguration runProfile) throws ExecutionException {
    List<String> perlParameters = new ArrayList<>(super.getPerlParameters(runProfile));
    String coverageBasePath =
      CoverageEnabledConfiguration.getOrCreate((PerlRunConfiguration)getEnvironment().getRunProfile()).getCoverageFilePath();

    Sdk effectiveSdk = runProfile.getEffectiveSdk();
    PerlHostData hostData = PerlHostData.notNullFrom(effectiveSdk);

    perlParameters.add(0, "-MDevel::Cover=-silent,1,-db," + hostData.getRemotePath(coverageBasePath) + ",-dir,.");

    return perlParameters;
  }
}
