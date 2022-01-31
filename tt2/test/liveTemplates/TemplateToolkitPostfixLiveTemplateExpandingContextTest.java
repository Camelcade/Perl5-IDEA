package liveTemplates;

import com.intellij.testFramework.LiveTemplateTestUtil;
import org.jetbrains.annotations.NotNull;

public class TemplateToolkitPostfixLiveTemplateExpandingContextTest extends TemplateToolkitPostfixLiveTemplateTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/liveTemplates/expandPostfixContext";
  }

  @Override
  protected void doTest(@NotNull String templateId, @NotNull String fileName) {
    initWithFileSmart(fileName);
    LiveTemplateTestUtil.doTestTemplateExpandingAvailability(myFixture, templateId, "Template Toolkit 2 Postfix", getTestResultsFilePath());
  }
}
