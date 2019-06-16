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

package parser;

import org.junit.Test;
public class PODParserTest extends PerlParserTestBase {
  public PODParserTest() {
    super("pod");
  }

  @Override
  protected String getTestDataPath() {
    return "testData/parser/pod";
  }

  @Test
  public void testVariousItems() {doTest();}

  @Test
  public void testVersionConfusedNumbers() {doTest();}

  @Test
  public void testIndexedItems() {doTest();}

  @Test
  public void testIndexedParagraphTop() {doTest();}

  @Test
  public void testIndexedSectionContent() {doTest();}

  @Test
  public void testItems() {doTest();}

  @Test
  public void testItemsIndexed() {doTest();}

  @Test
  public void testCloseMultiAngleWIthNewLine() {doTest();}

  @Test
  public void testCodeWithAnglesAndArrow() {doTest();}

  @Test
  public void testFormatterWithHeredoc() {doTest();}

  @Test
  public void testLinksWithUnbalancedOpeners() {doTest();}

  @Test
  public void testLinksWithUnbalancedCloseAngles() {doTest();}

  @Test
  public void testIncompleteLink() {doTest(false);}

  @Test
  public void testPodWeaverTags() {doTest();}

  @Test
  public void testUnknownSectionWIthContent() {doTest();}

  @Test
  public void testHierarchy() {
    doTest();
  }

  @Test
  public void testOverRecovery() {
    doTest(false);
  }

  @Test
  public void testForBeginContent() {
    doTest(false);
  }

  @Test
  public void testBeginRecovery() {
    doTest(false);
  }

  @Test
  public void testLinks() {doTest();}

  @Test
  public void testLinksWithCodeInside() {doTest();}

  @Test
  public void testLinksWithFalseQuote() {doTest();}

  @Test
  public void testLinksMan() {doTest();}

  @Test
  public void testLinksCode() {doTest();}

  @Test
  public void testLinksEscaped() {doTest();}

  @Test
  public void testLinksUrl() {doTest();}

  @Test
  public void testLinksComplex1() {doTest();}

  @Test
  public void testLinksComplex2() {doTest();}
}
