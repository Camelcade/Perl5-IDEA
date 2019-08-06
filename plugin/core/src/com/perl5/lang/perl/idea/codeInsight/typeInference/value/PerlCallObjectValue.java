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

package com.perl5.lang.perl.idea.codeInsight.typeInference.value;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ObjectUtils;
import com.intellij.util.Processor;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.mro.PerlMro;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class PerlCallObjectValue extends PerlCallValue {
  @Nullable
  private final String mySuperContext;

  private PerlCallObjectValue(@NotNull PerlValue namespaceNameValue,
                             @NotNull PerlValue subNameValue,
                             @NotNull List<PerlValue> arguments,
                             @Nullable String superContext) {
    super(namespaceNameValue, subNameValue, arguments);
    mySuperContext = superContext;
  }

  PerlCallObjectValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
    mySuperContext = deserializer.readNameString();
  }

  @Override
  protected void serializeData(@NotNull PerlValueSerializer serializer) throws IOException {
    super.serializeData(serializer);
    serializer.writeName(mySuperContext);
  }

  @NotNull
  @Override
  protected List<PerlValue> computeResolvedArguments(@NotNull PerlValue resolvedNamespaceValue, @NotNull PerlValueResolver valueResolver) {
    return ContainerUtil.prepend(super.computeResolvedArguments(resolvedNamespaceValue, valueResolver), resolvedNamespaceValue);
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.CALL_OBJECT_ID;
  }

  @Override
  public boolean processTargetNamespaceElements(@NotNull PsiElement contextElement,
                                                @NotNull PerlNamespaceItemProcessor<? super PsiNamedElement> processor) {
    Project project = contextElement.getProject();
    GlobalSearchScope searchScope = contextElement.getResolveScope();
    for (String contextNamespace : getNamespaceNameValue().resolve(contextElement).getNamespaceNames()) {
      for (String currentNamespaceName : PerlMro.getLinearISA(project, searchScope, getEffectiveNamespaceName(contextNamespace), isSuper())) {
        if (!processTargetNamespaceElements(project, searchScope, processor, currentNamespaceName, contextElement)) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean isSuper() {
    return mySuperContext != null;
  }

  @NotNull
  private String getEffectiveNamespaceName(String contextNamespace) {
    return ObjectUtils.notNull(mySuperContext, contextNamespace);
  }

  @NotNull
  @Override
  protected Set<String> computeNamespaceNames(@NotNull PerlValue resolvedNamespaceValue) {
    return resolvedNamespaceValue.isUnknown() ? Collections.singleton(PerlPackageUtil.UNIVERSAL_NAMESPACE) :
           super.computeNamespaceNames(resolvedNamespaceValue);
  }

  @Override
  protected void addFallbackTargets(@NotNull Set<String> namespaceNames,
                                    @NotNull Set<String> subNames,
                                    @NotNull List<PerlValue> resolvedArguments,
                                    boolean hasTarget,
                                    @NotNull PerlOneOfValue.Builder builder,
                                    @NotNull PerlValue resolvedNamespaceValue,
                                    @NotNull PerlValueResolver resolver) {
    if (subNames.size() == 1 && "new".equals(subNames.iterator().next())) {
      builder.addVariant(resolvedNamespaceValue);
    }
  }

  @Override
  protected boolean processCallTargets(@NotNull Project project,
                                       @NotNull GlobalSearchScope searchScope,
                                       @Nullable PsiElement contextElement,
                                       @NotNull Set<String> namespaceNames,
                                       @NotNull Set<String> subNames,
                                       @NotNull Processor<? super PsiNamedElement> processor) {
    for (String contextNamespace : namespaceNames) {
      for (String currentNamespaceName : PerlMro.getLinearISA(project, searchScope, getEffectiveNamespaceName(contextNamespace), isSuper())) {
        ProcessingContext processingContext = new ProcessingContext();
        processingContext.processBuiltIns = false;
        if (!processItemsInNamespace(project, searchScope, subNames, processor, currentNamespaceName, processingContext, contextElement)) {
          return false;
        }
        if (!processingContext.processAutoload) { // marker that we've got at least one result
          break;
        }
      }
    }
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PerlCallObjectValue)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    PerlCallObjectValue value = (PerlCallObjectValue)o;

    return mySuperContext != null ? mySuperContext.equals(value.mySuperContext) : value.mySuperContext == null;
  }

  @Override
  protected int computeHashCode() {
    int result = super.computeHashCode();
    result = 31 * result + (mySuperContext != null ? mySuperContext.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return getNamespaceNameValue() + "->" + (isSuper() ? mySuperContext + "::SUPER::" : "") + getSubNameValue() + getArgumentsAsString();
  }

  @NotNull
  @Override
  public String getPresentableText() {
    return PerlBundle.message(
      "perl.value.call.object.presentable",
      getNamespaceNameValue().getPresentableText(),
      getSubNameValue().getPresentableText(),
      getPresentableArguments());
  }

  @NotNull
  public static PerlValue create(@NotNull PerlValue namespaceValue, @NotNull String name) {
    return create(namespaceValue, name, Collections.emptyList(), null);
  }

  @NotNull
  public static PerlValue create(@NotNull PerlValue namespaceValue, @NotNull String name, @Nullable String superContext) {
    return create(namespaceValue, name, Collections.emptyList(), superContext);
  }

  @NotNull
  public static PerlValue create(@NotNull String namespace, @NotNull String name, @NotNull List<PerlValue> arguments) {
    return create(PerlScalarValue.create(namespace), name, arguments, null);
  }

  @NotNull
  public static PerlValue create(@NotNull PerlValue namespaceNameValue,
                                 @NotNull String name,
                                 @NotNull List<PerlValue> arguments) {
    return create(namespaceNameValue, PerlScalarValue.create(name), arguments, null);
  }

  @NotNull
  public static PerlValue create(@NotNull PerlValue namespaceNameValue,
                                 @NotNull String name,
                                 @NotNull List<PerlValue> arguments,
                                 @Nullable String superContext) {
    return create(namespaceNameValue, PerlScalarValue.create(name), arguments, superContext);
  }

  @NotNull
  public static PerlValue create(@NotNull PerlValue namespaceNameValue,
                                  @NotNull PerlValue nameValue,
                                  @NotNull List<PerlValue> arguments,
                                  @Nullable String superContext) {
    return PerlValuesManager.intern(new PerlCallObjectValue(namespaceNameValue, nameValue, arguments, superContext));
  }
}
