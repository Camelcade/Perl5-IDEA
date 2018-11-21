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

import static com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings.OptionalConstructions.*;

/**
 * Created by hurricup on 13.03.2016.
 */
public class PerlFormatterSpacingTest extends PerlFormatterTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/formatter/perl/spacing";
  }

  public void testScalarUnary() {doFormatTest();}

  public void testUseNoStatements() {doFormatTest();}

  public void testIssue1723() {doFormatTest();}

  public void testIssue1688() {doFormatTest();}

  public void testQwQuotesAndContent() {doFormatTest();}

  public void testSpacingAfterReference() {doFormatTest();}

  public void testAnonArrayArgument() {doFormatTest();}

  public void testAnonHashArgument() {doFormatTest();}

  public void testSequenceLike() {doFormatTest();}

  public void testSmarterHashFormatting() {doFormatTest();}

  public void testMoose() {doFormatTest();}

  public void testPolishing() {doFormatTest();}

  public void testFormatDeclaration() {doFormatTest();}

  public void testNamespaceDefinition() {
    doFormatTest();
  }

  public void testDeclarationsBeforeSignatureTrue() {
    getSettings().SPACE_BEFORE_METHOD_PARENTHESES = true;
    doTestDeclarations();
  }

  public void testDeclarationsBeforeSignatureFalse() {
    getSettings().SPACE_BEFORE_METHOD_PARENTHESES = false;
    doTestDeclarations();
  }

  public void testDeclarationsWithinSignatureTrue() {
    getSettings().SPACE_WITHIN_METHOD_PARENTHESES = true;
    doTestDeclarations();
  }

  public void testDeclarationsWithinSignatureFalse() {
    getSettings().SPACE_WITHIN_METHOD_PARENTHESES = false;
    doTestDeclarations();
  }

  public void testDeclarationsWithinEmptySignatureTrue() {
    getSettings().SPACE_WITHIN_EMPTY_METHOD_PARENTHESES = true;
    doTestDeclarations();
  }

  public void testDeclarationsWithinEmptySignatureFalse() {
    getSettings().SPACE_WITHIN_EMPTY_METHOD_PARENTHESES = false;
    doTestDeclarations();
  }

  public void testDeclarationsBeforeAttributeTrue() {
    getCustomSettings().SPACE_BEFORE_ATTRIBUTE = true;
    doTestDeclarations();
  }

  public void testDeclarationsBeforeAttributeFalse() {
    getCustomSettings().SPACE_BEFORE_ATTRIBUTE = false;
    doTestDeclarations();
  }

  private void doTestDeclarations() {
    doTestSingleSource("declarations");
  }

  public void testPerlTidy() {
    initWithPerlTidy();
    assertNoErrorElements();
    doFormatTestWithoutInitialization(getTestName(true), "");
  }

  public void testNewLineAfterComment() {doFormatTest();}

  public void testCallArguments() {doFormatTest();}

  public void testBlockInGrepMapSort() {doFormatTest();}

  public void testBlockInSubExpr() {
    doFormatTest();
  }

  public void testContinueBlock() {doFormatTest();}

  public void testIssue1607enabled() {
    getSettings().SPACE_BEFORE_IF_PARENTHESES = true;
    doFormatTest();
  }

  public void testIssue1607disabled() {
    getSettings().SPACE_BEFORE_IF_PARENTHESES = false;
    doFormatTest();
  }

  public void testNewlinesBraceStyleSubsSameLine() {
    getCustomSettings().BRACE_STYLE_SUB = SAME_LINE;
    doTestNewlinesCompounds();
  }

  public void testNewlinesBraceStyleSubsNextLine() {
    getCustomSettings().BRACE_STYLE_SUB = NEXT_LINE;
    doTestNewlinesCompounds();
  }

  public void testNewlinesBraceStyleCompoundsSameLine() {
    getCustomSettings().BRACE_STYLE_COMPOUND = SAME_LINE;
    doTestNewlinesCompounds();
  }

  public void testNewlinesBraceStyleCompoundsNextLine() {
    getCustomSettings().BRACE_STYLE_COMPOUND = NEXT_LINE;
    doTestNewlinesCompounds();
  }

  public void testNewlinesBraceStyleNamespaceSameLine() {
    getCustomSettings().BRACE_STYLE_NAMESPACE = SAME_LINE;
    doTestNewlinesCompounds();
  }

  public void testNewlinesBraceStyleNamespaceNextLine() {
    getCustomSettings().BRACE_STYLE_NAMESPACE = NEXT_LINE;
    doTestNewlinesCompounds();
  }

  public void testNewlinesElseOnNewLineTrue() {
    getCustomSettings().ELSE_ON_NEW_LINE = true;
    doTestNewlinesCompounds();
  }

  public void testNewlinesElseOnNewLineFalse() {
    getCustomSettings().ELSE_ON_NEW_LINE = false;
    doTestNewlinesCompounds();
  }

  private void doTestNewlinesCompounds() {
    doTestSingleSource("newlinesCompounds");
  }

  public void testSpacingInsideBlockTrue() {
    getSettings().SPACE_WITHIN_BRACES = true;
    doTestSingleSource("spacingInsideBlock");
  }

  public void testSpacingInsideBlockFalse() {
    getSettings().SPACE_WITHIN_BRACES = false;
    doTestSingleSource("spacingInsideBlock");
  }

  public void testSpacingSemicolonAfterTrue() {
    getSettings().SPACE_AFTER_SEMICOLON = true;
    doTestSemicolon();
  }

  public void testSpacingSemicolonAfterFalse() {
    getSettings().SPACE_AFTER_SEMICOLON = false;
    doTestSemicolon();
  }

  public void testSpacingSemicolonBeforeTrue() {
    getSettings().SPACE_BEFORE_SEMICOLON = true;
    doTestSemicolon();
  }

  public void testSpacingSemicolonBeforeFalse() {
    getSettings().SPACE_BEFORE_SEMICOLON = false;
    doTestSemicolon();
  }

  private void doTestSemicolon() {
    doTestSingleSource("spacingSemicolonInIterator");
  }

  public void testSpacingTernaryBeforeQuestTrue() {
    getSettings().SPACE_BEFORE_QUEST = true;
    doTestTernary();
  }

  public void testSpacingTernaryBeforeQuestFalse() {
    getSettings().SPACE_BEFORE_QUEST = false;
    doTestTernary();
  }

  public void testSpacingTernaryAfterQuestTrue() {
    getSettings().SPACE_AFTER_QUEST = true;
    doTestTernary();
  }

  public void testSpacingTernaryAfterQuestFalse() {
    getSettings().SPACE_AFTER_QUEST = false;
    doTestTernary();
  }

  public void testSpacingTernaryAfterColonTrue() {
    getSettings().SPACE_AFTER_COLON = true;
    doTestTernary();
  }

  public void testSpacingTernaryAfterColonFalse() {
    getSettings().SPACE_AFTER_COLON = false;
    doTestTernary();
  }

  public void testSpacingTernaryBeforeColonTrue() {
    getSettings().SPACE_BEFORE_COLON = true;
    doTestTernary();
  }

  public void testSpacingTernaryBeforeColonFalse() {
    getSettings().SPACE_BEFORE_COLON = false;
    doTestTernary();
  }

  private void doTestTernary() {
    doTestSingleSource("spacingTernary");
  }

  public void testSpacingBeforeCommaTrue() {
    getSettings().SPACE_BEFORE_COMMA = true;
    doTestComma();
  }

  public void testSpacingBeforeCommaFalse() {
    getSettings().SPACE_BEFORE_COMMA = false;
    doTestComma();
  }

  public void testSpacingAfterCommaTrue() {
    getSettings().SPACE_AFTER_COMMA = true;
    doTestComma();
  }

  public void testSpacingAfterCommaFalse() {
    getSettings().SPACE_AFTER_COMMA = false;
    doTestComma();
  }

  private void doTestComma() {
    doTestSingleSource("spacingComma");
  }

  private void doTestFormatOperators() {
    doTestSingleSource("spacingAroundOperators");
  }

  public void testSpacingAroundAdditiveTrue() {
    getSettings().SPACE_AROUND_ADDITIVE_OPERATORS = true;
    doTestFormatOperators();
  }

  public void testSpacingAroundAdditiveFalse() {
    getSettings().SPACE_AROUND_ADDITIVE_OPERATORS = false;
    doTestFormatOperators();
  }

  public void testSpacingAroundAssignmentTrue() {
    getSettings().SPACE_AROUND_ASSIGNMENT_OPERATORS = true;
    doTestFormatOperators();
  }

  public void testSpacingAroundAssignmentFalse() {
    getSettings().SPACE_AROUND_ASSIGNMENT_OPERATORS = false;
    doTestFormatOperators();
  }

  public void testSpacingAroundBitwiseTrue() {
    getSettings().SPACE_AROUND_BITWISE_OPERATORS = true;
    doTestFormatOperators();
  }

  public void testSpacingAroundBitwiseFalse() {
    getSettings().SPACE_AROUND_BITWISE_OPERATORS = false;
    doTestFormatOperators();
  }

  public void testSpacingAroundEqualityTrue() {
    getSettings().SPACE_AROUND_EQUALITY_OPERATORS = true;
    doTestFormatOperators();
  }

  public void testSpacingAroundEqualityFalse() {
    getSettings().SPACE_AROUND_EQUALITY_OPERATORS = false;
    doTestFormatOperators();
  }

  public void testSpacingAroundLogicalTrue() {
    getSettings().SPACE_AROUND_LOGICAL_OPERATORS = true;
    doTestFormatOperators();
  }

  public void testSpacingAroundLogicalFalse() {
    getSettings().SPACE_AROUND_LOGICAL_OPERATORS = false;
    doTestFormatOperators();
  }

  public void testSpacingAroundMultiplicativeTrue() {
    getSettings().SPACE_AROUND_MULTIPLICATIVE_OPERATORS = true;
    doTestFormatOperators();
  }

  public void testSpacingAroundMultiplicativeFalse() {
    getSettings().SPACE_AROUND_MULTIPLICATIVE_OPERATORS = false;
    doTestFormatOperators();
  }

  public void testSpacingAroundRelationalTrue() {
    getSettings().SPACE_AROUND_RELATIONAL_OPERATORS = true;
    doTestFormatOperators();
  }

  public void testSpacingAroundRelationalFalse() {
    getSettings().SPACE_AROUND_RELATIONAL_OPERATORS = false;
    doTestFormatOperators();
  }

  public void testSpacingAroundShiftTrue() {
    getSettings().SPACE_AROUND_SHIFT_OPERATORS = true;
    doTestFormatOperators();
  }

  public void testSpacingAroundShiftFalse() {
    getSettings().SPACE_AROUND_SHIFT_OPERATORS = false;
    doTestFormatOperators();
  }

  public void testSpacingAroundUnaryTrue() {
    getSettings().SPACE_AROUND_UNARY_OPERATOR = true;
    doTestFormatOperators();
  }

  public void testSpacingAroundUnaryFalse() {
    getSettings().SPACE_AROUND_UNARY_OPERATOR = false;
    doTestFormatOperators();
  }

  public void testSpacingBeforeCompoundLbraceTrue() {
    getSettings().SPACE_BEFORE_IF_LBRACE = true;
    doCompoundSpacingTest();
  }

  public void testSpacingBeforeCompoundLbraceFalse() {
    getSettings().SPACE_BEFORE_IF_LBRACE = false;
    doCompoundSpacingTest();
  }

  public void testSpacingBeforeElseTrue() {
    getSettings().SPACE_BEFORE_ELSE_KEYWORD = true;
    doCompoundSpacingTest();
  }

  public void testSpacingBeforeElseFalse() {
    getSettings().SPACE_BEFORE_ELSE_KEYWORD = false;
    doCompoundSpacingTest();
  }

  private void doCompoundSpacingTest() {
    getCustomSettings().BRACE_STYLE_COMPOUND = SAME_LINE;
    getCustomSettings().ELSE_ON_NEW_LINE = false;
    doFormatTest();
  }

  public void testSpacingBeforeTermBraceTrue() {
    getSettings().SPACE_BEFORE_DO_LBRACE = true;
    doFormatTest();
  }

  public void testSpacingBeforeTermBraceFalse() {
    getSettings().SPACE_BEFORE_DO_LBRACE = false;
    doFormatTest();
  }

  public void testSpacesWithinParenthesesTrue() {
    getSettings().SPACE_WITHIN_PARENTHESES = true;
    doFormatTest();
  }

  public void testSpacesWithinParenthesesFalse() {
    getSettings().SPACE_WITHIN_PARENTHESES = false;
    doFormatTest();
  }

  public void testSpacesAroundRangeTrue() {
    getCustomSettings().SPACE_AROUND_RANGE_OPERATORS = true;
    doFormatTest();
  }

  public void testSpacesAroundRangeFalse() {
    getCustomSettings().SPACE_AROUND_RANGE_OPERATORS = false;
    doFormatTest();
  }

  public void testSpacingConcatTrue() {
    getCustomSettings().SPACE_AROUND_CONCAT_OPERATOR = true;
    doFormatTest();
  }

  public void testSpacingConcatFalse() {
    getCustomSettings().SPACE_AROUND_CONCAT_OPERATOR = false;
    doFormatTest();
  }

  public void testSpacesWithinArrayTrue() {
    getCustomSettings().SPACES_WITHIN_ANON_ARRAY = true;
    doFormatTest();
  }

  public void testSpacesWithinArrayFalse() {
    getCustomSettings().SPACES_WITHIN_ANON_ARRAY = false;
    doFormatTest();
  }

  public void testSpacesWithinHashTrue() {
    getCustomSettings().SPACES_WITHIN_ANON_HASH = true;
    doFormatTest();
  }

  public void testSpacesWithinHashFalse() {
    getCustomSettings().SPACES_WITHIN_ANON_HASH = false;
    doFormatTest();
  }

  public void testSpaceAfterMyTrue() {
    getCustomSettings().SPACE_AFTER_VARIABLE_DECLARATION_KEYWORD = true;
    doFormatTest();
  }

  public void testSpaceAfterMyFalse() {
    getCustomSettings().SPACE_AFTER_VARIABLE_DECLARATION_KEYWORD = false;
    doFormatTest();
  }

  public void testMainFormatAsIs() {doTestMainFormat(WHATEVER);}

  public void testMainFormatLong() {doTestMainFormat(FORCE);}

  public void testMainFormatShort() {doTestMainFormat(SUPPRESS);}

  private void doTestMainFormat(int value) {
    getCustomSettings().MAIN_FORMAT = value;
    doFormatTest();
  }

  public void testStatementModifierParensAsIs() {doTestStatementModifierParens(WHATEVER);}

  public void testStatementModifierParensForce() {doTestStatementModifierParens(FORCE);}

  public void testStatementModifierParensSuppress() {doTestStatementModifierParens(SUPPRESS);}

  private void doTestStatementModifierParens(int value) {
    getCustomSettings().OPTIONAL_PARENTHESES = value;
    doFormatTest();
  }


  public void testSimpleDereferenceAsIs() {doTestSimpleDereference(WHATEVER);}

  public void testSimpleDereferenceBraced() {doTestSimpleDereference(FORCE);}

  public void testSimpleDereferenceUnbraced() {doTestSimpleDereference(SUPPRESS);}

  private void doTestSimpleDereference(int value) {
    getCustomSettings().OPTIONAL_DEREFERENCE_SIMPLE = value;
    doFormatTest();
  }


  public void testHashRefElementAsIs() {doTestHashRefElementFormat(WHATEVER);}

  public void testHashRefElementDoubleBuck() {doTestHashRefElementFormat(FORCE);}

  public void testHashRefElementDereference() {doTestHashRefElementFormat(SUPPRESS);}

  private void doTestHashRefElementFormat(int value) {
    getCustomSettings().OPTIONAL_DEREFERENCE_HASHREF_ELEMENT = value;
    doFormatTest();
  }

  public void testIndexesDereferenceAsIs() {doTestIndexesDereference(WHATEVER);}

  public void testIndexesDereferenceForce() {doTestIndexesDereference(FORCE);}

  public void testIndexesDereferenceSuppress() {doTestIndexesDereference(SUPPRESS);}

  private void doTestIndexesDereference(int value) {
    getCustomSettings().OPTIONAL_DEREFERENCE = value;
    doFormatTest();
  }

  public void testQuoteHeredocOpenerAsIs() {doTestQuoteHeredocOpener(WHATEVER);}

  public void testQuoteHeredocOpenerForce() {doTestQuoteHeredocOpener(FORCE);}

  public void testQuoteHeredocOpenerSuppress() {doTestQuoteHeredocOpener(SUPPRESS);}

  private void doTestQuoteHeredocOpener(int value) {
    getCustomSettings().OPTIONAL_QUOTES_HEREDOC_OPENER = value;
    doFormatTest();
  }

  public void testQuoteHashIndexAsIs() {doTestQuoteHashIndex(WHATEVER);}

  public void testQuoteHashIndexForce() {doTestQuoteHashIndex(FORCE);}

  public void testQuoteHashIndexSuppress() {doTestQuoteHashIndex(SUPPRESS);}

  private void doTestQuoteHashIndex(int value) {
    getCustomSettings().OPTIONAL_QUOTES_HASH_INDEX = value;
    doFormatTest();
  }

  public void testFatCommaAsIs() {doTestFatComma(WHATEVER);}

  public void testFatCommaForce() {doTestFatComma(FORCE);}

  public void testFatCommaSuppress() {doTestFatComma(SUPPRESS);}

  private void doTestFatComma(int value) {
    getCustomSettings().OPTIONAL_QUOTES = value;
    doFormatTest();
  }

  public void testIssue1482() {
    doFormatTest();
  }

  public void testCallArgumentsTrue() {
    getSettings().SPACE_WITHIN_METHOD_CALL_PARENTHESES = true;
    doFormatTest();
  }

  public void testCallArgumentsFalse() {
    getSettings().SPACE_WITHIN_METHOD_CALL_PARENTHESES = false;
    doFormatTest();
  }

  public void testSpacingInConditionFalse() {
    getSettings().SPACE_WITHIN_IF_PARENTHESES = false;
    doFormatTest();
  }

  public void testSpacingInConditionTrue() {
    getSettings().SPACE_WITHIN_IF_PARENTHESES = true;
    doFormatTest();
  }

  public void testSpacingInQwFalse() {
    getCustomSettings().SPACE_WITHIN_QW_QUOTES = false;
    doFormatTest();
  }

  public void testSpacingInQwTrue() {
    getCustomSettings().SPACE_WITHIN_QW_QUOTES = true;
    doFormatTest();
  }


  public void testSpacingBeforeConditionTrue() {
    getSettings().SPACE_BEFORE_IF_PARENTHESES = true;
    doFormatTest();
  }

  public void testSpacingBeforeConditionFalse() {
    getSettings().SPACE_BEFORE_IF_PARENTHESES = false;
    doFormatTest();
  }



  public void testExceptionClass() {doFormatTest();}

  public void testIndentedHeredoc() {
    doFormatTest();
  }

  public void testIndentedHeredocShiftLeft() {doFormatTest();}

  public void testIndentedHeredocShiftLeftEmpty() {doFormatTest();}

  public void testIndentedHeredocShiftLeftWithBadString() {doFormatTest();}

  public void testIndentedHeredocShiftLeftWithLeadingNewLine() {doFormatTest();}

  public void testIndentedHeredocShiftRight() {doFormatTest();}

  public void testIndentedHeredocShiftRightEmpty() {doFormatTest();}

  public void testIndentedHeredocShiftRightWithBadString() {doFormatTest();}

  public void testIndentedHeredocShiftRightWithLeadingNewLine() {doFormatTest();}

  public void testIssue1782() {doFormatTest();}
}
