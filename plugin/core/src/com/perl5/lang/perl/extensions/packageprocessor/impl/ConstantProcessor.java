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

package com.perl5.lang.perl.extensions.packageprocessor.impl;

import com.intellij.psi.ElementManipulators;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPragmaProcessorBase;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager;
import com.perl5.lang.perl.idea.folding.PerlFoldingSettings;
import com.perl5.lang.perl.parser.constant.psi.light.PerlLightConstantDefinitionElement;
import com.perl5.lang.perl.psi.PsiPerlAnonHash;
import com.perl5.lang.perl.psi.PsiPerlExpr;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElementBase;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.stubs.imports.PerlUseStatementStub;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.util.PerlHashUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.LIGHT_SUB_DEFINITION;

public class ConstantProcessor extends PerlPragmaProcessorBase {
  @NotNull
  @Override
  public List<PerlDelegatingLightNamedElement> computeLightElementsFromPsi(@NotNull PerlUseStatementElement useStatementElement) {
    PsiPerlExpr useArguments = useStatementElement.getExpr();
    if (useArguments == null) {
      return Collections.emptyList();
    }
    boolean multipleDefinition = useArguments instanceof PsiPerlAnonHash;

    List<PerlDelegatingLightNamedElement> result = new ArrayList<>();
    PerlHashUtil.processHashElements(useArguments, (keyElement, valElement) -> {
      if (useStatementElement.isAcceptableIdentifierElement(keyElement)) {
        result.add(new PerlLightConstantDefinitionElement(
          useStatementElement,
          ElementManipulators.getValueText(keyElement),
          LIGHT_SUB_DEFINITION,
          keyElement,
          PerlPackageUtil.getContextNamespaceName(useStatementElement),
          Collections.emptyList(),
          PerlSubAnnotations.tryToFindAnnotations(keyElement, useArguments, useStatementElement),
          PerlValuesManager.lazy(valElement)
        ));
      }

      return multipleDefinition;
    });

    return result;
  }

  @NotNull
  @Override
  public List<PerlDelegatingLightNamedElement> computeLightElementsFromStubs(@NotNull PerlUseStatementElement useStatementElement,
                                                                             @NotNull PerlUseStatementStub useStatementStub) {
    return useStatementStub.getLightNamedElementsStubs().stream()
      .filter(childStub -> childStub.getStubType() == LIGHT_SUB_DEFINITION)
      .map(childStub -> new PerlLightConstantDefinitionElement(useStatementElement, (PerlSubDefinitionStub)childStub))
      .collect(Collectors.toList());
  }

  @Nullable
  @Override
  public String getArgumentsFoldingText(@NotNull PerlUseStatementElementBase useStatementElement) {
    return PerlBundle.message("perl.fold.ph.text.constant");
  }

  @Override
  public boolean isFoldedByDefault(@NotNull PerlUseStatementElementBase useStatementElement) {
    return PerlFoldingSettings.getInstance().COLLAPSE_CONSTANT_BLOCKS;
  }
}
