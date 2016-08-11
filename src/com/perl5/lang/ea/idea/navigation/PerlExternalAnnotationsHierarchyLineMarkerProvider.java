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

package com.perl5.lang.ea.idea.navigation;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.perl5.lang.ea.psi.PerlExternalAnnotationDeclaration;
import com.perl5.lang.ea.psi.PerlExternalAnnotationNamespace;
import com.perl5.lang.ea.psi.PerlExternalAnnotationsIdentifierOwner;
import com.perl5.lang.perl.util.PerlAnnotationsUtil;
import com.perl5.lang.perl.util.PerlExternalAnnotationsLevels;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by hurricup on 10.08.2016.
 */
public class PerlExternalAnnotationsHierarchyLineMarkerProvider extends RelatedItemLineMarkerProvider implements PerlExternalAnnotationsLevels
{
	private static void calculateGutterIcons(
			@NotNull PerlExternalAnnotationsIdentifierOwner element,
			@NotNull Collection<? extends PsiElement> variants,
			Collection<? super RelatedItemLineMarkerInfo> result
	)
	{
		PsiElement nameIdentifier = element.getNameIdentifier();
		if (nameIdentifier == null)
		{
			return;
		}

		int psiElementLevel = PerlAnnotationsUtil.getPsiElementLevel(element);
		if (psiElementLevel == UNKNOWN_LEVEL)
		{
			return;
		}

		int superElementLevel = UNKNOWN_LEVEL;
		int subElementLevel = -1;
		PsiElement superTarget = null;
		PsiElement subTarget = null;

		for (PsiElement currentElement : variants)
		{
			int currentElementLevel = PerlAnnotationsUtil.getPsiElementLevel(currentElement);
			if (currentElementLevel < psiElementLevel && currentElementLevel > subElementLevel)
			{
				subTarget = currentElement;
				subElementLevel = currentElementLevel;
			}
			if (currentElementLevel > psiElementLevel && currentElementLevel < superElementLevel)
			{
				superTarget = currentElement;
				superElementLevel = currentElementLevel;
			}
		}

		if (superTarget != null)
		{
			result.add(NavigationGutterIconBuilder
					.create(AllIcons.Gutter.ImplementingMethod)
					.setTargets(superTarget)
					.setTooltipText(PerlAnnotationsUtil.getAnnotationLevelName(superElementLevel))
					.createLineMarkerInfo(nameIdentifier));
		}
		if (subTarget != null)
		{
			result.add(NavigationGutterIconBuilder
					.create(AllIcons.Gutter.ImplementedMethod)
					.setTargets(subTarget)
					.setTooltipText(PerlAnnotationsUtil.getAnnotationLevelName(subElementLevel))
					.createLineMarkerInfo(nameIdentifier));
		}
	}

	@Override
	protected void collectNavigationMarkers(@NotNull PsiElement element, Collection<? super RelatedItemLineMarkerInfo> result)
	{
		if (element instanceof PerlExternalAnnotationNamespace)
		{
			String packageName = ((PerlExternalAnnotationNamespace) element).getPackageName();
			if (StringUtil.isEmpty(packageName))
			{
				return;
			}

			calculateGutterIcons(
					(PerlExternalAnnotationNamespace) element,
					PerlAnnotationsUtil.getExternalAnnotationsNamespaces(element.getProject(), packageName),
					result
			);

		}
		else if (element instanceof PerlExternalAnnotationDeclaration)
		{

			String canonicalName = ((PerlExternalAnnotationDeclaration) element).getCanonicalName();
			if (StringUtil.isEmpty(canonicalName))
			{
				return;
			}

			calculateGutterIcons(
					(PerlExternalAnnotationDeclaration) element,
					PerlAnnotationsUtil.getExternalAnnotationsSubDeclarations(element.getProject(), canonicalName),
					result
			);
		}
		super.collectNavigationMarkers(element, result);
	}

}
