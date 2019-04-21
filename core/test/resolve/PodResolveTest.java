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

package resolve;

import base.PodLightTestCase;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class PodResolveTest extends PodLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/resolve/pod";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerlPod();
  }

  public void testPerlVar() {
    doTest("L<perlvar>");
  }

  public void testPerlVarTitled() {
    doTest("L<VarC<ia>bles|perlvar>");
  }

  public void testPerlVarGeneralVariables() {
    doTest("L<perlvar/General Variables>");
  }

  public void testPerlVarGeneralVariablesQuoted() {
    doTest("L<perlvar/\"General Variables\">");
  }

  public void testPerlVarGeneralVariablesTitled() {
    doTest("L<Vars|perlvar/General Variables>");
  }

  public void testPerlVarGeneralVariablesQuotedTitled() {
    doTest("L<C<VARIABLES>|perlvar/\"General Variables\">");
  }

  public void testPerlVarArg() {
    doTest("L<perlvar/$ARG>");
  }

  public void testPerlVarArgQuoted() {
    doTest("L<perlvar/\"$ARG\">");
  }

  public void testPerlVarArgTitled() {
    doTest("L<ARG|perlvar/$ARG>");
  }

  public void testPerlVarArgQuotedTitled() {
    doTest("L<AC<R>G|perlvar/\"$ARG\">");
  }

  public void testIndex() {
    doTest("L<perlpod/Z>");
  }

  public void testIndexQuoted() {
    doTest("L<perlpod/\"Z\">");
  }

  public void testInsideFile() {
    doTest();
  }

  public void testNearFile() {
    VirtualFile targetFile = myFixture.copyFileToProject("definitions.pod", "lib/definitions.pod");
    assertNotNull(targetFile);
    markAsLibRoot(targetFile.getParent(), true);

    doTest();
  }

  public void testIndexTitled() {
    doTest("L<zero formatter|perlpod/Z>");
  }

  public void testIndexQuotedTitled() {
    doTest("L<ZERO|perlpod/\"Z\">");
  }

  private void doTest(@NotNull String text) {
    initWithTextSmartWithoutErrors(text);
    doTestResolveWithoutInit(false);
  }

  @Override
  protected boolean restrictFilesParsing() {
    return false;
  }

  private void doTest() {
    doTestResolve();
  }
}
