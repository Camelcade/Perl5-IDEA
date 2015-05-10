// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.lexer;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.perl5.lang.perl.PerlElementType;
import com.perl5.lang.perl.PerlTokenType;
import com.perl5.lang.perl.psi.impl.*;

public interface PerlElementTypes {

  IElementType BLOCK = new PerlElementType("BLOCK");
  IElementType BLOCK_COMPOUND = new PerlElementType("BLOCK_COMPOUND");
  IElementType CALLABLE = new PerlElementType("CALLABLE");
  IElementType COMPILE_REGEX = new PerlElementType("COMPILE_REGEX");
  IElementType COMPOUND_STATEMENT = new PerlElementType("COMPOUND_STATEMENT");
  IElementType DO_TERM = new PerlElementType("DO_TERM");
  IElementType EVAL_TERM = new PerlElementType("EVAL_TERM");
  IElementType EXPR = new PerlElementType("EXPR");
  IElementType FILE_READ_TERM = new PerlElementType("FILE_READ_TERM");
  IElementType FOREACH_COMPOUND = new PerlElementType("FOREACH_COMPOUND");
  IElementType FOREACH_STATEMENT_MODIFIER = new PerlElementType("FOREACH_STATEMENT_MODIFIER");
  IElementType FOR_COMPOUND = new PerlElementType("FOR_COMPOUND");
  IElementType FOR_STATEMENT_MODIFIER = new PerlElementType("FOR_STATEMENT_MODIFIER");
  IElementType GIVEN_COMPOUND = new PerlElementType("GIVEN_COMPOUND");
  IElementType GREP_TERM = new PerlElementType("GREP_TERM");
  IElementType IF_COMPOUND = new PerlElementType("IF_COMPOUND");
  IElementType IF_STATEMENT_MODIFIER = new PerlElementType("IF_STATEMENT_MODIFIER");
  IElementType LABEL = new PerlElementType("LABEL");
  IElementType LABEL_DECLARATION = new PerlElementType("LABEL_DECLARATION");
  IElementType LAST_STATEMENT = new PerlElementType("LAST_STATEMENT");
  IElementType MAP_TERM = new PerlElementType("MAP_TERM");
  IElementType MATCH_REGEX = new PerlElementType("MATCH_REGEX");
  IElementType NEXT_STATEMENT = new PerlElementType("NEXT_STATEMENT");
  IElementType NO_STATEMENT = new PerlElementType("NO_STATEMENT");
  IElementType OPEN_FILE = new PerlElementType("OPEN_FILE");
  IElementType OPEN_HANDLE = new PerlElementType("OPEN_HANDLE");
  IElementType OPEN_MODE = new PerlElementType("OPEN_MODE");
  IElementType OPEN_REF = new PerlElementType("OPEN_REF");
  IElementType OP_10_EXPR = new PerlElementType("OP_10_EXPR");
  IElementType OP_11_EXPR = new PerlElementType("OP_11_EXPR");
  IElementType OP_12_EXPR = new PerlElementType("OP_12_EXPR");
  IElementType OP_13_EXPR = new PerlElementType("OP_13_EXPR");
  IElementType OP_14_EXPR = new PerlElementType("OP_14_EXPR");
  IElementType OP_15_EXPR = new PerlElementType("OP_15_EXPR");
  IElementType OP_16_EXPR = new PerlElementType("OP_16_EXPR");
  IElementType OP_17_EXPR = new PerlElementType("OP_17_EXPR");
  IElementType OP_18_EXPR = new PerlElementType("OP_18_EXPR");
  IElementType OP_19_EXPR = new PerlElementType("OP_19_EXPR");
  IElementType OP_1_EXPR = new PerlElementType("OP_1_EXPR");
  IElementType OP_20_EXPR = new PerlElementType("OP_20_EXPR");
  IElementType OP_21_EXPR = new PerlElementType("OP_21_EXPR");
  IElementType OP_22_EXPR = new PerlElementType("OP_22_EXPR");
  IElementType OP_23_EXPR = new PerlElementType("OP_23_EXPR");
  IElementType OP_24_EXPR = new PerlElementType("OP_24_EXPR");
  IElementType OP_2_EXPR = new PerlElementType("OP_2_EXPR");
  IElementType OP_3_PREF_EXPR = new PerlElementType("OP_3_PREF_EXPR");
  IElementType OP_3_SUFF_EXPR = new PerlElementType("OP_3_SUFF_EXPR");
  IElementType OP_4_EXPR = new PerlElementType("OP_4_EXPR");
  IElementType OP_5_OTHER_EXPR = new PerlElementType("OP_5_OTHER_EXPR");
  IElementType OP_5_REF_EXPR = new PerlElementType("OP_5_REF_EXPR");
  IElementType OP_6_EXPR = new PerlElementType("OP_6_EXPR");
  IElementType OP_7_EXPR = new PerlElementType("OP_7_EXPR");
  IElementType OP_8_EXPR = new PerlElementType("OP_8_EXPR");
  IElementType OP_9_EXPR = new PerlElementType("OP_9_EXPR");
  IElementType PACKAGE_NAMESPACE = new PerlElementType("PACKAGE_NAMESPACE");
  IElementType PERL_REGEX = new PerlElementType("PERL_REGEX");
  IElementType PERL_REGEX_MODIFIERS = new PerlElementType("PERL_REGEX_MODIFIERS");
  IElementType REDO_STATEMENT = new PerlElementType("REDO_STATEMENT");
  IElementType REFERENCE_VALUE = new PerlElementType("REFERENCE_VALUE");
  IElementType REPLACEMENT_REGEX = new PerlElementType("REPLACEMENT_REGEX");
  IElementType REQUIRE_STATEMENT = new PerlElementType("REQUIRE_STATEMENT");
  IElementType SORT_TERM = new PerlElementType("SORT_TERM");
  IElementType SUB_DECLARATION = new PerlElementType("SUB_DECLARATION");
  IElementType SUB_DEFINITION = new PerlElementType("SUB_DEFINITION");
  IElementType TR_MODIFIERS = new PerlElementType("TR_MODIFIERS");
  IElementType TR_REGEX = new PerlElementType("TR_REGEX");
  IElementType TR_REPLACEMENTLIST = new PerlElementType("TR_REPLACEMENTLIST");
  IElementType TR_SEARCHLIST = new PerlElementType("TR_SEARCHLIST");
  IElementType UNDEF_STATEMENT = new PerlElementType("UNDEF_STATEMENT");
  IElementType UNLESS_COMPOUND = new PerlElementType("UNLESS_COMPOUND");
  IElementType UNLESS_STATEMENT_MODIFIER = new PerlElementType("UNLESS_STATEMENT_MODIFIER");
  IElementType UNTIL_COMPOUND = new PerlElementType("UNTIL_COMPOUND");
  IElementType UNTIL_STATEMENT_MODIFIER = new PerlElementType("UNTIL_STATEMENT_MODIFIER");
  IElementType USE_STATEMENT = new PerlElementType("USE_STATEMENT");
  IElementType VARIABLE_DECLARATION_GLOBAL = new PerlElementType("VARIABLE_DECLARATION_GLOBAL");
  IElementType VARIABLE_DECLARATION_LEXICAL = new PerlElementType("VARIABLE_DECLARATION_LEXICAL");
  IElementType VARIABLE_DECLARATION_LOCAL = new PerlElementType("VARIABLE_DECLARATION_LOCAL");
  IElementType WHEN_STATEMENT_MODIFIER = new PerlElementType("WHEN_STATEMENT_MODIFIER");
  IElementType WHILE_COMPOUND = new PerlElementType("WHILE_COMPOUND");
  IElementType WHILE_STATEMENT_MODIFIER = new PerlElementType("WHILE_STATEMENT_MODIFIER");

  IElementType PERL_ARRAY = new PerlTokenType("PERL_ARRAY");
  IElementType PERL_ARROW_COMMA = new PerlTokenType("=>");
  IElementType PERL_BAREWORD = new PerlTokenType("PERL_BAREWORD");
  IElementType PERL_COLON = new PerlTokenType(":");
  IElementType PERL_COMMA = new PerlTokenType(",");
  IElementType PERL_COMMENT = new PerlTokenType("PERL_COMMENT");
  IElementType PERL_COMMENT_BLOCK = new PerlTokenType("PERL_COMMENT_BLOCK");
  IElementType PERL_DEPACKAGE = new PerlTokenType("::");
  IElementType PERL_DEREFERENCE = new PerlTokenType("->");
  IElementType PERL_FILEHANDLE = new PerlTokenType("PERL_FILEHANDLE");
  IElementType PERL_FUNCTION = new PerlTokenType("PERL_FUNCTION");
  IElementType PERL_GLOB = new PerlTokenType("PERL_GLOB");
  IElementType PERL_HADLE = new PerlTokenType("PERL_HANDLE");
  IElementType PERL_HASH = new PerlTokenType("PERL_HASH");
  IElementType PERL_KEYWORD = new PerlTokenType("PERL_KEYWORD");
  IElementType PERL_LANGLE = new PerlTokenType("<");
  IElementType PERL_LBRACE = new PerlTokenType("{");
  IElementType PERL_LBRACK = new PerlTokenType("[");
  IElementType PERL_LPAREN = new PerlTokenType("(");
  IElementType PERL_NUMBER = new PerlTokenType("PERL_NUMBER");
  IElementType PERL_NUMBER_VERSION = new PerlTokenType("PERL_NUMBER_VERSION");
  IElementType PERL_OPERATOR = new PerlTokenType("PERL_OPERATOR");
  IElementType PERL_OPERATOR_FILETEST = new PerlTokenType("PERL_OPERATOR_FILETEST");
  IElementType PERL_OPERATOR_UNARY = new PerlTokenType("PERL_OPERATOR_UNARY");
  IElementType PERL_PACKAGE = new PerlTokenType("PERL_PACKAGE");
  IElementType PERL_POD = new PerlTokenType("PERL_POD");
  IElementType PERL_QUOTE = new PerlTokenType("\"");
  IElementType PERL_RANGLE = new PerlTokenType(">");
  IElementType PERL_RBRACE = new PerlTokenType("}");
  IElementType PERL_RBRACK = new PerlTokenType("]");
  IElementType PERL_REGEX_MODIFIER = new PerlTokenType("PERL_REGEX_MODIFIER");
  IElementType PERL_REGEX_QUOTE = new PerlTokenType("PERL_REGEX_QUOTE");
  IElementType PERL_REGEX_TOKEN = new PerlTokenType("PERL_REGEX_TOKEN");
  IElementType PERL_RPAREN = new PerlTokenType(")");
  IElementType PERL_SCALAR = new PerlTokenType("PERL_SCALAR");
  IElementType PERL_SEMI = new PerlTokenType(";");
  IElementType PERL_SIGIL_ARRAY = new PerlTokenType("@");
  IElementType PERL_SIGIL_HASH = new PerlTokenType("%");
  IElementType PERL_SIGIL_SCALAR = new PerlTokenType("$");
  IElementType PERL_STRING = new PerlTokenType("PERL_STRING");
  IElementType PERL_STRING_CONTENT = new PerlTokenType("PERL_STRING_CONTENT");
  IElementType PERL_STRING_MULTILINE = new PerlTokenType("PERL_STRING_MULTILINE");
  IElementType PERL_STRING_MULTILINE_END = new PerlTokenType("PERL_STRING_MULTILINE_END");
  IElementType PERL_TAG = new PerlTokenType("PERL_TAG");
  IElementType PERL_VERSION = new PerlTokenType("PERL_VERSION");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == BLOCK) {
        return new PerlBlockImpl(node);
      }
      else if (type == BLOCK_COMPOUND) {
        return new PerlBlockCompoundImpl(node);
      }
      else if (type == CALLABLE) {
        return new PerlCallableImpl(node);
      }
      else if (type == COMPILE_REGEX) {
        return new PerlCompileRegexImpl(node);
      }
      else if (type == COMPOUND_STATEMENT) {
        return new PerlCompoundStatementImpl(node);
      }
      else if (type == DO_TERM) {
        return new PerlDoTermImpl(node);
      }
      else if (type == EVAL_TERM) {
        return new PerlEvalTermImpl(node);
      }
      else if (type == EXPR) {
        return new PerlExprImpl(node);
      }
      else if (type == FILE_READ_TERM) {
        return new PerlFileReadTermImpl(node);
      }
      else if (type == FOREACH_COMPOUND) {
        return new PerlForeachCompoundImpl(node);
      }
      else if (type == FOREACH_STATEMENT_MODIFIER) {
        return new PerlForeachStatementModifierImpl(node);
      }
      else if (type == FOR_COMPOUND) {
        return new PerlForCompoundImpl(node);
      }
      else if (type == FOR_STATEMENT_MODIFIER) {
        return new PerlForStatementModifierImpl(node);
      }
      else if (type == GIVEN_COMPOUND) {
        return new PerlGivenCompoundImpl(node);
      }
      else if (type == GREP_TERM) {
        return new PerlGrepTermImpl(node);
      }
      else if (type == IF_COMPOUND) {
        return new PerlIfCompoundImpl(node);
      }
      else if (type == IF_STATEMENT_MODIFIER) {
        return new PerlIfStatementModifierImpl(node);
      }
      else if (type == LABEL) {
        return new PerlLabelImpl(node);
      }
      else if (type == LABEL_DECLARATION) {
        return new PerlLabelDeclarationImpl(node);
      }
      else if (type == LAST_STATEMENT) {
        return new PerlLastStatementImpl(node);
      }
      else if (type == MAP_TERM) {
        return new PerlMapTermImpl(node);
      }
      else if (type == MATCH_REGEX) {
        return new PerlMatchRegexImpl(node);
      }
      else if (type == NEXT_STATEMENT) {
        return new PerlNextStatementImpl(node);
      }
      else if (type == NO_STATEMENT) {
        return new PerlNoStatementImpl(node);
      }
      else if (type == OPEN_FILE) {
        return new PerlOpenFileImpl(node);
      }
      else if (type == OPEN_HANDLE) {
        return new PerlOpenHandleImpl(node);
      }
      else if (type == OPEN_MODE) {
        return new PerlOpenModeImpl(node);
      }
      else if (type == OPEN_REF) {
        return new PerlOpenRefImpl(node);
      }
      else if (type == OP_10_EXPR) {
        return new PerlOp10ExprImpl(node);
      }
      else if (type == OP_11_EXPR) {
        return new PerlOp11ExprImpl(node);
      }
      else if (type == OP_12_EXPR) {
        return new PerlOp12ExprImpl(node);
      }
      else if (type == OP_13_EXPR) {
        return new PerlOp13ExprImpl(node);
      }
      else if (type == OP_14_EXPR) {
        return new PerlOp14ExprImpl(node);
      }
      else if (type == OP_15_EXPR) {
        return new PerlOp15ExprImpl(node);
      }
      else if (type == OP_16_EXPR) {
        return new PerlOp16ExprImpl(node);
      }
      else if (type == OP_17_EXPR) {
        return new PerlOp17ExprImpl(node);
      }
      else if (type == OP_18_EXPR) {
        return new PerlOp18ExprImpl(node);
      }
      else if (type == OP_19_EXPR) {
        return new PerlOp19ExprImpl(node);
      }
      else if (type == OP_1_EXPR) {
        return new PerlOp1ExprImpl(node);
      }
      else if (type == OP_20_EXPR) {
        return new PerlOp20ExprImpl(node);
      }
      else if (type == OP_21_EXPR) {
        return new PerlOp21ExprImpl(node);
      }
      else if (type == OP_22_EXPR) {
        return new PerlOp22ExprImpl(node);
      }
      else if (type == OP_23_EXPR) {
        return new PerlOp23ExprImpl(node);
      }
      else if (type == OP_24_EXPR) {
        return new PerlOp24ExprImpl(node);
      }
      else if (type == OP_2_EXPR) {
        return new PerlOp2ExprImpl(node);
      }
      else if (type == OP_3_PREF_EXPR) {
        return new PerlOp3PrefExprImpl(node);
      }
      else if (type == OP_3_SUFF_EXPR) {
        return new PerlOp3SuffExprImpl(node);
      }
      else if (type == OP_4_EXPR) {
        return new PerlOp4ExprImpl(node);
      }
      else if (type == OP_5_OTHER_EXPR) {
        return new PerlOp5OtherExprImpl(node);
      }
      else if (type == OP_5_REF_EXPR) {
        return new PerlOp5RefExprImpl(node);
      }
      else if (type == OP_6_EXPR) {
        return new PerlOp6ExprImpl(node);
      }
      else if (type == OP_7_EXPR) {
        return new PerlOp7ExprImpl(node);
      }
      else if (type == OP_8_EXPR) {
        return new PerlOp8ExprImpl(node);
      }
      else if (type == OP_9_EXPR) {
        return new PerlOp9ExprImpl(node);
      }
      else if (type == PACKAGE_NAMESPACE) {
        return new PerlPackageNamespaceImpl(node);
      }
      else if (type == PERL_REGEX) {
        return new PerlPerlRegexImpl(node);
      }
      else if (type == PERL_REGEX_MODIFIERS) {
        return new PerlPerlRegexModifiersImpl(node);
      }
      else if (type == REDO_STATEMENT) {
        return new PerlRedoStatementImpl(node);
      }
      else if (type == REFERENCE_VALUE) {
        return new PerlReferenceValueImpl(node);
      }
      else if (type == REPLACEMENT_REGEX) {
        return new PerlReplacementRegexImpl(node);
      }
      else if (type == REQUIRE_STATEMENT) {
        return new PerlRequireStatementImpl(node);
      }
      else if (type == SORT_TERM) {
        return new PerlSortTermImpl(node);
      }
      else if (type == SUB_DECLARATION) {
        return new PerlSubDeclarationImpl(node);
      }
      else if (type == SUB_DEFINITION) {
        return new PerlSubDefinitionImpl(node);
      }
      else if (type == TR_MODIFIERS) {
        return new PerlTrModifiersImpl(node);
      }
      else if (type == TR_REGEX) {
        return new PerlTrRegexImpl(node);
      }
      else if (type == TR_REPLACEMENTLIST) {
        return new PerlTrReplacementlistImpl(node);
      }
      else if (type == TR_SEARCHLIST) {
        return new PerlTrSearchlistImpl(node);
      }
      else if (type == UNDEF_STATEMENT) {
        return new PerlUndefStatementImpl(node);
      }
      else if (type == UNLESS_COMPOUND) {
        return new PerlUnlessCompoundImpl(node);
      }
      else if (type == UNLESS_STATEMENT_MODIFIER) {
        return new PerlUnlessStatementModifierImpl(node);
      }
      else if (type == UNTIL_COMPOUND) {
        return new PerlUntilCompoundImpl(node);
      }
      else if (type == UNTIL_STATEMENT_MODIFIER) {
        return new PerlUntilStatementModifierImpl(node);
      }
      else if (type == USE_STATEMENT) {
        return new PerlUseStatementImpl(node);
      }
      else if (type == VARIABLE_DECLARATION_GLOBAL) {
        return new PerlVariableDeclarationGlobalImpl(node);
      }
      else if (type == VARIABLE_DECLARATION_LEXICAL) {
        return new PerlVariableDeclarationLexicalImpl(node);
      }
      else if (type == VARIABLE_DECLARATION_LOCAL) {
        return new PerlVariableDeclarationLocalImpl(node);
      }
      else if (type == WHEN_STATEMENT_MODIFIER) {
        return new PerlWhenStatementModifierImpl(node);
      }
      else if (type == WHILE_COMPOUND) {
        return new PerlWhileCompoundImpl(node);
      }
      else if (type == WHILE_STATEMENT_MODIFIER) {
        return new PerlWhileStatementModifierImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
