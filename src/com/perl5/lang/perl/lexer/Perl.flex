package com.perl5.lang.perl.lexer;

/*
    http://jflex.de/manual.html
    http://www2.cs.tum.edu/projects/cup

*/

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

%class PerlLexer
%extends PerlLexerProto
%implements FlexLexer, PerlElementTypes
%unicode
%public

%function advance
%type IElementType

/*
http://perldoc.perl.org/perlop.html#Quote-Like-Operators
q qq qx qw m qr {}
s tr y {}{}
<>
//
*/


//%line
//%column
%{
    public void setTokenStart(int position){zzCurrentPos = zzStartRead = position;}
    public int getNextTokenStart(){ return zzMarkedPos;}
    public boolean isLastToken(){ return zzMarkedPos == zzEndRead; }

    public void yybegin_YYINITIAL(){yybegin(YYINITIAL);}
    public void yybegin_LEX_MULTILINE(){yybegin(LEX_MULTILINE);}
    public void yybegin_LEX_MULTILINE_TOKEN(){yybegin(LEX_MULTILINE_TOKEN);}
    public void yybegin_LEX_MULTILINE_WAITING(){yybegin(LEX_MULTILINE_WAITING);}
    public void yybegin_LEX_EOF(){yybegin(LEX_EOF);}
    public void yybegin_LEX_POD(){yybegin(LEX_POD);}

    public boolean yystate_LEX_MULTILINE_WAITING(){return yystate() == LEX_MULTILINE_WAITING;}

%}


/*
// Char classes
*/
NEW_LINE = \r?\n
WHITE_SPACE     = [ \t\f]
WHITE_SPACE_LINE = {WHITE_SPACE}*{NEW_LINE}?

CHAR_SEMI       = ;
//CHAR_ANY        = .
ALFANUM         = [a-zA-Z0-9_]
FUNCTION_NAME   = [a-zA-Z_]{ALFANUM}+
THE_END         = __END__
THE_DATA         = __DATA__
LINE            = .*
FULL_LINE       = .*{NEW_LINE}?

DX_STRING        = \`[^\`\n\r]*\`
DQ_STRING        = \"[^\"\n\r]*\"
SQ_STRING        = \'[^\'\n\r]*\'

PERL_OPERATORS = "++" | "--" | "**" | "!" | "~" | "\\" | "+" | "-" | "=~" | "!~" | "*" | "/" | "%" | "x" | "<<" | ">>" | "<" | ">" | "<=" | ">=" | "lt" | "gt" | "le" | "ge" | "==" | "!=" | "<=>" | "eq" | "ne" | "cmp" | "~~" | "&" | "|" | "^" | "&&" | "||" | "//" | ".." | "..." | "?" | ":" | "=" | "+=" | "-=" | "*=" | "not" | "and" | "or" | "xor" | "defined" | "ref" | "scalar" | "exists"

MULTILINE_MARKER = [^\"\'\s\n\r ]+
MULTILINE_OPENER_SQ = "<<"{WHITE_SPACE}*\'{MULTILINE_MARKER}\'
MULTILINE_OPENER_DQ = "<<"{WHITE_SPACE}*\"{MULTILINE_MARKER}\"
MULTILINE_OPENER_DQ_BARE = "<<"{WHITE_SPACE}*{ALFANUM}+
MULTILINE_OPENER_DX = "<<"{WHITE_SPACE}*\`{MULTILINE_MARKER}\`

POD_OPEN         = \=(pod|head1|head2|head3|head4|over|item|back|begin|end|for|encoding){FULL_LINE}
POD_CLOSE       = \="cut"{FULL_LINE}

DEPACKAGE = "::"
DEREFERENCE = "->"

PACKAGE_NAME = {ALFANUM}+ ({DEPACKAGE}{ALFANUM}+)*

PACKAGE_STATIC_CALL = ({ALFANUM}+{DEPACKAGE})+
PACKAGE_INSTANCE_CALL = {ALFANUM}+ ({DEPACKAGE}{ALFANUM}+)* {DEREFERENCE}

FUNCTION_NAME = {ALFANUM}+

// @todo temp solution for package variables. need to implement smart lexing
VAR_SCALAR = [$]{PACKAGE_NAME}?{ALFANUM}+
VAR_ARRAY = [@]{PACKAGE_NAME}?{ALFANUM}+
VAR_HASH = [%]{PACKAGE_NAME}?{ALFANUM}+
VAR_GLOB = [*]{PACKAGE_NAME}?{ALFANUM}+

VAR_SCALAR_SPECIAL = "$^WARNING_BITS" | "$^WIDE_SYSTEM_CALLS" | "$^UNICODE" | "$^TAINT" | "$^UTF8LOCALE" | "$^RE_TRIE_MAXBUF" | "$^CHILD_ERROR_NATIVE" | "$^ENCODING" | "$^OPEN" | "$^RE_DEBUG_FLAGS" | "$^A" | "$^C" | "$^T" | "$^S" | "$^V" | "$^W" | "$^X" | "$^D" | "$^E" | "$^F" | "$^H" | "$^I" | "$^L" | "$^M" | "$^N" | "$^O" | "$^P" | "$^R" | "$^H" | "$!" | "$\"" | "$#" | "$$" | "$%" | "$&" | "$'" | "$(" | "$)" | "$*" | "$+" | "$," | "$_" | "$-" | "$`" | "$." | "$a" | "$/" | "$0" | "$:" | "$;" | "$<" | "$=" | "$>" | "$?" | "$@" | "$[" | "$\"" | "$]" | "$|" | "$^" | "$~" | "$+" | "$-" | "$_" | "$!" | "$+" | "$-"
VAR_ARRAY_SPECIAL = "@_" | "@!" | "@+" | "@-" | "@^H"
VAR_HASH_SPECIAL = "%!" | "%+" | "%-" | "%^H"

PERL_VERSION_CHUNK = [0-9][0-9_]*
PERL_VERSION = "v"?{PERL_VERSION_CHUNK}(\.{PERL_VERSION_CHUNK})*

NUMBER = "-"?[0-9_]+(\.[0-9_]+)?

END_OF_LINE_COMMENT = "#" {FULL_LINE}

//%state STRING
//%state YYINITIAL
%xstate LEX_PACKAGE_DEFINITION, LEX_PACKAGE_DEFINITION_VERSION, LEX_PACKAGE_DEFINITION_BLOCK
%xstate LEX_EOF
%xstate LEX_POD

%state LEX_MULTILINE_WAITING
%xstate LEX_MULTILINE, LEX_MULTILINE_TOKEN

%state LEX_PACKAGE_USE, LEX_PACKAGE_USE_PARAMS, LEX_PACKAGE_USE_VERSION
%state LEX_REQUIRE

%state LEX_FUNCTION_DEFINITION
%state LEX_DEREFERENCE
%%

// inclusive states
{NEW_LINE}   { return processNewLine();}
{WHITE_SPACE}+   {return TokenType.WHITE_SPACE;}
","			    {return PERL_COMMA;}
"=>"			{return PERL_COMMA;}
{CHAR_SEMI}     {return processSemicolon();}

{END_OF_LINE_COMMENT}  { return PERL_COMMENT; }

<YYINITIAL>{
    {THE_END}               {processDataOpener(); break;}
    {THE_DATA}               {processDataOpener(); break;}
    {POD_OPEN}               {processPodOpener();break;}

    "use"			{yybegin(LEX_PACKAGE_USE);return PerlFunctionUtil.getFunctionType(yytext().toString());}
    "no"			{yybegin(LEX_PACKAGE_USE); return PerlFunctionUtil.getFunctionType(yytext().toString());}
    "require"	    {yybegin(LEX_REQUIRE); return PerlFunctionUtil.getFunctionType(yytext().toString());}

    "sub"			{yybegin(LEX_FUNCTION_DEFINITION); return PerlFunctionUtil.getFunctionType(yytext().toString());}
    "package"       {yybegin(LEX_PACKAGE_DEFINITION); return PerlFunctionUtil.getFunctionType(yytext().toString());}
}


<LEX_EOF>
{
    {FULL_LINE} {
        if( isLastToken() )
            return endDataBlock();
        break;
    }
}
<LEX_POD>
{
    {POD_CLOSE} { return endPodBlock(); }
    {FULL_LINE} {
        if( isLastToken() )
        {
            endPodBlock();
        }
        break;
    }
}

<LEX_MULTILINE>{
    {LINE}  {
        if( isMultilineEnd() || isLastToken())
        {
            return endMultiline();
        }
        break;
    }
    {NEW_LINE}  {
        if( isLastToken() )
        {
            return endMultiline();
        }
        break;
    }
}
<LEX_MULTILINE_TOKEN>{
    .*  {yybegin(YYINITIAL);return PERL_MULTILINE_MARKER;}
}

<LEX_PACKAGE_DEFINITION>{
    {WHITE_SPACE_LINE}  {return TokenType.WHITE_SPACE;}
    {PACKAGE_NAME}      {yybegin(LEX_PACKAGE_DEFINITION_VERSION); return PERL_PACKAGE;}
    .   {yybegin(YYINITIAL);  return TokenType.BAD_CHARACTER;}
}

<LEX_PACKAGE_DEFINITION_VERSION>{
    ";"              {return processSemicolon();}
    "{"              {yybegin(YYINITIAL); return PERL_LBRACE;}
    {WHITE_SPACE_LINE}  {return TokenType.WHITE_SPACE;}
    {PERL_VERSION}      {yybegin(LEX_PACKAGE_DEFINITION_BLOCK); return PERL_VERSION;}
    .   {
        yybegin(YYINITIAL);
        return TokenType.BAD_CHARACTER;}
}

<LEX_PACKAGE_DEFINITION_BLOCK>{
    ";"              {return processSemicolon();}
    {WHITE_SPACE_LINE}  {return TokenType.WHITE_SPACE;}
    "{"      {yybegin(YYINITIAL); return PERL_LBRACE;}
    .   {yybegin(YYINITIAL); return TokenType.BAD_CHARACTER;}
}

/////////////////////////////////////////// USE/NO ... /////////////////////////////////////////////////////////////////
<LEX_PACKAGE_USE,LEX_PACKAGE_USE_VERSION>{
    {PERL_VERSION}  {
        yybegin(LEX_PACKAGE_USE_PARAMS);
        return PERL_VERSION;
    }
}
<LEX_PACKAGE_USE>{
    {PACKAGE_NAME}    {
        yybegin(LEX_PACKAGE_USE_VERSION);
        return PerlPackageUtil.getPackageType(yytext().toString());
    }
    .   {yypushback(1); yybegin(YYINITIAL); break;}
}
<LEX_PACKAGE_USE_VERSION>
{
    {ALFANUM}+  {
        yybegin(LEX_PACKAGE_USE_PARAMS);
        return PERL_STRING;
    }
}
<LEX_PACKAGE_USE_PARAMS>
{
    {ALFANUM}+  {return PERL_STRING;}
}
/////////////////////////////////////////// REQUIRE ... ////////////////////////////////////////////////////////////////

<LEX_REQUIRE>
{
    {PERL_VERSION}  {
        yybegin(YYINITIAL);
        return PERL_VERSION;
    }
    {PACKAGE_NAME}    {
        yybegin(YYINITIAL);
        return PerlPackageUtil.getPackageType(yytext().toString());
    }
    {DQ_STRING}     {return PERL_STRING;}
    {SQ_STRING}     {return PERL_STRING;}
    .   {yypushback(1);yybegin(YYINITIAL);break;}
}

/////////////////////////////////////////////// sub ... ////////////////////////////////////////////////////////////////
<LEX_FUNCTION_DEFINITION>{
    {FUNCTION_NAME}    {yybegin(YYINITIAL);return PERL_FUNCTION;}
}

{PACKAGE_STATIC_CALL} {
    yypushback(2);
    return PerlPackageUtil.getPackageType(yytext().toString());
}

{PACKAGE_INSTANCE_CALL} {
    yypushback(2);
    return PERL_PACKAGE;
}

{MULTILINE_OPENER_SQ}   {return processMultilineOpener();}
{MULTILINE_OPENER_DQ}   {return processMultilineOpener();}
{MULTILINE_OPENER_DQ_BARE}   {return processMultilineOpener();}
{MULTILINE_OPENER_DX}   {return processMultilineOpener();}

"{"             {return PERL_LBRACE;}
"}"             {return PERL_RBRACE;}
"["             {return PERL_LBRACK;}
"]"             {return PERL_RBRACK;}
"("             {return PERL_LPAREN;}
")"             {return PERL_RPAREN;}
{DEREFERENCE}	{yybegin(LEX_DEREFERENCE);return PERL_DEREFERENCE;}
{DEPACKAGE}		{return PERL_DEPACKAGE;}
{DQ_STRING}     {return PERL_STRING;}
{DX_STRING}     {return PERL_STRING;}
{SQ_STRING}     {return PERL_STRING;}
{NUMBER}        {return PERL_NUMBER;}

///////////////////////////////// PERL VARIABLE ////////////////////////////////////////////////////////////////////////
{VAR_SCALAR_SPECIAL} {return PERL_SCALAR;}
{VAR_SCALAR} {return PERL_SCALAR;}
{VAR_HASH_SPECIAL} {return PERL_HASH;}
{VAR_HASH} {return PERL_HASH;}
{VAR_ARRAY_SPECIAL} {return PERL_ARRAY;}
{VAR_ARRAY} {return PERL_ARRAY;}
{VAR_GLOB} {return PERL_GLOB;}
"@" {return PERL_SIGIL_ARRAY;}
"%" {return PERL_SIGIL_HASH;}
"$" {return PERL_SIGIL_SCALAR;}

{PERL_OPERATORS}    {return PERL_OPERATOR;}
{FUNCTION_NAME} { return PERL_FUNCTION;}

/* error fallback [^] */
[^]    { return TokenType.BAD_CHARACTER; }
