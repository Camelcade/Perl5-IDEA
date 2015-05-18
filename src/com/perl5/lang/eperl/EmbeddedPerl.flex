package com.perl5.lang.eperl.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import com.perl5.lang.perl.util.PerlFunctionUtil;
import com.perl5.lang.perl.util.PerlScalarUtil;
import com.perl5.lang.perl.util.PerlHashUtil;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlArrayUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;

%%

%class EmbeddedPerlLexer
%implements EmbeddedPerlElementTypes, FlexLexer
%unicode
%public

%function advance
%type IElementType

PERL_START = "<?"
PERL_END = "?>"
HTML_CODE = ~{PERL_START}
PERL_CODE = ~{PERL_END}

%state LEX_PERL_CODE
%state LEX_OPEN_TOKEN
%state LEX_CLOSE_TOKEN
%xstate LEX_NO_OPENER

%%

<YYINITIAL>{
    {HTML_CODE} {yypushback(2);yybegin(LEX_OPEN_TOKEN);return DATA_HTML;}
    [^] {yybegin(LEX_NO_OPENER);yypushback(1);break;}
}

<LEX_NO_OPENER>
{
    [^]* {return DATA_HTML;}
}

<LEX_OPEN_TOKEN>
{
    {PERL_START} {yybegin(LEX_PERL_CODE);return DATA_TOKEN_OPEN;}
}

<LEX_PERL_CODE>{
    {PERL_CODE} {yypushback(2);yybegin(LEX_CLOSE_TOKEN);return DATA_PERL;}
}

<LEX_CLOSE_TOKEN>
{
    {PERL_END} {yybegin(YYINITIAL);return DATA_TOKEN_CLOSE;}
}

[^] {return TokenType.BAD_CHARACTER;}
