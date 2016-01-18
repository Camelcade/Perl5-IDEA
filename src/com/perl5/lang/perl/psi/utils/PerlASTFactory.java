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

package com.perl5.lang.perl.psi.utils;

import com.intellij.lang.DefaultASTFactoryImpl;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexerUtil;
import com.perl5.lang.perl.psi.impl.*;
import org.jetbrains.annotations.NotNull;

public class PerlASTFactory extends DefaultASTFactoryImpl implements PerlElementTypes
{
	@Override
	public LeafElement createComment(@NotNull IElementType type, CharSequence text)
	{
		if (type == HEREDOC_END)
			return new PerlHeredocTerminatorElementImpl(type, text);
		else
			return super.createComment(type, text);
	}

	@NotNull
	@Override
	public LeafElement createLeaf(@NotNull IElementType type, CharSequence text)
	{
		if (PerlLexerUtil.STRING_CONTENT_TOKENS.contains(type))
			return new PerlStringContentElementImpl(type, text);
		else if (type == VARIABLE_NAME)
			return new PerlVariableNameElementImpl(type, text);
		else if (type == SUB)
			return new PerlSubNameElementImpl(type, text);
		else if (type == PACKAGE)
			return new PerlNamespaceElementImpl(type, text);
		else if (type == VERSION_ELEMENT)
			return new PerlVersionElementImpl(type, text);
		else if (type == LABEL)
			return new PerlGotoLabelElementImpl(type, text);
		else if (type == POD)
			return super.createComment(type, text);
		else
			return super.createLeaf(type, text);
	}

}
