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

package com.perl5.lang.perl.idea.run.prove;

import com.google.common.annotations.VisibleForTesting;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@VisibleForTesting
public class PerlTestRunConfiguration extends PerlAbstractTestRunConfiguration {
  private static final Logger LOG = Logger.getInstance(PerlTestRunConfiguration.class);
  private static final String PROVE = "prove";
  private static final String TEST_HARNESS = "Test::Harness";
  private static final String PROVE_FORMAT_ARGUMENT = "--formatter";
  private static final String PROVE_FRAMEWORK_NAME = "Prove";
  private static final Pattern MISSING_FILTER_PATTERN = Pattern.compile("Can't load module (\\S+) at .+?/prove line");
  private static final String PROVE_PLUGIN_NAMESPACE = "App::Prove::Plugin";
  private static final List<String> PROVE_DEFAULT_ARGUMENTS = Arrays.asList("--merge", "--recurse");
  private static final String PROVE_JOBS_SHORT_PREFIX = "-j";
  private static final String PROVE_JOBS_PARAMETER = "--jobs";

  public PerlTestRunConfiguration(@NotNull Project project,
                                  @NotNull ConfigurationFactory factory,
                                  @Nullable String name) {
    super(project, factory, name);
  }

  protected @NotNull List<String> getDefaultTestRunnerArguments() {
    return PROVE_DEFAULT_ARGUMENTS;
  }

  protected @NotNull String getTestRunnerLocalPath() throws ExecutionException {
    Sdk perlSdk = getEffectiveSdk();
    VirtualFile proveScript = PerlRunUtil.findLibraryScriptWithNotification(perlSdk, getProject(), PROVE, TEST_HARNESS);
    if (proveScript == null) {
      throw new ExecutionException(PerlBundle.message("perl.run.error.prove.missing", perlSdk.getName()));
    }
    return proveScript.getPath();
  }

  @Override
  protected @NotNull List<String> getTestsArguments() {
    List<String> testScriptParametersList = getTestScriptParametersList();
    if (testScriptParametersList.isEmpty()) {
      return Collections.emptyList();
    }
    var result = new ArrayList<String>();
    result.add("::");
    result.addAll(testScriptParametersList);
    return result;
  }

  @Override
  protected @NotNull List<String> getScriptArguments() {
    List<String> userArguments = super.getScriptArguments();
    for (Iterator<String> iterator = userArguments.iterator(); iterator.hasNext(); ) {
      String userParameter = iterator.next();
      if (StringUtil.startsWith(userParameter, PROVE_JOBS_SHORT_PREFIX)) {
        iterator.remove();
      }
      else if (PROVE_FORMAT_ARGUMENT.equals(userParameter) || PROVE_JOBS_PARAMETER.equals(userParameter)) {
        iterator.remove();
        if (iterator.hasNext()) {
          iterator.next();
          iterator.remove();
        }
      }
    }
    return userArguments;
  }

  protected @NotNull String getFrameworkName() {
    return PROVE_FRAMEWORK_NAME;
  }

  @Override
  protected @NotNull ProcessHandler doPatchProcessHandler(@NotNull ProcessHandler processHandler) {
    try {
      Sdk effectiveSdk = getEffectiveSdk();
      processHandler.addProcessListener(new ProcessAdapter() {
        @Override
        public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
          String inputText = event.getText();
          Matcher matcher = MISSING_FILTER_PATTERN.matcher(inputText);
          if (matcher.find()) {
            String libraryName = PROVE_PLUGIN_NAMESPACE + PerlPackageUtil.NAMESPACE_SEPARATOR + matcher.group(1);
            PerlRunUtil.showMissingLibraryNotification(getProject(), effectiveSdk, Collections.singletonList(libraryName));
          }
        }
      });
    }
    catch (ExecutionException e) {
      LOG.warn("Missing effective sdk for test configuration: " + getName());
    }
    return processHandler;
  }

  @Override
  protected void checkConfigurationScriptPath() throws RuntimeConfigurationException {
    if (StringUtil.isEmptyOrSpaces(getScriptPath())) {
      throw new RuntimeConfigurationException(PerlBundle.message("perl.run.error.no.test.set"));
    }
    if (computeTargetFiles().isEmpty()) {
      throw new RuntimeConfigurationException(PerlBundle.message("perl.run.error.no.tests.found"));
    }
  }

  @Override
  public void checkConfiguration() throws RuntimeConfigurationException {
    super.checkConfiguration();
    if (PerlRunUtil.findScript(getProject(), PROVE) == null) {
      throw new RuntimeConfigurationException(PerlBundle.message("perl.run.error.no.prove.found"));
    }
  }

  @Override
  protected @NotNull PerlTestRunConfigurationProducer getRunConfigurationProducer() {
    return PerlTestRunConfigurationProducer.getInstance();
  }
}
