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

package resolve;


import base.PerlLightTestCase;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.internals.PerlVersion;
import org.junit.Test;
public class PerlResolveTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "resolve/perl";
  }

  @Test
  public void testDuckCallResolveMultiInSubSecond() { doTest(); }

  @Test
  public void testBuiltinFqn() {
    doTest();
  }

  @Test
  public void testBuiltinImported() {
    doTest();
  }

  @Test
  public void testScalarUtil() {
    withPerl532();
    doTest();
  }

  @Test
  public void testUseMoo() {
    withMoo();
    doTest();
  }

  @Test
  public void testUseMooRole() {
    withMoo();
    doTest();
  }

  @Test
  public void testUseMoose() {
    withMoose();
    doTest();
  }

  @Test
  public void testUseMooseRole() {
    withMoose();
    doTest();
  }

  @Test
  public void testUseMooseUtilTypeConstraints() {
    withMoose();
    doTest();
  }

  @Test
  public void testUseMooseXClassAttirubte() {
    withMooseX();
    doTest();
  }

  @Test
  public void testUseMooseXMethodAttirbutes() {
    withMooseX();
    doTest();
  }

  @Test
  public void testUseMooseXMethodAttirbutesRole() {
    withMooseX();
    doTest();
  }

  @Test
  public void testUseMooseXRoleParametrized() {
    withMooseX();
    doTest();
  }

  @Test
  public void testUseMooseXRoleWithOverloading() {
    withMooseX();
    doTest();
  }

  @Test
  public void testUseMooseXTypesCheckedUtilExports() {
    withMooseX();
    doTest();
  }

  @Test
  public void testUseRoleTiny() {
    withRoleTiny();
    doTest();
  }

  @Test
  public void testAngledHandles() {
    doTest();
  }

  @Test
  public void testFunctionParametersMethodModifiers() {doTest();}

  @Test
  public void testFunctionParametersMethodModifiersSuper() {doTest();}

  @Test
  public void testFunctionParametersFun() {doTest();}

  @Test
  public void testPackageFileReference() {doTest();}

  @Test
  public void testSlices520() {doTest();}

  @Test
  public void testMojoLite() {doTest();}

  @Test
  public void testBlessedInference() {doTest();}

  @Test
  public void testBlessedInferenceContext() {doTest();}

  @Test
  public void testSimpleMainTrue() {doTestSimpleMain(true);}

  @Test
  public void testSimpleMainFalse() { doTestSimpleMain(false); }

  private void doTestSimpleMain(boolean value) {
    myFixture.copyFileToProject("simpleMain2.pl");
    initWithFileSmartWithoutErrors("simpleMain");
    PerlSharedSettings.getInstance(getProject()).SIMPLE_MAIN_RESOLUTION = value;
    checkSerializedReferencesWithFile();
  }

  @Test
  public void testMooseKeywords() {
    withMoose();
    doTest();
  }

  @Test
  public void testLog4perl() {
    withLog4perl();
    doTest();
  }

  @Test
  public void testLog4perlInFooBar() {
    withLog4perl();
    doTest();
  }

  @Test
  public void testTestMoreWithPlan() {
    withTestMore();
    doTest();
  }

  @Test
  public void testCpanfile() {
    addTestLibrary("cpanfile");
    initWithCpanFile();
    checkSerializedReferencesWithFile();
  }

  @Test
  public void testFileSpec() {
    withFileSpec();
    doTest();
  }

  @Test
  public void testPodWeaverTags() {doTest();}

  @Test
  public void testReadonly() {doTest();}

  @Test
  public void testTypesStandard() {doTest();}

  @Test
  public void testIssue1723() {doTest();}

  @Test
  public void testIssue1669() {doTest();}

  @Test
  public void testIssue1646() {doTest();}

  @Test
  public void testRegexpMatchesVariables() {doTest();}

  @Test
  public void testWildCardReturns() {
    doTest();
  }

  @Test
  public void testProperSelfType() {doTest();}

  @Test
  public void testCoreGlobal() {doTest();}

  @Test
  public void testBuiltInNamespaces() {doTest();}

  @Test
  public void testFalsePositiveCore() {doTest();}

  @Test
  public void testSignaturesVariables() {doTest();}

  @Test
  public void testFancyMethodCall() {
    doTest();
  }

  @Test
  public void testMooseAttrs() {doTest();}

  @Test
  public void testMooseDoesIsa() {doTest();}

  @Test
  public void testMojoAttrSmartResolve() {
    doTest();
  }

  @Test
  public void testMojoAttrs() {doTest();}

  @Test
  public void testIssue1483() {doTest();}

  @Test
  public void testClassAccessorLib() {doTest();}

  @Test
  public void testClassAccessor() {doTest();}

  @Test
  public void testExceptionClassFields() {doTestResolve(true);}

  @Test
  public void testExceptionClassAlias() {doTest();}

  @Test
  public void testConstantsWithCollapsedLists() {doTest();}

  @Test
  public void testLibraryNamespace() {doTest();}

  @Test
  public void testLibraryConstant() {doTest();}

  @Test
  public void testExceptionClass() {doTest();}

  @Test
  public void testReferenceResolve() {doTest();}

  @Test
  public void testExportedSubs() {
    doTest();
  }

  @Test
  public void testDerefWithComments() {
    doTest();
  }

  @Test
  public void testStringToPackage() {
    doTest();
  }

  @Test
  public void testConstant() {
    doTest();
  }

  @Test
  public void testConstantEx() {
    doTest();
  }

  @Test
  public void testVariableIsa() {
    doTest();
  }

  @Test
  public void testSequentialHereDocs() {
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
  public void testVariablesAndElements() {
    doTest();
  }

  @Test
  public void testVariableBuiltInComplex() {doTest();}

  @Test
  public void testMainSubs() {
    doTest();
  }

  @Test
  public void testInterpolatedElements() {
    doTest();
  }

  @Test
  public void testSingleWordPackages() {
    doTest();
  }

  @Test
  public void testPackageRanges() {
    doTest();
  }

  @Test
  public void testReturnsPackageTag() {doTest();}

  @Test
  public void testPackageTagVarType() {doTest();}

  @Test
  public void testUsePackage() {
    addCustomPackage();
    setTargetPerlVersion(PerlVersion.V5_10);
    doTest();
    setTargetPerlVersion(PerlVersion.V5_12);
    checkSerializedReferencesWithFile();
    setTargetPerlVersion(PerlVersion.V5_14);
    checkSerializedReferencesWithFile();
    setTargetPerlVersion(PerlVersion.V5_16);
    checkSerializedReferencesWithFile();
    setTargetPerlVersion(PerlVersion.V5_18);
    checkSerializedReferencesWithFile();
    setTargetPerlVersion(PerlVersion.V5_20);
    checkSerializedReferencesWithFile();
    setTargetPerlVersion(PerlVersion.V5_22);
    checkSerializedReferencesWithFile();
    setTargetPerlVersion(PerlVersion.V5_24);
    checkSerializedReferencesWithFile();
    setTargetPerlVersion(PerlVersion.V5_26);
    checkSerializedReferencesWithFile("no_resolve");
  }

  @Test
  public void testLazyExportOkQw() {doTest();}

  @Test
  public void testLazyExportOkQwImport() {
    myFixture.copyFileToProject("lazyExportOkQw.code", "lazyExportOkQw.pl");
    doTest();
  }

  private void doTest() {
    doTestResolve();
  }
}
