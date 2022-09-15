package resolve;

import base.HTMLMasonLightTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class HTMLMasonComponentResolveTest extends HTMLMasonLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "resolve/parentComponent";
  }

  @Test
  public void testSameRootAbsolutePath() {
    doTestSingleRoot("singleroot", "subdir/kid.mas");
  }

  @Test
  public void testSameRootRelativePath() {
    doTestSingleRoot("singleroot", "subdir/kid2.mas");
  }

  @Test
  public void testCrossLevel() {
    doTestSingleRoot("crosslevel", "zero/first.mas");
  }

  @Test
  public void testDifferentRootAbsolutePath() {
    doTestDifferentRoot("kid.mas");
  }

  @Test
  public void testDifferentRootRelativePath() {
    doTestDifferentRoot("kid2.mas");
  }

  private void doTestSingleRoot(@NotNull String testDataDir, @NotNull String fileName) {
    var root = myFixture.copyDirectoryToProject(testDataDir, "");
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
