/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package parserHeavy;

/**
 * Created by hurricup on 28.02.2016.
 */

import categories.Heavy;
import org.junit.experimental.categories.Category;
import parser.PerlParserTestBase;

@Category(Heavy.class)
public class PerlSourcesParserTestIo extends PerlParserTestBase {
  public static final String DATA_PATH = "testDataHeavy/parser/perl5/io";

  @Override
  protected String getTestDataPath() {
    return DATA_PATH;
  }

  public void testargv() {
    doTest("argv");
  }

  public void testbinmode() {
    doTest("binmode");
  }

  public void testbom() {
    doTest("bom");
  }

  public void testclosepid() {
    doTest("closepid");
  }

  public void testcrlf() {
    doTest("crlf");
  }

  public void testcrlf_through() {
    doTest("crlf_through");
  }

  public void testdata() {
    doTest("data");
  }

  public void testdefout() {
    doTest("defout");
  }

  public void testdup() {
    doTest("dup");
  }

  public void testeintr() {
    doTest("eintr");
  }

  public void testeintr_print() {
    doTest("eintr_print");
  }

  public void testerrno() {
    doTest("errno");
  }

  public void testerrnosig() {
    doTest("errnosig");
  }

  public void testfflush() {
    doTest("fflush");
  }

  public void testfs() {
    doTest("fs");
  }

  public void testinplace() {
    doTest("inplace");
  }

  public void testiofile() {
    doTest("iofile");
  }

  public void testiprefix() {
    doTest("iprefix");
  }

  public void testlayers() {
    doTest("layers");
  }

  public void testnargv() {
    doTest("nargv");
  }

  public void testopen() {
    doTest("open");
  }

  public void testopenpid() {
    doTest("openpid");
  }

  public void testperlio() {
    doTest("perlio");
  }

  public void testperlio_fail() {
    doTest("perlio_fail");
  }

  public void testperlio_leaks() {
    doTest("perlio_leaks");
  }

  public void testperlio_open() {
    doTest("perlio_open");
  }

  public void testpipe() {
    doTest("pipe");
  }

  public void testprint() {
    doTest("print");
  }

  public void testpvbm() {
    doTest("pvbm");
  }

  public void testread() {
    doTest("read");
  }

  public void testsay() {
    doTest("say");
  }

  public void testsem() {
    doTest("sem");
  }

  public void testsemctl() {
    doTest("semctl");
  }

  public void testshm() {
    doTest("shm");
  }

  public void testsocket() {
    doTest("socket");
  }

  public void testtell() {
    doTest("tell");
  }

  public void testthrough() {
    doTest("through");
  }

  public void testutf8() {
    doTest("utf8");
  }
}
