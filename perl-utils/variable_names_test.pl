#!/usr/bin/perl
use warnings;
use strict;
use v5.10;

my @scalar_vars = (
    "OLD_PERL_VERSION",
    "^WIN32_SLOPPY_STAT",
    "^MATCH",
    "^PREMATCH",
    "^POSTMATCH",
    "LAST_SUBMATCH_RESULT",
    "^LAST_FH",
    "^UTF8CACHE",
    "^GLOBAL_PHASE",
    "!",
    "^",
    "^RE_TRIE_MAXBUF",
    "LAST_REGEXP_CODE_RESULT",
    "\"",
    "^S",
    "LIST_SEPARATOR",
    "^T",
    "MATCH",
    "\$",
    "^TAINT",
    "%",
    "^UNICODE",
    "NR",
    "&",
    "^UTF8LOCALE",
    "'",
    "^V",
    "OFS",
    "(",
    "^W",
    "ORS",
    ")",
    "^WARNING_BITS",
    "OS_ERROR",
    "*",
    "OSNAME",
    "+",
    "^X",
    "OUTPUT_AUTO_FLUSH",
    ",",
    "_",
    "OUTPUT_FIELD_SEPARATOR",
    "-",
    "`",
    "OUTPUT_RECORD_SEPARATOR",
    ".",
    "a",
    "PERL_VERSION",
    "/",
    "ACCUMULATOR",
    "PERLDB",
    "0",
    "ARG",
    "PID",
    ":",
    "ARGV",
    "POSTMATCH",
    ";",
    "b",
    "PREMATCH",
    "<",
    "BASETIME",
    "PROCESS_ID",
    "=",
    "CHILD_ERROR",
    "PROGRAM_NAME",
    ">",
    "COMPILING",
    "REAL_GROUP_ID",
    "?",
    "DEBUGGING",
    "REAL_USER_ID",
    "@",
    "EFFECTIVE_GROUP_ID",
    "RS",
    "[",
    "EFFECTIVE_USER_ID",
    "SUBSCRIPT_SEPARATOR",
    "\\",
    "EGID",
    "SUBSEP",
    "]",
    "ERRNO",
    "SYSTEM_FD_MAX",
    "EUID",
    "UID",
    "^A",
    "EVAL_ERROR",
    "WARNING",
    "^C",
    "EXCEPTIONS_BEING_CAUGHT",
    "|",
    "^CHILD_ERROR_NATIVE",
    "EXECUTABLE_NAME",
    "~",
    "^D",
    "EXTENDED_OS_ERROR",
    "^E",
    "FORMAT_FORMFEED",
    "^ENCODING",
    "FORMAT_LINE_BREAK_CHARACTERS",
    "^F",
    "FORMAT_LINES_LEFT",
    "^H",
    "FORMAT_LINES_PER_PAGE",
    "^I",
    "FORMAT_NAME",
    "^L",
    "FORMAT_PAGE_NUMBER",
    "^M",
    "FORMAT_TOP_NAME",
    "^N",
    "GID",
    "^O",
    "INPLACE_EDIT",
    "^OPEN",
    "INPUT_LINE_NUMBER",
    "^P",
    "INPUT_RECORD_SEPARATOR",
    "^R",
    "^RE_DEBUG_FLAGS",
    "LAST_PAREN_MATCH"

);

my @array_vars = (
    "+",
    "-",
    "_",
    "F",
    "ARG",
    "LAST_MATCH_END",
    "ARGV",
    "INC",
    "^CAPTURE",
    "LAST_MATCH_START"
);

my @hash_vars = (
    "!",
    "+",
    "-",
    "^H",
    "ENV",
    "INC",
    "SIG",
    "LAST_PAREN_MATCH",
    "^CAPTURE",
    "^CAPTURE_ALL",
    "OS_ERROR",
    "ERRNO"
);

generate_tests(Scalar => '$', @scalar_vars);
generate_tests(Array => '@', @array_vars);
generate_tests(Hash => '%', @hash_vars);


sub generate_tests {
    my $type = shift;
    my $sigil = shift;

    for my $name (@_) {
        my $test_name = $name;
        my $prefix = "";
        if (length $test_name > 1 && $test_name =~ s/^\^//) {
            $prefix = "Ctrl";
        }
        if (length $test_name == 1 && $test_name !~ /\w/) {
            $test_name = "ord" . ord $test_name;
        }
        $test_name = join "", map {"\u$_"} split /_/, lc $test_name;
        $name =~ s/^(\^.{2,})$/{$1}/;
        $name =~ s{\\}{\\\\};
        $name =~ s{"}{\\"};
        say "public void test$type$prefix\u$test_name(){doTest(\"$sigil$name\");}";
    }

}


