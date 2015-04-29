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
  IElementType BLOCK_ITEM = new PerlElementType("BLOCK_ITEM");
  IElementType CALEE = new PerlElementType("CALEE");
  IElementType CALL_LEFTWARD = new PerlElementType("CALL_LEFTWARD");
  IElementType CALL_RIGHTWARD = new PerlElementType("CALL_RIGHTWARD");
  IElementType CODE_LINE = new PerlElementType("CODE_LINE");
  IElementType CODE_LINES = new PerlElementType("CODE_LINES");
  IElementType EVAL = new PerlElementType("EVAL");
  IElementType EXPR = new PerlElementType("EXPR");
  IElementType FUNCTION_DEFINITION = new PerlElementType("FUNCTION_DEFINITION");
  IElementType FUNCTION_DEFINITION_ANON = new PerlElementType("FUNCTION_DEFINITION_ANON");
  IElementType FUNCTION_DEFINITION_NAMED = new PerlElementType("FUNCTION_DEFINITION_NAMED");
  IElementType HASH = new PerlElementType("HASH");
  IElementType IF_BLOCK = new PerlElementType("IF_BLOCK");
  IElementType IF_BLOCK_ELSE = new PerlElementType("IF_BLOCK_ELSE");
  IElementType IF_BLOCK_ELSIF = new PerlElementType("IF_BLOCK_ELSIF");
  IElementType IF_BRANCH_CONDITIONAL = new PerlElementType("IF_BRANCH_CONDITIONAL");
  IElementType IF_POSTFIX = new PerlElementType("IF_POSTFIX");
  IElementType LOCAL_DEFINITION = new PerlElementType("LOCAL_DEFINITION");
  IElementType MULTILINE_STRING = new PerlElementType("MULTILINE_STRING");
  IElementType MY_DEFINITION = new PerlElementType("MY_DEFINITION");
  IElementType OBJECT_METHOD = new PerlElementType("OBJECT_METHOD");
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
  IElementType PACKAGE_BARE = new PerlElementType("PACKAGE_BARE");
  IElementType PACKAGE_DEFINITION = new PerlElementType("PACKAGE_DEFINITION");
  IElementType PACKAGE_FUNCTION = new PerlElementType("PACKAGE_FUNCTION");
  IElementType PACKAGE_ITEM = new PerlElementType("PACKAGE_ITEM");
  IElementType PACKAGE_METHOD = new PerlElementType("PACKAGE_METHOD");
  IElementType PACKAGE_NO = new PerlElementType("PACKAGE_NO");
  IElementType PACKAGE_REQUIRE = new PerlElementType("PACKAGE_REQUIRE");
  IElementType PACKAGE_USE = new PerlElementType("PACKAGE_USE");
  IElementType PACKAGE_USE_ARGUMENTS = new PerlElementType("PACKAGE_USE_ARGUMENTS");
  IElementType SCALAR = new PerlElementType("SCALAR");
  IElementType TERM = new PerlElementType("TERM");
  IElementType VARIABLE_DEFINITION = new PerlElementType("VARIABLE_DEFINITION");
  IElementType VARIABLE_DEFINITION_ARGUMENTS = new PerlElementType("VARIABLE_DEFINITION_ARGUMENTS");

  IElementType PERL_ARRAY = new PerlTokenType("PERL_ARRAY");
  IElementType PERL_COMMA = new PerlTokenType(",");
  IElementType PERL_COMMENT = new PerlTokenType("PERL_COMMENT");
  IElementType PERL_COMMENT_BLOCK = new PerlTokenType("PERL_COMMENT_BLOCK");
  IElementType PERL_DEPACKAGE = new PerlTokenType("::");
  IElementType PERL_DEREFERENCE = new PerlTokenType("->");
  IElementType PERL_FUNCTION = new PerlTokenType("PERL_FUNCTION");
  IElementType PERL_GLOB = new PerlTokenType("PERL_GLOB");
  IElementType PERL_HASH = new PerlTokenType("PERL_HASH");
  IElementType PERL_LBRACE = new PerlTokenType("{");
  IElementType PERL_LBRACK = new PerlTokenType("[");
  IElementType PERL_LPAREN = new PerlTokenType("(");
  IElementType PERL_MULTILINE_MARKER = new PerlTokenType("PERL_MULTILINE_MARKER");
  IElementType PERL_MULTILINE_MARKER_HTML = new PerlTokenType("PERL_MULTILINE_MARKER_HTML");
  IElementType PERL_MULTILINE_MARKER_XHTML = new PerlTokenType("PERL_MULTILINE_MARKER_XHTML");
  IElementType PERL_MULTILINE_MARKER_XML = new PerlTokenType("PERL_MULTILINE_MARKER_XML");
  IElementType PERL_NUMBER = new PerlTokenType("PERL_NUMBER");
  IElementType PERL_OPERATOR = new PerlTokenType("PERL_OPERATOR");
  IElementType PERL_PACKAGE = new PerlTokenType("PERL_PACKAGE");
  IElementType PERL_POD = new PerlTokenType("PERL_POD");
  IElementType PERL_RBRACE = new PerlTokenType("}");
  IElementType PERL_RBRACK = new PerlTokenType("]");
  IElementType PERL_RPAREN = new PerlTokenType(")");
  IElementType PERL_SCALAR = new PerlTokenType("PERL_SCALAR");
  IElementType PERL_SEMI = new PerlTokenType(";");
  IElementType PERL_SIGIL_ARRAY = new PerlTokenType("@");
  IElementType PERL_SIGIL_HASH = new PerlTokenType("%");
  IElementType PERL_SIGIL_SCALAR = new PerlTokenType("$");
  IElementType PERL_STRING = new PerlTokenType("PERL_STRING");
  IElementType PERL_STRING_MULTILINE = new PerlTokenType("PERL_STRING_MULTILINE");
  IElementType PERL_VERSION = new PerlTokenType("PERL_VERSION");
  IElementType STRING = new PerlTokenType("string");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == ARRAY) {
        return new PerlArrayImpl(node);
      }
      else if (type == BLOCK) {
        return new PerlBlockImpl(node);
      }
      else if (type == BLOCK_ITEM) {
        return new PerlBlockItemImpl(node);
      }
      else if (type == CALEE) {
        return new PerlCaleeImpl(node);
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
      else if (type == CODE_LINES) {
        return new PerlCodeLinesImpl(node);
      }
      else if (type == EVAL) {
        return new PerlEvalImpl(node);
      }
      else if (type == EXPR) {
        return new PerlExprImpl(node);
      }
      else if (type == FUNCTION_DEFINITION) {
        return new PerlFunctionDefinitionImpl(node);
      }
      else if (type == FUNCTION_DEFINITION_ANON) {
        return new PerlFunctionDefinitionAnonImpl(node);
      }
      else if (type == FUNCTION_DEFINITION_NAMED) {
        return new PerlFunctionDefinitionNamedImpl(node);
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
      else if (type == IF_BRANCH_CONDITIONAL) {
        return new PerlIfBranchConditionalImpl(node);
      }
      else if (type == IF_POSTFIX) {
        return new PerlIfPostfixImpl(node);
      }
      else if (type == LOCAL_DEFINITION) {
        return new PerlLocalDefinitionImpl(node);
      }
      else if (type == MULTILINE_STRING) {
        return new PerlMultilineStringImpl(node);
      }
      else if (type == MY_DEFINITION) {
        return new PerlMyDefinitionImpl(node);
      }
      else if (type == OBJECT_METHOD) {
        return new PerlObjectMethodImpl(node);
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
      else if (type == PACKAGE_BARE) {
        return new PerlPackageBareImpl(node);
      }
      else if (type == PACKAGE_DEFINITION) {
        return new PerlPackageDefinitionImpl(node);
      }
      else if (type == PACKAGE_FUNCTION) {
        return new PerlPackageFunctionImpl(node);
      }
      else if (type == PACKAGE_ITEM) {
        return new PerlPackageItemImpl(node);
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
      else if (type == SCALAR) {
        return new PerlScalarImpl(node);
      }
      else if (type == TERM) {
        return new PerlTermImpl(node);
      }
      else if (type == VARIABLE_DEFINITION) {
        return new PerlVariableDefinitionImpl(node);
      }
      else if (type == VARIABLE_DEFINITION_ARGUMENTS) {
        return new PerlVariableDefinitionArgumentsImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
