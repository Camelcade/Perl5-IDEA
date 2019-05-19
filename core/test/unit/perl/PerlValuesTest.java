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
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;


public class PerlValuesTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/unit/perl/perlValues";
  }

  public void testBlessContext() {doTest();}

  public void testBlessContextDeferred() {doTest();}

  public void testBlessSimple() {doTest();}

  public void testBlessSimpleDeferred() {doTest();}

  public void testScalarDerefInplace() {doTest();}

  public void testScalarDerefInplaceDeep() {doTest();}

  public void testScalarDerefInplaceIndirect() {doTest();}

  public void testScalarDerefResolve() {doTest();}

  public void testScalarDerefResolveIndirect() {doTest();}

  public void testArrayAppendOneOf() {doTest();}

  public void testArrayAppendOneOfUnknown() {doTest();}

  public void testArrayPrependOneOf() {doTest();}

  public void testArrayInsertOneOf() {doTest();}

  public void testArrayElementFromSliceMultiOneOf() {doTest();}

  public void testHashSlice() {doTest();}

  public void testHashSliceMulti() {doTest();}

  public void testHashSliceMultiOneOf() {doTest();}

  public void testHashSliceMultiOneOfDeferred() {doTest();}

  public void testArraySlice() {doTest();}

  public void testArraySliceMulti() {doTest();}

  public void testArraySliceMultiAll() {doTest();}

  public void testArraySliceMultiAllDeferred() {doTest();}

  public void testArraySliceMultiOneOf() {doTest();}

  public void testArraySliceMultiOneOfDeferred() {doTest();}

  public void testArithmeticNegation() {doTest();}

  public void testArithmeticNegationDouble() {doTest();}

  public void testArithmeticNegationString() {doTest();}

  public void testArithmeticNegationStringMinus() {doTest();}

  public void testArithmeticNegationStringPlus() {doTest();}

  public void testArithmeticNegationUndef() {doTest();}

  public void testScalarOneOf() {doTest();}

  public void testArrayElementOneOf() { doTest(); }

  public void testArrayElementOneOfDeferred() { doTest(); }

  public void testArrayElement() { doTest(); }

  public void testArrayElementUndef() { doTest(); }

  public void testArrayElementUnknown() { doTest(); }

  public void testArrayElementFirst() { doTest(); }

  public void testArrayElementFirstNegative() { doTest(); }

  public void testArrayElementNegative() { doTest(); }

  public void testArrayElementOOB() { doTest(); }

  public void testArrayElementOOBNegative() { doTest(); }

  public void testVariableAnnotationVariant() {
    // fixme this behviour is questionable, see commit message
    doTest();
  }

  public void testVariableDeclarationVariant() {doTest();}

  public void testVariableAnnotationVariantTransient() {doTest();}

  public void testVariableAnnotationVariantTransientMixed() {doTest();}

  public void testArbitraryConstructor() {doTest();}

  public void testArbitraryConstructorStatic() {
    // fixme this behaviour is questionable, see commit message
    doTest();
  }

  public void testArbitraryConstructors() {doTest();}

  public void testConstantSingle() {doTest();}

  public void testConstantMulti() {doTest();}

  public void testConstantMultiImport() {
    myFixture.copyFileToProject("constantMultiImportFile.pl");
    doTest();
  }

  public void testShadowedValue() {doTest();}

  public void testShadowedValues() {doTest();}

  public void testSubExtractorDirect() {doTest();}

  public void testSubExtractorDirectObject() {doTest();}

  public void testSubExtractorIndirect() {doTest();}

  public void testSubExtractorIndirectOtherFile() {
    PerlSharedSettings.getInstance(getProject()).SIMPLE_MAIN_RESOLUTION = false;
    myFixture.copyFileToProject("extractorFunctions.pl");
    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
    doTest();
  }

  public void testCrossSubVariable() {doTest();}

  public void testCrossSubVariableDynamic() {doTest();}

  public void testCrossSubVariableDynamicClosure() {doTest();}

  public void testCrossSubVariableBuiltIn() {doTest();}

  public void testScalarExpr() {doTest();}

  public void testScalarExprTransparent() {doTest();}

  public void testCallScalar() {doTest();}

  public void testCallArray() {doTest();}

  public void testCallHash() {doTest();}

  public void testCallScalarFancy() {doTest();}

  public void testCallArrayFancy() {doTest();}

  public void testCallHashFancy() {doTest();}

  public void testScalarOtherScope() {doTest();}

  public void testHashToScalar() {doTest();}

  public void testArrayToScalar() {doTest();}

  public void testArrayToHashAmbiguous() {doTest();}

  public void testArrayToHashAmbiguousDeferred() {doTest();}

  public void testArrayToScalarAmbiguous() {doTest();}

  public void testArrayToScalarAmbiguousDeferred() {doTest();}

  public void testScalarToArray() {doTest();}

  public void testHashElement() {doTest();}

  public void testHashElementDeferredKey() {doTest();}

  public void testHashElementAmbiguousBoth() {doTest();}

  public void testHashElementAmbiguousBothDeferred() {doTest();}

  public void testHashElementAmbiguousKey() {doTest();}

  public void testHashElementAmbiguousKeyOneMissing() {doTest();}

  public void testHashElementAmbiguousHashOneMissing() {doTest();}

  public void testHashElementVariable() {doTest();}

  public void testHashElementDeferredFailed() {doTest();}

  public void testHashElementDeferredSuccess() {doTest();}

  public void testHashMerge() {doTest();}

  public void testHashUnmerged() {doTest();}

  public void testArrayMerge() {doTest();}

  public void testArrayUnmerged() {doTest();}

  public void testScalarRef() {doTest();}

  public void testScalarRefStatic() {doTest();}

  public void testHashRef() {doTest();}

  public void testHashRefFromHash() {doTest();}

  public void testArrayRef() {doTest();}

  public void testArrayRefFromArray() {doTest();}

  public void testArrayList() {doTest();}

  public void testArrayListJoin() {doTest();}

  public void testArrayFromHash() {doTest();}

  public void testHashList() {doTest();}

  public void testHashListJoin() {doTest();}

  public void testHashFromArray() {doTest();}

  public void testArrayStringList() {doTest();}

  public void testHashStringList() {doTest();}

  public void testBuiltIn() {doTest();}

  public void testDeclarationSingle() {
    doTest();
  }

  public void testDeclarationMulti() {
    doTest();
  }

  public void testDeclarationAssignmentNew() {
    doTest();
  }

  public void testVariableBeforeAssignment() {
    doTest();
  }

  public void testVariableAfterAssignment() {
    doTest();
  }

  public void testAnnotatedSingleInside() {
    doTest();
  }

  public void testAnnotatedSingle() {
    doTest();
  }

  public void testAnnotatedMulti() {
    doTest();
  }

  public void testAnnotatedMultiNonFirst() {
    doTest();
  }

  public void testAnnotatedConcurrentStatement() {
    doTest();
  }

  public void testAnnotatedConcurrentStatementOuter() {
    doTest();
  }

  public void testAnnotatedConcurrentRealTypeInside() {
    doTest();
  }

  public void testAnnotatedConcurrentRealTypeMulti() {
    doTest();
  }

  public void testAnnotatedConcurrentRealTypeSingle() {
    doTest();
  }

  public void testAnnotatedConcurrentRealTypeWins() {
    doTest();
  }

  private void doTest() {
    doTestPerlValue();
  }
}
