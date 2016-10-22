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

package com.perl5.lang.perl.lexer;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.mojolicious.MojoliciousElementTypes;
import com.perl5.lang.perl.parser.Class.Accessor.ClassAccessorElementTypes;
import com.perl5.lang.perl.parser.moose.MooseElementTypes;
import com.perl5.lang.perl.parser.perlswitch.PerlSwitchElementTypes;
import com.perl5.lang.perl.parser.trycatch.TryCatchElementTypes;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 10.08.2015.
 */
public abstract class PerlBaseLexer extends PerlProtoLexer
		implements PerlElementTypes,
		PerlSwitchElementTypes,
		TryCatchElementTypes,
		ClassAccessorElementTypes,
		MojoliciousElementTypes,
		MooseElementTypes
{
	private static final String BASIC_IDENTIFIER_PATTERN_TEXT = "[_\\p{L}\\d][_\\p{L}\\d]*"; // something strange in Java with unicode props; Added digits to opener for package Encode::KR::2022_KR;
	private static final String PACKAGE_SEPARATOR_PATTERN_TEXT =
			"(?:" +
					"(?:::)+'?" +
					"|" +
					"(?:::)*'" +
					")";
	public static final Pattern AMBIGUOUS_PACKAGE_PATTERN = Pattern.compile(
			"(" +
					PACKAGE_SEPARATOR_PATTERN_TEXT + "?" +        // optional opening separator,
					"(?:" +
					BASIC_IDENTIFIER_PATTERN_TEXT +
					PACKAGE_SEPARATOR_PATTERN_TEXT +
					")*" +
					")" +
					"(" +
					BASIC_IDENTIFIER_PATTERN_TEXT +
					")");

	private static final Map<IElementType, IElementType> SIGILS_TO_TOKENS_MAP = new THashMap<>();

	static
	{
		SIGILS_TO_TOKENS_MAP.put(SIGIL_SCALAR, SCALAR_NAME);
		SIGILS_TO_TOKENS_MAP.put(SIGIL_SCALAR_INDEX, SCALAR_NAME);
		SIGILS_TO_TOKENS_MAP.put(SIGIL_ARRAY, ARRAY_NAME);
		SIGILS_TO_TOKENS_MAP.put(SIGIL_HASH, HASH_NAME);
		SIGILS_TO_TOKENS_MAP.put(SIGIL_CODE, CODE_NAME);
		SIGILS_TO_TOKENS_MAP.put(SIGIL_GLOB, GLOB_NAME);
	}

	// last captured heredoc marker
	protected final Stack<PerlHeredocQueueElement> heredocQueue = new Stack<>();
	protected final PerlBracesStack myBracesStack = new PerlBracesStack();
	protected final PerlBracesStack myBracketsStack = new PerlBracesStack();
	private IElementType myCurrentSigilToken;
	private IElementType myNonLabelTokenType;
	private int myNonLabelState;

	/**
	 * We've met any sigil
	 */
	protected IElementType startUnbracedVariable(@NotNull IElementType sigilToken)
	{
		return startUnbracedVariable(getRealLexicalState(), sigilToken);
	}

	/**
	 * We've met any sigil
	 */
	protected IElementType startUnbracedVariable(int state, @NotNull IElementType sigilToken)
	{
		myCurrentSigilToken = sigilToken;
		yybegin(state);
		pushStateAndBegin(PerlLexer.VARIABLE_UNBRACED);
		return sigilToken;
	}

	/**
	 * We've met one of the $ / [${]
	 */
	protected IElementType processUnbracedScalarSigil()
	{
		myCurrentSigilToken = SIGIL_SCALAR;
		return SIGIL_SCALAR;
	}

	protected IElementType startBracedVariable()
	{
		myBracesStack.push(0);
		yybegin(PerlLexer.VARIABLE_BRACED);
		return getLeftBrace();
	}

	protected IElementType startBracedBlock(int afterState)
	{
		myBracesStack.push(0);
		yybegin(afterState);
		pushStateAndBegin(PerlLexer.YYINITIAL);
		return getLeftBrace();
	}

	protected IElementType getLeftBraceCode()
	{
		return getLeftBrace(LEFT_BRACE_CODE);
	}

	protected IElementType getLeftBrace()
	{
		return getLeftBrace(LEFT_BRACE);
	}

	private IElementType getLeftBrace(IElementType braceType)
	{
		if (!myBracesStack.isEmpty())
		{
			myBracesStack.incLast();
		}
		return braceType;
	}

	@SuppressWarnings("Duplicates")
	protected IElementType getRightBrace(int afterState)
	{
		if (!myBracesStack.isEmpty())
		{
			if (myBracesStack.decLast() == 0)
			{
				myBracesStack.pop();
				popState();
				return RIGHT_BRACE;
			}
		}
		yybegin(afterState);
		return RIGHT_BRACE;
	}

	protected IElementType startBracketedBlock(int afterState)
	{
		myBracketsStack.push(0);
		yybegin(afterState);
		pushStateAndBegin(PerlLexer.YYINITIAL);
		return getLeftBracket();
	}

	protected IElementType getLeftBracket()
	{
		if (!myBracketsStack.isEmpty())
		{
			myBracketsStack.incLast();
		}
		return LEFT_BRACKET;
	}

	@SuppressWarnings("Duplicates")
	protected IElementType getRightBracket(int afterState)
	{
		if (!myBracketsStack.isEmpty())
		{
			if (myBracketsStack.decLast() == 0)
			{
				myBracketsStack.pop();
				popState();
				return RIGHT_BRACKET;
			}
		}
		yybegin(afterState);
		return RIGHT_BRACKET;
	}

	/**
	 * We've met identifier (variable name)
	 */
	@NotNull
	protected IElementType getUnbracedVariableNameToken()
	{
		popState();
		return SIGILS_TO_TOKENS_MAP.get(myCurrentSigilToken);
	}

	@NotNull
	protected IElementType getBracedVariableNameToken()
	{
		yybegin(PerlLexer.YYINITIAL);
		return SIGILS_TO_TOKENS_MAP.get(myCurrentSigilToken);
	}

	@Override
	protected void resetInternals()
	{
		super.resetInternals();
		myBracesStack.clear();
		myBracketsStack.clear();
	}

	protected void checkIfLabel(int newState, IElementType tokenType)
	{
		myNonLabelTokenType = tokenType;
		myNonLabelState = newState;
		yybegin(PerlLexer.LEX_LABEL);
	}

	protected IElementType getLabelToken()
	{
		yypushback(yylength());
		yybegin(PerlLexerGenerated.YYINITIAL);
		return IDENTIFIER;
	}

	protected IElementType getNonLabelToken()
	{
		yypushback(yylength());
		yybegin(myNonLabelState);
		return myNonLabelTokenType;
	}
}
