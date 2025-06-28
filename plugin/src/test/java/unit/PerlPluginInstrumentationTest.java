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
import org.jetbrains.annotations.NotNull;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;

public class PerlPluginInstrumentationTest extends PerlInstrumentationTestCase {
  private static @NotNull Object[] buildData(@NotNull String moduleName, @NotNull Object clazz) {
    return new Object[]{
      moduleName, clazz, Pattern.compile("lib/modules/perl5\\.plugin\\." + moduleName + "\\.main\\.jar!")
    };
  }
  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return !CategoriesFilter.shouldRun(PerlPluginInstrumentationTest.class) ? Collections.emptyList() : Arrays.asList(new Object[][]{
      buildData("asdf", AsdfTestUtil.getInstrumentationTestClass()),
      buildData("berrybrew", BerryBrewTestUtil.getInstrumentationTestClass()),
      buildData("carton", PerlCartonDirectoryConfigurationProvider.class),
      buildData("copyright", PerlCopyrightsVariablesProvider.class),
      buildData("backend", PerlParserDefinition.class),
      buildData("coverage", PerlCoverageEnabledConfiguration.class),
      buildData("cpan", PerlInstallPackagesWithCpanAction.class),
      buildData("cpanminus", PerlInstallCpanmAction.class),
      buildData("debugger", PerlDebuggerProgramRunner.class),
      buildData("docker", PerlDockerTestUtil.getInstrumentationTestClass()),
      buildData("idea", PerlIdeaTestUtil.getInstrumentationTestClass()),
      buildData("intelliLang", PerlInjectionSupport.class),
      buildData("makeMaker", PerlMakeMakerDirectoryConfigurationProvider.class),
      buildData("moduleBuild", PerlModuleBuildDirectoryConfigurationProvider.class),
      buildData("perlInstall", PerlInstallHandlerBase.class),
      buildData("perlbrew", PerlBrewTestUtil.getInstrumentationTestClass()),
      buildData("plenv", PlenvTestUtil.getInstrumentationTestClass()),
      buildData("profiler", PerlProfilerProcess.class),
      buildData("terminal", PerlLocalTerminalCustomizer.class),
      buildData("wsl", PerlWslInputFilterProvider.class),
    });
  }
}
