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

package com.perl5.lang.perl.psi.utils;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Processor;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PsiPerlCallArgumentsImpl;
import com.perl5.lang.perl.util.PerlArrayUtil;

import java.util.ArrayList;
import java.util.List;

public class PerlSubArgumentsExtractor implements Processor<PsiPerlStatement>, PerlElementPatterns {
  private List<PerlSubArgument> myArguments = new ArrayList<>();

  @Override
  public boolean process(PsiPerlStatement statement) {
    if (myArguments.isEmpty() && PerlPsiUtil.isSelfShortcutStatement(statement)) {
      myArguments.add(PerlSubArgument.self());
      return true;
    }
    else if (EMPTY_SHIFT_STATEMENT_PATTERN.accepts(statement)) {
      myArguments.add(myArguments.isEmpty() ? PerlSubArgument.self() : PerlSubArgument.empty());
      return true;
    }
    else if (DECLARATION_ASSIGNING_PATTERN.accepts(statement)) {
      PerlAssignExpression assignExpression = PsiTreeUtil.getChildOfType(statement, PerlAssignExpression.class);

      if (assignExpression == null) {
        return false;
      }

      PsiElement leftSide = assignExpression.getLeftSide();
      PsiElement rightSide = assignExpression.getRightSide();

      if (rightSide == null) {
        return false;
      }

      PerlVariableDeclarationExpr variableDeclaration = PsiTreeUtil.findChildOfType(leftSide, PerlVariableDeclarationExpr.class, false);

      if (variableDeclaration == null) {
        return false;
      }

      String variableClass = variableDeclaration.getDeclarationType();
      if (variableClass == null) {
        variableClass = "";
      }

      List<PsiElement> rightSideElements = PerlArrayUtil.collectListElements(rightSide);
      int sequenceIndex = 0;

      boolean processNextStatement = true;
      PsiElement run = variableDeclaration.getFirstChild();
      while (run != null) {
        PerlSubArgument newArgument = null;

        if (run instanceof PerlVariableDeclarationElement) {
          PerlVariable variable = ((PerlVariableDeclarationElement)run).getVariable();
          newArgument = PerlSubArgument.mandatory(
            variable.getActualType(),
            variable.getName(),
            variableClass
          );
        }
        else if (run.getNode().getElementType() == RESERVED_UNDEF) {
          newArgument = myArguments.isEmpty() ? PerlSubArgument.self() : PerlSubArgument.empty();
        }

        if (newArgument != null) {
          // we've found argument of left side
          if (rightSideElements.size() > sequenceIndex) {
            PsiElement rightSideElement = rightSideElements.get(sequenceIndex);
            boolean addArgument = false;

            if (SHIFT_PATTERN.accepts(rightSideElement)) // shift on the left side
            {
              assert rightSideElement instanceof PsiPerlSubCallExpr;
              PsiPerlCallArguments callArguments = ((PsiPerlSubCallExpr)rightSideElement).getCallArguments();
              List<PsiPerlExpr> argumentsList =
                callArguments == null ? null : ((PsiPerlCallArgumentsImpl)callArguments).getArgumentsList();
              if (argumentsList == null || argumentsList.isEmpty() || ALL_ARGUMENTS_PATTERN.accepts(argumentsList.get(0))) {
                addArgument = true;
                sequenceIndex++;
              }
            }
            else if (ALL_ARGUMENTS_ELEMENT_PATTERN.accepts(rightSideElement)) // $_[smth] on the left side
            {
              addArgument = true;
              sequenceIndex++;
            }
            else if (ALL_ARGUMENTS_PATTERN.accepts(rightSideElement))    // @_ on the left side
            {
              addArgument = true;
              processNextStatement = false;
            }

            if (addArgument) {
              myArguments.add(newArgument);
            }
          }
        }

        run = run.getNextSibling();
      }

      return processNextStatement;
    }
    return false;
  }

  public List<PerlSubArgument> getArguments() {
    return myArguments;
  }
}
