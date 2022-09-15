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

package completion;


import base.PerlCompletionPopupTestCase;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.idea.codeInsight.Perl5CodeInsightSettings;
import org.junit.Test;

public class PerlCompletionPopupTest extends PerlCompletionPopupTestCase {

  @Override
  protected String getBaseDataPath() {
    return "completionPopup/perl";
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
    doTestWithAutoColon("use Mojolicious<caret>", ":", true);
  }

  @Test
  public void testAutoColonUseDisabled() {
    doTestWithoutAutoColon("use Mojolicious<caret>", ":", false);
  }

  @Test
  public void testAutoColonUseDisabledCompletion() {
    doTestWithoutAutoColon("use Mojolicious:<caret>", ":", true);
  }

  @Test
  public void testScalarCtrl() {doTest("$<caret>", "^");}

  @Test
  public void testArrayCtrl() {doTest("@<caret>", "^");}

  @Test
  public void testHashCtrl() {doTest("%<caret>", "^");}

  @Test
  public void testUnicode() {doTest("qq|test<caret>test|", "\\N");}

  @Test
  public void testUnicodeBraces() {doTest("qx|test\\N<caret>test|", "{");}

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
    doTestWithAutoColon("CORE:<caret>", ":", true);
  }

  @Test
  public void testScalarColons() {doTestWithAutoColon("$<caret>", ":", true);}

  @Test
  public void testArrayColons() {doTestWithAutoColon("@<caret>", ":", true);}

  @Test
  public void testArraySizeColons() {doTestWithAutoColon("$#<caret>", ":", true);}

  @Test
  public void testHashColons() {doTestWithAutoColon("%<caret>", ":", true);}

  @Test
  public void testGlobColons() {doTestWithAutoColon("*<caret>", ":", true);}

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
  public void testNamespaceInScalar() {doTestPopupAfterCompletion("$My<caret>", "MyTest::Some::Package::", true);}

  @Test
  public void testNamespaceInArray() {doTestPopupAfterCompletion("@My<caret>", "MyTest::Some::Package::", true);}

  @Test
  public void testNamespaceInHash() {doTestPopupAfterCompletion("%$My<caret>", "MyTest::Some::Package::", true);}

  @Test
  public void testNamespaceInCode() {doTestPopupAfterCompletion("My<caret>", "MyTest::Some::Package", false);}

  @Test
  public void testSpaceBug() {doTestNegative("qw//<caret>", ";");}

  @Test
  public void testAfterPackage() {doTest("MyTest::Constants<caret>", "-");}

  @Test
  public void testAfterPackageWithSep() {doTest("MyTest::Constants::<caret>", "-");}

  @Test
  public void testAfterPackageDeref() {doTest("MyTest::Constants-><caret>", ">");}

  @Test
  public void testAfterPackageWithSepDeref() {doTest("MyTest::Constants::-><caret>", ">");}

  @Test
  public void testAfterPackageDerefAndComment() {doTest("MyTest::Constants-> # comment \n<caret>", ">");}

  @Test
  public void testAfterPackageWithSepDerefAndComment() {doTest("MyTest::Constants::-> # comment \n<caret>", ">");}

  @Test
  public void testAfterPackageWithComment() {doTest("MyTest::Constants #comment\n<caret>", "-");}

  @Test
  public void testAfterPackageWithSepWithComment() {doTest("MyTest::Constants:: #comment\n<caret>", "-");}
}
