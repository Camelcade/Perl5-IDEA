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

package completion;

import base.PerlCompletionPopupTestCase;
import com.intellij.testFramework.fixtures.CompletionAutoPopupTester;
import com.perl5.lang.pod.filetypes.PodFileType;

public class PodCompletionPopupTest extends PerlCompletionPopupTestCase {
  protected CompletionAutoPopupTester myTester;

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
  }

  public void testLinkOpenAngle() {doTest("L<caret>", "<");}

  public void testLinkBareSection() {doTest("=head1 section name\n\n" + "L<<caret>>", "/");}

  public void testLinkTitledName() {doTest("L<Test name<caret>>", "|");}

  public void testLinkTitledSection() {doTest("=head1 section name\n\n" + "L<Test name|<caret>>", "/");}

  public void testLinkTitledNamedSection() {doTest("L<Test name|perldoc<caret>>", "/");}
}
