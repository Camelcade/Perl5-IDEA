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

package com.perl5.lang.perl.psi.stubs.namespaces;

import com.intellij.psi.stubs.StubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedElement;
import com.perl5.lang.perl.psi.stubs.PerlLightElementsIndex;
import org.jetbrains.annotations.NotNull;

/**
 * Index for namespace_name => namespace
 *
 * @see PerlNamespaceIndex
 */
public class PerlLightNamespaceIndex extends PerlLightElementsIndex<PerlNamespaceDefinitionElement> {
  public static final int VERSION = 1;
  @SuppressWarnings("rawtypes")
  public static final StubIndexKey<String, PerlPolyNamedElement>
    LIGHT_NAMESPACE_KEY = StubIndexKey.createIndexKey("perl.package.light.direct");

  @Override
  public int getVersion() {
    return super.getVersion() + VERSION;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public @NotNull StubIndexKey<String, PerlPolyNamedElement> getKey() {
    return LIGHT_NAMESPACE_KEY;
  }

  @Override
  protected Class<PerlNamespaceDefinitionElement> getLightPsiClass() {
    return PerlNamespaceDefinitionElement.class;
  }

  @Override
  protected boolean matchesKey(@NotNull String key, @NotNull PerlNamespaceDefinitionElement element) {
    return key.equals(element.getNamespaceName());
  }

  public static @NotNull PerlLightNamespaceIndex getInstance() {
    return StubIndexExtension.EP_NAME.findExtensionOrFail(PerlLightNamespaceIndex.class);
  }
}
