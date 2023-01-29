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
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

public class PerlQuoteLikeReparseTest extends PerlReparseMultiTestCase {

  @Override
  protected String getBaseDataPath() {
    return "unit/perl/reparse/quoteLike";
  }

  public PerlQuoteLikeReparseTest(@NotNull String name, @NotNull String codeSample) {
    super(name, codeSample);
  }

  @Test
  public void testNewItem() { doTest("new_item"); }

  @Test
  public void testNewLine() { doTest("\n"); }

  @Test
  public void testDoubleQuote() { doTest("\""); }

  @Test
  public void testScalar() { doTest("$something"); }

  @Test
  public void testScalarCast() { doTest("${say $something}"); }

  @Test
  public void testArray() { doTest("@something"); }

  @Test
  public void testArrayCast() { doTest("@{say $something}"); }

  @Test
  public void testDoubleQuoteEscaped() { doTest("\\\""); }

  @Test
  public void testSlash() { doTest("# comment /"); }

  @Test
  public void testSlashEscaped() { doTest("#comment \\/"); }

  @Test
  public void testSingleQuote() { doTest("'"); }

  @Test
  public void testSingleQuoteEscaped() { doTest("\\'"); }

  @Test
  public void testBackTickQuote() { doTest("# comment `"); }

  @Test
  public void testBackTickQuoteEscaped() { doTest("#comment \\`"); }

  @Test
  public void testBracesOpenBraces() { doTest("# comment {}"); }

  @Test
  public void testBracesOpenBrace() { doTest("# comment {"); }

  @Test
  public void testBracesOpenBraceEscaped() { doTest("# comment \\{"); }

  @Test
  public void testBracesCloseBrace() { doTest("# comment }"); }

  @Test
  public void testBracesCloseBraceEscaped() { doTest("# comment \\}"); }

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
      {"TrReplaceBrace", """
        sub something{
          tr{matc h}{repla<caret>cement 3};
        }
        """},
      {"TrMatchBrace", """
        sub something{
          tr{ma<caret>tc h}<replacement $3>;
        }
        """},
      {"TrReplaceSlash", """
        sub something{
          tr/matc h/r epl<caret>acement/;
        }
        """},
      {"QwQuote", """
        sub something {
          say 'sub start';
        qw"
           item1
           <caret>
           item2
        ";
          say 'sub end';
        }"""},
      {"UseVarsQwSingleQuote", """
        BEGIN {
          say 'begin start';
         use vars qw'
           $item1
           <caret>
           @item2
        ';
          say 'begin end';
        }"""},
      {"BlockInRegexpSlash", """
        sub something{
          s/test/
            say 'hi';
            <caret>
            say 'hi'
          /e;
        }
        """},
      {"BlockInRegexpBrace", """
        sub something{
          s{test}{
            say 'hi';
            <caret>
            say 'hi'
          }e;
        }
        """},
      {"RegexMatchSlash", """
        sub something{
          m/test
            say 'hi';
            <caret>
            say 'hi'
          /;
        }
        """},
      {"RegexMatchXSlash", """
        sub something{
          m/test
            say 'hi';
            <caret>
            say 'hi'
          /x;
        }
        """},
      {"RegexMatchXXSlash", """
        sub something{
          m/test
            say 'hi';
            <caret>
            say 'hi'
          /xx;
        }
        """},
      {"RegexMatchSlashNoOperator", """
        sub something{
          /test
            say 'hi';
            <caret>
            say 'hi'
          /;
        }
        """},
      {"RegexMatchXSlashNoOperator", """
        sub something{
          /test
            say 'hi';
            <caret>
            say 'hi'
          /x;
        }
        """},
      {"RegexMatchXXSlashNoOperator", """
        sub something{
          /test
            say 'hi';
            <caret>
            say 'hi'
          /xx;
        }
        """},
      {"RegexMatchBrace", """
        sub something{
          m{test
            say 'hi';
            <caret>
            say 'hi'
          };
        }
        """},
      {"RegexMatchXBrace", """
        sub something{
          m{test
            say 'hi';
            <caret>
            say 'hi'
          }x;
        }
        """},
      {"RegexMatchXXBrace", """
        sub something{
          m{test
            say 'hi';
            <caret>
            say 'hi'
          }xx;
        }
        """},
      {"RegexCompileSlash", """
        sub something{
          qr/test
            say 'hi';
            <caret>
            say 'hi'
          /;
        }
        """},
      {"RegexCompileXSlash", """
        sub something{
          qr/test
            say 'hi';
            <caret>
            say 'hi'
          /x;
        }
        """},
      {"RegexCompileXXSlash", """
        sub something{
          qr/test
            say 'hi';
            <caret>
            say 'hi'
          /xx;
        }
        """},
      {"RegexCompileBrace", """
        sub something{
          qr{test
            say 'hi';
            <caret>
            say 'hi'
          };
        }
        """},
      {"RegexCompileXBrace", """
        sub something{
          qr{test
            say 'hi';
            <caret>
            say 'hi'
          }x;
        }
        """},
      {"RegexCompileXXBrace", """
        sub something{
          qr{test
            say 'hi';
            <caret>
            say 'hi'
          }xx;
        }
        """},
      {"RegexReplacementSlash", """
        sub something{
          s/test
            say 'hi';
            say 'hi'
          /replace<caret>ment $1/;
        }
        """},
      {"RegexReplacementBrace", """
        sub something{
          s{test
            say 'hi';
            say 'hi'
          }{replace<caret>ment $3};
        }
        """},
      {"RegexReplaceSlash", """
        sub something{
          s/test
            say 'hi';
            <caret>
            say 'hi'
          /replacement $1/;
        }
        """},
      {"RegexReplaceXSlash", """
        sub something{
          s/test
            say 'hi';
            <caret>
            say 'hi'
          /replacement $1/x;
        }
        """},
      {"RegexReplaceXXSlash", """
        sub something{
          s/test
            say 'hi';
            <caret>
            say 'hi'
          /replacement $2/xx;
        }
        """},
      {"RegexReplaceBrace", """
        sub something{
          s{test
            say 'hi';
            <caret>
            say 'hi'
          }<replacement $3>;
        }
        """},
      {"RegexReplaceXBrace", """
        sub something{
          s{test
            say 'hi';
            <caret>
            say 'hi'
          }<replacement $3>x;
        }
        """},
      {"RegexReplaceXXBrace", """
        sub something{
          s{test
            say 'hi';
            <caret>
            say 'hi'
          } | replacement $4|xx;
        }
        """},
      {"StringSQNoOperator", """
        sub something{
          'test  <caret> other';
        }
        """},
      {"StringSQSlash", """
        sub something{
          q/test  <caret> other/;
        }
        """},
      {"StringSQBraces", """
        sub something{
          q{test  <caret> other};
        }
        """},
      {"StringQQNoOperator", """
        sub something{
          "test  <caret> other";
        }
        """},
      {"StringQQSlash", """
        sub something{
          qq/test  <caret> other/;
        }
        """},
      {"StringQQSingleQuote", """
        sub something{
          qq'test  <caret> other';
        }
        """},
      {"StringQQBraces", """
        sub something{
          qq{test  <caret> other};
        }
        """},
      {"StringQXNoOperator", """
        sub something{
          `test  <caret> other`;
        }
        """},
      {"StringQXSlash", """
        sub something{
          qx/test  <caret> other/;
        }
        """},
      {"StringQXSingleQuote", """
        sub something{
          qx'test  <caret> other';
        }
        """},
      {"StringQXBraces", """
        sub something{
          qx{test  <caret> other};
        }
        """},
      {"TrMatchSlash", """
        sub something{
          tr/mat<caret>c h/r eplacement/;
        }
        """}
    });
  }
}
