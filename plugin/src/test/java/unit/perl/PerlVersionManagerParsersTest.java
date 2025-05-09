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

package unit.perl;


import base.PerlLightTestCase;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.idea.sdk.versionManager.perlbrew.PerlBrewAdapter;
import com.perl5.lang.perl.idea.sdk.versionManager.plenv.PlenvAdapter;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.function.Function;
public class PerlVersionManagerParsersTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "unit/perl/vmList";
  }

  @Test
  public void testPerlbrewAvailable() {
    doTest(PerlBrewAdapter::parseInstallableDistributionsList);
  }

  @Test
  public void testPerlbrewAvailable2023() {
    doTest(PerlBrewAdapter::parseInstallableDistributionsList);
  }

  @Test
  public void testPerlbrewClean() {
    doTest(PerlBrewAdapter::parseInstalledDistributionsList);
  }

  @Test
  public void testPerlbrewSelected() {
    doTest(PerlBrewAdapter::parseInstalledDistributionsList);
  }

  @Test
  public void testPlenvAvailable() {
    doTest(PlenvAdapter::parseInstallableDistributionsList);
  }

  @Test
  public void testPlenvClean() {
    doTest(PlenvAdapter::parseInstalledDistributionsList);
  }

  @Test
  public void testPlenvGlobal() {
    doTest(PlenvAdapter::parseInstalledDistributionsList);
  }

  @Test
  public void testPlenvLocal() {
    doTest(PlenvAdapter::parseInstalledDistributionsList);
  }

  private void doTest(@NotNull Function<? super List<String>, ? extends List<String>> parser) {
    String outputText = loadFile(new File(getTestDataPath(), getTestName(true) + ".code"));
    List<String> output = StringUtil.split(outputText, "\n");
    List<String> parsedOutput = parser.apply(output);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), StringUtil.join(parsedOutput, "\n"));
  }
}
