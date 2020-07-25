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

import base.PerlLightTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class PerlReparseTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/unit/perl/reparse";
  }

  @Test
  public void testBeginSay() {doTestNamedBlock("BEGIN", "say 'hi';");}

  @Test
  public void testUnitcheckSay() {doTestNamedBlock("UNITCHECK", "say 'hi';");}

  @Test
  public void testCheckSay() {doTestNamedBlock("CHECK", "say 'hi';");}

  @Test
  public void testInitSay() {doTestNamedBlock("INIT", "say 'hi';");}

  @Test
  public void testEndSay() {doTestNamedBlock("END", "say 'hi';");}

  @Test
  public void testAutoloadSay() {doTestNamedBlock("AUTOLOAD", "say 'hi';");}

  @Test
  public void testDestorySay() {doTestNamedBlock("DESTROY", "say 'hi';");}

  @Test
  public void testBeginOpenBrace() {doTestNamedBlock("BEGIN", "grep {");}

  @Test
  public void testUnitcheckOpenBrace() {doTestNamedBlock("UNITCHECK", "grep {");}

  @Test
  public void testCheckOpenBrace() {doTestNamedBlock("CHECK", "grep {");}

  @Test
  public void testInitOpenBrace() {doTestNamedBlock("INIT", "grep {");}

  @Test
  public void testEndOpenBrace() {doTestNamedBlock("END", "grep {");}

  @Test
  public void testAutoloadOpenBrace() {doTestNamedBlock("AUTOLOAD", "grep {");}

  @Test
  public void testDestoryOpenBrace() {doTestNamedBlock("DESTROY", "grep {");}

  @Test
  public void testDoBlock() {doTest("say 'hi';");}

  @Test
  public void testElseBlock() {doTest("say 'hi';");}

  @Test
  public void testElsifBlock() {doTest("say 'hi';");}

  @Test
  public void testEvalBlock() {doTest("say 'hi';");}

  @Test
  public void testForBlock() {doTest("say 'hi';");}

  @Test
  public void testForeachBlock() {doTest("say 'hi';");}

  @Test
  public void testGivenBlock() {doTest("say 'hi';");}

  @Test
  public void testGrepBlock() {doTest("say 'hi';");}

  @Test
  public void testHandleBlock() {doTest("say 'hi';");}

  @Test
  public void testIfBlock() {doTest("say 'hi';");}

  @Test
  public void testMapBlock() {doTest("say 'hi';");}

  @Test
  public void testNamespaceBlock() {doTest("say 'hi';");}

  @Test
  public void testSortBlock() {doTest("say 'hi';");}

  @Test
  public void testSubAnonBlock() {doTest("say 'hi';");}

  @Test
  public void testUnlessBlock() {doTest("say 'hi';");}

  @Test
  public void testUntilBlock() {doTest("say 'hi';");}

  @Test
  public void testWhenBlock() {doTest("say 'hi';");}

  @Test
  public void testWhileBlock() {doTest("say 'hi';");}

  @Test
  public void testAfterBody() {doTest("say 'hi';");}

  @Test
  public void testAroundBody() {doTest("say 'hi';");}

  @Test
  public void testAugmentBody() {doTest("say 'hi';");}

  @Test
  public void testBeforeBody() {doTest("say 'hi';");}

  @Test
  public void testFunBody() {doTest("say 'hi';");}

  @Test
  public void testFuncBody() {doTest("say 'hi';");}

  @Test
  public void testMethodBody() {doTest("say 'hi';");}

  @Test
  public void testOverrideBody() {doTest("say 'hi';");}

  @Test
  public void testSubAnonBody() {doTest("say 'hi';");}

  @Test
  public void testSubAnonBodyInner() {doTest("say 'hi';");}

  @Test
  public void testSubBodyInner() {doTest("say 'hi';");}

  @Test
  public void testSubBodyStatement() {doTestSub("say 'hi';");}

  @Test
  public void testLineCommentText() {
    doTest("text");
  }

  @Test
  public void testLineCommentNewLine() {
    doTest("\n");
  }

  @Test
  public void testLineCommentAnnotation() {
    doTest("@method ");
  }

  @Test
  public void testSubBodyHeredoc() {doTestSub("say <<'SOMETHING';");}

  @Test
  public void testSubBodyOpenBrace() {doTestSub("{");}

  @Test
  public void testSubBodyCloseBrace() {doTestSub("}");}

  @Test
  public void testSubBodyRegexp() {doTestSub("m/");}

  private void doTestSub(@NotNull String textToInsert) {
    initWithFileSmart("subBody");
    doTestReparseWithoutInit(textToInsert);
  }

  private void doTestNamedBlock(@NotNull String blockName, @NotNull String textToInsert) {
    String content = "sub something {\n" +
                     "  say 'sub start';\n" +
                     blockName + " {\n" +
                     "   say 'block start';\n" +
                     "   <caret>\n" +
                     "   say 'block end';\n" +
                     "  }\n" +
                     "  say 'sub end';\n" +
                     "}";
    initWithTextSmartWithoutErrors(content);
    doTestReparseWithoutInit(textToInsert);
  }
  private void doTest(@NotNull String textToInsert) {
    doTestReparse(textToInsert);
  }
}
