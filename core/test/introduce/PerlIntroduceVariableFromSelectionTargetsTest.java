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

public class PerlIntroduceVariableFromSelectionTargetsTest extends PerlIntroduceVariableFromCaretTargetsTest {
  @Override
  protected String getTestDataPath() {
    return "testData/introduce/targets/selection";
  }

  public void testHereDocOpenerSelected() {doTest();}

  public void testStringOneWord() {doTest();}

  public void testStringTwoWords() {doTest();}

  public void testListWithList() {doTest();}

  public void testAddMultiFromPart() {doTest();}

  public void testStringQQVariablePartial() {doTest();}

  public void testStringLazyQXStart() {doTest();}

  public void testStringLazy() {doTest();}

  public void testStringLazyOp() {doTest();}

  public void testStringLazyQQ() {doTest();}

  public void testStringLazyQQOp() {doTest();}

  public void testStringLazyQX() {doTest();}

  public void testStringLazyQXOp() {doTest();}

  public void testRegexpCompileVariableCross() {doTest();}

  public void testRegexpMatchExplicitVariableCross() {doTest();}

  public void testRegexpMatchImplicitVariableCross() {doTest();}

  public void testQwPartial() {doTest();}

  public void testQqStringWithVariableMiddle() {doTest();}

  public void testStringQOpFull() {doTest();}

  public void testStringQOpPartial() {doTest();}

  public void testStringQPlainFull() {doTest();}

  public void testStringQqOpFull() {doTest();}

  public void testStringQqOpPartial() {doTest();}

  public void testStringQqPlainFull() {doTest();}

  public void testStringQwOpFull() {doTest();}

  public void testStringQwOpPartial() {doTest();}

  public void testStringQxOpFull() {doTest();}

  public void testStringQxOpPartial() {doTest();}

  public void testStringQxPlainFull() {doTest();}

  public void testStringBareFull() {doTest();}

  public void testStringBarePartial() {doTest();}
}
