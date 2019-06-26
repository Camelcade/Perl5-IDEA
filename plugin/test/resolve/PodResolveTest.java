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

package resolve;


import base.PodLightTestCase;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
public class PodResolveTest extends PodLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/resolve/pod";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerlPod();
  }

  @Test
  public void testHead1SameFormatting() {doTest();}

  @Test
  public void testPerlVar() {
    doTest("L<perlvar>");
  }

  @Test
  public void testPerlVarTitled() {
    doTest("L<VarC<ia>bles|perlvar>");
  }

  @Test
  public void testPerlVarGeneralVariables() {
    doTest("L<perlvar/General Variables>");
  }

  @Test
  public void testPerlVarGeneralVariablesQuoted() {
    doTest("L<perlvar/\"General Variables\">");
  }

  @Test
  public void testPerlVarGeneralVariablesTitled() {
    doTest("L<Vars|perlvar/General Variables>");
  }

  @Test
  public void testPerlVarGeneralVariablesQuotedTitled() {
    doTest("L<C<VARIABLES>|perlvar/\"General Variables\">");
  }

  @Test
  public void testPerlVarArg() {
    doTest("L<perlvar/$ARG>");
  }

  @Test
  public void testPerlVarArgQuoted() {
    doTest("L<perlvar/\"$ARG\">");
  }

  @Test
  public void testPerlVarArgTitled() {
    doTest("L<ARG|perlvar/$ARG>");
  }

  @Test
  public void testPerlVarArgQuotedTitled() {
    doTest("L<AC<R>G|perlvar/\"$ARG\">");
  }

  @Test
  public void testIndex() {
    doTest("L<perlpod/Z>");
  }

  @Test
  public void testIndexQuoted() {
    doTest("L<perlpod/\"Z\">");
  }

  @Test
  public void testInsideFile() {
    doTest();
  }

  @Test
  public void testExternalPod() {
    doTestExternalFile("definitions.pod");
  }

  @Test
  public void testExternalPm() {
    doTestExternalFile("definitions.pm");
  }

  private void doTestExternalFile(@NotNull String fileToCopy) {
    initWithFileSmartWithoutErrors("toExternalFile");
    VirtualFile targetFile = myFixture.copyFileToProject(fileToCopy, "lib/" + fileToCopy);
    assertNotNull(targetFile);
    markAsLibRoot(targetFile.getParent(), true);

    doTestResolveWithoutInit(false);
  }

  @Test
  public void testIndexTitled() {
    doTest("L<zero formatter|perlpod/Z>");
  }

  @Test
  public void testIndexQuotedTitled() {
    doTest("L<ZERO|perlpod/\"Z\">");
  }

  private void doTest(@NotNull String text) {
    initWithTextSmartWithoutErrors(text);
    doTestResolveWithoutInit(false);
  }

  private void doTest() {
    doTestResolve();
  }
}
