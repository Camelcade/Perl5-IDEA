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

package com.perl5.lang.perl.idea.smartkeys.options;

import com.intellij.openapi.options.BeanConfigurable;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.ui.IdeBorderFactory;
import com.perl5.lang.perl.idea.codeInsight.Perl5CodeInsightSettings;

import javax.swing.*;

/**
 * Created by hurricup on 22.11.2015.
 */
public class PerlSmartKeyOptions extends BeanConfigurable<Perl5CodeInsightSettings> implements UnnamedConfigurable
{
	public PerlSmartKeyOptions()
	{
		super(Perl5CodeInsightSettings.getInstance());
		checkBox("HEREDOC_AUTO_INSERTION", "Automatically insert here-doc terminator");
	}

	@Override
	public JComponent createComponent()
	{
		JComponent panel = super.createComponent();
		if (panel != null)
		{
			panel.setBorder(IdeBorderFactory.PlainSmallWithIndent.createTitledBorder(null, "Perl5", 0, 0, null, null));
		}

		return panel;
	}
}
