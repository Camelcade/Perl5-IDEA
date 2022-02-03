package base;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.perl5.lang.perl.idea.sdk.versionManager.plenv.PlenvTestUtil;
import org.jetbrains.annotations.NotNull;

public class PlenvLocalInterpreterConfigurator extends PerlInterpreterConfigurator {
  public static final PerlInterpreterConfigurator INSTANCE = new PlenvLocalInterpreterConfigurator();
  private static final String PLENV_HOME = "~/.plenv/bin/plenv";
  private static final String DISTRIBUTION_ID = "5.32.0";

  private PlenvLocalInterpreterConfigurator() {
  }

  @Override
  void setUpPerlInterpreter(@NotNull Project project, @NotNull Disposable testDisposable) {
    addSdk(FileUtil.expandUserHome(PLENV_HOME), DISTRIBUTION_ID, PlenvTestUtil.getVersionManagerHandler(), project, testDisposable);
  }

  @Override
  public String toString() {
    return "plenv: " + DISTRIBUTION_ID;
  }
}
