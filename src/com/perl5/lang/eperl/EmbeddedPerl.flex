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
%abstract
%unicode
%public

%function advance
%type IElementType

PERL_START = "<?"
PERL_END = "?>"

HTML_CODE = [^]*{PERL_START}
PERL_CODE = [^]*{PERL_END}

%state LEX_PERL_CODE

%%

<YYINITIAL>{
    {PERL_END} {return DATA_TOKEN_CLOSE;}
    {HTML_CODE} {yypushback(2);yybegin(LEX_PERL_CODE);return DATA_HTML;}
}

<LEX_PERL_CODE>{
    {PERL_START} {return DATA_TOKEN_OPEN;}
    {PERL_CODE} {yypushback(2);yybegin(YYINITIAL);return DATA_PERL;}
}


[^] {return TokenType.BAD_CHARACTER;}
