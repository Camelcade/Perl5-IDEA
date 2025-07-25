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

package com.perl5.lang.perl.idea.folding;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.perl5.lang.perl.idea.PerlPathMacros;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@State(
  name = "PerlCodeFoldingSettings",
  storages = @Storage(PerlPathMacros.APP_CODEINSIGHT_SETTINGS_FILE),
  perClient = true
)
public class PerlFoldingSettingsImpl implements PersistentStateComponent<PerlFoldingSettingsImpl> {
  public boolean COLLAPSE_COMMENTS = true;
  public boolean COLLAPSE_CONSTANT_BLOCKS = false;
  public boolean COLLAPSE_ANON_ARRAYS = false;
  public boolean COLLAPSE_ANON_HASHES = false;
  public boolean COLLAPSE_PARENTHESISED = false;
  public boolean COLLAPSE_HEREDOCS = false;
  public boolean COLLAPSE_TEMPLATES = false;
  public boolean COLLAPSE_QW = false;
  public boolean COLLAPSE_CHAR_SUBSTITUTIONS = true;

  @Override
  public @Nullable PerlFoldingSettingsImpl getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull PerlFoldingSettingsImpl state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  public static PerlFoldingSettingsImpl getInstance() {
    return ApplicationManager.getApplication().getService(PerlFoldingSettingsImpl.class);
  }

  @Override
  public void noStateLoaded() {
    loadState(new PerlFoldingSettingsImpl());
  }
}
