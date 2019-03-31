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

public class PerlIntroduceVariableSuggesterTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/introduce/suggester";
  }

  public void testCallChainedGetter() {doTest();}

  public void testCallChainedGetterWithReturnValue() {doTest();}

  public void testCallChainedMethod() {doTest();}

  public void testCallChainedMethodWithReturnValue() {doTest();}

  public void testCallConstructor() {doTest();}

  public void testCallConstructorVariable() {doTest();}

  public void testCallMethod() {doTest();}

  public void testCallMethodGetter() {doTest();}

  public void testCallMethodGetterWithReturnValue() {doTest();}

  public void testCallMethodWithReturnValue() {doTest();}

  public void testArraySlice() {doTest();}

  public void testArraySliceDeref() {doTest();}

  public void testArraySliceDerefBraced() {doTest();}

  public void testHashSlice() {doTest();}

  public void testHashSliceDeref() {doTest();}

  public void testHashSliceDerefBraced() {doTest();}
  
  public void testDerefArray() {doTest();}

  public void testDerefArrayWithModifier() {doTest();}

  public void testDerefArrayElement() {doTest();}

  public void testDerefArrayElementBraceless() {doTest();}

  public void testDerefArrayRef() {doTest();}

  public void testDerefArrayReference() {doTest();}

  public void testDerefHash() {doTest();}

  public void testDerefHashElement() {doTest();}

  public void testDerefHashElementBraceless() {doTest();}

  public void testDerefHashRef() {doTest();}

  public void testDerefHashReference() {doTest();}

  public void testDerefScalar() {doTest();}

  public void testDerefScalarRef() {doTest();}

  public void testDerefScalarReference() {doTest();}

  public void testHashIndexDerefStringRef() {doTest();}

  public void testHashIndexDerefStringReference() {doTest();}

  public void testArrayIndexDerefVariableRef() {doTest();}

  public void testArrayIndexDerefVariableReference() {doTest();}

  public void testAnonArray() {doTest();}

  public void testAnonHash() {doTest();}

  public void testArrayIndexDerefInvocation() {doTest();}

  public void testArrayIndexDerefNumber() {doTest();}

  public void testArrayIndexDerefNumberLong() {doTest();}

  public void testArrayIndexDerefVariable() {doTest();}

  public void testArrayIndexInvocation() {doTest();}

  public void testArrayIndexNumber() {doTest();}

  public void testArrayIndexVariable() {doTest();}

  public void testGrep() {doTest();}

  public void testGrepMap() {doTest();}

  public void testGrepMapSort() {doTest();}

  public void testMap() {doTest();}

  public void testSort() {doTest();}

  public void testDo() {doTest();}

  public void testEval() {doTest();}

  public void testAnonSub() {doTest();}

  public void testAnonSubCall() {doTest();}

  public void testVariableCall() {doTest();}

  public void testHashIndexDerefStringLong() {doTest();}

  public void testHashIndexInvocation() {doTest();}

  public void testHashIndexString() {doTest();}

  public void testHashIndexStringQQ() {doTest();}

  public void testHashIndexVariable() {doTest();}

  public void testHashIndexDerefInvocation() {doTest();}

  public void testHashIndexDerefString() {doTest();}

  public void testHashIndexDerefStringQQ() {doTest();}

  public void testHashIndexDerefVariable() {doTest();}

  public void testRegexCompile() {doTest();}

  public void testRegexMatch() {doTest();}

  public void testRegexReplace() {doTest();}

  public void testNumber() {doTest();}

  public void testStringPathAbsolute() {doTest();}

  public void testStringPathAbsoluteWindows() {doTest();}

  public void testStringPathRelative() {doTest();}

  public void testStringPathRelativeWindows() {doTest();}

  public void testStringPathAbsoluteQQ() {doTest();}

  public void testStringPathRelativeQQ() {doTest();}

  public void testStringPathAbsoluteQX() {doTest();}

  public void testStringPathRelativeQX() {doTest();}

  public void testStringPackage() {doTest();}

  public void testStringPackageQQ() {doTest();}

  public void testStringPackageQX() {doTest();}

  public void testString() {doTest();}

  public void testStringLong() {doTest();}

  public void testStringWithBadCharacter() {doTest();}

  public void testStringQQ() {doTest();}

  public void testStringQQLong() {doTest();}

  public void testStringQQWithBadCharacter() {doTest();}

  public void testStringQQWithInterpolation() {doTest();}

  public void testStringQX() {doTest();}

  private void doTest() {
    doTestIntroduceVariableNamesSuggester();
  }
}
