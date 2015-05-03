// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.lexer;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.perl5.lang.perl.PerlElementType;
import com.perl5.lang.perl.PerlTokenType;
import com.perl5.lang.perl.psi.impl.*;

public interface PerlElementTypes {

  IElementType ARRAY = new PerlElementType("ARRAY");
  IElementType BLOCK = new PerlElementType("BLOCK");
  IElementType BLOCK_BLOCK = new PerlElementType("BLOCK_BLOCK");
  IElementType BLOCK_CONDITIONAL = new PerlElementType("BLOCK_CONDITIONAL");
  IElementType CALEE = new PerlElementType("CALEE");
  IElementType CALL_ARGUMENTS = new PerlElementType("CALL_ARGUMENTS");
  IElementType CALL_LEFTWARD = new PerlElementType("CALL_LEFTWARD");
  IElementType CALL_RIGHTWARD = new PerlElementType("CALL_RIGHTWARD");
  IElementType CODE_LINE = new PerlElementType("CODE_LINE");
  IElementType EVAL = new PerlElementType("EVAL");
  IElementType EXPR = new PerlElementType("EXPR");
  IElementType FILE_ITEM = new PerlElementType("FILE_ITEM");
  IElementType FOR_BLOCK = new PerlElementType("FOR_BLOCK");
  IElementType FOR_BLOCK_ARGUMENTS = new PerlElementType("FOR_BLOCK_ARGUMENTS");
  IElementType GIVEN_BLOCK = new PerlElementType("GIVEN_BLOCK");
  IElementType GREP_EXPR = new PerlElementType("GREP_EXPR");
  IElementType HASH = new PerlElementType("HASH");
  IElementType IF_BLOCK = new PerlElementType("IF_BLOCK");
  IElementType IF_BLOCK_ELSE = new PerlElementType("IF_BLOCK_ELSE");
  IElementType IF_BLOCK_ELSIF = new PerlElementType("IF_BLOCK_ELSIF");
  IElementType IF_POSTFIX = new PerlElementType("IF_POSTFIX");
  IElementType KEYS_ARGS = new PerlElementType("KEYS_ARGS");
  IElementType KEYS_EXPR = new PerlElementType("KEYS_EXPR");
  IElementType LAST_EXPR = new PerlElementType("LAST_EXPR");
  IElementType LIST_EXPR = new PerlElementType("LIST_EXPR");
  IElementType LOCAL_DEFINITION = new PerlElementType("LOCAL_DEFINITION");
  IElementType MY_DEFINITION = new PerlElementType("MY_DEFINITION");
  IElementType OBJECT_METHOD = new PerlElementType("OBJECT_METHOD");
  IElementType OBJECT_METHOD_OBJECT = new PerlElementType("OBJECT_METHOD_OBJECT");
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
  IElementType OP_5_EXPR = new PerlElementType("OP_5_EXPR");
  IElementType OP_6_EXPR = new PerlElementType("OP_6_EXPR");
  IElementType OP_7_EXPR = new PerlElementType("OP_7_EXPR");
  IElementType OP_8_EXPR = new PerlElementType("OP_8_EXPR");
  IElementType OP_9_EXPR = new PerlElementType("OP_9_EXPR");
  IElementType OUR_DEFINITION = new PerlElementType("OUR_DEFINITION");
  IElementType PACKAGE_DEFINITION = new PerlElementType("PACKAGE_DEFINITION");
  IElementType PACKAGE_METHOD = new PerlElementType("PACKAGE_METHOD");
  IElementType PACKAGE_NO = new PerlElementType("PACKAGE_NO");
  IElementType PACKAGE_REQUIRE = new PerlElementType("PACKAGE_REQUIRE");
  IElementType PACKAGE_USE = new PerlElementType("PACKAGE_USE");
  IElementType PACKAGE_USE_ARGUMENTS = new PerlElementType("PACKAGE_USE_ARGUMENTS");
  IElementType PERL_REGEX = new PerlElementType("PERL_REGEX");
  IElementType PERL_REGEX_MODIFIERS = new PerlElementType("PERL_REGEX_MODIFIERS");
  IElementType PERL_VERSION = new PerlElementType("PERL_VERSION");
  IElementType QW_EXPR = new PerlElementType("QW_EXPR");
  IElementType REGEX = new PerlElementType("REGEX");
  IElementType RETURN_EXPR = new PerlElementType("RETURN_EXPR");
  IElementType SCALAR = new PerlElementType("SCALAR");
  IElementType SHIFT_EXPR = new PerlElementType("SHIFT_EXPR");
  IElementType SORT_EXPR = new PerlElementType("SORT_EXPR");
  IElementType SORT_OP_ARGS = new PerlElementType("SORT_OP_ARGS");
  IElementType SUB_BLOCK_ANON = new PerlElementType("SUB_BLOCK_ANON");
  IElementType SUB_BLOCK_NAMED = new PerlElementType("SUB_BLOCK_NAMED");
  IElementType TERM = new PerlElementType("TERM");
  IElementType TR_MODIFIERS = new PerlElementType("TR_MODIFIERS");
  IElementType TR_REGEX = new PerlElementType("TR_REGEX");
  IElementType TR_REPLACEMENTLIST = new PerlElementType("TR_REPLACEMENTLIST");
  IElementType TR_SEARCHLIST = new PerlElementType("TR_SEARCHLIST");
  IElementType VARIABLE_DEFINITION = new PerlElementType("VARIABLE_DEFINITION");
  IElementType VARIABLE_DEFINITION_ARGUMENTS = new PerlElementType("VARIABLE_DEFINITION_ARGUMENTS");
  IElementType WHILE_BLOCK = new PerlElementType("WHILE_BLOCK");

  IElementType PERL_ARRAY = new PerlTokenType("PERL_ARRAY");
  IElementType PERL_ARROW_COMMA = new PerlTokenType("=>");
  IElementType PERL_BAREWORD = new PerlTokenType("PERL_BAREWORD");
  IElementType PERL_COMMENT = new PerlTokenType("PERL_COMMENT");
  IElementType PERL_COMMENT_BLOCK = new PerlTokenType("PERL_COMMENT_BLOCK");
  IElementType PERL_DEPACKAGE = new PerlTokenType("::");
  IElementType PERL_FUNCTION = new PerlTokenType("PERL_FUNCTION");
  IElementType PERL_GLOB = new PerlTokenType("PERL_GLOB");
  IElementType PERL_HASH = new PerlTokenType("PERL_HASH");
  IElementType PERL_KEYWORD = new PerlTokenType("PERL_KEYWORD");
  IElementType PERL_LBRACE = new PerlTokenType("{");
  IElementType PERL_LBRACK = new PerlTokenType("[");
  IElementType PERL_LPAREN = new PerlTokenType("(");
  IElementType PERL_NUMBER = new PerlTokenType("PERL_NUMBER");
  IElementType PERL_NUMBER_VERSION = new PerlTokenType("PERL_NUMBER_VERSION");
  IElementType PERL_OPERATOR = new PerlTokenType("PERL_OPERATOR");
  IElementType PERL_PACKAGE = new PerlTokenType("PERL_PACKAGE");
  IElementType PERL_POD = new PerlTokenType("PERL_POD");
  IElementType PERL_QUOTE = new PerlTokenType("\"");
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

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == ARRAY) {
        return new PerlArrayImpl(node);
      }
      else if (type == BLOCK) {
        return new PerlBlockImpl(node);
      }
      else if (type == BLOCK_BLOCK) {
        return new PerlBlockBlockImpl(node);
      }
      else if (type == BLOCK_CONDITIONAL) {
        return new PerlBlockConditionalImpl(node);
      }
      else if (type == CALEE) {
        return new PerlCaleeImpl(node);
      }
      else if (type == CALL_ARGUMENTS) {
        return new PerlCallArgumentsImpl(node);
      }
      else if (type == CALL_LEFTWARD) {
        return new PerlCallLeftwardImpl(node);
      }
      else if (type == CALL_RIGHTWARD) {
        return new PerlCallRightwardImpl(node);
      }
      else if (type == CODE_LINE) {
        return new PerlCodeLineImpl(node);
      }
      else if (type == EVAL) {
        return new PerlEvalImpl(node);
      }
      else if (type == EXPR) {
        return new PerlExprImpl(node);
      }
      else if (type == FILE_ITEM) {
        return new PerlFileItemImpl(node);
      }
      else if (type == FOR_BLOCK) {
        return new PerlForBlockImpl(node);
      }
      else if (type == FOR_BLOCK_ARGUMENTS) {
        return new PerlForBlockArgumentsImpl(node);
      }
      else if (type == GIVEN_BLOCK) {
        return new PerlGivenBlockImpl(node);
      }
      else if (type == GREP_EXPR) {
        return new PerlGrepExprImpl(node);
      }
      else if (type == HASH) {
        return new PerlHashImpl(node);
      }
      else if (type == IF_BLOCK) {
        return new PerlIfBlockImpl(node);
      }
      else if (type == IF_BLOCK_ELSE) {
        return new PerlIfBlockElseImpl(node);
      }
      else if (type == IF_BLOCK_ELSIF) {
        return new PerlIfBlockElsifImpl(node);
      }
      else if (type == IF_POSTFIX) {
        return new PerlIfPostfixImpl(node);
      }
      else if (type == KEYS_ARGS) {
        return new PerlKeysArgsImpl(node);
      }
      else if (type == KEYS_EXPR) {
        return new PerlKeysExprImpl(node);
      }
      else if (type == LAST_EXPR) {
        return new PerlLastExprImpl(node);
      }
      else if (type == LIST_EXPR) {
        return new PerlListExprImpl(node);
      }
      else if (type == LOCAL_DEFINITION) {
        return new PerlLocalDefinitionImpl(node);
      }
      else if (type == MY_DEFINITION) {
        return new PerlMyDefinitionImpl(node);
      }
      else if (type == OBJECT_METHOD) {
        return new PerlObjectMethodImpl(node);
      }
      else if (type == OBJECT_METHOD_OBJECT) {
        return new PerlObjectMethodObjectImpl(node);
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
      else if (type == OP_5_EXPR) {
        return new PerlOp5ExprImpl(node);
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
      else if (type == OUR_DEFINITION) {
        return new PerlOurDefinitionImpl(node);
      }
      else if (type == PACKAGE_DEFINITION) {
        return new PerlPackageDefinitionImpl(node);
      }
      else if (type == PACKAGE_METHOD) {
        return new PerlPackageMethodImpl(node);
      }
      else if (type == PACKAGE_NO) {
        return new PerlPackageNoImpl(node);
      }
      else if (type == PACKAGE_REQUIRE) {
        return new PerlPackageRequireImpl(node);
      }
      else if (type == PACKAGE_USE) {
        return new PerlPackageUseImpl(node);
      }
      else if (type == PACKAGE_USE_ARGUMENTS) {
        return new PerlPackageUseArgumentsImpl(node);
      }
      else if (type == PERL_REGEX) {
        return new PerlPerlRegexImpl(node);
      }
      else if (type == PERL_REGEX_MODIFIERS) {
        return new PerlPerlRegexModifiersImpl(node);
      }
      else if (type == PERL_VERSION) {
        return new PerlPerlVersionImpl(node);
      }
      else if (type == QW_EXPR) {
        return new PerlQwExprImpl(node);
      }
      else if (type == REGEX) {
        return new PerlRegexImpl(node);
      }
      else if (type == RETURN_EXPR) {
        return new PerlReturnExprImpl(node);
      }
      else if (type == SCALAR) {
        return new PerlScalarImpl(node);
      }
      else if (type == SHIFT_EXPR) {
        return new PerlShiftExprImpl(node);
      }
      else if (type == SORT_EXPR) {
        return new PerlSortExprImpl(node);
      }
      else if (type == SORT_OP_ARGS) {
        return new PerlSortOpArgsImpl(node);
      }
      else if (type == SUB_BLOCK_ANON) {
        return new PerlSubBlockAnonImpl(node);
      }
      else if (type == SUB_BLOCK_NAMED) {
        return new PerlSubBlockNamedImpl(node);
      }
      else if (type == TERM) {
        return new PerlTermImpl(node);
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
      else if (type == VARIABLE_DEFINITION) {
        return new PerlVariableDefinitionImpl(node);
      }
      else if (type == VARIABLE_DEFINITION_ARGUMENTS) {
        return new PerlVariableDefinitionArgumentsImpl(node);
      }
      else if (type == WHILE_BLOCK) {
        return new PerlWhileBlockImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
