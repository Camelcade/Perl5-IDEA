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
  IElementType EXPR = new PerlElementType("EXPR");
  IElementType LABEL = new PerlElementType("LABEL");
  IElementType NO_STATEMENT = new PerlElementType("NO_STATEMENT");
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
  IElementType PACKAGE_NAMESPACE = new PerlElementType("PACKAGE_NAMESPACE");
  IElementType SUB_DEFINITION = new PerlElementType("SUB_DEFINITION");
  IElementType USE_STATEMENT = new PerlElementType("USE_STATEMENT");

  IElementType CALL_RIGHTWARD = new PerlTokenType("call_rightward");
  IElementType NESTED_ELEMENT = new PerlTokenType("nested_element");
  IElementType PERL_ARRAY = new PerlTokenType("PERL_ARRAY");
  IElementType PERL_ARROW_COMMA = new PerlTokenType("=>");
  IElementType PERL_BAREWORD = new PerlTokenType("PERL_BAREWORD");
  IElementType PERL_COMMA = new PerlTokenType(",");
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
  IElementType PERL_VERSION = new PerlTokenType("PERL_VERSION");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == BLOCK) {
        return new PerlBlockImpl(node);
      }
      else if (type == EXPR) {
        return new PerlExprImpl(node);
      }
      else if (type == LABEL) {
        return new PerlLabelImpl(node);
      }
      else if (type == NO_STATEMENT) {
        return new PerlNoStatementImpl(node);
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
      else if (type == PACKAGE_NAMESPACE) {
        return new PerlPackageNamespaceImpl(node);
      }
      else if (type == SUB_DEFINITION) {
        return new PerlSubDefinitionImpl(node);
      }
      else if (type == USE_STATEMENT) {
        return new PerlUseStatementImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
