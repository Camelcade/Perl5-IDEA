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

package unit;

import base.PerlInstrumentationTestCase;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.carton.PerlCartonDirectoryConfigurationProvider;
import com.perl5.lang.perl.coverage.PerlCoverageEnabledConfiguration;
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
import com.perl5.lang.perl.profiler.PerlProfilerBundle;
import org.jetbrains.annotations.NotNull;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class PerlPluginInstrumentationTest extends PerlInstrumentationTestCase {

  public PerlPluginInstrumentationTest(@NotNull String ignoredName, @NotNull Class<?> cls) {
    super(cls);
  }

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
      {"asdf", AsdfTestUtil.getInstrumentationTestClass()},
      {"berrybrew", BerryBrewTestUtil.getInstrumentationTestClass()},
      {"carton", PerlCartonDirectoryConfigurationProvider.class},
      {"copyright", PerlCopyrightsVariablesProvider.class},
      {"core", PerlParserDefinition.class},
      {"coverage", PerlCoverageEnabledConfiguration.class},
      {"debugger", PerlDebuggerProgramRunner.class},
      {"docker", PerlDockerTestUtil.getInstrumentationTestClass()},
      {"idea", PerlIdeaTestUtil.getInstrumentationTestClass()},
      {"intelliLang", PerlInjectionSupport.class},
      {"makeMaker", PerlMakeMakerDirectoryConfigurationProvider.class},
      {"moduleBuild", PerlModuleBuildDirectoryConfigurationProvider.class},
      {"perlInstall", PerlInstallHandlerBase.class},
      {"perlbrew", PerlBrewTestUtil.getInstrumentationTestClass()},
      {"plenv", PlenvTestUtil.getInstrumentationTestClass()},
      {"profiler", PerlProfilerBundle.class},
      {"terminal", PerlLocalTerminalCustomizer.class},
      {"wsl", PerlWslInputFilterProvider.class},
    });
  }
}
