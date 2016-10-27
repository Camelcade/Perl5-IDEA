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

package com.perl5.lang.perl.lexer.adapters;

import com.intellij.lexer.Lexer;
import com.intellij.lexer.MergingLexerAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 16.10.2016.
 * Third level of lexer adapter merges necessary tokens
 */
public class PerlMergingLexerAdapter extends MergingLexerAdapter implements PerlElementTypes
{
	public final static TokenSet TOKENS_TO_MERGE = TokenSet.create(
			POD, STRING_CONTENT, REGEX_TOKEN, STRING_CONTENT_QQ, STRING_CONTENT_XQ, HEREDOC, HEREDOC_QQ, HEREDOC_QX
	);

	public PerlMergingLexerAdapter(@Nullable Project project)
	{
		this(project, true, false);
	}

	public PerlMergingLexerAdapter(@Nullable Project project, boolean allowToMergeCodeBlocks, boolean forceSublexing)
	{
		this(new PerlSublexingLexerAdapter(project, allowToMergeCodeBlocks, forceSublexing));
	}

	public PerlMergingLexerAdapter(@NotNull Lexer original)
	{
		super(original, TOKENS_TO_MERGE);
	}
}
