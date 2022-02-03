package base;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.util.Disposer;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.docker.PerlDockerTestUtil;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerHandler;
import org.jetbrains.annotations.NotNull;

public class PerlSystemDockerInterpreterConfigurator extends PerlInterpreterConfigurator {
  public static final PerlInterpreterConfigurator INSTANCE = new PerlSystemDockerInterpreterConfigurator();
  private static final String PERL_HOME = "/usr/local/bin/perl";
  private static final String DOCKER_IMAGE = "hurricup/camelcade-test:perl5";

  private PerlSystemDockerInterpreterConfigurator() {
  }

  @Override
  protected @NotNull PerlHostData<?, ?> getHostData() {
    return PerlDockerTestUtil.createHostData(DOCKER_IMAGE);
  }

  @Override
  void setUpPerlInterpreter(@NotNull Project project, @NotNull Disposable testDisposable) {
    PerlVersionManagerData<?, ?> versionManagerData = PerlVersionManagerHandler.getDefaultHandler().createData();
    PerlSdkType.createAndAddSdk(PERL_HOME, getHostData(), versionManagerData, sdk -> {
      Disposer.register(testDisposable, () -> WriteAction.run(() -> PerlSdkTable.getInstance().removeJdk(sdk)));
      PerlProjectManager.getInstance(project).setProjectSdk(sdk);
    }, project);
  }

  @Override
  public String toString() {
    return "docker: " + PERL_HOME + "@" + DOCKER_IMAGE;
  }
}
