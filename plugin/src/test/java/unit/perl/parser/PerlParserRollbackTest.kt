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

package unit.perl.parser

import categories.Heavy
import com.perl5.lang.perl.idea.project.PerlNamesCache
import com.perl5.lang.perl.parser.builder.PerlPsiBuilderFactory
import com.perl5.lang.perl.psi.utils.PerlElementFactory
import org.junit.Test
import org.junit.experimental.categories.Category

@Category(Heavy::class)
class PerlParserRollbackTest : PerlParserTestBase() {
  override fun getBaseDataPath(): String {
    return "unit/perl/parser/rollbacks"
  }

  @Test
  fun testPerlTidy() {
    doTest()
  }

  @Test
  fun testPinxi() {
    doTest()
  }

  @Test
  fun testCatalyst() {
    doTest()
  }

  @Test
  fun testMojo() {
    doTest()
  }

  @Test
  fun testMoose() {
    doTest()
  }

  @Test
  fun testPerl536() {
    doTest()
  }

  @Test
  fun testPerl534() {
    doTest()
  }

  @Test
  fun testPerl532() {
    doTest()
  }

  @Test
  fun testPerl5303() {
    doTest()
  }

  @Test
  fun testPerl5125() {
    doTest()
  }

  @Test
  fun testMysqltuner() {
    doTest()
  }

  override fun doTest() {
    val diagnostics = PerlPsiBuilderFactory.PerlBuilderDiagnostics()
    PerlPsiBuilderFactory.withDebugBuilder(diagnostics) {
      PerlNamesCache.getInstance(project).cleanCache()
      val testFileText = loadTestFile("largeFiles/${getTestName(true)}")
      PerlElementFactory.createFile(project, testFileText)
    }

    assertSameLinesWithFile(testResultsFilePath, diagnostics.toString())
  }
}