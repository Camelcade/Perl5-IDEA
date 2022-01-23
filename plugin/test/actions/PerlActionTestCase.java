package actions;

import base.PerlLightTestCase;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.ui.TestDialogManager;
import org.jetbrains.annotations.NotNull;

public abstract class PerlActionTestCase extends PerlLightTestCase {
  protected abstract @NotNull AnAction getAction();

  protected void doTest(@NotNull String testDialogInput) {
    TestDialogManager.setTestInputDialog(message -> testDialogInput);
    doTest();
  }

  protected void doTest() {
    initWithFileSmart();
    myFixture.testAction(getAction());
    checkEditorWithFile();
  }
}
