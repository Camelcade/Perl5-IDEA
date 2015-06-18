/*
    Copyright 2015 Alexandr Evstigneev

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.perl5.lang.perl.lexer;

import com.perl5.lang.perl.util.PerlSubUtil;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;


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

    protected int trenarCounter = 0;
%}


/*
// Char classes
*/
NEW_LINE = \r?\n

WHITE_SPACE     = [ \t\f]

EMPTY_SPACE = [ \t\f\r\n]
BAREWORD = [a-zA-Z_][a-zA-Z0-9_]*
BAREWORD_BRACED = "{"{WHITE_SPACE}*{BAREWORD}{WHITE_SPACE}*"}"

BAREWORD_MINUS = "-" * {BAREWORD}
BAREWORD_STRING_COMMA = {BAREWORD_MINUS}{EMPTY_SPACE}*"=>"

// bad solution, $scalar -function eats it
ANYWORD = [^ \t\f\r\n]

BUILT_IN_SCALAR_NAME = [1-9][0-9]*|"FORMAT_LINE_BREAK_CHARACTERS"|"EXCEPTIONS_BEING_CAUGHT"|"LAST_REGEXP_CODE_RESULT"|"OUTPUT_RECORD_SEPARATOR"|"INPUT_RECORD_SEPARATOR"|"OUTPUT_FIELD_SEPARATOR"|"FORMAT_LINES_PER_PAGE"|"SUBSCRIPT_SEPARATOR"|"^CHILD_ERROR_NATIVE"|"EFFECTIVE_GROUP_ID"|"FORMAT_PAGE_NUMBER"|"MULTILINE_MATCHING"|"^WIDE_SYSTEM_CALLS"|"EFFECTIVE_USER_ID"|"EXTENDED_OS_ERROR"|"FORMAT_LINES_LEFT"|"INPUT_LINE_NUMBER"|"OUTPUT_AUTO_FLUSH"|"LAST_MATCH_START"|"LAST_PAREN_MATCH"|"EXECUTABLE_NAME"|"FORMAT_FORMFEED"|"FORMAT_TOP_NAME"|"^RE_DEBUG_FLAGS"|"^RE_TRIE_MAXBUF"|"LAST_MATCH_END"|"LIST_SEPARATOR"|"REAL_GROUP_ID"|"SYSTEM_FD_MAX"|"^WARNING_BITS"|"INPLACE_EDIT"|"PERL_VERSION"|"PROGRAM_NAME"|"REAL_USER_ID"|"ACCUMULATOR"|"CHILD_ERROR"|"FORMAT_NAME"|"^UTF8LOCALE"|"EVAL_ERROR"|"PROCESS_ID"|"COMPILING"|"DEBUGGING"|"POSTMATCH"|"^ENCODING"|"BASETIME"|"OS_ERROR"|"OVERLOAD"|"PREMATCH"|"^UNICODE"|"WARNING"|"OSNAME"|"PERLDB"|"SUBSEP"|"^TAINT"|"ERRNO"|"MATCH"|"^OPEN"|"ARGV"|"EGID"|"EUID"|"OFMT"|"ARG"|"ENV"|"GID"|"INC"|"OFS"|"ORS"|"PID"|"SIG"|"UID"|"NR"|"RS"|"\""|"\\"|"^A"|"^C"|"^D"|"^E"|"^F"|"^H"|"^I"|"^L"|"^M"|"^N"|"^O"|"^P"|"^R"|"^S"|"^T"|"^V"|"^W"|"^X"|"!"|"$"|"%"|"&"|"'"|"("|")"|"+"|","|"-"|"."|"/"|"0"|";"|"<"|"="|">"|"@"|"["|"]"|"^"|"_"|"`"|"a"|"b"|"|"|"~"|"?"
PERL_SCALAR_BUILT_IN = "$" ("{" {BUILT_IN_SCALAR_NAME} "}" | {BUILT_IN_SCALAR_NAME} ) [^a-zA-Z0-9_]

BUILT_IN_ARRAY_NAME = "LAST_MATCH_START"|"EXPORT_TAGS"|"EXPORT_OK"|"OVERLOAD"|"EXPORT"|"ARGV"|"ENV"|"INC"|"ISA"|"SIG"|"^H"|"!"|"+"|"-"|"_"
PERL_ARRAY_BUILT_IN = "@" ("{" {BUILT_IN_ARRAY_NAME} "}" | {BUILT_IN_ARRAY_NAME} ) [^a-zA-Z0-9_]
PERL_ARRAY_INDEX_BUILT_IN = "$#" ("{" {BUILT_IN_ARRAY_NAME} "}" | {BUILT_IN_ARRAY_NAME} ) [^a-zA-Z0-9_]

BUILT_IN_HASH_NAME = "EXPORT_TAGS"|"OVERLOAD"|"ENV"|"INC"|"SIG"|"^H"|"!"|"+"|"-"
PERL_HASH_BUILT_IN = "%" ("{" {BUILT_IN_HASH_NAME} "}" | {BUILT_IN_HASH_NAME} ) [^a-zA-Z0-9_]

// http://perldoc.perl.org/perldata.html#Identifier-parsing
//PERL_XIDS = [\w && \p{XID_Start}]
//PERL_XIDC = [\w && \p{XID_Continue}]
PERL_XIDS = [_a-zA-Z]
PERL_XIDC = [_a-zA-Z0-9]


PERL_BASIC_IDENTIFIER = {PERL_XIDS} {PERL_XIDC}*
PERL_PACKAGE_CANONICAL = "::"* ({PERL_BASIC_IDENTIFIER} "::" +) + | "::"

// todo adjust in perl lexer too
PERL_PACKAGE_AMBIGUOUS = "::"* {PERL_BASIC_IDENTIFIER} ("::"+ {PERL_BASIC_IDENTIFIER})+

CAPTURE_HANDLE_READ = "<"{BAREWORD}">"


CHAR_ANY        = .|{NEW_LINE}
QUOTE           = "\"" | "'" | "`"

CAPTURE_LABEL_DEFINITION = {BAREWORD}{EMPTY_SPACE}*":"[^:]

PERL_VERSION_CHUNK = [0-9][0-9_]*
PERL_VERSION = "v"?{PERL_VERSION_CHUNK}("." {PERL_VERSION_CHUNK})*
// heading _ removed to avoid @_ parsing as sigil-number

NUMBER_EXP = [eE][+-]?[0-9_]+
NUMBER_FLOAT = "." [0-9][0-9_]*
NUMBER_INT = [0-9][0-9_] *  {NUMBER_FLOAT}? {NUMBER_EXP}?
NUMBER_SMALL = {NUMBER_FLOAT}{NUMBER_EXP}?
NUMBER_HEX = "0x" [0-9a-fA-F]+
NUMBER_BIN = "0b"[01]+
NUMBER = {NUMBER_HEX} | {NUMBER_BIN}| {NUMBER_INT} | {NUMBER_SMALL}

X_OP_STICKED = "x"[0-9]+[^a-zA-Z]*

PERL_OPERATORS =  ","  | "++" | "--" | "**" | "!" | "~" | "\\" | "+" | "-" | "=~" | "!~" | "<<" | ">>" | "<" | ">" | "<=" | ">=" | "==" | "!=" | "<=>" | "~~" | "|" | "^" | "&&" | "||" | "/" | ".." | "..." | "?" | "=" | "+=" | "-=" | "*="

// atm making the same, but seems unary are different
PERL_OPERATORS_FILETEST = "-" [rwxoRWXOezsfdlpSbctugkTBMAC]

HEREDOC_MARKER = [a-zA-Z0-9_]+
HEREDOC_OPENER = "<<"{WHITE_SPACE}* (\'{HEREDOC_MARKER}\' | \"{HEREDOC_MARKER}\" | \`{HEREDOC_MARKER}\` | {HEREDOC_MARKER})

%state LEX_BAREWORD_STRING
%state LEX_CODE

%state LEX_HEREDOC_WAITING
%xstate LEX_HEREDOC_MARKER, LEX_HEREDOC_OPENER

%xstate LEX_QUOTE_LIKE_OPENER, LEX_QUOTE_LIKE_CHARS, LEX_QUOTE_LIKE_CLOSER
%xstate LEX_QUOTE_LIKE_LIST_OPENER, LEX_QUOTE_LIKE_WORDS, LEX_QUOTE_LIKE_LIST_CLOSER
TRANS_MODIFIERS = [cdsr]
%xstate LEX_TRANS_OPENER, LEX_TRANS_CHARS, LEX_TRANS_CLOSER, LEX_TRANS_MODIFIERS
%xstate LEX_REGEX_OPENER
%xstate LEX_PREPARSED_ITEMS

%state LEX_SUB_DEFINITION
%xstate LEX_SUB_NAME
%xstate LEX_SUB_PROTOTYPE

%xstate LEX_BAREWORD_BRACED
%xstate LEX_BAREWORD_STRING_COMMA
%xstate LEX_PACKAGE_METHOD_CALL, LEX_PACKAGE_METHOD_CALL_VAR, LEX_PACKAGE_METHOD_CALL_FANCY
%xstate LEX_SUPER_METHOD_CALL
%xstate LEX_MAIN_FUNCTION_CALL
%xstate LEX_LABEL
%xstate LEX_LABEL_DEFINITION
%xstate LEX_METHOD_CALL
%xstate LEX_PACKAGE_FUNCTION_CALL
%xstate LEX_HANDLE_READ
%xstate LEX_HANDLE, LEX_HANDLE_HANDLE
%xstate LEX_HANDLE_FILETEST
%xstate LEX_VARIABLE
%state LEX_HTML_BLOCK
%%

//exclusive
<LEX_HEREDOC_OPENER>
{
    {WHITE_SPACE}+ {return TokenType.WHITE_SPACE;}
    {BAREWORD}     {popState();return PERL_STRING_CONTENT;}
    {QUOTE}        {popState();return processStringOpener();}
    [^]            {throw new Error("Can't be here");}
}

//exclusive
<LEX_HEREDOC_MARKER>{
    {ANYWORD}+   {popState();return PERL_HEREDOC_END;}
}

// exclusive
<LEX_HANDLE_READ>
{
    {BAREWORD} {endCustomBlock();return getHandleTokenType();}
    "<" {return PERL_LANGLE;}
    . {yypushback(1);endCustomBlock();break;}
}

// exclusive
<LEX_LABEL_DEFINITION>
{
    {BAREWORD} {
        if( trenarCounter > 0 )
        {
            endCustomBlock();
            return PERL_SUB;
        }
        else
            return PERL_LABEL;
    }
    ":" {endCustomBlock(); return PERL_COLON;}
    {NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}
    {WHITE_SPACE}+ {return TokenType.WHITE_SPACE;}
    . {yypushback(1);endCustomBlock();break;}
}

// exclusive
<LEX_BAREWORD_STRING_COMMA>
{
    {BAREWORD_MINUS}   {endCustomBlock(); return PERL_STRING_CONTENT; }
    . {yypushback(1);endCustomBlock();break;}
}


// exclusive
<LEX_BAREWORD_BRACED>
{
    "{" {return PERL_LBRACE;}
    {NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}
    {WHITE_SPACE}+ {return TokenType.WHITE_SPACE;}
    {BAREWORD}   {endCustomBlock();return guessBracedBareword(); }
    . {yypushback(1);endCustomBlock();break;}
}

// exclusive
<LEX_SUB_NAME>
{
    {PERL_PACKAGE_CANONICAL} {return getPackageTokenType();}
    {BAREWORD} {yybegin(LEX_SUB_DEFINITION);return getSubTokenType();}
    {NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}
    {WHITE_SPACE}+ {return TokenType.WHITE_SPACE;}
    .   {yypushback(1);yybegin(LEX_SUB_DEFINITION);break;}
}

// inclusive
<LEX_SUB_DEFINITION>
{
    "{" {popState();yypushback(1);break;}   // block of definition
    ";" {popState();yypushback(1);break;}   // end of declaration
    ":" {popState();yypushback(1);break;}   // attribute
    "(" {yybegin(LEX_SUB_PROTOTYPE);return PERL_LPAREN;}
}

// exclusive
<LEX_SUB_PROTOTYPE>
{
    ")" {popState();return PERL_RPAREN;}
    {NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}
    {WHITE_SPACE}+ {return TokenType.WHITE_SPACE;}
    .   {return PERL_SUB_PROTOTYPE_TOKEN; }
}

// inclusive states
{NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}

{WHITE_SPACE}+   {return TokenType.WHITE_SPACE;}
";"     {return processSemicolon();}

/**
**/
<LEX_REGEX_OPENER>{
    {EMPTY_SPACE}+  {return processOpenerWhiteSpace();}
    .   { return parseRegex(); }
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


///////////////////////// package definition ///////////////////////////////////////////////////////////////////////////

{HEREDOC_OPENER}   {return processHeredocOpener();}

{QUOTE}         {return processStringOpener();}

"?"             {trenarCounter++;return PERL_OPERATOR;}
":"             {trenarCounter--;return PERL_COLON;}
"->"            {return PERL_DEREFERENCE;}
"=>"            {return PERL_ARROW_COMMA; } // for barewords in array
","            {return PERL_COMMA; }
// todo issue #92
{BAREWORD_BRACED} {startCustomBlock(LEX_BAREWORD_BRACED);}   // disambiguates things like $var{m}
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

{PERL_SCALAR_BUILT_IN}      {yypushback(1);return parseBuiltInVariable();}
{PERL_ARRAY_BUILT_IN}       {yypushback(1);return parseBuiltInVariable();}
{PERL_ARRAY_INDEX_BUILT_IN} {yypushback(1);return parseBuiltInVariable();}
{PERL_HASH_BUILT_IN}        {yypushback(1);return parseBuiltInVariable();}

"@" {return PERL_SIGIL_ARRAY;}
"%" {return guessMod();}
"*" {return guessMul();}
"&" {return guessAmp();}
"$#" {return PERL_SIGIL_SCALAR_INDEX;}
"$" {return PERL_SIGIL_SCALAR;}

"sub" {pushState();yybegin(LEX_SUB_NAME);return getReservedTokenType();}
"/"   {   // regexp or div
    IElementType tokenType = guessDiv();
    if( tokenType == null )
        break;
    return tokenType;
}
{BAREWORD_STRING_COMMA} {startCustomBlock(LEX_BAREWORD_STRING_COMMA);break;}
{PERL_OPERATORS}    {return PERL_OPERATOR;}
{PERL_OPERATORS_FILETEST} {return PERL_OPERATOR_FILETEST;}
{X_OP_STICKED} {yypushback(yylength()-1);return PERL_OPERATOR_X;}
{CAPTURE_LABEL_DEFINITION} {startCustomBlock(LEX_LABEL_DEFINITION);break;}
{CAPTURE_HANDLE_READ} {startCustomBlock(LEX_HANDLE_READ);break;}

{PERL_PACKAGE_CANONICAL} {return getPackageTokenType();}
{BAREWORD} { return guessBareword();}
{PERL_PACKAGE_AMBIGUOUS} {
    IElementType tokenType = guessPackageName();
    if( tokenType != null )
        return  tokenType;
    break;
}

/* error fallback [^] */
[^]    { return TokenType.BAD_CHARACTER; }
