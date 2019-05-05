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

package com.perl5.lang.tt2.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import com.perl5.lang.tt2.psi.TemplateToolkitPerlBlockElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class TemplateToolkitPerlBlockElementImpl extends TemplateToolkitCompositeElementImpl implements TemplateToolkitPerlBlockElement {
  private List<PerlVariableDeclarationElement> myImplicitVariables = null;

  public TemplateToolkitPerlBlockElementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @NotNull
  protected List<PerlVariableDeclarationElement> buildImplicitVariables() {
    List<PerlVariableDeclarationElement> variables = new ArrayList<>();
    variables.add(PerlImplicitVariableDeclaration.createLexical(this, "$context", "Template::Context"));
    variables.add(PerlImplicitVariableDeclaration.createLexical(this, "$stash", "Template::Stash"));
    return variables;
  }

  @NotNull
  @Override
  public List<PerlVariableDeclarationElement> getImplicitVariables() {
    if (myImplicitVariables == null) {
      myImplicitVariables = buildImplicitVariables();
    }
    return myImplicitVariables;
  }

  @Override
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                     @NotNull ResolveState state,
                                     PsiElement lastParent,
                                     @NotNull PsiElement place) {
    return PerlResolveUtil.processChildren(
      this,
      processor,
      state,
      lastParent,
      place
    );
  }
}
