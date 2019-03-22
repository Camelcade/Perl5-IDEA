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

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import org.jetbrains.annotations.Nullable;

/**
 * Represents match regexp and qr regexp
 */
public interface PerlSimpleRegex extends PsiElement {
  @Nullable
  default PsiPerlPerlRegex getRegex() {
    PsiElement[] children = getChildren();
    if (children.length == 0) {
      return null;
    }
    if (PerlTokenSets.LAZY_PARSABLE_REGEXPS.contains(PsiUtilCore.getElementType(children[0]))) {
      children = children[0].getChildren();
    }
    return children.length == 0 ? null : ObjectUtils.tryCast(children[0], PsiPerlPerlRegex.class);
  }

  @Nullable
  PsiPerlPerlRegexModifiers getPerlRegexModifiers();
}
