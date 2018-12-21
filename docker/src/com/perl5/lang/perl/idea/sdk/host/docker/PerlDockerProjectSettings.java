/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.sdk.host.docker;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Tag;
import com.perl5.lang.perl.idea.PerlPathMacros;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
  name = "Perl5DockerSettings",
  storages = @Storage(PerlPathMacros.PERL5_PROJECT_SETTINGS_FILE)
)
class PerlDockerProjectSettings implements PersistentStateComponent<PerlDockerProjectSettings> {
  @Tag("additional-params")
  @NotNull
  private String myAdditionalDockerParameters = "";

  @NotNull
  public String getAdditionalDockerParameters() {
    return myAdditionalDockerParameters;
  }

  public void setAdditionalDockerParameters(@NotNull String additionalDockerParameters) {
    myAdditionalDockerParameters = additionalDockerParameters;
  }

  @Nullable
  @Override
  public PerlDockerProjectSettings getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull PerlDockerProjectSettings state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  public static PerlDockerProjectSettings getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, PerlDockerProjectSettings.class);
  }
}
