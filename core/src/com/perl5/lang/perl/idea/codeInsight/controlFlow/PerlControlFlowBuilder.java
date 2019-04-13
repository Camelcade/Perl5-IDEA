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

import com.intellij.codeInsight.controlflow.*;
import com.intellij.codeInsight.controlflow.impl.TransparentInstructionImpl;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.Function;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.PerlAssignExpression.PerlAssignValueDescriptor;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.impl.PerlHeredocTerminatorElementImpl;
import com.perl5.lang.perl.psi.mixins.PerlStatementMixin;
import com.perl5.lang.perl.psi.properties.PerlBlockOwner;
import com.perl5.lang.perl.psi.properties.PerlDieScope;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;
import static com.perl5.lang.perl.lexer.PerlTokenSets.LAZY_CODE_BLOCKS;

public class PerlControlFlowBuilder extends ControlFlowBuilder {
  public static final Set<String> DIE_SUBS = ContainerUtil.newHashSet(
    "die",
    "croak",
    "confess"
  );

  private static final TokenSet LOOP_MODIFIERS = TokenSet.create(
    FOR_STATEMENT_MODIFIER,
    UNTIL_STATEMENT_MODIFIER,
    WHILE_STATEMENT_MODIFIER
  );

  private static final TokenSet NO_RESULT_INSTRUCTIONS = TokenSet.create(
    EXIT_EXPR, GOTO_EXPR, NEXT_EXPR, LAST_EXPR, REDO_EXPR
  );

  /**
   * We should create a trasparent node for following elements
   */
  private static final TokenSet TRANSPARENT_CONTAINERS = TokenSet.orSet(
    LAZY_CODE_BLOCKS,
    TokenSet.create(
      NAMESPACE_CONTENT, NAMESPACE_DEFINITION,
      DO_EXPR,
      BLOCK, CONTINUE_BLOCK, CONDITION_EXPR,
      CALL_ARGUMENTS, PARENTHESISED_CALL_ARGUMENTS,
      WHILE_COMPOUND, UNTIL_COMPOUND,
      IF_COMPOUND, UNLESS_COMPOUND, CONDITIONAL_BLOCK, UNCONDITIONAL_BLOCK, BLOCK_COMPOUND,
      FOR_COMPOUND, FOREACH_COMPOUND,
      HEREDOC_END, HEREDOC_END_INDENTABLE,
      TRYCATCH_EXPR, TRY_EXPR, CATCH_EXPR, FINALLY_EXPR, CATCH_CONDITION, EXCEPT_EXPR, OTHERWISE_EXPR, CONTINUATION_EXPR, TRYCATCH_COMPOUND,
      COMMA_SEQUENCE_EXPR, PARENTHESISED_EXPR,
      AND_EXPR, LP_AND_EXPR, OR_EXPR, LP_OR_XOR_EXPR, TERNARY_EXPR
    ));

  /**
   * We should not create a node for following elements, only accept children.
   */
  private static final TokenSet IGNORED_CONTAINERS = TokenSet.create(
    FOR_INIT, FOR_CONDITION, FOR_MUTATOR
  );

  /**
   * Modifiers with false branch, instead of true
   */
  private static final TokenSet FALSE_VALUE_MODIFIERS = TokenSet.create(
    UNLESS_STATEMENT_MODIFIER, UNTIL_STATEMENT_MODIFIER
  );

  // modifier's loops should be edged back here. Maps statement -> modifier
  private final Map<PsiElement, Instruction> myStatementsModifiersMap = ContainerUtil.newHashMap();
  private final Map<String, Instruction> myLabelsDeclarations = ContainerUtil.newHashMap();
  private final List<Instruction> myGotos = ContainerUtil.newArrayList();

  public ControlFlow build(PsiElement element) {
    addEntryPointNode(element);

    element.acceptChildren(new PerlControlFlowVisitor(element));

    // create end pseudo node and close all pending edges
    Instruction exitInstruction = startNode(null);
    processGotos();
    checkPending(exitInstruction);

    return getCompleteControlFlow();
  }

  /**
   * links gotos or add pending edges
   */
  private void processGotos() {
    for (Instruction gotoInstruction : myGotos) {
      PsiPerlGotoExpr perlGotoExpr = Objects.requireNonNull(ObjectUtils.tryCast(gotoInstruction.getElement(), PsiPerlGotoExpr.class));
      PsiPerlExpr expr = perlGotoExpr.getExpr();
      if (expr instanceof PsiPerlLabelExpr) {
        Instruction labelDeclaration = myLabelsDeclarations.get(expr.getText());
        if (labelDeclaration != null) {
          Collection<Instruction> gotoEdges = gotoInstruction.allSucc();
          if (gotoEdges.size() > 1) {
            throw new RuntimeException(
              "Goto has more than one out edge: " + gotoEdges.stream().map(
                instruction -> String.valueOf(instruction.getElement())).collect(Collectors.joining(",")));
          }
          else if (gotoEdges.isEmpty()) {
            addEdge(gotoInstruction, labelDeclaration);
            LOG.warn("No out edge from goto");
          }
          else {
            PsiElement scopeElement = getGotoScope(perlGotoExpr);
            PsiElement labelElement = labelDeclaration.getElement();
            if (labelElement != null && !PsiTreeUtil.isAncestor(scopeElement, labelElement, true)) {
              continue;
            }
            gotoEdges.forEach(it -> it.allPred().remove(gotoInstruction));
            gotoEdges.clear();
            addEdge(gotoInstruction, labelDeclaration);
          }
        }
      }
    }
  }

  @NotNull
  @Override
  public Instruction startNode(@Nullable PsiElement element) {
    if (NO_RESULT_INSTRUCTIONS.contains(PsiUtilCore.getElementType(element))) {
      return startNoResultNode(element);
    }
    return super.startNode(element);
  }

  @NotNull
  public Instruction startNoResultNode(@Nullable PsiElement element) {
    final Instruction instruction = new PerlNoResultInstruction(this, element);
    addNodeAndCheckPending(instruction);
    return instruction;
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

  @NotNull
  private TransparentInstruction createNextInstruction(@NotNull PsiElement element) {
    return new TransparentInstructionImpl(this, element, "next");
  }

  /**
   * Creates an instruction for {@code element} without processing children. If elementType is in the {@code TRANSPARENT_CONTAINERS}
   * tokenSet - creates a transparent node.
   *
   * @return new instruction or null if element is null
   */
  @Nullable
  @Contract("null -> null")
  private Instruction startNodeSmart(@Nullable PsiElement element) {
    IElementType elementType = PsiUtilCore.getElementType(element);
    if (TRANSPARENT_CONTAINERS.contains(elementType)) {
      return startTransparentNode(element, "");
    }
    else if (element != null && !IGNORED_CONTAINERS.contains(elementType)) {
      return startNode(element);
    }
    return null;
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
  private static PsiElement getGotoScope(@NotNull PsiPerlGotoExpr o) {
    return Objects.requireNonNull(getDieScopeBlock(o));
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
      PsiPerlBlock block = PerlBlockOwner.findBlock((PerlBlockOwner)element);
      if (block != null) {
        return block;
      }
    }
    return element;
  }

  @SuppressWarnings("UnusedReturnValue")
  public Instruction startConditionalNode(PsiElement condition, boolean result) {
    return startConditionalNode(condition, condition, result);
  }

  @SuppressWarnings("UnusedReturnValue")
  public Instruction startIteratorConditionalNode(@Nullable PsiElement iterator) {
    PerlIteratorConditionInstruction instruction = new PerlIteratorConditionInstruction(this, iterator, true);
    addNodeAndCheckPending(instruction);
    return instruction;
  }

  // fixme given & friends
  // fixme revert do transparency?
  private class PerlControlFlowVisitor extends PerlRecursiveVisitor {
    private final Map<PsiElement, Instruction> myLoopNextInstructions = ContainerUtil.newHashMap();
    private final Map<PsiElement, Instruction> myLoopRedoInstructions = ContainerUtil.newHashMap();
    @NotNull
    private final PsiElement myElement;
    private final AtomicNotNullLazyValue<HeredocCollector> myCollectorProvider = AtomicNotNullLazyValue.createValue(
      () -> {
        HeredocCollector collector = new HeredocCollector();
        getElement().accept(collector);
        return collector;
      }
    );

    public PerlControlFlowVisitor(@NotNull PsiElement element) {
      myElement = element;
    }

    @NotNull
    public PsiElement getElement() {
      return myElement;
    }

    private void acceptSafe(@Nullable PsiElement o) {
      if (o != null) {
        o.accept(this);
      }
    }

    @Override
    public void visitPerlSubDefinitionElement(@NotNull PerlSubDefinitionElement o) {
      startTransparentNode(o, "collecting pendings");
      Instruction shortCut = prevInstruction;
      startNodeSmart(o);
      o.acceptChildren(this);
      startTransparentNode(o, "collecting pendings");
      addPendingEdge(o.getContainingFile(), prevInstruction);
      prevInstruction = shortCut;
    }

    @Override
    public void visitCatchExpr(@NotNull PsiPerlCatchExpr o) {
      PsiPerlCatchCondition catchCondition = o.getCatchCondition();

      startNode(o);
      if (catchCondition != null) {
        acceptSafe(catchCondition);
      }

      addPendingEdge(o, prevInstruction);
      startConditionalNode(o, catchCondition, true);
      PsiPerlSubExpr subExpr = o.getSub();
      if (subExpr != null) {
        acceptSafe(subExpr);
      }
      else {
        acceptSafe(o.getBlock());
      }
    }

    @Override
    public void visitTrycatchExpr(@NotNull PsiPerlTrycatchExpr o) {
      acceptSafe(o.getTryExpression());

      // get map of exceptions and handlers; Internals won't be handled for now
      o.getExceptExpressions().forEach(this::acceptSafe);

      List<Instruction> catchesTails = ContainerUtil.newArrayList();
      // catches
      for (PerlCatchExpr catchExpr : o.getCatchExpressions()) {
        acceptSafe(catchExpr);
        catchesTails.add(prevInstruction);
        flowAbrupted();
      }

      // executed in case of uncatched exception
      o.getOtherwiseExpressions().forEach(this::acceptSafe);

      // executed anyway
      o.getFinallyExpressions().forEach(finallyExpr -> {
        startTransparentNode(finallyExpr, "anchor");
        catchesTails.forEach(tail -> addEdge(tail, prevInstruction));
        catchesTails.clear();
        acceptSafe(finallyExpr);
      });

      TransparentInstruction anchor = startTransparentNode(o, "anchor");
      catchesTails.forEach(tail -> addEdge(tail, anchor));
    }

    @Override
    public void visitLastExpr(@NotNull PsiPerlLastExpr o) {
      super.visitLastExpr(o);
      addPendingEdge(o.getTargetScope(), prevInstruction);
      flowAbrupted();
    }

    @Override
    public void visitRedoExpr(@NotNull PsiPerlRedoExpr o) {
      super.visitRedoExpr(o);
      visitNextRedo(o, myLoopRedoInstructions);
    }

    private void visitNextRedo(@NotNull PerlFlowControlExpr o, @NotNull Map<PsiElement, Instruction> targetMap) {
      PsiElement targetLoop = o.getTargetScope();

      Instruction loopInstruction = targetMap.get(targetLoop);
      if (loopInstruction != null) {
        addEdge(prevInstruction, loopInstruction);
      }
      else {
        addPendingEdge(targetLoop, prevInstruction);
      }
      flowAbrupted();
    }

    @Override
    public void visitNextExpr(@NotNull PsiPerlNextExpr o) {
      super.visitNextExpr(o);
      visitNextRedo(o, myLoopNextInstructions);
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
    public void visitBlockCompound(@NotNull PsiPerlBlockCompound o) {
      TransparentInstruction nextInstruction = createNextInstruction(o);
      myLoopNextInstructions.put(o, nextInstruction);
      myLoopRedoInstructions.put(o, startTransparentNode(o, "redo"));
      acceptSafe(o.getBlock());
      addNodeAndCheckPending(nextInstruction);
      acceptSafe(o.getContinueBlock());
      myLoopNextInstructions.remove(o);
      myLoopRedoInstructions.remove(o);
    }

    @Override
    public void visitForCompound(@NotNull PsiPerlForCompound o) {
      acceptSafe(o.getForInit());

      TransparentInstruction loopInstruction = startTransparentNode(o, "loopAnchor");
      TransparentInstruction nextAnchor = createNextInstruction(o);

      PsiPerlForMutator mutator = o.getForMutator();
      if (mutator == null) {
        addNodeAndCheckPending(nextAnchor);
      }

      PsiPerlForCondition condition = o.getForCondition();
      Instruction conditionalInstruction = null;
      if (condition != null) {
        acceptSafe(condition);
        conditionalInstruction = prevInstruction;
        startConditionalNode(o, condition, true);
      }

      myLoopNextInstructions.put(o, nextAnchor);
      myLoopRedoInstructions.put(o, startTransparentNode(o, "redo"));
      acceptSafe(o.getBlock());

      if (mutator != null) {
        addNodeAndCheckPending(nextAnchor);
        acceptSafe(mutator);
      }

      addEdge(prevInstruction, loopInstruction);
      myLoopNextInstructions.remove(o);
      myLoopRedoInstructions.remove(o);
      prevInstruction = conditionalInstruction;
    }

    @Override
    public void visitForeachCompound(@NotNull PsiPerlForeachCompound o) {
      startNodeSmart(o);
      PsiPerlConditionExpr sourceElement = o.getConditionExpr();
      acceptSafe(sourceElement);
      Instruction loopInstruction = startIterationNode(o, o.getForeachIterator(), sourceElement);
      TransparentInstruction nextInstruction = createNextInstruction(o);
      myLoopNextInstructions.put(o, nextInstruction);
      startIteratorConditionalNode(sourceElement); // fake condition if iterator is not finished yet
      myLoopRedoInstructions.put(o, startTransparentNode(o, "redo"));
      acceptSafe(o.getBlock());
      addNodeAndCheckPending(nextInstruction);
      acceptSafe(o.getContinueBlock());
      addEdge(prevInstruction, loopInstruction);
      prevInstruction = loopInstruction;
      myLoopNextInstructions.remove(o);
      myLoopRedoInstructions.remove(o);
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
      TransparentInstruction nextInstruction = createNextInstruction(o);
      myLoopNextInstructions.put(o, nextInstruction);

      PsiPerlConditionExpr conditionExpr = o.getConditionExpr();
      acceptSafe(conditionExpr);

      Instruction conditionInstruction = prevInstruction;

      startConditionalNode(conditionExpr, conditionValue);
      myLoopRedoInstructions.put(o, startTransparentNode(o, "redo"));
      acceptSafe(o.getBlock());
      addNodeAndCheckPending(nextInstruction);
      acceptSafe(o.getContinueBlock());

      addEdge(prevInstruction, startInstruction);
      prevInstruction = conditionInstruction;
      myLoopNextInstructions.remove(o);
      myLoopRedoInstructions.remove(o);
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
    public void visitTernaryExpr(@NotNull PsiPerlTernaryExpr o) {
      PsiElement[] children = o.getChildren();
      if (children.length != 3) {
        super.visitTernaryExpr(o);
        return;
      }
      children[0].accept(this);
      Instruction conditionInstruction = prevInstruction;
      startConditionalNode(o, children[0], true);
      children[1].accept(this);
      Instruction trueInstruction = prevInstruction;
      prevInstruction = conditionInstruction;
      children[2].accept(this);
      startNodeSmart(o);
      addEdge(trueInstruction, prevInstruction);
    }

    @Override
    public void visitDefinedExpr(@NotNull PsiPerlDefinedExpr o) {
      o.getArgument().accept(this);
      startNodeSmart(o);
    }

    @Override
    public void visitExpr(@NotNull PsiPerlExpr o) {
      PsiElement run = o.getFirstChild();
      PsiElement lastRun = null;
      List<Instruction> instructionsToLink = ContainerUtil.newArrayList();
      while (run != null) {
        if (!PerlPsiUtil.isCommentOrSpace(run)) {
          run.accept(this);
          IElementType elementType = PsiUtilCore.getElementType(run);
          if (lastRun != null) {
            if (elementType == OPERATOR_AND || elementType == OPERATOR_AND_LP) {
              instructionsToLink.add(prevInstruction);
              startPartialConditionalNode(run, o, lastRun, true);
            }
            else if (elementType == OPERATOR_OR || elementType == OPERATOR_OR_LP || elementType == OPERATOR_OR_DEFINED) {
              instructionsToLink.add(prevInstruction);
              startPartialConditionalNode(run, o, lastRun, false);
            }
          }
        }
        lastRun = run;
        run = run.getNextSibling();
      }
      Instruction outerInstruction = startNodeSmart(o);
      instructionsToLink.forEach(instruction -> addEdge(instruction, outerInstruction));
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
        leftSide.accept(this);
        for (PsiElement target : PerlAssignExpression.flattenAssignmentPart(leftSide)) {
          PerlAssignValueDescriptor rightPartDescriptor =
            ObjectUtils.notNull(o.getRightPartOfAssignment(target), PerlAssignValueDescriptor.EMPTY);
          addNodeAndCheckPending(new PerlAssignInstruction(PerlControlFlowBuilder.this, target, rightPartDescriptor, operator));
        }
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
        startNoResultNode(o);
        addPendingEdge(dieScope, prevInstruction);
        flowAbrupted();
      }
      else {
        acceptSafe(o.getCallArguments());
        acceptSafe(o.getMethod());
        startNodeSmart(o);
      }
    }

    @Override
    public void visitStatement(@NotNull PsiPerlStatement o) {
      if (!(o instanceof PerlStatementMixin)) {
        super.visitStatement(o);
        return;
      }

      PsiPerlStatementModifier modifier = ((PerlStatementMixin)o).getModifier();
      PsiPerlExpr statementExpression = o.getExpr();

      /**
       * The while and until modifiers have the usual "while loop" semantics (conditional evaluated first),
       * except when applied to a do-BLOCK (or to the Perl4 do-SUBROUTINE statement), in which case the block
       * executes once before the conditional is evaluated.
       */
      if (statementExpression instanceof PsiPerlDoExpr &&
          (modifier instanceof PsiPerlWhileStatementModifier || modifier instanceof PsiPerlUntilStatementModifier)
      ) {
        startTransparentNode(o, "statement");
        Instruction loopInstruction = prevInstruction;
        myLoopRedoInstructions.put(o, loopInstruction);
        acceptSafe(statementExpression);
        acceptSafe(modifier);
        Instruction modifierLoopConditionInstruction = Objects.requireNonNull(myStatementsModifiersMap.get(o));
        addEdge(prevInstruction, loopInstruction);
        prevInstruction = modifierLoopConditionInstruction;
      }
      else {
        acceptSafe(modifier);
        Instruction modifierLoopConditionInstruction = myStatementsModifiersMap.get(o);
        if (modifierLoopConditionInstruction != null) {
          myLoopNextInstructions.put(o, modifierLoopConditionInstruction);
        }
        myLoopRedoInstructions.put(o, startTransparentNode(o, "redo"));
        acceptSafe(statementExpression);
        if (modifierLoopConditionInstruction != null) {
          addEdge(prevInstruction, modifierLoopConditionInstruction);
          prevInstruction = modifierLoopConditionInstruction;
        }
      }
      myStatementsModifiersMap.remove(o);
      myLoopNextInstructions.remove(o);
      myLoopRedoInstructions.remove(o);
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
      myStatementsModifiersMap.put(Objects.requireNonNull(o.getParent()), startIterationNode(o, null, source));
      startIteratorConditionalNode(source);
    }

    @Override
    public void visitPerlStatementModifier(@NotNull PerlStatementModifier o) {
      PsiPerlExpr condition = o.getExpr();
      acceptSafe(condition);
      if (LOOP_MODIFIERS.contains(PsiUtilCore.getElementType(o))) {
        myStatementsModifiersMap.put(Objects.requireNonNull(o.getParent()), prevInstruction);
      }
      else {
        addPendingEdge(o.getParent(), prevInstruction);
      }
      startConditionalNode(o, condition, !FALSE_VALUE_MODIFIERS.contains(PsiUtilCore.getElementType(o)));
    }

    @Override
    public void visitHeredocOpener(@NotNull PsiPerlHeredocOpener o) {
      Instruction openerInstruction = startNode(o);
      PerlHeredocElementImpl heredocBody = myCollectorProvider.getValue().getBody(o);
      if (heredocBody == null) {
        return;
      }
      ArrayList<Pair<PsiElement, Instruction>> pendingBackup = new ArrayList<>(pending);
      flowAbrupted();
      startTransparentNode(heredocBody, "pendingCatcher");
      Collection<Pair<PsiElement, Instruction>> pendingToRestore = ContainerUtil.subtract(pendingBackup, pending);

      prevInstruction = openerInstruction;
      visitElement(heredocBody);

      pendingToRestore.forEach(it -> addPendingEdge(it.first, it.second));
    }

    @Override
    public void visitLabelDeclaration(@NotNull PsiPerlLabelDeclaration o) {
      Instruction declarationInstruction = startNode(o);
      String labelName = o.getName();
      if (StringUtil.isNotEmpty(labelName)) {
        myLabelsDeclarations.putIfAbsent(labelName, declarationInstruction);
      }
    }

    @Override
    public void visitGotoExpr(@NotNull PsiPerlGotoExpr o) {
      super.visitGotoExpr(o);
      myGotos.add(prevInstruction);
      addPendingEdge(getGotoScope(o), prevInstruction);
      flowAbrupted();
    }

    @Override
    public void visitHeredocElement(@NotNull PerlHeredocElementImpl o) {
      // we are inlining this after opener
    }

    @Override
    public void visitExitExpr(@NotNull PsiPerlExitExpr o) {
      super.visitExitExpr(o);
      addPendingEdge(getDieScope(o), prevInstruction);
      flowAbrupted();
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

  // this won't handle nested heredocs.
  private class HeredocCollector extends PerlRecursiveVisitor {
    private final List<PsiElement> myOpeners = ContainerUtil.newArrayList();
    // may contain nulls for heredocs without bodies
    private final List<PsiElement> myBodies = ContainerUtil.newArrayList();
    private int myTerminatorCounter = 0;

    @Override
    public void visitHeredocOpener(@NotNull PsiPerlHeredocOpener o) {
      myOpeners.add(o);
    }

    @Override
    public void visitHeredocElement(@NotNull PerlHeredocElementImpl o) {
      myBodies.add(o);
      super.visitHeredocElement(o);
    }

    @Override
    public void visitHeredocTeminator(@NotNull PerlHeredocTerminatorElementImpl o) {
      myTerminatorCounter++;
      if (myTerminatorCounter > myBodies.size()) {
        myBodies.add(null);
      }
    }

    /**
     * @return body psi element for opener if any
     */
    @Nullable
    public PerlHeredocElementImpl getBody(@NotNull PsiPerlHeredocOpener opener) {
      int openerIndex = myOpeners.indexOf(opener);
      if (openerIndex == -1 || myBodies.size() <= openerIndex) {
        return null;
      }
      return ObjectUtils.tryCast(myBodies.get(openerIndex), PerlHeredocElementImpl.class);
    }
  }

  public static void iteratePrev(@Nullable PsiElement element,
                                 @NotNull final Function<? super Instruction, ControlFlowUtil.Operation> processor) {
    if (element != null) {
      iteratePrev(PerlControlFlowBuilder.getFor(element).getInstructions(), processor);
    }
  }

  public static void iteratePrev(@NotNull Instruction[] instructions,
                                 @NotNull final Function<? super Instruction, ControlFlowUtil.Operation> processor) {
    ControlFlowUtil.iteratePrev(instructions.length - 1, instructions, processor);
  }
}
