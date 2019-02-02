/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package annotator;

import base.PerlLightTestCase;
import com.intellij.spellchecker.inspections.SpellCheckingInspection;
import com.intellij.testFramework.ExpectedHighlightingData;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.inspections.*;
import com.perl5.lang.perl.parser.moose.idea.inspections.MooseMultiAttributeAccessorInspection;
import com.perl5.lang.perl.psi.PerlFile;

/**
 * Created by hurricup on 09.11.2016.
 */
public class PerlAnnotatorTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/annotator/perl";
  }

  public void testDeleteInList() {doTestUnreachableCode();}

  public void testUnreachableReturnWithLpLogic() {doTestUnreachableCode();}

  public void testIssue1884() {doTestHashLoopInspection();}

  public void testUnreachableGotoLabel() {doTestUnreachableCode();}

  public void testUnreachableDieWithHeredoc() {doTestUnreachableCode();}

  public void testUnreachableDbReturn() {doTestUnreachableCode();}

  public void testUnreachableInnerSub() {doTestUnreachableCode();}

  public void testUnreachableDereference() {doTestUnreachableCode();}

  public void testUnreachableCommaSequence() {doTestUnreachableCode();}

  public void testUnreachableCommaSequenceConditional() {doTestUnreachableCode();}

  public void testUnreachableCommaSequenceParens() {doTestUnreachableCode();}

  public void testUnreachableCommaSequenceParensConditional() {doTestUnreachableCode();}

  private void doTestUnreachableCode() {
    doInspectionTest(PerlUnreachableCodeInspection.class);
  }

  public void testLoopControlInspection() {doTestLoopControlInspection();}

  public void testLoopControlInspectionInFakeContainers() {doTestLoopControlInspection();}

  public void testLoopControlTryCatch() {doTestLoopControlInspection();}

  private void doTestLoopControlInspection() {
    doInspectionTest(PerlLoopControlInspection.class);
  }

  private void doTestUseVarsInspection() {doInspectionTest(PerlUseVarsInspection.class);}

  public void testLoopInHashInspection() {doTestHashLoopInspection();}

  private void doTestHashLoopInspection() {
    doInspectionTest(PerlHashLoopInspection.class);
  }

  public void testUseVarsInspection() {doTestUseVarsInspection();}

  public void testUseVarsEmptyInspection() {doTestUseVarsInspection();}

  public void testIssue1723() {doShadowingTest();}

  public void testIssue1726() {doTestUnresolvedSubInspection();}

  public void testUseVarsShadowing() {doShadowingTest();}

  public void testUseVarsShadowingReversal() {doShadowingTest();}

  public void testCaptureGroupsScalarsUnresolved() {doTestUnresolvedVariableInspection();}

  public void testCaptureGroupsScalars() {doAnnotatorTest();}

  public void testBuiltInVariables() {doAnnotatorTest();}

  // duplicates caused by injection (double check)
  public void testSpellChecker() {
    ExpectedHighlightingData.expectedDuplicatedHighlighting(() -> doInspectionTest(SpellCheckingInspection.class));
  }

  public void testBuiltInWithShadowing() {doAnnotatorTest();}

  public void testBuiltInArray() {doAnnotatorTest();}

  public void testBuiltInHash() {doAnnotatorTest();}

  public void testSignaturesShadowing() {doShadowingTest();}

  public void testVariableShadowingInspection() {doShadowingTest();}

  public void testVariableShadowingBuiltIn() {doShadowingTest();}

  public void testUnusedLexicalVariableInspection() {doInspectionTest(PerlUnusedLexicalVariableInspection.class);}

  private void doTestUnresolvedVariableInspection() {
    doInspectionTest(PerlUnresolvedVariableInspection.class);
  }

  public void testUnresolvedVariableInspection() {doTestUnresolvedVariableInspection();}

  public void testUnusedGlobalVariableInspection() {doInspectionTest(PerlUnusedGlobalVariableInspection.class);}

  public void testFileLevelVariableInspection() {doInspectionTest(PerlFileLevelVariableInspection.class);}

  public void testBuiltInVariableRedeclarationInspection() {doInspectionTest(PerlBuiltinVariableRedeclarationInspection.class);}

  public void testCpanfile() {
    initWithCpanFile();
    assertInstanceOf(myFixture.getFile(), PerlFile.class);
    addVirtualFileFilter();
    myFixture.enableInspections(PerlUseStrictInspection.class, PerlUseWarningsInspection.class);
    myFixture.checkHighlighting(true, false, false);
    removeVirtualFileFilter();
  }

  public void testUseStrictInspection() {doInspectionTest(PerlUseStrictInspection.class);}

  public void testUseWarningsInspection() {doInspectionTest(PerlUseWarningsInspection.class);}

  public void testUnusedTypeGlobInspection() {doInspectionTest(PerlUnusedTypeGlobInspection.class);}

  public void testUnusedSubInspection() {doInspectionTest(PerlUnusedSubInspection.class);}

  public void testUnresolvedSubInspection() {doTestUnresolvedSubInspection();}

  private void doTestUnresolvedSubInspection() {
    doInspectionTest(PerlUnresolvedSubInspection.class);
  }

  public void testSimpleMainResolutionTrue() {doTestSimpleMainResolution(true);}

  public void testSimpleMainResolutionFalse() {doTestSimpleMainResolution(false);}

  private void doTestSimpleMainResolution(boolean optionValue) {
    PerlSharedSettings.getInstance(getProject()).SIMPLE_MAIN_RESOLUTION = optionValue;
    doInspectionTest(PerlMultipleSubDefinitionsInspection.class);
  }

  public void testMultipleSubsDefinitionsInspection() {doInspectionTest(PerlMultipleSubDefinitionsInspection.class);}

  public void testMultipleSubsDeclarationsInspection() {doInspectionTest(PerlMultipleSubDeclarationsInspection.class);}

  public void testRedundantNamespaceInspection() {doInspectionTest(PerlRedundantNamespaceInspection.class);}

  public void testUnresolvedPackageFileInspection() {doInspectionTest(PerlUnresolvedPackageFileInspection.class);}

  public void testUnresolvedNamespaceInspection() {doTestUnresolvedNamespaceInspection();}

  public void testIssue1733() {
    doTestUnresolvedNamespaceInspection();
  }

  private void doTestUnresolvedNamespaceInspection() {
    doInspectionTest(PerlUnresolvedNamespaceInspection.class);
  }

  public void testMultipleNamespaceDefinitionInspection() {doInspectionTest(PerlMultipleNamespaceDefinitionsInspection.class);}

  public void testClashedNamespacesInspection() {doInspectionTest(PerlClashedNamespacesInspection.class);}

  public void testUnresolvedLabelInspection() {doInspectionTest(PerlUnresolvedLabelInspection.class);}

  /*
  public void testIdentifierInspection() {
    initWithFileSmart();
    getFile().getVirtualFile().setCharset(CharsetToolkit.US_ASCII_CHARSET);
    myFixture.enableInspections(PerlIdentifierInspection.class);
    myFixture.checkHighlighting(true, false, false);
  }
  */

  public void testFancyMethodCall() {doInspectionTest(PerlFancyMethodCallInspection.class);}

  public void testMooseAttributesDeprecation() {doDeprecationTest();}

  public void testNsRecursiveInheritance1() {doInspectionTest(PerlNamespaceRecursiveInheritanceInspection.class);}

  public void testNsRecursiveInheritance2() {doInspectionTest(PerlNamespaceRecursiveInheritanceInspection.class);}

  public void testNsRecursiveInheritance3() {doInspectionTest(PerlNamespaceRecursiveInheritanceInspection.class);}

  public void testNamespaceDeprecation() {doDeprecationTest();}

  public void testMojoAttrsDeprecation() {doDeprecationTest();}

  public void testConstants() {
    doAnnotatorTest();
  }

  public void testIncorrectConstants() { doAnnotatorTest();}

  public void testExceptionClass() {doAnnotatorTest();}

  public void testDeprecations() {
    doDeprecationTest();
  }

  public void testExceptionClassAliasDeprecation() {
    doDeprecationTest();
  }

  // Caused by multiple entities created from one identifier
  public void testClassAccessorDeprecation() {
    ExpectedHighlightingData.expectedDuplicatedHighlighting(this::doDeprecationTest);
  }

  public void testExceptionClassDeprecation() {
    doDeprecationTest();
  }

  public void testUnresolvedBuiltIns() {
    doInspectionTest(PerlUnresolvedSubInspection.class);
  }

  private void doDeprecationTest() {
    doInspectionTest(PerlDeprecatedInspection.class);
  }

  private void doShadowingTest() {
    doInspectionTest(PerlVariableShadowingInspection.class);
  }

  public void testMooseAttrAccessorsHandles() {
    doMooseMultiAttributesAccessorsTest();
  }

  public void testMooseAttrsAccessorsHandles() {
    doMooseMultiAttributesAccessorsTest();
  }

  private void doMooseMultiAttributesAccessorsTest() {
    doInspectionTest(MooseMultiAttributeAccessorInspection.class);
  }
}
