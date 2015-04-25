// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.lexer;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.perl5.lang.perl.PerlElementType;
import com.perl5.lang.perl.PerlTokenType;
import com.perl5.lang.perl.psi.impl.*;

public interface PerlElementTypes {

  IElementType CODE_LINE = new PerlElementType("CODE_LINE");
  IElementType FUNCTION_CALL = new PerlElementType("FUNCTION_CALL");
  IElementType FUNCTION_DEFINITION = new PerlElementType("FUNCTION_DEFINITION");
  IElementType FUNCTION_DEFINITION_ANON = new PerlElementType("FUNCTION_DEFINITION_ANON");
  IElementType FUNCTION_DEFINITION_NAMED = new PerlElementType("FUNCTION_DEFINITION_NAMED");
  IElementType IF_BLOCK = new PerlElementType("IF_BLOCK");
  IElementType IF_BLOCK_ELSE = new PerlElementType("IF_BLOCK_ELSE");
  IElementType IF_BLOCK_ELSIF = new PerlElementType("IF_BLOCK_ELSIF");
  IElementType IF_BRANCH = new PerlElementType("IF_BRANCH");
  IElementType IF_BRANCH_CONDITIONAL = new PerlElementType("IF_BRANCH_CONDITIONAL");
  IElementType METHOD_CALL = new PerlElementType("METHOD_CALL");
  IElementType OBJECT_CALL = new PerlElementType("OBJECT_CALL");
  IElementType PACKAGE_DEFINITION = new PerlElementType("PACKAGE_DEFINITION");
  IElementType PACKAGE_NAMESPACE = new PerlElementType("PACKAGE_NAMESPACE");
  IElementType PACKAGE_OBJECT_CALL = new PerlElementType("PACKAGE_OBJECT_CALL");
  IElementType PACKAGE_STATIC_CALL = new PerlElementType("PACKAGE_STATIC_CALL");
  IElementType PERL_ARRAY_VALUE = new PerlElementType("PERL_ARRAY_VALUE");
  IElementType PERL_BLOCK = new PerlElementType("PERL_BLOCK");
  IElementType PERL_CALL_PARAM = new PerlElementType("PERL_CALL_PARAM");
  IElementType PERL_CALL_PARAMS = new PerlElementType("PERL_CALL_PARAMS");
  IElementType PERL_CALL_PARAMS_ANY = new PerlElementType("PERL_CALL_PARAMS_ANY");
  IElementType PERL_CALL_PARAMS_STRICT = new PerlElementType("PERL_CALL_PARAMS_STRICT");
  IElementType PERL_EXPRESSION = new PerlElementType("PERL_EXPRESSION");
  IElementType PERL_HASH_VALUE = new PerlElementType("PERL_HASH_VALUE");
  IElementType PERL_SCALAR_FUNCTION_RESULT = new PerlElementType("PERL_SCALAR_FUNCTION_RESULT");
  IElementType PERL_SCALAR_VALUE = new PerlElementType("PERL_SCALAR_VALUE");
  IElementType PERL_SUBEXPRESSION = new PerlElementType("PERL_SUBEXPRESSION");

  IElementType PERL_COMMA = new PerlTokenType(",");
  IElementType PERL_COMMENT = new PerlTokenType("PERL_COMMENT");
  IElementType PERL_COMMENT_BLOCK = new PerlTokenType("PERL_COMMENT_BLOCK");
  IElementType PERL_DEPACKAGE = new PerlTokenType("::");
  IElementType PERL_DEREFERENCE = new PerlTokenType("->");
  IElementType PERL_DQ_STRING = new PerlTokenType("PERL_DQ_STRING");
  IElementType PERL_FUNCTION_BUILT_IN = new PerlTokenType("PERL_FUNCTION_BUILT_IN");
  IElementType PERL_FUNCTION_USER = new PerlTokenType("PERL_FUNCTION_USER");
  IElementType PERL_LBRACE = new PerlTokenType("{");
  IElementType PERL_LBRACK = new PerlTokenType("[");
  IElementType PERL_LPAREN = new PerlTokenType("(");
  IElementType PERL_MULTILINE_DQ = new PerlTokenType("PERL_MULTILINE_DQ");
  IElementType PERL_MULTILINE_HTML = new PerlTokenType("PERL_MULTILINE_HTML");
  IElementType PERL_MULTILINE_MARKER = new PerlTokenType("PERL_MULTILINE_MARKER");
  IElementType PERL_MULTILINE_SQ = new PerlTokenType("PERL_MULTILINE_SQ");
  IElementType PERL_MULTILINE_XML = new PerlTokenType("PERL_MULTILINE_XML");
  IElementType PERL_NUMBER = new PerlTokenType("PERL_NUMBER");
  IElementType PERL_OPERATOR = new PerlTokenType("PERL_OPERATOR");
  IElementType PERL_PACKAGE_BUILT_IN = new PerlTokenType("PERL_PACKAGE_BUILT_IN");
  IElementType PERL_PACKAGE_BUILT_IN_DEPRECATED = new PerlTokenType("PERL_PACKAGE_BUILT_IN_DEPRECATED");
  IElementType PERL_PACKAGE_BUILT_IN_PRAGMA = new PerlTokenType("PERL_PACKAGE_BUILT_IN_PRAGMA");
  IElementType PERL_PACKAGE_USER = new PerlTokenType("PERL_PACKAGE_USER");
  IElementType PERL_POD = new PerlTokenType("PERL_POD");
  IElementType PERL_RBRACE = new PerlTokenType("}");
  IElementType PERL_RBRACK = new PerlTokenType("]");
  IElementType PERL_RPAREN = new PerlTokenType(")");
  IElementType PERL_SEMI = new PerlTokenType(";");
  IElementType PERL_SIGIL_ARRAY = new PerlTokenType("PERL_SIGIL_ARRAY");
  IElementType PERL_SIGIL_HASH = new PerlTokenType("PERL_SIGIL_HASH");
  IElementType PERL_SIGIL_SCALAR = new PerlTokenType("PERL_SIGIL_SCALAR");
  IElementType PERL_SQ_STRING = new PerlTokenType("PERL_SQ_STRING");
  IElementType PERL_VARIABLE_ARRAY = new PerlTokenType("PERL_VARIABLE_ARRAY");
  IElementType PERL_VARIABLE_ARRAY_BUILT_IN = new PerlTokenType("PERL_VARIABLE_ARRAY_BUILT_IN");
  IElementType PERL_VARIABLE_GLOB = new PerlTokenType("PERL_VARIABLE_GLOB");
  IElementType PERL_VARIABLE_GLOB_BUILT_IN = new PerlTokenType("PERL_VARIABLE_GLOB_BUILT_IN");
  IElementType PERL_VARIABLE_HASH = new PerlTokenType("PERL_VARIABLE_HASH");
  IElementType PERL_VARIABLE_HASH_BUILT_IN = new PerlTokenType("PERL_VARIABLE_HASH_BUILT_IN");
  IElementType PERL_VARIABLE_SCALAR = new PerlTokenType("PERL_VARIABLE_SCALAR");
  IElementType PERL_VARIABLE_SCALAR_BUILT_IN = new PerlTokenType("PERL_VARIABLE_SCALAR_BUILT_IN");
  IElementType PERL_VERSION = new PerlTokenType("PERL_VERSION");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == CODE_LINE) {
        return new PerlCodeLineImpl(node);
      }
      else if (type == FUNCTION_CALL) {
        return new PerlFunctionCallImpl(node);
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
      else if (type == IF_BLOCK) {
        return new PerlIfBlockImpl(node);
      }
      else if (type == IF_BLOCK_ELSE) {
        return new PerlIfBlockElseImpl(node);
      }
      else if (type == IF_BLOCK_ELSIF) {
        return new PerlIfBlockElsifImpl(node);
      }
      else if (type == IF_BRANCH) {
        return new PerlIfBranchImpl(node);
      }
      else if (type == IF_BRANCH_CONDITIONAL) {
        return new PerlIfBranchConditionalImpl(node);
      }
      else if (type == METHOD_CALL) {
        return new PerlMethodCallImpl(node);
      }
      else if (type == OBJECT_CALL) {
        return new PerlObjectCallImpl(node);
      }
      else if (type == PACKAGE_DEFINITION) {
        return new PerlPackageDefinitionImpl(node);
      }
      else if (type == PACKAGE_NAMESPACE) {
        return new PerlPackageNamespaceImpl(node);
      }
      else if (type == PACKAGE_OBJECT_CALL) {
        return new PerlPackageObjectCallImpl(node);
      }
      else if (type == PACKAGE_STATIC_CALL) {
        return new PerlPackageStaticCallImpl(node);
      }
      else if (type == PERL_ARRAY_VALUE) {
        return new PerlPerlArrayValueImpl(node);
      }
      else if (type == PERL_BLOCK) {
        return new PerlPerlBlockImpl(node);
      }
      else if (type == PERL_CALL_PARAM) {
        return new PerlPerlCallParamImpl(node);
      }
      else if (type == PERL_CALL_PARAMS) {
        return new PerlPerlCallParamsImpl(node);
      }
      else if (type == PERL_CALL_PARAMS_ANY) {
        return new PerlPerlCallParamsAnyImpl(node);
      }
      else if (type == PERL_CALL_PARAMS_STRICT) {
        return new PerlPerlCallParamsStrictImpl(node);
      }
      else if (type == PERL_EXPRESSION) {
        return new PerlPerlExpressionImpl(node);
      }
      else if (type == PERL_HASH_VALUE) {
        return new PerlPerlHashValueImpl(node);
      }
      else if (type == PERL_SCALAR_FUNCTION_RESULT) {
        return new PerlPerlScalarFunctionResultImpl(node);
      }
      else if (type == PERL_SCALAR_VALUE) {
        return new PerlPerlScalarValueImpl(node);
      }
      else if (type == PERL_SUBEXPRESSION) {
        return new PerlPerlSubexpressionImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
