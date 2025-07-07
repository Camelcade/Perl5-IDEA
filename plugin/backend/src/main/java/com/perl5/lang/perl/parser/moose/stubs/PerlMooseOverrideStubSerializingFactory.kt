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

package com.perl5.lang.perl.parser.moose.stubs

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.tree.IElementType
import com.perl5.lang.perl.parser.moose.psi.impl.PerlMooseOverrideStatement
import com.perl5.lang.perl.psi.PerlSubDefinitionElement
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStubSerializingFactory


open class PerlMooseOverrideStubSerializingFactory(elementType: IElementType) : PerlSubDefinitionStubSerializingFactory(elementType) {
  override fun createPsi(stub: PerlSubDefinitionStub): PerlSubDefinitionElement = PerlMooseOverrideStatement(stub, elementType)

  override fun shouldCreateStub(node: ASTNode): Boolean {
    val psi = node.psi

    return psi is PerlMooseOverrideStatement &&
      psi.isValid &&
      StringUtil.isNotEmpty(psi.subName)
  }
}