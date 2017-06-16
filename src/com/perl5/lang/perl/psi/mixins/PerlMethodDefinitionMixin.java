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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlVariableDeclarationLightElementImpl;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 10.11.2015.
 */
public abstract class PerlMethodDefinitionMixin extends PerlSubDefinitionBase implements PerlMethodDefinition {
  // fixme see the #717
  protected static final String DEFAULT_INVOCANT_NAME = "$self";
  protected List<PerlVariableDeclarationElement> myImplicitVariables;

  public PerlMethodDefinitionMixin(@NotNull ASTNode node) {
    super(node);
  }

  public PerlMethodDefinitionMixin(@NotNull PerlSubDefinitionStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @NotNull
  protected List<PerlVariableDeclarationElement> buildImplicitVariables() {
    List<PerlVariableDeclarationElement> newImplicitVariables = new ArrayList<PerlVariableDeclarationElement>();
    if (isValid()) {
      newImplicitVariables.add(new PerlVariableDeclarationLightElementImpl(
        getManager(),
        PerlLanguage.INSTANCE,
        getDefaultInvocantName(),
        true,
        false,
        true,
        this
      ));
    }
    return newImplicitVariables;
  }

  @Override
  public boolean isMethod() {
    return true;
  }

  @Nullable
  @Override
  protected PsiElement getSignatureContainer() {
    return getMethodSignatureContent();
  }

  @Override
  protected boolean processSignatureElement(PsiElement signatureElement, List<PerlSubArgument> arguments) {
    if (signatureElement instanceof PsiPerlMethodSignatureInvocant)    // explicit invocant
    {
      PerlVariable variable = PsiTreeUtil.findChildOfType(signatureElement, PerlVariable.class);
      if (variable != null) {
        arguments.add(PerlSubArgument.mandatory(variable.getActualType(), variable.getName()));
      }
    }
    else if (signatureElement instanceof PerlVariableDeclarationElement) {
      if (arguments.isEmpty()) // implicit invocant
      {
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
    PsiPerlMethodSignatureContent methodSignatureContent = getMethodSignatureContent();
    return methodSignatureContent != null && methodSignatureContent.getFirstChild() instanceof PsiPerlMethodSignatureInvocant;
  }

  @NotNull
  @Override
  public List<PerlVariableDeclarationElement> getImplicitVariables() {
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

  @NotNull
  public static String getDefaultInvocantName() {
    return DEFAULT_INVOCANT_NAME;
  }
}
