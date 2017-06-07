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

import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.perl5.lang.perl.idea.stubs.PerlPolyNamedElementStub;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Interface represents poly-named declaration, when one expression declares multiple entities with different names
 */
public interface PerlPolyNamedElement extends StubBasedPsiElement<PerlPolyNamedElementStub> {
  /**
   * @return collecting map of names to identifiers
   */
  @NotNull
  Map<String, PsiElement> collectNameIdentifiersMap();

  /**
   * @return Map of names to name identifiers, cached
   */
  @NotNull
  default Map<String, PsiElement> getNameIdentifiersMap() {
    return CachedValuesManager.getCachedValue(this, () ->
      CachedValueProvider.Result.create(collectNameIdentifiersMap(), PerlPolyNamedElement.this)
    );
  }

  /**
   * @return Name identifier by name; null if unresovable
   */
  @Nullable
  default PsiElement getNameIdentifierByName(@NotNull String name) {
    return getNameIdentifiersMap().get(name);
  }

  /**
   * Returns name from identifier
   *
   * @param identifier identifier from getNameIdentifiersMap
   * @return name or null if name is unresolvable
   */
  @Nullable
  default String getNameFromIdentifier(PsiElement identifier) {
    return ElementManipulators.getValueText(identifier);
  }

  /**
   * @return Names of element; override this to use stubs
   */
  @NotNull
  default List<String> getNamesList() {
    return new ArrayList<>(getNameIdentifiersMap().keySet());
  }

  /**
   * @return Map of light elements, bound to the names identifiers, one for each name identifier
   */
  @NotNull
  default Map<String, PerlDelegatingLightNamedElement> getLightElementsMap() {
    return CachedValuesManager.getCachedValue(this, () ->
      CachedValueProvider.Result.create(calcLightElementsMap(), PerlPolyNamedElement.this));
  }

  /**
   * @return Map of light elements, bound to the names identifiers, one for each name identifier
   */
  @NotNull
  default Map<String, PerlDelegatingLightNamedElement> calcLightElementsMap() {
    List<String> namesList = getNamesList();
    Map<String, PerlDelegatingLightNamedElement> result = new THashMap<>();

    for (String name : namesList) {
      assert name != null;
      result.put(name, createLightElement(name));
    }

    return result;
  }

  /**
   * Creates light named element by name
   *
   * @param name light element name
   * @return new light element
   */
  @NotNull
  default PerlDelegatingLightNamedElement createLightElement(@NotNull String name) {
    //noinspection unchecked
    return (PerlDelegatingLightNamedElement)new com.perl5.lang.perl.psi.PerlDelegatingLightNamedElement<PerlPolyNamedElement>(this, name);
  }

  /**
   * @return light element bound to the name identifier with name specified; null if unresolvable
   */
  @Nullable
  default PerlDelegatingLightNamedElement getLightElementByName(@NotNull String name) {
    return getLightElementsMap().get(name);
  }
}
