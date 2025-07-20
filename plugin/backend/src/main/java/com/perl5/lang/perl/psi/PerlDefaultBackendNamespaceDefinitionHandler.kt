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

import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.search.GlobalSearchScope
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor
import com.perl5.lang.perl.util.PerlNamespaceUtil
import com.perl5.lang.perl.util.PerlPackageUtil
import com.perl5.lang.perl.util.processors.PerlNamespaceEntityProcessor


open class PerlDefaultBackendNamespaceDefinitionHandler<T : PerlNamespaceDefinitionElement> : PerlNamespaceDefinitionHandler<T> {
  override fun processExportDescriptors(
    namespace: T,
    processor: PerlNamespaceEntityProcessor<in PerlExportDescriptor>
  ): Boolean {
    val namespaceName: String = namespace.getNamespaceName() ?: return true
    if (StringUtil.isEmpty(namespaceName)) {
      return true
    }
    return PerlNamespaceUtil.processExportDescriptors(
      namespace.getProject(), GlobalSearchScope.fileScope(namespace.containingFile.originalFile), namespaceName, processor
    )
  }

  override fun getParentNamespaceDefinitions(namespace: T): List<PerlNamespaceDefinitionElement> =
    PerlNamespaceUtil.collectNamespaceDefinitions(namespace.getProject(), namespace.getParentNamespacesNames())

  override fun getChildNamespaceDefinitions(namespace: T): List<PerlNamespaceDefinitionElement> =
    PerlPackageUtil.getChildNamespaces(namespace.getProject(), namespace.getNamespaceName())
}