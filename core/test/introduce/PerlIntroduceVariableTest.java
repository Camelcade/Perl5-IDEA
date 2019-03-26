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
import com.intellij.application.options.CodeStyle;
import com.perl5.lang.perl.PerlLanguage;

import static com.intellij.psi.codeStyle.CommonCodeStyleSettings.WRAP_AS_NEEDED;

public class PerlIntroduceVariableTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/introduce/full";
  }

  public void testAddMultiLong() {doTest();}

  public void testAddMultiLongWithWrap() {
    CodeStyle.getSettings(getProject()).getCommonSettings(PerlLanguage.INSTANCE).BINARY_OPERATION_WRAP = WRAP_AS_NEEDED;
    doTest();
  }

  public void testStringPartialEnd() {doTest();}

  public void testStringPartialEndLazy() {doTest();}

  public void testStringPartialEndLazyQQ() {doTest();}

  public void testStringPartialEndLazyQX() {doTest();}

  public void testStringPartialEndQQ() {doTest();}

  public void testStringPartialEndQX() {doTest();}

  public void testStringPartialMid() {doTest();}

  public void testStringPartialMidLazy() {doTest();}

  public void testStringPartialMidQQ() {doTest();}

  public void testStringPartialMidQQLazy() {doTest();}

  public void testStringPartialMidQX() {doTest();}

  public void testStringPartialMidQXLazy() {doTest();}

  public void testStringPartialStart() {doTest();}

  public void testStringPartialStartLazy() {doTest();}

  public void testStringPartialStartQQ() {doTest();}

  public void testStringPartialStartQQLazy() {doTest();}

  public void testStringPartialStartQX() {doTest();}

  public void testStringPartialStartQXLazy() {doTest();}

  public void testStringQOp() {doTest();}

  public void testStringQPlain() {doTest();}

  public void testStringQqOp() {doTest();}

  public void testStringQqPlain() {doTest();}

  public void testStringQxOp() {doTest();}

  public void testStringQxPlain() {doTest();}

  public void testSubCall() {doTest();}

  public void testStringPartialLimits() {doTest();}

  public void testStringFull() {doTest();}

  public void testScalarBraced() {doTest();}

  public void testScalarDerefBraced() {doTest();}

  public void testScalarDerefUnbraced() {doTest();}

  public void testScalarUnbraced() {doTest();}

  public void testRegexMatchEmpty() {doTest();}

  public void testRegexMatchExplicit() {doTest();}

  public void testRegexMatchImplicit() {doTest();}

  public void testRegexMatchLazy() {doTest();}

  public void testRegexReplace() {doTest();}

  public void testRegexReplaceLiteralQuotes() {doTest();}

  public void testRegexReplaceLazy() {doTest();}

  public void testListToQw() {doTest();}

  public void testListToQwSame() {doTest();}

  public void testMethodCallNoParens() {doTest();}

  public void testMethodCallNotParenthesized() {doTest();}

  public void testMethodCallParens() {doTest();}

  public void testMethodCallParenthesized() {doTest();}

  public void testNestedCallWithoutParens() {doTest();}

  public void testNestedCallWithParens() {doTest();}

  public void testPackageForms() {doTest();}

  public void testPackageFormsReverse() {doTest();}

  public void testPackageFormsString() {doTest();}

  public void testPackageFormsStringQq() {doTest();}

  public void testPackageFormsStringQx() {doTest();}

  public void testPlusMinus() {doTest();}

  public void testQwRange() {doTest();}

  public void testQwRangeLazy() {doTest();}

  public void testQwRangeScalar() {doTest();}

  public void testQwRangeScalarLazy() {doTest();}

  public void testStringFromStringList() {doTest();}

  public void testListItemFromNestedList() {doTest();}

  public void testStringBareSelection() {doTest();}

  public void testAddMulti() {doTest();}

  public void testAddMultiFromPart() {doTest();}

  public void testAddMultiFromPartEnd() {doTest();}

  public void testAddMultiFromPartWrong() {doTest();}

  public void testStringListPartial() {doTest();}

  public void testStringListElement() {doTest();}

  public void testStringListElementWithQuote() {doTest();}

  public void testGlobBraced() {doTest();}

  public void testGlobDerefBraced() {doTest();}

  public void testGlobDerefUnbraced() {doTest();}

  public void testGlobUnbraced() {doTest();}

  public void testHashBraced() {doTest();}

  public void testHashDerefBraced() {doTest();}

  public void testHashDerefUnbraced() {doTest();}

  public void testHashUnbraced() {doTest();}

  public void testListFull() {doTest();}

  public void testListPartial() {doTest();}

  public void testListItemSingle() {doTest();}

  public void testListItemTwo() {doTest();}

  public void testArrayBraced() {doTest();}

  public void testArrayDerefBraced() {doTest();}

  public void testArrayDerefUnbraced() {doTest();}

  public void testArrayUnbraced() {doTest();}

  public void testCodeBraced() {doTest();}

  public void testCodeDerefBraced() {doTest();}

  public void testCodeDerefUnbraced() {doTest();}

  public void testCodeUnbraced() {doTest();}

  public void testComparision() {doTest();}

  public void testDerefSimple() {doTest();}

  public void testDerefSizes() {doTest();}

  public void testDerefStyles() {doTest();}

  public void testScopeFromInner() { doTest(); }

  public void testScopeInline() { doTest(); }

  public void testScopeModifier() { doTest(); }

  public void testScopeModifierNoInline() { doTest(); }

  public void testScopeNewStatement() { doTest(); }

  private void doTest() {
    doTestIntroduceVariable();
  }
}
