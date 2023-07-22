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

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

%%

// http://perldoc.perl.org/perlop.html#Gory-details-of-parsing-quoted-constructs
// http://perldoc.perl.org/perldata.html#Identifier-parsing

%class PerlLexer
%extends PerlBaseLexer
%unicode
%public


%function perlAdvance
%type IElementType


/*
// Char classes
*/
NEW_LINE = \R
WHITE_SPACE = [ \t\f]
ANY_SPACE = [ \t\f\n\r]
NON_SPACE = [^ \t\f\n\r]
LINE_COMMENT = "#" .*
LINE_COMMENT_ANNOTATION = "#@"[\w] .*
LINE_COMMENT_WITH_NEW_LINE = {LINE_COMMENT} \R
SPACE_OR_COMMENT = {ANY_SPACE}|{LINE_COMMENT_WITH_NEW_LINE}
MAY_BE_SPACES = {ANY_SPACE}*
MAY_BE_SPACES_OR_COMMENTS = {SPACE_OR_COMMENT}*
MUST_BE_SPACES_OR_COMMENTS = {SPACE_OR_COMMENT}+
NON_SPACE_AHEAD = {MAY_BE_SPACES_OR_COMMENTS}[^ \t\f\n\r\#]

FP_SUFFIX = {MUST_BE_SPACES_OR_COMMENTS} {IDENTIFIER} {MAY_BE_SPACES_OR_COMMENTS} "("
ESCAPED_SPACE_OR_COMMENT = "\\"({ANY_SPACE}|"#")

// http://perldoc.perl.org/perldata.html#Identifier-parsing
PERL_XIDS = [\w && \p{XID_Start}_]
PERL_XIDC = [\w && \p{XID_Continue}]

IDENTIFIER = {PERL_XIDS} {PERL_XIDC}*
// following chunks may start with a number
IDENTIFIER_CONTINUE = {PERL_XIDC}+

FARROW = "=>"
BAREWORD_MINUS = "-" ? {IDENTIFIER}

// qualified identifer can't start with ', but variable can
PACKAGE_SEPARATOR = "::"+ "'" ? | "::"* "'"
QUALIFIED_IDENTIFIER_TAIL = {PACKAGE_SEPARATOR} {IDENTIFIER_CONTINUE}
QUALIFIED_IDENTIFIER_WITHOUT_TRAILING_SEPARATOR = ("::"+ "'" ?) ? {IDENTIFIER} {QUALIFIED_IDENTIFIER_TAIL}*
QUALIFIED_IDENTIFIER = {QUALIFIED_IDENTIFIER_WITHOUT_TRAILING_SEPARATOR}  "::" *
QUALIFYING_PACKAGE = {QUALIFIED_IDENTIFIER_WITHOUT_TRAILING_SEPARATOR}  {PACKAGE_SEPARATOR}
VARIABLE_QUALIFIED_IDENTIFIER = ("::"* "'" ?) ? {IDENTIFIER} {QUALIFIED_IDENTIFIER_TAIL}*  "::" *

DQ_STRING = "\"" ([^\"]|"\\\\"|"\\\"" )* "\""?
SQ_STRING = "\'" ([^\']|"\\\\"|"\\\'" )* "\'"?
XQ_STRING = "\`" ([^\`]|"\\\\"|"\\\`" )* "\`"?

QUOTE_LIKE_SUFFIX= ("'" ({IDENTIFIER_CONTINUE} {QUALIFIED_IDENTIFIER_TAIL}*)? ("'" {IDENTIFIER_CONTINUE}? "'") ? )?
CORE_PREFIX = "CORE::"?

PERL_VERSION_CHUNK = [0-9][0-9_]*
PERL_VERSION = "v"?{PERL_VERSION_CHUNK}("." {PERL_VERSION_CHUNK})*
// heading _ removed to avoid @_ parsing as sigil-number

// duplicated in Pod lexer
NUMBER_EXP = [eE][+-]?[0-9_]+
NUMBER_INT = [0-9][0-9_]*
HEX_DIGIT = [0-9a-fA-F]
HEX_NUMBER_SHORT = {HEX_DIGIT}{HEX_DIGIT}?
UNICODE_CHAR_NAME = [\w][\w \-\(\)\:]+
SIMPLE_HEX_NUMBER = ({HEX_DIGIT}|"_")+
NUMBER_HEX = "0"[xX][0-9a-fA-F_]+
NUMBER_BIN = "0"[bB][01_]+
OCT_DIGIT = [0-7]
NUMBER_OCT = "0"{OCT_DIGIT}+

SIMPLE_OCT_NUMBER=({OCT_DIGIT}|"_")+
SIMPLE_OCT_NUMBER_SHORT={OCT_DIGIT}({OCT_DIGIT}{OCT_DIGIT}?)?

SPECIAL_VARIABLE_NAME = [\"\'\[\]\`\\\!\%\&\(\)\+\,\-\.\/\;\<\=\>\|\~\?\:\*\^\@\_\$]
NUMERIC_VARIABLE_NAME = "0" | [1-9] \d*
CAPPED_SINGLE_LETTER_VARIABLE_NAME = "^"[\]\[A-Z\^_?\\]
VARIABLE_NAME = {VARIABLE_QUALIFIED_IDENTIFIER} | {NUMERIC_VARIABLE_NAME} | "::" | {CAPPED_SINGLE_LETTER_VARIABLE_NAME} | {SPECIAL_VARIABLE_NAME}
CAPPED_BRACED_VARIABLE = {CAPPED_SINGLE_LETTER_VARIABLE_NAME}[\w_]*

AMBIGUOUS_SIGIL_SUFFIX = {MAY_BE_SPACES_OR_COMMENTS} ("{"|[\"\'\[\]\`\\\!\)\+\,\-\.\/\;\<\=\>\|\~\?\:\^]|{CAPPED_SINGLE_LETTER_VARIABLE_NAME}|
  {IDENTIFIER}{MAY_BE_SPACES_OR_COMMENTS}"=>" )

// atm making the same, but seems unary are different
PERL_OPERATORS_FILETEST = "-" [rwxoRWXOezsfdlpSbctugkTBMAC]

// fixme this regexps allows to start from space, unsure if it's right
HEREDOC_MARKER_DQ = [^\"\n\r]*
HEREDOC_MARKER_SQ = [^\'\n\r]*
HEREDOC_MARKER_XQ = [^\`\n\r]*
QUOTED_HEREDOC_MARKER = {WHITE_SPACE}*(\'{HEREDOC_MARKER_SQ}\'| \"{HEREDOC_MARKER_DQ}\" | \`{HEREDOC_MARKER_XQ}\`)

HEREDOC_MARKER_DQ_NON_EMPTY = [^\"\n\r\s][^\`\n\r]*
HEREDOC_MARKER_SQ_NON_EMPTY = [^\'\n\r\s][^\`\n\r]*
HEREDOC_MARKER_XQ_NON_EMPTY = [^\`\n\r\s][^\`\n\r]*
QUOTED_HEREDOC_MARKER_NON_EMPTY = {WHITE_SPACE}*(\'{HEREDOC_MARKER_SQ_NON_EMPTY}\'| \"{HEREDOC_MARKER_DQ_NON_EMPTY}\" | \`{HEREDOC_MARKER_XQ_NON_EMPTY}\`)

UNQUOTED_HEREDOC_MARKER = [a-zA-Z_] \d* {IDENTIFIER}?

END_BLOCK = "__END__"
DATA_BLOCK = "__DATA__"
LINE_TO_END = .+ {NEW_LINE}?

SUB_PROTOTYPE = [\[\$\@\%\&\*\]\;\+\\_]+
SUB_PROTOTYPE_WITH_SPACES = ({WHITE_SPACE} | {SUB_PROTOTYPE})+

POD_START = "="[\w].* {NEW_LINE} ?
POD_END = "=cut" ({WHITE_SPACE} .*)?

BLOCK_NAMES = "BEGIN"|"UNITCHECK"|"CHECK"|"INIT"|"END"|"AUTOLOAD"|"DESTROY"|"ADJUST"
TAG_NAMES = "__FILE__"|"__LINE__"|"__SUB__"

// Types for Type::Standard
TYPES_TINY = "ArrayRef"|"ConsumerOf"|"CycleTuple"|"Dict"|"Enum"|"HasMethods"|"HashRef"|"InstanceOf"|"Join"|"Map"|"Maybe"|"Optional"|"Overload"|"Ref"|"ScalarRef"|"Split"|"StrMatch"|"Tied"|"Tuple"
READONLY="Readonly"

// auto-generated by handlesubs.pl
NAMED_UNARY_OPERATORS = "umask"|"srand"|"sleep"|"setservent"|"setprotoent"|"setnetent"|"sethostent"|"reset"|"readline"|"rand"|"prototype"|"localtime"|"gmtime"|"getsockname"|"getpwuid"|"getpwnam"|"getprotobyname"|"getpgrp"|"getpeername"|"getnetbyname"|"gethostbyname"|"getgrnam"|"getgrgid"|"exists"|"caller"
BARE_HANDLE_ACCEPTORS = "truncate"|"syswrite"|"sysseek"|"sysread"|"sysopen"|"stat"|"select"|"seekdir"|"seek"|"read"|"opendir"|"open"|"lstat"|"ioctl"|"flock"|"fcntl"|"binmode"
NAMED_UNARY_BARE_HANDLE_ACCEPTORS = "write"|"telldir"|"tell"|"rewinddir"|"readdir"|"getc"|"fileno"|"eof"|"closedir"|"close"|"chdir"
LIST_OPERATORS = "warn"|"waitpid"|"vec"|"utime"|"untie"|"tied"|"tie"|"system"|"syscall"|"symlink"|"substr"|"sprintf"|"socketpair"|"socket"|"shutdown"|"shmwrite"|"shmread"|"shmget"|"shmctl"|"setsockopt"|"setpriority"|"setpgrp"|"send"|"semop"|"semget"|"semctl"|"rindex"|"rename"|"recv"|"pipe"|"pack"|"msgsnd"|"msgrcv"|"msgget"|"msgctl"|"lock"|"listen"|"link"|"kill"|"join"|"index"|"getsockopt"|"getservbyport"|"getservbyname"|"getprotobynumber"|"getpriority"|"getnetbyaddr"|"gethostbyaddr"|"formline"|"exec"|"dump"|"die"|"dbmopen"|"dbmclose"|"crypt"|"connect"|"chown"|"chmod"|"bind"|"atan2"|"accept"
NAMED_ARGUMENTLESS = "wait"|"times"|"time"|"setpwent"|"setgrent"|"getservent"|"getpwent"|"getprotoent"|"getppid"|"getnetent"|"getlogin"|"gethostent"|"getgrent"|"fork"|"endservent"|"endpwent"|"endprotoent"|"endnetent"|"endhostent"|"endgrent"|"break"
IMPLICIT_USERS = "unpack"|"unlink"|"ucfirst"|"uc"|"study"|"stat"|"sqrt"|"sin"|"rmdir"|"ref"|"readpipe"|"readlink"|"quotemeta"|"pos"|"ord"|"oct"|"mkdir"|"lstat"|"log"|"length"|"lcfirst"|"lc"|"int"|"hex"|"glob"|"fc"|"exp"|"evalbytes"|"cos"|"chroot"|"chr"|"chop"|"chomp"|"alarm"|"abs"
CORE_LIST = "NEXT"|"bigrat"|"version"|"Win32"|"Memoize"|"experimental"|"bignum"|"bigint"|"autodie"|"Socket"|"DB_File"|"parent"|"Encode"|"Digest"|"Fatal"|"perlfaq"|"CPAN"|"encoding"
AMBIGOUS_PACKAGES="version"|"JSON"|"YAML"|"Test"
TEXT_OPERATORS = "x"|"and"|"or"|"xor"|"lt"|"gt"|"le"|"ge"|"ne"|"eq"|"cmp"|"isa"

REGEX_COMMENT = "(?#"[^)]*")"
REGEX_ARRAY_NEGATING = [\^\:\\\[\{\.\,\'\"})]
REGEX_HASH_NEGATING = [\^\:\\\[\{\.\,]
HANDLE_NEGATING = {MAY_BE_SPACES_OR_COMMENTS} ("("|"->"|":")
PRINT_HANDLE_NEGATING = {MAY_BE_SPACES_OR_COMMENTS} ("("|"->"|":"|";"|"=>"|"&&"|"||"|",")

BRACE_AFTER_SPACE_COMMENTS = {MAY_BE_SPACES_OR_COMMENTS}"{"

//REGEX_CHAR_CLASS = "\\" [dswDSW]
REGEX_POSIX_CHARGROUPS = "alpha"|"alnum"|"ascii"|"cntrl"|"digit"|"graph"|"lower"|"print"|"punct"|"space"|"uppper"|"xdigit"|"word"|"blank"
POSIX_CHARGROUP = "[:" "^"? {REGEX_POSIX_CHARGROUPS} ":]"
POSIX_CHARGROUP_DOUBLE = "[[:" "^"? {REGEX_POSIX_CHARGROUPS} ":]]"
POSIX_CHARGROUP_ANY = {POSIX_CHARGROUP}|{POSIX_CHARGROUP_DOUBLE}

%state OCT_SUBSTITUTION, SUBSTITUTION_CODE_OCT, OCT_SUBSTITUTION_AMBIGUOUS
%state HEX_SUBSTITUTION
%state UNICODE_SUBSTITUTION, UNICODE_SUBSTITUTION_IN_BRACE, SUBSTITUTION_CODE, SUBSTITUTION_CLOSE_BRACE
%state LEX_HANDLE, LEX_HANDLE_STRICT,LEX_PRINT_HANDLE,LEX_PRINT_HANDLE_STRICT
%xstate END_BLOCK
%xstate POD_STATE, ENDLESS_POD_STATE

%xstate QUOTE_LIKE_OPENER_Q, QUOTE_LIKE_OPENER_QQ, QUOTE_LIKE_OPENER_QX, QUOTE_LIKE_OPENER_QW, QUOTE_LIKE_OPENER_GLOB
%xstate QUOTE_LIKE_OPENER_Q_NOSHARP, QUOTE_LIKE_OPENER_QQ_NOSHARP, QUOTE_LIKE_OPENER_QX_NOSHARP, QUOTE_LIKE_OPENER_QW_NOSHARP

%xstate TRANS_OPENER, TRANS_OPENER_NO_SHARP
%xstate REGEX_OPENER, REGEX_OPENER_NO_SHARP

%state PACKAGE_ARGUMENTS, VERSION_OR_OPERAND, REQUIRE_ARGUMENTS, BRACED_STRING, VERSION_OR_OPERAND_OR_ATTR
%state VERSION_OR_OPERAND_USE, BAREWORD_USE
%state PACKAGE_DECLARATION_ARGUMENTS, CLASS_DECLARATION_ARGUMENTS

%xstate STRING_Q, STRING_Q_CHAR, STRING_Q_ESC, STRING_Q_CONTINUE
%xstate STRING_LIST, STRING_LIST_CHAR
%xstate STRING_QQ, STRING_QQ_CHAR,
%xstate STRING_QQ_RESTRICTED, STRING_QQ_RESTRICTED_CHAR
%xstate STRING_RE, STRING_RE_CHAR
%xstate STRING_QX, STRING_QX_CHAR
%xstate STRING_QX_RESTRICTED, STRING_QX_RESTRICTED_CHAR
%xstate STRING_TR_BEGIN, STRING_TR, STRING_TR_NOT_MINUS, STRING_TR_CHAR

%xstate MATCH_REGEX, MATCH_REGEX_X, MATCH_REGEX_XX
%xstate MATCH_REGEX_SQ, MATCH_REGEX_X_SQ, MATCH_REGEX_XX_SQ
%xstate REGEX_CHARCLASS_X, REGEX_CHARCLASS_XX
%xstate REGEX_QUOTED_X
%xstate BLOCK_IN_MATCH_REGEX

%state PREPARSED_ITEMS

%xstate BLOCK_DECLARATION
%xstate SUB_DECLARATION, METHOD_DECLARATION
%xstate SUB_DECLARATION_CONTENT, METHOD_DECLARATION_CONTENT
%xstate SUB_ATTRIBUTES, SUB_PROTOTYPE
%xstate SUB_ATTRIBUTE

%state HANDLE_WITH_ANGLE, DOUBLE_ANGLE_CLOSE

%state VARIABLE_DECLARATION,VARIABLE_DECLARATION_STRICT,VAR_ATTRIBUTES,VAR_ATTRIBUTES_START

// this is a hack for stupid tailing commas in calls
%state QUOTED_HEREDOC_OPENER, BARE_HEREDOC_OPENER, BACKREF_HEREDOC_OPENER
%state QUOTED_HEREDOC_OPENER_INDENTABLE, BARE_HEREDOC_OPENER_INDENTABLE, BACKREF_HEREDOC_OPENER_INDENTABLE

%state USE_VARS_STRING
%xstate ANNOTATION,ANNOTATION_KEY,ANNOTATION_TYPE,ANNOTATION_TYPE_NESTED,ANNOTATION_PACKAGE_RETURNS,ANNOTATION_STRING,ANNOTATION_FALLBACK
%xstate ANNOTATION_TYPE_AFTER_VAR

%state VARIABLE_UNBRACED, VARIABLE_BRACED

%state AFTER_VARIABLE, AFTER_IDENTIFIER, AFTER_DEREFERENCE, AFTER_VALUE,
%xstate AFTER_RIGHT_BRACE
%state AFTER_POSSIBLE_SIGIL

%xstate CAPTURE_FORMAT,CAPTURE_FORMAT_NON_EMPTY
%xstate CAPTURE_HEREDOC, CAPTURE_NON_EMPTY_HEREDOC
%xstate CAPTURE_HEREDOC_INDENTABLE_TERMINATOR
%xstate CAPTURE_HEREDOC_WITH_EMPTY_MARKER, CAPTURE_NON_EMPTY_HEREDOC_WITH_EMPTY_MARKER

%state AFTER_IDENTIFIER_WITH_LABEL
%xstate LEX_SUB_NAME
%xstate INTERPOLATED_VARIABLE_SUFFIX

// skips and pops state
%xstate AFTER_TRY, AFTER_TRY_BLOCK
%xstate BEFORE_TRY_TRYCATCH, AFTER_TRY_TRYCATCH, AFTER_TRY_TRYCATCH_BLOCK
%xstate AFTER_CATCH_TRYCATCH
%xstate AFTER_EXCEPT, AFTER_EXCEPT_BLOCK, AFTER_OTHERWISE, AFTER_OTHERWISE_BLOCK
%xstate AFTER_CONTINUATION, AFTER_CONTINUATION_BLOCK
%xstate AFTER_CATCH, AFTER_CATCH_BLOCK, BEFORE_CATCH_BLOCK,CATCH_PACKAGE,BEFORE_WITH
%xstate AFTER_FINALLY, AFTER_FINALLY_BLOCK

%xstate FORCE_PACKAGE_TOKEN

%%

<LEX_SUB_NAME>{
	{IDENTIFIER_CONTINUE} |
	{CAPPED_SINGLE_LETTER_VARIABLE_NAME} |
	{SPECIAL_VARIABLE_NAME} {popState();return SUB_NAME;}
}

/////////////////////////////////////// pod capture ////////////////////////////////////////////////////////////////////

<POD_STATE> {
	{POD_END}  	{yybegin(YYINITIAL);return POD;}
}
<ENDLESS_POD_STATE,POD_STATE> {
	.+			{}
	\R+			{}
	<<EOF>>		{yybegin(YYINITIAL);return POD;}
}

/////////////////////////////////// end of pod capture /////////////////////////////////////////////////////////////////

/////////////////////////////////// heredoc capture ////////////////////////////////////////////////////////////////////

<CAPTURE_NON_EMPTY_HEREDOC_WITH_EMPTY_MARKER>{
	.+ \R?	{}
	\R		{yypushback(1);yybegin(CAPTURE_HEREDOC_WITH_EMPTY_MARKER);return heredocQueue.peek().getTargetElement();}
	<<EOF>>	{yybegin(YYINITIAL);return heredocQueue.peek().getTargetElement();}
}

<CAPTURE_HEREDOC_WITH_EMPTY_MARKER>{
	\R		{
		heredocQueue.poll();
		popState();

		if (!heredocQueue.isEmpty())
		{
			startHeredocCapture();
		}
		return HEREDOC_END;
	}
	.+ \R?	{yybegin(CAPTURE_NON_EMPTY_HEREDOC_WITH_EMPTY_MARKER);}
}

<CAPTURE_NON_EMPTY_HEREDOC>{
	.+			{
		if( isCloseMarker())
		{
			pullback(0);
			yybegin(CAPTURE_HEREDOC);
			return heredocQueue.peek().getTargetElement();
		}
	}
	\R+			{}
	<<EOF>>		{yybegin(YYINITIAL);return heredocQueue.peek().getTargetElement();}
}

<CAPTURE_HEREDOC>{
	.+		{
		if( isCloseMarker()){
			PerlHeredocQueueElement currentHeredoc = heredocQueue.poll();
			if( currentHeredoc.isIndentable() ){
			        pullback(0);
			        yybegin(CAPTURE_HEREDOC_INDENTABLE_TERMINATOR);
			}
			else{
        			popState();
                		return HEREDOC_END;
			}
		}
		else
		{
			yybegin(CAPTURE_NON_EMPTY_HEREDOC);
		}
	}
	\R		{yybegin(CAPTURE_NON_EMPTY_HEREDOC);}
}

<CAPTURE_HEREDOC_INDENTABLE_TERMINATOR>{
  {WHITE_SPACE}+    {return TokenType.WHITE_SPACE;}
  {NON_SPACE}.*     {popState();return HEREDOC_END_INDENTABLE;}
}

////////////////////////////////// end of heredoc capture //////////////////////////////////////////////////////////////


/////////////////////////////////// format capture /////////////////////////////////////////////////////////////////////
<CAPTURE_FORMAT_NON_EMPTY>{
	"." 		{yypushback(1);yybegin(CAPTURE_FORMAT);return FORMAT;}
	.+			{}
	\R			{}
	<<EOF>>		{yybegin(YYINITIAL);return FORMAT;}
}


<CAPTURE_FORMAT>{
	"." 		{yybegin(YYINITIAL);return FORMAT_TERMINATOR;}
	.+			{yybegin(CAPTURE_FORMAT_NON_EMPTY);}
	\R			{yybegin(CAPTURE_FORMAT_NON_EMPTY);}
}
/////////////////////////////////// end of format capture //////////////////////////////////////////////////////////////

/////////////////////////////////// quote like openers /////////////////////////////////////////////////////////////////

<QUOTE_LIKE_OPENER_Q, QUOTE_LIKE_OPENER_QQ, QUOTE_LIKE_OPENER_QX, QUOTE_LIKE_OPENER_QW, TRANS_OPENER, REGEX_OPENER>{
	{WHITE_SPACE}+	{setNoSharpState(); return TokenType.WHITE_SPACE;}
	{NEW_LINE}		{return getNewLineToken();}
}

<QUOTE_LIKE_OPENER_Q_NOSHARP,QUOTE_LIKE_OPENER_QQ_NOSHARP,QUOTE_LIKE_OPENER_QX_NOSHARP,QUOTE_LIKE_OPENER_QW_NOSHARP,TRANS_OPENER_NO_SHARP,REGEX_OPENER_NO_SHARP>
{
	{WHITE_SPACE}+	{return TokenType.WHITE_SPACE;}
	{LINE_COMMENT}	{return COMMENT_LINE;}
	{NEW_LINE}		{return getNewLineToken();}
}

<QUOTE_LIKE_OPENER_Q, QUOTE_LIKE_OPENER_QQ, QUOTE_LIKE_OPENER_QX, QUOTE_LIKE_OPENER_QW,QUOTE_LIKE_OPENER_Q_NOSHARP, QUOTE_LIKE_OPENER_QQ_NOSHARP, QUOTE_LIKE_OPENER_QX_NOSHARP, QUOTE_LIKE_OPENER_QW_NOSHARP>{
	[^]	{return captureString();}
}

<TRANS_OPENER,TRANS_OPENER_NO_SHARP> [^] {return captureTr();}
<REGEX_OPENER,REGEX_OPENER_NO_SHARP> [^] {return captureRegex();}

/////////////////////////////////// annotations ////////////////////////////////////////////////////////////////////////
<ANNOTATION_FALLBACK>{
        {WHITE_SPACE}+		{return TokenType.WHITE_SPACE;}
	[\S].*			{yybegin(YYINITIAL);return COMMENT_LINE;}
        [^]                     {yypushback(1);yybegin(YYINITIAL);}
}

<ANNOTATION> "#@"			{yybegin(ANNOTATION_KEY);}

<ANNOTATION_STRING,ANNOTATION_TYPE,ANNOTATION_TYPE_AFTER_VAR,ANNOTATION_PACKAGE_RETURNS,ANNOTATION_TYPE_NESTED>{
	{WHITE_SPACE}+			{return TokenType.WHITE_SPACE;}
}

<ANNOTATION_TYPE,ANNOTATION_TYPE_AFTER_VAR,ANNOTATION_PACKAGE_RETURNS>{
        "ArrayRef"              {yybegin(ANNOTATION_FALLBACK);pushStateAndBegin(ANNOTATION_TYPE_NESTED);return TYPE_ARRAYREF;}
        "HashRef"               {yybegin(ANNOTATION_FALLBACK);pushStateAndBegin(ANNOTATION_TYPE_NESTED);return TYPE_HASHREF;}
}

<ANNOTATION_TYPE_NESTED>{
        "ArrayRef"              {pushStateAndBegin(ANNOTATION_TYPE_NESTED);return TYPE_ARRAYREF;}
        "HashRef"               {pushStateAndBegin(ANNOTATION_TYPE_NESTED);return TYPE_HASHREF;}
}

<ANNOTATION_TYPE>{
  "$" {VARIABLE_NAME}          {yybegin(ANNOTATION_TYPE_AFTER_VAR);return ANNOTATION_SCALAR;}
  "@" {VARIABLE_NAME}          {yybegin(ANNOTATION_TYPE_AFTER_VAR);return ANNOTATION_ARRAY;}
  "%" {VARIABLE_NAME}          {yybegin(ANNOTATION_TYPE_AFTER_VAR);return ANNOTATION_HASH;}
}

<ANNOTATION_TYPE_NESTED>{
        "["                     {return LEFT_BRACKET;}
        "]"                     {popState();return RIGHT_BRACKET;}
	"__PACKAGE__"           {return TAG_PACKAGE;}
	{QUALIFIED_IDENTIFIER}	{return PACKAGE;}
        "*"                     {return OPERATOR_MUL;}
}

<ANNOTATION_TYPE,ANNOTATION_TYPE_AFTER_VAR,ANNOTATION_TYPE_NESTED,ANNOTATION_PACKAGE_RETURNS>{
	"__PACKAGE__"           {yybegin(ANNOTATION_FALLBACK);return TAG_PACKAGE;}
	{QUALIFIED_IDENTIFIER}	{yybegin(ANNOTATION_FALLBACK);return PACKAGE;}
        "*"                     {yybegin(ANNOTATION_FALLBACK);return OPERATOR_MUL;}
}

<ANNOTATION_STRING>{
	{IDENTIFIER}			{yybegin(ANNOTATION_FALLBACK);return STRING_CONTENT;}
}

<ANNOTATION_KEY>{
	"deprecated"	{yybegin(ANNOTATION_FALLBACK);return ANNOTATION_DEPRECATED_KEY;}
	"returns"		{yybegin(ANNOTATION_PACKAGE_RETURNS);return ANNOTATION_RETURNS_KEY;}
	"type"			{yybegin(ANNOTATION_TYPE);return ANNOTATION_TYPE_KEY;}
	"override"		{yybegin(ANNOTATION_FALLBACK);return ANNOTATION_OVERRIDE_KEY;}
	"method"		{yybegin(ANNOTATION_FALLBACK);return ANNOTATION_METHOD_KEY;}
	"noinject"		{yybegin(ANNOTATION_FALLBACK);return ANNOTATION_NO_INJECT_KEY;}
	"abstract"		{yybegin(ANNOTATION_FALLBACK);return ANNOTATION_ABSTRACT_KEY;}
	"inject"		{yybegin(ANNOTATION_STRING);return ANNOTATION_INJECT_KEY;}
	"noinspection"	{yybegin(ANNOTATION_STRING);return ANNOTATION_NOINSPECTION_KEY;}
	{IDENTIFIER}	{yybegin(ANNOTATION_FALLBACK);return ANNOTATION_UNKNOWN_KEY;}
	<<EOF>>			{yybegin(YYINITIAL);return COMMENT_LINE;}
}

<ANNOTATION,ANNOTATION_TYPE,ANNOTATION_TYPE_AFTER_VAR,ANNOTATION_TYPE_NESTED,ANNOTATION_PACKAGE_RETURNS,ANNOTATION_KEY,ANNOTATION_STRING>
{
	[^]				{yypushback(1);yybegin(ANNOTATION_FALLBACK);}
}
/////////////////////////////////// end of annotations /////////////////////////////////////////////////////////////////

<QUOTED_HEREDOC_OPENER>{
	{DQ_STRING}   { return captureQuotedHeredocMarker(HEREDOC_QQ, QUOTE_LIKE_OPENER_QQ, false);}
	{SQ_STRING}   { return captureQuotedHeredocMarker(HEREDOC, QUOTE_LIKE_OPENER_Q, false);}
	{XQ_STRING}   { return captureQuotedHeredocMarker(HEREDOC_QX, QUOTE_LIKE_OPENER_QX, false);}
}

<BACKREF_HEREDOC_OPENER>{
	{UNQUOTED_HEREDOC_MARKER} {return captureBareHeredocMarker(HEREDOC, false);}
}

<BARE_HEREDOC_OPENER>{
	"\\" 			  {yybegin(BACKREF_HEREDOC_OPENER);return OPERATOR_REFERENCE;}
	{UNQUOTED_HEREDOC_MARKER} {return captureBareHeredocMarker(HEREDOC_QQ, false);}
}

<QUOTED_HEREDOC_OPENER_INDENTABLE>{
	{DQ_STRING}   { return captureQuotedHeredocMarker(HEREDOC_QQ, QUOTE_LIKE_OPENER_QQ, true);}
	{SQ_STRING}   { return captureQuotedHeredocMarker(HEREDOC, QUOTE_LIKE_OPENER_Q, true);}
	{XQ_STRING}   { return captureQuotedHeredocMarker(HEREDOC_QX, QUOTE_LIKE_OPENER_QX, true);}
}

<BACKREF_HEREDOC_OPENER_INDENTABLE>{
	{UNQUOTED_HEREDOC_MARKER} {return captureBareHeredocMarker(HEREDOC, true);}
}

<BARE_HEREDOC_OPENER_INDENTABLE>{
	"\\" 			  {yybegin(BACKREF_HEREDOC_OPENER_INDENTABLE);return OPERATOR_REFERENCE;}
	{UNQUOTED_HEREDOC_MARKER} {return captureBareHeredocMarker(HEREDOC_QQ, true);}
}


<INTERPOLATED_VARIABLE_SUFFIX>{
	"{" / {WHITE_SPACE}* {BAREWORD_MINUS} {WHITE_SPACE}* "}"
											{return startBracedBlockWithState(BRACED_STRING);	}
	"["										{
                    if( isLastChar()){
                      pushback();popState();
                    }
                    else{
                      return startBracketedBlock();
                    }
                }
	"["	/ {REGEX_ARRAY_NEGATING}			{pushback();popState();}
	"{"	/ {REGEX_HASH_NEGATING}				{pushback();popState();}
	"{"										{
                    if( isLastChar()){
                      pushback();popState();
                    }
                    else{
                      return startBracedBlock();
                    }
                }
	"->" / [\{\[] {REGEX_ARRAY_NEGATING} 	{pushback();popState();}
	"->" / [\{\[]							{return OPERATOR_DEREFERENCE;}
	[^]										{yypushback(1);popState();}
	<<EOF>>									{yybegin(YYINITIAL);}
}

<REGEX_CHARCLASS_X, REGEX_CHARCLASS_XX>{
  {POSIX_CHARGROUP_ANY} {return REGEX_TOKEN;}
  "]"                   {popState();return REGEX_TOKEN;}
}

<REGEX_CHARCLASS_XX>{
  {ANY_SPACE}+	          {return TokenType.WHITE_SPACE;}
  [^\\\]\[$@ \t\f\n\r]+   {return REGEX_TOKEN;}
}

<REGEX_CHARCLASS_X>{
  [^\\\]\[$@]+            {return REGEX_TOKEN;}
}

<REGEX_QUOTED_X>{
  "\\E"       {popState();return REGEX_TOKEN;}
  [^\\$@]+    {return REGEX_TOKEN;}
}

<REGEX_CHARCLASS_X,REGEX_QUOTED_X,REGEX_CHARCLASS_XX>{
  "\\".     {return REGEX_TOKEN;}
  [^]       {return REGEX_TOKEN;}
  <<EOF>>   {popState();}
}

<MATCH_REGEX_XX,MATCH_REGEX_XX_SQ>{
        "["                             {pushStateAndBegin(REGEX_CHARCLASS_XX);return REGEX_TOKEN;}
}

<MATCH_REGEX_X,MATCH_REGEX_XX,MATCH_REGEX_X_SQ,MATCH_REGEX_XX_SQ>
{
	{ESCAPED_SPACE_OR_COMMENT}	{return REGEX_TOKEN;}
	{ANY_SPACE}+			{return TokenType.WHITE_SPACE;}
	{LINE_COMMENT}			{return COMMENT_LINE;}
        "["                             {pushStateAndBegin(REGEX_CHARCLASS_X);return REGEX_TOKEN;}
        "\\Q"                           {pushStateAndBegin(REGEX_QUOTED_X);return REGEX_TOKEN;}
        <MATCH_REGEX,MATCH_REGEX_SQ>{
            "(??" / "{"      { pushStateAndBegin(BLOCK_IN_MATCH_REGEX);return REGEX_TOKEN; }
            "(?" / "{"       { pushStateAndBegin(BLOCK_IN_MATCH_REGEX);return REGEX_TOKEN; }
            {REGEX_COMMENT}  {return COMMENT_LINE;}
            "@" / [\]%\\]    {return REGEX_TOKEN;}
            "$" / \s+ "]"    {return REGEX_TOKEN;}
            "\\".            {return REGEX_TOKEN;}
	}
}

<BLOCK_IN_MATCH_REGEX>{
  "{"	{
              popState();
              return startBracedBlock();
        }
}


<MATCH_REGEX_X_SQ,MATCH_REGEX_XX_SQ>
	[^\\ \t\f\n\r#\(\[]+	{return REGEX_TOKEN;}
<MATCH_REGEX_X,MATCH_REGEX_XX>
	[^\\ \t\f\n\r$@#\(\[]+	{return REGEX_TOKEN;}
<MATCH_REGEX_SQ>
	[^\\\(\[]+		{return REGEX_TOKEN;}
<MATCH_REGEX>
	[^$@\\\(\[]+		{return REGEX_TOKEN;}

<MATCH_REGEX,MATCH_REGEX_X,MATCH_REGEX_XX,MATCH_REGEX_SQ,MATCH_REGEX_X_SQ,MATCH_REGEX_XX_SQ>
	[^] 			{return REGEX_TOKEN;}

<MATCH_REGEX, MATCH_REGEX_X,MATCH_REGEX_XX,REGEX_CHARCLASS_X,REGEX_CHARCLASS_XX,REGEX_QUOTED_X>{
  "$" / {MAY_BE_SPACES_OR_COMMENTS}[(|)]      {return REGEX_TOKEN;}
  "$#" / {MAY_BE_SPACES_OR_COMMENTS}[(|)]     {return REGEX_TOKEN;}
  "@" / {MAY_BE_SPACES_OR_COMMENTS}[(|)]      {return REGEX_TOKEN;}
}

//////////////////////////////////// END OF REGULAR EXPRESSION /////////////////////////////////////////////////////////

//////////////////////////////////// TRY-CATCH SYNTAXES ////////////////////////////////////////////////////////////////

<AFTER_TRY,AFTER_TRY_TRYCATCH,AFTER_CATCH,AFTER_CATCH_TRYCATCH,AFTER_FINALLY,BEFORE_CATCH_BLOCK,CATCH_PACKAGE,BEFORE_WITH,
  AFTER_TRY_BLOCK,AFTER_CATCH_BLOCK,AFTER_EXCEPT_BLOCK,AFTER_OTHERWISE_BLOCK,AFTER_FINALLY_BLOCK,AFTER_CONTINUATION_BLOCK,
  AFTER_EXCEPT, AFTER_CONTINUATION,  AFTER_OTHERWISE, AFTER_OTHERWISE_BLOCK,
  AFTER_TRY_TRYCATCH_BLOCK, AFTER_CATCH_TRYCATCH
  >{
  {NEW_LINE}   				{return getNewLineToken();}
  {WHITE_SPACE}+  			{return TokenType.WHITE_SPACE;}
  {LINE_COMMENT}			{return COMMENT_LINE;}
}

<AFTER_TRY>                       "{"            {return startBracedBlock(AFTER_TRY_BLOCK);}
<BEFORE_TRY_TRYCATCH>             "try"          {yybegin(AFTER_TRY_TRYCATCH);return RESERVED_TRY;}
<AFTER_TRY_TRYCATCH>              "{"            {return startBracedBlock(AFTER_TRY_TRYCATCH_BLOCK);}
<AFTER_EXCEPT>                    "{"            {return startBracedBlock(AFTER_EXCEPT_BLOCK);}
<AFTER_OTHERWISE>                 "{"            {return startBracedBlock(AFTER_OTHERWISE_BLOCK);}
<AFTER_CATCH,BEFORE_CATCH_BLOCK>  "{"            {return startBracedBlock(AFTER_CATCH_BLOCK);}
<AFTER_CATCH_TRYCATCH>            "{"            {return startBracedBlock(YYINITIAL);}
<AFTER_FINALLY>                   "{"            {return startBracedBlock(AFTER_FINALLY_BLOCK);}
<AFTER_CONTINUATION>              "{"            {return startBracedBlock(AFTER_CONTINUATION_BLOCK);}

<AFTER_CATCH>{
  // TryCatch:: condition
  "("            {return startParethesizedBlock(BEFORE_CATCH_BLOCK, CATCH_PACKAGE);}

  // Error:: condition
  {QUALIFIED_IDENTIFIER}  / {SPACE_OR_COMMENT}"with" {yybegin(BEFORE_WITH);return PACKAGE;}
}

<BEFORE_WITH>     "with"  {yybegin(BEFORE_CATCH_BLOCK);return RESERVED_CATCH_WITH;}
<CATCH_PACKAGE>   {QUALIFIED_IDENTIFIER}	{yybegin(YYINITIAL);return PACKAGE;}

<AFTER_TRY_BLOCK,AFTER_CATCH_BLOCK,AFTER_FINALLY_BLOCK,AFTER_EXCEPT_BLOCK,AFTER_OTHERWISE_BLOCK,AFTER_CONTINUATION_BLOCK>{
  "catch"        / {MAY_BE_SPACES_OR_COMMENTS} ("("|"{"|{QUALIFIED_IDENTIFIER}) {yybegin(AFTER_CATCH); return RESERVED_CATCH;}
}

<AFTER_TRY_TRYCATCH_BLOCK>{
  "catch"        / {MAY_BE_SPACES_OR_COMMENTS} "{" {yybegin(AFTER_CATCH_TRYCATCH); return RESERVED_CATCH;}
  [^]            {    yypushback(1);yybegin(YYINITIAL);}
}

<AFTER_TRY_BLOCK,AFTER_CATCH_BLOCK,AFTER_FINALLY_BLOCK,AFTER_EXCEPT_BLOCK,AFTER_OTHERWISE_BLOCK,AFTER_CONTINUATION_BLOCK>{
  "except"       / {BRACE_AFTER_SPACE_COMMENTS} {yybegin(AFTER_EXCEPT); return RESERVED_EXCEPT;}
  "otherwise"    / {BRACE_AFTER_SPACE_COMMENTS} {yybegin(AFTER_OTHERWISE); return RESERVED_OTHERWISE;}
  "finally"	 / {BRACE_AFTER_SPACE_COMMENTS} {yybegin(AFTER_FINALLY); return RESERVED_FINALLY;}
  "continuation" / {BRACE_AFTER_SPACE_COMMENTS} {yybegin(AFTER_CONTINUATION); return RESERVED_CONTINUATION;}

  [^]            {yypushback(1);yybegin(AFTER_VALUE);}
}

<AFTER_TRY,AFTER_TRY_TRYCATCH,AFTER_CATCH,AFTER_CATCH_TRYCATCH,AFTER_FINALLY,CATCH_PACKAGE,BEFORE_CATCH_BLOCK,CATCH_PACKAGE,BEFORE_WITH,AFTER_EXCEPT,AFTER_OTHERWISE,AFTER_CONTINUATION>
  [^]            {yypushback(1);yybegin(YYINITIAL);}


//////////////////////////////////// END OF TRY-CATCH SYNTAXES /////////////////////////////////////////////////////////

<STRING_QQ,STRING_RE> "@" [(|)] {return STRING_CONTENT_QQ;}
<STRING_QX> "@" [(|)] {return STRING_CONTENT_XQ;}

<STRING_QQ,STRING_RE,STRING_QX,MATCH_REGEX,MATCH_REGEX_X,MATCH_REGEX_XX,REGEX_QUOTED_X,REGEX_CHARCLASS_X,REGEX_CHARCLASS_XX>
{
	"@" /  {NON_SPACE_AHEAD} 	{pushState();yybegin(INTERPOLATED_VARIABLE_SUFFIX);return startUnbracedVariable(SIGIL_ARRAY);}
	"$#" / {NON_SPACE_AHEAD} 	{return startUnbracedVariable(SIGIL_SCALAR_INDEX);}
	"$" /  {NON_SPACE_AHEAD}   	{pushState();yybegin(INTERPOLATED_VARIABLE_SUFFIX);return startUnbracedVariable(SIGIL_SCALAR); }
}

<STRING_QQ,STRING_RE,STRING_QX,STRING_TR,STRING_TR_NOT_MINUS>{
        "\\t"     {return STRING_SPECIAL_TAB;}
        "\\n"     {return STRING_SPECIAL_NEWLINE;}
        "\\r"     {return STRING_SPECIAL_RETURN;}
        "\\f"     {return STRING_SPECIAL_FORMFEED;}
        "\\b"     {return STRING_SPECIAL_BACKSPACE;}
        "\\a"     {return STRING_SPECIAL_ALARM;}
        "\\e"     {return STRING_SPECIAL_ESCAPE;}

        "\\l"     {return STRING_SPECIAL_LCFIRST;}
        "\\u"     {return STRING_SPECIAL_TCFIRST;}

        "\\c" [^]? {return STRING_SPECIAL_SUBST;}

        "\\L"     {return STRING_SPECIAL_LOWERCASE_START;}
        "\\U"     {return STRING_SPECIAL_UPPERCASE_START;}
        "\\F"     {return STRING_SPECIAL_FOLDCASE_START;}
        "\\E"     {return STRING_SPECIAL_MODIFIER_END;}
        "\\Q"     {return STRING_SPECIAL_QUOTE_START;}
        "\\N"     {pushState();yybegin(UNICODE_SUBSTITUTION);return STRING_SPECIAL_UNICODE;}
        "\\x"     {pushState();yybegin(HEX_SUBSTITUTION);return STRING_SPECIAL_HEX;}
        "\\o"       {pushState();yybegin(OCT_SUBSTITUTION);return STRING_SPECIAL_OCT;}
        "\\" / "0"  {pushState();yybegin(OCT_SUBSTITUTION_AMBIGUOUS);return STRING_SPECIAL_OCT_AMBIGUOUS;}
}

<STRING_RE>{
        "\\"[1-9]\d*   {return STRING_SPECIAL_BACKREF;}
}

<STRING_QQ,STRING_QX>{
        "\\" / {OCT_DIGIT}      {pushState();yybegin(OCT_SUBSTITUTION_AMBIGUOUS);return STRING_SPECIAL_OCT_AMBIGUOUS;}
}

<STRING_TR_CHAR> [^]             {yybegin(STRING_TR);return STRING_CONTENT_QQ;}
<STRING_QQ_CHAR> [^]             {yybegin(STRING_QQ);return STRING_CONTENT_QQ;}
<STRING_RE_CHAR> [^]             {yybegin(STRING_RE);return STRING_CONTENT_QQ;}
<STRING_QX_CHAR> [^]             {yybegin(STRING_QX);return STRING_CONTENT_XQ;}
<STRING_QQ_RESTRICTED_CHAR> [^]             {yybegin(STRING_QQ_RESTRICTED);return STRING_CONTENT_QQ;}
<STRING_QX_RESTRICTED_CHAR> [^]             {yybegin(STRING_QX_RESTRICTED);return STRING_CONTENT_XQ;}

<STRING_QQ>{
        "\\"              {yybegin(STRING_QQ_CHAR);return STRING_SPECIAL_ESCAPE_CHAR;}
	// chars with special treatments
	[^$\@\\]+					{return STRING_CONTENT_QQ;}
	[^]						{return STRING_CONTENT_QQ;}
}

<STRING_RE>{
        "\\"              {yybegin(STRING_RE_CHAR);return STRING_SPECIAL_ESCAPE_CHAR;}
	// chars with special treatments
	[^$\@\\]+					{return STRING_CONTENT_QQ;}
	[^]						{return STRING_CONTENT_QQ;}
}

<STRING_QX>{
        "\\"              {yybegin(STRING_QX_CHAR);return STRING_SPECIAL_ESCAPE_CHAR;}
	// chars with special treatments
	[^\$\@\\]+					{return STRING_CONTENT_XQ;}
	[^]						{return STRING_CONTENT_XQ;}
}

// single quoted analogs of strings, same escaping rules as in q string with single quote quote
<STRING_QQ_RESTRICTED>{
  [^\\]+          {return STRING_CONTENT_QQ;}
  "\\" / ['\\]    {yybegin(STRING_QQ_RESTRICTED_CHAR);return STRING_SPECIAL_ESCAPE_CHAR;}
  "\\"            {return STRING_CONTENT_QQ;}
}
<STRING_QX_RESTRICTED>{
  [^\\]+          {return STRING_CONTENT_XQ;}
  "\\" / ['\\]    {yybegin(STRING_QX_RESTRICTED_CHAR);return STRING_SPECIAL_ESCAPE_CHAR;}
  "\\"            {return STRING_CONTENT_XQ;}
}

<STRING_TR_BEGIN>{
  "-"             {yybegin(STRING_TR);return STRING_CONTENT_QQ;}
  [^]             {yypushback(1);yybegin(STRING_TR);}
}

<STRING_TR>{
  "-" / [^]         {yybegin(STRING_TR_NOT_MINUS);return STRING_SPECIAL_RANGE;}
  <STRING_TR_NOT_MINUS>{
    [^\\\-]+          {return STRING_CONTENT_QQ;}
    "-"               {return STRING_CONTENT_QQ;}
    "\\"              {yybegin(STRING_TR_CHAR);return STRING_SPECIAL_ESCAPE_CHAR;}
  }
}

<OCT_SUBSTITUTION_AMBIGUOUS>{
  {SIMPLE_OCT_NUMBER_SHORT} {popState();return NUMBER_OCT;}
  [^]                       {popState();yypushback(1);}
}

<OCT_SUBSTITUTION>{
  "{"                 {yybegin(SUBSTITUTION_CODE_OCT);return STRING_SPECIAL_LEFT_BRACE;}
  [^]                 {popState();yypushback(1);}
}

<SUBSTITUTION_CODE_OCT>{
  {SIMPLE_OCT_NUMBER} {yybegin(SUBSTITUTION_CLOSE_BRACE);return NUMBER_OCT;}
  [^]                 {yypushback(1);yybegin(SUBSTITUTION_CLOSE_BRACE);}
}

<HEX_SUBSTITUTION>{
  {HEX_NUMBER_SHORT}  {popState();return NUMBER_HEX;}
  "{"                 {yybegin(SUBSTITUTION_CODE);return STRING_SPECIAL_LEFT_BRACE;}
  [^]                 {popState();yypushback(1);}
}

<UNICODE_SUBSTITUTION>{
  "{"   {yybegin(UNICODE_SUBSTITUTION_IN_BRACE);return STRING_SPECIAL_LEFT_BRACE;}
  [^]   {yypushback(1);popState();}
}

<UNICODE_SUBSTITUTION_IN_BRACE>{
  "U+"                {yybegin(SUBSTITUTION_CODE);return STRING_SPECIAL_UNICODE_CODE_PREFIX;}
  {UNICODE_CHAR_NAME} {yybegin(SUBSTITUTION_CLOSE_BRACE);return STRING_CHAR_NAME;}
  [^]                 {yypushback(1);yybegin(SUBSTITUTION_CLOSE_BRACE);}
}

<SUBSTITUTION_CODE>{
  {SIMPLE_HEX_NUMBER} {yybegin(SUBSTITUTION_CLOSE_BRACE);return NUMBER_HEX;}
  [^]                 {yypushback(1);yybegin(SUBSTITUTION_CLOSE_BRACE);}
}

<SUBSTITUTION_CLOSE_BRACE>{
  "}"                 {popState();return STRING_SPECIAL_RIGHT_BRACE;}
  [^\}]+              {return TokenType.BAD_CHARACTER;}
}

<STRING_LIST_CHAR> [^]                  {yybegin(STRING_LIST);return STRING_CONTENT;}

<STRING_LIST>
{
  {ANY_SPACE}            {return TokenType.WHITE_SPACE;}
  [^\\\s]+               {return STRING_CONTENT;}
  "\\"                   {
    IElementType tokenType = getSQBackSlashTokenType();
    if( tokenType == STRING_SPECIAL_ESCAPE_CHAR){
        yybegin(STRING_LIST_CHAR);
    }
    return tokenType;
  }
}

// we've got some text, continuing
<STRING_Q_CONTINUE>{
	[^\\]+			       {}
        "\\"                           {
          IElementType tokenType = getSQBackSlashTokenType();
          if( tokenType == STRING_SPECIAL_ESCAPE_CHAR){
              yybegin(STRING_Q_ESC);
              yypushback(1);
              return STRING_CONTENT;
          }
        }
        <<EOF>>                        {yybegin(YYINITIAL);return STRING_CONTENT;}
}

<STRING_Q_CHAR>       [^]              {yybegin(STRING_Q_CONTINUE);}
<STRING_Q_ESC>        [^]              {yybegin(STRING_Q_CHAR);return STRING_SPECIAL_ESCAPE_CHAR;}

<STRING_Q>{
	[^\\]+				{yybegin(STRING_Q_CONTINUE);}
        "\\"                            {
        IElementType tokenType = getSQBackSlashTokenType();
        if( tokenType == STRING_CONTENT){
            yybegin(STRING_Q_CONTINUE);
        }
        else{
            yybegin(STRING_Q_CHAR);
            return tokenType;
        }
      }
}

^{POD_START} 				{yybegin(POD_STATE);}

{NEW_LINE}   				{return getNewLineToken();}
{WHITE_SPACE}+  			{return TokenType.WHITE_SPACE;}
{END_BLOCK}				{yybegin(END_BLOCK);yypushback(yylength()-7);return TAG_END;}
{DATA_BLOCK}				{yybegin(END_BLOCK);yypushback(yylength()-8);return TAG_DATA;}
{LINE_COMMENT_ANNOTATION}	        {return COMMENT_ANNOTATION;}
{LINE_COMMENT}				{return COMMENT_LINE;}

<HANDLE_WITH_ANGLE>{
        "$" / {VARIABLE_QUALIFIED_IDENTIFIER} ">"          {return startUnbracedVariable(SIGIL_SCALAR);}
        {QUALIFYING_PACKAGE} / {IDENTIFIER_CONTINUE} ">"   {return QUALIFYING_PACKAGE;}
	{IDENTIFIER} 	                                   {return HANDLE;}
	">"				                   {yybegin(AFTER_IDENTIFIER);return RIGHT_ANGLE;}
	[^]				                   {yybegin(YYINITIAL);yypushback(1);break;}
}

<END_BLOCK>{
  {POD_START}           {yybegin(ENDLESS_POD_STATE);}
  {LINE_TO_END}         {return COMMENT_BLOCK;}
  [^] 		        {return COMMENT_BLOCK;}
}

/////////////////////////////////  subs, anon subs, methods, etc //////////////////////////////////////////////////////////
<SUB_DECLARATION,METHOD_DECLARATION, BLOCK_DECLARATION,
  SUB_ATTRIBUTES, SUB_ATTRIBUTE,
  SUB_DECLARATION_CONTENT, METHOD_DECLARATION_CONTENT>{
  {NEW_LINE}   				{return getNewLineToken();}
  {WHITE_SPACE}+  			{return TokenType.WHITE_SPACE;}
  {LINE_COMMENT}			{return COMMENT_LINE;}
}

<SUB_DECLARATION>{
	{QUALIFIED_IDENTIFIER} 		{pushStateAndBegin(YYINITIAL, SUB_DECLARATION_CONTENT);return getIdentifierTokenWithoutIndex();}
	("("|"{"|":")                   {pushStateAndBegin(AFTER_VALUE, SUB_DECLARATION_CONTENT);yypushback(1);}
}

<METHOD_DECLARATION>{
	{QUALIFIED_IDENTIFIER} 		{pushStateAndBegin(YYINITIAL,METHOD_DECLARATION_CONTENT);return getIdentifierTokenWithoutIndex();}
}

<SUB_DECLARATION,METHOD_DECLARATION>{
	[^]                             {yypushback(1);yybegin(YYINITIAL);}
}

<METHOD_DECLARATION_CONTENT>{
	"(" 						{return startParethesizedBlock(SUB_ATTRIBUTES, YYINITIAL, SUB_SIGNATURE);}
}

<SUB_DECLARATION_CONTENT>{
	"(" / {SUB_PROTOTYPE_WITH_SPACES}? ")"	{return startParethesizedBlock(SUB_ATTRIBUTES, SUB_PROTOTYPE);}
	"(" 					{return startParethesizedBlock(SUB_ATTRIBUTES, YYINITIAL, SUB_SIGNATURE);}
}

<BLOCK_DECLARATION> "{"     {yybegin(YYINITIAL); return getLeftBrace();}

<SUB_DECLARATION_CONTENT,SUB_ATTRIBUTES,METHOD_DECLARATION_CONTENT>{
	"{"     					{popState(); return getLeftBrace();}
}

<SUB_ATTRIBUTE>{
	{IDENTIFIER} / "("	{pushState();yybegin(QUOTE_LIKE_OPENER_Q);return ATTRIBUTE_IDENTIFIER;}
	{IDENTIFIER}		{return ATTRIBUTE_IDENTIFIER;}
	[^]                     {yypushback(1);popState();}
}

<SUB_DECLARATION_CONTENT,SUB_ATTRIBUTES, METHOD_DECLARATION_CONTENT>{
	":"							{yybegin(SUB_ATTRIBUTES);pushState();yybegin(SUB_ATTRIBUTE);return COLON;}
	[^]							{popState();yypushback(1);yybegin(YYINITIAL);}
}

<SUB_PROTOTYPE>{
        {WHITE_SPACE}                   {return TokenType.WHITE_SPACE;}
	{SUB_PROTOTYPE}			{return SUB_PROTOTYPE_TOKEN;}
	")"						{return getRightParen(SUB_ATTRIBUTES);}
}
/////////////////////////////////  END OF subs, anon subs, methods, etc //////////////////////////////////////////////////////////

<VAR_ATTRIBUTES_START> ":"	{yybegin(VAR_ATTRIBUTES); return COLON;}

<VAR_ATTRIBUTES>
{
	":"					{return COLON;}
	{IDENTIFIER} / "("	{pushState();yybegin(QUOTE_LIKE_OPENER_Q);return ATTRIBUTE_IDENTIFIER;}
	{IDENTIFIER}		{return ATTRIBUTE_IDENTIFIER;}
}

<VAR_ATTRIBUTES,VAR_ATTRIBUTES_START> [^]	{yypushback(1);yybegin(AFTER_VARIABLE);}

<VARIABLE_DECLARATION>
{
	{CORE_PREFIX}"sub" 		{yybegin(SUB_DECLARATION);return  RESERVED_SUB;}
	"__PACKAGE__"                   {yybegin(VARIABLE_DECLARATION_STRICT);return TAG_PACKAGE;}
	{QUALIFIED_IDENTIFIER}		{yybegin(VARIABLE_DECLARATION_STRICT);return PACKAGE;}
	<VARIABLE_DECLARATION_STRICT>{
		"$" 		{return startUnbracedVariable(VAR_ATTRIBUTES_START, SIGIL_SCALAR);}
		"@"			{return startUnbracedVariable(VAR_ATTRIBUTES_START, SIGIL_ARRAY);}
		"%"			{return startUnbracedVariable(VAR_ATTRIBUTES_START, SIGIL_HASH);}
		"("			{return startParethesizedBlock(VAR_ATTRIBUTES_START, YYINITIAL);}
		[^]			{yypushback(1);yybegin(AFTER_VARIABLE);}
	}
}


<VERSION_OR_OPERAND_USE>{
	{PERL_VERSION}					{yybegin(BAREWORD_USE);return NUMBER_VERSION;}
}

<VERSION_OR_OPERAND_OR_ATTR>{
        ":"               {yybegin(VAR_ATTRIBUTES); return COLON;}
	{PERL_VERSION}	  {yybegin(VAR_ATTRIBUTES_START);return NUMBER_VERSION;}
        [^]               {yypushback(1); yybegin(YYINITIAL);}
}

<VERSION_OR_OPERAND,REQUIRE_ARGUMENTS,PACKAGE_ARGUMENTS,PACKAGE_DECLARATION_ARGUMENTS>{
	{PERL_VERSION}					{yybegin(YYINITIAL);return NUMBER_VERSION;}
}

<VERSION_OR_OPERAND_USE,BAREWORD_USE>{
  "-"{IDENTIFIER}  {yybegin(YYINITIAL); return STRING_CONTENT;}
}

<VERSION_OR_OPERAND,VERSION_OR_OPERAND_USE,REQUIRE_ARGUMENTS,BAREWORD_USE>{
	[^]								{yypushback(1);yybegin(YYINITIAL);}
}

<PACKAGE_ARGUMENTS>{
	{QUALIFIED_IDENTIFIER}			{yybegin(VERSION_OR_OPERAND_USE);return registerUse();}
	[^]								{yybegin(YYINITIAL);pushback();}
}

<CLASS_DECLARATION_ARGUMENTS>{
	{QUALIFIED_IDENTIFIER}			{yybegin(VERSION_OR_OPERAND_OR_ATTR);return registerPackage(PACKAGE);}
}

<PACKAGE_DECLARATION_ARGUMENTS>{
	{QUALIFIED_IDENTIFIER}			{yybegin(VERSION_OR_OPERAND);return registerPackage(PACKAGE);}
        <CLASS_DECLARATION_ARGUMENTS>{
	  [^]								{yybegin(YYINITIAL);pushback();}
        }
}

<USE_VARS_STRING>
{
	"@" 	{return startUnbracedVariable(SIGIL_ARRAY);}
	"$#" 	{return startUnbracedVariable(SIGIL_SCALAR_INDEX);}
	"$" 	{return startUnbracedVariable(SIGIL_SCALAR); }
	"%"		{return startUnbracedVariable(SIGIL_HASH);}
	"*" 	{return startUnbracedVariable(SIGIL_GLOB);}
	"&"		{return startUnbracedVariable(SIGIL_CODE);}
}

<VARIABLE_UNBRACED>{
        // we are here after a sigil, and we can't cast to the $OLD_PERL_VERSION variable $]
        "$" / "]" {return getUnbracedVariableNameToken();}
	// this is a subset of builtins, $;, $, for example, can't be dereferenced
	"$" / [\{\"\'\[\]\`\\\!\%\&\(\+\-\.\/\<\=\>\|\~\?\:\*\^\@\_\$\:\w_\d]		{return processUnbracedScalarSigil();}
	"{"											{return startBracedVariable();}
	{VARIABLE_NAME}								{return getUnbracedVariableNameToken();}
}

<VARIABLE_BRACED>{
	{VARIABLE_NAME}	/ {MAY_BE_SPACES_OR_COMMENTS}"}"				{return getBracedVariableNameToken();}
	{CAPPED_BRACED_VARIABLE} / {MAY_BE_SPACES_OR_COMMENTS}"}"		{return getBracedVariableNameToken();}
	[^]														{yypushback(1);yybegin(YYINITIAL);}
}

////////////////////////// COMMON PART /////////////////////////////////////////////////////////////////////////////////

<BRACED_STRING>{
	{BAREWORD_MINUS}	{return STRING_CONTENT;}
	"}"					{return getRightBrace(AFTER_VARIABLE);}
}

<YYINITIAL,AFTER_VARIABLE,AFTER_VALUE,AFTER_IDENTIFIER,AFTER_IDENTIFIER_WITH_LABEL,LEX_HANDLE,LEX_HANDLE_STRICT,LEX_PRINT_HANDLE,LEX_PRINT_HANDLE_STRICT>{
	{CORE_PREFIX}"if"	 	{ yybegin(YYINITIAL); return RESERVED_IF;}
	{CORE_PREFIX}"unless"	{ yybegin(YYINITIAL); return RESERVED_UNLESS;}
	{CORE_PREFIX}"while"	{ yybegin(YYINITIAL); return RESERVED_WHILE;}
	{CORE_PREFIX}"until"	{ yybegin(YYINITIAL); return RESERVED_UNTIL;}
	{CORE_PREFIX}"for"	 	{ yybegin(YYINITIAL); return RESERVED_FOR;}
	{CORE_PREFIX}"foreach"	{ yybegin(YYINITIAL); return RESERVED_FOREACH;}
	{CORE_PREFIX}"when"	 	{ yybegin(YYINITIAL); return RESERVED_WHEN;}
}

<YYINITIAL,AFTER_IDENTIFIER>{
	":"  	{yybegin(YYINITIAL);return COLON;}
}

<AFTER_VALUE,AFTER_VARIABLE,AFTER_IDENTIFIER>{
	"*" 	{yybegin(AFTER_POSSIBLE_SIGIL);return OPERATOR_MUL;}
	"%" 	{yybegin(AFTER_POSSIBLE_SIGIL);return OPERATOR_MOD;}
	"&" 	{yybegin(AFTER_POSSIBLE_SIGIL);return OPERATOR_BITWISE_AND;}

	"<" 	{yybegin(YYINITIAL);return OPERATOR_LT_NUMERIC;}

        <AFTER_IDENTIFIER_WITH_LABEL>{
          "&&" 	{yybegin(YYINITIAL);return OPERATOR_AND;}
          "**"	{yybegin(YYINITIAL);return OPERATOR_POW;}
          "%=" 	{yybegin(YYINITIAL);return OPERATOR_MOD_ASSIGN;}
          "*=" 	{yybegin(YYINITIAL);return OPERATOR_MUL_ASSIGN;}
          "&" "."? "=" 	{yybegin(YYINITIAL);return OPERATOR_BITWISE_AND_ASSIGN;}
          "**=" 	{yybegin(YYINITIAL);return OPERATOR_POW_ASSIGN;}
          "&&="	{yybegin(YYINITIAL);return OPERATOR_AND_ASSIGN;}

          // ambiguous with double negation
          "~~"	{yybegin(YYINITIAL);return OPERATOR_SMARTMATCH;}

          // ambiguous with nyi
          "..." 	{yybegin(YYINITIAL);return OPERATOR_HELLIP;}
        }
}

<YYINITIAL>{
  "isa"       {yybegin(AFTER_IDENTIFIER);return SUB_NAME;}
}

<AFTER_RIGHT_BRACE, YYINITIAL>{
  {TEXT_OPERATORS} / {MAY_BE_SPACES} ":"	{yybegin(YYINITIAL); return IDENTIFIER;}
}

<AFTER_VALUE,YYINITIAL,AFTER_VARIABLE,AFTER_IDENTIFIER,LEX_HANDLE,LEX_HANDLE_STRICT,LEX_PRINT_HANDLE,LEX_PRINT_HANDLE_STRICT,AFTER_IDENTIFIER_WITH_LABEL>{
	"x" / [\d]*	{yybegin(YYINITIAL); return OPERATOR_X;}
        <AFTER_RIGHT_BRACE>{
          "and"		{yybegin(YYINITIAL); return OPERATOR_AND_LP;}
          "or"		{yybegin(YYINITIAL); return OPERATOR_OR_LP;}
          "xor"		{yybegin(YYINITIAL); return OPERATOR_XOR_LP;}
        }
	"lt" / {QUOTE_LIKE_SUFFIX}		{yybegin(YYINITIAL); return OPERATOR_LT_STR;}
	"gt" / {QUOTE_LIKE_SUFFIX}		{yybegin(YYINITIAL); return OPERATOR_GT_STR;}
	"le" / {QUOTE_LIKE_SUFFIX}		{yybegin(YYINITIAL); return OPERATOR_LE_STR;}
	"ge" / {QUOTE_LIKE_SUFFIX}		{yybegin(YYINITIAL); return OPERATOR_GE_STR;}
	"eq" / {QUOTE_LIKE_SUFFIX} 		{yybegin(YYINITIAL); return OPERATOR_EQ_STR;}
	"ne" / {QUOTE_LIKE_SUFFIX}		{yybegin(YYINITIAL); return OPERATOR_NE_STR;}
	"cmp" / {QUOTE_LIKE_SUFFIX} 	{yybegin(YYINITIAL); return OPERATOR_CMP_STR;}
        "isa" {yybegin(YYINITIAL); return OPERATOR_ISA;}
}

<AFTER_RIGHT_BRACE> {
  {NEW_LINE}   				{return getNewLineToken();}
  {WHITE_SPACE}+  			{return TokenType.WHITE_SPACE;}
  [^]   {yypushback(1);yybegin(YYINITIAL);}
}

<AFTER_IDENTIFIER_WITH_LABEL>{
        {IDENTIFIER} / "::" {yybegin(YYINITIAL);pushback();}
        {IDENTIFIER} / {MAY_BE_SPACES_OR_COMMENTS} "(" {yybegin(YYINITIAL);pushback();}
	{IDENTIFIER}	{yybegin(YYINITIAL);return IDENTIFIER;}
	[^]				{yypushback(1);yybegin(YYINITIAL);}
}

<YYINITIAL>{
	"..." 	{yybegin(YYINITIAL);return OPERATOR_NYI;}
}

<AFTER_DEREFERENCE>{
	"$*"	{yybegin(AFTER_VALUE);return DEREF_SCALAR;}
	"$#*"	{yybegin(AFTER_VALUE);return DEREF_SCALAR_INDEX;}
	"@*"	{yybegin(AFTER_VALUE);return DEREF_ARRAY;}
	"%*"	{yybegin(AFTER_VALUE);return DEREF_HASH;}
	"**"	{yybegin(AFTER_VALUE);return DEREF_GLOB;}
	"&*"	{yybegin(AFTER_VALUE);return DEREF_CODE;}

	"@"		{yybegin(AFTER_VARIABLE); return SIGIL_ARRAY;}
	"%"		{yybegin(AFTER_VARIABLE); return SIGIL_HASH;}
	"*"		{yybegin(AFTER_VARIABLE); return SIGIL_GLOB;}
	"&"		{yybegin(AFTER_VARIABLE); return SIGIL_CODE;}
}

// normal variable, unambiguous
<YYINITIAL,AFTER_DEREFERENCE,AFTER_IDENTIFIER,AFTER_VARIABLE,AFTER_POSSIBLE_SIGIL,AFTER_VALUE>{
	"@" 	{return startUnbracedVariable(AFTER_VARIABLE, SIGIL_ARRAY);}
	"$#" 	{return startUnbracedVariable(AFTER_VARIABLE, SIGIL_SCALAR_INDEX);}
	"$" 	{return startUnbracedVariable(AFTER_VARIABLE, SIGIL_SCALAR); }
}

// ambiguous variable in unambiguous situation
<YYINITIAL,AFTER_POSSIBLE_SIGIL>{
	"%"		{return startUnbracedVariable(AFTER_VARIABLE, SIGIL_HASH);}
	"*"		{return startUnbracedVariable(AFTER_VARIABLE, SIGIL_GLOB);}
        "&&" 	        {yybegin(YYINITIAL);return OPERATOR_AND;}
	"&"		{return startUnbracedVariable(AFTER_VARIABLE, SIGIL_CODE);}
}

// ambiguous variable in ambiguous situation
<AFTER_IDENTIFIER>
{
        "&" / {MAY_BE_SPACES_OR_COMMENTS} "~"  {yybegin(AFTER_POSSIBLE_SIGIL);return OPERATOR_BITWISE_AND;}
	"%" / {AMBIGUOUS_SIGIL_SUFFIX}	{return startUnbracedVariable(AFTER_VARIABLE, SIGIL_HASH);}
	"*"	/ {AMBIGUOUS_SIGIL_SUFFIX}	{return startUnbracedVariable(AFTER_VARIABLE, SIGIL_GLOB);}
        "&&" 	{yybegin(YYINITIAL);return OPERATOR_AND;}
	"&"	/ {AMBIGUOUS_SIGIL_SUFFIX}	{return startUnbracedVariable(AFTER_VARIABLE, SIGIL_CODE);}
}

// postfix operators
<AFTER_VARIABLE,AFTER_VALUE>{
	"++" 	{yybegin(AFTER_VALUE);return OPERATOR_PLUS_PLUS;}
	"--" 	{yybegin(AFTER_VALUE);return OPERATOR_MINUS_MINUS;}
}

// prefix operators
<YYINITIAL>{
	"++" 	{yybegin(YYINITIAL);return OPERATOR_PLUS_PLUS;}
	"--" 	{yybegin(YYINITIAL);return OPERATOR_MINUS_MINUS;}
}

<DOUBLE_ANGLE_CLOSE> ">>" {yybegin(AFTER_IDENTIFIER);return RIGHT_ANGLE;}

// operands and starters
<YYINITIAL>{
        <AFTER_IDENTIFIER,AFTER_VARIABLE> {
          "<" / "$"? {QUALIFIED_IDENTIFIER_WITHOUT_TRAILING_SEPARATOR}? ">"	{yybegin(HANDLE_WITH_ANGLE);return LEFT_ANGLE;}
          "<<" / ">>"                           {yybegin(DOUBLE_ANGLE_CLOSE);return LEFT_ANGLE;}
        }
	"<"    {pushStateAndBegin(AFTER_VALUE, QUOTE_LIKE_OPENER_GLOB);return captureString();}
}

<AFTER_VALUE,AFTER_VARIABLE,AFTER_IDENTIFIER>{
	"/"   	{yybegin(YYINITIAL);return OPERATOR_DIV;}
        <AFTER_RIGHT_BRACE>{
        	"//" 	{yybegin(YYINITIAL);return OPERATOR_OR_DEFINED;}
        }
	"/=" 	{yybegin(YYINITIAL);return OPERATOR_DIV_ASSIGN;}
	"//=" 	{yybegin(YYINITIAL);return OPERATOR_OR_DEFINED_ASSIGN;}
}

<YYINITIAL>{
	"/"   {regexCommand=RESERVED_M; sectionsNumber=1; pushStateAndBegin(AFTER_VALUE,AFTER_VALUE); return captureRegex();}
}

<YYINITIAL>{
	"switch"					{return getPerlSwitchToken(RESERVED_SWITCH);}
	"case"						{return getPerlSwitchToken(RESERVED_CASE);}
}

// known identifiers
<YYINITIAL,AFTER_IDENTIFIER,AFTER_VARIABLE,LEX_HANDLE,LEX_HANDLE_STRICT,LEX_PRINT_HANDLE,LEX_PRINT_HANDLE_STRICT>{
	"split"						{yybegin(YYINITIAL); return BUILTIN_LIST;}
	{BARE_HANDLE_ACCEPTORS}				{yybegin(LEX_HANDLE);return BUILTIN_LIST;}
	{NAMED_UNARY_BARE_HANDLE_ACCEPTORS}	{yybegin(LEX_HANDLE); return BUILTIN_UNARY;}
	{NAMED_UNARY_OPERATORS}				{yybegin(AFTER_IDENTIFIER); return BUILTIN_UNARY;}
	{TYPES_TINY}                            {yybegin(AFTER_IDENTIFIER);return CUSTOM_UNARY;}
	{READONLY}                            {yybegin(AFTER_IDENTIFIER);return SUB_NAME;}

	{IMPLICIT_USERS}					{yybegin(AFTER_IDENTIFIER);return BUILTIN_UNARY;}
	{PERL_OPERATORS_FILETEST} / [^a-zA-Z0-9_] 	{yybegin(LEX_HANDLE);return OPERATOR_FILETEST;}

	{NAMED_ARGUMENTLESS}				{yybegin(AFTER_VALUE);return BUILTIN_ARGUMENTLESS;}	// fixme we can return special token here to help parser
	{LIST_OPERATORS}					{yybegin(YYINITIAL);return BUILTIN_LIST;}

	"not"						{yybegin(YYINITIAL); return OPERATOR_NOT_LP;}

	{CORE_PREFIX}"my"		{yybegin(VARIABLE_DECLARATION); return RESERVED_MY;}
	{CORE_PREFIX}"field"    	{yybegin(VARIABLE_DECLARATION); return RESERVED_FIELD;}
	{CORE_PREFIX}"our"		{yybegin(VARIABLE_DECLARATION); return RESERVED_OUR;}
	{CORE_PREFIX}"local"		{yybegin(YYINITIAL); return RESERVED_LOCAL;}
	{CORE_PREFIX}"state"		{yybegin(VARIABLE_DECLARATION); return RESERVED_STATE;}

	{CORE_PREFIX}"elsif"	 	{yybegin(YYINITIAL); return RESERVED_ELSIF;}
	{CORE_PREFIX}"else"	 		{yybegin(YYINITIAL); return RESERVED_ELSE;}
	{CORE_PREFIX}"given"	 	{yybegin(YYINITIAL); return RESERVED_GIVEN;}
	{CORE_PREFIX}"default"	 	{yybegin(YYINITIAL); return RESERVED_DEFAULT;}
	{CORE_PREFIX}"continue"	 	{yybegin(YYINITIAL); return RESERVED_CONTINUE;}

	{CORE_PREFIX}"format"	 	{myFormatWaiting = true; yybegin(AFTER_IDENTIFIER); return RESERVED_FORMAT;}

        "async" / {MUST_BE_SPACES_OR_COMMENTS} {CORE_PREFIX}"sub"   {return RESERVED_ASYNC;}
	{CORE_PREFIX}"sub" 		                  {yybegin(SUB_DECLARATION);return  RESERVED_SUB;}

	{CORE_PREFIX}"package"	 	{yybegin(PACKAGE_DECLARATION_ARGUMENTS); return RESERVED_PACKAGE;}
	{CORE_PREFIX}"class"	 	{yybegin(CLASS_DECLARATION_ARGUMENTS); return RESERVED_CLASS;}
	{CORE_PREFIX}"use"	 		{yybegin(PACKAGE_ARGUMENTS); return RESERVED_USE;}
	{CORE_PREFIX}"no"	 		{yybegin(PACKAGE_ARGUMENTS); return RESERVED_NO;}
	{CORE_PREFIX}"require"	 	{yybegin(REQUIRE_ARGUMENTS); return RESERVED_REQUIRE;}

	{CORE_PREFIX}"undef"		{yybegin(YYINITIAL); return RESERVED_UNDEF;}

        "async" / {MUST_BE_SPACES_OR_COMMENTS} "method" {return RESERVED_ASYNC;}
	"method" / {MAY_BE_SPACES_OR_COMMENTS}"("      {pushStateAndBegin(YYINITIAL,METHOD_DECLARATION_CONTENT); return RESERVED_METHOD_FP;}
	"method"			        {yybegin(METHOD_DECLARATION); return RESERVED_METHOD;}
	"func"				        {yybegin(METHOD_DECLARATION); return RESERVED_FUNC;}
	"fun"  / {FP_SUFFIX}		        {yybegin(METHOD_DECLARATION); return RESERVED_FUN;}
	"fun"  / {MAY_BE_SPACES_OR_COMMENTS}"("        {pushStateAndBegin(YYINITIAL,METHOD_DECLARATION_CONTENT); return RESERVED_FUN;}
        "override" / {FP_SUFFIX}                {yybegin(METHOD_DECLARATION); return RESERVED_OVERRIDE_FP;}
        "after" / {FP_SUFFIX}                   {yybegin(METHOD_DECLARATION); return RESERVED_AFTER_FP;}
        "before" / {FP_SUFFIX}                  {yybegin(METHOD_DECLARATION); return RESERVED_BEFORE_FP;}
        "augment" / {FP_SUFFIX}                 {yybegin(METHOD_DECLARATION); return RESERVED_AUGMENT_FP;}
        "around" / {FP_SUFFIX}                  {yybegin(METHOD_DECLARATION); return RESERVED_AROUND_FP;}

	"try" / {BRACE_AFTER_SPACE_COMMENTS} { return handleTry();}

        // this is Moose's with, not Exception's
	"with"					{yybegin(YYINITIAL); return RESERVED_WITH;}
        "extends"				{yybegin(YYINITIAL); return RESERVED_EXTENDS;}
	"meta"					{yybegin(YYINITIAL); return RESERVED_META;}
	"override"				{yybegin(YYINITIAL); return RESERVED_OVERRIDE;}
	"around"				{yybegin(YYINITIAL); return RESERVED_AROUND;}
	"augment"				{yybegin(YYINITIAL); return RESERVED_AUGMENT;}
	"after"					{yybegin(YYINITIAL); return RESERVED_AFTER;}
	"before"				{yybegin(YYINITIAL); return RESERVED_BEFORE;}
	"has"					{yybegin(YYINITIAL); return RESERVED_HAS;}
	"super"				        {yybegin(YYINITIAL); return RESERVED_SUPER;}
	"inner"				        {yybegin(YYINITIAL); return RESERVED_INNER;}

	// special treatment?
	{CORE_PREFIX}"print"	 	{yybegin(LEX_PRINT_HANDLE); return RESERVED_PRINT;}
	{CORE_PREFIX}"printf"	 	{yybegin(LEX_PRINT_HANDLE); return RESERVED_PRINTF;}
	{CORE_PREFIX}"say"	 		{yybegin(LEX_PRINT_HANDLE); return RESERVED_SAY;}

	{CORE_PREFIX}"grep"	 { yybegin(YYINITIAL); return RESERVED_GREP;}
	{CORE_PREFIX}"map"	 { yybegin(YYINITIAL); return RESERVED_MAP;}
	{CORE_PREFIX}"sort"	 { yybegin(YYINITIAL); return RESERVED_SORT;}

	{CORE_PREFIX}"do"	 { yybegin(YYINITIAL); return RESERVED_DO;}
	{CORE_PREFIX}"eval"	 { yybegin(YYINITIAL); return RESERVED_EVAL;}

	{CORE_PREFIX}"goto"	 { yybegin(AFTER_IDENTIFIER_WITH_LABEL); return RESERVED_GOTO;}
	{CORE_PREFIX}"redo"	 { yybegin(AFTER_IDENTIFIER_WITH_LABEL); return RESERVED_REDO;}
	{CORE_PREFIX}"next"	 { yybegin(AFTER_IDENTIFIER_WITH_LABEL); return RESERVED_NEXT;}
	{CORE_PREFIX}"last"	 { yybegin(AFTER_IDENTIFIER_WITH_LABEL); return RESERVED_LAST;}

	{CORE_PREFIX}"bless"    { yybegin(YYINITIAL); return RESERVED_BLESS;}
	{CORE_PREFIX}"splice"    { yybegin(YYINITIAL); return RESERVED_SPLICE;}
	{CORE_PREFIX}"delete"    { yybegin(YYINITIAL); return RESERVED_DELETE;}
	{CORE_PREFIX}"wantarray" { yybegin(AFTER_VALUE); return RESERVED_WANTARRAY;}

	{CORE_PREFIX}"defined" / {MAY_BE_SPACES_OR_COMMENTS} "//"	{ yybegin(AFTER_IDENTIFIER); return RESERVED_DEFINED;}
	{CORE_PREFIX}"defined"	                                { yybegin(YYINITIAL); return RESERVED_DEFINED;}

        {CORE_PREFIX}"reverse"   { yybegin(YYINITIAL); return BUILTIN_UNARY;}
	{CORE_PREFIX}"scalar"	 { yybegin(YYINITIAL); return RESERVED_SCALAR;}
	{CORE_PREFIX}"keys"	 { yybegin(YYINITIAL); return RESERVED_KEYS;}
	{CORE_PREFIX}"values"	 { yybegin(YYINITIAL); return RESERVED_VALUES;}
	{CORE_PREFIX}"each"	 { yybegin(YYINITIAL); return RESERVED_EACH;}
	{CORE_PREFIX}"return"	 { yybegin(YYINITIAL); return RESERVED_RETURN;}
	{CORE_PREFIX}"exit"	 { yybegin(AFTER_IDENTIFIER); return RESERVED_EXIT;}
	{CORE_PREFIX}"shift"	 { yybegin(AFTER_IDENTIFIER); return RESERVED_SHIFT;}
	{CORE_PREFIX}"unshift"	 { yybegin(YYINITIAL); return RESERVED_UNSHIFT;}
	{CORE_PREFIX}"push"	 { yybegin(YYINITIAL); return RESERVED_PUSH;}
	{CORE_PREFIX}"pop"	 { yybegin(AFTER_IDENTIFIER); return RESERVED_POP;}

	{BLOCK_NAMES} / {BRACE_AFTER_SPACE_COMMENTS}		{yybegin(BLOCK_DECLARATION);return BLOCK_NAME;}
	{TAG_NAMES}						{yybegin(AFTER_VALUE); return TAG;}

	{CORE_PREFIX}"y"  / {QUOTE_LIKE_SUFFIX} {pushStateAndBegin(TRANS_OPENER);return RESERVED_Y;}
	{CORE_PREFIX}"tr" / {QUOTE_LIKE_SUFFIX} {pushStateAndBegin(TRANS_OPENER);return RESERVED_TR;}

	{CORE_PREFIX}"qw" / {QUOTE_LIKE_SUFFIX} {pushStateAndBegin(AFTER_VALUE, QUOTE_LIKE_OPENER_QW);return RESERVED_QW;}
	{CORE_PREFIX}"qq" / {QUOTE_LIKE_SUFFIX} {pushStateAndBegin(AFTER_VALUE, QUOTE_LIKE_OPENER_QQ);return RESERVED_QQ;}
	{CORE_PREFIX}"qx" / {QUOTE_LIKE_SUFFIX} {pushStateAndBegin(AFTER_VALUE, QUOTE_LIKE_OPENER_QX);return RESERVED_QX;}
	{CORE_PREFIX}"q" / {QUOTE_LIKE_SUFFIX}  {pushStateAndBegin(AFTER_VALUE, QUOTE_LIKE_OPENER_Q);return RESERVED_Q;}

	{CORE_PREFIX}"qr" / {QUOTE_LIKE_SUFFIX} {regexCommand=RESERVED_QR; sectionsNumber=1; pushStateAndBegin(REGEX_OPENER); return regexCommand;}
	{CORE_PREFIX}"m" / {QUOTE_LIKE_SUFFIX}  {regexCommand=RESERVED_M; sectionsNumber=1; pushStateAndBegin(REGEX_OPENER); return regexCommand;}
	{CORE_PREFIX}"s" / {QUOTE_LIKE_SUFFIX}  {regexCommand=RESERVED_S; sectionsNumber=2; pushStateAndBegin(REGEX_OPENER); return regexCommand;}
}

<AFTER_DEREFERENCE>{
	{QUALIFIED_IDENTIFIER} 		{yybegin(AFTER_VALUE);return getIdentifierTokenWithoutIndex();}
}

// following is for require
<REQUIRE_ARGUMENTS>{
	{QUALIFIED_IDENTIFIER} / "("	{yybegin(YYINITIAL);return getIdentifierToken();}
	{QUALIFIED_IDENTIFIER}			{yybegin(AFTER_IDENTIFIER);return PACKAGE;}
}

// handles hash indexes after variable and/or ->
<AFTER_DEREFERENCE,AFTER_VARIABLE>{
	"["															{return startBracketedBlock(AFTER_VARIABLE);}
	"{"															{return startBracedBlock(AFTER_VARIABLE);}
	"{" / {WHITE_SPACE}* {BAREWORD_MINUS} {WHITE_SPACE}* "}"	{return getLeftBrace(BRACED_STRING);}
}

<AFTER_DEREFERENCE> "("     	{return getLeftParen(YYINITIAL);}

<AFTER_VALUE,AFTER_VARIABLE> "." / {NUMBER_INT} {return OPERATOR_CONCAT;}

// always checked except explicit states
<YYINITIAL,AFTER_VARIABLE,AFTER_VALUE,AFTER_IDENTIFIER,AFTER_POSSIBLE_SIGIL>{
	{FARROW} 	{yybegin(YYINITIAL);return FAT_COMMA;}
	"," 		{yybegin(YYINITIAL);return COMMA;}
	";"     	{yybegin(YYINITIAL);return SEMICOLON;}
	"=" 		{yybegin(YYINITIAL);return OPERATOR_ASSIGN;}
	"->" 		{yybegin(AFTER_DEREFERENCE); return OPERATOR_DEREFERENCE;}
	"["     	{return getLeftBracket(YYINITIAL);}
	"("     	{return getLeftParen(YYINITIAL);}
	"{"     	{return getLeftBrace(YYINITIAL);}
	"}"     	{return getRightBrace(AFTER_RIGHT_BRACE);}
	"]"     	{return getRightBracket(AFTER_VALUE);}
	")"     	{return getRightParen(AFTER_VALUE);}
	":"			{yybegin(YYINITIAL);return COLON;}

	"||" 	{yybegin(YYINITIAL);return OPERATOR_OR;}
	">="	{yybegin(YYINITIAL);return OPERATOR_GE_NUMERIC;}
	"<="	{yybegin(YYINITIAL);return OPERATOR_LE_NUMERIC;}
	"=="	{yybegin(YYINITIAL);return OPERATOR_EQ_NUMERIC;}
	"!="	{yybegin(YYINITIAL);return OPERATOR_NE_NUMERIC;}
	"+="	{yybegin(YYINITIAL);return OPERATOR_PLUS_ASSIGN;}
	"-="	{yybegin(YYINITIAL);return OPERATOR_MINUS_ASSIGN;}
	".=" 	{yybegin(YYINITIAL);return OPERATOR_CONCAT_ASSIGN;}
	"x=" 	{yybegin(YYINITIAL);return OPERATOR_X_ASSIGN;}
	"|" "."? "=" 	{yybegin(YYINITIAL);return OPERATOR_BITWISE_OR_ASSIGN;}
	"^" "."? "=" 	{yybegin(YYINITIAL);return OPERATOR_BITWISE_XOR_ASSIGN;}
	"<<=" 	{yybegin(YYINITIAL);return OPERATOR_SHIFT_LEFT_ASSIGN;}
	">>=" 	{yybegin(YYINITIAL);return OPERATOR_SHIFT_RIGHT_ASSIGN;}
	"||=" 	{yybegin(YYINITIAL);return OPERATOR_OR_ASSIGN;}
	"<=>" 	{yybegin(YYINITIAL);return OPERATOR_CMP_NUMERIC;}
	">" 	{yybegin(YYINITIAL);return OPERATOR_GT_NUMERIC;}

	"=~" 	{yybegin(YYINITIAL);return OPERATOR_RE;}
	"!~" 	{yybegin(YYINITIAL);return OPERATOR_NOT_RE;}

	"<<" 	{yybegin(YYINITIAL);setHeredocLike(true);return OPERATOR_SHIFT_LEFT;}
	"<<" / {QUALIFIED_IDENTIFIER}"(" 	{yybegin(YYINITIAL);setHeredocLike(true);return OPERATOR_SHIFT_LEFT;}
	">>" 	{yybegin(YYINITIAL);return OPERATOR_SHIFT_RIGHT;}

	"?"  	{yybegin(YYINITIAL);return QUESTION;}

	"&." 	        {yybegin(YYINITIAL);return OPERATOR_BITWISE_AND;}
	"|" "."? 	{yybegin(YYINITIAL);return OPERATOR_BITWISE_OR;}
	"^" "."? 	{yybegin(YYINITIAL);return OPERATOR_BITWISE_XOR;}

	"+" 		{yybegin(YYINITIAL);return OPERATOR_PLUS;}
	"-" "."? 		{yybegin(YYINITIAL);return OPERATOR_MINUS;}
	"!" 		{yybegin(YYINITIAL);return OPERATOR_NOT;}
	"~" "."? 		{yybegin(YYINITIAL);return OPERATOR_BITWISE_NOT;}
	"\\" 		{yybegin(YYINITIAL);return OPERATOR_REFERENCE;}

	".." 	{yybegin(YYINITIAL);return OPERATOR_FLIP_FLOP;}
	"." 	{yybegin(YYINITIAL);return OPERATOR_CONCAT;}

	"<<" / {QUOTED_HEREDOC_MARKER}   	{yybegin(QUOTED_HEREDOC_OPENER);return OPERATOR_HEREDOC;}
	"<<" / "\\"?{UNQUOTED_HEREDOC_MARKER} 	{yybegin(BARE_HEREDOC_OPENER);return OPERATOR_HEREDOC;}

	"<<~" / {QUOTED_HEREDOC_MARKER_NON_EMPTY} {yybegin(QUOTED_HEREDOC_OPENER_INDENTABLE);return OPERATOR_HEREDOC;}
	"<<~" / "\\"?{UNQUOTED_HEREDOC_MARKER} 	  {yybegin(BARE_HEREDOC_OPENER_INDENTABLE);return OPERATOR_HEREDOC;}

	{DQ_STRING} {pushStateAndBegin(AFTER_VALUE, QUOTE_LIKE_OPENER_QQ);return captureString();}
	{SQ_STRING} {pushStateAndBegin(AFTER_VALUE, QUOTE_LIKE_OPENER_Q);return captureString();}
	{XQ_STRING} {pushStateAndBegin(AFTER_VALUE, QUOTE_LIKE_OPENER_QX);return captureString();}

	// fixme optimize via merging?
	{BAREWORD_MINUS} / {MAY_BE_SPACES_OR_COMMENTS}* {FARROW}	{yybegin(AFTER_VALUE);return STRING_CONTENT;}

	{NUMBER_OCT}									 {yybegin(AFTER_VALUE);return NUMBER_OCT;}
	{NUMBER_BIN}									 {yybegin(AFTER_VALUE);return NUMBER_BIN;}
	{NUMBER_HEX}									 {yybegin(AFTER_VALUE);return NUMBER_HEX;}
	{NUMBER_INT} / ".."                              {yybegin(AFTER_VALUE);return NUMBER;}
	"." {NUMBER_INT} {NUMBER_EXP}?	 				 {yybegin(AFTER_VALUE);return NUMBER;}
	{NUMBER_INT} ("." {NUMBER_INT}? )? {NUMBER_EXP}? {yybegin(AFTER_VALUE);return NUMBER;}
	{PERL_VERSION}  		                {yybegin(AFTER_VALUE);return NUMBER_VERSION;}
}

<LEX_HANDLE>{
	"("						{return getLeftParen(LEX_HANDLE_STRICT);}
	<LEX_HANDLE_STRICT>{
		{IDENTIFIER} / {HANDLE_NEGATING}	{yybegin(YYINITIAL);pushback();}
		{IDENTIFIER}				{yybegin(AFTER_IDENTIFIER);return HANDLE;}
		[^]					{pushback();yybegin(YYINITIAL);}
	}
}

<LEX_PRINT_HANDLE>{
	"("						{return getLeftParen(LEX_PRINT_HANDLE_STRICT);}
	<LEX_PRINT_HANDLE_STRICT>{
		{IDENTIFIER} / {PRINT_HANDLE_NEGATING}	{yybegin(YYINITIAL);pushback();}
		{IDENTIFIER}				{yybegin(YYINITIAL);return HANDLE;}
		[^]					{pushback();yybegin(YYINITIAL);}
	}
}

<FORCE_PACKAGE_TOKEN>{
  {NEW_LINE}   				{return getNewLineToken();}
  {WHITE_SPACE}+  			{return TokenType.WHITE_SPACE;}
  {LINE_COMMENT_ANNOTATION}	        {return COMMENT_ANNOTATION;}
  {LINE_COMMENT}			{return COMMENT_LINE;}

  {IDENTIFIER}                          {popState();return PACKAGE;}
}

<YYINITIAL,AFTER_VARIABLE,AFTER_VALUE,AFTER_IDENTIFIER,AFTER_POSSIBLE_SIGIL>{
        "default" / {ANY_SPACE}* "("  {return SUB_NAME;}

        "new" / {MUST_BE_SPACES_OR_COMMENTS}{AMBIGOUS_PACKAGES}{ANY_SPACE}* "("  {pushStateAndBegin(FORCE_PACKAGE_TOKEN);return SUB_NAME;}
        {AMBIGOUS_PACKAGES} / {ANY_SPACE}* "("                           {return SUB_NAME;}

        "parent" / {MAY_BE_SPACES_OR_COMMENTS}"{"      {return SUB_NAME;}
	{CORE_LIST}				{return PACKAGE;}
	"__PACKAGE__"           {yybegin(AFTER_IDENTIFIER);return TAG_PACKAGE;}
	{QUALIFIED_IDENTIFIER} 	{return getIdentifierToken();}
}

/* error fallback [^] */
[^]    { return TokenType.BAD_CHARACTER; }
