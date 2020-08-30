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

package com.perl5.lang.perl.profiler.parser.frames;

import com.google.common.annotations.VisibleForTesting;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.profiler.api.BaseCallStackElement;
import com.intellij.psi.NavigatablePsiElement;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@VisibleForTesting
public abstract class PerlCallStackElement extends BaseCallStackElement {
  protected static final Logger LOG = Logger.getInstance(PerlCallStackElement.class);
  protected static final String TRY_TINY_SUFFIX = "::try {...}";
  protected static final int MAX_FILE_TO_OPEN = 10;

  private final @NotNull String myFrameText;

  protected PerlCallStackElement(@NotNull String frameText) {
    myFrameText = frameText.trim();
  }

  protected @NotNull String getFrameText() {
    return myFrameText;
  }

  @Override
  public @NotNull String fullName() {
    return myFrameText;
  }

  @Override
  public boolean isNavigatable() {
    return true;
  }

  @Override
  public final @NotNull NavigatablePsiElement[] calcNavigatables(@NotNull Project project) {
    return ReadAction.compute(() -> {
      if (project.isDisposed()) {
        return NavigatablePsiElement.EMPTY_NAVIGATABLE_ELEMENT_ARRAY;
      }

      var perlSdk = PerlProjectManager.getSdk(project);
      if (perlSdk == null) {
        return NavigatablePsiElement.EMPTY_NAVIGATABLE_ELEMENT_ARRAY;
      }

      return computeNavigatables(project, perlSdk).toArray(NavigatablePsiElement.EMPTY_NAVIGATABLE_ELEMENT_ARRAY);
    });
  }

  protected abstract @NotNull List<NavigatablePsiElement> computeNavigatables(@NotNull Project project, @NotNull Sdk perlSdk);

  public static @NotNull PerlCallStackElement create(@NotNull String frameText) {
    if (frameText.contains(TRY_TINY_SUFFIX)) {
      return new PerlTryStackElement(frameText);
    }
    return new PerlFqnStackElement(frameText);
  }

  @Override
  public final String toString() {
    return myFrameText;
  }
}
