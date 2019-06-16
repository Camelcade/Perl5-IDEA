/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package smartKeys.quoteLike;

import org.junit.Test;

/**
 * Test for single-section operators: q/qw/qq/qx/m/qr
 */
public abstract class PerlQuoteLikeSingleTestCase extends PerlQuoteLikeTestCase {

  @Test
  public void testParenInParen() {doTestWithBS("$a = (" + OP + "<caret>);", "(", "$a = (" + OP + "(<caret>));");}

  @Test
  public void testBracketInBracket() {doTestWithBS("$a = [" + OP + "<caret>];", "[", "$a = [" + OP + "[<caret>]];");}

  @Test
  public void testBraceInBrace() {doTestWithBS("$a = {" + OP + "<caret>};", "{", "$a = {" + OP + "{<caret>}};");}

  @Test
  public void testIssue1791() {doTestWithBS("foreach (" + OP + "<caret>){}", "(", "foreach (" + OP + "(<caret>)){}");}

  @Test
  public void testParenInParenSpaced() {doTestWithBS("$a = ( " + OP + " <caret> );", "(", "$a = ( " + OP + " (<caret>) );");}

  @Test
  public void testBracketInBracketSpaced() {doTestWithBS("$a = [ " + OP + " <caret> ];", "[", "$a = [ " + OP + " [<caret>] ];");}

  @Test
  public void testBraceInBraceSpaced() {doTestWithBS("$a = { " + OP + " <caret> };", "{", "$a = { " + OP + " {<caret>} };");}

  @Test
  public void testIssue1791Spaced() {doTestWithBS("foreach ( " + OP + " <caret> ){}", "(", "foreach ( " + OP + " (<caret>) ){}");}

  @Test
  public void testPaired() {doTest(OP + "<caret>", "<", OP + "<<caret>>");}

  @Test
  public void testPairedWithSpace() {doTest(OP + " <caret>", "{", OP + " {<caret>}");}

  @Test
  public void testUnpaired() {doTest(OP + "<caret>", "|", OP + "|<caret>|");}

  @Test
  public void testUnpairedWithSpace() {doTest(OP + " <caret>", ",", OP + " ,<caret>,");}

  @Test
  public void testSharp() {doTest(OP + "<caret>", "#", OP + "#<caret>#");}

  @Test
  public void testSharpWithSpace() {doTest(OP + " <caret>", "#", OP + " #<caret>");}

  @Test
  public void testLetter() {doTest(OP + "<caret>", "a", OP + "a<caret>");}

  @Test
  public void testLetterWithSpace() {doTest(OP + " <caret>", "a", OP + " a<caret>a");}

  @Test
  public void testAmbiguousLetter() {doTest(OP + "<caret>", "w", OP + "w<caret>");}

  @Test
  public void testAmbiguousLetterWithSpace() {doTest(OP + " <caret>", "w", OP + " w<caret>w");}

  @Test
  public void testQuoteSingle() {doTest(OP + "<caret>", "'", OP + "'<caret>'");}

  @Test
  public void testQuoteSingleWithSpace() {doTest(OP + " <caret>", "'", OP + " '<caret>'");}

  @Test
  public void testQuoteDouble() {doTest(OP + "<caret>", "\"", OP + "\"<caret>\"");}

  @Test
  public void testQuoteDoubleWithSpace() {doTest(OP + " <caret>", "\"", OP + " \"<caret>\"");}

  @Test
  public void testQuoteTick() {doTest(OP + "<caret>", "`", OP + "`<caret>`");}

  @Test
  public void testQuoteTickWithSpace() {doTest(OP + " <caret>", "`", OP + " `<caret>`");}

  @Test
  public void testParen() {doTest(OP + "<caret>", "(", OP + "(<caret>)");}

  @Test
  public void testParenWithSpace() {doTest(OP + " <caret>", "(", OP + " (<caret>)");}

  @Test
  public void testBracket() {doTest(OP + "<caret>", "[", OP + "[<caret>]");}

  @Test
  public void testBracketWithSpace() {doTest(OP + " <caret>", "[", OP + " [<caret>]");}

  @Test
  public void testBrace() {doTest(OP + "<caret>", "{", OP + "{<caret>}");}

  @Test
  public void testBraceWithSpace() {doTest(OP + " <caret>", "{", OP + " {<caret>}");}

  @Test
  public void testClosedPaired() {doTest(OP + "<caret>>", "<", OP + "<<caret>>");}

  @Test
  public void testClosedPairedWithSpace() {doTest(OP + " <caret>>", "<", OP + " <<caret>>");}

  @Test
  public void testClosedUnPaired() {doTest(OP + "<caret>|", "|", OP + "|<caret>|");}

  @Test
  public void testClosedUnPairedWithSpace() {doTest(OP + " <caret>|", "|", OP + " |<caret>|");}

  @Test
  public void testClosedSQ() {doTest(OP + "<caret>'", "'", OP + "'<caret>'");}

  @Test
  public void testClosedSQWithSpace() {doTest(OP + " <caret>'", "'", OP + " '<caret>'");}

  @Test
  public void testClosedDQ() {doTest(OP + "<caret>\"", "\"", OP + "\"<caret>\"");}

  @Test
  public void testClosedDQWithSpace() {doTest(OP + " <caret>\"", "\"", OP + " \"<caret>\"");}

  @Test
  public void testClosedXQ() {doTest(OP + "<caret>`", "`", OP + "`<caret>`");}

  @Test
  public void testClosedXQWithSpace() {doTest(OP + " <caret>`", "`", OP + " `<caret>`");}

  @Test
  public void testPairedSwapped() {doTest(OP + "<caret>", ">", OP + "><caret>>");}

  @Test
  public void testRemovingPaired() {doTestBS(OP + "<<caret>>", OP + "<caret>");}

  @Test
  public void testRemovingPairedWithSpace() {doTestBS(OP + " <<caret>>", OP + " <caret>");}

  @Test
  public void testRemovingUnPaired() {doTestBS(OP + "|<caret>|", OP + "<caret>");}

  @Test
  public void testRemovingUnPairedWithSpace() {doTestBS(OP + " |<caret>|", OP + " <caret>");}

  @Test
  public void testRemovingSQ() {doTestBS(OP + "'<caret>'", OP + "<caret>");}

  @Test
  public void testRemovingSQWithSpace() {doTestBS(OP + " '<caret>'", OP + " <caret>");}

  @Test
  public void testRemovingDQ() {doTestBS(OP + "\"<caret>\"", OP + "<caret>");}

  @Test
  public void testRemovingDQWithSpace() {doTestBS(OP + " \"<caret>\"", OP + " <caret>");}

  @Test
  public void testRemovingXQ() {doTestBS(OP + "`<caret>`", OP + "<caret>");}

  @Test
  public void testRemovingXQWithSpace() {doTestBS(OP + " `<caret>`", OP + " <caret>");}

  @Test
  public void testRemovingParen() {doTestBS(OP + "(<caret>)", OP + "<caret>");}

  @Test
  public void testRemovingParenWithSpace() {doTestBS(OP + " (<caret>)", OP + " <caret>");}

  @Test
  public void testRemovingBracket() {doTestBS(OP + "[<caret>]", OP + "<caret>");}

  @Test
  public void testRemovingBracketWithSpace() {doTestBS(OP + " [<caret>]", OP + " <caret>");}

  @Test
  public void testRemovingBrace() {doTestBS(OP + "{<caret>}", OP + "<caret>");}

  @Test
  public void testRemovingBraceWithSpace() {doTestBS(OP + " {<caret>}", OP + " <caret>");}

  @Test
  public void testDoubleClosePaired() {doTest(OP + "<<caret>>", ">", OP + "<><caret>");}

  @Test
  public void testDoubleCloseUnPaired() {doTest(OP + "|<caret>|", "|", OP + "||<caret>");}

  @Test
  public void testDoubleCloseSQ() {doTest(OP + "'<caret>'", "'", OP + "''<caret>");}

  @Test
  public void testDoubleCloseDQ() {doTest(OP + "\"<caret>\"", "\"", OP + "\"\"<caret>");}

  @Test
  public void testDoubleCloseXQ() {doTest(OP + "`<caret>`", "`", OP + "``<caret>");}

  @Test
  public void testDoubleCloseParen() {doTest(OP + "(<caret>)", ")", OP + "()<caret>");}

  @Test
  public void testDoubleCloseBracket() {doTest(OP + "[<caret>]", "]", OP + "[]<caret>");}

  @Test
  public void testDoubleCloseBrace() {doTest(OP + "{<caret>}", "}", OP + "{}<caret>");}
}
