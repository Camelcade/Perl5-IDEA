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

public abstract class PerlReparseBlockTestCase extends PerlReparseMultiTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/unit/perl/reparseBlock";
  }

  @Test
  public void testNumber() {doTest("42");}

  @Test
  public void testOpenBrace() {doTest("{");}

  @Test
  public void testCloseBrace() {doTest("}");}

  @Test
  public void testOpenBraceQuoted() {doTest("'{'");}

  @Test
  public void testCloseBraceQuoted() {doTest("'}'");}

  @Test
  public void testBalancedBraces() {doTest("{}");}

  @Test
  public void testOpenBracket() {doTest("[");}

  @Test
  public void testCloseBracket() {doTest("]");}

  @Test
  public void testOpenBracketQuoted() {doTest("'['");}

  @Test
  public void testCloseBracketQuoted() {doTest("']'");}

  @Test
  public void testBalancedBrackets() {doTest("[]");}

  @Test
  public void testOpenParen() {doTest("(");}

  @Test
  public void testCloseParen() {doTest(")");}

  @Test
  public void testOpenParenQuoted() {doTest("'('");}

  @Test
  public void testCloseParenQuoted() {doTest("')'");}

  @Test
  public void testBalancedParens() {doTest("()");}

  @Test
  public void testOpenQuote() {doTest("'");}

  @Test
  public void testQuotes() {doTest("''");}

  @Test
  public void testOpenHeredoc() {doTest("<<EOM");}

  public static class CodeBlock extends PerlReparseBlockTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi';\n" +
             "  my $test = sub {\n" +
             "    say 'sub';\n" +
             "    some => <caret>;\n" +
             "    say 'sub end';\n" +
             "  };\n" +
             "}";
    }
  }

  public static class AnonHash extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi';\n" +
             "  my $test = {\n" +
             "    say => 'start',\n" +
             "    some => <caret>,\n" +
             "    say =>'end'\n" +
             "  };\n" +
             "}";
    }
  }

  public static class HashIndex extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi';\n" +
             "  $test{\n" +
             "    say => 'start',\n" +
             "    some => <caret>,\n" +
             "    say =>'end'\n" +
             "  };\n" +
             "}";
    }
  }

  public static class HashIndexDeref extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi';\n" +
             "  say $test->{\n" +
             "    say => 'start',\n" +
             "    some => <caret>,\n" +
             "    say =>'end'\n" +
             "  };\n" +
             "}";
    }
  }

  public static class AnonArray extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi';\n" +
             "  my $test = [\n" +
             "    say => 'start',\n" +
             "    some => <caret>,\n" +
             "    say =>'end'\n" +
             "  ];\n" +
             "}";
    }
  }

  public static class ArrayIndex extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi';\n" +
             "  say $test[\n" +
             "    say => 'start',\n" +
             "    some => <caret>,\n" +
             "    say =>'end'\n" +
             "  ];\n" +
             "}";
    }
  }

  public static class ArrayIndexDeref extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi';\n" +
             "  say $test->[\n" +
             "    say => 'start',\n" +
             "    some => <caret>,\n" +
             "    say =>'end'\n" +
             "  ];\n" +
             "}";
    }
  }

  public static class ParenthesizedList extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi';\n" +
             "  my @test = (\n" +
             "    say => 'start',\n" +
             "    some => <caret>,\n" +
             "    say =>'end'\n" +
             "  );\n" +
             "}";
    }
  }
  public static class ParenthesizedCall extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi';\n" +
             "  test(\n" +
             "    say => 'start',\n" +
             "    some => <caret>,\n" +
             "    say =>'end'\n" +
             "  );\n" +
             "}";
    }
  }

  public static class ForeachIterator extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  foreach(@something + <caret> ){\n" +
             "    say 'hi'\n" +
             "  }\n" +
             "}\n";
    }
  }

  public static class ForeachIteratorModifier extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi' foreach(@something + <caret> );\n" +
             "}\n";
    }
  }

  public static class IfCondition extends PerlReparseBlockTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  if( $a &&  <caret> ){\n" +
             "    say 'hi'\n" +
             "  }\n" +
             "}\n";
    }
  }

  public static class WhileCondition extends PerlReparseBlockTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  while( $a &&  <caret> ){\n" +
             "    say 'hi'\n" +
             "  }\n" +
             "}\n";
    }
  }

  public static class UntilCondition extends PerlReparseBlockTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  until( $a &&  <caret> ){\n" +
             "    say 'hi'\n" +
             "  }\n" +
             "}\n";
    }
  }

  public static class IfConditionModifier extends PerlReparseBlockTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi' if( $a &&  <caret> );\n" +
             "}\n";
    }
  }

  public static class UnlessCondition extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  unless( $a &&  <caret> ){\n" +
             "    say 'hi'\n" +
             "  }\n" +
             "}\n";
    }
  }

  public static class ElseifCondition extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  if($b ){} \n" +
             "  elsif( $a &&  <caret> ){\n" +
             "    say 'hi'\n" +
             "  }\n" +
             "}\n";
    }
  }

  public static class ForIterator extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  for(my $i = 0; i < 10; <caret> ){\n" +
             "    say 'hi'\n" +
             "  }\n" +
             "}\n";
    }
  }

  public static class ScalarDeref extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi';\n" +
             "  ${\n" +
             "    say => 'start';\n" +
             "    some => <caret>;\n" +
             "    say =>'end';\n" +
             "  };\n" +
             "}";
    }
  }

  public static class ArrayDeref extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi';\n" +
             "  @{\n" +
             "    say => 'start';\n" +
             "    some => <caret>;\n" +
             "    say =>'end';\n" +
             "  };\n" +
             "}";
    }
  }

  public static class ScalarDerefInHeredoc extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi';\n" +
             "  <<EOM;\n" +
             "text  ${\n" +
             "    say => 'start';\n" +
             "    some => <caret>;\n" +
             "    say =>'end';\n" +
             "  }; text\n" +
             "EOM\n" +
             "}";
    }
  }

  public static class ArrayDerefInHeredoc extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi';\n" +
             "  <<EOM;\n" +
             "  text @{\n" +
             "    say => 'start';\n" +
             "    some => <caret>;\n" +
             "    say =>'end';\n" +
             "  }; text\n" +
             "EOM\n" +
             "}";
    }
  }

  public static class ScalarDerefInString extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi';\n" +
             "say \"text  ${\n" +
             "    say => 'start';\n" +
             "    some => <caret>;\n" +
             "    say =>'end';\n" +
             "  }; text\"\n" +
             "}";
    }
  }

  public static class ArrayDerefInString extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi';\n" +
             "say \"  text @{\n" +
             "    say => 'start';\n" +
             "    some => <caret>;\n" +
             "    say =>'end';\n" +
             "  }; text\"\n" +
             "}";
    }
  }

  public static class HashDeref extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi';\n" +
             "  %{\n" +
             "    say => 'start';\n" +
             "    some => <caret>;\n" +
             "    say =>'end';\n" +
             "  };\n" +
             "}";
    }
  }

  public static class CodeDeref extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi';\n" +
             "  &{\n" +
             "    say => 'start';\n" +
             "    some => <caret>;\n" +
             "    say =>'end';\n" +
             "  };\n" +
             "}";
    }
  }

  public static class GlobDeref extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  say 'hi';\n" +
             "  *{\n" +
             "    say => 'start';\n" +
             "    some => <caret>;\n" +
             "    say =>'end';\n" +
             "  };\n" +
             "}";
    }
  }

  public static class SubSignature extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  sub something($var,<caret>){\n" +
             "    say 'hi'\n" +
             "  }\n" +
             "}\n";
    }
  }

  public static class SubSignatureAnon extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  sub ($var,<caret>){\n" +
             "    say 'hi'\n" +
             "  }\n" +
             "}\n";
    }
  }

  public static class MethodSignature extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  method something($var,<caret>){\n" +
             "    say 'hi'\n" +
             "  }\n" +
             "}\n";
    }
  }

  public static class FuncSignature extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  func something($var,<caret>){\n" +
             "    say 'hi'\n" +
             "  }\n" +
             "}\n";
    }
  }

  public static class FunSignature extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  func something($var,<caret>){\n" +
             "    say 'hi'\n" +
             "  }\n" +
             "}\n";
    }
  }

  public static class AroundSignature extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  around something($var,<caret>){\n" +
             "    say 'hi'\n" +
             "  }\n" +
             "}\n";
    }
  }

  public static class AfterSignature extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  around something($var,<caret>){\n" +
             "    say 'hi'\n" +
             "  }\n" +
             "}\n";
    }
  }

  public static class BeforeSignature extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  around something($var,<caret>){\n" +
             "    say 'hi'\n" +
             "  }\n" +
             "}\n";
    }
  }

  public static class AugmentSignature extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  augment something($var,<caret>){\n" +
             "    say 'hi'\n" +
             "  }\n" +
             "}\n";
    }
  }

  public static class OverrideSignature extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  override something($var,<caret>){\n" +
             "    say 'hi'\n" +
             "  }\n" +
             "}\n";
    }
  }

  public static class ParenthesizedExpression extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return "{\n" +
             "  my $test = (2 + <caret>);\n" +
             "}\n";
    }
  }
}
