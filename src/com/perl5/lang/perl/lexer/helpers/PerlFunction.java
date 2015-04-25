package com.perl5.lang.perl.lexer.helpers;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.PerlTokenType;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlFunction implements PerlElementTypes
{
	protected static final HashMap<String,IElementType> knownFunctions = new HashMap<String,IElementType>();

	private static final ArrayList<String> BUILT_IN = new ArrayList<String>( Arrays.asList(
			// http://perldoc.perl.org/perlfunc.html
//			Functions for SCALARs or strings
			"chomp",
			"chop",
			"chr",
			"crypt",
			"fc",
			"hex",
			"index",
			"lc",
			"lcfirst",
			"length",
			"oct",
			"ord",
			"pack",
//			"q//",
//			"qq//",
			"reverse",
			"rindex",
			"sprintf",
			"substr",
//			"tr///",
			"uc",
			"ucfirst",
//			"y///",
			"fc",

//			Regular expressions and pattern matching
//			"m//",
			"pos",
//			"qr//",
			"quotemeta",
//			"s///",
			"split",
			"study",

//			Numeric functions
			"abs",
			"atan2",
			"cos",
			"exp",
			"hex",
			"int",
			"log",
			"oct",
			"rand",
			"sin",
			"sqrt",
			"srand",

//			Functions for real @ARRAYs
			"each",
			"keys",
			"pop",
			"push",
			"shift",
			"splice",
			"unshift",
			"values",

//			Functions for list data
			"grep",
			"join",
			"map",
//			"qw//",
			"reverse",
			"sort",
			"unpack",

//			Functions for real %HASHes
			"delete",
			"each",
			"exists",
			"keys",
			"values",

//			Input and output functions
			"binmode",
			"close",
			"closedir",
			"dbmclose",
			"dbmopen",
			"die",
			"eof",
			"fileno",
			"flock",
			"format",
			"getc",
			"print",
			"printf",
			"read",
			"readdir",
			"readline",
			"rewinddir",
			"say",
			"seek",
			"seekdir",
			"select",
			"syscall",
			"sysread",
			"sysseek",
			"syswrite",
			"tell",
			"telldir",
			"truncate",
			"warn",
			"write",

//			Functions for fixed-length data or records
			"pack",
			"read",
			"syscall",
			"sysread",
			"sysseek",
			"syswrite",
			"unpack",
			"vec",

//			Functions for filehandles, files or directories
			"-r",
			"-w",
			"-x",
			"-o",
			"-R",
			"-W",
			"-X",
			"-O",
			"-e",
			"-z",
			"-s",
			"-f",
			"-d",
			"-l",
			"-p",
			"-S",
			"-b",
			"-c",
			"-t",
			"-u",
			"-g",
			"-k",
			"-T",
			"-B",
			"-M",
			"-A",
			"-C",
			"chdir",
			"chmod",
			"chown",
			"chroot",
			"fcntl",
			"glob",
			"ioctl",
			"link",
			"lstat",
			"mkdir",
			"open",
			"opendir",
			"readlink",
			"rename",
			"rmdir",
			"stat",
			"symlink",
			"sysopen",
			"umask",
			"unlink",
			"utime",

//			Keywords related to the control flow of your Perl program
			"break",
			"caller",
			"continue",
			"die",
			"do",
			"dump",
			"eval",
			"evalbytes",
			"exit",
			"__FILE__",
			"goto",
			"last",
			"__LINE__",
			"next",
			"__PACKAGE__",
			"redo",
			"return",
			"sub",
			"__SUB__",
			"wantarray",

//			Keywords related to scoping
			"caller",
			"import",
			"local",
			"my",
			"our",
			"package",
			"state",
			"use",

//			Miscellaneous functions
			"defined",
			"formline",
			"lock",
			"prototype",
			"reset",
			"scalar",
			"undef",

//			Functions for processes and process groups
			"alarm",
			"exec",
			"fork",
			"getpgrp",
			"getppid",
			"getpriority",
			"kill",
			"pipe",
//			"qx//",
			"readpipe",
			"setpgrp",
			"setpriority",
			"sleep",
			"system",
			"times",
			"wait",
			"waitpid",

//			Keywords related to Perl modules
			"do",
			"import",
			"no",
			"package",
			"require",
			"use",

//			Keywords related to classes and object-orientation
			"bless",
			"dbmclose",
			"dbmopen",
			"package",
			"ref",
			"tie",
			"tied",
			"untie",
			"use",

//			Low-level socket functions
			"accept",
			"bind",
			"connect",
			"getpeername",
			"getsockname",
			"getsockopt",
			"listen",
			"recv",
			"send",
			"setsockopt",
			"shutdown",
			"socket",
			"socketpair",

//			System V interprocess communication functions
			"msgctl",
			"msgget",
			"msgrcv",
			"msgsnd",
			"semctl",
			"semget",
			"semop",
			"shmctl",
			"shmget",
			"shmread",
			"shmwrite",

//			Fetching user and group info
			"endgrent",
			"endhostent",
			"endnetent",
			"endpwent",
			"getgrent",
			"getgrgid",
			"getgrnam",
			"getlogin",
			"getpwent",
			"getpwnam",
			"getpwuid",
			"setgrent",
			"setpwent",

//			Fetching network info
			"endprotoent",
			"endservent",
			"gethostbyaddr",
			"gethostbyname",
			"gethostent",
			"getnetbyaddr",
			"getnetbyname",
			"getnetent",
			"getprotobyname",
			"getprotobynumber",
			"getprotoent",
			"getservbyname",
			"getservbyport",
			"getservent",
			"sethostent",
			"setnetent",
			"setprotoent",
			"setservent",

//			Time-related functions
			"gmtime",
			"localtime",
			"time",
			"times",

//			"Non-function keywords",
			"AUTOLOAD",
			"BEGIN",
			"CHECK",
			"CORE",
			"__DATA__",
			"default",
			"DESTROY",
			"else",
			"elseif",
			"elsif",
			"END",
			"__END__",
			"for",
			"foreach",
			"given",
			"if",
			"INIT",
			"UNITCHECK",
			"unless",
			"until",
			"when",
			"while",
			"x",

			// Operators are being checked explicitly
			"and",
			"cmp",
			"eq",
			"ge",
			"gt",
			"le",
			"lt",
			"ne",
			"not",
			"or",
			"xor"
	));

	static{
		for( String functionName: BUILT_IN )
		{
			knownFunctions.put(functionName, PERL_FUNCTION_BUILT_IN);
		}
	}

	public static IElementType getFunctionType(String function)
	{
		IElementType functionType;

		functionType = knownFunctions.get(function);

		return functionType == null
				? PERL_FUNCTION_USER
				: functionType;
	}


}
