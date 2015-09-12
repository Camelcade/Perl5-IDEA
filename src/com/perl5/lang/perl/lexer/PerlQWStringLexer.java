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

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import java.io.IOException;

/**
 * Created by evstigneev on 11.09.2015.
 */
public class PerlQWStringLexer extends PerlQQStringLexer {
    @Override
    public IElementType perlAdvance() throws IOException {
        int bufferEnd = getBufferEnd();
        int tokenStart = getTokenEnd();

        if (tokenStart < getBufferStart() + 1 || tokenStart >= bufferEnd - 1)    // empty buffer and opening/closing quote
            return super.perlAdvance();

        CharSequence buffer = getBuffer();

        setTokenStart(tokenStart);
        int tokenEnd = tokenStart;

        if (buffer.charAt(tokenStart) == '\n') // newlines
        {
            setTokenEnd(tokenStart + 1);
            return TokenType.NEW_LINE_INDENT;
        } else if (Character.isWhitespace(buffer.charAt(tokenStart))) // whitespaces
        {
            char currentChar;
            while (tokenEnd < bufferEnd && (currentChar = buffer.charAt(tokenEnd)) != '\n' && Character.isWhitespace(currentChar))
                tokenEnd++;
            setTokenEnd(tokenEnd);
            return TokenType.WHITE_SPACE;
        }

        // words
        boolean isEscaped = false;

        while (tokenEnd < bufferEnd - 1) {
            char currentChar = buffer.charAt(tokenEnd);
            if (!isEscaped && Character.isWhitespace(currentChar))
                break;

            isEscaped = !isEscaped && currentChar == '\\';

            tokenEnd++;
        }
        setTokenEnd(tokenEnd);

        return STRING_CONTENT;
    }

    @Override
    protected IElementType getOpenQuoteToken() {
        return QUOTE_SINGLE_OPEN;
    }

    @Override
    protected IElementType getCloseQuoteToken() {
        return QUOTE_SINGLE_CLOSE;
    }
}
