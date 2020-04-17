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
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
public class PerlStubsTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/unit/perl/stubs";
  }

  @Test
  public void testFunctionParametersFun_pl() {doTest();}

  @Test
  public void testMojoliciousLite_pl() {doTest();}

  @Test
  public void testExceptionClass_pl() {doTest();}

  @Test
  public void testAugment_pl() {doTest();}

  @Test
  public void testDoRequire_pl() {doTest();}

  @Test
  public void testSubDeclaration_pl() {doTest();}

  @Test
  public void testSubDefinition_pl() {doTest();}

  @Test
  public void testMethodDefinition_pl() {doTest();}

  @Test
  public void testFuncDefinition_pl() {doTest();}

  @Test
  public void testClassAccessor_pl() {doTest();}

  @Test
  public void testMooseAttr_pl() {doTest();}

  @Test
  public void testConstants_pl() {doTest();}

  @Test
  public void testNamespace_pl() {
    doTest();
  }

  @Test
  public void testNamespace_pm() {
    doTest();
  }

  @Test
  public void testNamespaceDeprecated_pl() {
    doTest();
  }

  @Test
  public void testVariables_pl() {
    doTest();
  }

  @Test
  public void testGlobs_pl() {
    doTest();
  }

  @Test
  public void testNestedPodItems_pl() {doTest();}

  @Test
  public void testNestedPodItemsLong_pl() {doTest();}

  @Test
  public void testNestedPodSections_pl() {doTest();}

  @NotNull
  @Override
  protected String computeAnswerFileName(@NotNull String appendix) {
    return getTestName(true).replace('_', '.') + ".txt";
  }

  private void doTest() {
    doTestStubs();
  }
}
