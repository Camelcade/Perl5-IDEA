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

package com.perl5.lang.htmlmason.lexer;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonCustomTag;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlTemplatingLexer;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.perl5.lang.htmlmason.lexer.HTMLMasonLexer.PERL_EXPR;
import static com.perl5.lang.htmlmason.lexer.HTMLMasonLexer.PERL_EXPR_FILTER;


/**
 * Created by hurricup on 29.10.2016.
 */
public abstract class HTMLMasonBaseLexer extends PerlTemplatingLexer implements HTMLMasonElementTypes, PerlElementTypes
{
	private final CommentEndCalculator COMMENT_END_CALCULATOR = commentText ->
	{
		int realLexicalState = getRealLexicalState();
		if (realLexicalState == PERL_EXPR || realLexicalState == PERL_EXPR_FILTER)
		{
			return StringUtil.indexOf(commentText, KEYWORD_BLOCK_CLOSER);
		}
		return -1;
	};
	@Nullable
	protected Map<String, HTMLMasonCustomTag> myCustomTagsMap;

	@Nullable
	@Override
	protected CommentEndCalculator getCommentEndCalculator()
	{
		return COMMENT_END_CALCULATOR;
	}

	public HTMLMasonBaseLexer withProject(@Nullable Project project)
	{
		myCustomTagsMap = project == null ? null : HTMLMasonSettings.getInstance(project).getCustomTagsMap();
		return this;
	}
}
