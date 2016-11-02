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

package com.perl5.lang.mason2.lexer;

import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.mason2.elementType.Mason2ElementTypes;
import com.perl5.lang.perl.lexer.adapters.PerlMergingLexerAdapter;
import com.perl5.lang.perl.lexer.adapters.PerlTemplatingMergingLexerAdapter;

/**
 * Created by hurricup on 20.12.2015.
 */
public class Mason2TemplatingLexerAdapter extends PerlTemplatingMergingLexerAdapter implements Mason2ElementTypes
{
	private final static TokenSet TOKENS_TO_MERGE = TokenSet.orSet(
			PerlMergingLexerAdapter.TOKENS_TO_MERGE,
			TokenSet.create(MASON_TEMPLATE_BLOCK_HTML)
	);

	public Mason2TemplatingLexerAdapter(Project project)
	{
		super(project, new Mason2TemplatingLexer(null).withProject(project), TOKENS_TO_MERGE);
	}
}
