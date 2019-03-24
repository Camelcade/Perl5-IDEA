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

public class PerlIntroduceVariableTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/introduce/full";
  }

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
