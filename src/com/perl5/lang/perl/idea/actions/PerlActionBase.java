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

package com.perl5.lang.perl.idea.actions;

import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.projectRoots.Sdk;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 26.04.2016.
 */
public abstract class PerlActionBase extends AnAction {
  protected boolean isEnabled(AnActionEvent event) {
    return PerlProjectManager.isPerlEnabled(event.getDataContext());
  }


  @Nullable
  protected Sdk getPerlSdk(AnActionEvent event) {
    return PerlProjectManager.getSdk(event.getProject());
  }

  @Override
  public void update(@NotNull AnActionEvent event) {
    final boolean enabled = isEnabled(event);
    event.getPresentation().setEnabled(enabled);
    if (ActionPlaces.isPopupPlace(event.getPlace())) {
      event.getPresentation().setVisible(enabled);
    }
  }
}
