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

import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.extensions.parser.PerlReferencesProvider;
import com.perl5.lang.perl.lexer.PerlBaseLexer;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlStatement;
import com.perl5.lang.perl.psi.references.PerlNamespaceReference;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 23.05.2015.
 */
public class PerlStringContentElementImpl extends LeafPsiElement implements PerlStringContentElement
{
	static final String FILE_PATH_PATTERN_TEXT = "\\.?[a-zA-Z0-9\\-_]+(?:\\.[a-zA-Z0-9\\-_]*)*";
	static final String FILE_PATH_DELIMITER_PATTERN_TEXT = "(?:\\\\+|/+)";
	static final Pattern FILE_PATH_PATTERN = Pattern.compile(
			FILE_PATH_DELIMITER_PATTERN_TEXT + "?" +
					"(?:" + FILE_PATH_PATTERN_TEXT + FILE_PATH_DELIMITER_PATTERN_TEXT + ")+ ?" +
					"(" + FILE_PATH_PATTERN_TEXT + ")" + FILE_PATH_DELIMITER_PATTERN_TEXT + "?"
	);
	protected Boolean looksLikePath = null;
	protected Boolean looksLikePackage = null;
	protected AtomicNotNullLazyValue<PsiReference[]> myReferences;

	public PerlStringContentElementImpl(@NotNull IElementType type, CharSequence text)
	{
		super(type, text);
		createMyReferences();
	}

	private void createMyReferences()
	{
		myReferences = new AtomicNotNullLazyValue<PsiReference[]>()
		{
			@NotNull
			@Override
			protected PsiReference[] compute()
			{
				if (looksLikePackage())
				{
					return new PsiReference[]{new PerlNamespaceReference(PerlStringContentElementImpl.this, null)};
				}
				else
				{
					@SuppressWarnings("unchecked")
					PerlReferencesProvider referencesProvider = PsiTreeUtil.getParentOfType(PerlStringContentElementImpl.this, PerlReferencesProvider.class, true, PsiPerlStatement.class);

					PsiReference[] references = null;

					if (referencesProvider != null)
					{
						references = referencesProvider.getReferences(PerlStringContentElementImpl.this);
					}

					if (references == null)
					{
						references = PsiReference.EMPTY_ARRAY;
					}

					return references;
				}
			}
		};
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PerlVisitor) ((PerlVisitor) visitor).visitStringContentElement(this);
		else super.accept(visitor);
	}

	@NotNull
	@Override
	public PsiReference[] getReferences()
	{
		return myReferences.getValue();
	}

	@Override
	public PsiReference getReference()
	{
		return myReferences.getValue().length > 0 ? myReferences.getValue()[0] : null;
	}

	@Override
	public boolean looksLikePackage()
	{
		if (looksLikePackage != null)
			return looksLikePackage;

		String text = getText();
		return looksLikePackage = text.contains("::") && PerlBaseLexer.AMBIGUOUS_PACKAGE_PATTERN.matcher(text).matches();
	}

	@Override
	public boolean looksLikePath()
	{
		if (looksLikePath != null)
			return looksLikePath;
		return looksLikePath = FILE_PATH_PATTERN.matcher(getText()).matches();
	}

	@Override
	public String getContentFileName()
	{
		if (looksLikePath())
		{
			Matcher m = FILE_PATH_PATTERN.matcher(getText());
			if (m.matches())
				return m.group(1);
		}
		return null;
	}

	@Override
	public void clearCaches()
	{
		super.clearCaches();
		createMyReferences();
	}


/*
	@Override
	public TextRange getTextRange()
	{
		PsiElement parent = getParent();
		if (parent instanceof PerlElementRangeProvider)
		{
			return ((PerlElementRangeProvider) parent).getNestedElementTextRange(this);
		}
		return super.getTextRange();
	}

	@Override
	public int getTextLength()
	{
		PsiElement parent = getParent();
		if (parent instanceof PerlElementRangeProvider)
		{
			return ((PerlElementRangeProvider) parent).getNestedElementLength(this, super.getTextLength());
		}
		return super.getTextLength();
	}



	@Override
	public int getTextOffset()
	{
		PsiElement parent = getParent();
		if (parent instanceof PerlElementRangeProvider)
		{
			return ((PerlElementRangeProvider) parent).getNestedElementTextRange(this);
		}
		return super.getTextOffset();
	}
*/

	/*

	@Override
	public int getStartOffset()
	{
		PsiElement parent = getParent();
		if (parent instanceof PerlElementRangeProvider)
		{
			return ((PerlElementRangeProvider) parent).getNestedElementStartOffset(this, super.getStartOffset());
		}
		return super.getStartOffset();
	}

*/
}


