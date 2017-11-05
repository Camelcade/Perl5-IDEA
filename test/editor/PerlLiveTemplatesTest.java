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

public class PerlLiveTemplatesTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/liveTemplates/perl";
  }

  public void testConst() {doTest();}

  public void testContinue() {doTest();}

  public void testDo() {doTest();}

  public void testPod() {doTest();}

  public void testElse() {doTest();}

  public void testElsif() {doTest();}

  public void testEval() {doTest();}

  public void testForeachPrefix() {doTest();}

  public void testForPrefix() {doTest();}

  public void testGiven() {doTest();}

  public void testGrep() {doTest();}

  public void testIfPrefix() {doTest();}

  public void testMap() {doTest();}

  public void testSort() {doTest();}

  public void testSql() {doTest();}

  public void testSub() {doTest();}

  public void testSubAnon() {doTest();}

  public void testUnlessPrefix() {doTest();}

  public void testUntilPrefix() {doTest();}

  public void testUseParent() {doTest();}

  public void testUseStrict() {doTest();}

  public void testUseWarnings() {doTest();}

  public void testWhilePrefix() {doTest();}

  private void doTest() {
    doTest(false);
  }

  private void doTest(boolean checkErrors) {
    doLiveTemplateTest(checkErrors);
  }
}
