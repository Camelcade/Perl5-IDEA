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

package base;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.DebugUtil;
import com.intellij.psi.impl.source.tree.injected.InjectedFileViewProvider;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public abstract class PerlLightTestCaseBase extends LightCodeInsightFixtureTestCase {

  public abstract String getFileExtension();

  protected void testSmartKey(String original, char typed, String expected) {
    initWithTextSmart(original);
    myFixture.type(typed);
    myFixture.checkResult(expected);
  }

  public void initWithFileSmart() {
    initWithFileSmart(getTestName(true));
  }

  public void initWithFileSmart(String filename) {
    try {
      initWithFile(filename, getFileExtension());
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void initWithTextSmart(String content) {
    initWithFileContent("test", getFileExtension(), content);
  }

  public void initWithFileContent(String filename, String extension, String content) {
    myFixture.configureByText(filename + (extension.isEmpty() ? "" : "." + extension), content);
  }

  public void initWithFile(String filename, String extension) throws IOException {
    initWithFile(filename, extension, filename + (extension.isEmpty() ? "" : ".code"));
  }

  public void initWithFile(String targetFileName, String targetFileExtension, String sourceFileNameWithExtension) throws IOException {
    initWithFileContent(targetFileName, targetFileExtension,
                        FileUtil.loadFile(new File(getTestDataPath(), sourceFileNameWithExtension), CharsetToolkit.UTF8, true));
  }

  public void initWithFileSmartWithoutErrors() {
    initWithFileSmartWithoutErrors(getTestName(true));
  }

  public void initWithFileSmartWithoutErrors(@NotNull String filename) {
    initWithFileSmart(filename);
    assertNoErrorElements();
  }


  protected void assertNoErrorElements() {
    assertFalse(
      "PsiFile contains error elements:\n" + getFile().getText(),
      DebugUtil.psiToString(getFile(), true, false).contains("PsiErrorElement")
    );
  }


  protected void doFormatTest() {
    doFormatTest("");
  }

  protected void doFormatTest(@NotNull String answerSuffix) {
    doFormatTest(getTestName(true), answerSuffix);
  }

  protected void doFormatTest(@NotNull String filename, @NotNull String resultSuffix) {
    doFormatTest(filename, filename, resultSuffix);
  }

  protected void doFormatTest(@NotNull String sourceFileName, @NotNull String resultFileName, @NotNull String resultSuffix) {
    initWithFileSmartWithoutErrors(sourceFileName);
    doFormatTestWithoutInitialization(resultFileName, resultSuffix);
  }

  protected void doFormatTestWithoutInitialization(@NotNull String resultFileName, @NotNull String resultSuffix) {
    WriteCommandAction.writeCommandAction(getProject()).run(() -> {
      PsiFile file = myFixture.getFile();
      if (file.getViewProvider() instanceof InjectedFileViewProvider) {
        //noinspection ConstantConditions
        file = file.getContext().getContainingFile();
      }
      TextRange rangeToUse = file.getTextRange();
      CodeStyleManager.getInstance(getProject()).reformatText(file, rangeToUse.getStartOffset(), rangeToUse.getEndOffset());
    });

    String resultFilePath = getTestDataPath() + "/" + resultFileName + resultSuffix + ".txt";
    UsefulTestCase.assertSameLinesWithFile(resultFilePath, myFixture.getFile().getText());
    assertNoErrorElements();
  }

}
