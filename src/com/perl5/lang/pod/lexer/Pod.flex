package com.perl5.lang.pod.lexer;

/*
    http://jflex.de/manual.html
    http://www2.cs.tum.edu/projects/cup

*/

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.pod.PodTokenTypes;
import org.jetbrains.annotations.NotNull;

%%

%class PodLexer
%extends PodLexerProto
%implements FlexLexer, PodTokenTypes
%unicode
%public

%function advance
%type IElementType

LINE_TERMINATOR = [\r\n]+
WHITE_SPACE     = [ \t\f]+

TEXT_ANY        = .*
FULL_LINE       = ([^=].*)?{LINE_TERMINATOR}
CODE_LINE       = {WHITE_SPACE}.+{LINE_TERMINATOR}

POD_OPEN        = \="pod"
POD_TAG         = \=(head1|head2|head3|head4|over|item|back|begin|end|for|encoding)
POD_CLOSE       = \="cut"

%xstate LEX_POD, LEX_LAST_LINE, LEX_TAG_LINE
%%

<YYINITIAL>{
    {POD_OPEN} {yybegin(LEX_TAG_LINE);return POD_MARKER;}
    {POD_TAG}  {yybegin(LEX_TAG_LINE);return POD_TAG;}
    {POD_CLOSE} {yybegin(LEX_LAST_LINE); return POD_TAG;}

    {FULL_LINE} {return TokenType.BAD_CHARACTER;}
}

<LEX_TAG_LINE>{
    .*{LINE_TERMINATOR} {yybegin(LEX_POD);return POD_TEXT;}
}

<LEX_POD>{
    {POD_TAG}  {yybegin(LEX_TAG_LINE);return POD_TAG;}
    {POD_CLOSE} {yybegin(LEX_LAST_LINE); return POD_TAG;}
    {CODE_LINE} {return POD_CODE;}
    {FULL_LINE} {return POD_TEXT;}
}

<LEX_LAST_LINE>{
    {TEXT_ANY}  {return POD_TEXT;}
    {LINE_TERMINATOR} {yybegin(YYINITIAL); return TokenType.WHITE_SPACE;}
}

/* error fallback */
[^]    { return TokenType.BAD_CHARACTER; }
