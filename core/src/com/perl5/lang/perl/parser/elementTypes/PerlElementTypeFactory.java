/*
 * Copyright 2015-2017 Alexandr Evstigneev
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
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.psi.impl.*;
import com.perl5.lang.perl.psi.stubs.PerlStubElementTypes;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.util.PerlPackageUtil.__PACKAGE__;

/**
 * Created by hurricup on 25.05.2015.
 */
public class PerlElementTypeFactory {
  private static final Logger LOG = Logger.getInstance(PerlElementTypeFactory.class);

  @NotNull
  public static IElementType getTokenType(@NotNull String name) {
    switch (name) {
      case "STRING_CONTENT":
      case "STRING_CONTENT_QQ":
      case "STRING_CONTENT_XQ":
        return new PerlStringContentTokenType(name);
      case "SCALAR_NAME":
      case "ARRAY_NAME":
      case "HASH_NAME":
      case "GLOB_NAME":
      case "CODE_NAME":
        return new PerlTokenTypeEx(name, PerlVariableNameElementImpl.class);
      case "subname":
      case "list":
      case "unary":
      case "unary_custom":
      case "argumentless":
        return new PerlTokenTypeEx(name, PerlSubNameElementImpl.class);
      case "package::name":
      case "package::name::":
      case "constant":
      case "vars":
      case __PACKAGE__:
        return new PerlTokenTypeEx(name, PerlNamespaceElementImpl.class);
      case "LAZY_PARSABLE_BLOCK":
        return new PerlLazyCodeBlockElementType(name);
      case "LAZY_PARSABLE_BLOCK_WITH_TRYCATCH":
        return new PerlLazyCodeBlockElementTypeWithTryCatch(name);
      case "HEREDOC_END":
      case "HEREDOC_END_INDENTABLE":
        return new PerlTokenTypeEx(name, PerlHeredocTerminatorElementImpl.class);
      case "VERSION_ELEMENT":
        return new PerlTokenTypeEx(name, PerlVersionElementImpl.class);
      case "HEREDOC_QQ":
        return new PerlLazyQQStringElementType(name, PerlHeredocElementImpl.class);
      case "HEREDOC_QX":
        return new PerlLazyQXStringElementType(name, PerlHeredocElementImpl.class);
      case "HEREDOC":
        return new PerlLazyQStringElementType(name, PerlHeredocElementImpl.class);
      case "PARSABLE_STRING_USE_VARS":
        return new PerlLazyUseVarsElementType(name);
      case "LP_REGEX":
        return new PerlLazyMatchRegexpElementType(name);
      case "LP_REGEX_X":
        return new PerlLazyExtendedMatchRegexpElementType(name);
      case "LP_REGEX_XX":
        return new PerlLazySuperExtendedMatchRegexpElementType(name);
      case "LP_REGEX_REPLACEMENT":
        return new PerlLazyReplacementRegexpElementType(name);
      case "LP_STRING_Q":
        return new PerlLazyQStringElementType(name);
      case "LP_STRING_QQ":
        return new PerlLazyQQStringElementType(name);
      case "LP_STRING_QX":
        return new PerlLazyQXStringElementType(name);
      case "LP_STRING_QW":
        return new PerlLazyQWStringElementType(name);
      case "COMMENT_ANNOTATION":
        return new PerlLazyAnnotationElementType("PERL_ANNOTATION");
    }
    return new PerlTokenType(name);
  }

  @NotNull
  public static IElementType getElementType(@NotNull String name) {
    switch (name) {
      case "SUB_DEFINITION":
        return PerlStubElementTypes.SUB_DEFINITION;
      case "METHOD_DEFINITION":
        return PerlStubElementTypes.METHOD_DEFINITION;
      case "FUNC_DEFINITION":
        return PerlStubElementTypes.FUNC_DEFINITION;
      case "SUB_DECLARATION":
        return PerlStubElementTypes.SUB_DECLARATION;
      case "GLOB_VARIABLE":
        return PerlStubElementTypes.PERL_GLOB;
      case "NAMESPACE_DEFINITION":
        return PerlStubElementTypes.PERL_NAMESPACE;
      case "VARIABLE_DECLARATION_ELEMENT":
        return PerlStubElementTypes.PERL_VARIABLE_DECLARATION_ELEMENT;
      case "USE_STATEMENT":
        return PerlStubElementTypes.PERL_USE_STATEMENT;
      case "NO_STATEMENT":
        return PerlStubElementTypes.PERL_NO_STATEMENT;
      case "DO_EXPR":
        return PerlStubElementTypes.PERL_DO_EXPR;
      case "REQUIRE_EXPR":
        return PerlStubElementTypes.PERL_REQUIRE_EXPR;
      case "ADD_EXPR":
        return new PerlElementTypeEx(name, PsiPerlAddExprImpl.class);
      case "AND_EXPR":
        return new PerlElementTypeEx(name, PsiPerlAndExprImpl.class);
      case "ANON_ARRAY":
        return new PerlElementTypeEx(name, PsiPerlAnonArrayImpl.class);
      case "ANON_HASH":
        return new PerlElementTypeEx(name, PsiPerlAnonHashImpl.class);
      case "ARRAY_SLICE":
        return new PerlElementTypeEx(name, PsiPerlArraySliceImpl.class);
      case "ARRAY_CAST_EXPR":
        return new PerlElementTypeEx(name, PsiPerlArrayCastExprImpl.class);
      case "HASH_SLICE":
        return new PerlElementTypeEx(name, PsiPerlHashSliceImpl.class);
      case "ARRAY_INDEX":
        return new PerlElementTypeEx(name, PsiPerlArrayIndexImpl.class);
      case "ARRAY_INDEX_VARIABLE":
        return new PerlElementTypeEx(name, PsiPerlArrayIndexVariableImpl.class);
      case "ARRAY_VARIABLE":
        return new PerlElementTypeEx(name, PsiPerlArrayVariableImpl.class);
      case "ASSIGN_EXPR":
        return new PerlElementTypeEx(name, PsiPerlAssignExprImpl.class);
      case "ATTRIBUTE":
        return new PerlElementTypeEx(name, PsiPerlAttributeImpl.class);
      case "BITWISE_AND_EXPR":
        return new PerlElementTypeEx(name, PsiPerlBitwiseAndExprImpl.class);
      case "BITWISE_OR_XOR_EXPR":
        return new PerlElementTypeEx(name, PsiPerlBitwiseOrXorExprImpl.class);
      case "BLOCK":
        return new PerlElementTypeEx(name, PsiPerlBlockImpl.class);
      case "BLOCK_COMPOUND":
        return new PerlElementTypeEx(name, PsiPerlBlockCompoundImpl.class);
      case "CALL_ARGUMENTS":
        return new PerlElementTypeEx(name, PsiPerlCallArgumentsImpl.class);
      case "CODE_CAST_EXPR":
        return new PerlElementTypeEx(name, PsiPerlCodeCastExprImpl.class);
      case "CODE_VARIABLE":
        return new PerlElementTypeEx(name, PsiPerlCodeVariableImpl.class);
      case "COMMA_SEQUENCE_EXPR":
        return new PerlElementTypeEx(name, PsiPerlCommaSequenceExprImpl.class);
      case "COMPARE_EXPR":
        return new PerlElementTypeEx(name, PsiPerlCompareExprImpl.class);
      case "COMPILE_REGEX":
        return new PerlElementTypeEx(name, PsiPerlCompileRegexImpl.class);
      case "CONDITIONAL_BLOCK":
        return new PerlElementTypeEx(name, PsiPerlConditionalBlockImpl.class);
      case "UNCONDITIONAL_BLOCK":
        return new PerlElementTypeEx(name, PsiPerlUnconditionalBlockImpl.class);
      case "CONDITION_EXPR":
        return new PerlElementTypeEx(name, PsiPerlConditionExprImpl.class);
      case "CONTINUE_BLOCK":
        return new PerlElementTypeEx(name, PsiPerlContinueBlockImpl.class);
      case "DEFAULT_COMPOUND":
        return new PerlElementTypeEx(name, PsiPerlDefaultCompoundImpl.class);
      case "DEREF_EXPR":
        return new PerlElementTypeEx(name, PsiPerlDerefExprImpl.class);
      case "EQUAL_EXPR":
        return new PerlElementTypeEx(name, PsiPerlEqualExprImpl.class);
      case "EVAL_EXPR":
        return new PerlElementTypeEx(name, PsiPerlEvalExprImpl.class);
      case "EXPR":
        return new PerlElementTypeEx(name) {
          @NotNull
          @Override
          public PsiElement getPsiElement(@NotNull ASTNode node) {
            throw new RuntimeException("Instantiating " + node);
          }
        };
      case "FILE_READ_EXPR":
        return new PerlElementTypeEx(name, PsiPerlFileReadExprImpl.class);
      case "FLIPFLOP_EXPR":
        return new PerlElementTypeEx(name, PsiPerlFlipflopExprImpl.class);
      case "FORMAT_DEFINITION":
        return new PerlElementTypeEx(name, PsiPerlFormatDefinitionImpl.class);
      case "FOR_COMPOUND":
        return new PerlElementTypeEx(name, PsiPerlForCompoundImpl.class);
      case "FOREACH_COMPOUND":
        return new PerlElementTypeEx(name, PsiPerlForeachCompoundImpl.class);
      case "FOR_OR_FOREACH":
        return new PerlElementTypeEx(name) {
          @NotNull
          @Override
          public PsiElement getPsiElement(@NotNull ASTNode node) {
            throw new RuntimeException("Instantiating " + node);
          }
        };
      case "FOR_INIT":
        return new PerlElementTypeEx(name, PsiPerlForInitImpl.class);
      case "FOR_CONDITION":
        return new PerlElementTypeEx(name, PsiPerlForConditionImpl.class);
      case "FOR_MUTATOR":
        return new PerlElementTypeEx(name, PsiPerlForMutatorImpl.class);
      case "FOR_STATEMENT_MODIFIER":
        return new PerlElementTypeEx(name, PsiPerlForStatementModifierImpl.class);
      case "FUNC_SIGNATURE_CONTENT":
        return new PerlElementTypeEx(name, PsiPerlFuncSignatureContentImpl.class);
      case "GIVEN_COMPOUND":
        return new PerlElementTypeEx(name, PsiPerlGivenCompoundImpl.class);
      case "GLOB_CAST_EXPR":
        return new PerlElementTypeEx(name, PsiPerlGlobCastExprImpl.class);
      case "GLOB_SLOT":
        return new PerlElementTypeEx(name, PsiPerlGlobSlotImpl.class);
      case "GOTO_EXPR":
        return new PerlElementTypeEx(name, PsiPerlGotoExprImpl.class);
      case "GREP_EXPR":
        return new PerlElementTypeEx(name, PsiPerlGrepExprImpl.class);
      case "HASH_CAST_EXPR":
        return new PerlElementTypeEx(name, PsiPerlHashCastExprImpl.class);
      case "HASH_INDEX":
        return new PerlElementTypeEx(name, PsiPerlHashIndexImpl.class);
      case "HASH_VARIABLE":
        return new PerlElementTypeEx(name, PsiPerlHashVariableImpl.class);
      case "HEREDOC_OPENER":
        return new PerlElementTypeEx(name, PsiPerlHeredocOpenerImpl.class);
      case "IF_COMPOUND":
        return new PerlElementTypeEx(name, PsiPerlIfCompoundImpl.class);
      case "IF_STATEMENT_MODIFIER":
        return new PerlElementTypeEx(name, PsiPerlIfStatementModifierImpl.class);
      case "LAST_EXPR":
        return new PerlElementTypeEx(name, PsiPerlLastExprImpl.class);
      case "LP_AND_EXPR":
        return new PerlElementTypeEx(name, PsiPerlLpAndExprImpl.class);
      case "LP_NOT_EXPR":
        return new PerlElementTypeEx(name, PsiPerlLpNotExprImpl.class);
      case "LP_OR_XOR_EXPR":
        return new PerlElementTypeEx(name, PsiPerlLpOrXorExprImpl.class);
      case "MAP_EXPR":
        return new PerlElementTypeEx(name, PsiPerlMapExprImpl.class);
      case "MATCH_REGEX":
        return new PerlElementTypeEx(name, PsiPerlMatchRegexImpl.class);
      case "METHOD":
        return new PerlElementTypeEx(name, PsiPerlMethodImpl.class);
      case "METHOD_SIGNATURE_CONTENT":
        return new PerlElementTypeEx(name, PsiPerlMethodSignatureContentImpl.class);
      case "METHOD_SIGNATURE_INVOCANT":
        return new PerlElementTypeEx(name, PsiPerlMethodSignatureInvocantImpl.class);
      case "MUL_EXPR":
        return new PerlElementTypeEx(name, PsiPerlMulExprImpl.class);
      case "NAMED_BLOCK":
        return new PerlElementTypeEx(name, PsiPerlNamedBlockImpl.class);
      case "NAMESPACE_CONTENT":
        return new PerlElementTypeEx(name, PsiPerlNamespaceContentImpl.class);
      case "NESTED_CALL":
        return new PerlNestedCallElementType(name);
      case "NEXT_EXPR":
        return new PerlElementTypeEx(name, PsiPerlNextExprImpl.class);
      case "NUMBER_CONSTANT":
        return new PerlElementTypeEx(name, PsiPerlNumberConstantImpl.class);
      case "NYI_STATEMENT":
        return new PerlElementTypeEx(name, PsiPerlNyiStatementImpl.class);
      case "OR_EXPR":
        return new PerlElementTypeEx(name, PsiPerlOrExprImpl.class);
      case "PARENTHESISED_EXPR":
        return new PerlElementTypeEx(name, PsiPerlParenthesisedExprImpl.class);
      case "PERL_HANDLE_EXPR":
        return new PerlElementTypeEx(name, PsiPerlPerlHandleExprImpl.class);
      case "PERL_REGEX":
        return new PerlElementTypeEx(name, PsiPerlPerlRegexImpl.class);
      case "PERL_REGEX_MODIFIERS":
        return new PerlElementTypeEx(name, PsiPerlPerlRegexModifiersImpl.class);
      case "POW_EXPR":
        return new PerlElementTypeEx(name, PsiPerlPowExprImpl.class);
      case "PREFIX_UNARY_EXPR":
        return new PerlElementTypeEx(name, PsiPerlPrefixUnaryExprImpl.class);
      case "PREF_PP_EXPR":
        return new PerlElementTypeEx(name, PsiPerlPrefPpExprImpl.class);
      case "PRINT_EXPR":
        return new PerlElementTypeEx(name, PsiPerlPrintExprImpl.class);
      case "REDO_EXPR":
        return new PerlElementTypeEx(name, PsiPerlRedoExprImpl.class);
      case "REF_EXPR":
        return new PerlElementTypeEx(name, PsiPerlRefExprImpl.class);
      case "REGEX_EXPR":
        return new PerlElementTypeEx(name, PsiPerlRegexExprImpl.class);
      case "REPLACEMENT_REGEX":
        return new PerlElementTypeEx(name, PsiPerlReplacementRegexImpl.class);
      case "RETURN_EXPR":
        return new PerlElementTypeEx(name, PsiPerlReturnExprImpl.class);
      case "SCALAR_EXPR":
        return new PerlElementTypeEx(name, PsiPerlScalarExprImpl.class);
      case "EXIT_EXPR":
        return new PerlElementTypeEx(name, PsiPerlExitExprImpl.class);
      case "ARRAY_ELEMENT":
        return new PerlElementTypeEx(name, PsiPerlArrayElementImpl.class);
      case "SCALAR_CALL":
        return new PerlElementTypeEx(name, PsiPerlScalarCallImpl.class);
      case "SCALAR_CAST_EXPR":
        return new PerlElementTypeEx(name, PsiPerlScalarCastExprImpl.class);
      case "HASH_ELEMENT":
        return new PerlElementTypeEx(name, PsiPerlHashElementImpl.class);
      case "SCALAR_INDEX_CAST_EXPR":
        return new PerlElementTypeEx(name, PsiPerlScalarIndexCastExprImpl.class);
      case "SCALAR_VARIABLE":
        return new PerlElementTypeEx(name, PsiPerlScalarVariableImpl.class);
      case "SHIFT_EXPR":
        return new PerlElementTypeEx(name, PsiPerlShiftExprImpl.class);
      case "SORT_EXPR":
        return new PerlElementTypeEx(name, PsiPerlSortExprImpl.class);
      case "STATEMENT":
        return new PerlElementTypeEx(name, PsiPerlStatementImpl.class);
      case "STATEMENT_MODIFIER":
        return new PerlElementTypeEx(name) {
          @NotNull
          @Override
          public PsiElement getPsiElement(@NotNull ASTNode node) {
            throw new RuntimeException("Instantiating " + node);
          }
        };
      case "STRING_BARE":
        return new PerlElementTypeEx(name, PsiPerlStringBareImpl.class);
      case "STRING_DQ":
        return new PerlElementTypeEx(name, PsiPerlStringDqImpl.class);
      case "STRING_LIST":
        return new PerlElementTypeEx(name, PsiPerlStringListImpl.class);
      case "STRING_SQ":
        return new PerlElementTypeEx(name, PsiPerlStringSqImpl.class);
      case "STRING_XQ":
        return new PerlElementTypeEx(name, PsiPerlStringXqImpl.class);
      case "SUB_CALL_EXPR":
        return new PerlElementTypeEx(name, PsiPerlSubCallExprImpl.class);
      case "SUB_EXPR":
        return new PerlElementTypeEx(name, PsiPerlSubExprImpl.class);
      case "CONTINUE_EXPR":
        return new PerlElementTypeEx(name, PsiPerlContinueExprImpl.class);
      case "SUB_SIGNATURE":
        return new PerlElementTypeEx(name, PsiPerlSubSignatureImpl.class);
      case "SUB_SIGNATURE_ELEMENT_IGNORE":
        return new PerlElementTypeEx(name, PsiPerlSubSignatureElementIgnoreImpl.class);
      case "SUFF_PP_EXPR":
        return new PerlElementTypeEx(name, PsiPerlSuffPpExprImpl.class);
      case "TAG_SCALAR":
        return new PerlElementTypeEx(name, PsiPerlTagScalarImpl.class);
      case "TERM_EXPR":
        return new PerlElementTypeEx(name, PsiPerlTermExprImpl.class);
      case "TERNARY_EXPR":
        return new PerlElementTypeEx(name, PsiPerlTernaryExprImpl.class);
      case "TR_MODIFIERS":
        return new PerlElementTypeEx(name, PsiPerlTrModifiersImpl.class);
      case "TR_REGEX":
        return new PerlElementTypeEx(name, PsiPerlTrRegexImpl.class);
      case "TR_REPLACEMENTLIST":
        return new PerlElementTypeEx(name, PsiPerlTrReplacementlistImpl.class);
      case "TR_SEARCHLIST":
        return new PerlElementTypeEx(name, PsiPerlTrSearchlistImpl.class);
      case "UNDEF_EXPR":
        return new PerlElementTypeEx(name, PsiPerlUndefExprImpl.class);
      case "UNLESS_COMPOUND":
        return new PerlElementTypeEx(name, PsiPerlUnlessCompoundImpl.class);
      case "UNLESS_STATEMENT_MODIFIER":
        return new PerlElementTypeEx(name, PsiPerlUnlessStatementModifierImpl.class);
      case "UNTIL_COMPOUND":
        return new PerlElementTypeEx(name, PsiPerlUntilCompoundImpl.class);
      case "UNTIL_STATEMENT_MODIFIER":
        return new PerlElementTypeEx(name, PsiPerlUntilStatementModifierImpl.class);
      case "VARIABLE_DECLARATION_GLOBAL":
        return new PerlElementTypeEx(name, PsiPerlVariableDeclarationGlobalImpl.class);
      case "VARIABLE_DECLARATION_LEXICAL":
        return new PerlElementTypeEx(name, PsiPerlVariableDeclarationLexicalImpl.class);
      case "VARIABLE_DECLARATION_LOCAL":
        return new PerlElementTypeEx(name, PsiPerlVariableDeclarationLocalImpl.class);
      case "VARIABLE_DECLARATION_WRAPPER":
        return new PerlElementTypeEx(name, PsiPerlVariableDeclarationElementImpl.class);
      case "WHEN_COMPOUND":
        return new PerlElementTypeEx(name, PsiPerlWhenCompoundImpl.class);
      case "WHEN_STATEMENT_MODIFIER":
        return new PerlElementTypeEx(name, PsiPerlWhenStatementModifierImpl.class);
      case "WHILE_COMPOUND":
        return new PerlElementTypeEx(name, PsiPerlWhileCompoundImpl.class);
      case "WHILE_STATEMENT_MODIFIER":
        return new PerlElementTypeEx(name, PsiPerlWhileStatementModifierImpl.class);
      case "TRYCATCH_EXPR":
        return new PerlElementTypeEx(name, PsiPerlTrycatchExprImpl.class);
      case "TRYCATCH_COMPOUND":
        return new PerlElementTypeEx(name, PsiPerlTrycatchCompoundImpl.class);
      case "TRY_EXPR":
        return new PerlElementTypeEx(name, PsiPerlTryExprImpl.class);
      case "CATCH_EXPR":
        return new PerlElementTypeEx(name, PsiPerlCatchExprImpl.class);
      case "CATCH_CONDITION":
        return new PerlElementTypeEx(name, PsiPerlCatchConditionImpl.class);
      case "TYPE_CONSTRAINTS":
        return new PerlElementTypeEx(name, PsiPerlTypeConstraintsImpl.class);
      case "FINALLY_EXPR":
        return new PerlElementTypeEx(name, PsiPerlFinallyExprImpl.class);
      case "EXCEPT_EXPR":
        return new PerlElementTypeEx(name, PsiPerlExceptExprImpl.class);
      case "OTHERWISE_EXPR":
        return new PerlElementTypeEx(name, PsiPerlOtherwiseExprImpl.class);
      case "CONTINUATION_EXPR":
        return new PerlElementTypeEx(name, PsiPerlContinuationExprImpl.class);
      case "POST_DEREF_EXPR":
        return new PerlElementTypeEx(name, PsiPerlPostDerefExprImpl.class);
      case "POST_DEREF_GLOB_EXPR":
        return new PerlElementTypeEx(name, PsiPerlPostDerefGlobExprImpl.class);
      case "POST_DEREF_ARRAY_SLICE_EXPR":
        return new PerlElementTypeEx(name, PsiPerlPostDerefArraySliceExprImpl.class);
      case "POST_DEREF_HASH_SLICE_EXPR":
        return new PerlElementTypeEx(name, PsiPerlPostDerefHashSliceExprImpl.class);
      case "LABEL_DECLARATION":
        return new PerlElementTypeEx(name, PsiPerlLabelDeclarationImpl.class);
      case "LABEL_EXPR":
        return new PerlElementTypeEx(name, PsiPerlLabelExprImpl.class);
      case "ANNOTATION_ABSTRACT":
        return new PerlElementTypeEx(name, PsiPerlAnnotationAbstractImpl.class);
      case "ANNOTATION_DEPRECATED":
        return new PerlElementTypeEx(name, PsiPerlAnnotationDeprecatedImpl.class);
      case "ANNOTATION_METHOD":
        return new PerlElementTypeEx(name, PsiPerlAnnotationMethodImpl.class);
      case "ANNOTATION_OVERRIDE":
        return new PerlElementTypeEx(name, PsiPerlAnnotationOverrideImpl.class);
      case "ANNOTATION_RETURNS":
        return new PerlElementTypeEx(name, PsiPerlAnnotationReturnsImpl.class);
      case "ANNOTATION_TYPE":
        return new PerlElementTypeEx(name, PsiPerlAnnotationTypeImpl.class);
      case "ANNOTATION_INJECT":
        return new PerlElementTypeEx(name, PsiPerlAnnotationInjectImpl.class);
      case "ANNOTATION_NOINSPECTION":
        return new PerlElementTypeEx(name, PsiPerlAnnotationNoinspectionImpl.class);
      case "PARENTHESISED_CALL_ARGUMENTS":
        return new PerlElementTypeEx(name, PsiPerlParenthesisedCallArgumentsImpl.class);
      case "PACKAGE_EXPR":
        return new PerlElementTypeEx(name, PsiPerlPackageExprImpl.class);
      case "ARRAYREF_TYPE":
        return new PerlElementTypeEx(name, PsiPerlArrayrefTypeImpl.class);
      case "HASHREF_TYPE":
        return new PerlElementTypeEx(name, PsiPerlHashrefTypeImpl.class);
      case "FOREACH_ITERATOR":
        return new PerlElementTypeEx(name, PsiPerlForeachIteratorImpl.class);
      case "ATTRIBUTES":
        return new PerlElementTypeEx(name, PsiPerlAttributesImpl.class);
      default: {
        LOG.error("Unknown token:" + name);
        throw new RuntimeException("Unknown token:" + name);
      }
    }
  }
}