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

import base.PerlLightTestCase;
import org.jetbrains.annotations.NotNull;

public class PerlParameterInfoHandlerBuiltInTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/parameterInfo/builtInLeftward";
  }

  public void testClose() {doTest();}

  public void testOct() {doTest();}

  public void testOpen() {doTest();}

  public void testOpendir() {doTest();}

  public void testOrd() {doTest();}

  public void testRequire() {doTest();}

  public void testPrint() {doTest();}

  public void testPrintf() {doTest();}

  public void testSprintf() {doTest();}

  public void testSay() {doTest();}

  public void testMap() {doTest();}

  public void testGrep() {doTest();}

  public void testSort() {doTest();}

  public void testScalar() {doTest();}

  public void testEach() {doTest();}

  public void testKeys() {doTest();}

  public void testValues() {doTest();}

  public void testDelete() {doTest();}

  public void testSplice() {doTest();}

  public void testDefined() {doTest();}

  public void testWantarray() {doTest();}

  public void testBless() {doTest();}

  public void testPop() {doTest();}

  public void testShift() {doTest();}

  public void testPush() {doTest();}

  public void testUnshift() {doTest();}

  public void testRef() {doTest();}

  public void testSplit() {doTest();}

  public void testJoin() {doTest();}

  public void testLength() {doTest();}

  public void testExists() {doTest();}

  public void testUndef() {doTest();}

  public void testEval() {doTest();}

  public void testGoto() {doTest();}

  public void testRedo() {doTest();}

  public void testNext() {doTest();}

  public void testLast() {doTest();}

  public void testReturn() {doTest();}

  public void testExit() {doTest();}

  public void testReverse() {doTest();}

  public void testWarn() {doTest();}

  public void testDie() {doTest();}

  @NotNull
  protected String getCodeFromName(@NotNull String name) {
    return name + "(<caret>);";
  }

  private void doTest() {
    String name = getTestName(true);
    initWithTextSmartWithoutErrors(getCodeFromName(name));
    doTestParameterInfoWithoutInit();
  }
}
