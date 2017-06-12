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

package com.perl5.lang.perl.psi.light;

import com.intellij.psi.*;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlPolyNamedElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents one of the declarations of {@link PerlPolyNamedElement}
 */
public class PerlDelegatingLightNamedElement<DelegatePsi extends PerlPolyNamedElement, MyStub extends StubElement>
  extends PerlDelegatingLightElement<DelegatePsi, MyStub>
  implements PsiNameIdentifierOwner {

  @NotNull
  private final String myName;

  @Nullable
  private PsiElement myNameIdentifier;

  public PerlDelegatingLightNamedElement(@NotNull MyStub stub, @NotNull String name) {
    super(stub);
    myName = name;
  }

  public PerlDelegatingLightNamedElement(@NotNull DelegatePsi delegate,
                                         @NotNull String name,
                                         @NotNull IStubElementType elementType,
                                         @NotNull PsiElement nameIdentifier) {
    super(delegate, elementType);
    myName = name;
    myNameIdentifier = nameIdentifier;
  }

  @NotNull
  @Override
  public final String getName() {
    return myName;
  }

  @NotNull
  @Override
  public PsiElement getNameIdentifier() {
    if (myNameIdentifier != null) {
      return myNameIdentifier;
    }

    for (PerlDelegatingLightNamedElement element : getDelegate().calcLightElements()) {
      if (element.equals(this)) {
        myNameIdentifier = element.getNameIdentifier();
      }
    }

    throw new PsiInvalidElementAccessException(this, "Attempt to access element without identifier");
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    return ElementManipulators.handleContentChange(getNameIdentifier(), name);
  }

  @Override
  public void navigate(boolean requestFocus) {
    PsiElement nameIdentifier = getNameIdentifier();
    if (nameIdentifier instanceof NavigatablePsiElement) {
      ((NavigatablePsiElement)nameIdentifier).navigate(requestFocus);
    }
    else {
      super.navigate(requestFocus);
    }
  }

  @NotNull
  @Override
  public PsiElement getNavigationElement() {
    return getNameIdentifier();
  }

  @Override
  public int getTextOffset() {
    return getNameIdentifier().getTextOffset();
  }

  @Override
  public boolean isValid() {
    return getDelegate().isValid() && (myNameIdentifier == null || myNameIdentifier.isValid());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PerlDelegatingLightNamedElement)) return false;
    if (!super.equals(o)) return false;

    PerlDelegatingLightNamedElement<?, ?> element = (PerlDelegatingLightNamedElement<?, ?>)o;

    return getName().equals(element.getName());
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + getName().hashCode();
    return result;
  }
}
