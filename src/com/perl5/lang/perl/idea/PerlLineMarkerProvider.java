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
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.util.PerlPackageUtil;
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
		// @todo this should not be shown on static method calls (we should jump to definition of method)
		// @todo thi should not be shown if there is only one result found?
		if(element instanceof LeafPsiElement)
		{
			IElementType elementType = ((LeafPsiElement) element).getElementType();

/*
			if( elementType == PERL_PACKAGE	)
			{
				Project project = element.getProject();

				final List<PsiElement> scalars = PerlPackageUtil.findPackageDefinitions(project, element.getText());
				if (scalars.size() > 0)
				{
					NavigationGutterIconBuilder<PsiElement> builder =
							NavigationGutterIconBuilder.create(
									PerlIcons.PM_FILE).
									setTargets(scalars).
									setTooltipText("To package definition");
					result.add(builder.createLineMarkerInfo(element));
				}
			}
*/
		}

	}
}
