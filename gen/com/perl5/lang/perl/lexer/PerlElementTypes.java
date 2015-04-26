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
  IElementType ARRAY_VALUE = new PerlElementType("ARRAY_VALUE");
  IElementType BLOCK = new PerlElementType("BLOCK");
  IElementType CALL_PARAM = new PerlElementType("CALL_PARAM");
  IElementType CALL_PARAMS = new PerlElementType("CALL_PARAMS");
  IElementType CALL_PARAMS_ANY = new PerlElementType("CALL_PARAMS_ANY");
  IElementType CALL_PARAMS_STRICT = new PerlElementType("CALL_PARAMS_STRICT");
  IElementType CODE_LINE = new PerlElementType("CODE_LINE");
  IElementType CODE_LINE_INVALID_ELEMENT = new PerlElementType("CODE_LINE_INVALID_ELEMENT");
  IElementType EVAL = new PerlElementType("EVAL");
  IElementType EVAL_INVALID = new PerlElementType("EVAL_INVALID");
  IElementType EXPRESSION = new PerlElementType("EXPRESSION");
  IElementType FUNCTION = new PerlElementType("FUNCTION");
  IElementType FUNCTION_CALL = new PerlElementType("FUNCTION_CALL");
  IElementType FUNCTION_CALL_ANY = new PerlElementType("FUNCTION_CALL_ANY");
  IElementType FUNCTION_DEFINITION_ANON = new PerlElementType("FUNCTION_DEFINITION_ANON");
  IElementType FUNCTION_DEFINITION_NAMED = new PerlElementType("FUNCTION_DEFINITION_NAMED");
  IElementType GLOB = new PerlElementType("GLOB");
  IElementType HASH = new PerlElementType("HASH");
  IElementType HASH_VALUE = new PerlElementType("HASH_VALUE");
  IElementType IF_BLOCK = new PerlElementType("IF_BLOCK");
  IElementType IF_BLOCK_ELSE = new PerlElementType("IF_BLOCK_ELSE");
  IElementType IF_BLOCK_ELSIF = new PerlElementType("IF_BLOCK_ELSIF");
  IElementType IF_BRANCH = new PerlElementType("IF_BRANCH");
  IElementType IF_BRANCH_CONDITIONAL = new PerlElementType("IF_BRANCH_CONDITIONAL");
  IElementType METHOD_CALL = new PerlElementType("METHOD_CALL");
  IElementType OBJECT_CALL = new PerlElementType("OBJECT_CALL");
  IElementType PACKAGE_BARE = new PerlElementType("PACKAGE_BARE");
  IElementType PACKAGE_DEFINITION_INVALID = new PerlElementType("PACKAGE_DEFINITION_INVALID");
  IElementType PACKAGE_NAMESPACE = new PerlElementType("PACKAGE_NAMESPACE");
  IElementType PACKAGE_NO = new PerlElementType("PACKAGE_NO");
  IElementType PACKAGE_NO_INVALID = new PerlElementType("PACKAGE_NO_INVALID");
  IElementType PACKAGE_OBJECT_CALL = new PerlElementType("PACKAGE_OBJECT_CALL");
  IElementType PACKAGE_REQUIRE = new PerlElementType("PACKAGE_REQUIRE");
  IElementType PACKAGE_REQUIRE_INVALID = new PerlElementType("PACKAGE_REQUIRE_INVALID");
  IElementType PACKAGE_STATIC_CALL = new PerlElementType("PACKAGE_STATIC_CALL");
  IElementType PACKAGE_USE = new PerlElementType("PACKAGE_USE");
  IElementType PACKAGE_USE_INVALID = new PerlElementType("PACKAGE_USE_INVALID");
  IElementType SCALAR = new PerlElementType("SCALAR");
  IElementType SCALAR_VALUE = new PerlElementType("SCALAR_VALUE");
  IElementType STRING = new PerlElementType("STRING");
  IElementType SUBEXPRESSION = new PerlElementType("SUBEXPRESSION");

  IElementType CHARS = new PerlTokenType("chars");
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

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == ARRAY) {
        return new PerlArrayImpl(node);
      }
      else if (type == ARRAY_VALUE) {
        return new PerlArrayValueImpl(node);
      }
      else if (type == BLOCK) {
        return new PerlBlockImpl(node);
      }
      else if (type == CALL_PARAM) {
        return new PerlCallParamImpl(node);
      }
      else if (type == CALL_PARAMS) {
        return new PerlCallParamsImpl(node);
      }
      else if (type == CALL_PARAMS_ANY) {
        return new PerlCallParamsAnyImpl(node);
      }
      else if (type == CALL_PARAMS_STRICT) {
        return new PerlCallParamsStrictImpl(node);
      }
      else if (type == CODE_LINE) {
        return new PerlCodeLineImpl(node);
      }
      else if (type == CODE_LINE_INVALID_ELEMENT) {
        return new PerlCodeLineInvalidElementImpl(node);
      }
      else if (type == EVAL) {
        return new PerlEvalImpl(node);
      }
      else if (type == EVAL_INVALID) {
        return new PerlEvalInvalidImpl(node);
      }
      else if (type == EXPRESSION) {
        return new PerlExpressionImpl(node);
      }
      else if (type == FUNCTION) {
        return new PerlFunctionImpl(node);
      }
      else if (type == FUNCTION_CALL) {
        return new PerlFunctionCallImpl(node);
      }
      else if (type == FUNCTION_CALL_ANY) {
        return new PerlFunctionCallAnyImpl(node);
      }
      else if (type == FUNCTION_DEFINITION_ANON) {
        return new PerlFunctionDefinitionAnonImpl(node);
      }
      else if (type == FUNCTION_DEFINITION_NAMED) {
        return new PerlFunctionDefinitionNamedImpl(node);
      }
      else if (type == GLOB) {
        return new PerlGlobImpl(node);
      }
      else if (type == HASH) {
        return new PerlHashImpl(node);
      }
      else if (type == HASH_VALUE) {
        return new PerlHashValueImpl(node);
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
      else if (type == PACKAGE_BARE) {
        return new PerlPackageBareImpl(node);
      }
      else if (type == PACKAGE_DEFINITION_INVALID) {
        return new PerlPackageDefinitionInvalidImpl(node);
      }
      else if (type == PACKAGE_NAMESPACE) {
        return new PerlPackageNamespaceImpl(node);
      }
      else if (type == PACKAGE_NO) {
        return new PerlPackageNoImpl(node);
      }
      else if (type == PACKAGE_NO_INVALID) {
        return new PerlPackageNoInvalidImpl(node);
      }
      else if (type == PACKAGE_OBJECT_CALL) {
        return new PerlPackageObjectCallImpl(node);
      }
      else if (type == PACKAGE_REQUIRE) {
        return new PerlPackageRequireImpl(node);
      }
      else if (type == PACKAGE_REQUIRE_INVALID) {
        return new PerlPackageRequireInvalidImpl(node);
      }
      else if (type == PACKAGE_STATIC_CALL) {
        return new PerlPackageStaticCallImpl(node);
      }
      else if (type == PACKAGE_USE) {
        return new PerlPackageUseImpl(node);
      }
      else if (type == PACKAGE_USE_INVALID) {
        return new PerlPackageUseInvalidImpl(node);
      }
      else if (type == SCALAR) {
        return new PerlScalarImpl(node);
      }
      else if (type == SCALAR_VALUE) {
        return new PerlScalarValueImpl(node);
      }
      else if (type == STRING) {
        return new PerlStringImpl(node);
      }
      else if (type == SUBEXPRESSION) {
        return new PerlSubexpressionImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
