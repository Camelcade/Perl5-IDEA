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

package com.perl5.lang.htmlmason.idea.navigation;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonMethodDefinition;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 12.03.2016.
 */
public class HTMLMasonLineMarkerProvider extends RelatedItemLineMarkerProvider implements HTMLMasonElementTypes
{
	@Override
	protected void collectNavigationMarkers(@NotNull PsiElement element, Collection<? super RelatedItemLineMarkerInfo> result)
	{
		if (element instanceof HTMLMasonFileImpl)
		{
			HTMLMasonFileImpl parentComponent = ((HTMLMasonFileImpl) element).getParentComponent();
			if (parentComponent != null)
			{
				NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
						.create(AllIcons.Gutter.ImplementingMethod)
						.setTargets(parentComponent)
						.setTooltipText("Parent component");

				result.add(builder.createLineMarkerInfo(element));
			}

			List<HTMLMasonFileImpl> childComponents = ((HTMLMasonFileImpl) element).getChildComponents();
			if (childComponents.size() > 0)
			{
				NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
						.create(AllIcons.Gutter.ImplementedMethod)
						.setTargets(childComponents)
						.setTooltipText("Child components");

				result.add(builder.createLineMarkerInfo(element));
			}
		}
		else if (element instanceof HTMLMasonMethodDefinition)
		{
			String methodName = ((HTMLMasonMethodDefinition) element).getName();
			PsiFile component = element.getContainingFile();
			if (StringUtil.isNotEmpty(methodName) && component instanceof HTMLMasonFileImpl)
			{
				// method in parent components
				HTMLMasonMethodDefinition methodDefinition = ((HTMLMasonFileImpl) component).findMethodDefinitionByNameInParents(methodName);
				if (methodDefinition != null)
				{
					NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
							.create(AllIcons.Gutter.OverridingMethod)
							.setTargets(methodDefinition)
							.setTooltipText("Overriding method");

					result.add(builder.createLineMarkerInfo(element));
				}

				// method in subcomponents
				List<HTMLMasonMethodDefinition> methodDefinitions = ((HTMLMasonFileImpl) component).findMethodDefinitionByNameInChildComponents(methodName);
				if (!methodDefinitions.isEmpty())
				{
					NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
							.create(AllIcons.Gutter.OverridenMethod)
							.setTargets(methodDefinitions)
							.setTooltipText("Overriden methods");

					result.add(builder.createLineMarkerInfo(element));
				}
			}
		}
	}
}
