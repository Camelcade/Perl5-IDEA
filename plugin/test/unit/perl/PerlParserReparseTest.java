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

package unit.perl;

import org.junit.Test;
import unit.perl.parser.PerlParserTestBase;

public class PerlParserReparseTest extends PerlParserTestBase {
  @Override
  protected String getBaseDataPath() {
    return "testData/unit/perl/parserReparse";
  }

  @Test
  public void testReparsedBlockSub() {doTest();}

  @Test
  public void testLineComment() {doTest();}

  @Test
  public void testReparsedBlockInRegexp() {doTest();}

  @Test
  public void testReparsedHeredoc() {doTest();}

  @Test
  public void testReparsedHeredocQQ() {doTest();}

  @Test
  public void testReparsedHeredocQX() {doTest();}

  @Test
  public void testReparsedQw() {doTest();}

  @Test
  public void testReparsedUseVars() {
    setSkipSpaces(false);
    doTestWithTyping("$othervar");
  }

  @Test
  public void testReparsedRegexMatch() {doTest();}

  @Test
  public void testReparsedRegexMatchX() {doTest();}

  @Test
  public void testReparsedRegexMatchXX() {doTest();}

  @Test
  public void testReparsedRegexCompile() {doTest();}

  @Test
  public void testReparsedRegexCompileX() {doTest();}

  @Test
  public void testReparsedRegexCompileXX() {doTest();}

  @Test
  public void testReparsedRegexMatchNoOp() {doTest();}

  @Test
  public void testReparsedRegexMatchNoOpX() {doTest();}

  @Test
  public void testReparsedRegexMatchNoOpXX() {doTest();}

  @Test
  public void testReparsedStringQQ() {doTest();}

  @Test
  public void testReparsedStringQQNoOp() {doTest();}

  @Test
  public void testReparsedStringQX() {doTest();}

  @Test
  public void testReparsedStringQXNoOp() {doTest();}

  @Test
  public void testReparsedStringSQ() {doTest();}

  @Test
  public void testReparsedStringSQNoOp() {doTest();}

  @Override
  protected void doTest() {
    doTestWithTyping("1;");
  }
}
