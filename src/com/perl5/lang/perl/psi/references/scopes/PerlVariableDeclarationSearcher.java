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

package com.perl5.lang.perl.psi.references.scopes;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.BaseScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.PsiPerlStatement;
import com.perl5.lang.perl.psi.references.PerlBuiltInVariablesService;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 17.02.2016.
 */
public class PerlVariableDeclarationSearcher extends BaseScopeProcessor {
  private final String myName;
  private final PerlVariableType myVariableType;
  private final PsiElement myVariable;
  private PerlVariableDeclarationElement myResult;
  //	private PerlVariable myPossibleResult;

  public PerlVariableDeclarationSearcher(String name, PerlVariableType variableType, PsiElement anchorElement) {
    myName = name;
    myVariableType = variableType;
    myVariable = anchorElement;
  }

  public PerlVariableDeclarationSearcher(@NotNull PerlVariable variable) {
    this(variable.getName(), variable.getActualType(), variable);
  }

  @Override
  public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
    if (element instanceof PerlVariableDeclarationElement) {
      PerlVariable variable = ((PerlVariableDeclarationElement)element).getVariable();
      if (variable != null && !variable.equals(myVariable)) {
        if (myVariableType == variable.getActualType() && StringUtil.equals(myName, variable.getName())) {
          PsiElement declarationStatement = PsiTreeUtil.getParentOfType(element, PsiPerlStatement.class);

          if (declarationStatement == null || !PsiTreeUtil.isAncestor(declarationStatement, myVariable, false)) {
            myResult = (PerlVariableDeclarationElement)element;
            return false;
          }
        }
      }
    }
    return true;
  }

  public PerlVariableDeclarationElement getResult() {
    return myResult;
  }

  public boolean processBuiltIns() {
    PerlVariableDeclarationElement variableDeclaration =
      PerlBuiltInVariablesService.getInstance(myVariable.getProject()).getVariableDeclaration(myVariableType, myName);
    if (variableDeclaration == null) {
      return true;
    }
    myResult = variableDeclaration;
    return true;
  }
}
