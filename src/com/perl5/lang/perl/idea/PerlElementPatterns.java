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

import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlHeredocTerminatorImpl;
import com.perl5.lang.perl.psi.impl.PerlStringContentImpl;

/**
 * Created by hurricup on 31.05.2015.
 */
public interface PerlElementPatterns
{
	public static final PsiElementPattern.Capture<PerlStringContentImpl> STRING_CONENT_PATTERN = PlatformPatterns.psiElement(PerlStringContentImpl.class).withLanguage(PerlLanguage.INSTANCE);
	public static final PsiElementPattern.Capture<PerlNamespace> NAMESPACE_NAME_PATTERN = PlatformPatterns.psiElement(PerlNamespace.class).withLanguage(PerlLanguage.INSTANCE);
	public static final PsiElementPattern.Capture<PerlUseStatement> USE_STATEMENT_PATTERN = PlatformPatterns.psiElement(PerlUseStatement.class).withLanguage(PerlLanguage.INSTANCE);
	public static final PsiElementPattern.Capture<PerlRequireTerm> REQUIRE_TERM_PATTERN = PlatformPatterns.psiElement(PerlRequireTerm.class).withLanguage(PerlLanguage.INSTANCE);
	public static final PsiElementPattern.Capture<PerlFunction> FUNCTION_PATTERN = PlatformPatterns.psiElement(PerlFunction.class).withLanguage(PerlLanguage.INSTANCE);
	public static final PsiElementPattern.Capture<PerlVariable> VARIABLE_PATTERN = PlatformPatterns.psiElement(PerlVariable.class).withLanguage(PerlLanguage.INSTANCE);
	public static final PsiElementPattern.Capture<PerlVariableName> VARIABLE_NAME_PATTERN = PlatformPatterns.psiElement(PerlVariableName.class).withLanguage(PerlLanguage.INSTANCE);
	public static final PsiElementPattern.Capture<PerlHeredocTerminatorImpl> HEREDOC_TERMINATOR_PATTERN = PlatformPatterns.psiElement(PerlHeredocTerminatorImpl.class).withLanguage(PerlLanguage.INSTANCE);
}
