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

public abstract class PerlQuoteLikeReparseTestCase extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/unit/perl/reparseQuoteLike";
  }

  @Test
  public void testNewItem() {doTest("new_item");}

  @Test
  public void testDoubleQuote() {doTest("\"");}

  @Test
  public void testDoubleQuoteEscaped() {doTest("\\\"");}

  @Test
  public void testSlash() {doTest("# comment /");}

  @Test
  public void testSlashEscaped() {doTest("#comment \\/");}

  @Test
  public void testSingleQuote() {doTest("'");}

  @Test
  public void testSingleQuoteEscaped() {doTest("\\'");}

  @Test
  public void testBracesOpenBraces() {doTest("# comment {}");}

  @Test
  public void testBracesOpenBrace() {doTest("# comment {");}

  @Test
  public void testBracesOpenBraceEscaped() {doTest("# comment \\{");}

  @Test
  public void testBracesCloseBrace() {doTest("# comment }");}

  @Test
  public void testBracesCloseBraceEscaped() {doTest("# comment \\}");}


  private void doTest(@NotNull String textToInsert) {
    initWithTextSmartWithoutErrors(buildCodeSample());
    doTestReparseWithoutInit(textToInsert);
  }

  protected abstract @NotNull String buildCodeSample();

  @Override
  protected @NotNull String computeAnswerFileName(@NotNull String appendix) {
    return super.computeAnswerFileName(getClass().getSimpleName());
  }

  public static class QwQuote extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {

      return "sub something {\n" +
             "  say 'sub start';\n" +
             "qw\"\n" +
             "   item1\n" +
             "   <caret>\n" +
             "   item2\n" +
             "\";\n" +
             "  say 'sub end';\n" +
             "}";
    }
  }

  public static class UseVarsQwSingleQuote extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {

      return "BEGIN {\n" +
             "  say 'begin start';\n" +
             " use vars qw'\n" +
             "   $item1\n" +
             "   <caret>\n" +
             "   @item2\n" +
             "';\n" +
             "  say 'begin end';\n" +
             "}";
    }
  }

  public static class BlockInRegexpSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  s/test/\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  /e;\n" +
             "}\n";
    }
  }

  public static class BlockInRegexpBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  s{test}{\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  }e;\n" +
             "}\n";
    }
  }
}
