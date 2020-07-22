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

package com.perl5.lang.perl.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Misc helper methods
 */
public class PerlUtil implements PerlElementTypes {

  public static @NotNull String getParentsChain(@Nullable PsiElement element) {
    if (element == null) {
      return "null";
    }
    StringBuilder sb = new StringBuilder();
    while (true) {
      sb.append(element.getClass()).append("(").append(PsiUtilCore.getElementType(element)).append(")").append(": ");
      if (element instanceof PsiFile || element.getParent() == null) {
        break;
      }
      element = element.getParent();
    }
    return sb.toString();
  }
}
