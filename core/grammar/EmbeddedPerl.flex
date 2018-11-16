package com.perl5.lang.embedded.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.embedded.lexer.EmbeddedPerlBaseLexer;
import com.perl5.lang.embedded.psi.EmbeddedPerlElementTypes;

%%

%class EmbeddedPerlLexer
%extends EmbeddedPerlBaseLexer
%implements EmbeddedPerlElementTypes
//%abstract
%unicode
%public


%function perlAdvance
%type IElementType


CODE_OPEN_MARKER = "<?"
CODE_CLOSE_MARKER = "?>"
WHITE_SPACE = [ \t\f]
ANY_SPACE = ({WHITE_SPACE}|\R)+

STEP_PATTERN = [^< \t\f\n\r]+

%state PERL
%state CHECK_WHITE_SPACE
%state POST_PERL

%%

//// first spaces after perl should be a spaces, not template
<POST_PERL>{
	{ANY_SPACE} 		{yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
	[^]					{yypushback(1);yybegin(YYINITIAL);}
}


// checks if white space should be a white space or template
<CHECK_WHITE_SPACE>{
	{CODE_OPEN_MARKER}	{pullback(0);yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
	[^]					{yypushback(1);yybegin(YYINITIAL);return EMBED_TEMPLATE_BLOCK_HTML;}
	<<EOF>>				{yybegin(YYINITIAL);return EMBED_TEMPLATE_BLOCK_HTML;}
}

<PERL>{
	{CODE_CLOSE_MARKER}	{yybegin(POST_PERL);return EMBED_MARKER_CLOSE;}
	[^]					{return delegateLexing();}
}

// initial means we are in HTML block
<YYINITIAL>{
	{CODE_OPEN_MARKER} / {CODE_CLOSE_MARKER}?	{yybegin(PERL);return EMBED_MARKER_OPEN;}
	{ANY_SPACE} 								{yybegin(CHECK_WHITE_SPACE);}
	{STEP_PATTERN}								{return EMBED_TEMPLATE_BLOCK_HTML;}
	"<"{STEP_PATTERN}?							{return EMBED_TEMPLATE_BLOCK_HTML;}
}

