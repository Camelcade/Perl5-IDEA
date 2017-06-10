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

package com.perl5.lang.perl.psi.utils;

import com.intellij.psi.stubs.StubBase;
import com.perl5.lang.perl.psi.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.PerlDelegatingSubElement;
import com.perl5.lang.perl.psi.PerlPolyNamedElement;
import com.perl5.lang.perl.psi.stubs.PerlStubElementTypes;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import org.jetbrains.annotations.NotNull;

public class PerlLightStubUtil {
  @NotNull
  public static PerlDelegatingLightNamedElement createPsiElement(@NotNull StubBase stub, @NotNull PerlPolyNamedElement delegate) {
    if (stub instanceof PerlSubDefinitionStub) {
      return new PerlDelegatingSubElement(
        delegate,
        ((PerlSubDefinitionStub)stub).getSubName(),
        ((PerlSubDefinitionStub)stub).getPackageName(),
        ((PerlSubDefinitionStub)stub).getSubArgumentsList(),
        ((PerlSubDefinitionStub)stub).getAnnotations()
      );
    }
    throw new IllegalArgumentException("Don't know how to create psi from " + stub);
  }

  @NotNull
  public static StubBase createStub(@NotNull PerlDelegatingLightNamedElement lightNamedElement) {
    if (lightNamedElement instanceof PerlDelegatingSubElement) {
      //noinspection unchecked
      return (StubBase)PerlStubElementTypes.SUB_DEFINITION.createStub(lightNamedElement, null);
    }
    throw new IllegalArgumentException("Don't know how to create stub from " + lightNamedElement);
  }
}
