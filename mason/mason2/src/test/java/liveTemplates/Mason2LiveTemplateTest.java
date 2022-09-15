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

package liveTemplates;


import base.Mason2TopLevelComponentTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class Mason2LiveTemplateTest extends Mason2TopLevelComponentTestCase {
  @Override
  protected String getBaseDataPath() {
    return "liveTemplates/mc";
  }

  @Test
  public void testSayStdout() {doTest("sout"); }

  @Test
  public void testSayStderr() {doTest("serr"); }

  @Test
  public void testPrintStdout() {doTest("pout"); }

  @Test
  public void testPrintStderr() {doTest("perr"); }

  @Test
  public void testConst() {doTest("cons");}

  @Test
  public void testDo() {doTest("do");}

  @Test
  public void testPod() {doTest("pod");}

  @Test
  public void testElse() {doTest("el");}

  @Test
  public void testElsif() {doTest("eli");}

  @Test
  public void testEval() {doTest("ev");}

  @Test
  public void testForeach() {doTest("fe");}

  @Test
  public void testFor() {doTest("fo");}

  @Test
  public void testGiven() {doTest("gi");}

  @Test
  public void testGrep() {doTest("gr");}

  @Test
  public void testIf() {doTest("if");}

  @Test
  public void testMap() {doTest("ma");}

  @Test
  public void testSubAnon() {doTest("sa");}

  @Test
  public void testSort() {doTest("so");}

  @Test
  public void testSql() {doTest("sql");}

  @Test
  public void testSub() {doTest("sub");}

  @Test
  public void testUnless() {doTest("unl");}

  @Test
  public void testUntil() {doTest("unt");}

  @Test
  public void testUseParent() {doTest("upa");}

  @Test
  public void testUseStrict() {doTest("ust");}

  @Test
  public void testUseWarnings() {doTest("uwa");}

  @Test
  public void testUseVersion() {doTest("uv");}

  @Test
  public void testWhile() {doTest("wh");}

  protected void doTest(@NotNull String textToType) {
    doLiveTemplateBulkTest(textToType);
  }
}
