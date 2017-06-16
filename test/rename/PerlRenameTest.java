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

package rename;

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.testFramework.UsefulTestCase;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 03.11.2016.
 */
public class PerlRenameTest extends PerlLightCodeInsightFixtureTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/rename/perl";
  }

  public void testExceptionClassAliasLocal() {doTest();}

  public void testExceptionClassAliasStatic() {doTest();}

  public void testExceptionClassAliasMethod() {doTest();}

  public void testConstantBare() {doTest();}

  public void testConstantSQ() {doTest();}

  public void testConstantDQ() {doTest();}

  public void testConstantQQ() {doTest();}

  public void testConstantQ() {doTest();}

  public void testConstantQW() {doTest();}

  public void testExceptionClassSQ() {doTest();}

  public void testExceptionClassDQ() {doTest();}

  public void testExceptionClassQW() {doTest();}

  public void testExceptionClassBare() {doTest();}

  public void testExceptionClassQ() {doTest();}

  public void testExceptionClassQQ() {doTest();}

  public void testHeredocBare() {doTest();}

  public void testHeredocDQ() {doTest();}

  public void testHeredocDQSpaced() {doTest();}

  public void testHeredocEscaped() {doTest();}

  public void testHeredocIndented() {doTest();}

  public void testHeredocSQ() {doTest();}

  public void testHeredocSQSpaced() {doTest();}

  public void testHeredocXQ() {doTest();}

  public void testHeredocXQSpaced() {doTest();}

  public void testGlobRename() {
    doTest();
  }

  public void testLexicalVariable() {
    doTest();
  }

  public void testMultiVariable() {
    doTest();
  }

  public void testLabelRename() {
    doTest();
  }

  public void testMojoHelper() {
    doTest("newName");
  }

  public void testPackageName() {
    doTest("Foo::Bar::Moo");
  }

  public void testMultiPackage() {
    doTest("Foo::Bar::Boo");
  }

  public void testPackageRanges() {
    doTest("Some::Other::Package");
  }

  protected void doTest() {
    doTest("NewName");
  }

  protected void doTest(@NotNull String newName) {
    initWithFileSmart();
    doRenameAtCaret(newName);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getFile().getText());
  }

  protected void doRenameAtCaret(@NotNull String newName) {
    myFixture.renameElementAtCaret(newName);
  }
}
