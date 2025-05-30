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

package completion;

import base.PerlCompletionPopupTestCase;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import org.jetbrains.annotations.NotNull;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

@SuppressWarnings("Junit4RunWithInspection")
@RunWith(Parameterized.class)
public class PerlAutoColonInVariablesPopupTest extends PerlCompletionPopupTestCase {
  private final @NotNull String myName;
  private final boolean myIsAutocolonEnabled;
  private final boolean myShouldHavePopupOnFirstColon;
  private final boolean myShouldHavePopupOnSecondColon;
  private final @NotNull Function<? super String, String> myContentWrapper;

  public PerlAutoColonInVariablesPopupTest(@NotNull String name,
                                           boolean isAutocolonEnabled,
                                           boolean shouldHavePopupOnFirstColon,
                                           boolean shouldHavePopupOnSecondColon,
                                           @NotNull Function<? super String, String> contentWrapper) {
    myName = name;
    myIsAutocolonEnabled = isAutocolonEnabled;
    myShouldHavePopupOnFirstColon = shouldHavePopupOnFirstColon;
    myShouldHavePopupOnSecondColon = shouldHavePopupOnSecondColon;
    myContentWrapper = contentWrapper;
  }

  @Override
  protected String getBaseDataPath() {
    return "completionPopup/perl";
  }

  @Override
  public String getFileExtension() {
    return PerlFileTypePackage.EXTENSION;
  }

  protected @NotNull String wrapToContext(@NotNull String sample) {
    return myContentWrapper.apply(sample);
  }

  protected boolean isAutoColonEnabled() {
    return myIsAutocolonEnabled;
  }

  protected boolean shouldHavePopupOnFirstColon() {
    return myShouldHavePopupOnFirstColon;
  }

  ;

  protected boolean shouldHavePopupOnSecondColon() {
    return myShouldHavePopupOnSecondColon;
  }

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
  public void testArrayStartSecond() { doTest("@:<caret>", shouldHavePopupOnSecondColon()); }

  @Test
  public void testArrayEndFirst() { doTest("@MyTest::Some::Package<caret>", shouldHavePopupOnFirstColon()); }

  @Test
  public void testArrayEndSecond() { doTest("@MyTest::Some::Package:<caret>", shouldHavePopupOnSecondColon()); }

  @Test
  public void testHashStartFirst() {
    assumeCode();
    doTest("%<caret>", shouldHavePopupOnFirstColon());
  }

  private void assumeCode() {
    Assume.assumeTrue("Available only in code", myName.equals("code"));
  }

  @Test
  public void testHashStartSecond() {
    assumeCode();
    doTest("%:<caret>", shouldHavePopupOnSecondColon());
  }

  @Test
  public void testHashEndFirst() {
    assumeCode();
    doTest("%MyTest::Some::Package<caret>", shouldHavePopupOnFirstColon());
  }

  @Test
  public void testHashEndSecond() {
    assumeCode();
    doTest("%MyTest::Some::Package:<caret>", shouldHavePopupOnSecondColon());
  }

  @Test
  public void testGlobStartFirst() {
    assumeCode();
    doTest("*<caret>", shouldHavePopupOnFirstColon());
  }

  @Test
  public void testGlobStartSecond() {
    assumeCode();
    doTest("*:<caret>", shouldHavePopupOnSecondColon());
  }

  @Test
  public void testGlobEndFirst() {
    assumeCode();
    withFuture();
    doTest("*Future<caret>", shouldHavePopupOnFirstColon());
  }

  @Test
  public void testGlobEndSecond() {
    assumeCode();
    withFuture();
    doTest("*Future:<caret>", shouldHavePopupOnSecondColon());
  }

  @Parameterized.Parameters(name = "{0},{1}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
      {"stringXQ", true, false, true, (Function<? super String, String>)sample -> "qx/test" + sample + " /"},
      {"stringXQ", false, false, true, (Function<? super String, String>)sample -> "qx/test" + sample + " /"},
      {"stringDQ", true, false, true, (Function<? super String, String>)sample -> "qq/test" + sample + " /"},
      {"stringDQ", false, false, true, (Function<? super String, String>)sample -> "qq/test" + sample + " /"},
      {"heredocDQ", true, false, true, (Function<? super String, String>)sample -> "<<EOM\n" +
                                                                                   "test" + sample + " test\n" +
                                                                                   "EOM"},
      {"heredocDQ", false, false, true, (Function<? super String, String>)sample -> "<<EOM\n" +
                                                                                    "test" + sample + " test\n" +
                                                                                    "EOM"},
      {"heredocXQ", true, false, true, (Function<? super String, String>)sample -> "<<`EOM`\n" +
                                                                                   "test" + sample + " test\n" +
                                                                                   "EOM"},
      {"heredocXQ", false, false, true, (Function<? super String, String>)sample -> "<<`EOM`\n" +
                                                                                    "test" + sample + " test\n" +
                                                                                    "EOM"},
      {"regex", true, false, true, (Function<? super String, String>)sample -> "/test" + sample + " /"},
      {"regex", false, false, true, (Function<? super String, String>)sample -> "/test" + sample + " /"},
      {"regexX", true, false, true, (Function<? super String, String>)sample -> "/test" + sample + " /x"},
      {"regexX", false, false, true, (Function<? super String, String>)sample -> "/test" + sample + " /x"},
      {"code", true, true, true, (Function<? super String, String>)sample -> sample},
      {"code", false, false, true, (Function<? super String, String>)sample -> sample},
    });
  }
}
