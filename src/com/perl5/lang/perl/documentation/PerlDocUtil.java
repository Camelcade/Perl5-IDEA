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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.PerlScopes;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.pod.PodSearchHelper;
import com.perl5.lang.pod.parser.psi.*;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PerlDocUtil
{
	public static String getVariableDoc(PerlVariable variable)
	{
		return null;
	}

	@Nullable
	public static String getPerlFuncDoc(PsiElement element)
	{
		final Project project = element.getProject();
		final String elementText = element.getText();
		List<PodCompositeElement> elements = searchPerlFunc(project, elementText);
		if (elements.isEmpty())
		{
			return getPerlOpDoc(element);
		}

		for (PodCompositeElement podElement : elements)
		{
			return renderElement(podElement);
		}

		return null;
	}

	@Nullable
	public static String getPerlOpDoc(PsiElement element)
	{
		final Project project = element.getProject();
		final String elementText = element.getText();
		List<PodCompositeElement> elements = searchPerlOp(project, elementText);

		for (PodCompositeElement podElement : elements)
		{
			return renderElement(podElement);
		}

		return null;
	}

	@Nullable
	protected static String renderElement(PodCompositeElement element)
	{
		PodTitledSection podSection = null;

		if (element instanceof PodTitledSection)
		{
			podSection = (PodTitledSection) element;
		}

		if (podSection != null)
		{
			boolean hasContent = podSection.hasContent();
			PsiElement run = podSection;
			PsiElement lastSection = podSection;

			// detecting first section
			while (true)
			{
				PsiElement prevSibling = run.getPrevSibling();
				if (prevSibling == null)
				{
					break;
				}
				if (prevSibling instanceof PodSection && ((PodSection) prevSibling).hasContent())
				{
					break;
				}
				if (prevSibling instanceof PodTitledSection)
				{
					podSection = (PodTitledSection) prevSibling;
				}
				run = prevSibling;
			}

			// detecting last section
			while (!hasContent)
			{
				PsiElement nextSibling = lastSection.getNextSibling();

				if (nextSibling == null)
					break;
				hasContent = nextSibling instanceof PodSection && ((PodSection) nextSibling).hasContent();

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

			builder.append(PodRenderUtil.renderPsiRangeAsHTML(podSection, lastSection));
			builder.append(closeTag);
			return builder.toString();
		}
		return null;
	}

	protected static List<PodCompositeElement> searchPerlVar(Project project, String text)
	{
		return Collections.emptyList();
	}

	/**
	 * Port of Pod::Perldoc::search_perlop
	 *
	 * @param project project
	 * @param text    search token
	 * @return list of results
	 */
	protected static List<PodCompositeElement> searchPerlOp(Project project, String text)
	{
		if (StringUtil.isNotEmpty(text))
		{
			if (text.matches("-[rwxoRWXOeszfdlpSbctugkTBMAC]"))
			{
				text = "-X";
			}
			else if ("=>".equals(text))
			{
				text = ",";
			}
			else if ("?".equals(text) || ":".equals(text))
			{
				text = "?:";
			}

			final Pattern pattern = Pattern.compile(Pattern.quote(text) + "(\\b|$)");

			PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, PodSearchHelper.PERL_OP_FILE_NAME, PerlScopes.getProjectAndLibrariesScope(project));
			if (psiFiles.length > 0)
			{
				PsiFile psiFile = psiFiles[0];
				final List<PodCompositeElement> result = new ArrayList<PodCompositeElement>();

				PsiTreeUtil.processElements(psiFile, new PsiElementProcessor()
				{
					@Override
					public boolean execute(@NotNull PsiElement element)
					{
						if (element instanceof PodFormatterX)
						{
							String elementText = ((PodFormatterX) element).getTitleText();

							if (StringUtil.isNotEmpty(elementText) && pattern.matcher(elementText).lookingAt())
							{

								PsiElement container = PsiTreeUtil.getParentOfType(element, PodTitledSection.class);
								if (container != null)
								{
									result.add((PodCompositeElement) container);
									return false;
								}
							}
						}
						return true;
					}
				});
				return result;
			}
		}
		return Collections.emptyList();
	}

	protected static List<PodCompositeElement> searchPerlApi(Project project, String text)
	{
		return Collections.emptyList();
	}

	/**
	 * Port of Pod::Perldoc::search_perlfunc
	 *
	 * @param project project
	 * @param text    search token
	 * @return list of results
	 */
	protected static List<PodCompositeElement> searchPerlFunc(final Project project, String text)
	{
		if (StringUtil.isNotEmpty(text))
		{
			if (text.matches("-[rwxoRWXOeszfdlpSbctugkTBMAC]"))
			{
				text = "-X";
			}

			final Pattern pattern = Pattern.compile(Pattern.quote(text) + "(\\b|$)");

			PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, PodSearchHelper.PERL_FUNC_FILE_NAME, PerlScopes.getProjectAndLibrariesScope(project));
			if (psiFiles.length > 0)
			{
				PsiFile psiFile = psiFiles[0];
				final List<PodCompositeElement> result = new ArrayList<PodCompositeElement>();

				PsiTreeUtil.processElements(psiFile, new PsiElementProcessor()
				{
					@Override
					public boolean execute(@NotNull PsiElement element)
					{
						if (element instanceof PodSectionItem && ((PodSectionItem) element).getListLevel() < 2)
						{
							String title = ((PodTitledSection) element).getTitleText();

							if (StringUtil.isNotEmpty(title) && pattern.matcher(title).lookingAt())
							{
								result.add((PodCompositeElement) element);
								return false;
							}
						}
						return true;
					}
				});
				return result;
			}
		}
		return Collections.emptyList();
	}

	protected static List<PodCompositeElement> searchPerlFaqs(Project project, String text)
	{
		return Collections.emptyList();
	}

}
