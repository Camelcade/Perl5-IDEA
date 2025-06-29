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

package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import org.jetbrains.annotations.NotNull;


public interface PerlReferenceOwner extends PsiElement {
  static PsiReference @NotNull [] getReferencesWithCache(@NotNull PerlReferenceOwner referenceOwner) {
    return referenceOwner.hasReferences() ? referenceOwner.computeReferences() : PsiReference.EMPTY_ARRAY;
  }

  /**
   * Indicates that element must have a reference
   */
  default boolean hasReferences() {
    return false;
  }

  /**
   * Computing references for psi element
   */
  default PsiReference @NotNull [] computeReferences() {
    return ReferenceProvidersRegistry.getReferencesFromProviders(this);
  }
}
