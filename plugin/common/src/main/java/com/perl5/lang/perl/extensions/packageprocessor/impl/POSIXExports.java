/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.extensions.packageprocessor.impl;

import java.util.List;


public interface POSIXExports {
  List<String> EXPORT = List.of(
    "%SIGRT",
    "ARG_MAX",
    "B0",
    "B110",
    "B1200",
    "B134",
    "B150",
    "B1800",
    "B19200",
    "B200",
    "B2400",
    "B300",
    "B38400",
    "B4800",
    "B50",
    "B600",
    "B75",
    "B9600",
    "BRKINT",
    "BUFSIZ",
    "CHAR_BIT",
    "CHAR_MAX",
    "CHAR_MIN",
    "CHILD_MAX",
    "CLK_TCK",
    "CLOCAL",
    "CLOCKS_PER_SEC",
    "CREAD",
    "CS5",
    "CS6",
    "CS7",
    "CS8",
    "CSIZE",
    "CSTOPB",
    "DBL_DIG",
    "DBL_EPSILON",
    "DBL_MANT_DIG",
    "DBL_MAX",
    "DBL_MAX_10_EXP",
    "DBL_MAX_EXP",
    "DBL_MIN",
    "DBL_MIN_10_EXP",
    "DBL_MIN_EXP",
    "E2BIG",
    "EACCES",
    "EADDRINUSE",
    "EADDRNOTAVAIL",
    "EAFNOSUPPORT",
    "EAGAIN",
    "EALREADY",
    "EBADF",
    "EBADMSG",
    "EBUSY",
    "ECANCELED",
    "ECHILD",
    "ECHO",
    "ECHOE",
    "ECHOK",
    "ECHONL",
    "ECONNABORTED",
    "ECONNREFUSED",
    "ECONNRESET",
    "EDEADLK",
    "EDESTADDRREQ",
    "EDOM",
    "EDQUOT",
    "EEXIST",
    "EFAULT",
    "EFBIG",
    "EHOSTDOWN",
    "EHOSTUNREACH",
    "EIDRM",
    "EILSEQ",
    "EINPROGRESS",
    "EINTR",
    "EINVAL",
    "EIO",
    "EISCONN",
    "EISDIR",
    "ELOOP",
    "EMFILE",
    "EMLINK",
    "EMSGSIZE",
    "ENAMETOOLONG",
    "ENETDOWN",
    "ENETRESET",
    "ENETUNREACH",
    "ENFILE",
    "ENOBUFS",
    "ENODATA",
    "ENODEV",
    "ENOENT",
    "ENOEXEC",
    "ENOLCK",
    "ENOLINK",
    "ENOMEM",
    "ENOMSG",
    "ENOPROTOOPT",
    "ENOSPC",
    "ENOSR",
    "ENOSTR",
    "ENOSYS",
    "ENOTBLK",
    "ENOTCONN",
    "ENOTDIR",
    "ENOTEMPTY",
    "ENOTRECOVERABLE",
    "ENOTSOCK",
    "ENOTSUP",
    "ENOTTY",
    "ENXIO",
    "EOF",
    "EOPNOTSUPP",
    "EOTHER",
    "EOVERFLOW",
    "EOWNERDEAD",
    "EPERM",
    "EPFNOSUPPORT",
    "EPIPE",
    "EPROCLIM",
    "EPROTO",
    "EPROTONOSUPPORT",
    "EPROTOTYPE",
    "ERANGE",
    "EREMOTE",
    "ERESTART",
    "EROFS",
    "ESHUTDOWN",
    "ESOCKTNOSUPPORT",
    "ESPIPE",
    "ESRCH",
    "ESTALE",
    "ETIME",
    "ETIMEDOUT",
    "ETOOMANYREFS",
    "ETXTBSY",
    "EUSERS",
    "EWOULDBLOCK",
    "EXDEV",
    "EXIT_FAILURE",
    "EXIT_SUCCESS",
    "FD_CLOEXEC",
    "FILENAME_MAX",
    "FLT_DIG",
    "FLT_EPSILON",
    "FLT_MANT_DIG",
    "FLT_MAX",
    "FLT_MAX_10_EXP",
    "FLT_MAX_EXP",
    "FLT_MIN",
    "FLT_MIN_10_EXP",
    "FLT_MIN_EXP",
    "FLT_RADIX",
    "FLT_ROUNDS",
    "FP_ILOGB0",
    "FP_ILOGBNAN",
    "FP_INFINITE",
    "FP_NAN",
    "FP_NORMAL",
    "FP_SUBNORMAL",
    "FP_ZERO",
    "F_DUPFD",
    "F_GETFD",
    "F_GETFL",
    "F_GETLK",
    "F_OK",
    "F_RDLCK",
    "F_SETFD",
    "F_SETFL",
    "F_SETLK",
    "F_SETLKW",
    "F_UNLCK",
    "F_WRLCK",
    "HUGE_VAL",
    "HUPCL",
    "ICANON",
    "ICRNL",
    "IEXTEN",
    "IGNBRK",
    "IGNCR",
    "IGNPAR",
    "INFINITY",
    "INLCR",
    "INPCK",
    "INT_MAX",
    "INT_MIN",
    "ISIG",
    "ISTRIP",
    "IXOFF",
    "IXON",
    "LC_ALL",
    "LC_COLLATE",
    "LC_CTYPE",
    "LC_MESSAGES",
    "LC_MONETARY",
    "LC_NUMERIC",
    "LC_TIME",
    "LDBL_DIG",
    "LDBL_EPSILON",
    "LDBL_MANT_DIG",
    "LDBL_MAX",
    "LDBL_MAX_10_EXP",
    "LDBL_MAX_EXP",
    "LDBL_MIN",
    "LDBL_MIN_10_EXP",
    "LDBL_MIN_EXP",
    "LINK_MAX",
    "LONG_MAX",
    "LONG_MIN",
    "L_ctermid",
    "L_cuserid",
    "L_tmpname",
    "MAX_CANON",
    "MAX_INPUT",
    "MB_CUR_MAX",
    "MB_LEN_MAX",
    "M_1_PI",
    "M_2_PI",
    "M_2_SQRTPI",
    "M_E",
    "M_LN10",
    "M_LN2",
    "M_LOG10E",
    "M_LOG2E",
    "M_PI",
    "M_PI_2",
    "M_PI_4",
    "M_SQRT1_2",
    "M_SQRT2",
    "NAME_MAX",
    "NAN",
    "NCCS",
    "NDEBUG",
    "NGROUPS_MAX",
    "NOFLSH",
    "NULL",
    "OPEN_MAX",
    "OPOST",
    "O_ACCMODE",
    "O_APPEND",
    "O_CREAT",
    "O_EXCL",
    "O_NOCTTY",
    "O_NONBLOCK",
    "O_RDONLY",
    "O_RDWR",
    "O_TRUNC",
    "O_WRONLY",
    "PARENB",
    "PARMRK",
    "PARODD",
    "PATH_MAX",
    "PIPE_BUF",
    "RAND_MAX",
    "R_OK",
    "SA_NOCLDSTOP",
    "SA_NOCLDWAIT",
    "SA_NODEFER",
    "SA_ONSTACK",
    "SA_RESETHAND",
    "SA_RESTART",
    "SA_SIGINFO",
    "SCHAR_MAX",
    "SCHAR_MIN",
    "SEEK_CUR",
    "SEEK_END",
    "SEEK_SET",
    "SHRT_MAX",
    "SHRT_MIN",
    "SIGABRT",
    "SIGALRM",
    "SIGBUS",
    "SIGCHLD",
    "SIGCONT",
    "SIGFPE",
    "SIGHUP",
    "SIGILL",
    "SIGINT",
    "SIGKILL",
    "SIGPIPE",
    "SIGPOLL",
    "SIGPROF",
    "SIGQUIT",
    "SIGRTMAX",
    "SIGRTMIN",
    "SIGSEGV",
    "SIGSTOP",
    "SIGSYS",
    "SIGTERM",
    "SIGTRAP",
    "SIGTSTP",
    "SIGTTIN",
    "SIGTTOU",
    "SIGURG",
    "SIGUSR1",
    "SIGUSR2",
    "SIGVTALRM",
    "SIGXCPU",
    "SIGXFSZ",
    "SIG_BLOCK",
    "SIG_DFL",
    "SIG_ERR",
    "SIG_IGN",
    "SIG_SETMASK",
    "SIG_UNBLOCK",
    "SSIZE_MAX",
    "STDERR_FILENO",
    "STDIN_FILENO",
    "STDOUT_FILENO",
    "STREAM_MAX",
    "S_IRGRP",
    "S_IROTH",
    "S_IRUSR",
    "S_IRWXG",
    "S_IRWXO",
    "S_IRWXU",
    "S_ISBLK",
    "S_ISCHR",
    "S_ISDIR",
    "S_ISFIFO",
    "S_ISGID",
    "S_ISREG",
    "S_ISUID",
    "S_IWGRP",
    "S_IWOTH",
    "S_IWUSR",
    "S_IXGRP",
    "S_IXOTH",
    "S_IXUSR",
    "TCIFLUSH",
    "TCIOFF",
    "TCIOFLUSH",
    "TCION",
    "TCOFLUSH",
    "TCOOFF",
    "TCOON",
    "TCSADRAIN",
    "TCSAFLUSH",
    "TCSANOW",
    "TMP_MAX",
    "TOSTOP",
    "TZNAME_MAX",
    "UCHAR_MAX",
    "UINT_MAX",
    "ULONG_MAX",
    "USHRT_MAX",
    "VEOF",
    "VEOL",
    "VERASE",
    "VINTR",
    "VKILL",
    "VMIN",
    "VQUIT",
    "VSTART",
    "VSTOP",
    "VSUSP",
    "VTIME",
    "WEXITSTATUS",
    "WIFEXITED",
    "WIFSIGNALED",
    "WIFSTOPPED",
    "WNOHANG",
    "WSTOPSIG",
    "WTERMSIG",
    "WUNTRACED",
    "W_OK",
    "X_OK",
    "_PC_CHOWN_RESTRICTED",
    "_PC_LINK_MAX",
    "_PC_MAX_CANON",
    "_PC_MAX_INPUT",
    "_PC_NAME_MAX",
    "_PC_NO_TRUNC",
    "_PC_PATH_MAX",
    "_PC_PIPE_BUF",
    "_PC_VDISABLE",
    "_POSIX_ARG_MAX",
    "_POSIX_CHILD_MAX",
    "_POSIX_CHOWN_RESTRICTED",
    "_POSIX_JOB_CONTROL",
    "_POSIX_LINK_MAX",
    "_POSIX_MAX_CANON",
    "_POSIX_MAX_INPUT",
    "_POSIX_NAME_MAX",
    "_POSIX_NGROUPS_MAX",
    "_POSIX_NO_TRUNC",
    "_POSIX_OPEN_MAX",
    "_POSIX_PATH_MAX",
    "_POSIX_PIPE_BUF",
    "_POSIX_SAVED_IDS",
    "_POSIX_SSIZE_MAX",
    "_POSIX_STREAM_MAX",
    "_POSIX_TZNAME_MAX",
    "_POSIX_VDISABLE",
    "_POSIX_VERSION",
    "_SC_ARG_MAX",
    "_SC_CHILD_MAX",
    "_SC_CLK_TCK",
    "_SC_JOB_CONTROL",
    "_SC_NGROUPS_MAX",
    "_SC_OPEN_MAX",
    "_SC_PAGESIZE",
    "_SC_SAVED_IDS",
    "_SC_STREAM_MAX",
    "_SC_TZNAME_MAX",
    "_SC_VERSION",
    "_exit",
    "abort",
    "access",
    "acos",
    "asctime",
    "asin",
    "assert",
    "atan",
    "atexit",
    "atof",
    "atoi",
    "atol",
    "bsearch",
    "calloc",
    "ceil",
    "cfgetispeed",
    "cfgetospeed",
    "cfsetispeed",
    "cfsetospeed",
    "clearerr",
    "clock",
    "cosh",
    "creat",
    "ctermid",
    "ctime",
    "cuserid",
    "difftime",
    "div",
    "dup",
    "dup2",
    "errno",
    "execl",
    "execle",
    "execlp",
    "execv",
    "execve",
    "execvp",
    "fabs",
    "fclose",
    "fdopen",
    "feof",
    "ferror",
    "fflush",
    "fgetc",
    "fgetpos",
    "fgets",
    "floor",
    "fmod",
    "fopen",
    "fpathconf",
    "fprintf",
    "fputc",
    "fputs",
    "fread",
    "free",
    "freopen",
    "frexp",
    "fscanf",
    "fseek",
    "fsetpos",
    "fstat",
    "fsync",
    "ftell",
    "fwrite",
    "getchar",
    "getcwd",
    "getegid",
    "getenv",
    "geteuid",
    "getgid",
    "getgroups",
    "getpid",
    "gets",
    "getuid",
    "isalnum",
    "isalpha",
    "isatty",
    "iscntrl",
    "isdigit",
    "isgraph",
    "islower",
    "isprint",
    "ispunct",
    "isspace",
    "isupper",
    "isxdigit",
    "labs",
    "ldexp",
    "ldiv",
    "localeconv",
    "log10",
    "longjmp",
    "lseek",
    "malloc",
    "mblen",
    "mbstowcs",
    "mbtowc",
    "memchr",
    "memcmp",
    "memcpy",
    "memmove",
    "memset",
    "mkfifo",
    "mktime",
    "modf",
    "offsetof",
    "pathconf",
    "pause",
    "perror",
    "pow",
    "putc",
    "putchar",
    "puts",
    "qsort",
    "raise",
    "realloc",
    "remove",
    "rewind",
    "scanf",
    "setbuf",
    "setgid",
    "setjmp",
    "setlocale",
    "setpgid",
    "setsid",
    "setuid",
    "setvbuf",
    "sigaction",
    "siglongjmp",
    "signal",
    "sigpending",
    "sigprocmask",
    "sigsetjmp",
    "sigsuspend",
    "sinh",
    "sscanf",
    "stderr",
    "stdin",
    "stdout",
    "strcat",
    "strchr",
    "strcmp",
    "strcoll",
    "strcpy",
    "strcspn",
    "strerror",
    "strftime",
    "strlen",
    "strncat",
    "strncmp",
    "strncpy",
    "strpbrk",
    "strrchr",
    "strspn",
    "strstr",
    "strtod",
    "strtok",
    "strtol",
    "strtoul",
    "strxfrm",
    "sysconf",
    "tan",
    "tanh",
    "tcdrain",
    "tcflow",
    "tcflush",
    "tcgetattr",
    "tcgetpgrp",
    "tcsendbreak",
    "tcsetattr",
    "tcsetpgrp",
    "tmpfile",
    "tmpnam",
    "tolower",
    "toupper",
    "ttyname",
    "tzname",
    "tzset",
    "uname",
    "ungetc",
    "vfprintf",
    "vprintf",
    "vsprintf",
    "wcstombs",
    "wctomb"
  );

  List<String> EXPORT_OK = List.of(
    "FE_DOWNWARD",
    "FE_TONEAREST",
    "FE_TOWARDZERO",
    "FE_UPWARD",
    "Inf",
    "NaN",
    "abs",
    "acosh",
    "alarm",
    "asinh",
    "atan2",
    "atanh",
    "cbrt",
    "chdir",
    "chmod",
    "chown",
    "close",
    "closedir",
    "copysign",
    "cos",
    "erf",
    "erfc",
    "exit",
    "exp",
    "exp2",
    "expm1",
    "fcntl",
    "fdim",
    "fegetround",
    "fesetround",
    "fileno",
    "fma",
    "fmax",
    "fmin",
    "fork",
    "fpclassify",
    "getc",
    "getgrgid",
    "getgrnam",
    "getlogin",
    "getpgrp",
    "getppid",
    "getpwnam",
    "getpwuid",
    "gmtime",
    "hypot",
    "ilogb",
    "isfinite",
    "isgreater",
    "isgreaterequal",
    "isinf",
    "isless",
    "islessequal",
    "islessgreater",
    "isnan",
    "isnormal",
    "isunordered",
    "j0",
    "j1",
    "jn",
    "kill",
    "lchown",
    "lgamma",
    "link",
    "localtime",
    "log",
    "log1p",
    "log2",
    "logb",
    "lrint",
    "mkdir",
    "nan",
    "nearbyint",
    "nextafter",
    "nexttoward",
    "nice",
    "open",
    "opendir",
    "pipe",
    "printf",
    "rand",
    "read",
    "readdir",
    "remainder",
    "remquo",
    "rename",
    "rewinddir",
    "rint",
    "rmdir",
    "round",
    "scalbn",
    "signbit",
    "sin",
    "sleep",
    "sprintf",
    "sqrt",
    "srand",
    "stat",
    "strtold",
    "system",
    "tgamma",
    "time",
    "times",
    "trunc",
    "umask",
    "unlink",
    "utime",
    "wait",
    "waitpid",
    "write",
    "y0",
    "y1",
    "yn"
  );
}
