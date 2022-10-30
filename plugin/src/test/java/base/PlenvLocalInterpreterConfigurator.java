package base;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.perl5.lang.perl.idea.sdk.versionManager.plenv.PlenvTestUtil;
import org.jetbrains.annotations.NotNull;

public class PlenvLocalInterpreterConfigurator extends PerlInterpreterConfigurator {
  public static final PerlInterpreterConfigurator INSTANCE = new PlenvLocalInterpreterConfigurator();
  private static final String PLENV_HOME = "~/.plenv/bin/plenv";
  private static final String DISTRIBUTION_ID = PerlPlatformTestCase.PERL_TEST_VERSION;

  private PlenvLocalInterpreterConfigurator() {
  }

  @Override
  void setUpPerlInterpreter(@NotNull Project project) {
    addSdk(FileUtil.expandUserHome(PLENV_HOME), DISTRIBUTION_ID, PlenvTestUtil.getVersionManagerHandler(), project);
  }

  @Override
  public String toString() {
    return "plenv: " + DISTRIBUTION_ID;
  }
}
