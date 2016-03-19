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

package com.perl5.lang.htmlmason;

/**
 * Created by hurricup on 05.03.2016.
 */
public interface HTMLMasonSyntaxElements
{
	String COMPONENT_SLUG_SELF = "SELF";
	String COMPONENT_SLUG_PARENT = "PARENT";
	String COMPONENT_SLUG_REQUEST = "REQUEST";

	String KEYWORD_BLOCK_OPENER = "<%";
	String KEYWORD_BLOCK_CLOSER = "%>";

	String KEYWORD_CALL_OPENER = "<&";
	String KEYWORD_CALL_OPENER_FILTER = "<&|";

	String KEYWORD_CALL_CLOSER = "&>";

	String KEYWORD_CALL_CLOSE_TAG_START = "</&";
	String KEYWORD_TAG_CLOSER = ">";

	String KEYWORD_PERL = "perl";
	String KEYWORD_PERL_OPENER_UNCLOSED = "<%" + KEYWORD_PERL;
	String KEYWORD_PERL_OPENER = KEYWORD_PERL_OPENER_UNCLOSED + ">";
	String KEYWORD_PERL_CLOSER = "</%" + KEYWORD_PERL + ">";

	String KEYWORD_INIT = "init";
	String KEYWORD_INIT_OPENER_UNCLOSED = "<%" + KEYWORD_INIT;
	String KEYWORD_INIT_OPENER = KEYWORD_INIT_OPENER_UNCLOSED + ">";
	String KEYWORD_INIT_CLOSER = "</%" + KEYWORD_INIT + ">";

	String KEYWORD_CLEANUP = "cleanup";
	String KEYWORD_CLEANUP_OPENER_UNCLOSED = "<%" + KEYWORD_CLEANUP;
	String KEYWORD_CLEANUP_OPENER = KEYWORD_CLEANUP_OPENER_UNCLOSED + ">";
	String KEYWORD_CLEANUP_CLOSER = "</%" + KEYWORD_CLEANUP + ">";

	String KEYWORD_ONCE = "once";
	String KEYWORD_ONCE_OPENER_UNCLOSED = "<%" + KEYWORD_ONCE;
	String KEYWORD_ONCE_OPENER = KEYWORD_ONCE_OPENER_UNCLOSED + ">";
	String KEYWORD_ONCE_CLOSER = "</%" + KEYWORD_ONCE + ">";

	String KEYWORD_SHARED = "shared";
	String KEYWORD_SHARED_OPENER_UNCLOSED = "<%" + KEYWORD_SHARED;
	String KEYWORD_SHARED_OPENER = KEYWORD_SHARED_OPENER_UNCLOSED + ">";
	String KEYWORD_SHARED_CLOSER = "</%" + KEYWORD_SHARED + ">";

	String KEYWORD_METHOD = "method";
	String KEYWORD_METHOD_OPENER = "<%" + KEYWORD_METHOD;
	String KEYWORD_METHOD_CLOSER = "</%" + KEYWORD_METHOD + ">";

	String KEYWORD_DEF = "def";
	String KEYWORD_DEF_OPENER = "<%" + KEYWORD_DEF;
	String KEYWORD_DEF_CLOSER = "</%" + KEYWORD_DEF + ">";

	String KEYWORD_FLAGS = "flags";
	String KEYWORD_FLAGS_OPENER_UNCLOSED = "<%" + KEYWORD_FLAGS;
	String KEYWORD_FLAGS_OPENER = KEYWORD_FLAGS_OPENER_UNCLOSED + ">";
	String KEYWORD_FLAGS_CLOSER = "</%" + KEYWORD_FLAGS + ">";

	String KEYWORD_ATTR = "attr";
	String KEYWORD_ATTR_OPENER_UNCLOSED = "<%" + KEYWORD_ATTR;
	String KEYWORD_ATTR_OPENER = KEYWORD_ATTR_OPENER_UNCLOSED + ">";
	String KEYWORD_ATTR_CLOSER = "</%" + KEYWORD_ATTR + ">";

	String KEYWORD_ARGS = "args";
	String KEYWORD_ARGS_OPENER_UNCLOSED = "<%" + KEYWORD_ARGS;
	String KEYWORD_ARGS_OPENER = KEYWORD_ARGS_OPENER_UNCLOSED + ">";
	String KEYWORD_ARGS_CLOSER = "</%" + KEYWORD_ARGS + ">";

	String KEYWORD_FILTER = "filter";
	String KEYWORD_FILTER_OPENER_UNCLOSED = "<%" + KEYWORD_FILTER;
	String KEYWORD_FILTER_OPENER = KEYWORD_FILTER_OPENER_UNCLOSED + ">";
	String KEYWORD_FILTER_CLOSER = "</%" + KEYWORD_FILTER + ">";

	String KEYWORD_TEXT = "text";
	String KEYWORD_TEXT_OPENER_UNCLOSED = "<%" + KEYWORD_TEXT;
	String KEYWORD_TEXT_OPENER = KEYWORD_TEXT_OPENER_UNCLOSED + ">";
	String KEYWORD_TEXT_CLOSER = "</%" + KEYWORD_TEXT + ">";

	String KEYWORD_DOC = "doc";
	String KEYWORD_DOC_OPENER_UNCLOSED = "<%" + KEYWORD_DOC;
	String KEYWORD_DOC_OPENER = KEYWORD_DOC_OPENER_UNCLOSED + ">";
	String KEYWORD_DOC_CLOSER = "</%" + KEYWORD_DOC + ">";
}
