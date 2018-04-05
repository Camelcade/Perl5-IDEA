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

import com.intellij.codeInsight.controlflow.ConditionalInstruction;
import com.intellij.codeInsight.controlflow.ControlFlow;
import com.intellij.codeInsight.controlflow.ControlFlowBuilder;
import com.intellij.codeInsight.controlflow.Instruction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mixins.PerlStatementMixin;
import com.perl5.lang.perl.psi.mixins.PerlSubDefinitionBase;
import com.perl5.lang.perl.psi.properties.PerlBlockOwner;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;

public class PerlControlFlowBuilder extends ControlFlowBuilder {
  private static final Set<String> DIE_SUBS = new THashSet<>(Arrays.asList(
    "die",
    "croak",
    "confess"
  ));

  private static final TokenSet LOOP_MODIFIERS = TokenSet.create(
    FOR_STATEMENT_MODIFIER,
    UNTIL_STATEMENT_MODIFIER,
    WHILE_STATEMENT_MODIFIER
  );

  /**
   * Modifiers with false branch, instead of true
   */
  private static final TokenSet FALSE_VALUE_MODIFIERS = TokenSet.create(
    UNLESS_STATEMENT_MODIFIER, UNTIL_STATEMENT_MODIFIER
  );

  private Instruction myLastModifierExpressionInstruction;

  public ControlFlow build(PsiElement element) {
    return super.build(new PerlControlFlowVisitor(), element);
  }

  public static ControlFlow getFor(@NotNull PsiElement element) {
    return CachedValuesManager
      .getCachedValue(element, () -> CachedValueProvider.Result.create(new PerlControlFlowBuilder().build(element), element));
  }

  @SuppressWarnings("UnusedReturnValue")
  public Instruction startPartialConditionalNode(@NotNull PsiElement element,
                                                 @NotNull PsiElement condition,
                                                 @NotNull PsiElement lastConditionElement,
                                                 final boolean result) {
    final ConditionalInstruction instruction =
      new PartialConditionalInstructionImpl(this, element, condition, lastConditionElement, result);
    addNodeAndCheckPending(instruction);
    return instruction;
  }

  // fixme here-doc opener
  private class PerlControlFlowVisitor extends PerlRecursiveVisitor {

    @Override
    public void visitExpr(@NotNull PsiPerlExpr o) {
      PsiElement run = o.getFirstChild();
      PsiElement lastRun = null;
      while (run != null) {
        if (!PerlPsiUtil.isCommentOrSpace(run)) {
          run.accept(this);
          IElementType elementType = PsiUtilCore.getElementType(run);
          if (lastRun != null) {
            if (elementType == OPERATOR_AND || elementType == OPERATOR_AND_LP) {
              addPendingEdge(o, prevInstruction);
              startPartialConditionalNode(run, o, lastRun, true);
            }
            else if (elementType == OPERATOR_OR || elementType == OPERATOR_OR_LP || elementType == OPERATOR_OR_DEFINED) {
              addPendingEdge(o, prevInstruction);
              startPartialConditionalNode(run, o, lastRun, false);
            }
          }
        }
        lastRun = run;
        run = run.getNextSibling();
      }
      startNode(o);
    }

    @Override
    public void visitAssignExpr(@NotNull PsiPerlAssignExpr o) {
      PsiElement rightSide = o.getLastChild();
      while (true) {
        PsiElement operator = PerlPsiUtil.getPrevSignificantSibling(rightSide);
        if (operator == null) {
          return;
        }
        PsiElement leftSide = PerlPsiUtil.getPrevSignificantSibling(operator);
        if (leftSide == null) {
          return;
        }
        IElementType operatorType = PsiUtilCore.getElementType(operator);
        if (operatorType != OPERATOR_ASSIGN) {
          leftSide.accept(this);
        }
        if (operatorType == OPERATOR_AND_ASSIGN) {
          addPendingEdge(o, prevInstruction);
          startConditionalNode(o, leftSide, true);
        }
        else if (operatorType == OPERATOR_OR_ASSIGN || operatorType == OPERATOR_OR_DEFINED_ASSIGN) {
          addPendingEdge(o, prevInstruction);
          startConditionalNode(o, leftSide, false);
        }

        rightSide.accept(this);
        addNodeAndCheckPending(new PerlAssignInstuction(PerlControlFlowBuilder.this, o, leftSide, rightSide, operator));
        rightSide = leftSide;
      }
    }

    @Override
    public void visitSubCallExpr(@NotNull PsiPerlSubCallExpr o) {
      PsiPerlMethod method = o.getMethod();
      if (method == null) {
        super.visitSubCallExpr(o);
        return;
      }

      // this is assumption, because we are building flow on stubbing and can't resolve. For now...
      //List<PsiElement> targetElements = method.getTargetElements();
      PerlSubNameElement subNameElement = method.getSubNameElement();
      if (subNameElement != null && DIE_SUBS.contains(subNameElement.getText())) {
        PsiPerlCallArguments arguments = o.getCallArguments();
        if (arguments != null) {
          arguments.accept(this);
        }
        PsiElement dieScope = getDieScope(o);
        startNode(o);
        addPendingEdge(dieScope, prevInstruction);
        flowAbrupted();
      }
      else {
        super.visitSubCallExpr(o);
      }
    }

    /**
     * @return neares scope for die/croak/confess
     */
    @NotNull
    private PsiElement getDieScope(@NotNull PsiElement element) {
      return getNestedBlockOrElement(
        Objects.requireNonNull(
          PsiTreeUtil.getParentOfType(
            element,
            PerlSubExpr.class,
            PerlEvalExpr.class,
            PerlSubDefinitionBase.class,
            PsiFile.class
          )));
    }


    @Override
    public void visitStatement(@NotNull PsiPerlStatement o) {
      if (o instanceof PerlStatementMixin) {
        PsiPerlStatementModifier modifier = ((PerlStatementMixin)o).getModifier();
        Instruction modifierExpression = null;
        if (modifier != null) {
          modifier.accept(this);
          modifierExpression = myLastModifierExpressionInstruction;
        }
        PsiPerlExpr expr = o.getExpr();
        if (expr != null) {
          expr.accept(this);
        }

        if (LOOP_MODIFIERS.contains(PsiUtilCore.getElementType(modifier))) {
          addEdge(prevInstruction, modifierExpression);
          prevInstruction = modifierExpression;
        }
      }
      else {
        super.visitStatement(o);
      }
    }

    @Override
    public void visitBlock(@NotNull PsiPerlBlock o) {
      o.acceptChildren(this);
    }

    @Override
    public void visitParenthesisedCallArguments(@NotNull PsiPerlParenthesisedCallArguments o) {
      o.acceptChildren(this);
    }

    @Override
    public void visitCallArguments(@NotNull PsiPerlCallArguments o) {
      o.acceptChildren(this);
    }

    @Override
    public void visitReturnExpr(@NotNull PsiPerlReturnExpr o) {
      PsiPerlExpr returnValueExpr = o.getReturnValueExpr();
      if (returnValueExpr != null) {
        returnValueExpr.accept(this);
      }
      startNode(o);
      addPendingEdge(getReturnScope(o), prevInstruction);
      flowAbrupted();
    }

    @NotNull
    private PsiElement getReturnScope(@NotNull PsiPerlReturnExpr o) {
      return getNestedBlockOrElement(o.getReturnScope());
    }

    /**
     * @return nested block if any
     */
    @NotNull
    private PsiElement getNestedBlockOrElement(@NotNull PsiElement element) {
      if (element instanceof PerlSubDefinitionElement) {
        PsiPerlBlock body = ((PerlSubDefinitionElement)element).getSubDefinitionBody();
        if (body != null) {
          return body;
        }
      }
      else if (element instanceof PerlBlockOwner) {
        PsiPerlBlock block = ((PerlBlockOwner)element).getBlock();
        if (block != null) {
          return block;
        }
      }
      return element;
    }

    @Override
    public void visitPerlStatementModifier(@NotNull PerlStatementModifier o) {
      PsiPerlExpr condition = o.getExpr();
      if (condition != null) {
        condition.accept(this);
        myLastModifierExpressionInstruction = prevInstruction;
      }
      addPendingEdge(o.getParent(), prevInstruction);
      startConditionalNode(o, condition, !FALSE_VALUE_MODIFIERS.contains(PsiUtilCore.getElementType(o)));
    }

    @Override
    public void visitElement(@NotNull PsiElement element) {
      if (!(element instanceof LeafPsiElement)) {
        super.visitElement(element);
        startNode(element);
      }
    }
  }
}
