/*
 * Copyright 2015-2020 Alexandr Evstigneev
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
import org.junit.Test;

import java.util.List;
import java.util.function.Function;
public class PerlRegexParts extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/unit/perl/regex";
  }

  @Test
  public void testLazyParsable() {
    doTestRegexString();
  }

  @Test
  public void testLazyParsableX() {
    doTestRegexString();
  }

  @Test
  public void testLazyParsableXX() {
    doTestRegexString();
  }

  @Test
  public void testBlock() {
    doTestRegexEval();
  }

  @Test
  public void testEmptyBlock() {
    doTestRegexEval();
  }

  @Test
  public void testLazyParsableBlock() {
    doTestRegexEval();
  }

  @Test
  public void testEmpty() {
    doTestRegexString();
  }

  @Test
  public void testNormal() {
    doTestRegexString();
  }

  private void doTestRegexString() {
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
