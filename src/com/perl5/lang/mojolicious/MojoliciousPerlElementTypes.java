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

package com.perl5.lang.mojolicious;

import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.tree.IElementType;

import java.util.regex.Pattern;

/**
 * Created by hurricup on 22.12.2015.
 */
public interface MojoliciousPerlElementTypes extends MojoliciousPerlSyntaxElements
{

	IElementType MOJO_BLOCK_OPENER = new MojoliciousPerlTokenType(KEYWORD_MOJO_BLOCK_OPENER);
	IElementType MOJO_BLOCK_CLOSER = new MojoliciousPerlTokenType(KEYWORD_MOJO_BLOCK_CLOSER);
	IElementType MOJO_BLOCK_EXPR_OPENER = new MojoliciousPerlTokenType(KEYWORD_MOJO_BLOCK_EXPR_OPENER);
	IElementType MOJO_BLOCK_EXPR_ESCAPED_OPENER = new MojoliciousPerlTokenType(KEYWORD_MOJO_BLOCK_EXPR_ESCAPED_OPENER);
	IElementType MOJO_BLOCK_EXPR_NOSPACE_CLOSER = new MojoliciousPerlTokenType(KEYWORD_MOJO_BLOCK_EXPR_NOSPACE_CLOSER);

	IElementType MOJO_LINE_OPENER = new MojoliciousPerlTokenType(KEYWORD_MOJO_LINE_OPENER);
	IElementType MOJO_LINE_EXPR_OPENER = new MojoliciousPerlTokenType(KEYWORD_MOJO_LINE_EXPR_OPENER);
	IElementType MOJO_LINE_EXPR_ESCAPED_OPENER = new MojoliciousPerlTokenType(KEYWORD_MOJO_LINE_EXPR_ESCAPED_OPENER);

	IElementType MOJO_BLOCK_OPENER_TAG = new MojoliciousPerlTokenType(KEYWORD_MOJO_BLOCK_OPENER_TAG);
	IElementType MOJO_LINE_OPENER_TAG = new MojoliciousPerlTokenType(KEYWORD_MOJO_LINE_OPENER_TAG);

	IElementType MOJO_BEGIN = new MojoliciousPerlTokenType(KEYWORD_MOJO_BEGIN);
	IElementType MOJO_END = new MojoliciousPerlTokenType(KEYWORD_MOJO_END);

	@Deprecated
	IElementType EMBED_MARKER_OPEN = new MojoliciousPerlTokenType("EMBED_MARKER_OPEN");
	@Deprecated
	IElementType EMBED_MARKER_CLOSE = new MojoliciousPerlTokenType("EMBED_MARKER_CLOSE");
	@Deprecated
	IElementType EMBED_MARKER = new MojoliciousPerlTokenType("EMBED_MARKER_KEY");
	@Deprecated
	IElementType EMBED_MARKER_SEMICOLON = new MojoliciousPerlTokenType("EMBED_MARKER_SEMICOLON");


}
