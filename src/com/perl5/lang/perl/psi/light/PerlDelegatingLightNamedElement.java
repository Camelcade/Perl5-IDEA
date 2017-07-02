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

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.Function;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.psi.PerlPolyNamedElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents one of the declarations of {@link PerlPolyNamedElement}
 */
public class PerlDelegatingLightNamedElement<Delegate extends PerlPolyNamedElement> extends PerlDelegatingLightElement<Delegate>
  implements PsiNameIdentifierOwner {

  @NotNull
  protected String myName;

  @Nullable
  private PsiElement myNameIdentifier;

  @NotNull
  private Function<String, String> myNameComputation = name -> name;

  public PerlDelegatingLightNamedElement(@NotNull Delegate delegate,
                                         @NotNull String name,
                                         @NotNull IStubElementType elementType) {
    this(delegate, name, elementType, null);
  }

  public PerlDelegatingLightNamedElement(@NotNull Delegate delegate,
                                         @NotNull String name,
                                         @NotNull IStubElementType elementType,
                                         @Nullable PsiElement nameIdentifier) {
    super(delegate, elementType);
    myName = name;
    myNameIdentifier = nameIdentifier;
  }

  @NotNull
  @Override
  public String getName() {
    return myName;
  }

  @Override
  public IStubElementType getElementType() {
    return (IStubElementType)super.getElementType();
  }

  public PerlDelegatingLightNamedElement withNameComputation(@NotNull Function<String, String> nameComputation) {
    myNameComputation = nameComputation;
    return this;
  }

  @NotNull
  @Override
  public PsiElement getNameIdentifier() {
    if (myNameIdentifier != null && myNameIdentifier.isValid()) {
      return myNameIdentifier;
    }

    for (PerlDelegatingLightNamedElement element : getDelegate().calcLightElementsFromPsi()) {
      if (element.equals(this)) {
        return myNameIdentifier = element.getNameIdentifier();
      }
    }

    throw new PsiInvalidElementAccessException(this, myNameIdentifier == null
                                                     ? "Attempt to access element without identifier"
                                                     : "Could not restore valid identifier");
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String newBaseName) throws IncorrectOperationException {
    myNameIdentifier = ElementManipulators.handleContentChange(getNameIdentifier(), newBaseName);
    myName = getNameComputation().fun(newBaseName);
    return this;
  }

  @NotNull
  public Function<String, String> getNameComputation() {
    return myNameComputation;
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
  public TextRange getTextRange() {
    return getNameIdentifier().getTextRange();
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

    PerlDelegatingLightNamedElement<?> element = (PerlDelegatingLightNamedElement<?>)o;

    return getName().equals(element.getName());
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + getName().hashCode();
    return result;
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) {
      ((PerlVisitor)visitor).visitLightNamedElement(this);
    }
    else {
      super.accept(visitor);
    }
  }

  @NotNull
  @Override
  public PsiElement[] getChildren() {
    return super.getChildren();
  }

  @Override
  public ItemPresentation getPresentation() {
    return new PerlItemPresentationSimple(this, getName());
  }
}
