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

package intellilang;

import base.PerlLightTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

@SuppressWarnings("Junit4RunWithInspection")
@RunWith(Parameterized.class)
public class PerlInjectedStringTypingTest extends PerlLightTestCase {
  private final @NotNull String myName;
  private final @NotNull Function<String, String> myContentModifier;

  public PerlInjectedStringTypingTest(@NotNull String name, @NotNull Function<String, String> contentModifier) {
    myName = name;
    myContentModifier = contentModifier;
  }

  @Override
  protected String getBaseDataPath() {
    return "intellilang/perl/typing";
  }

  @Test
  public void testStringWithInterpolationAfterVariable() { doTest(" typedtext"); }

  @Test
  public void testStringWithInterpolationAfterVariableJoin() {doTest("typedtext");}

  @Test
  public void testStringWithInterpolationBeforeVariable() {doTest("typedtext");}

  @Test
  public void testStringWithInterpolationEnd() {doTest("typedtext");}

  @Test
  public void testStringWithInterpolationInText() {doTest("typedtext");}

  @Test
  public void testStringWithInterpolationStart() {doTest("typedtext");}

  @Test
  public void testSimpleTab() {doTestSimpleText("\t");}

  @Test
  public void testSimpleNewLine() {doTestSimpleText("\n");}

  @Ignore("This requires specific test")
  @Test
  public void testSimpleCR() {doTestSimpleText("\r");}

  @Test
  public void testSimpleFormFeed() {doTestSimpleText("\f");}

  @Test
  public void testSimpleBackspace() {doTestSimpleText("\b");}

  @Test
  public void testSimpleAlarm() {doTestSimpleText("" + (char)11);}

  @Test
  public void testSimpleEscape() {doTestSimpleText("" + (char)27);}

  @Test
  public void testSimpleSmile() {doTestSimpleText("😇");}

  @Test
  public void testSimpleScalar() {doTestSimpleText("$scalar");}

  @Test
  public void testSimpleScalarBlock() {doTestSimpleText("${say 42}");}

  @Test
  public void testSimpleArray() {doTestSimpleText("@array");}

  @Test
  public void testWithEncoded() {doTest("A");}

  @Test
  public void testWithWrongCodes() {doTest("A");}

  @Test
  public void testExistingEscapes() {doTest("\\");}

  @Test
  public void testSimpleArrayBlock() {doTestSimpleText("@{say 42}");}

  @Override
  protected boolean withInjections() {
    return true;
  }

  private void doTestSimpleText(@NotNull String textToType) {
    doTest(textToType, "simpleText");
  }

  private void doTest(@NotNull String textToType) {
    initWithFileSmart();
    doTestInjectedTypingWithoutInit(textToType);
  }

  private void doTest(@NotNull String textToType, @NotNull String fileName) {
    initWithFileSmart(fileName);
    doTestInjectedTypingWithoutInit(textToType);
  }

  @Override
  protected String getResultsTestDataPath() {
    return super.getResultsTestDataPath() + "/" + myName;
  }

  @Override
  public void initWithFileContent(String filename, String extension, String content) {
    super.initWithFileContent(filename, extension, myContentModifier.apply(content));
  }

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
      {"StringXQ", (Function<String, String>)content -> "#@inject HTML\n" + "`" + content + "`"},
      {"StringDQ", (Function<String, String>)content -> "#@inject HTML\n" + "\"" + content + "\""},
      {"StringQ", (Function<String, String>)content -> "#@inject HTML\n" + "'" + content + "'"}
    });
  }
}
