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

package com.perl5.lang.perl.idea.folding;

import com.intellij.application.options.editor.CodeFoldingOptionsProvider;
import com.intellij.openapi.options.BeanConfigurable;

import javax.swing.*;

/**
 * Created by hurricup on 28.09.2015.
 */
public class PerlCodeFoldingOptionsProvider extends BeanConfigurable<PerlFoldingSettings> implements CodeFoldingOptionsProvider
{
	public PerlCodeFoldingOptionsProvider(PerlFoldingSettings beanInstance)
	{
		super(beanInstance);

		checkBox("COLLAPSE_COMMENTS", "Sequentional line comments");
		checkBox("COLLAPSE_CONSTANT_BLOCKS", "Block of constants");
		checkBox("COLLAPSE_ANON_ARRAYS", "Anonymous arrays");
		checkBox("COLLAPSE_ANON_HASHES", "Anonymous hashes");
		checkBox("COLLAPSE_PARENTHESISED", "Parenthesised expressions");
		checkBox("COLLAPSE_HEREDOCS", "Here-docs");
		checkBox("COLLAPSE_TEMPLATES", "Template parts (Mojolicious, Mason, etc.)");
		checkBox("COLLAPSE_QW", "QW contents");
	}

	@Override
	public JComponent createComponent()
	{
		JComponent result = super.createComponent();
		if (result != null)
		{
			result.setBorder(BorderFactory.createTitledBorder("Perl5"));
		}
		return result;
	}
}
