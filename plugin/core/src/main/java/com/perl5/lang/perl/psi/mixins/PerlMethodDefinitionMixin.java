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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PerlMethodDefinition;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.PsiPerlMethodSignatureInvocant;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.util.PerlScalarUtil.DEFAULT_SELF_SCALAR_NAME;


public abstract class PerlMethodDefinitionMixin extends PerlSubDefinitionBase implements PerlMethodDefinition {
  protected List<PerlVariableDeclarationElement> myImplicitVariables;

  public PerlMethodDefinitionMixin(@NotNull ASTNode node) {
    super(node);
  }

  public PerlMethodDefinitionMixin(@NotNull PerlSubDefinitionStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  protected @NotNull List<PerlVariableDeclarationElement> buildImplicitVariables() {
    return Collections.singletonList(PerlImplicitVariableDeclaration.createInvocant(this));
  }

  @Override
  public boolean isMethod() {
    return true;
  }

  @Override
  protected boolean processSignatureElement(PsiElement signatureElement, List<? super PerlSubArgument> arguments) {
    if (signatureElement instanceof PsiPerlMethodSignatureInvocant) {
      PerlVariable variable = PsiTreeUtil.findChildOfType(signatureElement, PerlVariable.class);
      if (variable != null) {
        arguments.add(PerlSubArgument.mandatory(variable.getActualType(), variable.getName()));
      }
    }
    else if (signatureElement instanceof PerlVariableDeclarationElement) {
      if (arguments.isEmpty()) {
        arguments.add(PerlSubArgument.mandatoryScalar(getDefaultInvocantName().substring(1)));
      }

      return super.processSignatureElement(signatureElement, arguments);
    }
    return false;
  }

  /**
   * Checks if method has an explicit invocant
   *
   * @return check result
   */
  private boolean hasExplicitInvocant() {
    PsiElement signatureContainer = getSignatureContent();
    return signatureContainer != null && signatureContainer.getFirstChild() instanceof PsiPerlMethodSignatureInvocant;
  }

  @Override
  public @NotNull List<PerlVariableDeclarationElement> getImplicitVariables() {
    if (hasExplicitInvocant()) {
      return Collections.emptyList();
    }
    else {
      if (myImplicitVariables == null) {
        myImplicitVariables = buildImplicitVariables();
      }
      return myImplicitVariables;
    }
  }

  @SuppressWarnings("SameReturnValue")
  public static @NotNull String getDefaultInvocantName() {
    // fixme see #717
    return DEFAULT_SELF_SCALAR_NAME;
  }
}
