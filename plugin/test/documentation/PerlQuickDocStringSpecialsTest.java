/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package documentation;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class PerlQuickDocStringSpecialsTest extends PerlQuickDocTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/documentation/perl/quickdoc/stringSpecials";
  }

  @Test
  public void testTab() {doTestDq("test \\<caret>t text");}

  @Test
  public void testNewLine() {doTestDq("test \\<caret>n text");}

  @Test
  public void testReturn() {doTestDq("test \\<caret>r text");}

  @Test
  public void testFormFeed() {doTestDq("test \\<caret>f text");}

  @Test
  public void testBackSpace() {doTestDq("test \\<caret>b text");}

  @Test
  public void testAlarm() {doTestDq("test \\<caret>a text");}

  @Test
  public void testEscape() {doTestDq("test \\<caret>e text");}

  @Test
  public void testLowerNext() {doTestDq("test \\<caret>l text");}

  @Test
  public void testTitleNext() {doTestDq("test \\<caret>u text");}

  @Test
  public void testLowerStart() {doTestDq("test \\<caret>L text");}

  @Test
  public void testUpperStart() {doTestDq("test \\<caret>U text");}

  @Test
  public void testFoldStart() {doTestDq("test \\<caret>F text");}

  @Test
  public void testQuoteStart() {doTestDq("test \\<caret>Q text");}

  @Test
  public void testEndBlock() {doTestDq("test \\<caret>E text");}

  @Test
  public void testCodedSubstitution() {doTestDq("test \\<caret>cA text");}

  @Test
  public void testHexX() {doTestDq("test \\<caret>xFF text");}

  @Test
  public void testHexBracedX() {doTestDq("test \\<caret>x{FF} text");}

  @Test
  public void testHexBracedOpenBrace() {doTestDq("test \\x<caret>{FF} text");}

  @Test
  public void testHexBracedCloseBrace() {doTestDq("test \\x{FF<caret>} text");}

  @Test
  public void testUnicodeNamedN() {doTestDq("test \\<caret>N{BICYCLIST} text");}

  @Test
  public void testUnicodeNamedName() {doTestDq("test \\N{BI<caret>CYCLIST} text");}

  @Test
  public void testUnicodeNamedOpenBrace() {doTestDq("test \\N<caret>{BICYCLIST} text");}

  @Test
  public void testUnicodeNamedCloseBrace() {doTestDq("test \\N{BICYCLIST<caret>} text");}

  @Test
  public void testEscapeCharDq() {doTestDq("test <caret>\\zCYCLIST text");}

  @Test
  public void testHexNumberBracedDq() {doTestDq("text \\x{26<caret>3a} text");}

  @Test
  public void testHexNumberDq() {doTestDq("text \\x1<caret>b text");}

  @Test
  public void testUnicodeNumberDq() {doTestDq("text \\N{U+26<caret>3D} text");}

  @Test
  public void testOctNumberBracedDq() {doTestDq("text \\o{23<caret>72} text");}

  @Test
  public void testOctNumberDq() {doTestDq("text \\0<caret>12 text");}

  @Test
  public void testBackref1() {doTestDq("text \\<caret>1 text");}

  @Test
  public void testBackref1000() {doTestDq("text \\10<caret>00 text");}

  @Test
  public void testBackref1Re() {doTestRe("text \\<caret>1 text");}

  @Test
  public void testBackref1000Re() {doTestRe("text \\100<caret>0 text");}

  @Test
  public void testEscapeCharSq() {doTestSq("test <caret>\\' text");}


  private void doTestDq(@NotNull String stringContentWithCaret) {
    doTestText("say \"" + stringContentWithCaret + "\"");
  }

  private void doTestRe(@NotNull String stringContentWithCaret) {
    doTestText("s/match/" + stringContentWithCaret + "/;");
  }

  private void doTestSq(@NotNull String stringContentWithCaret) {
    doTestText("say '" + stringContentWithCaret + "'");
  }

  private void doTestText(String textToInit) {
    assertTrue(textToInit.contains("<caret>"));
    withPerlPod528();
    initWithTextSmart(textToInit);
    doTestQuickDocWithoutInit();
  }
}
