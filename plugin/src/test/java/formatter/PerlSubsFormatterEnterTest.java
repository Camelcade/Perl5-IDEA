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

package formatter;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings.OptionalConstructions.*;

public abstract class PerlSubsFormatterEnterTest extends PerlFormatterTestCase {

  @Override
  protected final String getBaseDataPath() {
    return "formatter/perl/enter/subs";
  }

  @Override
  protected String getResultsTestDataPath() {
    return super.getResultsTestDataPath() + "/answers";
  }

  @Test
  public void testBodyAfterOpenBrace() {
    doTest();
  }

  @Test
  public void testBodyAfterStatementLast() {
    doTest();
  }

  @Test
  public void testBodyAfterStatementMid() {
    doTest();
  }

  @Test
  public void testSignatureElementDefaultValue() {
    doTest();
  }

  @Test
  public void testSignatureElementDefaultValueAfterAssign() {
    doTest();
  }

  @Test
  public void testSignatureElementDefaultValueAfterAssignMiddle() {
    doTest();
  }

  @Test
  public void testSignatureElementDefaultValueAfterAssignSecond() {
    doTest();
  }

  @Test
  public void testSignatureElementDefaultValueSecond() {
    getCustomSettings().ALIGN_CONSECUTIVE_ASSIGNMENTS = NO_ALIGN;
    doTest("signatureElementDefaultValueSecond");
  }

  @Test
  public void testSignatureElementDefaultValueSecondAligned() {
    getCustomSettings().ALIGN_CONSECUTIVE_ASSIGNMENTS = ALIGN_LINES;
    doTest("signatureElementDefaultValueSecond");
  }

  @Test
  public void testSignatureElementDefaultValueSecondAlignedStatement() {
    getCustomSettings().ALIGN_CONSECUTIVE_ASSIGNMENTS = ALIGN_IN_STATEMENT;
    doTest("signatureElementDefaultValueSecond");
  }

  @Test
  public void testSignatureElementDefaultValueMiddle() {
    getCustomSettings().ALIGN_CONSECUTIVE_ASSIGNMENTS = NO_ALIGN;
    doTest("signatureElementDefaultValueMiddle");
  }

  @Test
  public void testSignatureElementDefaultValueMiddleAligned() {
    getCustomSettings().ALIGN_CONSECUTIVE_ASSIGNMENTS = ALIGN_LINES;
    doTest("signatureElementDefaultValueMiddle");
  }

  @Test
  public void testSignatureElementDefaultValueMiddleAlignedStatement() {
    getCustomSettings().ALIGN_CONSECUTIVE_ASSIGNMENTS = ALIGN_IN_STATEMENT;
    doTest("signatureElementDefaultValueMiddle");
  }

  @Test
  public void testAttributeSecond() {
    getCustomSettings().ALIGN_ATTRIBUTES = false;
    doTest("attributeSecond");
  }

  @Test
  public void testAttributeSecondAligned() {
    getCustomSettings().ALIGN_ATTRIBUTES = true;
    doTest("attributeSecond");
  }

  @Test
  public void testAttributeMiddle() {
    getCustomSettings().ALIGN_ATTRIBUTES = false;
    doTest("attributeMiddle");
  }

  @Test
  public void testAttributeMiddleAligned() {
    getCustomSettings().ALIGN_ATTRIBUTES = true;
    doTest("attributeMiddle");
  }

  @Test
  public void testSignatureEmpty() {
    doTest();
  }

  @Test
  public void testSignatureAfterComma() {
    doTest();
  }

  @Test
  public void testSignatureAfterCommaMiddle() {
    doTest();
  }

  @Test
  public void testSignatureAfterCommaContinuation() {
    getSettings().ALIGN_MULTILINE_PARAMETERS = false;
    doTest("signatureAfterCommaContinuation");
  }

  @Test
  public void testSignatureAfterCommaContinuationAligned() {
    getSettings().ALIGN_MULTILINE_PARAMETERS = true;
    doTest("signatureAfterCommaContinuation");
  }

  @Test
  public void testSignatureAfterCommaContinuationMiddle() {
    getSettings().ALIGN_MULTILINE_PARAMETERS = false;
    doTest("signatureAfterCommaContinuationMiddle");
  }

  @Test
  public void testSignatureAfterCommaContinuationMiddleAligned() {
    getSettings().ALIGN_MULTILINE_PARAMETERS = true;
    doTest("signatureAfterCommaContinuationMiddle");
  }

  @Override
  protected @NotNull String computeAnswerFileName(@NotNull String appendix) {
    return super.computeAnswerFileName("." + getResultAppendix());
  }

  protected abstract @NotNull String getResultAppendix();

  protected void doTest() {
    doTestEnter();
  }

  protected void doTest(@NotNull String fileName) {
    doTestEnter(fileName);
  }

  public static class After extends PerlSubsFormatterEnterTest {
    @Override
    protected @NotNull String getResultAppendix() {
      return "after";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "after somemethod";
    }
  }

  public static class Around extends PerlSubsFormatterEnterTest {
    @Override
    protected @NotNull String getResultAppendix() {
      return "around";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "around somemethod";
    }
  }

  public static class Augment extends PerlSubsFormatterEnterTest {
    @Override
    protected @NotNull String getResultAppendix() {
      return "augment";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "augment somemethod";
    }
  }

  public static class Before extends PerlSubsFormatterEnterTest {
    @Override
    protected @NotNull String getResultAppendix() {
      return "before";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "before somemethod";
    }
  }

  public static class Fun extends PerlSubsFormatterEnterTest {
    @Override
    protected @NotNull String getResultAppendix() {
      return "fun";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "fun somefun";
    }
  }

  public static class Func extends PerlSubsFormatterEnterTest {
    @Override
    protected @NotNull String getResultAppendix() {
      return "func";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "func somefunc";
    }
  }

  public static class FunExpr extends PerlSubsFormatterEnterTest {
    @Override
    protected @NotNull String getResultAppendix() {
      return "funExpr";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "fun";
    }
  }

  public static class Method extends PerlSubsFormatterEnterTest {
    @Override
    protected @NotNull String getResultAppendix() {
      return "method";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "method somemethod";
    }
  }

  public static class MethodExpr extends PerlSubsFormatterEnterTest {
    @Override
    protected @NotNull String getResultAppendix() {
      return "methodExpr";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "method";
    }
  }

  public static class OverrideKw extends PerlSubsFormatterEnterTest {
    @Override
    protected @NotNull String getResultAppendix() {
      return "override";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "override somemethod";
    }
  }

  public static class Sub extends PerlSubsFormatterEnterTest {
    @Override
    protected @NotNull String getResultAppendix() {
      return "sub";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "sub somesub";
    }
  }

  public static class SubExpr extends PerlSubsFormatterEnterTest {
    @Override
    protected @NotNull String getResultAppendix() {
      return "subExpr";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "sub";
    }
  }
}
