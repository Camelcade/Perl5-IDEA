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

package com.perl5.lang.perl.psi.utils;

import com.intellij.codeInsight.controlflow.ControlFlowUtil;
import com.intellij.codeInsight.controlflow.Instruction;
import com.intellij.injected.editor.VirtualFileWindow;
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
import com.perl5.lang.perl.idea.codeInsight.controlFlow.PerlControlFlowBuilder;
import com.perl5.lang.perl.idea.codeInsight.controlFlow.PerlMutationInstruction;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlOneOfValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlBuiltInVariable;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.references.scopes.PerlVariableDeclarationSearcher;
import com.perl5.lang.perl.util.PerlUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.intellij.codeInsight.controlflow.ControlFlowUtil.Operation.CONTINUE;
import static com.intellij.codeInsight.controlflow.ControlFlowUtil.Operation.NEXT;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE_PROVIDER;


public class PerlResolveUtil {
  private static final Logger LOG = Logger.getInstance(PerlResolveUtil.class);

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
      if (run instanceof PerlCompositeElement &&
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
  @Nullable
  public static PerlVariableDeclarationElement getLexicalDeclaration(PerlVariable variable) {
    if (variable instanceof PerlVariableDeclarationElement) {
      return (PerlVariableDeclarationElement)variable;
    }
    if (variable.getExplicitNamespaceName() != null) {
      return null;
    }
    return CachedValuesManager.getCachedValue(variable, () -> {
      PerlVariableDeclarationSearcher variableProcessor = new PerlVariableDeclarationSearcher(variable);
      if (PerlResolveUtil.treeWalkUp(variable, variableProcessor)) {
        variableProcessor.processBuiltIns();
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
    if (sourceElements == null || sourceElements.length == 0) {
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
    if (references == null || references.length == 0) {
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
  @NotNull
  public static PerlValue inferVariableValue(@NotNull PerlVariable variable) {
    PerlVariableDeclarationElement lexicalDeclaration = getLexicalDeclaration(variable);

    PsiElement stopElement = lexicalDeclaration;
    if (lexicalDeclaration instanceof PerlImplicitVariableDeclaration) {
      stopElement = lexicalDeclaration.getContext();
    }

    String variableName = variable.getName();
    String namespaceName = variable.getExplicitNamespaceName();
    PerlVariableType actualType = variable.getActualType();

    return getValueFromControlFlow(variable, namespaceName, variableName, actualType, lexicalDeclaration, stopElement);
  }

  @NotNull
  public static PerlValue inferVariableValue(@NotNull PerlBuiltInVariable variable, @NotNull PsiElement contextElement) {
    return getValueFromControlFlow(
      contextElement,
      null,
      variable.getVariableName(),
      variable.getActualType(),
      variable,
      computeStopElement(variable, contextElement)
    );
  }

  /**
   * Computes stop element for resolving a built in variable. For {@code @_} it's a wrapping sub. For {@code $_} it may vary: wrapping
   * iterator or smth.
   */
  @Nullable
  private static PsiElement computeStopElement(@NotNull PerlBuiltInVariable variable, @NotNull PsiElement contextElement) {
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
   * @param stopElement        stop element, lexical declaration or it's context for the light elements
   * @return a value of found variable or {@link PerlValues#UNKNOWN_VALUE}
   * @see PerlControlFlowBuilder#getControlFlowScope(com.intellij.psi.PsiElement)
   */
  @NotNull
  private static PerlValue getValueFromControlFlow(@NotNull PsiElement element,
                                                   @Nullable String namespaceName,
                                                   @NotNull String variableName,
                                                   @NotNull PerlVariableType actualType,
                                                   @Nullable PerlVariableDeclarationElement lexicalDeclaration,
                                                   @Nullable PsiElement stopElement) {
    PsiElement controlFlowScope = PerlControlFlowBuilder.getControlFlowScope(element);
    if (controlFlowScope == null) {
      VirtualFile virtualFile = PsiUtilCore.getVirtualFile(element);
      if (!(element instanceof PsiFile) || !(virtualFile instanceof VirtualFileWindow)) {
        LOG.error("Unable to find control flow scope for:" +
                  element.getClass() +
                  " at " +
                  element.getTextOffset() +
                  " in " +
                  virtualFile + "; " +
                  PerlUtil.getParentsChain(element));
      }
      return UNKNOWN_VALUE;
    }
    Instruction[] instructions = PerlControlFlowBuilder.getFor(controlFlowScope).getInstructions();
    PsiElement elementToFind = element instanceof PerlFile ? element.getContext() : element;
    int elementInstructionIndex = ControlFlowUtil.findInstructionNumberByElement(instructions, elementToFind);
    if( elementInstructionIndex  < 0 && element instanceof PerlFile){
      elementInstructionIndex  = PerlControlFlowBuilder.findInstructionNumberByRange(instructions, elementToFind);
    }
    if (elementInstructionIndex  < 0) {
      LOG.warn("Unable to find an instruction for " +
               element.getClass() + "; " +
               element.getText() + "; " +
               element.getTextRange() + "; " +
               PsiUtilCore.getVirtualFile(element) + "; " +
               controlFlowScope.getClass() + "; " +
               PerlUtil.getParentsChain(element));
      return UNKNOWN_VALUE;
    }
    int currentInstructionIndex = elementInstructionIndex;
    PerlOneOfValue.Builder valueBuilder = PerlOneOfValue.builder();
    ControlFlowUtil.iteratePrev(currentInstructionIndex, instructions, currentInstruction -> {
      if (!(currentInstruction instanceof PerlMutationInstruction)) {
        PsiElement instructionElement = currentInstruction.getElement();
        if ((instructionElement instanceof PerlSubDefinitionElement || instructionElement instanceof PerlSubExpr) &&
            lexicalDeclaration instanceof PerlBuiltInVariable && "_".equals(variableName) && actualType == PerlVariableType.ARRAY) {
          valueBuilder.addVariant(PerlValues.ARGUMENTS_VALUE);
          return CONTINUE;
        }
        if (Objects.equals(stopElement, instructionElement)) {
          return CONTINUE;
        }
        if (currentInstruction.num() == 1 && instructionElement != null && instructionElement.getContext() != null) {
          valueBuilder.addVariant(
            getValueFromControlFlow(instructionElement, namespaceName, variableName, actualType, lexicalDeclaration, stopElement));
        }
        return NEXT;
      }
      if (currentInstruction.num() >= currentInstructionIndex) {
        return NEXT;
      }
      PsiElement assignee = ((PerlMutationInstruction)currentInstruction).getLeftSide();
      if (!(assignee instanceof PerlVariable) || ((PerlVariable)assignee).getActualType() != actualType) {
        return NEXT;
      }
      if (!Objects.equals(variableName, ((PerlVariable)assignee).getName())) {
        return NEXT;
      }
      String explicitNamespaceName = ((PerlVariable)assignee).getExplicitNamespaceName();
      if ((explicitNamespaceName != null || namespaceName != null) && !Objects.equals(namespaceName, explicitNamespaceName)) {
        return NEXT;
      }
      PerlVariableDeclarationElement assigneeDeclaration = getLexicalDeclaration((PerlVariable)assignee);
      if (lexicalDeclaration == null && assigneeDeclaration == null && !(assignee.getParent() instanceof PerlVariableDeclarationElement) ||
          lexicalDeclaration != null &&
          (Objects.equals(lexicalDeclaration, assigneeDeclaration) || Objects.equals(lexicalDeclaration, assignee.getParent()))
      ) {
        valueBuilder.addVariant(((PerlMutationInstruction)currentInstruction).createValue());
      }
      return CONTINUE;
    });


    if (lexicalDeclaration != null) {
      PerlValue declaredValue = lexicalDeclaration.getDeclaredValue();
      if( !declaredValue.isUnknown()){
        valueBuilder.addVariant(declaredValue);
      }
    }
    return valueBuilder.build();
  }

  /**
   * @see #computeReturnValueFromControlFlow(PsiElement)
   */
  @NotNull
  public static AtomicNotNullLazyValue<PerlValue> computeReturnValueFromControlFlowLazy(@Nullable PsiElement subElement) {
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
  @NotNull
  public static PerlValue computeReturnValueFromControlFlow(PsiElement subElement) {
    PerlOneOfValue.Builder valueBuilder = PerlOneOfValue.builder();
    Instruction[] instructions = PerlControlFlowBuilder.getFor(subElement).getInstructions();
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
}
