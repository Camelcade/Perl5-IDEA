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

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.spellchecker.inspections.SpellCheckingInspection;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.inspections.*;

/**
 * Created by hurricup on 09.11.2016.
 */
public class PerlAnnotatorTest extends PerlLightCodeInsightFixtureTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/annotator/perl";
  }

  public void testBuiltInVariables() {doAnnotatorTest();}

  public void testSpellChecker() {doInspectionTest(SpellCheckingInspection.class);}

  public void testBuiltInWithShadowing() {doAnnotatorTest();}

  public void testBuiltInArray() {doAnnotatorTest();}

  public void testBuiltInHash() {doAnnotatorTest();}

  public void testSignaturesShadowing() {doShadowingTest();}

  public void testVariableShadowingInspection() {doShadowingTest();}

  public void testVariableShadowingBuiltIn() {doShadowingTest();}


  public void testUnusedLexicalVariableInspection() {doInspectionTest(PerlUnusedLexicalVariableInspection.class);}

  public void testUnresolvedVariableInspection() {doInspectionTest(PerlUnresolvedVariableInspection.class);}

  public void testUnusedGlobalVariableInspection() {doInspectionTest(PerlUnusedGlobalVariableInspection.class);}

  public void testFileLevelVariableInspection() {doInspectionTest(PerlFileLevelVariableInspection.class);}

  public void testBuiltInVariableRedeclarationInspection() {doInspectionTest(PerlBuiltinVariableRedeclarationInspection.class);}

  public void testUseStrictInspection() {doInspectionTest(PerlUseStrictInspection.class);}

  public void testUseWarningsInspection() {doInspectionTest(PerlUseWarningsInspection.class);}

  public void testUnusedTypeGlobInspection() {doInspectionTest(PerlUnusedTypeGlobInspection.class);}

  public void testUnusedSubInspection() {doInspectionTest(PerlUnusedSubInspection.class);}

  public void testUnresolvedSubInspection() {doInspectionTest(PerlUnresolvedSubInspection.class);}

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

  public void testUnresolvedNamespaceInspection() {doInspectionTest(PerlUnresolvedNamespaceInspection.class);}

  public void testMultipleNamespaceDefinitionInspection() {doInspectionTest(PerlMultipleNamespaceDefinitionsInspection.class);}

  public void testClashedNamespacesInspection() {doInspectionTest(PerlClashedNamespacesInspection.class);}

  public void testUnresolvedLabelInspection() {doInspectionTest(PerlUnresolvedLabelInspection.class);}

  public void testIdentifierInspection() {
    initWithFileSmart();
    getFile().getVirtualFile().setCharset(CharsetToolkit.US_ASCII_CHARSET);
    myFixture.enableInspections(PerlIdentifierInspection.class);
    myFixture.checkHighlighting(true, false, false);
  }

  public void testFancyMethodCall() {doInspectionTest(PerlFancyMethodCallInspection.class);}

  public void testMooseAttributesDeprecation() {doDeprecationTest();}

  public void testNsRecursiveInheritance1() {doInspectionTest(PerlNamespaceRecursiveInheritanceInspection.class);}

  public void testNsRecursiveInheritance2() {doInspectionTest(PerlNamespaceRecursiveInheritanceInspection.class);}

  public void testNsRecursiveInheritance3() {doInspectionTest(PerlNamespaceRecursiveInheritanceInspection.class);}

  public void testNamespaceDeprecation() {doDeprecationTest();}

  public void testMojoAttrsDeprecation() {doDeprecationTest();}

  public void testMojoHelperDeprecation() {doDeprecationTest();}

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

  public void testClassAccessorDeprecation() {
    doDeprecationTest();
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
}
