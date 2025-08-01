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

package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;


public interface PerlStringList extends PerlQuoted {
  /**
   * Collects string contents
   *
   * @return list of strings
   */
  @NotNull
  List<String> getStringContents();

  /**
   * @return true iff {@code element} is a string content inside the qw list
   */
  static boolean isListElement(@Nullable PsiElement element) {
    if (PsiUtilCore.getElementType(element) != STRING_CONTENT) {
      return false;
    }
    PsiElement parentElement = element.getParent();
    IElementType parentElementType = PsiUtilCore.getElementType(parentElement);
    if (parentElementType != STRING_BARE) {
      return false;
    }
    return PsiUtilCore.getElementType(parentElement.getParent()) == STRING_LIST;
  }
}
