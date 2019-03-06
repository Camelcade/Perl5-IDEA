/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.run.prove;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.util.PerlPluginUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.intellij.execution.configurations.GeneralCommandLine.ParentEnvironmentType.CONSOLE;
import static com.intellij.execution.configurations.GeneralCommandLine.ParentEnvironmentType.NONE;
import static com.perl5.lang.perl.util.PerlRunUtil.PERL_I;

class PerlTestRunConfiguration extends GenericPerlRunConfiguration {
  private static final Logger LOG = Logger.getInstance(PerlTestRunConfiguration.class);
  private static final String PROVE = "prove";
  private static final String TEST_HARNESS = "Test::Harness";
  private static final String PROVE_PASS_PREFIX = "PROVE_PASS_";
  private static final String PROVE_PASS_PLUGIN_PARAMETER = "-PPassEnv";

  public PerlTestRunConfiguration(Project project,
                                  @NotNull ConfigurationFactory factory,
                                  String name) {
    super(project, factory, name);
  }

  @NotNull
  @Override
  public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
    return new PerlTestRunConfigurationEditor(getProject());
  }

  @Nullable
  @Override
  protected PerlCommandLine createBaseCommandLine(@NotNull Project project,
                                                  @NotNull Sdk perlSdk,
                                                  @NotNull VirtualFile scriptFile,
                                                  @NotNull List<String> additionalPerlParameters,
                                                  @NotNull Map<String, String> additionalEnvironmentVariables) throws ExecutionException {

    VirtualFile proveScript = PerlRunUtil.findLibraryScriptWithNotification(perlSdk, getProject(), PROVE, TEST_HARNESS);
    if (proveScript == null) {
      throw new ExecutionException(PerlBundle.message("perl.run.error.prove.missing", perlSdk.getName()));
    }

    String interpreterPath = PerlProjectManager.getInterpreterPath(perlSdk);
    if (StringUtil.isEmpty(interpreterPath)) {
      LOG.warn("Empty interpreter path in " + perlSdk);
      return null;
    }
    PerlHostData<?, ?> perlHostData = PerlHostData.notNullFrom(perlSdk);
    PerlCommandLine commandLine = new PerlCommandLine(interpreterPath)
      .withParameters(PerlRunUtil.PERL_I + perlHostData.getRemotePath(PerlPluginUtil.getHelpersLibPath()))
      .withParameters(perlHostData.getRemotePath(proveScript.getPath()))
      .withParameters(PROVE_PASS_PLUGIN_PARAMETER)
      .withParameters(getScriptParameters())
      .withParameters(perlHostData.getRemotePath(scriptFile.getPath()))
      .withProject(project)
      .withSdk(perlSdk);

    ArrayList<String> testPerlParameters = new ArrayList<>(getPerlParametersList());
    testPerlParameters.addAll(additionalPerlParameters);
    for (VirtualFile libRoot : PerlProjectManager.getInstance(project).getModulesLibraryRoots()) {
      testPerlParameters.add(PERL_I + perlHostData.getRemotePath(libRoot.getCanonicalPath()));
    }

    // environment
    Map<String, String> environment = new HashMap<>(getEnvs());
    environment.putAll(additionalEnvironmentVariables);
    if (!testPerlParameters.isEmpty()) {
      String currentOpt = environment.get(PerlRunUtil.PERL5OPT);
      if (StringUtil.isNotEmpty(currentOpt)) {
        testPerlParameters.addAll(0, StringUtil.split(currentOpt, " "));
      }
      environment.put(PerlRunUtil.PERL5OPT, StringUtil.join(testPerlParameters, " "));
    }
    environment.forEach((key, val) -> commandLine.withEnvironment(PROVE_PASS_PREFIX + key, val));

    commandLine.withParentEnvironmentType(isPassParentEnvs() ? CONSOLE : NONE);
    return commandLine;
  }
}
