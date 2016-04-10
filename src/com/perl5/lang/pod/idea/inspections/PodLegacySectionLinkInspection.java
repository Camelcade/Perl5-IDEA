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

package com.perl5.lang.pod.idea.inspections;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.pod.parser.psi.PodFormatterL;
import com.perl5.lang.pod.parser.psi.PodLinkDescriptor;
import com.perl5.lang.pod.parser.psi.PodVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 10.04.2016.
 */
public class PodLegacySectionLinkInspection extends LocalInspectionTool
{
	@NotNull
	@Override
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PodVisitor()
		{
			@Override
			public void visitPodFormatterL(@NotNull PodFormatterL o)
			{
				PodLinkDescriptor descriptor = o.getLinkDescriptor();

				if (descriptor != null && !descriptor.isUrl() && descriptor.getSection() != null)
				{
					PsiElement contentBlock = o.getContentBlock();
					TextRange sectionRange = descriptor.getSectionTextRangeInLink();
					if (contentBlock != null && sectionRange != null)
					{
						if (isSectionLegacy(contentBlock.getText(), sectionRange))
						{
							holder.registerProblem(o, "Section \"" + descriptor.getSection() + "\" should have a slash before it", ProblemHighlightType.LIKE_DEPRECATED, sectionRange.shiftRight(contentBlock.getStartOffsetInParent()));
						}

					}
				}
				super.visitPodFormatterL(o);
			}

			private boolean isSectionLegacy(String text, TextRange sectionRange)
			{
				if (text == null)
					return false;

				int sectionStartOffset = sectionRange.getStartOffset();
				if (sectionStartOffset == 0)
					return true;

				char prevChar = text.charAt(sectionStartOffset - 1);
				if (sectionStartOffset == 1)
					return prevChar != '/';

				char prevPrevChar = text.charAt(sectionStartOffset - 2);

				return !(prevChar == '/' || prevChar == '"' && prevPrevChar == '/');
			}

		};
	}
}
