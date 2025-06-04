/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package unit;

import base.PerlInstrumentationTestCase;
import categories.CategoriesFilter;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.carton.PerlCartonDirectoryConfigurationProvider;
import com.perl5.lang.perl.coverage.PerlCoverageEnabledConfiguration;
import com.perl5.lang.perl.cpan.action.PerlInstallPackagesWithCpanAction;
import com.perl5.lang.perl.cpanminus.action.PerlInstallCpanmAction;
import com.perl5.lang.perl.debugger.run.run.debugger.PerlDebuggerProgramRunner;
import com.perl5.lang.perl.idea.configuration.module.idea.PerlIdeaTestUtil;
import com.perl5.lang.perl.idea.copyright.PerlCopyrightsVariablesProvider;
import com.perl5.lang.perl.idea.sdk.host.docker.PerlDockerTestUtil;
import com.perl5.lang.perl.idea.sdk.host.wsl.PerlWslInputFilterProvider;
import com.perl5.lang.perl.idea.sdk.perlInstall.PerlInstallHandlerBase;
import com.perl5.lang.perl.idea.sdk.versionManager.asdf.AsdfTestUtil;
import com.perl5.lang.perl.idea.sdk.versionManager.berrybrew.BerryBrewTestUtil;
import com.perl5.lang.perl.idea.sdk.versionManager.perlbrew.PerlBrewTestUtil;
import com.perl5.lang.perl.idea.sdk.versionManager.plenv.PlenvTestUtil;
import com.perl5.lang.perl.idea.terminal.PerlLocalTerminalCustomizer;
import com.perl5.lang.perl.intellilang.PerlInjectionSupport;
import com.perl5.lang.perl.makeMaker.PerlMakeMakerDirectoryConfigurationProvider;
import com.perl5.lang.perl.moduleBuild.PerlModuleBuildDirectoryConfigurationProvider;
import com.perl5.lang.perl.profiler.run.PerlProfilerProcess;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class PerlPluginInstrumentationTest extends PerlInstrumentationTestCase {
  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return !CategoriesFilter.shouldRun(PerlPluginInstrumentationTest.class) ? Collections.emptyList() : Arrays.asList(new Object[][]{
      {"asdf", AsdfTestUtil.getInstrumentationTestClass(), PLUGIN_PATTERN_STRING},
      {"berrybrew", BerryBrewTestUtil.getInstrumentationTestClass(), PLUGIN_PATTERN_STRING},
      {"carton", PerlCartonDirectoryConfigurationProvider.class, PLUGIN_PATTERN_STRING},
      {"copyright", PerlCopyrightsVariablesProvider.class, PLUGIN_PATTERN_STRING},
      {"core", PerlParserDefinition.class, PLUGIN_PATTERN_STRING},
      {"coverage", PerlCoverageEnabledConfiguration.class, PLUGIN_PATTERN_STRING},
      {"cpan", PerlInstallPackagesWithCpanAction.class, PLUGIN_PATTERN_STRING},
      {"cpanminus", PerlInstallCpanmAction.class, PLUGIN_PATTERN_STRING},
      {"debugger", PerlDebuggerProgramRunner.class, PLUGIN_PATTERN_STRING},
      {"docker", PerlDockerTestUtil.getInstrumentationTestClass(), PLUGIN_PATTERN_STRING},
      {"idea", PerlIdeaTestUtil.getInstrumentationTestClass(), PLUGIN_PATTERN_STRING},
      {"intelliLang", PerlInjectionSupport.class, PLUGIN_PATTERN_STRING},
      {"makeMaker", PerlMakeMakerDirectoryConfigurationProvider.class, PLUGIN_PATTERN_STRING},
      {"moduleBuild", PerlModuleBuildDirectoryConfigurationProvider.class, PLUGIN_PATTERN_STRING},
      {"perlInstall", PerlInstallHandlerBase.class, PLUGIN_PATTERN_STRING},
      {"perlbrew", PerlBrewTestUtil.getInstrumentationTestClass(), PLUGIN_PATTERN_STRING},
      {"plenv", PlenvTestUtil.getInstrumentationTestClass(), PLUGIN_PATTERN_STRING},
      {"profiler", PerlProfilerProcess.class, PLUGIN_PATTERN_STRING},
      {"terminal", PerlLocalTerminalCustomizer.class, PLUGIN_PATTERN_STRING},
      {"wsl", PerlWslInputFilterProvider.class, PLUGIN_PATTERN_STRING},
    });
  }
}
