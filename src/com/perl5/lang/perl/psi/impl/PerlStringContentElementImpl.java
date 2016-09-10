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

package com.perl5.lang.perl.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.extensions.parser.PerlReferencesProvider;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlLeafPsiElementWithCachingReference;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlStatement;
import com.perl5.lang.perl.psi.references.PerlNamespaceReference;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 23.05.2015.
 */
public class PerlStringContentElementImpl extends PerlLeafPsiElementWithCachingReference implements PerlStringContentElement
{
	static final String FILE_PATH_PATTERN_TEXT = "\\.?[\\p{L}\\d\\-_]+(?:\\.[\\p{L}\\d\\-_]*)*";
	static final String FILE_PATH_DELIMITER_PATTERN_TEXT = "(?:\\\\+|/+)";
	static final Pattern FILE_PATH_PATTERN = Pattern.compile(
			FILE_PATH_DELIMITER_PATTERN_TEXT + "*" +
					"(?:" + FILE_PATH_PATTERN_TEXT + FILE_PATH_DELIMITER_PATTERN_TEXT + ")+ ?" +
					"(" + FILE_PATH_PATTERN_TEXT + ")" + FILE_PATH_DELIMITER_PATTERN_TEXT + "?"
	);
	protected Boolean looksLikePath = null;
	protected Boolean looksLikePackage = null;

	public PerlStringContentElementImpl(@NotNull IElementType type, CharSequence text)
	{
		super(type, text);
	}

	@Override
	public void computeReferences(List<PsiReference> psiReferences)
	{
		if (looksLikePackage())
		{
			psiReferences.add(new PerlNamespaceReference(PerlStringContentElementImpl.this, null));
		}
		else
		{
			@SuppressWarnings("unchecked")
			PerlReferencesProvider referencesProvider = PsiTreeUtil.getParentOfType(PerlStringContentElementImpl.this, PerlReferencesProvider.class, true, PsiPerlStatement.class);

			if (referencesProvider != null)
			{
				PsiReference[] references = referencesProvider.getReferences(PerlStringContentElementImpl.this);
				if (references != null)
				{
					Collections.addAll(psiReferences, references);
				}
			}
		}
		super.computeReferences(psiReferences);
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PerlVisitor)
		{
			((PerlVisitor) visitor).visitStringContentElement(this);
		}
		else
		{
			super.accept(visitor);
		}
	}

	@Override
	public boolean looksLikePackage()
	{
		if (looksLikePackage != null)
		{
			return looksLikePackage;
		}

		return getElementType() == PerlElementTypes.STRING_PACKAGE;
	}

	@Override
	public boolean looksLikePath()
	{
		if (looksLikePath != null)
		{
			return looksLikePath;
		}
		return looksLikePath = FILE_PATH_PATTERN.matcher(getContinuosText()).matches();
	}

	@Override
	public String getContentFileName()
	{
		if (looksLikePath())
		{
			Matcher m = FILE_PATH_PATTERN.matcher(getContinuosText());
			if (m.matches())
			{
				return m.group(1);
			}
		}
		return null;
	}

	@Override
	public String getContinuosText()    // fixme some caching here would be nice; Also we now could implement smarter logic
	{
		StringBuilder builder = new StringBuilder("");

		PsiElement currentElement = this;

		while (currentElement.getPrevSibling() instanceof PerlStringContentElement)
		{
			currentElement = currentElement.getPrevSibling();
		}

		while (currentElement instanceof PerlStringContentElement)
		{
			builder.append(currentElement.getNode().getText());
			currentElement = currentElement.getNextSibling();
		}

		return builder.toString();
	}
}


