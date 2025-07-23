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

package com.jetbrains.rdclient.daemon.com.jetbrains.rdclient.daemon

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.colors.CodeInsightColors
import com.jetbrains.rd.ide.model.HighlighterModel
import com.jetbrains.rdclient.daemon.FrontendHighlighterSuppressionHandler
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter
import com.perl5.lang.perl.util.PerlFrontendUtil

class FrontendHighlighterSuppressionHandler : FrontendHighlighterSuppressionHandler {
  // Heavy highlighters generated only on the backend
  private val unSupressedIds = listOf(
    PerlSyntaxHighlighter.UNUSED_DEPRECATED,
  ).map { "IJ.${it.externalName}" }.toSet()

  override fun shouldSuppress(
    highlighterModel: HighlighterModel,
    document: Document
  ): Boolean {
    if (!PerlFrontendUtil.isPluginDocument(document)) {
      return false
    }
    val attributeId = highlighterModel.properties.attributeId
    if (attributeId.startsWith("IJ.PERL_") && !unSupressedIds.contains(attributeId)) {
      return true
    }
    // suppress all matched braces highlighting from the backend for perl files
    val attributeName = highlighterModel.textAttributesKey?.externalName ?: return false
    return attributeName == CodeInsightColors.MATCHED_BRACE_ATTRIBUTES.externalName ||
      attributeName == CodeInsightColors.UNMATCHED_BRACE_ATTRIBUTES.externalName
  }
}