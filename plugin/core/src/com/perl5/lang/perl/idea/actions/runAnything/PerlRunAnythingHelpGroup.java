/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.actions.runAnything;

import com.intellij.ide.actions.runAnything.activity.RunAnythingProvider;
import com.intellij.ide.actions.runAnything.groups.RunAnythingHelpGroup;
import com.intellij.ide.actions.runAnything.items.RunAnythingItem;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;

public class PerlRunAnythingHelpGroup extends RunAnythingHelpGroup<PerlRunAnythingProvider> {
  @Override
  public @NotNull Collection<PerlRunAnythingProvider> getProviders() {
    throw new RuntimeException("NYI");
  }

  @Override
  public @NotNull String getTitle() {
    return Objects.requireNonNull(RunAnythingProvider.EP_NAME.findExtension(PerlRunAnythingProvider.class)).getHelpGroupTitle();
  }

  @Override
  protected int getMaxInitialItems() {
    return 10;
  }

  @Override
  public @NotNull Collection<RunAnythingItem> getGroupItems(@NotNull DataContext dataContext, @NotNull String pattern) {
    return ContainerUtil.filter(
      Objects.requireNonNull(RunAnythingProvider.EP_NAME.findExtension(PerlRunAnythingProvider.class)).getHelpItems(dataContext),
      Objects::nonNull);
  }
}
