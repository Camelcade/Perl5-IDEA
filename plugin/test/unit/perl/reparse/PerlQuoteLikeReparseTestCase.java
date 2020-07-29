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

package unit.perl.reparse;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public abstract class PerlQuoteLikeReparseTestCase extends PerlReparseMultiTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/unit/perl/reparseQuoteLike";
  }

  @Test
  public void testNewItem() {doTest("new_item");}

  @Test
  public void testDoubleQuote() {doTest("\"");}

  @Test
  public void testScalar() {doTest("$something");}

  @Test
  public void testScalarCast() {doTest("${say $something}");}

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
  public void testBackTickQuote() {doTest("# comment `");}

  @Test
  public void testBackTickQuoteEscaped() {doTest("#comment \\`");}

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

  public static class RegexMatchSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  m/test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  /;\n" +
             "}\n";
    }
  }

  public static class RegexMatchXSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  m/test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  /x;\n" +
             "}\n";
    }
  }

  public static class RegexMatchXXSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  m/test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  /xx;\n" +
             "}\n";
    }
  }

  public static class RegexMatchSlashNoOperator extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  /test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  /;\n" +
             "}\n";
    }
  }

  public static class RegexMatchXSlashNoOperator extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  /test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  /x;\n" +
             "}\n";
    }
  }

  public static class RegexMatchXXSlashNoOperator extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  /test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  /xx;\n" +
             "}\n";
    }
  }

  public static class RegexMatchBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  m{test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  };\n" +
             "}\n";
    }
  }

  public static class RegexMatchXBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  m{test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  }x;\n" +
             "}\n";
    }
  }

  public static class RegexMatchXXBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  m{test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  }xx;\n" +
             "}\n";
    }
  }

  public static class RegexCompileSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  qr/test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  /;\n" +
             "}\n";
    }
  }

  public static class RegexCompileXSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  qr/test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  /x;\n" +
             "}\n";
    }
  }

  public static class RegexCompileXXSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  qr/test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  /xx;\n" +
             "}\n";
    }
  }

  public static class RegexCompileBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  qr{test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  };\n" +
             "}\n";
    }
  }

  public static class RegexCompileXBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  qr{test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  }x;\n" +
             "}\n";
    }
  }

  public static class RegexCompileXXBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  qr{test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  }xx;\n" +
             "}\n";
    }
  }

  public static class RegexReplacementSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  s/test\n" +
             "    say 'hi';\n" +
             "    say 'hi'\n" +
             "  /replace<caret>ment $1/;\n" +
             "}\n";
    }
  }

  public static class RegexReplacementBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  s{test\n" +
             "    say 'hi';\n" +
             "    say 'hi'\n" +
             "  }{replace<caret>ment $3};\n" +
             "}\n";
    }
  }

  public static class RegexReplaceSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  s/test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  /replacement $1/;\n" +
             "}\n";
    }
  }

  public static class RegexReplaceXSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  s/test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  /replacement $1/x;\n" +
             "}\n";
    }
  }

  public static class RegexReplaceXXSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  s/test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  /replacement $2/xx;\n" +
             "}\n";
    }
  }

  public static class RegexReplaceBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  s{test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  }<replacement $3>;\n" +
             "}\n";
    }
  }

  public static class RegexReplaceXBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  s{test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  }<replacement $3>x;\n" +
             "}\n";
    }
  }

  public static class RegexReplaceXXBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  s{test\n" +
             "    say 'hi';\n" +
             "    <caret>\n" +
             "    say 'hi'\n" +
             "  } | replacement $4|xx;\n" +
             "}\n";
    }
  }

  public static class StringSQNoOperator extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  'test  <caret> other';\n" +
             "}\n";
    }
  }

  public static class StringSQSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  q/test  <caret> other/;\n" +
             "}\n";
    }
  }

  public static class StringSQBraces extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  q{test  <caret> other};\n" +
             "}\n";
    }
  }

  public static class StringQQNoOperator extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  \"test  <caret> other\";\n" +
             "}\n";
    }
  }

  public static class StringQQSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  qq/test  <caret> other/;\n" +
             "}\n";
    }
  }

  public static class StringQQSingleQuote extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  qq'test  <caret> other';\n" +
             "}\n";
    }
  }

  public static class StringQQBraces extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  qq{test  <caret> other};\n" +
             "}\n";
    }
  }

  public static class StringQXNoOperator extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  `test  <caret> other`;\n" +
             "}\n";
    }
  }

  public static class StringQXSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  qx/test  <caret> other/;\n" +
             "}\n";
    }
  }

  public static class StringQXSingleQuote extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  qx'test  <caret> other';\n" +
             "}\n";
    }
  }

  public static class StringQXBraces extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "sub something{\n" +
             "  qx{test  <caret> other};\n" +
             "}\n";
    }
  }
}
