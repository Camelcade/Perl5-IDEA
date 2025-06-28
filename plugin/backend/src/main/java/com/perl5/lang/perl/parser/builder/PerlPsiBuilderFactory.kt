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

package com.perl5.lang.perl.parser.builder

import com.intellij.lang.*
import com.intellij.lang.impl.PerlDebugBuilder
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.TestOnly
import java.util.concurrent.atomic.AtomicInteger

class PerlPsiBuilderFactory private constructor() {
  companion object {
    private var BUILDER_DIAGNOSTICS: PerlBuilderDiagnostics? = null

    fun createBuilder(project: Project,
                      chameleon: ASTNode,
                      lexer: Lexer?,
                      lang: Language,
                      seq: CharSequence): PsiBuilder {
      return BUILDER_DIAGNOSTICS?.let {
        val parserDefinition = getParserDefinition(lang, chameleon.elementType)
        return PerlDebugBuilder(
          project, parserDefinition, lexer ?: parserDefinition.createLexer(project), chameleon, seq, it)
      } ?: PsiBuilderFactory.getInstance().createBuilder(project, chameleon, lexer, lang, seq)
    }

    private fun getParserDefinition(language: Language?, tokenType: IElementType): ParserDefinition {
      val adjusted = language ?: tokenType.language
      val parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(adjusted)
                             ?: throw AssertionError(
                               "ParserDefinition absent for language: '" + adjusted.id + "' (" + adjusted.javaClass.getName() + "), " +
                               "for elementType: '" + tokenType.debugName + "' (" + tokenType.javaClass.getName() + ")")
      return parserDefinition
    }

    @TestOnly
    fun withDebugBuilder(diagnostics: PerlBuilderDiagnostics, runnable: Runnable) {
      val odlDiagnostics = BUILDER_DIAGNOSTICS
      try {
        BUILDER_DIAGNOSTICS = diagnostics
        runnable.run()
      }
      finally {
        BUILDER_DIAGNOSTICS = odlDiagnostics
      }
    }
  }

  class PerlBuilderDiagnostics {
    val rollbacks = hashMapOf<Int, AtomicInteger>()
    val passes = mutableListOf<Pair<Int,Int>>()

    override fun toString(): String {
      var totalRollbacks = 0
      var totalRolledback = 0
      rollbacks.forEach {
        totalRollbacks += it.value.get()
        totalRolledback += it.key * it.value.get()
      }
      var lexemeCount = 0
      var builders = 0
      val passes = this.passes.joinToString("\n") {
        builders++
        lexemeCount += it.second
        "${it.first};${it.second}"
      }

      val rollbacks = rollbacks.entries.asSequence()
        .sortedBy { it.key }
        .joinToString("\n") {
          val length = it.key
          val rollbacks = it.value.get()
          val tokensRolled = length * rollbacks
          "$length;$rollbacks;$tokensRolled;${tokensRolled * 100 / lexemeCount}%;${rollbacks * 100 / totalRollbacks}%;${tokensRolled * 100 / totalRolledback}%"
        }
      return """Diagnostics:
          Passes: $builders
    Tokens count: $lexemeCount
 Total rollbacks: $totalRollbacks
     Rolled back: $totalRolledback (${totalRolledback * 100 / lexemeCount}%) tokens

Characters;Tokens
$passes

Tokens;Rollbacks;Tokens rolled;% of tokens count;% of rollbacks;% of rolled back
$rollbacks
        """
    }
  }
}