package com.perl5.lang.embedded.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlProtoLexer;
import com.perl5.lang.embedded.psi.EmbeddedPerlElementTypes;

%%

%class EmbeddedPerlLexerGenerated
%extends PerlProtoLexer
%implements com.perl5.lang.embedded.psi.EmbeddedPerlElementTypes
//%abstract
%unicode
%public


%function perlAdvance
%type IElementType

EVERYTHING = [^]*

%%

{EVERYTHING} {return EMBED_TEMPLATE_BLOCK_HTML;}
