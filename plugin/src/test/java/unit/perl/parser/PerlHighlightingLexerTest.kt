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

package unit.perl.parser

import base.PerlLightTestCase
import com.perl5.lang.perl.lexer.PerlLexer
import com.perl5.lang.perl.lexer.PerlLexingContext
import com.perl5.lang.perl.lexer.adapters.PerlMergingLexerAdapter
import org.jetbrains.annotations.NonNls
import org.junit.Test
import java.lang.reflect.Modifier

class PerlHighlightingLexerTest : PerlLightTestCase() {
  private val lexerStates = PerlLexer::class.java.declaredFields
    .filter { Modifier.isStatic(it.modifiers) && it.type == Int::class.java && it.canAccess(null) }
    .associate { it.getInt(null) to it.name }

  override fun getBaseDataPath(): @NonNls String = "unit/perl/lexer"

  @Test
  fun testHeredocInRegexp(): Unit = doTestLexer()

  @Test
  fun testHeredocInRegexpSublexed(): Unit = doTestLexer("heredocInRegexp", true)

  private fun doTestLexer(sourceName: String? = null, forceSubLexing: Boolean = false) {
    val testFileText = loadFileContent("${sourceName ?: getTestName(true)}${realDataFileExtension}")
    val lexer = PerlMergingLexerAdapter(PerlLexingContext.create(project).withEnforcedSublexing(forceSubLexing))
    lexer.start(testFileText)
    val sb = StringBuilder()
    while (lexer.tokenType != null) {
      sb.append("[").append(lexer.tokenType).append("] ").append(lexer.tokenStart).append("-").append(lexer.tokenEnd).append(" ")
        .append(lexerStates[lexer.state] ?: lexer.state).append("\n")
      if (lexer.tokenStart <= lexer.tokenEnd) {
        sb.append(protectSpaces(testFileText.substring(lexer.tokenStart, lexer.tokenEnd))).append("\n")
      } else {
        sb.append("************** BAD TOKEN RANGE *************").append("\n")
      }
      lexer.advance()
    }
    assertSameLinesWithFile(testResultsFilePath, sb.toString())
  }
}