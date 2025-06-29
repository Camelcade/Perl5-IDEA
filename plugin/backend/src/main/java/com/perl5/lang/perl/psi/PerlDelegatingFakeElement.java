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

package com.perl5.lang.perl.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.model.psi.PsiSymbolReference;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.FakePsiElement;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Fake wrapper for PsiElements, allowing e.g. to override presentation
 */
public class PerlDelegatingFakeElement extends FakePsiElement {
  private final @NotNull NavigatablePsiElement myDelegate;

  public PerlDelegatingFakeElement(@NotNull NavigatablePsiElement delegate) {
    myDelegate = delegate;
  }

  @Override
  public PsiElement getParent() {
    return myDelegate.getParent();
  }

  @Override
  public @NotNull TextRange getTextRangeInParent() {
    return myDelegate.getTextRangeInParent();
  }

  @Override
  public @NotNull Collection<? extends @NotNull PsiSymbolReference> getOwnReferences() {
    return myDelegate.getOwnReferences();
  }

  @Override
  public @Nullable String getText() {
    return myDelegate.getText();
  }

  @Override
  @Contract(pure = true)
  public @NotNull Project getProject() throws PsiInvalidElementAccessException {
    return myDelegate.getProject();
  }

  @Override
  @Contract(pure = true)
  public @NotNull Language getLanguage() {
    return myDelegate.getLanguage();
  }

  @Override
  @Contract(pure = true)
  public PsiManager getManager() {
    return myDelegate.getManager();
  }

  @Override
  @Contract(pure = true)
  public PsiElement @NotNull [] getChildren() {
    return myDelegate.getChildren();
  }

  @Override
  @Contract(pure = true)
  public @Nullable PsiElement getFirstChild() {
    return myDelegate.getFirstChild();
  }

  @Override
  @Contract(pure = true)
  public @Nullable PsiElement getLastChild() {
    return myDelegate.getLastChild();
  }

  @Override
  @Contract(pure = true)
  public @Nullable PsiElement getNextSibling() {
    return myDelegate.getNextSibling();
  }

  @Override
  @Contract(pure = true)
  public @Nullable PsiElement getPrevSibling() {
    return myDelegate.getPrevSibling();
  }

  @Override
  @Contract(pure = true)
  public PsiFile getContainingFile() throws PsiInvalidElementAccessException {
    return myDelegate.getContainingFile();
  }

  @Override
  @Contract(pure = true)
  public @Nullable TextRange getTextRange() {
    return myDelegate.getTextRange();
  }

  @Override
  @Contract(pure = true)
  public int getStartOffsetInParent() {
    return myDelegate.getStartOffsetInParent();
  }

  @Override
  @Contract(pure = true)
  public int getTextLength() {
    return myDelegate.getTextLength();
  }

  @Override
  @Contract(pure = true)
  public @Nullable PsiElement findElementAt(int offset) {
    return myDelegate.findElementAt(offset);
  }

  @Override
  @Contract(pure = true)
  public @Nullable PsiReference findReferenceAt(int offset) {
    return myDelegate.findReferenceAt(offset);
  }

  @Override
  @Contract(pure = true)
  public int getTextOffset() {
    return myDelegate.getTextOffset();
  }

  @Override
  @Contract(pure = true)
  public char @NotNull [] textToCharArray() {
    return myDelegate.textToCharArray();
  }

  @Override
  @Contract(pure = true)
  public @NotNull PsiElement getNavigationElement() {
    return myDelegate.getNavigationElement();
  }

  @Override
  @Contract(pure = true)
  public PsiElement getOriginalElement() {
    return myDelegate.getOriginalElement();
  }

  @Override
  @Contract(pure = true)
  public boolean textMatches(@NotNull CharSequence text) {
    return myDelegate.textMatches(text);
  }

  @Override
  @Contract(pure = true)
  public boolean textMatches(@NotNull PsiElement element) {
    return myDelegate.textMatches(element);
  }

  @Override
  @Contract(pure = true)
  public boolean textContains(char c) {
    return myDelegate.textContains(c);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    myDelegate.accept(visitor);
  }

  @Override
  public void acceptChildren(@NotNull PsiElementVisitor visitor) {
    myDelegate.acceptChildren(visitor);
  }

  @Override
  public PsiElement copy() {
    return myDelegate.copy();
  }

  @Override
  public PsiElement add(@NotNull PsiElement element) throws IncorrectOperationException {
    return myDelegate.add(element);
  }

  @Override
  public PsiElement addBefore(@NotNull PsiElement element, @Nullable PsiElement anchor) throws IncorrectOperationException {
    return myDelegate.addBefore(element, anchor);
  }

  @Override
  public PsiElement addAfter(@NotNull PsiElement element, @Nullable PsiElement anchor) throws IncorrectOperationException {
    return myDelegate.addAfter(element, anchor);
  }

  @Override
  @Deprecated
  public void checkAdd(@NotNull PsiElement element) throws IncorrectOperationException {
    myDelegate.checkAdd(element);
  }

  @Override
  public PsiElement addRange(PsiElement first, PsiElement last) throws IncorrectOperationException {
    return myDelegate.addRange(first, last);
  }

  @Override
  public PsiElement addRangeBefore(@NotNull PsiElement first, @NotNull PsiElement last, PsiElement anchor)
    throws IncorrectOperationException {
    return myDelegate.addRangeBefore(first, last, anchor);
  }

  @Override
  public PsiElement addRangeAfter(PsiElement first, PsiElement last, PsiElement anchor) throws IncorrectOperationException {
    return myDelegate.addRangeAfter(first, last, anchor);
  }

  @Override
  public void delete() throws IncorrectOperationException {
    myDelegate.delete();
  }

  @Override
  @Deprecated
  public void checkDelete() throws IncorrectOperationException {
    myDelegate.checkDelete();
  }

  @Override
  public void deleteChildRange(PsiElement first, PsiElement last) throws IncorrectOperationException {
    myDelegate.deleteChildRange(first, last);
  }

  @Override
  public PsiElement replace(@NotNull PsiElement newElement) throws IncorrectOperationException {
    return myDelegate.replace(newElement);
  }

  @Override
  @Contract(pure = true)
  public boolean isValid() {
    return myDelegate.isValid();
  }

  @Override
  @Contract(pure = true)
  public boolean isWritable() {
    return myDelegate.isWritable();
  }

  @Override
  @Contract(pure = true)
  public final @Nullable PsiReference getReference() {
    return myDelegate.getReference();
  }

  @Override
  @Contract(pure = true)
  public final PsiReference @NotNull [] getReferences() {
    return myDelegate.getReferences();
  }

  @Override
  @Contract(pure = true)
  public <T> @Nullable T getCopyableUserData(@NotNull Key<T> key) {
    return myDelegate.getCopyableUserData(key);
  }

  @Override
  public <T> void putCopyableUserData(@NotNull Key<T> key, @Nullable T value) {
    myDelegate.putCopyableUserData(key, value);
  }

  @Override
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                     @NotNull ResolveState state,
                                     @Nullable PsiElement lastParent, @NotNull PsiElement place) {
    return myDelegate.processDeclarations(processor, state, lastParent, place);
  }

  @Override
  @Contract(pure = true)
  public @Nullable PsiElement getContext() {
    return myDelegate.getContext();
  }

  @Override
  @Contract(pure = true)
  public boolean isPhysical() {
    return myDelegate.isPhysical();
  }

  @Override
  @Contract(pure = true)
  public @NotNull GlobalSearchScope getResolveScope() {
    return myDelegate.getResolveScope();
  }

  @Override
  @Contract(pure = true)
  public @NotNull SearchScope getUseScope() {
    return myDelegate.getUseScope();
  }

  @Override
  @Contract(pure = true)
  public @Nullable ASTNode getNode() {
    return myDelegate.getNode();
  }

  @Override
  @Contract(pure = true)
  public @NonNls String toString() {
    return myDelegate.toString();
  }

  @Override
  @Contract(pure = true)
  public boolean isEquivalentTo(PsiElement another) {
    return myDelegate.isEquivalentTo(another);
  }

  @Override
  public <T> @Nullable T getUserData(@NotNull Key<T> key) {
    return myDelegate.getUserData(key);
  }

  @Override
  public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {
    myDelegate.putUserData(key, value);
  }

  @Override
  public @Nullable String getName() {
    return myDelegate.getName();
  }

  @Override
  public void navigate(boolean requestFocus) {
    myDelegate.navigate(requestFocus);
  }

  @Override
  public boolean canNavigate() {
    return myDelegate.canNavigate();
  }

  @Override
  public boolean canNavigateToSource() {
    return myDelegate.canNavigateToSource();
  }
}
