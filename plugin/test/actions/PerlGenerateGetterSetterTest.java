package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.perl5.lang.perl.idea.generation.GeneratePerlGetterSetterAction;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class PerlGenerateGetterSetterTest extends PerlActionTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/actions/generateGetterSetter";
  }

  @Override
  protected @NotNull AnAction getAction() {
    return new GeneratePerlGetterSetterAction();
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
