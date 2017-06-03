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

package com.perl5.lang.perl.idea.codeInsight;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.perl5.lang.perl.idea.PerlPathMacros;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 22.11.2015.
 */
@State(
  name = "Perl5CodeInsightSettings",
  storages = {
    @Storage(
      file = PerlPathMacros.APP_OTHER_SETTINGS_FILE
    )
  }
)

public class Perl5CodeInsightSettings implements PersistentStateComponent<Perl5CodeInsightSettings> {
  public boolean HEREDOC_AUTO_INSERTION = true;

  @Nullable
  @Override
  public Perl5CodeInsightSettings getState() {
    return this;
  }

  @Override
  public void loadState(Perl5CodeInsightSettings state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  public static Perl5CodeInsightSettings getInstance() {
    return ServiceManager.getService(Perl5CodeInsightSettings.class);
  }
}
