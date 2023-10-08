/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

import com.intellij.codeInsight.controlflow.ControlFlowUtil;
import com.intellij.codeInsight.controlflow.Instruction;
import com.intellij.injected.editor.VirtualFileWindow;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.PairProcessor;
import com.perl5.lang.perl.extensions.PerlImplicitVariablesProvider;
import com.perl5.lang.perl.idea.codeInsight.controlFlow.PerlAssignInstruction;
import com.perl5.lang.perl.idea.codeInsight.controlFlow.PerlControlFlowBuilder;
import com.perl5.lang.perl.idea.codeInsight.controlFlow.PerlMutationInstruction;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlOneOfValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlBuiltInVariable;
import com.perl5.lang.perl.psi.impl.PerlSubCallElement;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.references.scopes.PerlVariableDeclarationSearcher;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.Objects;

import static com.intellij.codeInsight.controlflow.ControlFlowUtil.Operation.CONTINUE;
import static com.intellij.codeInsight.controlflow.ControlFlowUtil.Operation.NEXT;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE_PROVIDER;


public final class PerlResolveUtil {
  private PerlResolveUtil() {
  }

  private static final Logger LOG = Logger.getInstance(PerlResolveUtil.class);
  private static boolean SUPPRESS_ERRORS = false;

  public static boolean treeWalkUp(@Nullable PsiElement place, @NotNull PsiScopeProcessor processor) {
    PsiElement lastParent = null;
    PsiElement run = place;
    ResolveState state = ResolveState.initial();
    while (run != null) {
      ProgressManager.checkCanceled();
      if (place != run && !run.processDeclarations(processor, state, lastParent, place)) {
        return false;
      }
      lastParent = run;

      run = run.getContext();
    }
    return true;
  }

  public static boolean processChildren(@NotNull PsiElement element,
                                        @NotNull PsiScopeProcessor processor,
                                        @NotNull ResolveState resolveState,
                                        @Nullable PsiElement lastParent,
                                        @NotNull PsiElement place) {
    PsiElement run = lastParent == null ? element.getLastChild() : lastParent.getPrevSibling();
    while (run != null) {
      ProgressManager.checkCanceled();
      if ((run instanceof PerlCompositeElement || run instanceof PerlSubCallElement) &&
          !(run instanceof PerlLexicalScope) && // fixme this should be in composite
          !run.processDeclarations(processor, resolveState, null, place)
      ) {
        return false;
      }
      run = run.getPrevSibling();
    }

    // checking implicit variables fixme: decide, move processchildren to here or move this one to processDeclarations?
    if (element instanceof PerlImplicitVariablesProvider) {
      for (PerlVariableDeclarationElement wrapper : ((PerlImplicitVariablesProvider)element).getImplicitVariables()) {
        ProgressManager.checkCanceled();
        if (!processor.execute(wrapper, resolveState)) {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Searching for most recent lexically visible variable declaration
   *
   * @param variable variable to search declaration for
   * @return variable in declaration term or null if there is no such one
   */
  public static @Nullable PerlVariableDeclarationElement getLexicalDeclaration(PerlVariable variable) {
    if (variable instanceof PerlVariableDeclarationElement) {
      return (PerlVariableDeclarationElement)variable;
    }
    if (variable.getExplicitNamespaceName() != null) {
      return null;
    }
    return CachedValuesManager.getCachedValue(variable, () -> {
      PerlVariableDeclarationSearcher variableProcessor = new PerlVariableDeclarationSearcher(variable);
      if (PerlResolveUtil.treeWalkUp(variable, variableProcessor)) {
        variableProcessor.searchBuiltIn();
      }
      return CachedValueProvider.Result.create(variableProcessor.getResult(), variable.getContainingFile());
    });
  }

  /**
   * Processing all targets of all references of all {@code sourceElements}
   */
  @SuppressWarnings("UnusedReturnValue")
  public static boolean processResolveTargets(@NotNull PairProcessor<PsiElement, PsiReference> processor,
                                              PsiElement... sourceElements) {
    if (sourceElements == null) {
      return true;
    }

    for (PsiElement element : sourceElements) {
      if (!processResolveTargets(processor, element.getReferences())) {
        return false;
      }
    }

    return true;
  }


  /**
   * Processing target elements of {@code references}
   */
  public static boolean processResolveTargets(@NotNull PairProcessor<PsiElement, PsiReference> processor,
                                              @Nullable PsiReference... references) {
    if (references == null) {
      return true;
    }

    for (PsiReference reference : references) {
      if (reference instanceof PsiPolyVariantReference) {
        for (ResolveResult resolveResult : ((PsiPolyVariantReference)reference).multiResolve(false)) {
          PsiElement targetElement = resolveResult.getElement();
          if (targetElement != null && !processor.process(targetElement, reference)) {
            return false;
          }
        }
      }
      else if (reference != null) {
        PsiElement targetElement = reference.resolve();
        if (targetElement != null && !processor.process(targetElement, reference)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Attempts to infer a variable type from control flow graph and previous assignments
   *
   * @return an inferred value, probably unknown
   * @see PerlControlFlowBuilder
   * @see PerlValue
   */
  public static @NotNull PerlValue inferVariableValue(@NotNull PerlVariable variable) {
    try{
      return getValueFromControlFlow(new TypeInferringContext(variable)).buildValue();
    }
    catch (StackOverflowError e){
      LOG.error("Stack overflow while inferring variable value: " + variable + "; file: " + PsiUtilCore.getVirtualFile(variable));
      return UNKNOWN_VALUE;
    }
  }

  public static @NotNull PerlValue inferVariableValue(@NotNull PerlBuiltInVariable variable, @NotNull PsiElement contextElement) {
    return getValueFromControlFlow(new TypeInferringContext(variable, contextElement)).buildValue();
  }

  /**
   * Computes the stop element for resolving a built-in variable. For {@code @_} it's a wrapping sub. For {@code $_} it may vary: wrapping
   * iterator or something.
   */
  static @Nullable PsiElement computeStopElement(@NotNull PerlBuiltInVariable variable, @NotNull PsiElement contextElement) {
    if (!variable.getName().equals("_")) {
      return null;
    }
    if (variable.getActualType() == PerlVariableType.ARRAY) {
      // fixme this actually won't work, because this node is in the end of CFG
      return PsiTreeUtil.getParentOfType(contextElement, PerlSubDefinitionElement.class, PerlSubExpr.class);
    }
    return null;
  }


  /**
   * Building a control flow for the {@code element} and attempts to find a value of variable
   *
   * @param element            to build control flow for
   * @param namespaceName      namespace name of the variable if any
   * @param variableName       name of the variable
   * @param actualType         actual type of the variable
   * @param lexicalDeclaration variable declaration element
   * @param stopElement        stop element, lexical declaration or its context for the light elements
   * @return a value of found variable or {@link PerlValues#UNKNOWN_VALUE}
   * @see PerlControlFlowBuilder#getControlFlowScope(com.intellij.psi.PsiElement)
   */
  private static @NotNull TypeInferringContext getValueFromControlFlow(@NotNull TypeInferringContext inferringContext) {
    var element = inferringContext.getContextElement();
    PsiElement controlFlowScope = PerlControlFlowBuilder.getControlFlowScope(element);
    if (controlFlowScope == null) {
      noControlFlowScopeError(element);
      return inferringContext;
    }
    Instruction[] instructions = PerlControlFlowBuilder.getFor(controlFlowScope);
    PsiElement elementToFind = element instanceof PerlFile ? element.getContext() : element;
    int elementInstructionIndex = findElementInstruction(elementToFind, instructions, element);
    if (elementInstructionIndex < 0) {
      noElementInstructionError(element, controlFlowScope);
      return inferringContext;
    }
    ControlFlowUtil.iteratePrev(elementInstructionIndex, instructions, currentInstruction -> {
      if (currentInstruction instanceof PerlMutationInstruction mutationInstruction) {
        return processMutationInstruction(inferringContext, elementInstructionIndex, mutationInstruction);
      }
      return processAccessInstruction(inferringContext, currentInstruction);
    });

    var lexicalDeclaration = inferringContext.getLexicalDeclaration();
    if (lexicalDeclaration != null) {
      PerlValue declaredValue = lexicalDeclaration.getDeclaredValue();
      if (!declaredValue.isUnknown()) {
        inferringContext.addVariant(declaredValue);
      }
    }
    return inferringContext;
  }

  private static void noElementInstructionError(@NotNull PsiElement element, @NotNull PsiElement controlFlowScope) {
    String message = "Unable to find an instruction for " +
                     element.getClass() + "; " +
                     element.getText() + "; " +
                     element.getTextRange() + "; " +
                     PsiUtilCore.getVirtualFile(element) + "; " +
                     controlFlowScope.getClass() + "; " +
                     PerlPsiUtil.dumpHierarchy(element);
    Application application = ApplicationManager.getApplication();
    if (!SUPPRESS_ERRORS && (application.isUnitTestMode() || application.isInternal())) {
      LOG.error(message);
    }
    else {
      LOG.warn(message);
    }
  }

  private static void noControlFlowScopeError(@NotNull PsiElement element) {
    VirtualFile virtualFile = PsiUtilCore.getVirtualFile(element);
    if (!(element instanceof PsiFile) || !(virtualFile instanceof VirtualFileWindow)) {
      LOG.error("Unable to find control flow scope for:" +
                element.getClass() +
                " at " +
                element.getTextOffset() +
                " in " +
                virtualFile + "; " +
                PerlPsiUtil.dumpHierarchy(element));
    }
  }

  private static @NotNull ControlFlowUtil.Operation processAccessInstruction(@NotNull TypeInferringContext inferringContext,
                                                                             @NotNull Instruction currentInstruction) {
    PsiElement instructionElement = currentInstruction.getElement();
    if ((instructionElement instanceof PerlSubDefinitionElement || instructionElement instanceof PerlSubExpr) &&
        inferringContext.getLexicalDeclaration() instanceof PerlBuiltInVariable &&
        "_".equals(inferringContext.getVariableName()) &&
        inferringContext.getActualType() == PerlVariableType.ARRAY) {
      inferringContext.addVariant(PerlValues.ARGUMENTS_VALUE);
      return CONTINUE;
    }
    if (inferringContext.isMyVariable(instructionElement) &&
        instructionElement.getParent() instanceof PerlDerefExpression derefExpression &&
        instructionElement.getPrevSibling() == null) {
      var children = derefExpression.getChildren();
      if (children.length > 1 && children[1] instanceof PerlSubCallElement subCallElement) {
        inferringContext.addDuckCall(subCallElement.getSubName());
      }
    }
    if (Objects.equals(inferringContext.getStopElement(), instructionElement)) {
      return CONTINUE;
    }
    if (currentInstruction.num() == 1 && instructionElement != null && instructionElement.getContext() != null) {
      getValueFromControlFlow(inferringContext.withContext(instructionElement));
    }
    return NEXT;
  }

  private static @NotNull ControlFlowUtil.Operation processMutationInstruction(@NotNull TypeInferringContext inferringContext,
                                                                               int elementInstructionIndex,
                                                                               @NotNull PerlMutationInstruction currentInstruction) {
    if (currentInstruction.num() > elementInstructionIndex) {
      return NEXT;
    }
    // fixme pop instruction should be decomposed
    if (currentInstruction.num() == elementInstructionIndex && !(currentInstruction instanceof PerlAssignInstruction)) {
      return NEXT;
    }
    PsiElement assignee = currentInstruction.getLeftSide();
    if (!inferringContext.isMyVariable(assignee)) {
      return NEXT;
    }
    String explicitNamespaceName = ((PerlVariable)assignee).getExplicitNamespaceName();
    var namespaceName = inferringContext.getNamespaceName();
    if ((explicitNamespaceName != null || namespaceName != null) && !Objects.equals(namespaceName, explicitNamespaceName)) {
      return NEXT;
    }
    PerlVariableDeclarationElement assigneeDeclaration = getLexicalDeclaration((PerlVariable)assignee);
    var lexicalDeclaration = inferringContext.getLexicalDeclaration();
    if (inferringContext.getContextElement() == assignee ||
        lexicalDeclaration == null && assigneeDeclaration == null && !(assignee.getParent() instanceof PerlVariableDeclarationElement) ||
        lexicalDeclaration != null && (
          Objects.equals(lexicalDeclaration, assigneeDeclaration) ||
          Objects.equals(lexicalDeclaration, assignee.getParent()))
    ) {
      inferringContext.addVariant(currentInstruction.createValue());
    }
    return CONTINUE;
  }

  private static int findElementInstruction(@Nullable PsiElement elementToFind,
                                            @NotNull Instruction[] instructions,
                                            @Nullable PsiElement originalElementToFind) {
    if (elementToFind == null) {
      return -1;
    }
    int elementInstructionIndex = ControlFlowUtil.findInstructionNumberByElement(instructions, elementToFind);

    if (elementInstructionIndex < 0 && originalElementToFind instanceof PerlFile) {
      elementInstructionIndex = PerlControlFlowBuilder.findInstructionNumberByRange(instructions, elementToFind);
    }

    return elementInstructionIndex;
  }

  /**
   * @see #computeReturnValueFromControlFlow(PsiElement)
   */
  public static @NotNull AtomicNotNullLazyValue<PerlValue> computeReturnValueFromControlFlowLazy(@Nullable PsiElement subElement) {
    if (subElement == null) {
      return UNKNOWN_VALUE_PROVIDER;
    }
    return AtomicNotNullLazyValue.createValue(() -> {
      if (!subElement.isValid()) {
        LOG.error("Computing value for invalid element");
        return UNKNOWN_VALUE;
      }
      return computeReturnValueFromControlFlow(subElement);
    });
  }

  /**
   * @return a perl value for the psi element, computed from the control flow graph
   * @apiNote should be used for subs and subs expressions
   */
  public static @NotNull PerlValue computeReturnValueFromControlFlow(PsiElement subElement) {
    PerlOneOfValue.Builder valueBuilder = PerlOneOfValue.builder();
    Instruction[] instructions = PerlControlFlowBuilder.getFor(subElement);
    Instruction exitInstruction = instructions[instructions.length - 1];
    PerlControlFlowBuilder.iteratePrev(instructions, it -> {
      if (it == exitInstruction || it.num() == 0) {
        return NEXT;
      }
      PsiElement element = it.getElement();
      if (element == null) {
        return NEXT;
      }
      valueBuilder.addVariant(PerlValuesManager.from(element));
      return CONTINUE;
    });

    return valueBuilder.build();
  }

  /**
   * @return true iff {@code reference} is not null and has at least one target
   */
  @Contract("null->false")
  public static boolean isResolvable(@Nullable PsiReference reference) {
    if (reference == null) {
      return false;
    }

    if (reference instanceof PsiPolyVariantReference) {
      return ((PsiPolyVariantReference)reference).multiResolve(false).length > 0;
    }
    return reference.resolve() != null;
  }

  /**
   * @return true iff {@code psiElement} not null, has at least one resolvable reference
   * @see #isResolvable(PsiReference)
   */
  @Contract("null -> false")
  public static boolean isResolvable(@Nullable PsiElement psiElement) {
    if (psiElement == null) {
      return false;
    }
    for (PsiReference reference : psiElement.getReferences()) {
      if (isResolvable(reference)) {
        return true;
      }
    }
    return false;
  }

  @TestOnly
  public static void runWithoutErrors(@NotNull Runnable run) {
    boolean backup = SUPPRESS_ERRORS;
    SUPPRESS_ERRORS = true;
    try {
      run.run();
    }
    finally {
      SUPPRESS_ERRORS = backup;
    }
  }
}
