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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 20.09.2015.
 */
public class PerlStringBareImplMixin extends PerlCompositeElementImpl implements PerlString
{
	public PerlStringBareImplMixin(ASTNode node)
	{
		super(node);
	}

	@NotNull
	@Override
	public String getStringContent()
	{
		return getText();
	}

	@Override
	public void setStringContent(String newContent)
	{
		PerlStringContentElementImpl newName = PerlElementFactory.createStringContent(getProject(), newContent);
		if (newName != null)
			getFirstChild().replace(newName);
		else
			throw new IncorrectOperationException("Unable to create string from: " + newContent);
	}

	@NotNull
	@Override
	public TextRange getContentTextRangeInParent()
	{
		return new TextRange(0, getTextLength());
	}

	@Override
	public int getContentLength()
	{
		return getContentTextRangeInParent().getLength();
	}

}
