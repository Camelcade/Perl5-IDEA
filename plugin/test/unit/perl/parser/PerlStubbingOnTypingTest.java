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

import base.PerlLightTestCase;
import categories.Performance;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.psi.PsiDocumentManager;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceIndex;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Ignore
@Category(Performance.class)
public class PerlStubbingOnTypingTest extends PerlLightTestCase {
  @Test
  public void testStubbingPerformance() {
    initWithPerlTidy();
    Editor editor = getEditor();
    editor.getCaretModel().moveToOffset(1133848);
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
    Logger.getInstance(PerlStubbingOnTypingTest.class).warn("Time elapsed: " + elapsed + "; per iteraton: " + (elapsed / iterations));
  }
}
