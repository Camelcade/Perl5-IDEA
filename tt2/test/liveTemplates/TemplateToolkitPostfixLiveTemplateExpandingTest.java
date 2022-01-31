package liveTemplates;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class TemplateToolkitPostfixLiveTemplateExpandingTest extends TemplateToolkitPostfixLiveTemplateTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/liveTemplates/expandPostfix";
  }

  @Test
  @Override
  public void testForeach() {
    doTest("fe", "liveTemplatesTestForeach");
  }

  @Override
  protected void doTest(@NotNull String templateId, @NotNull String fileName) {
    doLiveTemplateBulkTest(fileName, templateId);
  }
}
