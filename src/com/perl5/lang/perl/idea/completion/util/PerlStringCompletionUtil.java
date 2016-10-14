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

package com.perl5.lang.perl.idea.completion.util;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Processor;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageOptionsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageParentsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessor;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.idea.intellilang.PerlLanguageInjector;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by hurricup on 24.01.2016.
 */
public class PerlStringCompletionUtil implements PerlElementPatterns
{
	public static final Set<String> HASH_INDEXES_CACHE = new THashSet<String>();
	public static final Set<String> HEREDOC_OPENERS_CACHE = new THashSet<String>();
	public static final String[] REF_TYPES = new String[]{
			"SCALAR",
			"ARRAY",
			"HASH",
			"CODE",
			"REF",
			"GLOB",
			"LVALUE",
			"FORMAT",
			"IO",
			"VSTRING",
			"Regexp"
	};

	protected static void addElement(String text, CompletionResultSet result)
	{
		result.addElement(LookupElementBuilder.create(text));
	}

	public static void fillWithHashIndexes(final @NotNull PsiElement element, @NotNull final CompletionResultSet result)
	{
		for (String text : HASH_INDEXES_CACHE)
		{
			addElement(text, result);
		}

		PsiFile file = element.getContainingFile();

		file.accept(
				new PerlRecursiveVisitor()
				{
					@Override
					public void visitStringContentElement(@NotNull PerlStringContentElementImpl o)
					{
						if (o == element)
						{
							super.visitStringContentElement(o);
						}
						else if (SIMPLE_HASH_INDEX.accepts(o))
						{
							processStringElement(o);
						}
					}

					@Override
					public void visitCommaSequenceExpr(@NotNull PsiPerlCommaSequenceExpr o)
					{
						if (o.getParent() instanceof PsiPerlAnonHash)
						{
							PsiElement sequenceElement = o.getFirstChild();
							boolean isKey = true;

							while (sequenceElement != null)
							{
								IElementType elementType = sequenceElement.getNode().getElementType();
								if (isKey && sequenceElement instanceof PerlString)
								{
									for (PerlStringContentElement stringElement : PerlPsiUtil.collectStringElements(sequenceElement))
									{
										processStringElement(stringElement);
									}
								}
								else if (elementType == COMMA || elementType == FAT_COMMA)
								{
									isKey = !isKey;
								}

								sequenceElement = PerlPsiUtil.getNextSignificantSibling(sequenceElement);
							}
						}
						super.visitCommaSequenceExpr(o);
					}

					protected void processStringElement(PerlStringContentElement stringContentElement)
					{
						String text = stringContentElement.getText();
						if (StringUtil.isNotEmpty(text) && !HASH_INDEXES_CACHE.contains(text) && PerlLexer.IDENTIFIER_PATTERN.matcher(text).matches())
						{
							HASH_INDEXES_CACHE.add(text);
							addElement(text, result);
						}
					}
				});
	}

	public static void fillWithExportableEntities(@NotNull PsiElement element, @NotNull final CompletionResultSet result)
	{
		final String contextPackageName = PerlPackageUtil.getContextPackageName(element);
		element.getContainingFile().accept(
				new PerlRecursiveVisitor()
				{
					@Override
					public void visitSubDeclaration(@NotNull PsiPerlSubDeclaration o)
					{
						if (contextPackageName.equals(o.getPackageName()))
						{
							result.addElement(LookupElementBuilder.create(o.getSubName()));
						}
						super.visitSubDeclaration(o);
					}

					@Override
					public void visitSubDefinitionBase(@NotNull PerlSubDefinitionBase o)
					{
						if (contextPackageName.equals(o.getPackageName()))
						{
							result.addElement(LookupElementBuilder.create(o.getSubName()));
						}
						super.visitSubDefinitionBase(o);
					}

				}
		);
	}

	public static void fillWithUseParameters(final @NotNull PsiElement stringContentElement, @NotNull final CompletionResultSet resultSet)
	{
		final Project project = stringContentElement.getProject();

		@SuppressWarnings("unchecked")
		PsiPerlUseStatement useStatement = PsiTreeUtil.getParentOfType(stringContentElement, PsiPerlUseStatement.class, true, PsiPerlStatement.class);

		if (useStatement != null)
		{
			PerlPackageProcessor packageProcessor = useStatement.getPackageProcessor();
			if (packageProcessor != null)
			{
				// fixme we should allow lookup elements customization by package processor
				if (packageProcessor instanceof PerlPackageOptionsProvider)
				{
					Map<String, String> options = ((PerlPackageOptionsProvider) packageProcessor).getOptions();

					for (Map.Entry<String, String> option : options.entrySet())
					{
						resultSet.addElement(LookupElementBuilder
								.create(option.getKey())
								.withTypeText(option.getValue(), true)
								.withIcon(PerlIcons.PERL_OPTION)
						);
					}

					options = ((PerlPackageOptionsProvider) packageProcessor).getOptionsBundles();

					for (Map.Entry<String, String> option : options.entrySet())
					{
						resultSet.addElement(LookupElementBuilder
								.create(option.getKey())
								.withTypeText(option.getValue(), true)
								.withIcon(PerlIcons.PERL_OPTIONS)
						);
					}
				}

				if (packageProcessor instanceof PerlPackageParentsProvider && ((PerlPackageParentsProvider) packageProcessor).hasPackageFilesOptions())
				{
					PerlPackageUtil.processPackageFilesForPsiElement(stringContentElement, new Processor<String>()
					{
						@Override
						public boolean process(String s)
						{
							resultSet.addElement(PerlPackageCompletionUtil.getPackageLookupElement(project, s));
							return true;
						}
					});
				}

				Set<String> export = new HashSet<String>();
				Set<String> exportOk = new HashSet<String>();
				packageProcessor.addExports(useStatement, export, exportOk);
				exportOk.removeAll(export);

				for (String subName : export)
				{
					resultSet.addElement(LookupElementBuilder
							.create(subName)
							.withIcon(PerlIcons.SUB_GUTTER_ICON)
							.withTypeText("default", true)
					);
				}
				for (String subName : exportOk)
				{
					resultSet.addElement(LookupElementBuilder
							.create(subName)
							.withIcon(PerlIcons.SUB_GUTTER_ICON)
							.withTypeText("optional", true)
					);
				}
			}
		}

	}

	public static void fillWithRefTypes(@NotNull final CompletionResultSet resultSet)
	{
		for (String refType : REF_TYPES)
		{
			resultSet.addElement(LookupElementBuilder.create(refType));
		}
	}

	public static void fillWithInjectableMarkers(@NotNull PsiElement element, @NotNull final CompletionResultSet resultSet)
	{
		// injectable markers
		for (Map.Entry<String, Language> entry : PerlLanguageInjector.LANGUAGE_MAP.entrySet())
		{
			String abbreviation = entry.getKey();
			Language language = entry.getValue();

			LookupElementBuilder newItem = LookupElementBuilder
					.create(abbreviation)
					.withTypeText("inject with " + language.getDisplayName(), true);

			if (language.getAssociatedFileType() != null)
			{
				newItem = newItem.withIcon(language.getAssociatedFileType().getIcon());
			}

			resultSet.addElement(newItem);
		}

	}

	public static void fillWithHeredocOpeners(@NotNull PsiElement element, @NotNull final CompletionResultSet resultSet)
	{
		// cached values
		for (String marker : HEREDOC_OPENERS_CACHE)
		{
			resultSet.addElement(LookupElementBuilder.create(marker));
		}

		// collect new values
		PsiFile file = element.getContainingFile();
		if (file != null)
		{
			file.accept(new PerlRecursiveVisitor()
			{
				@Override
				public void visitHeredocOpener(@NotNull PsiPerlHeredocOpener o)
				{
					String openerName = o.getName();
					if (StringUtil.isNotEmpty(openerName) && !HEREDOC_OPENERS_CACHE.contains(openerName) && !PerlLanguageInjector.LANGUAGE_MAP.containsKey(openerName))
					{
						HEREDOC_OPENERS_CACHE.add(openerName);
						resultSet.addElement(LookupElementBuilder.create(openerName));
					}
					super.visitHeredocOpener(o);
				}
			});
		}
	}

}
