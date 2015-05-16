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

BUILT_IN_SCALAR_NAME = [1-9][0-9]*|"FORMAT_LINE_BREAK_CHARACTERS"|"EXCEPTIONS_BEING_CAUGHT"|"LAST_REGEXP_CODE_RESULT"|"OUTPUT_RECORD_SEPARATOR"|"INPUT_RECORD_SEPARATOR"|"OUTPUT_FIELD_SEPARATOR"|"FORMAT_LINES_PER_PAGE"|"SUBSCRIPT_SEPARATOR"|"^CHILD_ERROR_NATIVE"|"EFFECTIVE_GROUP_ID"|"FORMAT_PAGE_NUMBER"|"MULTILINE_MATCHING"|"^WIDE_SYSTEM_CALLS"|"EFFECTIVE_USER_ID"|"EXTENDED_OS_ERROR"|"FORMAT_LINES_LEFT"|"INPUT_LINE_NUMBER"|"OUTPUT_AUTO_FLUSH"|"LAST_MATCH_START"|"LAST_PAREN_MATCH"|"EXECUTABLE_NAME"|"FORMAT_FORMFEED"|"FORMAT_TOP_NAME"|"^RE_DEBUG_FLAGS"|"^RE_TRIE_MAXBUF"|"LAST_MATCH_END"|"LIST_SEPARATOR"|"REAL_GROUP_ID"|"SYSTEM_FD_MAX"|"^WARNING_BITS"|"INPLACE_EDIT"|"PERL_VERSION"|"PROGRAM_NAME"|"REAL_USER_ID"|"ACCUMULATOR"|"CHILD_ERROR"|"FORMAT_NAME"|"^UTF8LOCALE"|"EVAL_ERROR"|"PROCESS_ID"|"COMPILING"|"DEBUGGING"|"POSTMATCH"|"^ENCODING"|"BASETIME"|"OS_ERROR"|"OVERLOAD"|"PREMATCH"|"^UNICODE"|"WARNING"|"OSNAME"|"PERLDB"|"SUBSEP"|"^TAINT"|"ERRNO"|"MATCH"|"^OPEN"|"ARGV"|"EGID"|"EUID"|"OFMT"|"ARG"|"ENV"|"GID"|"INC"|"OFS"|"ORS"|"PID"|"SIG"|"UID"|"NR"|"RS"|"\""|"\\"|"^A"|"^C"|"^D"|"^E"|"^F"|"^H"|"^I"|"^L"|"^M"|"^N"|"^O"|"^P"|"^R"|"^S"|"^T"|"^V"|"^W"|"^X"|"!"|"$"|"%"|"&"|"'"|"("|")"|"+"|","|"-"|"."|"/"|"0"|":"|";"|"<"|"="|">"|"?"|"@"|"["|"]"|"^"|"_"|"`"|"a"|"b"|"|"|"~"
PERL_SCALAR_BUILT_IN = "$" ("{" {BUILT_IN_SCALAR_NAME} "}" | {BUILT_IN_SCALAR_NAME} )

BUILT_IN_ARRAY_NAME = "LAST_MATCH_START"|"EXPORT_TAGS"|"EXPORT_OK"|"OVERLOAD"|"EXPORT"|"ARGV"|"ENV"|"INC"|"ISA"|"SIG"|"^H"|"!"|"+"|"-"|"_"
PERL_ARRAY_BUILT_IN = "@" ("{" {BUILT_IN_ARRAY_NAME} "}" | {BUILT_IN_ARRAY_NAME} )

BUILT_IN_HASH_NAME = "EXPORT_TAGS"|"OVERLOAD"|"ENV"|"INC"|"SIG"|"^H"|"!"|"+"|"-"
PERL_HASH_BUILT_IN = "%" ("{" {BUILT_IN_HASH_NAME} "}" | {BUILT_IN_HASH_NAME} )

BUILT_IN_GLOB_NAME = "ARGVOUT"|"STDERR"|"STDOUT"|"STDIN"|"ARGV"
PERL_GLOB_BUILT_IN = "*" ("{" {BUILT_IN_GLOB_NAME} "}" | {BUILT_IN_GLOB_NAME} )

PERL_SCALAR = "$"+ "::" ? {BAREWORD}("::" {BAREWORD})*
PERL_SCALAR_BRACES = "$"+ "{" {BAREWORD} "}"

PERL_HASH = "%" "::" ? {BAREWORD}("::" {BAREWORD})*
PERL_HASH_BRACES = "%{" {BAREWORD} "}"

PERL_ARRAY = "@" "::" ? {BAREWORD}("::" {BAREWORD})*
PERL_ARRAY_BRACES = "@{" {BAREWORD} "}"

PERL_GLOB = "*" "::" ? {BAREWORD}("::" {BAREWORD})*
PERL_GLOB_BRACES = "*{" {BAREWORD} "}"

CHAR_ANY        = .|{NEW_LINE}
FULL_LINE       = .*{NEW_LINE}?
QUOTE           = "\"" | "'" | "`"
END_OF_LINE_COMMENT = "#" [^\r\n] *

PERL_VERSION_CHUNK = [0-9][0-9_]*
PERL_VERSION = "v"?{PERL_VERSION_CHUNK}("." {PERL_VERSION_CHUNK})*
// heading _ removed to avoid @_ parsing as sigil-number

NUMBER_EXP = [eE][+-]?[0-9_]+
NUMBER_FLOAT = "." [0-9_]+
NUMBER_INT = [0-9][0-9_] *  {NUMBER_FLOAT}?{NUMBER_EXP}?
NUMBER_HEX = "0x" [0-9a-fA-F]+
NUMBER_BIN = "0b"[01]+
NUMBER = {NUMBER_HEX} | {NUMBER_BIN}| {NUMBER_INT}

X_OP_STICKED = "x"[0-9]+[^a-zA-Z]*

THE_END         = __END__
THE_DATA        = __DATA__

BLOCK_NAMES = "BEGIN" | "UNITCHECK" | "CHECK" | "INIT" | "END"

PERL_OPERATORS =  ","  | "++" | "--" | "**" | "!" | "~" | "\\" | "+" | "-" | "=~" | "!~" | "*" | "%"  | "<<" | ">>" | "<" | ">" | "<=" | ">=" | "==" | "!=" | "<=>" | "~~" | "&" | "|" | "^" | "&&" | "||" | "/" | ".." | "..." | "?" | "=" | "+=" | "-=" | "*="

// atm making the same, but seems unary are different
PERL_OPERATORS_FILETEST = "-" [rwxoRWXOezsfdlpSbctugkTBMAC] [^a-zA-Z0-9_]

MULTILINE_OPENER_SQ = "<<"{WHITE_SPACE}*\'{ANYWORD}+\'
MULTILINE_OPENER_DQ = "<<"{WHITE_SPACE}*\"{ANYWORD}+\"
MULTILINE_OPENER_DX = "<<"{WHITE_SPACE}*\`{ANYWORD}+\`
MULTILINE_OPENER_DQ_BARE = "<<"{WHITE_SPACE}*{BAREWORD}

POD_OPEN         = \=(pod|head1|head2|head3|head4|over|item|back|begin|end|for|encoding){FULL_LINE}
POD_CLOSE       = \="cut"{FULL_LINE}

QUOTE_LIST_FUNCTIONS = "qw"
QUOTE_FUNCTIONS = "qq" | "qx" | "q"
TRANS_FUNCTIONS = "tr" | "y"
REGEX_FUNCTIONS = "s" | "qr" | "m"
PERL_SYN_QUOTE_LIKE = {QUOTE_LIST_FUNCTIONS} | {QUOTE_FUNCTIONS} | {TRANS_FUNCTIONS} | {REGEX_FUNCTIONS}

PERL_SYN_INCLUDE = "use" | "require" | "no"
PERL_SYN_BLOCK_OP = "do" | "eval"
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

%xstate LEX_QUOTE_LIKE_OPENER, LEX_QUOTE_LIKE_CHARS, LEX_QUOTE_LIKE_CLOSER
%xstate LEX_QUOTE_LIKE_LIST_OPENER, LEX_QUOTE_LIKE_WORDS, LEX_QUOTE_LIKE_LIST_CLOSER
TRANS_MODIFIERS = [cdsr]
%xstate LEX_TRANS_OPENER, LEX_TRANS_CHARS, LEX_TRANS_CLOSER, LEX_TRANS_MODIFIERS
%xstate LEX_REGEX_OPENER
%xstate LEX_PREPARSED_ITEMS

%state LEX_SUB_DEFINITION
%xstate LEX_SUB_PROTOTYPE

%%

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
    {NEW_LINE}   {
        IElementType tokenType = processNewLine();
        if( tokenType != null )
           return tokenType;
        break;
    }
    {WHITE_SPACE}+ {return TokenType.WHITE_SPACE;}
    .   {return PERL_SUB_PROTOTYPE_TOKEN; }
}

// inclusive states
{NEW_LINE}   {
    IElementType tokenType = processNewLine();
    if( tokenType != null )
       return tokenType;
    break;
}

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

{PERL_SCALAR_BUILT_IN} {return PERL_SCALAR_BUILT_IN;}
{PERL_ARRAY_BUILT_IN} {return PERL_ARRAY_BUILT_IN;}
{PERL_HASH_BUILT_IN} {return PERL_HASH_BUILT_IN;}
{PERL_GLOB_BUILT_IN} {return PERL_GLOB_BUILT_IN;}

{PERL_SCALAR} {return PERL_SCALAR;}
{PERL_ARRAY} {return PERL_ARRAY;}
{PERL_HASH} {return PERL_HASH;}
{PERL_GLOB} {return PERL_GLOB;}

{PERL_SCALAR_BRACES} {return PERL_SCALAR;}
{PERL_ARRAY_BRACES} {return PERL_ARRAY;}
{PERL_HASH_BRACES} {return PERL_HASH;}
{PERL_GLOB_BRACES} {return PERL_GLOB;}

"@" {return PERL_SIGIL_ARRAY;}
"%" {return PERL_SIGIL_HASH;}
"$#" {return PERL_SIGIL_SCALAR_INDEX;}
"$" {return PERL_SIGIL_SCALAR;}

"sub" {pushState();yybegin(LEX_SUB_DEFINITION);return PERL_KEYWORD;}
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

{X_OP_STICKED} {yypushback(yylength()-1);return PERL_OPERATOR;}
{BAREWORD} { return PERL_BAREWORD;}

/* error fallback [^] */
[^]    { return TokenType.BAD_CHARACTER; }
