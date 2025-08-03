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

package com.perl5.lang.perl.idea.codeInsight.typeInference.value.serialization

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.Processor
import com.perl5.lang.perl.extensions.imports.PerlImportsProvider
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.*
import com.perl5.lang.perl.util.PerlNamespaceUtil
import com.perl5.lang.perl.util.PerlPackageUtilCore


class PerlCallStaticValueBackendHelper : PerlCallValueBackendHelper<PerlCallStaticValue>() {
  override val serializationId: Int
    get() = PerlValueBackendHelper.CALL_STATIC_ID

  override fun serializeData(value: PerlCallStaticValue, serializer: PerlValueSerializer) {
    super.serializeData(value, serializer)
    serializer.writeBoolean(value.hasExplicitNamespace())
  }

  override fun deserialize(
    deserializer: PerlValueDeserializer,
    baseValue: PerlValue,
    parameter: PerlValue,
    arguments: List<PerlValue>
  ): PerlValue = PerlCallStaticValue(baseValue, parameter, arguments, deserializer.readBoolean())

  // fixme resolve namespace and subs first
  override fun processTargetNamespaceElements(
    callValue: PerlCallStaticValue,
    contextElement: PsiElement,
    processor: PerlNamespaceItemProcessor<in PsiNamedElement>
  ): Boolean {
    val project: Project = contextElement.project
    val searchScope = contextElement.resolveScope
    for (currentNamespaceName in callValue.namespaceNameValue.resolve(contextElement).namespaceNames) {
      if (!processTargetNamespaceElements(project, searchScope, processor, currentNamespaceName, contextElement)) {
        return false
      }
    }

    val containingNamespace = PerlPackageUtilCore.getContainingNamespace(contextElement.originalElement)
    val namespaceName = containingNamespace?.getNamespaceName()
    if (!StringUtil.isEmpty(namespaceName)) {
      processExportDescriptors(
        project, searchScope, processor, PerlImportsProvider.getAllExportDescriptors(containingNamespace)
      )
    }
    return true
  }

  override fun processCallTargets(
    callValue: PerlCallStaticValue,
    project: Project,
    searchScope: GlobalSearchScope,
    contextElement: PsiElement?,
    namespaceNames: MutableSet<String>,
    subNames: MutableSet<String>,
    processor: Processor<in PsiNamedElement?>
  ): Boolean {
    for (contextNamespace in namespaceNames) {
      val processingContext = ProcessingContext()
      processingContext.processBuiltIns = !callValue.hasExplicitNamespace()
      if (!processItemsInNamespace(project, searchScope, subNames, processor, contextNamespace, processingContext, contextElement)) {
        return false
      }
    }

    if (!callValue.hasExplicitNamespace() && contextElement != null) {
      val containingNamespace = PerlPackageUtilCore.getContainingNamespace(contextElement.originalElement)
      val namespaceName = containingNamespace?.getNamespaceName()
      if (!StringUtil.isEmpty(namespaceName)) {
        processExportDescriptorsItems(
          project, searchScope, subNames, processor, PerlImportsProvider.getAllExportDescriptors(containingNamespace)
        )
      }
    }

    return true
  }

  override fun addFallbackTargets(
    callValue: PerlCallStaticValue,
    namespaceNames: MutableSet<String>,
    subNames: MutableSet<String>,
    resolvedArguments: MutableList<PerlValue>,
    hasTarget: Boolean,
    builder: PerlOneOfValue.Builder,
    resolvedNamespaceValue: PerlValue,
    resolver: PerlValueResolver
  ) {
    if (!hasTarget && callValue.hasExplicitNamespace() && subNames.size == 1 && namespaceNames.size == 1 && resolvedArguments.isEmpty()) {
      val possiblePackageName = PerlPackageUtilCore.join(namespaceNames.iterator().next(), subNames.iterator().next())
      if (!PerlNamespaceUtil.getNamespaceDefinitions(resolver.project, resolver.resolveScope, possiblePackageName).isEmpty()) {
        builder.addVariant(PerlScalarValue.create(possiblePackageName))
      }
    }
  }
}