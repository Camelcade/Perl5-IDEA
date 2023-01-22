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

package unit.perl.reparse;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public abstract class PerlQuoteLikeReparseTestCase extends PerlReparseMultiTestCase {
  @Override
  protected String getBaseDataPath() {
    return "unit/perl/reparse/quoteLike";
  }

  @Test
  public void testNewItem() {doTest("new_item");}

  @Test
  public void testNewLine() {doTest("\n");}

  @Test
  public void testDoubleQuote() {doTest("\"");}

  @Test
  public void testScalar() {doTest("$something");}

  @Test
  public void testScalarCast() {doTest("${say $something}");}

  @Test
  public void testArray() {doTest("@something");}

  @Test
  public void testArrayCast() {doTest("@{say $something}");}

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

      return """
        sub something {
          say 'sub start';
        qw"
           item1
           <caret>
           item2
        ";
          say 'sub end';
        }""";
    }
  }

  public static class UseVarsQwSingleQuote extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {

      return """
        BEGIN {
          say 'begin start';
         use vars qw'
           $item1
           <caret>
           @item2
        ';
          say 'begin end';
        }""";
    }
  }

  public static class BlockInRegexpSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          s/test/
            say 'hi';
            <caret>
            say 'hi'
          /e;
        }
        """;
    }
  }

  public static class BlockInRegexpBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          s{test}{
            say 'hi';
            <caret>
            say 'hi'
          }e;
        }
        """;
    }
  }

  public static class RegexMatchSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          m/test
            say 'hi';
            <caret>
            say 'hi'
          /;
        }
        """;
    }
  }

  public static class RegexMatchXSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          m/test
            say 'hi';
            <caret>
            say 'hi'
          /x;
        }
        """;
    }
  }

  public static class RegexMatchXXSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          m/test
            say 'hi';
            <caret>
            say 'hi'
          /xx;
        }
        """;
    }
  }

  public static class RegexMatchSlashNoOperator extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          /test
            say 'hi';
            <caret>
            say 'hi'
          /;
        }
        """;
    }
  }

  public static class RegexMatchXSlashNoOperator extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          /test
            say 'hi';
            <caret>
            say 'hi'
          /x;
        }
        """;
    }
  }

  public static class RegexMatchXXSlashNoOperator extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          /test
            say 'hi';
            <caret>
            say 'hi'
          /xx;
        }
        """;
    }
  }

  public static class RegexMatchBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          m{test
            say 'hi';
            <caret>
            say 'hi'
          };
        }
        """;
    }
  }

  public static class RegexMatchXBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          m{test
            say 'hi';
            <caret>
            say 'hi'
          }x;
        }
        """;
    }
  }

  public static class RegexMatchXXBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          m{test
            say 'hi';
            <caret>
            say 'hi'
          }xx;
        }
        """;
    }
  }

  public static class RegexCompileSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          qr/test
            say 'hi';
            <caret>
            say 'hi'
          /;
        }
        """;
    }
  }

  public static class RegexCompileXSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          qr/test
            say 'hi';
            <caret>
            say 'hi'
          /x;
        }
        """;
    }
  }

  public static class RegexCompileXXSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          qr/test
            say 'hi';
            <caret>
            say 'hi'
          /xx;
        }
        """;
    }
  }

  public static class RegexCompileBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          qr{test
            say 'hi';
            <caret>
            say 'hi'
          };
        }
        """;
    }
  }

  public static class RegexCompileXBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          qr{test
            say 'hi';
            <caret>
            say 'hi'
          }x;
        }
        """;
    }
  }

  public static class RegexCompileXXBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          qr{test
            say 'hi';
            <caret>
            say 'hi'
          }xx;
        }
        """;
    }
  }

  public static class RegexReplacementSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          s/test
            say 'hi';
            say 'hi'
          /replace<caret>ment $1/;
        }
        """;
    }
  }

  public static class RegexReplacementBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          s{test
            say 'hi';
            say 'hi'
          }{replace<caret>ment $3};
        }
        """;
    }
  }

  public static class RegexReplaceSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          s/test
            say 'hi';
            <caret>
            say 'hi'
          /replacement $1/;
        }
        """;
    }
  }

  public static class RegexReplaceXSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          s/test
            say 'hi';
            <caret>
            say 'hi'
          /replacement $1/x;
        }
        """;
    }
  }

  public static class RegexReplaceXXSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          s/test
            say 'hi';
            <caret>
            say 'hi'
          /replacement $2/xx;
        }
        """;
    }
  }

  public static class RegexReplaceBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          s{test
            say 'hi';
            <caret>
            say 'hi'
          }<replacement $3>;
        }
        """;
    }
  }

  public static class RegexReplaceXBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          s{test
            say 'hi';
            <caret>
            say 'hi'
          }<replacement $3>x;
        }
        """;
    }
  }

  public static class RegexReplaceXXBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          s{test
            say 'hi';
            <caret>
            say 'hi'
          } | replacement $4|xx;
        }
        """;
    }
  }

  public static class StringSQNoOperator extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          'test  <caret> other';
        }
        """;
    }
  }

  public static class StringSQSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          q/test  <caret> other/;
        }
        """;
    }
  }

  public static class StringSQBraces extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          q{test  <caret> other};
        }
        """;
    }
  }

  public static class StringQQNoOperator extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          "test  <caret> other";
        }
        """;
    }
  }

  public static class StringQQSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          qq/test  <caret> other/;
        }
        """;
    }
  }

  public static class StringQQSingleQuote extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          qq'test  <caret> other';
        }
        """;
    }
  }

  public static class StringQQBraces extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          qq{test  <caret> other};
        }
        """;
    }
  }

  public static class StringQXNoOperator extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          `test  <caret> other`;
        }
        """;
    }
  }

  public static class StringQXSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          qx/test  <caret> other/;
        }
        """;
    }
  }

  public static class StringQXSingleQuote extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          qx'test  <caret> other';
        }
        """;
    }
  }

  public static class StringQXBraces extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          qx{test  <caret> other};
        }
        """;
    }
  }

  public static class TrMatchSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          tr/mat<caret>c h/r eplacement/;
        }
        """;
    }
  }

  public static class TrReplaceSlash extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          tr/matc h/r epl<caret>acement/;
        }
        """;
    }
  }

  public static class TrMatchBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          tr{ma<caret>tc h}<replacement $3>;
        }
        """;
    }
  }

  public static class TrReplaceBrace extends PerlQuoteLikeReparseTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        sub something{
          tr{matc h}{repla<caret>cement 3};
        }
        """;
    }
  }
}
