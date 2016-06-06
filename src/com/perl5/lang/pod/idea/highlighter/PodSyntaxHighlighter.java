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

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.lexer.PodLexerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * Created by hurricup on 21.04.2015.
 */
public class PodSyntaxHighlighter extends SyntaxHighlighterBase implements PodElementTypes
{
	public static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

	public static final TextAttributesKey POD_TAG = createTextAttributesKey("POD_TAG", DefaultLanguageHighlighterColors.DOC_COMMENT_TAG);
	public static final TextAttributesKey POD_TEXT = createTextAttributesKey("POD_TEXT", DefaultLanguageHighlighterColors.DOC_COMMENT);
	public static final TextAttributesKey POD_CODE = createTextAttributesKey("POD_CODE", DefaultLanguageHighlighterColors.DOC_COMMENT);

	public static final HashMap<IElementType, TextAttributesKey[]> attributesMap = new HashMap<IElementType, TextAttributesKey[]>();

	private static final TokenSet POD_TOKENS = TokenSet.create(
			POD_POD,

			POD_HEAD1,
			POD_HEAD2,
			POD_HEAD3,
			POD_HEAD4,

			POD_OVER,
			POD_ITEM,
			POD_BACK,

			POD_BEGIN,
			POD_END,

			POD_FOR,
			POD_ENCODING,

			POD_CUT,
			POD_UNKNOWN

	);

	static
	{
		attributesMap.put(POD_POD, new TextAttributesKey[]{PodSyntaxHighlighter.POD_TAG});
//		ATTRIBUTES_MAP.put(PodElementTypes.POD_TEXT, new TextAttributesKey[]{PodSyntaxHighlighter.POD_TEXT});
		attributesMap.put(PodElementTypes.POD_NEWLINE, new TextAttributesKey[]{PodSyntaxHighlighter.POD_TEXT});
		attributesMap.put(PodElementTypes.POD_CODE, new TextAttributesKey[]{PodSyntaxHighlighter.POD_CODE});
	}

	private final Project myProject;

	public PodSyntaxHighlighter(Project myProject)
	{
		this.myProject = myProject;
	}

	@NotNull
	@Override
	public Lexer getHighlightingLexer()
	{
		return new PodLexerAdapter(myProject);
	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType)
	{
		if (POD_TOKENS.contains(tokenType))
		{
			return attributesMap.get(POD_POD);
		}
		else if (attributesMap.containsKey(tokenType))
		{
			return attributesMap.get(tokenType);
		}

		return EMPTY_KEYS;
	}
}
