package run;

import base.PerlPlatformTestCase;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.testFramework.PlatformTestUtil;
import com.intellij.testFramework.TestActionEvent;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.actions.PerlDeparseFileAction;
import org.junit.Test;

public class PerlHeavyActionsTest extends PerlPlatformTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/run/actions";
  }

  @Test
  public void testDeparseFile() {
    copyDirToModule("deparseFile");
    openModuleFileInEditor("./test.pl");
    var action = new PerlDeparseFileAction();
    var actionEvent = new TestActionEvent(action);
    action.update(actionEvent);
    var eventPresentation = actionEvent.getPresentation();
    assertTrue("Action is not visible: " + action, eventPresentation.isVisible());
    assertTrue("Action is not enabled: " + action, eventPresentation.isEnabled());
    action.actionPerformed(actionEvent);
    PlatformTestUtil.dispatchAllEventsInIdeEventQueue();
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
}
