/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.completion.providers;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class PerlDelegatingVariableCompletionProcessor extends PerlVariableCompletionProcessor implements Cloneable {
  @NotNull
  private PerlVariableCompletionProcessor myDelegate;

  public PerlDelegatingVariableCompletionProcessor(@NotNull PerlVariableCompletionProcessor delegate) {
    myDelegate = delegate;
  }

  @Override
  protected @NotNull CompletionResultSet getResultSet() {
    return getDelegate().getResultSet();
  }

  @NotNull
  protected PerlVariableCompletionProcessor getDelegate() {
    return myDelegate;
  }

  @Override
  public @NotNull PsiElement getVariableNameElement() {
    return getDelegate().getVariableNameElement();
  }

  @Override
  public @NotNull PsiElement getVariableElement() {
    return getDelegate().getVariableElement();
  }

  @Override
  public boolean isFullQualified() {
    return getDelegate().isFullQualified();
  }

  @Override
  public boolean hasBraces() {
    return getDelegate().hasBraces();
  }

  @Override
  public boolean isDeclaration() {
    return getDelegate().isDeclaration();
  }

  @Override
  protected void addElement(@NotNull LookupElementBuilder lookupElement) {
    getDelegate().addElement(lookupElement);
  }

  @Override
  public boolean isForceShortMain() {
    return getDelegate().isForceShortMain();
  }

  @Override
  public boolean result() {
    return getDelegate().result();
  }

  @Override
  public @NotNull PerlVariableCompletionProcessor withPrefixMatcher(@NotNull String prefix) {
    try {
      PerlDelegatingVariableCompletionProcessor clone = (PerlDelegatingVariableCompletionProcessor)clone();
      clone.myDelegate = myDelegate.withPrefixMatcher(prefix);
      return clone;
    }
    catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }
}
