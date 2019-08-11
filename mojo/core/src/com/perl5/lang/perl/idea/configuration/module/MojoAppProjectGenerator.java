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

package com.perl5.lang.perl.idea.configuration.module;

import com.intellij.openapi.ui.InputValidator;
import com.perl5.lang.mojolicious.MojoIcons;
import com.perl5.lang.mojolicious.idea.actions.MojoGenerateAction;
import com.perl5.lang.mojolicious.idea.actions.MojoGenerateAppAction;
import com.perl5.lang.mojolicious.model.MojoApp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class MojoAppProjectGenerator extends MojoProjectGenerator {
  @Nullable
  @Override
  public Icon getLogo() {
    return MojoIcons.MOJO_LOGO;
  }

  @NotNull
  @Override
  protected String getEntityLabel() {
    return "Application name:";
  }

  @NotNull
  @Override
  protected InputValidator getNameValidator() {
    return new MojoApp.NameValidator();
  }

  @NotNull
  @Override
  public String getName() {
    return "Mojo Application";
  }

  @NotNull
  protected MojoGenerateAction getGenerationAction() {
    return new MojoGenerateAppAction();
  }

}
