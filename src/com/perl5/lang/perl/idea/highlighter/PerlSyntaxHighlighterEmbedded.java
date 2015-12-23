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

package com.perl5.lang.perl.idea.highlighter;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 23.12.2015.
 */
public abstract class PerlSyntaxHighlighterEmbedded extends PerlSyntaxHighlighter
{
	public PerlSyntaxHighlighterEmbedded(Project project)
	{
		super(project);
	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType)
	{
		if( getMarkersTokenSet().contains(tokenType))
		{
			return PerlSyntaxHighlighter.EMBED_MARKER_KEYS;
		}
		return new TextAttributesKey[0];
	}

	public abstract TokenSet getMarkersTokenSet();
}
