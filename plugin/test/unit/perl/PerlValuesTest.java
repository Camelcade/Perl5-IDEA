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

package unit.perl;


import base.PerlLightTestCase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.run.debugger.PerlDebuggerEditorsProvider;
import com.perl5.lang.perl.psi.PerlVariable;
import org.junit.Test;
public class PerlValuesTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/unit/perl/perlValues";
  }

  @Test
  public void testIssue2109a() {doTest();}

  @Test
  public void testIssue2109b() {doTest();}

  @Test
  public void testIssue2092First() {doTest();}

  @Test
  public void testIssue2092Second() {doTest();}

  @Test
  public void testAnnotatedSub() {doTest();}

  @Test
  public void testAnnotatedSubIndirect() {doTest();}

  @Test
  public void testAnnotatedSubNot() {doTest();}

  @Test
  public void testVariableInAnonSub() {doTest();}

  @Test
  public void testVariableInAnonSubAfter() {doTest();}

  @Test
  public void testVariableInAnonSubAround() {doTest();}

  @Test
  public void testVariableInAnonSubAugment() {doTest();}

  @Test
  public void testVariableInAnonSubBefore() {doTest();}

  @Test
  public void testVariableInAnonSubOverride() {doTest();}

  @Test
  public void testRecursiveCall() {doTest();}

  @Test
  public void testInterpolationString() {doTest();}

  @Test
  public void testInterpolationHeredoc() {doTest();}

  @Test
  public void testInterpolationConstantHeredocSecond() {doTest();}

  @Test
  public void testInterpolationHeredocArray() {doTest();}

  @Test
  public void testOuterVariable() {doTest();}

  @Test
  public void testContextSimple() {doTestFileWithContext();}

  @Test
  public void testIssue2073Second() {doTest();}

  @Test
  public void testIssue2073Third() {doTest();}

  @Test
  public void testMethodImplicitInvocant() {doTest();}

  @Test
  public void testSubSignatureFirst() {doTest();}

  @Test
  public void testSubSignatureFirstDefault() {doTest();}

  @Test
  public void testSubSignatureFirstDefaultMissing() {doTest();}

  @Test
  public void testSubSignatureSecond() {doTest();}

  @Test
  public void testSubSignatureSecondDefault() {doTest();}

  @Test
  public void testSubSignatureSecondDefaultMissing() {doTest();}

  @Test
  public void testSubSignatureSecondDefaultUndef() {doTest();}

  @Test
  public void testSubSignatureSecondDefaultUnknown() {doTest();}

  @Test
  public void testSubSignatureSecondIgnored() {doTest();}

  @Test
  public void testSubSignatureSecondIgnoredDefault() {doTest();}

  @Test
  public void testMojoAttr() {
    withMojo();
    doTest();
  }

  @Test
  public void testMojoAttrDefault() {
    withMojo();
    doTest();
  }

  @Test
  public void testMojoAttrDefaultArgs() {
    withMojo();
    doTest();
  }

  @Test
  public void testMojoAttrDefaultObject() {
    withMojo();
    doTest();
  }

  @Test
  public void testMojoAttrDefaultObjectArgs() {
    withMojo();
    doTest();
  }

  @Test
  public void testMojoAttrEmpty() {
    withMojo();
    doTest();
  }

  @Test
  public void testMojoAttrEmptyArgs() {
    withMojo();
    doTest();
  }

  @Test
  public void testMojoAttrEmptyObject() {
    withMojo();
    doTest();
  }

  @Test
  public void testMojoAttrEmptyObjectArgs() {
    withMojo();
    doTest();
  }

  @Test
  public void testMojoAttrNoArgs() {
    withMojo();
    doTest();
  }

  @Test
  public void testMojoAttrSub() {
    withMojo();
    doTest();
  }

  @Test
  public void testMojoAttrSubArgs() {
    withMojo();
    doTest();
  }

  @Test
  public void testMojoAttrSubObject() {
    withMojo();
    doTest();
  }

  @Test
  public void testMojoAttrSubObjectArgs() {
    withMojo();
    doTest();
  }

  @Test
  public void testSuperCall() {doTest();}

  @Test
  public void testSuperCallParent() {doTest();}

  @Test
  public void testPushValueMultiOneOfs() {doTest();}

  @Test
  public void testArraySliceDeferred() {doTest();}

  @Test
  public void testReturnArray() {doTest();}

  @Test
  public void testReturnArraySingleElement() {doTest();}

  @Test
  public void testHashRefElement() {doTest();}

  @Test
  public void testArrayRefElement() {doTest();}

  @Test
  public void testHashDerefToHash() {doTest();}

  @Test
  public void testHashDerefToHashInvalid() {doTest();}

  @Test
  public void testHashDerefToScalar() {doTest();}

  @Test
  public void testArrayDerefToArray() {doTest();}

  @Test
  public void testArrayDerefToArrayInvalid() {doTest();}

  @Test
  public void testArrayDerefToScalar() {doTest();}

  @Test
  public void testShiftPopInSub() {doTest();}

  @Test
  public void testShiftInSub() {doTest();}

  @Test
  public void testPopInSub() {doTest();}

  @Test
  public void testShiftInSubProxy() {doTest();}

  @Test
  public void testPopInSubProxy() {doTest();}

  @Test
  public void testShiftInSubReturn() {doTest();}

  @Test
  public void testPopInSubReturn() {doTest();}

  @Test
  public void testShiftInSubDelegate() {doTest();}

  @Test
  public void testPopInSubDelegate() {doTest();}

  @Test
  public void testShiftOnce() {doTest();}

  @Test
  public void testShiftTwice() {doTest();}

  @Test
  public void testShiftAll() {doTest();}

  @Test
  public void testShiftMissing() {doTest();}

  @Test
  public void testPopOnce() {doTest();}

  @Test
  public void testPopTwice() {doTest();}

  @Test
  public void testPopAll() {doTest();}

  @Test
  public void testPopMissing() {doTest();}

  @Test
  public void testShiftArray() {doTest();}

  @Test
  public void testPopArray() {doTest();}

  @Test
  public void testShiftArrayReturn() {doTest();}

  @Test
  public void testPopArrayReturn() {doTest();}

  @Test
  public void testPushValue() {doTest();}

  @Test
  public void testPushValueArrayEmpty() {doTest();}

  @Test
  public void testPushValueArray() {doTest();}

  @Test
  public void testPushValueArrayResult() {doTest();}

  @Test
  public void testPushValueMulti() {doTest();}

  @Test
  public void testPushValueNonEmpty() {doTest();}

  @Test
  public void testPushValueArrayNonEmpty() {doTest();}

  @Test
  public void testPushValueMultiNonEmpty() {doTest();}

  @Test
  public void testUnshiftValue() {doTest();}

  @Test
  public void testUnshiftValueArrayEmpty() {doTest();}

  @Test
  public void testUnshiftValueArray() {doTest();}

  @Test
  public void testUnshiftValueArrayResult() {doTest();}

  @Test
  public void testUnshiftValueMulti() {doTest();}

  @Test
  public void testUnshiftValueNonEmpty() {doTest();}

  @Test
  public void testUnshiftValueArrayNonEmpty() {doTest();}

  @Test
  public void testUnshiftValueMultiNonEmpty() {doTest();}

  @Test
  public void testArgumentResolve() {doTest();}

  @Test
  public void testArgumentAnnotated() {doTest();}

  @Test
  public void testArgumentFirst() {doTest();}

  @Test
  public void testArgumentSelf() {doTest();}

  @Test
  public void testArgumentTyped() {doTest();}

  @Test
  public void testBlessContext() {doTest();}

  @Test
  public void testBlessContextDeferred() {doTest();}

  @Test
  public void testBlessSimple() {doTest();}

  @Test
  public void testBlessSimpleDeferred() {doTest();}

  @Test
  public void testScalarDerefInplace() {doTest();}

  @Test
  public void testScalarDerefInplaceDeep() {doTest();}

  @Test
  public void testScalarDerefInplaceIndirect() {doTest();}

  @Test
  public void testScalarDerefResolve() {doTest();}

  @Test
  public void testScalarDerefResolveIndirect() {doTest();}

  @Test
  public void testArrayAppendOneOf() {doTest();}

  @Test
  public void testArrayAppendOneOfUnknown() {doTest();}

  @Test
  public void testArrayPrependOneOf() {doTest();}

  @Test
  public void testArrayInsertOneOf() {doTest();}

  @Test
  public void testArrayElementFromSliceMultiOneOf() {doTest();}

  @Test
  public void testHashSlice() {doTest();}

  @Test
  public void testHashSliceMulti() {doTest();}

  @Test
  public void testHashSliceMultiOneOf() {doTest();}

  @Test
  public void testHashSliceMultiOneOfDeferred() {doTest();}

  @Test
  public void testArraySlice() {doTest();}

  @Test
  public void testArraySliceMulti() {doTest();}

  @Test
  public void testArraySliceMultiAll() {doTest();}

  @Test
  public void testArraySliceMultiAllDeferred() {doTest();}

  @Test
  public void testArraySliceMultiOneOf() {doTest();}

  @Test
  public void testArraySliceMultiOneOfDeferred() {doTest();}

  @Test
  public void testArithmeticNegation() {doTest();}

  @Test
  public void testArithmeticNegationDouble() {doTest();}

  @Test
  public void testArithmeticNegationString() {doTest();}

  @Test
  public void testArithmeticNegationStringMinus() {doTest();}

  @Test
  public void testArithmeticNegationStringPlus() {doTest();}

  @Test
  public void testArithmeticNegationUndef() {doTest();}

  @Test
  public void testScalarOneOf() {doTest();}

  @Test
  public void testArrayElementOneOf() { doTest(); }

  @Test
  public void testArrayElementOneOfDeferred() { doTest(); }

  @Test
  public void testArrayElement() { doTest(); }

  @Test
  public void testArrayElementUndef() { doTest(); }

  @Test
  public void testArrayElementUnknown() { doTest(); }

  @Test
  public void testArrayElementFirst() { doTest(); }

  @Test
  public void testArrayElementFirstNegative() { doTest(); }

  @Test
  public void testArrayElementNegative() { doTest(); }

  @Test
  public void testArrayElementOOB() { doTest(); }

  @Test
  public void testArrayElementOOBNegative() { doTest(); }

  @Test
  public void testVariableAnnotationVariant() {
    // fixme this behviour is questionable, see commit message
    doTest();
  }

  @Test
  public void testVariableDeclarationVariant() {doTest();}

  @Test
  public void testVariableAnnotationVariantTransient() {doTest();}

  @Test
  public void testVariableAnnotationVariantTransientMixed() {doTest();}

  @Test
  public void testArbitraryConstructor() {doTest();}

  @Test
  public void testArbitraryConstructorStatic() {
    // fixme this behaviour is questionable, see commit message
    doTest();
  }

  @Test
  public void testArbitraryConstructors() {doTest();}

  @Test
  public void testConstantSingle() {doTest();}

  @Test
  public void testConstantMulti() {doTest();}

  @Test
  public void testConstantMultiImport() {
    myFixture.copyFileToProject("constantMultiImportFile.pl");
    doTest();
  }

  @Test
  public void testShadowedValue() {doTest();}

  @Test
  public void testShadowedValues() {doTest();}

  @Test
  public void testSubExtractorDirect() {doTest();}

  @Test
  public void testSubExtractorDirectObject() {doTest();}

  @Test
  public void testSubExtractorIndirect() {doTest();}

  @Test
  public void testSubExtractorIndirectOtherFile() {
    PerlSharedSettings.getInstance(getProject()).SIMPLE_MAIN_RESOLUTION = false;
    myFixture.copyFileToProject("extractorFunctions.pl");
    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
    doTest();
  }

  @Test
  public void testCrossSubVariable() {doTest();}

  @Test
  public void testCrossSubVariableDynamic() {doTest();}

  @Test
  public void testCrossSubVariableDynamicClosure() {doTest();}

  @Test
  public void testCrossSubVariableBuiltIn() {doTest();}

  @Test
  public void testScalarExpr() {doTest();}

  @Test
  public void testScalarExprTransparent() {doTest();}

  @Test
  public void testCallScalar() {doTest();}

  @Test
  public void testCallArray() {doTest();}

  @Test
  public void testCallHash() {doTest();}

  @Test
  public void testCallScalarFancy() {doTest();}

  @Test
  public void testCallArrayFancy() {doTest();}

  @Test
  public void testCallHashFancy() {doTest();}

  @Test
  public void testScalarOtherScope() {doTest();}

  @Test
  public void testHashToScalar() {doTest();}

  @Test
  public void testArrayToScalar() {doTest();}

  @Test
  public void testArrayToHashAmbiguous() {doTest();}

  @Test
  public void testArrayToHashAmbiguousDeferred() {doTest();}

  @Test
  public void testArrayToScalarAmbiguous() {doTest();}

  @Test
  public void testArrayToScalarAmbiguousDeferred() {doTest();}

  @Test
  public void testScalarToArray() {doTest();}

  @Test
  public void testHashElement() {doTest();}

  @Test
  public void testHashElementDeferredKey() {doTest();}

  @Test
  public void testHashElementAmbiguousBoth() {doTest();}

  @Test
  public void testHashElementAmbiguousBothDeferred() {doTest();}

  @Test
  public void testHashElementAmbiguousKey() {doTest();}

  @Test
  public void testHashElementAmbiguousKeyOneMissing() {doTest();}

  @Test
  public void testHashElementAmbiguousHashOneMissing() {doTest();}

  @Test
  public void testHashElementVariable() {doTest();}

  @Test
  public void testHashElementDeferredFailed() {doTest();}

  @Test
  public void testHashElementDeferredSuccess() {doTest();}

  @Test
  public void testHashMerge() {doTest();}

  @Test
  public void testHashUnmerged() {doTest();}

  @Test
  public void testArrayMerge() {doTest();}

  @Test
  public void testArrayUnmerged() {doTest();}

  @Test
  public void testScalarRef() {doTest();}

  @Test
  public void testScalarRefStatic() {doTest();}

  @Test
  public void testHashRef() {doTest();}

  @Test
  public void testHashRefFromHash() {doTest();}

  @Test
  public void testArrayRef() {doTest();}

  @Test
  public void testArrayRefFromArray() {doTest();}

  @Test
  public void testArrayList() {doTest();}

  @Test
  public void testArrayListJoin() {doTest();}

  @Test
  public void testArrayFromHash() {doTest();}

  @Test
  public void testHashList() {doTest();}

  @Test
  public void testHashListJoin() {doTest();}

  @Test
  public void testHashFromArray() {doTest();}

  @Test
  public void testArrayStringList() {doTest();}

  @Test
  public void testHashStringList() {doTest();}

  @Test
  public void testBuiltIn() {doTest();}

  @Test
  public void testDeclarationSingle() {
    doTest();
  }

  @Test
  public void testDeclarationMulti() {
    doTest();
  }

  @Test
  public void testDeclarationAssignmentNew() {
    doTest();
  }

  @Test
  public void testVariableBeforeAssignment() {
    doTest();
  }

  @Test
  public void testVariableAfterAssignment() {
    doTest();
  }

  @Test
  public void testAnnotatedSingleInside() {
    doTest();
  }

  @Test
  public void testAnnotatedSingle() {
    doTest();
  }

  @Test
  public void testAnnotatedMulti() {
    doTest();
  }

  @Test
  public void testAnnotatedMultiNonFirst() {
    doTest();
  }

  @Test
  public void testAnnotatedConcurrentStatement() {
    doTest();
  }

  @Test
  public void testAnnotatedConcurrentStatementOuter() {
    doTest();
  }

  @Test
  public void testAnnotatedConcurrentRealTypeInside() {
    doTest();
  }

  @Test
  public void testAnnotatedConcurrentRealTypeMulti() {
    doTest();
  }

  @Test
  public void testAnnotatedConcurrentRealTypeSingle() {
    doTest();
  }

  @Test
  public void testAnnotatedConcurrentRealTypeWins() {
    doTest();
  }

  private void doTest() {
    doTestPerlValue();
  }

  private void doTestFileWithContext() {
    initWithFileSmartWithoutErrors();
    PsiElement elementAtCaret = getElementAtCaretWithoutInjection();
    assertNotNull(elementAtCaret);
    PsiFile lightFile = PerlDebuggerEditorsProvider.INSTANCE.createFile(getProject(), "$var", elementAtCaret);
    PsiElement leafElement = lightFile.findElementAt(2);
    assertNotNull(leafElement);
    PerlVariable variable = PsiTreeUtil.getParentOfType(leafElement, PerlVariable.class);
    assertNotNull(variable);
    doTestPerlValueWithoutInit(variable);
  }
}
