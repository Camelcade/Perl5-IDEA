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

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.light.LightElement;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 26.11.2016.
 */
public class PerlDelegatingLightElement<Delegate extends PsiElement> extends LightElement {
  private final Delegate myDelegate;

  public PerlDelegatingLightElement(Delegate delegate) {
    super(delegate.getManager(), delegate.getLanguage());
    myDelegate = delegate;
  }

  public Delegate getDelegate() {
    return myDelegate;
  }

  @Override
  public String toString() {
    return getDelegate().toString();
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
  public void acceptChildren(@NotNull PsiElementVisitor visitor) {
    getDelegate().acceptChildren(visitor);
  }

  @Override
  public PsiReference getReference() {
    return getDelegate().getReference();
  }

  @NotNull
  @Override
  public PsiReference[] getReferences() {
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
  public PsiElement getOriginalElement() {
    return getDelegate().getOriginalElement();
  }

  @NotNull
  @Override
  public GlobalSearchScope getResolveScope() {
    return getDelegate().getResolveScope();
  }

  @NotNull
  @Override
  public SearchScope getUseScope() {
    return getDelegate().getUseScope();
  }

  @NotNull
  @Override
  public Project getProject() {
    return getDelegate().getProject();
  }

  @Override
  public boolean isEquivalentTo(PsiElement another) {
    return getDelegate().isEquivalentTo(another);
  }

  @Nullable
  @Override
  public Icon getIcon(int flags) {
    return getDelegate().getIcon(flags);
  }

  @NotNull
  @Override
  public Language getLanguage() {
    return getDelegate().getLanguage();
  }

  @Override
  public PsiManager getManager() {
    return getDelegate().getManager();
  }

  @Override
  public PsiElement getParent() {
    return getDelegate().getParent();
  }

  @NotNull
  @Override
  public PsiElement[] getChildren() {
    return getDelegate().getChildren();
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

  @NotNull
  @Override
  public char[] textToCharArray() {
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
  public void checkAdd(@NotNull PsiElement element) throws IncorrectOperationException {
    getDelegate().checkAdd(element);
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
  public void checkDelete() throws IncorrectOperationException {
    getDelegate().checkDelete();
  }

  @Override
  public PsiElement replace(@NotNull PsiElement newElement) throws IncorrectOperationException {
    return getDelegate().replace(newElement);
  }

  @Override
  public ASTNode getNode() {
    return getDelegate().getNode();
  }

  @Override
  public String getText() {
    return getDelegate().getText();
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    getDelegate().accept(visitor);
  }

  @Override
  public PsiElement copy() {
    return getDelegate().copy();
  }

  @NotNull
  @Override
  public PsiElement getNavigationElement() {
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
  public int hashCode() {
    return getDelegate().hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PerlDelegatingLightElement<?> element = (PerlDelegatingLightElement<?>)o;

    return getDelegate().equals(element.getDelegate());
  }
}
