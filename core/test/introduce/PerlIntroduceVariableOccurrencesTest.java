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

public class PerlIntroduceVariableOccurrencesTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/introduce/occurrences";
  }

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

  protected void doTest() {
    doTestIntroduceVariableOccurances();
  }
}
