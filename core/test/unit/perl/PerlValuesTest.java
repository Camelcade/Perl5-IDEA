/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

/**
 * Created by hurricup on 02.04.2016.
 */
public class PerlValuesTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/unit/perl/perlValues";
  }

  public void testShadowedValue() {doTest();}

  public void testShadowedValues() {doTest();}

  public void testSubExtractorDirect() {doTest();}

  public void testSubExtractorDirectObject() {doTest();}

  public void testSubExtractorIndirect() {doTest();}

  public void testSubExtractorIndirectOtherFile() {
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
