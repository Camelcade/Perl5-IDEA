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

package com.perl5.lang.perl.idea.refactoring.introduce;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.refactoring.introduce.inplace.InplaceVariableIntroducer;
import org.jetbrains.annotations.NotNull;

public class PerlVariableIntroducer extends InplaceVariableIntroducer<PsiElement> {
  public PerlVariableIntroducer(@NotNull PsiNamedElement elementToRename,
                                @NotNull Editor editor,
                                PsiElement[] occurrences,
                                @NotNull String suggestedName) {
    super(elementToRename, editor, elementToRename.getProject(), "Introduce variable", occurrences, null);
    myInitialName = suggestedName;
    myOldName = suggestedName;
  }
}
