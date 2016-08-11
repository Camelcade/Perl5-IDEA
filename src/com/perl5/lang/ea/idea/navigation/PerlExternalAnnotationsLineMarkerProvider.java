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
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.perl5.PerlBundle;
import com.perl5.lang.ea.psi.PerlExternalAnnotationDeclaration;
import com.perl5.lang.ea.psi.PerlExternalAnnotationNamespace;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PerlSubBase;
import com.perl5.lang.perl.util.PerlAnnotationsUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by hurricup on 10.08.2016.
 */
public class PerlExternalAnnotationsLineMarkerProvider extends RelatedItemLineMarkerProvider
{
	@Override
	protected void collectNavigationMarkers(@NotNull PsiElement element, Collection<? super RelatedItemLineMarkerInfo> result)
	{
		if (element instanceof PerlNamespaceDefinition)
		{
			PsiElement nameIdentifier = ((PerlNamespaceDefinition) element).getNameIdentifier();
			if (nameIdentifier == null)
			{
				return;
			}

			String packageName = ((PerlNamespaceDefinition) element).getPackageName();
			if (StringUtil.isEmpty(packageName))
			{
				return;
			}

			if (((PerlNamespaceDefinition) element).getLocalAnnotations() != null)
			{
				return;
			}

			Collection<PerlExternalAnnotationNamespace> namespcaceExternalAnnotations = PerlAnnotationsUtil.getExternalAnnotationsNamespaces(element.getProject(), packageName);
			if (namespcaceExternalAnnotations.isEmpty())
			{
				return;
			}

			result.add(NavigationGutterIconBuilder
					.create(AllIcons.Gutter.ExtAnnotation)
					.setTargets(namespcaceExternalAnnotations)
					.setTooltipText(PerlBundle.message("perl.external.annotations"))
					.setCellRenderer(new ExternalAnnotationCellRenderer())
					.createLineMarkerInfo(nameIdentifier)
			);
		}
		else if (element instanceof PerlSubBase)
		{
			PsiElement nameIdentifier = ((PerlSubBase) element).getNameIdentifier();
			if (nameIdentifier == null)
			{
				return;
			}
			String canonicalName = ((PerlSubBase) element).getCanonicalName();
			if (StringUtil.isEmpty(canonicalName))
			{
				return;
			}

			if (((PerlSubBase) element).getLocalAnnotations() != null)
			{
				return;
			}

			Collection<PerlExternalAnnotationDeclaration> subExternalAnnotations = PerlAnnotationsUtil.getExternalAnnotationsSubDeclarations(element.getProject(), canonicalName);
			if (subExternalAnnotations.isEmpty())
			{
				return;
			}
			result.add(NavigationGutterIconBuilder
					.create(AllIcons.Gutter.ExtAnnotation)
					.setTargets(subExternalAnnotations)
					.setTooltipText(PerlBundle.message("perl.external.annotations"))
					.setCellRenderer(new ExternalAnnotationCellRenderer())
					.createLineMarkerInfo(nameIdentifier)
			);
		}

	}

	private static class ExternalAnnotationCellRenderer extends DefaultPsiElementCellRenderer
	{
		public ExternalAnnotationCellRenderer()
		{
		}

		@Override
		public String getElementText(PsiElement element)
		{
			return PerlAnnotationsUtil.getPsiElementLevelName(element);
		}

		@Override
		public String getContainerText(PsiElement element, String name)
		{
			return null;
		}
	}
}
