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

package com.perl5.lang.pod.elementTypes;

import com.intellij.lang.Language;
import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.lexer.PodElementTypes;
import org.jetbrains.annotations.NonNls;

/**
 * Created by hurricup on 31.03.2016.
 */
public class PodTemplatingElementType extends TemplateDataElementType implements PerlElementTypes, PodElementTypes
{
	public PodTemplatingElementType(@NonNls String debugName, Language language)
	{
		super(debugName, language, POD, POD_OUTER);
	}

	@Override
	protected Language getTemplateFileLanguage(TemplateLanguageFileViewProvider viewProvider)
	{
		return PodLanguage.INSTANCE;
	}

}
