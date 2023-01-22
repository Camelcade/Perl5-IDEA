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

package com.perl5.lang.perl.psi.references;

import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.psi.PsiPerlLabelExpr;
import com.perl5.lang.perl.psi.references.providers.PerlAnnotationVariableReferenceProvider;
import com.perl5.lang.perl.psi.references.providers.PerlSimpleSubReferenceProvider;
import com.perl5.lang.perl.psi.references.providers.PerlSubReferenceProvider;
import com.perl5.lang.perl.psi.references.providers.PerlVariableReferencesProvider;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.idea.PerlElementPatterns.*;


public class PerlReferencesContributor extends PsiReferenceContributor {
  @Override
  public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
    registrar.registerReferenceProvider(
      EXPORT_ASSIGNED_STRING_CONTENT,
      new PerlSimpleSubReferenceProvider()
    );

    registrar.registerReferenceProvider(VARIABLE_NAME_PATTERN, new PerlVariableReferencesProvider());
    registrar.registerReferenceProvider(ANNOTATION_VARIABLE_PATTERN, new PerlAnnotationVariableReferenceProvider());

    registrar.registerReferenceProvider(LABEL_EXPR_PATTERN, new PsiReferenceProvider() {
      @Override
      public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        return new PsiReference[]{new PerlLabelReference((PsiPerlLabelExpr)element)};
      }
    });

    registrar.registerReferenceProvider(SUB_NAME_PATTERN, new PerlSubReferenceProvider());
  }
}
