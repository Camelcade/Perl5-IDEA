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

package codeInsight.perl;

import base.PerlLightTestCase;

public class PerlControlFlowTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/controlFlow/perl";
  }

  public void testKeysExpr() {doTest();}

  public void testValuesExpr() {doTest();}

  public void testEachExpr() {doTest();}

  public void testMissingVariable2() {doTest();}

  public void testMissingVariable() {doTest();}

  public void testMyVariable() {doTest();}

  public void testMyVariableAssignment() {doTest();}

  public void testMyVariableAssignmentInParens() {doTest();}

  public void testDerefWithCodeRef() {doTest();}

  public void testDerefWithVariableFirst() {doTest();}

  public void testDerefWithVariableLast() {doTest();}

  public void testDerefWithVariables() {doTest();}

  public void testSubStringOrCall() {doTest();}

  public void testAssignListToList() {doTest();}

  public void testAssignListToScalar() {doTest();}

  public void testAssignListToScalarParallel() {doTest();}

  public void testAssignScalarToList() {doTest();}

  public void testAssignStringListToList() {doTest();}

  public void testAssignStringListToScalar() {doTest();}

  public void testAssignStringListToScalarParallel() {doTest();}

  public void testLpLogicAfterComma() {doTest();}

  public void testReturnConfessIf() {doTest();}

  public void testPushLpAndNext() {doTest();}

  public void testPushLpOrNext() {doTest();}

  public void testPushLpXorNext() {doTest();}

  public void testGotoAfterAnonSub() {doTest();}

  public void testGotoFromInnerEval() {doTest();}

  public void testGotoFromInnerSub() {doTest();}

  public void testGotoFromInnerFunc() {doTest();}

  public void testGotoFromInnerMethod() {doTest();}

  public void testExitFromInnerSub() {doTest();}

  public void testExitFromInnerFunc() {doTest();}

  public void testExitFromInnerMethod() {doTest();}

  public void testDeleteInList() {doTest();}

  public void testNextLabeled() {doTest();}

  public void testLastLabeled() {doTest();}

  public void testRedoLabeled() {doTest();}

  public void testGotoExpr() {doTest();}

  public void testGotoLabel() {doTest();}

  public void testGotoLabelDouble() {doTest();}

  public void testGotoRef() {doTest();}

  public void testUnreachableDieWithHeredoc() {doTest();}

  public void testUnreachableDbReturn() {doTest();}

  public void testUnreachableInnerSub() {doTest();}

  public void testSubExprLazyParsable() {doTest();}

  public void testCustomDieDeref() {doTest();}

  public void testCommaSequenceWithReturn() {doTest();}

  public void testCommaSequenceWithReturnConditional() {doTest();}

  public void testCommaSequenceWithReturnParens() {doTest();}

  public void testCommaSequenceWithReturnParensConditional() {doTest();}

  public void testDieDoSub() {doTest();}

  public void testExitFor() {doTest();}

  public void testRedoAnonSub() {doTest();}

  public void testRedoBlock() {doTest();}

  public void testRedoBlockWithContinue() {doTest();}

  public void testRedoBlockWithContinueFromContinue() {doTest();}

  public void testRedoDoFor() {doTest();}

  public void testRedoEvalFor() {doTest();}

  public void testRedoFor() {doTest();}

  public void testRedoForeach() {doTest();}

  public void testRedoForeachModifier() {doTest();}

  public void testRedoForeachWithContinue() {doTest();}

  public void testRedoForeachWithContinueFromContinue() {doTest();}

  public void testRedoForNested() {doTest();}

  public void testRedoFunc() {doTest();}

  public void testRedoGrep() {doTest();}

  public void testRedoMap() {doTest();}

  public void testRedoMethod() {doTest();}

  public void testRedoSort() {doTest();}

  public void testRedoSub() {doTest();}

  public void testRedoUntil() {doTest();}

  public void testRedoUntilWithContinue() {doTest();}

  public void testRedoUntilWithContinueFromContinue() {doTest();}

  public void testRedoWhile() {doTest();}

  public void testRedoWhileWithContinue() {doTest();}

  public void testRedoWhileWithContinueFromContinue() {doTest();}

  public void testLastAnonSub() {doTest();}

  public void testLastBlock() {doTest();}

  public void testLastBlockWithContinue() {doTest();}

  public void testLastBlockWithContinueFromContinue() {doTest();}

  public void testLastDoFor() {doTest();}

  public void testLastEvalFor() {doTest();}

  public void testLastEval() {doTest();}

  public void testLastEvalWithLabel() {doTest();}

  public void testLastFor() {doTest();}

  public void testLastForeach() {doTest();}

  public void testLastForeachModifier() {doTest();}

  public void testLastForeachWithContinue() {doTest();}

  public void testLastForeachWithContinueFromContinue() {doTest();}

  public void testLastForNested() {doTest();}

  public void testLastFunc() {doTest();}

  public void testLastGrep() {doTest();}

  public void testLastMap() {doTest();}

  public void testLastMethod() {doTest();}

  public void testLastSort() {doTest();}

  public void testLastSub() {doTest();}

  public void testLastUntil() {doTest();}

  public void testLastUntilWithContinue() {doTest();}

  public void testLastUntilWithContinueFromContinue() {doTest();}

  public void testLastWhile() {doTest();}

  public void testLastWhileWithContinue() {doTest();}

  public void testLastWhileWithContinueFromContinue() {doTest();}

  public void testBlockContinue() {doTest();}

  public void testNextGrep() {doTest();}

  public void testNextMap() {doTest();}

  public void testNextSort() {doTest();}

  public void testNextSub() {doTest();}

  public void testNextMethod() {doTest();}

  public void testNextFunc() {doTest();}

  public void testNextAnonSub() {doTest();}

  public void testNextDoFor() {doTest();}

  public void testNextEvalFor() {doTest();}

  public void testNextForNested() {doTest();}

  public void testNextForeach() {doTest();}

  public void testNextForeachWithContinue() {doTest();}

  public void testNextForeachWithContinueFromContinue() {doTest();}

  public void testNextFor() {doTest();}

  public void testNextWhile() {doTest();}

  public void testNextWhileWithContinue() {doTest();}

  public void testNextWhileWithContinueFromContinue() {doTest();}

  public void testNextUntil() {doTest();}

  public void testNextUntilWithContinue() {doTest();}

  public void testNextUntilWithContinueFromContinue() {doTest();}

  public void testNextBlock() {doTest();}

  public void testNextBlockWithContinue() {doTest();}

  public void testNextBlockWithContinueFromContinue() {doTest();}

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

  public void testReturnDo() {doTest();}

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

  public void testTryCatchOrInFor() {doTest();}

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
