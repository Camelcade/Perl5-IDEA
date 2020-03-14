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
public class PerlIntroduceVariableSuggesterTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/introduce/suggester";
  }

  @Test
  public void testCallChainedPrivate() {doTest();}

  @Test
  public void testParenthesizedExpr() {doTest();}

  @Test
  public void testParenthesizedExprWithParens() {doTest();}

  @Test
  public void testParenthesizedList() {doTest();}

  @Test
  public void testParenthesizedValue() {doTest();}

  @Test
  public void testStringList() {doTest();}

  @Test
  public void testCallChainedGetter() {doTest();}

  @Test
  public void testCallChainedGetterWithReturnValue() {doTest();}

  @Test
  public void testCallChainedMethod() {doTest();}

  @Test
  public void testCallChainedMethodWithReturnValue() {doTest();}

  @Test
  public void testCallConstructor() {doTest();}

  @Test
  public void testCallConstructorVariable() {doTest();}

  @Test
  public void testCallMethod() {doTest();}

  @Test
  public void testCallMethodGetter() {doTest();}

  @Test
  public void testCallMethodGetterWithReturnValue() {doTest();}

  @Test
  public void testCallMethodWithReturnValue() {doTest();}

  @Test
  public void testArraySlice() {doTest();}

  @Test
  public void testArraySliceDeref() {doTest();}

  @Test
  public void testArraySliceDerefBraced() {doTest();}

  @Test
  public void testHashSlice() {doTest();}

  @Test
  public void testHashSliceDeref() {doTest();}

  @Test
  public void testHashSliceDerefBraced() {doTest();}

  @Test
  public void testDerefArray() {doTest();}

  @Test
  public void testDerefArrayWithModifier() {doTest();}

  @Test
  public void testDerefArrayElement() {doTest();}

  @Test
  public void testDerefArrayElementBraceless() {doTest();}

  @Test
  public void testDerefArrayRef() {doTest();}

  @Test
  public void testDerefArrayReference() {doTest();}

  @Test
  public void testDerefHash() {doTest();}

  @Test
  public void testDerefHashElement() {doTest();}

  @Test
  public void testDerefHashElementBraceless() {doTest();}

  @Test
  public void testDerefHashRef() {doTest();}

  @Test
  public void testDerefHashReference() {doTest();}

  @Test
  public void testDerefScalar() {doTest();}

  @Test
  public void testDerefScalarRef() {doTest();}

  @Test
  public void testDerefScalarReference() {doTest();}

  @Test
  public void testHashIndexDerefStringRef() {doTest();}

  @Test
  public void testHashIndexDerefStringReference() {doTest();}

  @Test
  public void testArrayIndexDerefVariableRef() {doTest();}

  @Test
  public void testArrayIndexDerefVariableReference() {doTest();}

  @Test
  public void testAnonArray() {doTest();}

  @Test
  public void testAnonHash() {doTest();}

  @Test
  public void testArrayIndexDerefInvocation() {doTest();}

  @Test
  public void testArrayIndexDerefNumber() {doTest();}

  @Test
  public void testArrayIndexDerefNumberLong() {doTest();}

  @Test
  public void testArrayIndexDerefVariable() {doTest();}

  @Test
  public void testArrayIndexInvocation() {doTest();}

  @Test
  public void testArrayIndexNumber() {doTest();}

  @Test
  public void testArrayIndexVariable() {doTest();}

  @Test
  public void testGrep() {doTest();}

  @Test
  public void testGrepMap() {doTest();}

  @Test
  public void testGrepMapSort() {doTest();}

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
  public void testVariableCall() {doTest();}

  @Test
  public void testHashIndexDerefStringLong() {doTest();}

  @Test
  public void testHashIndexInvocation() {doTest();}

  @Test
  public void testHashIndexString() {doTest();}

  @Test
  public void testHashIndexStringQQ() {doTest();}

  @Test
  public void testHashIndexVariable() {doTest();}

  @Test
  public void testHashIndexDerefInvocation() {doTest();}

  @Test
  public void testHashIndexDerefString() {doTest();}

  @Test
  public void testHashIndexDerefStringQQ() {doTest();}

  @Test
  public void testHashIndexDerefVariable() {doTest();}

  @Test
  public void testRegexCompile() {doTest();}

  @Test
  public void testRegexMatch() {doTest();}

  @Test
  public void testRegexReplace() {doTest();}

  @Test
  public void testNumber() {doTest();}

  @Test
  public void testStringPathAbsolute() {
    assumeNotWindows();
    doTest();
  }

  @Test
  public void testStringPathAbsoluteWindows() {doTest();}

  @Test
  public void testStringPathRelative() {doTest();}

  @Test
  public void testStringPathRelativeWindows() {doTest();}

  @Test
  public void testStringPathAbsoluteQQ() {
    assumeNotWindows();
    doTest();
  }

  @Test
  public void testStringPathRelativeQQ() {doTest();}

  @Test
  public void testStringPathAbsoluteQX() {doTest();}

  @Test
  public void testStringPathRelativeQX() {doTest();}

  @Test
  public void testStringPackage() {doTest();}

  @Test
  public void testStringPackageQQ() {doTest();}

  @Test
  public void testStringPackageQX() {doTest();}

  @Test
  public void testString() {doTest();}

  @Test
  public void testStringLong() {doTest();}

  @Test
  public void testStringWithBadCharacter() {doTest();}

  @Test
  public void testStringQQ() {doTest();}

  @Test
  public void testStringQQLong() {doTest();}

  @Test
  public void testStringQQWithBadCharacter() {doTest();}

  @Test
  public void testStringQQWithInterpolation() {doTest();}

  @Test
  public void testStringQX() {doTest();}

  private void doTest() {
    doTestIntroduceVariableNamesSuggester();
  }
}
