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

import com.intellij.codeInsight.documentation.DocumentationManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.xml.util.XmlStringUtil;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.parser.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URLEncoder;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodRenderUtil implements PodElementTypes
{
	public static String renderPsiElementAsHTML(@Nullable PsiElement firstElement)
	{
		return renderPsiRangeAsHTML(firstElement, firstElement);
	}

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
			else if (run.getNode().getElementType() != POD_OUTER)
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

	public static String renderPsiElementAsText(@Nullable PsiElement element)
	{
		return renderPsiRangeAsText(element, element);
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
		return result.toString().trim();
	}

	public static void renderPsiRangeAsText(@Nullable PsiElement firstElement, @Nullable PsiElement lastElement, @NotNull StringBuilder builder, @NotNull PodRenderingContext context)
	{
		if (firstElement == null)
			return;

		PsiElement run = firstElement;
		while (run != null)
		{
			if (run.getNode().getElementType() == POD_OUTER)
			{
				continue;
			}
			else if (run instanceof PodRenderableElement)
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
				else
				{
					builder.append(" ");
				}
			}

			if (lastElement != null && lastElement.equals(run))
			{
				break;
			}
			run = run.getNextSibling();
		}
	}

	public static String getHTMLLink(@NotNull PodLinkDescriptor descriptor)
	{
		return getHTMLLink(descriptor.getCanonicalUrl(), !descriptor.isUrl(), descriptor.getTitle());
	}

	public static String getHTMLPsiLink(@NotNull PodLinkTarget target)
	{
		if (StringUtil.isNotEmpty(target.getPodLink()))
		{
			return getHTMLPsiLink(target.getPodLink(), target.getPodLinkText());
		}
		return "";
	}

	public static String getHTMLPsiLink(@NotNull String link, @Nullable String text)
	{
		return getHTMLLink(link, true, text);
	}

	public static String getHTMLPsiLink(@NotNull String link)
	{
		return getHTMLLink(link, true, null);
	}

	public static String getHTMLLink(@NotNull String link, boolean isPsi, @Nullable String text)
	{
		StringBuilder builder = new StringBuilder();

		builder.append("<a href=\"");

		if (isPsi)
		{
			builder.append(DocumentationManager.PSI_ELEMENT_PROTOCOL);
			builder.append(PodRenderUtil.encodeLink(link));
		}
		else
		{
			builder.append(link);
		}

		builder.append("\">");
		builder.append(text == null ? link : text);
		builder.append("</a>");

		return builder.toString();
	}

	public static String encodeLink(String link)
	{
		try
		{
			return URLEncoder.encode(link, "UTF-8");
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static String getPodLinkForElement(PodTitledSection element)
	{
		String elementLink = element.getTitleText();
		if (StringUtil.isEmpty(elementLink))
		{
			return null;
		}

		PsiFile psiFile = element.getContainingFile();

		if (psiFile instanceof PodLinkTarget)
		{
			String fileLink = ((PodLinkTarget) psiFile).getPodLink();
			if (StringUtil.isNotEmpty(fileLink))
			{
				return fileLink + "/" + elementLink;
			}
		}
		return "/" + elementLink;
	}

}
