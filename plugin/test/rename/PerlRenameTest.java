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

package rename;


import base.PerlLightTestCase;

public class PerlRenameTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/rename/perl";
  }

  public void testPackageTag() {doTestRename();}

  public void testAccessorModification() {doTestRename();}

  public void testMojoAttrs() {doTestRename();}

  public void testClassAccessorSimple() {doTestRename();}

  public void testClassAccessorSimpleRo() {doTestRename();}

  public void testClassAccessorSimpleWo() {doTestRename();}

  public void testExceptionClassFieldMethod() {doTestRename();}

  public void testExceptionClassFieldStatic() {doTestRename();}

  public void testExceptionClassAliasLocal() {doTestRename();}

  public void testExceptionClassAliasStatic() {doTestRename();}

  public void testExceptionClassAliasMethod() {doTestRename();}

  public void testConstantBare() {doTestRename();}

  public void testConstantSQ() {doTestRename();}

  public void testConstantDQ() {doTestRename();}

  public void testConstantQQ() {doTestRename();}

  public void testConstantQ() {doTestRename();}

  public void testConstantQW() {doTestRename();}

  public void testExceptionClassSQ() {doTestRename();}

  public void testExceptionClassDQ() {doTestRename();}

  public void testExceptionClassQW() {doTestRename();}

  public void testExceptionClassBare() {doTestRename();}

  public void testExceptionClassQ() {doTestRename();}

  public void testExceptionClassQQ() {doTestRename();}

  public void testHeredocBare() {doTestRename();}

  public void testHeredocDQ() {doTestRename();}

  public void testHeredocDQSpaced() {doTestRename();}

  public void testHeredocEscaped() {doTestRename();}

  public void testHeredocIndented() {doTestRename();}

  public void testHeredocSQ() {doTestRename();}

  public void testHeredocSQSpaced() {doTestRename();}

  public void testHeredocXQ() {doTestRename();}

  public void testHeredocXQSpaced() {doTestRename();}

  public void testGlobRename() {
    doTestRename();
  }

  public void testLexicalVariable() {
    doTestRename();
  }

  public void testMultiVariable() {
    doTestRename();
  }

  public void testLabelRename() {
    doTestRename();
  }

  public void testPackageName() {
    doTestRename("Foo::Bar::Moo");
  }

  public void testMultiPackage() {
    doTestRename("Foo::Bar::Boo");
  }

  public void testPackageRanges() {
    doTestRename("Some::Other::Package");
  }

  public void testBrokenTemplate() {
    myFixture.copyFileToProject("TestFirst.pm");
    doTestRename("some_new_name");
  }
}
