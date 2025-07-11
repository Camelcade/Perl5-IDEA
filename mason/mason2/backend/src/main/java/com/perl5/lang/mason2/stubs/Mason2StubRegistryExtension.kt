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

package com.perl5.lang.mason2.stubs

import com.intellij.psi.stubs.StubRegistry
import com.intellij.psi.stubs.StubRegistryExtension
import com.perl5.lang.mason2.elementType.Mason2ElementTypes
import com.perl5.lang.mason2.elementType.Mason2ElementTypes.Companion.MASON_AUGMENT_MODIFIER
import com.perl5.lang.mason2.elementType.Mason2ElementTypes.Companion.MASON_FILTER_DEFINITION
import com.perl5.lang.mason2.elementType.Mason2ElementTypes.Companion.MASON_METHOD_DEFINITION
import com.perl5.lang.mason2.elementType.Mason2ElementTypes.Companion.MASON_NAMESPACE_DEFINITION
import com.perl5.lang.mason2.elementType.Mason2ElementTypes.Companion.MASON_OVERRIDE_DEFINITION
import com.perl5.lang.mason2.elementType.MasonAugmentMethodModifierStubSerializingFactory
import com.perl5.lang.mason2.elementType.MasonNamespaceStubSerializingFactory
import com.perl5.lang.perl.psi.stubs.PerlFileStubserializer

class Mason2StubRegistryExtension : StubRegistryExtension {
  override fun register(registry: StubRegistry) {
    listOf(Mason2ElementTypes.PP_FILE, Mason2ElementTypes.COMPONENT_FILE)
      .forEach { registry.registerStubSerializer(it, PerlFileStubserializer(it)) }

    listOf(
      MASON_OVERRIDE_DEFINITION to ::MasonOverrideStubSerializingFactory,
      MASON_METHOD_DEFINITION to ::MasonMethodDefinitionStubSerializingFactory,
      MASON_FILTER_DEFINITION to ::MasonFilterDefinitionStubSerializingFactory,
      MASON_NAMESPACE_DEFINITION to ::MasonNamespaceStubSerializingFactory,
      MASON_AUGMENT_MODIFIER to ::MasonAugmentMethodModifierStubSerializingFactory,
    ).forEach { (elementType, factory) -> registry.registerStubSerializingFactory(elementType, factory(elementType)) }
  }
}