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
package com.perl5.lang.embedded.psi

import com.intellij.psi.templateLanguages.TemplateDataElementType
import com.intellij.psi.tree.IElementType
import com.perl5.lang.embedded.EmbeddedPerlLanguage
import com.perl5.lang.perl.psi.stubs.PerlFileElementType
import com.perl5.lang.pod.elementTypes.PodTemplatingElementType

object EmbeddedPerlElementTypes {
  @JvmField
  val FILE: PerlFileElementType = PerlFileElementType("Embedded Perl5", EmbeddedPerlLanguage.INSTANCE)

  @JvmField
  val EMBED_TEMPLATE_BLOCK_HTML: IElementType = EmbeddedPerlTemplateTokenType()

  @JvmField
  val EMBED_OUTER_ELEMENT_TYPE: IElementType = EmbeddedPerlTokenType("EMBED_OUTER_ELEMENT_TYPE")

  @JvmField
  val EMBED_HTML_TEMPLATE_DATA: IElementType = TemplateDataElementType(
    "EMBED_HTML_TEMPLATE_DATA", EmbeddedPerlLanguage.INSTANCE, EMBED_TEMPLATE_BLOCK_HTML,
    EMBED_OUTER_ELEMENT_TYPE
  )

  @JvmField
  val EMBED_POD_TEMPLATE_DATA: IElementType = PodTemplatingElementType("EMBED_POD_TEMPLATE_DATA", EmbeddedPerlLanguage.INSTANCE)

  @JvmField
  val EMBED_MARKER_OPEN: IElementType = EmbeddedPerlTokenType("EMBED_MARKER_OPEN")

  @JvmField
  val EMBED_MARKER_CLOSE: IElementType = EmbeddedPerlTokenType("EMBED_MARKER_CLOSE")
}
