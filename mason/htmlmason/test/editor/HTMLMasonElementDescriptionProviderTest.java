package editor;

import base.HTMLMasonLightTestCase;
import org.junit.Test;

public class HTMLMasonElementDescriptionProviderTest extends HTMLMasonLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/descriptionProvider";
  }

  @Test
  public void testSubcomponent() {
    doTest();
  }

  @Test
  public void testMethod() {
    doTest();
  }

  private void doTest() {
    var targetFile =
      myFixture.copyFileToProject(getTestName(true) + ".code", "components/subdir/" + getTestName(true) + "." + getFileExtension());
    assertNotNull(targetFile);
    markAsComponentRoot(targetFile.getParent().getParent());
    myFixture.configureFromExistingVirtualFile(targetFile);
    doElementDescriptionTestWithoutInit();
  }
}
