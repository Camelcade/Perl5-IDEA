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

import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.psi.impl.*;
import com.perl5.lang.perl.psi.stubs.PerlStubElementTypes;
import com.perl5.lang.perl.psi.stubs.calls.PerlSubCallElementType;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.util.PerlPackageUtil.__PACKAGE__;


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
      case "HEREDOC_QQ", "HEREDOC_QX", "HEREDOC" -> new PerlHeredocElementType(name, PerlHeredocElementImpl.class);
      case "SUB_DEFINITION" -> PerlStubElementTypes.SUB_DEFINITION;
      case "METHOD_DEFINITION" -> PerlStubElementTypes.METHOD_DEFINITION;
      case "FUNC_DEFINITION" -> PerlStubElementTypes.FUNC_DEFINITION;
      case "SUB_DECLARATION" -> PerlStubElementTypes.SUB_DECLARATION;
      case "GLOB_VARIABLE" -> PerlStubElementTypes.PERL_GLOB;
      case "NAMESPACE_DEFINITION" -> PerlStubElementTypes.PERL_NAMESPACE;
      case "VARIABLE_DECLARATION_ELEMENT" -> PerlStubElementTypes.PERL_VARIABLE_DECLARATION_ELEMENT;
      case "DO_EXPR" -> PerlStubElementTypes.PERL_DO_EXPR;
      case "DO_BLOCK_EXPR" -> new PerlElementTypeEx(name, PsiPerlDoBlockExprImpl.class);
      case "REQUIRE_EXPR" -> PerlStubElementTypes.PERL_REQUIRE_EXPR;
      case "AFTER_MODIFIER" -> new PerlElementTypeEx(name, PsiPerlAfterModifierImpl.class);
      case "BEFORE_MODIFIER" -> new PerlElementTypeEx(name, PsiPerlBeforeModifierImpl.class);
      case "AROUND_MODIFIER" -> new PerlElementTypeEx(name, PsiPerlAroundModifierImpl.class);
      case "AROUND_SIGNATURE_INVOCANTS" -> new PerlElementTypeEx(name, PsiPerlAroundSignatureInvocantsImpl.class);
      case "AUGMENT_MODIFIER" -> new PerlElementTypeEx(name, PsiPerlAugmentModifierImpl.class);
      case "ADD_EXPR" -> new PerlElementTypeEx(name, PsiPerlAddExprImpl.class);
      case "AND_EXPR" -> new PerlElementTypeEx(name, PsiPerlAndExprImpl.class);
      case "ANON_ARRAY" -> new PerlAnonArrayElementType(name, PsiPerlAnonArrayImpl.class);
      case "UNICODE_CHAR" -> new PerlElementTypeEx(name, PsiPerlUnicodeCharImpl.class);
      case "HEX_CHAR" -> new PerlElementTypeEx(name, PsiPerlHexCharImpl.class);
      case "OCT_CHAR" -> new PerlElementTypeEx(name, PsiPerlOctCharImpl.class);
      case "ESC_CHAR" -> new PerlElementTypeEx(name, PsiPerlEscCharImpl.class);
      case "ANON_HASH" -> new PerlAnonHashElementType();
      case "ARRAY_SLICE" -> new PerlElementTypeEx(name, PsiPerlArraySliceImpl.class);
      case "HASH_ARRAY_SLICE" -> new PerlElementTypeEx(name, PsiPerlHashArraySliceImpl.class);
      case "ARRAY_CAST_EXPR" -> new PerlDereferenceElementType.Array(name, PsiPerlArrayCastExprImpl.class);
      case "HASH_SLICE" -> new PerlElementTypeEx(name, PsiPerlHashSliceImpl.class);
      case "HASH_HASH_SLICE" -> new PerlElementTypeEx(name, PsiPerlHashHashSliceImpl.class);
      case "ARRAY_INDEX" -> new PerlAnonArrayElementType(name, PsiPerlArrayIndexImpl.class);
      case "ARRAY_INDEX_VARIABLE" -> new PerlElementTypeEx(name, PsiPerlArrayIndexVariableImpl.class);
      case "ARRAY_VARIABLE" -> new PerlElementTypeEx(name, PsiPerlArrayVariableImpl.class);
      case "ASSIGN_EXPR" -> new PerlElementTypeEx(name, PsiPerlAssignExprImpl.class);
      case "ATTRIBUTE" -> new PerlElementTypeEx(name, PsiPerlAttributeImpl.class);
      case "ISA_EXPR" -> new PerlElementTypeEx(name, PsiPerlIsaExprImpl.class);
      case "BITWISE_AND_EXPR" -> new PerlElementTypeEx(name, PsiPerlBitwiseAndExprImpl.class);
      case "BITWISE_OR_XOR_EXPR" -> new PerlElementTypeEx(name, PsiPerlBitwiseOrXorExprImpl.class);
      case "BLOCK" -> new PerlCodeBlockElementType();
      case "BLOCK_BRACELESS" -> new PerlElementTypeEx(name, PsiPerlBlockBracelessImpl.class);
      case "BLOCK_SCALAR" -> new PerlElementTypeEx(name, PsiPerlBlockScalarImpl.class);
      case "BLOCK_ARRAY" -> new PerlElementTypeEx(name, PsiPerlBlockArrayImpl.class);
      case "BLOCK_HASH" -> new PerlElementTypeEx(name, PsiPerlBlockHashImpl.class);
      case "BLOCK_GLOB" -> new PerlElementTypeEx(name, PsiPerlBlockGlobImpl.class);
      case "BLOCK_CODE" -> new PerlElementTypeEx(name, PsiPerlBlockCodeImpl.class);
      case "BLOCK_COMPOUND" -> new PerlElementTypeEx(name, PsiPerlBlockCompoundImpl.class);
      case "CALL_ARGUMENTS" -> new PerlElementTypeEx(name, PsiPerlCallArgumentsImpl.class);
      case "CODE_CAST_EXPR" -> new PerlDereferenceElementType.Code(name, PsiPerlCodeCastExprImpl.class);
      case "CODE_VARIABLE" -> new PerlElementTypeEx(name, PsiPerlCodeVariableImpl.class);
      case "COMMA_SEQUENCE_EXPR" -> new PerlElementTypeEx(name, PsiPerlCommaSequenceExprImpl.class);
      case "COMPARE_EXPR" -> new PerlElementTypeEx(name, PsiPerlCompareExprImpl.class);
      case "COMPILE_REGEX" -> new PerlCompileRegexpElementType(name, PsiPerlCompileRegexImpl.class);
      case "CONDITIONAL_BLOCK" -> new PerlElementTypeEx(name, PsiPerlConditionalBlockImpl.class);
      case "UNCONDITIONAL_BLOCK" -> new PerlElementTypeEx(name, PsiPerlUnconditionalBlockImpl.class);
      case "CONDITION_EXPR" -> new PerlParenthesizedElementType(name, PsiPerlConditionExprImpl.class);
      case "CONTINUE_BLOCK" -> new PerlElementTypeEx(name, PsiPerlContinueBlockImpl.class);
      case "DEFAULT_COMPOUND" -> new PerlElementTypeEx(name, PsiPerlDefaultCompoundImpl.class);
      case "DEREF_EXPR" -> new PerlElementTypeEx(name, PsiPerlDerefExprImpl.class);
      case "EQUAL_EXPR" -> new PerlElementTypeEx(name, PsiPerlEqualExprImpl.class);
      case "EVAL_EXPR" -> new PerlElementTypeEx(name, PsiPerlEvalExprImpl.class);
      case "EXPR" -> new PerlElementTypeEx(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          throw new RuntimeException("Instantiating " + node);
        }
      };
      case "FILE_READ_EXPR" -> new PerlElementTypeEx(name, PsiPerlFileReadExprImpl.class);
      case "FILE_GLOB_EXPR" -> new PerlElementTypeEx(name, PsiPerlFileGlobExprImpl.class);
      case "FLIPFLOP_EXPR" -> new PerlElementTypeEx(name, PsiPerlFlipflopExprImpl.class);
      case "FORMAT_DEFINITION" -> new PerlElementTypeEx(name, PsiPerlFormatDefinitionImpl.class);
      case "FOR_COMPOUND" -> new PerlElementTypeEx(name, PsiPerlForCompoundImpl.class);
      case "FOREACH_COMPOUND" -> new PerlElementTypeEx(name, PsiPerlForeachCompoundImpl.class);
      case "FOR_OR_FOREACH" -> new PerlElementTypeEx(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          throw new RuntimeException("Instantiating " + node);
        }
      };
      case "FOR_INIT" -> new PerlElementTypeEx(name, PsiPerlForInitImpl.class);
      case "FOR_CONDITION" -> new PerlElementTypeEx(name, PsiPerlForConditionImpl.class);
      case "FOR_MUTATOR" -> new PerlElementTypeEx(name, PsiPerlForMutatorImpl.class);
      case "FOR_STATEMENT_MODIFIER" -> new PerlElementTypeEx(name, PsiPerlForStatementModifierImpl.class);
      case "GIVEN_COMPOUND" -> new PerlElementTypeEx(name, PsiPerlGivenCompoundImpl.class);
      case "GLOB_CAST_EXPR" -> new PerlDereferenceElementType.Glob(name, PsiPerlGlobCastExprImpl.class);
      case "GLOB_SLOT" -> new PerlElementTypeEx(name, PsiPerlGlobSlotImpl.class);
      case "GOTO_EXPR" -> new PerlElementTypeEx(name, PsiPerlGotoExprImpl.class);
      case "GREP_EXPR" -> new PerlElementTypeEx(name, PsiPerlGrepExprImpl.class);
      case "HASH_CAST_EXPR" -> new PerlDereferenceElementType.Hash(name, PsiPerlHashCastExprImpl.class);
      case "HASH_INDEX" -> new PerlHashIndexElementType(name, PsiPerlHashIndexImpl.class);
      case "HASH_VARIABLE" -> new PerlElementTypeEx(name, PsiPerlHashVariableImpl.class);
      case "HEREDOC_OPENER" -> new PerlElementTypeEx(name, PsiPerlHeredocOpenerImpl.class);
      case "IF_COMPOUND" -> new PerlElementTypeEx(name, PsiPerlIfCompoundImpl.class);
      case "IF_STATEMENT_MODIFIER" -> new PerlElementTypeEx(name, PsiPerlIfStatementModifierImpl.class);
      case "LAST_EXPR" -> new PerlElementTypeEx(name, PsiPerlLastExprImpl.class);
      case "LP_AND_EXPR" -> new PerlElementTypeEx(name, PsiPerlLpAndExprImpl.class);
      case "LP_NOT_EXPR" -> new PerlElementTypeEx(name, PsiPerlLpNotExprImpl.class);
      case "LP_OR_XOR_EXPR" -> new PerlElementTypeEx(name, PsiPerlLpOrXorExprImpl.class);
      case "MAP_EXPR" -> new PerlElementTypeEx(name, PsiPerlMapExprImpl.class);
      case "MATCH_REGEX" -> new PerlMatchRegexpElementType(name, PsiPerlMatchRegexImpl.class);
      case "METHOD" -> new PerlElementTypeEx(name, PsiPerlMethodImpl.class);
      case "METHOD_SIGNATURE_INVOCANT" -> new PerlElementTypeEx(name, PsiPerlMethodSignatureInvocantImpl.class);
      case "MUL_EXPR" -> new PerlElementTypeEx(name, PsiPerlMulExprImpl.class);
      case "NAMED_BLOCK" -> new PerlElementTypeEx(name, PsiPerlNamedBlockImpl.class);
      case "NAMESPACE_CONTENT" -> new PerlElementTypeEx(name, PsiPerlNamespaceContentImpl.class);
      case "NEXT_EXPR" -> new PerlElementTypeEx(name, PsiPerlNextExprImpl.class);
      case "NUMBER_CONSTANT" -> new PerlElementTypeEx(name, PsiPerlNumberConstantImpl.class);
      case "NYI_STATEMENT" -> new PerlElementTypeEx(name, PsiPerlNyiStatementImpl.class);
      case "OR_EXPR" -> new PerlElementTypeEx(name, PsiPerlOrExprImpl.class);
      case "PARENTHESISED_EXPR" -> new PerlParenthesizedElementType(name, PsiPerlParenthesisedExprImpl.class);
      case "PERL_HANDLE_EXPR" -> new PerlElementTypeEx(name, PsiPerlPerlHandleExprImpl.class);
      case "PERL_REGEX" -> new PerlElementTypeEx(name, PsiPerlPerlRegexImpl.class);
      case "PERL_REGEX_MODIFIERS" -> new PerlElementTypeEx(name, PsiPerlPerlRegexModifiersImpl.class);
      case "REGEX_REPLACEMENT" -> new PerlElementTypeEx(name, PsiPerlRegexReplacementImpl.class);
      case "POW_EXPR" -> new PerlElementTypeEx(name, PsiPerlPowExprImpl.class);
      case "PREFIX_UNARY_EXPR" -> new PerlElementTypeEx(name, PsiPerlPrefixUnaryExprImpl.class);
      case "PREF_PP_EXPR" -> new PerlElementTypeEx(name, PsiPerlPrefPpExprImpl.class);
      case "PRINT_EXPR" -> new PerlElementTypeEx(name, PsiPerlPrintExprImpl.class);
      case "REDO_EXPR" -> new PerlElementTypeEx(name, PsiPerlRedoExprImpl.class);
      case "REF_EXPR" -> new PerlElementTypeEx(name, PsiPerlRefExprImpl.class);
      case "REGEX_EXPR" -> new PerlElementTypeEx(name, PsiPerlRegexExprImpl.class);
      case "REPLACEMENT_REGEX" -> new PerlRegexReplacementElementType(name, PsiPerlReplacementRegexImpl.class);
      case "RETURN_EXPR" -> new PerlElementTypeEx(name, PsiPerlReturnExprImpl.class);
      case "ARRAY_SHIFT_EXPR" -> new PerlElementTypeEx(name, PsiPerlArrayShiftExprImpl.class);
      case "ARRAY_UNSHIFT_EXPR" -> new PerlElementTypeEx(name, PsiPerlArrayUnshiftExprImpl.class);
      case "ARRAY_PUSH_EXPR" -> new PerlElementTypeEx(name, PsiPerlArrayPushExprImpl.class);
      case "ARRAY_POP_EXPR" -> new PerlElementTypeEx(name, PsiPerlArrayPopExprImpl.class);
      case "SCALAR_EXPR" -> new PerlElementTypeEx(name, PsiPerlScalarExprImpl.class);
      case "DELETE_EXPR" -> new PerlElementTypeEx(name, PsiPerlDeleteExprImpl.class);
      case "SPLICE_EXPR" -> new PerlElementTypeEx(name, PsiPerlSpliceExprImpl.class);
      case "BLESS_EXPR" -> new PerlElementTypeEx(name, PsiPerlBlessExprImpl.class);
      case "KEYS_EXPR" -> new PerlElementTypeEx(name, PsiPerlKeysExprImpl.class);
      case "DEFINED_EXPR" -> new PerlElementTypeEx(name, PsiPerlDefinedExprImpl.class);
      case "WANTARRAY_EXPR" -> new PerlElementTypeEx(name, PsiPerlWantarrayExprImpl.class);
      case "VALUES_EXPR" -> new PerlElementTypeEx(name, PsiPerlValuesExprImpl.class);
      case "EACH_EXPR" -> new PerlElementTypeEx(name, PsiPerlEachExprImpl.class);
      case "EXIT_EXPR" -> new PerlElementTypeEx(name, PsiPerlExitExprImpl.class);
      case "ARRAY_ELEMENT" -> new PerlElementTypeEx(name, PsiPerlArrayElementImpl.class);
      case "SCALAR_CALL" -> new PerlElementTypeEx(name, PsiPerlScalarCallImpl.class);
      case "SCALAR_CAST_EXPR" -> new PerlDereferenceElementType.Scalar(name, PsiPerlScalarCastExprImpl.class);
      case "HASH_ELEMENT" -> new PerlElementTypeEx(name, PsiPerlHashElementImpl.class);
      case "SCALAR_INDEX_CAST_EXPR" -> new PerlDereferenceElementType.ScalarIndex(name, PsiPerlScalarIndexCastExprImpl.class);
      case "SCALAR_VARIABLE" -> new PerlElementTypeEx(name, PsiPerlScalarVariableImpl.class);
      case "SHIFT_EXPR" -> new PerlElementTypeEx(name, PsiPerlShiftExprImpl.class);
      case "SORT_EXPR" -> new PerlElementTypeEx(name, PsiPerlSortExprImpl.class);
      case "STATEMENT" -> new PerlElementTypeEx(name, PsiPerlStatementImpl.class);
      case "STATEMENT_MODIFIER" -> new PerlElementTypeEx(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          throw new RuntimeException("Instantiating " + node);
        }
      };
      case "STRING_BARE" -> new PerlElementTypeEx(name, PsiPerlStringBareImpl.class);
      case "STRING_DQ" -> new PerlQQStringElementType(name, PsiPerlStringDqImpl.class);
      case "STRING_LIST" -> new PerlStringListElementType(name, PsiPerlStringListImpl.class);
      case "STRING_SQ" -> new PerlQStringElementType(name, PsiPerlStringSqImpl.class);
      case "STRING_XQ" -> new PerlQXStringElementType(name, PsiPerlStringXqImpl.class);
      case "SUB_CALL" -> new PerlSubCallElementType(name);
      case "SUB_EXPR" -> new PerlElementTypeEx(name, PsiPerlSubExprImpl.class);
      case "FUN_EXPR" -> new PerlElementTypeEx(name, PsiPerlFunExprImpl.class);
      case "METHOD_EXPR" -> new PerlElementTypeEx(name, PsiPerlMethodExprImpl.class);
      case "CONTINUE_EXPR" -> new PerlElementTypeEx(name, PsiPerlContinueExprImpl.class);
      case "SIGNATURE_CONTENT" -> new PerlElementTypeEx(name, PsiPerlSignatureContentImpl.class);
      case "SIGNATURE_ELEMENT" -> new PerlElementTypeEx(name, PsiPerlSignatureElementImpl.class);
      case "SUB_SIGNATURE_ELEMENT_IGNORE" -> new PerlElementTypeEx(name, PsiPerlSubSignatureElementIgnoreImpl.class);
      case "SUFF_PP_EXPR" -> new PerlElementTypeEx(name, PsiPerlSuffPpExprImpl.class);
      case "TAG_SCALAR" -> new PerlElementTypeEx(name, PsiPerlTagScalarImpl.class);
      case "TERNARY_EXPR" -> new PerlElementTypeEx(name, PsiPerlTernaryExprImpl.class);
      case "TR_MODIFIERS" -> new PerlElementTypeEx(name, PsiPerlTrModifiersImpl.class);
      case "TR_REGEX" -> new PerlRegexReplacementElementType(name, PsiPerlTrRegexImpl.class);
      case "TR_REPLACEMENTLIST" -> new PerlElementTypeEx(name, PsiPerlTrReplacementlistImpl.class);
      case "TR_SEARCHLIST" -> new PerlElementTypeEx(name, PsiPerlTrSearchlistImpl.class);
      case "UNDEF_EXPR" -> new PerlElementTypeEx(name, PsiPerlUndefExprImpl.class);
      case "UNLESS_COMPOUND" -> new PerlElementTypeEx(name, PsiPerlUnlessCompoundImpl.class);
      case "UNLESS_STATEMENT_MODIFIER" -> new PerlElementTypeEx(name, PsiPerlUnlessStatementModifierImpl.class);
      case "UNTIL_COMPOUND" -> new PerlElementTypeEx(name, PsiPerlUntilCompoundImpl.class);
      case "UNTIL_STATEMENT_MODIFIER" -> new PerlElementTypeEx(name, PsiPerlUntilStatementModifierImpl.class);
      case "VARIABLE_DECLARATION_GLOBAL" -> new PerlElementTypeEx(name, PsiPerlVariableDeclarationGlobalImpl.class);
      case "VARIABLE_DECLARATION_LEXICAL" -> new PerlElementTypeEx(name, PsiPerlVariableDeclarationLexicalImpl.class);
      case "VARIABLE_DECLARATION_LOCAL" -> new PerlElementTypeEx(name, PsiPerlVariableDeclarationLocalImpl.class);
      case "VARIABLE_DECLARATION_WRAPPER" -> new PerlElementTypeEx(name, PsiPerlVariableDeclarationElementImpl.class);
      case "WHEN_COMPOUND" -> new PerlElementTypeEx(name, PsiPerlWhenCompoundImpl.class);
      case "WHEN_STATEMENT_MODIFIER" -> new PerlElementTypeEx(name, PsiPerlWhenStatementModifierImpl.class);
      case "WHILE_COMPOUND" -> new PerlElementTypeEx(name, PsiPerlWhileCompoundImpl.class);
      case "WHILE_STATEMENT_MODIFIER" -> new PerlElementTypeEx(name, PsiPerlWhileStatementModifierImpl.class);
      case "TRYCATCH_EXPR" -> new PerlElementTypeEx(name, PsiPerlTrycatchExprImpl.class);
      case "TRYCATCH_COMPOUND" -> new PerlElementTypeEx(name, PsiPerlTrycatchCompoundImpl.class);
      case "TRY_EXPR" -> new PerlElementTypeEx(name, PsiPerlTryExprImpl.class);
      case "CATCH_EXPR" -> new PerlElementTypeEx(name, PsiPerlCatchExprImpl.class);
      case "CATCH_CONDITION" -> new PerlElementTypeEx(name, PsiPerlCatchConditionImpl.class);
      case "TYPE_CONSTRAINTS" -> new PerlElementTypeEx(name, PsiPerlTypeConstraintsImpl.class);
      case "TYPE_SPECIFIER" -> new PerlElementTypeEx(name, PsiPerlTypeSpecifierImpl.class);
      case "FINALLY_EXPR" -> new PerlElementTypeEx(name, PsiPerlFinallyExprImpl.class);
      case "EXCEPT_EXPR" -> new PerlElementTypeEx(name, PsiPerlExceptExprImpl.class);
      case "OTHERWISE_EXPR" -> new PerlElementTypeEx(name, PsiPerlOtherwiseExprImpl.class);
      case "CONTINUATION_EXPR" -> new PerlElementTypeEx(name, PsiPerlContinuationExprImpl.class);
      case "POST_DEREF_EXPR" -> new PerlElementTypeEx(name, PsiPerlPostDerefExprImpl.class);
      case "POST_DEREF_GLOB_EXPR" -> new PerlElementTypeEx(name, PsiPerlPostDerefGlobExprImpl.class);
      case "POST_DEREF_ARRAY_SLICE_EXPR" -> new PerlElementTypeEx(name, PsiPerlPostDerefArraySliceExprImpl.class);
      case "POST_DEREF_HASH_SLICE_EXPR" -> new PerlElementTypeEx(name, PsiPerlPostDerefHashSliceExprImpl.class);
      case "LABEL_DECLARATION" -> new PerlElementTypeEx(name, PsiPerlLabelDeclarationImpl.class);
      case "LABEL_EXPR" -> new PerlElementTypeEx(name, PsiPerlLabelExprImpl.class);
      case "ANNOTATION_ABSTRACT" -> new PerlElementTypeEx(name, PsiPerlAnnotationAbstractImpl.class);
      case "ANNOTATION_DEPRECATED" -> new PerlElementTypeEx(name, PsiPerlAnnotationDeprecatedImpl.class);
      case "ANNOTATION_METHOD" -> new PerlElementTypeEx(name, PsiPerlAnnotationMethodImpl.class);
      case "ANNOTATION_OVERRIDE" -> new PerlElementTypeEx(name, PsiPerlAnnotationOverrideImpl.class);
      case "ANNOTATION_RETURNS" -> new PerlElementTypeEx(name, PsiPerlAnnotationReturnsImpl.class);
      case "ANNOTATION_TYPE" -> new PerlElementTypeEx(name, PsiPerlAnnotationTypeImpl.class);
      case "ANNOTATION_INJECT" -> new PerlElementTypeEx(name, PsiPerlAnnotationInjectImpl.class);
      case "ANNOTATION_NO_INJECT" -> new PerlElementTypeEx(name, PsiPerlAnnotationNoInjectImpl.class);
      case "ANNOTATION_NOINSPECTION" -> new PerlElementTypeEx(name, PsiPerlAnnotationNoinspectionImpl.class);
      case "PARENTHESISED_CALL_ARGUMENTS" ->
        new PerlParenthesizedCallArgumentsElementType(name, PsiPerlParenthesisedCallArgumentsImpl.class);
      case "PACKAGE_EXPR" -> new PerlElementTypeEx(name, PsiPerlPackageExprImpl.class);
      case "ARRAYREF_TYPE" -> new PerlElementTypeEx(name, PsiPerlArrayrefTypeImpl.class);
      case "HASHREF_TYPE" -> new PerlElementTypeEx(name, PsiPerlHashrefTypeImpl.class);
      case "FOREACH_ITERATOR" -> new PerlElementTypeEx(name, PsiPerlForeachIteratorImpl.class);
      case "ATTRIBUTES" -> new PerlElementTypeEx(name, PsiPerlAttributesImpl.class);
      case "SWITCH_COMPOUND" -> new PerlElementTypeEx(name, PsiPerlSwitchCompoundImpl.class);
      case "SWITCH_CONDITION" -> new PerlElementTypeEx(name, PsiPerlSwitchConditionImpl.class);
      case "CASE_COMPOUND" -> new PerlElementTypeEx(name, PsiPerlCaseCompoundImpl.class);
      case "CASE_DEFAULT" -> new PerlElementTypeEx(name, PsiPerlCaseDefaultImpl.class);
      case "CASE_CONDITION" -> new PerlElementTypeEx(name, PsiPerlCaseConditionImpl.class);
      case "CUSTOM_ATOM_EXPR" -> new PerlElementTypeEx(name, PsiPerlCustomAtomExprImpl.class);
      case "COMPOSITE_ATOM_EXPR" -> new PerlElementTypeEx(name, PsiPerlCompositeAtomExprImpl.class);
      case "ANNOTATION_VARIABLE" -> new PerlElementTypeEx(name, PsiPerlAnnotationVariableImpl.class);
      default -> {
        LOG.error("Unknown token:" + name);
        throw new RuntimeException("Unknown token:" + name);
      }
    };
  }
}