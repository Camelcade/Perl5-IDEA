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

package rename;


import base.PerlLightTestCase;
import com.intellij.openapi.ui.TestDialog;
import com.intellij.openapi.ui.TestDialogManager;
import org.junit.Test;

public class PerlRenameTest extends PerlLightTestCase {
  @Override
  protected void tearDown() throws Exception {
    try {
      TestDialogManager.setTestDialog(TestDialog.DEFAULT);
    }
    finally {
      super.tearDown();
    }
  }

  @Override
  protected String getBaseDataPath() {
    return "rename/perl";
  }

  @Test
  public void testFunctionParametersMethodModifiers() {
    TestDialogManager.setTestDialog(TestDialog.OK);
    doTestRename();
  }

  @Test
  public void testFunctionParametersMethodModifiersCurrent() {
    TestDialogManager.setTestDialog(TestDialog.NO);
    doTestRename();
  }

  @Test
  public void testFunctionParametersMethodModifiersSuper() {doTestRename();}

  @Test
  public void testPackageTag() {doTestRename();}

  @Test
  public void testAccessorModification() {doTestRename();}

  @Test
  public void testMojoAttrs() {doTestRename();}

  @Test
  public void testClassAccessorSimple() {doTestRename();}

  @Test
  public void testClassAccessorSimpleRo() {doTestRename();}

  @Test
  public void testClassAccessorSimpleWo() {doTestRename();}

  @Test
  public void testExceptionClassFieldMethod() {doTestRename();}

  @Test
  public void testExceptionClassFieldStatic() {doTestRename();}

  @Test
  public void testExceptionClassAliasLocal() {doTestRename();}

  @Test
  public void testExceptionClassAliasStatic() {doTestRename();}

  @Test
  public void testExceptionClassAliasMethod() {doTestRename();}

  @Test
  public void testConstantBare() {doTestRename();}

  @Test
  public void testConstantSQ() {doTestRename();}

  @Test
  public void testConstantDQ() {doTestRename();}

  @Test
  public void testConstantQQ() {doTestRename();}

  @Test
  public void testConstantQ() {doTestRename();}

  @Test
  public void testConstantQW() {doTestRename();}

  @Test
  public void testExceptionClassSQ() {doTestRename();}

  @Test
  public void testExceptionClassDQ() {doTestRename();}

  @Test
  public void testExceptionClassQW() {doTestRename();}

  @Test
  public void testExceptionClassBare() {doTestRename();}

  @Test
  public void testExceptionClassQ() {doTestRename();}

  @Test
  public void testExceptionClassQQ() {doTestRename();}

  @Test
  public void testHeredocBare() {doTestRename();}

  @Test
  public void testHeredocDQ() {doTestRename();}

  @Test
  public void testHeredocDQSpaced() {doTestRename();}

  @Test
  public void testHeredocEscaped() {doTestRename();}

  @Test
  public void testHeredocIndented() {doTestRename();}

  @Test
  public void testHeredocSQ() {doTestRename();}

  @Test
  public void testHeredocSQSpaced() {doTestRename();}

  @Test
  public void testHeredocXQ() {doTestRename();}

  @Test
  public void testHeredocXQSpaced() {doTestRename();}

  @Test
  public void testGlobRename() {
    doTestRename();
  }

  @Test
  public void testGlobPreviousRename() {
    doTestRename();
  }

  @Test
  public void testLexicalVariable() {
    doTestRename();
  }

  @Test
  public void testMultiVariable() {
    doTestRename();
  }

  @Test
  public void testLabelRename() {
    doTestRename();
  }

  @Test
  public void testPackageName() {
    doTestRename("Foo::Bar::Moo");
  }

  @Test
  public void testMultiPackage() {
    doTestRename("Foo::Bar::Boo");
  }

  @Test
  public void testPackageRanges() {
    doTestRename("Some::Other::Package");
  }

  @Test
  public void testBrokenTemplate() {
    myFixture.copyFileToProject("TestFirst.pm");
    doTestRename("some_new_name");
  }
}
