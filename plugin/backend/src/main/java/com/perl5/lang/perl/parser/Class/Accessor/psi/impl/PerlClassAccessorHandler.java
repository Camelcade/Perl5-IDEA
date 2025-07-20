/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser.Class.Accessor.psi.impl;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.util.Function;
import com.perl5.lang.perl.psi.PerlSubCallHandler;
import com.perl5.lang.perl.psi.PsiPerlNamespaceContent;
import com.perl5.lang.perl.psi.impl.PerlSubCallElement;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.stubs.calls.PerlSubCallElementData;
import com.perl5.lang.perl.psi.stubs.calls.PerlSubCallElementStub;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlResolveUtilCore;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.util.PerlPackageUtilCore;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.perl5.lang.perl.parser.Class.Accessor.ClassAccessorElementTypes.CLASS_ACCESSOR_METHOD;
import static com.perl5.lang.perl.parser.Class.Accessor.psi.impl.PerlClassAccessorMethod.GETTER_COMPUTATION;
import static com.perl5.lang.perl.parser.Class.Accessor.psi.impl.PerlClassAccessorMethod.SETTER_COMPUTATION;

public abstract class PerlClassAccessorHandler extends PerlSubCallHandler<PerlClassAccessorCallData> {
  private static final Logger LOG = Logger.getInstance(PerlClassAccessorHandler.class);

  @Override
  public @NotNull List<? extends PerlDelegatingLightNamedElement<?>> computeLightElementsFromPsi(@NotNull PerlSubCallElement psiElement) {
    String packageName = PerlPackageUtilCore.getContextNamespaceName(psiElement);
    if (StringUtil.isEmpty(packageName)) {
      return Collections.emptyList();
    }

    List<PsiElement> listElements = psiElement.getCallArgumentsList();
    if (listElements.isEmpty()) {
      return Collections.emptyList();
    }

    List<PerlDelegatingLightNamedElement<?>> result = new ArrayList<>();
    for (PsiElement listElement : listElements) {
      if (!psiElement.isAcceptableIdentifierElement(listElement)) {
        continue;
      }
      String baseName = ElementManipulators.getValueText(listElement);
      PerlSubAnnotations subAnnotations = PerlSubAnnotations.computeForLightElement(psiElement, listElement);
      for (Function<String, String> computation : getNamesComputations(psiElement)) {
        result.add(new PerlClassAccessorMethod(
          psiElement,
          baseName,
          computation,
          CLASS_ACCESSOR_METHOD,
          listElement,
          packageName,
          subAnnotations
        ));
      }
    }
    return result;
  }

  @Override
  public @NotNull List<? extends PerlDelegatingLightNamedElement<?>> computeLightElementsFromStubs(@NotNull PerlSubCallElement psiElement,
                                                                                                   @NotNull PerlSubCallElementStub stubElement) {
    return stubElement.getLightNamedElementsStubs().stream()
      .filter(childStub -> childStub.getElementType() == CLASS_ACCESSOR_METHOD)
      .map(childStub -> new PerlClassAccessorMethod(psiElement, (PerlSubDefinitionStub)childStub))
      .collect(Collectors.toList());
  }

  private @NotNull List<Function<String, String>> getNamesComputations(@NotNull PerlSubCallElement psiElement) {
    if (!isFollowBestPractice(psiElement)) {
      return Collections.singletonList(PerlClassAccessorMethod.SIMPLE_COMPUTATION);
    }
    return getNamesComputationsImpl(psiElement);
  }

  protected abstract @NotNull List<Function<String, String>> getNamesComputationsImpl(@NotNull PerlSubCallElement psiElement);

  @Override
  public @NotNull PerlClassAccessorCallData computeCallData(@NotNull PerlSubCallElement subCallElement) {
    return new PerlClassAccessorCallData(isFollowBestPractice(subCallElement));
  }

  @Override
  public PerlClassAccessorCallData deserialize(@NotNull StubInputStream dataStream) throws IOException {
    return new PerlClassAccessorCallData(dataStream.readBoolean());
  }

  @Override
  public void serialize(@NotNull PerlSubCallElementData callData, @NotNull StubOutputStream dataStream) throws IOException {
    LOG.assertTrue(callData instanceof PerlClassAccessorCallData, "Got " + callData);
    dataStream.writeBoolean(((PerlClassAccessorCallData)callData).isFBP());
  }

  static boolean isFollowBestPractice(@NotNull PerlSubCallElement psiElement) {
    PerlSubCallElementStub stub = psiElement.getGreenStub();
    if (stub != null) {
      PerlSubCallElementData callData = stub.getCallData();
      LOG.assertTrue(callData instanceof PerlClassAccessorCallData, "Got: " + callData);
      return ((PerlClassAccessorCallData)callData).isFBP();
    }
    return CachedValuesManager.getCachedValue(psiElement, () -> {
      Ref<Boolean> result = Ref.create(Boolean.FALSE);

      // fixme we need a smarter treewalkup here, scopes are not necessary here
      PerlResolveUtilCore.treeWalkUp(psiElement, (element, state) -> {
        if (element instanceof PsiPerlNamespaceContent) {
          return false;
        }

        if (element instanceof PerlSubCallElement subCallElement &&
            StringUtil.equals("follow_best_practice", subCallElement.getSubName())) {
          result.set(Boolean.TRUE);
          return false;
        }
        return true;
      });
      return CachedValueProvider.Result.create(result.get(), psiElement);
    }) == Boolean.TRUE;
  }

  public static class PerlClassAccessorRoHandler extends PerlClassAccessorHandler {
    @Override
    protected @NotNull List<Function<String, String>> getNamesComputationsImpl(@NotNull PerlSubCallElement psiElement) {
      return Collections.singletonList(GETTER_COMPUTATION);
    }
  }

  public static class PerlClassAccessorWoHandler extends PerlClassAccessorHandler {
    @Override
    protected @NotNull List<Function<String, String>> getNamesComputationsImpl(@NotNull PerlSubCallElement psiElement) {
      return Collections.singletonList(SETTER_COMPUTATION);
    }
  }

  public static class PerlClassAccessorRWHandler extends PerlClassAccessorHandler {
    @Override
    protected @NotNull List<Function<String, String>> getNamesComputationsImpl(@NotNull PerlSubCallElement psiElement) {
      return Arrays.asList(GETTER_COMPUTATION, SETTER_COMPUTATION);
    }
  }
}
