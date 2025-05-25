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
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerHandler;
import org.jetbrains.annotations.NotNull;

public abstract class PerlInterpreterConfigurator {

  void setUpPerlInterpreter(@NotNull Project project) {
    PerlSdkType.createAndAddSdk(
      getInterpreterPath(),
      createHostData(),
      createVersionManagerData(),
      sdk -> PerlProjectManager.getInstance(project).setProjectSdk(sdk),
      project);
  }

  /**
   * @return true iff installation is available for sdk (it can keep state)
   */
  public boolean isStateful() {
    return true;
  }

  protected @NotNull PerlHostData<?, ?> createHostData() {
    return PerlHostHandler.getDefaultHandler().createData();
  }

  protected @NotNull PerlVersionManagerData<?, ?> createVersionManagerData() {
    return getVersionManagerHandler().createData();
  }

  protected @NotNull PerlVersionManagerHandler<?, ?> getVersionManagerHandler() {
    return PerlVersionManagerHandler.getDefaultHandler();
  }

  protected @NotNull String getInterpreterPath() {
    throw new UnsupportedOperationException("Not implemented");
  }
}
