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

package smartKeys.quoteLike;


import editor.PerlSmartKeysTestCase;
import org.junit.Test;
public class PerlQuoteWithoutPrefixTest extends PerlSmartKeysTestCase {
  @Test
  public void testHeredocReplace() {doTest("<<caret>>", "<", "<<<caret>");}

  @Test
  public void testHeredocNotReplaceFat() {doTest("<<<caret>>>", "<", "<<<<caret>>>");}

  @Test
  public void testHeredocNotReplaceQQ() {doTest("qq<<caret>>", "<", "qq<<<caret>>");}

  @Test
  public void testHeredocReplaceWithOffset() {doTest("say <<caret>>", "<", "say <<<caret>");}

  @Test
  public void testHeredocNotReplaceWithOffset() {doTest("say qq<<caret>>", "<", "say qq<<<caret>>");}

  @Test
  public void testAfterRe() {doTest("$a =~ <caret>", "/", "$a =~ /<caret>/");}

  @Test
  public void testAfterNotRe() {doTest("$a !~ <caret>", "/", "$a !~ /<caret>/");}

  @Test
  public void testRegexInside() {doTest("$a =~ /<caret>/", "/", "$a =~ //<caret>");}

  @Test
  public void testSequentialAdd() {doTest("say <caret>; say 'some';", "'", "say '<caret>'; say 'some';");}

  @Test
  public void testSequentialRemove() {doTestBS("say '<caret>'; say 'some';", "say <caret>; say 'some';");}

  @Test
  public void testSingleQuote() {
    doTest("say <caret>;", "'", "say '<caret>';");
  }

  @Test
  public void testSingleQuoteEof() {
    doTest("say <caret>", "'", "say '<caret>'");
  }

  @Test
  public void testSingleQuoteInside() {
    doTest("say '<caret>'", "'", "say ''<caret>");
  }

  @Test
  public void testSingleQuoteAfter() {
    doTest("say ''<caret>", "'", "say '''<caret>'");
  }

  @Test
  public void testSingleRemoveOpen() {
    doTestBS("say '<caret>'", "say <caret>");
  }

  @Test
  public void testSingleRemoveClose() {
    doTestBS("say ''<caret>", "say '<caret>");
  }

  @Test
  public void testDoubleQuote() {
    doTest("say <caret>;", "\"", "say \"<caret>\";");
  }

  @Test
  public void testDoubleQuoteEof() {
    doTest("say <caret>", "\"", "say \"<caret>\"");
  }

  @Test
  public void testDoubleQuoteInside() {
    doTest("say \"<caret>\"", "\"", "say \"\"<caret>");
  }

  @Test
  public void testDoubleQuoteAfter() {
    doTest("say \"\"<caret>", "\"", "say \"\"\"<caret>\"");
  }

  @Test
  public void testDoubleRemoveOpen() {
    doTestBS("say \"<caret>\"", "say <caret>");
  }

  @Test
  public void testDoubleRemoveClose() {
    doTestBS("say \"\"<caret>", "say \"<caret>");
  }

  @Test
  public void testTickQuote() {
    doTest("say <caret>;", "`", "say `<caret>`;");
  }

  @Test
  public void testTickQuoteEof() {
    doTest("say <caret>", "`", "say `<caret>`");
  }

  @Test
  public void testTickQuoteInside() {
    doTest("say `<caret>`", "`", "say ``<caret>");
  }

  @Test
  public void testTickQuoteAfter() {
    doTest("say ``<caret>", "`", "say ```<caret>`");
  }

  @Test
  public void testTickRemoveOpen() {
    doTestBS("say `<caret>`", "say <caret>");
  }

  @Test
  public void testTickRemoveClose() {
    doTestBS("say ``<caret>", "say `<caret>");
  }

  @Test
  public void testOpenBegin() {doTest("<caret>", "'", "'<caret>'");}

  @Test
  public void testRemoveOpenBegin() {doTestBS("'<caret>'", "<caret>");}
}
