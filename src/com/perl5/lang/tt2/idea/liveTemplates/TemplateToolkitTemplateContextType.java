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

package com.perl5.lang.tt2.idea.liveTemplates;

import com.intellij.codeInsight.template.EverywhereContextType;
import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.perl5.lang.tt2.TemplateToolkitLanguage;
import com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes;
import com.perl5.lang.tt2.lexer.TemplateToolkitSyntaxElements;
import com.perl5.lang.tt2.psi.impl.PsiGetDirectiveImpl;
import com.perl5.lang.tt2.psi.impl.PsiIdentifierExprImpl;
import com.perl5.lang.tt2.psi.impl.PsiMacroContentImpl;
import com.perl5.lang.tt2.utils.TemplateToolkitPsiUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 12.06.2016.
 */
public abstract class TemplateToolkitTemplateContextType extends TemplateContextType implements TemplateToolkitElementTypes
{
	public TemplateToolkitTemplateContextType(@NotNull @NonNls String id, @NotNull String presentableName, @Nullable Class<? extends TemplateContextType> baseContextType)
	{
		super(id, presentableName, baseContextType);
	}

	protected abstract boolean isInContext(PsiElement element);

	@Override
	public boolean isInContext(@NotNull PsiFile file, int offset)
	{
		FileViewProvider viewProvider = file.getViewProvider();
		PsiFile ttFile = viewProvider.getPsi(TemplateToolkitLanguage.INSTANCE);

		if (ttFile != null)
		{
			PsiElement element = viewProvider.findElementAt(offset, TemplateToolkitLanguage.INSTANCE);
			if (element == null && offset > 0)
			{
				element = viewProvider.findElementAt(offset - 1, TemplateToolkitLanguage.INSTANCE);
			}

			if (element != null && element.getLanguage() == TemplateToolkitLanguage.INSTANCE)
			{
				return isInContext(element);
			}
		}

		return false;
	}

	public static class Generic extends TemplateToolkitTemplateContextType
	{
		public Generic()
		{
			super(TemplateToolkitLanguage.NAME, TemplateToolkitLanguage.NAME, EverywhereContextType.class);
		}

		public Generic(String id, String presentableName)
		{
			super(id, presentableName, TemplateToolkitTemplateContextType.Generic.class);
		}

		@Override
		protected boolean isInContext(PsiElement element)
		{
			return false;
		}
	}

	public static class CommandPosition extends TemplateToolkitTemplateContextType.Generic
	{
		public CommandPosition()
		{
			super(TemplateToolkitLanguage.NAME + ".command", "Directive");
		}

		@Override
		protected boolean isInContext(PsiElement element)
		{
			if (element.getPrevSibling() != null)
			{
				return false;
			}

			PsiElement parent = element.getParent();
			if (!(parent instanceof PsiIdentifierExprImpl))
			{
				return false;
			}

			PsiElement grandParent = parent.getParent();
			if (!(grandParent instanceof PsiGetDirectiveImpl))
			{
				return false;
			}

			PsiElement prevSignificantSibling = TemplateToolkitPsiUtils.getPrevSignificantSibling(grandParent);

			if (prevSignificantSibling == null)
			{
				return grandParent.getParent() instanceof PsiMacroContentImpl;
			}

			return TemplateToolkitSyntaxElements.CONSTRUCTION_PREFIX.contains(prevSignificantSibling.getNode().getElementType());
		}
	}
}
