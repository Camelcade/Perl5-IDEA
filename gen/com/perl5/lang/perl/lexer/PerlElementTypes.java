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
  IElementType LABEL = new PerlElementType("LABEL");
  IElementType NO_STATEMENT = new PerlElementType("NO_STATEMENT");
  IElementType PACKAGE_NAMESPACE = new PerlElementType("PACKAGE_NAMESPACE");
  IElementType PERL_VERSION = new PerlElementType("PERL_VERSION");
  IElementType SCALAR_PRIMITIVE = new PerlElementType("SCALAR_PRIMITIVE");
  IElementType USE_STATEMENT = new PerlElementType("USE_STATEMENT");

  IElementType EXPR = new PerlTokenType("expr");
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

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == BLOCK) {
        return new PerlBlockImpl(node);
      }
      else if (type == LABEL) {
        return new PerlLabelImpl(node);
      }
      else if (type == NO_STATEMENT) {
        return new PerlNoStatementImpl(node);
      }
      else if (type == PACKAGE_NAMESPACE) {
        return new PerlPackageNamespaceImpl(node);
      }
      else if (type == PERL_VERSION) {
        return new PerlPerlVersionImpl(node);
      }
      else if (type == SCALAR_PRIMITIVE) {
        return new PerlScalarPrimitiveImpl(node);
      }
      else if (type == USE_STATEMENT) {
        return new PerlUseStatementImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
