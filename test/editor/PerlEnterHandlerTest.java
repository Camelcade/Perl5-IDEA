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

public class PerlEnterHandlerTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/enterHandler/perl";
  }

  public void testEmpty() {doTest();}

  public void testCommentMiddle() {
    doTest();
  }

  public void testCommentMiddleIndented() {
    doTest();
  }

  public void testHeredocCloseBare() { doTest(); }

  public void testHeredocClosed() { doTest(); }

  public void testHeredocCloseDQ() { doTest(); }

  public void testHeredocCloseEscaped() { doTest(); }

  public void testHeredocCloseSQ() { doTest(); }

  public void testHeredocCloseWithIndentedAhead() { doTest(); }

  public void testHeredocCloseWithNormalAhead() { doTest(); }

  public void testHeredocCloseXQ() { doTest(); }

  public void testIndentedHeredocCloseBare() { doTest(); }

  public void testIndentedHeredocClosed() { doTest(); }

  public void testIndentedHeredocCloseDQ() { doTest(); }

  public void testIndentedHeredocCloseEscaped() { doTest(); }

  public void testIndentedHeredocCloseSQ() { doTest(); }

  public void testIndentedHeredocCloseWithIndentedAhead() { doTest(); }

  public void testIndentedHeredocCloseWithNormalAhead() { doTest(); }

  public void testIndentedHeredocCloseXQ() { doTest(); }


  private void doTest() {
    doTestEnter();
  }
}
