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
package com.perl5.lang.mason2.elementType

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.templateLanguages.TemplateDataElementType
import com.intellij.psi.tree.IElementType
import com.perl5.lang.mason2.Mason2Language
import com.perl5.lang.mason2.Mason2SyntaxElements
import com.perl5.lang.mason2.Mason2TemplatingLanguage
import com.perl5.lang.mason2.psi.impl.*
import com.perl5.lang.perl.psi.stubs.PerlFileElementType
import com.perl5.lang.pod.elementTypes.PodTemplatingElementType

interface Mason2ElementTypes : Mason2SyntaxElements {
  companion object {
    @JvmField
    val MASON_TEMPLATE_BLOCK_HTML: IElementType = MasonTemplateTokenType()

    @JvmField
    val MASON_OUTER_ELEMENT_TYPE: IElementType = MasonTemplatingTokenType("MASON_OUTER_ELEMENT_TYPE")

    @JvmField
    val MASON_HTML_TEMPLATE_DATA: IElementType = TemplateDataElementType(
      "MASON_HTML_TEMPLATE_DATA",
      Mason2TemplatingLanguage.INSTANCE,
      MASON_TEMPLATE_BLOCK_HTML,
      MASON_OUTER_ELEMENT_TYPE
    )

    @JvmField
    val MASON_POD_TEMPLATE_DATA: IElementType = PodTemplatingElementType("MASON_POD_TEMPLATE_DATA", Mason2TemplatingLanguage.INSTANCE)

    @JvmField
    val MASON_FILTERED_BLOCK_OPENER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_FILTERED_BLOCK_OPENER)

    @JvmField
    val MASON_FILTERED_BLOCK_CLOSER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_FILTERED_BLOCK_CLOSER)

    @JvmField
    val MASON_METHOD_MODIFIER_NAME: IElementType = object : MasonTemplatingTokenTypeEx("MASON_METHOD_MODIFIER_NAME") {
      override fun createLeafNode(leafText: CharSequence): ASTNode = MasonMethodModifierNameImpl(this, leafText)
    }

    @JvmField
    val MASON_SELF_POINTER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_SELF_POINTER)

    @JvmField
    val MASON_BLOCK_OPENER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_BLOCK_OPENER)

    @JvmField
    val MASON_BLOCK_CLOSER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_BLOCK_CLOSER)

    @JvmField
    val MASON_TAG_CLOSER: IElementType = MasonTemplatingTokenType(">")

    @JvmField
    val MASON_CALL_OPENER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_CALL_OPENER)

    @JvmField
    val MASON_CALL_CLOSER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_CALL_CLOSER)

    @JvmField
    val MASON_LINE_OPENER: IElementType = MasonTemplatingTokenType("%")

    @JvmField
    val MASON_EXPR_FILTER_PIPE: IElementType = MasonTemplatingTokenType("|")

    @JvmField
    val MASON_METHOD_OPENER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_METHOD_OPENER)

    @JvmField
    val MASON_METHOD_CLOSER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_METHOD_CLOSER)

    @JvmField
    val MASON_CLASS_OPENER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_CLASS_OPENER)

    @JvmField
    val MASON_CLASS_CLOSER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_CLASS_CLOSER)

    @JvmField
    val MASON_DOC_OPENER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_DOC_OPENER)

    @JvmField
    val MASON_DOC_CLOSER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_DOC_CLOSER)

    @JvmField
    val MASON_FLAGS_OPENER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_FLAGS_OPENER)

    @JvmField
    val MASON_FLAGS_CLOSER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_FLAGS_CLOSER)

    @JvmField
    val MASON_INIT_OPENER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_INIT_OPENER)

    @JvmField
    val MASON_INIT_CLOSER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_INIT_CLOSER)

    @JvmField
    val MASON_PERL_OPENER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_PERL_OPENER)

    @JvmField
    val MASON_PERL_CLOSER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_PERL_CLOSER)

    @JvmField
    val MASON_TEXT_OPENER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_TEXT_OPENER)

    @JvmField
    val MASON_TEXT_CLOSER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_TEXT_CLOSER)

    @JvmField
    val MASON_FILTER_OPENER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_FILTER_OPENER)

    @JvmField
    val MASON_FILTER_CLOSER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_FILTER_CLOSER)

    @JvmField
    val MASON_AFTER_OPENER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_AFTER_OPENER)

    @JvmField
    val MASON_AFTER_CLOSER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_AFTER_CLOSER)

    @JvmField
    val MASON_AUGMENT_OPENER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_AUGMENT_OPENER)

    @JvmField
    val MASON_AUGMENT_CLOSER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_AUGMENT_CLOSER)

    @JvmField
    val MASON_AROUND_OPENER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_AROUND_OPENER)

    @JvmField
    val MASON_AROUND_CLOSER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_AROUND_CLOSER)

    @JvmField
    val MASON_BEFORE_OPENER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_BEFORE_OPENER)

    @JvmField
    val MASON_BEFORE_CLOSER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_BEFORE_CLOSER)

    @JvmField
    val MASON_OVERRIDE_OPENER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_OVERRIDE_OPENER)

    @JvmField
    val MASON_OVERRIDE_CLOSER: IElementType = MasonTemplatingTokenType(Mason2SyntaxElements.KEYWORD_OVERRIDE_CLOSER)

    @JvmField
    val MASON_NAMESPACE_DEFINITION: IElementType = MasonNamespaceElementType("MASON_PACKAGE")

    @JvmField
    val MASON_AUGMENT_MODIFIER: IElementType = MasonAugmentMethodModifierElementType("MASON_AUGMENT_MODIFIER")

    @JvmField
    val MASON_OVERRIDE_DEFINITION: IElementType = MasonOverrideElementType("MASON_OVERRIDE_DEFINITION")

    @JvmField
    val MASON_METHOD_DEFINITION: IElementType = MasonMethodDefinitionElementType("MASON_METHOD_DEFINITION")

    @JvmField
    val MASON_FILTER_DEFINITION: IElementType = MasonFilterDefinitionElementType("MASON_FILTER_DEFINITION")

    @JvmField
    val MASON_AFTER_MODIFIER: IElementType = MasonMethodModifierElementType("MASON_AFTER_MODIFIER")

    @JvmField
    val MASON_BEFORE_MODIFIER: IElementType = MasonMethodModifierElementType("MASON_BEFORE_MODIFIER")

    @JvmField
    val MASON_FLAGS_STATEMENT: IElementType = object : MasonTemplatingElementType("FLAGS_STATEMENT") {
      override fun getPsiElement(node: ASTNode): PsiElement = MasonFlagsStatementImpl(node)
    }

    @JvmField
    val MASON_CALL_STATEMENT: IElementType = object : MasonTemplatingElementType("MASON_CALL_STATEMENT") {
      override fun getPsiElement(node: ASTNode): PsiElement = MasonCallStatementImpl(node)
    }

    @JvmField
    val MASON_AROUND_MODIFIER: IElementType = object : MasonTemplatingElementType("MASON_AROUND_MODIFIER") {
      override fun getPsiElement(node: ASTNode): PsiElement = MasonAroundMethodModifierImpl(node)
    }

    @JvmField
    val MASON_ABSTRACT_BLOCK: IElementType = object : MasonTemplatingElementType("MASON_ABSTRACT_BLOCK") {
      override fun getPsiElement(node: ASTNode): PsiElement = MasonAbstractBlockImpl(node)
    }

    @JvmField
    val MASON_TEXT_BLOCK: IElementType = object : MasonTemplatingElementType("MASON_TEXT_BLOCK") {
      override fun getPsiElement(node: ASTNode): PsiElement = MasonTextBlockImpl(node)
    }

    @JvmField
    val MASON_FILTERED_BLOCK: IElementType = object : MasonTemplatingElementType("MASON_FILTERED_BLOCK") {
      override fun getPsiElement(node: ASTNode): PsiElement = MasonFilteredBlockImpl(node)
    }

    @JvmField
    val MASON_SIMPLE_DEREF_EXPR: IElementType = object : MasonTemplatingElementType("MASON_DEREF_EXPRESSION") {
      override fun getPsiElement(node: ASTNode): PsiElement = MasonSimpleDerefExpressionImpl(node)
    }

    @JvmField
    val PP_FILE: PerlFileElementType = PerlFileElementType("Mason PP component", Mason2Language.INSTANCE)

    @JvmField
    val COMPONENT_FILE: PerlFileElementType = PerlFileElementType("Mason component", Mason2TemplatingLanguage.INSTANCE)
  }
}
