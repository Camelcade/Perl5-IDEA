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

package com.perl5.lang.perl.idea.completion.util;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.Processor;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.idea.PerlCompletionWeighter;
import com.perl5.lang.perl.internals.PerlFeaturesTable;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 25.07.2015.
 */
public class PerlPackageCompletionUtil
{
	public static final InsertHandler<LookupElement> COMPLETION_REOPENER = new CompletionOpener();

	/**
	 * Returns package lookup element by package name
	 *
	 * @param packageName package name in any form
	 * @return lookup element
	 */
	@NotNull
	public static LookupElementBuilder getPackageLookupElement(
			@NotNull Project project,
			@NotNull String packageName
	)
	{
		return getPackageLookupElement(project, packageName, null);
	}

	@NotNull
	public static LookupElementBuilder getPackageLookupElement(
			@NotNull Project project,
			@NotNull String packageName,
			@Nullable String lookupString
	)
	{
		return getPackageLookupElement(
				lookupString == null ? packageName : lookupString,
				PerlPackageUtil.isBuiltIn(packageName),
				PerlPackageUtil.isPragma(packageName),
				PerlPackageUtil.isDeprecated(project, packageName)
		);
	}

	@Nullable
	public static LookupElementBuilder getPackageLookupElement(@NotNull PerlNamespaceDefinition namespaceDefinition)
	{
		return getPackageLookupElement(namespaceDefinition, null);
	}

	@Nullable
	public static LookupElementBuilder getPackageLookupElement(
			@NotNull PerlNamespaceDefinition namespaceDefinition,
			@Nullable String lookupString
	)
	{
		String packageName = namespaceDefinition.getPackageName();
		if (packageName == null)
		{
			return null;
		}

		if (lookupString == null)
		{
			lookupString = packageName;
		}

		return getPackageLookupElement(
				lookupString,
				PerlPackageUtil.isBuiltIn(packageName),
				PerlPackageUtil.isPragma(packageName),
				namespaceDefinition.isDeprecated()
		);
	}

	// fixme we could generalize this
	@NotNull
	public static LookupElementBuilder getPackageLookupElement(
			String lookupString,
			boolean isBuitIn,
			boolean isPragma,
			boolean isDeprecated
	)
	{
		LookupElementBuilder result = LookupElementBuilder
				.create(lookupString)
				.withIcon(PerlIcons.PACKAGE_GUTTER_ICON);

		if (isBuitIn)
		{
			result = result.withBoldness(true);
		}

		if (isPragma)
		{
			result = result.withIcon(PerlIcons.PRAGMA_GUTTER_ICON);
		}

		if (isDeprecated)
		{
			result = result.withStrikeoutness(true);
		}

		return result;
	}


	/**
	 * Returns package lookup element with automatic re-opening autocompeltion
	 *
	 * @param packageName package name
	 * @return lookup element
	 */
	public static LookupElementBuilder getPackageLookupElementWithAutocomplete(
			Project project,
			String packageName,
			String lookupString
	)
	{
		return getPackageLookupElement(project, packageName, lookupString)
				.withInsertHandler(COMPLETION_REOPENER)
				.withTailText("...");
	}

	public static void fillWithDefinedNamespaces(@NotNull PsiElement element, @NotNull final CompletionResultSet result)
	{
		final Project project = element.getProject();

		result.addElement(getPackageLookupElement(project, PerlPackageUtil.UNIVERSAL_PACKAGE));

		for (String packageName : PerlPackageUtil.getDefinedPackageNames(project))
		{
			if (packageName.length() > 0 && Character.isLetterOrDigit(packageName.charAt(0)))
			{
				for (PerlNamespaceDefinition namespaceDefinition : PerlPackageUtil.getNamespaceDefinitions(project, packageName))
				{
					LookupElementBuilder packageLookupElement = getPackageLookupElement(namespaceDefinition);
					if (packageLookupElement != null)
					{
						result.addElement(packageLookupElement);
					}
				}
			}
		}
	}

	public static void fillWithAllPackageNamesWithAutocompletion(@NotNull PsiElement element, @NotNull final CompletionResultSet result)
	{
		final Project project = element.getProject();
		final String prefix = result.getPrefixMatcher().getPrefix();

		for (String packageName : PerlPackageUtil.getDefinedPackageNames(project))
		{
			if (packageName.length() > 0 && Character.isLetterOrDigit(packageName.charAt(0)))
			{
				addExpandablePackageElement(project, result, packageName, prefix);
			}
		}
	}

	protected static void addExpandablePackageElement(Project project, CompletionResultSet result, String packageName, String prefix)
	{
		String name = packageName + PerlPackageUtil.PACKAGE_SEPARATOR;
		if (!StringUtil.equals(prefix, name))
		{
			LookupElementBuilder newElement = PerlPackageCompletionUtil.getPackageLookupElementWithAutocomplete(project, packageName, name);
			newElement.putUserData(PerlCompletionWeighter.WEIGHT, -1);
			result.addElement(newElement);
		}
		name = packageName + PerlPackageUtil.PACKAGE_DEREFERENCE;
		if (!StringUtil.equals(prefix, name))
		{
			LookupElementBuilder newElement = PerlPackageCompletionUtil.getPackageLookupElementWithAutocomplete(project, packageName, name);
			newElement.putUserData(PerlCompletionWeighter.WEIGHT, -1);
			result.addElement(newElement);
		}
	}


	public static void fillWithAllPackageFiles(@NotNull PsiElement element, @NotNull final CompletionResultSet result)
	{
		final Project project = element.getProject();
		PerlPackageUtil.processPackageFilesForPsiElement(element, new Processor<String>()
		{
			@Override
			public boolean process(String s)
			{
				result.addElement(PerlPackageCompletionUtil.getPackageLookupElement(project, s));
				return true;
			}
		});
	}

	public static void fillWithVersionNumbers(@NotNull PsiElement element, @NotNull final CompletionResultSet result)
	{
		for (Map.Entry<String, List<String>> entry : PerlFeaturesTable.AVAILABLE_FEATURES_BUNDLES.entrySet())
		{
			String version = entry.getKey();
			if (StringUtil.startsWith(version, "5"))
			{
				result.addElement(
						LookupElementBuilder.create("v" + version)
								.withTypeText(StringUtil.join(entry.getValue(), " "))
				);
			}
		}
	}

	public static void fillWithPackageNameSuggestions(@NotNull PsiElement element, @NotNull final CompletionResultSet result)
	{
		PsiFile file = element.getContainingFile().getOriginalFile();

		VirtualFile virtualFile = file.getViewProvider().getVirtualFile();
		if (virtualFile.getFileType() == PerlFileTypePackage.INSTANCE)
		{
			result.addElement(LookupElementBuilder.create(virtualFile.getNameWithoutExtension()));
			if (file instanceof PerlFileImpl)
			{
				String packageName = ((PerlFileImpl) file).getFilePackageName();
				if (packageName != null)
				{
					result.addElement(LookupElementBuilder.create(packageName));
				}
			}
		}
	}

	/**
	 * Parent pragma additional insert
	 */
	static class CompletionOpener implements InsertHandler<LookupElement>
	{
		@Override
		public void handleInsert(final InsertionContext context, final LookupElement item)
		{
			// fixme this is bad check for auto-inserting, i belive
			if (context.getCompletionChar() != '\u0000')
			{
				context.setLaterRunnable(new Runnable()
				{
					@Override
					public void run()
					{
						Editor editor = context.getEditor();
						new CodeCompletionHandlerBase(CompletionType.BASIC).invokeCompletion(context.getProject(), editor, 1);
					}
				});
			}
		}
	}

}
