package liveTemplates;

import base.TemplateToolkitLightTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public abstract class TemplateToolkitPostfixLiveTemplateTestCase extends TemplateToolkitLightTestCase {
  @Override
  protected void setUp() throws Exception {
    enableLiveTemplatesTesting();
    super.setUp();
  }

  @Test
  public void testIf() { doTest("if"); }

  @Test
  public void testUnless() { doTest("un"); }

  @Test
  public void testFilter() { doTest("fi"); }

  @Test
  public void testForeach() { doTest("fe"); }

  @Test
  public void testWhile() { doTest("wh"); }

  @Test
  public void testWrapper() { doTest("wr"); }

  protected void doTest(@NotNull String templateId) {
    doTest(templateId, "liveTemplatesTest");
  }

  protected abstract void doTest(@NotNull String templateId, @NotNull String fileName);
}
