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

package com.perl5.lang.mason2.idea.highlighter;

import com.intellij.codeInsight.highlighting.TemplateLanguageErrorFilter;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.mason2.Mason2TemplatingFileViewProvider;
import com.perl5.lang.mason2.elementType.Mason2ElementTypes;

/**
 * Created by hurricup on 16.03.2016.
 */
public class Mason2TemplateLanguageErrorFilter extends TemplateLanguageErrorFilter implements Mason2ElementTypes
{
	private static final TokenSet START_TOKENS = TokenSet.create(
			MASON_BLOCK_OPENER,
			MASON_PERL_OPENER,
			MASON_CALL_OPENER,
			MASON_LINE_OPENER,
			TokenType.WHITE_SPACE
	);

	public Mason2TemplateLanguageErrorFilter()
	{
		super(START_TOKENS, Mason2TemplatingFileViewProvider.class, "HTML");
	}
}
