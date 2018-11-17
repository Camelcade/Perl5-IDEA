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

package parser;

import com.perl5.lang.tt2.TemplateToolkitParserDefinition;

/**
 * Created by hurricup on 06.06.2016.
 */
public class TemplateToolkitParserTest extends PerlParserTestBase {
  public TemplateToolkitParserTest() {
    super("", "tt", new TemplateToolkitParserDefinition());
  }

  @Override
  protected String getTestDataPath() {
    return "testData/parser";
  }

  public void testOperators() {
    doTest();
  }

  public void testPrecedence() {
    doTest();
  }

  public void testStrings() {
    doTest();
  }

  public void testHash() {
    doTest();
  }

  public void testSub() {
    doTest();
  }

  public void testArray() {
    doTest();
  }

  public void testVariables() {
    doTest();
  }

  public void testChomp() {
    doTest();
  }

  public void testComments() {
    doTest();
  }

  public void testGet() {
    doTest();
  }

  public void testCall() {
    doTest();
  }

  public void testSet() {
    doTest();
  }

  public void testDefault() {
    doTest();
  }

  public void testInsert() {
    doTest();
  }

  public void testInclude() {
    doTest();
  }

  public void testProcess() {
    doTest();
  }

  public void testBlockNamed() {
    doTest();
  }

  public void testBlockNamedUnclosed() {
    doTest(false);
  }

  public void testBlockAnon() {
    doTest();
  }

  public void testBlockAnonUnclosed() {
    doTest(false);
  }

  public void testWrapper() {
    doTest();
  }

  public void testWrapperUnclosed() {
    doTest(false);
  }

  public void testIf() {
    doTest();
  }

  public void testIfUnclosed() {
    doTest(false);
  }

  public void testSwitch() {
    doTest();
  }

  public void testForeach() {
    doTest();
  }

  public void testForeachUnclosed() {
    doTest(false);
  }

  public void testWhile() {
    doTest();
  }

  public void testWhileUnclosed() {
    doTest(false);
  }


  public void testFilter() {
    doTest();
  }

  public void testFilterPostfix() {
    doTest();
  }

  public void testFilterUnclosed() {
    doTest(false);
  }

  public void testThrow() {
    doTest();
  }

  public void testUse() {
    doTest();
  }

  public void testMacro() {
    doTest();
  }

  public void testPerl() {
    doTest();
  }

  public void testPerlUnclosed() {
    doTest(false);
  }

  public void testTryCatch() {
    doTest();
  }

  public void testTryCatchUnclosed() {
    doTest(false);
  }

  public void testNext() {
    doTest();
  }

  public void testLast() {
    doTest();
  }

  public void testReturn() {
    doTest();
  }

  public void testStop() {
    doTest();
  }

  public void testClear() {
    doTest();
  }

  public void testMeta() {
    doTest();
  }

  public void testDebug() {
    doTest();
  }

  public void testTags() {
    doTest();
  }

  public void testChompMarkers() {
    doTest();
  }

  public void testMultiDirectivesBlocks() {
    doTest();
  }

  public void testIssue1262() {
    doTest();
  }

  public void testIssue1263() {
    doTest();
  }
}
