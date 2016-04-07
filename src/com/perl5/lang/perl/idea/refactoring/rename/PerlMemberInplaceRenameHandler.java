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

package com.perl5.lang.perl.idea.refactoring.rename;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.refactoring.rename.inplace.MemberInplaceRenameHandler;
import com.intellij.refactoring.rename.inplace.MemberInplaceRenamer;
import com.perl5.lang.perl.PerlLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 07.04.2016.
 */
public class PerlMemberInplaceRenameHandler extends MemberInplaceRenameHandler
{
	@NotNull
	@Override
	protected MemberInplaceRenamer createMemberRenamer(@NotNull PsiElement element, PsiNameIdentifierOwner elementToRename, Editor editor)
	{
		return new PerlMemberInplaceRenamer(elementToRename, elementToRename, editor);
	}

	@Override
	protected boolean isAvailable(PsiElement element, Editor editor, PsiFile file)
	{
		Language fileLanguage = file.getViewProvider().getBaseLanguage();
		return fileLanguage.isKindOf(PerlLanguage.INSTANCE) && super.isAvailable(element, editor, file);
	}
}
