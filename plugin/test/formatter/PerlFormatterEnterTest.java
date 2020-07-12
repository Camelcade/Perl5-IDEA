/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package formatter;


import org.junit.Test;
public class PerlFormatterEnterTest extends PerlFormatterTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/formatter/perl/enter";
  }

  @Test
  public void testIssue1754() {
    getCustomSettings().ALIGN_COMMENTS_ON_CONSEQUENT_LINES = true;
    doTest();
  }

  @Test
  public void testReplacementBlockBefore() {doTest();}

  @Test
  public void testReplacementBlockInside() {doTest();}

  @Test
  public void testReplacementBlockAfter() {doTest();}

  @Test
  public void testCommaSequenceWithFatInSomething() {doTest();}

  @Test
  public void testCommaSequenceWithFatTop() {doTest();}

  @Test
  public void testIncompleteStatement() {doTest();}

  @Test
  public void testPlainQqString() {doTest();}

  @Test
  public void testPlainQString() {doTest();}

  @Test
  public void testPlainQxString() {doTest();}

  @Test
  public void testQqString() {doTest();}

  @Test
  public void testQString() {doTest();}

  @Test
  public void testQxString() {doTest();}

  @Test
  public void testAnonHash() {doTest();}

  @Test
  public void testAnonList() {doTest();}

  @Test
  public void testAnonArray() {doTest();}

  @Test
  public void testQwList() {doTest();}

  @Test
  public void testAnonHashInBlock() {doTest();}

  @Test
  public void testAnonListInBlock() {doTest();}

  @Test
  public void testAnonArrayInBlock() {doTest();}

  @Test
  public void testQwListInBlock() {doTest();}

  @Test
  public void testCommaSequenceTop() {doTest();}

  @Test
  public void testAfterStatement() {doTest();}

  @Test
  public void testAfterStatementInMethod() {doTest();}

  @Test
  public void testAfterStatementInBlock() {doTest();}

  @Test
  public void testAfterStatementInContinue() {doTest();}

  @Test
  public void testAfterStatementInElse() {doTest();}

  @Test
  public void testAfterStatementInElseIf() {doTest();}

  @Test
  public void testAfterStatementInFor() {doTest();}

  @Test
  public void testAfterStatementInForeach() {doTest();}

  @Test
  public void testAfterStatementInFunc() {doTest();}

  @Test
  public void testAfterStatementInGiven() {doTest();}

  @Test
  public void testAfterStatementInIf() {doTest();}

  @Test
  public void testAfterStatementInNamedBlock() {doTest();}

  @Test
  public void testAfterStatementInSub() {doTest();}

  @Test
  public void testAfterStatementInUnless() {doTest();}

  @Test
  public void testAfterStatementInUntil() {doTest();}

  @Test
  public void testAfterStatementInWhen() {doTest();}

  @Test
  public void testAfterStatementInWhile() {doTest();}


  private void doTest() {
    doTestEnter();
  }
}
