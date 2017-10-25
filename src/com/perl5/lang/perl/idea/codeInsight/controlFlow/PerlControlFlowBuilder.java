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

package com.perl5.lang.perl.idea.codeInsight.controlFlow;

import com.intellij.codeInsight.controlflow.ControlFlow;
import com.intellij.codeInsight.controlflow.ControlFlowBuilder;
import com.intellij.codeInsight.controlflow.Instruction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PsiPerlAssignExprImpl;
import com.perl5.lang.perl.psi.impl.PsiPerlCommaSequenceExprImpl;
import com.perl5.lang.perl.psi.impl.PsiPerlParenthesisedExprImpl;
import com.perl5.lang.perl.psi.mixins.PerlVariableDeclarationExprMixin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class PerlControlFlowBuilder extends ControlFlowBuilder {

  public ControlFlow build(PsiElement element) {
    return super.build(new PerlControlFlowVisitor(), element);
  }

  private Instruction read(@NotNull PsiElement element) {
    return new PerlReadInstruction(PerlControlFlowBuilder.this, element);
  }

  private Instruction write(@NotNull PsiElement variable, @Nullable PsiElement valueExpr) {
    return write(variable, valueExpr, 0);
  }

  private Instruction write(@NotNull PsiElement variable, @Nullable PsiElement valueExpr, int pos) {
    return new PerlWriteInstruction(this, variable, valueExpr, pos);
  }

  private Instruction readWrite(@NotNull PsiElement variable, @Nullable PsiElement valueExpr) {
    return new PerlWriteWithPrefixReadInstruction(this, variable, valueExpr, 0);
  }

  private Instruction writeRead(@NotNull PsiElement variable, @Nullable PsiElement valueExpr) {
    return writeRead(variable, valueExpr, 0);
  }

  private Instruction writeRead(@NotNull PsiElement variable, @Nullable PsiElement valueExpr, int pos) {
    return new PerlWriteWithPostfixReadInstruction(this, variable, valueExpr, pos);
  }

  @Override
  public Instruction startNode(@Nullable PsiElement element) {
    final Instruction instruction = new PerlDumbInstruction(this, element);
    addNode(instruction);
    checkPending(instruction);
    return instruction;
  }

  public static ControlFlow getFor(@NotNull PsiElement element) {
    return CachedValuesManager
      .getCachedValue(element, () -> CachedValueProvider.Result.create(new PerlControlFlowBuilder().build(element), element));
  }

  private class PerlControlFlowVisitor extends PerlRecursiveVisitor {
    @Override
    public void visitPerlVariable(@NotNull PerlVariable o) {
      addNode(createContextDependentInstruction(o));
    }

    @Override
    public void visitGlobVariable(@NotNull PsiPerlGlobVariable o) {
      addNode(createContextDependentInstruction(o));
    }

    private Instruction createContextDependentInstruction(@NotNull PsiElement variable) {
      PsiElement containingElement = variable.getParent();
      if (containingElement instanceof PsiPerlAssignExprImpl) {
        return createAssignAccessInstruction(variable, (PsiPerlAssignExpr)containingElement, 0);
      }
      else if (containingElement instanceof PerlVariableDeclaration) { // wrapper
        PsiElement declaration = containingElement.getParent();
        if (declaration instanceof PerlVariableDeclarationExprMixin) { // my/our/local/state
          PsiElement assignExpr = declaration.getParent();
          if (assignExpr instanceof PsiPerlAssignExprImpl) {
            List<PsiElement> declarationElements = Arrays.asList(declaration.getChildren());
            return createAssignAccessInstruction(variable, (PsiPerlAssignExpr)assignExpr, declarationElements.indexOf(containingElement));
          }
        }
      }
      else if (containingElement instanceof PsiPerlCommaSequenceExprImpl) {
        PsiElement parenthesizedExpression = containingElement.getParent();
        if (parenthesizedExpression instanceof PsiPerlParenthesisedExprImpl) {
          PsiElement assignExpr = parenthesizedExpression.getParent();
          if (assignExpr instanceof PsiPerlAssignExpr) {
            List<PsiElement> listElements = Arrays.asList(containingElement.getChildren());
            return createAssignAccessInstruction(variable, (PsiPerlAssignExpr)assignExpr, listElements.indexOf(variable));
          }
        }
      }

      return read(variable);
    }

    /**
     * @param variable      variable element
     * @param assignExpr    assign expression element
     * @param variableIndex index in parallel assignment sequence. 0 for single assignments
     * @return simple assignment write access, non parallel; read access if something went wrong
     */
    @NotNull
    private Instruction createAssignAccessInstruction(@NotNull PsiElement variable,
                                                      @NotNull PsiPerlAssignExpr assignExpr,
                                                      int variableIndex) {
      List<PsiPerlExpr> expressions = assignExpr.getExprList();
      assert !expressions.isEmpty();
      int i;

      for (i = 0; i < expressions.size(); i++) {
        PsiPerlExpr expression = expressions.get(i);
        if (PsiTreeUtil.isAncestor(expression, variable, false)) {
          break;
        }
      }

      if (i == expressions.size()) { // unknown
        return read(variable);
      }
      else if (i == 0) { // first
        return write(variable, expressions.get(1), variableIndex); // fixme check modifying assignments
      }
      else if (i == expressions.size() - 1) { // last
        return read(variable);
      }
      return writeRead(variable, expressions.get(i + 1), variableIndex);
    }


    @Override
    public void visitAssignExpr(@NotNull PsiPerlAssignExpr o) {
      startNode(o);
      PsiElement run = o.getLastChild();
      while (run != null) {
        run.accept(this);
        run = run.getPrevSibling();
      }
    }


    @Override
    public void visitElement(@NotNull PsiElement element) {
      if (!(element instanceof LeafPsiElement)) {
        startNode(element);
        super.visitElement(element);
      }
    }
  }
}
