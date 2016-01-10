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

package com.perl5.lang.mason.idea.findusages;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordOccurrence;
import com.intellij.lexer.Lexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.Processor;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexerAdapter;

/**
 * Created by hurricup on 10.01.2016.
 * This class been made to avoid thread-unsafe cacheBuilder
 */
public class MasonPurePerlWordsScanner extends DefaultWordsScanner implements PerlElementTypes
{
	private static final TokenSet myIdentifierTokenSet = PerlParserDefinition.IDENTIFIERS;
	private static final TokenSet myCommentTokenSet = TokenSet.orSet(PerlParserDefinition.COMMENTS, TokenSet.create(POD));
	private static final TokenSet myLiteralTokenSet = PerlParserDefinition.LITERALS;
	private static final TokenSet mySkipCodeContextTokenSet = TokenSet.EMPTY;
	private static final boolean myMayHaveFileRefsInLiterals = true;

	public MasonPurePerlWordsScanner()
	{
		super(null,
				myIdentifierTokenSet,
				myCommentTokenSet,
				myLiteralTokenSet
		);
		setMayHaveFileRefsInLiterals(myMayHaveFileRefsInLiterals);
	}

	@Override
	public void processWords(CharSequence fileText, Processor<WordOccurrence> processor)
	{
		Lexer myLexer = getLexer();
		myLexer.start(fileText);
		WordOccurrence occurrence = new WordOccurrence(fileText, 0, 0, null); // shared occurrence

		IElementType type;
		while ((type = myLexer.getTokenType()) != null)
		{
			if (myIdentifierTokenSet.contains(type))
			{
				//occurrence.init(fileText, myLexer.getTokenStart(), myLexer.getTokenEnd(), WordOccurrence.Kind.CODE);
				//if (!processor.process(occurrence)) return;
				if (!stripWords(processor, fileText, myLexer.getTokenStart(), myLexer.getTokenEnd(), WordOccurrence.Kind.CODE, occurrence, false))
					return;
			}
			else if (myCommentTokenSet.contains(type))
			{
				if (!stripWords(processor, fileText, myLexer.getTokenStart(), myLexer.getTokenEnd(), WordOccurrence.Kind.COMMENTS, occurrence, false))
					return;
			}
			else if (myLiteralTokenSet.contains(type))
			{
				if (!stripWords(processor, fileText, myLexer.getTokenStart(), myLexer.getTokenEnd(), WordOccurrence.Kind.LITERALS, occurrence, myMayHaveFileRefsInLiterals))
					return;
			}
			else if (!mySkipCodeContextTokenSet.contains(type))
			{
				if (!stripWords(processor, fileText, myLexer.getTokenStart(), myLexer.getTokenEnd(), WordOccurrence.Kind.CODE, occurrence, false))
					return;
			}
			myLexer.advance();
		}
	}

	protected Lexer getLexer()
	{
		return new PerlLexerAdapter(null);
	}
}
