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

package com.perl5.lang.perl.idea.codeInsight.typeInference.value;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Function;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class PerlValueResolver {
  @NotNull
  private final GlobalSearchScope myResolveScope;

  @NotNull
  private final PerlValuesCacheService myCacheService;

  @Nullable
  private final PsiFile myContextFile;

  public PerlValueResolver(@NotNull PsiElement contextElement) {
    myResolveScope = contextElement.getResolveScope();
    myCacheService = PerlValuesCacheService.getInstance(contextElement.getProject());
    myContextFile = ObjectUtils.doIfNotNull(contextElement.getContainingFile(), PsiFile::getOriginalFile);
  }

  @NotNull
  public PerlValue resolve(@NotNull PerlValue unresolvedValue) {
    PerlValue substitutedUnresolvedValue = substitute(unresolvedValue);
    if (substitutedUnresolvedValue.isUndef() || substitutedUnresolvedValue.isUnknown() || substitutedUnresolvedValue.isDeterministic()) {
      return substitutedUnresolvedValue;
    }
    return substitute(myCacheService.getResolvedValue(substitutedUnresolvedValue, this));
  }

  @Nullable
  public PsiFile getContextFile() {
    return myContextFile;
  }

  @NotNull
  public final GlobalSearchScope getResolveScope() {
    return myResolveScope;
  }

  @NotNull
  public final Project getProject() {
    return Objects.requireNonNull(myResolveScope.getProject());
  }

  @NotNull
  protected PerlValue substitute(@NotNull PerlValue perlValue) {
    return perlValue;
  }

  @NotNull
  public PerlValue resolve(@NotNull PerlValue unresolvedParameter, @NotNull PerlValueConverter converter) {
    PerlValue resolvedParameter = resolve(unresolvedParameter);
    if (resolvedParameter instanceof PerlOneOfValue) {
      return ((PerlOneOfValue)resolvedParameter).convert(converter::fun);
    }
    else {
      return converter.fun(resolvedParameter);
    }
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

  /**
   * Describes value resolve step.
   */
  public interface PerlValueConverter extends Function<PerlValue, PerlValue> {
  }
}
