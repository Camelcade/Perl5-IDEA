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

import static com.intellij.psi.codeStyle.CommonCodeStyleSettings.*;

public class PerlFormatterWrapTest extends PerlFormatterTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/formatter/perl/wrap";
  }

  public void testWrapBinaryExpressionsNever() {
    doTestWrapBinaryExpressions(DO_NOT_WRAP);
  }

  public void testWrapBinaryExpressionsAlways() {
    doTestWrapBinaryExpressions(WRAP_ALWAYS);
  }

  public void testWrapBinaryExpressionsLong() {
    doTestWrapBinaryExpressions(WRAP_AS_NEEDED);
  }

  public void testWrapBinaryExpressionsChomp() {
    doTestWrapBinaryExpressions(WRAP_ON_EVERY_ITEM);
  }

  private void doTestWrapBinaryExpressions(int wrapType) {
    getSettings().BINARY_OPERATION_WRAP = wrapType;
    doWrappingTestSingleSource("wrapBinaryExpressions");
  }

  public void testDereferenceNone() {
    doTestDereferenceWrap(DO_NOT_WRAP);
  }

  public void testDereferenceAlways() {
    doTestDereferenceWrap(WRAP_ALWAYS);
  }

  public void testDereferenceLong() {
    doTestDereferenceWrap(WRAP_AS_NEEDED);
  }

  public void testDereferenceChomp() {
    doTestDereferenceWrap(WRAP_ON_EVERY_ITEM);
  }

  private void doTestDereferenceWrap(int wrapType) {
    getSettings().METHOD_CALL_CHAIN_WRAP = wrapType;
    doWrappingTestSingleSource("dereference");
  }

  public void testDeclarationsWrapping() {doWrappingFormatTest();}

  public void testSignaturesWrapping() {doWrappingFormatTest();}

  public void testTernaryNone() {doTestTernary(DO_NOT_WRAP, false);}

  public void testTernaryAlways() {doTestTernary(WRAP_ALWAYS, false);}

  public void testTernaryLong() {doTestTernary(WRAP_AS_NEEDED, false);}

  public void testTernaryChomp() {doTestTernary(WRAP_ON_EVERY_ITEM, false);}

  public void testTernaryNoneSignNewLine() {doTestTernary(DO_NOT_WRAP, true);}

  public void testTernaryAlwaysSignNewLine() {doTestTernary(WRAP_ALWAYS, true);}

  public void testTernaryLongSignNewLine() {doTestTernary(WRAP_AS_NEEDED, true);}

  public void testTernaryChompSignNewLine() {doTestTernary(WRAP_ON_EVERY_ITEM, true);}

  private void doTestTernary(int wrapType, boolean signNewLine) {
    getSettings().TERNARY_OPERATION_WRAP = wrapType;
    getSettings().TERNARY_OPERATION_SIGNS_ON_NEXT_LINE = signNewLine;
    doWrappingTestSingleSource("ternary");
  }

  public void testQwWrapping() {doFormatTest();}


  public void testCommentsWrapTrue() {
    getSettings().WRAP_COMMENTS = true;
    doTestCommentsWrapping();
  }

  public void testCommentsWrapFalse() {
    getSettings().WRAP_COMMENTS = false;
    doTestCommentsWrapping();
  }

  private void doTestCommentsWrapping() {doWrappingTestSingleSource("comments");}

  public void testFatCommaWrapping() {doFormatTest();}

  public void testCommaWrapping() {
    doFormatTest();
  }

  public void testHeredocWrapping() {
    doFormatTest();
  }
}
