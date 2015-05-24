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
PERL_SCALAR_BUILT_IN = "$" ("{" {BUILT_IN_SCALAR_NAME} "}" | {BUILT_IN_SCALAR_NAME} )

BUILT_IN_ARRAY_NAME = "LAST_MATCH_START"|"EXPORT_TAGS"|"EXPORT_OK"|"OVERLOAD"|"EXPORT"|"ARGV"|"ENV"|"INC"|"ISA"|"SIG"|"^H"|"!"|"+"|"-"|"_"
PERL_ARRAY_BUILT_IN = "@" ("{" {BUILT_IN_ARRAY_NAME} "}" | {BUILT_IN_ARRAY_NAME} )

BUILT_IN_HASH_NAME = "EXPORT_TAGS"|"OVERLOAD"|"ENV"|"INC"|"SIG"|"^H"|"!"|"+"|"-"
PERL_HASH_BUILT_IN = "%" ("{" {BUILT_IN_HASH_NAME} "}" | {BUILT_IN_HASH_NAME} )

BUILT_IN_GLOB_NAME = "ARGVOUT"|"STDERR"|"STDOUT"|"STDIN"|"ARGV"
PERL_GLOB_BUILT_IN = "*" ("{" {BUILT_IN_GLOB_NAME} "}" | {BUILT_IN_GLOB_NAME} )

// http://perldoc.perl.org/perldata.html#Identifier-parsing
//PERL_XIDS = [\w && \p{XID_Start}]
//PERL_XIDC = [\w && \p{XID_Continue}]
PERL_XIDS = [_a-zA-Z]
PERL_XIDC = [_a-zA-Z0-9]


PERL_BASIC_IDENTIFIER = {PERL_XIDS} {PERL_XIDC}*
PERL_PACKAGE_CANONICAL = "::"* ({PERL_BASIC_IDENTIFIER} "::" +) + | "::"
PERL_IDENTIFIER = {PERL_PACKAGE_CANONICAL} * {PERL_BASIC_IDENTIFIER} | {PERL_PACKAGE_CANONICAL} {PERL_BASIC_IDENTIFIER} *
PERL_IDENTIFIER_VARIATION = "{" {EMPTY_SPACE}* {PERL_IDENTIFIER} {EMPTY_SPACE}* "}" | {PERL_IDENTIFIER}
// | "&"
PERL_SIGIL = ("$#" | "$" | "@" | "%" | "*" ) "$" *
CAPUTRE_PERL_VARIABLE = {PERL_SIGIL} {EMPTY_SPACE}* {PERL_IDENTIFIER_VARIATION}

//CAPTURE_PERL_SCALAR = "$" {EMPTY_SPACE}* {PERL_IDENTIFIER_VARIATION}
//CAPTURE_PERL_SCALAR_INDEX = "$#" {EMPTY_SPACE}* {PERL_IDENTIFIER_VARIATION}
//CAPTURE_PERL_ARRAY = "@" {EMPTY_SPACE}* {PERL_IDENTIFIER_VARIATION}
//CAPTURE_PERL_HASH = "%" {EMPTY_SPACE}* {PERL_IDENTIFIER_VARIATION}
//CAPTURE_PERL_GLOB = "*" {EMPTY_SPACE}* {PERL_IDENTIFIER_VARIATION}
//CAPTURE_PERL_CODE = "&" {EMPTY_SPACE}* {PERL_IDENTIFIER_VARIATION}

//PERL_VARIABLE_NAME = "::" ? {BAREWORD}("::" {BAREWORD})* "::" ?
//PERL_GLOB_VARIABLE_NAME = "::" ? {BAREWORD}("::" {BAREWORD})* "::" ?
//
//PERL_SCALAR_INDEX = "$#" {PERL_VARIABLE_NAME}
//
//// @todo think about this staff
//PERL_SCALAR = "$" {EMPTY_SPACE}* {PERL_VARIABLE_NAME}
//PERL_SCALAR_BRACES = "$" "{" {BAREWORD} "}"
//
//PERL_HASH = "%" {PERL_VARIABLE_NAME}
//PERL_HASH_BRACES = "%{" {BAREWORD} "}"
//
//PERL_ARRAY = "@" {PERL_VARIABLE_NAME}
//PERL_ARRAY_BRACES = "@{" {BAREWORD} "}"
//
//PERL_GLOB = "*" {PERL_GLOB_VARIABLE_NAME}
//PERL_GLOB_BRACES = "*{" {BAREWORD} "}"

VARIABLE_DECLARATOR = "my" | "state" | "our" | "local"
PRE_PACKAGE_SURE = "use" | "no" | "package" | {VARIABLE_DECLARATOR}
PERL_PACKAGE_SURE = "::" * {PERL_BASIC_IDENTIFIER} ("::" * {PERL_BASIC_IDENTIFIER}) *
CAPTURE_SURE_PACKAGE = {PRE_PACKAGE_SURE}{EMPTY_SPACE}+{PERL_PACKAGE_SURE}[^\.a-zA-Z0-9_]
CAPTURE_REQUIRE_PACKAGE = "require"{EMPTY_SPACE}+{PERL_PACKAGE_SURE}{EMPTY_SPACE}*[^\(:\-a-zA-Z0-9_]

PERL_PACKAGE_METHOD = {PERL_PACKAGE_CANONICAL} {BAREWORD}

CAPTURE_HANDLE_READ = "<"{BAREWORD}">"

DIR_HANDLE_PREFIX = "opendir" | "chdir" | "telldir" | "seekdir" | "rewinddir" | "readdir" | "closedir"
FILE_HANDLE_PREFIX_SYS = "sysopen" | "syswrite" | "sysseek" | "sysread"
FILE_HANDLE_PREFIX = "open" | "close" | "read" | "write" | "stat" | "ioctl" | "fcntl" | "lstat" | "truncate" | "tell" | "select" | "seek" | "getc" | "flock" | "fileno" | "eof" | "binmode"
HANDLE_PREFIX = {DIR_HANDLE_PREFIX} | {FILE_HANDLE_PREFIX} | {FILE_HANDLE_PREFIX_SYS}
HANDLE_PRINT_PREFIX = "say" | "print" | "printf"


CAPTURE_HANDLE_PRINT = {HANDLE_PRINT_PREFIX} ({EMPTY_SPACE}+ | {EMPTY_SPACE}* "(" {EMPTY_SPACE}*) {BAREWORD} {EMPTY_SPACE}+ [^{(:,\-\t\n\r\f ]
CAPTURE_HANDLE = {HANDLE_PREFIX} ({EMPTY_SPACE}+ | {EMPTY_SPACE}* "(" {EMPTY_SPACE}*) {BAREWORD} {EMPTY_SPACE}* [,)]
CAPTURE_HANDLE_FILETEST = {FILETEST} {EMPTY_SPACE}+ {BAREWORD} [^\(:\-a-zA-Z0-9_]

// ::main_func
CAPTURE_MAIN_CALL = "::"{BAREWORD}

// ->method
CAPTURE_METHOD_CALL = "->"{BAREWORD}

// ->SUPER::method
CAPTURE_SUPER_METHOD_CALL = "->"{EMPTY_SPACE}*"SUPER::"

// package->method
CAPTURE_PACKAGE_METHOD_CALL = {PERL_PACKAGE_METHOD}{EMPTY_SPACE}*"->"{EMPTY_SPACE}*{BAREWORD}

// package->$method
CAPTURE_PACKAGE_METHOD_CALL_VAR = {PERL_PACKAGE_METHOD}{EMPTY_SPACE}*"->"{EMPTY_SPACE}*"$"

CHAR_ANY        = .|{NEW_LINE}
FULL_LINE       = .*{NEW_LINE}?
QUOTE           = "\"" | "'" | "`"
END_OF_LINE_COMMENT = "#" [^\r\n] *

PERL_LABEL_PREFIX = "goto" | "next" | "last" | "redo"
// here can be parentesis
CAPTURE_LABEL = {PERL_LABEL_PREFIX}{EMPTY_SPACE}+{BAREWORD}

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

THE_END         = __END__
THE_DATA        = __DATA__

BLOCK_NAMES = "BEGIN" | "UNITCHECK" | "CHECK" | "INIT" | "END"

PERL_OPERATORS =  ","  | "++" | "--" | "**" | "!" | "~" | "\\" | "+" | "-" | "=~" | "!~" | "*" | "%"  | "<<" | ">>" | "<" | ">" | "<=" | ">=" | "==" | "!=" | "<=>" | "~~" | "&" | "|" | "^" | "&&" | "||" | "/" | ".." | "..." | "?" | "=" | "+=" | "-=" | "*="

// atm making the same, but seems unary are different
FILETEST = "-" [rwxoRWXOezsfdlpSbctugkTBMAC]
PERL_OPERATORS_FILETEST = {FILETEST} [^a-zA-Z0-9_]

HEREDOC_MARKER = [a-zA-Z0-9_]+
HEREDOC_OPENER = "<<"{WHITE_SPACE}* (\'{HEREDOC_MARKER}\' | \"{HEREDOC_MARKER}\" | \`{HEREDOC_MARKER}\` | {HEREDOC_MARKER})

QUOTE_LIST_FUNCTIONS = "qw"
QUOTE_FUNCTIONS = "qq" | "qx" | "q"
TRANS_FUNCTIONS = "tr" | "y"
REGEX_FUNCTIONS = "s" | "qr" | "m"
PERL_SYN_QUOTE_LIKE = {QUOTE_LIST_FUNCTIONS} | {QUOTE_FUNCTIONS} | {TRANS_FUNCTIONS} | {REGEX_FUNCTIONS}

PERL_SYN_INCLUDE = "use" | "require" | "no"
PERL_SYN_BLOCK_OP = "do" | "eval"
PERL_SYN_FLOW_CONTROL = "redo" | "next" | "last"
PERL_SYN_DECLARE = "package" | "sub" | {VARIABLE_DECLARATOR}
PERL_SYN_COMPOUND = "if" | "unless" | "given" | "while" | "until" | "for" | "foreach" | "elsif" | "else" | "continue"
// "print" | "say" | "open"

PERL_SYN_OTHER = "undef" | "grep" | "sort" | "map" | "close"
PERL_SYN_BINARY = "not" | "and" | "or" | "xor" | "x" | "lt" | "gt" | "le" | "ge" | "eq" | "ne" | "cmp"
PERL_SYN_UNARY = "defined" | "ref" | "exists" | "scalar"
FUNCTION_SPECIAL = {PERL_SYN_BLOCK_OP} | {PERL_SYN_INCLUDE} | {PERL_SYN_QUOTE_LIKE} | {PERL_SYN_FLOW_CONTROL} | {PERL_SYN_OTHER} | {PERL_SYN_BINARY}


PERL_TAGS = "__FILE__" | "__LINE__" | "__PACKAGE__" | "__SUB__"

//PERL_OPENER = "<?"
//PERL_CLOSER = "?>"
//HTML_BLOCK_START = ~{PERL_OPENER}
//HTML_BLOCK_MID = {PERL_CLOSER}~{PERL_OPENER}
//HTML_BLOCK_END = {PERL_CLOSER}~<<eof>>

%xstate LEX_EOF

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
%xstate LEX_SURE_PACKAGE, LEX_SURE_PACKAGE_PACKAGE
%xstate LEX_PACKAGE_METHOD_CALL, LEX_PACKAGE_METHOD_CALL_VAR
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
<LEX_VARIABLE>
{
    "$#"    {return PERL_SIGIL_SCALAR_INDEX;}
    "$"    {return PERL_SIGIL_SCALAR;}
    "@"    {return PERL_SIGIL_ARRAY;}
    "%"    {return PERL_SIGIL_HASH;}
    "*"    {return PERL_OPERATOR;}
    "&"    {return PERL_OPERATOR;}
    "{"     {return PERL_LBRACE;}
    "}"     {return PERL_RBRACE;}
    {NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}

    // we should control, where whitespace is allowed
    {WHITE_SPACE}+ {return TokenType.WHITE_SPACE;}
    {PERL_PACKAGE_CANONICAL} {return PerlPackageUtil.getPackageType(yytext().toString());}
    {PERL_BASIC_IDENTIFIER} {endCustomBlock();return PERL_VARIABLE_NAME;}
    [^] {yypushback(1);endCustomBlock();break;}
}

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
<LEX_HANDLE_HANDLE>
{
    "(" {return PERL_LPAREN;}
    {VARIABLE_DECLARATOR} {endCustomBlock();return PERL_FUNCTION_BUILT_IN;}
    {BAREWORD} {endCustomBlock();return PERL_HANDLE;}
    {NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}
    {WHITE_SPACE}+ {return TokenType.WHITE_SPACE;}
    . {yypushback(1);endCustomBlock();break;}
}

// exclusive
<LEX_HANDLE>
{
    {BAREWORD} {yybegin(LEX_HANDLE_HANDLE);return PERL_FUNCTION_BUILT_IN;}
    . {yypushback(1);endCustomBlock();break;}
}

// exclusive
<LEX_HANDLE_FILETEST>
{
    {PERL_OPERATORS_FILETEST} {yypushback(1);return PERL_OPERATOR_FILETEST;}
    {BAREWORD} {endCustomBlock();return PERL_HANDLE;}
    {NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}
    {WHITE_SPACE}+ {return TokenType.WHITE_SPACE;}
    . {yypushback(1);endCustomBlock();break;}
}

// exclusive
<LEX_HANDLE_READ>
{
    {BAREWORD} {endCustomBlock();return PERL_HANDLE;}
    "<" {return PERL_LANGLE;}
    . {yypushback(1);endCustomBlock();break;}
}

// exclusive
<LEX_PACKAGE_FUNCTION_CALL>
{
    {BAREWORD} {endCustomBlock();return PERL_FUNCTION;}
    {PERL_PACKAGE_CANONICAL} {return PerlPackageUtil.getPackageType(yytext().toString());}

    {NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}
    {WHITE_SPACE}+ {return TokenType.WHITE_SPACE;}
    . {yypushback(1);endCustomBlock();break;}
}

// exclusive
<LEX_METHOD_CALL>
{
    "->" {return PERL_DEREFERENCE;}
    {BAREWORD} {endCustomBlock();return PERL_FUNCTION;}

    {NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}
    {WHITE_SPACE}+ {return TokenType.WHITE_SPACE;}
    . {yypushback(1);endCustomBlock();break;}
}

// exclusive
<LEX_LABEL>
{
    {PERL_LABEL_PREFIX} {return PERL_KEYWORD;}
    {FUNCTION_SPECIAL} {endCustomBlock();return PERL_KEYWORD;}
    {PERL_SYN_COMPOUND} {endCustomBlock();return PERL_RESERVED;}
    {BAREWORD} {endCustomBlock(); return PERL_LABEL;}

    {NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}
    {WHITE_SPACE}+ {return TokenType.WHITE_SPACE;}
    . {yypushback(1);endCustomBlock();break;}
}

// exclusive
<LEX_LABEL_DEFINITION>
{
    {BAREWORD} {
        if( trenarCounter > 0 )
        {
            endCustomBlock();
            return PerlFunctionUtil.getFunctionType(yytext().toString());
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
<LEX_MAIN_FUNCTION_CALL>
{
    "::" {return PERL_PACKAGE_BUILT_IN;}
    {BAREWORD} {endCustomBlock(); return PERL_FUNCTION;}
    . {yypushback(1);endCustomBlock();break;}
}

// exclusive
<LEX_SUPER_METHOD_CALL>
{
    "->" {return PERL_DEREFERENCE;}
    "SUPER::" {return PERL_PACKAGE;}
    {BAREWORD} {endCustomBlock();return PERL_FUNCTION;}
    {NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}
    {WHITE_SPACE}+ {return TokenType.WHITE_SPACE;}
    . {yypushback(1);endCustomBlock();break;}
}

// exclusive
<LEX_PACKAGE_METHOD_CALL>
{
    "->" {return PERL_DEREFERENCE;}
    {BAREWORD} {endCustomBlock();return PERL_FUNCTION;}
    {PERL_PACKAGE_SURE} {return PerlPackageUtil.getPackageType(yytext().toString()); }
    {NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}
    {WHITE_SPACE}+ {return TokenType.WHITE_SPACE;}
    . {yypushback(1);endCustomBlock();break;}
}

// exclusive
<LEX_PACKAGE_METHOD_CALL_VAR>
{
    {PERL_PACKAGE_SURE} {endCustomBlock();return PerlPackageUtil.getPackageType(yytext().toString()); }
}


<LEX_SURE_PACKAGE_PACKAGE>
{
   {PERL_PACKAGE_SURE} {endCustomBlock();return PerlPackageUtil.getPackageType(yytext().toString());}

   {NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}
   {WHITE_SPACE}+ {return TokenType.WHITE_SPACE;}
    . {yypushback(1);endCustomBlock();break;}
}


// exclusive
<LEX_SURE_PACKAGE>
{
    {PRE_PACKAGE_SURE}|"require"    {yybegin(LEX_SURE_PACKAGE_PACKAGE);return PERL_RESERVED;}
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
    {BAREWORD}   {endCustomBlock();return PERL_STRING_CONTENT; }
    . {yypushback(1);endCustomBlock();break;}
}

// exclusive
<LEX_SUB_NAME>
{
    {PERL_PACKAGE_CANONICAL} {return PerlPackageUtil.getPackageType(yytext().toString());}
    {BAREWORD} {yybegin(LEX_SUB_DEFINITION);return PERL_FUNCTION;}
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

{END_OF_LINE_COMMENT}  { return PERL_COMMENT; }

<YYINITIAL, LEX_CODE>{
    {THE_END}               {processDataOpener(); break;}
    {THE_DATA}               {processDataOpener(); break;}
}

/**
**/
<LEX_REGEX_OPENER>{
    {EMPTY_SPACE}+  {return processOpenerWhiteSpace();}
    .   { return parseRegex(); }
}

<LEX_PREPARSED_ITEMS>{
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


///////////////////////// package definition ///////////////////////////////////////////////////////////////////////////

{HEREDOC_OPENER}   {return processHeredocOpener();}

{QUOTE}         {return processStringOpener();}

"?"             {trenarCounter++;return PERL_OPERATOR;}
":"             {trenarCounter--;return PERL_COLON;}
"->"            {return PERL_DEREFERENCE;}
"=>"            {return PERL_ARROW_COMMA; } // for barewords in array
","            {return PERL_COMMA; }
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
//{PERL_SCALAR_INDEX} {return PERL_SCALAR_INDEX;}

{PERL_SCALAR_BUILT_IN} {return PERL_SCALAR_BUILT_IN;}
{PERL_ARRAY_BUILT_IN} {return PERL_ARRAY_BUILT_IN;}
{PERL_HASH_BUILT_IN} {return PERL_HASH_BUILT_IN;}
{PERL_GLOB_BUILT_IN} {return PERL_GLOB_BUILT_IN;}

//{PERL_SCALAR} {return PERL_SCALAR;}
//{PERL_ARRAY} {return PERL_ARRAY;}
//{PERL_HASH} {return PERL_HASH;}
//{PERL_GLOB} {return PERL_GLOB;}
//
//{PERL_SCALAR_BRACES} {return PERL_SCALAR;}
//{PERL_ARRAY_BRACES} {return PERL_ARRAY;}
//{PERL_HASH_BRACES} {return PERL_HASH;}
//{PERL_GLOB_BRACES} {return PERL_GLOB;}

"@" {return PERL_SIGIL_ARRAY;}
"%" {return PERL_SIGIL_HASH;}
"$#" {return PERL_SIGIL_SCALAR_INDEX;}
"$" {return PERL_SIGIL_SCALAR;}
{CAPUTRE_PERL_VARIABLE} {startCustomBlock(LEX_VARIABLE);break;}

"sub" {pushState();yybegin(LEX_SUB_NAME);return PERL_KEYWORD;}
{CAPTURE_PACKAGE_METHOD_CALL} {startCustomBlock(LEX_PACKAGE_METHOD_CALL);break;}
{CAPTURE_PACKAGE_METHOD_CALL_VAR} {startCustomBlock(LEX_PACKAGE_METHOD_CALL_VAR);break;}
{CAPTURE_SUPER_METHOD_CALL} {startCustomBlock(LEX_SUPER_METHOD_CALL);break;}
{CAPTURE_SURE_PACKAGE} {startCustomBlock(LEX_SURE_PACKAGE);break;}
{CAPTURE_REQUIRE_PACKAGE} {startCustomBlock(LEX_SURE_PACKAGE);break;}

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

{BAREWORD_STRING_COMMA} {startCustomBlock(LEX_BAREWORD_STRING_COMMA);break;}

{PERL_OPERATORS}    {return PERL_OPERATOR;}
{PERL_OPERATORS_FILETEST} {yypushback(1);return PERL_OPERATOR_FILETEST;}
{PERL_SYN_UNARY} {return PERL_OPERATOR_UNARY;}

{PERL_SYN_COMPOUND} {return PERL_RESERVED;}
{PERL_SYN_DECLARE} {return PERL_RESERVED;}
{FUNCTION_SPECIAL} {return PERL_KEYWORD;}
{BLOCK_NAMES} {return PERL_BLOCK_NAME;}

<LEX_BAREWORD_STRING>
{
    {BAREWORD} { yybegin(YYINITIAL); return PERL_STRING_CONTENT;}
}

{X_OP_STICKED} {yypushback(yylength()-1);return PERL_OPERATOR;}

{CAPTURE_MAIN_CALL} {startCustomBlock(LEX_MAIN_FUNCTION_CALL);break;}
{CAPTURE_LABEL_DEFINITION} {startCustomBlock(LEX_LABEL_DEFINITION);break;}
{CAPTURE_LABEL} {startCustomBlock(LEX_LABEL);break;}
{CAPTURE_METHOD_CALL} {startCustomBlock(LEX_METHOD_CALL);break;}
{PERL_PACKAGE_METHOD} {startCustomBlock(LEX_PACKAGE_FUNCTION_CALL);break;}

{PERL_PACKAGE_CANONICAL} {return PerlPackageUtil.getPackageType(yytext().toString());}
{BUILT_IN_GLOB_NAME} {return PERL_HANDLE_BUILT_IN;}
{CAPTURE_HANDLE_READ} {startCustomBlock(LEX_HANDLE_READ);break;}
{CAPTURE_HANDLE_FILETEST} {startCustomBlock(LEX_HANDLE_FILETEST);break;}
{CAPTURE_HANDLE_PRINT} {startCustomBlock(LEX_HANDLE);break;}
{CAPTURE_HANDLE} {startCustomBlock(LEX_HANDLE);break;}
{BAREWORD} { return PerlFunctionUtil.getFunctionType(yytext().toString());}

/* error fallback [^] */
[^]    { return TokenType.BAD_CHARACTER; }
