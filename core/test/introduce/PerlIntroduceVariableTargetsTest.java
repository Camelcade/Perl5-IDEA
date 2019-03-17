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

package introduce;

import base.PerlLightTestCase;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.idea.refactoring.PerlIntroduceTarget;
import com.perl5.lang.perl.idea.refactoring.PerlIntroduceVariableHandler;

import java.util.List;

public class PerlIntroduceVariableTargetsTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/introduce/targets";
  }

  public void testAnd() {doTest();}

  public void testBitwiseAnd() {doTest();}

  public void testBitwiseOr() {doTest();}

  public void testCondition() {doTest();}

  public void testConstructorCall() {doTest();}

  public void testDereference() {doTest();}

  public void testIfModifier() {doTest();}

  public void testLpAnd() {doTest();}

  public void testLpOr() {doTest();}

  public void testMinus() {doTest();}

  public void testMul() {doTest();}

  public void testOr() {doTest();}

  public void testPlainStringQ() {doTest();}

  public void testPlainStringQQ() {doTest();}

  public void testPlainStringQQWithVariable() {doTest();}

  public void testPlainStringQWithVariable() {doTest();}

  public void testPlainStringQX() {doTest();}

  public void testPlainStringQXWithVariable() {doTest();}

  public void testPlus() {doTest();}

  public void testQqString() {doTest();}

  public void testQqStringWithVariable() {doTest();}

  public void testQString() {doTest();}

  public void testQStringWithVariable() {doTest();}

  public void testQxString() {doTest();}

  public void testQxStringWithVariable() {doTest();}

  public void testQwList() {doTest();}

  public void testQwListWithVariable() {doTest();}

  public void testShift() {doTest();}


  private void doTest() {
    initWithFileSmartWithoutErrors();
    PerlIntroduceVariableHandler introduceVariableHandler = new PerlIntroduceVariableHandler();
    List<PerlIntroduceTarget> introduceTargets = introduceVariableHandler.computeIntroduceTargets(getEditor(), getFile());
    StringBuilder sb = new StringBuilder();

    introduceTargets.forEach(it -> sb.append(serializePsiElement(it.getPlace()))
      .append("\n")
      .append("    ").append(it.getTextRangeInElement())
      .append("\n")
      .append("    '").append(it.render()).append("'")
      .append("\n"));

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }
}
