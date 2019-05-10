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

package surroundWith;

import base.PodLightTestCase;

import java.util.regex.Pattern;

public class PodSurroundWithTest extends PodLightTestCase {
  private static final Pattern NAME_PATTERN = Pattern.compile("Surround with \\w<...>");

  @Override
  protected String getTestDataPath() {
    return "testData/surroundWith/pod";
  }

  public void testVerbatimParagraph() {doTest(false);}

  public void testEscapeSymbols() {doTest();}

  public void testEscapeSymbolsWithTags() {doTest();}

  public void testBreakWords() {doTest();}

  public void testBreakTags() {doTest(false);}

  public void testAnglesByClose() {doTest();}

  public void testAnglesByOpen() {doTest();}

  public void testHeadingTitle() {doTest();}

  public void testHeadingTitleWithTag() {doTest(false);}

  public void testParagraphFirstHalf() {doTest();}

  public void testParagraphFull() {doTest();}

  public void testParagraphSpanLines() {doTest(true);}

  public void testParagraphSpanParagraphs() {doTest(false);}

  public void testParagraphFullInHeader() {doTest();}

  public void testParagraphMid() {doTest();}

  public void testParagraphSecondHalf() {doTest();}

  private void doTest() {
    doTest(true);
  }

  private void doTest(boolean shouldHaveSurrounders) {
    doTestSurrounders(it -> NAME_PATTERN.matcher(it).find(), shouldHaveSurrounders);
  }
}
