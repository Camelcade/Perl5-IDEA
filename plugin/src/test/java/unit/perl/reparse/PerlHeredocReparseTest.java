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

package unit.perl.reparse;

import base.PerlLightTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class PerlHeredocReparseTest extends PerlLightTestCase {
  private final @NotNull String mySampleFileNameSuffix;

  public PerlHeredocReparseTest(@NotNull String sampleFileNameSuffix) {
    mySampleFileNameSuffix = sampleFileNameSuffix;
  }

  @Override
  protected String getBaseDataPath() {
    return "unit/perl/reparse/heredoc";
  }

  @Test
  public void testSomething() { doTest("something"); }

  @Test
  public void testScalar() {doTest("$something");}

  @Test
  public void testScalarDeref() {doTest("${\\foo();}");}

  @Test
  public void testArray() {doTest("@something");}

  @Test
  public void testArrayDeref() {doTest("@{\\foo();}");}

  @Test
  public void testEscape() {doTest("\\");}

  @Test
  public void testNewLine() {doTest("\n");}

  @Test
  public void testSomethingIndented() {doTest("      something");}

  @Test
  public void testMarkerQ1() {doTest("MARKER_Q1");}

  @Test
  public void testMarkerQ1Indented() {doTest("    MARKER_Q1");}

  @Test
  public void testMarkerQ2() {doTest("MARKER_Q2");}

  @Test
  public void testMarkerQ2Indented() {doTest("    MARKER_Q2");}

  @Test
  public void testMarkerQ3() {doTest("MARKER_Q3");}

  @Test
  public void testMarkerQ3Indented() {doTest("    MARKER_Q3");}

  @Test
  public void testMarkerQX1() {doTest("MARKER_QX1");}

  @Test
  public void testMarkerQX1Indented() {doTest("    MARKER_QX1");}

  @Test
  public void testMarkerQX2() {doTest("MARKER_QX2");}

  @Test
  public void testMarkerQX2Indented() {doTest("    MARKER_QX2");}

  @Test
  public void testMarkerQX3() {doTest("MARKER_QX3");}

  @Test
  public void testMarkerQX3Indented() {doTest("    MARKER_QX3");}

  @Test
  public void testMarkerQQ1() {doTest("MARKER_QQ1");}

  @Test
  public void testMarkerQQ1Indented() {doTest("    MARKER_QQ1");}

  @Test
  public void testMarkerQQ2() {doTest("MARKER_QQ2");}

  @Test
  public void testMarkerQQ2Indented() {doTest("    MARKER_QQ2");}

  @Test
  public void testMarkerQQ3() { doTest("MARKER_QQ3"); }

  @Test
  public void testMarkerQQ3Indented() { doTest("    MARKER_QQ3"); }

  private void doTest(@NotNull String textToInsert) {
    initWithFileSmartWithoutErrors(getSampleFileNameName());
    doTestReparseWithoutInit(textToInsert);
  }

  protected @NotNull String getSampleFileNameName() {
    return "heredoc" + mySampleFileNameSuffix;
  }

  @Override
  protected String getResultsTestDataPath() {
    return super.getResultsTestDataPath() + "/" + mySampleFileNameSuffix;
  }

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
      {"Q"},
      {"QIndented"},
      {"QQ"},
      {"QQBare"},
      {"QQBareIndented"},
      {"QQIndented"},
      {"QX"},
      {"QXIndented"},
    });
  }
}
