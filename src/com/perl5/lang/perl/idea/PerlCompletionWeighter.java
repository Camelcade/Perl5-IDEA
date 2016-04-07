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

import com.intellij.codeInsight.completion.CompletionLocation;
import com.intellij.codeInsight.completion.CompletionWeigher;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.util.Key;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.PerlLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 01.06.2015.
 */
public class PerlCompletionWeighter extends CompletionWeigher
{
	public static final Key<Integer> WEIGHT = new Key<Integer>("WEIGHT");

	@Override
	public Comparable weigh(@NotNull LookupElement element, @NotNull CompletionLocation location)
	{
		if (PsiUtilCore.findLanguageFromElement(location.getCompletionParameters().getPosition()).isKindOf(PerlLanguage.INSTANCE))
		{
			Integer weight = element.getUserData(WEIGHT);
			return weight == null ? 0 : weight;
		}

		return 0;
	}
}
