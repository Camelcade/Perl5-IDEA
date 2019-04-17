/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

public class PerlQuickDocBuiltInTest extends PerlLightTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerlDoc();
  }

  @Override
  protected String getTestDataPath() {
    return "testData/documentation/perl/quickdoc/builtin";
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

  @NotNull
  @Override
  protected String getResultsFileExtension() {
    return "txt";
  }

  private void doTest() {
    initWithTextSmart(getBuiltInFromTestName() + " ");
    doTestQuickDocWithoutInit();
  }

  @NotNull
  private String getBuiltInFromTestName() {
    String name = getTestName(true);
    return name.startsWith("filetest") ? "-" + name.substring(name.length() - 1) : name;
  }
}
