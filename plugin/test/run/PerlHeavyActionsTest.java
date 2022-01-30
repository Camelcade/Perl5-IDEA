package run;

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
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.actions.PerlDeparseFileAction;
import com.perl5.lang.perl.idea.actions.PerlFormatWithPerlTidyAction;
import com.perl5.lang.perl.idea.actions.PerlRegenerateXSubsAction;
import com.perl5.lang.perl.idea.annotators.PerlCriticAnnotator;
import com.perl5.lang.perl.idea.annotators.PerlCriticErrorDescriptor;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.util.PerlSubUtil;
import com.perl5.lang.perl.xsubs.PerlXSubsState;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.List;

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
