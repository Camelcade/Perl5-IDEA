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
package com.perl5.lang.perl.parser.moose.psi.impl

import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.util.Function
import com.perl5.lang.perl.parser.PerlIdentifierRangeProvider
import com.perl5.lang.perl.psi.impl.PerlSubCallElement
import com.perl5.lang.perl.psi.light.PerlLightMethodDefinitionElement
import com.perl5.lang.perl.psi.properties.PerlPodAwareElement
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations
import com.perl5.lang.perl.psi.utils.PerlSubArgument

class PerlAttributeDefinition : PerlLightMethodDefinitionElement<PerlSubCallElement>, PerlIdentifierRangeProvider, PerlPodAwareElement {
  constructor(
    wrapper: PerlSubCallElement,
    name: String,
    elementType: IElementType,
    nameIdentifier: PsiElement?,
    packageName: String?,
    subArguments: MutableList<PerlSubArgument?>,
    annotations: PerlSubAnnotations?
  ) : super(wrapper, name, elementType, nameIdentifier, packageName, subArguments, annotations)

  constructor(
    wrapper: PerlSubCallElement,
    stub: PerlSubDefinitionStub
  ) : super(wrapper, stub)

  override fun getNameComputation(): Function<String, String> = DEFAULT_NAME_COMPUTATION

  override fun getRangeInIdentifier(): TextRange {
    val nameIdentifier = getNameIdentifier() ?: return TextRange.EMPTY_RANGE
    val manipulator = ElementManipulators.getNotNullManipulator<PsiElement?>(nameIdentifier)
    val defaultRange = manipulator.getRangeInElement(nameIdentifier)
    return if (StringUtil.startsWith(defaultRange.subSequence(nameIdentifier.node.chars), "+"))
      TextRange.create(defaultRange.startOffset + 1, defaultRange.endOffset)
    else
      defaultRange
  }

  override fun getPodAnchor(): PsiElement = delegate

  companion object {
    @JvmField
    val DEFAULT_NAME_COMPUTATION: Function<String, String> =
      Function { name: String -> if (StringUtil.startsWith(name, "+")) name.substring(1) else name }
  }
}
