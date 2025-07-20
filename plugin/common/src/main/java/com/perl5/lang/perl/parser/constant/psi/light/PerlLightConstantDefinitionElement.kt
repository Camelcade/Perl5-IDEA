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

package com.perl5.lang.perl.parser.constant.psi.light

import com.intellij.openapi.util.AtomicNotNullLazyValue
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.perl5.PerlIcons
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement
import com.perl5.lang.perl.psi.light.PerlLightSubDefinitionElement
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations
import com.perl5.lang.perl.psi.utils.PerlSubArgument
import javax.swing.Icon

class PerlLightConstantDefinitionElement : PerlLightSubDefinitionElement<PerlUseStatementElement?> {
  constructor(
    wrapper: PerlUseStatementElement,
    subName: String,
    elementType: IElementType,
    nameIdentifier: PsiElement,
    packageName: String?,
    subArguments: MutableList<PerlSubArgument>,
    annotations: PerlSubAnnotations?,
    returnValueFromCodeProvider: AtomicNotNullLazyValue<out PerlValue?>
  ) : super(wrapper, subName, elementType, nameIdentifier, packageName, subArguments, annotations, returnValueFromCodeProvider, null)

  constructor(delegate: PerlUseStatementElement, stub: PerlSubDefinitionStub) : super(delegate, stub)

  override fun getIcon(flags: Int): Icon = PerlIcons.CONSTANT_GUTTER_ICON

  override fun isMethod(): Boolean = true

  override fun isStatic(): Boolean = true
}