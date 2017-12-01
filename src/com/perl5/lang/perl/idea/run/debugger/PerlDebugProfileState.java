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
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.lang.perl.idea.run.PerlRunConfiguration;
import com.perl5.lang.perl.idea.run.PerlRunProfileState;
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

  public PerlDebugProfileState(ExecutionEnvironment environment) {
    super(environment);
  }

  @NotNull
  @Override
  protected String[] getPerlParameters(PerlRunConfiguration runProfile) {
    List<String> result = new ArrayList<>();
    result.add(DEBUG_ARGUMENT);

    for (String argument : super.getPerlParameters(runProfile)) {
      if (StringUtil.isNotEmpty(argument)) {
        result.add(argument);
      }
    }

    return result.toArray(new String[result.size()]);
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
    return localPath;
  }

  public String mapPathToLocal(String remotePath) {
    return remotePath;
  }


  public Integer getDebugPort() throws ExecutionException {
    if (myDebugPort == null) {
      myDebugPort = getDebugOptions().getDebugPort();
    }
    return myDebugPort;
  }
}
