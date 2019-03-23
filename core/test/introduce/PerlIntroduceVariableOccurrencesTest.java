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

/**
 * NB: Test taking TOP most introduce target if there are many of them
 */
public class PerlIntroduceVariableOccurrencesTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/introduce/occurrences";
  }

  public void testStringPartialStart() {doTest();}

  public void testStringPartialMid() {doTest();}

  public void testStringPartialEnd() {doTest();}

  public void testRegexReplace() {doTest();}

  public void testRegexReplaceLazy() {doTest();}

  public void testMethodCallNoParens() {doTest();}

  public void testMethodCallNotParenthesized() {doTest();}

  public void testMethodCallParens() {doTest();}

  public void testMethodCallParenthesized() {doTest();}

  public void testNestedCallWithParens() {doTest();}

  public void testNestedCallWithoutParens() {doTest();}

  public void testPackageForms() {doTest();}

  public void testPackageFormsString() {doTest();}

  public void testPackageFormsStringQq() {doTest();}

  public void testPackageFormsStringQx() {doTest();}

  public void testPackageFormsReverse() {doTest();}

  public void testRegexMatchImplicit() {doTest();}

  public void testRegexMatchExplicit() {doTest();}

  public void testRegexMatchLazy() {doTest();}

  public void testRegexMatchEmpty() {doTest();}

  public void testListPartial() {doTest();}

  public void testPlusMinus() {doTest();}

  public void testQwRange() {doTest();}

  public void testQwRangeScalar() {doTest();}

  public void testQwRangeLazy() {doTest();}

  public void testQwRangeScalarLazy() {doTest();}

  public void testListToQw() {doTest();}

  public void testListToQwSame() {doTest();}

  public void testDerefStyles() {doTest();}

  public void testDerefSimple() {doTest();}

  public void testDerefSizes() {doTest();}

  public void testListFull() {doTest();}

  public void testComparision() {doTest();}

  public void testStringFull() {doTest();}

  public void testSubCall() {doTest();}

  public void testStringBare() {doTest();}

  public void testStringQOp() {doTest();}

  public void testStringQPlain() {doTest();}

  public void testStringQqOp() {doTest();}

  public void testStringQqPlain() {doTest();}

  public void testStringQxOp() {doTest();}

  public void testStringQxPlain() {doTest();}

  public void testStatementSemi() {doTest();}

  public void testStatementNoSemi() {doTest();}

  public void testScalarBraced() {doTest();}

  public void testScalarUnbraced() {doTest();}

  public void testArrayBraced() {doTest();}

  public void testArrayUnbraced() {doTest();}

  public void testHashBraced() {doTest();}

  public void testHashUnbraced() {doTest();}

  public void testGlobBraced() {doTest();}

  public void testGlobUnbraced() {doTest();}

  public void testCodeBraced() {doTest();}

  public void testCodeUnbraced() {doTest();}

  public void testScalarDerefBraced() {doTest();}

  public void testScalarDerefUnbraced() {doTest();}

  public void testArrayDerefBraced() {doTest();}

  public void testArrayDerefUnbraced() {doTest();}

  public void testHashDerefBraced() {doTest();}

  public void testHashDerefUnbraced() {doTest();}

  public void testGlobDerefBraced() {doTest();}

  public void testGlobDerefUnbraced() {doTest();}

  public void testCodeDerefBraced() {doTest();}

  public void testCodeDerefUnbraced() {doTest();}

  protected void doTest() {
    doTestIntroduceVariableOccurances();
  }
}
