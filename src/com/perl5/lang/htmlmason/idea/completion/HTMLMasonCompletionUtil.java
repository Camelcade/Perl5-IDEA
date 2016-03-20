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

package com.perl5.lang.htmlmason.idea.completion;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Processor;
import com.perl5.lang.htmlmason.HTMLMasonSyntaxElements;
import com.perl5.lang.htmlmason.filetypes.HTMLMasonFileType;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonCompositeElement;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonMethodDefinition;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonSubcomponentDefitnition;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 20.03.2016.
 */
public class HTMLMasonCompletionUtil implements HTMLMasonSyntaxElements
{
	public static void fillWithComponentSlugs(@NotNull CompletionResultSet resultSet)
	{
		resultSet.addElement(LookupElementBuilder.create(COMPONENT_SLUG_PARENT));
		resultSet.addElement(LookupElementBuilder.create(COMPONENT_SLUG_SELF));
		resultSet.addElement(LookupElementBuilder.create(COMPONENT_SLUG_REQUEST));

	}

	public static void fillWithSubcomponents(@NotNull CompletionResultSet resultSet, @NotNull HTMLMasonFileImpl component)
	{
		for (HTMLMasonCompositeElement element : component.getSubComponentsDefinitions())
		{
			assert element instanceof HTMLMasonSubcomponentDefitnition;

			String name = ((HTMLMasonSubcomponentDefitnition) element).getName();
			if (name != null)
			{
				resultSet.addElement(LookupElementBuilder
						.create(name)
						.withIcon(element.getIcon(0))
				);
			}
		}
	}

	public static void fillWithMethods(@NotNull final CompletionResultSet resultSet, @NotNull HTMLMasonFileImpl component)
	{
		component.processMethodDefinitionsInThisOrParents(new Processor<HTMLMasonMethodDefinition>()
		{
			@Override
			public boolean process(HTMLMasonMethodDefinition element)
			{
				String name = element.getName();
				if (name != null)
				{
					resultSet.addElement(LookupElementBuilder
							.create(name)
							.withIcon(element.getIcon(0))
					);
				}
				return true;
			}
		});
	}

	public static void fillWithRelativeSubcomponents(@NotNull CompletionResultSet resultSet, @NotNull HTMLMasonFileImpl component)
	{
		VirtualFile containingFile = component.getComponentVirtualFile();
		VirtualFile root = null;
		if (containingFile != null && (root = containingFile.getParent()) != null)
		{
			VfsUtil.processFilesRecursively(root, new ComponentsFilesCollector("", root, resultSet));
		}
	}

	public static void fillWithAbsoluteSubcomponents(@NotNull final CompletionResultSet resultSet, @NotNull Project project)
	{
		HTMLMasonSettings masonSettings = HTMLMasonSettings.getInstance(project);

		for (VirtualFile componentRoot : masonSettings.getComponentsRootsVirtualFiles())
		{
			VfsUtil.processFilesRecursively(componentRoot, new ComponentsFilesCollector("/", componentRoot, resultSet));
		}
	}

	protected static class ComponentsFilesCollector implements Processor<VirtualFile>
	{
		private final String myPrefix;
		private final CompletionResultSet myResultSet;
		private final VirtualFile myRoot;

		public ComponentsFilesCollector(@NotNull String myPrefix, @NotNull VirtualFile root, @NotNull CompletionResultSet resultSet)
		{
			this.myPrefix = myPrefix;
			myResultSet = resultSet;
			myRoot = root;
		}

		@Override
		public boolean process(VirtualFile virtualFile)
		{
			if (virtualFile.getFileType() == HTMLMasonFileType.INSTANCE)
			{
				String relPath = VfsUtil.getRelativePath(virtualFile, myRoot);
				if (StringUtil.isNotEmpty(relPath))
				{
					myResultSet.addElement(LookupElementBuilder
							.create(myPrefix + relPath)
							.withIcon(HTMLMasonFileType.INSTANCE.getIcon()));
				}
			}
			return true;
		}
	}
}
