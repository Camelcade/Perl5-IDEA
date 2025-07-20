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

import com.perl5.lang.perl.psi.impl.PerlPolyNamedElement;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementStub;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Provides capability for a class to generated light psi elements from {@link com.intellij.psi.PsiElement} or {@link com.intellij.psi.stubs.StubElement}
 */
public interface PerlLightElementProvider<Psi extends PerlPolyNamedElement<Stub>, Stub extends PerlPolyNamedElementStub<Psi>> {
  /**
   * @return version of this handler. Used for stubs consistency. Bump this version if
   * stubs format or light element generation been changed
   */
  default int getVersion() {
    return 1;
  }

  /**
   * @return list of the light psi elements declared by the {@code psiElement}
   */
  default @NotNull List<? extends PerlDelegatingLightNamedElement<?>> computeLightElementsFromPsi(@NotNull Psi psiElement) {
    return Collections.emptyList();
  }

  /**
   * @return list of the light psi elements declared by the {@code stubElement}
   */
  default @NotNull List<? extends PerlDelegatingLightNamedElement<?>> computeLightElementsFromStubs(@NotNull Psi psiElement,
                                                                                                    @NotNull Stub stubElement) {
    return Collections.emptyList();
  }
}
