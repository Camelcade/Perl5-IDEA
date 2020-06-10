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

package com.perl5.lang.perl.idea.codeInsight.typeInference.value;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

public abstract class PerlValueResolver {
  private final @NotNull GlobalSearchScope myResolveScope;

  private final @NotNull PerlValuesCacheService myCacheService;

  private final @Nullable PsiFile myContextFile;

  public PerlValueResolver(@NotNull PsiElement contextElement) {
    myResolveScope = contextElement.getResolveScope();
    myCacheService = PerlValuesCacheService.getInstance(contextElement.getProject());
    myContextFile = ObjectUtils.doIfNotNull(contextElement.getContainingFile(), PsiFile::getOriginalFile);
  }

  public @NotNull PerlValue resolve(@NotNull PerlValue unresolvedValue) {
    PerlValue substitutedUnresolvedValue = substitute(unresolvedValue);
    if (substitutedUnresolvedValue.isUndef() || substitutedUnresolvedValue.isUnknown() || substitutedUnresolvedValue.isDeterministic()) {
      return substitutedUnresolvedValue;
    }
    return substitute(myCacheService.getResolvedValue(substitutedUnresolvedValue, this));
  }

  public @Nullable PsiFile getContextFile() {
    return myContextFile;
  }

  public final @NotNull GlobalSearchScope getResolveScope() {
    return myResolveScope;
  }

  public final @NotNull Project getProject() {
    return Objects.requireNonNull(myResolveScope.getProject());
  }

  protected @NotNull PerlValue substitute(@NotNull PerlValue perlValue) {
    return perlValue;
  }

  public @NotNull PerlValue resolve(@NotNull PerlValue unresolvedParameter, @NotNull Function<PerlValue, PerlValue> converter) {
    return PerlValuesBuilder.convert(resolve(unresolvedParameter), converter);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PerlValueResolver)) {
      return false;
    }

    PerlValueResolver resolver = (PerlValueResolver)o;

    if (!myResolveScope.equals(resolver.myResolveScope)) {
      return false;
    }
    return Objects.equals(myContextFile, resolver.myContextFile);
  }

  @Override
  public int hashCode() {
    int result = myResolveScope.hashCode();
    result = 31 * result + (myContextFile != null ? myContextFile.hashCode() : 0);
    return result;
  }
}
