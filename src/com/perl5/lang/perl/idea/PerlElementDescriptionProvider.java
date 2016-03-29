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
import com.intellij.psi.*;
import com.intellij.usageView.UsageViewNodeTextLocation;
import com.intellij.usageView.UsageViewShortNameLocation;
import com.intellij.usageView.UsageViewTypeLocation;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.properties.PerlNamedElement;
import com.perl5.lang.perl.psi.properties.PerlPackageMember;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlElementDescriptionProvider implements ElementDescriptionProvider
{
	@Nullable
	@Override
	public String getElementDescription(@NotNull PsiElement element, @NotNull ElementDescriptionLocation location)
	{
//		PsiElement parent = element.getParent();

//		System.out.println(element + " for " + location);
		if (element.getLanguage() == PerlLanguage.INSTANCE)
		{
			if (location == HighlightUsagesDescriptionLocation.INSTANCE    // ???
					|| location == UsageViewNodeTextLocation.INSTANCE        // child element of find usages
					)
				return getElementDescription(element, UsageViewShortNameLocation.INSTANCE);
			else if (location == UsageViewTypeLocation.INSTANCE)
			{
				if (element instanceof PerlSubDeclaration)
					return "Sub declaration";
				else if (element instanceof PerlHeredocOpener)
					return "Heredoc marker";
				else if (element instanceof PerlSubDefinition)
					return "Sub definition";
				else if (element instanceof PerlMethodDefinition)
					return "Method definition";
				else if (element instanceof PerlFuncDefinition)
					return "Function definition";
				else if (element instanceof PerlConstant)
					return "Constant";
				else if (element instanceof PerlNamespaceDefinition)
					return "Namespace definition";
				else if (element instanceof PerlFileImpl)
					return "perl file";
				else if (element instanceof PsiDirectoryContainer)
					return "Directory";
				else if (element instanceof PerlVariableNameElement)
					return getElementDescription(element.getParent(), location);
				else if (element instanceof PerlGlobVariable)
					return "Typeglob";
				else if (element instanceof PerlVariable || element instanceof PerlVariableDeclarationWrapper)
				{
					if (element instanceof PerlVariableDeclarationWrapper)
						element = ((PerlVariableDeclarationWrapper) element).getVariable();

					PerlVariableType actualType = ((PerlVariable) element).getActualType();
					if (actualType == PerlVariableType.ARRAY)
						return "array variable";
					else if (actualType == PerlVariableType.HASH)
						return "hash variable";
					else if (actualType == PerlVariableType.SCALAR)
						return "scalar variable";
				}
				return null;
			}
			// file renaming
			else if (location == UsageViewShortNameLocation.INSTANCE)
			{
				if (element instanceof PerlNamedElement)
					return ((PerlNamedElement) element).getPresentableName();
				else if (element instanceof PerlPackageMember)
					return ((PerlPackageMember) element).getCanonicalName();
				else if (element instanceof PsiNamedElement)
					return ((PsiNamedElement) element).getName();
			}
		}
//		return "Unhandled description of "+ element.getClass() + " in " + location.getClass();
		return null;
	}
}
