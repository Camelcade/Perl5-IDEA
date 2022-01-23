package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.perl5.lang.perl.idea.generation.GeneratePerlConstructorAction;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class PerlGenerateConstructorActionTest extends PerlActionTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/actions/generateConstructor";
  }

  @Override
  protected @NotNull AnAction getAction() {
    return new GeneratePerlConstructorAction();
  }

  @Test
  public void testSimple() {
    doTest();
  }

  @Test
  public void testWithParent() {
    doTest();
  }
}
