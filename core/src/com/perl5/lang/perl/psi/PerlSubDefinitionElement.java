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

package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.properties.PerlBlockOwner;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.psi.utils.PerlSubArgumentsExtractor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public interface PerlSubDefinitionElement extends PerlSubDefinition, PerlSubElement, PerlBlockOwner {
  @Override
  default String getPresentableName() {
    String args = getSubArgumentsListAsString();
    return this.getName() + (args.isEmpty() ? "()" : args);
  }

  /**
   * @return code block of this sub definition
   */
  @Nullable
  default PsiPerlBlock getSubDefinitionBody() {
    return null;
  }

  /**
   * @return sub arguments, extracted from PSI structure of code block
   */
  @NotNull
  default List<PerlSubArgument> getPerlSubArgumentsFromBody() {
    return getPerlSubArgumentsFromBody(getSubDefinitionBody());
  }

  @NotNull
  static List<PerlSubArgument> getPerlSubArgumentsFromBody(@Nullable PsiPerlBlock subBlock) {
    if (subBlock == null || !subBlock.isValid()) {
      return Collections.emptyList();
    }

    PerlSubArgumentsExtractor extractor = new PerlSubArgumentsExtractor();
    for (PsiElement statement : subBlock.getChildren()) {
      if (statement instanceof PsiPerlStatement) {
        if (!extractor.process((PsiPerlStatement)statement)) {
          break;
        }
      }
      else if (!(statement instanceof PerlAnnotation)) {
        break;
      }
    }

    return extractor.getArguments();
  }
}
