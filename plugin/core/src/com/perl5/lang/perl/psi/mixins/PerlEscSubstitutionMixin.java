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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class PerlEscSubstitutionMixin extends PerlNumericCharSubstitution {
  public PerlEscSubstitutionMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  protected int getCharCode() {
    PsiElement child = getFirstChild();
    if (child == null) {
      return -1;
    }
    String text = child.getText();
    if (text.length() < 3) {
      return -1;
    }
    char lastChar = Character.toUpperCase(text.charAt(2));
    return (int)lastChar ^ 64;
  }
}
