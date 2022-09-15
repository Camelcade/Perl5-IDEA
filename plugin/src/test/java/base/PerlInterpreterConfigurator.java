package base;

import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerHandler;
import org.jetbrains.annotations.NotNull;

public abstract class PerlInterpreterConfigurator {
  abstract void setUpPerlInterpreter(@NotNull Project project);

  protected void addSdk(@NotNull String pathToVersionManager,
                        @NotNull String distributionId,
                        @NotNull PerlRealVersionManagerHandler<?, ?> versionManagerHandler,
                        @NotNull Project project) {
    versionManagerHandler.createInterpreter(
      distributionId,
      versionManagerHandler.createAdapter(pathToVersionManager, getHostData()),
      sdk -> {
        PerlProjectManager.getInstance(project).setProjectSdk(sdk);
      },
      project
    );
  }

  /**
   * @return true iff installation is available for sdk (it can keep state)
   */
  public boolean isStateful() {
    return true;
  }

  protected @NotNull PerlHostData<?, ?> getHostData() {
    return PerlHostHandler.getDefaultHandler().createData();
  }
}
