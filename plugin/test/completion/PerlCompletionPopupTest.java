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

package completion;


import base.PerlCompletionPopupTestCase;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.idea.codeInsight.Perl5CodeInsightSettings;
import org.junit.Test;
public class PerlCompletionPopupTest extends PerlCompletionPopupTestCase {

  @Override
  protected String getBaseDataPath() {
    return "testData/completionPopup/perl";
  }

  @Override
  public String getFileExtension() {
    return PerlFileTypePackage.EXTENSION; // requires for package.. test
  }

  @Test
  public void testSmartCommaSequence() {
    Perl5CodeInsightSettings.getInstance().SMART_COMMA_SEQUENCE_TYPING = true;
    doTest(" ");
  }

  @Test
  public void testSmartCommaSequenceDisabled() {
    Perl5CodeInsightSettings.getInstance().SMART_COMMA_SEQUENCE_TYPING = false;
    doTestNegative(" ");
  }

  @Test
  public void testAutoColonUseEnabled() {
    Perl5CodeInsightSettings.getInstance().AUTO_INSERT_COLON = true;
    doTest("use Mojolicious<caret>", ":");
  }

  @Test
  public void testAutoColonUseDisabled() {
    Perl5CodeInsightSettings.getInstance().AUTO_INSERT_COLON = false;
    doTestNegative("use Mojolicious<caret>", ":");
  }

  @Test
  public void testScalarCtrl() {doTest("$<caret>", "^");}

  @Test
  public void testArrayCtrl() {doTest("@<caret>", "^");}

  @Test
  public void testHashCtrl() {doTest("%<caret>", "^");}

  @Test
  public void testScalarIndexCtrl() {doTest("$#<caret>", "^");}

  @Test
  public void testScalarCtrlBraces() {doTest("${<caret>}", "^");}

  @Test
  public void testArrayCtrlBraces() {doTest("@{<caret>}", "^");}

  @Test
  public void testHashCtrlBraces() {doTest("%{<caret>}", "^");}

  @Test
  public void testScalarIndexCtrlBraces() {doTest("$#{<caret>}", "^");}

  @Test
  public void testUse() {doTest("use<caret>", " ");}

  @Test
  public void testNo() {doTest("no<caret>", " ");}

  @Test
  public void testReturns() {doTest("#@returns<caret>", " ");}

  @Test
  public void testType() {doTest("#@type<caret>", " ");}

  @Test
  public void testPackage() {doTest("package<caret>", " ");}

  @Test
  public void testAnnotations() {doTest("#<caret>", "@");}

  @Test
  public void testObjectMethod() {
    doTest("UNIVERSAL-<caret>", ">");
  }

  @Test
  public void testStaticMethod() {
    doTest("CORE:<caret>", ":");
  }

  @Test
  public void testScalarName() {
    doTest("\n<caret>", "$");
  }

  @Test
  public void testArrayName() {
    doTest("\n<caret>", "@");
  }

  @Test
  public void testHashName() {
    doTest("\n<caret>", "%");
  }

  @Test
  public void testArrayIndexName() {
    doTest("\n<caret>", "$#");
  }

  @Test
  public void testDQOpen() {doTest("", "\"");}

  @Test
  public void testSQOpen() {doTest("", "'");}

  @Test
  public void testQOpen() {doTest("q<caret>", "/");}

  @Test
  public void testQQOpen() {doTest("qq<caret>", "/");}

  @Test
  public void testQWOpen() {doTest("qw<caret>", "/");}

  @Test
  public void testQWContinue() {doTest("qw(text1<caret>)", " ");}

  @Test
  public void testBracedScalar() {doTest("$<caret>", "{");}

  @Test
  public void testBracedArray() {doTest("@<caret>", "{");}

  @Test
  public void testBracedHash() {doTest("%<caret>", "{");}

  @Test
  public void testBracedArrayIndex() {doTest("$#<caret>", "{");}

  @Test
  public void testBracedGlob() {doTest("*<caret>", "{");}

  @Test
  public void testHashRefIndexOpen() {doTest("say $a->{something};say $b-><caret>", "{");}

  @Test
  public void testHashIndexOpen() {doTest("say $a->{something};say $b<caret>", "{");}

  @Test
  public void testHashGlobIndexOpen() {doTest("say $a->{something};say *b<caret>", "{");}

  @Test
  public void testSpaceBug() {doTestNegative("qw//<caret>", ";");}

}
