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

package formatter;


import org.junit.Test;

import static com.intellij.psi.codeStyle.CommonCodeStyleSettings.*;
public class PerlFormatterWrapTest extends PerlFormatterTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/formatter/perl/wrap";
  }

  @Test
  public void testAssignmentsNever() {
    doTestAssignments(DO_NOT_WRAP, false);
  }

  @Test
  public void testAssignmentsAlways() {
    doTestAssignments(WRAP_ALWAYS, false);
  }

  @Test
  public void testAssignmentsLong() {
    doTestAssignments(WRAP_AS_NEEDED, false);
  }

  @Test
  public void testAssignmentsChomp() {
    doTestAssignments(WRAP_ON_EVERY_ITEM, false);
  }

  @Test
  public void testAssignmentsNeverNextLine() {
    doTestAssignments(DO_NOT_WRAP, true);
  }

  @Test
  public void testAssignmentsAlwaysNextLine() {
    doTestAssignments(WRAP_ALWAYS, true);
  }

  @Test
  public void testAssignmentsLongNextLine() {
    doTestAssignments(WRAP_AS_NEEDED, true);
  }

  @Test
  public void testAssignmentsChompNextLine() {
    doTestAssignments(WRAP_ON_EVERY_ITEM, true);
  }

  private void doTestAssignments(int wrapType, boolean nextLine) {
    getSettings().ASSIGNMENT_WRAP = wrapType;
    getSettings().PLACE_ASSIGNMENT_SIGN_ON_NEXT_LINE = nextLine;
    doWrappingTestSingleSource("assignments");
  }

  @Test
  public void testCommaSequenceNever() {
    doTestCommaSequence(DO_NOT_WRAP);
  }

  @Test
  public void testCommaSequenceAlways() {
    doTestCommaSequence(WRAP_ALWAYS);
  }

  @Test
  public void testCommaSequenceLong() {
    doTestCommaSequence(WRAP_AS_NEEDED);
  }

  @Test
  public void testCommaSequenceChomp() {
    doTestCommaSequence(WRAP_ON_EVERY_ITEM);
  }

  private void doTestCommaSequence(int wrapType) {
    getSettings().ARRAY_INITIALIZER_WRAP = wrapType;
    doWrappingTestSingleSource("commaSequence");
  }

  @Test
  public void testAttributesNever() {
    doTestAttributes(DO_NOT_WRAP);
  }

  @Test
  public void testAttributesAlways() {
    doTestAttributes(WRAP_ALWAYS);
  }

  @Test
  public void testAttributesLong() {
    doTestAttributes(WRAP_AS_NEEDED);
  }

  @Test
  public void testAttributesChomp() {
    doTestAttributes(WRAP_ON_EVERY_ITEM);
  }

  private void doTestAttributes(int wrapType) {
    getCustomSettings().ATTRIBUTES_WRAP = wrapType;
    doWrappingTestSingleSource("attributes");
  }

  @Test
  public void testCallArgumentsNever() {
    doTestCallArguments(DO_NOT_WRAP);
  }

  @Test
  public void testCallArgumentsAlways() {
    doTestCallArguments(WRAP_ALWAYS);
  }

  @Test
  public void testCallArgumentsLong() {
    doTestCallArguments(WRAP_AS_NEEDED);
  }

  @Test
  public void testCallArgumentsChomp() {
    doTestCallArguments(WRAP_ON_EVERY_ITEM);
  }

  private void doTestCallArguments(int wrapType) {
    getSettings().CALL_PARAMETERS_WRAP = wrapType;
    doWrappingTestSingleSource("callArguments");
  }

  @Test
  public void testBinaryNever() {
    doTestBinary(DO_NOT_WRAP, false);
  }

  @Test
  public void testBinaryAlways() {
    doTestBinary(WRAP_ALWAYS, false);
  }

  @Test
  public void testBinaryLong() {
    doTestBinary(WRAP_AS_NEEDED, false);
  }

  @Test
  public void testBinaryChomp() {
    doTestBinary(WRAP_ON_EVERY_ITEM, false);
  }

  @Test
  public void testBinaryNeverSignNewLine() {
    doTestBinary(DO_NOT_WRAP, true);
  }

  @Test
  public void testBinaryAlwaysSignNewLine() {
    doTestBinary(WRAP_ALWAYS, true);
  }

  @Test
  public void testBinaryLongSignNewLine() {
    doTestBinary(WRAP_AS_NEEDED, true);
  }

  @Test
  public void testBinaryChompSignNewLine() {
    doTestBinary(WRAP_ON_EVERY_ITEM, true);
  }

  private void doTestBinary(int wrapType, boolean signNewLine) {
    getSettings().BINARY_OPERATION_WRAP = wrapType;
    getSettings().BINARY_OPERATION_SIGN_ON_NEXT_LINE = signNewLine;
    doWrappingTestSingleSource("binary");
  }

  @Test
  public void testDereferenceNone() {
    doTestDereferenceWrap(DO_NOT_WRAP);
  }

  @Test
  public void testDereferenceAlways() {
    doTestDereferenceWrap(WRAP_ALWAYS);
  }

  @Test
  public void testDereferenceLong() {
    doTestDereferenceWrap(WRAP_AS_NEEDED);
  }

  @Test
  public void testDereferenceChomp() {
    doTestDereferenceWrap(WRAP_ON_EVERY_ITEM);
  }

  private void doTestDereferenceWrap(int wrapType) {
    getSettings().METHOD_CALL_CHAIN_WRAP = wrapType;
    doWrappingTestSingleSource("dereference");
  }

  @Test
  public void testVariableDeclarationsNone() {doTestVariableDeclarations(DO_NOT_WRAP);}

  @Test
  public void testVariableDeclarationsAlways() {doTestVariableDeclarations(WRAP_ALWAYS);}

  @Test
  public void testVariableDeclarationsLong() {doTestVariableDeclarations(WRAP_AS_NEEDED);}

  @Test
  public void testVariableDeclarationsChomp() {doTestVariableDeclarations(WRAP_ON_EVERY_ITEM);}

  private void doTestVariableDeclarations(int wrapType) {
    getCustomSettings().VARIABLE_DECLARATION_WRAP = wrapType;
    doWrappingTestSingleSource("variableDeclarations");
  }

  @Test
  public void testSignaturesNone() {doTestSignatures(DO_NOT_WRAP);}

  @Test
  public void testSignaturesAlways() {doTestSignatures(WRAP_ALWAYS);}

  @Test
  public void testSignaturesLong() {doTestSignatures(WRAP_AS_NEEDED);}

  @Test
  public void testSignaturesChomp() {doTestSignatures(WRAP_ON_EVERY_ITEM);}

  private void doTestSignatures(int wrapType) {
    getSettings().METHOD_PARAMETERS_WRAP = wrapType;
    doWrappingTestSingleSource("signatures");
  }

  @Test
  public void testTernaryNone() {doTestTernary(DO_NOT_WRAP, false);}

  @Test
  public void testTernaryAlways() {doTestTernary(WRAP_ALWAYS, false);}

  @Test
  public void testTernaryLong() {doTestTernary(WRAP_AS_NEEDED, false);}

  @Test
  public void testTernaryChomp() {doTestTernary(WRAP_ON_EVERY_ITEM, false);}

  @Test
  public void testTernaryNoneSignNewLine() {doTestTernary(DO_NOT_WRAP, true);}

  @Test
  public void testTernaryAlwaysSignNewLine() {doTestTernary(WRAP_ALWAYS, true);}

  @Test
  public void testTernaryLongSignNewLine() {doTestTernary(WRAP_AS_NEEDED, true);}

  @Test
  public void testTernaryChompSignNewLine() {doTestTernary(WRAP_ON_EVERY_ITEM, true);}

  private void doTestTernary(int wrapType, boolean signNewLine) {
    getSettings().TERNARY_OPERATION_WRAP = wrapType;
    getSettings().TERNARY_OPERATION_SIGNS_ON_NEXT_LINE = signNewLine;
    doWrappingTestSingleSource("ternary");
  }

  @Test
  public void testQwListNever() {
    doTestQwList(DO_NOT_WRAP);
  }

  @Test
  public void testQwListAlways() {
    doTestQwList(WRAP_ALWAYS);
  }

  @Test
  public void testQwListLong() {
    doTestQwList(WRAP_AS_NEEDED);
  }

  @Test
  public void testQwListChomp() {
    doTestQwList(WRAP_ON_EVERY_ITEM);
  }

  private void doTestQwList(int wrapType) {
    getCustomSettings().QW_LIST_WRAP = wrapType;
    doWrappingTestSingleSource("qwList");
  }


  @Test
  public void testCommentsWrapTrue() {
    getSettings().WRAP_COMMENTS = true;
    doTestCommentsWrapping();
  }

  @Test
  public void testCommentsWrapFalse() {
    getSettings().WRAP_COMMENTS = false;
    doTestCommentsWrapping();
  }

  private void doTestCommentsWrapping() {doWrappingTestSingleSource("comments");}

  @Test
  public void testFatCommaWrapping() {
    getSettings().ARRAY_INITIALIZER_WRAP = WRAP_AS_NEEDED;
    doFormatTest();
  }

  @Test
  public void testHeredocWrapping() {
    getSettings().ARRAY_INITIALIZER_WRAP = WRAP_AS_NEEDED;
    doFormatTest();
  }
}
