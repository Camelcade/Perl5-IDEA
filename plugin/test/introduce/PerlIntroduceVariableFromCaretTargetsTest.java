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

package introduce;


import base.PerlLightTestCase;
import org.junit.Test;
public class PerlIntroduceVariableFromCaretTargetsTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/introduce/targets/caret";
  }

  @Test
  public void testHeredocBodyQQVariableMulti() {doTest();}

  @Test
  public void testHeredocBody() {doTest();}

  @Test
  public void testHeredocBodyQQ() {doTest();}

  @Test
  public void testHeredocBodyQQVariable() {doTest();}

  @Test
  public void testHeredocBodyQQVariableNested() {doTest();}

  @Test
  public void testHeredocBodyQQVariableUnclosed() {doTest(false);}

  @Test
  public void testHeredocBodyQX() {doTest();}

  @Test
  public void testHeredocBodyQXVariable() {doTest();}

  @Test
  public void testHereDocOpener() {doTest();}

  @Test
  public void testHereDocOpenerInSequence() { doTest(); }

  @Test
  public void testQwElementInList() { doTest(); }

  @Test
  public void testPackageExpr() { doTest(); }

  @Test
  public void testRegexpWithModifiersCompile() {
    doTest();
  }

  @Test
  public void testRegexpWithModifiersMatch() {
    doTest();
  }

  @Test
  public void testRegexpWithModifiersReplace() {
    doTest();
  }

  @Test
  public void testRegexpCompileEmpty() { doTest(); }

  @Test
  public void testRegexpCompileNotEmpty() { doTest(); }

  @Test
  public void testRegexpCompileVariable() { doTest(); }

  @Test
  public void testRegexpMatchExplicitEmpty() {doTest();}

  @Test
  public void testRegexpMatchExplicitNotEmpty() {doTest();}

  @Test
  public void testRegexpMatchExplicitVariable() {doTest();}

  @Test
  public void testRegexpMatchImplicitEmpty() {doTest();}

  @Test
  public void testRegexpMatchImplicitNotEmpty() {doTest();}

  @Test
  public void testRegexpMatchImplicitVariable() {doTest();}

  @Test
  public void testRegexpReplaceBetween() {doTest();}

  @Test
  public void testRegexpReplaceEmpty() {doTest();}

  @Test
  public void testRegexpReplaceEmptyReplace() {doTest();}

  @Test
  public void testRegexpReplaceNotEmpty() {doTest();}

  @Test
  public void testRegexpReplaceNotEmptyReplace() {doTest();}

  @Test
  public void testRegexpReplaceNotEmptyReplaceEval() {doTest();}

  @Test
  public void testRegexpReplaceVariable() {doTest();}

  @Test
  public void testRegexpReplaceVariableReplace() {doTest();}

  @Test
  public void testRegexpTrBetween() {doTest();}

  @Test
  public void testRegexpTrEmpty() {doTest();}

  @Test
  public void testRegexpTrEmptyReplace() {doTest();}

  @Test
  public void testRegexpTrNotEmpty() {doTest();}

  @Test
  public void testRegexpTrNotEmptyReplace() {doTest();}

  @Test
  public void testRegexpTrVariable() {doTest();}

  @Test
  public void testRegexpTrVariableReplace() {doTest();}

  @Test
  public void testRegexpYBetween() {doTest();}

  @Test
  public void testRegexpYEmpty() {doTest();}

  @Test
  public void testRegexpYEmptyReplace() {doTest();}

  @Test
  public void testRegexpYNotEmpty() {doTest();}

  @Test
  public void testRegexpYNotEmptyReplace() {doTest();}

  @Test
  public void testRegexpYVariable() {doTest();}

  @Test
  public void testRegexpYVariableReplace() {doTest();}

  @Test
  public void testAnd() {doTest();}

  @Test
  public void testBitwiseAnd() {doTest();}

  @Test
  public void testBitwiseOr() {doTest();}

  @Test
  public void testCondition() {doTest();}

  @Test
  public void testConstructorCall() {doTest();}

  @Test
  public void testDeclarationLocal() {doTest();}

  @Test
  public void testDeclarationMy() {doTest();}

  @Test
  public void testDeclarationOur() {doTest();}

  @Test
  public void testDeclarationState() {doTest();}

  @Test
  public void testDereference() {doTest();}

  @Test
  public void testIfModifier() {doTest();}

  @Test
  public void testLpAnd() {doTest();}

  @Test
  public void testLpOr() {doTest();}

  @Test
  public void testMinus() {doTest();}

  @Test
  public void testMul() {doTest();}

  @Test
  public void testOr() {doTest();}

  @Test
  public void testParenthesizedArguments() {doTest();}

  @Test
  public void testParenthesizedExpr() {doTest();}

  @Test
  public void testPlainStringQ() {doTest();}

  @Test
  public void testPlainStringQQ() {doTest();}

  @Test
  public void testPlainStringQQWithVariable() {doTest();}

  @Test
  public void testPlainStringQWithVariable() {doTest();}

  @Test
  public void testPlainStringQX() {doTest();}

  @Test
  public void testPlainStringQXWithVariable() {doTest();}

  @Test
  public void testPlus() {doTest();}

  @Test
  public void testQqString() {doTest();}

  @Test
  public void testQqStringWithVariable() {doTest();}

  @Test
  public void testQString() {doTest();}

  @Test
  public void testQStringWithVariable() {doTest();}

  @Test
  public void testQxString() {doTest();}

  @Test
  public void testQxStringWithVariable() {doTest();}

  @Test
  public void testQwList() {doTest();}

  @Test
  public void testQwListLazy() {doTest();}

  @Test
  public void testQwListSingle() {doTest();}

  @Test
  public void testQwListFirst() {doTest();}

  @Test
  public void testQwListLast() {doTest();}

  @Test
  public void testQwListWithVariable() {doTest();}

  @Test
  public void testShift() {doTest();}

  protected void doTest() {
    doTest(true);
  }

  protected void doTest(boolean checkErrors) {
    doTestIntroduceVariableTargets(checkErrors);
  }
}
