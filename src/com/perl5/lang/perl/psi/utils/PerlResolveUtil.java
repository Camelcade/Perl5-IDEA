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

import com.intellij.openapi.util.Pair;
import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.util.Processor;
import com.perl5.lang.perl.extensions.PerlImplicitVariablesProvider;
import com.perl5.lang.perl.psi.PerlCompositeElement;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.references.scopes.PerlVariableDeclarationSearcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 17.02.2016.
 */
public class PerlResolveUtil {
  public static boolean treeWalkUp(@Nullable PsiElement place, @NotNull PsiScopeProcessor processor) {
    PsiElement lastParent = null;
    PsiElement run = place;
    ResolveState state = ResolveState.initial();
    while (run != null) {
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
      if (run instanceof PerlCompositeElement &&
          !(run instanceof PerlLexicalScope) &&
          !run.processDeclarations(processor, resolveState, null, place)
        ) {
        return false;
      }
      run = run.getPrevSibling();
    }

    // checking implicit variables
    if (element instanceof PerlImplicitVariablesProvider) {
      for (PerlVariableDeclarationElement wrapper : ((PerlImplicitVariablesProvider)element).getImplicitVariables()) {
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
    if (variable.getExplicitPackageName() != null) {
      return null;
    }
    PerlVariableDeclarationSearcher variableProcessor = new PerlVariableDeclarationSearcher(variable);
    PerlResolveUtil.treeWalkUp(variable, variableProcessor);
    return variableProcessor.getResult();
  }

  /**
   * Processing all targets of all references of all elements
   *
   * @param processor processor
   * @param elements  references sources
   * @return processor result
   */
  public static boolean processElementReferencesResolveResults(@NotNull Processor<Pair<PsiElement, PsiReference>> processor,
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
  public static boolean processResolveElements(@NotNull Processor<Pair<PsiElement, PsiReference>> processor,
                                               @Nullable PsiReference... references) {
    if (references == null || references.length == 0) {
      return true;
    }

    for (PsiReference reference : references) {
      if (reference instanceof PsiPolyVariantReference) {
        for (ResolveResult resolveResult : ((PsiPolyVariantReference)reference).multiResolve(false)) {
          PsiElement targetElement = resolveResult.getElement();
          if (targetElement != null && !processor.process(Pair.create(targetElement, reference))) {
            return false;
          }
        }
      }
      else if (reference != null) {
        PsiElement targetElement = reference.resolve();
        if (targetElement != null && !processor.process(Pair.create(targetElement, reference))) {
          return false;
        }
      }
    }
    return true;
  }
}
