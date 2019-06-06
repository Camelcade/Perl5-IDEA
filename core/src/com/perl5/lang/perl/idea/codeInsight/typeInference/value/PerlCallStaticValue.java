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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Processor;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.extensions.imports.PerlImportsProvider;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public final class PerlCallStaticValue extends PerlCallValue {
  private final boolean myHasExplicitNamespace;

  public PerlCallStaticValue(@NotNull PerlValue namespaceNameValue,
                             @NotNull PerlValue subNameValue,
                             @NotNull List<PerlValue> arguments,
                             boolean hasExplicitNamespace) {
    super(namespaceNameValue, subNameValue, arguments);
    myHasExplicitNamespace = hasExplicitNamespace;
  }

  PerlCallStaticValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
    myHasExplicitNamespace = deserializer.readBoolean();
  }

  @Override
  protected void serializeData(@NotNull PerlValueSerializer serializer) throws IOException {
    super.serializeData(serializer);
    serializer.writeBoolean(myHasExplicitNamespace);
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.CALL_STATIC_ID;
  }

  // fixme resolve namespace and subs first
  @Override
  public boolean processTargetNamespaceElements(@NotNull Project project,
                                                @NotNull GlobalSearchScope searchScope,
                                                @NotNull PsiElement contextElement,
                                                @NotNull PerlNamespaceItemProcessor<? super PsiNamedElement> processor) {
    for (String currentNamespaceName : getNamespaceNameValue().resolve(contextElement).getNamespaceNames()) {
      if (!processTargetNamespaceElements(project, searchScope, processor, currentNamespaceName)) {
        return false;
      }
    }

    PerlNamespaceDefinitionElement containingNamespace = PerlPackageUtil.getContainingNamespace(contextElement.getOriginalElement());
    String namespaceName = containingNamespace == null ? null : containingNamespace.getNamespaceName();
    if (!StringUtil.isEmpty(namespaceName)) {
      processExportDescriptors(
        project, searchScope, processor, PerlImportsProvider.getAllExportDescriptors(containingNamespace));
    }
    return true;
  }

  @Override
  protected boolean processCallTargets(@NotNull Project project,
                                       @NotNull GlobalSearchScope searchScope,
                                       @Nullable PsiElement contextElement,
                                       @NotNull Set<String> namespaceNames,
                                       @NotNull Set<String> subNames,
                                       @NotNull Processor<? super PsiNamedElement> processor) {
    for (String contextNamespace : namespaceNames) {
      ProcessingContext processingContext = new ProcessingContext();
      processingContext.processBuiltIns = !myHasExplicitNamespace;
      if (!processItemsInNamespace(project, searchScope, subNames, processor, contextNamespace, processingContext, contextElement)) {
        return false;
      }
    }

    if (!myHasExplicitNamespace && contextElement != null) {
      PerlNamespaceDefinitionElement containingNamespace = PerlPackageUtil.getContainingNamespace(contextElement.getOriginalElement());
      String namespaceName = containingNamespace == null ? null : containingNamespace.getNamespaceName();
      if (!StringUtil.isEmpty(namespaceName)) {
        processExportDescriptorsItems(
          project, searchScope, subNames, processor::process, PerlImportsProvider.getAllExportDescriptors(containingNamespace));
      }
    }

    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    PerlCallStaticValue aStatic = (PerlCallStaticValue)o;

    return myHasExplicitNamespace == aStatic.myHasExplicitNamespace;
  }

  @Override
  protected int computeHashCode() {
    int result = super.computeHashCode();
    result = 31 * result + (myHasExplicitNamespace ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return getNamespaceNameValue() + "::" + getSubNameValue() + getArgumentsAsString();
  }

  @NotNull
  @Override
  public String getPresentableText() {
    return PerlBundle.message(
      "perl.value.call.static.presentable",
      getNamespaceNameValue().getPresentableText(),
      getSubNameValue().getPresentableText(),
      getPresentableArguments());
  }
}
