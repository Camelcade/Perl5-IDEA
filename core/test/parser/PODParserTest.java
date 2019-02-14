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

package parser;

import com.perl5.lang.pod.PodParserDefinition;

/**
 * Created by hurricup on 24.03.2016.
 */
public class PODParserTest extends PerlParserTestBase {
  public PODParserTest() {
    super("", "pod", new PodParserDefinition());
  }

  @Override
  protected String getTestDataPath() {
    return "testData/parser/pod";
  }

  public void testPodWeaverTags() {doTest();}

  public void testUnknownSectionWIthContent() {doTest();}

  public void testHierarchy() {
    doTest();
  }

  public void testOverRecovery() {
    doTest(false);
  }

  public void testForBeginContent() {
    doTest(false);
  }

  public void testBeginRecovery() {
    doTest(false);
  }

  public void testPerl() {doTest();}

  public void testPerl5004delta() {doTest();}

  public void testPerl5005delta() {doTest();}

  public void testPerl5100delta() {doTest();}

  public void testPerl5101delta() {doTest();}

  public void testPerl5120delta() {doTest();}

  public void testPerl5121delta() {doTest();}

  public void testPerl5122delta() {doTest();}

  public void testPerl5123delta() {doTest();}

  public void testPerl5124delta() {doTest();}

  public void testPerl5125delta() {doTest();}

  public void testPerl5140delta() {doTest();}

  public void testPerl5141delta() {doTest();}

  public void testPerl5142delta() {doTest();}

  public void testPerl5143delta() {doTest();}

  public void testPerl5144delta() {doTest();}

  public void testPerl5160delta() {doTest();}

  public void testPerl5161delta() {doTest();}

  public void testPerl5162delta() {doTest();}

  public void testPerl5163delta() {doTest();}

  public void testPerl5180delta() {doTest();}

  public void testPerl5181delta() {doTest();}

  public void testPerl5182delta() {doTest();}

  public void testPerl5184delta() {doTest();}

  public void testPerl5200delta() {doTest();}

  public void testPerl5201delta() {doTest();}

  public void testPerl5202delta() {doTest();}

  public void testPerl5203delta() {doTest();}

  public void testPerl5220delta() {doTest();}

  public void testPerl5221delta() {doTest();}

  public void testPerl5222delta() {doTest();}

  public void testPerl5240delta() {doTest();}

  public void testPerl561delta() {doTest();}

  public void testPerl56delta() {doTest();}

  public void testPerl581delta() {doTest();}

  public void testPerl582delta() {doTest();}

  public void testPerl583delta() {doTest();}

  public void testPerl584delta() {doTest();}

  public void testPerl585delta() {doTest();}

  public void testPerl586delta() {doTest();}

  public void testPerl587delta() {doTest();}

  public void testPerl588delta() {doTest();}

  public void testPerl589delta() {doTest();}

  public void testPerl58delta() {doTest();}

  public void testPerlaix() {doTest();}

  public void testPerlamiga() {doTest();}

  public void testPerlandroid() {doTest();}

  public void testPerlapi() {doTest();}

  public void testPerlapio() {doTest();}

  public void testPerlartistic() {doTest();}

  public void testPerlbook() {doTest();}

  public void testPerlboot() {doTest();}

  public void testPerlbot() {doTest();}

  public void testPerlbs2000() {doTest();}

  public void testPerlcall() {doTest();}

  public void testPerlce() {doTest();}

  public void testPerlcheat() {doTest();}

  public void testPerlclib() {doTest();}

  public void testPerlcn() {doTest();}

  public void testPerlcommunity() {doTest();}

  public void testPerlcygwin() {doTest();}

  public void testPerldata() {doTest();}

  public void testPerldbmfilter() {doTest();}

  public void testPerldebguts() {doTest();}

  public void testPerldebtut() {doTest();}

  public void testPerldebug() {doTest();}

  public void testPerldelta() {doTest();}

  public void testPerldiag() {doTest();}

  public void testPerldoc() {doTest();}

  public void testPerldos() {doTest();}

  public void testPerldsc() {doTest();}

  public void testPerldtrace() {doTest();}

  public void testPerlebcdic() {doTest();}

  public void testPerlembed() {doTest();}

  public void testPerlexperiment() {doTest();}

  public void testPerlfaq() {doTest();}

  public void testPerlfaq1() {doTest();}

  public void testPerlfaq2() {doTest();}

  public void testPerlfaq3() {doTest();}

  public void testPerlfaq4() {doTest();}

  public void testPerlfaq5() {doTest();}

  public void testPerlfaq6() {doTest();}

  public void testPerlfaq7() {doTest();}

  public void testPerlfaq8() {doTest();}

  public void testPerlfaq9() {doTest();}

  public void testPerlfilter() {doTest();}

  public void testPerlfork() {doTest();}

  public void testPerlform() {doTest();}

  public void testPerlfreebsd() {doTest();}

  public void testPerlfunc() {doTest();}

  public void testPerlgit() {doTest();}

  public void testPerlglossary() {doTest();}

  public void testPerlgpl() {doTest();}

  public void testPerlguts() {doTest();}

  public void testPerlhack() {doTest();}

  public void testPerlhacktips() {doTest();}

  public void testPerlhacktut() {doTest();}

  public void testPerlhaiku() {doTest();}

  public void testPerlhist() {doTest();}

  public void testPerlhpux() {doTest();}

  public void testPerlhurd() {doTest();}

  public void testPerlintern() {doTest();}

  public void testPerlinterp() {doTest();}

  public void testPerlintro() {doTest();}

  public void testPerliol() {doTest();}

  public void testPerlipc() {doTest();}

  public void testPerlirix() {doTest();}

  public void testPerljp() {doTest();}

  public void testPerlko() {doTest();}

  public void testPerllexwarn() {doTest();}

  public void testPerllinux() {doTest();}

  public void testPerllocale() {doTest();}

  public void testPerllol() {doTest();}

  public void testPerlmacos() {doTest();}

  public void testPerlmacosx() {doTest();}

  public void testPerlmod() {doTest();}

  public void testPerlmodinstall() {doTest();}

  public void testPerlmodlib() {doTest();}

  public void testPerlmodstyle() {doTest();}

  public void testPerlmroapi() {doTest();}

  public void testPerlnetware() {doTest();}

  public void testPerlnewmod() {doTest();}

  public void testPerlnumber() {doTest();}

  public void testPerlobj() {doTest();}

  public void testPerlootut() {doTest();}

  public void testPerlop() {doTest();}

  public void testPerlopenbsd() {doTest();}

  public void testPerlopentut() {doTest();}

  public void testPerlos2() {doTest();}

  public void testPerlos390() {doTest();}

  public void testPerlos400() {doTest();}

  public void testPerlpacktut() {doTest();}

  public void testPerlperf() {doTest();}

  public void testPerlplan9() {doTest();}

  public void testPerlpod() {doTest();}

  public void testPerlpodspec() {doTest();}

  public void testPerlpodstyle() {doTest();}

  public void testPerlpolicy() {doTest();}

  public void testPerlport() {doTest();}

  public void testPerlpragma() {doTest();}

  public void testPerlqnx() {doTest();}

  public void testPerlre() {doTest();}

  public void testPerlreapi() {doTest();}

  public void testPerlrebackslash() {doTest();}

  public void testPerlrecharclass() {doTest();}

  public void testPerlref() {doTest();}

  public void testPerlreftut() {doTest();}

  public void testPerlreguts() {doTest();}

  public void testPerlrepository() {doTest();}

  public void testPerlrequick() {doTest();}

  public void testPerlreref() {doTest();}

  public void testPerlretut() {doTest();}

  public void testPerlriscos() {doTest();}

  public void testPerlrun() {doTest();}

  public void testPerlsec() {doTest();}

  public void testPerlsolaris() {doTest();}

  public void testPerlsource() {doTest();}

  public void testPerlstyle() {doTest();}

  public void testPerlsub() {doTest();}

  public void testPerlsymbian() {doTest();}

  public void testPerlsyn() {doTest();}

  public void testPerlsynology() {doTest();}

  public void testPerlthrtut() {doTest();}

  public void testPerltie() {doTest();}

  public void testPerltoc() {doTest();}

  public void testPerltodo() {doTest();}

  public void testPerltooc() {doTest();}

  public void testPerltoot() {doTest();}

  public void testPerltrap() {doTest();}

  public void testPerltru64() {doTest();}

  public void testPerltw() {doTest();}

  public void testPerlunicode() {doTest();}

  public void testPerlunicook() {doTest();}

  public void testPerlunifaq() {doTest();}

  public void testPerluniintro() {doTest();}

  public void testPerluniprops() {doTest();}

  public void testPerlunitut() {doTest();}

  public void testPerlutil() {doTest();}

  public void testPerlvar() {doTest();}

  public void testPerlvms() {doTest();}

  public void testPerlvos() {doTest();}

  public void testPerlwin32() {doTest();}

  public void testPerlxs() {doTest();}

  public void testPerlxstut() {doTest();}

  public void testPerlxstypemap() {doTest();}
}
