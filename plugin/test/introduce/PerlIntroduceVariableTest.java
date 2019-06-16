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
import com.intellij.application.options.CodeStyle;
import com.perl5.lang.perl.PerlLanguage;
import org.junit.Test;

import static com.intellij.psi.codeStyle.CommonCodeStyleSettings.WRAP_AS_NEEDED;
public class PerlIntroduceVariableTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/introduce/full";
  }

  @Test
  public void testParenthesizedExpr() {doTest();}

  @Test
  public void testParenthesizedExprWithParens() {doTest();}

  @Test
  public void testParenthesizedList() {doTest();}

  @Test
  public void testParenthesizedValue() {doTest();}

  @Test
  public void testStringListInplace() {doTest();}

  @Test
  public void testArraySlice() {doTest();}

  @Test
  public void testHashSlice() {doTest();}

  @Test
  public void testGrep() {doTest();}

  @Test
  public void testMap() {doTest();}

  @Test
  public void testSort() {doTest();}

  @Test
  public void testDo() {doTest();}

  @Test
  public void testEval() {doTest();}

  @Test
  public void testAnonSub() {doTest();}

  @Test
  public void testAnonSubCall() {doTest();}

  @Test
  public void testRegexExprMatch() {doTest();}

  @Test
  public void testRegexExprReplace() {doTest();}

  @Test
  public void testRegexExprTr() {doTest();}

  @Test
  public void testRegexExprY() {doTest();}

  @Test
  public void testRegexMatch() {doTest();}

  @Test
  public void testRegexReplaceSimple() {doTest();}

  @Test
  public void testOver200occurrences() {doTest();}

  @Test
  public void testOver1000occurrences() {doTest();}

  @Test
  public void testHeredocPartFromString() {doTest();}

  @Test
  public void testStringFromHeredoc() {doTest();}

  @Test
  public void testStringFromHeredocPartial() {doTest();}

  @Test
  public void testStringFromHeredocPartialWithVariable() {doTest();}

  @Test
  public void testStringQFromHeredoc() {doTest();}

  @Test
  public void testStringQFromHeredocSelection() {doTest();}

  @Test
  public void testHeredocBodyQQWithDoubleQuote() {doTest();}

  @Test
  public void testHeredocBodyQQWithDoubleQuoteAndCloseAngle() {doTest();}

  @Test
  public void testHeredocBodyQQWithDoubleQuoteAndAll() {doTest();}

  @Test
  public void testHeredocBodyQQVariable() {doTest();}

  @Test
  public void testHeredocBodyQQVariableMulti() {doTest();}

  @Test
  public void testHeredocBodyQQVariableBraced() {doTest();}

  @Test
  public void testHeredocBodyQQVariableNested() {doTest();}

  @Test
  public void testHeredocBodyQQVariableSelection() {doTest();}

  @Test
  public void testHeredocBodyQQVariableSelectionAfter() {doTest();}

  @Test
  public void testHeredocBodyQQVariableSelectionAround() {doTest();}

  @Test
  public void testHeredocBodyQQVariableSelectionBefore() {doTest();}

  @Test
  public void testHeredocBodyQXVariable() {doTest();}

  @Test
  public void testVariableInsideStringPartialEnd() {doTest();}

  @Test
  public void testVariableInsideStringPartialEndLazy() {doTest();}

  @Test
  public void testVariableInsideStringPartialEndLazyQQ() {doTest();}

  @Test
  public void testVariableInsideStringPartialEndLazyQX() {doTest();}

  @Test
  public void testVariableInsideStringPartialEndQQ() {doTest();}

  @Test
  public void testVariableInsideStringPartialEndQX() {doTest();}

  @Test
  public void testVariableInsideStringPartialMid() {doTest();}

  @Test
  public void testVariableInsideStringPartialMidLazy() {doTest();}

  @Test
  public void testVariableInsideStringPartialMidQQ() {doTest();}

  @Test
  public void testVariableInsideStringPartialMidQQLazy() {doTest();}

  @Test
  public void testVariableInsideStringPartialMidQX() {doTest();}

  @Test
  public void testVariableInsideStringPartialMidQXLazy() {doTest();}

  @Test
  public void testVariableInsideStringPartialStart() {doTest();}

  @Test
  public void testVariableInsideStringPartialStartLazy() {doTest();}

  @Test
  public void testVariableInsideStringPartialStartQQ() {doTest();}

  @Test
  public void testVariableInsideStringPartialStartQQLazy() {doTest();}

  @Test
  public void testVariableInsideStringPartialStartQX() {doTest();}

  @Test
  public void testVariableInsideStringPartialStartQXLazy() {doTest();}

  @Test
  public void testVariableLeftStringPartialEnd() {doTest();}

  @Test
  public void testVariableLeftStringPartialEndLazy() {doTest();}

  @Test
  public void testVariableLeftStringPartialEndLazyQQ() {doTest();}

  @Test
  public void testVariableLeftStringPartialEndLazyQX() {doTest();}

  @Test
  public void testVariableLeftStringPartialEndQQ() {doTest();}

  @Test
  public void testVariableLeftStringPartialEndQX() {doTest();}

  @Test
  public void testVariableLeftStringPartialMid() {doTest();}

  @Test
  public void testVariableLeftStringPartialMidLazy() {doTest();}

  @Test
  public void testVariableLeftStringPartialMidQQ() {doTest();}

  @Test
  public void testVariableLeftStringPartialMidQQLazy() {doTest();}

  @Test
  public void testVariableLeftStringPartialMidQX() {doTest();}

  @Test
  public void testVariableLeftStringPartialMidQXLazy() {doTest();}

  @Test
  public void testVariableLeftStringPartialStart() {doTest();}

  @Test
  public void testVariableLeftStringPartialStartLazy() {doTest();}

  @Test
  public void testVariableLeftStringPartialStartQQ() {doTest();}

  @Test
  public void testVariableLeftStringPartialStartQQLazy() {doTest();}

  @Test
  public void testVariableLeftStringPartialStartQX() {doTest();}

  @Test
  public void testVariableLeftStringPartialStartQXLazy() {doTest();}

  @Test
  public void testVariableRightStringPartialEnd() {doTest();}

  @Test
  public void testVariableRightStringPartialEndLazy() {doTest();}

  @Test
  public void testVariableRightStringPartialEndLazyQQ() {doTest();}

  @Test
  public void testVariableRightStringPartialEndLazyQX() {doTest();}

  @Test
  public void testVariableRightStringPartialEndQQ() {doTest();}

  @Test
  public void testVariableRightStringPartialEndQX() {doTest();}

  @Test
  public void testVariableRightStringPartialMid() {doTest();}

  @Test
  public void testVariableRightStringPartialMidLazy() {doTest();}

  @Test
  public void testVariableRightStringPartialMidQQ() {doTest();}

  @Test
  public void testVariableRightStringPartialMidQQLazy() {doTest();}

  @Test
  public void testVariableRightStringPartialMidQX() {doTest();}

  @Test
  public void testVariableRightStringPartialMidQXLazy() {doTest();}

  @Test
  public void testVariableRightStringPartialStart() {doTest();}

  @Test
  public void testVariableRightStringPartialStartLazy() {doTest();}

  @Test
  public void testVariableRightStringPartialStartQQ() {doTest();}

  @Test
  public void testVariableRightStringPartialStartQQLazy() {doTest();}

  @Test
  public void testVariableRightStringPartialStartQX() {doTest();}

  @Test
  public void testVariableRightStringPartialStartQXLazy() {doTest();}

  @Test
  public void testAddMultiLong() {doTest();}

  @Test
  public void testAddMultiLongWithWrap() {
    CodeStyle.getSettings(getProject()).getCommonSettings(PerlLanguage.INSTANCE).BINARY_OPERATION_WRAP = WRAP_AS_NEEDED;
    doTest();
  }

  @Test
  public void testStringPartialEnd() {doTest();}

  @Test
  public void testStringPartialEndLazy() {doTest();}

  @Test
  public void testStringPartialEndLazyQQ() {doTest();}

  @Test
  public void testStringPartialEndLazyQX() {doTest();}

  @Test
  public void testStringPartialEndQQ() {doTest();}

  @Test
  public void testStringPartialEndQX() {doTest();}

  @Test
  public void testStringPartialMid() {doTest();}

  @Test
  public void testStringPartialMidLazy() {doTest();}

  @Test
  public void testStringPartialMidQQ() {doTest();}

  @Test
  public void testStringPartialMidQQLazy() {doTest();}

  @Test
  public void testStringPartialMidQX() {doTest();}

  @Test
  public void testStringPartialMidQXLazy() {doTest();}

  @Test
  public void testStringPartialStart() {doTest();}

  @Test
  public void testStringPartialStartLazy() {doTest();}

  @Test
  public void testStringPartialStartQQ() {doTest();}

  @Test
  public void testStringPartialStartQQLazy() {doTest();}

  @Test
  public void testStringPartialStartQX() {doTest();}

  @Test
  public void testStringPartialStartQXLazy() {doTest();}

  @Test
  public void testStringQOp() {doTest();}

  @Test
  public void testStringQPlain() {doTest();}

  @Test
  public void testStringQqOp() {doTest();}

  @Test
  public void testStringQqPlain() {doTest();}

  @Test
  public void testStringQxOp() {doTest();}

  @Test
  public void testStringQxPlain() {doTest();}

  @Test
  public void testSubCall() {doTest();}

  @Test
  public void testStringPartialLimits() {doTest();}

  @Test
  public void testStringFull() {doTest();}

  @Test
  public void testScalarBraced() {doTest();}

  @Test
  public void testScalarDerefBraced() {doTest();}

  @Test
  public void testScalarDerefUnbraced() {doTest();}

  @Test
  public void testScalarUnbraced() {doTest();}

  @Test
  public void testRegexMatchEmpty() {doTest();}

  @Test
  public void testRegexMatchExplicit() {doTest();}

  @Test
  public void testRegexMatchImplicit() {doTest();}

  @Test
  public void testRegexMatchLazy() {doTest();}

  @Test
  public void testRegexReplace() {doTest();}

  @Test
  public void testRegexReplaceLiteralQuotes() {doTest();}

  @Test
  public void testRegexReplaceLazy() {doTest();}

  @Test
  public void testListToQw() {doTest();}

  @Test
  public void testListToQwSame() {doTest();}

  @Test
  public void testMethodCallNoParens() {doTest();}

  @Test
  public void testMethodCallNotParenthesized() {doTest();}

  @Test
  public void testMethodCallParens() {doTest();}

  @Test
  public void testMethodCallParenthesized() {doTest();}

  @Test
  public void testNestedCallWithoutParens() {doTest();}

  @Test
  public void testNestedCallWithParens() {doTest();}

  @Test
  public void testPackageForms() {doTest();}

  @Test
  public void testPackageFormsReverse() {doTest();}

  @Test
  public void testPackageFormsString() {doTest();}

  @Test
  public void testPackageFormsStringQq() {doTest();}

  @Test
  public void testPackageFormsStringQx() {doTest();}

  @Test
  public void testPlusMinus() {doTest();}

  @Test
  public void testQwRange() {doTest();}

  @Test
  public void testQwRangeLazy() {doTest();}

  @Test
  public void testQwRangeScalar() {doTest();}

  @Test
  public void testQwRangeScalarLazy() {doTest();}

  @Test
  public void testStringFromStringList() {doTest();}

  @Test
  public void testListItemFromNestedList() {doTest();}

  @Test
  public void testStringBareSelection() {doTest();}

  @Test
  public void testAddMulti() {doTest();}

  @Test
  public void testAddMultiFromPart() {doTest();}

  @Test
  public void testAddMultiFromPartEnd() {doTest();}

  @Test
  public void testAddMultiFromPartWrong() {doTest();}

  @Test
  public void testStringListPartial() {doTest();}

  @Test
  public void testStringListElement() {doTest();}

  @Test
  public void testStringListElementWithQuote() {doTest();}

  @Test
  public void testGlobBraced() {doTest();}

  @Test
  public void testGlobDerefBraced() {doTest();}

  @Test
  public void testGlobDerefUnbraced() {doTest();}

  @Test
  public void testGlobUnbraced() {doTest();}

  @Test
  public void testHashBraced() {doTest();}

  @Test
  public void testHashDerefBraced() {doTest();}

  @Test
  public void testHashDerefUnbraced() {doTest();}

  @Test
  public void testHashUnbraced() {doTest();}

  @Test
  public void testListFull() {doTest();}

  @Test
  public void testListPartial() {doTest();}

  @Test
  public void testListItemSingle() {doTest();}

  @Test
  public void testListItemTwo() {doTest();}

  @Test
  public void testArrayBraced() {doTest();}

  @Test
  public void testArrayDerefBraced() {doTest();}

  @Test
  public void testArrayDerefUnbraced() {doTest();}

  @Test
  public void testArrayUnbraced() {doTest();}

  @Test
  public void testCodeBraced() {doTest();}

  @Test
  public void testCodeDerefBraced() {doTest();}

  @Test
  public void testCodeDerefUnbraced() {doTest();}

  @Test
  public void testCodeUnbraced() {doTest();}

  @Test
  public void testComparision() {doTest();}

  @Test
  public void testDerefSimple() {doTest();}

  @Test
  public void testDerefSizes() {doTest();}

  @Test
  public void testDerefStyles() {doTest();}

  @Test
  public void testScopeFromInner() { doTest(); }

  @Test
  public void testScopeInline() { doTest(); }

  @Test
  public void testScopeModifier() { doTest(); }

  @Test
  public void testScopeModifierNoInline() { doTest(); }

  @Test
  public void testScopeNewStatement() { doTest(); }

  private void doTest() {
    doTestIntroduceVariable();
  }
}
