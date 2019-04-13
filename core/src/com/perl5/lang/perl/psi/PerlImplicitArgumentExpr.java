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

package com.perl5.lang.perl.psi;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an expression which may have an argument or use the implicit one
 */
public interface PerlImplicitArgumentExpr extends PsiPerlExpr {
  @NotNull
  default PsiElement getArgument() {
    PsiElement[] children = getChildren();
    if (children.length == 0) {
      return getImplicitArgument();
    }
    if (children.length > 1) {
      Logger.getInstance(PerlImplicitArgumentExpr.class).error("Got more than one child from expression: " + this + "; " + getText());
    }
    return children[0];
  }

  /**
   * @return an implicit argument for this expression, e.g. {@code $_} or {@code @_}
   */
  @NotNull
  PsiElement getImplicitArgument();
}
