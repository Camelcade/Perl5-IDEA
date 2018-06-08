/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package unit.perl;

import base.PerlLightTestCase;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlReplacementRegex;
import com.perl5.lang.perl.psi.PsiPerlReplacementRegex;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public class PerlRegexParts extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/unit/perl/regex";
  }

  public void testLazyParsable() {
    doTestRegexRegex();
  }

  public void testLazyParsableX() {
    doTestRegexRegex();
  }

  public void testLazyParsableXX() {
    doTestRegexRegex();
  }

  public void testBlock() {
    doTestRegexEval();
  }

  public void testEmptyBlock() {
    doTestRegexEval();
  }

  public void testLazyParsableBlock() {
    doTestRegexEval();
  }

  public void testEmpty() {
    doTestRegexRegex();
  }

  public void testNormal() {
    doTestRegexRegex();
  }

  private void doTestRegexRegex() {
    doTestCommon(PerlReplacementRegex::getReplaceRegex);
  }

  private void doTestRegexEval() {
    doTestCommon(PerlReplacementRegex::getReplaceBlock);
  }

  private void doTestCommon(@NotNull Function<PsiPerlReplacementRegex, PsiElement> producer) {
    initWithFileSmart();
    PsiPerlReplacementRegex regex = getElementAtCaret(PsiPerlReplacementRegex.class);
    assertNotNull(regex);
    List<PsiElement> regexParts = regex.getParts();
    assertSize(2, regexParts);
    assertEquals(regexParts.get(0), regex.getMatchRegex());
    assertEquals(regexParts.get(1), producer.apply(regex));
  }
}
