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

public abstract class PerlHeredocReparseTestCase extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/unit/perl/reparseHeredoc";
  }

  @Test
  public void testSomething() {doTest("something");}

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
  public void testMarkerQQ3() {doTest("MARKER_QQ3");}

  @Test
  public void testMarkerQQ3Indented() {doTest("    MARKER_QQ3");}

  private void doTest(@NotNull String textToInsert) {
    initWithFileSmartWithoutErrors(getSampleFileName());
    doTestReparseWithoutInit(textToInsert);
  }

  protected abstract @NotNull String getSampleFileName();

  @Override
  protected String getResultsTestDataPath() {
    return super.getResultsTestDataPath() + "/" + getClass().getSimpleName();
  }

  public static class Q extends PerlHeredocReparseTestCase {
    @Override
    protected @NotNull String getSampleFileName() {
      return "heredocQ";
    }
  }

  public static class QIndented extends PerlHeredocReparseTestCase {
    @Override
    protected @NotNull String getSampleFileName() {
      return "heredocQIndented";
    }
  }

  public static class QX extends PerlHeredocReparseTestCase {
    @Override
    protected @NotNull String getSampleFileName() {
      return "heredocQX";
    }
  }

  public static class QXIndented extends PerlHeredocReparseTestCase {
    @Override
    protected @NotNull String getSampleFileName() {
      return "heredocQXIndented";
    }
  }

  public static class QQ extends PerlHeredocReparseTestCase {
    @Override
    protected @NotNull String getSampleFileName() {
      return "heredocQQ";
    }
  }

  public static class QQIndented extends PerlHeredocReparseTestCase {
    @Override
    protected @NotNull String getSampleFileName() {
      return "heredocQQIndented";
    }
  }

  public static class QQBare extends PerlHeredocReparseTestCase {
    @Override
    protected @NotNull String getSampleFileName() {
      return "heredocQQBare";
    }
  }

  public static class QQBareIndented extends PerlHeredocReparseTestCase {
    @Override
    protected @NotNull String getSampleFileName() {
      return "heredocQQBareIndented";
    }
  }
}
