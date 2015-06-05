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

import com.intellij.codeInsight.highlighting.HighlightUsagesDescriptionLocation;
import com.intellij.psi.ElementDescriptionLocation;
import com.intellij.psi.ElementDescriptionProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.usageView.UsageViewLongNameLocation;
import com.intellij.usageView.UsageViewNodeTextLocation;
import com.intellij.usageView.UsageViewShortNameLocation;
import com.intellij.usageView.UsageViewTypeLocation;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlFileElementImpl;
import com.perl5.lang.perl.psi.properties.PerlNamedElement;
import com.perl5.lang.perl.psi.properties.PerlPackageMember;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlElementDescriptionProvider implements ElementDescriptionProvider
{
	@Nullable
	@Override
	public String getElementDescription( @NotNull PsiElement element, @NotNull ElementDescriptionLocation location)
	{
		PsiElement parent = element.getParent();

		if( location == HighlightUsagesDescriptionLocation.INSTANCE 	// ???
				|| location == UsageViewNodeTextLocation.INSTANCE 		// child element of find usages
				)
			return getElementDescription(element, UsageViewShortNameLocation.INSTANCE);
		else if( location == UsageViewTypeLocation.INSTANCE )
		{
			if( element instanceof PerlSubDeclaration || parent instanceof PerlSubDeclaration)
				return "Sub declaration";
			else if( element instanceof PerlSubDefinition || parent instanceof PerlSubDefinition)
				return "Sub definition";
			else if( element instanceof PerlNamespaceDefinition || parent instanceof PerlNamespaceDefinition)
				return "Namespace definition";
			else if( element instanceof PerlFileElementImpl)
				return "Perl file";
			else return "Undefined type for class: " + element.getClass();
		}
		// file renaming
		else if( location == UsageViewShortNameLocation.INSTANCE)
		{
			if( element instanceof PerlPackageMember)
				return ((PerlPackageMember) element).getCanonicalName();
			else if( element instanceof PsiNamedElement)
				return ((PsiNamedElement) element).getName();
		}

		return "Unhandled description of "+ element.getClass() + " in " + location.getClass();
	}
}
