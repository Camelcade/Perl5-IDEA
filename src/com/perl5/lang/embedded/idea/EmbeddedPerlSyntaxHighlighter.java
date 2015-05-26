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

package com.perl5.lang.embedded.idea;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.embedded.EmbeddedPerlLexerAdapter;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.pod.idea.highlighter.PodHighlightingLexer;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * Created by hurricup on 19.05.2015.
 */
public class EmbeddedPerlSyntaxHighlighter extends PerlSyntaxHighlighter implements PerlElementTypes
{
	public static final TextAttributesKey EMBED_MARKER = createTextAttributesKey("PERL_EMBED_MARKER", DefaultLanguageHighlighterColors.METADATA);

	TextAttributesKey[]	EMBED_MARKER_KEYS = new TextAttributesKey[]{EMBED_MARKER,PERL_BUILT_IN};

	@NotNull
	@Override
	public Lexer getHighlightingLexer()
	{
		return new EmbeddedPerlLexerAdapter();
	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType)
	{
		if( tokenType == PerlElementTypes.EMBED_MARKER )
			return EMBED_MARKER_KEYS;
		else
			return super.getTokenHighlights(tokenType);
	}
}
