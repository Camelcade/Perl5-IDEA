package com.perl5.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

/**
 * Created by hurricup on 19.04.2015.
 */
public abstract class PerlLexerProto implements FlexLexer, PerlTokenTypes
{

/*
  StringBuffer stringBuffer = new StringBuffer();
*/
 /* private IElementType element(int type, Object value) {
    return new IElementType(type, value);
  }*/

	protected IElementType checkBuiltInScalar()
	{
    /*
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
"$LAST_MATCH_END"			{return PERL_VARIA
BLE;}
"$^RE_DEBUG_FLAGS"			{return PERL_VAR
IABLE;}
"$LAST_PAREN_MATCH"			{return PERL_VARIABLE;}
     */
		// here we need to check built-in scalars
		return PERL_VARIABLE_SCALAR;
	}
	protected IElementType checkBuiltInArray()
	{
		// here we need to check built-in scalars
		return PERL_VARIABLE_ARRAY;
	}
	protected IElementType checkBuiltInHash()
	{
		// here we need to check built-in scalars
		return PERL_VARIABLE_HASH;
	}
	protected IElementType checkBuiltInGlob()
	{
		// here we need to check built-in scalars
		return PERL_VARIABLE_GLOB;
	}
	protected IElementType checkCorePackage()
	{
		return PERL_PACKAGE;
	}
}
