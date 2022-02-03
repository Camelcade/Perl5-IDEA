package base;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.util.Disposer;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerHandler;
import org.jetbrains.annotations.NotNull;

public abstract class PerlInterpreterConfigurator {
  abstract void setUpPerlInterpreter(@NotNull Project project,
                                     @NotNull Disposable testDisposable);

  protected void addSdk(@NotNull String pathToVersionManager,
                        @NotNull String distributionId,
                        @NotNull PerlRealVersionManagerHandler<?, ?> versionManagerHandler,
                        @NotNull Project project,
                        @NotNull Disposable testDisposable) {
    versionManagerHandler.createInterpreter(
      distributionId,
      versionManagerHandler.createAdapter(pathToVersionManager, getHostData()),
      sdk -> {
        Disposer.register(testDisposable, () -> WriteAction.run(() -> PerlSdkTable.getInstance().removeJdk(sdk)));
        PerlProjectManager.getInstance(project).setProjectSdk(sdk);
      },
      project
    );
  }

  protected @NotNull PerlHostData<?, ?> getHostData() {
    return PerlHostHandler.getDefaultHandler().createData();
  }
}
