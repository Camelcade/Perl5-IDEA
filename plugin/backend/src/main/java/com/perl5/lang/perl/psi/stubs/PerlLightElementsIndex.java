/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.stubs;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Processor;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedElement;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("rawtypes")
public abstract class PerlLightElementsIndex<LightPsi extends PsiElement> extends PerlStubIndexBase<PerlPolyNamedElement> {
  private static final int VERSION = 1;

  @Override
  public int getVersion() {
    return super.getVersion() + VERSION;
  }

  /**
   * @return a class of {@code PsiElement} we should seek in the light elements provided
   */
  protected abstract Class<LightPsi> getLightPsiClass();


  @Override
  protected @NotNull Class<PerlPolyNamedElement> getPsiClass() {
    return PerlPolyNamedElement.class;
  }


  /**
   * @return true iff {@code lightPsi} element matches {@code key} passed to filtering method
   */
  protected abstract boolean matchesKey(@NotNull String key, @NotNull LightPsi lightPsi);

  public boolean processLightElements(@NotNull Project project,
                                      @NotNull String keyText,
                                      @NotNull GlobalSearchScope scope,
                                      @NotNull Processor<? super LightPsi> processor) {
    return processElements(project, keyText, scope, polyNamedElement -> {
      Class<LightPsi> lightPsiClass = getLightPsiClass();
      for (PerlDelegatingLightNamedElement<?> lightNamedElement : ((PerlPolyNamedElement<?>)polyNamedElement).getLightElements()) {
        //noinspection unchecked
        if (lightPsiClass.isInstance(lightNamedElement) &&
            matchesKey(keyText, (LightPsi)lightNamedElement) &&
            !processor.process((LightPsi)lightNamedElement)) {
          return false;
        }
      }
      return true;
    });
  }
}
