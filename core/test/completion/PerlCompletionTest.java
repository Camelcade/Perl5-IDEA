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

package completion;

import base.PerlLightTestCase;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.lang.perl.idea.project.PerlNamesCache;
import com.perl5.lang.perl.internals.PerlVersion;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 04.03.2016.
 */
public class PerlCompletionTest extends PerlLightTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    PerlNamesCache.getInstance(getProject()).forceCacheUpdate();
  }

  @Override
  protected String getTestDataPath() {
    return "testData/completion/perl";
  }

  public void testImportInSuperClass() {
    doTest();
  }

  public void testTypeGlobAsMethod() {
    doTest();
  }

  public void testTypeGlobAsStatic() {
    doTest();
  }

  public void testFileSpec() {
    withFileSpec();
    doTest();
  }

  public void testTestMoreParameters() {
    withTestMore();
    doTest();
  }

  public void testCpanfile() {
    withCpanfile();
    initWithCpanFile();
    doTestCompletionCheck();
  }

  public void testLog4perlOptions() {doTest();}

  public void testLog4perlEasy() {doTestLog4Perl();}

  public void testLog4perlEasyScalar() {doTestLog4Perl();}

  public void testLog4perlGetLogger() {doTestLog4Perl();}

  public void testLog4perlLevels() {doTestLog4Perl();}

  private void doTestLog4Perl() {
    doTestCompletion((lookup, presentation) -> StringUtil.contains(StringUtil.notNullize(presentation.getTypeText()), "Log4perl"));
  }

  public void testReadonlyBare() {doTest();}

  public void testReadonlyImport() {doTest();}

  public void testReadonlyImported() {doTest();}

  public void testReadonlyMethod() {doTest();}

  public void testTypesStandard() {doTest();}

  public void testVariableSubSignature() {doTest();}

  public void testVariableAnonSubSignature() {doTest();}

  public void testVariableMethodSignature() {doTest();}

  public void testVariableFuncSignature() {doTest();}

  public void testDataPrinter() {doTest();}

  public void testListMoreUtilsParams() {doTest();}

  public void testBuiltInPackageExtension() {doTest();}

  public void testAnnotatedRefMethod() {doTest();}

  public void testWildCardReturns() {doTest();}

  public void testVariableArrayInArray() {doTest();}

  public void testVariableArrayInScalar() {doTest();}

  public void testVariableHashInArray() {doTest();}

  public void testVariableHashInHash() {doTest();}

  public void testVariableHashInScalar() {doTest();}

  public void testVariableScalarLocal() {doTest();}

  public void testVariableScalarMy() {doTest();}

  public void testVariableScalarOur() {doTest();}

  public void testVariableScalarState() {doTest();}

  public void testNotOverridenSubs() {doTest();}

  public void testMooseImports() {doTest();}

  public void testMooseXMethodAttributes() {doTest();}

  public void testMooseXMethodAttributesRole() {doTest();}

  public void testMooseXRoleWithOverloading() {doTest();}

  public void testMooseXRoleParametrized() {doTest();}

  public void testMooseXClassAttribute() {doTest();}

  public void testMooseXTypesCheckedUtilExports() {doTest();}

  public void testMooseUtilTypeConstraints() {doTest();}

  public void testMooseRoleImports() {doTest();}

  public void testMooseAttrs() {doTest();}

  public void testMojoAttrs() {doTest();}


  public void testClassAccessor() {
    doTest();
  }

  public void testExceptionClassAliasLocal() {
    doTest();
  }

  public void testExceptionClassAliasStatic() {
    doTest();
  }

  public void testExceptionClassAliasMethod() {
    doTest();
  }

  public void testLibraryConstants() {
    doTest();
  }

  public void testUsePackageInCurrentDir510() {
    doTest(PerlVersion.V5_10);
  }

  public void testUsePackageInCurrentDir512() {
    doTest(PerlVersion.V5_12);
  }

  public void testUsePackageInCurrentDir514() {
    doTest(PerlVersion.V5_14);
  }

  public void testUsePackageInCurrentDir516() {
    doTest(PerlVersion.V5_16);
  }

  public void testUsePackageInCurrentDir518() {
    doTest(PerlVersion.V5_18);
  }

  public void testUsePackageInCurrentDir520() {
    doTest(PerlVersion.V5_20);
  }

  public void testUsePackageInCurrentDir522() {
    doTest(PerlVersion.V5_22);
  }

  public void testUsePackageInCurrentDir524() {
    doTest(PerlVersion.V5_24);
  }

  public void testUsePackageInCurrentDir526() {
    doTest(PerlVersion.V5_26);
  }

  public void testUsePackageInCurrentDir528() {
    doTest(PerlVersion.V5_28);
  }

  public void doTest(@NotNull PerlVersion version) {
    addCustomPackage();
    setTargetPerlVersion(version);
    initWithFileSmart("usePackageInCurrentDir");
    doTestCompletionCheck("", (__, presentation) -> "MyCustomPackage".equals(presentation.getItemText()));
  }

  public void testExceptionClass() {
    doTest();
  }

  public void testReferenceCompletion() {doTest();}

  public void testCaptureScalar() {doTest();}

  public void testCaptureArray() {doTest();}

  public void testCaptureHash() {doTest();}

  public void testAnnotationReturnsPackage() {
    doTest();
  }

  public void testAnnotationTypePackage() {
    doTest();
  }

  public void testImportSubsParam() {
    doTest();
  }

  public void testImportSubsParamWithoutExport() {
    doTest();
  }

  public void testImportSubsParamWithoutExportOk() {
    doTest();
  }

  public void testImportParamWithoutOption() {
    doTest();
  }

  public void testImportParamWithoutOptionBundles() {
    doTest();
  }

  public void testImportParamWithoutParent() {
    doTest();
  }

  private void doTestExportArray() {
    doTest();
  }

  public void testExportArrayEmpty() {
    doTestExportArray();
  }

  public void testExportArrayNonEmpty() {
    doTestExportArray();
  }

  public void testExportOkArrayEmpty() {
    doTestExportArray();
  }

  public void testExportOkArrayNonEmpty() {
    doTestExportArray();
  }

  public void testPackageToStringQ() {
    doTestStringCompletion();
  }

  public void testPackageToStringQPartial() {
    doTest();
  }

  public void testPackageToStringQQ() {
    doTestStringCompletion();
  }

  public void testPackageToStringQWFirst() {
    doTestStringCompletion();
  }

  public void testPackageToStringQWNonFirst() {
    doTestStringCompletion();
  }

  private void doTestStringCompletion() {
    doTest();
  }

  public void testUnresolvedSubDeclaration() {
    doTest();
  }

  public void testUnresolvedSubDefinition() {
    doTest();
  }

  public void testConstants() {
    doTest();
  }

  public void testConstantsWithPackage() {
    doTest();
  }

  public void testVariableInDeclaration() {
    doTest();
  }

  public void testImportSubs() {
    doTest();
  }

  public void testImportHashes() {
    doTest();
  }

  public void testImportArrays() {
    doTest();
  }

  public void testImportScalars() {
    doTest();
  }

  public void testImportDancer() {
    doTest();
  }

  public void testImportDancer2() {
    doTest();
  }

  public void testImportPosixOk() {
    doTest();
  }

  public void testImportPosix() {
    doTest();
  }

  public void testImportPosixVar() {
    doTest();
  }

  public void testLexicalMy() {
    doTest();
  }

  public void testLexicalState() {
    doTest();
  }

  public void testLexicalOur() {
    doTest();
  }

  public void testSameStatementSimple() {
    doTest();
  }

  public void testSameStatementMap() {
    doTest();
  }

  public void testHeredocOpenerBare() {
    doTestHeredoc();
  }

  public void testHeredocOpenerBackref() {
    doTestHeredoc();
  }

  public void testHeredocOpenerQQ() {
    doTestHeredoc();
  }

  public void testHeredocOpenerSQ() {
    doTestHeredoc();
  }

  public void testHeredocOpenerXQ() {
    doTestHeredoc();
  }

  private void doTestHeredoc() {
    doTest();
  }

  public void testRefTypes() {
    doTestCompletionFromText("my $var = '<caret>'");
  }

  public void testHashIndexBare() {
    doTestCompletionFromText("$$a{testindex}; $b->{<caret>}");
  }


  public void testAnnotation() {
    doTest();
  }

  public void testInjectMarkers() {
    doTest();
  }


  public void testNextLabels() {
    doTest();
  }

  public void testGotoLabels() {
    doTest();
  }

  public void testPackageUse() {
    doTest();
  }

  public void testPackageNo() {
    doTest();
  }

  public void testPackageRequire() {
    doTest();
  }

  public void testPackageMy() {
    doTest();
  }

  public void testPackageOur() {
    doTest();
  }

  public void testPackageState() {
    doTest();
  }

  public void testTryCatch() {
    doTest();
  }

  public void testReturnsPackageTag() {doTest();}

  public void testPackageVarTypePerl() {doTest();}

  public void testPackageVarTypeAnnotation() {doTest();}

  public void testLazyExportOkQw() {doTest();}

  public void testGlobSlots() {doTest();}

  private void doTest() {
    doTestCompletion();
  }
}
