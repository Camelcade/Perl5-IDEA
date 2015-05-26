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

package com.perl5.lang.pod.idea.highlighter;

import com.intellij.lexer.LayeredLexer;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.idea.highlighter.PerlHighlightningLexer;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.lexer.PodLexerAdapter;

/**
 * Created by hurricup on 24.04.2015.
 */
public class PodHighlightingLexer extends LayeredLexer
{
	public PodHighlightingLexer()
	{
		super(new PodLexerAdapter());

		registerSelfStoppingLayer(
			new PerlHighlightningLexer(false),
			new IElementType[]{PodElementTypes.POD_CODE},
			IElementType.EMPTY_ARRAY
		);
	}

}
