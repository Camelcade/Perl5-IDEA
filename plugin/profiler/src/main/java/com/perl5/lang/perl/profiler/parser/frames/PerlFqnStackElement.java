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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Regular stackframe: Foo::Bar::methods
 */
class PerlFqnStackElement extends PerlCallStackElement {
  PerlFqnStackElement(@NotNull String name) {
    super(name);
  }

  @Override
  protected @NotNull List<NavigatablePsiElement> computeNavigatables(@NotNull Project project, @NotNull Sdk perlSdk) {
    List<NavigatablePsiElement> result = new ArrayList<>();
    PerlSubUtil.processRelatedItems(project, GlobalSearchScope.allScope(project), getFrameText(), it -> {
      if (it instanceof NavigatablePsiElement) {
        result.add((NavigatablePsiElement)it);
      }
      return true;
    });
    return result;
  }
}
