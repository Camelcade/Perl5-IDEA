/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public abstract class PerlCachingReference<T extends PsiElement> extends PsiPolyVariantReferenceBase<T> {
  private static final ResolveCache.PolyVariantResolver<PerlCachingReference<?>> RESOLVER = PerlCachingReference::resolveInner;

  private final TextRange myExplicitRange;

  public PerlCachingReference(@NotNull T psiElement) {
    this(psiElement, null);
  }

  public PerlCachingReference(@NotNull T element, @Nullable TextRange textRange) {
    this(element, textRange, false);
  }

  public PerlCachingReference(@NotNull T element, @Nullable TextRange range, boolean soft) {
    super(element, range, soft);
    myExplicitRange = range;
  }

  @Override
  public Object @NotNull [] getVariants() {
    return EMPTY_ARRAY;
  }

  protected abstract @NotNull ResolveResult[] resolveInner(boolean incompleteCode);

  @Override
  public final ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
    return ResolveCache.getInstance(myElement.getProject()).resolveWithCaching(this, RESOLVER, true, incompleteCode);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PerlCachingReference<?> cachingReference)) {
      return false;
    }
    return Objects.equals(myExplicitRange, cachingReference.myExplicitRange) &&
           Objects.equals(getElement(), cachingReference.getElement());
  }

  @Override
  public int hashCode() {

    return Objects.hash(myExplicitRange, getElement());
  }
}
