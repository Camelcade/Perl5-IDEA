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
import org.jetbrains.annotations.NotNull;

public class PerlLiveTemplatesTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/liveTemplates/perl";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    enableLiveTemplatesTesting();
  }

  public void testMethod() {doTest("me");}

  public void testFunc() {doTest("fu");}


  public void testConst() {doTest("cons");}

  public void testContinue() {doTest("cont");}

  public void testDo() {doTest("do");}

  public void testPod() {doTest("pod");}

  public void testElse() {doTest("el");}

  public void testElsif() {doTest("eli");}

  public void testEval() {doTest("ev");}

  public void testForeachPrefix() {doTest("fe");}

  public void testForPrefix() {doTest("fo");}

  public void testGiven() {doTest("gi");}

  public void testGrep() {doTest("gr");}

  public void testIfPrefix() {doTest("if");}

  public void testMap() {doTest("ma");}

  public void testSubAnon() {doTest("sa");}

  public void testSort() {doTest("sort");}

  public void testSql() {doTest("sql");}

  public void testSub() {doTest("sub");}

  public void testUnlessPrefix() {doTest("unl");}

  public void testUntilPrefix() {doTest("unt");}

  public void testUseParent() {doTest("upa");}

  public void testUseStrict() {doTest("ust");}

  public void testUseWarnings() {doTest("uwa");}

  public void testWhilePrefix() {doTest("wh");}

  private void doTest(@NotNull String textToType) {
    doLiveTemplateTest("liveTemplatesTest", textToType);
  }
}
