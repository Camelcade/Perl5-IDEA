/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.actions;

import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public abstract class PerlActionBase extends AnAction implements PerlAction {
  public PerlActionBase() {
  }

  public PerlActionBase(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text,
                        @Nls(capitalization = Nls.Capitalization.Sentence) @Nullable String description,
                        @Nullable Icon icon) {
    super(text, description, icon);
  }

  public PerlActionBase(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text,
                        @Nullable Icon icon) {
    this(text, null, icon);
  }

  public PerlActionBase(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text) {
    this(text, null, null);
  }

  protected boolean isEnabled(@NotNull AnActionEvent event) {
    return PerlProjectManager.isPerlEnabled(event.getDataContext());
  }

  @Override
  public void update(@NotNull AnActionEvent event) {
    final boolean enabled = isEnabled(event);
    var eventPresentation = event.getPresentation();
    eventPresentation.setEnabled(enabled);
    if (alwaysHideDisabled() || ActionPlaces.isPopupPlace(event.getPlace())) {
      eventPresentation.setVisible(enabled);
    }
    else {
      eventPresentation.setVisible(true);
    }
  }

  protected boolean alwaysHideDisabled() {
    return false;
  }
}
