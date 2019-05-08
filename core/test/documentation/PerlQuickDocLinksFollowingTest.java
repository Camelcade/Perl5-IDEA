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

public class PerlQuickDocLinksFollowingTest extends PerlLightTestCase {
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerlPod();
  }

  @Override
  protected String getTestDataPath() {
    return "testData/documentation/perl/links";
  }

  public void testAbs() {doTest();}

  public void testAccept() {doTest();}

  public void testAlarm() {doTest();}

  public void testAtan2() {doTest();}

  public void testBind() {doTest();}

  public void testBinmode() {doTest();}

  public void testBless() {doTest();}

  public void testBreak() {doTest();}

  public void testCaller() {doTest();}

  public void testChdir() {doTest();}

  public void testChmod() {doTest();}

  public void testChomp() {doTest();}

  public void testChop() {doTest();}

  public void testChown() {doTest();}

  public void testChr() {doTest();}

  public void testChroot() {doTest();}

  public void testClose() {doTest();}

  public void testClosedir() {doTest();}

  public void testConnect() {doTest();}

  public void testContinue() {doTest();}

  public void testCos() {doTest();}

  public void testCrypt() {doTest();}

  public void testDbmclose() {doTest();}

  public void testDbmopen() {doTest();}

  public void testDefault() {doTest();}

  public void testDefined() {doTest();}

  public void testDelete() {doTest();}

  public void testDie() {doTest();}

  public void testDo() {doTest();}

  public void testDump() {doTest();}

  public void testEach() {doTest();}

  public void testEndgrent() {doTest();}

  public void testEndhostent() {doTest();}

  public void testEndnetent() {doTest();}

  public void testEndprotoent() {doTest();}

  public void testEndpwent() {doTest();}

  public void testEndservent() {doTest();}

  public void testEof() {doTest();}

  public void testEvalbytes() {doTest();}

  public void testEval() {doTest();}

  public void testExec() {doTest();}

  public void testExists() {doTest();}

  public void testExit() {doTest();}

  public void testExp() {doTest();}

  public void testFc() {doTest();}

  public void testFcntl() {doTest();}

  public void testFileno() {doTest();}

  public void testFlock() {doTest();}

  public void testFork() {doTest();}

  public void testFormat() {doTest();}

  public void testFormline() {doTest();}

  public void testGetc() {doTest();}

  public void testGetgrent() {doTest();}

  public void testGetgrgid() {doTest();}

  public void testGetgrnam() {doTest();}

  public void testGethostbyaddr() {doTest();}

  public void testGethostbyname() {doTest();}

  public void testGethostent() {doTest();}

  public void testGetlogin() {doTest();}

  public void testGetnetbyaddr() {doTest();}

  public void testGetnetbyname() {doTest();}

  public void testGetnetent() {doTest();}

  public void testGetpeername() {doTest();}

  public void testGetpgrp() {doTest();}

  public void testGetppid() {doTest();}

  public void testGetpriority() {doTest();}

  public void testGetprotobyname() {doTest();}

  public void testGetprotobynumber() {doTest();}

  public void testGetprotoent() {doTest();}

  public void testGetpwent() {doTest();}

  public void testGetpwnam() {doTest();}

  public void testGetpwuid() {doTest();}

  public void testGetservbyname() {doTest();}

  public void testGetservbyport() {doTest();}

  public void testGetservent() {doTest();}

  public void testGetsockname() {doTest();}

  public void testGetsockopt() {doTest();}

  public void testGlob() {doTest();}

  public void testGmtime() {doTest();}

  public void testGoto() {doTest();}

  public void testGrep() {doTest();}

  public void testHex() {doTest();}

  public void testImport() {doTest();}

  public void testIndex() {doTest();}

  public void testInt() {doTest();}

  public void testIoctl() {doTest();}

  public void testJoin() {doTest();}

  public void testKeys() {doTest();}

  public void testKill() {doTest();}

  public void testLast() {doTest();}

  public void testLc() {doTest();}

  public void testLcfirst() {doTest();}

  public void testLength() {doTest();}

  public void testLink() {doTest();}

  public void testListen() {doTest();}

  public void testLocal() {doTest();}

  public void testLocaltime() {doTest();}

  public void testLock() {doTest();}

  public void testLog() {doTest();}

  public void testLstat() {doTest();}

  public void testMap() {doTest();}

  public void testMkdir() {doTest();}

  public void testMsgctl() {doTest();}

  public void testMsgget() {doTest();}

  public void testMsgrcv() {doTest();}

  public void testMsgsnd() {doTest();}

  public void testMy() {doTest();}

  public void testNext() {doTest();}

  public void testOct() {doTest();}

  public void testOpen() {doTest();}

  public void testOpendir() {doTest();}

  public void testOrd() {doTest();}

  public void testPack() {doTest();}

  public void testPackage() {doTest();}

  public void testPipe() {doTest();}

  public void testPop() {doTest();}

  public void testPos() {doTest();}

  public void testPrint() {doTest();}

  public void testPrintf() {doTest();}

  public void testPrototype() {doTest();}

  public void testPush() {doTest();}

  public void testQuotemeta() {doTest();}

  public void testRand() {doTest();}

  public void testRead() {doTest();}

  public void testReaddir() {doTest();}

  public void testReadline() {doTest();}

  public void testReadlink() {doTest();}

  public void testReadpipe() {doTest();}

  public void testRecv() {doTest();}

  public void testRequire() {doTest();}

  public void testRedo() {doTest();}

  public void testRef() {doTest();}

  public void testRename() {doTest();}

  public void testReset() {doTest();}

  public void testReturn() {doTest();}

  public void testReverse() {doTest();}

  public void testRewinddir() {doTest();}

  public void testRindex() {doTest();}

  public void testRmdir() {doTest();}

  public void testSay() {doTest();}

  public void testScalar() {doTest();}

  public void testSeek() {doTest();}

  public void testSeekdir() {doTest();}

  public void testSelect() {doTest();}

  public void testSemctl() {doTest();}

  public void testSemget() {doTest();}

  public void testSemop() {doTest();}

  public void testSend() {doTest();}

  public void testSetgrent() {doTest();}

  public void testSethostent() {doTest();}

  public void testSetnetent() {doTest();}

  public void testSetpgrp() {doTest();}

  public void testSetpriority() {doTest();}

  public void testSetprotoent() {doTest();}

  public void testSetpwent() {doTest();}

  public void testSetservent() {doTest();}

  public void testSetsockopt() {doTest();}

  public void testShift() {doTest();}

  public void testShmctl() {doTest();}

  public void testShmget() {doTest();}

  public void testShmread() {doTest();}

  public void testShmwrite() {doTest();}

  public void testShutdown() {doTest();}

  public void testSin() {doTest();}

  public void testSleep() {doTest();}

  public void testSocket() {doTest();}

  public void testSocketpair() {doTest();}

  public void testSort() {doTest();}

  public void testSplice() {doTest();}

  public void testSplit() {doTest();}

  public void testSprintf() {doTest();}

  public void testSqrt() {doTest();}

  public void testSrand() {doTest();}

  public void testStat() {doTest();}

  public void testState() {doTest();}

  public void testStudy() {doTest();}

  public void testSub() {doTest();}

  public void testSubstr() {doTest();}

  public void testSymlink() {doTest();}

  public void testSyscall() {doTest();}

  public void testSysopen() {doTest();}

  public void testSysread() {doTest();}

  public void testSysseek() {doTest();}

  public void testSystem() {doTest();}

  public void testSyswrite() {doTest();}

  public void testTell() {doTest();}

  public void testTelldir() {doTest();}

  public void testTie() {doTest();}

  public void testTied() {doTest();}

  public void testTime() {doTest();}

  public void testTimes() {doTest();}

  public void testTruncate() {doTest();}

  public void testUc() {doTest();}

  public void testUcfirst() {doTest();}

  public void testUmask() {doTest();}

  public void testUndef() {doTest();}

  public void testUnlink() {doTest();}

  public void testUnpack() {doTest();}

  public void testUnshift() {doTest();}

  public void testUntie() {doTest();}

  public void testUse() {doTest();}

  public void testNo() {doTest();}

  public void testUtime() {doTest();}

  public void testValues() {doTest();}

  public void testVec() {doTest();}

  public void testWait() {doTest();}

  public void testWaitpid() {doTest();}

  public void testWantarray() {doTest();}

  public void testWarn() {doTest();}

  public void testWrite() {doTest();}

  public void testX() {doTest();}

  public void testFiletestLowerr() {doTest();}

  public void testFiletestLowerw() {doTest();}

  public void testFiletestLowerx() {doTest();}

  public void testFiletestLowero() {doTest();}

  public void testFiletestLowere() {doTest();}

  public void testFiletestLowerz() {doTest();}

  public void testFiletestLowers() {doTest();}

  public void testFiletestLowerf() {doTest();}

  public void testFiletestLowerd() {doTest();}

  public void testFiletestLowerl() {doTest();}

  public void testFiletestLowerp() {doTest();}

  public void testFiletestLowerb() {doTest();}

  public void testFiletestLowerc() {doTest();}

  public void testFiletestLowert() {doTest();}

  public void testFiletestLoweru() {doTest();}

  public void testFiletestLowerg() {doTest();}

  public void testFiletestLowerk() {doTest();}

  public void testFiletestUpperR() {doTest();}

  public void testFiletestUpperW() {doTest();}

  public void testFiletestUpperX() {doTest();}

  public void testFiletestUpperO() {doTest();}

  public void testFiletestUpperS() {doTest();}

  public void testFiletestUpperT() {doTest();}

  public void testFiletestUpperB() {doTest();}

  public void testFiletestUpperM() {doTest();}

  public void testFiletestUpperA() {doTest();}

  public void testFiletestUpperC() {doTest();}

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

  public void testDefaultScalar() {doTest("$_");}

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

  private void doTest(@NotNull String text) {
    initWithTextSmart(text + " ");
    getEditor().getCaretModel().moveToOffset(text.length() > 2 ? 2 : text.length() - 1);
    doTestDocumentationLinksWithoutInit();
  }

  private void doTest() {
    doTest(getBuiltInFromTestName());
  }
}
