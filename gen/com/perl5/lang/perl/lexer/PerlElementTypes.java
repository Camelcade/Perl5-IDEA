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
  IElementType ARRAY_DEREFERENCE = new PerlElementType("ARRAY_DEREFERENCE");
  IElementType ARRAY_ELEMENT = new PerlElementType("ARRAY_ELEMENT");
  IElementType ARRAY_ELEMENTS = new PerlElementType("ARRAY_ELEMENTS");
  IElementType ARRAY_SLICE = new PerlElementType("ARRAY_SLICE");
  IElementType ARRAY_VALUE = new PerlElementType("ARRAY_VALUE");
  IElementType BLOCK = new PerlElementType("BLOCK");
  IElementType BLOCK_ITEM = new PerlElementType("BLOCK_ITEM");
  IElementType CODE_CHUNK_INVALID = new PerlElementType("CODE_CHUNK_INVALID");
  IElementType CODE_CHUNK_VALID = new PerlElementType("CODE_CHUNK_VALID");
  IElementType CODE_LINE = new PerlElementType("CODE_LINE");
  IElementType CODE_LINE_ELEMENT = new PerlElementType("CODE_LINE_ELEMENT");
  IElementType CODE_LINE_ELEMENTS = new PerlElementType("CODE_LINE_ELEMENTS");
  IElementType CODE_LINE_INVALID_ELEMENT = new PerlElementType("CODE_LINE_INVALID_ELEMENT");
  IElementType CONTROLS = new PerlElementType("CONTROLS");
  IElementType EVAL = new PerlElementType("EVAL");
  IElementType EVAL_INVALID = new PerlElementType("EVAL_INVALID");
  IElementType EXPR = new PerlElementType("EXPR");
  IElementType EXPRESSION = new PerlElementType("EXPRESSION");
  IElementType FUNCTION = new PerlElementType("FUNCTION");
  IElementType FUNCTION_CALL = new PerlElementType("FUNCTION_CALL");
  IElementType FUNCTION_DEFINITION = new PerlElementType("FUNCTION_DEFINITION");
  IElementType FUNCTION_DEFINITION_ANON = new PerlElementType("FUNCTION_DEFINITION_ANON");
  IElementType FUNCTION_DEFINITION_NAMED = new PerlElementType("FUNCTION_DEFINITION_NAMED");
  IElementType GLOB = new PerlElementType("GLOB");
  IElementType HASH = new PerlElementType("HASH");
  IElementType HASH_DEREFERENCE = new PerlElementType("HASH_DEREFERENCE");
  IElementType HASH_SLICE = new PerlElementType("HASH_SLICE");
  IElementType HASH_VALUE = new PerlElementType("HASH_VALUE");
  IElementType IF_BLOCK = new PerlElementType("IF_BLOCK");
  IElementType IF_BLOCK_ELSE = new PerlElementType("IF_BLOCK_ELSE");
  IElementType IF_BLOCK_ELSIF = new PerlElementType("IF_BLOCK_ELSIF");
  IElementType IF_BRANCH = new PerlElementType("IF_BRANCH");
  IElementType IF_BRANCH_CONDITIONAL = new PerlElementType("IF_BRANCH_CONDITIONAL");
  IElementType LOCAL_DEFINITION = new PerlElementType("LOCAL_DEFINITION");
  IElementType METHOD_CALL = new PerlElementType("METHOD_CALL");
  IElementType MULTILINE_MARKER = new PerlElementType("MULTILINE_MARKER");
  IElementType MULTILINE_STRING = new PerlElementType("MULTILINE_STRING");
  IElementType MY_DEFINITION = new PerlElementType("MY_DEFINITION");
  IElementType OBJECT_METHOD_CALL = new PerlElementType("OBJECT_METHOD_CALL");
  IElementType OP_10 = new PerlElementType("OP_10");
  IElementType OP_11 = new PerlElementType("OP_11");
  IElementType OP_12 = new PerlElementType("OP_12");
  IElementType OP_13 = new PerlElementType("OP_13");
  IElementType OP_14 = new PerlElementType("OP_14");
  IElementType OP_15 = new PerlElementType("OP_15");
  IElementType OP_16 = new PerlElementType("OP_16");
  IElementType OP_17 = new PerlElementType("OP_17");
  IElementType OP_18 = new PerlElementType("OP_18");
  IElementType OP_19 = new PerlElementType("OP_19");
  IElementType OP_20 = new PerlElementType("OP_20");
  IElementType OP_21 = new PerlElementType("OP_21");
  IElementType OP_22 = new PerlElementType("OP_22");
  IElementType OP_23 = new PerlElementType("OP_23");
  IElementType OP_24 = new PerlElementType("OP_24");
  IElementType OP_3 = new PerlElementType("OP_3");
  IElementType OP_4 = new PerlElementType("OP_4");
  IElementType OP_5 = new PerlElementType("OP_5");
  IElementType OP_6 = new PerlElementType("OP_6");
  IElementType OP_7 = new PerlElementType("OP_7");
  IElementType OP_8 = new PerlElementType("OP_8");
  IElementType OP_9 = new PerlElementType("OP_9");
  IElementType OP_RIGHT_ARRAY_OPERAND = new PerlElementType("OP_RIGHT_ARRAY_OPERAND");
  IElementType OP_RIGHT_SCALAR_OPERAND = new PerlElementType("OP_RIGHT_SCALAR_OPERAND");
  IElementType OUR_DEFINITION = new PerlElementType("OUR_DEFINITION");
  IElementType PACKAGE_BARE = new PerlElementType("PACKAGE_BARE");
  IElementType PACKAGE_DEFINITION = new PerlElementType("PACKAGE_DEFINITION");
  IElementType PACKAGE_DEFINITION_INVALID = new PerlElementType("PACKAGE_DEFINITION_INVALID");
  IElementType PACKAGE_FUNCTION_CALL = new PerlElementType("PACKAGE_FUNCTION_CALL");
  IElementType PACKAGE_ITEM = new PerlElementType("PACKAGE_ITEM");
  IElementType PACKAGE_METHOD_CALL = new PerlElementType("PACKAGE_METHOD_CALL");
  IElementType PACKAGE_NO = new PerlElementType("PACKAGE_NO");
  IElementType PACKAGE_NO_INVALID = new PerlElementType("PACKAGE_NO_INVALID");
  IElementType PACKAGE_REQUIRE = new PerlElementType("PACKAGE_REQUIRE");
  IElementType PACKAGE_REQUIRE_INVALID = new PerlElementType("PACKAGE_REQUIRE_INVALID");
  IElementType PACKAGE_USE = new PerlElementType("PACKAGE_USE");
  IElementType PACKAGE_USE_ARGUMENTS = new PerlElementType("PACKAGE_USE_ARGUMENTS");
  IElementType PACKAGE_USE_INVALID = new PerlElementType("PACKAGE_USE_INVALID");
  IElementType SCALAR = new PerlElementType("SCALAR");
  IElementType SCALAR_ANON_ARRAY = new PerlElementType("SCALAR_ANON_ARRAY");
  IElementType SCALAR_ANON_HASH = new PerlElementType("SCALAR_ANON_HASH");
  IElementType SCALAR_ARRAY_ELEMENT = new PerlElementType("SCALAR_ARRAY_ELEMENT");
  IElementType SCALAR_DEREFERENCE = new PerlElementType("SCALAR_DEREFERENCE");
  IElementType SCALAR_FUNCTION_RESULT = new PerlElementType("SCALAR_FUNCTION_RESULT");
  IElementType SCALAR_HASH_ELEMENT = new PerlElementType("SCALAR_HASH_ELEMENT");
  IElementType SCALAR_VALUE = new PerlElementType("SCALAR_VALUE");
  IElementType SCALAR_VALUE_DETERMINED = new PerlElementType("SCALAR_VALUE_DETERMINED");
  IElementType SCALAR_VALUE_MUTABLE = new PerlElementType("SCALAR_VALUE_MUTABLE");
  IElementType STRING = new PerlElementType("STRING");
  IElementType SUBEXPRESSION = new PerlElementType("SUBEXPRESSION");
  IElementType VARIABLE = new PerlElementType("VARIABLE");
  IElementType VARIABLES = new PerlElementType("VARIABLES");
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

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == ARRAY) {
        return new PerlArrayImpl(node);
      }
      else if (type == ARRAY_DEREFERENCE) {
        return new PerlArrayDereferenceImpl(node);
      }
      else if (type == ARRAY_ELEMENT) {
        return new PerlArrayElementImpl(node);
      }
      else if (type == ARRAY_ELEMENTS) {
        return new PerlArrayElementsImpl(node);
      }
      else if (type == ARRAY_SLICE) {
        return new PerlArraySliceImpl(node);
      }
      else if (type == ARRAY_VALUE) {
        return new PerlArrayValueImpl(node);
      }
      else if (type == BLOCK) {
        return new PerlBlockImpl(node);
      }
      else if (type == BLOCK_ITEM) {
        return new PerlBlockItemImpl(node);
      }
      else if (type == CODE_CHUNK_INVALID) {
        return new PerlCodeChunkInvalidImpl(node);
      }
      else if (type == CODE_CHUNK_VALID) {
        return new PerlCodeChunkValidImpl(node);
      }
      else if (type == CODE_LINE) {
        return new PerlCodeLineImpl(node);
      }
      else if (type == CODE_LINE_ELEMENT) {
        return new PerlCodeLineElementImpl(node);
      }
      else if (type == CODE_LINE_ELEMENTS) {
        return new PerlCodeLineElementsImpl(node);
      }
      else if (type == CODE_LINE_INVALID_ELEMENT) {
        return new PerlCodeLineInvalidElementImpl(node);
      }
      else if (type == CONTROLS) {
        return new PerlControlsImpl(node);
      }
      else if (type == EVAL) {
        return new PerlEvalImpl(node);
      }
      else if (type == EVAL_INVALID) {
        return new PerlEvalInvalidImpl(node);
      }
      else if (type == EXPR) {
        return new PerlExprImpl(node);
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
      else if (type == FUNCTION_DEFINITION) {
        return new PerlFunctionDefinitionImpl(node);
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
      else if (type == HASH_DEREFERENCE) {
        return new PerlHashDereferenceImpl(node);
      }
      else if (type == HASH_SLICE) {
        return new PerlHashSliceImpl(node);
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
      else if (type == LOCAL_DEFINITION) {
        return new PerlLocalDefinitionImpl(node);
      }
      else if (type == METHOD_CALL) {
        return new PerlMethodCallImpl(node);
      }
      else if (type == MULTILINE_MARKER) {
        return new PerlMultilineMarkerImpl(node);
      }
      else if (type == MULTILINE_STRING) {
        return new PerlMultilineStringImpl(node);
      }
      else if (type == MY_DEFINITION) {
        return new PerlMyDefinitionImpl(node);
      }
      else if (type == OBJECT_METHOD_CALL) {
        return new PerlObjectMethodCallImpl(node);
      }
      else if (type == OP_10) {
        return new PerlOp10Impl(node);
      }
      else if (type == OP_11) {
        return new PerlOp11Impl(node);
      }
      else if (type == OP_12) {
        return new PerlOp12Impl(node);
      }
      else if (type == OP_13) {
        return new PerlOp13Impl(node);
      }
      else if (type == OP_14) {
        return new PerlOp14Impl(node);
      }
      else if (type == OP_15) {
        return new PerlOp15Impl(node);
      }
      else if (type == OP_16) {
        return new PerlOp16Impl(node);
      }
      else if (type == OP_17) {
        return new PerlOp17Impl(node);
      }
      else if (type == OP_18) {
        return new PerlOp18Impl(node);
      }
      else if (type == OP_19) {
        return new PerlOp19Impl(node);
      }
      else if (type == OP_20) {
        return new PerlOp20Impl(node);
      }
      else if (type == OP_21) {
        return new PerlOp21Impl(node);
      }
      else if (type == OP_22) {
        return new PerlOp22Impl(node);
      }
      else if (type == OP_23) {
        return new PerlOp23Impl(node);
      }
      else if (type == OP_24) {
        return new PerlOp24Impl(node);
      }
      else if (type == OP_3) {
        return new PerlOp3Impl(node);
      }
      else if (type == OP_4) {
        return new PerlOp4Impl(node);
      }
      else if (type == OP_5) {
        return new PerlOp5Impl(node);
      }
      else if (type == OP_6) {
        return new PerlOp6Impl(node);
      }
      else if (type == OP_7) {
        return new PerlOp7Impl(node);
      }
      else if (type == OP_8) {
        return new PerlOp8Impl(node);
      }
      else if (type == OP_9) {
        return new PerlOp9Impl(node);
      }
      else if (type == OP_RIGHT_ARRAY_OPERAND) {
        return new PerlOpRightArrayOperandImpl(node);
      }
      else if (type == OP_RIGHT_SCALAR_OPERAND) {
        return new PerlOpRightScalarOperandImpl(node);
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
      else if (type == PACKAGE_DEFINITION_INVALID) {
        return new PerlPackageDefinitionInvalidImpl(node);
      }
      else if (type == PACKAGE_FUNCTION_CALL) {
        return new PerlPackageFunctionCallImpl(node);
      }
      else if (type == PACKAGE_ITEM) {
        return new PerlPackageItemImpl(node);
      }
      else if (type == PACKAGE_METHOD_CALL) {
        return new PerlPackageMethodCallImpl(node);
      }
      else if (type == PACKAGE_NO) {
        return new PerlPackageNoImpl(node);
      }
      else if (type == PACKAGE_NO_INVALID) {
        return new PerlPackageNoInvalidImpl(node);
      }
      else if (type == PACKAGE_REQUIRE) {
        return new PerlPackageRequireImpl(node);
      }
      else if (type == PACKAGE_REQUIRE_INVALID) {
        return new PerlPackageRequireInvalidImpl(node);
      }
      else if (type == PACKAGE_USE) {
        return new PerlPackageUseImpl(node);
      }
      else if (type == PACKAGE_USE_ARGUMENTS) {
        return new PerlPackageUseArgumentsImpl(node);
      }
      else if (type == PACKAGE_USE_INVALID) {
        return new PerlPackageUseInvalidImpl(node);
      }
      else if (type == SCALAR) {
        return new PerlScalarImpl(node);
      }
      else if (type == SCALAR_ANON_ARRAY) {
        return new PerlScalarAnonArrayImpl(node);
      }
      else if (type == SCALAR_ANON_HASH) {
        return new PerlScalarAnonHashImpl(node);
      }
      else if (type == SCALAR_ARRAY_ELEMENT) {
        return new PerlScalarArrayElementImpl(node);
      }
      else if (type == SCALAR_DEREFERENCE) {
        return new PerlScalarDereferenceImpl(node);
      }
      else if (type == SCALAR_FUNCTION_RESULT) {
        return new PerlScalarFunctionResultImpl(node);
      }
      else if (type == SCALAR_HASH_ELEMENT) {
        return new PerlScalarHashElementImpl(node);
      }
      else if (type == SCALAR_VALUE) {
        return new PerlScalarValueImpl(node);
      }
      else if (type == SCALAR_VALUE_DETERMINED) {
        return new PerlScalarValueDeterminedImpl(node);
      }
      else if (type == SCALAR_VALUE_MUTABLE) {
        return new PerlScalarValueMutableImpl(node);
      }
      else if (type == STRING) {
        return new PerlStringImpl(node);
      }
      else if (type == SUBEXPRESSION) {
        return new PerlSubexpressionImpl(node);
      }
      else if (type == VARIABLE) {
        return new PerlVariableImpl(node);
      }
      else if (type == VARIABLES) {
        return new PerlVariablesImpl(node);
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
