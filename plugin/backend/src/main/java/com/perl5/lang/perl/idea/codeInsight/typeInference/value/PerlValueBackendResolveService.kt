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

package com.perl5.lang.perl.idea.codeInsight.typeInference.value

import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.util.RecursionManager
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiNamedElement
import com.intellij.util.Processor
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.serialization.PerlCallValueBackendHelper
import com.perl5.lang.perl.psi.PerlCallableElement
import com.perl5.lang.perl.psi.PerlSubElement
import com.perl5.lang.perl.util.PerlMroUtil
import com.perl5.lang.perl.util.PerlPackageUtil
import java.util.*
import java.util.Set
import java.util.function.Consumer
import java.util.stream.Collectors


class PerlValueBackendResolveService : PerlValueResolveService {
  override fun computeResolve(
    callValue: PerlCallValue,
    resolvedNamespaceValue: PerlValue,
    resolvedSubNameValue: PerlValue,
    resolver: PerlValueResolver
  ): PerlValue {
    val subNames = resolvedSubNameValue.subNames
    if (subNames.isEmpty()) {
      return UNKNOWN_VALUE
    }

    val namespaceNames = PerlCallValueBackendHelper[callValue].computeNamespaceNames(resolvedNamespaceValue)
    if (namespaceNames.isEmpty()) {
      return UNKNOWN_VALUE
    }

    val resolvedArguments: MutableList<PerlValue> = callValue.computeResolvedArguments(resolvedNamespaceValue, resolver)
    val argumentsValue = PerlArrayValue.builder().addElements(resolvedArguments).build()

    val builder = PerlOneOfValue.builder()
    val hasTargets = booleanArrayOf(false)
    RecursionManager.doPreventingRecursion(
      com.intellij.openapi.util.Pair.create(resolver.resolveScope, callValue), true
    ) {
      PerlCallValueBackendHelper[callValue]
        .processCallTargets(
          callValue, resolver.project, resolver.resolveScope, resolver.contextFile, namespaceNames, subNames,
          Processor { it: PsiNamedElement? ->
            hasTargets[0] = true
            if (it is PerlSubElement) {
              builder.addVariant(PerlSubValueResolver(it, argumentsValue).resolve(it.getReturnValue()))
            }
            true
          })
      null
    }

    PerlCallValueBackendHelper[callValue].addFallbackTargets(
      callValue, namespaceNames, subNames, resolvedArguments, hasTargets[0], builder, resolvedNamespaceValue, resolver
    )

    return builder.build()
  }

  override fun computeResolve(duckValue: PerlDuckValue, resolver: PerlValueResolver, resolvedElements: List<PerlValue>): PerlValue {
    if (duckValue.isEmpty || !PerlValue.isDuckTypingEnabled()) {
      return UNKNOWN_VALUE
    }
    val usedCallableNames = resolvedElements.stream()
      .flatMap { it.subNames.stream() }
      .distinct()
      .sorted()
      .collect(Collectors.toList())

    if (usedCallableNames.isEmpty()) {
      return UNKNOWN_VALUE
    }

    val baseSubName = usedCallableNames.removeFirst()
    val baseNamespaces = HashSet<String?>()
    PerlPackageUtil.processCallablesNamespaceNames(resolver, baseSubName, Processor { it: PerlCallableElement? ->
      baseNamespaces.add(it!!.getNamespaceName())
      true
    })

    if (baseNamespaces.isEmpty()) {
      return UNKNOWN_VALUE
    }

    val valueBuilder = PerlOneOfValue.builder()
    val namespaceNameProcessor: Consumer<String?> = Consumer { it -> valueBuilder.addVariant(PerlScalarValue.create(it)) }
    if (usedCallableNames.isEmpty()) {
      baseNamespaces.forEach(namespaceNameProcessor)
    }
    else {
      baseNamespaces.forEach { namespaceName ->
        processNamespacesWithAllCallables(
          namespaceName, Set.copyOf(usedCallableNames), resolver, HashSet<String>(), namespaceNameProcessor
        )
      }
    }
    return valueBuilder.build()
  }

  private fun processNamespacesWithAllCallables(
    namespaceName: String?,
    callableNames: MutableSet<String>,
    resolver: PerlValueResolver,
    recursionControlSet: MutableSet<String>,
    namespaceNameConsumer: Consumer<in String?>
  ) {
    if (namespaceName == null || StringUtil.isEmpty(namespaceName) || !recursionControlSet.add(namespaceName)) {
      return
    }

    ProgressManager.checkCanceled()
    val callablesLeft = HashSet(callableNames)
    val project = resolver.project
    val resolveScope = resolver.resolveScope
    val notFinished =
      PerlMroUtil.processCallables(project, resolveScope, namespaceName, callableNames, false, Processor { it: PerlCallableElement? ->
        callablesLeft.remove(it!!.getCallableName())
        !callablesLeft.isEmpty()
      }, false)

    if (!notFinished) {
      namespaceNameConsumer.accept(namespaceName)
      return
    }

    val callablesToFind = Collections.unmodifiableSet(callablesLeft)
    for (childNamespace in PerlPackageUtil.getChildNamespaces(project, namespaceName, resolveScope)) {
      processNamespacesWithAllCallables(
        childNamespace.getNamespaceName()!!, callablesToFind, resolver, recursionControlSet, namespaceNameConsumer
      )
    }
  }

}