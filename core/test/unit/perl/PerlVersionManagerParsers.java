/*
 * Copyright 2015-2019 Alexandr Evstigneev
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
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.idea.sdk.versionManager.perlbrew.PerlBrewAdapter;
import com.perl5.lang.perl.idea.sdk.versionManager.plenv.PlenvAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class PerlVersionManagerParsers extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/unit/perl/vmList";
  }

  public void testPerlbrewAvailable() {
    doTest(PerlBrewAdapter::parseInstallableDistributionsList);
  }

  public void testPerlbrewClean() {
    doTest(PerlBrewAdapter::parseInstalledDistributionsList);
  }

  public void testPerlbrewSelected() {
    doTest(PerlBrewAdapter::parseInstalledDistributionsList);
  }

  public void testPlenvAvailable() {
    doTest(PlenvAdapter::parseInstallableDistributionsList);
  }

  public void testPlenvClean() {
    doTest(PlenvAdapter::parseInstalledDistributionsList);
  }

  public void testPlenvGlobal() {
    doTest(PlenvAdapter::parseInstalledDistributionsList);
  }

  public void testPlenvLocal() {
    doTest(PlenvAdapter::parseInstalledDistributionsList);
  }

  private void doTest(@NotNull Function<List<String>, List<String>> parser) {
    try {
      String outputText = FileUtil.loadFile(new File(getTestDataPath(), getTestName(true) + ".code"));
      List<String> output = StringUtil.split(outputText, "\n");
      List<String> parsedOutput = parser.apply(output);
      UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), StringUtil.join(parsedOutput, "\n"));
    }
    catch (IOException e) {
      fail(e.getMessage());
    }
  }
}
