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
public class PerlSourcesParserTestComp extends PerlParserTestBase {
  public static final String DATA_PATH = "testDataHeavy/parser/perl5/comp";

  @Override
  protected String getTestDataPath() {
    return DATA_PATH;
  }

  public void testbproto() {
    doTest("bproto");
  }

  public void testcmdopt() {
    doTest("cmdopt");
  }

  public void testcolon() {
    doTest("colon");
  }

  public void testdecl() {
    doTest("decl");
  }

  public void testfinal_line_num() {
    doTest("final_line_num");
  }

  public void testfold() {
    doTest("fold");
  }

  public void testform_scope() {
    doTest("form_scope");
  }

  public void testhints() {
    doTest("hints");
  }

  public void testhints_aux() {
    doTest("hints_aux");
  }

  public void testline_aux() {
    doTest("line_aux");
  }

  public void testline_debug() {
    doTest("line_debug");
  }

  public void testmultiline() {
    doTest("multiline");
  }

  public void testopsubs() {
    doTest("opsubs");
  }

  public void testour() {
    doTest("our");
  }

  public void testpackage() {
    doTest("package");
  }

  public void testpackage_block() {
    doTest("package_block");
  }

  public void testparser() {
    doTest("parser");
  }

  public void testproto() {
    doTest("proto");
  }

  public void testredef() {
    doTest("redef");
  }

  public void testrequire() {
    doTest("require");
  }

  public void testretainedlines() {
    doTest("retainedlines");
  }

  public void testterm() {
    doTest("term");
  }

  public void testuproto() {
    doTest("uproto");
  }

  public void testuse() {
    doTest("use");
  }

  public void testutf() {
    doTest("utf");
  }
}
