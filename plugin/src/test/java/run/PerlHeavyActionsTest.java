package run;

import base.PerlInterpreterConfigurator;
import base.PerlPlatformTestCase;
import categories.Heavy;
import com.intellij.lang.annotation.Annotation;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.fixtures.CodeInsightTestUtil;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.intellij.util.concurrency.Semaphore;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.actions.PerlDeparseFileAction;
import com.perl5.lang.perl.idea.actions.PerlFormatWithPerlTidyAction;
import com.perl5.lang.perl.idea.actions.PerlRegenerateXSubsAction;
import com.perl5.lang.perl.idea.annotators.PerlCriticAnnotator;
import com.perl5.lang.perl.idea.annotators.PerlCriticErrorDescriptor;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.util.PerlSubUtil;
import com.perl5.lang.perl.xsubs.PerlXSubsState;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnconstructableJUnitTestCase")
@Category(Heavy.class)
public class PerlHeavyActionsTest extends PerlPlatformTestCase {
  public PerlHeavyActionsTest(@NotNull PerlInterpreterConfigurator interpreterConfigurator) {
    super(interpreterConfigurator);
  }

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
    assertEquals("XSubs Change Detected", notification.getTitle());
    assertEquals("XSubs declarations file is absent or outdated.", notification.getContent());
    assertSize(1, notification.getActions());

    runActionWithTestEvent(new PerlRegenerateXSubsAction());

    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
    assertEmpty(PerlSubUtil.getSubDeclarations(getProject(), "Hash::StoredIterator::hash_get_iterator"));

    var deparsedFileRoot = getProject().getBaseDir();
    deparsedFileRoot.refresh(false, false);
    var deparsedFile = deparsedFileRoot.findFileByRelativePath(PerlXSubsState.DEPARSED_FILE_NAME);
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
  @Ignore
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
    var annotations = CodeInsightTestUtil.runExternalAnnotator(annotator, openedPsiFile, information, it -> {
      for (PerlCriticErrorDescriptor descriptor : it) {
        result.add(descriptor.toString());
      }
    });
    for (Annotation annotation : annotations) {
      result.add(annotation.getStartOffset() + "-" +
                 annotation.getEndOffset() + " " +
                 annotation.getSeverity() + " " +
                 annotation.getMessage());
    }
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(""), String.join("\n", result));
  }
}
