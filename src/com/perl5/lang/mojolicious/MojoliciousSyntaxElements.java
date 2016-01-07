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

/**
 * Created by hurricup on 26.12.2015.
 */
public interface MojoliciousSyntaxElements
{
	String KEYWORD_MOJO_BLOCK_OPENER = "<%";
	String KEYWORD_MOJO_BLOCK_CLOSER = "%>";
	String KEYWORD_MOJO_BLOCK_EXPR_OPENER = "<%=";
	String KEYWORD_MOJO_BLOCK_EXPR_ESCAPED_OPENER = "<%==";
	String KEYWORD_MOJO_BLOCK_EXPR_NOSPACE_CLOSER = "=%>";

	String KEYWORD_MOJO_LINE_OPENER = "%";
	String KEYWORD_MOJO_LINE_EXPR_OPENER = "%=";
	String KEYWORD_MOJO_LINE_EXPR_ESCAPED_OPENER = "%==";

	String KEYWORD_MOJO_BLOCK_OPENER_TAG = "<%%";
	String KEYWORD_MOJO_LINE_OPENER_TAG = "%%";

	String KEYWORD_MOJO_BEGIN = "begin";
	String KEYWORD_MOJO_END = "end";

	IElementType MOJO_TEMPLATE_BLOCK_HTML = new MojoliciousTokenType("MOJO_TEMPLATE_BLOCK_HTML");
	IElementType MOJO_OUTER_ELEMENT_TYPE = new MojoliciousElementType("MOJO_OUTER_ELEMENT_TYPE");
	IElementType MOJO_HTML_TEMPLATE_DATA = new TemplateDataElementType("MOJO_HTML_TEMPLATE_DATA", MojoliciousLanguage.INSTANCE, MOJO_TEMPLATE_BLOCK_HTML, MOJO_OUTER_ELEMENT_TYPE);

}
