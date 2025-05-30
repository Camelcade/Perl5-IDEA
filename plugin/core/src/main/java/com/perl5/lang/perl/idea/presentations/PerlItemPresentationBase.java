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

package com.perl5.lang.perl.idea.presentations;

import com.intellij.navigation.ColoredItemPresentation;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.psi.PerlDeprecatable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public abstract class PerlItemPresentationBase implements ColoredItemPresentation {
  final PsiElement myElement;
  TextAttributesKey myAttributesKey;

  public PerlItemPresentationBase(PsiElement element) {
    myElement = element;
    myAttributesKey = myElement instanceof PerlDeprecatable deprecatable && deprecatable.isDeprecated()
                      ? CodeInsightColors.DEPRECATED_ATTRIBUTES
                      : null;
  }

  @Override
  public @Nullable String getLocationString() {
    if (!myElement.isValid()) {
      return null;
    }
    PsiFile containingFile = getElement().getContainingFile();
    return containingFile == null ? null : containingFile.getName();// + suffix;
  }

  @Override
  public @Nullable Icon getIcon(boolean unused) {
    return myElement.getIcon(0);
  }

  public PsiElement getElement() {
    return myElement;
  }

  @Override
  public @Nullable TextAttributesKey getTextAttributesKey() {
    return myAttributesKey;
  }

  public void setAttributesKey(TextAttributesKey myAttributesKey) {
    this.myAttributesKey = myAttributesKey;
  }
}
