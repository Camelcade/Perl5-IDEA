package liveTemplates;

import com.intellij.testFramework.LiveTemplateTestUtil;
import org.jetbrains.annotations.NotNull;

public class TemplateToolkitLiveTemplateExpandingContextTest extends TemplateToolkitLiveTemplateTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/liveTemplates/expandContext";
  }

  @Override
  protected void doTest(@NotNull String templateId, @NotNull String fileName) {
    initWithFileSmart(fileName);
    LiveTemplateTestUtil.doTestTemplateExpandingAvailability(myFixture, templateId, "Template Toolkit 2", getTestResultsFilePath());
  }
}
