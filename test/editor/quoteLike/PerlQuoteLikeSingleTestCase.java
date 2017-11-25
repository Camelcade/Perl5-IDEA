/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package editor.quoteLike;

/**
 * Test for single-section operators: q/qw/qq/qx/m/qr
 */
public abstract class PerlQuoteLikeSingleTestCase extends PerlQuoteLikeTestCase {

  public void testPaired() {doTest(OP + "<caret>", "<", OP + "<<caret>>");}

  public void testPairedWithSpace() {doTest(OP + " <caret>", "{", OP + " {<caret>}");}

  public void testUnpaired() {doTest(OP + "<caret>", "|", OP + "|<caret>|");}

  public void testUnpairedWithSpace() {doTest(OP + " <caret>", ",", OP + " ,<caret>,");}

  public void testSharp() {doTest(OP + "<caret>", "#", OP + "#<caret>#");}

  public void testSharpWithSpace() {doTest(OP + " <caret>", "#", OP + " #<caret>");}

  public void testLetter() {doTest(OP + "<caret>", "a", OP + "a<caret>");}

  public void testLetterWithSpace() {doTest(OP + " <caret>", "a", OP + " a<caret>a");}

  public void testAmbiguousLetter() {doTest(OP + "<caret>", "w", OP + "w<caret>");}

  public void testAmbiguousLetterWithSpace() {doTest(OP + " <caret>", "w", OP + " w<caret>w");}

  public void testQuoteSingle() {doTest(OP + "<caret>", "'", OP + "'<caret>'");}

  public void testQuoteSingleWithSpace() {doTest(OP + " <caret>", "'", OP + " '<caret>'");}

  public void testQuoteDouble() {doTest(OP + "<caret>", "\"", OP + "\"<caret>\"");}

  public void testQuoteDoubleWithSpace() {doTest(OP + " <caret>", "\"", OP + " \"<caret>\"");}

  public void testQuoteTick() {doTest(OP + "<caret>", "`", OP + "`<caret>`");}

  public void testQuoteTickWithSpace() {doTest(OP + " <caret>", "`", OP + " `<caret>`");}

  public void testParen() {doTest(OP + "<caret>", "(", OP + "(<caret>)");}

  public void testParenWithSpace() {doTest(OP + " <caret>", "(", OP + " (<caret>)");}

  public void testBracket() {doTest(OP + "<caret>", "[", OP + "[<caret>]");}

  public void testBracketWithSpace() {doTest(OP + " <caret>", "[", OP + " [<caret>]");}

  public void testBrace() {doTest(OP + "<caret>", "{", OP + "{<caret>}");}

  public void testBraceWithSpace() {doTest(OP + " <caret>", "{", OP + " {<caret>}");}

  public void testClosedPaired() {doTest(OP + "<caret>>", "<", OP + "<<caret>>");}

  public void testClosedPairedWithSpace() {doTest(OP + " <caret>>", "<", OP + " <<caret>>");}

  public void testClosedUnPaired() {doTest(OP + "<caret>|", "|", OP + "|<caret>|");}

  public void testClosedUnPairedWithSpace() {doTest(OP + " <caret>|", "|", OP + " |<caret>|");}

  public void testClosedSQ() {doTest(OP + "<caret>'", "'", OP + "'<caret>'");}

  public void testClosedSQWithSpace() {doTest(OP + " <caret>'", "'", OP + " '<caret>'");}

  public void testClosedDQ() {doTest(OP + "<caret>\"", "\"", OP + "\"<caret>\"");}

  public void testClosedDQWithSpace() {doTest(OP + " <caret>\"", "\"", OP + " \"<caret>\"");}

  public void testClosedXQ() {doTest(OP + "<caret>`", "`", OP + "`<caret>`");}

  public void testClosedXQWithSpace() {doTest(OP + " <caret>`", "`", OP + " `<caret>`");}

  public void testPairedSwapped() {doTest(OP + "<caret>", ">", OP + "><caret>>");}

  public void testRemovingPaired() {doTestBS(OP + "<<caret>>", OP + "<caret>");}

  public void testRemovingPairedWithSpace() {doTestBS(OP + " <<caret>>", OP + " <caret>");}

  public void testRemovingUnPaired() {doTestBS(OP + "|<caret>|", OP + "<caret>");}

  public void testRemovingUnPairedWithSpace() {doTestBS(OP + " |<caret>|", OP + " <caret>");}

  public void testRemovingSQ() {doTestBS(OP + "'<caret>'", OP + "<caret>");}

  public void testRemovingSQWithSpace() {doTestBS(OP + " '<caret>'", OP + " <caret>");}

  public void testRemovingDQ() {doTestBS(OP + "\"<caret>\"", OP + "<caret>");}

  public void testRemovingDQWithSpace() {doTestBS(OP + " \"<caret>\"", OP + " <caret>");}

  public void testRemovingXQ() {doTestBS(OP + "`<caret>`", OP + "<caret>");}

  public void testRemovingXQWithSpace() {doTestBS(OP + " `<caret>`", OP + " <caret>");}

  public void testRemovingParen() {doTestBS(OP + "(<caret>)", OP + "<caret>");}

  public void testRemovingParenWithSpace() {doTestBS(OP + " (<caret>)", OP + " <caret>");}

  public void testRemovingBracket() {doTestBS(OP + "[<caret>]", OP + "<caret>");}

  public void testRemovingBracketWithSpace() {doTestBS(OP + " [<caret>]", OP + " <caret>");}

  public void testRemovingBrace() {doTestBS(OP + "{<caret>}", OP + "<caret>");}

  public void testRemovingBraceWithSpace() {doTestBS(OP + " {<caret>}", OP + " <caret>");}

  public void testDoubleClosePaired() {doTest(OP + "<<caret>>", ">", OP + "<><caret>");}

  public void testDoubleCloseUnPaired() {doTest(OP + "|<caret>|", "|", OP + "||<caret>");}

  public void testDoubleCloseSQ() {doTest(OP + "'<caret>'", "'", OP + "''<caret>");}

  public void testDoubleCloseDQ() {doTest(OP + "\"<caret>\"", "\"", OP + "\"\"<caret>");}

  public void testDoubleCloseXQ() {doTest(OP + "`<caret>`", "`", OP + "``<caret>");}

  public void testDoubleCloseParen() {doTest(OP + "(<caret>)", ")", OP + "()<caret>");}

  public void testDoubleCloseBracket() {doTest(OP + "[<caret>]", "]", OP + "[]<caret>");}

  public void testDoubleCloseBrace() {doTest(OP + "{<caret>}", "}", OP + "{}<caret>");}
}
