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

  @Override
  protected boolean isFileTestTest() {
    return isFileTestName(myName);
  }

  @Test
  public void test() {doTest();}

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return BUILTINS.stream().map(n -> new Object[]{n}).toList();
  }

  private static final List<String> BUILTINS = List.of(
    "abs", "accept", "alarm", "atan2", "bind", "binmode", "bless", "break", "caller", "chdir", "chmod",
    "chomp", "chop", "chown", "chr", "chroot", "close", "closedir", "connect", "continue", "cos",
    "crypt", "dbmclose", "dbmopen", "default", "defined", "delete", "die", "do", "dump", "each",
    "endgrent", "endhostent", "endnetent", "endprotoent", "endpwent", "endservent", "eof", "eval",
    "evalbytes", "exec", "exists", "exit", "exp", "fc", "fcntl", "fileno", "filetestLowerb",
    "filetestLowerc", "filetestLowerd", "filetestLowere", "filetestLowerf", "filetestLowerg",
    "filetestLowerk", "filetestLowerl", "filetestLowero", "filetestLowerp", "filetestLowerr",
    "filetestLowers", "filetestLowert", "filetestLoweru", "filetestLowerw", "filetestLowerx",
    "filetestLowerz", "filetestUpperA", "filetestUpperB", "filetestUpperC", "filetestUpperM",
    "filetestUpperO", "filetestUpperR", "filetestUpperS", "filetestUpperT", "filetestUpperW",
    "filetestUpperX", "flock", "fork", "format", "formline", "getc", "getgrent", "getgrgid", "getgrnam",
    "gethostbyaddr", "gethostbyname", "gethostent", "getlogin", "getnetbyaddr", "getnetbyname",
    "getnetent", "getpeername", "getpgrp", "getppid", "getpriority", "getprotobyname",
    "getprotobynumber", "getprotoent", "getpwent", "getpwnam", "getpwuid", "getservbyname",
    "getservbyport", "getservent", "getsockname", "getsockopt", "glob", "gmtime", "goto", "grep", "hex",
    "import", "index", "int", "ioctl", "join", "keys", "kill", "last", "lc", "lcfirst", "length",
    "link", "listen", "local", "localtime", "lock", "log", "lstat", "map", "mkdir", "msgctl", "msgget",
    "msgrcv", "msgsnd", "my", "next", "no", "oct", "open", "opendir", "ord", "pack", "package", "pipe",
    "pop", "pos", "print", "printf", "prototype", "push", "quotemeta", "rand", "read", "readdir",
    "readline", "readlink", "readpipe", "recv", "redo", "ref", "rename", "require", "reset", "return",
    "reverse", "rewinddir", "rindex", "rmdir", "say", "scalar", "seek", "seekdir", "select", "semctl",
    "semget", "semop", "send", "setgrent", "sethostent", "setnetent", "setpgrp", "setpriority",
    "setprotoent", "setpwent", "setservent", "setsockopt", "shift", "shmctl", "shmget", "shmread",
    "shmwrite", "shutdown", "sin", "sleep", "socket", "socketpair", "sort", "splice", "split",
    "sprintf", "sqrt", "srand", "stat", "state", "study", "sub", "substr", "symlink", "syscall",
    "sysopen", "sysread", "sysseek", "system", "syswrite", "tell", "telldir", "tie", "tied", "time",
    "times", "truncate", "uc", "ucfirst", "umask", "undef", "unlink", "unpack", "unshift", "untie",
    "use", "utime", "values", "vec", "wait", "waitpid", "wantarray", "warn", "write", "x"
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
