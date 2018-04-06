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
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mixins.PerlStatementMixin;
import com.perl5.lang.perl.psi.properties.PerlBlockOwner;
import com.perl5.lang.perl.psi.properties.PerlDieScope;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
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
   * We should not create a node for following elements, only accept children.
   */
  private static final TokenSet TRANSPARENT_CONTAINERS = TokenSet.create(
    BLOCK, CONTINUE_BLOCK, CONDITION_EXPR,
    CALL_ARGUMENTS, PARENTHESISED_CALL_ARGUMENTS,
    WHILE_COMPOUND, UNTIL_COMPOUND,
    IF_COMPOUND, UNLESS_COMPOUND, CONDITIONAL_BLOCK, UNCONDITIONAL_BLOCK,
    FOR_COMPOUND
  );

  /**
   * Modifiers with false branch, instead of true
   */
  private static final TokenSet FALSE_VALUE_MODIFIERS = TokenSet.create(
    UNLESS_STATEMENT_MODIFIER, UNTIL_STATEMENT_MODIFIER
  );

  // modifier's loops should be edged back here
  private Instruction myModifierLoopInstruction = null;

  public ControlFlow build(PsiElement element) {
    super.build(new PerlControlFlowVisitor(), element);
    return getCompleteControlFlow();
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

  public Instruction startIterationNode(@Nullable PsiElement element,
                                        @Nullable PsiElement targetElement,
                                        @Nullable PsiElement sourceElement) {
    PerlIterateInstruction instruction = new PerlIterateInstruction(this, element, targetElement, sourceElement);
    addNodeAndCheckPending(instruction);
    return instruction;
  }

  private void startNodeSmart(@Nullable PsiElement element) {
    if (TRANSPARENT_CONTAINERS.contains(PsiUtilCore.getElementType(element))) {
      startTransparentNode(element, "");
    }
    else if (element != null) {
      startNode(element);
    }
  }

  /**
   * @return nearest scope for die & friends
   */
  public static PerlDieScope getDieScope(@NotNull PsiElement element) {
    return Objects.requireNonNull(PsiTreeUtil.getParentOfType(element, PerlDieScope.class));
  }

  /**
   * @return nearest scope for die/croak/confess
   */
  @NotNull
  private static PsiElement getDieScopeBlock(@NotNull PsiElement element) {
    return getNestedBlockOrElement(getDieScope(element));
  }

  @NotNull
  private static PsiElement getReturnScopeBlock(@NotNull PsiPerlReturnExpr o) {
    return getNestedBlockOrElement(o.getReturnScope());
  }

  /**
   * @return nested block if any
   */
  @NotNull
  private static PsiElement getNestedBlockOrElement(@NotNull PsiElement element) {
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

  public Instruction startConditionalNode(PsiElement condition, boolean result) {
    return startConditionalNode(condition, condition, result);
  }

  public Instruction startIteratorConditionalNode(@Nullable PsiElement iterator) {
    PerlIteratorConditionInstruction instruction = new PerlIteratorConditionInstruction(this, iterator, true);
    addNodeAndCheckPending(instruction);
    return instruction;
  }

  // fixme shouldn't we move subs elements in the beginning of the subgraph?
  // fixme for indexed
  // fixme given & friends
  // fixme next/last/redo
  private class PerlControlFlowVisitor extends PerlRecursiveVisitor {

    private void acceptSafe(@Nullable PsiElement o) {
      if (o != null) {
        o.accept(this);
      }
    }

    @Override
    public void visitGrepExpr(@NotNull PsiPerlGrepExpr o) {
      processSortMapGrep(o);
    }

    @Override
    public void visitMapExpr(@NotNull PsiPerlMapExpr o) {
      processSortMapGrep(o);
    }

    @Override
    public void visitSortExpr(@NotNull PsiPerlSortExpr o) {
      processSortMapGrep(o);
    }

    private void processSortMapGrep(@NotNull PsiElement o) {
      PsiElement[] children = o.getChildren();
      if (children.length > 0) {
        PsiElement bodyElement = children.length == 1 ? null : children[0];
        PsiElement sourceElement = children.length == 1 ? children[0] : children[1];
        acceptSafe(sourceElement);
        Instruction loopInstruction = startIterationNode(o, null, sourceElement);
        startIteratorConditionalNode(sourceElement);
        acceptSafe(bodyElement);
        addEdge(prevInstruction, loopInstruction);
        prevInstruction = loopInstruction;
      }
      startNodeSmart(o);
    }

    @Override
    public void visitForCompound(@NotNull PsiPerlForCompound o) {
      if (o.getForIterator() != null) {
        processIndexedFor(o);
      }
      else {
        processForWithIterator(o);
      }
    }

    private void processForWithIterator(@NotNull PsiPerlForCompound o) {
      startNodeSmart(o);
      PsiPerlConditionExpr sourceElement = o.getConditionExpr();
      acceptSafe(sourceElement);
      Instruction loopInstruction = startIterationNode(o, o.getForeachIterator(), sourceElement);
      startIteratorConditionalNode(sourceElement); // fake condition if iterator is not finished yet
      acceptSafe(o.getBlock());
      acceptSafe(o.getContinueBlock());
      addEdge(prevInstruction, loopInstruction);
      prevInstruction = loopInstruction;
    }

    private void processIndexedFor(@NotNull PsiPerlForCompound o) {
      startNodeSmart(o);
    }

    @Override
    public void visitWhileCompound(@NotNull PsiPerlWhileCompound o) {
      processWhileUntil(o, true);
    }

    @Override
    public void visitUntilCompound(@NotNull PsiPerlUntilCompound o) {
      processWhileUntil(o, false);
    }

    private void processWhileUntil(@NotNull PerlWhileUntilCompound o, boolean conditionValue) {
      startNodeSmart(o);
      Instruction startInstruction = prevInstruction;

      PsiPerlConditionExpr conditionExpr = o.getConditionExpr();
      acceptSafe(conditionExpr);

      Instruction conditionInstruction = prevInstruction;

      startConditionalNode(conditionExpr, conditionValue);
      acceptSafe(o.getBlock());

      acceptSafe(o.getContinueBlock());

      addEdge(prevInstruction, startInstruction);
      prevInstruction = conditionInstruction;
    }

    @Override
    public void visitIfCompound(@NotNull PsiPerlIfCompound o) {
      processIfUnlessCompound(o, true);
    }

    @Override
    public void visitUnlessCompound(@NotNull PsiPerlUnlessCompound o) {
      processIfUnlessCompound(o, false);
    }

    private void processIfUnlessCompound(@NotNull PerlIfUnlessCompound o, boolean conditionValue) {
      startNodeSmart(o);
      List<PsiPerlConditionalBlock> conditionBlocks = o.getConditionalBlockList();

      // if/unless branch
      PsiPerlConditionalBlock mainBranch = conditionBlocks.isEmpty() ? null : conditionBlocks.remove(0);
      processConditionalBranch(mainBranch, conditionValue, o);

      // elsif branches
      conditionBlocks.forEach(branch -> processConditionalBranch(branch, true, o));

      acceptSafe(o.getUnconditionalBlock());
    }

    /**
     * Processes conditional branch (if/unless/elsif/etc)
     *
     * @param branch         branch to process
     * @param conditionValue value of condition (if/unless)
     * @param scope          wrapping scope for pending edges
     */
    private void processConditionalBranch(@Nullable PsiPerlConditionalBlock branch,
                                          boolean conditionValue,
                                          @NotNull PsiElement scope) {
      if (branch == null) {
        return;
      }
      PsiPerlConditionExpr mainBranchCondition = branch.getConditionExpr();
      mainBranchCondition.accept(this);
      Instruction elseFlow = prevInstruction;
      startConditionalNode(mainBranchCondition, conditionValue);
      acceptSafe(branch.getBlock());
      addPendingEdge(scope, prevInstruction);
      prevInstruction = elseFlow;
    }

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
      startNodeSmart(o);
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
        addNodeAndCheckPending(new PerlAssignInstruction(PerlControlFlowBuilder.this, o, leftSide, rightSide, operator));
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
        acceptSafe(o.getCallArguments());
        PsiElement dieScope = getDieScopeBlock(o);
        startNode(o);
        addPendingEdge(dieScope, prevInstruction);
        flowAbrupted();
      }
      else {
        super.visitSubCallExpr(o);
      }
    }

    @Override
    public void visitStatement(@NotNull PsiPerlStatement o) {
      if (!(o instanceof PerlStatementMixin)) {
        super.visitStatement(o);
        return;
      }

      acceptSafe(((PerlStatementMixin)o).getModifier());
      Instruction modifierLoopInstruction = myModifierLoopInstruction;
      acceptSafe(o.getExpr());
      if (modifierLoopInstruction != null) {
        addEdge(prevInstruction, modifierLoopInstruction);
        prevInstruction = modifierLoopInstruction;
        myModifierLoopInstruction = null;
      }
    }

    @Override
    public void visitReturnExpr(@NotNull PsiPerlReturnExpr o) {
      acceptSafe(o.getReturnValueExpr());
      startNode(o);
      addPendingEdge(getReturnScopeBlock(o), prevInstruction);
      flowAbrupted();
    }

    @Override
    public void visitForStatementModifier(@NotNull PsiPerlForStatementModifier o) {
      PsiPerlExpr source = o.getExpr();
      acceptSafe(source);
      myModifierLoopInstruction = startIterationNode(o, null, source);
      startIteratorConditionalNode(source);
    }

    @Override
    public void visitPerlStatementModifier(@NotNull PerlStatementModifier o) {
      PsiPerlExpr condition = o.getExpr();
      acceptSafe(condition);
      if (LOOP_MODIFIERS.contains(PsiUtilCore.getElementType(o))) {
        myModifierLoopInstruction = prevInstruction;
      }
      else {
        addPendingEdge(o.getParent(), prevInstruction);
      }
      startConditionalNode(o, condition, !FALSE_VALUE_MODIFIERS.contains(PsiUtilCore.getElementType(o)));
    }

    @Override
    public void visitHeredocOpener(@NotNull PsiPerlHeredocOpener o) {
      startNode(o);
    }

    @Override
    public void visitElement(@NotNull PsiElement element) {
      if (element instanceof LeafPsiElement) {
        return;
      }
      element.acceptChildren(this);
      startNodeSmart(element);
    }
  }
}
