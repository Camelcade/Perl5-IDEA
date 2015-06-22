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
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.PerlTokenType;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.lexer.PodLexerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * Created by hurricup on 21.04.2015.
 *
 */
public class PodSyntaxHighlighter  extends SyntaxHighlighterBase
{
	public static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

	public static final TextAttributesKey POD_TAG = createTextAttributesKey("POD_TAG", DefaultLanguageHighlighterColors.DOC_COMMENT_TAG);
	public static final TextAttributesKey POD_TEXT = createTextAttributesKey("POD_TEXT", DefaultLanguageHighlighterColors.DOC_COMMENT);
	public static final TextAttributesKey POD_CODE = createTextAttributesKey("POD_CODE", DefaultLanguageHighlighterColors.DOC_COMMENT);

	public static final HashMap<IElementType,TextAttributesKey[]> attributesMap = new HashMap<IElementType,TextAttributesKey[]>();

//	private final PerlSyntaxHighlighter PERL_SYNTAX_HIGHLIGHTER;

	static{
		attributesMap.put(PodElementTypes.POD_TAG, new TextAttributesKey[]{PodSyntaxHighlighter.POD_TAG});
		attributesMap.put(PodElementTypes.POD_TEXT, new TextAttributesKey[]{PodSyntaxHighlighter.POD_TEXT});
		attributesMap.put(PodElementTypes.POD_NEWLINE, new TextAttributesKey[]{PodSyntaxHighlighter.POD_TEXT});
		attributesMap.put(PodElementTypes.POD_CODE, new TextAttributesKey[]{PodSyntaxHighlighter.POD_CODE});
	}

	Project myProject;

	public PodSyntaxHighlighter(Project myProject)
	{
		this.myProject = myProject;

//		PERL_SYNTAX_HIGHLIGHTER = new PerlSyntaxHighlighter(myProject);
	}

	@NotNull
	@Override
	public Lexer getHighlightingLexer() {
		return new PodLexerAdapter(myProject);
	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {

//		if( tokenType instanceof PerlTokenType)
//			return PERL_SYNTAX_HIGHLIGHTER.getTokenHighlights(tokenType);
//		else
		if(attributesMap.containsKey(tokenType))
			return attributesMap.get(tokenType);

		return EMPTY_KEYS;
	}
}
