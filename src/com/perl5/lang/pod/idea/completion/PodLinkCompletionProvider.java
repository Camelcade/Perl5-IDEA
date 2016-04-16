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

package com.perl5.lang.pod.idea.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.intellij.util.Processor;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.completion.util.PerlPackageCompletionUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.pod.filetypes.PodFileType;
import com.perl5.lang.pod.parser.psi.references.PodLinkToFileReference;
import com.perl5.lang.pod.parser.psi.references.PodLinkToSectionReference;
import com.perl5.lang.pod.parser.psi.util.PodFileUtil;
import com.perl5.lang.pod.psi.PsiPodFormatLink;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Created by hurricup on 16.04.2016.
 */
public class PodLinkCompletionProvider extends CompletionProvider<CompletionParameters>
{
	protected static void addFilesCompletions(PsiPodFormatLink link, @NotNull final CompletionResultSet result)
	{
		final Project project = link.getProject();
		final Set<String> foundPods = new THashSet<String>();

		PerlPackageUtil.processFilesForPsiElement(link, new PerlPackageUtil.ClassRootVirtualFileProcessor()
		{
			@Override
			public boolean process(VirtualFile file, VirtualFile classRoot)
			{
				String className = PodFileUtil.getPackageNameFromVirtualFile(file, classRoot);
				if (StringUtil.isNotEmpty(className))
				{
					boolean isBuiltIn = false;
					if (StringUtil.startsWith(className, "pods::"))
					{
						isBuiltIn = true;
						className = className.substring(6);
					}
					if (!foundPods.contains(className))
					{
						result.addElement(LookupElementBuilder.create(className).withIcon(PerlIcons.POD_FILE).withBoldness(isBuiltIn));
						foundPods.add(className);
					}
				}
				return true;
			}
		}, PodFileType.INSTANCE);

		PerlPackageUtil.processPackageFilesForPsiElement(link, new Processor<String>()
		{
			@Override
			public boolean process(String s)
			{
				if (StringUtil.isNotEmpty(s))
				{
					if (!foundPods.contains(s))
					{
						result.addElement(PerlPackageCompletionUtil.getPackageLookupElement(project, s));
						foundPods.add(s);
					}
				}
				return true;
			}
		});
	}

	protected static void addSectionsCompletions(@NotNull CompletionResultSet result, PsiReference[] linkReferences)
	{

	}

	@Override
	protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
	{
		PsiElement element = parameters.getOriginalPosition();
		if (element == null)
			return;

		PsiPodFormatLink psiPodFormatLink = PsiTreeUtil.getParentOfType(element, PsiPodFormatLink.class);
		if (psiPodFormatLink == null)
			return;

		TextRange elementRange = element.getTextRange().shiftRight(-psiPodFormatLink.getTextOffset());
		CharSequence linkText = psiPodFormatLink.getText();
		PsiReference[] references = psiPodFormatLink.getReferences();
		for (PsiReference reference : references)
		{
			TextRange referenceRange = reference.getRangeInElement();
			if (referenceRange.contains(elementRange))
			{
				if (reference instanceof PodLinkToFileReference)
				{
					addFilesCompletions(psiPodFormatLink, result);
					return;
				}
				else if (reference instanceof PodLinkToSectionReference)
				{
					addSectionsCompletions(result, references);
					return;
				}
			}
		}
	}
}
