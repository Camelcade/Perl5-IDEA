/*
 * Copyright 2015-2023 Alexandr Evstigneev
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
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Collection;
import java.util.List;

@Category(Heavy.class)
@RunWith(Parameterized.class)
@SuppressWarnings("Junit4RunWithInspection")
public class PerlQuickDocBuiltInTest extends PerlLightTestCase {

  static final String ANSWERS_PATH = "documentation/perl/quickdoc/builtin";

  @Parameter
  public @NotNull String myName;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerl528();
  }

  @Override
  protected String getBaseDataPath() {
    return ANSWERS_PATH;
  }

  @Override
  protected @NotNull String computeAnswerFileNameWithoutExtension(@NotNull String appendix) {
    return myName + appendix;
  }

  @Override
  protected @NotNull String getBuiltInFromTestName() {
    return getBuiltInFromName(myName);
  }

  @Test
  public void test() {doTest();}

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return BUILTINS.stream().map(n -> new Object[]{n}).toList();
  }

  private static final List<String> BUILTINS = List.of(
    "Abs","Accept","Alarm","Atan2","Bind","Binmode","Bless","Break","Caller","Chdir",
    "Chmod","Chomp","Chop","Chown","Chr","Chroot","Close","Closedir","Connect","Continue",
    "Cos","Crypt","Dbmclose","Dbmopen","Default","Defined","Delete","Die","Do","Dump",
    "Each","Endgrent","Endhostent","Endnetent","Endprotoent","Endpwent","Endservent",
    "Eof","Evalbytes","Eval","Exec","Exists","Exit","Exp","Fc","Fcntl","Fileno","Flock",
    "Fork","Format","Formline","Getc","Getgrent","Getgrgid","Getgrnam","Gethostbyaddr",
    "Gethostbyname","Gethostent","Getlogin","Getnetbyaddr","Getnetbyname","Getnetent",
    "Getpeername","Getpgrp","Getppid","Getpriority","Getprotobyname","Getprotobynumber",
    "Getprotoent","Getpwent","Getpwnam","Getpwuid","Getservbyname","Getservbyport",
    "Getservent","Getsockname","Getsockopt","Glob","Gmtime","Goto","Grep","Hex","Import",
    "Index","Int","Ioctl","Join","Keys","Kill","Last","Lc","Lcfirst","Length","Link",
    "Listen","Local","Localtime","Lock","Log","Lstat","Map","Mkdir","Msgctl","Msgget",
    "Msgrcv","Msgsnd","My","Next","Oct","Open","Opendir","Ord","Pack","Package","Pipe",
    "Pop","Pos","Print","Printf","Prototype","Push","Quotemeta","Rand","Read","Readdir",
    "Readline","Readlink","Readpipe","Recv","Require","Redo","Ref","Rename","Reset",
    "Return","Reverse","Rewinddir","Rindex","Rmdir","Say","Scalar","Seek","Seekdir",
    "Select","Semctl","Semget","Semop","Send","Setgrent","Sethostent","Setnetent",
    "Setpgrp","Setpriority","Setprotoent","Setpwent","Setservent","Setsockopt","Shift",
    "Shmctl","Shmget","Shmread","Shmwrite","Shutdown","Sin","Sleep","Socket","Socketpair",
    "Sort","Splice","Split","Sprintf","Sqrt","Srand","Stat","State","Study","Sub",
    "Substr","Symlink","Syscall","Sysopen","Sysread","Sysseek","System","Syswrite",
    "Tell","Telldir","Tie","Tied","Time","Times","Truncate","Uc","Ucfirst","Umask",
    "Undef","Unlink","Unpack","Unshift","Untie","Use","No","Utime","Values","Vec",
    "Wait","Waitpid","Wantarray","Warn","Write","X","FiletestLowerr","FiletestLowerw",
    "FiletestLowerx","FiletestLowero","FiletestLowere","FiletestLowerz","FiletestLowers",
    "FiletestLowerf","FiletestLowerd","FiletestLowerl","FiletestLowerp","FiletestLowerb",
    "FiletestLowerc","FiletestLowert","FiletestLoweru","FiletestLowerg","FiletestLowerk",
    "FiletestUpperR","FiletestUpperW","FiletestUpperX","FiletestUpperO","FiletestUpperS",
    "FiletestUpperT","FiletestUpperB","FiletestUpperM","FiletestUpperA","FiletestUpperC"
  );

  @Override
  protected @NotNull String getResultsFileExtension() {
    return "txt";
  }

  private void doTest() {
    initWithTextSmart(getBuiltInFromTestName() + " ");
    doTestQuickDocWithoutInit();
  }
}
