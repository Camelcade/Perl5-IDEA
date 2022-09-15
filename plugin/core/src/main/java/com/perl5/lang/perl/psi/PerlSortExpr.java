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

package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.properties.PerlBlockOwner;
import com.perl5.lang.perl.psi.properties.PerlLabelScope;
import com.perl5.lang.perl.psi.properties.PerlReturnScope;
import org.jetbrains.annotations.Nullable;


public interface PerlSortExpr extends PsiElement, PerlLabelScope, PerlBlockOwner, PerlReturnScope {

  default @Nullable PsiPerlMethod getMethod() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlMethod.class);
  }

  /**
   * @return psi element to sort if any
   */
  default @Nullable PsiElement getTarget() {
    PsiElement[] children = getChildren();
    return children.length == 2 ? children[1] : null;
  }
}
