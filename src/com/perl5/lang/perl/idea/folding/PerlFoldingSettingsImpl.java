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

package com.perl5.lang.perl.idea.folding;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.perl5.lang.perl.idea.PerlPathMacros;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 28.09.2015.
 */

@State(
  name = "PerlCodeFoldingSettings",
  storages = @Storage(file = PerlPathMacros.APP_CODEINSIGHT_SETTINGS_FILE)
)
public class PerlFoldingSettingsImpl extends PerlFoldingSettings implements PersistentStateComponent<PerlFoldingSettings> {
  @Nullable
  @Override
  public PerlFoldingSettings getState() {
    return this;
  }

  @Override
  public void loadState(PerlFoldingSettings state) {
    XmlSerializerUtil.copyBean(state, this);
  }
}
