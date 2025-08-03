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

package com.perl5.lang.pod.parser.psi

import com.intellij.openapi.util.ClassExtension
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil


private val EP = ClassExtension<PodSectionRenderer<*>>("com.perl5.lang.pod.sectionRenderer")

interface PodSectionRenderer<Section : PodSection> {
  /**
   * Appends HTML representation of the section to the `builder`
   */
  fun renderElementAsHTML(podSection: Section, builder: StringBuilder, context: PodRenderingContext) {
    PodRenderUtil.renderPsiRangeAsHTML(podSection.firstChild, null, builder, context)
  }

  companion object {
    @Suppress("UNCHECKED_CAST")
    fun <Section : PodSection> instance(clazz: Class<Section>): PodSectionRenderer<Section> =
      EP.findSingle(clazz) as PodSectionRenderer<Section>

    operator fun <Section : PodSection> get(section: Section): PodSectionRenderer<Section> = instance(section.javaClass)
  }
}