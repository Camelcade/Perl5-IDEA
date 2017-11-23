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

  //public void testLetterWithSpace() {doTest(OP + " <caret>", "a", OP + " a<caret>a");}

  public void testAmbiguousLetter() {doTest(OP + "<caret>", "w", OP + "w<caret>");}

  //public void testAmbiguousLetterWithSpace() {doTest(OP + " <caret>", "w", OP + " w<caret>w");}
}
