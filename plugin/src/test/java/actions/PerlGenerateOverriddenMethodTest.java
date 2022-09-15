package actions;

import com.intellij.codeInsight.generation.actions.OverrideMethodsAction;
import com.intellij.openapi.actionSystem.AnAction;
import com.perl5.lang.perl.extensions.generation.PerlCodeGeneratorImpl;
import com.perl5.lang.perl.idea.codeInsight.PerlMethodMember;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;
import java.util.function.UnaryOperator;

public class PerlGenerateOverriddenMethodTest extends PerlActionTestCase {
  @Override
  protected String getBaseDataPath() {
    return "actions/generateOverride";
  }

  @Override
  protected @NotNull AnAction getAction() {
    return new OverrideMethodsAction();
  }

  @Test
  public void testNothing() {
    doTest();
  }

  @Test
  public void testAll() {
    doTest();
  }

  @Override
  protected void doTest() {
    doTest(it -> it);
  }

  protected void doTest(@NotNull UnaryOperator<List<PerlMethodMember>> filter) {
    PerlCodeGeneratorImpl.withChooser(filter, super::doTest);
  }
}
