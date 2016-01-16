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

package com.perl5.lang.mason.idea.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import com.intellij.util.Processor;
import com.perl5.lang.mason.filetypes.MasonPurePerlComponentFileType;
import com.perl5.lang.mason.idea.configuration.MasonSettings;
import com.perl5.lang.mason.psi.impl.MasonFileImpl;
import com.perl5.lang.perl.psi.PerlString;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 10.01.2016.
 */
public class MasonComponentsCompletionProvider extends CompletionProvider<CompletionParameters>
{
	@Override
	protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
	{
		PsiElement position = parameters.getPosition();
		PsiElement parent = position.getParent();

		if (parent instanceof PerlString)
		{
			Project project = position.getProject();
			MasonSettings masonSettings = MasonSettings.getInstance(project);

			String fullPrefix = ((PerlString) parent)
					.getStringContent()
					.replace(CompletionInitializationContext.DUMMY_IDENTIFIER, "")
					.replace(CompletionInitializationContext.DUMMY_IDENTIFIER_TRIMMED, "");
			result = result.withPrefixMatcher(new PlainPrefixMatcher(fullPrefix));

			final CompletionResultSet finalResultSet = result;

			PsiFile psiFile = position.getContainingFile();
			if (psiFile instanceof MasonFileImpl)
			{
				final VirtualFile containingFile = ((MasonFileImpl) psiFile).getContainingVirtualFile();
				VirtualFile containingDir = null;
				if (containingFile != null && (containingDir = containingFile.getParent()) != null)
				{
					VfsUtil.processFilesRecursively(containingDir, new MasonRootsProcessor(containingDir)
					{
						@Override
						public boolean process(VirtualFile virtualFile)
						{
							FileType fileType = virtualFile.getFileType();
							if (fileType instanceof MasonPurePerlComponentFileType && !containingFile.equals(virtualFile))
							{
								String relativePath = VfsUtil.getRelativePath(virtualFile, getRoot());
								if (StringUtil.isNotEmpty(relativePath))
								{
									finalResultSet.addElement(LookupElementBuilder
											.create(relativePath)
											.withIcon(fileType.getIcon())
									);
								}
							}
							return true;
						}
					});
				}
			}


			for (VirtualFile componentRoot : masonSettings.getComponentsRootsVirtualFiles())
			{
				VfsUtil.processFilesRecursively(componentRoot, new MasonRootsProcessor(componentRoot)
				{
					@Override
					public boolean process(VirtualFile virtualFile)
					{
						FileType fileType = virtualFile.getFileType();
						if (fileType instanceof MasonPurePerlComponentFileType)
						{
							String relativePath = VfsUtil.getRelativePath(virtualFile, getRoot());
							if (StringUtil.isNotEmpty(relativePath))
							{
								finalResultSet.addElement(LookupElementBuilder
										.create("/" + relativePath)
										.withIcon(fileType.getIcon())
								);
							}
						}
						return true;
					}
				});
			}
		}
	}

	public static abstract class MasonRootsProcessor implements Processor<VirtualFile>
	{
		private final VirtualFile myRoot;

		public MasonRootsProcessor(VirtualFile myRoot)
		{
			this.myRoot = myRoot;
		}

		public VirtualFile getRoot()
		{
			return myRoot;
		}
	}
}
