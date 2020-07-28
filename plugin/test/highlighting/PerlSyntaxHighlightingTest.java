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

package highlighting;


import org.junit.Test;
import unit.perl.parser.PerlParserTest;

public class PerlSyntaxHighlightingTest extends PerlParserTest {

  @Override
  protected String getResultsTestDataPath() {
    return "testData/highlighting/perl/syntax";
  }

  @Test
  public void testPerlTidy() {
    initWithPerlTidy();
    doTestHighlighterWithoutInit();
  }

  @Override
  protected void doTest() {
    doTest(true);
  }

  @Override
  protected void doTest(boolean checkErrors) {
    doTestHighlighter(checkErrors);
  }
}
