/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package editor;

import base.PerlLightTestCase;
import com.intellij.testFramework.UsefulTestCase;

public class PerlSmartkeyTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/smartkey/perl";
  }

  public void testCommentMiddle() {
    doTestEnter();
  }

  public void testCommentMiddleIndented() {
    doTestEnter();
  }

  public void testHeredocCloseBare() { doTestEnter(); }

  public void testHeredocClosed() { doTestEnter(); }

  public void testHeredocCloseDQ() { doTestEnter(); }

  public void testHeredocCloseEscaped() { doTestEnter(); }

  public void testHeredocCloseSQ() { doTestEnter(); }

  public void testHeredocCloseWithIndentedAhead() { doTestEnter(); }

  public void testHeredocCloseWithNormalAhead() { doTestEnter(); }

  public void testHeredocCloseXQ() { doTestEnter(); }

  public void testIndentedHeredocCloseBare() { doTestEnter(); }

  public void testIndentedHeredocClosed() { doTestEnter(); }

  public void testIndentedHeredocCloseDQ() { doTestEnter(); }

  public void testIndentedHeredocCloseEscaped() { doTestEnter(); }

  public void testIndentedHeredocCloseSQ() { doTestEnter(); }

  public void testIndentedHeredocCloseWithIndentedAhead() { doTestEnter(); }

  public void testIndentedHeredocCloseWithNormalAhead() { doTestEnter(); }

  public void testIndentedHeredocCloseXQ() { doTestEnter(); }

  protected void doTestEnter() {
    doTest('\n');
  }

  private String getResultAppendix(char typed) {
    return ".typed#" + (int)typed;
  }

  protected void doTest(char typed) {
    initWithFileSmart();
    myFixture.type(typed);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(getResultAppendix(typed)), getFile().getText());
  }
}
