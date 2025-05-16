/*
 * Copyright 2015-2025 Alexandr Evstigneev
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
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerHandler;
import org.jetbrains.annotations.NotNull;

abstract class PerlVersionManagerBasedConfigurator extends PerlInterpreterConfigurator {

  @Override
  void setUpPerlInterpreter(@NotNull Project project) {
    PerlRealVersionManagerHandler<?, ?> versionManagerHandler = getVersionManagerHandler();
    var adapter = versionManagerHandler.createAdapter(getPathToVersionManager(), createHostData());
    versionManagerHandler.createInterpreter(
      getDistributionId(),
      adapter,
      sdk -> PerlProjectManager.getInstance(project).setProjectSdk(sdk),
      project
    );
  }

  protected abstract @NotNull String getPathToVersionManager();


  protected @NotNull String getDistributionId() {
    return PerlPlatformTestCase.PERL_TEST_VERSION;
  }

  @Override
  protected abstract @NotNull PerlRealVersionManagerHandler<?, ?> getVersionManagerHandler();
}
