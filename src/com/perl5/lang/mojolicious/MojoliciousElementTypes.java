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
import com.perl5.lang.embedded.EmbeddedPerlLanguage;
import com.perl5.lang.pod.elementTypes.PodTemplatingElementType;

/**
 * Created by hurricup on 22.12.2015.
 */
public interface MojoliciousElementTypes extends MojoliciousSyntaxElements
{
	IElementType MOJO_TEMPLATE_BLOCK_HTML = new MojoliciousTokenType("MOJO_TEMPLATE_BLOCK_HTML");
	IElementType MOJO_OUTER_ELEMENT_TYPE = new MojoliciousTokenType("MOJO_OUTER_ELEMENT_TYPE");
	IElementType MOJO_HTML_TEMPLATE_DATA = new TemplateDataElementType("MOJO_HTML_TEMPLATE_DATA", MojoliciousLanguage.INSTANCE, MOJO_TEMPLATE_BLOCK_HTML, MOJO_OUTER_ELEMENT_TYPE);
	IElementType MOJO_POD_TEMPLATE_DATA = new PodTemplatingElementType("MOJO_POD_TEMPLATE_DATA", EmbeddedPerlLanguage.INSTANCE);

	IElementType MOJO_BLOCK_OPENER = new MojoliciousTokenType(KEYWORD_MOJO_BLOCK_OPENER);
	IElementType MOJO_BLOCK_EXPR_OPENER = new MojoliciousTokenType(KEYWORD_MOJO_BLOCK_EXPR_OPENER);
	IElementType MOJO_BLOCK_EXPR_ESCAPED_OPENER = new MojoliciousTokenType(KEYWORD_MOJO_BLOCK_EXPR_ESCAPED_OPENER);

	IElementType MOJO_BLOCK_CLOSER = new MojoliciousTokenType(KEYWORD_MOJO_BLOCK_CLOSER);
	IElementType MOJO_BLOCK_NOSPACE_CLOSER = new MojoliciousTokenType(KEYWORD_MOJO_BLOCK_EXPR_NOSPACE_CLOSER);
	IElementType MOJO_BLOCK_CLOSER_SEMI = new MojoliciousTokenType(";" + KEYWORD_MOJO_BLOCK_CLOSER);
	IElementType MOJO_BLOCK_EXPR_NOSPACE_CLOSER = new MojoliciousTokenType(KEYWORD_MOJO_BLOCK_EXPR_NOSPACE_CLOSER);
	IElementType MOJO_BLOCK_EXPR_CLOSER = new MojoliciousTokenType(KEYWORD_MOJO_BLOCK_CLOSER);

	IElementType MOJO_LINE_OPENER = new MojoliciousTokenType(KEYWORD_MOJO_LINE_OPENER);
	IElementType MOJO_LINE_EXPR_OPENER = new MojoliciousTokenType(KEYWORD_MOJO_LINE_EXPR_OPENER);
	IElementType MOJO_LINE_EXPR_ESCAPED_OPENER = new MojoliciousTokenType(KEYWORD_MOJO_LINE_EXPR_ESCAPED_OPENER);

	IElementType MOJO_BLOCK_OPENER_TAG = new MojoliciousTokenType(KEYWORD_MOJO_BLOCK_OPENER_TAG);
	IElementType MOJO_LINE_OPENER_TAG = new MojoliciousTokenType(KEYWORD_MOJO_LINE_OPENER_TAG);

	IElementType MOJO_BEGIN = new MojoliciousTokenType(KEYWORD_MOJO_BEGIN);
	IElementType MOJO_END = new MojoliciousTokenType(KEYWORD_MOJO_END);
}
