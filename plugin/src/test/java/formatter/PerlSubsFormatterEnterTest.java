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

package formatter;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Collection;

import static com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings.OptionalConstructions.*;

@SuppressWarnings("Junit4RunWithInspection")
@RunWith(Parameterized.class)
public class PerlSubsFormatterEnterTest extends PerlFormatterTestCase {

  @Parameter public String myResultAppendix;
  @Parameter(1) public String myPerTestCode;

  @Override
  public @NotNull String getPerTestCode() {
    return myPerTestCode;
  }

  public String getResultAppendix() {
    return myResultAppendix;
  }

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

  protected void doTest() {
    doTestEnter();
  }

  protected void doTest(@NotNull String fileName) {
    doTestEnter(fileName);
  }

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return getSubLikeTestParameters();
  }
}
