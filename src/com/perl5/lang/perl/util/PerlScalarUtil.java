package com.perl5.lang.perl.util;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;

import java.util.*;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlScalarUtil implements PerlElementTypes
{
	protected static final HashMap<String,IElementType> BUILT_IN_MAP = new HashMap<String,IElementType>();

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
			"$EXTENDED_OS_ERROR",
			"$^E",
			"$FORMAT_FORMFEED",
			"$^ENCODING",
			"$FORMAT_LINE_BREAK_CHARACTERS",
			"$^F",
			"$FORMAT_LINES_LEFT",
			"$^H",
			"$FORMAT_LINES_PER_PAGE",
			"$^I",
			"$FORMAT_NAME",
			"$^L",
			"$FORMAT_PAGE_NUMBER",
			"$^M",
			"$FORMAT_TOP_NAME",
			"$^N",
			"$GID",
			"$^O",
			"$INPLACE_EDIT",
			"$^OPEN",
			"$INPUT_LINE_NUMBER",
			"$^P",
			"$INPUT_RECORD_SEPARATOR",
			"$^R",
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

	static{
		for( String builtIn: BUILT_IN )
		{
			BUILT_IN_MAP.put(builtIn, PERL_SCALAR);
		}
	}

	public static IElementType getScalarType(String scalar)
	{
		IElementType scalarType = BUILT_IN_MAP.get(scalar);

		return scalarType == null
				? PERL_SCALAR
				: scalarType;
	}

/*
	public static List<PerlScalar> findScalars(Project project, String key) {
		List<PerlScalar> result = null;

		Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, PerlFileTypePackage.INSTANCE,
				GlobalSearchScope.allScope(project));

		virtualFiles.addAll(FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, PerlFileTypeScript.INSTANCE,
				GlobalSearchScope.allScope(project)));

		for (VirtualFile virtualFile : virtualFiles) {
			PerlFile perlFile = (PerlFile) PsiManager.getInstance(project).findFile(virtualFile);
			if (perlFile != null) {
				PerlPackageNamespace[] packageDefinitions = PsiTreeUtil.getChildrenOfType(perlFile, PerlPackageNamespace.class );
				if (packageDefinitions != null) {
					for (PerlPackageNamespace packageDefinition: packageDefinitions) {
						if (key.equals(packageDefinition.getPackageBare().getText())) {
							if (result == null) {
								result = new ArrayList<PerlScalar>();
							}
							result.add(scalar);
						}
					}
				}
			}
		}
		return result != null ? result : Collections.<PerlScalar>emptyList();
	}
*/

}
