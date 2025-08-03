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

package com.perl5.lang.perl.psi

import com.intellij.openapi.util.ClassExtension
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor
import com.perl5.lang.perl.util.processors.PerlNamespaceEntityProcessor


private val EP = ClassExtension<PerlNamespaceDefinitionHandler<*>>("com.perl5.namespaceDefinitionHalder")

/**
 * Provides additional functionality for the namespacees.
 */
interface PerlNamespaceDefinitionHandler<Ns : PerlNamespaceDefinitionElement> {

  @Suppress("UnusedReturnValue")
  fun processExportDescriptors(namespace: Ns, processor: PerlNamespaceEntityProcessor<in PerlExportDescriptor>): Boolean

  fun getParentNamespaceDefinitions(namespace: Ns): List<PerlNamespaceDefinitionElement>

  fun getChildNamespaceDefinitions(namespace: Ns): List<PerlNamespaceDefinitionElement>

  fun collectLinearISA(namespace: Ns, recursionMap: MutableSet<String>, result: MutableList<String>): Unit = namespace.mro.getLinearISA(
    namespace.getProject(), getParentNamespaceDefinitions(namespace), recursionMap, result
  )

  companion object {
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T : PerlNamespaceDefinitionElement> instance(ns: T): PerlNamespaceDefinitionHandler<T> =
      instance(ns.javaClass) as PerlNamespaceDefinitionHandler<T>

    @JvmStatic
    fun instance(cls: Class<*>): PerlNamespaceDefinitionHandler<*> = EP.findSingle(cls) as PerlNamespaceDefinitionHandler<*>
    inline fun <reified T : PerlNamespaceElement> instance(): T = instance(T::class.java) as T
  }
}