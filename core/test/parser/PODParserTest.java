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

package parser;

import com.perl5.lang.pod.PodParserDefinition;

/**
 * Created by hurricup on 24.03.2016.
 */
public class PODParserTest extends PerlParserTestBase {
  public PODParserTest() {
    super("", "pod", new PodParserDefinition());
  }

  @Override
  protected String getTestDataPath() {
    return "testData/parser/pod";
  }

  public void testCloseMultiAngleWIthNewLine() {doTest();}

  public void testCodeWithAnglesAndArrow() {doTest();}

  public void testFormatterWithHeredoc() {doTest();}

  public void testLinksWithUnbalancedOpeners() {doTest();}

  public void testLinksWithUnbalancedCloseAngles() {doTest();}

  public void testIncompleteLink() {doTest(false);}

  public void testPodWeaverTags() {doTest();}

  public void testUnknownSectionWIthContent() {doTest();}

  public void testHierarchy() {
    doTest();
  }

  public void testOverRecovery() {
    doTest(false);
  }

  public void testForBeginContent() {
    doTest(false);
  }

  public void testBeginRecovery() {
    doTest(false);
  }

  public void testLinks() {doTest();}

  public void testLinksWithCodeInside() {doTest();}

  public void testLinksWithFalseQuote() {doTest();}

  public void testLinksMan() {doTest();}

  public void testLinksCode() {doTest();}

  public void testLinksEscaped() {doTest();}

  public void testLinksUrl() {doTest();}

  public void testLinksComplex1() {doTest();}

  public void testLinksComplex2() {doTest();}
}
