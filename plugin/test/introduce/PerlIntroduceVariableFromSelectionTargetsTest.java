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

import org.junit.Test;
public class PerlIntroduceVariableFromSelectionTargetsTest extends PerlIntroduceVariableFromCaretTargetsTest {
  @Override
  protected String getBaseDataPath() {
    return "testData/introduce/targets/selection";
  }

  @Test
  public void testParenthesizedListOpeningParen() {doTest();}

  @Test
  public void testParenthesizedListBeforeClosingParen() {doTest();}

  @Test
  public void testParenthesizedListAfterClosingParen() {doTest();}

  @Test
  public void testQwLastParen() {doTest();}

  @Test
  public void testGrep() {doTest();}

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
  public void testHereDocOpenerSelected() {doTest();}

  @Test
  public void testStringOneWord() {doTest();}

  @Test
  public void testStringTwoWords() {doTest();}

  @Test
  public void testListWithList() {doTest();}

  @Test
  public void testAddMultiFromPart() {doTest();}

  @Test
  public void testStringQQVariablePartial() {doTest();}

  @Test
  public void testStringLazyQXStart() {doTest();}

  @Test
  public void testStringLazy() {doTest();}

  @Test
  public void testStringLazyOp() {doTest();}

  @Test
  public void testStringLazyQQ() {doTest();}

  @Test
  public void testStringLazyQQOp() {doTest();}

  @Test
  public void testStringLazyQX() {doTest();}

  @Test
  public void testStringLazyQXOp() {doTest();}

  @Test
  public void testRegexpCompileVariableCross() {doTest();}

  @Test
  public void testRegexpMatchExplicitVariableCross() {doTest();}

  @Test
  public void testRegexpMatchImplicitVariableCross() {doTest();}

  @Test
  public void testQwPartial() {doTest();}

  @Test
  public void testQqStringWithVariableMiddle() {doTest();}

  @Test
  public void testStringQOpFull() {doTest();}

  @Test
  public void testStringQOpPartial() {doTest();}

  @Test
  public void testStringQPlainFull() {doTest();}

  @Test
  public void testStringQqOpFull() {doTest();}

  @Test
  public void testStringQqOpPartial() {doTest();}

  @Test
  public void testStringQqPlainFull() {doTest();}

  @Test
  public void testStringQwOpFull() {doTest();}

  @Test
  public void testStringQwOpPartial() {doTest();}

  @Test
  public void testStringQxOpFull() {doTest();}

  @Test
  public void testStringQxOpPartial() {doTest();}

  @Test
  public void testStringQxPlainFull() {doTest();}

  @Test
  public void testStringBareFull() {doTest();}

  @Test
  public void testStringBarePartial() {doTest();}
}
