/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea;

import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.perl5.lang.mojolicious.psi.impl.MojoliciousFileImpl;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.PlatformPatterns.psiFile;

/**
 * Created by hurricup on 31.05.2015.
 */
public interface PerlElementPatterns extends PerlElementTypes
{
	PsiElementPattern.Capture<PsiElement> WHITE_SPACE_AND_COMMENTS = psiElement().whitespaceCommentOrError();

	PsiElementPattern.Capture<PerlStringContentElement> STRING_CONTENT_PATTERN = psiElement(PerlStringContentElement.class);

	PsiElementPattern.Capture<PerlStringContentElement> STRING_CONTENT_IN_STRING_BARE = STRING_CONTENT_PATTERN.inside(PsiPerlStringBare.class);
	PsiElementPattern.Capture<PerlStringContentElement> STRING_CONTENT_IN_SQ_STRING_BEGIN = STRING_CONTENT_PATTERN.afterLeaf(psiElement(QUOTE_SINGLE_OPEN)).inside(PsiPerlStringSq.class);
	PsiElementPattern.Capture<PerlStringContentElement> STRING_CONTENT_IN_DQ_STRING_BEGIN = STRING_CONTENT_PATTERN.afterLeaf(psiElement(QUOTE_DOUBLE_OPEN)).inside(PsiPerlStringDq.class);
	PsiElementPattern.Capture<PerlStringContentElement> STRING_CONTENT_IN_QW_STRING_LIST = STRING_CONTENT_PATTERN.inside(PsiPerlStringList.class);

	PsiElementPattern.Capture<PerlStringContentElement> STRING_CONTENT_IN_LIST_OR_STRING_START = STRING_CONTENT_PATTERN.andOr(
			STRING_CONTENT_IN_QW_STRING_LIST,
			STRING_CONTENT_IN_SQ_STRING_BEGIN,
			STRING_CONTENT_IN_DQ_STRING_BEGIN
	);


	PsiElementPattern.Capture<PerlStringContentElement> SIMPLE_HASH_INDEX = STRING_CONTENT_PATTERN.withSuperParent(2, PsiPerlHashIndex.class).andOr(
			STRING_CONTENT_IN_STRING_BARE,
			STRING_CONTENT_IN_SQ_STRING_BEGIN,
			STRING_CONTENT_IN_DQ_STRING_BEGIN,
			STRING_CONTENT_IN_QW_STRING_LIST
	);

	PsiElementPattern.Capture<PerlNamespaceElement> NAMESPACE_NAME_PATTERN = psiElement(PerlNamespaceElement.class);
	PsiElementPattern.Capture<PsiPerlUseStatement> USE_STATEMENT_PATTERN = psiElement(PsiPerlUseStatement.class);
	PsiElementPattern.Capture<PsiPerlNoStatement> NO_STATEMENT_PATTERN = psiElement(PsiPerlNoStatement.class);
	PsiElementPattern.Capture<PerlSubNameElement> SUB_NAME_PATTERN = psiElement(PerlSubNameElement.class);

	PsiElementPattern.Capture<PsiPerlMethod> METHOD_PATTERN = psiElement(PsiPerlMethod.class);
	PsiElementPattern.Capture IN_OBJECT_CALL_PATTERN = psiElement().withParent(PsiPerlNestedCall.class);

	PsiElementPattern.Capture IN_STATIC_METHOD_PATTERN = psiElement().withParent(METHOD_PATTERN.andNot(IN_OBJECT_CALL_PATTERN));
	PsiElementPattern.Capture IN_OBJECT_METHOD_PATTERN = psiElement().withParent(METHOD_PATTERN.and(IN_OBJECT_CALL_PATTERN));

	PsiElementPattern.Capture<PerlVariable> VARIABLE_PATTERN = psiElement(PerlVariable.class);

	PsiElementPattern.Capture<PerlGlobVariable> GLOB_PATTERN = psiElement(PerlGlobVariable.class);

	PsiElementPattern.Capture IN_VARIABLE_PATTERN = psiElement().inside(VARIABLE_PATTERN);
	PsiElementPattern.Capture IN_GLOB_PATTERN = psiElement().inside(GLOB_PATTERN);

	// fixme move this to mojo patterns
	PsiElementPattern.Capture IN_MOJOLICIOUS_FILE = psiElement().inFile(psiFile(MojoliciousFileImpl.class));

	PsiElementPattern.Capture<PerlVariableNameElement> VARIABLE_NAME_PATTERN = psiElement(PerlVariableNameElement.class);
	PsiElementPattern.Capture<PerlVariableNameElement> VARIABLE_NAME_PATTERN_IN_DECLARATION = VARIABLE_NAME_PATTERN.withParent(
			VARIABLE_PATTERN.withParent(psiElement(PerlVariableDeclarationWrapper.class))
	);

	PsiElementPattern.Capture<PsiElement> UNKNOWN_ANNOTATION_PATTERN = psiElement(PerlElementTypes.ANNOTATION_UNKNOWN_KEY);

	PsiElementPattern.Capture INSIDE_SUB_SIGNATURE = psiElement().inside(PsiPerlSubSignatureContent.class);
	PsiElementPattern.Capture INSIDE_LEXICAL_DECLARATION = psiElement().inside(PsiPerlVariableDeclarationLexical.class);
	PsiElementPattern.Capture INSIDE_GLOBAL_DECLARATION = psiElement().inside(PsiPerlVariableDeclarationGlobal.class);
	PsiElementPattern.Capture INSIDE_USE_VARS = psiElement().inside(IPerlUseVars.class);


	PsiElementPattern.Capture<PerlVariableNameElement> VARIABLE_COMPLETION_PATTERN =
			VARIABLE_NAME_PATTERN.andOr(IN_VARIABLE_PATTERN, IN_GLOB_PATTERN)
					.andNot(INSIDE_GLOBAL_DECLARATION)
					.andNot(INSIDE_LEXICAL_DECLARATION)
					.andNot(INSIDE_SUB_SIGNATURE)
					.andNot(INSIDE_USE_VARS);


	PsiElementPattern.Capture<PsiPerlIfCompound> INCOMPLETED_IF_COMPOUND =
			psiElement(PsiPerlIfCompound.class)
					.andNot(
							psiElement()
									.withLastChild(psiElement(PsiPerlUnconditionalBlock.class)));

	PsiElementPattern.Capture<PsiElement> ELSE_ELSIF_PLACE =
			psiElement().inside(
					psiElement(PsiPerlStatement.class).afterSiblingSkipping(WHITE_SPACE_AND_COMMENTS, INCOMPLETED_IF_COMPOUND)
			);

	// @EXPORT = ();

	PsiElementPattern.Capture<PsiPerlArrayVariable> EXPORT_ARRAY_VARIABLE_PATTERN =
			psiElement(PsiPerlArrayVariable.class).andOr(
					psiElement().withText("@EXPORT"),
					psiElement().withText("@EXPORT_OK")
			);

	PsiElementPattern.Capture<PsiPerlVariableDeclarationGlobal> EXPORT_ARRAY_VARIABLE_DECLARATION =
			psiElement(PsiPerlVariableDeclarationGlobal.class).withChild(
					psiElement(PerlVariableDeclarationWrapper.class).withChild(EXPORT_ARRAY_VARIABLE_PATTERN)
			);


	PsiElementPattern.Capture<PsiElement> EXPORT_ASSIGNMENT =
			psiElement()
					.afterLeafSkipping(WHITE_SPACE_AND_COMMENTS, psiElement(OPERATOR_ASSIGN))
					.withParent(
							psiElement(PsiPerlAssignExpr.class).withFirstChild(
									psiElement().andOr(
											EXPORT_ARRAY_VARIABLE_PATTERN,
											EXPORT_ARRAY_VARIABLE_DECLARATION
									)
							)
					);

	PsiElementPattern.Capture<PsiPerlParenthesisedExpr> EXPORT_ASSIGNMENT_PARENTHESISED = psiElement(PsiPerlParenthesisedExpr.class).and(EXPORT_ASSIGNMENT);

	PsiElementPattern.Capture<PsiElement> EXPORT_ASSIGNED_STRING_CONTENT = psiElement().andOr(
			STRING_CONTENT_IN_QW_STRING_LIST.andOr(
					psiElement().withParent(EXPORT_ASSIGNMENT),
					psiElement().withSuperParent(2, EXPORT_ASSIGNMENT_PARENTHESISED)
			),
			STRING_CONTENT_PATTERN.withParent(
					psiElement(PerlString.class).andOr(
							psiElement().withParent(psiElement(PsiPerlCommaSequenceExpr.class).withParent(EXPORT_ASSIGNMENT_PARENTHESISED)),
							psiElement().withParent(EXPORT_ASSIGNMENT_PARENTHESISED)
					)
			)
	);

	PsiElementPattern.Capture<PerlStringContentElement> USE_PARAMETERS_PATTERN = STRING_CONTENT_PATTERN.andOr(
			psiElement().inside(USE_STATEMENT_PATTERN),
			psiElement().inside(NO_STATEMENT_PATTERN)
	);

	// pattern for shift;
	PsiElementPattern.Capture<PsiPerlNamedUnaryExpr> TAILING_SHIFT_PATTERN =
			psiElement(PsiPerlNamedUnaryExpr.class).withFirstChild(
					psiElement(PsiPerlMethod.class).withText("shift")
			).beforeLeaf(psiElement(SEMICOLON));


	PsiElementPattern.Capture<PsiPerlStatement> EMPTY_SHIFT_STATEMENT =
			psiElement(PsiPerlStatement.class).withFirstChild(TAILING_SHIFT_PATTERN);
}
