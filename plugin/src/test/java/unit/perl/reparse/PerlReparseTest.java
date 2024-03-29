/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

import base.PerlLightTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class PerlReparseTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "unit/perl/reparse";
  }

  @Test
  public void testAnnotationBreakAt() { doTest(" "); }

  @Test
  public void testAnnotationBreakNewline() { doTest("\n"); }

  @Test
  public void testAnnotationBreakText() { doTest(" "); }

  @Test
  public void testAnnotationArray() { doTest("a"); }

  @Test
  public void testAnnotationArrayTyped() { doTest("a"); }

  @Test
  public void testAnnotationArrayTypedComment() { doTest("a"); }

  @Test
  public void testAnnotationHash() { doTest("a"); }

  @Test
  public void testAnnotationHashTyped() { doTest("a"); }

  @Test
  public void testAnnotationHashTypedComment() { doTest("a"); }

  @Test
  public void testAnnotationScalar() { doTest("a"); }

  @Test
  public void testAnnotationScalarTyped() { doTest("a"); }

  @Test
  public void testAnnotationScalarTypedComment() { doTest("a"); }

  @Test
  public void testAnnotationType() { doTest("a"); }

  @Test
  public void testAnnotationTypeComment() { doTest("a"); }

  @Test
  public void testBreakHeredocQQ() { doTest("EOM"); }

  @Test
  public void testBreakHeredocQX() { doTest("EOM"); }

  @Test
  public void testBreakRegexM() { doTest(">"); }

  @Test
  public void testBreakRegexQR() {doTest(">");}

  @Test
  public void testBreakRegexSMatch() {doTest(">");}

  @Test
  public void testBreakRegexSReplace() {doTest(">");}

  @Test
  public void testBreakRegexSReplaceE() {doTest(">");}

  @Test
  public void testBreakStringQQ() {doTest("\"");}

  @Test
  public void testBreakStringQQOp() {doTest(">");}

  @Test
  public void testBreakStringQX() {doTest("`");}

  @Test
  public void testBreakStringQXOp() {doTest(">");}

  @Test
  public void testPodManglingStart() {doTestBS();}

  @Test
  public void testPodManglingEnd() {doTestBS();}

  @Test
  public void testRegexInCodeBrace() {doTest("edit");}

  @Test
  public void testRegexInCodeSlash() {doTest("edit");}

  @Test
  public void testRegexTrBrace() {doTest(" ");}

  @Test
  public void testRegexTrSlash() {doTest(" ");}

  @Test
  public void testBlockCommentData() {doTest("test");}

  @Test
  public void testBlockCommentEnd() {doTest("test");}

  @Test
  public void testBlockCommentDataWithPod() {doTest("test");}

  @Test
  public void testBlockCommentEndWithPod() {doTest("test");}

  @Test
  public void testBlockCommentDataWithPodInPod() {doTest("test");}

  @Test
  public void testBlockCommentEndWithPodInPod() {doTest("test");}

  @Test
  public void testBlockCommentDataWithPodInPodWithoutCut() {doTest("test");}

  @Test
  public void testBlockCommentEndWithPodInPodWithoutCut() {doTest("test");}

  @Test
  public void testBlockCommentDataWithPodInPodWithLastCut() {doTest("test");}

  @Test
  public void testBlockCommentEndWithPodInPodWithLastCut() {doTest("test");}

  @Test
  public void testPrototypeSub() {doTest("$");}

  @Test
  public void testPrototypeSubAnon() {doTest("$");}

  @Test
  public void testSignatureAfter() {doTest(", $edit");}

  @Test
  public void testSignatureAround() {doTest(", $edit");}

  @Test
  public void testSignatureAugment() {doTest(", $edit");}

  @Test
  public void testSignatureBefore() {doTest(", $edit");}

  @Test
  public void testSignatureFun() {doTest(", $edit");}

  @Test
  public void testSignatureFunc() {doTest(", $edit");}

  @Test
  public void testSignatureMethod() {doTest(", $edit");}

  @Test
  public void testSignatureOverride() {doTest(", $edit");}

  @Test
  public void testSignatureSub() {doTest(", $edit");}

  @Test
  public void testSignatureSubAnon() {doTest(", $edit");}

  @Test
  public void testAttributeAnonSubDefinition() {doTest("edit");}

  @Test
  public void testAttributeAnonSubDefinitionEnd() {doTest("edit");}

  @Test
  public void testAttributeFuncDefinition() {doTest("edit");}

  @Test
  public void testAttributeFuncDefinitionEnd() {doTest("edit");}

  @Test
  public void testAttributeMethodDefinition() {doTest("edit");}

  @Test
  public void testAttributeMethodDefinitionEnd() {doTest("edit");}

  @Test
  public void testAttributeMy() {doTest("edit");}

  @Test
  public void testAttributeMyEnd() {doTest("edit");}

  @Test
  public void testAttributeMyMulti() {doTest("edit");}

  @Test
  public void testAttributeMyMultiEnd() {doTest("edit");}

  @Test
  public void testAttributeOur() {doTest("edit");}

  @Test
  public void testAttributeOurEnd() {doTest("edit");}

  @Test
  public void testAttributeOurMulti() {doTest("edit");}

  @Test
  public void testAttributeOurMultiEnd() {doTest("edit");}

  @Test
  public void testAttributeState() {doTest("edit");}

  @Test
  public void testAttributeStateEnd() {doTest("edit");}

  @Test
  public void testAttributeStateMulti() {doTest("edit");}

  @Test
  public void testAttributeStateMultiEnd() {doTest("edit");}

  @Test
  public void testAttributeSubDeclaration() {doTest("edit");}

  @Test
  public void testAttributeSubDeclarationEnd() {doTest("edit");}

  @Test
  public void testAttributeSubDefinition() {doTest("edit");}

  @Test
  public void testAttributeSubDefinitionEnd() {doTest("edit");}

  @Test
  public void testAttributeSubDefinitionWithString() {doTest("edit");}

  @Test
  public void testAttributeSubDefinitionWithStringEnd() {doTest("edit");}

  @Test
  public void testNamespaceSubDeclaration() {doTest("edit");}

  @Test
  public void testNamespaceSubDefinition() {doTest("edit");}

  @Test
  public void testNamespaceSubDeclarationEnd() {doTest("edit");}

  @Test
  public void testNamespaceSubDefinitionEnd() {doTest("edit");}

  @Test
  public void testNamespaceCallLegacy() {doTest("edit");}

  @Test
  public void testNamespaceCallLegacyEnd() {doTest("edit");}

  @Test
  public void testNamespaceCallObject() {doTest("edit");}

  @Test
  public void testNamespaceCallObjectEnd() {doTest("edit");}

  @Test
  public void testNamespaceCallStatic() {doTest("edit");}

  @Test
  public void testNamespaceCallStaticEnd() {doTest("edit");}

  @Test
  public void testNamespaceMy() {doTest("edit");}

  @Test
  public void testNamespaceMyEnd() {doTest("edit");}

  @Test
  public void testNamespaceNo() {doTest("edit");}

  @Test
  public void testNamespaceNoEnd() {doTest("edit");}

  @Test
  public void testNamespaceOur() {doTest("edit");}

  @Test
  public void testNamespaceOurEnd() {doTest("edit");}

  @Test
  public void testNamespacePackage() {doTest("edit");}

  @Test
  public void testNamespacePackageEnd() {doTest("edit");}

  @Test
  public void testNamespaceRequire() {doTest("edit");}

  @Test
  public void testNamespaceRequireEnd() {doTest("edit");}

  @Test
  public void testNamespaceReturns() {doTest("edit");}

  @Test
  public void testNamespaceReturnsEnd() {doTest("edit");}

  @Test
  public void testNamespaceState() {doTest("edit");}

  @Test
  public void testNamespaceStateEnd() {doTest("edit");}

  @Test
  public void testNamespaceType() {doTest("edit");}

  @Test
  public void testNamespaceTypeEnd() {doTest("edit");}

  @Test
  public void testNamespaceUse() {doTest("edit");}

  @Test
  public void testNamespaceUseEnd() {doTest("edit");}

  @Test
  public void testSubNameInAfterDefinition() {doTest("edit");}

  @Test
  public void testSubNameInAfterDefinitionEnd() {doTest("edit");}

  @Test
  public void testSubNameInAroundDefinition() {doTest("edit");}

  @Test
  public void testSubNameInAroundDefinitionEnd() {doTest("edit");}

  @Test
  public void testSubNameInAugmentDefinition() {doTest("edit");}

  @Test
  public void testSubNameInAugmentDefinitionEnd() {doTest("edit");}

  @Test
  public void testSubNameInBeforeDefinition() {doTest("edit");}

  @Test
  public void testSubNameInBeforeDefinitionEnd() {doTest("edit");}

  @Test
  public void testSubNameInCall() {doTest("edit");}

  @Test
  public void testSubNameInCallEnd() {doTest("edit");}

  @Test
  public void testSubNameInCallFqn() {doTest("edit");}

  @Test
  public void testSubNameInCallFqnEnd() {doTest("edit");}

  @Test
  public void testSubNameInCallLegacy() {doTest("edit");}

  @Test
  public void testSubNameInCallLegacyEnd() {doTest("edit");}

  @Test
  public void testSubNameInCallNoParens() {doTest("edit");}

  @Test
  public void testSubNameInCallNoParensEnd() {doTest("edit");}

  @Test
  public void testSubNameInCallObject() {doTest("edit");}

  @Test
  public void testSubNameInCallObjectEnd() {doTest("edit");}

  @Test
  public void testSubNameInDeclaration() {doTest("edit");}

  @Test
  public void testSubNameInDeclarationEnd() {doTest("edit");}

  @Test
  public void testSubNameInFuncDefinition() {doTest("edit");}

  @Test
  public void testSubNameInFuncDefinitionEnd() {doTest("edit");}

  @Test
  public void testSubNameInFunDefinition() {doTest("edit");}

  @Test
  public void testSubNameInFunDefinitionEnd() {doTest("edit");}

  @Test
  public void testSubNameInMethodDefinition() {doTest("edit");}

  @Test
  public void testSubNameInMethodDefinitionEnd() {doTest("edit");}

  @Test
  public void testSubNameInSubDefinition() {doTest("edit");}

  @Test
  public void testSubNameInSubDefinitionEnd() {doTest("edit");}

  @Test
  public void testSubNameInVar() {doTest("edit");}

  @Test
  public void testSubNameInVarEnd() {doTest("edit");}

  @Test
  public void testRegexTokenInCompile() {doTest("edit");}

  @Test
  public void testRegexTokenInCompileEnd() {doTest("edit");}

  @Test
  public void testRegexTokenInCompileX() {doTest("edit");}

  @Test
  public void testRegexTokenInCompileXEnd() {doTest("edit");}

  @Test
  public void testRegexTokenInMatch() {doTest("edit");}

  @Test
  public void testRegexTokenInMatchEnd() {doTest("edit");}

  @Test
  public void testRegexTokenInMatchWithOp() {doTest("edit");}

  @Test
  public void testRegexTokenInMatchWithOpEnd() {doTest("edit");}

  @Test
  public void testRegexTokenInMatchX() {doTest("edit");}

  @Test
  public void testRegexTokenInMatchXEnd() {doTest("edit");}

  @Test
  public void testRegexTokenInMatchXWithOp() {doTest("edit");}

  @Test
  public void testRegexTokenInMatchXWithOpEnd() {doTest("edit");}

  @Test
  public void testRegexTokenInSearchReplace() {doTest("edit");}

  @Test
  public void testRegexTokenInSearchReplaceEnd() {doTest("edit");}

  @Test
  public void testRegexTokenInSearchReplaceX() {doTest("edit");}

  @Test
  public void testRegexTokenInSearchReplaceXEnd() {doTest("edit");}

  @Test
  public void testPrintCallArguments() {doTest("42,");}

  @Test
  public void testBlockInBlock() {doTest("42;");}

  @Test
  public void testBlockInFile() {doTest("42;");}

  @Test
  public void testBlockInCall() {doTest("42;");}

  @Test
  public void testAnonHashInCall() {doTest("42,");}

  @Test
  public void testAnonHashInCallEnsured() {doTest("42");}

  @Test
  public void testAnonHashInBlock() {doTest("42,");}

  @Test
  public void testAnonHashInBlockEnsured() {doTest("42");}

  @Test
  public void testAnonHashInFile() {doTest("42,");}

  @Test
  public void testAnonHashInFileEnsured() {doTest("42");}

  @Test
  public void testStringContentQwList() {doTest("some_thing");}

  @Test
  public void testStringContentQQString() {doTest("some_thing");}

  @Test
  public void testStringContentQQStringEnd() {doTest("some_thing");}

  @Test
  public void testStringContentQQStringQuoted() {doTest("some_thing");}

  @Test
  public void testStringContentQQStringQuotedEnd() {doTest("some_thing");}

  @Test
  public void testStringContentQString() {doTest("some_thing");}

  @Test
  public void testStringContentQStringEnd() {doTest("some_thing");}

  @Test
  public void testStringContentQStringQuoted() {doTest("some_thing");}

  @Test
  public void testStringContentQStringQuotedEnd() {doTest("some_thing");}

  @Test
  public void testStringContentQXString() {doTest("some_thing");}

  @Test
  public void testStringContentQXStringEnd() {doTest("some_thing");}

  @Test
  public void testStringContentQXStringQuoted() {doTest("some_thing");}

  @Test
  public void testStringContentQXStringQuotedEnd() {doTest("some_thing");}

  @Test
  public void testStringContentSqBeforeHashRocket() {doTest("some_thing");}

  @Test
  public void testStringContentSqHashIndex() {doTest("some_thing");}

  @Test
  public void testStringContentQwListEnd() {doTest("some_thing");}

  @Test
  public void testStringContentSqBeforeHashRocketEnd() {doTest("some_thing");}

  @Test
  public void testStringContentSqHashIndexEnd() {doTest("some_thing");}

  @Test
  public void testStringContentSqHashIndexSlash() {doTest("some thing");}

  @Test
  public void testHeredocOpenerBare() {
    doTest("A");
  }

  @Test
  public void testHeredocOpenerSQ() {
    doTest("A");
  }

  @Test
  public void testHeredocOpenerQQ() {
    doTest("A");
  }

  @Test
  public void testHeredocOpenerQX() {
    doTest("A");
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

  private void doTestBS() {doTestReparseBs();}
}
