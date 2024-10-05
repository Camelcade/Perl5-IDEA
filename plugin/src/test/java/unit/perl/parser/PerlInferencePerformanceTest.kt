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

package unit.perl.parser

import base.PerlLightTestCase
import com.intellij.lang.Language
import com.intellij.psi.impl.PsiModificationTrackerImpl
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl
import com.perl5.lang.perl.psi.PerlSubDefinitionElement
import com.perl5.lang.perl.psi.utils.PerlResolveUtil
import org.junit.Test

class PerlInferencePerformanceTest : PerlLightTestCase() {
  @Test
  fun testPerlTidy() {
    initWithPerlTidy()
    val subDefinition =
      PsiTreeUtil.findElementOfClassAtOffset<PerlSubDefinitionElement>(file, 229113, PerlSubDefinitionElement::class.java, false)!!
    assertEquals("Perl::Tidy::Formatter::new", subDefinition.canonicalName)
    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(project)
    val start = System.nanoTime()
    var lastStart = start
    repeat(10) {
      PerlResolveUtil.computeReturnValueFromControlFlow(subDefinition)
      System.err.println("Done in ${System.nanoTime() - lastStart}")
      lastStart = System.nanoTime()
      (PsiModificationTracker.getInstance(project) as PsiModificationTrackerImpl).incLanguageModificationCount(Language.ANY)
    }
    System.err.println((System.nanoTime() - start) / 1000_000 / 10)
  }
}