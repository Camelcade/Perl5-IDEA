/*
 * Copyright 2015-2024 Alexandr Evstigneev
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
package com.perl5.lang.perl.extensions.packageprocessor.impl

import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class Dancer2PackageProcessor : DancerPackageProcessor() {
  private val myExportDescriptors: ImmutableList<PerlExportDescriptor> by lazy {
    PerlDancer2DSL.DSL_KEYWORDS.map { PerlExportDescriptor.create("Dancer2::Core::DSL", it) }.toImmutableList()
  }

  override fun getExportDescriptors(): List<PerlExportDescriptor> = myExportDescriptors
}
