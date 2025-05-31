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

package smartKeys;


import com.intellij.application.options.CodeStyle;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import editor.PerlSmartKeysTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Collection;

import static com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings.OptionalConstructions.*;


@SuppressWarnings("Junit4RunWithInspection")
@RunWith(Parameterized.class)
public class PerlSubSignatureTypedHandlerTest extends PerlSmartKeysTestCase {

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
  protected String getBaseDataPath() {
    return "smartKeys/perl";
  }

  @Override
  protected @NotNull String computeAnswerFileName(@NotNull String appendix) {
    return super.computeAnswerFileName("." + getResultAppendix());
  }

  protected PerlCodeStyleSettings getCustomSettings() {
    return CodeStyle.getSettings(getProject()).getCustomSettings(PerlCodeStyleSettings.class);
  }

  @Test
  public void testEqualsInSignatureNoAlign() {
    getCustomSettings().ALIGN_CONSECUTIVE_ASSIGNMENTS = NO_ALIGN;
    doTestTypingEquals();
  }

  @Test
  public void testEqualsInSignatureInStatement() {
    getCustomSettings().ALIGN_CONSECUTIVE_ASSIGNMENTS = ALIGN_IN_STATEMENT;
    doTestTypingEquals();
  }

  @Test
  public void testEqualsInSignatureLines() {
    getCustomSettings().ALIGN_CONSECUTIVE_ASSIGNMENTS = ALIGN_LINES;
    doTestTypingEquals();
  }

  private void doTestTypingEquals() {
    initWithFileSmart("equalInSignature");
    doTestTypingWithoutInit("=");
  }

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return getSubLikeTestParameters();
  }
}
