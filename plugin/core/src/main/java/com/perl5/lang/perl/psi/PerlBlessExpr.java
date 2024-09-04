/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.util.PerlArrayUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents bless expression
 */
public interface PerlBlessExpr extends PsiPerlExpr {
  /**
   * @return first argument of bless expression if any
   */
  default @Nullable PsiElement getReferenceExpression() {
    List<PsiElement> unpackedChildren = PerlArrayUtil.collectChildrenList(this);
    return unpackedChildren.size() > 0 ? unpackedChildren.getFirst() : null;
  }

  /**
   * @return second argument of bless expression if any
   */
  default @Nullable PsiElement getBlessExpression() {
    List<PsiElement> unpackedChildren = PerlArrayUtil.collectChildrenList(this);
    return unpackedChildren.size() > 1 ? unpackedChildren.get(1) : null;
  }
}
