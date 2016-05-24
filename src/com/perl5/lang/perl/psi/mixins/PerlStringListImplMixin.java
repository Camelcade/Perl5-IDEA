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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlStringList;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 24.05.2016.
 */
public class PerlStringListImplMixin extends PerlCompositeElementImpl implements PerlStringList
{
	public PerlStringListImplMixin(@NotNull ASTNode node)
	{
		super(node);
	}

	@NotNull
	@Override
	public List<String> getStringContents()
	{
		List<String> result = new ArrayList<String>();

		PsiElement run = getFirstChild();
		StringBuilder builder = null;

		while (run != null)
		{
			while (run instanceof PerlStringContentElementImpl)
			{
				if (builder == null)
				{
					builder = new StringBuilder();
				}
				builder.append(run.getNode().getText());
				run = run.getNextSibling();
			}

			if (builder != null)
			{
				result.add(builder.toString());
				builder = null;
			}
			else
			{
				run = run.getNextSibling();
			}
		}

		return result;
	}
}
