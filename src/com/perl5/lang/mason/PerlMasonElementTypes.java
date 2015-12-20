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

package com.perl5.lang.mason;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.PerlTokenType;

/**
 * Created by hurricup on 20.12.2015.
 */
public interface PerlMasonElementTypes
{
	IElementType MASON_BLOCK_OPENER = new PerlTokenType("<%");
	IElementType MASON_BLOCK_CLOSER = new PerlTokenType("%>");
	IElementType MASON_TAG_CLOSER = new PerlTokenType(">");

	IElementType MASON_CALL_OPENER = new PerlTokenType("<&");
	IElementType MASON_CALL_CLOSER = new PerlTokenType("&>");

	IElementType MASON_LINE_OPENER = new PerlTokenType("%");

	IElementType MASON_CLASS_OPENER = new PerlTokenType("<%class>");
	IElementType MASON_CLASS_CLOSER = new PerlTokenType("</%class>");

	IElementType MASON_DOC_OPENER = new PerlTokenType("<%doc>");
	IElementType MASON_DOC_CLOSER = new PerlTokenType("</%doc>");

	IElementType MASON_FLAGS_OPENER = new PerlTokenType("<%flags>");
	IElementType MASON_FLAGS_CLOSER = new PerlTokenType("</%flags>");

	IElementType MASON_INIT_OPENER = new PerlTokenType("<%init>");
	IElementType MASON_INIT_CLOSER = new PerlTokenType("</%init>");

	IElementType MASON_PERL_OPENER = new PerlTokenType("<%perl>");
	IElementType MASON_PERL_CLOSER = new PerlTokenType("</%perl>");

	IElementType MASON_TEXT_OPENER = new PerlTokenType("<%text>");
	IElementType MASON_TEXT_CLOSER = new PerlTokenType("</%text>");

	IElementType MASON_METHOD_OPENER = new PerlTokenType("<%method");
	IElementType MASON_METHOD_CLOSER = new PerlTokenType("</%method");

	IElementType MASON_FILTER_OPENER = new PerlTokenType("<%filter");
	IElementType MASON_FILTER_CLOSER = new PerlTokenType("</%filter");

	IElementType MASON_AFTER_OPENER = new PerlTokenType("<%after");
	IElementType MASON_AFTER_CLOSER = new PerlTokenType("</%after");

	IElementType MASON_AUGMENT_OPENER = new PerlTokenType("<%augment");
	IElementType MASON_AUGMENT_CLOSER = new PerlTokenType("</%augment");

	IElementType MASON_AROUND_OPENER = new PerlTokenType("<%around");
	IElementType MASON_AROUND_CLOSER = new PerlTokenType("</%around");

	IElementType MASON_BEFORE_OPENER = new PerlTokenType("<%before");
	IElementType MASON_BEFORE_CLOSER = new PerlTokenType("</%before>");

	IElementType MASON_OVERRIDE_OPENER = new PerlTokenType("<%override");
	IElementType MASON_OVERRIDE_CLOSER = new PerlTokenType("</%override>");
}
