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

package com.perl5.lang.htmlmason;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.perl5.lang.mason2.idea.configuration.VariableDescription;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;

import java.util.List;

public final class MasonCoreUtilCore {
  private MasonCoreUtilCore() {
  }

  public static void fillVariablesList(PsiElement parent,
                                       List<? super PerlVariableDeclarationElement> targetList,
                                       List<? extends VariableDescription> sourceList) {
    for (VariableDescription variableDescription : sourceList) {
      String variableType = variableDescription.variableType;
      if (StringUtil.isEmpty(variableType)) {
        variableType = null;
      }
      targetList.add(
        PerlImplicitVariableDeclaration.createGlobal(
          parent,
          variableDescription.variableName,
          variableType
        ));
    }
  }
}
