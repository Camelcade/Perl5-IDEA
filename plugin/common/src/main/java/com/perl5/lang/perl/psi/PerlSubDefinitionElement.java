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

import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.properties.PerlBlockOwner;
import com.perl5.lang.perl.psi.properties.PerlDieScope;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.psi.utils.PerlSubArgumentsExtractor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public interface PerlSubDefinitionElement extends PerlSubDefinition, PerlSubElement, PerlDieScope, PerlBlockOwner, NavigationItem {
  @Override
  default @Nullable String getPresentableName() {
    String args = getSubArgumentsListAsString();
    return this.getName() + (args.isEmpty() ? "()" : args);
  }

  /**
   * @return code block of this sub definition
   */
  default @Nullable PsiPerlBlock getSubDefinitionBody() {
    return null;
  }

  /**
   * @return sub arguments, extracted from PSI structure of code block
   */
  default @NotNull List<PerlSubArgument> getPerlSubArgumentsFromBody() {
    return getPerlSubArgumentsFromBody(getSubDefinitionBody());
  }

  static @NotNull List<PerlSubArgument> getPerlSubArgumentsFromBody(@Nullable PsiPerlBlock subBlock) {
    if (subBlock == null || !subBlock.isValid()) {
      return Collections.emptyList();
    }

    PerlSubArgumentsExtractor extractor = new PerlSubArgumentsExtractor();
    for (PsiElement statement : subBlock.getChildren()) {
      if (statement instanceof PsiPerlStatement perlStatement) {
        if (!extractor.process(perlStatement)) {
          break;
        }
      }
      else if (!(statement instanceof PerlAnnotation)) {
        break;
      }
    }

    return extractor.getArguments();
  }

  /**
   * @return a psi element for the control flow building if available
   */
  default @Nullable PsiElement getControlFlowElement() {
    return this;
  }
}
