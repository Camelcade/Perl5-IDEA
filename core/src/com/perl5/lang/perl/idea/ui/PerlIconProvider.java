/*
 * Copyright 2015-2018 Alexandr Evstigneev
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
import com.perl5.PerlIcons;
import com.perl5.lang.perl.psi.PerlSubExpr;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PerlIconProvider extends IconProvider {
  @Nullable
  @Override
  public Icon getIcon(@NotNull PsiElement element, int flags) {
    return getIcon(element);
  }

  @Nullable
  public static Icon getIcon(@Nullable PsiElement element) {
    if (element instanceof PerlSubExpr) {
      return PerlIcons.ANON_SUB_ICON;
    }
    if (element instanceof PerlVariable) {
      return getIcon(((PerlVariable)element).getActualType());
    }
    else if (element instanceof PerlVariableDeclarationElement) {
      return getIcon(((PerlVariableDeclarationElement)element).getActualType());
    }
    return null;
  }

  @Nullable
  public static Icon getIcon(@Nullable PerlVariableType actualType) {
    if (actualType == null) {
      return null;
    }
    switch (actualType) {
      case SCALAR:
        return PerlIcons.SCALAR_GUTTER_ICON;
      case ARRAY:
        return PerlIcons.ARRAY_GUTTER_ICON;
      case HASH:
        return PerlIcons.HASH_GUTTER_ICON;
      case GLOB:
        return PerlIcons.GLOB_GUTTER_ICON;
    }
    return null;
  }
}

