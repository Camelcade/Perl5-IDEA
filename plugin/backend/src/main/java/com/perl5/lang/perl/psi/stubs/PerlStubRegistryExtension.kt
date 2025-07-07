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

package com.perl5.lang.perl.psi.stubs

import com.intellij.psi.stubs.StubRegistry
import com.intellij.psi.stubs.StubRegistryExtension
import com.perl5.lang.perl.lexer.PerlElementTypes
import com.perl5.lang.perl.parser.Class.Accessor.ClassAccessorElementTypes
import com.perl5.lang.perl.parser.moose.MooseElementTypes
import com.perl5.lang.perl.parser.moose.stubs.PerlMooseOverrideStubSerializingFactory
import com.perl5.lang.perl.psi.stubs.subsdeclarations.PerlSubDeclarationStubSerializingFactory
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlFuncDefinitionStubSerializingFactory
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlLightSubDefinitionStubSerializingFactory
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlMethodDefinitionStubSerializingFactory
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStubSerializingFactory
import com.perl5.lang.pod.lexer.PodElementTypes
import com.perl5.lang.pod.parser.psi.stubs.PodFileStubSerializer

class PerlStubRegistryExtension : StubRegistryExtension {
  override fun register(registry: StubRegistry) {
    PerlStubElementTypes.FILE.let { registry.registerStubSerializer(it, PerlFileStubserializer(it)) }
    PodElementTypes.FILE.let { registry.registerStubSerializer(it, PodFileStubSerializer()) }

    listOf(
      PerlElementTypes.SUB_DECLARATION to ::PerlSubDeclarationStubSerializingFactory,
      PerlStubElementTypes.SUB_DEFINITION to ::PerlSubDefinitionStubSerializingFactory,
      PerlStubElementTypes.METHOD_DEFINITION to ::PerlMethodDefinitionStubSerializingFactory,
      PerlStubElementTypes.FUNC_DEFINITION to ::PerlFuncDefinitionStubSerializingFactory,

      MooseElementTypes.MOOSE_STATEMENT_OVERRIDE to ::PerlMooseOverrideStubSerializingFactory,
    ).forEach { (elementType, factory) ->
      registry.registerStubSerializingFactory(elementType, factory(elementType))
    }

    listOf(
      ClassAccessorElementTypes.CLASS_ACCESSOR_METHOD,
      PerlStubElementTypes.LIGHT_SUB_DEFINITION,
      PerlStubElementTypes.LIGHT_METHOD_DEFINITION,
      PerlStubElementTypes.LIGHT_ATTRIBUTE_DEFINITION,

      ).forEach {
      registry.registerStubSerializingFactory(it, PerlLightSubDefinitionStubSerializingFactory(it))
    }
  }
}