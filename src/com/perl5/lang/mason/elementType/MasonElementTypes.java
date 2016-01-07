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

package com.perl5.lang.mason.elementType;

import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.mason.MasonPerlLanguage;
import com.perl5.lang.mason.MasonPerlSyntaxElements;

/**
 * Created by hurricup on 21.12.2015.
 */
public interface MasonElementTypes extends MasonPerlSyntaxElements
{
	IElementType MASON_TEMPLATE_BLOCK_HTML = new MasonPerlTokenType("MASON_TEMPLATE_BLOCK_HTML");
	IElementType MASON_OUTER_ELEMENT_TYPE = new MasonPerlElementType("MASON_OUTER_ELEMENT_TYPE");
	IElementType MASON_HTML_TEMPLATE_DATA = new TemplateDataElementType("MASON_HTML_TEMPLATE_DATA", MasonPerlLanguage.INSTANCE, MASON_TEMPLATE_BLOCK_HTML, MASON_OUTER_ELEMENT_TYPE);

	IElementType MASON_BLOCK_OPENER = new MasonPerlTokenType(KEYWORD_BLOCK_OPENER);

	IElementType MASON_BLOCK_CLOSER = new MasonPerlTokenType(KEYWORD_BLOCK_CLOSER);
	IElementType MASON_TAG_CLOSER = new MasonPerlTokenType(">");

	IElementType MASON_CALL_OPENER = new MasonPerlTokenType(KEYWORD_CALL_OPENER);
	IElementType MASON_CALL_CLOSER = new MasonPerlTokenType(KEYWORD_CALL_CLOSER);

	IElementType MASON_LINE_OPENER = new MasonPerlTokenType("%");
	IElementType MASON_EXPR_FILTER_PIPE = new MasonPerlTokenType("|");

	IElementType MASON_METHOD_OPENER = new MasonPerlTokenType(KEYWORD_METHOD_OPENER);
	IElementType MASON_METHOD_CLOSER = new MasonPerlTokenType(KEYWORD_METHOD_CLOSER);

	IElementType MASON_CLASS_OPENER = new MasonPerlTokenType(KEYWORD_CLASS_OPENER);
	IElementType MASON_CLASS_CLOSER = new MasonPerlTokenType(KEYWORD_CLASS_CLOSER);

	IElementType MASON_DOC_OPENER = new MasonPerlTokenType(KEYWORD_DOC_OPENER);
	IElementType MASON_DOC_CLOSER = new MasonPerlTokenType(KEYWORD_DOC_CLOSER);

	IElementType MASON_FLAGS_OPENER = new MasonPerlTokenType(KEYWORD_FLAGS_OPENER);
	IElementType MASON_FLAGS_CLOSER = new MasonPerlTokenType(KEYWORD_FLAGS_CLOSER);

	IElementType MASON_INIT_OPENER = new MasonPerlTokenType(KEYWORD_INIT_OPENER);
	IElementType MASON_INIT_CLOSER = new MasonPerlTokenType(KEYWORD_INIT_CLOSER);

	IElementType MASON_PERL_OPENER = new MasonPerlTokenType(KEYWORD_PERL_OPENER);
	IElementType MASON_PERL_CLOSER = new MasonPerlTokenType(KEYWORD_PERL_CLOSER);

	IElementType MASON_TEXT_OPENER = new MasonPerlTokenType(KEYWORD_TEXT_OPENER);
	IElementType MASON_TEXT_CLOSER = new MasonPerlTokenType(KEYWORD_TEXT_CLOSER);

	IElementType MASON_FILTER_OPENER = new MasonPerlTokenType(KEYWORD_FILTER_OPENER);
	IElementType MASON_FILTER_CLOSER = new MasonPerlTokenType(KEYWORD_FILTER_CLOSER);

	IElementType MASON_AFTER_OPENER = new MasonPerlTokenType(KEYWORD_AFTER_OPENER);
	IElementType MASON_AFTER_CLOSER = new MasonPerlTokenType(KEYWORD_AFTER_CLOSER);

	IElementType MASON_AUGMENT_OPENER = new MasonPerlTokenType(KEYWORD_AUGMENT_OPENER);
	IElementType MASON_AUGMENT_CLOSER = new MasonPerlTokenType(KEYWORD_AUGMENT_CLOSER);

	IElementType MASON_AROUND_OPENER = new MasonPerlTokenType(KEYWORD_AROUND_OPENER);
	IElementType MASON_AROUND_CLOSER = new MasonPerlTokenType(KEYWORD_AROUND_CLOSER);

	IElementType MASON_BEFORE_OPENER = new MasonPerlTokenType(KEYWORD_BEFORE_OPENER);
	IElementType MASON_BEFORE_CLOSER = new MasonPerlTokenType(KEYWORD_BEFORE_CLOSER);

	IElementType MASON_OVERRIDE_OPENER = new MasonPerlTokenType(KEYWORD_OVERRIDE_OPENER);
	IElementType MASON_OVERRIDE_CLOSER = new MasonPerlTokenType(KEYWORD_OVERRIDE_CLOSER);

	IElementType MASON_FLAGS_STATEMENT = new MasonPerlElementType("FLAGS_STATEMENT");
	IElementType MASON_OVERRIDE_STATEMENT = new MasonPerlOverrideStubElementType(KEYWORD_OVERRIDE);
	IElementType MASON_NAMESPACE_DEFINITION = new MasonNamespaceElementType("MASON_PACKAGE");
}
