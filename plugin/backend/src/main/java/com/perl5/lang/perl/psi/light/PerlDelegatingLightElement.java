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

package com.perl5.lang.perl.psi.light;

import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.light.LightElement;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class PerlDelegatingLightElement<Delegate extends PsiElement> extends LightElement {
  private final IElementType myElementType;
  private final @NotNull Delegate myDelegate;

  public PerlDelegatingLightElement(@NotNull Delegate delegate, @NotNull IElementType elementType) {
    super(delegate.getManager(), delegate.getLanguage());
    myDelegate = delegate;
    myElementType = elementType;
  }

  public IElementType getElementType() {
    return myElementType;
  }

  public @NotNull Delegate getDelegate() {
    return myDelegate;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" + getElementType().toString() + ")";
  }

  @Override
  public PsiElement getFirstChild() {
    return getDelegate().getFirstChild();
  }

  @Override
  public PsiElement getLastChild() {
    return getDelegate().getLastChild();
  }

  @Override
  public final PsiReference getReference() {
    return getDelegate().getReference();
  }

  @Override
  public final PsiReference @NotNull [] getReferences() {
    return getDelegate().getReferences();
  }

  @Override
  public PsiReference findReferenceAt(int offset) {
    return getDelegate().findReferenceAt(offset);
  }

  @Override
  public PsiElement addRange(PsiElement first, PsiElement last) throws IncorrectOperationException {
    return getDelegate().addRange(first, last);
  }

  @Override
  public PsiElement addRangeBefore(@NotNull PsiElement first, @NotNull PsiElement last, PsiElement anchor)
    throws IncorrectOperationException {
    return getDelegate().addRangeBefore(first, last, anchor);
  }

  @Override
  public PsiElement addRangeAfter(PsiElement first, PsiElement last, PsiElement anchor) throws IncorrectOperationException {
    return getDelegate().addRangeAfter(first, last, anchor);
  }

  @Override
  public void deleteChildRange(PsiElement first, PsiElement last) throws IncorrectOperationException {
    getDelegate().deleteChildRange(first, last);
  }

  @Override
  public boolean textContains(char c) {
    return getDelegate().textContains(c);
  }

  @Override
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                     @NotNull ResolveState state,
                                     PsiElement lastParent,
                                     @NotNull PsiElement place) {
    return getDelegate().processDeclarations(processor, state, lastParent, place);
  }

  @Override
  public PsiElement getContext() {
    return getDelegate().getContext();
  }

  @Override
  public @NotNull GlobalSearchScope getResolveScope() {
    return getDelegate().getResolveScope();
  }

  @Override
  public @NotNull SearchScope getUseScope() {
    return getDelegate().getUseScope();
  }

  @Override
  public @NotNull Project getProject() {
    return getDelegate().getProject();
  }

  @Override
  public boolean isEquivalentTo(PsiElement another) {
    return getDelegate().isEquivalentTo(another);
  }

  @Override
  public @Nullable Icon getIcon(int flags) {
    return getDelegate().getIcon(flags);
  }

  @Override
  public @NotNull Language getLanguage() {
    return getDelegate().getLanguage();
  }

  @Override
  public PsiManager getManager() {
    return getDelegate().getManager();
  }

  @Override
  public PsiElement getParent() {
    return getDelegate();
  }

  @Override
  public PsiFile getContainingFile() {
    return getDelegate().getContainingFile();
  }

  @Override
  public TextRange getTextRange() {
    return getDelegate().getTextRange();
  }

  @Override
  public int getStartOffsetInParent() {
    return getDelegate().getStartOffsetInParent();
  }

  @Override
  public char @NotNull [] textToCharArray() {
    return getDelegate().textToCharArray();
  }

  @Override
  public boolean textMatches(@NotNull CharSequence text) {
    return getDelegate().textMatches(text);
  }

  @Override
  public boolean textMatches(@NotNull PsiElement element) {
    return getDelegate().textMatches(element);
  }

  @Override
  public PsiElement findElementAt(int offset) {
    return getDelegate().findElementAt(offset);
  }

  @Override
  public int getTextOffset() {
    return getDelegate().getTextOffset();
  }

  @Override
  public boolean isWritable() {
    return getDelegate().isWritable();
  }

  @Override
  public PsiElement add(@NotNull PsiElement element) throws IncorrectOperationException {
    return getDelegate().add(element);
  }

  @Override
  public PsiElement addBefore(@NotNull PsiElement element, PsiElement anchor) throws IncorrectOperationException {
    return getDelegate().addBefore(element, anchor);
  }

  @Override
  public PsiElement addAfter(@NotNull PsiElement element, PsiElement anchor) throws IncorrectOperationException {
    return getDelegate().addAfter(element, anchor);
  }

  @Override
  public void delete() throws IncorrectOperationException {
    getDelegate().delete();
  }

  @Override
  public PsiElement replace(@NotNull PsiElement newElement) throws IncorrectOperationException {
    return getDelegate().replace(newElement);
  }

  @Override
  public String getText() {
    return getDelegate().getText();
  }

  @Override
  public PsiElement copy() {
    return getDelegate().copy();
  }

  @Override
  public @NotNull PsiElement getNavigationElement() {
    return getDelegate().getNavigationElement();
  }

  @Override
  public PsiElement getPrevSibling() {
    return getDelegate().getPrevSibling();
  }

  @Override
  public PsiElement getNextSibling() {
    return getDelegate().getNextSibling();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof PerlDelegatingLightElement<?> lightElement)) {
      return false;
    }

    return myDelegate.equals(lightElement.myDelegate);
  }

  @Override
  public int hashCode() {
    return getDelegate().hashCode();
  }
}
