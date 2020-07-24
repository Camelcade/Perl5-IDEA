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

package parser;

import base.PerlLightTestCaseBase;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.LineColumn;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.psi.impl.DebugUtil;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageManagerImpl;
import com.intellij.testFramework.EditorTestUtil;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.intellij.testFramework.ParsingTestCase.*;
import static java.util.Objects.requireNonNull;


public abstract class PerlParserTestBase extends PerlLightTestCaseBase {
  private boolean mySkipSpaces = true;

  private static final List<String> REPLACES = Arrays.asList(
    EditorTestUtil.SELECTION_START_TAG,
    EditorTestUtil.SELECTION_END_TAG,
    EditorTestUtil.BLOCK_SELECTION_START_TAG,
    EditorTestUtil.BLOCK_SELECTION_END_TAG
  );
  private static final List<String> REPLACEMENTS = ContainerUtil.map(REPLACES, it -> it.replace(it.charAt(1), '_'));

  @Override
  protected void tearDown() throws Exception {
    try {
      PsiFile file = getFile();
      if (file != null) {
        InjectedLanguageManagerImpl.getInstanceImpl(getProject()).dropFileCaches(file);
        WriteAction.run(file::delete);
      }
    }
    finally {
      super.tearDown();
    }
  }

  protected @NotNull String myFileExt;
  public PerlParserTestBase() {
    this(PerlFileTypeScript.EXTENSION_PL);
  }

  public PerlParserTestBase(@NotNull String fileExt) {
    myFileExt = fileExt;
  }

  @Override
  public String getFileExtension() {
    return myFileExt;
  }

  protected boolean allTreesInSingleFile() {
    return true;
  }

  protected boolean checkAllPsiRoots() {
    return true;
  }

  @Override
  public void initWithFileContent(String filename, String extension, String content) {
    super.initWithFileContent(filename, extension, StringUtil.replace(content, REPLACES, REPLACEMENTS).trim());
  }

  protected boolean includeRanges() {
    return false;
  }

  protected void doTest(boolean ensureNoErrorElements) {
    initWithFileSmart();
    doTestWithoutInit(ensureNoErrorElements);
  }

  protected void doTestWithTyping(@NotNull String toType) {
    initWithFileSmart();
    myFixture.type(toType);
    WriteAction.run(() -> PsiDocumentManager.getInstance(getProject()).commitAllDocuments());
    doTestWithoutInit(true);
  }

  protected void doTestWithoutInit(boolean ensureNoErrorElements) {
    PsiFile psiFile = getFile();
    String text = psiFile.getText();

    ensureParsed(psiFile);
    assertEquals("doc text mismatch", text, requireNonNull(psiFile.getViewProvider().getDocument()).getText());
    ensureCorrectReparse(psiFile);
    doCheckResult(getAnswersDataPath(), psiFile, checkAllPsiRoots(), getTestName(true), skipSpaces(), includeRanges(),
                  allTreesInSingleFile());
    if (ensureNoErrorElements) {
      ensureNoErrorElements();
    }
  }

  protected String getAnswersDataPath() {
    return getTestDataPath();
  }

  protected void ensureNoErrorElements() {
    getFile().accept(new PsiRecursiveElementVisitor() {
      private static final int TAB_WIDTH = 8;

      @Override
      public void visitErrorElement(@NotNull PsiErrorElement element) {
        // Very dump approach since a corresponding Document is not available.
        String text = getFile().getText();
        String[] lines = StringUtil.splitByLinesKeepSeparators(text);

        int offset = element.getTextOffset();
        LineColumn position = StringUtil.offsetToLineColumn(text, offset);
        int lineNumber = position != null ? position.line : -1;
        int column = position != null ? position.column : 0;

        String line = StringUtil.trimTrailing(lines[lineNumber]);
        // Sanitize: expand indentation tabs, replace the rest with a single space
        int numIndentTabs = StringUtil.countChars(line.subSequence(0, column), '\t', 0, true);
        int indentedColumn = column + numIndentTabs * (TAB_WIDTH - 1);
        String lineWithNoTabs = StringUtil.repeat(" ", numIndentTabs * TAB_WIDTH) + line.substring(numIndentTabs).replace('\t', ' ');
        String errorUnderline = StringUtil.repeat(" ", indentedColumn) + StringUtil.repeat("^", Math.max(1, element.getTextLength()));

        fail(String.format("Unexpected error element: %s:%d:%d\n\n%s\n%s\n%s",
                           getFile().getName(), lineNumber + 1, column,
                           lineWithNoTabs, errorUnderline, element.getErrorDescription()));
      }
    });
  }

  protected void doCheckErrors() {
    assertFalse(
      "PsiFile contains error elements",
      DebugUtil.psiToString(getFile(), skipSpaces(), includeRanges()).contains("PsiErrorElement")
    );
  }

  @Deprecated // this is legacy for heavy tests
  public void doTest(String name) {
    doTest(true);
  }

  @Deprecated // this is legacy for heavy tests
  public void doTest(String name, boolean check) {
    doTest(check);
  }

  public void doTest() {
    doTest(true);
  }

  protected boolean skipSpaces() {
    return mySkipSpaces;
  }

  protected void setSkipSpaces(boolean skipSpaces) {
    mySkipSpaces = skipSpaces;
  }

  protected String getPerlTidy() {
    try {
      return FileUtil.loadFile(new File("testData", "perlTidy.code"), CharsetToolkit.UTF8, true).trim();
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
