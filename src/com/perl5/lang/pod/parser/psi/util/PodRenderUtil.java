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

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.xml.util.XmlStringUtil;
import com.perl5.lang.pod.parser.psi.PodRenderableElement;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodRenderUtil
{
	public static String renderPsiRangeAsHTML(@Nullable PsiElement firstElement, @Nullable PsiElement lastElement)
	{
		return renderPsiRangeAsHTML(firstElement, lastElement, new PodRenderingContext());
	}

	public static String renderPsiRangeAsHTML(@Nullable PsiElement firstElement, @Nullable PsiElement lastElement, PodRenderingContext context)
	{
		if (firstElement == null)
			return "";

		StringBuilder result = new StringBuilder();
		renderPsiRangeAsHTML(firstElement, lastElement, result, context);
		return result.toString();
	}

	public static void renderPsiRangeAsHTML(@Nullable PsiElement firstElement, @Nullable PsiElement lastElement, @NotNull StringBuilder builder, @NotNull PodRenderingContext context)
	{
		if (firstElement == null)
			return;

		PsiElement run = firstElement;
		while (run != null)
		{
			if (run instanceof PodRenderableElement)
			{
				((PodRenderableElement) run).renderElementAsHTML(builder, context);
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

	public static String renderPsiRangeAsText(@Nullable PsiElement firstElement, @Nullable PsiElement lastElement)
	{
		return renderPsiRangeAsText(firstElement, lastElement, new PodRenderingContext());
	}

	public static String renderPsiRangeAsText(@Nullable PsiElement firstElement, @Nullable PsiElement lastElement, PodRenderingContext context)
	{
		if (firstElement == null)
			return "";

		StringBuilder result = new StringBuilder();
		renderPsiRangeAsText(firstElement, lastElement, result, context);
		return result.toString();
	}

	public static void renderPsiRangeAsText(@Nullable PsiElement firstElement, @Nullable PsiElement lastElement, @NotNull StringBuilder builder, @NotNull PodRenderingContext context)
	{
		if (firstElement == null)
			return;

		PsiElement run = firstElement;
		while (run != null)
		{
			if (run instanceof PodRenderableElement)
			{
				((PodRenderableElement) run).renderElementAsText(builder, context);
			}
			else
			{
				String text = run.getText();
				if (!StringUtil.equals(text, "\n"))
				{
					builder.append(run.getText());
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
