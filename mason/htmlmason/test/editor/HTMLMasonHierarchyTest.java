package editor;

import base.HTMLMasonLightTestCase;
import org.junit.Test;

public class HTMLMasonHierarchyTest extends HTMLMasonLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/hierarchy";
  }

  @Test
  public void testComponents() {
    var root = myFixture.copyDirectoryToProject("simple", "");
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
}
