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

package com.perl5.lang.htmlmason.elementType;

import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.htmlmason.HTMLMasonLanguage;
import com.perl5.lang.htmlmason.HTMLMasonSyntaxElements;

/**
 * Created by hurricup on 05.03.2016.
 */
public interface HTMLMasonElementTypes extends HTMLMasonSyntaxElements
{
	IElementType HTML_MASON_TEMPLATE_BLOCK_HTML = new HTMLMasonTokenType("HTML_MASON_TEMPLATE_BLOCK_HTML");
	IElementType HTML_MASON_OUTER_ELEMENT_TYPE = new HTMLMasonElementType("HTML_MASON_OUTER_ELEMENT_TYPE");
	IElementType HTML_MASON_HTML_TEMPLATE_DATA = new TemplateDataElementType(
			"HTML_MASON_HTML_TEMPLATE_DATA",
			HTMLMasonLanguage.INSTANCE,
			HTML_MASON_TEMPLATE_BLOCK_HTML,
			HTML_MASON_OUTER_ELEMENT_TYPE
	);

	IElementType HTML_MASON_BLOCK_OPENER = new HTMLMasonTokenType(KEYWORD_BLOCK_OPENER);
	IElementType HTML_MASON_BLOCK_CLOSER = new HTMLMasonTokenType(KEYWORD_BLOCK_CLOSER);

	IElementType HTML_MASON_LINE_OPENER = new HTMLMasonTokenType("%");
	IElementType HTML_MASON_EXPR_FILTER_PIPE = new HTMLMasonTokenType("|");

	IElementType HTML_MASON_TAG_CLOSER = new HTMLMasonTokenType(KEYWORD_TAG_CLOSER);

	IElementType HTML_MASON_CALL_OPENER = new HTMLMasonTokenType(KEYWORD_CALL_OPENER);
	IElementType HTML_MASON_CALL_FILTERING_OPENER = new HTMLMasonTokenType(KEYWORD_CALL_OPENER_FILTER);
	IElementType HTML_MASON_CALL_CLOSER = new HTMLMasonTokenType(KEYWORD_CALL_CLOSER);
	IElementType HTML_MASON_CALL_CLOSE_TAG = new HTMLMasonTokenType(KEYWORD_CALL_CLOSE_TAG);
	IElementType HTML_MASON_CALL_CLOSE_TAG_START = new HTMLMasonTokenType(KEYWORD_CALL_CLOSE_TAG_START);

	IElementType HTML_MASON_METHOD_OPENER = new HTMLMasonTokenType(KEYWORD_METHOD_OPENER);
	IElementType HTML_MASON_METHOD_CLOSER = new HTMLMasonTokenType(KEYWORD_METHOD_CLOSER);

	IElementType HTML_MASON_DEF_OPENER = new HTMLMasonTokenType(KEYWORD_DEF_OPENER);
	IElementType HTML_MASON_DEF_CLOSER = new HTMLMasonTokenType(KEYWORD_DEF_CLOSER);

	IElementType HTML_MASON_DOC_OPENER = new HTMLMasonTokenType(KEYWORD_DOC_OPENER);
	IElementType HTML_MASON_DOC_CLOSER = new HTMLMasonTokenType(KEYWORD_DOC_CLOSER);

	IElementType HTML_MASON_FLAGS_OPENER = new HTMLMasonTokenType(KEYWORD_FLAGS_OPENER);
	IElementType HTML_MASON_FLAGS_CLOSER = new HTMLMasonTokenType(KEYWORD_FLAGS_CLOSER);

	IElementType HTML_MASON_ATTR_OPENER = new HTMLMasonTokenType(KEYWORD_ATTR_OPENER);
	IElementType HTML_MASON_ATTR_CLOSER = new HTMLMasonTokenType(KEYWORD_ATTR_CLOSER);

	IElementType HTML_MASON_ARGS_OPENER = new HTMLMasonTokenType(KEYWORD_ARGS_OPENER);
	IElementType HTML_MASON_ARGS_CLOSER = new HTMLMasonTokenType(KEYWORD_ARGS_CLOSER);

	IElementType HTML_MASON_INIT_OPENER = new HTMLMasonTokenType(KEYWORD_INIT_OPENER);
	IElementType HTML_MASON_INIT_CLOSER = new HTMLMasonTokenType(KEYWORD_INIT_CLOSER);

	IElementType HTML_MASON_ONCE_OPENER = new HTMLMasonTokenType(KEYWORD_ONCE_OPENER);
	IElementType HTML_MASON_ONCE_CLOSER = new HTMLMasonTokenType(KEYWORD_ONCE_CLOSER);

	IElementType HTML_MASON_SHARED_OPENER = new HTMLMasonTokenType(KEYWORD_SHARED_OPENER);
	IElementType HTML_MASON_SHARED_CLOSER = new HTMLMasonTokenType(KEYWORD_SHARED_CLOSER);

	IElementType HTML_MASON_CLEANUP_OPENER = new HTMLMasonTokenType(KEYWORD_CLEANUP_OPENER);
	IElementType HTML_MASON_CLEANUP_CLOSER = new HTMLMasonTokenType(KEYWORD_CLEANUP_CLOSER);

	IElementType HTML_MASON_PERL_OPENER = new HTMLMasonTokenType(KEYWORD_PERL_OPENER);
	IElementType HTML_MASON_PERL_CLOSER = new HTMLMasonTokenType(KEYWORD_PERL_CLOSER);

	IElementType HTML_MASON_TEXT_OPENER = new HTMLMasonTokenType(KEYWORD_TEXT_OPENER);
	IElementType HTML_MASON_TEXT_CLOSER = new HTMLMasonTokenType(KEYWORD_TEXT_CLOSER);

	IElementType HTML_MASON_FILTER_OPENER = new HTMLMasonTokenType(KEYWORD_FILTER_OPENER);
	IElementType HTML_MASON_FILTER_CLOSER = new HTMLMasonTokenType(KEYWORD_FILTER_CLOSER);

	IElementType HTML_MASON_METHOD_DEFINITION = new HTMLMasonElementType("MASON_METHOD_DEFINITION");
	IElementType HTML_MASON_SUBCOMPONENT_DEFINITION = new HTMLMasonElementType("MASON_DEF_DEFINITION");
	IElementType HTML_MASON_FLAGS_STATEMENT = new HTMLMasonElementType("FLAGS_STATEMENT");
	IElementType HTML_MASON_CALL_STATEMENT = new HTMLMasonElementType("MASON_CALL_STATEMENT");
	IElementType HTML_MASON_ABSTRACT_BLOCK = new HTMLMasonElementType("MASON_ABSTRACT_BLOCK");
	IElementType HTML_MASON_ARGS_BLOCK = new HTMLMasonElementType("MASON_ARGS_BLOCK");
	IElementType HTML_MASON_ATTR_BLOCK = new HTMLMasonElementType("MASON_ATTR_BLOCK");
	IElementType HTML_MASON_TEXT_BLOCK = new HTMLMasonElementType("MASON_TEXT_BLOCK");
}
