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

package com.perl5.lang.perl.util;

import java.util.Set;

import static com.perl5.lang.perl.psi.utils.PerlPsiUtil.DOUBLE_QUOTE;


public final class PerlBuiltInScalars {
  private PerlBuiltInScalars() {
  }

  public static final Set<String> BUILT_IN = Set.of(
    "OLD_PERL_VERSION",
    "^WIN32_SLOPPY_STAT",  // deprecated
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
    DOUBLE_QUOTE,
    "^S",
    "LIST_SEPARATOR",
    "^T",
    "MATCH",
    "$",
    "^TAINT",
    "%",
    "^UNICODE",
    "NR",
    "&",
    "^UTF8LOCALE",
    "OFMT",
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
    "^ENCODING", // deprecated
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
    "LAST_PAREN_MATCH",
    "^LAST_SUCCESSFUL_PATTERN",
    "INC",
    "^MAX_NESTED_EVAL_BEGIN_BLOCKS",
    "^RE_COMPILE_RECURSION_LIMIT",
    "^SAFE_LOCALES"
  );
}
