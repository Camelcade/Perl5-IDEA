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

package com.perl5.lang.perl.psi;

import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementStub;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Interface represents poly-named declaration, when one expression declares multiple entities with different names
 */
public interface PerlPolyNamedElement extends StubBasedPsiElement<PerlPolyNamedElementStub> {
  @NotNull
  default List<PerlDelegatingLightNamedElement> getLightElements() {
    return CachedValuesManager.getCachedValue(this, () -> CachedValueProvider.Result.create(calcLightElements(), this)
    );
  }

  /**
   * Calculates light elements from stubs or psi
   */
  @NotNull
  List<PerlDelegatingLightNamedElement> calcLightElements();
}
