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

package com.perl5.lang.htmlmason.idea.configuration;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;

/**
 * Created by hurricup on 11.03.2016.
 */
enum HTMLMasonCustomTagRole implements HTMLMasonElementTypes
{
	PERL("as <%perl>", true, HTML_MASON_PERL_OPENER, HTML_MASON_PERL_CLOSER),
	METHOD("as <%method>", false, HTML_MASON_METHOD_OPENER, HTML_MASON_METHOD_CLOSER),
	DEF("as <%def>", false, HTML_MASON_DEF_OPENER, HTML_MASON_DEF_CLOSER),
	ARGS("as <%args>", true, HTML_MASON_ARGS_OPENER, HTML_MASON_ARGS_CLOSER);

	private final String myTitle;
	private final IElementType myOpenToken;
	private final IElementType myCloseToken;
	private final boolean myIsSimple;

	HTMLMasonCustomTagRole(String title, boolean isSimple, IElementType openToken, IElementType closeToken)
	{
		myTitle = title;
		myOpenToken = openToken;
		myCloseToken = closeToken;
		myIsSimple = isSimple;
	}

	public String getTitle()
	{
		return myTitle;
	}

	public IElementType getOpenToken()
	{
		return myOpenToken;
	}

	public IElementType getCloseToken()
	{
		return myCloseToken;
	}

	public boolean isSimple()
	{
		return myIsSimple;
	}
}
