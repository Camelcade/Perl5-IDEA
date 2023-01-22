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

package com.perl5.lang.perl.psi.light;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.Function;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.parser.PerlIdentifierRangeProvider;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents one of the declarations of {@link PerlPolyNamedElement}
 */
public class PerlDelegatingLightNamedElement<Delegate extends PerlPolyNamedElement<?>> extends PerlDelegatingLightElement<Delegate>
  implements PsiNameIdentifierOwner {
  public static final Function<String, String> DEFAULT_NAME_COMPUTATION = name -> name;

  protected @NotNull String myName;
  private @Nullable PsiElement myNameIdentifier;

  private boolean myIsImplicit = false;

  private @NotNull Function<String, String> myNameComputation = DEFAULT_NAME_COMPUTATION;

  public PerlDelegatingLightNamedElement(@NotNull Delegate delegate,
                                         @NotNull String name,
                                         @NotNull IStubElementType<?, ?> elementType) {
    this(delegate, name, elementType, null);
  }

  public PerlDelegatingLightNamedElement(@NotNull Delegate delegate,
                                         @NotNull String name,
                                         @NotNull IStubElementType<?, ?> elementType,
                                         @Nullable PsiElement nameIdentifier) {
    super(delegate, elementType);
    myName = name;
    myNameIdentifier = nameIdentifier;
  }

  @Override
  public @NotNull String getName() {
    return myName;
  }

  @Override
  public IStubElementType<?, PerlDelegatingLightNamedElement<?>> getElementType() {
    return (IStubElementType<?, PerlDelegatingLightNamedElement<?>>)super.getElementType();
  }

  protected final void setNameComputation(@NotNull Function<String, String> nameComputation) {
    myNameComputation = nameComputation;
  }

  @Override
  public @Nullable PsiElement getNameIdentifier() {
    if (myIsImplicit) {
      return null;
    }
    if (myNameIdentifier != null && myNameIdentifier.isValid()) {
      return myNameIdentifier;
    }

    for (PerlDelegatingLightNamedElement<?> element : getDelegate().computeLightElementsFromPsi()) {
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
    // fixme should be in com.perl5.lang.perl.psi.utils.PerlPsiUtil.renameElement()
    PsiElement nameIdentifier = getNameIdentifier();
    if (nameIdentifier == null) {
      return this;
    }
    ElementManipulator<PsiElement> manipulator = ElementManipulators.getManipulator(nameIdentifier);
    TextRange identifierRange = this instanceof PerlIdentifierRangeProvider
                                ? ((PerlIdentifierRangeProvider)this).getRangeInIdentifier()
                                : ElementManipulators.getValueTextRange(nameIdentifier);
    myNameIdentifier = manipulator.handleContentChange(nameIdentifier, identifierRange, newBaseName);
    myName = getNameComputation().fun(newBaseName);
    return this;
  }

  public @NotNull Function<String, String> getNameComputation() {
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

  public boolean isImplicit() {
    return myIsImplicit;
  }

  public void setImplicit(boolean implicit) {
    myIsImplicit = implicit;
  }

  @Override
  public @NotNull PsiElement getNavigationElement() {
    return myIsImplicit ? getDelegate() : Objects.requireNonNull(getNameIdentifier());
  }

  @Override
  public int getTextOffset() {
    return getNavigationElement().getTextOffset();
  }

  @Override
  public TextRange getTextRange() {
    return myIsImplicit ? super.getTextRange() : Objects.requireNonNull(getNameIdentifier()).getTextRange();
  }

  @Override
  public boolean isValid() {
    return getDelegate().isValid() && (myIsImplicit || myNameIdentifier == null || myNameIdentifier.isValid());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PerlDelegatingLightNamedElement)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    PerlDelegatingLightNamedElement<?> element = (PerlDelegatingLightNamedElement<?>)o;

    if (myIsImplicit != element.myIsImplicit) {
      return false;
    }
    return myName.equals(element.myName);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + myName.hashCode();
    result = 31 * result + (myIsImplicit ? 1 : 0);
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

  @Override
  public PsiElement @NotNull [] getChildren() {
    return super.getChildren();
  }

  @Override
  public ItemPresentation getPresentation() {
    return new PerlItemPresentationSimple(this, getName());
  }
}
