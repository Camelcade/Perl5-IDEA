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

import com.intellij.psi.ElementDescriptionLocation;
import com.intellij.psi.ElementDescriptionProvider;
import com.intellij.psi.PsiElement;
import com.intellij.usageView.UsageViewLongNameLocation;
import com.intellij.usageView.UsageViewShortNameLocation;
import com.perl5.lang.perl.psi.PerlHeredocOpener;
import com.perl5.lang.perl.psi.PerlNamedElement;
import com.perl5.lang.perl.psi.PerlFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlElementDescriptionProvider implements ElementDescriptionProvider
{
	@Nullable
	@Override
	public String getElementDescription( @NotNull PsiElement element, @NotNull ElementDescriptionLocation location)
	{
		if (location == UsageViewShortNameLocation.INSTANCE || location == UsageViewLongNameLocation.INSTANCE)
			if( element instanceof PerlNamedElement )
				return ((PerlNamedElement) element).getName();
			else
				return null;
		else if(element instanceof PerlHeredocOpener)
			return "Heredoc marker";
		else if(element instanceof PerlFunction)
			return "User function";
		else
			return null;
	}
}
