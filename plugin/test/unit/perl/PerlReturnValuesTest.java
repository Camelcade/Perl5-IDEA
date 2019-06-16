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
import org.junit.Test;
public class PerlReturnValuesTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/unit/perl/returnValues";
  }

  @Test
  public void testMultiPushUnshift() {doTest();}

  @Test
  public void testMultiPushUnshiftKnown() {doTest();}

  @Test
  public void testArrayRefElement() {doTest();}

  @Test
  public void testHashRefElement() {doTest();}

  @Test
  public void testArrayDeref() {doTest();}

  @Test
  public void testHashDeref() {doTest();}

  @Test
  public void testShiftPopMethodIncomplete() {doTest();}

  @Test
  public void testShiftPopArgs() {doTest();}

  @Test
  public void testShiftPopMethod() {doTest();}

  @Test
  public void testReturnImplicit() {doTest();}

  @Test
  public void testShiftOnce() {doTest();}

  @Test
  public void testShiftPop() {doTest();}

  @Test
  public void testShiftTwice() {doTest();}

  @Test
  public void testPopOnce() {doTest();}

  @Test
  public void testPopShift() {doTest();}

  @Test
  public void testPopTwice() {doTest();}

  @Test
  public void testShiftValue() {doTest();}

  @Test
  public void testPopValue() {doTest();}

  @Test
  public void testPushArray() {doTest();}

  @Test
  public void testPushArrayComplex() {doTest();}

  @Test
  public void testPushArrayComplexHash() {doTest();}

  @Test
  public void testScalarDereference() {doTest();}

  @Test
  public void testScalarDereferenceDouble() {doTest();}

  @Test
  public void testArraySliceSingle() {doTest();}

  @Test
  public void testArraySliceMulti() {doTest();}

  @Test
  public void testHashSliceSingle() {doTest();}

  @Test
  public void testHashSliceMulti() {doTest();}

  @Test
  public void testArithmeticNegation() {doTest();}

  @Test
  public void testArithmeticNegationDouble() {doTest();}

  @Test
  public void testArithmeticNoop() {doTest();}

  @Test
  public void testArithmeticNoopDouble() {doTest();}

  @Test
  public void testArrayElement() {doTest();}

  @Test
  public void testReturnArgsAsScalar() {doTest();}

  @Test
  public void testReturnArgsArray() {doTest();}

  @Test
  public void testReturnArgsHash() {doTest();}

  @Test
  public void testReturnArgsHashElement() {doTest();}

  @Test
  public void testLoopSimple() {doTest();}

  @Test
  public void testLoopComplex() {doTest();}

  @Test
  public void testAssignParallel() {doTest();}

  @Test
  public void testReAssign() {doTest();}

  @Test
  public void testReAssignParallel() {doTest();}

  @Test
  public void testReAssignUnknown() {doTest();}

  @Test
  public void testReAssignUnknownDeclared() {doTest();}

  @Test
  public void testReAssignUnknownAnnotated() {doTest();}

  @Test
  public void testAssignParallelFirst() {doTest();}

  @Test
  public void testAssignParallelSecond() {doTest();}

  @Test
  public void testAssignParallelLast() {doTest();}

  @Test
  public void testAssignParallelList() {doTest();}

  @Test
  public void testUndef() {doTest();}

  @Test
  public void testReturnUndef() {doTest();}

  @Test
  public void testFuncString() {doTest();}

  @Test
  public void testMethodString() {doTest();}

  @Test
  public void testReturnString() {doTest();}

  @Test
  public void testString() {doTest();}

  @Test
  public void testStringTernaryCall() {doTest();}

  @Test
  public void testStringOrCall() {doTest();}

  @Test
  public void testStringOrCallLp() {doTest();}

  @Test
  public void testStringAndCall() {doTest();}

  @Test
  public void testStringAndCallLp() {doTest();}

  @Test
  public void testReturnStringTernaryCall() {doTest();}

  @Test
  public void testReturnStringOrCall() {doTest();}

  @Test
  public void testReturnStringOrCallLp() {doTest();}

  @Test
  public void testReturnStringOrCallLpParenthesized() {doTest();}

  @Test
  public void testReturnStringAndCall() {doTest();}

  @Test
  public void testReturnStringAndCallLp() {doTest();}

  @Test
  public void testReturnStringAndCallLpParenthesized() {doTest();}

  @Test
  public void testStringOrCallIf() {doTest();}

  @Test
  public void testStringOrCallIfPostfix() {doTest();}

  @Test
  public void testCallCall() {doTest();}

  @Test
  public void testCall() {doTest();}

  @Test
  public void testReturnCall() {doTest();}

  @Test
  public void testCallObject() {doTest();}

  @Test
  public void testCallStatic() {doTest();}

  private void doTest() {
    doTestReturnValue();
  }
}
