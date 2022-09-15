/*
 * Copyright 2015-2020 Alexandr Evstigneev
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
import categories.Heavy;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(Heavy.class)
public class PerlQuickDocBuiltInCompletionTest extends PerlLightTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerlPod528();
  }

  @Override
  protected String getBaseDataPath() {
    return PerlQuickDocBuiltInTest.ANSWERS_PATH;
  }

  @Test
  public void testAbs() {doTest();}

  @Test
  public void testAccept() {doTest();}

  @Test
  public void testAlarm() {doTest();}

  @Test
  public void testAtan2() {doTest();}

  @Test
  public void testBind() {doTest();}

  @Test
  public void testBinmode() {doTest();}

  @Test
  public void testBless() {doTest();}

  @Test
  public void testBreak() {doTest();}

  @Test
  public void testCaller() {doTest();}

  @Test
  public void testChdir() {doTest();}

  @Test
  public void testChmod() {doTest();}

  @Test
  public void testChomp() {doTest();}

  @Test
  public void testChop() {doTest();}

  @Test
  public void testChown() {doTest();}

  @Test
  public void testChr() {doTest();}

  @Test
  public void testChroot() {doTest();}

  @Test
  public void testClose() {doTest();}

  @Test
  public void testClosedir() {doTest();}

  @Test
  public void testConnect() {doTest();}

  @Test
  public void testContinue() {doTest();}

  @Test
  public void testCos() {doTest();}

  @Test
  public void testCrypt() {doTest();}

  @Test
  public void testDbmclose() {doTest();}

  @Test
  public void testDbmopen() {doTest();}

  @Test
  public void testDefault() {doTest();}

  @Test
  public void testDefined() {doTest();}

  @Test
  public void testDelete() {doTest();}

  @Test
  public void testDie() {doTest();}

  @Test
  public void testDo() {doTest();}

  @Test
  public void testDump() {doTest();}

  @Test
  public void testEach() {doTest();}

  @Test
  public void testEndgrent() {doTest();}

  @Test
  public void testEndhostent() {doTest();}

  @Test
  public void testEndnetent() {doTest();}

  @Test
  public void testEndprotoent() {doTest();}

  @Test
  public void testEndpwent() {doTest();}

  @Test
  public void testEndservent() {doTest();}

  @Test
  public void testEof() {doTest();}

  @Test
  public void testEvalbytes() {doTest();}

  @Test
  public void testEval() {doTest();}

  @Test
  public void testExec() {doTest();}

  @Test
  public void testExists() {doTest();}

  @Test
  public void testExit() {doTest();}

  @Test
  public void testExp() {doTest();}

  @Test
  public void testFc() {doTest();}

  @Test
  public void testFcntl() {doTest();}

  @Test
  public void testFileno() {doTest();}

  @Test
  public void testFlock() {doTest();}

  @Test
  public void testFork() {doTest();}

  @Test
  public void testFormat() {doTest();}

  @Test
  public void testFormline() {doTest();}

  @Test
  public void testGetc() {doTest();}

  @Test
  public void testGetgrent() {doTest();}

  @Test
  public void testGetgrgid() {doTest();}

  @Test
  public void testGetgrnam() {doTest();}

  @Test
  public void testGethostbyaddr() {doTest();}

  @Test
  public void testGethostbyname() {doTest();}

  @Test
  public void testGethostent() {doTest();}

  @Test
  public void testGetlogin() {doTest();}

  @Test
  public void testGetnetbyaddr() {doTest();}

  @Test
  public void testGetnetbyname() {doTest();}

  @Test
  public void testGetnetent() {doTest();}

  @Test
  public void testGetpeername() {doTest();}

  @Test
  public void testGetpgrp() {doTest();}

  @Test
  public void testGetppid() {doTest();}

  @Test
  public void testGetpriority() {doTest();}

  @Test
  public void testGetprotobyname() {doTest();}

  @Test
  public void testGetprotobynumber() {doTest();}

  @Test
  public void testGetprotoent() {doTest();}

  @Test
  public void testGetpwent() {doTest();}

  @Test
  public void testGetpwnam() {doTest();}

  @Test
  public void testGetpwuid() {doTest();}

  @Test
  public void testGetservbyname() {doTest();}

  @Test
  public void testGetservbyport() {doTest();}

  @Test
  public void testGetservent() {doTest();}

  @Test
  public void testGetsockname() {doTest();}

  @Test
  public void testGetsockopt() {doTest();}

  @Test
  public void testGlob() {doTest();}

  @Test
  public void testGmtime() {doTest();}

  @Test
  public void testGoto() {doTest();}

  @Test
  public void testGrep() {doTest();}

  @Test
  public void testHex() {doTest();}

  @Test
  public void testImport() {doTest();}

  @Test
  public void testIndex() {doTest();}

  @Test
  public void testInt() {doTest();}

  @Test
  public void testIoctl() {doTest();}

  @Test
  public void testJoin() {doTest();}

  @Test
  public void testKeys() {doTest();}

  @Test
  public void testKill() {doTest();}

  @Test
  public void testLast() {doTest();}

  @Test
  public void testLc() {doTest();}

  @Test
  public void testLcfirst() {doTest();}

  @Test
  public void testLength() {doTest();}

  @Test
  public void testLink() {doTest();}

  @Test
  public void testListen() {doTest();}

  @Test
  public void testLocal() {doTest();}

  @Test
  public void testLocaltime() {doTest();}

  @Test
  public void testLock() {doTest();}

  @Test
  public void testLog() {doTest();}

  @Test
  public void testLstat() {doTest();}

  @Test
  public void testMap() {doTest();}

  @Test
  public void testMkdir() {doTest();}

  @Test
  public void testMsgctl() {doTest();}

  @Test
  public void testMsgget() {doTest();}

  @Test
  public void testMsgrcv() {doTest();}

  @Test
  public void testMsgsnd() {doTest();}

  @Test
  public void testMy() {doTest();}

  @Test
  public void testNext() {doTest();}

  @Test
  public void testOct() {doTest();}

  @Test
  public void testOpen() {doTest();}

  @Test
  public void testOpendir() {doTest();}

  @Test
  public void testOrd() {doTest();}

  @Test
  public void testPack() {doTest();}

  @Test
  public void testPackage() {doTest();}

  @Test
  public void testPipe() {doTest();}

  @Test
  public void testPop() {doTest();}

  @Test
  public void testPos() {doTest();}

  @Test
  public void testPrint() {doTest();}

  @Test
  public void testPrintf() {doTest();}

  @Test
  public void testPrototype() {doTest();}

  @Test
  public void testPush() {doTest();}

  @Test
  public void testQuotemeta() {doTest();}

  @Test
  public void testRand() {doTest();}

  @Test
  public void testRead() {doTest();}

  @Test
  public void testReaddir() {doTest();}

  @Test
  public void testReadline() {doTest();}

  @Test
  public void testReadlink() {doTest();}

  @Test
  public void testReadpipe() {doTest();}

  @Test
  public void testRecv() {doTest();}

  @Test
  public void testRequire() {doTest();}

  @Test
  public void testRedo() {doTest();}

  @Test
  public void testRef() {doTest();}

  @Test
  public void testRename() {doTest();}

  @Test
  public void testReset() {doTest();}

  @Test
  public void testReturn() {doTest();}

  @Test
  public void testReverse() {doTest();}

  @Test
  public void testRewinddir() {doTest();}

  @Test
  public void testRindex() {doTest();}

  @Test
  public void testRmdir() {doTest();}

  @Test
  public void testSay() {doTest();}

  @Test
  public void testScalar() {doTest();}

  @Test
  public void testSeek() {doTest();}

  @Test
  public void testSeekdir() {doTest();}

  @Test
  public void testSelect() {doTest();}

  @Test
  public void testSemctl() {doTest();}

  @Test
  public void testSemget() {doTest();}

  @Test
  public void testSemop() {doTest();}

  @Test
  public void testSend() {doTest();}

  @Test
  public void testSetgrent() {doTest();}

  @Test
  public void testSethostent() {doTest();}

  @Test
  public void testSetnetent() {doTest();}

  @Test
  public void testSetpgrp() {doTest();}

  @Test
  public void testSetpriority() {doTest();}

  @Test
  public void testSetprotoent() {doTest();}

  @Test
  public void testSetpwent() {doTest();}

  @Test
  public void testSetservent() {doTest();}

  @Test
  public void testSetsockopt() {doTest();}

  @Test
  public void testShift() {doTest();}

  @Test
  public void testShmctl() {doTest();}

  @Test
  public void testShmget() {doTest();}

  @Test
  public void testShmread() {doTest();}

  @Test
  public void testShmwrite() {doTest();}

  @Test
  public void testShutdown() {doTest();}

  @Test
  public void testSin() {doTest();}

  @Test
  public void testSleep() {doTest();}

  @Test
  public void testSocket() {doTest();}

  @Test
  public void testSocketpair() {doTest();}

  @Test
  public void testSort() {doTest();}

  @Test
  public void testSplice() {doTest();}

  @Test
  public void testSplit() {doTest();}

  @Test
  public void testSprintf() {doTest();}

  @Test
  public void testSqrt() {doTest();}

  @Test
  public void testSrand() {doTest();}

  @Test
  public void testStat() {doTest();}

  @Test
  public void testState() {doTest();}

  @Test
  public void testStudy() {doTest();}

  @Test
  public void testSubstr() {doTest();}

  @Test
  public void testSymlink() {doTest();}

  @Test
  public void testSyscall() {doTest();}

  @Test
  public void testSysopen() {doTest();}

  @Test
  public void testSysread() {doTest();}

  @Test
  public void testSysseek() {doTest();}

  @Test
  public void testSystem() {doTest();}

  @Test
  public void testSyswrite() {doTest();}

  @Test
  public void testTell() {doTest();}

  @Test
  public void testTelldir() {doTest();}

  @Test
  public void testTie() {doTest();}

  @Test
  public void testTied() {doTest();}

  @Test
  public void testTime() {doTest();}

  @Test
  public void testTimes() {doTest();}

  @Test
  public void testTruncate() {doTest();}

  @Test
  public void testUc() {doTest();}

  @Test
  public void testUcfirst() {doTest();}

  @Test
  public void testUmask() {doTest();}

  @Test
  public void testUndef() {doTest();}

  @Test
  public void testUnlink() {doTest();}

  @Test
  public void testUnpack() {doTest();}

  @Test
  public void testUnshift() {doTest();}

  @Test
  public void testUntie() {doTest();}

  @Test
  public void testUse() {doTest();}

  @Test
  public void testNo() {doTest();}

  @Test
  public void testUtime() {doTest();}

  @Test
  public void testValues() {doTest();}

  @Test
  public void testVec() {doTest();}

  @Test
  public void testWait() {doTest();}

  @Test
  public void testWaitpid() {doTest();}

  @Test
  public void testWantarray() {doTest();}

  @Test
  public void testWarn() {doTest();}

  @Test
  public void testWrite() {doTest();}

  @Ignore("This test result differs from the operators on place, bad test")
  @Test
  public void testX() {doTest();}

  @Test
  public void testFiletestLowerr() {doTest();}

  @Test
  public void testFiletestLowerw() {doTest();}

  @Test
  public void testFiletestLowerx() {doTest();}

  @Test
  public void testFiletestLowero() {doTest();}

  @Test
  public void testFiletestLowere() {doTest();}

  @Test
  public void testFiletestLowerz() {doTest();}

  @Test
  public void testFiletestLowers() {doTest();}

  @Test
  public void testFiletestLowerf() {doTest();}

  @Test
  public void testFiletestLowerd() {doTest();}

  @Test
  public void testFiletestLowerl() {doTest();}

  @Test
  public void testFiletestLowerp() {doTest();}

  @Test
  public void testFiletestLowerb() {doTest();}

  @Test
  public void testFiletestLowerc() {doTest();}

  @Test
  public void testFiletestLowert() {doTest();}

  @Test
  public void testFiletestLoweru() {doTest();}

  @Test
  public void testFiletestLowerg() {doTest();}

  @Test
  public void testFiletestLowerk() {doTest();}

  @Test
  public void testFiletestUpperR() {doTest();}

  @Test
  public void testFiletestUpperW() {doTest();}

  @Test
  public void testFiletestUpperX() {doTest();}

  @Test
  public void testFiletestUpperO() {doTest();}

  @Test
  public void testFiletestUpperS() {doTest();}

  @Test
  public void testFiletestUpperT() {doTest();}

  @Test
  public void testFiletestUpperB() {doTest();}

  @Test
  public void testFiletestUpperM() {doTest();}

  @Test
  public void testFiletestUpperA() {doTest();}

  @Test
  public void testFiletestUpperC() {doTest();}

  @Override
  protected @NotNull String getResultsFileExtension() {
    return "txt";
  }

  private void doTest() {
    initWithTextSmart("");
    doTestCompletionQuickDoc(getBuiltInFromTestName());
  }
}
