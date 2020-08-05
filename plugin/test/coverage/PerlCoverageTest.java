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

package coverage;

import base.PerlLightTestCaseBase;
import base.PerlPlatformTestCase;
import categories.Heavy;
import com.intellij.coverage.CoverageDataManager;
import com.intellij.coverage.CoverageExecutor;
import com.intellij.coverage.CoverageSuite;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.rt.coverage.data.ClassData;
import com.intellij.rt.coverage.data.LineData;
import com.intellij.rt.coverage.data.ProjectData;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.idea.coverage.PerlCoverageSuite;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.pty4j.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Category(Heavy.class)
public class PerlCoverageTest extends PerlPlatformTestCase {
  private static final int MAX_RUNNING_TIME = 10_000;

  @Override
  protected String getBaseDataPath() {
    return "testData/coverage";
  }

  @Override
  protected String getResultsTestDataPath() {
    return super.getResultsTestDataPath() + "/answers";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    addPerlBrewSdk(getPerl526DistibutionId("test_coverage"));
  }


  @Test
  public void testCoverageRun() {
    runScriptWithCoverage("simple", "testscript.pl");
    compareCoverageSuiteWithFile();
  }

  private void compareCoverageSuiteWithFile() {
    CoverageDataManager coverageDataManager = CoverageDataManager.getInstance(getProject());
    CoverageSuite[] suites = coverageDataManager.getSuites();
    assertSize(1, suites);
    CoverageSuite coverageSuite = suites[0];
    assertInstanceOf(coverageSuite, PerlCoverageSuite.class);
    ProjectData projectData = coverageSuite.getCoverageData(coverageDataManager);
    assertNotNull(projectData);
    Map<String, ClassData> filesData = projectData.getClasses();
    List<String> filePaths = new ArrayList<>(filesData.keySet());
    filePaths.sort(Comparator.naturalOrder());
    StringBuilder sb = new StringBuilder();
    for (String filePath : filePaths) {
      ClassData fileData = filesData.get(filePath);
      assertNotNull(fileData);
      VirtualFile coveredFile = VfsUtil.findFileByIoFile(new File(filePath), true);
      assertNotNull(coveredFile);
      PsiFile coveredPsiFile = PsiManager.getInstance(getProject()).findFile(coveredFile);
      assertNotNull(coveredPsiFile);
      Document coveredDocument = PsiDocumentManager.getInstance(getProject()).getDocument(coveredPsiFile);
      assertNotNull(coveredDocument);
      sb.append("File: ").append(coveredFile.getName()).append(PerlLightTestCaseBase.SEPARATOR_NEWLINES);

      for (Object lineData : fileData.getLines()) {
        if (lineData == null) {
          continue;
        }
        assertInstanceOf(lineData, LineData.class);
        int status = ((LineData)lineData).getStatus();
        int lineNumber = ((LineData)lineData).getLineNumber() - 1;
        int offset = coveredDocument.getLineStartOffset(lineNumber);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> coveredDocument.insertString(offset, status + " | "));
      }
      sb.append(coveredDocument.getText()).append(PerlLightTestCaseBase.SEPARATOR_NEWLINES);
    }
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(""), sb.toString());
  }

  private void runScriptWithCoverage(@NotNull String directory, @NotNull String script) {
    copyDirToModule(directory);
    GenericPerlRunConfiguration runConfiguration = createRunConfiguration(script);
    Pair<ExecutionEnvironment, RunContentDescriptor> pair = null;
    try {
      pair = executeConfiguration(runConfiguration, CoverageExecutor.EXECUTOR_ID);
    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    RunContentDescriptor contentDescriptor = pair.second;
    ProcessHandler processHandler = contentDescriptor.getProcessHandler();
    assertNotNull(processHandler);
    if (!processHandler.waitFor(MAX_RUNNING_TIME)) {
      fail("Process hasn't finished in " + MAX_RUNNING_TIME);
    }
    @SuppressWarnings("ConstantConditions")
    int exitCode = processHandler.getExitCode();
    assertEquals("Wrong exit code: " + exitCode, 0, exitCode);
  }
}
