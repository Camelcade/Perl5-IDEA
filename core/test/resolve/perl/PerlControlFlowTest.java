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

package resolve.perl;

import base.PerlLightTestCase;

public class PerlControlFlowTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/controlFlow/perl";
  }

  public void testNextDoFor() {doTest();}

  public void testNextEvalFor() {doTest();}

  public void testNextForNested() {doTest();}

  public void testNextForeach() {doTest();}

  public void testNextFor() {doTest();}

  public void testNextWhile() {doTest();}

  public void testNextUntil() {doTest();}

  public void testNextBlock(){doTest();}

  public void testNextForeachModifier() {doTest();}

  public void testRegexpEval() {doTest();}

  public void testRegexpReplace() {doTest();}

  public void testHeredocQQSequence() {doTest();}

  public void testHeredocQQAfterEmpty() {doTest();}

  public void testHeredocQQ() {doTest();}

  public void testHeredocQ() {doTest();}

  public void testHeredocQX() {doTest();}

  public void testAnonSubAsParameter() {doTest();}

  public void testAssignChainParallel() {doTest();}

  public void testAssignDeclarationSimple() {doTest();}

  public void testAssignDeclarationParens() {doTest();}

  public void testAssignDeclarationMulti() {doTest();}

  public void testSimpleRead() {doTest();}

  public void testAssignSimple() {doTest();}

  public void testAssignParallel() {doTest();}

  public void testAssignChain() {doTest();}

  public void testAssignExpressions() {doTest();}

  public void testStatementModifierFor() {doTest();}

  public void testStatementModifierForeach() {doTest();}

  public void testStatementModifierIf() {doTest();}

  public void testStatementModifierUnless() {doTest();}

  public void testStatementModifierUntil() {doTest();}

  public void testStatementModifierWhen() {doTest();}

  public void testStatementModifierWhile() {doTest();}

  public void testPrintExpression() {doTest();}

  public void testElementsAndSlices() {doTest();}

  public void testCallExpression() {doTest();}

  public void testReturnAnonSub() {doTest();}

  public void testReturnEmpty() {doTest();}

  public void testReturnEval() {doTest();}

  public void testReturnResult() {doTest();}

  public void testReturnSort() {doTest();}

  public void testReturnSub() {doTest();}

  public void testReturnMethod() {doTest();}

  public void testReturnFunc() {doTest();}

  /*
  public void testReturnTry() {doTest();}

  public void testReturnCatch() {doTest();}

  public void testReturnFinally() {doTest();}
  */

  public void testBinaryExpressions() {doTest();}

  public void testExprAnd() {doTest();}

  public void testExprAndLp() {doTest();}

  public void testExprOr() {doTest();}

  public void testExprOrDefined() {doTest();}

  public void testExprOrLp() {doTest();}

  public void testExprTernary() {doTest();}

  public void testHeredocs() {doTest();}

  public void testReturnOrSomething() {doTest();}

  public void testDieSub() {doTest();}

  public void testCroakAnonSub() {doTest();}

  public void testConfessEval() {doTest();}

  public void testDieDo() {doTest();}

  public void testWhileCompound() {doTest();}

  public void testWhileCompoundWithContinue() {doTest();}

  public void testUntilCompound() {doTest();}

  public void testUntilCompoundWithContinue() {doTest();}

  public void testIfCompound() {doTest();}

  public void testUnlessCompound() {doTest();}

  public void testForIterator() {doTest();}

  public void testForIteratorWithContinue() {doTest();}

  public void testForIteratorWithoutIterator() {doTest();}

  public void testNestedModifier() {doTest();}

  public void testGrepExpr() {doTest();}

  public void testGrepWithArg() {doTest();}

  public void testGrepWithBlock() {doTest();}

  public void testSortExpr() {doTest();}

  public void testSortWithArg() {doTest();}

  public void testSortWithSubName() {doTest();}

  public void testSortWithBlock() {doTest();}

  public void testMapExpr() {doTest();}

  public void testMapWithArg() {doTest();}

  public void testMapWithBlock() {doTest();}

  public void testDoWhile() {doTest();}

  public void testDoUntil() {doTest();}

  public void testForIndexedEmpty() {doTest();}

  public void testForIndexedFull() {doTest();}

  public void testForIndexedNoAlteration() {doTest();}

  public void testForIndexedNoCondition() {doTest();}

  public void testForIndexedNoInit() {doTest();}

  public void testTryCompound() {doTest();}

  public void testTryCatchCompound() {doTest();}

  public void testTry() {doTest();}

  public void testTryCatch() {doTest();}

  public void testTryCatchCatch() {doTest();}

  public void testTryCatchCatchConditional() {doTest();}

  public void testTryCatchCatchFinally() {doTest();}

  public void testTryCatchConditional() {doTest();}

  public void testTryCatchFinally() {doTest();}

  public void testTryCatchOtherwiseFinally() {doTest();}

  public void testTryExceptCatchFinally() {doTest();}

  public void testTryFinally() {doTest();}


  private void doTest() {doTestControlFlow();}
}
