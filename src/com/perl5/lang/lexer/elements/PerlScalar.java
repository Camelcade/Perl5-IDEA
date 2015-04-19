package com.perl5.lang.lexer.elements;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.perl5.highlighter.PerlSyntaxHighlighter;
import com.perl5.highlighter.SelfStyled;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlScalar extends PerlVariable implements SelfStyled
{
	private static final TextAttributesKey[] attributesKeys = new TextAttributesKey[]{PerlSyntaxHighlighter.PERL_SCALAR};
	@Override
	public TextAttributesKey[] getTextAttributesKey()
	{
		return attributesKeys;
	}

	public PerlScalar(PerlVariableScope scope, boolean isBuiltIn) {
		super("PERL_SCALAR", scope, isBuiltIn);
	}

	public static final ArrayList<String> BUILT_IN = new ArrayList<String>( Arrays.asList(
			"$!",
			"$^RE_TRIE_MAXBUF",
			"$LAST_REGEXP_CODE_RESULT",
			"$\"",
			"$^S",
			"$LIST_SEPARATOR",
			"$#",
			"$^T",
			"$MATCH",
			"$$",
			"$^TAINT",
			"$MULTILINE_MATCHING",
			"$%",
			"$^UNICODE",
			"$NR",
			"$&",
			"$^UTF8LOCALE",
			"$OFMT",
			"$'",
			"$^V",
			"$OFS",
			"$(",
			"$^W",
			"$ORS",
			"$)",
			"$^WARNING_BITS",
			"$OS_ERROR",
			"$*",
			"$^WIDE_SYSTEM_CALLS",
			"$OSNAME",
			"$+",
			"$^X",
			"$OUTPUT_AUTO_FLUSH",
			"$,",
			"$_",
			"$OUTPUT_FIELD_SEPARATOR",
			"$-",
			"$`",
			"$OUTPUT_RECORD_SEPARATOR",
			"$.",
			"$a",
			"$PERL_VERSION",
			"$/",
			"$ACCUMULATOR",
			"$PERLDB",
			"$0",
			"$ARG",
			"$PID",
			"$:",
			"$ARGV",
			"$POSTMATCH",
			"$;",
			"$b",
			"$PREMATCH",
			"$<",
			"$BASETIME",
			"$PROCESS_ID",
			"$=",
			"$CHILD_ERROR",
			"$PROGRAM_NAME",
			"$>",
			"$COMPILING",
			"$REAL_GROUP_ID",
			"$?",
			"$DEBUGGING",
			"$REAL_USER_ID",
			"$@",
			"$EFFECTIVE_GROUP_ID",
			"$RS",
			"$[",
			"$EFFECTIVE_USER_ID",
			"$SUBSCRIPT_SEPARATOR",
			"$\\",
			"$EGID",
			"$SUBSEP",
			"$]",
			"$ERRNO",
			"$SYSTEM_FD_MAX",
			"$^",
			"$EUID",
			"$UID",
			"$^A",
			"$EVAL_ERROR",
			"$WARNING",
			"$^C",
			"$EXCEPTIONS_BEING_CAUGHT",
			"$|",
			"$^CHILD_ERROR_NATIVE",
			"$EXECUTABLE_NAME",
			"$~",
			"$^D",
			"$EXTENDED_OS_ERROR$^E",
			"$FORMAT_FORMFEED$^ENCODING",
			"$FORMAT_LINE_BREAK_CHARACTERS$^F",
			"$FORMAT_LINES_LEFT$^H",
			"$FORMAT_LINES_PER_PAGE$^I",
			"$FORMAT_NAME$^L",
			"$FORMAT_PAGE_NUMBER$^M",
			"$FORMAT_TOP_NAME$^N",
			"$GID$^O",
			"$INPLACE_EDIT$^OPEN",
			"$INPUT_LINE_NUMBER$^P",
			"$INPUT_RECORD_SEPARATOR$^R",
			"$LAST_MATCH_END",
			"$^RE_DEBUG_FLAGS",
			"$LAST_PAREN_MATCH",

			// array elements
			"$+",
			"$-",
			"$_",
			"$ARGV",
			"$INC",
			"$LAST_MATCH_START",

			// hashes elements
			"$!",
			"$+",
			"$-",
			"$^H",
			"$ENV",
			"$INC",
			"$OVERLOAD",
			"$SIG"
	));
}
