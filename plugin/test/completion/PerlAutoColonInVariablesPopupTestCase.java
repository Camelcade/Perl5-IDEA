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

package completion;

import base.PerlCompletionPopupTestCase;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import org.jetbrains.annotations.NotNull;
import org.junit.Assume;
import org.junit.Test;

public abstract class PerlAutoColonInVariablesPopupTestCase extends PerlCompletionPopupTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/completionPopup/perl";
  }

  @Override
  public String getFileExtension() {
    return PerlFileTypePackage.EXTENSION; // requires for package.. test
  }

  protected abstract @NotNull String wrapToContext(@NotNull String sample);

  protected abstract boolean isAutoColonEnabled();

  protected abstract boolean shouldHavePopupOnFirstColon();

  protected abstract boolean shouldHavePopupOnSecondColon();

  protected void doTest(@NotNull String sample, boolean expected) {
    if (isAutoColonEnabled()) {
      doTestWithAutoColon(wrapToContext(sample), ":", expected);
    }
    else {
      doTestWithoutAutoColon(wrapToContext(sample), ":", expected);
    }
  }

  @Test
  public void testScalarStartFirst() {doTest("$<caret>", shouldHavePopupOnFirstColon());}

  @Test
  public void testScalarStartSecond() {doTest("$:<caret>", shouldHavePopupOnSecondColon());}

  @Test
  public void testScalarEndFirst() {doTest("$MyTest::Some::Package<caret>", shouldHavePopupOnFirstColon());}

  @Test
  public void testScalarEndSecond() {doTest("$MyTest::Some::Package:<caret>", shouldHavePopupOnSecondColon());}

  @Test
  public void testArrayStartFirst() {doTest("@<caret>", shouldHavePopupOnFirstColon());}

  @Test
  public void testArrayStartSecond() {doTest("@:<caret>", shouldHavePopupOnSecondColon());}

  @Test
  public void testArrayEndFirst() {doTest("@MyTest::Some::Package<caret>", shouldHavePopupOnFirstColon());}

  @Test
  public void testArrayEndSecond() {doTest("@MyTest::Some::Package:<caret>", shouldHavePopupOnSecondColon());}

  @Test
  public void testHashStartFirst() {doTest("%<caret>", shouldHavePopupOnFirstColon());}

  @Test
  public void testHashStartSecond() {doTest("%:<caret>", shouldHavePopupOnSecondColon());}

  @Test
  public void testHashEndFirst() {doTest("%MyTest::Some::Package<caret>", shouldHavePopupOnFirstColon());}

  @Test
  public void testHashEndSecond() {doTest("%MyTest::Some::Package:<caret>", shouldHavePopupOnSecondColon());}

  @Test
  public void testGlobStartFirst() {doTest("*<caret>", shouldHavePopupOnFirstColon());}

  @Test
  public void testGlobStartSecond() {doTest("*:<caret>", shouldHavePopupOnSecondColon());}

  @Test
  public void testGlobEndFirst() {
    withFuture();
    doTest("*Future<caret>", shouldHavePopupOnFirstColon());
  }

  @Test
  public void testGlobEndSecond() {
    withFuture();
    doTest("*Future:<caret>", shouldHavePopupOnSecondColon());
  }

  public static class PlainEnabled extends PerlAutoColonInVariablesPopupTestCase {
    @Override
    protected @NotNull String wrapToContext(@NotNull String sample) {
      return sample;
    }

    @Override
    protected boolean isAutoColonEnabled() {
      return true;
    }

    @Override
    protected boolean shouldHavePopupOnFirstColon() {
      return true;
    }

    @Override
    protected boolean shouldHavePopupOnSecondColon() {
      return true;
    }
  }

  public static class PlainDisabled extends PlainEnabled {
    @Override
    protected boolean isAutoColonEnabled() {
      return false;
    }

    @Override
    protected boolean shouldHavePopupOnFirstColon() {
      return false;
    }
  }

  private abstract static class LiteralsTestCase extends PerlAutoColonInVariablesPopupTestCase {
    @Override
    protected boolean shouldHavePopupOnFirstColon() {
      return false;
    }

    @Override
    protected boolean shouldHavePopupOnSecondColon() {
      return true;
    }

    @Override
    public void testHashStartFirst() {
      skipTest();
    }

    @Override
    public void testHashStartSecond() {
      skipTest();
    }

    @Override
    public void testHashEndFirst() {
      skipTest();
    }

    @Override
    public void testHashEndSecond() {
      skipTest();
    }

    @Override
    public void testGlobStartFirst() {
      skipTest();
    }

    @Override
    public void testGlobStartSecond() {
      skipTest();
    }

    @Override
    public void testGlobEndFirst() {
      skipTest();
    }

    @Override
    public void testGlobEndSecond() {
      skipTest();
    }

    private void skipTest() {
      //noinspection ConstantConditions
      Assume.assumeTrue("Not available in string", false);
    }
  }

  public static class DQStringEnabled extends LiteralsTestCase {
    @Override
    protected @NotNull String wrapToContext(@NotNull String sample) {
      return "qq/test" + sample + " /";
    }

    @Override
    protected boolean isAutoColonEnabled() {
      return true;
    }
  }

  public static class DQSTringDisabled extends DQStringEnabled {
    @Override
    protected boolean isAutoColonEnabled() {
      return false;
    }
  }

  public static class XQStringEnabled extends LiteralsTestCase {
    @Override
    protected @NotNull String wrapToContext(@NotNull String sample) {
      return "qx/test" + sample + " /";
    }

    @Override
    protected boolean isAutoColonEnabled() {
      return true;
    }
  }

  public static class XQStringDisabled extends XQStringEnabled {
    @Override
    protected boolean isAutoColonEnabled() {
      return false;
    }
  }

  public static class RegexEnabled extends LiteralsTestCase {
    @Override
    protected @NotNull String wrapToContext(@NotNull String sample) {
      return "/test" + sample + " /";
    }

    @Override
    protected boolean isAutoColonEnabled() {
      return true;
    }
  }

  public static class RegexDisabled extends RegexEnabled {
    @Override
    protected boolean isAutoColonEnabled() {
      return false;
    }
  }

  public static class RegexXEnabled extends LiteralsTestCase {
    @Override
    protected @NotNull String wrapToContext(@NotNull String sample) {
      return "/test" + sample + " /x";
    }

    @Override
    protected boolean isAutoColonEnabled() {
      return true;
    }
  }

  public static class RegexXDisabled extends RegexXEnabled {
    @Override
    protected boolean isAutoColonEnabled() {
      return false;
    }
  }

  public static class HeredocDQEnabled extends LiteralsTestCase {
    @Override
    protected @NotNull String wrapToContext(@NotNull String sample) {
      return "<<EOM\n" +
             "test" + sample + " test\n" +
             "EOM";
    }

    @Override
    protected boolean isAutoColonEnabled() {
      return true;
    }
  }

  public static class HeredocDQDisabled extends HeredocDQEnabled {
    @Override
    protected boolean isAutoColonEnabled() {
      return false;
    }
  }

  public static class HeredocXQEnabled extends LiteralsTestCase {
    @Override
    protected @NotNull String wrapToContext(@NotNull String sample) {
      return "<<`EOM`\n" +
             "test" + sample + " test\n" +
             "EOM";
    }

    @Override
    protected boolean isAutoColonEnabled() {
      return true;
    }
  }

  public static class HeredocXQDisabled extends HeredocXQEnabled {
    @Override
    protected boolean isAutoColonEnabled() {
      return false;
    }
  }
}
