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
import com.intellij.psi.tree.IElementType;
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
      case "regex" -> new PerlRegexTokenType(name);
      case "STRING_CONTENT", "STRING_CONTENT_QQ", "STRING_CONTENT_XQ" -> new PerlStringContentTokenType(name);
      case "SCALAR_NAME", "ARRAY_NAME", "HASH_NAME", "GLOB_NAME" -> new PerlVariableNameTokenType(name);
      case "POD" -> new PerlPodReparseableTokenType(name);
      case "subname", "list", "unary", "unary_custom", "argumentless" -> new PerlSubNameTokenType(name);
      case "package::name", "package::name::", __PACKAGE__ -> new PerlNamespaceNameTokenType(name);
      case "COMMENT_LINE" -> new PerlReparseableCommentTokenType(name);
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
      case "ADD_EXPR", "AFTER_MODIFIER", "AND_EXPR", "ANNOTATION_ABSTRACT", "ANNOTATION_DEPRECATED", "ANNOTATION_INJECT",
           "ANNOTATION_METHOD", "ANNOTATION_NOINSPECTION", "ANNOTATION_NO_INJECT", "ANNOTATION_OVERRIDE", "ANNOTATION_RETURNS",
           "ANNOTATION_TYPE", "ANNOTATION_VARIABLE", "AROUND_MODIFIER", "AROUND_SIGNATURE_INVOCANTS", "ARRAYREF_TYPE", "ARRAY_ELEMENT",
           "ARRAY_INDEX_VARIABLE", "ARRAY_POP_EXPR", "ARRAY_PUSH_EXPR", "ARRAY_SHIFT_EXPR", "ARRAY_SLICE", "ARRAY_UNSHIFT_EXPR",
           "ARRAY_VARIABLE", "ASSIGN_EXPR", "ATTRIBUTE", "ATTRIBUTES", "AUGMENT_MODIFIER", "BEFORE_MODIFIER", "BITWISE_AND_EXPR",
           "BITWISE_OR_XOR_EXPR", "BLESS_EXPR", "BLOCK_ARRAY", "BLOCK_BRACELESS", "BLOCK_CODE", "BLOCK_COMPOUND", "BLOCK_GLOB",
           "BLOCK_HASH", "BLOCK_SCALAR", "CALL_ARGUMENTS", "CASE_COMPOUND", "CASE_CONDITION", "CASE_DEFAULT", "CATCH_CONDITION",
           "CATCH_EXPR", "CODE_VARIABLE", "COMMA_SEQUENCE_EXPR", "COMPARE_EXPR", "COMPOSITE_ATOM_EXPR", "CONDITIONAL_BLOCK",
           "CONTINUATION_EXPR", "CONTINUE_BLOCK", "CONTINUE_EXPR", "CUSTOM_ATOM_EXPR", "DEFAULT_COMPOUND", "DEFINED_EXPR", "DELETE_EXPR",
           "DEREF_EXPR", "DO_BLOCK_EXPR", "EACH_EXPR", "EQUAL_EXPR", "ESC_CHAR", "EVAL_EXPR", "EXCEPT_EXPR", "EXIT_EXPR", "EXPR",
           "FILE_GLOB_EXPR", "FILE_READ_EXPR", "FINALLY_EXPR", "FLIPFLOP_EXPR", "FOREACH_COMPOUND", "FOREACH_ITERATOR", "FORMAT_DEFINITION",
           "FOR_COMPOUND", "FOR_CONDITION", "FOR_INIT", "FOR_MUTATOR", "FOR_OR_FOREACH", "FOR_STATEMENT_MODIFIER", "FUN_EXPR",
           "GIVEN_COMPOUND", "GLOB_SLOT", "GOTO_EXPR", "GREP_EXPR", "HASHREF_TYPE", "HASH_ARRAY_SLICE", "HASH_ELEMENT", "HASH_HASH_SLICE",
           "HASH_SLICE", "HASH_VARIABLE", "HEREDOC_OPENER", "HEX_CHAR", "IF_COMPOUND", "IF_STATEMENT_MODIFIER", "ISA_EXPR", "KEYS_EXPR",
           "LABEL_DECLARATION", "LABEL_EXPR", "LAST_EXPR", "LP_AND_EXPR", "LP_NOT_EXPR", "LP_OR_XOR_EXPR", "MAP_EXPR", "METHOD",
           "METHOD_EXPR", "METHOD_SIGNATURE_INVOCANT", "MUL_EXPR", "NAMED_BLOCK", "NAMESPACE_CONTENT", "NEXT_EXPR", "NUMBER_CONSTANT",
           "NYI_STATEMENT", "OCT_CHAR", "OR_EXPR", "OTHERWISE_EXPR", "PACKAGE_EXPR", "PERL_HANDLE_EXPR", "PERL_REGEX",
           "PERL_REGEX_MODIFIERS", "POST_DEREF_ARRAY_SLICE_EXPR", "POST_DEREF_EXPR", "POST_DEREF_GLOB_EXPR", "POST_DEREF_HASH_SLICE_EXPR",
           "POW_EXPR", "PREFIX_UNARY_EXPR", "PREF_PP_EXPR", "PRINT_EXPR", "REDO_EXPR", "REF_EXPR", "REGEX_EXPR", "REGEX_REPLACEMENT",
           "RETURN_EXPR", "SCALAR_CALL", "SCALAR_EXPR", "SCALAR_VARIABLE", "SHIFT_EXPR", "SIGNATURE_CONTENT", "SIGNATURE_ELEMENT",
           "SORT_EXPR", "SPLICE_EXPR", "STATEMENT", "STATEMENT_MODIFIER", "STRING_BARE", "SUB_EXPR", "SUB_SIGNATURE_ELEMENT_IGNORE",
           "SUFF_PP_EXPR", "SWITCH_COMPOUND", "SWITCH_CONDITION", "TAG_SCALAR", "TERNARY_EXPR", "TRYCATCH_COMPOUND", "TRYCATCH_EXPR",
           "TRY_EXPR", "TR_MODIFIERS", "TR_REPLACEMENTLIST", "TR_SEARCHLIST", "TYPE_CONSTRAINTS", "TYPE_SPECIFIER", "UNCONDITIONAL_BLOCK",
           "UNDEF_EXPR", "UNICODE_CHAR", "UNLESS_COMPOUND", "UNLESS_STATEMENT_MODIFIER", "UNTIL_COMPOUND", "UNTIL_STATEMENT_MODIFIER",
           "VALUES_EXPR", "VARIABLE_DECLARATION_GLOBAL", "VARIABLE_DECLARATION_LEXICAL", "VARIABLE_DECLARATION_LOCAL",
           "VARIABLE_DECLARATION_WRAPPER", "WANTARRAY_EXPR", "WHEN_COMPOUND", "WHEN_STATEMENT_MODIFIER", "WHILE_COMPOUND",
           "WHILE_STATEMENT_MODIFIER" -> new PerlElementType(name);
      case "REQUIRE_EXPR" -> PerlStubElementTypes.PERL_REQUIRE_EXPR;
      case "ANON_ARRAY", "ARRAY_INDEX" -> new PerlAnonArrayElementType(name);
      case "ANON_HASH" -> new PerlAnonHashElementType();
      case "ARRAY_CAST_EXPR" -> new PerlDereferenceElementType.Array(name);
      case "BLOCK" -> new PerlCodeBlockElementType();
      case "CODE_CAST_EXPR" -> new PerlDereferenceElementType.Code(name);
      case "COMPILE_REGEX" -> new PerlCompileRegexpElementType(name);
      case "CONDITION_EXPR", "PARENTHESISED_EXPR" -> new PerlParenthesizedElementType(name);
      case "GLOB_CAST_EXPR" -> new PerlDereferenceElementType.Glob(name);
      case "HASH_CAST_EXPR" -> new PerlDereferenceElementType.Hash(name);
      case "HASH_INDEX" -> new PerlHashIndexElementType(name);
      case "MATCH_REGEX" -> new PerlMatchRegexpElementType(name);
      case "REPLACEMENT_REGEX", "TR_REGEX" -> new PerlRegexReplacementElementType(name);
      case "SCALAR_CAST_EXPR" -> new PerlDereferenceElementType.Scalar(name);
      case "SCALAR_INDEX_CAST_EXPR" -> new PerlDereferenceElementType.ScalarIndex(name);
      case "STRING_DQ" -> new PerlQQStringElementType(name);
      case "STRING_LIST" -> new PerlStringListElementType(name);
      case "STRING_SQ" -> new PerlQStringElementType(name);
      case "STRING_XQ" -> new PerlQXStringElementType(name);
      case "SUB_CALL" -> new PerlSubCallElementType(name);
      case "PARENTHESISED_CALL_ARGUMENTS" -> new PerlParenthesizedCallArgumentsElementType(name);
      default -> {
        LOG.error("Unknown token:" + name);
        throw new RuntimeException("Unknown token:" + name);
      }
    };
  }
}