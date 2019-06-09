/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package documentation;

import base.PerlLightTestCase;
import org.jetbrains.annotations.NotNull;

public class PerlQuickDocBuiltInVariableTest extends PerlLightTestCase {

  static final String ANSWERS_PATH = "testData/documentation/perl/quickdoc/builtinVars";

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerlPod();
  }

  @Override
  protected String getTestDataPath() {
    return ANSWERS_PATH;
  }

  public void testScalarOldPerlVersion() {doTest("$OLD_PERL_VERSION");}

  public void testScalarCtrlWin32SloppyStat() {doTest("${^WIN32_SLOPPY_STAT}");}

  public void testScalarCtrlMatch() {doTest("${^MATCH}");}

  public void testScalarCtrlPrematch() {doTest("${^PREMATCH}");}

  public void testScalarCtrlPostmatch() {doTest("${^POSTMATCH}");}

  public void testScalarLastSubmatchResult() {doTest("$LAST_SUBMATCH_RESULT");}

  public void testScalarCtrlLastFh() {doTest("${^LAST_FH}");}

  public void testScalarCtrlUtf8cache() {doTest("${^UTF8CACHE}");}

  public void testScalarCtrlGlobalPhase() {doTest("${^GLOBAL_PHASE}");}

  public void testScalarOrd33() {doTest("$!");}

  public void testScalarOrd94() {doTest("$^");}

  public void testScalarCtrlReTrieMaxbuf() {doTest("${^RE_TRIE_MAXBUF}");}

  public void testScalarLastRegexpCodeResult() {doTest("$LAST_REGEXP_CODE_RESULT");}

  public void testScalarOrd34() {doTest("$\"");}

  public void testScalarCtrlS() {doTest("$^S");}

  public void testScalarListSeparator() {doTest("$LIST_SEPARATOR");}

  public void testScalarCtrlT() {doTest("$^T");}

  public void testScalarMatch() {doTest("$MATCH");}

  public void testScalarOrd36() {doTest("$$");}

  public void testScalarCtrlTaint() {doTest("${^TAINT}");}

  public void testScalarOrd37() {doTest("$%");}

  public void testScalarCtrlUnicode() {doTest("${^UNICODE}");}

  public void testScalarNr() {doTest("$NR");}

  public void testScalarOrd38() {doTest("$&");}

  public void testScalarCtrlUtf8locale() {doTest("${^UTF8LOCALE}");}

  public void testScalarOrd39() {doTest("$'");}

  public void testScalarCtrlV() {doTest("$^V");}

  public void testScalarOfs() {doTest("$OFS");}

  public void testScalarOrd40() {doTest("$(");}

  public void testScalarCtrlW() {doTest("$^W");}

  public void testScalarOrs() {doTest("$ORS");}

  public void testScalarOrd41() {doTest("$)");}

  public void testScalarCtrlWarningBits() {doTest("${^WARNING_BITS}");}

  public void testScalarOsError() {doTest("$OS_ERROR");}

  public void testScalarOrd42() {doTest("$*");}

  public void testScalarOsname() {doTest("$OSNAME");}

  public void testScalarOrd43() {doTest("$+");}

  public void testScalarCtrlX() {doTest("$^X");}

  public void testScalarOrd44() {doTest("$,");}

  public void testScalar() {doTest("$_");}

  public void testScalarOutputFieldSeparator() {doTest("$OUTPUT_FIELD_SEPARATOR");}

  public void testScalarOrd45() {doTest("$-");}

  public void testScalarOrd96() {doTest("$`");}

  public void testScalarOutputRecordSeparator() {doTest("$OUTPUT_RECORD_SEPARATOR");}

  public void testScalarOrd46() {doTest("$.");}

  public void testScalarA() {doTest("$a");}

  public void testScalarPerlVersion() {doTest("$PERL_VERSION");}

  public void testScalarOrd47() {doTest("$/");}

  public void testScalarAccumulator() {doTest("$ACCUMULATOR");}

  public void testScalarPerldb() {doTest("$PERLDB");}

  public void testScalar0() {doTest("$0");}

  public void testScalar1() {doTest("$1");}

  public void testScalar10() {doTest("$10");}

  public void testScalarArg() {doTest("$ARG");}

  public void testScalarPid() {doTest("$PID");}

  public void testScalarOrd58() {doTest("$:");}

  public void testScalarArgv() {doTest("$ARGV");}

  public void testScalarPostmatch() {doTest("$POSTMATCH");}

  public void testScalarOrd59() {doTest("$;");}

  public void testScalarB() {doTest("$b");}

  public void testScalarPrematch() {doTest("$PREMATCH");}

  public void testScalarOrd60() {doTest("$<");}

  public void testScalarBasetime() {doTest("$BASETIME");}

  public void testScalarProcessId() {doTest("$PROCESS_ID");}

  public void testScalarOrd61() {doTest("$=");}

  public void testScalarChildError() {doTest("$CHILD_ERROR");}

  public void testScalarProgramName() {doTest("$PROGRAM_NAME");}

  public void testScalarOrd62() {doTest("$>");}

  public void testScalarCompiling() {doTest("$COMPILING");}

  public void testScalarRealGroupId() {doTest("$REAL_GROUP_ID");}

  public void testScalarOrd63() {doTest("$?");}

  public void testScalarDebugging() {doTest("$DEBUGGING");}

  public void testScalarRealUserId() {doTest("$REAL_USER_ID");}

  public void testScalarOrd64() {doTest("$@");}

  public void testScalarEffectiveGroupId() {doTest("$EFFECTIVE_GROUP_ID");}

  public void testScalarRs() {doTest("$RS");}

  public void testScalarOrd91() {doTest("$[");}

  public void testScalarEffectiveUserId() {doTest("$EFFECTIVE_USER_ID");}

  public void testScalarSubscriptSeparator() {doTest("$SUBSCRIPT_SEPARATOR");}

  public void testScalarOrd92() {doTest("$\\");}

  public void testScalarEgid() {doTest("$EGID");}

  public void testScalarSubsep() {doTest("$SUBSEP");}

  public void testScalarOrd93() {doTest("$]");}

  public void testScalarErrno() {doTest("$ERRNO");}

  public void testScalarSystemFdMax() {doTest("$SYSTEM_FD_MAX");}

  public void testScalarEuid() {doTest("$EUID");}

  public void testScalarUid() {doTest("$UID");}

  public void testScalarCtrlA() {doTest("$^A");}

  public void testScalarEvalError() {doTest("$EVAL_ERROR");}

  public void testScalarWarning() {doTest("$WARNING");}

  public void testScalarCtrlC() {doTest("$^C");}

  public void testScalarExceptionsBeingCaught() {doTest("$EXCEPTIONS_BEING_CAUGHT");}

  public void testScalarOrd124() {doTest("$|");}

  public void testScalarCtrlChildErrorNative() {doTest("${^CHILD_ERROR_NATIVE}");}

  public void testScalarExecutableName() {doTest("$EXECUTABLE_NAME");}

  public void testScalarOrd126() {doTest("$~");}

  public void testScalarCtrlD() {doTest("$^D");}

  public void testScalarExtendedOsError() {doTest("$EXTENDED_OS_ERROR");}

  public void testScalarCtrlE() {doTest("$^E");}

  public void testScalarFormatFormfeed() {doTest("$FORMAT_FORMFEED");}

  public void testScalarCtrlEncoding() {doTest("${^ENCODING}");}

  public void testScalarFormatLineBreakCharacters() {doTest("$FORMAT_LINE_BREAK_CHARACTERS");}

  public void testScalarCtrlF() {doTest("$^F");}

  public void testScalarFormatLinesLeft() {doTest("$FORMAT_LINES_LEFT");}

  public void testScalarCtrlH() {doTest("$^H");}

  public void testScalarFormatLinesPerPage() {doTest("$FORMAT_LINES_PER_PAGE");}

  public void testScalarCtrlI() {doTest("$^I");}

  public void testScalarFormatName() {doTest("$FORMAT_NAME");}

  public void testScalarCtrlL() {doTest("$^L");}

  public void testScalarFormatPageNumber() {doTest("$FORMAT_PAGE_NUMBER");}

  public void testScalarCtrlM() {doTest("$^M");}

  public void testScalarFormatTopName() {doTest("$FORMAT_TOP_NAME");}

  public void testScalarCtrlN() {doTest("$^N");}

  public void testScalarGid() {doTest("$GID");}

  public void testScalarCtrlO() {doTest("$^O");}

  public void testScalarInplaceEdit() {doTest("$INPLACE_EDIT");}

  public void testScalarCtrlOpen() {doTest("${^OPEN}");}

  public void testScalarInputLineNumber() {doTest("$INPUT_LINE_NUMBER");}

  public void testScalarCtrlP() {doTest("$^P");}

  public void testScalarInputRecordSeparator() {doTest("$INPUT_RECORD_SEPARATOR");}

  public void testScalarCtrlR() {doTest("$^R");}

  public void testScalarCtrlReDebugFlags() {doTest("${^RE_DEBUG_FLAGS}");}

  public void testScalarLastParenMatch() {doTest("$LAST_PAREN_MATCH");}

  public void testArrayOrd43() {doTest("@+");}

  public void testArrayOrd45() {doTest("@-");}

  public void testArray() {doTest("@_");}

  public void testArrayF() {doTest("@F");}

  public void testArrayArg() {doTest("@ARG");}

  public void testArrayLastMatchEnd() {doTest("@LAST_MATCH_END");}

  public void testArrayArgv() {doTest("@ARGV");}

  public void testArrayInc() {doTest("@INC");}

  public void testArrayCtrlCapture() {doTest("@{^CAPTURE}");}

  public void testArrayLastMatchStart() {doTest("@LAST_MATCH_START");}

  public void testHashOrd33() {doTest("%!");}

  public void testHashOrd43() {doTest("%+");}

  public void testHashOrd45() {doTest("%-");}

  public void testHashCtrlH() {doTest("%^H");}

  public void testHashEnv() {doTest("%ENV");}

  public void testHashInc() {doTest("%INC");}

  public void testHashSig() {doTest("%SIG");}

  public void testHashLastParenMatch() {doTest("%LAST_PAREN_MATCH");}

  public void testHashCtrlCapture() {doTest("%{^CAPTURE}");}

  public void testHashCtrlCaptureAll() {doTest("%{^CAPTURE_ALL}");}

  public void testHashOsError() {doTest("%OS_ERROR");}

  public void testHashErrno() {doTest("%ERRNO");}


  @NotNull
  @Override
  protected String getResultsFileExtension() {
    return "txt";
  }

  private void doTest(@NotNull String text) {
    initWithTextSmart(text + " ");
    getEditor().getCaretModel().moveToOffset(text.length() == 2 ? 1 : 2);
    doTestQuickDocWithoutInit();
  }
}
