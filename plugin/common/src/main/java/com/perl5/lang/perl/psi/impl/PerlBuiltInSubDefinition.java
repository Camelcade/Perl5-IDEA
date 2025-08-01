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

package com.perl5.lang.perl.psi.impl;

import com.intellij.psi.PsiManager;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PerlBuiltInSubDefinition extends PerlImplicitSubDefinition {
  public PerlBuiltInSubDefinition(@NotNull PsiManager manager,
                                  @NotNull String subName,
                                  @NotNull String packageName,
                                  @NotNull List<PerlSubArgument> argumentList,
                                  @NotNull PerlValue returnValue) {
    super(manager, subName, packageName, argumentList, returnValue);
  }

  @Override
  public boolean isBuiltIn() {
    return true;
  }
}
