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

package com.perl5.lang.perl.parser;

import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.embedded.EmbeddedPerlLanguage;
import com.perl5.lang.embedded.psi.EmbeddedPerlElementType;
import com.perl5.lang.embedded.psi.EmbeddedPerlTokenType;
import com.perl5.lang.mason.MasonPerlElementType;
import com.perl5.lang.mason.MasonPerlLanguage;
import com.perl5.lang.mason.MasonPerlTokenType;

/**
 * Created by hurricup on 21.12.2015.
 */
public interface MasonPerlParserExtension
{
	IElementType TEMPLATE_BLOCK_HTML = new MasonPerlTokenType("TEMPLATE_BLOCK_HTML");
	IElementType OUTER_ELEMENT_TYPE = new MasonPerlElementType("OUTER_ELEMENT_TYPE");
	IElementType HTML_TEMPLATE_DATA = new TemplateDataElementType("HTML_TEMPLATE_DATA", MasonPerlLanguage.INSTANCE, TEMPLATE_BLOCK_HTML, OUTER_ELEMENT_TYPE);

	String KEYWORD_BLOCK_OPENER = "<% ";
	IElementType MASON_BLOCK_OPENER = new MasonPerlTokenType(KEYWORD_BLOCK_OPENER);

	String KEYWORD_BLOCK_CLOSER = " %>";
	IElementType MASON_BLOCK_CLOSER = new MasonPerlTokenType(KEYWORD_BLOCK_CLOSER);
	IElementType MASON_TAG_CLOSER = new MasonPerlTokenType(">");

	String KEYWORD_CALL_OPENER = "<& ";
	IElementType MASON_CALL_OPENER = new MasonPerlTokenType(KEYWORD_CALL_OPENER);
	String KEYWORD_CALL_CLOSER = " &>";
	IElementType MASON_CALL_CLOSER = new MasonPerlTokenType(KEYWORD_CALL_CLOSER);

	IElementType MASON_LINE_OPENER = new MasonPerlTokenType("%");
	IElementType MASON_EXPR_FILTER_PIPE = new MasonPerlTokenType("|");

	String KEYWORD_METHOD = "method";
	String KEYWORD_METHOD_OPENER = "<%" + KEYWORD_METHOD;
	String KEYWORD_METHOD_CLOSER = "</%" + KEYWORD_METHOD + ">";
	IElementType MASON_METHOD_OPENER = new MasonPerlTokenType(KEYWORD_METHOD_OPENER);
	IElementType MASON_METHOD_CLOSER = new MasonPerlTokenType(KEYWORD_METHOD_CLOSER);

	String KEYWORD_CLASS = "class";
	String KEYWORD_CLASS_OPENER = "<%" + KEYWORD_CLASS + ">";
	String KEYWORD_CLASS_CLOSER = "</%" + KEYWORD_CLASS + ">";
	IElementType MASON_CLASS_OPENER = new MasonPerlTokenType(KEYWORD_CLASS_OPENER);
	IElementType MASON_CLASS_CLOSER = new MasonPerlTokenType(KEYWORD_CLASS_CLOSER);

	String KEYWORD_DOC = "doc";
	String KEYWORD_DOC_OPENER = "<%" + KEYWORD_DOC + ">";
	String KEYWORD_DOC_CLOSER = "</%" + KEYWORD_DOC + ">";
	IElementType MASON_DOC_OPENER = new MasonPerlTokenType(KEYWORD_DOC_OPENER);
	IElementType MASON_DOC_CLOSER = new MasonPerlTokenType(KEYWORD_DOC_CLOSER);

	String KEYWORD_FLAGS = "flags";
	String KEYWORD_FLAGS_OPENER = "<%" + KEYWORD_FLAGS + ">";
	String KEYWORD_FLAGS_CLOSER = "</%" + KEYWORD_FLAGS + ">";
	IElementType MASON_FLAGS_OPENER = new MasonPerlTokenType(KEYWORD_FLAGS_OPENER);
	IElementType MASON_FLAGS_CLOSER = new MasonPerlTokenType(KEYWORD_FLAGS_CLOSER);

	String KEYWORD_INIT = "init";
	String KEYWORD_INIT_OPENER = "<%" + KEYWORD_INIT + ">";
	String KEYWORD_INIT_CLOSER = "</%" + KEYWORD_INIT + ">";
	IElementType MASON_INIT_OPENER = new MasonPerlTokenType(KEYWORD_INIT_OPENER);
	IElementType MASON_INIT_CLOSER = new MasonPerlTokenType(KEYWORD_INIT_CLOSER);

	String KEYWORD_PERL = "perl";
	String KEYWORD_PERL_OPENER = "<%" + KEYWORD_PERL + ">";
	String KEYWORD_PERL_CLOSER = "</%" + KEYWORD_PERL + ">";
	IElementType MASON_PERL_OPENER = new MasonPerlTokenType(KEYWORD_PERL_OPENER);
	IElementType MASON_PERL_CLOSER = new MasonPerlTokenType(KEYWORD_PERL_CLOSER);

	String KEYWORD_TEXT = "text";
	String KEYWORD_TEXT_OPENER = "<%" + KEYWORD_TEXT + ">";
	String KEYWORD_TEXT_CLOSER = "</%" + KEYWORD_TEXT + ">";
	IElementType MASON_TEXT_OPENER = new MasonPerlTokenType(KEYWORD_TEXT_OPENER);
	IElementType MASON_TEXT_CLOSER = new MasonPerlTokenType(KEYWORD_TEXT_CLOSER);

	String KEYWORD_FILTER = "filter";
	String KEYWORD_FILTER_OPENER = "<%" + KEYWORD_FILTER;
	String KEYWORD_FILTER_CLOSER = "</%" + KEYWORD_FILTER + ">";
	IElementType MASON_FILTER_OPENER = new MasonPerlTokenType(KEYWORD_FILTER_OPENER);
	IElementType MASON_FILTER_CLOSER = new MasonPerlTokenType(KEYWORD_FILTER_CLOSER);

	String KEYWORD_AFTER = "after";
	String KEYWORD_AFTER_OPENER = "<%" + KEYWORD_AFTER;
	String KEYWORD_AFTER_CLOSER = "</%" + KEYWORD_AFTER + ">";
	IElementType MASON_AFTER_OPENER = new MasonPerlTokenType(KEYWORD_AFTER_OPENER);
	IElementType MASON_AFTER_CLOSER = new MasonPerlTokenType(KEYWORD_AFTER_CLOSER);

	String KEYWORD_AUGMENT = "augment";
	String KEYWORD_AUGMENT_OPENER = "<%" + KEYWORD_AUGMENT;
	String KEYWORD_AUGMENT_CLOSER = "</%" + KEYWORD_AUGMENT + ">";
	IElementType MASON_AUGMENT_OPENER = new MasonPerlTokenType(KEYWORD_AUGMENT_OPENER);
	IElementType MASON_AUGMENT_CLOSER = new MasonPerlTokenType(KEYWORD_AUGMENT_CLOSER);

	String KEYWORD_AROUND = "around";
	String KEYWORD_AROUND_OPENER = "<%" + KEYWORD_AROUND;
	String KEYWORD_AROUND_CLOSER = "</%" + KEYWORD_AROUND + ">";
	IElementType MASON_AROUND_OPENER = new MasonPerlTokenType(KEYWORD_AROUND_OPENER);
	IElementType MASON_AROUND_CLOSER = new MasonPerlTokenType(KEYWORD_AROUND_CLOSER);

	String KEYWORD_BEFORE = "before";
	String KEYWORD_BEFORE_OPENER = "<%" + KEYWORD_BEFORE;
	String KEYWORD_BEFORE_CLOSER = "</%" + KEYWORD_BEFORE + ">";
	IElementType MASON_BEFORE_OPENER = new MasonPerlTokenType(KEYWORD_BEFORE_OPENER);
	IElementType MASON_BEFORE_CLOSER = new MasonPerlTokenType(KEYWORD_BEFORE_CLOSER);

	String KEYWORD_OVERRIDE = "override";
	String KEYWORD_OVERRIDE_OPENER = "<%" + KEYWORD_OVERRIDE;
	String KEYWORD_OVERRIDE_CLOSER = "</%" + KEYWORD_OVERRIDE + ">";
	IElementType MASON_OVERRIDE_OPENER = new MasonPerlTokenType(KEYWORD_OVERRIDE_OPENER);
	IElementType MASON_OVERRIDE_CLOSER = new MasonPerlTokenType(KEYWORD_OVERRIDE_CLOSER);
}
