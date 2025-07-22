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

package com.perl5.lang.mojolicious.psi;

import com.intellij.psi.ElementDescriptionLocation;
import com.intellij.psi.ElementDescriptionProvider;
import com.intellij.psi.PsiElement;
import com.intellij.usageView.UsageViewTypeLocation;
import com.perl5.lang.mojolicious.MojoBundle;
import com.perl5.lang.mojolicious.psi.impl.MojoHelperDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MojoElementDescriptionProvider implements ElementDescriptionProvider {
  @Override
  public @Nullable String getElementDescription(@NotNull PsiElement element, @NotNull ElementDescriptionLocation location) {
    if (location == UsageViewTypeLocation.INSTANCE) {
      if (element instanceof MojoHelperDefinition) {
        return MojoBundle.message("perl.type.mojo.helper");
      }
    }

    return null;
  }
}
