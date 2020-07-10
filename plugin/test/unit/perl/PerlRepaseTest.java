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
import com.intellij.lang.ASTNode;
import com.intellij.lang.FileASTNode;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Couple;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.BlockSupportImpl;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.testFramework.UsefulTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class PerlRepaseTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/unit/perl/reparse";
  }

  @Test
  public void testAfterBody() {doTest("say 'hi';");}

  @Test
  public void testAroundBody() {doTest("say 'hi';");}

  @Test
  public void testAugmentBody() {doTest("say 'hi';");}

  @Test
  public void testBeforeBody() {doTest("say 'hi';");}

  @Test
  public void testFunBody() {doTest("say 'hi';");}

  @Test
  public void testFuncBody() {doTest("say 'hi';");}

  @Test
  public void testMethodBody() {doTest("say 'hi';");}

  @Test
  public void testOverrideBody() {doTest("say 'hi';");}

  @Test
  public void testSubAnonBody() {doTest("say 'hi';");}

  @Test
  public void testSubAnonBodyInner() {doTest("say 'hi';");}

  @Test
  public void testSubBodyInner() {doTest("say 'hi';");}

  @Test
  public void testSubBodyStatement() {doTestSub("say 'hi';");}

  @Test
  public void testSubBodyHeredoc() {doTestSub("say <<'SOMETHING';");}

  @Test
  public void testSubBodyOpenBrace() {doTestSub("{");}

  @Test
  public void testSubBodyCloseBrace() {doTestSub("}");}

  @Test
  public void testSubBodyRegexp() {doTestSub("m/");}

  private void doTestSub(@NotNull String textToInsert) {
    initWithFileSmart("subBody");
    doTestWithoutInit(textToInsert);
  }

  private void doTest(@NotNull String textToInsert) {
    initWithFileSmart();
    doTestWithoutInit(textToInsert);
  }

  private void doTestWithoutInit(@NotNull String textToInsert) {
    PsiFile psiFile = getFile();
    assertInstanceOf(psiFile, PsiFileImpl.class);
    FileASTNode fileNode = psiFile.getNode();
    Editor editor = getEditor();
    int offset = editor.getCaretModel().getOffset();
    CharSequence documentText = editor.getDocument().getCharsSequence();
    String newText = documentText.subSequence(0, offset) + textToInsert + documentText.subSequence(offset, documentText.length());
    Couple<ASTNode> roots =
      BlockSupportImpl.findReparseableRoots((PsiFileImpl)psiFile, fileNode, TextRange.create(offset, offset), newText);


    StringBuilder result = new StringBuilder(newText).append(SEPARATOR_NEWLINES);
    result.insert(offset + textToInsert.length(), "<caret>");

    if (roots == null) {
      result.append("Full reparse");
    }
    else {
      result.append(roots.first).append(SEPARATOR_NEWLINES);
      result.append(roots.first.getChars());
    }

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), result.toString());
  }
}
