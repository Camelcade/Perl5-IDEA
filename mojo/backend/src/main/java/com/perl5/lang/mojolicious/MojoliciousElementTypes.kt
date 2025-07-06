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
package com.perl5.lang.mojolicious

import com.intellij.psi.templateLanguages.TemplateDataElementType
import com.intellij.psi.tree.IElementType
import com.perl5.lang.perl.psi.stubs.PerlFileElementType
import com.perl5.lang.pod.elementTypes.PodTemplatingElementType

interface MojoliciousElementTypes : MojoliciousSyntaxElements {
  companion object {
    @JvmField
    val MOJO_TEMPLATE_BLOCK_HTML: IElementType = MojoTemplateTokenType()

    @JvmField
    val MOJO_OUTER_ELEMENT_TYPE: IElementType = MojoliciousTokenType("MOJO_OUTER_ELEMENT_TYPE")

    @JvmField
    val MOJO_HTML_TEMPLATE_DATA: IElementType =
      TemplateDataElementType("MOJO_HTML_TEMPLATE_DATA", MojoliciousLanguage.INSTANCE, MOJO_TEMPLATE_BLOCK_HTML, MOJO_OUTER_ELEMENT_TYPE)

    @JvmField
    val MOJO_POD_TEMPLATE_DATA: IElementType = PodTemplatingElementType("MOJO_POD_TEMPLATE_DATA", MojoliciousLanguage.INSTANCE)

    @JvmField
    val MOJO_BLOCK_OPENER: IElementType = MojoliciousTokenType(MojoliciousSyntaxElements.KEYWORD_MOJO_BLOCK_OPENER)

    @JvmField
    val MOJO_BLOCK_EXPR_OPENER: IElementType = MojoliciousTokenType(MojoliciousSyntaxElements.KEYWORD_MOJO_BLOCK_EXPR_OPENER)

    @JvmField
    val MOJO_BLOCK_EXPR_ESCAPED_OPENER: IElementType =
      MojoliciousTokenType(MojoliciousSyntaxElements.KEYWORD_MOJO_BLOCK_EXPR_ESCAPED_OPENER)

    @JvmField
    val MOJO_BLOCK_CLOSER: IElementType = MojoliciousTokenType(MojoliciousSyntaxElements.KEYWORD_MOJO_BLOCK_CLOSER)

    @JvmField
    val MOJO_BLOCK_NOSPACE_CLOSER: IElementType = MojoliciousTokenType(MojoliciousSyntaxElements.KEYWORD_MOJO_BLOCK_EXPR_NOSPACE_CLOSER)

    @JvmField
    val MOJO_BLOCK_CLOSER_SEMI: IElementType = MojoliciousTokenType(";" + MojoliciousSyntaxElements.KEYWORD_MOJO_BLOCK_CLOSER)

    @JvmField
    val MOJO_BLOCK_EXPR_NOSPACE_CLOSER: IElementType =
      MojoliciousTokenType(MojoliciousSyntaxElements.KEYWORD_MOJO_BLOCK_EXPR_NOSPACE_CLOSER)

    @JvmField
    val MOJO_BLOCK_EXPR_CLOSER: IElementType = MojoliciousTokenType(MojoliciousSyntaxElements.KEYWORD_MOJO_BLOCK_CLOSER)

    @JvmField
    val MOJO_LINE_OPENER: IElementType = MojoliciousTokenType(MojoliciousSyntaxElements.KEYWORD_MOJO_LINE_OPENER)

    @JvmField
    val MOJO_LINE_EXPR_OPENER: IElementType = MojoliciousTokenType(MojoliciousSyntaxElements.KEYWORD_MOJO_LINE_EXPR_OPENER)

    @JvmField
    val MOJO_LINE_EXPR_ESCAPED_OPENER: IElementType = MojoliciousTokenType(MojoliciousSyntaxElements.KEYWORD_MOJO_LINE_EXPR_ESCAPED_OPENER)

    @JvmField
    val MOJO_BLOCK_OPENER_TAG: IElementType = MojoliciousTokenType(MojoliciousSyntaxElements.KEYWORD_MOJO_BLOCK_OPENER_TAG)

    @JvmField
    val MOJO_LINE_OPENER_TAG: IElementType = MojoliciousTokenType(MojoliciousSyntaxElements.KEYWORD_MOJO_LINE_OPENER_TAG)

    @JvmField
    val MOJO_BEGIN: IElementType = MojoliciousTokenType(MojoliciousSyntaxElements.KEYWORD_MOJO_BEGIN)

    @JvmField
    val MOJO_END: IElementType = MojoliciousTokenType(MojoliciousSyntaxElements.KEYWORD_MOJO_END)

    @JvmField
    val FILE: PerlFileElementType = PerlFileElementType("Mojolicious Perl5 Template", MojoliciousLanguage.INSTANCE)
  }
}
