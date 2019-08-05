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

package unit.perl;


import base.PerlLightTestCase;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import org.junit.Test;
public class PerlControlFlowTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/unit/perl/controlFlow";
  }

  @Test
  public void testSwitch() {
    PerlSharedSettings.getInstance(getProject()).PERL_SWITCH_ENABLED = true;
    doTest();
  }

  @Test
  public void testVariableInAnonSub() {doTest();}

  @Test
  public void testVariableInAnonSubAfter() {doTest();}

  @Test
  public void testVariableInAnonSubAround() {doTest();}

  @Test
  public void testVariableInAnonSubAugment() {doTest();}

  @Test
  public void testVariableInAnonSubBefore() {doTest();}

  @Test
  public void testVariableInAnonSubOverride() {doTest();}

  @Test
  public void testInterpolationArray() {
    doTest();
  }

  @Test
  public void testInterpolationConst() {
    doTest();
  }

  @Test
  public void testInterpolationConstantHeredoc() {
    doTest();
  }

  @Test
  public void testInterpolationConstantHeredocSecond() {
    doTest();
  }

  @Test
  public void testInterpolationHeredoc() {
    doTest();
  }

  @Test
  public void testInterpolationString() {
    doTest();
  }

  @Test
  public void testIssue2073() {doTest();}

  @Test
  public void testMethodFirst() {doTest();}

  @Test
  public void testMethodExplicitInvocant() {doTest();}

  @Test
  public void testSubSignatureThird() {doTest();}

  @Test
  public void testSubSignatureThirdAfterSkipped() {doTest();}

  @Test
  public void testSubSignatureThirdDefault() {doTest();}

  @Test
  public void testPopValue() {doTest();}

  @Test
  public void testShiftValue() {doTest();}

  @Test
  public void testPushValue() {doTest();}

  @Test
  public void testPushValueParenthesized() {doTest();}

  @Test
  public void testUnshiftValue() {doTest();}

  @Test
  public void testUnshiftValueParenthesized() {doTest();}

  @Test
  public void testBlessExpr() {doTest();}

  @Test
  public void testSpliceExpr() {doTest();}

  @Test
  public void testCrossSubVariableDynamic() {doTest();}

  @Test
  public void testDefinedExpr() {doTest();}

  @Test
  public void testKeysExpr() {doTest();}

  @Test
  public void testValuesExpr() {doTest();}

  @Test
  public void testEachExpr() {doTest();}

  @Test
  public void testMissingVariable2() {doTest();}

  @Test
  public void testMissingVariable() {doTest();}

  @Test
  public void testMyVariable() {doTest();}

  @Test
  public void testMyVariableAssignment() {doTest();}

  @Test
  public void testMyVariableAssignmentInParens() {doTest();}

  @Test
  public void testDerefWithCodeRef() {doTest();}

  @Test
  public void testDerefWithVariableFirst() {doTest();}

  @Test
  public void testDerefWithVariableLast() {doTest();}

  @Test
  public void testDerefWithVariables() {doTest();}

  @Test
  public void testSubStringOrCall() {doTest();}

  @Test
  public void testAssignListToList() {doTest();}

  @Test
  public void testAssignListToScalar() {doTest();}

  @Test
  public void testAssignListToScalarParallel() {doTest();}

  @Test
  public void testAssignScalarToList() {doTest();}

  @Test
  public void testAssignStringListToList() {doTest();}

  @Test
  public void testAssignStringListToScalar() {doTest();}

  @Test
  public void testAssignStringListToScalarParallel() {doTest();}

  @Test
  public void testLpLogicAfterComma() {doTest();}

  @Test
  public void testReturnConfessIf() {doTest();}

  @Test
  public void testPushLpAndNext() {doTest();}

  @Test
  public void testPushLpOrNext() {doTest();}

  @Test
  public void testPushLpXorNext() {doTest();}

  @Test
  public void testGotoAfterAnonSub() {doTest();}

  @Test
  public void testGotoFromInnerEval() {doTest();}

  @Test
  public void testGotoFromInnerSub() {doTest();}

  @Test
  public void testGotoFromInnerFunc() {doTest();}

  @Test
  public void testGotoFromInnerMethod() {doTest();}

  @Test
  public void testExitFromInnerSub() {doTest();}

  @Test
  public void testExitFromInnerFunc() {doTest();}

  @Test
  public void testExitFromInnerMethod() {doTest();}

  @Test
  public void testDeleteInList() {doTest();}

  @Test
  public void testNextLabeled() {doTest();}

  @Test
  public void testLastLabeled() {doTest();}

  @Test
  public void testRedoLabeled() {doTest();}

  @Test
  public void testGotoExpr() {doTest();}

  @Test
  public void testGotoLabel() {doTest();}

  @Test
  public void testGotoLabelDouble() {doTest();}

  @Test
  public void testGotoRef() {doTest();}

  @Test
  public void testUnreachableDieWithHeredoc() {doTest();}

  @Test
  public void testUnreachableDbReturn() {doTest();}

  @Test
  public void testUnreachableInnerSub() {doTest();}

  @Test
  public void testSubExprLazyParsable() {doTest();}

  @Test
  public void testCustomDieDeref() {doTest();}

  @Test
  public void testCommaSequenceWithReturn() {doTest();}

  @Test
  public void testCommaSequenceWithReturnConditional() {doTest();}

  @Test
  public void testCommaSequenceWithReturnParens() {doTest();}

  @Test
  public void testCommaSequenceWithReturnParensConditional() {doTest();}

  @Test
  public void testDieDoSub() {doTest();}

  @Test
  public void testExitFor() {doTest();}

  @Test
  public void testRedoAnonSub() {doTest();}

  @Test
  public void testRedoBlock() {doTest();}

  @Test
  public void testRedoBlockWithContinue() {doTest();}

  @Test
  public void testRedoBlockWithContinueFromContinue() {doTest();}

  @Test
  public void testRedoDoFor() {doTest();}

  @Test
  public void testRedoEvalFor() {doTest();}

  @Test
  public void testRedoFor() {doTest();}

  @Test
  public void testRedoForeach() {doTest();}

  @Test
  public void testRedoForeachModifier() {doTest();}

  @Test
  public void testRedoForeachWithContinue() {doTest();}

  @Test
  public void testRedoForeachWithContinueFromContinue() {doTest();}

  @Test
  public void testRedoForNested() {doTest();}

  @Test
  public void testRedoFunc() {doTest();}

  @Test
  public void testRedoGrep() {doTest();}

  @Test
  public void testRedoMap() {doTest();}

  @Test
  public void testRedoMethod() {doTest();}

  @Test
  public void testRedoSort() {doTest();}

  @Test
  public void testRedoSub() {doTest();}

  @Test
  public void testRedoUntil() {doTest();}

  @Test
  public void testRedoUntilWithContinue() {doTest();}

  @Test
  public void testRedoUntilWithContinueFromContinue() {doTest();}

  @Test
  public void testRedoWhile() {doTest();}

  @Test
  public void testRedoWhileWithContinue() {doTest();}

  @Test
  public void testRedoWhileWithContinueFromContinue() {doTest();}

  @Test
  public void testLastAnonSub() {doTest();}

  @Test
  public void testLastBlock() {doTest();}

  @Test
  public void testLastBlockWithContinue() {doTest();}

  @Test
  public void testLastBlockWithContinueFromContinue() {doTest();}

  @Test
  public void testLastDoFor() {doTest();}

  @Test
  public void testLastEvalFor() {doTest();}

  @Test
  public void testLastEval() {doTest();}

  @Test
  public void testLastEvalWithLabel() {doTest();}

  @Test
  public void testLastFor() {doTest();}

  @Test
  public void testLastForeach() {doTest();}

  @Test
  public void testLastForeachModifier() {doTest();}

  @Test
  public void testLastForeachWithContinue() {doTest();}

  @Test
  public void testLastForeachWithContinueFromContinue() {doTest();}

  @Test
  public void testLastForNested() {doTest();}

  @Test
  public void testLastFunc() {doTest();}

  @Test
  public void testLastGrep() {doTest();}

  @Test
  public void testLastMap() {doTest();}

  @Test
  public void testLastMethod() {doTest();}

  @Test
  public void testLastSort() {doTest();}

  @Test
  public void testLastSub() {doTest();}

  @Test
  public void testLastUntil() {doTest();}

  @Test
  public void testLastUntilWithContinue() {doTest();}

  @Test
  public void testLastUntilWithContinueFromContinue() {doTest();}

  @Test
  public void testLastWhile() {doTest();}

  @Test
  public void testLastWhileWithContinue() {doTest();}

  @Test
  public void testLastWhileWithContinueFromContinue() {doTest();}

  @Test
  public void testBlockContinue() {doTest();}

  @Test
  public void testNextGrep() {doTest();}

  @Test
  public void testNextMap() {doTest();}

  @Test
  public void testNextSort() {doTest();}

  @Test
  public void testNextSub() {doTest();}

  @Test
  public void testNextMethod() {doTest();}

  @Test
  public void testNextFunc() {doTest();}

  @Test
  public void testNextAnonSub() {doTest();}

  @Test
  public void testNextDoFor() {doTest();}

  @Test
  public void testNextEvalFor() {doTest();}

  @Test
  public void testNextForNested() {doTest();}

  @Test
  public void testNextForeach() {doTest();}

  @Test
  public void testNextForeachWithContinue() {doTest();}

  @Test
  public void testNextForeachWithContinueFromContinue() {doTest();}

  @Test
  public void testNextFor() {doTest();}

  @Test
  public void testNextWhile() {doTest();}

  @Test
  public void testNextWhileWithContinue() {doTest();}

  @Test
  public void testNextWhileWithContinueFromContinue() {doTest();}

  @Test
  public void testNextUntil() {doTest();}

  @Test
  public void testNextUntilWithContinue() {doTest();}

  @Test
  public void testNextUntilWithContinueFromContinue() {doTest();}

  @Test
  public void testNextBlock() {doTest();}

  @Test
  public void testNextBlockWithContinue() {doTest();}

  @Test
  public void testNextBlockWithContinueFromContinue() {doTest();}

  @Test
  public void testNextForeachModifier() {doTest();}

  @Test
  public void testRegexpEval() {doTest();}

  @Test
  public void testRegexpReplace() {doTest();}

  @Test
  public void testHeredocQQSequence() {doTest();}

  @Test
  public void testHeredocQQAfterEmpty() {doTest();}

  @Test
  public void testHeredocQQ() {doTest();}

  @Test
  public void testHeredocQ() {doTest();}

  @Test
  public void testHeredocQX() {doTest();}

  @Test
  public void testAnonSubAsParameter() {doTest();}

  @Test
  public void testAssignChainParallel() {doTest();}

  @Test
  public void testAssignDeclarationSimple() {doTest();}

  @Test
  public void testAssignDeclarationParens() {doTest();}

  @Test
  public void testAssignDeclarationMulti() {doTest();}

  @Test
  public void testSimpleRead() {doTest();}

  @Test
  public void testAssignSimple() {doTest();}

  @Test
  public void testAssignParallel() {doTest();}

  @Test
  public void testAssignChain() {doTest();}

  @Test
  public void testAssignExpressions() {doTest();}

  @Test
  public void testStatementModifierFor() {doTest();}

  @Test
  public void testStatementModifierForeach() {doTest();}

  @Test
  public void testStatementModifierIf() {doTest();}

  @Test
  public void testStatementModifierUnless() {doTest();}

  @Test
  public void testStatementModifierUntil() {doTest();}

  @Test
  public void testStatementModifierWhen() {doTest();}

  @Test
  public void testStatementModifierWhile() {doTest();}

  @Test
  public void testPrintExpression() {doTest();}

  @Test
  public void testElementsAndSlices() {doTest();}

  @Test
  public void testCallExpression() {doTest();}

  @Test
  public void testReturnAnonSub() {doTest();}

  @Test
  public void testReturnEmpty() {doTest();}

  @Test
  public void testReturnEval() {doTest();}

  @Test
  public void testReturnDo() {doTest();}

  @Test
  public void testReturnResult() {doTest();}

  @Test
  public void testReturnSort() {doTest();}

  @Test
  public void testReturnSub() {doTest();}

  @Test
  public void testReturnMethod() {doTest();}

  @Test
  public void testReturnFunc() {doTest();}

  /*
  @Test
    public void testReturnTry() {doTest();}

  @Test
    public void testReturnCatch() {doTest();}

  @Test
    public void testReturnFinally() {doTest();}
  */

  @Test
  public void testBinaryExpressions() {doTest();}

  @Test
  public void testExprAnd() {doTest();}

  @Test
  public void testExprAndLp() {doTest();}

  @Test
  public void testExprOr() {doTest();}

  @Test
  public void testExprOrDefined() {doTest();}

  @Test
  public void testExprOrLp() {doTest();}

  @Test
  public void testExprTernary() {doTest();}

  @Test
  public void testHeredocs() {doTest();}

  @Test
  public void testReturnOrSomething() {doTest();}

  @Test
  public void testDieSub() {doTest();}

  @Test
  public void testCroakAnonSub() {doTest();}

  @Test
  public void testConfessEval() {doTest();}

  @Test
  public void testDieDo() {doTest();}

  @Test
  public void testWhileCompound() {doTest();}

  @Test
  public void testWhileCompoundWithContinue() {doTest();}

  @Test
  public void testUntilCompound() {doTest();}

  @Test
  public void testUntilCompoundWithContinue() {doTest();}

  @Test
  public void testIfCompound() {doTest();}

  @Test
  public void testUnlessCompound() {doTest();}

  @Test
  public void testForIterator() {doTest();}

  @Test
  public void testForIteratorWithContinue() {doTest();}

  @Test
  public void testForIteratorWithoutIterator() {doTest();}

  @Test
  public void testNestedModifier() {doTest();}

  @Test
  public void testGrepExpr() {doTest();}

  @Test
  public void testGrepWithArg() {doTest();}

  @Test
  public void testGrepWithBlock() {doTest();}

  @Test
  public void testSortExpr() {doTest();}

  @Test
  public void testSortWithArg() {doTest();}

  @Test
  public void testSortWithSubName() {doTest();}

  @Test
  public void testSortWithBlock() {doTest();}

  @Test
  public void testMapExpr() {doTest();}

  @Test
  public void testMapWithArg() {doTest();}

  @Test
  public void testMapWithBlock() {doTest();}

  @Test
  public void testDoWhile() {doTest();}

  @Test
  public void testDoUntil() {doTest();}

  @Test
  public void testForIndexedEmpty() {doTest();}

  @Test
  public void testForIndexedFull() {doTest();}

  @Test
  public void testForIndexedNoAlteration() {doTest();}

  @Test
  public void testForIndexedNoCondition() {doTest();}

  @Test
  public void testForIndexedNoInit() {doTest();}

  @Test
  public void testTryCompound() {doTest();}

  @Test
  public void testTryCatchOrInFor() {doTest();}

  @Test
  public void testTryCatchCompound() {doTest();}

  @Test
  public void testTry() {doTest();}

  @Test
  public void testTryCatch() {doTest();}

  @Test
  public void testTryCatchCatch() {doTest();}

  @Test
  public void testTryCatchCatchConditional() {doTest();}

  @Test
  public void testTryCatchCatchFinally() {doTest();}

  @Test
  public void testTryCatchConditional() {doTest();}

  @Test
  public void testTryCatchFinally() {doTest();}

  @Test
  public void testTryCatchOtherwiseFinally() {doTest();}

  @Test
  public void testTryExceptCatchFinally() {doTest();}

  @Test
  public void testTryFinally() {doTest();}

  private void doTest() {doTestControlFlow();}
}
