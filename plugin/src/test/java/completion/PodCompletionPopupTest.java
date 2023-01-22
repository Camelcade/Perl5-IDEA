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

package completion;


import base.PerlCompletionPopupTestCase;
import com.intellij.codeInsight.lookup.LookupManager;
import com.perl5.lang.pod.filetypes.PodFileType;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
public class PodCompletionPopupTest extends PerlCompletionPopupTestCase {
  @Override
  protected String getBaseDataPath() {
    return "completionPopup/pod";
  }

  @Override
  public String getFileExtension() {
    return PodFileType.EXTENSION;
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerlPod528();
  }

  @Test
  public void testSurroundWithLinkPopup() {
    doTestSurrounders(it -> it.contains("Surround with L<...>"), true);
    myTester.joinAutopopup();
    myTester.joinCompletion();
    assertNotNull(LookupManager.getActiveLookup(getEditor()));
  }

  @Test
  public void testColonStart() {doTestNegative("<caret>sometext", ":");}

  @Test
  public void testSpaceHeader1() {doTest("=head1<caret>", " ");}

  @Test
  public void testSpaceHeader2() {doTest("=head2<caret>", " ");}

  @Test
  public void testSpaceFunc() {doTest("=func<caret>", " ");}

  @Test
  public void testSpaceMethod() {doTest("=method<caret>", " ");}

  @Test
  public void testSpaceAttr() {doTest("=attr<caret>", " ");}

  @Test
  public void testSpaceHeader3() {doTest("=head3<caret>", " ");}

  @Test
  public void testSpaceHeader4() {doTest("=head4<caret>", " ");}

  @Test
  public void testSpaceItem() {doTest("=item<caret>", " ");}

  @Test
  public void testSpaceEncoding() {doTest("=encoding<caret>", " ");}

  @Test
  public void testForSpace() {doTest("=for<caret>", " ");}

  @Test
  public void testBeginSpace() {doTest("=begin<caret>", " ");}

  @Test
  public void testEndSpace() {doTest("=end<caret>", " ");}

  @Test
  public void testForColon() {doTest("=for <caret>", ":");}

  @Test
  public void testBeginColon() {doTest("=begin <caret>", ":");}

  @Test
  public void testEndColon() {doTest("=end <caret>", ":");}

  @Test
  public void testLinkOpenAngle() {doTest("L<caret>", "<");}

  @Test
  public void testLinkBareSection() {
    doTest("""
             =head1 section name

             L<<caret>>""", "/");
  }

  @Test
  public void testLinkTitledName() {doTest("L<Test name<caret>>", "|");}

  @Test
  public void testLinkTitledSection() {
    doTest("""
             =head1 section name

             L<Test name|<caret>>""", "/");
  }

  @Test
  public void testLinkTitledNamedSection() {doTest("L<Test name|perldoc<caret>>", "/");}

  @Test
  public void testCommandFileStart() {
    doTest("", "=");
  }

  @Test
  public void testCommandFileSecondLine() {doTest("\n<caret>", "=");}

  @Test
  public void testCommandFileThirdLine() {doTest("\n\n<caret>", "=");}

  @Test
  public void testCommandAfterCommand() {doTest("=head1 test\n\n<caret>", "=");}

  @Test
  public void testCommandAfterCommandWrong() {doTestNegative("=head1 test\n<caret>", "=");}

  @Test
  public void testLinkSectionAnyKeyStart() {doTest("L<perlpod/<caret>>", "=");}

  @Test
  public void testLinkSectionAnyKeyContinue() {doTest("L<perlvar/$<caret>>", "'");}

  @Override
  protected void doTest(@NotNull String initial, @NotNull String toType) {
    myFixture.copyFileToProject("test.pm");
    super.doTest(initial, toType);
  }

  @Override
  protected void doTestNegative(@NotNull String initial, @NotNull String toType) {
    myFixture.copyFileToProject("test.pm");
    super.doTestNegative(initial, toType);
  }
}
