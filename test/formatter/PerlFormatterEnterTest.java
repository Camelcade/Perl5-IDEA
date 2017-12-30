/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

public class PerlFormatterEnterTest extends PerlFormatterTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/formatter/perl/enter";
  }

  public void testPlainQqString() {doTest();}

  public void testPlainQString() {doTest();}

  public void testPlainQxString() {doTest();}

  public void testQqString() {doTest();}

  public void testQString() {doTest();}

  public void testQxString() {doTest();}

  public void testAnonHash() {doTest();}

  public void testAnonList() {doTest();}

  public void testAnonArray() {doTest();}

  public void testQwList() {doTest();}

  public void testCommaSequenceTop() {doTest();}

  public void testAfterStatement() {doTest();}

  public void testAfterStatementInMethod() {doTest();}

  public void testAfterStatementInBlock() {doTest();}

  public void testAfterStatementInContinue() {doTest();}

  public void testAfterStatementInElse() {doTest();}

  public void testAfterStatementInElseIf() {doTest();}

  public void testAfterStatementInFor() {doTest();}

  public void testAfterStatementInForeach() {doTest();}

  public void testAfterStatementInFunc() {doTest();}

  public void testAfterStatementInGiven() {doTest();}

  public void testAfterStatementInIf() {doTest();}

  public void testAfterStatementInNamedBlock() {doTest();}

  public void testAfterStatementInSub() {doTest();}

  public void testAfterStatementInUnless() {doTest();}

  public void testAfterStatementInUntil() {doTest();}

  public void testAfterStatementInWhen() {doTest();}

  public void testAfterStatementInWhile() {doTest();}


  private void doTest() {
    doTestEnter();
  }
}
