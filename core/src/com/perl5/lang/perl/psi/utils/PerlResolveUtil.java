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

import com.intellij.codeInsight.controlflow.ControlFlowUtil;
import com.intellij.codeInsight.controlflow.Instruction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ObjectUtils;
import com.intellij.util.PairProcessor;
import com.perl5.lang.perl.extensions.PerlImplicitVariablesProvider;
import com.perl5.lang.perl.idea.codeInsight.controlFlow.PerlAssignInstruction;
import com.perl5.lang.perl.idea.codeInsight.controlFlow.PerlControlFlowBuilder;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlOneOfValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.psi.PerlCompositeElement;
import com.perl5.lang.perl.psi.PerlFile;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.references.scopes.PerlVariableDeclarationSearcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.intellij.codeInsight.controlflow.ControlFlowUtil.Operation.CONTINUE;
import static com.intellij.codeInsight.controlflow.ControlFlowUtil.Operation.NEXT;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlUnknownValue.UNKNOWN_VALUE;

/**
 * Created by hurricup on 17.02.2016.
 */
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
   * Processing all targets of all references of all elements
   *
   * @param processor processor
   * @param elements  references sources
   * @return processor result
   */
  @SuppressWarnings("UnusedReturnValue")
  public static boolean processElementReferencesResolveResults(@NotNull PairProcessor<PsiElement, PsiReference> processor,
                                                               PsiElement... elements) {
    if (elements == null || elements.length == 0) {
      return true;
    }

    for (PsiElement element : elements) {
      if (!processResolveElements(processor, element.getReferences())) {
        return false;
      }
    }

    return true;
  }


  /**
   * Processing target elements of array of references
   *
   * @param processor  processor
   * @param references references to iterate
   * @return processor result
   */
  public static boolean processResolveElements(@NotNull PairProcessor<PsiElement, PsiReference> processor,
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
    PerlLexicalScope perlLexicalScope;
    if (lexicalDeclaration instanceof PerlImplicitVariableDeclaration) {
      PsiElement declarationContext = lexicalDeclaration.getContext();
      if (declarationContext == null) {
        perlLexicalScope = ObjectUtils.tryCast(variable.getContainingFile(), PerlFile.class);
      }
      else {
        stopElement = declarationContext;
        perlLexicalScope = PsiTreeUtil.getParentOfType(declarationContext, PerlLexicalScope.class, false);
      }
    }
    else {
      // fixme we probably should check containing file context
      perlLexicalScope = lexicalDeclaration == null
                         ? ObjectUtils.tryCast(variable.getContainingFile(), PerlFile.class)
                         : PsiTreeUtil.getParentOfType(lexicalDeclaration, PerlLexicalScope.class);
    }
    if (perlLexicalScope == null) {
      LOG.error("Unable to find lexical scope for:" +
                variable.getClass() +
                " at " +
                variable.getTextOffset() +
                " in " +
                PsiUtilCore.getVirtualFile(variable));
      return UNKNOWN_VALUE;
    }

    Instruction[] instructions = PerlControlFlowBuilder.getFor(perlLexicalScope).getInstructions();
    int currentInstructionIndex = ControlFlowUtil.findInstructionNumberByElement(instructions, variable);
    if (currentInstructionIndex < 0) {
      if (!Objects.equals(variable.getContainingFile(), perlLexicalScope.getContainingFile())) {
        // fixme should handle getContext. One of this item in the generated file
        return UNKNOWN_VALUE;
      }
      LOG.error("Unable to find an instruction for " +
                variable.getText() + "; " +
                variable.getTextRange() + "; " +
                PsiUtilCore.getVirtualFile(variable));
      return UNKNOWN_VALUE;
    }
    PerlOneOfValue.Builder valueBuilder = PerlOneOfValue.builder();

    String variableName = variable.getName();
    String namespaceName = variable.getExplicitNamespaceName();
    PerlVariableType actualType = variable.getActualType();
    PsiElement finalStopElement = stopElement;

    ControlFlowUtil.iteratePrev(currentInstructionIndex, instructions, currentInstruction -> {
      if (!(currentInstruction instanceof PerlAssignInstruction)) {
        return Objects.equals(finalStopElement, currentInstruction.getElement()) ? CONTINUE : NEXT;
      }
      if (currentInstruction.num() > currentInstructionIndex) {
        return NEXT;
      }
      PsiElement assignee = ((PerlAssignInstruction)currentInstruction).getLeftSide();
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
      valueBuilder.addVariant(PerlValue.from(assignee, ((PerlAssignInstruction)currentInstruction).getRightSide()));
      return CONTINUE;
    });

    return valueBuilder.build();
  }
}
