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

package unit.perl.parser;

import categories.Performance;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.psi.PsiDocumentManager;
import com.perl5.lang.perl.lexer.PerlLexingContext;
import com.perl5.lang.perl.lexer.adapters.PerlMergingLexerAdapter;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceIndex;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Ignore
@Category(Performance.class)
public class PerlLexerPerformanceTest extends PerlParserTestBase {

  private static final int START_OFFSET = 1133848;

  @Ignore
  @Test
  public void testStubbingPerformanceInsideLeaf() {
    initWithPerlTidy();
    Editor editor = getEditor();
    CaretModel caretModel = editor.getCaretModel();
    caretModel.moveToOffset(START_OFFSET);
    WriteCommandAction.runWriteCommandAction(getProject(), () -> {
      EditorModificationUtil.insertStringAtCaret(editor, "test");
      caretModel.moveToOffset(START_OFFSET + 2);
      PsiDocumentManager.getInstance(getProject()).commitAllDocuments();
    });
    doTestTypingPerformance();
  }

  @Test
  public void testStubbingPerformanceWithPod() {
    initWithPerlTidy();
    Editor editor = getEditor();
    CaretModel caretModel = editor.getCaretModel();
    caretModel.moveToOffset(0);
    String insertion = "=pod\n\ntest\n\n=cut\n\n";
    WriteCommandAction.runWriteCommandAction(getProject(), () -> {
      EditorModificationUtil.insertStringAtCaret(editor, insertion);
      PsiDocumentManager.getInstance(getProject()).commitAllDocuments();
    });
    caretModel.moveToOffset(START_OFFSET + insertion.length());
    doTestTypingPerformance();
  }

  @Ignore
  @Test
  public void testStubbingPerformance() {
    initWithPerlTidy();
    Editor editor = getEditor();
    editor.getCaretModel().moveToOffset(1133848);
    PerlNamespaceIndex.getInstance().getAllNames(getProject());
    doTestTypingPerformance();
  }

  private void doTestTypingPerformance() {
    Editor editor = getEditor();
    long started = System.currentTimeMillis();
    Document document = editor.getDocument();
    PsiDocumentManager documentManager = PsiDocumentManager.getInstance(getProject());
    int iterations = 300;

    for (int i = 0; i < iterations; i++) {
      WriteCommandAction.runWriteCommandAction(getProject(), () -> {
        EditorModificationUtil.insertStringAtCaret(editor, "a");
        documentManager.commitDocument(document);
      });
      PerlNamespaceIndex.getInstance().getAllNames(getProject());
    }

    long elapsed = System.currentTimeMillis() - started;
    Logger.getInstance(PerlLexerPerformanceTest.class)
      .warn(getTestName(true) + ": time elapsed: " + elapsed + "; per iteraton: " + (elapsed / iterations));
  }

  @Test
  public void testPerlTidyLexing() {

    String testData = getPerlTidy();

    final int iterations = 300;

    Logger logger = Logger.getInstance(PerlLexerPerformanceTest.class);

    long start = System.currentTimeMillis();
    for (int i = 0; i < iterations; i++) {
      testLexing(testData);
    }
    long elapsed = System.currentTimeMillis() - start;
    logger.warn("Lexed in " + elapsed + "; per iteration: " + elapsed / iterations);
  }

  private void testLexing(String testData) {
    PerlLexingContext lexingContext = PerlLexingContext.create(getProject()).withEnforcedSublexing(true);
    PerlMergingLexerAdapter perlLexer = new PerlMergingLexerAdapter(lexingContext);
    perlLexer.start(testData, 0, testData.length(), 0);

    while (perlLexer.getTokenType() != null) {
      perlLexer.advance();
    }
  }
}
