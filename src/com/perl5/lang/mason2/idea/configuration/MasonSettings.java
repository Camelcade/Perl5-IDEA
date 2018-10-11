/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.perl5.lang.htmlmason.idea.configuration.AbstractMasonSettings;
import com.perl5.lang.perl.idea.PerlPathMacros;
import com.perl5.lang.perl.idea.modules.PerlSourceRootType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 03.01.2016.
 */
@State(
  name = "Perl5MasonSettings",
  storages = @Storage(PerlPathMacros.PERL5_PROJECT_SHARED_SETTINGS_FILE)

)

public class MasonSettings extends AbstractMasonSettings implements PersistentStateComponent<MasonSettings> {
  public List<String> autobaseNames = new ArrayList<>(Arrays.asList("Base.mp", "Base.mc"));

  public MasonSettings() {
    globalVariables.add(new VariableDescription("$m", "Mason::Request"));
    changeCounter++;
  }

  @Override
  protected PerlSourceRootType getSourceRootType() {
    return Mason2SourceRootType.INSTANCE;
  }

  @Nullable
  @Override
  public MasonSettings getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull MasonSettings state) {
    XmlSerializerUtil.copyBean(state, this);
    changeCounter++;
  }

  public static MasonSettings getInstance(@NotNull Project project) {
    MasonSettings persisted = ServiceManager.getService(project, MasonSettings.class);
    if (persisted == null) {
      persisted = new MasonSettings();
    }

    return (MasonSettings)persisted.setProject(project);
  }
}
