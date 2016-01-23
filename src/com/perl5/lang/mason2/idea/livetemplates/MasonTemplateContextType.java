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

package com.perl5.lang.mason2.idea.livetemplates;

import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.codeInsight.template.XmlContextType;
import com.intellij.lang.Language;
import com.intellij.lang.html.HTMLLanguage;
import com.intellij.lang.xhtml.XHTMLLanguage;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.mason2.psi.impl.MasonTemplatingFileImpl;
import com.perl5.lang.perl.idea.livetemplates.PerlTemplateContextType;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 10.01.2016.
 */
public class MasonTemplateContextType extends TemplateContextType
{
	public MasonTemplateContextType()
	{
		super("PERL5_MASON", "&Mason2 template", PerlTemplateContextType.Generic.class);
	}

	static boolean isMyLanguage(Language language)
	{
		return language.isKindOf(HTMLLanguage.INSTANCE) || language.isKindOf(XHTMLLanguage.INSTANCE);
	}

	@Override
	public boolean isInContext(@NotNull PsiFile file, int offset)
	{
		return isMyLanguage(PsiUtilCore.getLanguageAtOffset(file, offset))
				&& !XmlContextType.isEmbeddedContent(file, offset)
				&& file instanceof MasonTemplatingFileImpl;
	}
}
