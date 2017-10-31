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

package parser;

import base.PerlLightTestCase;
import categories.Performance;
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerEx;
import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerImpl;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.PsiModificationTrackerImpl;
import com.intellij.testFramework.PlatformTestUtil;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.intellij.util.ArrayUtil;
import org.junit.experimental.categories.Category;

import java.util.Collections;

/**
 * Created by hurricup on 07.10.2016.
 */
@Category(Performance.class)
public class PerlUXPerformanceTest extends PerlLightTestCase {
  public void testEnterTyping() {
    initWithPerlTidy();
    myFixture.getEditor().getCaretModel().moveToLogicalPosition(new LogicalPosition(65, 0));
    final int iterations = 30;
    for (int i = 0; i < iterations; i++) {
      myFixture.type("\n");
    }

    final int time = 1000;
    PlatformTestUtil.startPerformanceTest("PerlTidy enter typing", iterations * time, () -> {
      long start = System.currentTimeMillis();
      for (int i = 0; i < iterations; i++) {
        myFixture.type("\n");
      }
      long length = System.currentTimeMillis() - start;
      System.err.println("Typing enter done in " + length / iterations + " ms per iteration  of " + time);
    }).cpuBound().useLegacyScaling().assertTiming();
  }

  public void testHighlighting() {
    initWithPerlTidy();
    final PsiFile file = getFile();
    final Editor editor = getEditor();
    final Project project = getProject();
    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(project);
    final DaemonCodeAnalyzerImpl codeAnalyzer = (DaemonCodeAnalyzerImpl)DaemonCodeAnalyzer.getInstance(project);
    final TextEditor textEditor = TextEditorProvider.getInstance().getTextEditor(editor);
    DaemonCodeAnalyzerEx codeAnalyzerEx = DaemonCodeAnalyzerEx.getInstanceEx(project);

    final int iterations = 30;
    for (int i = 0; i < iterations; i++) {
      codeAnalyzer.restart();
      ((PsiModificationTrackerImpl)getPsiManager().getModificationTracker()).incCounter();
      codeAnalyzer.runPasses(file, editor.getDocument(), Collections.singletonList(textEditor), ArrayUtil.EMPTY_INT_ARRAY, false, null);
      codeAnalyzerEx.getFileLevelHighlights(project, file);
    }

    final int time = 1100;
    PlatformTestUtil.startPerformanceTest("PerlTidy highlighting", iterations * time, () -> {
      long start = System.currentTimeMillis();
      for (int i = 0; i < iterations; i++) {
        codeAnalyzer.restart();
        ((PsiModificationTrackerImpl)getPsiManager().getModificationTracker()).incCounter();
        codeAnalyzer.runPasses(file, editor.getDocument(), Collections.singletonList(textEditor), ArrayUtil.EMPTY_INT_ARRAY, false, null);
        DaemonCodeAnalyzerEx.getInstanceEx(project).getFileLevelHighlights(project, file);
      }
      long length = System.currentTimeMillis() - start;
      System.err.println("Highlighting done in " + length / iterations + " ms per iteration of " + time);
    }).cpuBound().useLegacyScaling().assertTiming();
  }
}
