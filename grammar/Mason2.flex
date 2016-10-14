package com.perl5.lang.mason2.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlProtoLexer;
import com.perl5.lang.mason2.elementType.Mason2ElementTypes;

%%

%class Mason2LexerGenerated
%extends PerlProtoLexer
%implements Mason2ElementTypes
//%abstract
%unicode
%public


%function perlAdvance
%type IElementType

EVERYTHING = [^]+

%%

{EVERYTHING} {return MASON_TEMPLATE_BLOCK_HTML;}
