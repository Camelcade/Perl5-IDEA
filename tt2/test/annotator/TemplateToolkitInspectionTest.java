package annotator;

import base.TemplateToolkitLightTestCase;
import com.perl5.lang.tt2.idea.TemplateToolkitOrphanDirectiveInspection;
import org.junit.Test;

public class TemplateToolkitInspectionTest extends TemplateToolkitLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/annotator/inspection";
  }

  @Test
  public void testEnd() { doTest(); }

  @Test
  public void testElse() { doTest(); }

  @Test
  public void testElsif() { doTest(); }

  @Test
  public void testCatch() { doTest(); }

  @Test
  public void testFinal() { doTest(); }

  @Test
  public void testCase() { doTest(); }

  private void doTest() {
    //noinspection unchecked
    doInspectionTest(TemplateToolkitOrphanDirectiveInspection.class);
  }
}
