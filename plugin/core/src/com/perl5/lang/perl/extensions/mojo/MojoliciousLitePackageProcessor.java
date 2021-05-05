/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package com.perl5.lang.perl.extensions.mojo;

import com.perl5.lang.perl.extensions.packageprocessor.PerlFeaturesProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageParentsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlUtfProvider;
import com.perl5.lang.perl.extensions.packageprocessor.impl.BaseStrictWarningsProvidingProcessor;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.*;
import com.perl5.lang.perl.internals.PerlFeaturesTable;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.stubs.imports.PerlUseStatementStub;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.DELEGATE_METHOD_ARGUMENTS_LIST;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

public class MojoliciousLitePackageProcessor extends BaseStrictWarningsProvidingProcessor implements
                                                                                          PerlUtfProvider,
                                                                                          PerlFeaturesProvider,
                                                                                          PerlPackageParentsProvider {
  public static final String MOJOLICIOUS_LITE = "Mojolicious::Lite";
  private static final List<String> ROUTES_METHODS = Arrays.asList(
    "any", "get", "options", "patch", "post", "put", "websocket",
    // actually this method uses $root, but this is good enough for now
    "under"
  );
  private static final List<String> APP_METHODS = Arrays.asList("helper", "hook", "plugin");


  @Override
  public void changeParentsList(@NotNull PerlUseStatementElement useStatement, @NotNull List<String> currentList) {
    currentList.add(MOJOLICIOUS_LITE);
  }

  @Override
  public PerlFeaturesTable getFeaturesTable(PerlUseStatementElement useStatement, PerlFeaturesTable currentFeaturesTable) {
    return currentFeaturesTable.clone();
  }

  @Override
  public boolean hasPackageFilesOptions() {
    return false;
  }

  @Override
  public @NotNull List<PerlDelegatingLightNamedElement<?>> computeLightElementsFromPsi(@NotNull PerlUseStatementElement useStatementElement) {
    List<PerlDelegatingLightNamedElement<?>> result = new ArrayList<>();
    String contextNamespace = useStatementElement.getNamespaceName();
    List<PerlValue> constructorArguments = Arrays.asList(PerlScalarValue.create("moniker"), PerlValues.DUMMY_SCALAR);
    // there is a moniker argument, but it is meaningless here
    PerlValue app = PerlCallObjectValue.create(PerlScalarValue.create(contextNamespace), "new", constructorArguments, contextNamespace);

    result.add(createLightMethod(useStatementElement, contextNamespace, "app", app));
    result.add(createLightMethod(useStatementElement, contextNamespace, "new", app));

    PerlValue routes = PerlCallObjectValue.create(app, "routes");
    ROUTES_METHODS.forEach(it -> result.add(
      createLightMethod(useStatementElement, contextNamespace, it,
                        PerlCallObjectValue.create(routes, it, DELEGATE_METHOD_ARGUMENTS_LIST))));

    result.add(createLightMethod(useStatementElement, contextNamespace, "del",
                                 PerlCallObjectValue.create(routes, "delete", DELEGATE_METHOD_ARGUMENTS_LIST)));
    result.add(createLightMethod(useStatementElement, contextNamespace, "group", UNKNOWN_VALUE));

    APP_METHODS.forEach(it -> result.add(createLightMethod(
      useStatementElement, contextNamespace, it, PerlCallObjectValue.create(app, it, DELEGATE_METHOD_ARGUMENTS_LIST, contextNamespace))));

    return result;
  }

  public @NotNull MojoLightDelegatingSubDefinition createLightMethod(@NotNull PerlUseStatementElement useStatementElement,
                                                                     String contextNamespace,
                                                                     String methodName,
                                                                     PerlValue app) {
    return new MojoLightDelegatingSubDefinition(
      useStatementElement,
      contextNamespace, methodName,
      PerlValuesManager.lazy(app)
    );
  }

  @Override
  public @NotNull List<PerlDelegatingLightNamedElement<?>> computeLightElementsFromStubs(@NotNull PerlUseStatementElement useStatementElement,
                                                                                         @NotNull PerlUseStatementStub useStatementStub) {
    return computeLightElementsFromPsi(useStatementElement);
  }
}
