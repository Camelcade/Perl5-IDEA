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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public interface PerlFileContextSubstitutor {
  @Contract("null->null;!null->!null")
  @Nullable PsiElement computeSubstitutedContext(@Nullable PsiElement originalContext);

  @Contract("null->null")
  static @Nullable PsiElement getContext(@Nullable PsiElement originalFileContext) {
    PerlFileContextSubstitutor service = ApplicationManager.getApplication().getService(PerlFileContextSubstitutor.class);
    return service == null ? originalFileContext : service.computeSubstitutedContext(originalFileContext);
  }
}
