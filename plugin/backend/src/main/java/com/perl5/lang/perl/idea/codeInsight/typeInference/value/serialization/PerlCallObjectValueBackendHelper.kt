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
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.Processor
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.*
import com.perl5.lang.perl.psi.mro.PerlMro
import com.perl5.lang.perl.util.PerlPackageUtil


class PerlCallObjectValueBackendHelper : PerlCallValueBackendHelper<PerlCallObjectValue>() {
  override val serializationId: Int
    get() = PerlValueBackendHelper.CALL_OBJECT_ID

  override fun serializeData(value: PerlCallObjectValue, serializer: PerlValueSerializer) {
    super.serializeData(value, serializer)
    serializer.writeName(value.superContext)
  }

  override fun deserialize(
    deserializer: PerlValueDeserializer,
    baseValue: PerlValue,
    parameter: PerlValue,
    arguments: List<PerlValue>
  ): PerlValue = PerlCallObjectValue.create(baseValue, parameter, arguments, deserializer.readNameString())

  override fun computeNamespaceNames(resolvedNamespaceValue: PerlValue): MutableSet<String> =
    if (resolvedNamespaceValue.isUnknown) mutableSetOf(PerlPackageUtil.UNIVERSAL_NAMESPACE)
    else super.computeNamespaceNames(resolvedNamespaceValue)

  override fun processCallTargets(
    callValue: PerlCallObjectValue,
    project: Project,
    searchScope: GlobalSearchScope,
    contextElement: PsiElement?,
    namespaceNames: MutableSet<String>,
    subNames: MutableSet<String>,
    processor: Processor<in PsiNamedElement?>
  ): Boolean {
    for (contextNamespace in namespaceNames) {
      for (currentNamespaceName in PerlMro.getLinearISA(
        project,
        searchScope,
        getEffectiveNamespaceName(callValue, contextNamespace),
        callValue.isSuper
      )) {
        val processingContext = ProcessingContext()
        processingContext.processBuiltIns = false
        if (!processItemsInNamespace(project, searchScope, subNames, processor, currentNamespaceName, processingContext, contextElement)) {
          return false
        }
        if (!processingContext.processAutoload) { // marker that we've got at least one result
          break
        }
      }
    }
    return true
  }

  override fun processTargetNamespaceElements(
    callValue: PerlCallObjectValue,
    contextElement: PsiElement,
    processor: PerlNamespaceItemProcessor<in PsiNamedElement>
  ): Boolean {
    val project: Project = contextElement.project
    val searchScope: GlobalSearchScope = contextElement.resolveScope
    for (contextNamespace in callValue.namespaceNameValue.resolve(contextElement).namespaceNames) {
      for (currentNamespaceName in PerlMro.getLinearISA(
        project, searchScope, getEffectiveNamespaceName(callValue, contextNamespace), callValue.isSuper
      )) {
        if (!processTargetNamespaceElements(project, searchScope, processor, currentNamespaceName, contextElement)) {
          return false
        }
      }
    }
    return true
  }

  private fun getEffectiveNamespaceName(callValue: PerlCallObjectValue, contextNamespace: String): String =
    callValue.superContext ?: contextNamespace
}