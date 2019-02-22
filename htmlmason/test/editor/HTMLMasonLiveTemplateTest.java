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

package editor;

import base.HTMLMasonLightTestCase;
import org.jetbrains.annotations.NotNull;

public class HTMLMasonLiveTemplateTest extends HTMLMasonLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/liveTemplates";
  }

  public void testSayStdout() {doTest("sout"); }

  public void testSayStderr() {doTest("serr"); }

  public void testPrintStdout() {doTest("pout"); }

  public void testPrintStderr() {doTest("perr"); }

  public void testConst() {doTest("cons");}

  public void testDo() {doTest("do");}

  public void testPod() {doTest("pod");}

  public void testElse() {doTest("el");}

  public void testElsif() {doTest("eli");}

  public void testEval() {doTest("ev");}

  public void testForeach() {doTest("fe");}

  public void testFor() {doTest("fo");}

  public void testGiven() {doTest("gi");}

  public void testGrep() {doTest("gr");}

  public void testIf() {doTest("if");}

  public void testMap() {doTest("ma");}

  public void testSubAnon() {doTest("sa");}

  public void testSort() {doTest("so");}

  public void testSql() {doTest("sql");}

  public void testSub() {doTest("sub");}

  public void testUnless() {doTest("unl");}

  public void testUntil() {doTest("unt");}

  public void testUseParent() {doTest("upa");}

  public void testUseStrict() {doTest("ust");}

  public void testUseWarnings() {doTest("uwa");}

  public void testUseVersion() {doTest("uv");}

  public void testWhile() {doTest("wh");}

  protected void doTest(@NotNull String textToType) {
    doLiveTemplateTest(textToType);
  }
}
