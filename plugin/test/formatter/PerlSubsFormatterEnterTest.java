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

public abstract class PerlSubsFormatterEnterTest extends PerlFormatterTestCase {

  @Override
  protected final String getBaseDataPath() {
    return "testData/formatter/perl/enter/subs";
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
  public void testSignatureAfterCommaContinuation() {
    getSettings().ALIGN_MULTILINE_PARAMETERS = false;
    doTest();
  }

  @Test
  public void testSignatureAfterCommaContinuationAligned() {
    getSettings().ALIGN_MULTILINE_PARAMETERS = true;
    doTest();
  }

  @NotNull
  @Override
  protected String computeAnswerFileName(@NotNull String appendix) {
    return super.computeAnswerFileName("." + getResultAppendix());
  }

  @NotNull
  protected abstract String getResultAppendix();

  @Override
  public void initWithFileContent(String filename, String extension, String content) {
    super.initWithFileContent(filename, extension, getPatchedContent(content));
  }

  protected void doTest() {
    doTestEnter();
  }

  public static class After extends PerlSubsFormatterEnterTest {
    @NotNull
    @Override
    protected String getResultAppendix() {
      return "after";
    }

    @NotNull
    @Override
    protected String getPerTestCode() {
      return "after somemethod";
    }
  }

  public static class Around extends PerlSubsFormatterEnterTest {
    @NotNull
    @Override
    protected String getResultAppendix() {
      return "around";
    }

    @NotNull
    @Override
    protected String getPerTestCode() {
      return "around somemethod";
    }
  }

  public static class Augment extends PerlSubsFormatterEnterTest {
    @NotNull
    @Override
    protected String getResultAppendix() {
      return "augment";
    }

    @NotNull
    @Override
    protected String getPerTestCode() {
      return "augment somemethod";
    }
  }

  public static class Before extends PerlSubsFormatterEnterTest {
    @NotNull
    @Override
    protected String getResultAppendix() {
      return "before";
    }

    @NotNull
    @Override
    protected String getPerTestCode() {
      return "before somemethod";
    }
  }

  public static class Fun extends PerlSubsFormatterEnterTest {
    @NotNull
    @Override
    protected String getResultAppendix() {
      return "fun";
    }

    @NotNull
    @Override
    protected String getPerTestCode() {
      return "fun somefun";
    }
  }

  public static class Func extends PerlSubsFormatterEnterTest {
    @NotNull
    @Override
    protected String getResultAppendix() {
      return "func";
    }

    @NotNull
    @Override
    protected String getPerTestCode() {
      return "func somefunc";
    }
  }

  public static class FunExpr extends PerlSubsFormatterEnterTest {
    @NotNull
    @Override
    protected String getResultAppendix() {
      return "funExpr";
    }

    @NotNull
    @Override
    protected String getPerTestCode() {
      return "fun";
    }
  }

  public static class Method extends PerlSubsFormatterEnterTest {
    @NotNull
    @Override
    protected String getResultAppendix() {
      return "method";
    }

    @NotNull
    @Override
    protected String getPerTestCode() {
      return "method somemethod";
    }
  }

  public static class MethodExpr extends PerlSubsFormatterEnterTest {
    @NotNull
    @Override
    protected String getResultAppendix() {
      return "methodExpr";
    }

    @NotNull
    @Override
    protected String getPerTestCode() {
      return "method";
    }
  }

  public static class OverrideKw extends PerlSubsFormatterEnterTest {
    @NotNull
    @Override
    protected String getResultAppendix() {
      return "override";
    }

    @NotNull
    @Override
    protected String getPerTestCode() {
      return "override somemethod";
    }
  }

  public static class Sub extends PerlSubsFormatterEnterTest {
    @NotNull
    @Override
    protected String getResultAppendix() {
      return "sub";
    }

    @NotNull
    @Override
    protected String getPerTestCode() {
      return "sub somesub";
    }
  }

  public static class SubExpr extends PerlSubsFormatterEnterTest {
    @NotNull
    @Override
    protected String getResultAppendix() {
      return "subExpr";
    }

    @NotNull
    @Override
    protected String getPerTestCode() {
      return "sub";
    }
  }
}
