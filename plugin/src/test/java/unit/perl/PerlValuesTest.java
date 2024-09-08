/*
 * Copyright 2015-2024 Alexandr Evstigneev
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
import com.perl5.lang.perl.debugger.PerlDebuggerEditorsProvider;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import org.junit.Test;
public class PerlValuesTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "unit/perl/perlValues";
  }

  @Test
  public void testArrayNestedResult() { doTestArrayVariableValue(); }

  @Test
  public void testArrayNestedResultElement() { doTestScalarVariableValue(); }

  @Test
  public void testArrayEmptyCallResult() { doTestScalarVariableValue(); }

  @Test
  public void testArrayEmptyElement() { doTestScalarVariableValue(); }

  @Test
  public void testArrayRefEmptyElement() { doTestScalarVariableValue(); }

  @Test
  public void testSignatureLeadingAnnotation() { doTestScalarVariableValue(); }

  @Test
  public void testDuckTypeParentFiveMethods() { doTestScalarVariableValue(); }

  @Test
  public void testArrayElementSet() { doTestArrayVariableValue(); }

  @Test
  public void testArrayElementSetMulti() { doTestArrayVariableValue(); }

  @Test
  public void testArrayRefElementSetMulti() { doTestScalarVariableValue(); }

  @Test
  public void testArrayElementSetInitialized() { doTestArrayVariableValue(); }

  @Test
  public void testArrayElementSetUnintialized() { doTestArrayVariableValue(); }

  @Test
  public void testHashElementSet() { doTestHashVariableValue(); }

  @Test
  public void testHashElementSetMulti() { doTestHashVariableValue(); }

  @Test
  public void testHashRefElementSetMulti() { doTestScalarVariableValue(); }

  @Test
  public void testHashElementSetInitialized() { doTestHashVariableValue(); }

  @Test
  public void testHashElementSetUninitialized() { doTestHashVariableValue(); }

  @Test
  public void testArrayRefElementSet() { doTestScalarVariableValue(); }

  @Test
  public void testArrayRefElementSetInitialized() { doTestScalarVariableValue(); }

  @Test
  public void testArrayRefElementSetUnitialized() { doTestScalarVariableValue(); }

  @Test
  public void testHashRefElementSet() { doTestScalarVariableValue(); }

  @Test
  public void testHashRefElementSetInitialized() { doTestScalarVariableValue(); }

  @Test
  public void testHashRefElementSetUninitialized() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCall() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallResolve() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallResolveSubCall() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallResolveSubCallWithArgument() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallResolveSubCallFirstArg() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallResolveSubCallFirstArgWithArgument() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallResolveDistinct() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallResolveDistinctLonger() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallResolveDistinctInherited() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallResolveDistinctInheritedDifferentParents() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallResolveDistinctInheritedLonger() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallResolveDistinctPartial() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallResolveMulti() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallResolveInSub() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallResolveMultiInSub() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallResolveInSubSecond() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallResolveMultiInSubSecond() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallSubArgument() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallSubArgumentSignature() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallSubArgumentSecond() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallSubArgumentSecondSignature() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallEnd() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallFancy() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallMiddle() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallAnnotated() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallTyped() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallUniversal() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallMulti() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallSeveralPaths() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallSeveralPathsCommonAfter() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallSeveralPathsCommonBefore() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallSeveralPathsCommons() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallSeveralPathsWithOtherGetter() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallSeveralPathsSame() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallSeveralPathsSameCommonAfter() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallSeveralPathsSameCommonBefore() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallSeveralPathsSameCommons() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallSeveralPathsSameWithOtherGetter() { doTestScalarVariableValue(); }

  @Test
  public void testDuckCallMultiDeterministicBase() { doTestScalarVariableValue(); }

  @Test
  public void testOverwriteScalar() { doTestScalarVariableValue(); }

  @Test
  public void testReturnScalarToList() { doTestArrayVariableValue(); }

  @Test
  public void testReturnScalarToScalar() {
    doTestScalarVariableValue();
  }

  @Test
  public void testReturnSingleElementListToList() { doTestArrayVariableValue(); }

  @Test
  public void testReturnSingleElementListToScalar() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAssignmentInPlace() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAssignmentInPlaceChained() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAssignmentInPlaceChainedSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAssignmentInPlaceMulti() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAssignmentInPlaceMultiSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testMyVariable() {
    doTestScalarVariableValue();
  }

  @Test
  public void testOurVariable() {
    doTestScalarVariableValue();
  }

  @Test
  public void testLocalVariable() {
    doTestScalarVariableValue();
  }

  @Test
  public void testStateVariable() {
    doTestScalarVariableValue();
  }

  @Test
  public void testMyVariableDeclaration() {
    doTestScalarVariableValue();
  }

  @Test
  public void testOurVariableDeclaration() {
    doTestScalarVariableValue();
  }

  @Test
  public void testLocalVariableDeclaration() {
    doTestScalarVariableValue();
  }

  @Test
  public void testStateVariableDeclaration() {
    doTestScalarVariableValue();
  }

  @Test
  public void testMyVariableDeclarationChainSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testOurVariableDeclarationChainSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testLocalVariableDeclarationChainSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testStateVariableDeclarationChainSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testMyVariableDeclarationMulti() {
    doTestScalarVariableValue();
  }

  @Test
  public void testOurVariableDeclarationMulti() {
    doTestScalarVariableValue();
  }

  @Test
  public void testLocalVariableDeclarationMulti() {
    doTestScalarVariableValue();
  }

  @Test
  public void testStateVariableDeclarationMulti() {
    doTestScalarVariableValue();
  }

  @Test
  public void testMyVariableDeclarationMultiSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testOurVariableDeclarationMultiSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testLocalVariableDeclarationMultiSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testStateVariableDeclarationMultiSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testMyVariableSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testOurVariableSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testLocalVariableSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testStateVariableSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testDefinedWithComment() {
    doTestScalarVariableValue();
  }

  @Test
  public void testIssue2115() {
    PerlResolveUtil.runWithoutErrors(this::doTestScalarVariableValue);
  }

  @Test
  public void testBareLpCodeBlock() {
    doTestScalarVariableValue();
  }

  @Test
  public void testIssue2109a() {
    doTestScalarVariableValue();
  }

  @Test
  public void testIssue2109b() {
    doTestScalarVariableValue();
  }

  @Test
  public void testIssue2092First() {
    doTestScalarVariableValue();
  }

  @Test
  public void testIssue2092Second() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedSub() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedSubIndirect() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedSubNot() {
    doTestScalarVariableValue();
  }

  @Test
  public void testVariableInAnonSub() {
    doTestScalarVariableValue();
  }

  @Test
  public void testVariableInAnonSubAfter() {
    doTestScalarVariableValue();
  }

  @Test
  public void testVariableInAnonSubAround() {
    doTestScalarVariableValue();
  }

  @Test
  public void testVariableInAnonSubAugment() {
    doTestScalarVariableValue();
  }

  @Test
  public void testVariableInAnonSubBefore() {
    doTestScalarVariableValue();
  }

  @Test
  public void testVariableInAnonSubOverride() {
    doTestScalarVariableValue();
  }

  @Test
  public void testRecursiveCall() {
    doTestScalarVariableValue();
  }

  @Test
  public void testInterpolationString() {
    doTestScalarVariableValue();
  }

  @Test
  public void testInterpolationHeredoc() {
    doTestScalarVariableValue();
  }

  @Test
  public void testInterpolationConstantHeredocSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testInterpolationHeredocArray() {
    doTestArrayVariableValue();
  }

  @Test
  public void testOuterVariable() {
    doTestScalarVariableValue();
  }

  @Test
  public void testContextSimple() {doTestFileWithContext();}

  @Test
  public void testIssue2073Second() {
    doTestScalarVariableValue();
  }

  @Test
  public void testIssue2073Third() {
    doTestScalarVariableValue();
  }

  @Test
  public void testMethodImplicitInvocant() {
    doTestScalarVariableValue();
  }

  @Test
  public void testSubSignatureFirst() {
    doTestScalarVariableValue();
  }

  @Test
  public void testSubSignatureFirstDefault() {
    doTestScalarVariableValue();
  }

  @Test
  public void testSubSignatureFirstDefaultMissing() {
    doTestScalarVariableValue();
  }

  @Test
  public void testSubSignatureSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testSubSignatureSecondDefault() {
    doTestScalarVariableValue();
  }

  @Test
  public void testSubSignatureSecondDefaultMissing() {
    doTestScalarVariableValue();
  }

  @Test
  public void testSubSignatureSecondDefaultUndef() {
    doTestScalarVariableValue();
  }

  @Test
  public void testSubSignatureSecondDefaultUnknown() {
    doTestScalarVariableValue();
  }

  @Test
  public void testSubSignatureSecondIgnored() {
    doTestScalarVariableValue();
  }

  @Test
  public void testSubSignatureSecondIgnoredDefault() {
    doTestScalarVariableValue();
  }

  @Test
  public void testSuperCall() {
    doTestScalarVariableValue();
  }

  @Test
  public void testSuperCallParent() {
    doTestScalarVariableValue();
  }

  @Test
  public void testPushValueMultiOneOfs() {
    doTestArrayVariableValue();
  }

  @Test
  public void testArraySliceDeferred() {
    doTestArrayVariableValue();
  }

  @Test
  public void testReturnArray() {
    doTestArrayVariableValue();
  }

  @Test
  public void testReturnArraySingleElement() {
    doTestArrayVariableValue();
  }

  @Test
  public void testHashRefElement() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayRefElement() {
    doTestScalarVariableValue();
  }

  @Test
  public void testHashDerefToHash() {
    doTestHashVariableValue();
  }

  @Test
  public void testHashDerefToHashInvalid() {
    doTestHashVariableValue();
  }

  @Test
  public void testHashDerefToScalar() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayDerefToArray() {
    doTestArrayVariableValue();
  }

  @Test
  public void testArrayDerefToArrayInvalid() {
    doTestArrayVariableValue();
  }

  @Test
  public void testArrayDerefToScalar() {
    doTestScalarVariableValue();
  }

  @Test
  public void testShiftPopInSub() {
    doTestArrayVariableValue();
  }

  @Test
  public void testShiftInSub() {
    doTestArrayVariableValue();
  }

  @Test
  public void testPopInSub() {
    doTestArrayVariableValue();
  }

  @Test
  public void testShiftInSubProxy() {
    doTestArrayVariableValue();
  }

  @Test
  public void testPopInSubProxy() {
    doTestArrayVariableValue();
  }

  @Test
  public void testShiftInSubReturn() {
    doTestArrayVariableValue();
  }

  @Test
  public void testPopInSubReturn() {
    doTestArrayVariableValue();
  }

  @Test
  public void testShiftInSubDelegate() {
    doTestArrayVariableValue();
  }

  @Test
  public void testPopInSubDelegate() {
    doTestArrayVariableValue();
  }

  @Test
  public void testShiftOnce() {
    doTestArrayVariableValue();
  }

  @Test
  public void testShiftTwice() {
    doTestArrayVariableValue();
  }

  @Test
  public void testShiftAll() {
    doTestArrayVariableValue();
  }

  @Test
  public void testShiftMissing() {
    doTestArrayVariableValue();
  }

  @Test
  public void testPopOnce() {
    doTestArrayVariableValue();
  }

  @Test
  public void testPopTwice() {
    doTestArrayVariableValue();
  }

  @Test
  public void testPopAll() {
    doTestArrayVariableValue();
  }

  @Test
  public void testPopMissing() {
    doTestArrayVariableValue();
  }

  @Test
  public void testShiftArray() {
    doTestScalarVariableValue();
  }

  @Test
  public void testPopArray() {
    doTestScalarVariableValue();
  }

  @Test
  public void testShiftArrayReturn() {
    doTestScalarVariableValue();
  }

  @Test
  public void testPopArrayReturn() {
    doTestScalarVariableValue();
  }

  @Test
  public void testPushValue() {
    doTestArrayVariableValue();
  }

  @Test
  public void testPushValueArrayEmpty() {
    doTestArrayVariableValue();
  }

  @Test
  public void testPushValueArray() {
    doTestArrayVariableValue();
  }

  @Test
  public void testPushValueArrayResult() {
    doTestScalarVariableValue();
  }

  @Test
  public void testPushValueMulti() {
    doTestArrayVariableValue();
  }

  @Test
  public void testPushValueNonEmpty() {
    doTestArrayVariableValue();
  }

  @Test
  public void testPushValueArrayNonEmpty() {
    doTestArrayVariableValue();
  }

  @Test
  public void testPushValueMultiNonEmpty() {
    doTestArrayVariableValue();
  }

  @Test
  public void testUnshiftValue() {
    doTestArrayVariableValue();
  }

  @Test
  public void testUnshiftValueArrayEmpty() {
    doTestArrayVariableValue();
  }

  @Test
  public void testUnshiftValueArray() {
    doTestArrayVariableValue();
  }

  @Test
  public void testUnshiftValueArrayResult() {
    doTestScalarVariableValue();
  }

  @Test
  public void testUnshiftValueMulti() {
    doTestArrayVariableValue();
  }

  @Test
  public void testUnshiftValueNonEmpty() {
    doTestArrayVariableValue();
  }

  @Test
  public void testUnshiftValueArrayNonEmpty() {
    doTestArrayVariableValue();
  }

  @Test
  public void testUnshiftValueMultiNonEmpty() {
    doTestArrayVariableValue();
  }

  @Test
  public void testArgumentResolve() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArgumentAnnotated() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArgumentFirst() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArgumentSelf() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArgumentTyped() {
    doTestScalarVariableValue();
  }

  @Test
  public void testBlessContext() {
    doTestScalarVariableValue();
  }

  @Test
  public void testBlessContextDeferred() {
    doTestScalarVariableValue();
  }

  @Test
  public void testBlessSimple() {
    doTestScalarVariableValue();
  }

  @Test
  public void testBlessSimpleDeferred() {
    doTestScalarVariableValue();
  }

  @Test
  public void testScalarDerefInplace() {
    doTestScalarVariableValue();
  }

  @Test
  public void testScalarDerefInplaceDeep() {
    doTestScalarVariableValue();
  }

  @Test
  public void testScalarDerefInplaceIndirect() {
    doTestScalarVariableValue();
  }

  @Test
  public void testScalarDerefResolve() {
    doTestScalarVariableValue();
  }

  @Test
  public void testScalarDerefResolveIndirect() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayAppendOneOf() {
    doTestArrayVariableValue();
  }

  @Test
  public void testArrayAppendOneOfUnknown() {
    doTestArrayVariableValue();
  }

  @Test
  public void testArrayPrependOneOf() {
    doTestArrayVariableValue();
  }

  @Test
  public void testArrayInsertOneOf() {
    doTestArrayVariableValue();
  }

  @Test
  public void testArrayElementFromSliceMultiOneOf() {
    doTestScalarVariableValue();
  }

  @Test
  public void testHashSlice() {
    doTestArrayVariableValue();
  }

  @Test
  public void testHashSliceMulti() {
    doTestArrayVariableValue();
  }

  @Test
  public void testHashSliceMultiOneOf() {
    doTestArrayVariableValue();
  }

  @Test
  public void testHashSliceMultiOneOfDeferred() {
    doTestArrayVariableValue();
  }

  @Test
  public void testArraySlice() {
    doTestArrayVariableValue();
  }

  @Test
  public void testArraySliceMulti() {
    doTestArrayVariableValue();
  }

  @Test
  public void testArraySliceMultiAll() {
    doTestArrayVariableValue();
  }

  @Test
  public void testArraySliceMultiAllDeferred() {
    doTestArrayVariableValue();
  }

  @Test
  public void testArraySliceMultiOneOf() {
    doTestArrayVariableValue();
  }

  @Test
  public void testArraySliceMultiOneOfDeferred() {
    doTestArrayVariableValue();
  }

  @Test
  public void testArithmeticNegation() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArithmeticNegationDouble() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArithmeticNegationString() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArithmeticNegationStringMinus() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArithmeticNegationStringPlus() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArithmeticNegationUndef() {
    doTestScalarVariableValue();
  }

  @Test
  public void testScalarOneOf() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayElementOneOf() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayElementOneOfDiffSizes() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayElementOneOfDiffSizesNegative() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayElementOneOfEmpty() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayElementOneOfNegative() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayElementOneOfNegativeEmpty() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayElementOneOfDeferred() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayElement() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayElementUndef() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayElementUnknown() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayElementFirst() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayElementFirstNegative() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayElementNegative() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayElementOOB() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayElementOOBNegative() {
    doTestScalarVariableValue();
  }

  @Test
  public void testVariableAnnotationVariant() {
    // fixme this behviour is questionable, see commit message
    doTestScalarVariableValue();
  }

  @Test
  public void testVariableDeclarationVariant() {
    doTestScalarVariableValue();
  }

  @Test
  public void testVariableAnnotationVariantTransient() {
    doTestScalarVariableValue();
  }

  @Test
  public void testVariableAnnotationVariantTransientMixed() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArbitraryConstructor() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArbitraryConstructorStatic() {
    // fixme this behaviour is questionable, see commit message
    doTestScalarVariableValue();
  }

  @Test
  public void testArbitraryConstructors() {
    doTestScalarVariableValue();
  }

  @Test
  public void testConstantSingle() {
    doTestScalarVariableValue();
  }

  @Test
  public void testConstantMulti() {
    doTestScalarVariableValue();
  }

  @Test
  public void testConstantMultiImport() {
    myFixture.copyFileToProject("constantMultiImportFile.pl");
    doTestScalarVariableValue();
  }

  @Test
  public void testShadowedValue() {
    doTestScalarVariableValue();
  }

  @Test
  public void testShadowedValues() {
    doTestScalarVariableValue();
  }

  @Test
  public void testSubExtractorDirect() {
    doTestScalarVariableValue();
  }

  @Test
  public void testSubExtractorDirectObject() {
    doTestScalarVariableValue();
  }

  @Test
  public void testSubExtractorIndirect() {
    doTestScalarVariableValue();
  }

  @Test
  public void testSubExtractorIndirectOtherFile() {
    PerlSharedSettings.getInstance(getProject()).SIMPLE_MAIN_RESOLUTION = false;
    myFixture.copyFileToProject("extractorFunctions.pl");
    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
    doTestScalarVariableValue();
  }

  @Test
  public void testCrossSubVariable() {
    doTestScalarVariableValue();
  }

  @Test
  public void testCrossSubVariableDynamic() {
    doTestScalarVariableValue();
  }

  @Test
  public void testCrossSubVariableDynamicClosure() {
    doTestScalarVariableValue();
  }

  @Test
  public void testCrossSubVariableBuiltIn() {
    doTestScalarVariableValue();
  }

  @Test
  public void testScalarExpr() {
    doTestScalarVariableValue();
  }

  @Test
  public void testScalarExprTransparent() {
    doTestScalarVariableValue();
  }

  @Test
  public void testCallScalar() {
    doTestScalarVariableValue();
  }

  @Test
  public void testCallArray() {
    doTestArrayVariableValue();
  }

  @Test
  public void testCallHash() {
    doTestHashVariableValue();
  }

  @Test
  public void testCallScalarFancy() {
    doTestScalarVariableValue();
  }

  @Test
  public void testCallArrayFancy() {
    doTestArrayVariableValue();
  }

  @Test
  public void testCallHashFancy() {
    doTestHashVariableValue();
  }

  @Test
  public void testScalarOtherScope() {
    doTestScalarVariableValue();
  }

  @Test
  public void testHashToScalar() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayToScalar() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayToHashAmbiguous() {
    doTestHashVariableValue();
  }

  @Test
  public void testArrayToHashAmbiguousDeferred() {
    doTestHashVariableValue();
  }

  @Test
  public void testArrayToScalarAmbiguous() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayToScalarAmbiguousDeferred() {
    doTestScalarVariableValue();
  }

  @Test
  public void testScalarToArray() {
    doTestArrayVariableValue();
  }

  @Test
  public void testHashElement() {
    doTestScalarVariableValue();
  }

  @Test
  public void testHashElementDeferredKey() {
    doTestScalarVariableValue();
  }

  @Test
  public void testHashElementAmbiguousBoth() {
    doTestScalarVariableValue();
  }

  @Test
  public void testHashElementAmbiguousBothDeferred() {
    doTestScalarVariableValue();
  }

  @Test
  public void testHashElementAmbiguousKey() {
    doTestScalarVariableValue();
  }

  @Test
  public void testHashElementAmbiguousKeyOneMissing() {
    doTestScalarVariableValue();
  }

  @Test
  public void testHashElementAmbiguousHashOneMissing() {
    doTestScalarVariableValue();
  }

  @Test
  public void testHashElementVariable() {
    doTestScalarVariableValue();
  }

  @Test
  public void testHashElementDeferredFailed() {
    doTestScalarVariableValue();
  }

  @Test
  public void testHashElementDeferredSuccess() {
    doTestScalarVariableValue();
  }

  @Test
  public void testHashMerge() {
    doTestHashVariableValue();
  }

  @Test
  public void testHashUnmerged() {
    doTestHashVariableValue();
  }

  @Test
  public void testArrayMerge() {
    doTestArrayVariableValue();
  }

  @Test
  public void testArrayUnmerged() {
    doTestArrayVariableValue();
  }

  @Test
  public void testScalarRef() {
    doTestScalarVariableValue();
  }

  @Test
  public void testScalarRefStatic() {
    doTestScalarVariableValue();
  }

  @Test
  public void testHashRef() {
    doTestScalarVariableValue();
  }

  @Test
  public void testHashRefFromHash() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayRef() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayRefFromArray() {
    doTestScalarVariableValue();
  }

  @Test
  public void testArrayList() {
    doTestArrayVariableValue();
  }

  @Test
  public void testArrayListJoin() {
    doTestArrayVariableValue();
  }

  @Test
  public void testArrayFromHash() {
    doTestArrayVariableValue();
  }

  @Test
  public void testHashList() {
    doTestHashVariableValue();
  }

  @Test
  public void testHashListJoin() {
    doTestHashVariableValue();
  }

  @Test
  public void testHashFromArray() {
    doTestHashVariableValue();
  }

  @Test
  public void testArrayStringList() {
    doTestArrayVariableValue();
  }

  @Test
  public void testHashStringList() {
    doTestHashVariableValue();
  }

  @Test
  public void testBuiltIn() {
    doTestScalarVariableValue();
  }

  @Test
  public void testDeclarationSingle() {
    doTestScalarVariableValue();
  }

  @Test
  public void testDeclarationMulti() {
    doTestScalarVariableValue();
  }

  @Test
  public void testDeclarationAssignmentNew() {
    doTestScalarVariableValue();
  }

  @Test
  public void testVariableBeforeAssignment() {
    doTestScalarVariableValue();
  }

  @Test
  public void testVariableAfterAssignment() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedSingleInside() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedSingle() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedMulti() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedOverrideExplicitByExplicit() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedOverrideExplicitByWildcard() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedOverrideWildcardByExplicit() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedOverrideWildcardByWildcard() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedWithNamesFirst() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedIterable() {
    doTestArrayVariableValue();
  }

  @Test
  public void testAnnotatedIterableNot() {
    doTestArrayVariableValue();
  }

  @Test
  public void testAnnotatedIterableWildcard() {
    doTestArrayVariableValue();
  }

  @Test
  public void testAnnotatedIterator() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedIteratorNot() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedIteratorWildcard() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedSignatureSubMultiFirst() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedSignatureSubMultiSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedSignatureSubExactFirst() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedSignatureSubExactSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedSignatureSubWildcardFirst() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedSignatureSubWildcardSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedWithNamesSecond() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedWithNamesThird() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedWithName() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedWithWrongName() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedMultiNonFirst() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedConcurrentStatement() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedConcurrentStatementOuter() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedConcurrentRealTypeInside() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedConcurrentRealTypeMulti() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedConcurrentRealTypeSingle() {
    doTestScalarVariableValue();
  }

  @Test
  public void testAnnotatedConcurrentRealTypeWins() {
    doTestScalarVariableValue();
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
