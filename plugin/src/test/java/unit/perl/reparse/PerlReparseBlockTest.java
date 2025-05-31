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

package unit.perl.reparse;

import categories.Heavy;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@Category(Heavy.class)
public class PerlReparseBlockTest extends PerlReparseMultiTestCase {
  @Override
  protected String getBaseDataPath() {
    return "unit/perl/reparse/block";
  }

  @Test
  public void testNumber() { doTest("42"); }

  @Test
  public void testOpenBrace() { doTest("{"); }

  @Test
  public void testCloseBrace() { doTest("}"); }

  @Test
  public void testOpenBraceQuoted() { doTest("'{'"); }

  @Test
  public void testCloseBraceQuoted() { doTest("'}'"); }

  @Test
  public void testBalancedBraces() { doTest("{}"); }

  @Test
  public void testOpenBracket() { doTest("["); }

  @Test
  public void testCloseBracket() { doTest("]"); }

  @Test
  public void testOpenBracketQuoted() { doTest("'['"); }

  @Test
  public void testCloseBracketQuoted() { doTest("']'"); }

  @Test
  public void testBalancedBrackets() { doTest("[]"); }

  @Test
  public void testOpenParen() { doTest("("); }

  @Test
  public void testCloseParen() { doTest(")"); }

  @Test
  public void testOpenParenQuoted() { doTest("'('"); }

  @Test
  public void testCloseParenQuoted() { doTest("')'"); }

  @Test
  public void testBalancedParens() { doTest("()"); }

  @Test
  public void testOpenQuote() { doTest("'"); }

  @Test
  public void testQuotes() { doTest("''"); }

  @Test
  public void testOpenHeredoc() { doTest("<<EOM"); }

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
      {"CodeBlock", """
        if($a){
          say 'hi';
          my $test = sub {
            say 'sub';
            some => <caret>;
            say 'sub end';
          };
        }"""}, {"AnonHash", """
        if($a){
          say 'hi';
          my $test = {
            say => 'start',
            some => <caret>,
            say =>'end'
          };
        }"""}, {"HashIndex", """
        if($a){
          say 'hi';
          $test{
            say => 'start',
            some => <caret>,
            say =>'end'
          };
        }"""}, {"HashIndexDeref", """
        if($a){
          say 'hi';
          say $test->{
            say => 'start',
            some => <caret>,
            say =>'end'
          };
        }"""}, {"AnonArray", """
        if($a){
          say 'hi';
          my $test = [
            say => 'start',
            some => <caret>,
            say =>'end'
          ];
        }"""}, {"ArrayIndex", """
        if($a){
          say 'hi';
          say $test[
            say => 'start',
            some => <caret>,
            say =>'end'
          ];
        }"""}, {"ArrayIndexDeref", """
        if($a){
          say 'hi';
          say $test->[
            say => 'start',
            some => <caret>,
            say =>'end'
          ];
        }"""}, {"ParenthesizedList", """
        if($a){
          say 'hi';
          my @test = (
            say => 'start',
            some => <caret>,
            say =>'end'
          );
        }"""}, {"ParenthesizedCall", """
        if($a){
          say 'hi';
          test(
            say => 'start',
            some => <caret>,
            say =>'end'
          );
        }"""}, {"ForeachIterator", """
        if($a){
          foreach(@something + <caret> ){
            say 'hi'
          }
        }
        """}, {"ForeachIteratorModifier", """
        if($a){
          say 'hi' foreach(@something + <caret> );
        }
        """}, {"IfCondition", """
        if($a){
          if( $a &&  <caret> ){
            say 'hi'
          }
        }
        """}, {"WhileCondition", """
        if($a){
          while( $a &&  <caret> ){
            say 'hi'
          }
        }
        """}, {"UntilCondition", """
        if($a){
          until( $a &&  <caret> ){
            say 'hi'
          }
        }
        """}, {"IfConditionModifier", """
        if($a){
          say 'hi' if( $a &&  <caret> );
        }
        """}, {"UnlessCondition", """
        if($a){
          unless( $a &&  <caret> ){
            say 'hi'
          }
        }
        """}, {"ElseifCondition", """
        if($a){
          if($b ){}\s
          elsif( $a &&  <caret> ){
            say 'hi'
          }
        }
        """}, {"ForIterator", """
        if($a){
          for(my $i = 0; i < 10; <caret> ){
            say 'hi'
          }
        }
        """}, {"ScalarDeref", """
        if($a){
          say 'hi';
          ${
            say => 'start';
            some => <caret>;
            say =>'end';
          };
        }"""}, {"ArrayDeref", """
        if($a){
          say 'hi';
          @{
            say => 'start';
            some => <caret>;
            say =>'end';
          };
        }"""}, {"ScalarDerefInHeredoc", """
        if($a){
          say 'hi';
          <<EOM;
        text  ${
            say => 'start';
            some => <caret>;
            say =>'end';
          }; text
        EOM
        }"""}, {"ArrayDerefInHeredoc", """
        if($a){
          say 'hi';
          <<EOM;
          text @{
            say => 'start';
            some => <caret>;
            say =>'end';
          }; text
        EOM
        }"""}, {"ScalarDerefInString", """
        if($a){
          say 'hi';
        say "text  ${
            say => 'start';
            some => <caret>;
            say =>'end';
          }; text"
        }"""}, {"ArrayDerefInString", """
        if($a){
          say 'hi';
        say "  text @{
            say => 'start';
            some => <caret>;
            say =>'end';
          }; text"
        }"""}, {"HashDeref", """
        if($a){
          say 'hi';
          %{
            say => 'start';
            some => <caret>;
            say =>'end';
          };
        }"""}, {"CodeDeref", """
        if($a){
          say 'hi';
          &{
            say => 'start';
            some => <caret>;
            say =>'end';
          };
        }"""}, {"GlobDeref", """
        if($a){
          say 'hi';
          *{
            say => 'start';
            some => <caret>;
            say =>'end';
          };
        }"""}, {"SubSignature", """
        if($a){
          sub something($var,<caret>){
            say 'hi'
          }
        }
        """}, {"SubSignatureAnon", """
        if($a){
          sub ($var,<caret>){
            say 'hi'
          }
        }
        """}, {"MethodSignature", """
        if($a){
          method something($var,<caret>){
            say 'hi'
          }
        }
        """}, {"FuncSignature", """
        if($a){
          func something($var,<caret>){
            say 'hi'
          }
        }
        """}, {"FunSignature", """
        if($a){
          func something($var,<caret>){
            say 'hi'
          }
        }
        """}, {"AroundSignature", """
        if($a){
          around something($var,<caret>){
            say 'hi'
          }
        }
        """}, {"AfterSignature", """
        if($a){
          around something($var,<caret>){
            say 'hi'
          }
        }
        """}, {"BeforeSignature", """
        if($a){
          around something($var,<caret>){
            say 'hi'
          }
        }
        """}, {"AugmentSignature", """
        if($a){
          augment something($var,<caret>){
            say 'hi'
          }
        }
        """}, {"OverrideSignature", """
        if($a){
          override something($var,<caret>){
            say 'hi'
          }
        }
        """}, {"ParenthesizedExpression", """
        if($a){
          my $test = (2 + <caret>);
        }
        """}});
  }
}
