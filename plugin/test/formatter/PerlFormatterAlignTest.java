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


import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import org.junit.Test;

import static com.intellij.psi.codeStyle.CommonCodeStyleSettings.WRAP_AS_NEEDED;
import static com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings.OptionalConstructions.*;
public class PerlFormatterAlignTest extends PerlFormatterTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/formatter/perl/align";
  }


  @Test
  public void testAlignListElementsTrue() {
    getSettings().ARRAY_INITIALIZER_WRAP = WRAP_AS_NEEDED;
    getSettings().ALIGN_MULTILINE_ARRAY_INITIALIZER_EXPRESSION = true;
    doTestAlignListElements();
  }

  @Test
  public void testAlignListElementsFalse() {
    getSettings().ARRAY_INITIALIZER_WRAP = WRAP_AS_NEEDED;
    getSettings().ALIGN_MULTILINE_ARRAY_INITIALIZER_EXPRESSION = false;
    doTestAlignListElements();
  }

  private void doTestAlignListElements() {
    doWrappingTestSingleSource("alignListElements");
  }

  @Test
  public void testAlignCommentsTrue() {
    doTestAlignComments(true);
  }

  @Test
  public void testAlignCommentsFalse() {
    doTestAlignComments(false);
  }

  private void doTestAlignComments(boolean value) {
    getCustomSettings().ALIGN_COMMENTS_ON_CONSEQUENT_LINES = value;
    doTestSingleSource("alignComments");
  }

  @Test
  public void testAlignDereferenceFalse() {
    doTestAlignDereference(false, true);
  }

  @Test
  public void testAlignDereferenceTrue() {
    doTestAlignDereference(true, true);
  }

  @Test
  public void testAlignDereferenceFalseNoWrap() {
    doTestAlignDereferenceNoWrap(false);
  }

  @Test
  public void testAlignDereferenceTrueNoWrap() {
    doTestAlignDereferenceNoWrap(true);
  }

  private void doTestAlignDereferenceNoWrap(boolean align) {
    getSettings().ALIGN_MULTILINE_CHAINED_METHODS = align;
    getCustomSettings().METHOD_CALL_CHAIN_SIGN_NEXT_LINE = true;
    doFormatTest("alignDereferenceNoWrap", getTestName(true), "");
  }

  @Test
  public void testAlignDereferenceFalseNoWrapSameLine() {
    doTestAlignDereferenceNoWrapSameLine(false);
  }

  @Test
  public void testAlignDereferenceTrueNoWrapSameLine() {
    doTestAlignDereferenceNoWrapSameLine(true);
  }

  private void doTestAlignDereferenceNoWrapSameLine(boolean align) {
    getSettings().ALIGN_MULTILINE_CHAINED_METHODS = align;
    getCustomSettings().METHOD_CALL_CHAIN_SIGN_NEXT_LINE = false;
    doFormatTest("alignDereferenceNoWrap", getTestName(true), "");
  }

  @Test
  public void testAlignDereferenceFalseSameLine() {
    doTestAlignDereference(false, false);
  }

  @Test
  public void testAlignDereferenceTrueSameLine() {
    doTestAlignDereference(true, false);
  }

  private void doTestAlignDereference(boolean align, boolean signNextLine) {
    CommonCodeStyleSettings settings = getSettings();
    settings.RIGHT_MARGIN = 27;
    settings.METHOD_CALL_CHAIN_WRAP = CommonCodeStyleSettings.WRAP_AS_NEEDED;
    settings.ALIGN_MULTILINE_CHAINED_METHODS = align;
    getCustomSettings().METHOD_CALL_CHAIN_SIGN_NEXT_LINE = signNextLine;
    doFormatTest("alignDereference", getTestName(true), "");
  }

  @Test
  public void testAlignFatCommaTrue() {
    getCustomSettings().ALIGN_FAT_COMMA = true;
    doFormatTest();
  }

  @Test
  public void testAlignFatCommaFalse() {
    getCustomSettings().ALIGN_FAT_COMMA = false;
    doFormatTest();
  }

  @Test
  public void testAlignQwTrue() {
    getCustomSettings().ALIGN_QW_ELEMENTS = true;
    doFormatTest();
  }

  @Test
  public void testAlignQwFalse() {
    getCustomSettings().ALIGN_QW_ELEMENTS = false;
    doFormatTest();
  }

  @Test
  public void testTernaryTrue() {
    doTestTernary(true);
  }

  @Test
  public void testTernaryFalse() {
    doTestTernary(false);
  }

  private void doTestTernary(boolean value) {
    getSettings().ALIGN_MULTILINE_TERNARY_OPERATION = value;
    doTestSingleSource("ternary");
  }

  @Test
  public void testBinaryTrue() {
    doTestBinary(true);
  }

  @Test
  public void testBinaryFalse() {
    doTestBinary(false);
  }

  private void doTestBinary(boolean value) {
    getSettings().ALIGN_MULTILINE_BINARY_OPERATION = value;
    doTestSingleSource("binary");
  }

  @Test
  public void testSignaturesTrue() {
    doTestSignatures(true);
  }

  @Test
  public void testSignaturesFalse() {
    doTestSignatures(false);
  }

  private void doTestSignatures(boolean value) {
    getSettings().ALIGN_MULTILINE_PARAMETERS = value;
    doTestSingleSource("signatures");
  }

  @Test
  public void testVariableDeclarationsTrue() {
    doTestVariablesDeclarations(true);
  }

  @Test
  public void testVariableDeclarationsFalse() {
    doTestVariablesDeclarations(false);
  }

  private void doTestVariablesDeclarations(boolean value) {
    getCustomSettings().ALIGN_VARIABLE_DECLARATIONS = value;
    doTestSingleSource("variablesDeclaration");
  }

  @Test
  public void testCallArgumentsTrue() {
    doTestCallArguments(true);
  }

  @Test
  public void testCallArgumentsFalse() {
    doTestCallArguments(false);
  }

  private void doTestCallArguments(boolean value) {
    getSettings().ALIGN_MULTILINE_PARAMETERS_IN_CALLS = value;
    doTestSingleSource("callArguments");
  }

  @Test
  public void testAssignmentsStatement() {
    doTestAssignments(ALIGN_IN_STATEMENT);
  }

  @Test
  public void testAssignmentsLines() {
    doTestAssignments(ALIGN_LINES);
  }

  @Test
  public void testAssignmentsNone() {
    doTestAssignments(NO_ALIGN);
  }

  private void doTestAssignments(int value) {
    getCustomSettings().ALIGN_CONSECUTIVE_ASSIGNMENTS = value;
    doTestSingleSource("assignments");
  }


  @Test
  public void testAttributesTrue() {
    doTestAttributes(true);
  }

  @Test
  public void testAttributesFalse() {
    doTestAttributes(false);
  }

  private void doTestAttributes(boolean value) {
    getCustomSettings().ALIGN_ATTRIBUTES = value;
    doTestSingleSource("attributes");
  }
  
}
