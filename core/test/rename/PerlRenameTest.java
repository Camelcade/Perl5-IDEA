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


public class PerlRenameTest extends PerlRenameTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/rename/perl";
  }

  public void testPackageTag() {doTest();}

  public void testAccessorModification() {doTest();}

  public void testMojoAttrs() {doTest();}

  public void testClassAccessorSimple() {doTest();}

  public void testClassAccessorSimpleRo() {doTest();}

  public void testClassAccessorSimpleWo() {doTest();}

  public void testExceptionClassFieldMethod() {doTest();}

  public void testExceptionClassFieldStatic() {doTest();}

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

  public void testPackageName() {
    doTest("Foo::Bar::Moo");
  }

  public void testMultiPackage() {
    doTest("Foo::Bar::Boo");
  }

  public void testPackageRanges() {
    doTest("Some::Other::Package");
  }

}
