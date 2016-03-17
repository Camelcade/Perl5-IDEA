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

package com.perl5.lang.htmlmason.idea.highlighter;

import com.intellij.codeInsight.highlighting.TemplateLanguageErrorFilter;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.htmlmason.HTMLMasonFileViewProvider;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;

/**
 * Created by hurricup on 16.03.2016.
 */
public class HTMLMasonTemplateLanguageErrorFilter extends TemplateLanguageErrorFilter implements HTMLMasonElementTypes
{
	private static final TokenSet START_TOKENS = TokenSet.create(
			HTML_MASON_BLOCK_OPENER,
			HTML_MASON_PERL_OPENER,
			HTML_MASON_CALL_OPENER,
			HTML_MASON_CALL_FILTERING_OPENER,
			HTML_MASON_CALL_CLOSE_TAG_START,
			HTML_MASON_LINE_OPENER,
			TokenType.WHITE_SPACE
	);

	public HTMLMasonTemplateLanguageErrorFilter()
	{
		super(START_TOKENS, HTMLMasonFileViewProvider.class, "HTML");
	}
}
