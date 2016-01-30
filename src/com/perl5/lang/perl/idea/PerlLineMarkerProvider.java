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

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
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
			List<PerlNamespaceDefinition> parentNamespaces = ((PerlNamespaceDefinition) element).getParentNamespaceDefinitions();
			if (parentNamespaces.size() > 0)
			{
				NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
						.create(AllIcons.Gutter.ImplementingMethod)
						.setTargets(parentNamespaces)
						.setTooltipText("Parent classes");

				result.add(builder.createLineMarkerInfo(element));
			}

			Collection<PerlNamespaceDefinition> childNamespaces = ((PerlNamespaceDefinition) element).getChildNamespaceDefinitions();
			if (childNamespaces.size() > 0)
			{
				NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
						.create(AllIcons.Gutter.ImplementedMethod)
						.setTargets(childNamespaces)
						.setTooltipText("Subclasses");

				result.add(builder.createLineMarkerInfo(element));
			}

		}

	}
}
