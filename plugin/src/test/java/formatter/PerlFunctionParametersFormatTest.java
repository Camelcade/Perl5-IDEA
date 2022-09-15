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

import static com.intellij.psi.codeStyle.CommonCodeStyleSettings.*;
import static com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings.OptionalConstructions.*;

public class PerlFunctionParametersFormatTest extends PerlFormatterTestCase {
  @Override
  protected String getBaseDataPath() {
    return "formatter/perl/functionParameters";
  }

  @Test
  public void testDefaultSettings() {doTest();}

  @Test
  public void testBeforeParenTrue() {
    getSettings().SPACE_BEFORE_METHOD_PARENTHESES = true;
    doTest();
  }

  @Test
  public void testBeforeParentFalse() {
    getSettings().SPACE_BEFORE_METHOD_PARENTHESES = false;
    doTest();
  }

  @Test
  public void testWithinParensTrue() {
    getSettings().SPACE_WITHIN_METHOD_PARENTHESES = true;
    doTest();
  }

  @Test
  public void testWithinParensFalse() {
    getSettings().SPACE_WITHIN_METHOD_PARENTHESES = false;
    doTest();
  }

  @Test
  public void testWithinEmptyParensTrue() {
    getSettings().SPACE_WITHIN_EMPTY_METHOD_PARENTHESES = true;
    doTest();
  }

  @Test
  public void testWithinEmptyParensFalse() {
    getSettings().SPACE_WITHIN_EMPTY_METHOD_PARENTHESES = false;
    doTest();
  }

  @Test
  public void testSpaceBeforeAttributeTrue() {
    getCustomSettings().SPACE_BEFORE_ATTRIBUTE = true;
    doTest();
  }

  @Test
  public void testSpaceBeforeAttributeFalse() {
    getCustomSettings().SPACE_BEFORE_ATTRIBUTE = false;
    doTest();
  }

  @Test
  public void testSpaceInBracesTrue() {
    getSettings().SPACE_WITHIN_BRACES = true;
    doTest();
  }

  @Test
  public void testSpaceInBracesFalse() {
    getSettings().SPACE_WITHIN_BRACES = false;
    doTest();
  }

  @Test
  public void testSpaceAfterCommaTrue() {
    getSettings().SPACE_AFTER_COMMA = true;
    doTest();
  }

  @Test
  public void testSpaceAfterCommaFalse() {
    getSettings().SPACE_AFTER_COMMA = false;
    doTest();
  }

  @Test
  public void testSpaceAroundAssignTrue() {
    getSettings().SPACE_AROUND_ASSIGNMENT_OPERATORS = true;
    doTest();
  }

  @Test
  public void testSpaceAroundAssignFalse() {
    getSettings().SPACE_AROUND_ASSIGNMENT_OPERATORS = false;
    doTest();
  }

  @Test
  public void testSpaceBeforeLeftBraceTrue() {
    getSettings().SPACE_BEFORE_IF_LBRACE = true;
    doTest();
  }

  @Test
  public void testSpaceBeforeLeftBraceFalse() {
    getSettings().SPACE_BEFORE_IF_LBRACE = false;
    doTest();
  }

  @Test
  public void testSpaceBeforeLeftExprBraceTrue() {
    getSettings().SPACE_BEFORE_DO_LBRACE = true;
    doTest();
  }

  @Test
  public void testSpaceBeforeLeftExprBraceFalse() {
    getSettings().SPACE_BEFORE_DO_LBRACE = false;
    doTest();
  }

  @Test
  public void testParamsWrapNot() {
    getSettings().METHOD_PARAMETERS_WRAP = DO_NOT_WRAP;
    doWrapTest();
  }

  @Test
  public void testParamsWrapAlways() {
    getSettings().METHOD_PARAMETERS_WRAP = WRAP_ALWAYS;
    getSettings().ALIGN_MULTILINE_PARAMETERS = true;
    doWrapTest();
  }

  @Test
  public void testParamsWrapAlwaysWithoutAlignment() {
    getSettings().METHOD_PARAMETERS_WRAP = WRAP_ALWAYS;
    getSettings().ALIGN_MULTILINE_PARAMETERS = false;
    doWrapTest();
  }

  @Test
  public void testParamsWrapIfLong() {
    getSettings().METHOD_PARAMETERS_WRAP = WRAP_AS_NEEDED;
    doWrapTest();
  }

  @Test
  public void testParamsWrapChopIfLong() {
    getSettings().METHOD_PARAMETERS_WRAP = WRAP_ON_EVERY_ITEM;
    doWrapTest();
  }

  @Test
  public void testAttributesWrapNot() {
    getCustomSettings().ATTRIBUTES_WRAP = DO_NOT_WRAP;
    doWrapTest();
  }

  @Test
  public void testAttributesWrapAlways() {
    getCustomSettings().ATTRIBUTES_WRAP = WRAP_ALWAYS;
    getCustomSettings().ALIGN_ATTRIBUTES = true;
    doWrapTest();
  }

  @Test
  public void testAttributesWrapAlwaysNotAlign() {
    getCustomSettings().ATTRIBUTES_WRAP = WRAP_ALWAYS;
    getCustomSettings().ALIGN_ATTRIBUTES = false;
    doWrapTest();
  }

  @Test
  public void testAttributesWrapChopIfLong() {
    getCustomSettings().ATTRIBUTES_WRAP = WRAP_ON_EVERY_ITEM;
    doWrapTest();
  }

  @Test
  public void testAttributesWrapIfLong() {
    getCustomSettings().ATTRIBUTES_WRAP = WRAP_AS_NEEDED;
    doWrapTest();
  }

  @Test
  public void testAssignmentAlignmentsNo() {
    getCustomSettings().ALIGN_CONSECUTIVE_ASSIGNMENTS = NO_ALIGN;
    doTest();
  }

  @Test
  public void testAssignmentAlignmentsLines() {
    getCustomSettings().ALIGN_CONSECUTIVE_ASSIGNMENTS = ALIGN_LINES;
    doTest();
  }

  @Test
  public void testAssignmentAlignmentsStatements() {
    getCustomSettings().ALIGN_CONSECUTIVE_ASSIGNMENTS = ALIGN_IN_STATEMENT;
    doTest();
  }

  @Test
  public void testAssignmentOnTheNextLineTrue() {
    getSettings().ASSIGNMENT_WRAP = WRAP_ALWAYS;
    getSettings().PLACE_ASSIGNMENT_SIGN_ON_NEXT_LINE = true;
    doTest();
  }

  @Test
  public void testAssignmentOnTheNextLineFalse() {
    getSettings().ASSIGNMENT_WRAP = WRAP_ALWAYS;
    getSettings().PLACE_ASSIGNMENT_SIGN_ON_NEXT_LINE = false;
    doTest();
  }

  private void doWrapTest() {
    getSettings().RIGHT_MARGIN = 30;
    doTest();
  }

  private void doTest() {
    doFormatTest("functionParameters", getTestName(true), "");
  }
}
