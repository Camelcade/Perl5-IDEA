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
import com.perl5.lang.mason.MasonLanguage;
import com.perl5.lang.mason.MasonSyntaxElements;

/**
 * Created by hurricup on 21.12.2015.
 */
public interface MasonElementTypes extends MasonSyntaxElements
{
	IElementType MASON_TEMPLATE_BLOCK_HTML = new MasonTokenType("MASON_TEMPLATE_BLOCK_HTML");
	IElementType MASON_OUTER_ELEMENT_TYPE = new MasonElementType("MASON_OUTER_ELEMENT_TYPE");
	IElementType MASON_HTML_TEMPLATE_DATA = new TemplateDataElementType("MASON_HTML_TEMPLATE_DATA", MasonLanguage.INSTANCE, MASON_TEMPLATE_BLOCK_HTML, MASON_OUTER_ELEMENT_TYPE);

	IElementType MASON_FILTERED_BLOCK_OPENER = new MasonTokenType(KEYWORD_FILTERED_BLOCK_OPENER);
	IElementType MASON_FILTERED_BLOCK_CLOSER = new MasonTokenType(KEYWORD_FILTERED_BLOCK_CLOSER);
	IElementType MASON_SELF_POINTER = new MasonTokenType(KEYWORD_SELF_POINTER);

	IElementType MASON_BLOCK_OPENER = new MasonTokenType(KEYWORD_BLOCK_OPENER);

	IElementType MASON_BLOCK_CLOSER = new MasonTokenType(KEYWORD_BLOCK_CLOSER);
	IElementType MASON_TAG_CLOSER = new MasonTokenType(">");

	IElementType MASON_CALL_OPENER = new MasonTokenType(KEYWORD_CALL_OPENER);
	IElementType MASON_CALL_CLOSER = new MasonTokenType(KEYWORD_CALL_CLOSER);

	IElementType MASON_LINE_OPENER = new MasonTokenType("%");
	IElementType MASON_EXPR_FILTER_PIPE = new MasonTokenType("|");

	IElementType MASON_METHOD_OPENER = new MasonTokenType(KEYWORD_METHOD_OPENER);
	IElementType MASON_METHOD_CLOSER = new MasonTokenType(KEYWORD_METHOD_CLOSER);

	IElementType MASON_CLASS_OPENER = new MasonTokenType(KEYWORD_CLASS_OPENER);
	IElementType MASON_CLASS_CLOSER = new MasonTokenType(KEYWORD_CLASS_CLOSER);

	IElementType MASON_DOC_OPENER = new MasonTokenType(KEYWORD_DOC_OPENER);
	IElementType MASON_DOC_CLOSER = new MasonTokenType(KEYWORD_DOC_CLOSER);

	IElementType MASON_FLAGS_OPENER = new MasonTokenType(KEYWORD_FLAGS_OPENER);
	IElementType MASON_FLAGS_CLOSER = new MasonTokenType(KEYWORD_FLAGS_CLOSER);

	IElementType MASON_INIT_OPENER = new MasonTokenType(KEYWORD_INIT_OPENER);
	IElementType MASON_INIT_CLOSER = new MasonTokenType(KEYWORD_INIT_CLOSER);

	IElementType MASON_PERL_OPENER = new MasonTokenType(KEYWORD_PERL_OPENER);
	IElementType MASON_PERL_CLOSER = new MasonTokenType(KEYWORD_PERL_CLOSER);

	IElementType MASON_TEXT_OPENER = new MasonTokenType(KEYWORD_TEXT_OPENER);
	IElementType MASON_TEXT_CLOSER = new MasonTokenType(KEYWORD_TEXT_CLOSER);

	IElementType MASON_FILTER_OPENER = new MasonTokenType(KEYWORD_FILTER_OPENER);
	IElementType MASON_FILTER_CLOSER = new MasonTokenType(KEYWORD_FILTER_CLOSER);

	IElementType MASON_AFTER_OPENER = new MasonTokenType(KEYWORD_AFTER_OPENER);
	IElementType MASON_AFTER_CLOSER = new MasonTokenType(KEYWORD_AFTER_CLOSER);

	IElementType MASON_AUGMENT_OPENER = new MasonTokenType(KEYWORD_AUGMENT_OPENER);
	IElementType MASON_AUGMENT_CLOSER = new MasonTokenType(KEYWORD_AUGMENT_CLOSER);

	IElementType MASON_AROUND_OPENER = new MasonTokenType(KEYWORD_AROUND_OPENER);
	IElementType MASON_AROUND_CLOSER = new MasonTokenType(KEYWORD_AROUND_CLOSER);

	IElementType MASON_BEFORE_OPENER = new MasonTokenType(KEYWORD_BEFORE_OPENER);
	IElementType MASON_BEFORE_CLOSER = new MasonTokenType(KEYWORD_BEFORE_CLOSER);

	IElementType MASON_OVERRIDE_OPENER = new MasonTokenType(KEYWORD_OVERRIDE_OPENER);
	IElementType MASON_OVERRIDE_CLOSER = new MasonTokenType(KEYWORD_OVERRIDE_CLOSER);

	IElementType MASON_FLAGS_STATEMENT = new MasonElementType("FLAGS_STATEMENT");

	IElementType MASON_NAMESPACE_DEFINITION = new MasonNamespaceElementType("MASON_PACKAGE");
	IElementType MASON_OVERRIDE_DEFINITION = new MasonOverrideStubElementType("MASON_OVERRIDE_DEFINITION");
	IElementType MASON_METHOD_DEFINITION = new MasonMethodDefinitionStubElementType("MASON_METHOD_DEFINITION");
	IElementType MASON_FILTER_DEFINITION = new MasonFilterDefinitionStubElementType("MASON_FILTER_DEFINITION");

	IElementType MASON_AROUND_MODIFIER = new MasonElementType("MASON_AROUND_MODIFIER");
	IElementType MASON_AFTER_MODIFIER = new MasonElementType("MASON_AFTER_MODIFIER");
	IElementType MASON_AUGMENT_MODIFIER = new MasonElementType("MASON_AUGMENT_MODIFIER");
	IElementType MASON_BEFORE_MODIFIER = new MasonElementType("MASON_BEFOE_MODIFIER");

	IElementType MASON_ABSTRACT_BLOCK = new MasonElementType("MASON_ABSTRACT_BLOCK");
	IElementType MASON_TEXT_BLOCK = new MasonElementType("MASON_TEXT_BLOCK");
	IElementType MASON_FILTERED_BLOCK = new MasonElementType("MASON_FILTERED_BLOCK");
	IElementType MASON_SIMPLE_DEREF_EXPR = new MasonElementType("MASON_DEREF_EXPRESSION");
}
