/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.navigation;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PerlSubBase;
import com.perl5.lang.perl.psi.PerlSubDefinitionBase;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 26.04.2015.
 */
public class PerlLineMarkerProvider extends RelatedItemLineMarkerProvider implements PerlElementTypes
{
	@Override
	protected void collectNavigationMarkers(@NotNull PsiElement element, Collection<? super RelatedItemLineMarkerInfo> result)
	{
		if (element instanceof PerlNamespaceDefinition)
		{
			PsiElement nameIdentifier = ((PerlNamespaceDefinition) element).getNameIdentifier();
			if (nameIdentifier == null)
			{
				nameIdentifier = element;
			}

			List<PerlNamespaceDefinition> parentNamespaces = ((PerlNamespaceDefinition) element).getParentNamespaceDefinitions();
			if (!parentNamespaces.isEmpty())
			{
				NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
						.create(AllIcons.Gutter.ImplementingMethod)
						.setTargets(parentNamespaces)
						.setTooltipText("Parent classes");

				result.add(builder.createLineMarkerInfo(nameIdentifier));
			}

			Collection<PerlNamespaceDefinition> childNamespaces = ((PerlNamespaceDefinition) element).getChildNamespaceDefinitions();
			if (!childNamespaces.isEmpty())
			{
				NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
						.create(AllIcons.Gutter.ImplementedMethod)
						.setTargets(childNamespaces)
						.setTooltipText("Subclasses");

				result.add(builder.createLineMarkerInfo(nameIdentifier));
			}

		}
		else if (element instanceof PerlSubDefinitionBase && ((PerlSubDefinitionBase) element).isMethod())
		{
			PerlNamespaceDefinition containingNamespace = PsiTreeUtil.getParentOfType(element, PerlNamespaceDefinition.class);
			if (containingNamespace != null)
			{
				final String packageName = ((PerlSubDefinitionBase) element).getPackageName();
				final String subName = ((PerlSubDefinitionBase) element).getSubName();
				PsiElement nameIdentifier = ((PerlSubDefinitionBase) element).getNameIdentifier();
				if (nameIdentifier == null)
				{
					nameIdentifier = element;
				}

				if (StringUtil.isNotEmpty(packageName) && StringUtil.isNotEmpty(subName))
				{
					PerlSubBase parentSub = PerlSubUtil.getDirectSuperMethod((PerlSubBase) element);

					if (parentSub != null)
					{
						NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
								.create(AllIcons.Gutter.OverridingMethod)
								.setTarget(parentSub)
								.setTooltipText("Overriding method");

						result.add(builder.createLineMarkerInfo(nameIdentifier));
					}

					final List<PerlSubBase> overridingSubs = PerlSubUtil.getDirectOverridingSubs((PerlSubBase) element, containingNamespace);

					if (!overridingSubs.isEmpty())
					{
						NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
								.create(AllIcons.Gutter.OverridenMethod)
								.setTargets(overridingSubs)
								.setTooltipText("Overriden methods");

						result.add(builder.createLineMarkerInfo(nameIdentifier));
					}
				}
			}
		}
	}
}
