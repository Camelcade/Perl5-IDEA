/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package run;

import base.PerlPlatformTestCase;
import com.intellij.lang.annotation.Annotation;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.fixtures.CodeInsightTestUtil;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.intellij.util.EnvironmentUtil;
import com.intellij.util.concurrency.Semaphore;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.actions.PerlDeparseFileAction;
import com.perl5.lang.perl.idea.actions.PerlFormatWithPerlTidyAction;
import com.perl5.lang.perl.idea.actions.PerlRegenerateXSubsAction;
import com.perl5.lang.perl.idea.annotators.PerlCriticAnnotator;
import com.perl5.lang.perl.idea.annotators.PerlCriticErrorDescriptor;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerAdapter;
import com.perl5.lang.perl.util.PerlSubUtil;
import com.perl5.lang.perl.xsubs.PerlXSubsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.terminal.TerminalProjectOptionsProvider;
import org.jetbrains.plugins.terminal.fixture.TestShellSession;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class PerlHeavyActionsTest extends PerlPlatformTestCase {
  @Override
  protected String getBaseDataPath() {
    return "run/actions";
  }

  @Test
  public void testDeparseFile() {
    copyDirToModule("deparseFile");
    openModuleFileInEditor("./test.pl");
    runActionWithTestEvent(new PerlDeparseFileAction());
    var allEditors = FileEditorManager.getInstance(getProject()).getAllEditors();
    assertSize(2, allEditors);
    var editorWithDeparsedFile = ContainerUtil.find(allEditors, it -> it.getFile() instanceof LightVirtualFile);
    assertNotNull(editorWithDeparsedFile);
    var deparsedFile = editorWithDeparsedFile.getFile();
    assertNotNull(deparsedFile);
    var deparsedDocument = FileDocumentManager.getInstance().getDocument(deparsedFile);
    assertNotNull(deparsedDocument);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(""), deparsedDocument.getText());
  }

  @Test
  public void testDeparseXSubsFile() {
    var notificationSink = createNotificationSink();
    rescanXSubsSynchronously();
    var notification = notificationSink.get();
    assertNotNull("Expected notification about not up to date xsubs", notification);
    assertEquals("Perl5 XSubs Deparser", notification.getGroupId());
    assertEquals("XSubs change detected", notification.getTitle());
    assertEquals("XSubs declarations file is absent or outdated.", notification.getContent());
    assertSize(1, notification.getActions());

    assertEmpty(PerlSubUtil.getSubDeclarations(getProject(), "Hash::StoredIterator::hash_get_iterator"));
    runActionWithTestEvent(new PerlRegenerateXSubsAction());

    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
    assertNotEmpty(PerlSubUtil.getSubDeclarations(getProject(), "Hash::StoredIterator::hash_get_iterator"));

    var deparsedFile = PerlXSubsState.getInstance(getProject()).getDeparsedSubsFile();
    assertNotNull(deparsedFile);

    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
    notificationSink.set(null);
    rescanXSubsSynchronously();
    assertNull("XSubs are expected be up to date, got notification: " + notification, notificationSink.get());
  }

  private void rescanXSubsSynchronously() {
    var xSubsState = PerlXSubsState.getInstance(getProject());
    var semaphore = new Semaphore(1);
    xSubsState.rescanFiles(semaphore::up);
    waitWithEventsDispatching("Rescanning hasn't finished in time", semaphore::isUp);
  }

  @Test
  public void testReformatWithPerlTidy() {
    copyDirToModule("formatWithPerlTidy");
    VirtualFile openedVirtualFile = openAndGetModuleFileInEditor("./test.pl");
    var openedDocument = FileDocumentManager.getInstance().getDocument(openedVirtualFile);
    assertNotNull(openedDocument);
    runActionWithTestEvent(new PerlFormatWithPerlTidyAction());
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(""), openedDocument.getText());
  }

  @Test
  public void testPerlCritic() {
    copyDirToModule("critic");
    VirtualFile openedVirtualFile = openAndGetModuleFileInEditor("./test.pl");
    var openedPsiFile = PsiManager.getInstance(getProject()).findFile(openedVirtualFile);
    assertNotNull(openedPsiFile);
    var annotator = new PerlCriticAnnotator();
    PerlSharedSettings.getInstance(getProject()).PERL_CRITIC_ENABLED = true;
    var information = annotator.collectInformation(openedPsiFile);
    assertNotNull(information);
    List<String> result = new ArrayList<>();
    var annotations = ProgressManager.getInstance().runProcessWithProgressSynchronously(
      () -> CodeInsightTestUtil.runExternalAnnotator(annotator, openedPsiFile, information, it -> {
        for (PerlCriticErrorDescriptor descriptor : it) {
          result.add(descriptor.toString());
        }
      }), "", false, null);
    assertNotNull(annotations);
    for (Annotation annotation : annotations) {
      result.add(annotation.getStartOffset() + "-" +
                 annotation.getEndOffset() + " " +
                 annotation.getSeverity() + " " +
                 annotation.getMessage());
    }
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(""), String.join("\n", result));
  }

  @Test
  public void testVersionManagerListInstalled() {
    PerlVersionManagerAdapter versionManagerAdapter = getVersionManagerAdapter();

    var installedDistributions = versionManagerAdapter.getInstalledDistributionsListInTests();
    assertContains(installedDistributions, Pattern.quote(PERL_TEST_VERSION));
    assertNotContains(installedDistributions, "[^\\w\\d-.@_]");
  }

  private @NotNull PerlVersionManagerAdapter getVersionManagerAdapter() {
    assumeNonSystemSdk();
    var versionManagerAdapter = getSdkAdditionalData().getVersionManagerAdapter();
    assertNotNull("No version manager adapter for " + getSdk(), versionManagerAdapter);
    return versionManagerAdapter;
  }


  @Test
  public void testVersionManagerListInstallable() {
    PerlVersionManagerAdapter versionManagerAdapter = getVersionManagerAdapter();

    var installedDistributions = versionManagerAdapter.getInstallableDistributionsListInTests();
    assertContains(installedDistributions, Pattern.quote(PERL_TEST_VERSION));
    assertContains(installedDistributions, Pattern.quote("5.12."));
    assertContains(installedDistributions, Pattern.quote("RC1"));
    assertNotContains(installedDistributions, "[^\\w\\d-. ]");
  }

  private void assertContains(@Nullable Collection<String> collection, @NotNull String regex) {
    assertNotNull(collection);
    var pattern = Pattern.compile(regex);
    for (String item : collection) {
      if (pattern.matcher(item).find()) {
        return;
      }
    }
    fail("Collection does not contain item matching: " + regex + "; collection:\n" + String.join("\n", collection));
  }

  private void assertNotContains(@Nullable Collection<String> collection, @NotNull String regex) {
    assertNotNull(collection);
    var pattern = Pattern.compile(regex);
    for (String item : collection) {
      if (pattern.matcher(item).find()) {
        fail("Found item: " + item + " matching " + regex + " in collection:\n" + String.join("\n", collection));
      }
    }
  }

  @Test
  public void testLocalTerminalConfiguration() {
    assumeLocalSdk();
    var terminalProjectOptionsProvider = TerminalProjectOptionsProvider.getInstance(getProject());
    assertNotNull("Terminal project options provider is null!", terminalProjectOptionsProvider);
    var currentShellPath = terminalProjectOptionsProvider.getShellPath();
    try {
      var shellName = StringUtil.notNullize(EnvironmentUtil.getValue(EnvironmentUtil.SHELL_VARIABLE_NAME), currentShellPath);
      LOG.debug("Using shell: " + shellName);
      terminalProjectOptionsProvider.setShellPath(shellName + " -l");

      var session = new TestShellSession(getProject(), getTestRootDisposable());
      session.executeCommand("perl -V");
      var controlLine = getSdkAdditionalData().getVersionManagerHandler().getControlOutputForPerlVersion(PERL_TEST_VERSION);
      assertNotNull(controlLine);
      session.awaitBufferCondition(() -> session.getScreenLines().contains(controlLine), MAX_PROCESS_WAIT_TIME_MS);
    }
    catch (IOException | ExecutionException e) {
      fail(e.getMessage());
    }
    finally {
      terminalProjectOptionsProvider.setShellPath(currentShellPath);
    }
  }
}
