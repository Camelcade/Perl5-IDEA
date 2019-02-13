/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package documentation;

import base.PerlLightTestCase;
import com.intellij.codeInsight.documentation.DocumentationManager;
import com.intellij.lang.Language;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.lang.documentation.DocumentationProviderEx;
import com.intellij.psi.PsiElement;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.PerlLanguage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PerlDocumentationTest extends PerlLightTestCase {

  @Override
  protected String getTestDataPath() {
    return "testData/documentation/perl";
  }

  public void testUnknownSectionWithContent() {doTest();}

  public void testSubDefinitionInline() {doTest();}

  public void testSubDefinitionUsageInline() {doTest();}

  public void testExternalSubUsagePod() {doTest();}

  public void testSubDefinitionCross() {doTest();}

  public void testNamespaceDefinitionInline() {doTest();}

  @NotNull
  protected Language getLanguage() {
    return PerlLanguage.INSTANCE;
  }

  @NotNull
  @Override
  protected String getResultsFileExtension() {
    return "txt";
  }

  private void doTest() {
    initWithFileSmartWithoutErrors();
    List<Integer> caretsOffsets = getAndRemoveCarets();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < caretsOffsets.size(); i++) {
      Integer caretOffset = caretsOffsets.get(i);
      if (caretsOffsets.size() > 1) {
        sb.append("---------------------- ").append("Caret #").append(i).append(" at: ").append(caretOffset)
          .append("-----------------------------\n");
      }
      getEditor().getCaretModel().moveToOffset(caretOffset);
      PsiElement elementAtCaret = getFile().getViewProvider().findElementAt(getEditor().getCaretModel().getOffset(), getLanguage());
      assertNotNull(elementAtCaret);
      DocumentationProvider documentationProvider = DocumentationManager.getProviderFromElement(elementAtCaret);
      assertInstanceOf(documentationProvider, DocumentationProviderEx.class);
      PsiElement targetElement = DocumentationManager.getInstance(getProject()).findTargetElement(getEditor(), getFile(), elementAtCaret);
      assertNotNull(targetElement);
      String generatedDoc = documentationProvider.generateDoc(targetElement, elementAtCaret);
      assertNotNull(generatedDoc);
      sb.append(generatedDoc).append("\n");
    }

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }
}
