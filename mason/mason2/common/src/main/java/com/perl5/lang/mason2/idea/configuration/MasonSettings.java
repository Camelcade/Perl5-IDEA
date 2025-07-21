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

package com.perl5.lang.mason2.idea.configuration;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.perl5.lang.htmlmason.idea.configuration.AbstractMasonSettings;
import com.perl5.lang.perl.idea.PerlPathMacros;
import com.perl5.lang.perl.idea.modules.PerlSourceRootType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.perl5.lang.perl.util.PerlUtil.mutableList;


@State(
  name = "Perl5MasonSettings",
  storages = @Storage(PerlPathMacros.PERL5_PROJECT_SHARED_SETTINGS_FILE)

)

public class MasonSettings extends AbstractMasonSettings implements PersistentStateComponent<MasonSettings>, Disposable {
  public final List<String> autobaseNames = mutableList("Base.mp", "Base.mc");

  @SuppressWarnings("unused")
  public MasonSettings() {
    initInstance();
  }

  public MasonSettings(@NotNull Project project) {
    super(project);
    initInstance();
  }

  private void initInstance() {
    globalVariables.add(new VariableDescription("$m", "Mason::Request"));
    changeCounter++;
  }

  @Override
  public PerlSourceRootType getSourceRootType() {
    return Mason2SourceRootType.INSTANCE;
  }

  @Override
  public @Nullable MasonSettings getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull MasonSettings state) {
    XmlSerializerUtil.copyBean(state, this);
    changeCounter++;
  }

  public static @NotNull MasonSettings getInstance(@NotNull Project project) {
    return project.getService(MasonSettings.class);
  }

  @Override
  public void dispose() {

  }
}
