/*
 * Copyright 2015-2024 Alexandr Evstigneev
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
package com.perl5.lang.tt2.idea.liveTemplates

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.Expression
import com.intellij.codeInsight.template.ExpressionContext
import com.intellij.codeInsight.template.Macro
import com.intellij.codeInsight.template.Result
import com.intellij.util.containers.toArray
import com.perl5.lang.tt2.TemplateToolkitFilterNames
import com.perl5.lang.tt2.idea.liveTemplates.LookupElements.LOOKUP_ELEMENTS

object LookupElements {
  @JvmStatic
  val LOOKUP_ELEMENTS: Array<LookupElement> by lazy {
    TemplateToolkitFilterNames.FILTER_NAMES.map { LookupElementBuilder.create(it) }.toArray(LookupElement.EMPTY_ARRAY)
  }
}

class TemplateToolkitFilterNameMacro : Macro(), TemplateToolkitFilterNames {

  override fun getName(): String = "tt2FilterName"

  override fun getPresentableName(): String = "tt2FilterName()"

  override fun getDefaultValue(): String = "filtername"

  override fun calculateResult(params: Array<Expression>, context: ExpressionContext?): Result? = null

  override fun calculateQuickResult(params: Array<Expression>, context: ExpressionContext?): Result? = calculateResult(params, context)

  override fun calculateLookupItems(params: Array<Expression>, context: ExpressionContext?): Array<LookupElement> = LOOKUP_ELEMENTS
}
