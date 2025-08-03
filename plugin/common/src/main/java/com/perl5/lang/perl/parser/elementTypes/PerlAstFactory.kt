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

package com.perl5.lang.perl.parser.elementTypes

import com.intellij.lang.ASTFactory
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.impl.source.tree.PsiCommentImpl
import com.intellij.psi.tree.IElementType
import com.perl5.lang.perl.parser.PerlElementTypesGenerated.*
import com.perl5.lang.perl.parser.moose.MooseElementTypes.*
import com.perl5.lang.perl.parser.moose.psi.impl.PerlMooseKeywordElementImpl
import com.perl5.lang.perl.parser.moose.psi.impl.PerlMooseKeywordSubNameElementImpl
import com.perl5.lang.perl.psi.impl.*

class PerlAstFactory : ASTFactory() {
  private val tokenFactories = mapOf(
    ARRAY_NAME to ::PerlVariableNameElementImpl,
    BUILTIN_ARGUMENTLESS to ::PerlSubNameElementImpl,
    BUILTIN_LIST to ::PerlSubNameElementImpl,
    BUILTIN_UNARY to ::PerlSubNameElementImpl,
    COMMENT_BLOCK to ::PsiCommentImpl,
    COMMENT_LINE to ::PsiCommentImpl,
    CUSTOM_UNARY to ::PerlSubNameElementImpl,
    GLOB_NAME to ::PerlVariableNameElementImpl,
    HASH_NAME to ::PerlVariableNameElementImpl,
    HEREDOC_END to ::PerlHeredocTerminatorElementImpl,
    HEREDOC_END_INDENTABLE to ::PerlHeredocTerminatorElementImpl,
    PACKAGE to ::PerlNamespaceElementImpl,
    POD to ::PerlPodElement,
    QUALIFYING_PACKAGE to ::PerlNamespaceElementImpl,
    RESERVED_AFTER to ::PerlMooseKeywordElementImpl,
    RESERVED_AROUND to ::PerlMooseKeywordElementImpl,
    RESERVED_AUGMENT to ::PerlMooseKeywordElementImpl,
    RESERVED_BEFORE to ::PerlMooseKeywordElementImpl,
    RESERVED_EXTENDS to ::PerlMooseKeywordElementImpl,
    RESERVED_HAS to ::PerlMooseKeywordElementImpl,
    RESERVED_INNER to ::PerlMooseKeywordSubNameElementImpl,
    RESERVED_META to ::PerlMooseKeywordElementImpl,
    RESERVED_OVERRIDE to ::PerlMooseKeywordElementImpl,
    RESERVED_SUPER to ::PerlMooseKeywordSubNameElementImpl,
    RESERVED_WITH to ::PerlMooseKeywordElementImpl,
    SCALAR_NAME to ::PerlVariableNameElementImpl,
    STRING_CONTENT to ::PerlStringContentElementImpl,
    STRING_CONTENT_QQ to ::PerlStringContentElementImpl,
    STRING_CONTENT_XQ to ::PerlStringContentElementImpl,
    SUB_NAME to ::PerlSubNameElementImpl,
    TAG_PACKAGE to ::PerlNamespaceElementImpl,
    VERSION_ELEMENT to ::PerlVersionElementImpl,
  )

  override fun createLeaf(type: IElementType, text: CharSequence): LeafElement? =
    tokenFactories[type]?.let { it(type, text) }
}