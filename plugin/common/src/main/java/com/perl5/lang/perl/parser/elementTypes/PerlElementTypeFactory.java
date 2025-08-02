/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.psi.impl.PerlHeredocTerminatorElementImpl;
import com.perl5.lang.perl.psi.impl.PerlVersionElementImpl;
import com.perl5.lang.perl.psi.stubs.PerlStubElementTypes;
import com.perl5.lang.perl.psi.stubs.calls.PerlSubCallElementType;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.util.PerlPackageUtilCore.__PACKAGE__;


public class PerlElementTypeFactory {
  private static final Logger LOG = Logger.getInstance(PerlElementTypeFactory.class);

  public static @NotNull IElementType getTokenType(@NotNull String name) {
    return switch (name) {
      case "COMMENT_BLOCK" -> new PerlBlockCommentTokenType(name);
      case "ATTRIBUTE_IDENTIFIER" -> new PerlAttributeIdentifierTokenType(name);
      case "regex" -> new PerlRegexTokenType(name, LeafPsiElement.class);
      case "STRING_CONTENT", "STRING_CONTENT_QQ", "STRING_CONTENT_XQ" -> new PerlStringContentTokenType(name);
      case "SCALAR_NAME", "ARRAY_NAME", "HASH_NAME", "GLOB_NAME", "CODE_NAME" -> new PerlVariableNameTokenType(name);
      case "POD" -> new PerlPodReparseableTokenType(name);
      case "subname", "list", "unary", "unary_custom", "argumentless" -> new PerlSubNameTokenType(name);
      case "package::name", "package::name::", "constant", "vars", __PACKAGE__ -> new PerlNamespaceNameTokenType(name);
      case "HEREDOC_END", "HEREDOC_END_INDENTABLE" -> new PerlTokenTypeEx(name, PerlHeredocTerminatorElementImpl.class);
      case "VERSION_ELEMENT" -> new PerlTokenTypeEx(name, PerlVersionElementImpl.class);
      case "COMMENT_LINE" -> new PerlReparseableCommentTokenType(name, PsiCommentImpl.class);
      default -> new PerlTokenType(name);
    };
  }

  public static @NotNull IElementType getElementType(@NotNull String name) {
    return switch (name) {
      case "PARSABLE_STRING_USE_VARS" -> new PerlUseVarsStringElementType();
      case "COMMENT_ANNOTATION" -> new PerlAnnotationElementType(name);
      case "HEREDOC_QQ", "HEREDOC_QX", "HEREDOC" -> new PerlHeredocElementType(name);
      case "SUB_DEFINITION" -> PerlStubElementTypes.SUB_DEFINITION;
      case "METHOD_DEFINITION" -> PerlStubElementTypes.METHOD_DEFINITION;
      case "FUNC_DEFINITION" -> PerlStubElementTypes.FUNC_DEFINITION;
      case "SUB_DECLARATION" -> PerlStubElementTypes.SUB_DECLARATION;
      case "GLOB_VARIABLE" -> PerlStubElementTypes.PERL_GLOB;
      case "NAMESPACE_DEFINITION" -> PerlStubElementTypes.PERL_NAMESPACE;
      case "VARIABLE_DECLARATION_ELEMENT" -> PerlStubElementTypes.PERL_VARIABLE_DECLARATION_ELEMENT;
      case "DO_EXPR" -> PerlStubElementTypes.PERL_DO_EXPR;
      case "DO_BLOCK_EXPR" -> new PerlElementType(name);
      case "REQUIRE_EXPR" -> PerlStubElementTypes.PERL_REQUIRE_EXPR;
      case "AFTER_MODIFIER" -> new PerlElementType(name);
      case "BEFORE_MODIFIER" -> new PerlElementType(name);
      case "AROUND_MODIFIER" -> new PerlElementType(name);
      case "AROUND_SIGNATURE_INVOCANTS" -> new PerlElementType(name);
      case "AUGMENT_MODIFIER" -> new PerlElementType(name);
      case "ADD_EXPR" -> new PerlElementType(name);
      case "AND_EXPR" -> new PerlElementType(name);
      case "ANON_ARRAY" -> new PerlAnonArrayElementType(name);
      case "UNICODE_CHAR" -> new PerlElementType(name);
      case "HEX_CHAR" -> new PerlElementType(name);
      case "OCT_CHAR" -> new PerlElementType(name);
      case "ESC_CHAR" -> new PerlElementType(name);
      case "ANON_HASH" -> new PerlAnonHashElementType();
      case "ARRAY_SLICE" -> new PerlElementType(name);
      case "HASH_ARRAY_SLICE" -> new PerlElementType(name);
      case "ARRAY_CAST_EXPR" -> new PerlDereferenceElementType.Array(name);
      case "HASH_SLICE" -> new PerlElementType(name);
      case "HASH_HASH_SLICE" -> new PerlElementType(name);
      case "ARRAY_INDEX" -> new PerlAnonArrayElementType(name);
      case "ARRAY_INDEX_VARIABLE" -> new PerlElementType(name);
      case "ARRAY_VARIABLE" -> new PerlElementType(name);
      case "ASSIGN_EXPR" -> new PerlElementType(name);
      case "ATTRIBUTE" -> new PerlElementType(name);
      case "ISA_EXPR" -> new PerlElementType(name);
      case "BITWISE_AND_EXPR" -> new PerlElementType(name);
      case "BITWISE_OR_XOR_EXPR" -> new PerlElementType(name);
      case "BLOCK" -> new PerlCodeBlockElementType();
      case "BLOCK_BRACELESS" -> new PerlElementType(name);
      case "BLOCK_SCALAR" -> new PerlElementType(name);
      case "BLOCK_ARRAY" -> new PerlElementType(name);
      case "BLOCK_HASH" -> new PerlElementType(name);
      case "BLOCK_GLOB" -> new PerlElementType(name);
      case "BLOCK_CODE" -> new PerlElementType(name);
      case "BLOCK_COMPOUND" -> new PerlElementType(name);
      case "CALL_ARGUMENTS" -> new PerlElementType(name);
      case "CODE_CAST_EXPR" -> new PerlDereferenceElementType.Code(name);
      case "CODE_VARIABLE" -> new PerlElementType(name);
      case "COMMA_SEQUENCE_EXPR" -> new PerlElementType(name);
      case "COMPARE_EXPR" -> new PerlElementType(name);
      case "COMPILE_REGEX" -> new PerlCompileRegexpElementType(name);
      case "CONDITIONAL_BLOCK" -> new PerlElementType(name);
      case "UNCONDITIONAL_BLOCK" -> new PerlElementType(name);
      case "CONDITION_EXPR" -> new PerlParenthesizedElementType(name);
      case "CONTINUE_BLOCK" -> new PerlElementType(name);
      case "DEFAULT_COMPOUND" -> new PerlElementType(name);
      case "DEREF_EXPR" -> new PerlElementType(name);
      case "EQUAL_EXPR" -> new PerlElementType(name);
      case "EVAL_EXPR" -> new PerlElementType(name);
      case "EXPR" -> new PerlElementType(name);
      case "FILE_READ_EXPR" -> new PerlElementType(name);
      case "FILE_GLOB_EXPR" -> new PerlElementType(name);
      case "FLIPFLOP_EXPR" -> new PerlElementType(name);
      case "FORMAT_DEFINITION" -> new PerlElementType(name);
      case "FOR_COMPOUND" -> new PerlElementType(name);
      case "FOREACH_COMPOUND" -> new PerlElementType(name);
      case "FOR_OR_FOREACH" -> new PerlElementType(name);
      case "FOR_INIT" -> new PerlElementType(name);
      case "FOR_CONDITION" -> new PerlElementType(name);
      case "FOR_MUTATOR" -> new PerlElementType(name);
      case "FOR_STATEMENT_MODIFIER" -> new PerlElementType(name);
      case "GIVEN_COMPOUND" -> new PerlElementType(name);
      case "GLOB_CAST_EXPR" -> new PerlDereferenceElementType.Glob(name);
      case "GLOB_SLOT" -> new PerlElementType(name);
      case "GOTO_EXPR" -> new PerlElementType(name);
      case "GREP_EXPR" -> new PerlElementType(name);
      case "HASH_CAST_EXPR" -> new PerlDereferenceElementType.Hash(name);
      case "HASH_INDEX" -> new PerlHashIndexElementType(name);
      case "HASH_VARIABLE" -> new PerlElementType(name);
      case "HEREDOC_OPENER" -> new PerlElementType(name);
      case "IF_COMPOUND" -> new PerlElementType(name);
      case "IF_STATEMENT_MODIFIER" -> new PerlElementType(name);
      case "LAST_EXPR" -> new PerlElementType(name);
      case "LP_AND_EXPR" -> new PerlElementType(name);
      case "LP_NOT_EXPR" -> new PerlElementType(name);
      case "LP_OR_XOR_EXPR" -> new PerlElementType(name);
      case "MAP_EXPR" -> new PerlElementType(name);
      case "MATCH_REGEX" -> new PerlMatchRegexpElementType(name);
      case "METHOD" -> new PerlElementType(name);
      case "METHOD_SIGNATURE_INVOCANT" -> new PerlElementType(name);
      case "MUL_EXPR" -> new PerlElementType(name);
      case "NAMED_BLOCK" -> new PerlElementType(name);
      case "NAMESPACE_CONTENT" -> new PerlElementType(name);
      case "NEXT_EXPR" -> new PerlElementType(name);
      case "NUMBER_CONSTANT" -> new PerlElementType(name);
      case "NYI_STATEMENT" -> new PerlElementType(name);
      case "OR_EXPR" -> new PerlElementType(name);
      case "PARENTHESISED_EXPR" -> new PerlParenthesizedElementType(name);
      case "PERL_HANDLE_EXPR" -> new PerlElementType(name);
      case "PERL_REGEX" -> new PerlElementType(name);
      case "PERL_REGEX_MODIFIERS" -> new PerlElementType(name);
      case "REGEX_REPLACEMENT" -> new PerlElementType(name);
      case "POW_EXPR" -> new PerlElementType(name);
      case "PREFIX_UNARY_EXPR" -> new PerlElementType(name);
      case "PREF_PP_EXPR" -> new PerlElementType(name);
      case "PRINT_EXPR" -> new PerlElementType(name);
      case "REDO_EXPR" -> new PerlElementType(name);
      case "REF_EXPR" -> new PerlElementType(name);
      case "REGEX_EXPR" -> new PerlElementType(name);
      case "REPLACEMENT_REGEX" -> new PerlRegexReplacementElementType(name);
      case "RETURN_EXPR" -> new PerlElementType(name);
      case "ARRAY_SHIFT_EXPR" -> new PerlElementType(name);
      case "ARRAY_UNSHIFT_EXPR" -> new PerlElementType(name);
      case "ARRAY_PUSH_EXPR" -> new PerlElementType(name);
      case "ARRAY_POP_EXPR" -> new PerlElementType(name);
      case "SCALAR_EXPR" -> new PerlElementType(name);
      case "DELETE_EXPR" -> new PerlElementType(name);
      case "SPLICE_EXPR" -> new PerlElementType(name);
      case "BLESS_EXPR" -> new PerlElementType(name);
      case "KEYS_EXPR" -> new PerlElementType(name);
      case "DEFINED_EXPR" -> new PerlElementType(name);
      case "WANTARRAY_EXPR" -> new PerlElementType(name);
      case "VALUES_EXPR" -> new PerlElementType(name);
      case "EACH_EXPR" -> new PerlElementType(name);
      case "EXIT_EXPR" -> new PerlElementType(name);
      case "ARRAY_ELEMENT" -> new PerlElementType(name);
      case "SCALAR_CALL" -> new PerlElementType(name);
      case "SCALAR_CAST_EXPR" -> new PerlDereferenceElementType.Scalar(name);
      case "HASH_ELEMENT" -> new PerlElementType(name);
      case "SCALAR_INDEX_CAST_EXPR" -> new PerlDereferenceElementType.ScalarIndex(name);
      case "SCALAR_VARIABLE" -> new PerlElementType(name);
      case "SHIFT_EXPR" -> new PerlElementType(name);
      case "SORT_EXPR" -> new PerlElementType(name);
      case "STATEMENT" -> new PerlElementType(name);
      case "STATEMENT_MODIFIER" -> new PerlElementType(name);
      case "STRING_BARE" -> new PerlElementType(name);
      case "STRING_DQ" -> new PerlQQStringElementType(name);
      case "STRING_LIST" -> new PerlStringListElementType(name);
      case "STRING_SQ" -> new PerlQStringElementType(name);
      case "STRING_XQ" -> new PerlQXStringElementType(name);
      case "SUB_CALL" -> new PerlSubCallElementType(name);
      case "SUB_EXPR" -> new PerlElementType(name);
      case "FUN_EXPR" -> new PerlElementType(name);
      case "METHOD_EXPR" -> new PerlElementType(name);
      case "CONTINUE_EXPR" -> new PerlElementType(name);
      case "SIGNATURE_CONTENT" -> new PerlElementType(name);
      case "SIGNATURE_ELEMENT" -> new PerlElementType(name);
      case "SUB_SIGNATURE_ELEMENT_IGNORE" -> new PerlElementType(name);
      case "SUFF_PP_EXPR" -> new PerlElementType(name);
      case "TAG_SCALAR" -> new PerlElementType(name);
      case "TERNARY_EXPR" -> new PerlElementType(name);
      case "TR_MODIFIERS" -> new PerlElementType(name);
      case "TR_REGEX" -> new PerlRegexReplacementElementType(name);
      case "TR_REPLACEMENTLIST" -> new PerlElementType(name);
      case "TR_SEARCHLIST" -> new PerlElementType(name);
      case "UNDEF_EXPR" -> new PerlElementType(name);
      case "UNLESS_COMPOUND" -> new PerlElementType(name);
      case "UNLESS_STATEMENT_MODIFIER" -> new PerlElementType(name);
      case "UNTIL_COMPOUND" -> new PerlElementType(name);
      case "UNTIL_STATEMENT_MODIFIER" -> new PerlElementType(name);
      case "VARIABLE_DECLARATION_GLOBAL" -> new PerlElementType(name);
      case "VARIABLE_DECLARATION_LEXICAL" -> new PerlElementType(name);
      case "VARIABLE_DECLARATION_LOCAL" -> new PerlElementType(name);
      case "VARIABLE_DECLARATION_WRAPPER" -> new PerlElementType(name);
      case "WHEN_COMPOUND" -> new PerlElementType(name);
      case "WHEN_STATEMENT_MODIFIER" -> new PerlElementType(name);
      case "WHILE_COMPOUND" -> new PerlElementType(name);
      case "WHILE_STATEMENT_MODIFIER" -> new PerlElementType(name);
      case "TRYCATCH_EXPR" -> new PerlElementType(name);
      case "TRYCATCH_COMPOUND" -> new PerlElementType(name);
      case "TRY_EXPR" -> new PerlElementType(name);
      case "CATCH_EXPR" -> new PerlElementType(name);
      case "CATCH_CONDITION" -> new PerlElementType(name);
      case "TYPE_CONSTRAINTS" -> new PerlElementType(name);
      case "TYPE_SPECIFIER" -> new PerlElementType(name);
      case "FINALLY_EXPR" -> new PerlElementType(name);
      case "EXCEPT_EXPR" -> new PerlElementType(name);
      case "OTHERWISE_EXPR" -> new PerlElementType(name);
      case "CONTINUATION_EXPR" -> new PerlElementType(name);
      case "POST_DEREF_EXPR" -> new PerlElementType(name);
      case "POST_DEREF_GLOB_EXPR" -> new PerlElementType(name);
      case "POST_DEREF_ARRAY_SLICE_EXPR" -> new PerlElementType(name);
      case "POST_DEREF_HASH_SLICE_EXPR" -> new PerlElementType(name);
      case "LABEL_DECLARATION" -> new PerlElementType(name);
      case "LABEL_EXPR" -> new PerlElementType(name);
      case "ANNOTATION_ABSTRACT" -> new PerlElementType(name);
      case "ANNOTATION_DEPRECATED" -> new PerlElementType(name);
      case "ANNOTATION_METHOD" -> new PerlElementType(name);
      case "ANNOTATION_OVERRIDE" -> new PerlElementType(name);
      case "ANNOTATION_RETURNS" -> new PerlElementType(name);
      case "ANNOTATION_TYPE" -> new PerlElementType(name);
      case "ANNOTATION_INJECT" -> new PerlElementType(name);
      case "ANNOTATION_NO_INJECT" -> new PerlElementType(name);
      case "ANNOTATION_NOINSPECTION" -> new PerlElementType(name);
      case "PARENTHESISED_CALL_ARGUMENTS" -> new PerlParenthesizedCallArgumentsElementType(name);
      case "PACKAGE_EXPR" -> new PerlElementType(name);
      case "ARRAYREF_TYPE" -> new PerlElementType(name);
      case "HASHREF_TYPE" -> new PerlElementType(name);
      case "FOREACH_ITERATOR" -> new PerlElementType(name);
      case "ATTRIBUTES" -> new PerlElementType(name);
      case "SWITCH_COMPOUND" -> new PerlElementType(name);
      case "SWITCH_CONDITION" -> new PerlElementType(name);
      case "CASE_COMPOUND" -> new PerlElementType(name);
      case "CASE_DEFAULT" -> new PerlElementType(name);
      case "CASE_CONDITION" -> new PerlElementType(name);
      case "CUSTOM_ATOM_EXPR" -> new PerlElementType(name);
      case "COMPOSITE_ATOM_EXPR" -> new PerlElementType(name);
      case "ANNOTATION_VARIABLE" -> new PerlElementType(name);
      default -> {
        LOG.error("Unknown token:" + name);
        throw new RuntimeException("Unknown token:" + name);
      }
    };
  }
}