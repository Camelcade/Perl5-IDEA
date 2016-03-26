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

package com.perl5.lang.pod.parser.psi.util;

import com.intellij.psi.PsiElement;
import com.intellij.xml.util.XmlStringUtil;
import com.perl5.lang.pod.parser.psi.PodHTMLProvider;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodRenderUtil
{
	public static String renderPsiElement(@Nullable PsiElement element, PodRenderingContext context)
	{
		return renderPsiRange(element, element, context);
	}

	public static String renderPsiRange(@Nullable PsiElement firstElement, @Nullable PsiElement lastElement, PodRenderingContext context)
	{
		if (firstElement == null)
			return "";

		StringBuilder result = new StringBuilder();
		renderPsiRange(firstElement, lastElement, result, context);
		return result.toString();
	}

	public static void renderPsiRange(@Nullable PsiElement firstElement, @Nullable PsiElement lastElement, @NotNull StringBuilder builder, @NotNull PodRenderingContext context)
	{
		if (firstElement == null)
			return;

		PsiElement run = firstElement;
		while (run != null)
		{
			if (run instanceof PodHTMLProvider)
			{
				((PodHTMLProvider) run).renderElement(builder, context);
			}
			else
			{
				if (context.isSafe())
				{
					builder.append(run.getText());
				}
				else
				{
					builder.append(XmlStringUtil.escapeString(run.getText()));
				}
			}

			if (lastElement != null && lastElement.equals(run))
			{
				break;
			}
			run = run.getNextSibling();
		}
	}
}
