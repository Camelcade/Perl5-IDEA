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

package com.perl5.lang.perl.idea.refactoring;

import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.SuggestedNameInfo;
import com.intellij.refactoring.rename.NameSuggestionProvider;
import com.perl5.lang.perl.idea.intellilang.PerlLanguageInjector;
import com.perl5.lang.perl.psi.PerlHeredocOpener;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Created by hurricup on 12.06.2015.
 */
public class PerlNameSuggestionProvider implements NameSuggestionProvider
{
	@Nullable
	@Override
	public SuggestedNameInfo getSuggestedNames(PsiElement element, PsiElement nameSuggestionContext, Set<String> result)
	{
		if (element instanceof PerlHeredocOpener)
			result.addAll(PerlLanguageInjector.LANGUAGE_MAP.keySet());

		// todo play with this
		return SuggestedNameInfo.NULL_INFO;
	}
}
