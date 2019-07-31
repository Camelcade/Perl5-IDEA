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

package parser;


import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import org.junit.Test;
public class PerlParserTest extends PerlParserTestBase {
  @Override
  protected String getBaseDataPath() {
    return "testData/parser/perl";
  }

  @Test
  public void testShiftPopPushUnshift() {doTest();}

  @Test
  public void testCpanfile() {
    String ext = myFileExt;
    try {
      myFileExt = "";
      doTest();
    }
    finally {
      myFileExt = ext;
    }
  }

  @Test
  public void testDefinedSigils() {doTest();}

  @Test
  public void testIssue2084() {doTest();}

  @Test
  public void testFileTestPrecedence() {doTest();}

  @Test
  public void testSpliceExpr() {doTest();}

  @Test
  public void testDefinedExpr() {doTest();}

  @Test
  public void testScalarExpr() {doTest(false);}

  @Test
  public void testUndefInDeclaration() {doTest();}

  @Test
  public void testMatchRegexWithBlock() {doTest();}

  @Test
  public void testMatchRegexWithBlockUnclosed() {doTest(false);}

  @Test
  public void testDefaultSub() {doTest();}

  @Test
  public void testFalsePositiveIndexInRegexp() {doTest();}

  @Test
  public void testPackageAsMethod() {doTest();}

  @Test
  public void testParentSub() {doTest();}

  @Test
  public void testPodInTheData() {doTest();}

  @Test
  public void testPodInTheEnd() {doTest();}

  @Test
  public void testPeriodInIndex() {doTest();}

  @Test
  public void testVariableIndexWithConcatenation() {doTest();}

  @Test
  public void testOldPerlVersionAsIndex() {doTest();}

  @Test
  public void testReadonly() {doTest();}

  @Test
  public void testRightwardFancyCall() {doTest();}

  @Test
  public void testPrintSubAndSomething() {doTest();}

  @Test
  public void testDiamondAfterScalar() {doTest();}

  @Test
  public void testLpLogicAfterComma() {doTest();}

  @Test
  public void testFlowControlWithDeref() {doTest();}

  @Test
  public void testPackageSubPackage() {doTest();}

  @Test
  public void testDeleteInList() {doTest();}

  @Test
  public void testExitKeyword() {doTest();}

  @Test
  public void testSayWithFatComma() {doTest();}

  @Test
  public void testScalarUnary() {doTest();}

  @Test
  public void testSmarterHash() {doTest();}

  @Test
  public void testAttributes528() {doTest();}

  @Test
  public void testSubExprComplex() {doTest();}

  @Test
  public void testEndSimple() {doTest();}

  @Test
  public void testEndMiddle() {doTest();}

  @Test
  public void testDataSimple() {doTest();}

  @Test
  public void testDataMiddle() {doTest();}

  @Test
  public void testIssue1723() {doTest();}

  @Test
  public void testMoose() {doTest();}

  @Test
  public void testDeclarations() {doTest();}

  @Test
  public void testNumericX() {doTest();}

  @Test
  public void testStringCmpWithPackage() {doTest();}

  @Test
  public void testIssue1655() {doTest();}

  @Test
  public void testForIndexedContinue() {doTest(false);}

  @Test
  public void testForEachIndexedContinue() {doTest(false);}

  @Test
  public void testComplexType() {doTest();}

  @Test
  public void testComplexReturns() {doTest();}

  @Test
  public void testWildCardReturns() {doTest();}

  @Test
  public void testContinueExpr() {doTest();}

  @Test
  public void testRangeAfterNumber() {doTest();}

  @Test
  public void testStandardTypes() {doTest();}

  @Test
  public void testNamespaceBinding() {doTest();}

  @Test
  public void testIssue1506() {doTest();}

  @Test
  public void testParenthesizedPrintArguments() {doTest();}

  @Test
  public void testBareAccessors() {doTest();}

  @Test
  public void testClassAccessorSubDeclaration() {doTest();}

  @Test
  public void testUseBareword() {doTest();}

  @Test
  public void testClassAccessor() {doTest();}

  @Test
  public void testExceptionClass() {doTest();}

  @Test
  public void testIssue1449() {doTest();}

  @Test
  public void testDeclareReference() {doTest();}

  @Test
  public void testBuiltInWithRefs() {doTest();}

  @Test
  public void testIssue1434() {doTest();}

  @Test
  public void testIssue1435() {doTest();}

  @Test
  public void testLexicalSubs() {doTest();}

  @Test
  public void testComplexModifiers() {doTest();}

  @Test
  public void testRegexSpacing() {doTest();}

  @Test
  public void testSuperExtendedRegexp() {doTest();}

  @Test
  public void testUseVars() {
    doTest();
  }

  @Test
  public void testIncorrectIndexes() {
    doTest();
  }

  @Test
  public void testCharGroups() {
    doTest();
  }

  @Test
  public void testCoreModules() {
    doTest();
  }

  @Test
  public void testMultiTokenElements() {
    doTest();
  }

  @Test
  public void testHandleAcceptors() {
    doTest();
  }

  @Test
  public void testDynaLoaderCall() {
    doTest();
  }

  @Test
  public void testFancyImport() {
    doTest();
  }

  @Test
  public void testUniversalCan() {
    doTest();
  }

  @Test
  public void testDefinedGlob() {
    doTest();
  }

  @Test
  public void testCallPrecedance() {
    doTest(false);
  }

  @Test
  public void testSortArguments() {
    doTest();
  }

  @Test
  public void testPerlPod() {
    doTest();
  }

  @Test
  public void testPerlPodEmpty() {
    doTest();
  }

  @Test
  public void testPerlPodIncomplete() {
    doTest();
  }

  @Test
  public void testQuoteLikeWithHeredocs() {
    doTest();
  }

  @Test
  public void testVarsAndCasts() {
    doTest();
  }

  @Test
  public void testSubPrototypes() {
    doTest();
  }

  @Test
  public void testLookAheadEofBug() {
    doTest(false);
  }

  @Test
  public void testVariableAttributes() {
    doTest();
  }

  @Test
  public void testLazyParsableBlocks() {
    doTest();
  }

  @Test
  public void testCommentInRegexp() {
    doTest();
  }

  @Test
  public void testCommentInString() {
    doTest();
  }

  @Test
  public void testHeredocs() {
    doTest();
  }

  @Test
  public void testHeredocSequential() {
    doTest();
  }

  @Test
  public void testHeredocUnclosed() {
    doTest();
  }

  @Test
  public void testIndentableHeredocs() {
    doTest();
  }

  @Test
  public void testIndentableHeredocSequential() {
    doTest();
  }

  @Test
  public void testIndentableHeredocUnclosed() {
    doTest();
  }

  @Test
  public void testHeredocUnclosedWithEmptyMarker() {
    doTest();
  }


  @Test
  public void testSubAttributes() {
    doTest();
  }

  @Test
  public void testSubSignatures() {
    doTest();
  }

  @Test
  public void testSubSignaturesFromVimSupport() {
    doTest();
  }

  @Test
  public void testFormat() {
    doTest();
  }

  @Test
  public void testFormatEmpty() {
    doTest();
  }

  @Test
  public void testFormatIncomplete() {
    doTest(false);
  }

  @Test
  public void testFormatEmptyIncomplete() {
    doTest(false);
  }

  @Test
  public void testNamedBlocks() {
    doTest();
  }

  @Test
  public void testVariablesAndElements() {
    doTest();
  }

  @Test
  public void testRegexpTailingBuck() {
    doTest();
  }

  @Test
  public void testAmbiguousSigils() {
    doTest();
  }

  @Test
  public void testTryCatchHybrid() {
    doTest();
  }

  @Test
  public void testTryCatchVariants() {
    doTest();
  }

  @Test
  public void testTryDancerException() {doTest();}

  @Test
  public void testTryError() {doTest();}

  @Test
  public void testTryExceptionClass() {doTest();}

  @Test
  public void testTryTry__Catch() {doTest();}

  @Test
  public void testTryTryCatch() {doTest();}

  @Test
  public void testTryTryTiny() {doTest();}

  @Test
  public void testTryCatchErrors() {doTest();}

  @Test
  public void testHashAcceptors() {
    doTest();
  }

  @Test
  public void testKeywordAsLabel() {
    doTest();
  }

  @Test
  public void testKeywordsWithCore() {
    doTest();
  }

  @Test
  public void testParserRefactoringBugs() {
    doTest();
  }

  @Test
  public void testPostDeref() {
    doTest();
  }

  @Test
  public void testInterpolation() {
    doTest();
  }

  @Test
  public void testCompositeOps() {
    doTest();
  }

  @Test
  public void testCompositeOpsSpaces() {
    doTest(false);
  }

  @Test
  public void testAnnotation() {
    doTest(false);
  }

  @Test
  public void testConstant() {
    doTest();
  }

  @Test
  public void testBareUtfString() {
    doTest();
  }

  @Test
  public void testLazyParsing() {
    doTest();
  }

  @Test
  public void testParserTestSet() {
    doTest();
  }

  @Test
  public void testTrickySyntax() {
    doTest();
  }

  @Test
  public void testVariables() {
    doTest();
  }

  @Test
  public void testMethodSignaturesSimple() {
    doTest();
  }

  @Test
  public void testUtfIdentifiers() {
    doTest();
  }

  @Test
  public void testVarRefactoringBugs() {
    doTest();
  }

  @Test
  public void testSwitchAsSub() {
    doTest();
  }

  @Test
  public void testPerlSwitch() {
    PerlSharedSettings.getInstance(getProject()).PERL_SWITCH_ENABLED = true;
    doTest();
  }

  @Test
  public void testInterpolatedHashArrayElements() {
    doTest();
  }

  @Test
  public void testImplicitRegex() {
    doTest();
  }

  @Test
  public void testCamelcade94() {
    doTest(false);
  }

  @Test
  public void testIssue855() {
    doTest();
  }

  @Test
  public void testIssue867() {
    doTest();
  }

  @Test
  public void testRegexNModifier() {
    doTest();
  }

  @Test
  public void testUtf8PackageName() {
    doTest();
  }

  @Test
  public void testHexBinNumbersParsing() {
    doTest();
  }

  @Test
  public void test28BackrefStyleHeredoc() {
    doTest();
  }

  @Test
  public void testLabelsParsing() {
    doTest();
  }

  @Test
  public void testInterpolatedElements() {
    doTest();
  }

  @Test
  public void testOperatorsAfterLoopControl() {doTest();}
}
