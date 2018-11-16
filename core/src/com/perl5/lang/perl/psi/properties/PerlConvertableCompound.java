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

package com.perl5.lang.perl.psi.properties;

import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PsiPerlBlock;
import com.perl5.lang.perl.psi.mixins.PerlStatementMixin;

/**
 * Marker interface for compound statements that may be converted to statement modifiers:
 * if/unless
 * while/until
 * for/foreach
 * when
 */
public interface PerlConvertableCompound extends PerlCompound {
  /**
   * @return true if compound may be converted to the statement modifier
   */
  default boolean isConvertableToModifier() {
    PsiPerlBlock mainBlock = getBlock();
    if (mainBlock == null) {
      return false;
    }
    PsiElement[] children = mainBlock.getChildren();
    return children.length == 1
           && children[0] instanceof PerlStatementMixin &&
           ((PerlStatementMixin)children[0]).getModifier() == null;
  }
}
