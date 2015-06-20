/*
 * Copyright 2015 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.perl5.lang.perl.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hurricup on 11.05.2015.
 */
public interface PerlSubUtilBuiltIn
{
	public static final HashSet<String> BUILT_IN_UNARY = new HashSet<>(Arrays.asList(

	// original list taken from http://docstore.mik.ua/orelly/perl2/prog/ch03_10.htm
		"alarm",
		"getnetbyname",
		"lock",
		"rmdir",
		"caller",
		"getpgrp",
		"log",
		"scalar",
		"chdir",
		"getprotobyname",
		"lstat",
		"sin",
		"chroot",
		"glob",
//		"my",	// this being handled separately
		"sleep",
		"cos",
		"gmtime",
		"oct",
		"sqrt",
		"defined",
//		"goto",	// this specified in 21 line
		"ord",
		"srand",
		"delete",
		"hex",
		"quotemeta",
		"stat",
//		"do",	// being handled separately
		"int",
		"rand",
		"uc",
//		"eval",	// being handled separately
		"lc",
		"readlink",
		"ucfirst",
		"exists",
		"lcfirst",
		"ref",
		"umask",
		"exit",
		"length",
//		"require", // being handled separately
//		"undef", // being handled separately
		"gethostbyname",
		"localtime",
		"return"	// ("foo").bar
	));

	public static final HashSet<String> BUILT_IN = new HashSet<>( Arrays.asList(
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
			"xor",

			// were in implemented
			"eval",
			"if",
			"package",
			"require",
			"no",
			"use"
	));

}
