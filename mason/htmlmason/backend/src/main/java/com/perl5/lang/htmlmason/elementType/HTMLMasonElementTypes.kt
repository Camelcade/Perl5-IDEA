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
package com.perl5.lang.htmlmason.elementType

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.templateLanguages.TemplateDataElementType
import com.intellij.psi.tree.IElementType
import com.perl5.lang.htmlmason.HTMLMasonLanguage
import com.perl5.lang.htmlmason.HTMLMasonSyntaxElements
import com.perl5.lang.htmlmason.parser.psi.impl.*
import com.perl5.lang.perl.psi.stubs.PerlFileElementType
import com.perl5.lang.pod.elementTypes.PodTemplatingElementType

object HTMLMasonElementTypes {
  @JvmField
  val HTML_MASON_TEMPLATE_BLOCK_HTML: IElementType = HTMLMasonTemplateTokenType()

  @JvmField
  val HTML_MASON_OUTER_ELEMENT_TYPE: IElementType = HTMLMasonTokenType("HTML_MASON_OUTER_ELEMENT_TYPE")

  @JvmField
  val HTML_MASON_HTML_TEMPLATE_DATA: IElementType = TemplateDataElementType(
    "HTML_MASON_HTML_TEMPLATE_DATA",
    HTMLMasonLanguage.INSTANCE,
    HTML_MASON_TEMPLATE_BLOCK_HTML,
    HTML_MASON_OUTER_ELEMENT_TYPE
  )

  @JvmField
  val HTML_MASON_POD_TEMPLATE_DATA: IElementType = PodTemplatingElementType("HTML_MASON_POD_TEMPLATE_DATA", HTMLMasonLanguage.INSTANCE)

  @JvmField
  val HTML_MASON_BLOCK_OPENER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_BLOCK_OPENER)

  @JvmField
  val HTML_MASON_BLOCK_CLOSER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_BLOCK_CLOSER)

  @JvmField
  val HTML_MASON_LINE_OPENER: IElementType = HTMLMasonTokenType("%")

  @JvmField
  val HTML_MASON_EXPR_FILTER_PIPE: IElementType = HTMLMasonTokenType("|")

  @JvmField
  val HTML_MASON_TAG_CLOSER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_TAG_CLOSER)

  @JvmField
  val HTML_MASON_ESCAPER_NAME: IElementType = HTMLMasonTokenType("HTML_MASON_ESCAPER")

  @JvmField
  val HTML_MASON_DEFAULT_ESCAPER_NAME: IElementType = HTMLMasonTokenType("HTML_MASON_ESCAPER")

  @JvmField
  val HTML_MASON_CALL_OPENER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_CALL_OPENER)

  @JvmField
  val HTML_MASON_CALL_FILTERING_OPENER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_CALL_OPENER_FILTER)

  @JvmField
  val HTML_MASON_CALL_CLOSER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_CALL_CLOSER)

  @JvmField
  val HTML_MASON_CALL_CLOSER_UNMATCHED: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_CALL_CLOSER)

  @JvmField
  val HTML_MASON_CALL_CLOSE_TAG_START: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_CALL_CLOSE_TAG_START)

  @JvmField
  val HTML_MASON_CALL_CLOSE_TAG: IElementType =
    HTMLMasonElementType(HTMLMasonSyntaxElements.KEYWORD_CALL_CLOSE_TAG_START + HTML_MASON_TAG_CLOSER)

  @JvmField
  val HTML_MASON_METHOD_OPENER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_METHOD_OPENER)

  @JvmField
  val HTML_MASON_METHOD_CLOSER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_METHOD_CLOSER)

  @JvmField
  val HTML_MASON_DEF_OPENER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_DEF_OPENER)

  @JvmField
  val HTML_MASON_DEF_CLOSER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_DEF_CLOSER)

  @JvmField
  val HTML_MASON_DOC_OPENER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_DOC_OPENER)

  @JvmField
  val HTML_MASON_DOC_CLOSER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_DOC_CLOSER)

  @JvmField
  val HTML_MASON_FLAGS_OPENER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_FLAGS_OPENER)

  @JvmField
  val HTML_MASON_FLAGS_CLOSER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_FLAGS_CLOSER)

  @JvmField
  val HTML_MASON_ATTR_OPENER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_ATTR_OPENER)

  @JvmField
  val HTML_MASON_ATTR_CLOSER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_ATTR_CLOSER)

  @JvmField
  val HTML_MASON_ARGS_OPENER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_ARGS_OPENER)

  @JvmField
  val HTML_MASON_ARGS_CLOSER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_ARGS_CLOSER)

  @JvmField
  val HTML_MASON_INIT_OPENER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_INIT_OPENER)

  @JvmField
  val HTML_MASON_INIT_CLOSER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_INIT_CLOSER)

  @JvmField
  val HTML_MASON_ONCE_OPENER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_ONCE_OPENER)

  @JvmField
  val HTML_MASON_ONCE_CLOSER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_ONCE_CLOSER)

  @JvmField
  val HTML_MASON_SHARED_OPENER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_SHARED_OPENER)

  @JvmField
  val HTML_MASON_SHARED_CLOSER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_SHARED_CLOSER)

  @JvmField
  val HTML_MASON_CLEANUP_OPENER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_CLEANUP_OPENER)

  @JvmField
  val HTML_MASON_CLEANUP_CLOSER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_CLEANUP_CLOSER)

  @JvmField
  val HTML_MASON_PERL_OPENER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_PERL_OPENER)

  @JvmField
  val HTML_MASON_PERL_CLOSER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_PERL_CLOSER)

  @JvmField
  val HTML_MASON_TEXT_OPENER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_TEXT_OPENER)

  @JvmField
  val HTML_MASON_TEXT_CLOSER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_TEXT_CLOSER)

  @JvmField
  val HTML_MASON_FILTER_OPENER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_FILTER_OPENER)

  @JvmField
  val HTML_MASON_FILTER_CLOSER: IElementType = HTMLMasonTokenType(HTMLMasonSyntaxElements.KEYWORD_FILTER_CLOSER)

  @JvmField
  val HTML_MASON_CALL_STATEMENT: IElementType = HTMLMasonElementType("HTML_MASON_CALL_STATEMENT")

  @JvmField
  val HTML_MASON_TEXT_BLOCK: IElementType = HTMLMasonElementType("HTML_MASON_TEXT_BLOCK")

  @JvmField
  val HTML_MASON_ATTR_BLOCK: IElementType = HTMLMasonElementType("HTML_MASON_ATTR_BLOCK")

  @JvmField
  val HTML_MASON_HARD_NEWLINE: IElementType = HTMLMasonElementType("HTML_MASON_HARD_NEWLINE")

  @JvmField
  val HTML_MASON_METHOD_DEFINITION: HTMLMasonMethodElementType = HTMLMasonMethodElementType("HTML_MASON_METHOD_DEFINITION")

  @JvmField
  val HTML_MASON_SUBCOMPONENT_DEFINITION: HTMLMasonSubcomponentElementType = HTMLMasonSubcomponentElementType("HTML_MASON_DEF_DEFINITION")

  @JvmField
  val HTML_MASON_FLAGS_STATEMENT: HTMLMasonFlagsStatementElementType = HTMLMasonFlagsStatementElementType("HTML_MASON_FLAGS_STATEMENT")

  @JvmField
  val HTML_MASON_ARGS_BLOCK: HTMLMasonArgsBlockElementType = HTMLMasonArgsBlockElementType("HTML_MASON_ARGS_BLOCK")

  @JvmField
  val HTML_MASON_ONCE_BLOCK: IElementType = object : HTMLMasonElementType("HTML_MASON_ONCE_BLOCK") {
    override fun getPsiElement(node: ASTNode): PsiElement = HTMLMasonOnceBlockImpl(node)
  }

  @JvmField
  val HTML_MASON_INIT_BLOCK: IElementType = object : HTMLMasonElementType("HTML_MASON_INIT_BLOCK") {
    override fun getPsiElement(node: ASTNode): PsiElement = HTMLMasonInitBlockImpl(node)
  }

  @JvmField
  val HTML_MASON_CLEANUP_BLOCK: IElementType = object : HTMLMasonElementType("HTML_MASON_CLEANUP_BLOCK") {
    override fun getPsiElement(node: ASTNode): PsiElement = HTMLMasonCleanupBlockImpl(node)
  }

  @JvmField
  val HTML_MASON_SHARED_BLOCK: IElementType = object : HTMLMasonElementType("HTML_MASON_SHARED_BLOCK") {
    override fun getPsiElement(node: ASTNode): PsiElement = HTMLMasonSharedBlockImpl(node)
  }

  @JvmField
  val HTML_MASON_FILTERED_BLOCK: IElementType = object : HTMLMasonElementType("HTML_MASON_FILTERED_BLOCK") {
    override fun getPsiElement(node: ASTNode): PsiElement = HTMLMasonCompositeElementImpl(node)
  }

  @JvmField
  val HTML_MASON_FILTER_BLOCK: IElementType = object : HTMLMasonElementType("HTML_MASON_FILTER_BLOCK") {
    override fun getPsiElement(node: ASTNode): PsiElement = HTMLMasonFilterBlockImpl(node)
  }

  @JvmField
  val HTML_MASON_BLOCK: IElementType = object : HTMLMasonElementType("HTML_MASON_BLOCK") {
    override fun getPsiElement(node: ASTNode): PsiElement = HTMLMasonBlockImpl(node)
  }

  @JvmField
  val FILE: PerlFileElementType = PerlFileElementType("HTML::Mason component", HTMLMasonLanguage.INSTANCE)
}
