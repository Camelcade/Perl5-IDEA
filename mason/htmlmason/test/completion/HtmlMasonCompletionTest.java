package completion;

import base.HTMLMasonLightTestCase;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.Parameterized;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

@RunWith(Parameterized.class)
public class HtmlMasonCompletionTest extends HTMLMasonLightTestCase {

  private static final String COMPONENTS_ROOT_NAME = "components";
  private final @NotNull String mySampleName;

  @Override
  protected String getBaseDataPath() {
    return "testData/completion";
  }

  @org.junit.runners.Parameterized.Parameters(name = "{0}")
  public static Iterable<Object[]> fakeData() {
    return Collections.emptyList();
  }

  @com.intellij.testFramework.Parameterized.Parameters(name = "{0}")
  public static Iterable<Object[]> realData(Class<?> clazz) {
    return Arrays.asList(new Object[][]{
      {"include"},
      {"includeAbsolute"},
      {"includeBadSlug"},
      {"includeSelf"},
      {"includeParent"},
      {"includeRequest"},
      {"filter"},
      {"filterAbsolute"},
      {"filterBadSlug"},
      {"filterSelf"},
      {"filterParent"},
      {"filterRequest"},
      {"inherit"},
    });
  }

  public HtmlMasonCompletionTest(@NotNull String sampleName) {
    mySampleName = sampleName;
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    VirtualFile componentsRoot = myFixture.copyDirectoryToProject("testproject", COMPONENTS_ROOT_NAME);
    assertNotNull(componentsRoot);
    markAsComponentRoot(componentsRoot);
  }

  @Test
  public void testTop() {
    doTest("file.mas");
  }

  @Test
  public void testZero() {
    doTest("zero/file.mas");
  }

  @Test
  public void testZeroSubdir() {
    doTest("zero/subdir/file.mas");
  }

  @Test
  public void testZeroOtherdir() {
    doTest("zero/otherdir/file.mas");
  }

  @Test
  public void testZeroNonExistingDir() {
    doTest("zero/nonexistingdir/file.mas");
  }

  private void doTest(@NotNull String relativePath) {
    var testFile = myFixture.copyFileToProject(getSampleName() + ".code", FileUtil.join(COMPONENTS_ROOT_NAME, relativePath));
    assertNotNull(testFile);
    myFixture.configureFromExistingVirtualFile(testFile);
    doTestCompletionCheck("." + getSampleName(), null);
  }

  protected @NotNull String getSampleName() {
    return mySampleName;
  }
}
