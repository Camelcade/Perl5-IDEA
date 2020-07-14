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
import com.intellij.lang.ASTNode;
import com.intellij.lang.FileASTNode;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Couple;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.BlockSupportImpl;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.psi.PerlString;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class PerlReparseTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/unit/perl/reparse";
  }

  @Test
  public void testQwQuoteNewItem() {doTestQwList('"', "new_item");}

  @Test
  public void testQwQuoteQuote() {doTestQwList('"', "\"");}

  @Test
  public void testQwQuoteQuoteEscaped() {doTestQwList('"', "\\\"");}

  @Test
  public void testQwBracesNewItem() {doTestQwList('{', "new_item");}

  @Test
  public void testQwBracesQuote() {doTestQwList('{', "\"");}

  @Test
  public void testQwBracesQuoteEscaped() {doTestQwList('{', "\\\"");}

  @Test
  public void testQwBracesOpenBrace() {doTestQwList('{', "{");}

  @Test
  public void testQwBracesOpenBraceEscaped() {doTestQwList('{', "\\{");}

  @Test
  public void testQwBracesCloseBrace() {doTestQwList('{', "}");}

  @Test
  public void testQwBracesCloseBraceEscaped() {doTestQwList('{', "\\}");}

  @Test
  public void testUseVarsQwQuoteNewItem() {doTestUseVars('"', "new_item");}

  @Test
  public void testUseVarsQwQuoteQuote() {doTestUseVars('"', "\"");}

  @Test
  public void testUseVarsQwQuoteQuoteEscaped() {doTestUseVars('"', "\\\"");}

  @Test
  public void testUseVarsQwBracesNewItem() {doTestUseVars('{', "new_item");}

  @Test
  public void testUseVarsQwBracesQuote() {doTestUseVars('{', "\"");}

  @Test
  public void testUseVarsQwBracesQuoteEscaped() {doTestUseVars('{', "\\\"");}

  @Test
  public void testUseVarsQwBracesOpenBrace() {doTestUseVars('{', "{");}

  @Test
  public void testUseVarsQwBracesOpenBraceEscaped() {doTestUseVars('{', "\\{");}

  @Test
  public void testUseVarsQwBracesCloseBrace() {doTestUseVars('{', "}");}

  @Test
  public void testUseVarsQwBracesCloseBraceEscaped() {doTestUseVars('{', "\\}");}

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
  public void testRegexESlashCode() {doTestRegexESlash("say 'hi';");}

  @Test
  public void testRegexEBraceCode() {doTestRegexEBrace("say 'hi';");}

  @Test
  public void testRegexESlashSlash() {doTestRegexESlash("# something /");}

  @Test
  public void testRegexEBraceSlash() {doTestRegexEBrace("# something /");}

  @Test
  public void testRegexESlashSlashEscaped() {doTestRegexESlash("# something \\/");}

  @Test
  public void testRegexEBraceSlashEscaped() {doTestRegexEBrace("# something \\/");}

  @Test
  public void testRegexESlashOpenBrace() {doTestRegexESlash("# something {");}

  @Test
  public void testRegexEBraceOpenBrace() {doTestRegexEBrace("# something {");}

  @Test
  public void testRegexESlashCloseBrace() {doTestRegexESlash("# something }");}

  @Test
  public void testRegexEBraceCloseBrace() {doTestRegexEBrace("# something }");}

  @Test
  public void testRegexESlashCloseBraceEscaped() {doTestRegexESlash("# something \\}");}

  @Test
  public void testRegexEBraceCloseBraceEscaped() {doTestRegexEBrace("# something \\}");}

  @Test
  public void testRegexESlashBraces() {doTestRegexESlash("{}");}

  @Test
  public void testRegexEBraceBraces() {doTestRegexEBrace("{}");}

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
  public void testSubBodyHeredoc() {doTestSub("say <<'SOMETHING';");}

  @Test
  public void testSubBodyOpenBrace() {doTestSub("{");}

  @Test
  public void testSubBodyCloseBrace() {doTestSub("}");}

  @Test
  public void testSubBodyRegexp() {doTestSub("m/");}

  private void doTestSub(@NotNull String textToInsert) {
    initWithFileSmart("subBody");
    doTestWithoutInit(textToInsert);
  }

  private void doTestRegexEBrace(@NotNull String textToInsert) {
    initWithFileSmart("regexEBrace");
    doTestWithoutInit(textToInsert);
  }

  private void doTestRegexESlash(@NotNull String textToInsert) {
    initWithFileSmart("regexESlash");
    doTestWithoutInit(textToInsert);
  }

  private void doTestQwList(char openQuote, @NotNull String textToInsert) {
    char closeChar = PerlString.getQuoteCloseChar(openQuote);
    String content = "sub something {\n" +
                     "  say 'sub start';\n" +
                     "qw" + openQuote + "\n" +
                     "   item1\n" +
                     "   <caret>\n" +
                     "   item2\n" +
                     closeChar + ";\n" +
                     "  say 'sub end';\n" +
                     "}";
    initWithTextSmartWithoutErrors(content);

    doTestWithoutInit(textToInsert);
  }

  private void doTestUseVars(char openQuote, @NotNull String textToInsert) {
    char closeChar = PerlString.getQuoteCloseChar(openQuote);
    String content = "BEGIN {\n" +
                     "  say 'begin start';\n" +
                     " use vars qw" + openQuote + "\n" +
                     "   $item1\n" +
                     "   <caret>\n" +
                     "   @item2\n" +
                     closeChar + ";\n" +
                     "  say 'begin end';\n" +
                     "}";
    initWithTextSmartWithoutErrors(content);
    doTestWithoutInit(textToInsert);
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

    doTestWithoutInit(textToInsert);
  }

  private void doTest(@NotNull String textToInsert) {
    initWithFileSmart();
    doTestWithoutInit(textToInsert);
  }

  private void doTestWithoutInit(@NotNull String textToInsert) {
    PsiFile psiFile = getFile();
    assertInstanceOf(psiFile, PsiFileImpl.class);
    FileASTNode fileNode = psiFile.getNode();
    Editor editor = getEditor();
    int offset = editor.getCaretModel().getOffset();
    CharSequence documentText = editor.getDocument().getCharsSequence();
    String newText = documentText.subSequence(0, offset) + textToInsert + documentText.subSequence(offset, documentText.length());
    Couple<ASTNode> roots =
      BlockSupportImpl.findReparseableRoots((PsiFileImpl)psiFile, fileNode, TextRange.create(offset, offset), newText);

    StringBuilder result = new StringBuilder(newText).append(SEPARATOR_NEWLINES);
    result.insert(offset + textToInsert.length(), "<caret>");

    if (roots == null) {
      result.append("Full reparse");
    }
    else {
      result.append(roots.first).append(SEPARATOR_NEWLINES);
      result.append(roots.first.getChars());
    }

    myFixture.type(textToInsert);

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), result.toString());
  }
}
