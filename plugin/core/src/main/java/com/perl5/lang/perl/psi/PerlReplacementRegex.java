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
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;

public interface PerlReplacementRegex extends PerlRegexExpression {
  default @Nullable PsiPerlPerlRegex getMatchRegex() {
    List<PsiElement> parts = getParts();
    return parts.isEmpty() ? null : ObjectUtils.tryCast(parts.get(0), PsiPerlPerlRegex.class);
  }

  default @Nullable PsiPerlRegexReplacement getReplaceRegex() {
    List<PsiElement> parts = getParts();
    return parts.size() < 2 ? null : ObjectUtils.tryCast(parts.get(1), PsiPerlRegexReplacement.class);
  }

  default @Nullable PerlBlock getReplaceBlock() {
    List<PsiElement> parts = getParts();
    return parts.size() < 2 ? null : ObjectUtils.tryCast(parts.get(1), PerlBlock.class);
  }

  /**
   * @return parts of regexp, regex and block or two regexes. Incomplete regex may have only one regex.
   * method flatterns lazy parsable blocks
   */
  default @NotNull List<PsiElement> getParts() {
    List<PsiElement> result = new ArrayList<>();
    PsiElement run = getFirstChild();
    while (run != null) {
      IElementType elementType = PsiUtilCore.getElementType(run);
      if (elementType == PERL_REGEX || elementType == BLOCK_BRACELESS || elementType == REGEX_REPLACEMENT) {
        result.add(run);
      }
      run = run.getNextSibling();
    }

    return result;
  }
}
