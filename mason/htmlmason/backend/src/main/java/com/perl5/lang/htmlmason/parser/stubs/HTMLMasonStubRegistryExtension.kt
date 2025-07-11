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

package com.perl5.lang.htmlmason.parser.stubs

import com.intellij.psi.stubs.StubRegistry
import com.intellij.psi.stubs.StubRegistryExtension
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes.HTML_MASON_FLAGS_STATEMENT
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes.HTML_MASON_METHOD_DEFINITION
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes.HTML_MASON_SUBCOMPONENT_DEFINITION
import com.perl5.lang.htmlmason.elementType.HTMLMasonFlagsStatementStubSerializingFactory
import com.perl5.lang.htmlmason.elementType.HTMLMasonMethodStubSerializingFactory
import com.perl5.lang.htmlmason.elementType.HTMLMasonSubcomponentStubSerializingFactory
import com.perl5.lang.perl.psi.stubs.PerlFileStubserializer

class HTMLMasonStubRegistryExtension : StubRegistryExtension {
  override fun register(registry: StubRegistry) {
    HTMLMasonElementTypes.FILE.let { registry.registerStubSerializer(it, PerlFileStubserializer(it)) }
    listOf(
      HTML_MASON_SUBCOMPONENT_DEFINITION to ::HTMLMasonSubcomponentStubSerializingFactory,
      HTML_MASON_METHOD_DEFINITION to ::HTMLMasonMethodStubSerializingFactory,
      HTML_MASON_FLAGS_STATEMENT to ::HTMLMasonFlagsStatementStubSerializingFactory,
    ).forEach { (elementType, factory) -> registry.registerStubSerializingFactory(elementType, factory(elementType)) }
  }
}