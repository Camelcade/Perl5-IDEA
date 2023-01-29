/*
 * Copyright 2015-2023 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
      sdk -> PerlProjectManager.getInstance(project).setProjectSdk(sdk),
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
