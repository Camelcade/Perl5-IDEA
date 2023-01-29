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

package smartKeys;


import com.intellij.application.options.CodeStyle;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import editor.PerlSmartKeysTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings.OptionalConstructions.*;


public abstract class PerlSubSignatureTypedHandlerTestCase extends PerlSmartKeysTestCase {

  @Override
  protected String getBaseDataPath() {
    return "smartKeys/perl";
  }

  @Override
  protected @NotNull String computeAnswerFileName(@NotNull String appendix) {
    return super.computeAnswerFileName("." + getResultAppendix());
  }

  protected abstract @NotNull String getResultAppendix();

  protected CommonCodeStyleSettings getSettings() {
    return CodeStyle.getSettings(getProject()).getCommonSettings(PerlLanguage.INSTANCE);
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

  public static class After extends PerlSubSignatureTypedHandlerTestCase {
    @Override
    protected @NotNull String getResultAppendix() {
      return "after";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "after somemethod";
    }
  }

  public static class Around extends PerlSubSignatureTypedHandlerTestCase {
    @Override
    protected @NotNull String getResultAppendix() {
      return "around";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "around somemethod";
    }
  }

  public static class Augment extends PerlSubSignatureTypedHandlerTestCase {
    @Override
    protected @NotNull String getResultAppendix() {
      return "augment";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "augment somemethod";
    }
  }

  public static class Before extends PerlSubSignatureTypedHandlerTestCase {
    @Override
    protected @NotNull String getResultAppendix() {
      return "before";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "before somemethod";
    }
  }

  public static class Fun extends PerlSubSignatureTypedHandlerTestCase {
    @Override
    protected @NotNull String getResultAppendix() {
      return "fun";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "fun somefun";
    }
  }

  public static class Func extends PerlSubSignatureTypedHandlerTestCase {
    @Override
    protected @NotNull String getResultAppendix() {
      return "func";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "func somefunc";
    }
  }

  public static class FunExpr extends PerlSubSignatureTypedHandlerTestCase {
    @Override
    protected @NotNull String getResultAppendix() {
      return "funExpr";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "fun";
    }
  }

  public static class Method extends PerlSubSignatureTypedHandlerTestCase {
    @Override
    protected @NotNull String getResultAppendix() {
      return "method";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "method somemethod";
    }
  }

  public static class MethodExpr extends PerlSubSignatureTypedHandlerTestCase {
    @Override
    protected @NotNull String getResultAppendix() {
      return "methodExpr";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "method";
    }
  }

  public static class OverrideKw extends PerlSubSignatureTypedHandlerTestCase {
    @Override
    protected @NotNull String getResultAppendix() {
      return "override";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "override somemethod";
    }
  }

  public static class Sub extends PerlSubSignatureTypedHandlerTestCase {
    @Override
    protected @NotNull String getResultAppendix() {
      return "sub";
    }

    @Override
    protected @NotNull String getPerTestCode() {
      return "sub somesub";
    }
  }

  public static class SubExpr extends PerlSubSignatureTypedHandlerTestCase {
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
