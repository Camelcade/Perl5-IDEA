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

%class PerlLexerGenerated
%implements PerlLexerBase
%abstract
%unicode
%public

%function advance
%type IElementType


%{
    public void setTokenStart(int position){zzCurrentPos = zzStartRead = position;}
    public void setTokenEnd(int position){zzMarkedPos = position;}
    public CharSequence getBuffer(){ return zzBuffer;}
    public char[] getBufferArray(){ return zzBufferArray;}
    public int getBufferEnd() {return zzEndRead;}
    public int getNextTokenStart(){ return zzMarkedPos;}
    public boolean isLastToken(){ return zzMarkedPos == zzEndRead; }
    public void setState(int newState){ zzLexicalState = newState; }

%}


/*
// Char classes
*/
NEW_LINE = \r?\n
WHITE_SPACE     = [ \t\f]
//EMPTY_LINE = {WHITE_SPACE}*{NEW_LINE}?
EMPTY_SPACE = [ \t\f\r\n]
BAREWORD = [a-zA-Z_][a-zA-Z0-9_]*

// bad solution, $scalar -function eats it
ANYWORD = [^ \t\f\r\n]

PERL_SCALAR_INDEX = "$#" "::" ? {BAREWORD}("::" {BAREWORD})*
PERL_SCALAR_REGEXP = "$" [1-9][0-9]*
PERL_SCALAR = "$" "::" ? {BAREWORD}("::" {BAREWORD})*

PERL_HASH = "%" "::" ? {BAREWORD}("::" {BAREWORD})*
PERL_ARRAY = "@" "::" ? {BAREWORD}("::" {BAREWORD})*
PERL_GLOB = "*" "::" ? {BAREWORD}("::" {BAREWORD})*

CHAR_ANY        = .|{NEW_LINE}
LINE            = .*
FULL_LINE       = .*{NEW_LINE}?
QUOTE           = "\"" | "'" | "`"
END_OF_LINE_COMMENT = "#" {FULL_LINE}

PERL_VERSION_CHUNK = [0-9][0-9_]*
PERL_VERSION = "v"?{PERL_VERSION_CHUNK}("." {PERL_VERSION_CHUNK})*
// heading _ removed to avoid @_ parsing as sigil-number
NUMBER = [0-9][0-9_]*( "." [0-9_]+ )?
NUMBER_HEX = "0x" [0-9a-fA-F]+

THE_END         = __END__
THE_DATA        = __DATA__

BLOCK_NAMES = "BEGIN" | "UNITCHECK" | "CHECK" | "INIT" | "END"

PERL_OPERATORS =  ","  | "++" | "--" | "**" | "!" | "~" | "\\" | "+" | "-" | "=~" | "!~" | "*" | "%"  | "<<" | ">>" | "<" | ">" | "<=" | ">=" | "==" | "!=" | "<=>" | "~~" | "&" | "|" | "^" | "&&" | "||" | "/" | ".." | "..." | "?" | "=" | "+=" | "-=" | "*="

// atm making the same, but seems unary are different
PERL_OPERATORS_FILETEST = "-" [rwxoRWXOezsfdlpSbctugkTBMAC] [^a-zA-Z0-9_]

MULTILINE_OPENER_SQ = "<<"{WHITE_SPACE}*\'{BAREWORD}\'
MULTILINE_OPENER_DQ = "<<"{WHITE_SPACE}*\"{BAREWORD}\"
MULTILINE_OPENER_DX = "<<"{WHITE_SPACE}*\`{BAREWORD}\`
MULTILINE_OPENER_DQ_BARE = "<<"{WHITE_SPACE}*{BAREWORD}

POD_OPEN         = \=(pod|head1|head2|head3|head4|over|item|back|begin|end|for|encoding){FULL_LINE}
POD_CLOSE       = \="cut"{FULL_LINE}

QUOTE_LIST_FUNCTIONS = "qw"
QUOTE_FUNCTIONS = "qq" | "qx" | "q"
TRANS_FUNCTIONS = "tr" | "y"
REGEX_FUNCTIONS = "s" | "qr" | "m"
PERL_SYN_QUOTE_LIKE = {QUOTE_LIST_FUNCTIONS} | {QUOTE_FUNCTIONS} | {TRANS_FUNCTIONS} | {REGEX_FUNCTIONS}

PERL_SYN_INCLUDE = "use" | "require" | "no"
PERL_SYN_BLOCK_OP = "sub" | "do" | "eval"
PERL_SYN_FLOW_CONTROL = "redo" | "next" | "last"
PERL_SYN_DECLARE = "package" | "sub" | "my" | "our" | "local" | "state"
PERL_SYN_COMPOUND = "if" | "unless" | "given" | "while" | "until" | "for" | "foreach" | "elsif" | "else" | "continue"
PERL_SYN_OTHER = "undef" | "print" | "say" | "open" | "grep" | "sort" | "map" | "close"
PERL_SYN_BINARY = "not" | "and" | "or" | "xor" | "x" | "lt" | "gt" | "le" | "ge" | "eq" | "ne" | "cmp"
PERL_SYN_UNARY = "defined" | "ref" | "exists" | "scalar"
FUNCTION_SPECIAL = {PERL_SYN_COMPOUND} | {PERL_SYN_DECLARE} | {PERL_SYN_BLOCK_OP} | {PERL_SYN_INCLUDE} | {PERL_SYN_QUOTE_LIKE} | {PERL_SYN_FLOW_CONTROL} | {PERL_SYN_OTHER} | {PERL_SYN_BINARY}

PERL_TAGS = "__FILE__" | "__LINE__" | "__PACKAGE__" | "__SUB__"

%xstate LEX_EOF
%xstate LEX_POD

%state LEX_BAREWORD_STRING
%state LEX_CODE

%state LEX_MULTILINE_WAITING
%xstate LEX_MULTILINE, LEX_MULTILINE_TOKEN
%xstate LEX_QUOTE_LIKE_OPENER, LEX_QUOTE_LIKE_CHARS, LEX_QUOTE_LIKE_CLOSER
%xstate LEX_QUOTE_LIKE_LIST_OPENER, LEX_QUOTE_LIKE_WORDS, LEX_QUOTE_LIKE_LIST_CLOSER
TRANS_MODIFIERS = [cdsr]
%xstate LEX_TRANS_OPENER, LEX_TRANS_CHARS, LEX_TRANS_CLOSER, LEX_TRANS_MODIFIERS
%xstate LEX_REGEX_OPENER, LEX_REGEX_ITEMS

%%

// inclusive states
{NEW_LINE}   { return processNewLine();}
{WHITE_SPACE}+   {return TokenType.WHITE_SPACE;}
";"     {return processSemicolon();}

{END_OF_LINE_COMMENT}  { return PERL_COMMENT; }

<YYINITIAL, LEX_CODE>{
    {THE_END}               {processDataOpener(); break;}
    {THE_DATA}               {processDataOpener(); break;}
    {POD_OPEN}               {processPodOpener();break;}
}

/**
**/
<LEX_REGEX_OPENER>{
    {EMPTY_SPACE}+  {return processOpenerWhiteSpace();}
    .   { return parseRegex(); }
}

<LEX_REGEX_ITEMS>{
    {CHAR_ANY}   {
        IElementType nextTokenType = getParsedToken();
        if( nextTokenType == null )
            break;
        return nextTokenType;
    }
}

/**
    tr y
**/
<LEX_TRANS_OPENER>{
    {EMPTY_SPACE}+  {return processOpenerWhiteSpace();}
    .   {
            IElementType type = processTransQuote();
            if( type == null ) // disallowed sharp
                break;
            return type;
        }
}

<LEX_TRANS_CHARS>{
    {CHAR_ANY}   {
          IElementType tokenType = processTransChar();
          if( tokenType != null )
                return tokenType;
          break;
        }
}

<LEX_TRANS_CLOSER>{
    .   { return processTransCloser(); }
}

<LEX_TRANS_MODIFIERS>{
    {TRANS_MODIFIERS} {return PERL_REGEX_MODIFIER;}
    {CHAR_ANY}   { popState(); yypushback(1); break; }
}

/**
    qq qx q
**/
<LEX_QUOTE_LIKE_OPENER>{
    {EMPTY_SPACE}+  {return processOpenerWhiteSpace();}
    .   {
            IElementType type = processQuoteLikeQuote();
            if( type == null ) // disallowed sharp
                break;
            return type;
        }
}

<LEX_QUOTE_LIKE_CHARS>{
    {CHAR_ANY}   {
          IElementType tokenType = processQuoteLikeChar();
          if( tokenType != null )
                return tokenType;
          break;
        }
}

<LEX_QUOTE_LIKE_CLOSER>{
    .   { popState(); return PERL_QUOTE; }
}

/**
    qw ()
**/
<LEX_QUOTE_LIKE_LIST_OPENER>{
    {EMPTY_SPACE}+  {return processOpenerWhiteSpace();}
    .   {
            IElementType type = processQuoteLikeListQuote();
            if( type == null ) // disallowed sharp
                break;
            return type;
        }
}

<LEX_QUOTE_LIKE_WORDS>{
    {EMPTY_SPACE}+ {return processOpenerWhiteSpace(); }
    {ANYWORD}+   {
          IElementType tokenType = processQuoteLikeWord();
          if( tokenType != null )
                return tokenType;
          break;
        }
}

<LEX_QUOTE_LIKE_LIST_CLOSER>{
    .   { popState(); return PERL_QUOTE; }
}

/**
    __DATA__ and __END__ eof seeker
**/
<LEX_EOF>
{
    {FULL_LINE} {
        if( isLastToken() )
            return endDataBlock();
        break;
    }
}

/**
    pod section
**/
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

/**
    Multiline string
**/

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
    .*  {yybegin(YYINITIAL);return PERL_STRING_MULTILINE_END;}
}

///////////////////////// package definition ///////////////////////////////////////////////////////////////////////////

{MULTILINE_OPENER_SQ}   {return processMultilineOpener();}
{MULTILINE_OPENER_DQ}   {return processMultilineOpener();}
{MULTILINE_OPENER_DQ_BARE}   {return processMultilineOpener();}
{MULTILINE_OPENER_DX}   {return processMultilineOpener();}

{QUOTE}         {return processStringOpener();}

":"             {return PERL_COLON;}
"::"            {return PERL_DEPACKAGE;}
"->"            {return PERL_DEREFERENCE;}
"=>"            {return PERL_ARROW_COMMA; } // for barewords in array
","            {return PERL_COMMA; }
"{"             {return PERL_LBRACE;}
"}"             {return PERL_RBRACE;}
"["             {return PERL_LBRACK;}
"]"             {return PERL_RBRACK;}
"("             {return PERL_LPAREN;}
")"             {return PERL_RPAREN;}
"<"             {return PERL_LANGLE;}
">"             {return PERL_RANGLE;}


{NUMBER_HEX}        {return PERL_NUMBER;}
{NUMBER}        {return PERL_NUMBER;}
{PERL_VERSION}  {return PERL_NUMBER_VERSION;}

///////////////////////////////// PERL VARIABLE ////////////////////////////////////////////////////////////////////////
{PERL_SCALAR_INDEX} {return PERL_SCALAR_INDEX;}
{PERL_SCALAR_REGEXP} {return PERL_SCALAR;}
{PERL_SCALAR} {return PERL_SCALAR;}
{PERL_ARRAY} {return PERL_ARRAY;}
{PERL_HASH} {return PERL_HASH;}
{PERL_GLOB} {return PERL_GLOB;}
"@" {return PERL_SIGIL_ARRAY;}
"%" {return PERL_SIGIL_HASH;}
"$#" {return PERL_SIGIL_SCALAR_INDEX;}
"$" {return PERL_SIGIL_SCALAR;}

{REGEX_FUNCTIONS} {return processRegexOpener();}
{TRANS_FUNCTIONS} {return processTransOpener();}
{QUOTE_FUNCTIONS} {return processQuoteLikeStringOpener();}
{QUOTE_LIST_FUNCTIONS} {return processQuoteLikeListOpener();}
{PERL_TAGS}    {return PERL_TAG;}
"/"   {   // regexp or div
    IElementType tokenType = processDiv();
    if( tokenType == null )
        break;
    return tokenType;
}
{PERL_OPERATORS}    {return PERL_OPERATOR;}
{PERL_OPERATORS_FILETEST} {yypushback(1);return PERL_OPERATOR_FILETEST;}
{PERL_SYN_UNARY} {return PERL_OPERATOR_UNARY;}

{FUNCTION_SPECIAL} {return PERL_KEYWORD;}
{BLOCK_NAMES} {return PERL_BLOCK_NAME;}

<LEX_BAREWORD_STRING>
{
    {BAREWORD} { yybegin(YYINITIAL); return PERL_STRING_CONTENT;}
}

{BAREWORD} { return PERL_BAREWORD;}

/* error fallback [^] */
[^]    { return TokenType.BAD_CHARACTER; }
