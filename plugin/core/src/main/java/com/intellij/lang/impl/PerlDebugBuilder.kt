/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.intellij.lang.impl

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiBuilder.Marker
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.perl5.lang.perl.parser.builder.PerlPsiBuilderFactory
import java.util.concurrent.atomic.AtomicInteger

internal class PerlDebugBuilder(project: Project,
                       parserDefinition: ParserDefinition,
                       lexer: Lexer,
                       chameleon: ASTNode,
                       text: CharSequence,
                       val diagnostics: PerlPsiBuilderFactory.PerlBuilderDiagnostics) : PsiBuilderImpl(project, parserDefinition, lexer, chameleon, text) {

private val markersMap = hashMapOf<Marker, PerlStartMarker>()

  init {
    diagnostics.passes += text.length to lexemeCount
  }

  override fun mark(): Marker {
    return createPerlMarker(super.mark())
  }

  fun createPerlMarker(realMarker: Marker): PerlStartMarker {
    val perlMarker = PerlStartMarker(realMarker, this)
    markersMap.put(realMarker, perlMarker)
    return perlMarker
  }

  override fun getLatestDoneMarker(): StartMarker? {
    val realMarker = super.getLatestDoneMarker() ?: return null
    return markersMap.get(realMarker)!!
  }

  fun registerRollback(tokens: Int) {
    diagnostics.rollbacks.computeIfAbsent(tokens, { AtomicInteger() }).incrementAndGet()
  }
}