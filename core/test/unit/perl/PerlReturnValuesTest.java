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

package unit.perl;

import base.PerlLightTestCase;

/**
 * Created by hurricup on 02.04.2016.
 */
public class PerlReturnValuesTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/unit/perl/returnValues";
  }

  public void testLoopSimple() {doTest();}

  public void testLoopComplex() {doTest();}

  public void testAssignParallel() {doTest();}

  public void testReAssign() {doTest();}

  public void testReAssignParallel() {doTest();}

  public void testReAssignUnknown() {doTest();}

  public void testReAssignUnknownDeclared() {doTest();}

  public void testReAssignUnknownAnnotated() {doTest();}

  public void testAssignParallelFirst() {doTest();}

  public void testAssignParallelSecond() {doTest();}

  public void testAssignParallelLast() {doTest();}

  public void testAssignParallelList() {doTest();}

  public void testUndef() {doTest();}

  public void testReturnUndef() {doTest();}

  public void testFuncString() {doTest();}

  public void testMethodString() {doTest();}

  public void testReturnString() {doTest();}

  public void testString() {doTest();}

  public void testStringTernaryCall() {doTest();}

  public void testStringOrCall() {doTest();}

  public void testStringOrCallLp() {doTest();}

  public void testStringAndCall() {doTest();}

  public void testStringAndCallLp() {doTest();}

  public void testReturnStringTernaryCall() {doTest();}

  public void testReturnStringOrCall() {doTest();}

  public void testReturnStringOrCallLp() {doTest();}

  public void testReturnStringOrCallLpParenthesized() {doTest();}

  public void testReturnStringAndCall() {doTest();}

  public void testReturnStringAndCallLp() {doTest();}

  public void testReturnStringAndCallLpParenthesized() {doTest();}

  public void testStringOrCallIf() {doTest();}

  public void testStringOrCallIfPostfix() {doTest();}

  public void testCallCall() {doTest();}

  public void testCall() {doTest();}

  public void testReturnCall() {doTest();}

  public void testCallObject() {doTest();}

  public void testCallStatic() {doTest();}

  private void doTest() {
    doTestReturnValue();
  }
}
