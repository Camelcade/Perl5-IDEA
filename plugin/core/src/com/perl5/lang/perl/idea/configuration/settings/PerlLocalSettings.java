/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.configuration.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.perl5.lang.perl.idea.PerlPathMacros;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


@State(
  name = "Perl5LocalSettings",
  storages = @Storage(PerlPathMacros.PERL5_PROJECT_SETTINGS_FILE)
)

public class PerlLocalSettings implements PersistentStateComponent<PerlLocalSettings> {
  public boolean DISABLE_NO_INTERPRETER_WARNING = false;
  public boolean DISABLE_ASSOCIATIONS_CHECKING = false;
  public boolean ENABLE_REGEX_INJECTIONS = false;
  private String myPerlInterpreter;
  private List<String> myExternalLibrariesPaths = new ArrayList<>();

  public List<String> getExternalLibrariesPaths() {
    return myExternalLibrariesPaths;
  }

  public void setExternalLibrariesPaths(List<String> externalLibrariesPaths) {
    myExternalLibrariesPaths = new ArrayList<>(externalLibrariesPaths);
  }

  public String getPerlInterpreter() {
    return myPerlInterpreter;
  }

  public void setPerlInterpreter(String perlInterpreter) {
    myPerlInterpreter = perlInterpreter;
  }

  @Nullable
  @Override
  public PerlLocalSettings getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull PerlLocalSettings state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  public static PerlLocalSettings getInstance(@NotNull Project project) {
    PerlLocalSettings persisted = ServiceManager.getService(project, PerlLocalSettings.class);
    return persisted != null ? persisted : new PerlLocalSettings();
  }
}
