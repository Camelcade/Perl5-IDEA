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

  public void testUndef() {doTest();}

  public void testReturnUndef() {doTest();}

  public void testFuncString() {doTest();}

  public void testMethodString() {doTest();}

  public void testSubReturnString() {doTest();}

  public void testSubString() {doTest();}

  public void testSubStringTernaryCall() {doTest();}

  public void testSubStringOrCall() {doTest();}

  public void testSubStringOrCallLp() {doTest();}

  public void testSubStringAndCall() {doTest();}

  public void testSubStringAndCallLp() {doTest();}

  public void testSubReturnStringTernaryCall() {doTest();}

  public void testSubReturnStringOrCall() {doTest();}

  public void testSubReturnStringOrCallLp() {doTest();}

  public void testSubReturnStringOrCallLpParenthesized() {doTest();}

  public void testSubReturnStringAndCall() {doTest();}

  public void testSubReturnStringAndCallLp() {doTest();}

  public void testSubReturnStringAndCallLpParenthesized() {doTest();}

  public void testSubStringOrCallIf() {doTest();}

  public void testSubStringOrCallIfPostfix() {doTest();}

  public void testSubCallCall() {doTest();}

  public void testSubCall() {doTest();}

  public void testSubReturnCall() {doTest();}

  public void testSubCallObject() {doTest();}

  public void testSubCallStatic() {doTest();}

  private void doTest() {
    doTestReturnValue();
  }
}
