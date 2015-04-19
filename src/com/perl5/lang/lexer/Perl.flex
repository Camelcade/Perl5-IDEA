package com.perl5.lang.lexer;

/*
    http://jflex.de/manual.html
    http://www2.cs.tum.edu/projects/cup

*/

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import com.perl5.lang.lexer.elements.PerlFunction;

%%

%class PerlLexer
%extends PerlLexerProto
%implements FlexLexer, PerlTokenTypes
%unicode
%public

%function advance
%type IElementType

//%line
//%column

/*
//InputCharacter = [^\r\n]
*/
LINE_TERMINATOR = [\r\n]+
WHITE_SPACE     = [ \t\f\r\n]+
CHAR_SEMI       = ;
CHAR_ANY        = .
ALFANUM         = [a-zA-Z0-9_]
FUNCTION_NAME   = [a-zA-Z_]{ALFANUM}+
DQ_STRING        = \"[^\"]*\"
SQ_STRING        = \'[^\']*\'
THE_END         = __END__
THE_DATA         = __DATA__
FULL_LINE       = .*[\r\n]+

DEPACKAGE = "::"

PACKAGE_NAME = {ALFANUM}+ ({DEPACKAGE}{ALFANUM}+)*

PACKAGE_STATIC_CALL = ({ALFANUM}+{DEPACKAGE})+
PACKAGE_INSTANCE_CALL = {ALFANUM}+ ({DEPACKAGE}{ALFANUM}+)* "->"

FUNCTION_NAME = {ALFANUM}+
VAR_SCALAR = [$][$]*{ALFANUM}+
VAR_ARRAY = [@][$]*{ALFANUM}+
VAR_HASH = [%][$]*{ALFANUM}+
VAR_GLOB = [*][$]*{ALFANUM}+
NUMBER = "-"?[0-9_]+(\.[0-9_]+)?

END_OF_LINE_COMMENT = "#" {CHAR_ANY}* {LINE_TERMINATOR}?

//%state STRING
%state YYINITIAL
%state PACKAGE_DEFINITION
%state FUNCTION_DEFINITION
%state PACKAGE_USE
%state LEX_REQUIRE
%state PACKAGE_USE_PARAMS
%state PACKAGE_STATIC_CALL
%state PACKAGE_INSTANCE_CALL
%state LEX_EOF
%state LEX_DEREFERENCE
%%

<LEX_EOF>
{
    {FULL_LINE} { return PERL_COMMENT_MULTILINE; }
}
{THE_END}               {yybegin(LEX_EOF);return PERL_COMMENT_MULTILINE;}
{THE_DATA}               {yybegin(LEX_EOF);return PERL_COMMENT_MULTILINE;}
{END_OF_LINE_COMMENT}                           { return PERL_COMMENT; }
{CHAR_SEMI}     {yybegin(YYINITIAL);return PERL_SEMI;}
{WHITE_SPACE}   {return TokenType.WHITE_SPACE;}
"{"             {return PERL_LBRACE;}
"}"             {return PERL_RBRACE;}
"["             {return PERL_LBRACK;}
"]"             {return PERL_RBRACK;}
"("             {return PERL_LPAREN;}
")"             {return PERL_RPAREN;}
","			    {return PERL_COMMA;}
"=>"			{return PERL_COMMA;}
"->"			{yybegin(LEX_DEREFERENCE);return PERL_DEREFERENCE;}
{DEPACKAGE}		{return PERL_DEPACKAGE;}
{DQ_STRING}     {return PERL_DQ_STRING;}
{SQ_STRING}     {return PERL_SQ_STRING;}
{NUMBER}        {return PERL_NUMBER;}

///////////////////////////////// PERL VARIABLE ////////////////////////////////////////////////////////////////////////
{VAR_SCALAR} {return checkBuiltInScalar();}
{VAR_HASH} {return checkBuiltInHash();}
{VAR_ARRAY} {return checkBuiltInArray();}
{VAR_GLOB} {return checkBuiltInGlob();}

<PACKAGE_USE_PARAMS>
{
    {ALFANUM}+  {return PERL_SQ_STRING;}
}

<PACKAGE_USE>{
    {PACKAGE_NAME}    {
        yybegin(PACKAGE_USE_PARAMS);
        return checkCorePackage();
    }
}

<PACKAGE_DEFINITION>{
    {PACKAGE_NAME}    {return PERL_PACKAGE;}
}

<FUNCTION_DEFINITION>{
    {FUNCTION_NAME}    {yybegin(YYINITIAL);return PERL_USER_FUNCTION;}
}

<PACKAGE_STATIC_CALL>
{
    {ALFANUM}+ {return PERL_STATIC_METHOD_CALL;}
}

{PACKAGE_STATIC_CALL} {
    yybegin(PACKAGE_STATIC_CALL);
    yypushback(2);
    return PERL_PACKAGE;
}

<PACKAGE_INSTANCE_CALL>
{
    {ALFANUM}+ {return PERL_INSTANCE_METHOD_CALL;}
}

{PACKAGE_INSTANCE_CALL} {
    yybegin(PACKAGE_INSTANCE_CALL);
    yypushback(2);
    return PERL_PACKAGE;
}

<LEX_REQUIRE>{
    {PACKAGE_NAME}    {return PERL_PACKAGE;}
}

<YYINITIAL> {
  /* whitespace */
    "use"			{yybegin(PACKAGE_USE);return PerlFunction.getFunction(yytext().toString());}
    "no"			{yybegin(PACKAGE_USE); return PerlFunction.getFunction(yytext().toString());}

    "sub"			{yybegin(FUNCTION_DEFINITION); return PerlFunction.getFunction(yytext().toString());}
    "package"       {yybegin(PACKAGE_DEFINITION); return PerlFunction.getFunction(yytext().toString());}
    "require"       {yybegin(LEX_REQUIRE);return PerlFunction.getFunction(yytext().toString());}
}

"++"			{return PERL_OPERATOR;}
"--"			{return PERL_OPERATOR;}
"**"			{return PERL_OPERATOR;}
"!"			{return PERL_OPERATOR;}
"~"			{return PERL_OPERATOR;}
"\\"			{return PERL_OPERATOR;}
"+"			{return PERL_OPERATOR;}
"-"			{return PERL_OPERATOR;}
"=~"			{return PERL_OPERATOR;}
"!~"			{return PERL_OPERATOR;}
"*"			{return PERL_OPERATOR;}
"/"			{return PERL_OPERATOR;}
"%"			{return PERL_OPERATOR;}
"x"			{return PERL_OPERATOR;}
"<<"			{return PERL_OPERATOR;}
">>"			{return PERL_OPERATOR;}
"<"			{return PERL_OPERATOR;}
">"			{return PERL_OPERATOR;}
"<="			{return PERL_OPERATOR;}
">="			{return PERL_OPERATOR;}
"lt"			{return PERL_OPERATOR;}
"gt"			{return PERL_OPERATOR;}
"le"			{return PERL_OPERATOR;}
"ge"			{return PERL_OPERATOR;}
"=="			{return PERL_OPERATOR;}
"!="			{return PERL_OPERATOR;}
"<=>"			{return PERL_OPERATOR;}
"eq"			{return PERL_OPERATOR;}
"ne"			{return PERL_OPERATOR;}
"cmp"			{return PERL_OPERATOR;}
"~~"			{return PERL_OPERATOR;}
"&"			{return PERL_OPERATOR;}
"|"			{return PERL_OPERATOR;}
"^"			{return PERL_OPERATOR;}
"&&"			{return PERL_OPERATOR;}
"||"			{return PERL_OPERATOR;}
"//"			{return PERL_OPERATOR;}
".."			{return PERL_OPERATOR;}
"..."			{return PERL_OPERATOR;}
"?"			{return PERL_OPERATOR;}
":"			{return PERL_OPERATOR;}
"="			{return PERL_OPERATOR;}
"+="			{return PERL_OPERATOR;}
"-="			{return PERL_OPERATOR;}
"*="			{return PERL_OPERATOR;}
"not"			{return PERL_OPERATOR;}
"and"			{return PERL_OPERATOR;}
"or"			{return PERL_OPERATOR;}
"xor"			{return PERL_OPERATOR;}

{FUNCTION_NAME} { return PerlFunction.getFunction(yytext().toString());}

/* error fallback */
[^]    { return PERL_BAD_CHARACTER; }
