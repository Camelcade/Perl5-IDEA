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

package annotator;


import base.PerlLightTestCase;
import com.intellij.spellchecker.inspections.SpellCheckingInspection;
import com.intellij.testFramework.ExpectedHighlightingData;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.inspections.*;
import com.perl5.lang.perl.parser.moose.idea.inspections.MooseMultiAttributeAccessorInspection;
import com.perl5.lang.perl.psi.PerlFile;
import org.junit.Test;
public class PerlAnnotatorTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/annotator/perl";
  }

  @Test
  public void testTruthinessScalar() {doTruthinessTest();}

  @Test
  public void testTruthinessScalarCast() {doTruthinessTest();}

  private void doTruthinessTest() {
    doInspectionTest(PerlTruthinessInspection.class);
  }

  @Test
  public void testIssue1884() {doTestHashLoopInspection();}

  @Test
  public void testLoopControlInspection() {doTestLoopControlInspection();}

  @Test
  public void testLoopControlInspectionInFakeContainers() {doTestLoopControlInspection();}

  @Test
  public void testLoopControlTryCatch() {doTestLoopControlInspection();}

  private void doTestLoopControlInspection() {
    doInspectionTest(PerlLoopControlInspection.class);
  }

  private void doTestUseVarsInspection() {doInspectionTest(PerlUseVarsInspection.class);}

  @Test
  public void testLoopInHashInspection() {doTestHashLoopInspection();}

  private void doTestHashLoopInspection() {
    doInspectionTest(PerlHashLoopInspection.class);
  }

  @Test
  public void testUseVarsInspection() {doTestUseVarsInspection();}

  @Test
  public void testUseVarsEmptyInspection() {doTestUseVarsInspection();}

  @Test
  public void testIssue1723() {doShadowingTest();}

  @Test
  public void testIssue1726() {doTestUnresolvedSubInspection();}

  @Test
  public void testUseVarsShadowing() {doShadowingTest();}

  @Test
  public void testUseVarsShadowingReversal() {doShadowingTest();}

  @Test
  public void testCaptureGroupsScalarsUnresolved() {doTestUnresolvedVariableInspection();}

  @Test
  public void testCaptureGroupsScalars() {doAnnotatorTest();}

  @Test
  public void testBuiltInVariables() {doAnnotatorTest();}

  // duplicates caused by injection (double check)
  @Test
  public void testSpellChecker() {
    ExpectedHighlightingData.expectedDuplicatedHighlighting(() -> doInspectionTest(SpellCheckingInspection.class));
  }

  @Test
  public void testBuiltInWithShadowing() {doAnnotatorTest();}

  @Test
  public void testBuiltInArray() {doAnnotatorTest();}

  @Test
  public void testBuiltInHash() {doAnnotatorTest();}

  @Test
  public void testSignaturesShadowing() {doShadowingTest();}

  @Test
  public void testVariableShadowingInspection() {doShadowingTest();}

  @Test
  public void testVariableShadowingBuiltIn() {doShadowingTest();}

  @Test
  public void testUnusedLexicalVariableInspection() {doInspectionTest(PerlUnusedLexicalVariableInspection.class);}

  private void doTestUnresolvedVariableInspection() {
    doInspectionTest(PerlUnresolvedVariableInspection.class);
  }

  @Test
  public void testUnresolvedVariableInspection() {doTestUnresolvedVariableInspection();}

  @Test
  public void testUnusedGlobalVariableInspection() {doInspectionTest(PerlUnusedGlobalVariableInspection.class);}

  @Test
  public void testFileLevelVariableInspection() {doInspectionTest(PerlFileLevelVariableInspection.class);}

  @Test
  public void testBuiltInVariableRedeclarationInspection() {doInspectionTest(PerlBuiltinVariableRedeclarationInspection.class);}

  @Test
  public void testCpanfile() {
    initWithCpanFile();
    assertInstanceOf(myFixture.getFile(), PerlFile.class);
    addVirtualFileFilter();
    myFixture.enableInspections(PerlUseStrictInspection.class, PerlUseWarningsInspection.class);
    myFixture.checkHighlighting(true, false, false);
    removeVirtualFileFilter();
  }

  @Test
  public void testUseStrictInspection() {doInspectionTest(PerlUseStrictInspection.class);}

  @Test
  public void testUseWarningsInspection() {doInspectionTest(PerlUseWarningsInspection.class);}

  @Test
  public void testUnusedTypeGlobInspection() {doInspectionTest(PerlUnusedTypeGlobInspection.class);}

  @Test
  public void testUnusedSubInspection() {doInspectionTest(PerlUnusedSubInspection.class);}

  @Test
  public void testUnresolvedSubInspection() {doTestUnresolvedSubInspection();}

  private void doTestUnresolvedSubInspection() {
    doInspectionTest(PerlUnresolvedSubInspection.class);
  }

  @Test
  public void testSimpleMainResolutionTrue() {doTestSimpleMainResolution(true);}

  @Test
  public void testSimpleMainResolutionFalse() {doTestSimpleMainResolution(false);}

  private void doTestSimpleMainResolution(boolean optionValue) {
    myFixture.copyFileToProject("simpleMainResolutionDupe.pl");
    PerlSharedSettings.getInstance(getProject()).SIMPLE_MAIN_RESOLUTION = optionValue;
    doInspectionTest(PerlMultipleSubDefinitionsInspection.class);
  }

  @Test
  public void testMultipleSubsDefinitionsInspection() {doInspectionTest(PerlMultipleSubDefinitionsInspection.class);}

  @Test
  public void testMultipleSubsDeclarationsInspection() {doInspectionTest(PerlMultipleSubDeclarationsInspection.class);}

  @Test
  public void testRedundantNamespaceInspection() {doInspectionTest(PerlRedundantNamespaceInspection.class);}

  @Test
  public void testUnresolvedPackageFileInspection() {doTestUnresolvedPackageFile();}

  @Test
  public void testPackageFileReference() {doTestUnresolvedPackageFile();}

  private void doTestUnresolvedPackageFile() {
    doInspectionTest(PerlUnresolvedPackageFileInspection.class);
  }

  @Test
  public void testUnresolvedNamespaceInspection() {doTestUnresolvedNamespaceInspection();}

  @Test
  public void testIssue1733() {
    doTestUnresolvedNamespaceInspection();
  }

  private void doTestUnresolvedNamespaceInspection() {
    doInspectionTest(PerlUnresolvedNamespaceInspection.class);
  }

  @Test
  public void testMultipleNamespaceDefinitionInspection() {doInspectionTest(PerlMultipleNamespaceDefinitionsInspection.class);}

  @Test
  public void testClashedNamespacesInspection() {doInspectionTest(PerlClashedNamespacesInspection.class);}

  @Test
  public void testUnresolvedLabelInspection() {doInspectionTest(PerlUnresolvedLabelInspection.class);}

  /*
  @Test
    public void testIdentifierInspection() {
    initWithFileSmart();
    getFile().getVirtualFile().setCharset(CharsetToolkit.US_ASCII_CHARSET);
    myFixture.enableInspections(PerlIdentifierInspection.class);
    myFixture.checkHighlighting(true, false, false);
  }
  */

  @Test
  public void testFancyMethodCall() {doInspectionTest(PerlFancyMethodCallInspection.class);}

  @Test
  public void testMooseAttributesDeprecation() {doDeprecationTest();}

  @Test
  public void testNsRecursiveInheritance1() {doInspectionTest(PerlNamespaceRecursiveInheritanceInspection.class);}

  @Test
  public void testNsRecursiveInheritance2() {doInspectionTest(PerlNamespaceRecursiveInheritanceInspection.class);}

  @Test
  public void testNsRecursiveInheritance3() {doInspectionTest(PerlNamespaceRecursiveInheritanceInspection.class);}

  @Test
  public void testNamespaceDeprecation() {doDeprecationTest();}

  @Test
  public void testSubDeclarationDeprecation() {doDeprecationTest();}

  @Test
  public void testMojoAttrsDeprecation() {doDeprecationTest();}

  @Test
  public void testConstants() {
    doAnnotatorTest();
  }

  @Test
  public void testIncorrectConstants() { doAnnotatorTest();}

  @Test
  public void testExceptionClass() {doAnnotatorTest();}

  @Test
  public void testDeprecations() {
    doDeprecationTest();
  }

  @Test
  public void testExceptionClassAliasDeprecation() {
    doDeprecationTest();
  }

  // Caused by multiple entities created from one identifier
  @Test
  public void testClassAccessorDeprecation() {
    ExpectedHighlightingData.expectedDuplicatedHighlighting(this::doDeprecationTest);
  }

  @Test
  public void testExceptionClassDeprecation() {
    doDeprecationTest();
  }

  @Test
  public void testUnresolvedBuiltIns() {
    doInspectionTest(PerlUnresolvedSubInspection.class);
  }

  private void doDeprecationTest() {
    doInspectionTest(PerlDeprecatedInspection.class);
  }

  private void doShadowingTest() {
    doInspectionTest(PerlVariableShadowingInspection.class);
  }

  @Test
  public void testMooseAttrAccessorsHandles() {
    doMooseMultiAttributesAccessorsTest();
  }

  @Test
  public void testMooseAttrsAccessorsHandles() {
    doMooseMultiAttributesAccessorsTest();
  }

  private void doMooseMultiAttributesAccessorsTest() {
    doInspectionTest(MooseMultiAttributeAccessorInspection.class);
  }
}
