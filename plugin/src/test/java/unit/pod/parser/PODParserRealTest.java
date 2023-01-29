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

package unit.pod.parser;

import com.intellij.openapi.util.io.FileUtil;
import org.junit.Test;
import unit.perl.parser.PerlParserTestBase;

public class PODParserRealTest extends PerlParserTestBase {
  public PODParserRealTest() {
    super("pod");
  }

  @Override
  protected String getBaseDataPath() {
    return "testLibSets/perldoc528/pod";
  }

  @Override
  protected String getResultsTestDataPath() {
    return FileUtil.join(TEST_RESOURCES_ROOT,  "unit/pod/parser/pod");
  }

  @Override
  protected String getRealDataFileExtension() {
    return ".pod";
  }

  @Test
  public void testPerl() {doTest();}

  @Test
  public void testPerl56delta() {doTest();}

  @Test
  public void testPerl58delta() {doTest();}

  @Test
  public void testPerl561delta() {doTest();}

  @Test
  public void testPerl581delta() {doTest();}

  @Test
  public void testPerl582delta() {doTest();}

  @Test
  public void testPerl583delta() {doTest();}

  @Test
  public void testPerl584delta() {doTest();}

  @Test
  public void testPerl585delta() {doTest();}

  @Test
  public void testPerl586delta() {doTest();}

  @Test
  public void testPerl587delta() {doTest();}

  @Test
  public void testPerl588delta() {doTest();}

  @Test
  public void testPerl589delta() {doTest();}

  @Test
  public void testPerl5004delta() {doTest();}

  @Test
  public void testPerl5005delta() {doTest();}

  @Test
  public void testPerl5100delta() {doTest();}

  @Test
  public void testPerl5101delta() {doTest();}

  @Test
  public void testPerl5120delta() {doTest();}

  @Test
  public void testPerl5121delta() {doTest();}

  @Test
  public void testPerl5122delta() {doTest();}

  @Test
  public void testPerl5123delta() {doTest();}

  @Test
  public void testPerl5124delta() {doTest();}

  @Test
  public void testPerl5125delta() {doTest();}

  @Test
  public void testPerl5140delta() {doTest();}

  @Test
  public void testPerl5141delta() {doTest();}

  @Test
  public void testPerl5142delta() {doTest();}

  @Test
  public void testPerl5143delta() {doTest();}

  @Test
  public void testPerl5144delta() {doTest();}

  @Test
  public void testPerl5160delta() {doTest();}

  @Test
  public void testPerl5161delta() {doTest();}

  @Test
  public void testPerl5162delta() {doTest();}

  @Test
  public void testPerl5163delta() {doTest();}

  @Test
  public void testPerl5180delta() {doTest();}

  @Test
  public void testPerl5181delta() {doTest();}

  @Test
  public void testPerl5182delta() {doTest();}

  @Test
  public void testPerl5184delta() {doTest();}

  @Test
  public void testPerl5200delta() {doTest();}

  @Test
  public void testPerl5201delta() {doTest();}

  @Test
  public void testPerl5202delta() {doTest();}

  @Test
  public void testPerl5203delta() {doTest();}

  @Test
  public void testPerl5220delta() {doTest();}

  @Test
  public void testPerl5221delta() {doTest();}

  @Test
  public void testPerl5222delta() {doTest();}

  @Test
  public void testPerl5223delta() {doTest();}

  @Test
  public void testPerl5224delta() {doTest();}

  @Test
  public void testPerl5240delta() {doTest();}

  @Test
  public void testPerl5241delta() {doTest();}

  @Test
  public void testPerl5242delta() {doTest();}

  @Test
  public void testPerl5243delta() {doTest();}

  @Test
  public void testPerl5244delta() {doTest();}

  @Test
  public void testPerl5260delta() {doTest();}

  @Test
  public void testPerl5261delta() {doTest();}

  @Test
  public void testPerl5262delta() {doTest();}

  @Test
  public void testPerl5280delta() {doTest();}

  @Test
  public void testPerlaix() {doTest();}

  @Test
  public void testPerlamiga() {doTest();}

  @Test
  public void testPerlandroid() {doTest();}

  @Test
  public void testPerlapi() {doTest();}

  @Test
  public void testPerlapio() {doTest();}

  @Test
  public void testPerlartistic() {doTest();}

  @Test
  public void testPerlbook() {doTest();}

  @Test
  public void testPerlboot() {doTest();}

  @Test
  public void testPerlbot() {doTest();}

  @Test
  public void testPerlbs2000() {doTest();}

  @Test
  public void testPerlcall() {doTest();}

  @Test
  public void testPerlce() {doTest();}

  @Test
  public void testPerlcheat() {doTest();}

  @Test
  public void testPerlclib() {doTest();}

  @Test
  public void testPerlcn() {doTest();}

  @Test
  public void testPerlcommunity() {doTest();}

  @Test
  public void testPerlcygwin() {doTest();}

  @Test
  public void testPerldata() {doTest();}

  @Test
  public void testPerldbmfilter() {doTest();}

  @Test
  public void testPerldebguts() {doTest();}

  @Test
  public void testPerldebtut() {doTest();}

  @Test
  public void testPerldebug() {doTest();}

  @Test
  public void testPerldelta() {doTest();}

  @Test
  public void testPerldeprecation() {doTest();}

  @Test
  public void testPerldiag() {doTest();}

  @Test
  public void testPerldoc() {doTest();}

  @Test
  public void testPerldos() {doTest();}

  @Test
  public void testPerldsc() {doTest();}

  @Test
  public void testPerldtrace() {doTest();}

  @Test
  public void testPerlebcdic() {doTest();}

  @Test
  public void testPerlembed() {doTest();}

  @Test
  public void testPerlexperiment() {doTest();}

  @Test
  public void testPerlfaq() {doTest();}

  @Test
  public void testPerlfaq1() {doTest();}

  @Test
  public void testPerlfaq2() {doTest();}

  @Test
  public void testPerlfaq3() {doTest();}

  @Test
  public void testPerlfaq4() {doTest();}

  @Test
  public void testPerlfaq5() {doTest();}

  @Test
  public void testPerlfaq6() {doTest();}

  @Test
  public void testPerlfaq7() {doTest();}

  @Test
  public void testPerlfaq8() {doTest();}

  @Test
  public void testPerlfaq9() {doTest();}

  @Test
  public void testPerlfilter() {doTest();}

  @Test
  public void testPerlfork() {doTest();}

  @Test
  public void testPerlform() {doTest();}

  @Test
  public void testPerlfreebsd() {doTest();}

  @Test
  public void testPerlfunc() {doTest();}

  @Test
  public void testPerlgit() {doTest();}

  @Test
  public void testPerlglossary() {doTest();}

  @Test
  public void testPerlgpl() {doTest();}

  @Test
  public void testPerlguts() {doTest();}

  @Test
  public void testPerlhack() {doTest();}

  @Test
  public void testPerlhacktips() {doTest();}

  @Test
  public void testPerlhacktut() {doTest();}

  @Test
  public void testPerlhaiku() {doTest();}

  @Test
  public void testPerlhist() {doTest();}

  @Test
  public void testPerlhpux() {doTest();}

  @Test
  public void testPerlhurd() {doTest();}

  @Test
  public void testPerlintern() {doTest();}

  @Test
  public void testPerlinterp() {doTest();}

  @Test
  public void testPerlintro() {doTest();}

  @Test
  public void testPerliol() {doTest();}

  @Test
  public void testPerlipc() {doTest();}

  @Test
  public void testPerlirix() {doTest();}

  @Test
  public void testPerljp() {doTest();}

  @Test
  public void testPerlko() {doTest();}

  @Test
  public void testPerllexwarn() {doTest();}

  @Test
  public void testPerllinux() {doTest();}

  @Test
  public void testPerllocale() {doTest();}

  @Test
  public void testPerllol() {doTest();}

  @Test
  public void testPerlmacos() {doTest();}

  @Test
  public void testPerlmacosx() {doTest();}

  @Test
  public void testPerlmod() {doTest();}

  @Test
  public void testPerlmodinstall() {doTest();}

  @Test
  public void testPerlmodlib() {doTest();}

  @Test
  public void testPerlmodstyle() {doTest();}

  @Test
  public void testPerlmroapi() {doTest();}

  @Test
  public void testPerlnetware() {doTest();}

  @Test
  public void testPerlnewmod() {doTest();}

  @Test
  public void testPerlnumber() {doTest();}

  @Test
  public void testPerlobj() {doTest();}

  @Test
  public void testPerlootut() {doTest();}

  @Test
  public void testPerlop() {doTest();}

  @Test
  public void testPerlopenbsd() {doTest();}

  @Test
  public void testPerlopentut() {doTest();}

  @Test
  public void testPerlos2() {doTest();}

  @Test
  public void testPerlos390() {doTest();}

  @Test
  public void testPerlos400() {doTest();}

  @Test
  public void testPerlpacktut() {doTest();}

  @Test
  public void testPerlperf() {doTest();}

  @Test
  public void testPerlplan9() {doTest();}

  @Test
  public void testPerlpod() {doTest();}

  @Test
  public void testPerlpodspec() {doTest();}

  @Test
  public void testPerlpodstyle() {doTest();}

  @Test
  public void testPerlpolicy() {doTest();}

  @Test
  public void testPerlport() {doTest();}

  @Test
  public void testPerlpragma() {doTest();}

  @Test
  public void testPerlqnx() {doTest();}

  @Test
  public void testPerlre() {doTest();}

  @Test
  public void testPerlreapi() {doTest();}

  @Test
  public void testPerlrebackslash() {doTest();}

  @Test
  public void testPerlrecharclass() {doTest();}

  @Test
  public void testPerlref() {doTest();}

  @Test
  public void testPerlreftut() {doTest();}

  @Test
  public void testPerlreguts() {doTest();}

  @Test
  public void testPerlrepository() {doTest();}

  @Test
  public void testPerlrequick() {doTest();}

  @Test
  public void testPerlreref() {doTest();}

  @Test
  public void testPerlretut() {doTest();}

  @Test
  public void testPerlriscos() {doTest();}

  @Test
  public void testPerlrun() {doTest();}

  @Test
  public void testPerlsec() {doTest();}

  @Test
  public void testPerlsolaris() {doTest();}

  @Test
  public void testPerlsource() {doTest();}

  @Test
  public void testPerlstyle() {doTest();}

  @Test
  public void testPerlsub() {doTest();}

  @Test
  public void testPerlsymbian() {doTest();}

  @Test
  public void testPerlsyn() {doTest();}

  @Test
  public void testPerlsynology() {doTest();}

  @Test
  public void testPerlthrtut() {doTest();}

  @Test
  public void testPerltie() {doTest();}

  @Test
  public void testPerltoc() {doTest();}

  @Test
  public void testPerltodo() {doTest();}

  @Test
  public void testPerltooc() {doTest();}

  @Test
  public void testPerltoot() {doTest();}

  @Test
  public void testPerltrap() {doTest();}

  @Test
  public void testPerltru64() {doTest();}

  @Test
  public void testPerltw() {doTest();}

  @Test
  public void testPerlunicode() {doTest();}

  @Test
  public void testPerlunicook() {doTest();}

  @Test
  public void testPerlunifaq() {doTest();}

  @Test
  public void testPerluniintro() {doTest();}

  @Test
  public void testPerluniprops() {doTest();}

  @Test
  public void testPerlunitut() {doTest();}

  @Test
  public void testPerlutil() {doTest();}

  @Test
  public void testPerlvar() {doTest();}

  @Test
  public void testPerlvms() {doTest();}

  @Test
  public void testPerlvos() {doTest();}

  @Test
  public void testPerlwin32() {doTest();}

  @Test
  public void testPerlxs() {doTest();}

  @Test
  public void testPerlxstut() {doTest();}

  @Test
  public void testPerlxstypemap() {doTest();}
}
