package com.perl5.lang.lexer;

/*
    http://jflex.de/manual.html
    http://www2.cs.tum.edu/projects/cup

*/

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

%%

%class PerlLexer
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
ALFANUM         = [a-zA-Z0-9]

DEPACKAGE = "::"

PACKAGE_NAME = {ALFANUM}+ ({DEPACKAGE}{ALFANUM}+)*
FUNCTION_NAME = {ALFANUM}+
USER_VARIABLE = [$%*@]{ALFANUM}+

END_OF_LINE_COMMENT = "#" {CHAR_ANY}* {LINE_TERMINATOR}?

%{
  StringBuffer stringBuffer = new StringBuffer();

 /* private IElementType element(int type, Object value) {
    return new IElementType(type, value);
  }*/
%}

//%state STRING
%state PACKAGE_DEFINITION
%state FUNCTION_DEFINITION
%state PACKAGE_USE
%state YYINITIAL
%state STRING_SQ
%state STRING_DQ

%%

/*
<STRING_DQ> {
  \"                             { yybegin(YYINITIAL);
                                   return symbol(PERL_STRING,
                                   stringBuffer.toString()); }
  [^\n\r\"\\]+                   { stringBuffer.append( yytext() ); }
  \\t                            { stringBuffer.append('\t'); }
  \\n                            { stringBuffer.append('\n'); }

  \\r                            { stringBuffer.append('\r'); }
  \\\"                           { stringBuffer.append('\"'); }
  \\                             { stringBuffer.append('\\'); }
}

<STRING_SQ> {
  \'                             { yybegin(YYINITIAL);
                                   return symbol(PERL_STRING,
                                   stringBuffer.toString()); }
  \\\'                           { stringBuffer.append('\''); }
  {CHAR_ANY}                     { stringBuffer.append( yytext() ); }
}*/

{CHAR_SEMI}     {yybegin(YYINITIAL);return PERL_SEMI;}
{WHITE_SPACE}   {return TokenType.WHITE_SPACE;}
"{"             {return PERL_LCURLY;}
"}"             {return PERL_RCURLY;}
"["             {return PERL_LSQUARE;}
"]"             {return PERL_RSQUARE;}
"("             {return PERL_LBRACKET;}
")"             {return PERL_RBRACKET;}
","			{return PERL_COMMA;}
"=>"			{return PERL_COMMA;}
"->"			{return PERL_DEREFERENCE;}

///////////////////////////////// PERL VARIABLE ////////////////////////////////////////////////////////////////////////
"$!"			{return PERL_VARIABLE;}
"$^RE_TRIE_MAXBUF"			{return PERL_VARIABLE;}
"$LAST_REGEXP_CODE_RESULT"			{return PERL_VARIABLE;}
\$\"			{return PERL_VARIABLE;}
"$^S"			{return PERL_VARIABLE;}
"$LIST_SEPARATOR"			{return PERL_VARIABLE;}
"$#"			{return PERL_VARIABLE;}
"$^T"			{return PERL_VARIABLE;}
"$MATCH"			{return PERL_VARIABLE;}
"$$"			{return PERL_VARIABLE;}
"$^TAINT"			{return PERL_VARIABLE;}
"$MULTILINE_MATCHING"			{return PERL_VARIABLE;}
"$%"			{return PERL_VARIABLE;}
"$^UNICODE"			{return PERL_VARIABLE;}
"$NR"			{return PERL_VARIABLE;}
"$&"			{return PERL_VARIABLE;}
"$^UTF8LOCALE"			{return PERL_VARIABLE;}
"$OFMT"			{return PERL_VARIABLE;}
"$'"			{return PERL_VARIABLE;}
"$^V"			{return PERL_VARIABLE;}
"$OFS"			{return PERL_VARIABLE;}
"$("			{return PERL_VARIABLE;}
"$^W"			{return PERL_VARIABLE;}
"$ORS"			{return PERL_VARIABLE;}
"$)"			{return PERL_VARIABLE;}
"$^WARNING_BITS"			{return PERL_VARIABLE;}
"$OS_ERROR"			{return PERL_VARIABLE;}
"$*"			{return PERL_VARIABLE;}
"$^WIDE_SYSTEM_CALLS"			{return PERL_VARIABLE;}
"$OSNAME"			{return PERL_VARIABLE;}
"$+"			{return PERL_VARIABLE;}
"$^X"			{return PERL_VARIABLE;}
"$OUTPUT_AUTO_FLUSH"			{return PERL_VARIABLE;}
"$,"			{return PERL_VARIABLE;}
"$_"			{return PERL_VARIABLE;}
"$OUTPUT_FIELD_SEPARATOR"			{return PERL_VARIABLE;}
"$-"			{return PERL_VARIABLE;}
"$`"			{return PERL_VARIABLE;}
"$OUTPUT_RECORD_SEPARATOR"			{return PERL_VARIABLE;}
"$."			{return PERL_VARIABLE;}
"$a"			{return PERL_VARIABLE;}
"$PERL_VERSION"			{return PERL_VARIABLE;}
"$/"			{return PERL_VARIABLE;}
"$ACCUMULATOR"			{return PERL_VARIABLE;}
"$PERLDB"			{return PERL_VARIABLE;}
"$0"			{return PERL_VARIABLE;}
"$ARG"			{return PERL_VARIABLE;}
"$PID"			{return PERL_VARIABLE;}
"$:"			{return PERL_VARIABLE;}
"$ARGV"			{return PERL_VARIABLE;}
"$POSTMATCH"			{return PERL_VARIABLE;}
"$;"			{return PERL_VARIABLE;}
"$b"			{return PERL_VARIABLE;}
"$PREMATCH"			{return PERL_VARIABLE;}
"$<"			{return PERL_VARIABLE;}
"$BASETIME"			{return PERL_VARIABLE;}
"$PROCESS_ID"			{return PERL_VARIABLE;}
"$="			{return PERL_VARIABLE;}
"$CHILD_ERROR"			{return PERL_VARIABLE;}
"$PROGRAM_NAME"			{return PERL_VARIABLE;}
"$>"			{return PERL_VARIABLE;}
"$COMPILING"			{return PERL_VARIABLE;}
"$REAL_GROUP_ID"			{return PERL_VARIABLE;}
"$?"			{return PERL_VARIABLE;}
"$DEBUGGING"			{return PERL_VARIABLE;}
"$REAL_USER_ID"			{return PERL_VARIABLE;}
"$@"			{return PERL_VARIABLE;}
"$EFFECTIVE_GROUP_ID"			{return PERL_VARIABLE;}
"$RS"			{return PERL_VARIABLE;}
"$["			{return PERL_VARIABLE;}
"$EFFECTIVE_USER_ID"			{return PERL_VARIABLE;}
"$SUBSCRIPT_SEPARATOR"			{return PERL_VARIABLE;}
\$\\			{return PERL_VARIABLE;}
"$EGID"			{return PERL_VARIABLE;}
"$SUBSEP"			{return PERL_VARIABLE;}
"$]"			{return PERL_VARIABLE;}
"$ERRNO"			{return PERL_VARIABLE;}
"$SYSTEM_FD_MAX"			{return PERL_VARIABLE;}
"$^"			{return PERL_VARIABLE;}
"$EUID"			{return PERL_VARIABLE;}
"$UID"			{return PERL_VARIABLE;}
"$^A"			{return PERL_VARIABLE;}
"$EVAL_ERROR"			{return PERL_VARIABLE;}
"$WARNING"			{return PERL_VARIABLE;}
"$^C"			{return PERL_VARIABLE;}
"$EXCEPTIONS_BEING_CAUGHT"			{return PERL_VARIABLE;}
"$|"			{return PERL_VARIABLE;}
"$^CHILD_ERROR_NATIVE"			{return PERL_VARIABLE;}
"$EXECUTABLE_NAME"			{return PERL_VARIABLE;}
"$~"			{return PERL_VARIABLE;}
"$^D"			{return PERL_VARIABLE;}
"$EXTENDED_OS_ERROR"			{return PERL_VARIABLE;}
"%!"			{return PERL_VARIABLE;}
"$^E"			{return PERL_VARIABLE;}
"$FORMAT_FORMFEED"			{return PERL_VARIABLE;}
"%^H"			{return PERL_VARIABLE;}
"$^ENCODING"			{return PERL_VARIABLE;}
"$FORMAT_LINE_BREAK_CHARACTERS"			{return PERL_VARIABLE;}
"%ENV"			{return PERL_VARIABLE;}
"$^F"			{return PERL_VARIABLE;}
"$FORMAT_LINES_LEFT"			{return PERL_VARIABLE;}
"%INC"			{return PERL_VARIABLE;}
"$^H"			{return PERL_VARIABLE;}
"$FORMAT_LINES_PER_PAGE"			{return PERL_VARIABLE;}
"%OVERLOAD"			{return PERL_VARIABLE;}
"$^I"			{return PERL_VARIABLE;}
"$FORMAT_NAME"			{return PERL_VARIABLE;}
"%SIG"			{return PERL_VARIABLE;}
"$^L"			{return PERL_VARIABLE;}
"$FORMAT_PAGE_NUMBER"			{return PERL_VARIABLE;}
"@+"			{return PERL_VARIABLE;}
"$^M"			{return PERL_VARIABLE;}
"$FORMAT_TOP_NAME"			{return PERL_VARIABLE;}
"@-"			{return PERL_VARIABLE;}
"$^N"			{return PERL_VARIABLE;}
"$GID"			{return PERL_VARIABLE;}
"@_"			{return PERL_VARIABLE;}
"$^O"			{return PERL_VARIABLE;}
"$INPLACE_EDIT"			{return PERL_VARIABLE;}
"@ARGV"			{return PERL_VARIABLE;}
"$^OPEN"			{return PERL_VARIABLE;}
"$INPUT_LINE_NUMBER"			{return PERL_VARIABLE;}
"@INC"			{return PERL_VARIABLE;}
"$^P"			{return PERL_VARIABLE;}
"$INPUT_RECORD_SEPARATOR"			{return PERL_VARIABLE;}
"@LAST_MATCH_START"			{return PERL_VARIABLE;}
"$^R"			{return PERL_VARIABLE;}
"$LAST_MATCH_END"			{return PERL_VARIABLE;}
"$^RE_DEBUG_FLAGS"			{return PERL_VARIABLE;}
"$LAST_PAREN_MATCH"			{return PERL_VARIABLE;}
{USER_VARIABLE} {return PERL_USER_VARIABLE;}

<PACKAGE_DEFINITION>{
    {PACKAGE_NAME}    {return PERL_PACKAGE;}
}

<FUNCTION_DEFINITION>{
    {FUNCTION_NAME}    {return PERL_USER_FUNCTION;}
}

<PACKAGE_USE>{
    {PACKAGE_NAME}    {yybegin(YYINITIAL);return PERL_PACKAGE;}
}

<YYINITIAL> {
   /* comments */

//  \"                             { yybegin(STRING_DQ); stringBuffer.length(0); }
//  \'                             { yybegin(STRING_SQ); stringBuffer.length(0); }

  {END_OF_LINE_COMMENT}                           { yybegin(YYINITIAL); return PERL_COMMENT; }



  /* whitespace */

/////////////////////////////// RESERVED WORDS /////////////////////////////////////////////////////////////////////////
    "-A"			{return PERL_FUNCTION;}
    "END"			{return PERL_FUNCTION;}
    "length"			{return PERL_FUNCTION;}
    "setpgrp"			{return PERL_FUNCTION;}
    "-B"			{return PERL_FUNCTION;}
    "endgrent"			{return PERL_FUNCTION;}
    "link"			{return PERL_FUNCTION;}
    "setpriority"			{return PERL_FUNCTION;}
    "-b"			{return PERL_FUNCTION;}
    "endhostent"			{return PERL_FUNCTION;}
    "listen"			{return PERL_FUNCTION;}
    "setprotoent"			{return PERL_FUNCTION;}
    "-C"			{return PERL_FUNCTION;}
    "endnetent"			{return PERL_FUNCTION;}
    "local"			{return PERL_FUNCTION;}
    "setpwent"			{return PERL_FUNCTION;}
    "-c"			{return PERL_FUNCTION;}
    "endprotoent"			{return PERL_FUNCTION;}
    "localtime"			{return PERL_FUNCTION;}
    "setservent"			{return PERL_FUNCTION;}
    "-d"			{return PERL_FUNCTION;}
    "endpwent"			{return PERL_FUNCTION;}
    "log"			{return PERL_FUNCTION;}
    "setsockopt"			{return PERL_FUNCTION;}
    "-e"			{return PERL_FUNCTION;}
    "endservent"			{return PERL_FUNCTION;}
    "lstat"			{return PERL_FUNCTION;}
    "shift"			{return PERL_FUNCTION;}
    "-f"			{return PERL_FUNCTION;}
    "eof"			{return PERL_FUNCTION;}
    "map"			{return PERL_FUNCTION;}
    "shmctl"			{return PERL_FUNCTION;}
    "-g"			{return PERL_FUNCTION;}
    "eval"			{return PERL_FUNCTION;}
    "mkdir"			{return PERL_FUNCTION;}
    "shmget"			{return PERL_FUNCTION;}
    "-k"			{return PERL_FUNCTION;}
    "exec"			{return PERL_FUNCTION;}
    "msgctl"			{return PERL_FUNCTION;}
    "shmread"			{return PERL_FUNCTION;}
    "-l"			{return PERL_FUNCTION;}
    "exists"			{return PERL_FUNCTION;}
    "msgget"			{return PERL_FUNCTION;}
    "shmwrite"			{return PERL_FUNCTION;}
    "-M"			{return PERL_FUNCTION;}
    "exit"			{return PERL_FUNCTION;}
    "msgrcv"			{return PERL_FUNCTION;}
    "shutdown"			{return PERL_FUNCTION;}
    "-O"			{return PERL_FUNCTION;}
    "fcntl"			{return PERL_FUNCTION;}
    "msgsnd"			{return PERL_FUNCTION;}
    "sin"			{return PERL_FUNCTION;}
    "-o"			{return PERL_FUNCTION;}
    "fileno"			{return PERL_FUNCTION;}
    "my"			{return PERL_FUNCTION;}
    "sleep"			{return PERL_FUNCTION;}
    "-p"			{return PERL_FUNCTION;}
    "flock"			{return PERL_FUNCTION;}
    "next"			{return PERL_FUNCTION;}
    "socket"			{return PERL_FUNCTION;}
    "-r"			{return PERL_FUNCTION;}
    "fork"			{return PERL_FUNCTION;}
    "not"			{return PERL_FUNCTION;}
    "socketpair"			{return PERL_FUNCTION;}
    "-R"			{return PERL_FUNCTION;}
    "format"			{return PERL_FUNCTION;}
    "oct"			{return PERL_FUNCTION;}
    "sort"			{return PERL_FUNCTION;}
    "-S"			{return PERL_FUNCTION;}
    "formline"			{return PERL_FUNCTION;}
    "open"			{return PERL_FUNCTION;}
    "splice"			{return PERL_FUNCTION;}
    "-s"			{return PERL_FUNCTION;}
    "getc"			{return PERL_FUNCTION;}
    "opendir"			{return PERL_FUNCTION;}
    "split"			{return PERL_FUNCTION;}
    "-T"			{return PERL_FUNCTION;}
    "getgrent"			{return PERL_FUNCTION;}
    "ord"			{return PERL_FUNCTION;}
    "sprintf"			{return PERL_FUNCTION;}
    "-t"			{return PERL_FUNCTION;}
    "getgrgid"			{return PERL_FUNCTION;}
    "our"			{return PERL_FUNCTION;}
    "sqrt"			{return PERL_FUNCTION;}
    "-u"			{return PERL_FUNCTION;}
    "getgrnam"			{return PERL_FUNCTION;}

    "package"       {yybegin(PACKAGE_DEFINITION);return PERL_SYNTAX;}

    "pack"			{return PERL_FUNCTION;}
    "srand"			{return PERL_FUNCTION;}
    "-w"			{return PERL_FUNCTION;}
    "gethostbyaddr"			{return PERL_FUNCTION;}
    "pipe"			{return PERL_FUNCTION;}
    "stat"			{return PERL_FUNCTION;}
    "-W"			{return PERL_FUNCTION;}
    "gethostbyname"			{return PERL_FUNCTION;}
    "pop"			{return PERL_FUNCTION;}
    "state"			{return PERL_FUNCTION;}
    "-X"			{return PERL_FUNCTION;}
    "gethostent"			{return PERL_FUNCTION;}
    "pos"			{return PERL_FUNCTION;}
    "study"			{return PERL_FUNCTION;}
    "-x"			{return PERL_FUNCTION;}
    "getlogin"			{return PERL_FUNCTION;}
    "print"			{return PERL_FUNCTION;}
    "substr"			{return PERL_FUNCTION;}
    "-z"			{return PERL_FUNCTION;}
    "getnetbyaddr"			{return PERL_FUNCTION;}
    "printf"			{return PERL_FUNCTION;}
    "symlink"			{return PERL_FUNCTION;}
    "abs"			{return PERL_FUNCTION;}
    "getnetbyname"			{return PERL_FUNCTION;}
    "prototype"			{return PERL_FUNCTION;}
    "syscall"			{return PERL_FUNCTION;}
    "accept"			{return PERL_FUNCTION;}
    "getnetent"			{return PERL_FUNCTION;}
    "push"			{return PERL_FUNCTION;}
    "sysopen"			{return PERL_FUNCTION;}
    "alarm"			{return PERL_FUNCTION;}
    "getpeername"			{return PERL_FUNCTION;}
    "quotemeta"			{return PERL_FUNCTION;}
    "sysread"			{return PERL_FUNCTION;}
    "atan2"			{return PERL_FUNCTION;}
    "getpgrp"			{return PERL_FUNCTION;}
    "rand"			{return PERL_FUNCTION;}
    "sysseek"			{return PERL_FUNCTION;}
    "AUTOLOAD"			{return PERL_FUNCTION;}
    "getppid"			{return PERL_FUNCTION;}
    "read"			{return PERL_FUNCTION;}
    "system"			{return PERL_FUNCTION;}
    "BEGIN"			{return PERL_FUNCTION;}
    "getpriority"			{return PERL_FUNCTION;}
    "readdir"			{return PERL_FUNCTION;}
    "syswrite"			{return PERL_FUNCTION;}
    "bind"			{return PERL_FUNCTION;}
    "getprotobyname"			{return PERL_FUNCTION;}
    "readline"			{return PERL_FUNCTION;}
    "tell"			{return PERL_FUNCTION;}
    "binmode"			{return PERL_FUNCTION;}
    "getprotobynumber"			{return PERL_FUNCTION;}
    "readlink"			{return PERL_FUNCTION;}
    "telldir"			{return PERL_FUNCTION;}
    "bless"			{return PERL_FUNCTION;}
    "getprotoent"			{return PERL_FUNCTION;}
    "readpipe"			{return PERL_FUNCTION;}
    "tie"			{return PERL_FUNCTION;}
    "break"			{return PERL_FUNCTION;}
    "getpwent"			{return PERL_FUNCTION;}
    "recv"			{return PERL_FUNCTION;}
    "tied"			{return PERL_FUNCTION;}
    "caller"			{return PERL_FUNCTION;}
    "getpwnam"			{return PERL_FUNCTION;}
    "redo"			{return PERL_FUNCTION;}
    "time"			{return PERL_FUNCTION;}
    "chdir"			{return PERL_FUNCTION;}
    "getpwuid"			{return PERL_FUNCTION;}
    "ref"			{return PERL_FUNCTION;}
    "times"			{return PERL_FUNCTION;}
    "CHECK"			{return PERL_FUNCTION;}
    "getservbyname"			{return PERL_FUNCTION;}
    "rename"			{return PERL_FUNCTION;}
    "truncate"			{return PERL_FUNCTION;}
    "chmod"			{return PERL_FUNCTION;}
    "getservbyport"			{return PERL_FUNCTION;}
    "require"			{return PERL_FUNCTION;}
    "uc"			{return PERL_FUNCTION;}
    "chomp"			{return PERL_FUNCTION;}
    "getservent"			{return PERL_FUNCTION;}
    "reset"			{return PERL_FUNCTION;}
    "ucfirst"			{return PERL_FUNCTION;}
    "chop"			{return PERL_FUNCTION;}
    "getsockname"			{return PERL_FUNCTION;}
    "return"			{return PERL_FUNCTION;}
    "umask"			{return PERL_FUNCTION;}
    "chown"			{return PERL_FUNCTION;}
    "getsockopt"			{return PERL_FUNCTION;}
    "reverse"			{return PERL_FUNCTION;}
    "undef"			{return PERL_FUNCTION;}
    "chr"			{return PERL_FUNCTION;}
    "glob"			{return PERL_FUNCTION;}
    "rewinddir"			{return PERL_FUNCTION;}
    "UNITCHECK"			{return PERL_FUNCTION;}
    "chroot"			{return PERL_FUNCTION;}
    "gmtime"			{return PERL_FUNCTION;}
    "rindex"			{return PERL_FUNCTION;}
    "unlink"			{return PERL_FUNCTION;}
    "close"			{return PERL_FUNCTION;}
    "goto"			{return PERL_FUNCTION;}
    "rmdir"			{return PERL_FUNCTION;}
    "unpack"			{return PERL_FUNCTION;}
    "closedir"			{return PERL_FUNCTION;}
    "grep"			{return PERL_FUNCTION;}
    "say"			{return PERL_FUNCTION;}
    "unshift"			{return PERL_FUNCTION;}
    "connect"			{return PERL_FUNCTION;}
    "hex"			{return PERL_FUNCTION;}
    "scalar"			{return PERL_FUNCTION;}
    "untie"			{return PERL_FUNCTION;}
    "cos"			{return PERL_FUNCTION;}
    "index"			{return PERL_FUNCTION;}
    "seek"			{return PERL_FUNCTION;}
    "use"			{yybegin(PACKAGE_USE);return PERL_FUNCTION;}
    "crypt"			{return PERL_FUNCTION;}
    "INIT"			{return PERL_FUNCTION;}
    "seekdir"			{return PERL_FUNCTION;}
    "utime"			{return PERL_FUNCTION;}
    "dbmclose"			{return PERL_FUNCTION;}
    "int"			{return PERL_FUNCTION;}
    "select"			{return PERL_FUNCTION;}
    "values"			{return PERL_FUNCTION;}
    "dbmopen"			{return PERL_FUNCTION;}
    "ioctl"			{return PERL_FUNCTION;}
    "semctl"			{return PERL_FUNCTION;}
    "vec"			{return PERL_FUNCTION;}
    "defined"			{return PERL_FUNCTION;}
    "join"			{return PERL_FUNCTION;}
    "semget"			{return PERL_FUNCTION;}
    "wait"			{return PERL_FUNCTION;}
    "delete"			{return PERL_FUNCTION;}
    "keys"			{return PERL_FUNCTION;}
    "semop"			{return PERL_FUNCTION;}
    "waitpid"			{return PERL_FUNCTION;}
    "DESTROY"			{return PERL_FUNCTION;}
    "kill"			{return PERL_FUNCTION;}
    "send"			{return PERL_FUNCTION;}
    "wantarray"			{return PERL_FUNCTION;}
    "die"			{return PERL_FUNCTION;}
    "last"			{return PERL_FUNCTION;}
    "setgrent"			{return PERL_FUNCTION;}
    "warn"			{return PERL_FUNCTION;}
    "dump"			{return PERL_FUNCTION;}
    "lc"			{return PERL_FUNCTION;}
    "sethostent"			{return PERL_FUNCTION;}
    "write"			{return PERL_FUNCTION;}
    "each"			{return PERL_FUNCTION;}
    "lcfirst"			{return PERL_FUNCTION;}
    "setnetent"			{return PERL_FUNCTION;}

//////////////////////////// PERL SYNTAX ///////////////////////////////////////////////////////////////////////////////
    "__DATA__"			{return PERL_SYNTAX;}
    "else"			{return PERL_SYNTAX;}
    "lock"			{return PERL_SYNTAX;}
    "qw"			{return PERL_SYNTAX;}
    "__END__"			{return PERL_SYNTAX;}
    "elsif"			{return PERL_SYNTAX;}
    "lt"			{return PERL_SYNTAX;}
    "qx"			{return PERL_SYNTAX;}
    "__FILE__"			{return PERL_SYNTAX;}
    "eq"			{return PERL_SYNTAX;}
    "m"			{return PERL_SYNTAX;}
    "s"			{return PERL_SYNTAX;}
    "__LINE__"			{return PERL_SYNTAX;}
    "exp"			{return PERL_SYNTAX;}
    "ne"			{return PERL_SYNTAX;}
    "sub"			{yybegin(FUNCTION_DEFINITION); return PERL_SYNTAX;}
    "__PACKAGE__"			{return PERL_SYNTAX;}
    "for"			{return PERL_SYNTAX;}
    "no"			{return PERL_SYNTAX;}
    "tr"			{return PERL_SYNTAX;}
    "and"			{return PERL_SYNTAX;}
    "foreach"			{return PERL_SYNTAX;}
    "or"			{return PERL_SYNTAX;}
    "unless"			{return PERL_SYNTAX;}
    "cmp"			{return PERL_SYNTAX;}
    "ge"			{return PERL_SYNTAX;}
    "until"			{return PERL_SYNTAX;}
    "continue"			{return PERL_SYNTAX;}
    "gt"			{return PERL_SYNTAX;}
    "q"			{return PERL_SYNTAX;}
    "while"			{return PERL_SYNTAX;}
    "CORE"			{return PERL_SYNTAX;}
    "if"			{return PERL_SYNTAX;}
    "qq"			{return PERL_SYNTAX;}
    "xor"			{return PERL_SYNTAX;}
    "do"			{return PERL_SYNTAX;}
    "le"			{return PERL_SYNTAX;}
    "qr"			{return PERL_SYNTAX;}
    "y"			{return PERL_SYNTAX;}


/////////////////////////////////////////// PERL FILE HANDLES //////////////////////////////////////////////////////////
    "ARGV"			{return PERL_HANDLE;}
    "STDERR"			{return PERL_HANDLE;}
    "STDOUT"			{return PERL_HANDLE;}
    "ARGVOUT"			{return PERL_HANDLE;}
    "STDIN"			{return PERL_HANDLE;}

/////////////////////////////////////////// PERL MISC //////////////////////////////////////////////////////////////////
    "%+"			{return PERL_MISC;}
    "EXTENDED_OS_ERROR"			{return PERL_MISC;}
    "PERL_DESTRUCT_LEVEL"			{return PERL_MISC;}
    "%-"			{return PERL_MISC;}
    "fail"			{return PERL_MISC;}
    "PERL_DL_NONLAZY"			{return PERL_MISC;}
    "1"			{return PERL_MISC;}
    "FETCH"			{return PERL_MISC;}
    "PERL_ENCODING"			{return PERL_MISC;}
    "2"			{return PERL_MISC;}
    "FETCHSIZE"			{return PERL_MISC;}
    "PERL_HASH_SEED"			{return PERL_MISC;}
    "3"			{return PERL_MISC;}
    "file_name_is_absolute"			{return PERL_MISC;}
    "PERL_HASH_SEED_DEBUG"			{return PERL_MISC;}
    "4"			{return PERL_MISC;}
    "fileparse"			{return PERL_MISC;}
    "PERL_ROOT"			{return PERL_MISC;}
    "5"			{return PERL_MISC;}
    "fileparse_set_fstype"			{return PERL_MISC;}
    "PERL_SIGNALS"			{return PERL_MISC;}
    "6"			{return PERL_MISC;}
    "find"			{return PERL_MISC;}
    "PERL_UNICODE"			{return PERL_MISC;}
    "7"			{return PERL_MISC;}
    "finddepth"			{return PERL_MISC;}
    "PERL_VERSION"			{return PERL_MISC;}
    "8"			{return PERL_MISC;}
    "FIRSTKEY"			{return PERL_MISC;}
    "PERLDB"			{return PERL_MISC;}
    "9"			{return PERL_MISC;}
    "FORMAT_FORMFEED"			{return PERL_MISC;}
    "PERLIO"			{return PERL_MISC;}
    ":bytes"			{return PERL_MISC;}
    "FORMAT_LINE_BREAK_CHARACTERS"			{return PERL_MISC;}
    "PERLIO_DEBUG"			{return PERL_MISC;}
    ":crlf"			{return PERL_MISC;}
    "FORMAT_LINES_LEFT"			{return PERL_MISC;}
    "PERLLIB"			{return PERL_MISC;}
    ":mmap"			{return PERL_MISC;}
    "FORMAT_LINES_PER_PAGE"			{return PERL_MISC;}
    "PID"			{return PERL_MISC;}
    ":perlio"			{return PERL_MISC;}
    "FORMAT_NAME"			{return PERL_MISC;}
    "plan"			{return PERL_MISC;}
    ":pop"			{return PERL_MISC;}
    "FORMAT_PAGE_NUMBER"			{return PERL_MISC;}
    "pod"			{return PERL_MISC;}
    ":raw"			{return PERL_MISC;}
    "FORMAT_TOP_NAME"			{return PERL_MISC;}
    "POP"			{return PERL_MISC;}
    ":stdio"			{return PERL_MISC;}
    "freeze"			{return PERL_MISC;}
    "POSTMATCH"			{return PERL_MISC;}
    ":unix"			{return PERL_MISC;}
    "GETC"			{return PERL_MISC;}
    "PREMATCH"			{return PERL_MISC;}
    ":utf8"			{return PERL_MISC;}
    "GetOptions"			{return PERL_MISC;}
    "PRINT"			{return PERL_MISC;}
    ":win32"			{return PERL_MISC;}
    "GID"			{return PERL_MISC;}
    "PRINTF"			{return PERL_MISC;}
    "@F"			{return PERL_MISC;}
    "head1"			{return PERL_MISC;}
    "PROCESS_ID"			{return PERL_MISC;}
    "_"			{return PERL_MISC;}
    "head2"			{return PERL_MISC;}
    "PROGRAM_NAME"			{return PERL_MISC;}
    "__DIE__"			{return PERL_MISC;}
    "head3"			{return PERL_MISC;}
    "PUSH"			{return PERL_MISC;}
    "__WARN__"			{return PERL_MISC;}
    "head4"			{return PERL_MISC;}
    "RE_DEBUG_FLAGS"			{return PERL_MISC;}
    "a"			{return PERL_MISC;}
    "HOME"			{return PERL_MISC;}
    "RE_TRIE_MAXBUF"			{return PERL_MISC;}
    "abs2rel"			{return PERL_MISC;}
    "INC"			{return PERL_MISC;}
    "READ"			{return PERL_MISC;}
    "ACCUMULATOR"			{return PERL_MISC;}
    "INPLACE_EDIT"			{return PERL_MISC;}
    "READLINE"			{return PERL_MISC;}
    "ARG"			{return PERL_MISC;}
    "INPUT_LINE_NUMBER"			{return PERL_MISC;}
    "REAL_GROUP_ID"			{return PERL_MISC;}
    "b"			{return PERL_MISC;}
    "INPUT_RECORD_SEPARATOR"			{return PERL_MISC;}
    "REAL_USER_ID"			{return PERL_MISC;}
    "back"			{return PERL_MISC;}
    "is"			{return PERL_MISC;}
    "rel2abs"			{return PERL_MISC;}
    "basename"			{return PERL_MISC;}
    "is_deeply"			{return PERL_MISC;}
    "require_ok"			{return PERL_MISC;}
    "BASETIME"			{return PERL_MISC;}
    "ISA"			{return PERL_MISC;}
    "rmscopy"			{return PERL_MISC;}
    "begin"			{return PERL_MISC;}
    "isa"			{return PERL_MISC;}
    "rmtree"			{return PERL_MISC;}
    "builder"			{return PERL_MISC;}
    "isa_ok"			{return PERL_MISC;}
    "rootdir"			{return PERL_MISC;}
    "can"			{return PERL_MISC;}
    "isnt"			{return PERL_MISC;}
    "RS"			{return PERL_MISC;}
    "can_ok"			{return PERL_MISC;}
    "item"			{return PERL_MISC;}
    "safe_level"			{return PERL_MISC;}
    "canonpath"			{return PERL_MISC;}
    "LAST_MATCH_END"			{return PERL_MISC;}
    "SHIFT"			{return PERL_MISC;}
    "carp"			{return PERL_MISC;}
    "LAST_MATCH_START"			{return PERL_MISC;}
    "shortmess"			{return PERL_MISC;}
    "case_tolerant"			{return PERL_MISC;}
    "LAST_PAREN_MATCH"			{return PERL_MISC;}
    "SIG"			{return PERL_MISC;}
    "catdir"			{return PERL_MISC;}
    "LAST_REGEXP_CODE_RESULT"			{return PERL_MISC;}
    "skip"			{return PERL_MISC;}
    "catfile"			{return PERL_MISC;}
    "LAST_SUBMATCH_RESULT"			{return PERL_MISC;}
    "SKIP:"			{return PERL_MISC;}
    "catpath"			{return PERL_MISC;}
    "like"			{return PERL_MISC;}
    "SPLICE"			{return PERL_MISC;}
    "CHILD_ERROR"			{return PERL_MISC;}
    "LIST_SEPARATOR"			{return PERL_MISC;}
    "splitdir"			{return PERL_MISC;}
    "CHILD_ERROR_NATIVE"			{return PERL_MISC;}
    "LOGDIR"			{return PERL_MISC;}
    "splitpath"			{return PERL_MISC;}
    "CLEAR"			{return PERL_MISC;}
    "longmess"			{return PERL_MISC;}
    "STORE"			{return PERL_MISC;}
    "CLOSE"			{return PERL_MISC;}
    "MATCH"			{return PERL_MISC;}
    "STORESIZE"			{return PERL_MISC;}
    "cluck"			{return PERL_MISC;}
    "mkdtemp"			{return PERL_MISC;}
    "SUBSCRIPT_SEPARATOR"			{return PERL_MISC;}
    "cmp_ok"			{return PERL_MISC;}
    "mkpath"			{return PERL_MISC;}
    "SUBSEP"			{return PERL_MISC;}
    "COMPILING"			{return PERL_MISC;}
    "mkstemp"			{return PERL_MISC;}
    "SYSTEM_FD_MAX"			{return PERL_MISC;}
    "confess"			{return PERL_MISC;}
    "mkstemps"			{return PERL_MISC;}
    "TAINT"			{return PERL_MISC;}
    "copy"			{return PERL_MISC;}
    "mktemp"			{return PERL_MISC;}
    "tempdir"			{return PERL_MISC;}
    "cp"			{return PERL_MISC;}
    "move"			{return PERL_MISC;}
    "tempfile"			{return PERL_MISC;}
    "croak"			{return PERL_MISC;}
    "MULTILINE_MATCHING"			{return PERL_MISC;}
    "thaw"			{return PERL_MISC;}
    "curdir"			{return PERL_MISC;}
    "mv"			{return PERL_MISC;}
    "TIEARRAY"			{return PERL_MISC;}
    "cut"			{return PERL_MISC;}
    "NEXTKEY"			{return PERL_MISC;}
    "TIEHANDLE"			{return PERL_MISC;}
    "DATA"			{return PERL_MISC;}
    "no_upwards"			{return PERL_MISC;}
    "TIEHASH"			{return PERL_MISC;}
    "DEBUGGING"			{return PERL_MISC;}
    "NR"			{return PERL_MISC;}
    "TIESCALAR"			{return PERL_MISC;}
    "DELETE"			{return PERL_MISC;}
    "OFS"			{return PERL_MISC;}
    "tmpdir"			{return PERL_MISC;}
    "devnull"			{return PERL_MISC;}
    "ok"			{return PERL_MISC;}
    "tmpfile"			{return PERL_MISC;}
    "diag"			{return PERL_MISC;}
    "OPEN"			{return PERL_MISC;}
    "tmpnam"			{return PERL_MISC;}
    "dirname"			{return PERL_MISC;}
    "ORS"			{return PERL_MISC;}
    "TODO:"			{return PERL_MISC;}
    "EFFECTIVE_GROUP_ID"			{return PERL_MISC;}
    "OS_ERROR"			{return PERL_MISC;}
    "todo_skip"			{return PERL_MISC;}
    "EFFECTIVE_USER_ID"			{return PERL_MISC;}
    "OSNAME"			{return PERL_MISC;}
    "UID"			{return PERL_MISC;}
    "EGID"			{return PERL_MISC;}
    "OUTPUT_AUTOFLUSH"			{return PERL_MISC;}
    "UNICODE"			{return PERL_MISC;}
    "ENCODING"			{return PERL_MISC;}
    "OUTPUT_FIELD_SEPARATOR"			{return PERL_MISC;}
    "unlike"			{return PERL_MISC;}
    "end"			{return PERL_MISC;}
    "OUTPUT_RECORD_SEPARATOR"			{return PERL_MISC;}
    "unlink0"			{return PERL_MISC;}
    "ENV"			{return PERL_MISC;}
    "over"			{return PERL_MISC;}
    "UNSHIFT"			{return PERL_MISC;}
    "eq_array"			{return PERL_MISC;}
    "pass"			{return PERL_MISC;}
    "UNTIE"			{return PERL_MISC;}
    "eq_hash"			{return PERL_MISC;}
    "PATH"			{return PERL_MISC;}
    "updir"			{return PERL_MISC;}
    "eq_set"			{return PERL_MISC;}
    "path"			{return PERL_MISC;}
    "use_ok"			{return PERL_MISC;}
    "ERRNO"			{return PERL_MISC;}
    "PERL5DB"			{return PERL_MISC;}
    "UTF8CACHE"			{return PERL_MISC;}
    "EUID"			{return PERL_MISC;}
    "PERL5DB_THREADED"			{return PERL_MISC;}
    "UTF8LOCALE"			{return PERL_MISC;}
    "EVAL_ERROR"			{return PERL_MISC;}
    "PERL5LIB"			{return PERL_MISC;}
    "VERSION"			{return PERL_MISC;}
    "EXCEPTIONS_BEING_CAUGHT"			{return PERL_MISC;}
    "PERL5OPT"			{return PERL_MISC;}
    "WARNING"			{return PERL_MISC;}
    "EXECUTABLE_NAME"			{return PERL_MISC;}
    "PERL5SHELL"			{return PERL_MISC;}
    "WARNING_BITS"			{return PERL_MISC;}
    "EXISTS"			{return PERL_MISC;}
    "PERL_ALLOW_NON_IFS_LSP"			{return PERL_MISC;}
    "WIN32_SLOPPY_STAT"			{return PERL_MISC;}
    "EXTEND"			{return PERL_MISC;}
    "PERL_DEBUG_MSTATS"			{return PERL_MISC;}
    "WRITE"			{return PERL_MISC;}

///////////////////////////////// PERL OPERATORS ///////////////////////////////////////////////////////////////////////
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
}

/* error fallback */
[^]    { return PERL_BAD_CHARACTER; }
