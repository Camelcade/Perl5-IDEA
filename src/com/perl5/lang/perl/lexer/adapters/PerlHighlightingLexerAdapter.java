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

package com.perl5.lang.perl.lexer.adapters;

import com.intellij.lexer.LayeredLexer;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.pod.lexer.PodLexerAdapter;

/**
 * Created by hurricup on 15.10.2016.
 * fixme we need a lazy layered adapter
 */
public class PerlHighlightingLexerAdapter extends LayeredLexer implements PerlElementTypes
{
	public PerlHighlightingLexerAdapter()
	{
		super(new PerlLexerAdapter());
		registerSelfStoppingLayer(
				new PodLexerAdapter(),
				new IElementType[]{POD},
				IElementType.EMPTY_ARRAY
		);
		registerSelfStoppingLayer(
				PerlSubLexerAdapter.forStringSQ(),
				new IElementType[]{HEREDOC},
				IElementType.EMPTY_ARRAY
		);
		registerSelfStoppingLayer(
				PerlSubLexerAdapter.forCode(),
				new IElementType[]{LP_CODE_BLOCK},
				IElementType.EMPTY_ARRAY
		);
		registerSelfStoppingLayer(
				PerlSubLexerAdapter.forStringDQ(),
				new IElementType[]{HEREDOC_QQ},
				IElementType.EMPTY_ARRAY
		);
		registerSelfStoppingLayer(
				PerlSubLexerAdapter.forStringXQ(),
				new IElementType[]{HEREDOC_QX},
				IElementType.EMPTY_ARRAY
		);

	}
}