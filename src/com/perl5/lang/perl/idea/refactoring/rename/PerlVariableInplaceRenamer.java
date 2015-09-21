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

package com.perl5.lang.perl.idea.refactoring.rename;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.refactoring.rename.inplace.VariableInplaceRenamer;

import java.util.List;

/**
 * Created by hurricup on 20.09.2015.
 */
public class PerlVariableInplaceRenamer extends VariableInplaceRenamer
{
	public PerlVariableInplaceRenamer(PsiNamedElement elementToRename, Editor editor)
	{
		super(elementToRename, editor);
	}

	@Override
	protected void collectAdditionalElementsToRename(List<Pair<PsiElement, TextRange>> stringUsages)
	{
	}
}