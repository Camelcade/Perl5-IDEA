/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.intellilang;

import com.intellij.codeInsight.completion.CompletionUtil;
import com.intellij.lang.Language;
import com.intellij.openapi.application.Experiments;
import com.intellij.openapi.util.RecursionManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.util.Query;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;


public class PerlStringLanguageInjector implements LanguageInjector {
  @Override
  public void getLanguagesToInject(@NotNull PsiLanguageInjectionHost host, @NotNull InjectedLanguagePlaces injectionPlacesRegistrar) {
    if (!(host instanceof PerlString) || !host.isValidHost()) {
      return;
    }
    // before element
    PerlAnnotationInject injectAnnotation = PerlPsiUtil.getAnyAnnotationByClass(host, PerlAnnotationInject.class);
    if (injectAnnotation != null) {
      injectByAnnotation(host, injectionPlacesRegistrar, injectAnnotation);
    }

    // program context
    if (Experiments.getInstance().isFeatureEnabled("perl5.eval.auto.injection")) {
      PsiElement context = getPerlInjectionContext(host);
      if (context != null) {
        injectLanguage(host, injectionPlacesRegistrar, PerlLanguage.INSTANCE);
      }
    }
  }

  @Contract("null->null;!null->!null")
  public static @Nullable PsiElement getInjectionContextOrSelf(@Nullable PsiElement context) {
    if (context instanceof PsiLanguageInjectionHost) {
      return Objects.requireNonNullElse(getPerlInjectionContext((PsiLanguageInjectionHost)context), context);
    }
    return context;
  }

  public static @Nullable PsiElement getPerlInjectionContext(@NotNull PsiLanguageInjectionHost host) {
    return CachedValuesManager.getCachedValue(host, () -> CachedValueProvider.Result.create(
      RecursionManager.doPreventingRecursion(host, true, () -> computeInjectionContext(host)), host));
  }

  private static @Nullable PsiElement computeInjectionContext(@NotNull PsiLanguageInjectionHost host) {
    if (host instanceof PsiPerlStringXq) {
      return null;
    }
    PsiElement parent = host.getParent();
    if (parent instanceof PsiPerlEvalExpr) {
      return parent;
    }

    if (!(parent instanceof PsiPerlAssignExpr)) {
      return null;
    }

    PsiElement variable = ((PsiPerlAssignExpr)parent).getLeftPartOfAssignment(host);
    if (variable instanceof PerlVariableDeclarationExpr) {
      List<PsiPerlVariableDeclarationElement> variables = ((PerlVariableDeclarationExpr)variable).getVariableDeclarationElementList();
      if (variables.size() != 1) {
        return null;
      }
      variable = variables.get(0);
    }
    else if (variable instanceof PsiPerlScalarVariable) {
      PerlVariableDeclarationElement variableDeclarationElement = ((PsiPerlScalarVariable)variable).getLexicalDeclaration();
      if (variableDeclarationElement == null) {
        return null;
      }
      variable = variableDeclarationElement;
    }
    else {
      return null;
    }

    PsiElement realVariable = CompletionUtil.getOriginalOrSelf(variable);
    Query<PsiReference> references =
      ReferencesSearch.search(realVariable, GlobalSearchScope.fileScope(host.getContainingFile().getOriginalFile()));
    for (PsiReference reference : references) {
      PsiElement referenceElement = reference.getElement();
      PsiElement variableUsage = referenceElement.getParent();
      PsiElement usageContext = variableUsage.getParent();
      if (usageContext instanceof PsiPerlEvalExpr) {
        return usageContext;
      }
    }
    return null;
  }

  protected void injectByAnnotation(@NotNull PsiLanguageInjectionHost host,
                                    @NotNull InjectedLanguagePlaces injectionPlacesRegistrar,
                                    PerlAnnotationInject injectAnnotation) {
    String languageMarker = injectAnnotation.getLanguageMarker();
    if (languageMarker == null) {
      return;
    }
    injectLanguage(host, injectionPlacesRegistrar,
                   PerlInjectionMarkersService.getInstance(host.getProject()).getLanguageByMarker(languageMarker));
  }

  private void injectLanguage(@NotNull PsiLanguageInjectionHost host,
                              @NotNull InjectedLanguagePlaces injectionPlacesRegistrar,
                              Language targetLanguage) {
    if (targetLanguage == null) {
      return;
    }
    TextRange contentRange = ElementManipulators.getValueTextRange(host);
    if (contentRange.isEmpty()) {
      return;
    }
    injectionPlacesRegistrar.addPlace(targetLanguage, contentRange, null, null);
  }
}
