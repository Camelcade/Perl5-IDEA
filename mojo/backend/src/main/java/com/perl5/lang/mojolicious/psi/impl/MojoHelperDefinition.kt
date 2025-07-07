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
package com.perl5.lang.mojolicious.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.perl5.lang.mojolicious.MojoIcons
import com.perl5.lang.perl.psi.PerlSubExpr
import com.perl5.lang.perl.psi.impl.PerlSubCallElement
import com.perl5.lang.perl.psi.light.PerlLightMethodDefinitionElement
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub
import javax.swing.Icon

class MojoHelperDefinition : PerlLightMethodDefinitionElement<PerlSubCallElement> {
  constructor(
    wrapper: PerlSubCallElement,
    name: String,
    elementType: IElementType,
    nameIdentifier: PsiElement,
    packageName: String?,
    elementSub: PerlSubExpr
  ) : super(wrapper, name, elementType, nameIdentifier, packageName, elementSub)

  constructor(wrapper: PerlSubCallElement, stub: PerlSubDefinitionStub) : super(wrapper, stub)

  override fun isStatic(): Boolean = true

  override fun getIcon(flags: Int): Icon = MojoIcons.MOJO_FILE
}
