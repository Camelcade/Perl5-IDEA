/*
 * Copyright 2015-2022 Alexandr Evstigneev
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
  public void testArrayDeclarationMy() { doTest(); }

  @Test
  public void testHashDeclarationMy() { doTest(); }

  @Test
  public void testScalarDeclarationFuncSignature() { doTest(); }

  @Test
  public void testScalarDeclarationLocal() { doTest(); }

  @Test
  public void testScalarDeclarationMethodSignature() { doTest(); }

  @Test
  public void testScalarDeclarationMy() { doTest(); }

  @Test
  public void testScalarDeclarationMyBlock() { doTest(); }

  @Test
  public void testScalarDeclarationMyCommentAfter() { doTest(); }

  @Test
  public void testScalarDeclarationMyExpr() { doTest(); }

  @Test
  public void testScalarDeclarationMyExprMulti() { doTest(); }

  @Test
  public void testScalarDeclarationMyExprTrailingComment() { doTest(); }

  @Test
  public void testScalarDeclarationMyExprWrong() { doTest(); }

  @Test
  public void testScalarDeclarationMyForeach() { doTest(); }

  @Test
  public void testScalarDeclarationMyNewLine() { doTest(); }

  @Test
  public void testScalarDeclarationMySecondAssignment() { doTest(); }

  @Test
  public void testScalarDeclarationMyThirdMultiline() { doTest(); }

  @Test
  public void testScalarDeclarationMyTrailingComment() { doTest(); }

  @Test
  public void testScalarDeclarationOur() { doTest(); }

  @Test
  public void testScalarDeclarationState() { doTest(); }

  @Test
  public void testScalarDeclarationSubSignature() { doTest(); }

  @Test
  public void testScalarDeclarationSubSignatureAsync() { doTest(); }

  @Test
  public void testScalarDeclarationSubSignatureMultiline() { doTest(); }

  @Test
  public void testFunctionParametersMethodModifiers() {
    TestDialogManager.setTestDialog(TestDialog.OK);
    doTest();
  }

  @Test
  public void testFunctionParametersMethodModifiersCurrent() {
    TestDialogManager.setTestDialog(TestDialog.NO);
    doTest();
  }

  @Test
  public void testFunctionParametersMethodModifiersSuper() { doTest(); }

  @Test
  public void testPackageTag() { doTest(); }

  @Test
  public void testAccessorModification() { doTest(); }

  @Test
  public void testMojoAttrs() { doTest(); }

  @Test
  public void testClassAccessorSimple() { doTest(); }

  @Test
  public void testClassAccessorSimpleRo() { doTest(); }

  @Test
  public void testClassAccessorSimpleWo() { doTest(); }

  @Test
  public void testExceptionClassFieldMethod() { doTest(); }

  @Test
  public void testExceptionClassFieldStatic() { doTest(); }

  @Test
  public void testExceptionClassAliasLocal() { doTest(); }

  @Test
  public void testExceptionClassAliasStatic() { doTest(); }

  @Test
  public void testExceptionClassAliasMethod() { doTest(); }

  @Test
  public void testConstantBare() { doTest(); }

  @Test
  public void testConstantSQ() { doTest(); }

  @Test
  public void testConstantDQ() { doTest(); }

  @Test
  public void testConstantQQ() { doTest(); }

  @Test
  public void testConstantQ() { doTest(); }

  @Test
  public void testConstantQW() { doTest(); }

  @Test
  public void testExceptionClassSQ() { doTest(); }

  @Test
  public void testExceptionClassDQ() { doTest(); }

  @Test
  public void testExceptionClassQW() { doTest(); }

  @Test
  public void testExceptionClassBare() { doTest(); }

  @Test
  public void testExceptionClassQ() { doTest(); }

  @Test
  public void testExceptionClassQQ() { doTest(); }

  @Test
  public void testHeredocBare() { doTest(); }

  @Test
  public void testHeredocDQ() { doTest(); }

  @Test
  public void testHeredocDQSpaced() { doTest(); }

  @Test
  public void testHeredocEscaped() { doTest(); }

  @Test
  public void testHeredocIndented() { doTest(); }

  @Test
  public void testHeredocSQ() { doTest(); }

  @Test
  public void testHeredocSQSpaced() { doTest(); }

  @Test
  public void testHeredocXQ() { doTest(); }

  @Test
  public void testHeredocXQSpaced() { doTest(); }

  @Test
  public void testGlobRename() {
    doTest();
  }

  @Test
  public void testGlobPreviousRename() {
    doTest();
  }

  @Test
  public void testLexicalVariable() {
    doTest();
  }

  @Test
  public void testMultiVariable() {
    doTest();
  }

  @Test
  public void testLabelRename() {
    doTest();
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

  private void doTest() {
    doTestRename();
  }
}
