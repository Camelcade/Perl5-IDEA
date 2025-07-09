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

package com.perl5.lang.pod.elementTypes

import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.tree.IElementType
import com.perl5.lang.pod.parser.psi.mixin.PodSectionItem
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub
import com.perl5.lang.pod.psi.impl.PsiItemSectionImpl


class PodSectionItemStubSerializingFactory(elementType: IElementType) :
  PodStubBasedTitledSectionSerializingFactory<PodSectionItem>(elementType) {
  override fun createPsi(stub: PodSectionStub): PodSectionItem = PsiItemSectionImpl(stub, elementType)

  override fun createStub(psi: PodSectionItem, parentStub: StubElement<out PsiElement>?): PodSectionStub {
    val prefix = if (psi.isTargetable()) '+' else '-'
    return PodSectionStub(parentStub, elementType, prefix + psi.getPresentableText()!!)
  }

  override fun shouldCreateStub(psi: PodSectionItem): Boolean = psi.isIndexed() && StringUtil.isNotEmpty(psi.getPresentableText()) ||
    psi.isTargetable() && super.shouldCreateStub(psi)
}