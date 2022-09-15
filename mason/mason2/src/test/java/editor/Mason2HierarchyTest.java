package editor;

import base.Mason2TopLevelComponentTestCase;
import org.junit.Test;

public class Mason2HierarchyTest extends Mason2TopLevelComponentTestCase {
  @Override
  protected String getBaseDataPath() {
    return "hierarchy";
  }

  @Test
  public void testSimple() {
    var root = myFixture.copyDirectoryToProject(getTestName(true), "");
    assertNotNull(root);
    var componentsFile = root.findFileByRelativePath("components");
    assertNotNull(componentsFile);
    markAsComponentRoot(componentsFile);
    var kidFile = componentsFile.findFileByRelativePath("Base.mp");
    assertNotNull(kidFile);
    myFixture.openFileInEditor(kidFile);
    doTestTypeHierarchyWithoutInit(myFixture.getElementAtCaret());
  }
}
