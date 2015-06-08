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

package com.perl5.lang.perl.idea.livetemplates;

import com.intellij.codeInsight.template.EverywhereContextType;
import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.LanguageFileType;
import static com.intellij.patterns.PlatformPatterns.psiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.embedded.EmbeddedPerlFileType;
import com.perl5.lang.perl.PerlFileType;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.PsiPerlStatement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ELI-HOME on 01-Jun-15.
 *
 */
public abstract class PerlTemplateContextType extends TemplateContextType
{
	protected PerlTemplateContextType(@NotNull String id, @NotNull String presentableName, Class<? extends TemplateContextType> baseContextType)
	{
		super(id, presentableName, baseContextType);
	}

	@Override
	public boolean isInContext(@NotNull PsiFile psiFile, int fileOffset)
	{
		if( PsiUtilCore.getLanguageAtOffset(psiFile,fileOffset).isKindOf(PerlLanguage.INSTANCE) )
		{
			PsiElement element = psiFile.findElementAt(fileOffset);

			if( element == null )
				element = psiFile.findElementAt(fileOffset-1);

			if (element instanceof PsiWhiteSpace) {
				return false;
			}
			return element != null && isInContext(element);
		}
		return false;
	}

	protected abstract boolean isInContext(PsiElement element);

	public static class Generic extends PerlTemplateContextType
	{
		public Generic()
		{
			super("PERL5", PerlLanguage.NAME, EverywhereContextType.class);
		}

		@Override
		public boolean isInContext(PsiElement element)
		{
			return true;
		}
	}

	public static class Postfix extends PerlTemplateContextType
	{
		public Postfix()
		{
			super("PERL5_POSTFIX", "Postfix", Generic.class);
		}

		@Override
		public boolean isInContext(PsiElement element)
		{
			PsiPerlStatement statement = PsiTreeUtil.getParentOfType(element, PsiPerlStatement.class);

			return statement != null
					&& statement.getTextOffset() < element.getTextOffset()
					&& statement.getTextRange().getEndOffset() == element.getTextRange().getEndOffset();
		}
	}

	public static class Prefix extends PerlTemplateContextType
	{
		public Prefix()
		{
			super("PERL5_PREFIX", "Prefix", Generic.class);
		}

		@Override
		public boolean isInContext(PsiElement element)
		{
			PsiPerlStatement statement = PsiTreeUtil.getParentOfType(element, PsiPerlStatement.class);

			return statement != null
					&& statement.getTextOffset() == element.getTextOffset();
		}
	}

}
