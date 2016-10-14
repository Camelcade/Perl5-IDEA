package com.perl5.lang.htmlmason.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlProtoLexer;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;

%%

%class HTMLMasonLexerGenerated
%extends PerlProtoLexer
%implements HTMLMasonElementTypes
//%abstract
%unicode
%public


%function perlAdvance
%type IElementType

EVERYTHING = [^]+

%%

{EVERYTHING} {return HTML_MASON_TEMPLATE_BLOCK_HTML;}
