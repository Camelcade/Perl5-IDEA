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
import org.junit.Test;
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

  @Test
  public void testScalarOldPerlVersion() {doTest("$OLD_PERL_VERSION");}

  @Test
  public void testScalarCtrlWin32SloppyStat() {doTest("${^WIN32_SLOPPY_STAT}");}

  @Test
  public void testScalarCtrlMatch() {doTest("${^MATCH}");}

  @Test
  public void testScalarCtrlPrematch() {doTest("${^PREMATCH}");}

  @Test
  public void testScalarCtrlPostmatch() {doTest("${^POSTMATCH}");}

  @Test
  public void testScalarLastSubmatchResult() {doTest("$LAST_SUBMATCH_RESULT");}

  @Test
  public void testScalarCtrlLastFh() {doTest("${^LAST_FH}");}

  @Test
  public void testScalarCtrlUtf8cache() {doTest("${^UTF8CACHE}");}

  @Test
  public void testScalarCtrlGlobalPhase() {doTest("${^GLOBAL_PHASE}");}

  @Test
  public void testScalarOrd33() {doTest("$!");}

  @Test
  public void testScalarOrd94() {doTest("$^");}

  @Test
  public void testScalarCtrlReTrieMaxbuf() {doTest("${^RE_TRIE_MAXBUF}");}

  @Test
  public void testScalarLastRegexpCodeResult() {doTest("$LAST_REGEXP_CODE_RESULT");}

  @Test
  public void testScalarOrd34() {doTest("$\"");}

  @Test
  public void testScalarCtrlS() {doTest("$^S");}

  @Test
  public void testScalarListSeparator() {doTest("$LIST_SEPARATOR");}

  @Test
  public void testScalarCtrlT() {doTest("$^T");}

  @Test
  public void testScalarMatch() {doTest("$MATCH");}

  @Test
  public void testScalarOrd36() {doTest("$$");}

  @Test
  public void testScalarCtrlTaint() {doTest("${^TAINT}");}

  @Test
  public void testScalarOrd37() {doTest("$%");}

  @Test
  public void testScalarCtrlUnicode() {doTest("${^UNICODE}");}

  @Test
  public void testScalarNr() {doTest("$NR");}

  @Test
  public void testScalarOrd38() {doTest("$&");}

  @Test
  public void testScalarCtrlUtf8locale() {doTest("${^UTF8LOCALE}");}

  @Test
  public void testScalarOrd39() {doTest("$'");}

  @Test
  public void testScalarCtrlV() {doTest("$^V");}

  @Test
  public void testScalarOfs() {doTest("$OFS");}

  @Test
  public void testScalarOrd40() {doTest("$(");}

  @Test
  public void testScalarCtrlW() {doTest("$^W");}

  @Test
  public void testScalarOrs() {doTest("$ORS");}

  @Test
  public void testScalarOrd41() {doTest("$)");}

  @Test
  public void testScalarCtrlWarningBits() {doTest("${^WARNING_BITS}");}

  @Test
  public void testScalarOsError() {doTest("$OS_ERROR");}

  @Test
  public void testScalarOrd42() {doTest("$*");}

  @Test
  public void testScalarOsname() {doTest("$OSNAME");}

  @Test
  public void testScalarOrd43() {doTest("$+");}

  @Test
  public void testScalarCtrlX() {doTest("$^X");}

  @Test
  public void testScalarOrd44() {doTest("$,");}

  @Test
  public void testScalar() {doTest("$_");}

  @Test
  public void testScalarOutputFieldSeparator() {doTest("$OUTPUT_FIELD_SEPARATOR");}

  @Test
  public void testScalarOrd45() {doTest("$-");}

  @Test
  public void testScalarOrd96() {doTest("$`");}

  @Test
  public void testScalarOutputRecordSeparator() {doTest("$OUTPUT_RECORD_SEPARATOR");}

  @Test
  public void testScalarOrd46() {doTest("$.");}

  @Test
  public void testScalarA() {doTest("$a");}

  @Test
  public void testScalarPerlVersion() {doTest("$PERL_VERSION");}

  @Test
  public void testScalarOrd47() {doTest("$/");}

  @Test
  public void testScalarAccumulator() {doTest("$ACCUMULATOR");}

  @Test
  public void testScalarPerldb() {doTest("$PERLDB");}

  @Test
  public void testScalar0() {doTest("$0");}

  @Test
  public void testScalar1() {doTest("$1");}

  @Test
  public void testScalar10() {doTest("$10");}

  @Test
  public void testScalarArg() {doTest("$ARG");}

  @Test
  public void testScalarPid() {doTest("$PID");}

  @Test
  public void testScalarOrd58() {doTest("$:");}

  @Test
  public void testScalarArgv() {doTest("$ARGV");}

  @Test
  public void testScalarPostmatch() {doTest("$POSTMATCH");}

  @Test
  public void testScalarOrd59() {doTest("$;");}

  @Test
  public void testScalarB() {doTest("$b");}

  @Test
  public void testScalarPrematch() {doTest("$PREMATCH");}

  @Test
  public void testScalarOrd60() {doTest("$<");}

  @Test
  public void testScalarBasetime() {doTest("$BASETIME");}

  @Test
  public void testScalarProcessId() {doTest("$PROCESS_ID");}

  @Test
  public void testScalarOrd61() {doTest("$=");}

  @Test
  public void testScalarChildError() {doTest("$CHILD_ERROR");}

  @Test
  public void testScalarProgramName() {doTest("$PROGRAM_NAME");}

  @Test
  public void testScalarOrd62() {doTest("$>");}

  @Test
  public void testScalarCompiling() {doTest("$COMPILING");}

  @Test
  public void testScalarRealGroupId() {doTest("$REAL_GROUP_ID");}

  @Test
  public void testScalarOrd63() {doTest("$?");}

  @Test
  public void testScalarDebugging() {doTest("$DEBUGGING");}

  @Test
  public void testScalarRealUserId() {doTest("$REAL_USER_ID");}

  @Test
  public void testScalarOrd64() {doTest("$@");}

  @Test
  public void testScalarEffectiveGroupId() {doTest("$EFFECTIVE_GROUP_ID");}

  @Test
  public void testScalarRs() {doTest("$RS");}

  @Test
  public void testScalarOrd91() {doTest("$[");}

  @Test
  public void testScalarEffectiveUserId() {doTest("$EFFECTIVE_USER_ID");}

  @Test
  public void testScalarSubscriptSeparator() {doTest("$SUBSCRIPT_SEPARATOR");}

  @Test
  public void testScalarOrd92() {doTest("$\\");}

  @Test
  public void testScalarEgid() {doTest("$EGID");}

  @Test
  public void testScalarSubsep() {doTest("$SUBSEP");}

  @Test
  public void testScalarOrd93() {doTest("$]");}

  @Test
  public void testScalarErrno() {doTest("$ERRNO");}

  @Test
  public void testScalarSystemFdMax() {doTest("$SYSTEM_FD_MAX");}

  @Test
  public void testScalarEuid() {doTest("$EUID");}

  @Test
  public void testScalarUid() {doTest("$UID");}

  @Test
  public void testScalarCtrlA() {doTest("$^A");}

  @Test
  public void testScalarEvalError() {doTest("$EVAL_ERROR");}

  @Test
  public void testScalarWarning() {doTest("$WARNING");}

  @Test
  public void testScalarCtrlC() {doTest("$^C");}

  @Test
  public void testScalarExceptionsBeingCaught() {doTest("$EXCEPTIONS_BEING_CAUGHT");}

  @Test
  public void testScalarOrd124() {doTest("$|");}

  @Test
  public void testScalarCtrlChildErrorNative() {doTest("${^CHILD_ERROR_NATIVE}");}

  @Test
  public void testScalarExecutableName() {doTest("$EXECUTABLE_NAME");}

  @Test
  public void testScalarOrd126() {doTest("$~");}

  @Test
  public void testScalarCtrlD() {doTest("$^D");}

  @Test
  public void testScalarExtendedOsError() {doTest("$EXTENDED_OS_ERROR");}

  @Test
  public void testScalarCtrlE() {doTest("$^E");}

  @Test
  public void testScalarFormatFormfeed() {doTest("$FORMAT_FORMFEED");}

  @Test
  public void testScalarCtrlEncoding() {doTest("${^ENCODING}");}

  @Test
  public void testScalarFormatLineBreakCharacters() {doTest("$FORMAT_LINE_BREAK_CHARACTERS");}

  @Test
  public void testScalarCtrlF() {doTest("$^F");}

  @Test
  public void testScalarFormatLinesLeft() {doTest("$FORMAT_LINES_LEFT");}

  @Test
  public void testScalarCtrlH() {doTest("$^H");}

  @Test
  public void testScalarFormatLinesPerPage() {doTest("$FORMAT_LINES_PER_PAGE");}

  @Test
  public void testScalarCtrlI() {doTest("$^I");}

  @Test
  public void testScalarFormatName() {doTest("$FORMAT_NAME");}

  @Test
  public void testScalarCtrlL() {doTest("$^L");}

  @Test
  public void testScalarFormatPageNumber() {doTest("$FORMAT_PAGE_NUMBER");}

  @Test
  public void testScalarCtrlM() {doTest("$^M");}

  @Test
  public void testScalarFormatTopName() {doTest("$FORMAT_TOP_NAME");}

  @Test
  public void testScalarCtrlN() {doTest("$^N");}

  @Test
  public void testScalarGid() {doTest("$GID");}

  @Test
  public void testScalarCtrlO() {doTest("$^O");}

  @Test
  public void testScalarInplaceEdit() {doTest("$INPLACE_EDIT");}

  @Test
  public void testScalarCtrlOpen() {doTest("${^OPEN}");}

  @Test
  public void testScalarInputLineNumber() {doTest("$INPUT_LINE_NUMBER");}

  @Test
  public void testScalarCtrlP() {doTest("$^P");}

  @Test
  public void testScalarInputRecordSeparator() {doTest("$INPUT_RECORD_SEPARATOR");}

  @Test
  public void testScalarCtrlR() {doTest("$^R");}

  @Test
  public void testScalarCtrlReDebugFlags() {doTest("${^RE_DEBUG_FLAGS}");}

  @Test
  public void testScalarLastParenMatch() {doTest("$LAST_PAREN_MATCH");}

  @Test
  public void testArrayOrd43() {doTest("@+");}

  @Test
  public void testArrayOrd45() {doTest("@-");}

  @Test
  public void testArray() {doTest("@_");}

  @Test
  public void testArrayF() {doTest("@F");}

  @Test
  public void testArrayArg() {doTest("@ARG");}

  @Test
  public void testArrayLastMatchEnd() {doTest("@LAST_MATCH_END");}

  @Test
  public void testArrayArgv() {doTest("@ARGV");}

  @Test
  public void testArrayInc() {doTest("@INC");}

  @Test
  public void testArrayCtrlCapture() {doTest("@{^CAPTURE}");}

  @Test
  public void testArrayLastMatchStart() {doTest("@LAST_MATCH_START");}

  @Test
  public void testHashOrd33() {doTest("%!");}

  @Test
  public void testHashOrd43() {doTest("%+");}

  @Test
  public void testHashOrd45() {doTest("%-");}

  @Test
  public void testHashCtrlH() {doTest("%^H");}

  @Test
  public void testHashEnv() {doTest("%ENV");}

  @Test
  public void testHashInc() {doTest("%INC");}

  @Test
  public void testHashSig() {doTest("%SIG");}

  @Test
  public void testHashLastParenMatch() {doTest("%LAST_PAREN_MATCH");}

  @Test
  public void testHashCtrlCapture() {doTest("%{^CAPTURE}");}

  @Test
  public void testHashCtrlCaptureAll() {doTest("%{^CAPTURE_ALL}");}

  @Test
  public void testHashOsError() {doTest("%OS_ERROR");}

  @Test
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
