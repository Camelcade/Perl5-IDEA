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

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.perl5.lang.mojolicious.MojoliciousPerlFileImpl;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlHeredocTerminatorElementImpl;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.PlatformPatterns.psiFile;

/**
 * Created by hurricup on 31.05.2015.
 */
public interface PerlElementPatterns extends PerlElementTypes
{
	public static final PsiElementPattern.Capture<PerlStringContentElementImpl> STRING_CONENT_PATTERN = psiElement(PerlStringContentElementImpl.class).withLanguage(PerlLanguage.INSTANCE);
	public static final ElementPattern<PsiElement> SQ_OPENING_QUOTE = psiElement(QUOTE_SINGLE_OPEN);
	public static final PsiElementPattern.Capture<PerlStringContentElementImpl> SQ_STRING_BEGIN = STRING_CONENT_PATTERN.afterLeaf(SQ_OPENING_QUOTE).inside(PsiPerlStringSq.class);
	public static final ElementPattern<PsiElement> DQ_OPENING_QUOTE = psiElement(QUOTE_DOUBLE_OPEN);
	public static final PsiElementPattern.Capture<PerlStringContentElementImpl> DQ_STRING_BEGIN = STRING_CONENT_PATTERN.afterLeaf(DQ_OPENING_QUOTE).inside(PsiPerlStringDq.class);

	public static final PsiElementPattern.Capture<PerlNamespaceElement> NAMESPACE_NAME_PATTERN = psiElement(PerlNamespaceElement.class).withLanguage(PerlLanguage.INSTANCE);
	public static final PsiElementPattern.Capture<PsiPerlUseStatement> USE_STATEMENT_PATTERN = psiElement(PsiPerlUseStatement.class).withLanguage(PerlLanguage.INSTANCE);
	public static final PsiElementPattern.Capture<PsiPerlRequireExpr> REQUIRE_TERM_PATTERN = psiElement(PsiPerlRequireExpr.class).withLanguage(PerlLanguage.INSTANCE);
	public static final PsiElementPattern.Capture<PerlSubNameElement> SUB_NAME_PATTERN = psiElement(PerlSubNameElement.class).withLanguage(PerlLanguage.INSTANCE);

	public static final PsiElementPattern.Capture<PsiPerlMethod> METHOD_PATTERN = psiElement(PsiPerlMethod.class).withLanguage(PerlLanguage.INSTANCE);
	public static final PsiElementPattern.Capture IN_OBJECT_CALL_PATTERN = psiElement().withParent(PsiPerlNestedCall.class);

	public static final PsiElementPattern.Capture IN_STATIC_METHOD_PATTERN = psiElement().withParent(METHOD_PATTERN.andNot(IN_OBJECT_CALL_PATTERN));
	public static final PsiElementPattern.Capture IN_OBJECT_METHOD_PATTERN = psiElement().withParent(METHOD_PATTERN.and(IN_OBJECT_CALL_PATTERN));

	public static final PsiElementPattern.Capture<PerlVariable> VARIABLE_PATTERN = psiElement(PerlVariable.class).withLanguage(PerlLanguage.INSTANCE);
	public static final PsiElementPattern.Capture<PerlGlobVariable> GLOB_PATTERN = psiElement(PerlGlobVariable.class).withLanguage(PerlLanguage.INSTANCE);

	public static final PsiElementPattern.Capture IN_VARIABLE_PATTERN = psiElement().withLanguage(PerlLanguage.INSTANCE).inside(VARIABLE_PATTERN);
	public static final PsiElementPattern.Capture IN_GLOB_PATTERN = psiElement().withLanguage(PerlLanguage.INSTANCE).inside(GLOB_PATTERN);

	public static final PsiElementPattern.Capture IN_MOJOLICIOUS_FILE = psiElement().withLanguage(PerlLanguage.INSTANCE).inFile(psiFile(MojoliciousPerlFileImpl.class));

	public static final PsiElementPattern.Capture<PerlVariableNameElement> VARIABLE_NAME_PATTERN = psiElement(PerlVariableNameElement.class).withLanguage(PerlLanguage.INSTANCE);
	public static final PsiElementPattern.Capture<PerlHeredocTerminatorElementImpl> HEREDOC_TERMINATOR_PATTERN = psiElement(PerlHeredocTerminatorElementImpl.class).withLanguage(PerlLanguage.INSTANCE);
	public static final PsiElementPattern.Capture<PsiElement> UNKNOWN_ANNOTATION_PATTERN = psiElement(PerlElementTypes.ANNOTATION_UNKNOWN_KEY).withLanguage(PerlLanguage.INSTANCE);

	public static final PsiElementPattern.Capture INSIDE_LEXICAL_DECLARATION = psiElement().withLanguage(PerlLanguage.INSTANCE).inside(PsiPerlVariableDeclarationLexical.class);
	public static final PsiElementPattern.Capture INSIDE_GLOBAL_DECLARATION = psiElement().withLanguage(PerlLanguage.INSTANCE).inside(PsiPerlVariableDeclarationGlobal.class);
	public static final PsiElementPattern.Capture INSIDE_USE_VARS = psiElement().withLanguage(PerlLanguage.INSTANCE).inside(IPerlUseVars.class);
	public static final PsiElementPattern.Capture INSIDE_LOCAL_DECLARATION = psiElement().withLanguage(PerlLanguage.INSTANCE).inside(PsiPerlVariableDeclarationLocal.class);


	public static final PsiElementPattern.Capture<PerlVariableNameElement> VARIABLE_COMPLETION_PATTERN =
			VARIABLE_NAME_PATTERN.andOr(IN_VARIABLE_PATTERN, IN_GLOB_PATTERN).andNot(INSIDE_GLOBAL_DECLARATION).andNot(INSIDE_LEXICAL_DECLARATION).andNot(INSIDE_USE_VARS);


}
