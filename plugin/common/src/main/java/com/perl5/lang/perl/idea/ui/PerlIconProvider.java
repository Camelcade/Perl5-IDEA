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

package com.perl5.lang.perl.idea.ui;

import com.intellij.ide.IconProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.psi.PerlSubExpr;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;

public class PerlIconProvider extends IconProvider {
  @Override
  public @Nullable Icon getIcon(@NotNull PsiElement element, int flags) {
    return getIcon(element);
  }

  public static @Nullable Icon getIcon(@Nullable PsiElement element) {
    if (element instanceof PerlSubExpr) {
      return PerlIcons.ANON_SUB_ICON;
    }
    if (element instanceof PerlVariable perlVariable) {
      return getIcon(perlVariable.getActualType());
    }
    else if (element instanceof PerlVariableDeclarationElement variableDeclarationElement) {
      return getIcon(variableDeclarationElement.getActualType());
    }
    IElementType elementType = PsiUtilCore.getElementType(element);
    if (elementType == AFTER_MODIFIER) {
      return PerlIcons.AFTER_MODIFIER_GUTTER_ICON;
    }
    if (elementType == BEFORE_MODIFIER) {
      return PerlIcons.BEFORE_MODIFIER_GUTTER_ICON;
    }
    if (elementType == AROUND_MODIFIER) {
      return PerlIcons.AROUND_MODIFIER_GUTTER_ICON;
    }
    if (elementType == AUGMENT_MODIFIER) {
      return PerlIcons.AUGMENT_MODIFIER_GUTTER_ICON;
    }
    return null;
  }

  public static @Nullable Icon getIcon(@Nullable PerlVariableType actualType) {
    return actualType == null ? null : actualType.getIcon();
  }
}

