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

package completion;

import base.PerlCompletionPopupTestCase;
import com.perl5.lang.pod.filetypes.PodFileType;
import org.jetbrains.annotations.NotNull;

public class PodCompletionPopupTest extends PerlCompletionPopupTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/completionPopup/pod";
  }

  @Override
  public String getFileExtension() {
    return PodFileType.EXTENSION;
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerlPod();
    showLiveTemplatesInTests();
  }

  public void testSpaceHeader1() {doTest("=head1<caret>", " ");}

  public void testSpaceHeader2() {doTest("=head2<caret>", " ");}

  public void testSpaceFunc() {doTest("=func<caret>", " ");}

  public void testSpaceMethod() {doTest("=method<caret>", " ");}

  public void testSpaceAttr() {doTest("=attr<caret>", " ");}

  public void testSpaceHeader3() {doTest("=head3<caret>", " ");}

  public void testSpaceHeader4() {doTest("=head4<caret>", " ");}

  public void testSpaceEncoding() {doTest("=encoding<caret>", " ");}

  public void testForSpace() {doTest("=for<caret>", " ");}

  public void testBeginSpace() {doTest("=begin<caret>", " ");}

  public void testEndSpace() {doTest("=end<caret>", " ");}

  public void testForColon() {doTest("=for <caret>", ":");}

  public void testBeginColon() {doTest("=begin <caret>", ":");}

  public void testEndColon() {doTest("=end <caret>", ":");}

  public void testLinkOpenAngle() {doTest("L<caret>", "<");}

  public void testLinkBareSection() {doTest("=head1 section name\n\n" + "L<<caret>>", "/");}

  public void testLinkTitledName() {doTest("L<Test name<caret>>", "|");}

  public void testLinkTitledSection() {doTest("=head1 section name\n\n" + "L<Test name|<caret>>", "/");}

  public void testLinkTitledNamedSection() {doTest("L<Test name|perldoc<caret>>", "/");}

  public void testCommandFileStart() {
    doTest("", "=");
  }

  public void testCommandFileSecondLine() {doTest("\n<caret>", "=");}

  public void testCommandFileThirdLine() {doTest("\n\n<caret>", "=");}

  public void testCommandAfterCommand() {doTest("=head1 test\n\n<caret>", "=");}

  public void testCommandAfterCommandWrong() {doTestNegative("=head1 test\n<caret>", "=");}

  @Override
  protected boolean restrictFilesParsing() {
    return false;
  }

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
