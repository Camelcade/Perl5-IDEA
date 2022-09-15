package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.perl5.lang.perl.idea.generation.GeneratePerlGetterAction;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class PerlGenerateGetterTest extends PerlActionTestCase {
  @Override
  protected String getBaseDataPath() {
    return "actions/generateGetter";
  }

  @Override
  protected @NotNull AnAction getAction() {
    return new GeneratePerlGetterAction();
  }

  @Test
  public void testEmpty() {
    doTest("");
  }

  @Test
  public void testSingle() {
    doTest("single");
  }

  @Test
  public void testMulti() {
    doTest("one,two,three");
  }
}
