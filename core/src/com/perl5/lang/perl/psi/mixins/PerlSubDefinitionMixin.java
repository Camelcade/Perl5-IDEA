/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PsiPerlSubDefinition;
import com.perl5.lang.perl.psi.PsiPerlSubSignatureElementIgnore;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public abstract class PerlSubDefinitionMixin extends PerlSubDefinitionBase implements PsiPerlSubDefinition {
  public PerlSubDefinitionMixin(@NotNull ASTNode node) {
    super(node);
  }

  public PerlSubDefinitionMixin(@NotNull PerlSubDefinitionStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }


  @Override
  protected boolean processSignatureElement(PsiElement signatureElement, List<PerlSubArgument> arguments) {
    if (!super.processSignatureElement(signatureElement, arguments)) {
      if (signatureElement instanceof PsiPerlSubSignatureElementIgnore) {
        PerlSubArgument newArgument = PerlSubArgument.empty();
        newArgument.setOptional(signatureElement.getFirstChild() != signatureElement.getLastChild()); // has elements inside, means optional
        arguments.add(newArgument);
      }
      else if (signatureElement.getNode().getElementType() == PerlElementTypes.OPERATOR_ASSIGN && arguments.size() > 0) {
        // setting last element as optional
        arguments.get(arguments.size() - 1).setOptional(true);
      }
      else {
        return false;
      }
    }
    return true;
  }

  @Nullable
  protected PsiElement getSignatureContainer() {
    return getSubSignature();
  }
}
