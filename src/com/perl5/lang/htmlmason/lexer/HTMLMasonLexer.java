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

package com.perl5.lang.htmlmason.lexer;

import com.intellij.openapi.project.Project;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import gnu.trove.THashMap;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 05.03.2016.
 */
@SuppressWarnings("Duplicates")
public class HTMLMasonLexer extends AbstractMasonLexer implements HTMLMasonElementTypes
{
	public static final Pattern MASON_EXPRESSION_FILTER_BLOCK = Pattern.compile(
			"\\|\\s*"
					+ "(?:" + IDENTIFIER_PATTERN + "\\s*,\\s*" + ")*"
					+ IDENTIFIER_PATTERN
					+ "\\s*" + KEYWORD_BLOCK_CLOSER
	);
	public static final Pattern HTML_MASON_SIMPLE_OPENERS = Pattern.compile(
			"<%(" +
					KEYWORD_PERL + "|" +
					KEYWORD_INIT + "|" +
					KEYWORD_CLEANUP + "|" +
					KEYWORD_ONCE + "|" +
					KEYWORD_SHARED + "|" +
					KEYWORD_FLAGS + "|" +
					KEYWORD_ATTR + "|" +
					KEYWORD_ARGS + "|" +
					KEYWORD_FILTER + "|" +
					KEYWORD_DOC + "|" +
					KEYWORD_TEXT +
					")>"
	);
	public static final Pattern HTML_MASON_OPENERS = Pattern.compile(
			"<%(" +
					KEYWORD_METHOD + "|" +
					KEYWORD_DEF +
					")"
	);
	public static final Pattern HTML_MASON_CLOSERS = Pattern.compile(
			"</%(" +
					KEYWORD_METHOD + "|" +
					KEYWORD_DEF +
					")>"
	);
	public static final int LEX_MASON_HTML_BLOCK = LEX_CUSTOM1;             // template block
	public static final int LEX_MASON_PERL_BLOCK = LEX_CUSTOM2;             // complicated blocks <%kw>...</%kw>
	public static final int LEX_MASON_PERL_ARGS_BLOCK = LEX_CUSTOM8;        // <%args> block
	public static final int LEX_MASON_PERL_ATTR_BLOCK = LEX_CUSTOM9;        // <%attr> block
	public static final int LEX_MASON_PERL_LINE = LEX_CUSTOM3;              // % ...
	public static final int LEX_MASON_PERL_EXPR_BLOCK = LEX_CUSTOM4;        // <% ... | id1, id2, ... %>
	public static final int LEX_MASON_PERL_EXPR_FILTER_BLOCK = LEX_CUSTOM5; // | id1, id2, ... %> // same as above, but after pipe
	public static final int LEX_MASON_PERL_CALL_BLOCK = LEX_CUSTOM6;        // <& ... &>
	public static final int LEX_MASON_PERL_FILTERING_CALL_BLOCK = LEX_CUSTOM10; // <&| ... &>
	public static final int LEX_MASON_OPENING_TAG = LEX_CUSTOM7;            // lexing tag additional info
	protected static final Pattern DEFAULT_ESCAPERS_MERGED = Pattern.compile("[unh]+");
	private static final Map<String, IElementType> OPEN_TOKENS_MAP = new THashMap<String, IElementType>();
	private static final Map<String, String> OPEN_CLOSE_MAP = new THashMap<String, String>();
	private static final Map<String, IElementType> CLOSE_TOKENS_MAP = new THashMap<String, IElementType>();

	static
	{
		OPEN_TOKENS_MAP.put(KEYWORD_PERL_OPENER, HTML_MASON_PERL_OPENER);
		OPEN_TOKENS_MAP.put(KEYWORD_INIT_OPENER, HTML_MASON_INIT_OPENER);
		OPEN_TOKENS_MAP.put(KEYWORD_CLEANUP_OPENER, HTML_MASON_CLEANUP_OPENER);
		OPEN_TOKENS_MAP.put(KEYWORD_ONCE_OPENER, HTML_MASON_ONCE_OPENER);
		OPEN_TOKENS_MAP.put(KEYWORD_SHARED_OPENER, HTML_MASON_SHARED_OPENER);
		OPEN_TOKENS_MAP.put(KEYWORD_FLAGS_OPENER, HTML_MASON_FLAGS_OPENER);
		OPEN_TOKENS_MAP.put(KEYWORD_ATTR_OPENER, HTML_MASON_ATTR_OPENER);
		OPEN_TOKENS_MAP.put(KEYWORD_ARGS_OPENER, HTML_MASON_ARGS_OPENER);
		OPEN_TOKENS_MAP.put(KEYWORD_FILTER_OPENER, HTML_MASON_FILTER_OPENER);
		OPEN_TOKENS_MAP.put(KEYWORD_DOC_OPENER, HTML_MASON_DOC_OPENER);
		OPEN_TOKENS_MAP.put(KEYWORD_TEXT_OPENER, HTML_MASON_TEXT_OPENER);

		// parametrized
		OPEN_TOKENS_MAP.put(KEYWORD_METHOD_OPENER, HTML_MASON_METHOD_OPENER);
		OPEN_TOKENS_MAP.put(KEYWORD_DEF_OPENER, HTML_MASON_DEF_OPENER);
	}

	static
	{
		OPEN_CLOSE_MAP.put(KEYWORD_PERL_OPENER, KEYWORD_PERL_CLOSER);
		OPEN_CLOSE_MAP.put(KEYWORD_INIT_OPENER, KEYWORD_INIT_CLOSER);
		OPEN_CLOSE_MAP.put(KEYWORD_CLEANUP_OPENER, KEYWORD_CLEANUP_CLOSER);
		OPEN_CLOSE_MAP.put(KEYWORD_ONCE_OPENER, KEYWORD_ONCE_CLOSER);
		OPEN_CLOSE_MAP.put(KEYWORD_SHARED_OPENER, KEYWORD_SHARED_CLOSER);
		OPEN_CLOSE_MAP.put(KEYWORD_FLAGS_OPENER, KEYWORD_FLAGS_CLOSER);
		OPEN_CLOSE_MAP.put(KEYWORD_ATTR_OPENER, KEYWORD_ATTR_CLOSER);
		OPEN_CLOSE_MAP.put(KEYWORD_ARGS_OPENER, KEYWORD_ARGS_CLOSER);
		OPEN_CLOSE_MAP.put(KEYWORD_FILTER_OPENER, KEYWORD_FILTER_CLOSER);
		OPEN_CLOSE_MAP.put(KEYWORD_DOC_OPENER, KEYWORD_DOC_CLOSER);
		OPEN_CLOSE_MAP.put(KEYWORD_TEXT_OPENER, KEYWORD_TEXT_CLOSER);

		// parametrized
		OPEN_CLOSE_MAP.put(KEYWORD_METHOD_OPENER, KEYWORD_METHOD_CLOSER);
		OPEN_CLOSE_MAP.put(KEYWORD_DEF_OPENER, KEYWORD_DEF_CLOSER);
	}

	static
	{
		CLOSE_TOKENS_MAP.put(KEYWORD_PERL_CLOSER, HTML_MASON_PERL_CLOSER);
		CLOSE_TOKENS_MAP.put(KEYWORD_INIT_CLOSER, HTML_MASON_INIT_CLOSER);
		CLOSE_TOKENS_MAP.put(KEYWORD_CLEANUP_CLOSER, HTML_MASON_CLEANUP_CLOSER);
		CLOSE_TOKENS_MAP.put(KEYWORD_ONCE_CLOSER, HTML_MASON_ONCE_CLOSER);
		CLOSE_TOKENS_MAP.put(KEYWORD_SHARED_CLOSER, HTML_MASON_SHARED_CLOSER);
		CLOSE_TOKENS_MAP.put(KEYWORD_FLAGS_CLOSER, HTML_MASON_FLAGS_CLOSER);
		CLOSE_TOKENS_MAP.put(KEYWORD_ATTR_CLOSER, HTML_MASON_ATTR_CLOSER);
		CLOSE_TOKENS_MAP.put(KEYWORD_ARGS_CLOSER, HTML_MASON_ARGS_CLOSER);
		CLOSE_TOKENS_MAP.put(KEYWORD_FILTER_CLOSER, HTML_MASON_FILTER_CLOSER);
		CLOSE_TOKENS_MAP.put(KEYWORD_DOC_CLOSER, HTML_MASON_DOC_CLOSER);
		CLOSE_TOKENS_MAP.put(KEYWORD_TEXT_CLOSER, HTML_MASON_TEXT_CLOSER);

		// parametrized
		CLOSE_TOKENS_MAP.put(KEYWORD_METHOD_CLOSER, HTML_MASON_METHOD_CLOSER);
		CLOSE_TOKENS_MAP.put(KEYWORD_DEF_CLOSER, HTML_MASON_DEF_CLOSER);
	}

	protected boolean clearLine = false;
	private String BLOCK_CLOSE_TAG;

	public HTMLMasonLexer(Project project)
	{
		super(project);
	}

	@Override
	public int getInitialCustomState()
	{
		return LEX_MASON_HTML_BLOCK;
	}

	@Override
	public IElementType advance() throws IOException
	{
		IElementType result = super.advance();
		int currentCustomState = getCustomState();

		if (currentCustomState == LEX_MASON_PERL_ARGS_BLOCK && clearLine && result == OPERATOR_MOD)
		{
			result = SIGIL_HASH;
		}

		if (result == TokenType.NEW_LINE_INDENT)
		{
			clearLine = true;
		}
		else if (result != TokenType.WHITE_SPACE)
		{
			clearLine = false;
		}
		return result;
	}

	public IElementType perlAdvance() throws IOException
	{
		CharSequence buffer = getBuffer();
		int tokenStart = getNextTokenStart();
		int bufferEnd = getBufferEnd();
		int currentCustomState = getCustomState();

		if (bufferEnd == 0 || tokenStart >= bufferEnd)
		{
			return super.perlAdvance();
		}

		char currentChar = buffer.charAt(tokenStart);

		if (currentCustomState == LEX_MASON_PERL_LINE)
		{
			if (currentChar == '\n')
			{
				setTokenStart(tokenStart);
				setTokenEnd(tokenStart + 1);

				setCustomState(getInitialCustomState());
				return TokenType.NEW_LINE_INDENT;
			}
		}
		else if (currentCustomState == LEX_MASON_OPENING_TAG && currentChar == '>')
		{
			setTokenStart(tokenStart);
			setTokenEnd(tokenStart + 1);
			setCustomState(getInitialCustomState());
			return HTML_MASON_TAG_CLOSER;
		}
		else if (currentCustomState == LEX_MASON_PERL_EXPR_BLOCK && currentChar == '|')
		{
			Matcher m = MASON_EXPRESSION_FILTER_BLOCK.matcher(buffer);
			m.region(tokenStart, bufferEnd);
			if (m.lookingAt())
			{
				setTokenStart(tokenStart);
				setTokenEnd(tokenStart + 1);
				setCustomState(LEX_MASON_PERL_EXPR_FILTER_BLOCK);
				return HTML_MASON_EXPR_FILTER_PIPE;
			}
		}
		else if (currentCustomState == LEX_MASON_PERL_EXPR_FILTER_BLOCK && (currentChar == '-' || Character.isLetterOrDigit(currentChar)))
		{
			int offset = tokenStart;
			while (offset < bufferEnd && (currentChar = buffer.charAt(offset)) == '-' || Character.isLetterOrDigit(currentChar))
			{
				offset++;
			}
			int tokenLength = offset - tokenStart;
			CharSequence token = buffer.subSequence(tokenStart, offset);
			if (tokenLength <= 3 && DEFAULT_ESCAPERS_MERGED.matcher(token).matches())
			{
				while (tokenStart < offset)
				{
					pushPreparsedToken(tokenStart++, tokenStart, HTML_MASON_DEFAULT_ESCAPER_NAME);
				}
			}
			else
			{
				pushPreparsedToken(tokenStart, offset, HTML_MASON_ESCAPER_NAME);
			}
			return getPreParsedToken();
		}
		else if (currentCustomState == LEX_MASON_PERL_EXPR_BLOCK || currentCustomState == LEX_MASON_PERL_EXPR_FILTER_BLOCK)
		{
			if (bufferAtString(buffer, tokenStart, KEYWORD_BLOCK_CLOSER))
			{
				setTokenStart(tokenStart);
				setTokenEnd(tokenStart + KEYWORD_BLOCK_CLOSER.length());

				setCustomState(getInitialCustomState());
				return HTML_MASON_BLOCK_CLOSER;
			}

			if (parseTailSpaces(buffer, tokenStart, bufferEnd, KEYWORD_BLOCK_CLOSER))
			{
				return TokenType.WHITE_SPACE;
			}
		}
		else if (currentCustomState == LEX_MASON_PERL_CALL_BLOCK || currentCustomState == LEX_MASON_PERL_FILTERING_CALL_BLOCK)
		{
			if (bufferAtString(buffer, tokenStart, KEYWORD_CALL_CLOSER))
			{
				setTokenStart(tokenStart);
				setTokenEnd(tokenStart + KEYWORD_CALL_CLOSER.length());

				setCustomState(getInitialCustomState());
				return currentCustomState == LEX_MASON_PERL_CALL_BLOCK ? HTML_MASON_CALL_CLOSER : HTML_MASON_CALL_CLOSER_UNMATCHED;
			}

			if (parseTailSpaces(buffer, tokenStart, bufferEnd, KEYWORD_CALL_CLOSER))
			{
				return TokenType.WHITE_SPACE;
			}

		}
		else if (currentChar == '\n' && (currentCustomState == LEX_MASON_PERL_ARGS_BLOCK || currentCustomState == LEX_MASON_PERL_ATTR_BLOCK))
		{
			setTokenStart(tokenStart);
			setTokenEnd(tokenStart + 1);
			return HTML_MASON_HARD_NEWLINE;
		}
		else if (
				(currentCustomState == LEX_MASON_PERL_BLOCK ||
						currentCustomState == LEX_MASON_PERL_ARGS_BLOCK ||
						currentCustomState == LEX_MASON_PERL_ATTR_BLOCK) &&
						bufferAtString(buffer, tokenStart, BLOCK_CLOSE_TAG)
				)
		{
			setTokenStart(tokenStart);
			setTokenEnd(tokenStart + BLOCK_CLOSE_TAG.length());
			setCustomState(getInitialCustomState());
			return CLOSE_TOKENS_MAP.get(BLOCK_CLOSE_TAG);
		}
		else if (currentCustomState == getInitialCustomState())
		{
			int offset = tokenStart;

			boolean blockStart = false;
			boolean clearLine = true;
			Matcher m;
			Matcher matcherSimpleOpener = null;
			Matcher matcherOpener = null;

			int lastNonspaceCharacterOffset = -1;

			for (; offset < bufferEnd; offset++)
			{
				currentChar = buffer.charAt(offset);
				char nextChar1 = offset + 1 < bufferEnd ? buffer.charAt(offset + 1) : 0;
				char nextChar2 = offset + 2 < bufferEnd ? buffer.charAt(offset + 2) : 0;

				if (currentChar == '<' &&
						nextChar1 == '%' &&
						(
								offset + 2 < bufferEnd && Character.isWhitespace(buffer.charAt(offset + 2)) ||    // <%
										(matcherSimpleOpener = HTML_MASON_SIMPLE_OPENERS.matcher(buffer).region(offset, bufferEnd)).lookingAt() ||    // <%text>
										(matcherOpener = HTML_MASON_OPENERS.matcher(buffer).region(offset, bufferEnd)).lookingAt()    // <%augment...
						)
						)
				{
					blockStart = true;

					break;
				}
				else if (currentChar == '<' && nextChar1 == '/' && nextChar2 == '%' &&
						(m = HTML_MASON_CLOSERS.matcher(buffer).region(offset, bufferEnd)).lookingAt()
						)
				{
					String tag = m.group(0);
					pushPreparsedToken(offset, offset + tag.length(), CLOSE_TOKENS_MAP.get(tag));
					break;
				}
				else if (currentChar == '<' && nextChar1 == '&' && Character.isWhitespace(nextChar2))
				{
					pushPreparsedToken(offset, offset + KEYWORD_CALL_OPENER.length(), HTML_MASON_CALL_OPENER);
					parseCallComponentPath(offset + KEYWORD_CALL_OPENER.length());
					setCustomState(LEX_MASON_PERL_CALL_BLOCK);
					break;
				}
				else if (currentChar == '<' && nextChar1 == '&' && nextChar2 == '|')
				{
					pushPreparsedToken(offset, offset + KEYWORD_CALL_OPENER_FILTER.length(), HTML_MASON_CALL_FILTERING_OPENER);
					parseCallComponentPath(offset + KEYWORD_CALL_OPENER_FILTER.length());
					setCustomState(LEX_MASON_PERL_FILTERING_CALL_BLOCK);
					break;
				}
				else if (currentChar == '<' && nextChar1 == '/' && nextChar2 == '&')
				{
					int myOffset = offset;

					pushPreparsedToken(myOffset, myOffset + KEYWORD_CALL_CLOSE_TAG_START.length(), HTML_MASON_CALL_CLOSE_TAG_START);
					myOffset = preparseNewLinesAndSpaces(buffer, bufferEnd, myOffset + KEYWORD_CALL_CLOSE_TAG_START.length());

					int startOffset = myOffset;
					while (myOffset < bufferEnd && (currentChar = buffer.charAt(myOffset)) != '>' && !Character.isWhitespace(currentChar))
					{
						myOffset++;
					}
					if (myOffset > startOffset)
					{
						pushPreparsedToken(startOffset, myOffset, STRING_CONTENT);
					}

					myOffset = preparseNewLinesAndSpaces(buffer, bufferEnd, myOffset);

					if (myOffset < bufferEnd && buffer.charAt(myOffset) == '>')
					{
						pushPreparsedToken(myOffset++, myOffset, HTML_MASON_TAG_CLOSER);
					}

					break;
				}
				else if (clearLine && currentChar == '%')
				{
					pushPreparsedToken(offset, offset + 1, HTML_MASON_LINE_OPENER);
					setCustomState(LEX_MASON_PERL_LINE);
					break;
				}
				else if (currentChar == '\n')
				{
					clearLine = true;
				}
				else
				{
					if (!Character.isWhitespace(currentChar))
					{
						lastNonspaceCharacterOffset = offset;
					}
					clearLine = false;
				}
			}

			reLexHTMLBLock(tokenStart, offset, lastNonspaceCharacterOffset, HTML_MASON_TEMPLATE_BLOCK_HTML);

			if (offset >= bufferEnd)  // end of file, html block
			{
				yybegin(YYINITIAL);
			}
			else if (blockStart)
			{
				if (matcherSimpleOpener != null && matcherSimpleOpener.lookingAt())
				{
					// check for unnamed block
					String openingTag = matcherSimpleOpener.group(0);
					pushPreparsedToken(offset, offset + openingTag.length(), OPEN_TOKENS_MAP.get(openingTag));
					BLOCK_CLOSE_TAG = OPEN_CLOSE_MAP.get(openingTag);

					if (openingTag.equals(KEYWORD_DOC_OPENER))
					{
						offset += openingTag.length();
						int commentStart = offset;
						boolean gotCloseTag = false;

						while (offset < bufferEnd)
						{
							if (offset <= bufferEnd - KEYWORD_DOC_CLOSER.length() &&
									buffer.charAt(offset) == '<' &&
									buffer.charAt(offset + 1) == '/' &&
									buffer.charAt(offset + 2) == '%' &&
									buffer.charAt(offset + 3) == 'd' &&
									buffer.charAt(offset + 4) == 'o' &&
									buffer.charAt(offset + 5) == 'c' &&
									buffer.charAt(offset + 6) == '>'
									)
							{
								gotCloseTag = true;
								break;
							}
							offset++;
						}
						if (offset > commentStart)
						{
							pushPreparsedToken(commentStart, offset, COMMENT_BLOCK);
						}
						if (gotCloseTag)
						{
							pushPreparsedToken(offset, offset + KEYWORD_DOC_CLOSER.length(), HTML_MASON_DOC_CLOSER);
						}
					}
					else if (openingTag.equals(KEYWORD_TEXT_OPENER))
					{
						offset += openingTag.length();
						int commentStart = offset;
						boolean gotCloseTag = false;

						while (offset < bufferEnd)
						{
							if (offset <= bufferEnd - KEYWORD_TEXT_CLOSER.length() &&
									buffer.charAt(offset) == '<' &&
									buffer.charAt(offset + 1) == '/' &&
									buffer.charAt(offset + 2) == '%' &&
									buffer.charAt(offset + 3) == 't' &&
									buffer.charAt(offset + 4) == 'e' &&
									buffer.charAt(offset + 5) == 'x' &&
									buffer.charAt(offset + 6) == 't' &&
									buffer.charAt(offset + 7) == '>'
									)
							{
								gotCloseTag = true;
								break;
							}
							offset++;
						}

						if (offset > commentStart)
						{
							pushPreparsedToken(commentStart, offset, STRING_CONTENT);
						}
						if (gotCloseTag)
						{
							pushPreparsedToken(offset, offset + KEYWORD_TEXT_CLOSER.length(), HTML_MASON_TEXT_CLOSER);
						}
					}
					else if (openingTag.equals(KEYWORD_ATTR_OPENER))
					{
						setCustomState(LEX_MASON_PERL_ARGS_BLOCK);
					}
					else if (openingTag.equals(KEYWORD_ARGS_OPENER))
					{
						setCustomState(LEX_MASON_PERL_ARGS_BLOCK);
					}
					else
					{
						setCustomState(LEX_MASON_PERL_BLOCK);// fixme we should capture text here
					}
				}
				else if (matcherOpener != null && matcherOpener.lookingAt())
				{
					// check for named block
					String openingTag = matcherOpener.group(0);
					pushPreparsedToken(offset, offset + openingTag.length(), OPEN_TOKENS_MAP.get(openingTag));

					// spaces
					offset += openingTag.length();
					int startOffset = offset;
					while (offset < bufferEnd && Character.isWhitespace(buffer.charAt(offset)))
					{
						offset++;
					}

					if (offset > startOffset)
						pushPreparsedToken(startOffset, startOffset = offset, TokenType.WHITE_SPACE);

					while (offset < bufferEnd && (Character.isLetterOrDigit(currentChar = buffer.charAt(offset)) || currentChar == '_' || currentChar == '-' || currentChar == '.'))
					{
						offset++;
					}

					if (offset > startOffset) pushPreparsedToken(startOffset, startOffset = offset, IDENTIFIER);

					setCustomState(LEX_MASON_OPENING_TAG);
				}
				else
				{
					assert Character.isWhitespace(buffer.charAt(offset + 2));
					pushPreparsedToken(offset, offset + KEYWORD_BLOCK_OPENER.length(), HTML_MASON_BLOCK_OPENER);
					setCustomState(LEX_MASON_PERL_EXPR_BLOCK);
				}
			}

			if (preparsedTokensList.size() > 0)
			{
				return getPreParsedToken();
			}
		}
		return super.perlAdvance();
	}

	@Override
	public boolean isLineCommentEnd(int currentPosition)
	{
		CharSequence buffer = getBuffer();
		// fixme need to implement close tags handling i guess
		int customState = getCustomState();
		return buffer.charAt(currentPosition) == '\n' ||
				(customState == LEX_MASON_PERL_EXPR_BLOCK || customState == LEX_MASON_PERL_EXPR_FILTER_BLOCK)
						&& bufferAtString(buffer, currentPosition, KEYWORD_BLOCK_CLOSER)
				;
	}

	@Override
	protected String getKeywordCallCloser()
	{
		return KEYWORD_CALL_CLOSER;
	}
}
