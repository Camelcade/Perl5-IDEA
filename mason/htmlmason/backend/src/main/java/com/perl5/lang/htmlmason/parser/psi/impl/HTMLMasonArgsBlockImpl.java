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

package com.perl5.lang.htmlmason.parser.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonArgsBlock;
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonArgsBlockStub;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.FAT_COMMA;


public class HTMLMasonArgsBlockImpl extends HTMLMasonStubBasedElement<HTMLMasonArgsBlockStub> implements HTMLMasonArgsBlock {
  public HTMLMasonArgsBlockImpl(@NotNull HTMLMasonArgsBlockStub stub, @NotNull IElementType nodeType) {
    super(stub, nodeType);
  }

  public HTMLMasonArgsBlockImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public @NotNull List<PerlSubArgument> getArgumentsList() {
    HTMLMasonArgsBlockStub stub = getGreenStub();
    if (stub != null) {
      return stub.getArgumentsList();
    }

    return getArgumentsListHeavy();
  }

  protected @NotNull List<PerlSubArgument> getArgumentsListHeavy() {
    List<PerlSubArgument> result = new ArrayList<>();
    PsiElement run = getFirstChild();

    while (run != null) {
      if (run instanceof PerlVariableDeclarationElement variableDeclarationElement) {
        PerlVariable variable = variableDeclarationElement.getVariable();
        PsiElement nextSibling = PerlPsiUtil.getNextSignificantSibling(run);
        result.add(PerlSubArgument.create(
          variable.getActualType(),
          variable.getName(),
          nextSibling != null && nextSibling.getNode().getElementType() == FAT_COMMA
        ));
      }
      run = run.getNextSibling();
    }

    return result;
  }
}
