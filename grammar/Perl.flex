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

// http://perldoc.perl.org/perlop.html#Gory-details-of-parsing-quoted-constructs
// http://perldoc.perl.org/perldata.html#Identifier-parsing

%class PerlLexerGenerated
%extends PerlBaseLexer
%abstract
%unicode
%public


%function perlAdvance
%type IElementType


%{

    protected int trenarCounter = 0;

	public abstract IElementType startRegexp();
	public abstract IElementType getIdentifierToken();
	public abstract IElementType captureString();
	public abstract boolean captureInterpolatedCode();

%}


/*
// Char classes
*/
NEW_LINE = \R
WHITE_SPACE     = [ \t\f]
ANY_SPACE = [ \t\f\n\r]
LINE_COMMENT = "#" .* \R
SPACES_OR_COMMENTS = ({ANY_SPACE}|{LINE_COMMENT})*
ESCAPED_WHITE_SPACE="\\"{WHITE_SPACE}
ESCAPED_CHARACTER = "\\"({ANY_SPACE}|"#")

// http://perldoc.perl.org/perldata.html#Identifier-parsing
PERL_XIDS = [\w && \p{XID_Start}\d_] // seems in java \d does not matches XID_Start
PERL_XIDC = [\w && \p{XID_Continue}]

IDENTIFIER = {PERL_XIDS} {PERL_XIDC}*

FARROW = "=>"
BAREWORD_MINUS = "-" ? {IDENTIFIER}

// qualified identifer can't start with ', but variable can
QUALIFIED_IDENTIFIER = ("::"+ "'" ?) ? {IDENTIFIER} (("::"+ "'" ? | "::"* "'" ) {IDENTIFIER} )*  "::" *
VARIABLE_QUALIFIED_IDENTIFIER = ("::"* "'" ?) ? {IDENTIFIER} (("::"+ "'" ? | "::"* "'" ) {IDENTIFIER} )*  "::" *

DQ_STRING = "\"" ([^\"]|"\\\\"|"\\\"" )* "\""?
SQ_STRING = "\'" ([^\']|"\\\\"|"\\\'" )* "\'"?
XQ_STRING = "\`" ([^\`]|"\\\\"|"\\\`" )* "\`"?

QUOTE_LIKE_SUFFIX= ("'" {QUALIFIED_IDENTIFIER} ? )?
CORE_PREFIX = "CORE::"?

PERL_VERSION_CHUNK = [0-9][0-9_]*
PERL_VERSION = "v"?{PERL_VERSION_CHUNK}("." {PERL_VERSION_CHUNK})*
// heading _ removed to avoid @_ parsing as sigil-number

// duplicated in Pod lexer
NUMBER_EXP = [eE][+-]?[0-9_]+
NUMBER_INT = [0-9][0-9_]*
NUMBER_HEX = "0"[xX][0-9a-fA-F_]+
NUMBER_BIN = "0"[bB][01_]+

SPECIAL_VARIABLE_NAME = [\"\'\[\]\`\\\!\%\&\(\)\+\,\-\.\/\;\<\=\>\|\~\?\:\*\^\@\_\$]
CAPPED_SINGLE_LETTER_VARIABLE_NAME = "^"[\]\[A-Z\^_?\\]
VARIABLE_NAME = {VARIABLE_QUALIFIED_IDENTIFIER} | "::" | {CAPPED_SINGLE_LETTER_VARIABLE_NAME} | {SPECIAL_VARIABLE_NAME}
CAPPED_BRACED_VARIABLE = {CAPPED_SINGLE_LETTER_VARIABLE_NAME}[\w_]*

// atm making the same, but seems unary are different
PERL_OPERATORS_FILETEST = "-" [rwxoRWXOezsfdlpSbctugkTBMAC][^a-zA-Z0-9_]

HEREDOC_MARKER_DQ = [^\"\n\r]*
HEREDOC_MARKER_SQ = [^\'\n\r]*
HEREDOC_MARKER_XQ = [^\`\n\r]*
QUOTED_HEREDOC_MARKER = {WHITE_SPACE}*(\'{HEREDOC_MARKER_SQ}\'| \"{HEREDOC_MARKER_DQ}\" | \`{HEREDOC_MARKER_XQ}\`)
UNQUOTED_HEREDOC_MARKER = [\w_]{IDENTIFIER}?

END_BLOCK = "__END__" [^]+
DATA_BLOCK = "__DATA__" [^] +

SUB_PROTOTYPE = [\s\[$@%&*\];+]+

POD_START = "="[\w].* {NEW_LINE} ?
POD_LINE = (.+ {NEW_LINE} ? | {NEW_LINE})
POD_END = "=cut" ({WHITE_SPACE} .*)?

NAMED_UNARY_OPERATORS = "values"|"umask"|"ucfirst"|"uc"|"study"|"srand"|"sqrt"|"sleep"|"sin"|"shift"|"setservent"|"setprotoent"|"setnetent"|"sethostent"|"rmdir"|"reset"|"ref"|"readpipe"|"readlink"|"readline"|"rand"|"quotemeta"|"prototype"|"pop"|"ord"|"oct"|"log"|"localtime"|"length"|"lcfirst"|"lc"|"keys"|"int"|"hex"|"gmtime"|"getsockname"|"getpwuid"|"getpwnam"|"getprotobyname"|"getpgrp"|"getpeername"|"getnetbyname"|"gethostbyname"|"getgrnam"|"getgrgid"|"fc"|"exp"|"exit"|"exists"|"evalbytes"|"each"|"defined"|"cos"|"chroot"|"chr"|"caller"|"alarm"|"abs"
BARE_HANDLE_ACCEPTORS = "truncate"|"syswrite"|"sysseek"|"sysread"|"sysopen"|"select"|"seekdir"|"seek"|"read"|"opendir"|"open"|"ioctl"|"flock"|"fcntl"|"binmode"
NAMED_UNARY_BARE_HANDLE_ACCEPTORS = "write"|"telldir"|"tell"|"stat"|"rewinddir"|"readdir"|"lstat"|"getc"|"fileno"|"eof"|"closedir"|"close"|"chdir"

BLOCK_NAMES = "BEGIN"|"UNITCHECK"|"CHECK"|"INIT"|"END"|"AUTOLOAD"|"DESTROY"
TAG_NAMES = "__FILE__"|"__LINE__"|"__PACKAGE__"|"__SUB__"
NAMED_ARGUMENTLESS = "wantarray"|"wait"|"times"|"time"|"setpwent"|"setgrent"|"getservent"|"getpwent"|"getprotoent"|"getppid"|"getnetent"|"getlogin"|"gethostent"|"getgrent"|"fork"|"endservent"|"endpwent"|"endprotoent"|"endnetent"|"endhostent"|"endgrent"|"dump"|"continue"|"break"
LIST_OPERATORS = "warn"|"waitpid"|"vec"|"utime"|"untie"|"unshift"|"unpack"|"unlink"|"tied"|"tie"|"system"|"syscall"|"symlink"|"substr"|"sprintf"|"split"|"splice"|"socketpair"|"socket"|"shutdown"|"shmwrite"|"shmread"|"shmget"|"shmctl"|"setsockopt"|"setpriority"|"setpgrp"|"send"|"semop"|"semget"|"semctl"|"scalar"|"rindex"|"reverse"|"rename"|"recv"|"push"|"pos"|"pipe"|"pack"|"msgsnd"|"msgrcv"|"msgget"|"msgctl"|"mkdir"|"lock"|"listen"|"link"|"kill"|"join"|"index"|"import"|"glob"|"getsockopt"|"getservbyport"|"getservbyname"|"getprotobynumber"|"getpriority"|"getnetbyaddr"|"gethostbyaddr"|"formline"|"exec"|"die"|"delete"|"dbmopen"|"dbmclose"|"crypt"|"connect"|"chown"|"chop"|"chomp"|"chmod"|"bless"|"bind"|"atan2"|"accept"
HANDLES = "STDIN"|"STDOUT"|"STDERR"

REGEX_COMMENT = "(?#"[^)]*")"

%state LEX_OPERATOR

%state LEX_END_BLOCK
%state LEX_POD
%state LEX_QUOTE_LIKE_OPENER_Q, LEX_QUOTE_LIKE_OPENER_QQ, LEX_QUOTE_LIKE_OPENER_QX, LEX_QUOTE_LIKE_OPENER_QW
%state LEX_TRANS_OPENER, LEX_REGEX_OPENER

%state LEX_PACKAGE, LEX_PACKAGE_USE, LEX_PACKAGE_USE_VERSION, LEX_PACKAGE_REQUIRE, LEX_SUB, LEX_BRACED_STRING, LEX_ATTRIBUTES

%xstate LEX_STRING_CONTENT, LEX_STRING_CONTENT_QQ, LEX_STRING_CONTENT_XQ, LEX_STRING_LIST
%xstate LEX_MATCH_REGEX, LEX_EXTENDED_MATCH_REGEX, LEX_REPLACEMENT_REGEX

// custom lexical states to avoid crossing with navie ones
%state LEX_CUSTOM1, LEX_CUSTOM2, LEX_CUSTOM3, LEX_CUSTOM4, LEX_CUSTOM5, LEX_CUSTOM6, LEX_CUSTOM7, LEX_CUSTOM8, LEX_CUSTOM9, LEX_CUSTOM10
%state LEX_PREPARSED_ITEMS
%xstate LEX_SUB_PROTOTYPE
%state LEX_SUB_ATTRIBUTES
%state LEX_HANDLE, LEX_STRICT_HANDLE, LEX_ANGLED_HANDLE
%state LEX_PRINT
%state LEX_AFTER_DEREFERENCE, LEX_AFTER_IDENTIFIER, LEX_AFTER_REGEX_ACCEPTING_IDENTIFIER, LEX_AFTER_VARIABLE_NAME
%state LEX_AFTER_RIGHT_BRACKET

%state LEX_QUOTED_HEREDOC_OPENER, LEX_BARE_HEREDOC_OPENER, LEX_BARE_BACKREF_HEREDOC_OPENER

%state LEX_USE_VARS_STRING

%state LEX_VARIABLE_UNBRACED, LEX_VARIABLE_BRACED

%%

<LEX_QUOTED_HEREDOC_OPENER>{
	{DQ_STRING}	{
		yybegin(LEX_OPERATOR);
		pushState();
		heredocQueue.push(new PerlHeredocQueueElement(HEREDOC_QQ, yytext().subSequence(1,yylength()-1)));
		pullback(0);
		yybegin(LEX_QUOTE_LIKE_OPENER_QQ);
		return captureString();
	}
	{SQ_STRING} {
		yybegin(LEX_OPERATOR);
		pushState();
		heredocQueue.push(new PerlHeredocQueueElement(HEREDOC, yytext().subSequence(1,yylength()-1)));
		pullback(0);
		yybegin(LEX_QUOTE_LIKE_OPENER_Q);
		return captureString();
	}
	{XQ_STRING} {
		yybegin(LEX_OPERATOR);
		pushState();
		heredocQueue.push(new PerlHeredocQueueElement(HEREDOC_QX, yytext().subSequence(1,yylength()-1)));
		pullback(0);
		yybegin(LEX_QUOTE_LIKE_OPENER_QX);
		return captureString();
	}
}

<LEX_BARE_BACKREF_HEREDOC_OPENER>{
	{UNQUOTED_HEREDOC_MARKER} {	yybegin(LEX_OPERATOR);heredocQueue.push(new PerlHeredocQueueElement(HEREDOC, yytext()));return STRING_CONTENT;}
}

<LEX_BARE_HEREDOC_OPENER>{
	"\\" {yybegin(LEX_BARE_BACKREF_HEREDOC_OPENER);return OPERATOR_REFERENCE;}
	{UNQUOTED_HEREDOC_MARKER} {	yybegin(LEX_OPERATOR); heredocQueue.push(new PerlHeredocQueueElement(HEREDOC_QQ, yytext()));return STRING_CONTENT;}
}

<LEX_STRING_CONTENT_QQ,LEX_STRING_CONTENT_XQ,LEX_MATCH_REGEX,LEX_EXTENDED_MATCH_REGEX,LEX_REPLACEMENT_REGEX>
{
	"@" / [^] 	{return startUnbracedVariable(SIGIL_ARRAY);}
	"$#" / [^] 	{return startUnbracedVariable(SIGIL_SCALAR_INDEX);}
	"$" / [^] 	{return startUnbracedVariable(SIGIL_SCALAR); }
}

<LEX_EXTENDED_MATCH_REGEX>{
	{ESCAPED_CHARACTER}	{return REGEX_TOKEN;}
	{ANY_SPACE}+		{return TokenType.WHITE_SPACE;}
}

<LEX_MATCH_REGEX,LEX_EXTENDED_MATCH_REGEX,LEX_REPLACEMENT_REGEX>
{
	{REGEX_COMMENT}	{return COMMENT_LINE;}
	[^]				{return REGEX_TOKEN;}
}

<LEX_STRING_CONTENT_QQ>{
	[^$\@]+						{return STRING_CONTENT_QQ;}
	[\$\@]						{return STRING_CONTENT_QQ;}
}

<LEX_STRING_CONTENT_XQ>{
	[^\$\@]+					{return STRING_CONTENT_XQ;}
	[\$\@]						{return STRING_CONTENT_XQ;}
}

<LEX_STRING_LIST>
{
	[^\s\n\r]+				{return STRING_CONTENT;}
	{ESCAPED_WHITE_SPACE}	{return STRING_CONTENT;}
	{ANY_SPACE}+			{return TokenType.WHITE_SPACE;}
}

<LEX_STRING_CONTENT>{
	[^]+					{return STRING_CONTENT;}
}

<LEX_POD> {
	{POD_END} / {NEW_LINE}	{yybegin(YYINITIAL);return POD;}
	{POD_LINE}				{return POD;}
}

^{POD_START} 	{yybegin(LEX_POD);return POD;}
{NEW_LINE}   	{return TokenType.NEW_LINE_INDENT;}
{WHITE_SPACE}+  {return TokenType.WHITE_SPACE;}
{END_BLOCK}		{yybegin(LEX_END_BLOCK);return COMMENT_BLOCK;}
{DATA_BLOCK}	{yybegin(LEX_END_BLOCK);return COMMENT_BLOCK;}

<LEX_PRINT>{
	"@" 	{return startUnbracedVariable(LEX_AFTER_IDENTIFIER, SIGIL_ARRAY);}
	"$#" 	{return startUnbracedVariable(LEX_AFTER_IDENTIFIER, SIGIL_SCALAR_INDEX);}
	"$" 	{return startUnbracedVariable(LEX_AFTER_IDENTIFIER, SIGIL_SCALAR); }
	"%"		{return startUnbracedVariable(LEX_AFTER_IDENTIFIER, SIGIL_HASH);}
	"*"		{return startUnbracedVariable(LEX_AFTER_IDENTIFIER, SIGIL_GLOB);}
	"&"		{return startUnbracedVariable(LEX_AFTER_IDENTIFIER, SIGIL_CODE);}
}

<LEX_HANDLE> {
	"("				{yybegin(LEX_STRICT_HANDLE);return LEFT_PAREN;}
}

<LEX_ANGLED_HANDLE>{
	{IDENTIFIER} 	{return HANDLE;}
	">"				{yybegin(LEX_OPERATOR);return RIGHT_ANGLE;}
	[^]				{yybegin(YYINITIAL);yypushback(1);break;}
}

<LEX_HANDLE,LEX_STRICT_HANDLE>{
	{CORE_PREFIX}"my"			{yybegin(YYINITIAL);return RESERVED_MY;}
	{CORE_PREFIX}"our"			{yybegin(YYINITIAL);return RESERVED_OUR;}
	{CORE_PREFIX}"local"		{yybegin(YYINITIAL);return RESERVED_LOCAL;}
	{CORE_PREFIX}"state"		{yybegin(YYINITIAL);return RESERVED_STATE;}
	{IDENTIFIER} / [:(-] {pullback(0);yybegin(YYINITIAL);}
	{IDENTIFIER} 	{yybegin(LEX_AFTER_IDENTIFIER);return HANDLE;}
	[^]				{yybegin(YYINITIAL);yypushback(1);}
}

<LEX_END_BLOCK>{
	[^]+ 		{return COMMENT_BLOCK;}
}


<LEX_BRACED_STRING>{
	{BAREWORD_MINUS}	{return STRING_CONTENT;}
	"}"					{return getRightBrace(LEX_AFTER_VARIABLE_NAME);}
}

// fixme temproary
<LEX_PACKAGE>{
	"constant"		{yybegin(YYINITIAL);return PACKAGE_PRAGMA_CONSTANT;}
	"vars"			{yybegin(YYINITIAL);return PACKAGE_PRAGMA_VARS;}
}


<LEX_PACKAGE,LEX_PACKAGE_USE_VERSION,LEX_PACKAGE_REQUIRE>{
	{PERL_VERSION}					{yybegin(YYINITIAL);return NUMBER_VERSION;}
	[^]								{yypushback(1);yybegin(YYINITIAL);}
}


// handles hash indexes after variable and/or ->
<LEX_AFTER_DEREFERENCE,LEX_AFTER_VARIABLE_NAME,LEX_AFTER_RIGHT_BRACKET>{
	"{"							{return startStructuredBlock(LEX_AFTER_VARIABLE_NAME);}
}

<LEX_AFTER_VARIABLE_NAME,LEX_AFTER_DEREFERENCE>{
	"{" / {WHITE_SPACE}* {BAREWORD_MINUS} {WHITE_SPACE}* "}"	{yybegin(LEX_BRACED_STRING);return getLeftBrace();}
}

<LEX_SUB>{
	{QUALIFIED_IDENTIFIER} 		{return IDENTIFIER;}
	"(" / {SUB_PROTOTYPE}? ")"	{yybegin(LEX_SUB_PROTOTYPE);return LEFT_PAREN;}
	"(" 						{yybegin(YYINITIAL);return LEFT_PAREN;}
}

<LEX_SUB,LEX_SUB_ATTRIBUTES,LEX_ATTRIBUTES>{
	"{"     					{yybegin(YYINITIAL);return getLeftBraceCode();}
}

<LEX_SUB,LEX_SUB_ATTRIBUTES>{
	":"							{yybegin(LEX_ATTRIBUTES);return COLON;}
	[^]							{yypushback(1);yybegin(YYINITIAL);break;}
}

<LEX_SUB_PROTOTYPE>{
	{SUB_PROTOTYPE}			{return SUB_PROTOTYPE_TOKEN;}
	")"						{yybegin(LEX_SUB_ATTRIBUTES);return RIGHT_PAREN;}
}

<LEX_ATTRIBUTES>
{
	":"					{return COLON;}
	{IDENTIFIER} / "("	{pushState();yybegin(LEX_QUOTE_LIKE_OPENER_Q);return IDENTIFIER;}
	{IDENTIFIER}		{return IDENTIFIER;}
	[^]					{yypushback(1);yybegin(YYINITIAL);}
}

<YYINITIAL,LEX_OPERATOR,LEX_AFTER_RIGHT_BRACKET,LEX_AFTER_IDENTIFIER,LEX_AFTER_REGEX_ACCEPTING_IDENTIFIER,LEX_AFTER_VARIABLE_NAME,LEX_SUB,LEX_PRINT>{
	"{"     {yybegin(YYINITIAL);return getLeftBrace();}
}

// common logic
<YYINITIAL,LEX_OPERATOR,LEX_AFTER_RIGHT_BRACKET,LEX_AFTER_DEREFERENCE,LEX_AFTER_IDENTIFIER,LEX_AFTER_REGEX_ACCEPTING_IDENTIFIER,LEX_AFTER_VARIABLE_NAME,LEX_PRINT>{
	"["     {yybegin(YYINITIAL);return LEFT_BRACKET;}
	"("     {yybegin(YYINITIAL);return LEFT_PAREN;}
}

<YYINITIAL,LEX_OPERATOR,LEX_AFTER_RIGHT_BRACKET,LEX_AFTER_IDENTIFIER,LEX_AFTER_REGEX_ACCEPTING_IDENTIFIER,LEX_AFTER_VARIABLE_NAME,LEX_SUB,LEX_PRINT>{
	";"     {yybegin(YYINITIAL);return SEMICOLON;}
}

<YYINITIAL,LEX_AFTER_IDENTIFIER,LEX_AFTER_REGEX_ACCEPTING_IDENTIFIER,LEX_PRINT>{
	"++" 	{yybegin(YYINITIAL);return OPERATOR_PLUS_PLUS;}
	"--" 	{yybegin(YYINITIAL);return OPERATOR_MINUS_MINUS;}
}

<LEX_AFTER_VARIABLE_NAME,LEX_AFTER_RIGHT_BRACKET,LEX_OPERATOR>{
	"++" 	{yybegin(LEX_OPERATOR);return OPERATOR_PLUS_PLUS;}
	"--" 	{yybegin(LEX_OPERATOR);return OPERATOR_MINUS_MINUS;}
}



<YYINITIAL,LEX_OPERATOR,LEX_AFTER_RIGHT_BRACKET,LEX_AFTER_IDENTIFIER,LEX_AFTER_REGEX_ACCEPTING_IDENTIFIER,LEX_AFTER_VARIABLE_NAME,LEX_PRINT>{
	{FARROW} 	{yybegin(YYINITIAL);return FAT_COMMA;}
	"," 		{yybegin(YYINITIAL);return COMMA;}
	"}"     	{return getRightBrace(YYINITIAL);}
	"]"     	{yybegin(LEX_AFTER_RIGHT_BRACKET);return RIGHT_BRACKET;}
	")"     	{yybegin(LEX_OPERATOR);return RIGHT_PAREN;}

	{CORE_PREFIX}"if"	 	{ yybegin(YYINITIAL); return RESERVED_IF;}
	{CORE_PREFIX}"unless"	{ yybegin(YYINITIAL);return RESERVED_UNLESS;}
	{CORE_PREFIX}"while"	{ yybegin(YYINITIAL);return RESERVED_WHILE;}
	{CORE_PREFIX}"until"	{ yybegin(YYINITIAL);return RESERVED_UNTIL;}
	{CORE_PREFIX}"for"	 	{ yybegin(YYINITIAL);return RESERVED_FOR;}
	{CORE_PREFIX}"foreach"	{ yybegin(YYINITIAL);return RESERVED_FOREACH;}
	{CORE_PREFIX}"when"	 	{ yybegin(YYINITIAL);return RESERVED_WHEN;}
}

// may be after right brace, like anon hash
<YYINITIAL,LEX_OPERATOR,LEX_AFTER_RIGHT_BRACKET,LEX_AFTER_IDENTIFIER,LEX_AFTER_VARIABLE_NAME>{
	"->" 		{yybegin(LEX_AFTER_DEREFERENCE); return OPERATOR_DEREFERENCE;}
}

<LEX_OPERATOR,LEX_AFTER_RIGHT_BRACKET,LEX_AFTER_IDENTIFIER,LEX_AFTER_VARIABLE_NAME>{
	"**"	{yybegin(YYINITIAL);return OPERATOR_POW;}
	"%=" 	{yybegin(YYINITIAL);return OPERATOR_MOD_ASSIGN;}
	"*=" 	{yybegin(YYINITIAL);return OPERATOR_MUL_ASSIGN;}
	"&&" 	{yybegin(YYINITIAL);return OPERATOR_AND;}
	"&=" 	{yybegin(YYINITIAL);return OPERATOR_BITWISE_AND_ASSIGN;}
	"**=" 	{yybegin(YYINITIAL);return OPERATOR_POW_ASSIGN;}
	"&&="	{yybegin(YYINITIAL);return OPERATOR_AND_ASSIGN;}
	"*" 	{yybegin(YYINITIAL);return OPERATOR_MUL;}
	"%" 	{yybegin(YYINITIAL);return OPERATOR_MOD;}
	"&" 	{yybegin(YYINITIAL);return OPERATOR_BITWISE_AND;}
	">="	{yybegin(YYINITIAL);return OPERATOR_GE_NUMERIC;}
	"<="	{yybegin(YYINITIAL);return OPERATOR_LE_NUMERIC;}
	"=="	{yybegin(YYINITIAL);return OPERATOR_EQ_NUMERIC;}
	"!="	{yybegin(YYINITIAL);return OPERATOR_NE_NUMERIC;}
	"~~"	{yybegin(YYINITIAL);return OPERATOR_SMARTMATCH;}
	"+="	{yybegin(YYINITIAL);return OPERATOR_PLUS_ASSIGN;}
	"-="	{yybegin(YYINITIAL);return OPERATOR_MINUS_ASSIGN;}
	"/=" 	{yybegin(YYINITIAL);return OPERATOR_DIV_ASSIGN;}
	".=" 	{yybegin(YYINITIAL);return OPERATOR_CONCAT_ASSIGN;}
	"x=" 	{yybegin(YYINITIAL);return OPERATOR_X_ASSIGN;}
	"|=" 	{yybegin(YYINITIAL);return OPERATOR_BITWISE_OR_ASSIGN;}
	"^=" 	{yybegin(YYINITIAL);return OPERATOR_BITWISE_XOR_ASSIGN;}
	"<<=" 	{yybegin(YYINITIAL);return OPERATOR_SHIFT_LEFT_ASSIGN;}
	">>=" 	{yybegin(YYINITIAL);return OPERATOR_SHIFT_RIGHT_ASSIGN;}
	"||=" 	{yybegin(YYINITIAL);return OPERATOR_OR_ASSIGN;}
	"//=" 	{yybegin(YYINITIAL);return OPERATOR_OR_DEFINED_ASSIGN;}
	"//" 	{yybegin(YYINITIAL);return OPERATOR_OR_DEFINED;}
	"<=>" 	{yybegin(YYINITIAL);return OPERATOR_CMP_NUMERIC;}
	"<" 	{yybegin(YYINITIAL);return OPERATOR_LT_NUMERIC;}
	">" 	{yybegin(YYINITIAL);return OPERATOR_GT_NUMERIC;}

	"=~" 	{yybegin(YYINITIAL);return OPERATOR_RE;}
	"!~" 	{yybegin(YYINITIAL);return OPERATOR_NOT_RE;}

	"<<" 	{yybegin(YYINITIAL);return OPERATOR_SHIFT_LEFT;}
	">>" 	{yybegin(YYINITIAL);return OPERATOR_SHIFT_RIGHT;}

	"||" 	{yybegin(YYINITIAL);return OPERATOR_OR;}
	"?"  	{yybegin(YYINITIAL);return QUESTION;}
	":"  	{yybegin(YYINITIAL);return COLON;}
	"|" 	{yybegin(YYINITIAL);return OPERATOR_BITWISE_OR;}
	"^" 	{yybegin(YYINITIAL);return OPERATOR_BITWISE_XOR;}
	"=" 	{yybegin(YYINITIAL);return OPERATOR_ASSIGN;}
	"+" 	{yybegin(YYINITIAL);return OPERATOR_PLUS;}
	"-" 	{yybegin(YYINITIAL);return OPERATOR_MINUS;}

	"..." 	{yybegin(YYINITIAL);return OPERATOR_HELLIP;}
	".." 	{yybegin(YYINITIAL);return OPERATOR_FLIP_FLOP;}
	"." 	{yybegin(YYINITIAL);return OPERATOR_CONCAT;}
	"/"   	{yybegin(YYINITIAL);return OPERATOR_DIV;}

	"x"   		{yybegin(YYINITIAL);return OPERATOR_X;}
	"not"		{yybegin(YYINITIAL);return OPERATOR_NOT_LP;}
	"and"		{yybegin(YYINITIAL);return OPERATOR_AND_LP;}
	"or"		{yybegin(YYINITIAL);return OPERATOR_OR_LP;}
	"xor"		{yybegin(YYINITIAL);return OPERATOR_XOR_LP;}
	"lt"		{yybegin(YYINITIAL);return OPERATOR_LT_STR;}
	"gt"		{yybegin(YYINITIAL);return OPERATOR_GT_STR;}
	"le"		{yybegin(YYINITIAL);return OPERATOR_LE_STR;}
	"ge"		{yybegin(YYINITIAL);return OPERATOR_GE_STR;}
	"eq"		{yybegin(YYINITIAL);return OPERATOR_EQ_STR;}
	"ne"		{yybegin(YYINITIAL);return OPERATOR_NE_STR;}
	"cmp"		{yybegin(YYINITIAL);return OPERATOR_CMP_STR;}


}

<YYINITIAL>{
	"..." 	{yybegin(LEX_OPERATOR);return OPERATOR_NYI;}
}

<LEX_VARIABLE_UNBRACED>{
	// this is a subset of builtins, $;, $, for example, can't be dereferenced
	"$" / [\{\"\'\[\]\`\\\!\%\&\(\)\+\-\.\/\<\=\>\|\~\?\:\*\^\@\_\$\:\w_\d]		{return processUnbracedScalarSigil();}
	"{"											{return startBracedVariable();}
	{VARIABLE_NAME}								{return getUnbracedVariableNameToken();}
}

<LEX_VARIABLE_BRACED>{
	{VARIABLE_NAME}	/ {SPACES_OR_COMMENTS}"}"				{return getBracedVariableNameToken();}
	{CAPPED_BRACED_VARIABLE} / {SPACES_OR_COMMENTS}"}"		{return getBracedVariableNameToken();}
	[^]														{yypushback(1);yybegin(YYINITIAL);}
}

<LEX_USE_VARS_STRING>
{
	"@" 	{return startUnbracedVariable(SIGIL_ARRAY);}
	"$#" 	{return startUnbracedVariable(SIGIL_SCALAR_INDEX);}
	"$" 	{return startUnbracedVariable(SIGIL_SCALAR); }
	"%"		{return startUnbracedVariable(SIGIL_HASH);}
	"*"		{return startUnbracedVariable(SIGIL_GLOB);}
	"&"		{return startUnbracedVariable(SIGIL_CODE);}
}

<YYINITIAL,LEX_AFTER_DEREFERENCE,LEX_AFTER_IDENTIFIER,LEX_AFTER_REGEX_ACCEPTING_IDENTIFIER>{
	"@" 	{return startUnbracedVariable(LEX_AFTER_VARIABLE_NAME, SIGIL_ARRAY);}
	"$#" 	{return startUnbracedVariable(LEX_AFTER_VARIABLE_NAME, SIGIL_SCALAR_INDEX);}
	"$" 	{return startUnbracedVariable(LEX_AFTER_VARIABLE_NAME, SIGIL_SCALAR); }
	"%"		{return startUnbracedVariable(LEX_AFTER_VARIABLE_NAME, SIGIL_HASH);}
	"*"		{return startUnbracedVariable(LEX_AFTER_VARIABLE_NAME, SIGIL_GLOB);}
	"&"		{return startUnbracedVariable(LEX_AFTER_VARIABLE_NAME, SIGIL_CODE);}
}


<YYINITIAL,LEX_AFTER_IDENTIFIER,LEX_AFTER_REGEX_ACCEPTING_IDENTIFIER,LEX_PRINT>{
	{NUMBER_BIN}									{yybegin(LEX_OPERATOR);return NUMBER;}
	{NUMBER_HEX}									{yybegin(LEX_OPERATOR);return NUMBER;}
	{NUMBER_INT}? "." {NUMBER_INT} {NUMBER_EXP}?	{yybegin(LEX_OPERATOR);return NUMBER;}
	{NUMBER_INT} ("." {NUMBER_INT})? {NUMBER_EXP}?	{yybegin(LEX_OPERATOR);return NUMBER;}
}

<YYINITIAL,LEX_PACKAGE,LEX_PACKAGE_REQUIRE,LEX_AFTER_REGEX_ACCEPTING_IDENTIFIER,LEX_PRINT>{
	{PERL_VERSION}  		{yybegin(LEX_OPERATOR);return NUMBER_VERSION;}
}

<YYINITIAL,LEX_AFTER_IDENTIFIER,LEX_AFTER_REGEX_ACCEPTING_IDENTIFIER,LEX_PRINT>{
	{DQ_STRING}	{yybegin(LEX_OPERATOR);pushState();pullback(0);yybegin(LEX_QUOTE_LIKE_OPENER_QQ);return captureString();}
	{SQ_STRING} {yybegin(LEX_OPERATOR);pushState();pullback(0);yybegin(LEX_QUOTE_LIKE_OPENER_Q);return captureString();}
	{XQ_STRING} {yybegin(LEX_OPERATOR);pushState();pullback(0);yybegin(LEX_QUOTE_LIKE_OPENER_QX);return captureString();}

	{BAREWORD_MINUS} / {SPACES_OR_COMMENTS}* {FARROW}	{yybegin(LEX_OPERATOR);return STRING_CONTENT;}

	"<" / {IDENTIFIER}">"  		{yybegin(LEX_ANGLED_HANDLE);return LEFT_ANGLE;}
	"<"							{yybegin(LEX_OPERATOR);pushState();yypushback(1);yybegin(LEX_QUOTE_LIKE_OPENER_QQ);return captureString();}

	"<<" / {QUOTED_HEREDOC_MARKER}   		{yybegin(LEX_QUOTED_HEREDOC_OPENER);return OPERATOR_HEREDOC;}
	"<<" / "\\"{UNQUOTED_HEREDOC_MARKER} 	{yybegin(LEX_BARE_HEREDOC_OPENER);return OPERATOR_HEREDOC;}
	"<<" / {UNQUOTED_HEREDOC_MARKER} 		{yybegin(LEX_BARE_HEREDOC_OPENER);return OPERATOR_HEREDOC;}

	"/"   						{yybegin(LEX_OPERATOR);return startRegexp();}

	"split"						{yybegin(LEX_AFTER_REGEX_ACCEPTING_IDENTIFIER); return IDENTIFIER;}

	{NAMED_UNARY_OPERATORS}				{yybegin(LEX_AFTER_IDENTIFIER);return OPERATOR_NAMED_UNARY;}
	{BARE_HANDLE_ACCEPTORS}				{yybegin(LEX_HANDLE);return IDENTIFIER;}
	{NAMED_UNARY_BARE_HANDLE_ACCEPTORS}	{yybegin(LEX_HANDLE);return OPERATOR_NAMED_UNARY;}

	"!" 						{yybegin(YYINITIAL);return OPERATOR_NOT;}
	"+" 						{yybegin(YYINITIAL);return OPERATOR_PLUS_UNARY;}
	"-" 						{yybegin(YYINITIAL);return OPERATOR_MINUS_UNARY;}
	"~" 						{yybegin(YYINITIAL);return OPERATOR_BITWISE_NOT;}
	{PERL_OPERATORS_FILETEST} 	{yybegin(LEX_HANDLE);yypushback(1);return OPERATOR_FILETEST;}

	"\\" 						{yybegin(YYINITIAL);return OPERATOR_REFERENCE;}

	{CORE_PREFIX}"my"			{yybegin(YYINITIAL);return RESERVED_MY;}
	{CORE_PREFIX}"our"			{yybegin(YYINITIAL);return RESERVED_OUR;}
	{CORE_PREFIX}"local"		{yybegin(YYINITIAL);return RESERVED_LOCAL;}
	{CORE_PREFIX}"state"		{yybegin(YYINITIAL);return RESERVED_STATE;}

	{CORE_PREFIX}"elsif"	 	{yybegin(YYINITIAL);return RESERVED_ELSIF;}
	{CORE_PREFIX}"else"	 		{yybegin(YYINITIAL);return RESERVED_ELSE;}
	{CORE_PREFIX}"given"	 	{yybegin(YYINITIAL);return RESERVED_GIVEN;}
	{CORE_PREFIX}"default"	 	{yybegin(YYINITIAL);return RESERVED_DEFAULT;}
	{CORE_PREFIX}"continue"	 	{yybegin(YYINITIAL);return  RESERVED_CONTINUE;}

	{CORE_PREFIX}"format"	 	{yybegin(LEX_AFTER_IDENTIFIER);return  RESERVED_FORMAT;}
	{CORE_PREFIX}"sub"	 		{yybegin(LEX_SUB);return  RESERVED_SUB;}

	{CORE_PREFIX}"package"	 	{yybegin(LEX_PACKAGE); return  RESERVED_PACKAGE;}
	{CORE_PREFIX}"use"	 		{yybegin(LEX_PACKAGE_USE); return  RESERVED_USE;}
	{CORE_PREFIX}"no"	 		{yybegin(LEX_PACKAGE_USE); return  RESERVED_NO;}
	{CORE_PREFIX}"require"	 	{yybegin(LEX_PACKAGE_REQUIRE); return  RESERVED_REQUIRE;}

	{CORE_PREFIX}"undef"		{yybegin(LEX_AFTER_IDENTIFIER);return RESERVED_UNDEF;}

	"switch"					{yybegin(YYINITIAL);return RESERVED_SWITCH;}
	"case"						{yybegin(LEX_AFTER_REGEX_ACCEPTING_IDENTIFIER);return RESERVED_CASE;}

	"method"					{yybegin(YYINITIAL);return RESERVED_METHOD;}
	"func"						{yybegin(YYINITIAL);return RESERVED_FUNC;}

	"try"						{yybegin(YYINITIAL);return RESERVED_TRY;}
	"catch"						{yybegin(YYINITIAL);return RESERVED_CATCH;}
	"finally"					{yybegin(YYINITIAL);return RESERVED_FINALLY;}

	"inner"					{yybegin(YYINITIAL);return RESERVED_INNER;}
	"with"					{yybegin(YYINITIAL);return RESERVED_WITH;}
	"extends"				{yybegin(YYINITIAL);return RESERVED_EXTENDS;}
	"meta"					{yybegin(YYINITIAL);return RESERVED_META;}
	"override"				{yybegin(YYINITIAL);return RESERVED_OVERRIDE;}
	"around"				{yybegin(YYINITIAL);return RESERVED_AROUND;}
	"super"					{yybegin(YYINITIAL);return RESERVED_SUPER;}
	"augment"				{yybegin(YYINITIAL);return RESERVED_AUGMENT;}
	"after"					{yybegin(YYINITIAL);return RESERVED_AFTER;}
	"before"				{yybegin(YYINITIAL);return RESERVED_BEFORE;}
	"has"					{yybegin(YYINITIAL);return RESERVED_HAS;}

	// special treatment?
	{CORE_PREFIX}"print"	 	{yybegin(LEX_PRINT); return RESERVED_PRINT;}
	{CORE_PREFIX}"printf"	 	{yybegin(LEX_PRINT); return RESERVED_PRINTF;}
	{CORE_PREFIX}"say"	 		{yybegin(LEX_PRINT); return RESERVED_SAY;}

	{CORE_PREFIX}"grep"	 { yybegin(LEX_AFTER_REGEX_ACCEPTING_IDENTIFIER); return  RESERVED_GREP;}
	{CORE_PREFIX}"map"	 { yybegin(YYINITIAL);return  RESERVED_MAP;}
	{CORE_PREFIX}"sort"	 { yybegin(YYINITIAL);return  RESERVED_SORT;}

	{CORE_PREFIX}"do"	 { yybegin(YYINITIAL);return  RESERVED_DO;}
	{CORE_PREFIX}"eval"	 { yybegin(YYINITIAL);return  RESERVED_EVAL;}

	{CORE_PREFIX}"goto"	 { yybegin(YYINITIAL);return  RESERVED_GOTO;}
	{CORE_PREFIX}"redo"	 { yybegin(YYINITIAL);return  RESERVED_REDO;}
	{CORE_PREFIX}"next"	 { yybegin(YYINITIAL);return  RESERVED_NEXT;}
	{CORE_PREFIX}"last"	 { yybegin(YYINITIAL);return  RESERVED_LAST;}

	{CORE_PREFIX}"return"	 { yybegin(LEX_AFTER_REGEX_ACCEPTING_IDENTIFIER); return  RESERVED_RETURN;}

	{BLOCK_NAMES}			{yybegin(YYINITIAL);return BLOCK_NAME;}
	{TAG_NAMES}				{yybegin(LEX_OPERATOR); return TAG;}
	{NAMED_ARGUMENTLESS}	{yybegin(LEX_OPERATOR);return IDENTIFIER;}	// fixme we can return special token here to help parser
	{LIST_OPERATORS}		{yybegin(YYINITIAL);return IDENTIFIER;}

	{CORE_PREFIX}"y"  / {QUOTE_LIKE_SUFFIX} {return RESERVED_Y;}
	{CORE_PREFIX}"tr" / {QUOTE_LIKE_SUFFIX} {return RESERVED_TR;}

	{CORE_PREFIX}"qr" / {QUOTE_LIKE_SUFFIX} {yybegin(LEX_OPERATOR);return RESERVED_QR;}
	{CORE_PREFIX}"qw" / {QUOTE_LIKE_SUFFIX} {yybegin(LEX_OPERATOR);return RESERVED_QW;}
	{CORE_PREFIX}"qq" / {QUOTE_LIKE_SUFFIX} {yybegin(LEX_OPERATOR);return RESERVED_QQ;}
	{CORE_PREFIX}"qx" / {QUOTE_LIKE_SUFFIX} {yybegin(LEX_OPERATOR);return RESERVED_QX;}
	{CORE_PREFIX}"q" / {QUOTE_LIKE_SUFFIX}  {yybegin(LEX_OPERATOR);return RESERVED_Q;}

	{CORE_PREFIX}"m" / {QUOTE_LIKE_SUFFIX}  {return RESERVED_M;}
	{CORE_PREFIX}"s" / {QUOTE_LIKE_SUFFIX}  {return RESERVED_S;}

}

// identifiers fallbacks

<LEX_AFTER_DEREFERENCE>{
	"follow_best_practice"		{yybegin(LEX_OPERATOR);return RESERVED_FOLLOW_BEST_PRACTICE;}
	"mk_accessors"				{yybegin(LEX_OPERATOR);return RESERVED_MK_ACCESSORS;}
	"mk_ro_accessors"			{yybegin(LEX_OPERATOR);return RESERVED_MK_RO_ACCESSORS;}
	"mk_wo_accessors"			{yybegin(LEX_OPERATOR);return RESERVED_MK_WO_ACCESSORS;}
	"helper"					{yybegin(LEX_OPERATOR);return MOJO_HELPER_METHOD;}
	{QUALIFIED_IDENTIFIER} 		{yybegin(LEX_OPERATOR);return IDENTIFIER;}
}

<LEX_PACKAGE>{
	{QUALIFIED_IDENTIFIER}			{yybegin(YYINITIAL);return IDENTIFIER;}
}

<LEX_PACKAGE_REQUIRE>{
	{QUALIFIED_IDENTIFIER} / "("	{yybegin(LEX_AFTER_IDENTIFIER);return IDENTIFIER;}
	{QUALIFIED_IDENTIFIER}			{yybegin(LEX_OPERATOR);return IDENTIFIER;}
}

<LEX_PACKAGE_USE>{
	"constant"						{yybegin(LEX_PACKAGE_USE_VERSION);return PACKAGE_PRAGMA_CONSTANT;}
	"vars"							{yybegin(LEX_PACKAGE_USE_VERSION);return PACKAGE_PRAGMA_VARS;}
	{QUALIFIED_IDENTIFIER}			{yybegin(LEX_PACKAGE_USE_VERSION);return IDENTIFIER;}
}

// unsure if we need this, jut for better highlighting
<LEX_PRINT>	{HANDLES} 	{yybegin(YYINITIAL);return HANDLE;}

<YYINITIAL,LEX_AFTER_IDENTIFIER,LEX_AFTER_REGEX_ACCEPTING_IDENTIFIER,LEX_PRINT>{
	{QUALIFIED_IDENTIFIER} 	{yybegin(LEX_AFTER_IDENTIFIER);return getIdentifierToken();}
}

/* error fallback [^] */
[^]    { return TokenType.BAD_CHARACTER; }
