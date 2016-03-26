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

package com.perl5.lang.perl.documentation;

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.pod.PodScopes;
import com.perl5.lang.pod.parser.psi.PodCompositeElement;
import com.perl5.lang.pod.parser.psi.PodSectionItem;
import com.perl5.lang.pod.parser.psi.PodStubBasedSection;
import com.perl5.lang.pod.parser.psi.PodTitledSection;
import com.perl5.lang.pod.parser.psi.stubs.PodStubIndex;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;

import java.util.Collection;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PerlDocUtil
{
	public static String getVariableDoc(PerlVariable variable)
	{
		return null;
	}

	public static String getReservedDoc(PsiElement element)
	{
		Collection<PodStubBasedSection> elements = StubIndex.getElements(
				PodStubIndex.KEY,
				element.getText(),
				element.getProject(),
				PodScopes.getPerlFuncScope(element.getProject()),
				PodStubBasedSection.class
		);
		for (PsiElement podElement : elements)
		{
			PodTitledSection podSection = PsiTreeUtil.getParentOfType(podElement, PodTitledSection.class);
			if (podSection != null)
			{
				PsiElement lastSection = podSection;
				while (true)
				{
					PsiElement nextSibling = lastSection.getNextSibling();

					if (nextSibling == null)
						break;
					if (nextSibling instanceof PodCompositeElement && ((PodCompositeElement) nextSibling).isIndexed())
						break;

					lastSection = nextSibling;
				}

				StringBuilder builder = new StringBuilder();

				String closeTag = "";

				if (podSection instanceof PodSectionItem)
				{
					if (((PodSectionItem) podSection).isBulleted())
					{
						builder.append("<ul style=\"fon-size:200%;\">");
						closeTag = "</ul>";
					}
					else
					{
						builder.append("<dl>");
						closeTag = "</dl>";
					}
				}

				builder.append(PodRenderUtil.renderPsiRange(podSection, lastSection));
				builder.append(closeTag);
				return builder.toString();
			}
		}

		return null;
	}

}
