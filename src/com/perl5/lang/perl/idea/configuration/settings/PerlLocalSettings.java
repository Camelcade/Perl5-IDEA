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

package com.perl5.lang.perl.idea.configuration.settings;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.perl5.lang.perl.idea.PerlPathMacros;
import com.perl5.lang.perl.idea.actions.PerlFormatWithPerlTidyAction;
import com.perl5.lang.perl.idea.annotators.PerlCriticAnnotator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 02.06.2016.
 */
@State(
  name = "Perl5LocalSettings",
  storages = {
    @Storage(id = "default", file = StoragePathMacros.PROJECT_FILE),
    @Storage(id = "dir", file = PerlPathMacros.PERL5_PROJECT_SETTINGS_FILE, scheme = StorageScheme.DIRECTORY_BASED)
  }
)

public class PerlLocalSettings implements PersistentStateComponent<PerlLocalSettings> {
  public String PERL_PATH = "";
  public String PERL_TIDY_PATH = PerlFormatWithPerlTidyAction.PERL_TIDY_OS_DEPENDENT_NAME;
  public String PERL_CRITIC_PATH = PerlCriticAnnotator.PERL_CRITIC_OS_DEPENDENT_NAME;
  public boolean DISABLE_NO_INTERPRETER_WARNING = false;
  public boolean ENABLE_REGEX_INJECTIONS = false;
  private String myPerlInterpreter;

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
  public void loadState(PerlLocalSettings state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  public static PerlLocalSettings getInstance(@NotNull Project project) {
    PerlLocalSettings persisted = ServiceManager.getService(project, PerlLocalSettings.class);
    return persisted != null ? persisted : new PerlLocalSettings();
  }
}
