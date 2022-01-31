package liveTemplates;

import org.jetbrains.annotations.NotNull;

public class TemplateToolkitLiveTemplateExpandingTest extends TemplateToolkitLiveTemplateTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/liveTemplates/expand";
  }

  @Override
  protected void doTest(@NotNull String templateId, @NotNull String fileName) {
    doLiveTemplateBulkTest(fileName, templateId);
  }
}
