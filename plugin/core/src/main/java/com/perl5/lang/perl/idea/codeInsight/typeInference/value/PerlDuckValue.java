/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.codeInsight.typeInference.value;

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.mro.PerlMro;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

public class PerlDuckValue extends PerlListValue {
  private static final Set<String> GENERIC_NAMES = Set.of("new", "isa", "DOES", "can", "VERSION");
  private PerlDuckValue(@NotNull List<PerlValue> elements) {
    super(elements);
  }

  PerlDuckValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
  }

  @Override
  protected @NotNull PerlValue computeResolve(@NotNull PerlValueResolver resolver, @NotNull List<PerlValue> resolvedElements) {
    if (isEmpty() || !isDuckTypingEnabled()) {
      return UNKNOWN_VALUE;
    }
    var usedCallableNames = resolvedElements.stream()
      .flatMap(it -> it.getSubNames().stream())
      .distinct()
      .sorted()
      .collect(Collectors.toList());

    if (usedCallableNames.isEmpty()) {
      return UNKNOWN_VALUE;
    }

    var baseSubName = usedCallableNames.remove(0);
    var baseNamespaces = new HashSet<String>();
    PerlPackageUtil.processCallablesNamespaceNames(resolver, baseSubName, it -> {
      baseNamespaces.add(it.getNamespaceName());
      return true;
    });

    if (baseNamespaces.isEmpty()) {
      return UNKNOWN_VALUE;
    }

    var valueBuilder = PerlOneOfValue.builder();
    baseNamespaces.forEach(namespaceName -> processNamespacesWithAllCallables(
      namespaceName, usedCallableNames, resolver, new HashSet<>(), it -> valueBuilder.addVariant(PerlScalarValue.create(it))));
    return valueBuilder.build();
  }

  private static void processNamespacesWithAllCallables(@Nullable String namespaceName,
                                                        @NotNull Collection<String> callableNames,
                                                        @NotNull PerlValueResolver resolver,
                                                        @NotNull Set<String> recursionControlSet,
                                                        @NotNull Consumer<String> namespaceNameConsumer) {
    if (StringUtil.isEmpty(namespaceName) || !recursionControlSet.add(namespaceName) || callableNames.isEmpty()) {
      return;
    }
    ProgressManager.checkCanceled();
    var callablesLeft = new HashSet<>(callableNames);
    var project = resolver.getProject();
    var resolveScope = resolver.getResolveScope();
    boolean notFinished = PerlMro.processCallables(project, resolveScope, namespaceName, callablesLeft, false, it -> {
      callablesLeft.remove(it.getCallableName());
      return !callablesLeft.isEmpty();
    }, false);

    if (!notFinished) {
      namespaceNameConsumer.accept(namespaceName);
      return;
    }

    var callablesToFind = Collections.unmodifiableSet(callablesLeft);
    for (PerlNamespaceDefinitionElement childNamespace : PerlPackageUtil.getChildNamespaces(project, namespaceName, resolveScope)) {
      processNamespacesWithAllCallables(
        childNamespace.getNamespaceName(), callablesToFind, resolver, recursionControlSet, namespaceNameConsumer);
    }
  }

  @Override
  public String toString() {
    return "DuckType: " + getElements().stream().map(Object::toString).sorted().toList();
  }

  @Override
  protected boolean computeIsDeterministic() {
    return isEmpty();
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.DUCK_TYPE_ID;
  }

  public static boolean isDuckTypingEnabled() {
    return Registry.is("perl5.duck.typing.support", true);
  }

  public static final class Builder {
    private final Set<PerlValue> myElements = new HashSet<>();

    private Builder() {
    }

    public @NotNull PerlValue build() {
      return isEmpty() ? UNKNOWN_VALUE : new PerlDuckValue(List.copyOf(myElements));
    }

    public @NotNull Builder addElement(@NotNull String callableName) {
      if (!GENERIC_NAMES.contains(callableName)) {
        myElements.add(PerlScalarValue.create(callableName));
      }
      return this;
    }

    public boolean isEmpty() {
      return myElements.isEmpty();
    }
  }

  public static @NotNull Builder builder() {
    return new Builder();
  }
}
