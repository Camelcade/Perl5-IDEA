/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlQuoted;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlUseVarsStringElementType extends PerlReparseableElementType {

  public PerlUseVarsStringElementType() {
    super("PARSABLE_STRING_USE_VARS");
  }


  @Override
  protected boolean isParentOk(@NotNull ASTNode parent) {
    return false;
  }

  @Override
  protected boolean isReparseableOld(@NotNull ASTNode parent,
                                     @NotNull CharSequence buffer,
                                     @NotNull Language fileLanguage,
                                     @NotNull Project project) {
    return false;
  }

  protected char getOpenQuoteCharacter(@Nullable ASTNode parent) {
    if (parent == null) {
      return 0;
    }
    PsiElement psiElement = parent.getPsi();
    return psiElement instanceof PerlQuoted ? ((PerlQuoted)psiElement).getOpenQuote() : 0;
  }
}
