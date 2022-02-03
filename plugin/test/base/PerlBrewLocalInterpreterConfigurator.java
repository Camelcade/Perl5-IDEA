package base;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.perl5.lang.perl.idea.sdk.versionManager.perlbrew.PerlBrewTestUtil;
import org.jetbrains.annotations.NotNull;

public class PerlBrewLocalInterpreterConfigurator extends PerlInterpreterConfigurator {
  public static final PerlInterpreterConfigurator INSTANCE = new PerlBrewLocalInterpreterConfigurator();
  private static final String PERLBREW_HOME = "~/perl5/perlbrew/bin/perlbrew";
  private static final String DISTRIBUTION_ID = "perl-5.32.0@plugin_test";

  private PerlBrewLocalInterpreterConfigurator() {
  }

  @Override
  void setUpPerlInterpreter(@NotNull Project project, @NotNull Disposable testDisposable) {
    addSdk(FileUtil.expandUserHome(PERLBREW_HOME), DISTRIBUTION_ID, PerlBrewTestUtil.getVersionManagerHandler(), project, testDisposable);
  }

  @Override
  public String toString() {
    return "perlbrew: " + DISTRIBUTION_ID;
  }
}
