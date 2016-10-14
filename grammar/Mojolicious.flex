package com.perl5.lang.mojolicious.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlProtoLexer;
import com.perl5.lang.mojolicious.MojoliciousElementTypes;

%%

%class MojoliciousLexerGenerated
%extends PerlProtoLexer
%implements MojoliciousElementTypes
//%abstract
%unicode
%public


%function perlAdvance
%type IElementType

EVERYTHING = [^]+

%%

{EVERYTHING} {return MOJO_TEMPLATE_BLOCK_HTML;}
