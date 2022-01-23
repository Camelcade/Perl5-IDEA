package actions;

import base.PerlLightTestCase;
import com.intellij.openapi.actionSystem.AnAction;
import org.jetbrains.annotations.NotNull;

public abstract class PerlActionTestCase extends PerlLightTestCase {
  protected abstract @NotNull AnAction getAction();

  protected void doTest() {
    initWithFileSmart();
    myFixture.testAction(getAction());
    checkEditorWithFile();
  }
}
