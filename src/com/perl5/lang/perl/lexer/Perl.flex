package com.perl5.lang.perl.lexer;

/*
    http://jflex.de/manual.html
    http://www2.cs.tum.edu/projects/cup

*/

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import com.perl5.lang.perl.lexer.helpers.PerlFunction;
import com.perl5.lang.perl.lexer.helpers.PerlScalar;
import com.perl5.lang.perl.lexer.helpers.PerlHash;
import com.perl5.lang.perl.lexer.helpers.PerlGlob;
import com.perl5.lang.perl.lexer.helpers.PerlArray;
import com.perl5.lang.perl.lexer.helpers.PerlPackage;

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
*/


//%line
//%column
%{
    public void setTokenStart(int position){zzCurrentPos = zzStartRead = position;}
    public int getNextTokenStart(){ return zzMarkedPos;}
    public boolean isLastToken(){ return zzMarkedPos == zzEndRead; }

    public void yybegin_LEX_MULTILINE(){yybegin(LEX_MULTILINE);}
    public void yybegin_YYINITIAL(){yybegin(YYINITIAL);}
    public void yybegin_LEX_MULTILINE_TOKEN(){yybegin(LEX_MULTILINE_TOKEN);}

	protected int dataBlockStart = 0;

	protected void StartDataBlock()
	{
		dataBlockStart = zzStartRead;
        yybegin(LEX_EOF);
	}

	protected int podBlockStart = 0;

	protected void StartPodBlock()
	{
		podBlockStart = zzStartRead;
        yybegin(LEX_POD);
	}
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

PERL_OPERATORS = "++" | "--" | "**" | "!" | "~" | "\\" | "+" | "-" | "=~" | "!~" | "*" | "/" | "%" | "x" | "<<" | ">>" | "<" | ">" | "<=" | ">=" | "lt" | "gt" | "le" | "ge" | "==" | "!=" | "<=>" | "eq" | "ne" | "cmp" | "~~" | "&" | "|" | "^" | "&&" | "||" | "//" | ".." | "..." | "?" | ":" | "=" | "+=" | "-=" | "*=" | "not" | "and" | "or" | "xor" | "defined"

MULTILINE_MARKER = [^\"\'\s\n\r ]+
MULTILINE_MARKER_SQ = "<<"{WHITE_SPACE}*\'{MULTILINE_MARKER}\'
MULTILINE_MARKER_DQ = "<<"{WHITE_SPACE}*\"{MULTILINE_MARKER}\"
MULTILINE_MARKER_DQ_BARE = "<<"{WHITE_SPACE}*{ALFANUM}+
MULTILINE_MARKER_DX = "<<"{WHITE_SPACE}*\`{MULTILINE_MARKER}\`

POD_OPEN         = \=(pod|head1|head2|head3|head4|over|item|back|begin|end|for|encoding){FULL_LINE}
POD_CLOSE       = \="cut"{FULL_LINE}

DEPACKAGE = "::"

PACKAGE_NAME = {ALFANUM}+ ({DEPACKAGE}{ALFANUM}+)*

PACKAGE_STATIC_CALL = ({ALFANUM}+{DEPACKAGE})+
PACKAGE_INSTANCE_CALL = {ALFANUM}+ ({DEPACKAGE}{ALFANUM}+)* "->"

FUNCTION_NAME = {ALFANUM}+

// @todo temp solution for package variables. need to implement smart lexing
VAR_SCALAR = [$][$]*{PACKAGE_NAME}?{ALFANUM}+
VAR_ARRAY = [@][$]*{PACKAGE_NAME}?{ALFANUM}+
VAR_HASH = [%][$]*{PACKAGE_NAME}?{ALFANUM}+
VAR_GLOB = [*][$]*{PACKAGE_NAME}?{ALFANUM}+

PERL_VERSION_CHUNK = [0-9][0-9_]*
PERL_VERSION = "v"?{PERL_VERSION_CHUNK}(\.{PERL_VERSION_CHUNK})*

NUMBER = "-"?[0-9_]+(\.[0-9_]+)?

END_OF_LINE_COMMENT = "#" {FULL_LINE}

//%state STRING
//%state YYINITIAL
%xstate LEX_PACKAGE_DEFINITION, LEX_PACKAGE_DEFINITION_VERSION, LEX_PACKAGE_DEFINITION_BLOCK
%xstate LEX_EOF
%xstate LEX_POD
%xstate LEX_MULTILINE, LEX_MULTILINE_TOKEN

%state LEX_PACKAGE_USE, LEX_PACKAGE_USE_PARAMS, LEX_PACKAGE_USE_VERSION
%state LEX_REQUIRE

%state LEX_FUNCTION_DEFINITION
%state LEX_DEREFERENCE
%%

// inclusive states
{NEW_LINE}   {
    if( isWaitingMultiLine() ) // this could be done using lexical state, like prepared or smth
    {
        startMultiLine();
    }
    return TokenType.NEW_LINE_INDENT;
}
{WHITE_SPACE}+   {return TokenType.WHITE_SPACE;}
","			    {return PERL_COMMA;}
"=>"			{return PERL_COMMA;}
{CHAR_SEMI}     {
    yybegin(YYINITIAL);
    return PERL_SEMI;
}
{END_OF_LINE_COMMENT}  { return PERL_COMMENT; }

<YYINITIAL>{
    {THE_END}               {StartDataBlock(); break;}
    {THE_DATA}               {StartDataBlock(); break;}
    {POD_OPEN}               {StartPodBlock();break;}

    "use"			{yybegin(LEX_PACKAGE_USE);return PerlFunction.getFunctionType(yytext().toString());}
    "no"			{yybegin(LEX_PACKAGE_USE); return PerlFunction.getFunctionType(yytext().toString());}
    "require"	    {yybegin(LEX_REQUIRE); return PerlFunction.getFunctionType(yytext().toString());}

    "sub"			{yybegin(LEX_FUNCTION_DEFINITION); return PerlFunction.getFunctionType(yytext().toString());}
    "package"       {yybegin(LEX_PACKAGE_DEFINITION); return PerlFunction.getFunctionType(yytext().toString());}
}


<LEX_EOF>
{
    {FULL_LINE} {
        if( zzMarkedPos == zzEndRead )
        {
            zzCurrentPos = zzStartRead = dataBlockStart;
            return PERL_COMMENT_BLOCK;
        }
        break;
    }
}
<LEX_POD>
{
    {POD_CLOSE} {
        zzCurrentPos = zzStartRead = podBlockStart;
        yybegin(YYINITIAL);
        return PERL_POD;
    }
    {FULL_LINE} {
        if( zzMarkedPos == zzEndRead )
        {
            zzCurrentPos = zzStartRead = podBlockStart;
            return PERL_POD;
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
    {PACKAGE_NAME}      {yybegin(LEX_PACKAGE_DEFINITION_VERSION); return PerlPackage.getPackageType(yytext().toString());}
    .   {yybegin(YYINITIAL);  return TokenType.BAD_CHARACTER;}
}

<LEX_PACKAGE_DEFINITION_VERSION>{
    ";"              {yybegin(YYINITIAL);return PERL_SEMI;}
    "{"              {yybegin(YYINITIAL); return PERL_LBRACE;}
    {WHITE_SPACE_LINE}  {return TokenType.WHITE_SPACE;}
    {PERL_VERSION}      {yybegin(LEX_PACKAGE_DEFINITION_BLOCK); return PERL_VERSION;}
    .   {
        yybegin(YYINITIAL);
        return TokenType.BAD_CHARACTER;}
}

<LEX_PACKAGE_DEFINITION_BLOCK>{
    ";"              {yybegin(YYINITIAL);return PERL_SEMI;}
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
        return PerlPackage.getPackageType(yytext().toString());
    }
    .   {yypushback(1); yybegin(YYINITIAL); break;}
}
<LEX_PACKAGE_USE_VERSION>
{
    {ALFANUM}+  {
        yybegin(LEX_PACKAGE_USE_PARAMS);
        return PERL_SQ_STRING;
    }
}
<LEX_PACKAGE_USE_PARAMS>
{
    {ALFANUM}+  {return PERL_SQ_STRING;}
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
        return PerlPackage.getPackageType(yytext().toString());
    }
    {DQ_STRING}     {return PERL_DQ_STRING;}
    {SQ_STRING}     {return PERL_SQ_STRING;}
    .   {yypushback(1);yybegin(YYINITIAL);break;}
}

/////////////////////////////////////////////// sub ... ////////////////////////////////////////////////////////////////
<LEX_FUNCTION_DEFINITION>{
    {FUNCTION_NAME}    {yybegin(YYINITIAL);return PERL_FUNCTION_USER;}
}

{PACKAGE_STATIC_CALL} {
    yypushback(2);
    return PerlPackage.getPackageType(yytext().toString());
}

{PACKAGE_INSTANCE_CALL} {
    yypushback(2);
    return PerlPackage.getPackageType(yytext().toString());
}


{MULTILINE_MARKER_SQ}   {prepareForMultiline(PERL_MULTILINE_SQ);return PERL_MULTILINE_MARKER;}
{MULTILINE_MARKER_DQ}   {prepareForMultiline(PERL_MULTILINE_DQ);return PERL_MULTILINE_MARKER;}
{MULTILINE_MARKER_DQ_BARE}   {prepareForMultiline(PERL_MULTILINE_DQ);return PERL_MULTILINE_MARKER;}
{MULTILINE_MARKER_DX}   {prepareForMultiline(PERL_MULTILINE_DX);return PERL_MULTILINE_MARKER;}

"{"             {return PERL_LBRACE;}
"}"             {return PERL_RBRACE;}
"["             {return PERL_LBRACK;}
"]"             {return PERL_RBRACK;}
"("             {return PERL_LPAREN;}
")"             {return PERL_RPAREN;}
"->"			{yybegin(LEX_DEREFERENCE);return PERL_DEREFERENCE;}
{DEPACKAGE}		{return PERL_DEPACKAGE;}
{DQ_STRING}     {return PERL_DQ_STRING;}
{DX_STRING}     {return PERL_DX_STRING;}
{SQ_STRING}     {return PERL_SQ_STRING;}
{NUMBER}        {return PERL_NUMBER;}

///////////////////////////////// PERL VARIABLE ////////////////////////////////////////////////////////////////////////
{VAR_SCALAR} {return PerlScalar.getScalarType(yytext().toString());}
{VAR_HASH} {return PerlHash.getHashType(yytext().toString());}
{VAR_ARRAY} {return PerlArray.getArrayType(yytext().toString());}
{VAR_GLOB} {return PerlGlob.getGlobType(yytext().toString());}
"@" {return PERL_SIGIL_ARRAY;}
"%" {return PERL_SIGIL_HASH;}
"$" {return PERL_SIGIL_SCALAR;}

{PERL_OPERATORS}    {return PERL_OPERATOR;}
{FUNCTION_NAME} { return PerlFunction.getFunctionType(yytext().toString());}

/* error fallback [^] */
[^]    { return TokenType.BAD_CHARACTER; }
