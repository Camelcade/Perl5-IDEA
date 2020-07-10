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

package unit.perl;


import base.PerlLightTestCase;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Objects;

public class PerlStubsTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/unit/perl/stubs";
  }

  @Test
  public void testFunctionParametersFun_pl() {doTest();}

  @Test
  public void testMojoliciousLite_pl() {doTest();}

  @Test
  public void testExceptionClass_pl() {doTest();}

  @Test
  public void testAugment_pl() {doTest();}

  @Test
  public void testDoRequire_pl() {doTest();}

  @Test
  public void testSubDeclaration_pl() {doTest();}

  @Test
  public void testSubDefinition_pl() {doTest();}

  @Test
  public void testMethodDefinition_pl() {doTest();}

  @Test
  public void testFuncDefinition_pl() {doTest();}

  @Test
  public void testClassAccessor_pl() {doTest();}

  @Test
  public void testMooseAttr_pl() {doTest();}

  @Test
  public void testConstants_pl() {doTest();}

  @Test
  public void testNamespace_pl() {
    doTest();
  }

  @Test
  public void testNamespace_pm() {
    doTest();
  }

  @Test
  public void testNamespaceDeprecated_pl() {
    doTest();
  }

  @Test
  public void testVariables_pl() {
    doTest();
  }

  @Test
  public void testGlobs_pl() {
    doTest();
  }

  @Test
  public void testNestedPodItems_pl() {doTest();}

  @Test
  public void testNestedPodItemsLong_pl() {doTest();}

  @Test
  public void testNestedPodSections_pl() {doTest();}

  /**
   * We are expecting this confirms sub stubs update on lazy element modification
   */
  @Test
  public void testSubDefinitionModification() {
    VirtualFile virtualFile = myFixture.copyFileToProject("subDefinitionModification.pl");
    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
    PsiFile psiFile = PsiManager.getInstance(getProject()).findFile(virtualFile);
    assertNotNull(psiFile);
    PsiDocumentManager documentManager = PsiDocumentManager.getInstance(getProject());
    Document document = documentManager.getDocument(psiFile);
    assertNotNull(document);
    WriteCommandAction.runWriteCommandAction(getProject(), () -> {
      document.insertString(101, "my $newvar = shift;");
      documentManager.commitDocument(document);
    });
    PsiElement leafElement = psiFile.findElementAt(101);
    assertNotNull(leafElement);
    PerlSubDefinitionElement subDefinitionElement = PsiTreeUtil.getParentOfType(leafElement, PerlSubDefinitionElement.class);
    assertNotNull(subDefinitionElement);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), Objects.requireNonNull(subDefinitionElement.getPresentableName()));
  }

  @Override
  protected @NotNull String computeAnswerFileName(@NotNull String appendix) {
    return getTestName(true).replace('_', '.') + ".txt";
  }

  private void doTest() {
    doTestStubs();
  }
}
