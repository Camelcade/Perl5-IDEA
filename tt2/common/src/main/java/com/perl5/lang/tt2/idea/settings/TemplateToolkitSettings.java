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

package com.perl5.lang.tt2.idea.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.RootsChangeRescanningInfo;
import com.intellij.openapi.roots.ex.ProjectRootManagerEx;
import com.intellij.openapi.util.EmptyRunnable;
import com.intellij.util.messages.Topic;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import com.perl5.lang.perl.idea.PerlPathMacros;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@State(
  name = "TemplateToolkitSettings",
  storages = @Storage(PerlPathMacros.PERL5_PROJECT_SHARED_SETTINGS_FILE),
  perClient = true
)
public class TemplateToolkitSettings implements PersistentStateComponent<TemplateToolkitSettings> {
  public static final Topic<SettingsListener> TT2_SETTINGS_TOPIC =
    Topic.create("TT2 settings topiec", SettingsListener.class, Topic.BroadcastDirection.NONE);
  public static final String DEFAULT_START_TAG = "[%";
  public static final String DEFAULT_END_TAG = "%]";
  public static final String DEFAULT_OUTLINE_TAG = "%%";

  public final @NotNull List<String> substitutedExtensions = new ArrayList<>();
  public String START_TAG = DEFAULT_START_TAG;
  public String END_TAG = DEFAULT_END_TAG;
  public String OUTLINE_TAG = DEFAULT_OUTLINE_TAG;
  public boolean ENABLE_ANYCASE = false;

  @Transient
  private final transient @Nullable Project myProject;

  public TemplateToolkitSettings() {
    myProject = null;
  }

  public TemplateToolkitSettings(@NotNull Project project) {
    myProject = project;
  }

  public @NotNull Project getProject() {
    return Objects.requireNonNull(myProject);
  }

  public void settingsUpdated() {
    myProject.getMessageBus().syncPublisher(TT2_SETTINGS_TOPIC).settingsUpdated();
    ApplicationManager.getApplication().invokeLater(() -> WriteAction.run(
      () -> ProjectRootManagerEx.getInstanceEx(getProject()).makeRootsChange(
        EmptyRunnable.getInstance(), RootsChangeRescanningInfo.TOTAL_RESCAN)));
  }

  public @NotNull List<String> getSubstitutedExtensions() {
    return substitutedExtensions;
  }

  @Override
  public @Nullable TemplateToolkitSettings getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull TemplateToolkitSettings state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  @Override
  public void noStateLoaded() {
    loadState(new TemplateToolkitSettings());
  }

  public static @NotNull TemplateToolkitSettings getInstance(@NotNull Project project) {
    return project.getService(TemplateToolkitSettings.class);
  }

  public interface SettingsListener {
    void settingsUpdated();
  }
}
