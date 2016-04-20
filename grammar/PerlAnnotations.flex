/*
    Copyright 2015 Alexandr Evstigneev

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.perl5.lang.perl.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

%%

%class PerlAnnotationsLexerGenerated
%extends PerlProtoLexer
%implements PerlElementTypes
%abstract
%unicode
%public


%function perlAdvance
%type IElementType

%state LEX_PREPARSED

%{
	protected abstract IElementType parseFallback();
%}


/*
// Char classes
*/
NEW_LINE = \r?\n
WHITE_SPACE     = [ \t\f]

%%

// inclusive states
{NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}
{WHITE_SPACE}+   {return TokenType.WHITE_SPACE;}
"{"             {return LEFT_BRACE;}
"}"             {return RIGHT_BRACE;}
"["             {return LEFT_BRACKET;}
"]"             {return RIGHT_BRACKET;}
"("             {return LEFT_PAREN;}
")"             {return RIGHT_PAREN;}

/* error fallback [^] */
[^] {return parseFallback();}
