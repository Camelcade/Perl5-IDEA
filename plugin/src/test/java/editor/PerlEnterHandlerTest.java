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

package editor;


import base.PerlLightTestCase;
import org.junit.Test;
public class PerlEnterHandlerTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "enterHandler/perl";
  }

  @Test
  public void testEmpty() {doTest();}

  @Test
  public void testCommentMiddle() {
    doTest();
  }

  @Test
  public void testCommentMiddleIndented() {
    doTest();
  }

  @Test
  public void testHeredocCloseBare() { doTest(); }

  @Test
  public void testHeredocClosed() { doTest(); }

  @Test
  public void testHeredocCloseDQ() { doTest(); }

  @Test
  public void testHeredocCloseEscaped() { doTest(); }

  @Test
  public void testHeredocCloseSQ() { doTest(); }

  @Test
  public void testHeredocCloseWithIndentedAhead() { doTest(); }

  @Test
  public void testHeredocCloseWithNormalAhead() { doTest(); }

  @Test
  public void testHeredocCloseXQ() { doTest(); }

  @Test
  public void testIndentedHeredocCloseBare() { doTest(); }

  @Test
  public void testIndentedHeredocClosed() { doTest(); }

  @Test
  public void testIndentedHeredocCloseDQ() { doTest(); }

  @Test
  public void testIndentedHeredocCloseEscaped() { doTest(); }

  @Test
  public void testIndentedHeredocCloseSQ() { doTest(); }

  @Test
  public void testIndentedHeredocCloseWithIndentedAhead() { doTest(); }

  @Test
  public void testIndentedHeredocCloseWithNormalAhead() { doTest(); }

  @Test
  public void testIndentedHeredocCloseXQ() { doTest(); }


  private void doTest() {
    doTestEnter();
  }
}
