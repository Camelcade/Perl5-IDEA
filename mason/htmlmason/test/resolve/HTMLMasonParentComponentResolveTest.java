package resolve;

import base.HTMLMasonLightTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class HTMLMasonParentComponentResolveTest extends HTMLMasonLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/resolve/parentComponent";
  }

  @Test
  public void testSameRootAbsolutePath() {
    doTestSameRoot("subdir/kid.mas");
  }

  @Test
  public void testSameRootRelativePath() {
    doTestSameRoot("subdir/kid2.mas");
  }

  @Test
  public void testDifferentRootAbsolutePath() {
    doTestDifferentRoot("kid.mas");
  }

  @Test
  public void testDifferentRootRelativePath() {
    doTestDifferentRoot("kid2.mas");
  }

  private void doTestSameRoot(@NotNull String fileName) {
    var root = myFixture.copyDirectoryToProject("singleroot", "");
    assertNotNull(root);
    var components = root.findFileByRelativePath("components");
    assertNotNull(components);
    markAsComponentRoot(components);
    var fileToTest = components.findFileByRelativePath(fileName);
    assertNotNull(fileToTest);
    myFixture.openFileInEditor(fileToTest);
    checkSerializedReferencesWithFile();
  }

  private void doTestDifferentRoot(@NotNull String fileName) {
    var root = myFixture.copyDirectoryToProject("multiroot", "");
    assertNotNull(root);
    var components = root.findFileByRelativePath("components");
    assertNotNull(components);
    markAsComponentRoot(components);
    var components2 = root.findFileByRelativePath("other_components");
    assertNotNull(components2);
    markAsComponentRoot(components2);
    var fileToTest = components.findFileByRelativePath(fileName);
    assertNotNull(fileToTest);
    myFixture.openFileInEditor(fileToTest);
    checkSerializedReferencesWithFile();
  }
}
