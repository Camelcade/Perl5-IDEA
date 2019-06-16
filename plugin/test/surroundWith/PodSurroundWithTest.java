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
import org.junit.Test;

import java.util.regex.Pattern;
public class PodSurroundWithTest extends PodLightTestCase {
  private static final Pattern NAME_PATTERN = Pattern.compile("Surround with \\w<...>");

  @Override
  protected String getTestDataPath() {
    return "testData/surroundWith/pod";
  }

  @Test
  public void testVerbatimParagraph() {doTest(false);}

  @Test
  public void testEscapeSymbols() {doTest();}

  @Test
  public void testEscapeSymbolsWithTags() {doTest();}

  @Test
  public void testBreakWords() {doTest();}

  @Test
  public void testBreakTags() {doTest(false);}

  @Test
  public void testAnglesByClose() {doTest();}

  @Test
  public void testAnglesByOpen() {doTest();}

  @Test
  public void testHeadingTitle() {doTest();}

  @Test
  public void testHeadingTitleWithTag() {doTest(false);}

  @Test
  public void testParagraphFirstHalf() {doTest();}

  @Test
  public void testParagraphFull() {doTest();}

  @Test
  public void testParagraphSpanLines() {doTest(true);}

  @Test
  public void testParagraphSpanParagraphs() {doTest(false);}

  @Test
  public void testParagraphFullInHeader() {doTest();}

  @Test
  public void testParagraphMid() {doTest();}

  @Test
  public void testParagraphSecondHalf() {doTest();}

  private void doTest() {
    doTest(true);
  }

  private void doTest(boolean shouldHaveSurrounders) {
    doTestSurrounders(it -> NAME_PATTERN.matcher(it).find(), shouldHaveSurrounders);
  }
}
