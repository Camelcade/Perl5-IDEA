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
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.*
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings
import com.perl5.lang.perl.psi.PerlCallableElement
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsService
import com.perl5.lang.perl.util.PerlNamespaceUtil
import com.perl5.lang.perl.util.PerlPackageUtil
import com.perl5.lang.perl.util.PerlPackageUtilCore
import com.perl5.lang.perl.util.PerlSubUtilCore.SUB_AUTOLOAD


abstract class PerlCallValueBackendHelper<Val : PerlCallValue> : PerlParametrizedOperationValueBackendHelper<Val>() {
  override fun serializeData(value: Val, serializer: PerlValueSerializer) {
    super.serializeData(value, serializer)
    serializer.writeValuesList(value.arguments)
  }

  final override fun deserialize(deserializer: PerlValueDeserializer, baseValue: PerlValue, parameter: PerlValue): PerlValue =
    deserialize(deserializer, baseValue, parameter, deserializer.readValuesList())

  protected abstract fun deserialize(
    deserializer: PerlValueDeserializer,
    baseValue: PerlValue,
    parameter: PerlValue,
    arguments: List<PerlValue>
  ): PerlValue

  /**
   * Processes all possible call targets: subs declarations, definitions and typeglobs with `processor`
   * @param contextElement invocation point. Context element, necessary to compute additional imports
   */
  @Suppress("UnusedReturnValue")
  fun processCallTargets(
    callValue: Val,
    contextElement: PsiElement,
    processor: Processor<in PsiNamedElement?>
  ): Boolean {
    val project: Project = contextElement.project
    val searchScope = contextElement.resolveScope
    val subNames: MutableSet<String> = callValue.subNameValue.resolve(contextElement).subNames
    val namespaceNames: MutableSet<String> = computeNamespaceNames(callValue.namespaceNameValue.resolve(contextElement))
    return !subNames.isEmpty() && !namespaceNames.isEmpty() &&
      processCallTargets(callValue, project, searchScope, contextElement, namespaceNames, subNames, processor)
  }

  protected fun processItemsInNamespace(
    project: Project,
    searchScope: GlobalSearchScope,
    subNames: MutableSet<String>,
    processor: Processor<in PsiNamedElement?>,
    namespaceName: String,
    processingContext: ProcessingContext,
    contextElement: PsiElement?
  ): Boolean {
    val processorWrapper = Processor { it: PsiNamedElement? ->
      processingContext.processBuiltIns = false
      processingContext.processAutoload = false
      processor.process(it)
    }

    val subsEffectiveScope: GlobalSearchScope = getEffectiveScope(project, searchScope, namespaceName, contextElement)
    for (subName in subNames) {
      if (!PerlPackageUtil.processCallables(
          project,
          subsEffectiveScope,
          PerlPackageUtilCore.join(namespaceName, subName),
          processorWrapper
        )
      ) {
        return false
      }
    }

    // exports
    val exportDescriptors = PerlNamespaceUtil.getExportDescriptors(project, searchScope, namespaceName)
    if (!processExportDescriptorsItems(project, searchScope, subNames, processorWrapper, exportDescriptors)) {
      return false
    }

    // built-ins
    if (processingContext.processBuiltIns) {
      processingContext.processBuiltIns = false
      for (subName in subNames) {
        val coreSub = PerlImplicitDeclarationsService.getInstance(project).getCoreSub(subName)
        if (coreSub != null && !processorWrapper.process(coreSub)) {
          return false
        }
      }
    }

    // AUTOLOAD
    return !processingContext.processAutoload ||
      PerlPackageUtilCore.isUNIVERSAL(namespaceName) || PerlPackageUtilCore.isCORE(namespaceName) ||
      PerlPackageUtil.processCallables(project, searchScope, PerlPackageUtilCore.join(namespaceName, SUB_AUTOLOAD), processorWrapper)
  }

  /**
   * Adjust search scope if necessary. Used to handle simple main resolution
   *
   * @return original or adjusted scope to search
   */
  private fun getEffectiveScope(
    project: Project,
    originalScope: GlobalSearchScope,
    namespaceName: String,
    contextElement: PsiElement?
  ): GlobalSearchScope {
    val contextFile = contextElement?.containingFile?.originalFile
    if (PerlPackageUtilCore.MAIN_NAMESPACE_NAME == namespaceName &&
      PerlSharedSettings.getInstance(project).SIMPLE_MAIN_RESOLUTION && contextFile != null
    ) {
      return GlobalSearchScope.fileScope(contextFile)
    }
    return originalScope
  }

  protected fun processExportDescriptorsItems(
    project: Project,
    searchScope: GlobalSearchScope,
    subNames: Set<String>,
    processorWrapper: Processor<in PsiNamedElement?>,
    exportDescriptors: Set<PerlExportDescriptor>
  ): Boolean {
    for (exportDescriptor in exportDescriptors) {
      if (subNames.contains(exportDescriptor.importedName) &&
        !PerlPackageUtil.processCallables(project, searchScope, exportDescriptor.targetCanonicalName, processorWrapper)
      ) {
        return false
      }
    }
    return true
  }


  /**
   * @return set of a namepsaces names that should be used for the `resolvedNamespaceValue`
   */
  open fun computeNamespaceNames(resolvedNamespaceValue: PerlValue): MutableSet<String> = resolvedNamespaceValue.namespaceNames

  /**
   * Processes all possible call targets: subs declarations, definitions and typeglobs with `processor` for
   * the `namespaceName`
   */
  abstract fun processCallTargets(
    callValue: Val,
    project: Project,
    searchScope: GlobalSearchScope,
    contextElement: PsiElement?,
    namespaceNames: MutableSet<String>,
    subNames: MutableSet<String>,
    processor: Processor<in PsiNamedElement?>
  ): Boolean

  /**
   * Processes all elements in all namespaces targeted by current call qualifying namespace. E.g. for `Foo::Bar->foo` processes
   * all elements from `Foo::Bar`
   * @param contextElement origin element of a call. Used to process implicit import into the file
   */
  abstract fun processTargetNamespaceElements(
    callValue: Val,
    contextElement: PsiElement,
    processor: PerlNamespaceItemProcessor<in PsiNamedElement>
  ): Boolean

  protected class ProcessingContext {
    var processAutoload: Boolean = true
    var processBuiltIns: Boolean = true
  }

  protected fun processExportDescriptors(
    project: Project,
    searchScope: GlobalSearchScope,
    processor: PerlNamespaceItemProcessor<in PsiNamedElement>,
    exportDescriptors: MutableSet<out PerlExportDescriptor>
  ): Boolean {
    if (exportDescriptors.isEmpty()) {
      return true
    }
    val foundOne = booleanArrayOf(false)
    for (exportDescriptor in exportDescriptors) {
      foundOne[0] = false
      if (!PerlPackageUtil.processCallables(
          project,
          searchScope,
          exportDescriptor.targetCanonicalName,
          Processor { it: PerlCallableElement? ->
            foundOne[0] = true
            processor.processImportedItem(it!!, exportDescriptor)
          })
      ) {
        return false
      }

      if (!foundOne[0]) {
        if (!processor.processOrphanDescriptor(exportDescriptor)) {
          return false
        }
      }
    }
    return true
  }

  protected fun processTargetNamespaceElements(
    project: Project,
    searchScope: GlobalSearchScope,
    processor: PerlNamespaceItemProcessor<in PsiNamedElement>,
    currentNamespaceName: String,
    contextElement: PsiElement
  ): Boolean {
    val effectiveScope = getEffectiveScope(project, searchScope, currentNamespaceName, contextElement)

    if (!PerlPackageUtil.processCallablesInNamespace(
        project,
        effectiveScope,
        currentNamespaceName,
        Processor { t: PerlCallableElement? -> processor.processItem(t!!) })
    ) {
      return false
    }

    // exports
    val exportDescriptors =
      PerlNamespaceUtil.getExportDescriptors(project, effectiveScope, currentNamespaceName)
    return processExportDescriptors(project, effectiveScope, processor, exportDescriptors)
  }

  /**
   * Computes a fallback value. This method should handle two cases:
   * - invisible/complex constructor, where we can't compute a proper return value
   * - incorrectly lexed namespace FQNs, where `Foo::Bar` was lexed and parsed as `Foo::Bar()`
   *
   * @param hasTarget true iff we has processed a real target of this call
   */
  abstract fun addFallbackTargets(
    callValue: Val,
    namespaceNames: MutableSet<String>,
    subNames: MutableSet<String>,
    resolvedArguments: MutableList<PerlValue>,
    hasTarget: Boolean,
    builder: PerlOneOfValue.Builder,
    resolvedNamespaceValue: PerlValue,
    resolver: PerlValueResolver
  )

  companion object {
    @JvmStatic
    operator fun get(callValue: PerlCallValue): PerlCallValueBackendHelper<PerlCallValue> =
      PerlValueBackendHelper[callValue] as PerlCallValueBackendHelper<PerlCallValue>
  }
}