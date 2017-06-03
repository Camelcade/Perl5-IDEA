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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 03.11.2016.
 */
public abstract class PerlCachingReference<T extends PsiElement> extends PsiPolyVariantReferenceBase<T> {
  private final static ResolveCache.PolyVariantResolver<PerlCachingReference> RESOLVER = PerlCachingReference::resolveInner;

  public PerlCachingReference(T psiElement) {
    super(psiElement);
  }

  public PerlCachingReference(@NotNull T element, TextRange textRange) {
    super(element, textRange);
  }

  public PerlCachingReference(T element, TextRange range, boolean soft) {
    super(element, range, soft);
  }

  @NotNull
  @Override
  public Object[] getVariants() {
    return EMPTY_ARRAY;
  }

  protected abstract ResolveResult[] resolveInner(boolean incompleteCode);

  @NotNull
  @Override
  public final ResolveResult[] multiResolve(boolean incompleteCode) {
    return ResolveCache.getInstance(myElement.getProject()).resolveWithCaching(this, RESOLVER, true, incompleteCode);
  }
}
