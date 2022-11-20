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

package com.perl5.lang.perl.psi.references;

import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.perl5.lang.perl.psi.PerlAnnotationVariableElement;
import org.jetbrains.annotations.NotNull;

public class PerlAnnotationVariableReference extends PerlCachingReference<PerlAnnotationVariableElement> {
  public PerlAnnotationVariableReference(@NotNull PerlAnnotationVariableElement psiElement) {
    super(psiElement);
  }

  @Override
  protected @NotNull ResolveResult[] resolveInner(boolean incompleteCode) {
    return PsiElementResolveResult.createResults(myElement.getAnnotation().getTargets());
  }
}
