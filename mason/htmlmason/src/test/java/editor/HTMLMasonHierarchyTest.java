package editor;

import base.HTMLMasonLightTestCase;
import org.junit.Test;

public class HTMLMasonHierarchyTest extends HTMLMasonLightTestCase {
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
    var otherComponentsFile = root.findFileByRelativePath("other_components");
    assertNotNull(otherComponentsFile);
    markAsComponentRoot(componentsFile);
    markAsComponentRoot(otherComponentsFile);
    var kidFile = otherComponentsFile.findFileByRelativePath("kid.mas");
    assertNotNull(kidFile);
    myFixture.openFileInEditor(kidFile);
    doTestTypeHierarchyWithoutInit(getFile());
  }

  @Test
  public void testAutohandlers() {
    var root = myFixture.copyDirectoryToProject(getTestName(true), "");
    assertNotNull(root);
    var componentsFile = root.findFileByRelativePath("components");
    assertNotNull(componentsFile);
    markAsComponentRoot(componentsFile);
    var kidFile = componentsFile.findFileByRelativePath("autohandler");
    assertNotNull(kidFile);
    myFixture.openFileInEditor(kidFile);
    doTestTypeHierarchyWithoutInit(getFile());
  }
}
