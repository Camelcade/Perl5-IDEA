package run;

import base.PerlPlatformTestCase;
import categories.Heavy;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.actions.PerlDeparseFileAction;
import com.perl5.lang.perl.idea.actions.PerlRegenerateXSubsAction;
import com.perl5.lang.perl.util.PerlSubUtil;
import com.perl5.lang.perl.xsubs.PerlXSubsState;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(Heavy.class)
public class PerlHeavyActionsTest extends PerlPlatformTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/run/actions";
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
    runActionWithTestEvent(new PerlRegenerateXSubsAction());

    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
    assertEmpty(PerlSubUtil.getSubDeclarations(getProject(), "Hash::StoredIterator::hash_get_iterator"));

    var moduleRoot = getModuleRoot();
    moduleRoot.refresh(false, false);
    var deparsedFile = moduleRoot.findFileByRelativePath(PerlXSubsState.DEPARSED_FILE_NAME);
    assertNotNull(deparsedFile);
    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
  }
}
