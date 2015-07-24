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

package com.perl5.lang.perl.idea.completion.providers;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hurricup on 24.07.2015.
 */
public class PerlSubPackageCompletionProvider extends CompletionProvider<CompletionParameters>
{
//	public static final NamespaceSelectionHanlder NAMESPACE_SELECTION_HANLDER = new NamespaceSelectionHanlder();

	@Override
	protected void addCompletions(
			@NotNull final CompletionParameters parameters,
			ProcessingContext context,
			@NotNull CompletionResultSet result)
	{
		final PsiElement method = parameters.getPosition().getParent();
		assert method instanceof PsiPerlMethod;

		PerlNamespaceElement explicitNamespace = ((PsiPerlMethod) method).getNamespaceElement();

		final boolean isObjectMethod = ((PsiPerlMethod) method).isObjectMethod();

		String packagePrefix = result.getPrefixMatcher().getPrefix();

		if( explicitNamespace != null )
		{
			packagePrefix = explicitNamespace.getText() + packagePrefix;
			result = result.withPrefixMatcher(packagePrefix);
		}

		final CompletionResultSet resultSet = result;

		ApplicationManager.getApplication().runReadAction(new Runnable()
		{
			@Override
			public void run()
			{
				Project project = parameters.getPosition().getProject();

				// fixme we should re-open autocomplete here
				resultSet.addElement(PerlPackageUtil.getPackageLookupElement("SUPER::"));

				if( !isObjectMethod )
					for( String packageName: PerlPackageUtil.getDefinedPackageNames(project))
						resultSet.addElement(PerlPackageUtil.getPackageLookupElement(packageName));
			}
		});
	}



//	static class NamespaceSelectionHanlder implements InsertHandler<LookupElement>
//	{
//		@Override
//		public void handleInsert(final InsertionContext context, LookupElement item)
//		{
//			final Editor editor = context.getEditor();
//
//			EditorModificationUtil.insertStringAtCaret(editor, "::");
//
//			context.setLaterRunnable(new Runnable()
//			{
//				@Override
//				public void run()
//				{
//					new CodeCompletionHandlerBase(CompletionType.BASIC).invokeCompletion(context.getProject(), editor);
//				}
//			});
//		}
//	}
}
