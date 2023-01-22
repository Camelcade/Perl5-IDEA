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

public abstract class PerlReparseBlockTestCase extends PerlReparseMultiTestCase {
  @Override
  protected String getBaseDataPath() {
    return "unit/perl/reparse/block";
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
      return """
        if($a){
          say 'hi';
          my $test = sub {
            say 'sub';
            some => <caret>;
            say 'sub end';
          };
        }""";
    }
  }

  public static class AnonHash extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          say 'hi';
          my $test = {
            say => 'start',
            some => <caret>,
            say =>'end'
          };
        }""";
    }
  }

  public static class HashIndex extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          say 'hi';
          $test{
            say => 'start',
            some => <caret>,
            say =>'end'
          };
        }""";
    }
  }

  public static class HashIndexDeref extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          say 'hi';
          say $test->{
            say => 'start',
            some => <caret>,
            say =>'end'
          };
        }""";
    }
  }

  public static class AnonArray extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          say 'hi';
          my $test = [
            say => 'start',
            some => <caret>,
            say =>'end'
          ];
        }""";
    }
  }

  public static class ArrayIndex extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          say 'hi';
          say $test[
            say => 'start',
            some => <caret>,
            say =>'end'
          ];
        }""";
    }
  }

  public static class ArrayIndexDeref extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          say 'hi';
          say $test->[
            say => 'start',
            some => <caret>,
            say =>'end'
          ];
        }""";
    }
  }

  public static class ParenthesizedList extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          say 'hi';
          my @test = (
            say => 'start',
            some => <caret>,
            say =>'end'
          );
        }""";
    }
  }
  public static class ParenthesizedCall extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          say 'hi';
          test(
            say => 'start',
            some => <caret>,
            say =>'end'
          );
        }""";
    }
  }

  public static class ForeachIterator extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          foreach(@something + <caret> ){
            say 'hi'
          }
        }
        """;
    }
  }

  public static class ForeachIteratorModifier extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          say 'hi' foreach(@something + <caret> );
        }
        """;
    }
  }

  public static class IfCondition extends PerlReparseBlockTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          if( $a &&  <caret> ){
            say 'hi'
          }
        }
        """;
    }
  }

  public static class WhileCondition extends PerlReparseBlockTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          while( $a &&  <caret> ){
            say 'hi'
          }
        }
        """;
    }
  }

  public static class UntilCondition extends PerlReparseBlockTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          until( $a &&  <caret> ){
            say 'hi'
          }
        }
        """;
    }
  }

  public static class IfConditionModifier extends PerlReparseBlockTestCase {
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          say 'hi' if( $a &&  <caret> );
        }
        """;
    }
  }

  public static class UnlessCondition extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          unless( $a &&  <caret> ){
            say 'hi'
          }
        }
        """;
    }
  }

  public static class ElseifCondition extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          if($b ){}\s
          elsif( $a &&  <caret> ){
            say 'hi'
          }
        }
        """;
    }
  }

  public static class ForIterator extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          for(my $i = 0; i < 10; <caret> ){
            say 'hi'
          }
        }
        """;
    }
  }

  public static class ScalarDeref extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          say 'hi';
          ${
            say => 'start';
            some => <caret>;
            say =>'end';
          };
        }""";
    }
  }

  public static class ArrayDeref extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          say 'hi';
          @{
            say => 'start';
            some => <caret>;
            say =>'end';
          };
        }""";
    }
  }

  public static class ScalarDerefInHeredoc extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          say 'hi';
          <<EOM;
        text  ${
            say => 'start';
            some => <caret>;
            say =>'end';
          }; text
        EOM
        }""";
    }
  }

  public static class ArrayDerefInHeredoc extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          say 'hi';
          <<EOM;
          text @{
            say => 'start';
            some => <caret>;
            say =>'end';
          }; text
        EOM
        }""";
    }
  }

  public static class ScalarDerefInString extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          say 'hi';
        say "text  ${
            say => 'start';
            some => <caret>;
            say =>'end';
          }; text"
        }""";
    }
  }

  public static class ArrayDerefInString extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          say 'hi';
        say "  text @{
            say => 'start';
            some => <caret>;
            say =>'end';
          }; text"
        }""";
    }
  }

  public static class HashDeref extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          say 'hi';
          %{
            say => 'start';
            some => <caret>;
            say =>'end';
          };
        }""";
    }
  }

  public static class CodeDeref extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          say 'hi';
          &{
            say => 'start';
            some => <caret>;
            say =>'end';
          };
        }""";
    }
  }

  public static class GlobDeref extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          say 'hi';
          *{
            say => 'start';
            some => <caret>;
            say =>'end';
          };
        }""";
    }
  }

  public static class SubSignature extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          sub something($var,<caret>){
            say 'hi'
          }
        }
        """;
    }
  }

  public static class SubSignatureAnon extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          sub ($var,<caret>){
            say 'hi'
          }
        }
        """;
    }
  }

  public static class MethodSignature extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          method something($var,<caret>){
            say 'hi'
          }
        }
        """;
    }
  }

  public static class FuncSignature extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          func something($var,<caret>){
            say 'hi'
          }
        }
        """;
    }
  }

  public static class FunSignature extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          func something($var,<caret>){
            say 'hi'
          }
        }
        """;
    }
  }

  public static class AroundSignature extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          around something($var,<caret>){
            say 'hi'
          }
        }
        """;
    }
  }

  public static class AfterSignature extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          around something($var,<caret>){
            say 'hi'
          }
        }
        """;
    }
  }

  public static class BeforeSignature extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          around something($var,<caret>){
            say 'hi'
          }
        }
        """;
    }
  }

  public static class AugmentSignature extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          augment something($var,<caret>){
            say 'hi'
          }
        }
        """;
    }
  }

  public static class OverrideSignature extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          override something($var,<caret>){
            say 'hi'
          }
        }
        """;
    }
  }

  public static class ParenthesizedExpression extends PerlReparseBlockTestCase{
    @Override
    protected @NotNull String buildCodeSample() {
      return """
        if($a){
          my $test = (2 + <caret>);
        }
        """;
    }
  }
}
