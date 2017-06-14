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

import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.extensions.packageprocessor.impl.POSIXExports;
import com.perl5.lang.perl.extensions.packageprocessor.impl.PerlDancer2DSL;
import com.perl5.lang.perl.extensions.packageprocessor.impl.PerlDancerDSL;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import com.perl5.lang.perl.idea.project.PerlNamesCache;
import com.perl5.lang.perl.internals.PerlVersion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 04.03.2016.
 */
public class PerlCompletionTest extends PerlCompletionCodeInsightFixtureTestCase {

  private List<String> SUPER_HERE_DOC_MARKER = Collections.singletonList("MYSUPERMARKER");

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    PerlNamesCache.getInstance(getProject()).forceCacheUpdate();
  }

  @Override
  protected String getTestDataPath() {
    return "testData/completion/perl";
  }

  public void testLibraryConstants() {
    doTestContains("LIBRARY_CONST1",
                   "LIBRARY_CONST2",
                   "LIBRARY_CONST3",
                   "LIBRARY_CONST4");
  }

  public void testUsePackageInCurrentDir() {
    addCustomPackage();
    setTargetPerlVersion(PerlVersion.V5_10);
    doTestPackageFilesAndVersions("MyCustomPackage");
    setTargetPerlVersion(PerlVersion.V5_12);
    doTestPackageFilesAndVersions("MyCustomPackage");
    setTargetPerlVersion(PerlVersion.V5_14);
    doTestPackageFilesAndVersions("MyCustomPackage");
    setTargetPerlVersion(PerlVersion.V5_16);
    doTestPackageFilesAndVersions("MyCustomPackage");
    setTargetPerlVersion(PerlVersion.V5_18);
    doTestPackageFilesAndVersions("MyCustomPackage");
    setTargetPerlVersion(PerlVersion.V5_20);
    doTestPackageFilesAndVersions("MyCustomPackage");
    setTargetPerlVersion(PerlVersion.V5_22);
    doTestPackageFilesAndVersions("MyCustomPackage");
    setTargetPerlVersion(PerlVersion.V5_24);
    doTestPackageFilesAndVersions("MyCustomPackage");
    setTargetPerlVersion(PerlVersion.V5_26);
    doTestPackageFilesAndVersions();
  }

  public void testExceptionClass() {
    doTestContains(
      "Exception1::", "Exception2::", "Exception3::", "Exception4::", "Exception5::",
      "Exception1->", "Exception2->", "Exception3->", "Exception4->", "Exception5->"
    );
  }

  public void testReferenceCompletion() {doTestContains("declared_reference");}

  public void testCaptureScalar() {doTestContains("^CAPTURE", "^CAPTURE", "^CAPTURE_ALL");}

  public void testCaptureArray() {doTestContains("^CAPTURE", "^CAPTURE", "^CAPTURE_ALL");}

  public void testCaptureHash() {doTestContains("^CAPTURE", "^CAPTURE_ALL");}

  public void testAnnotationReturnsPackage() {
    doTest(LIBRARY_PACKAGES);
  }

  public void testAnnotationTypePackage() {
    doTest(LIBRARY_PACKAGES);
  }

  public void testImportSubsParam() {
    doTest("somesub1", "someconst1", "multiconst1", "somesub2", "SOMECONST2", "MULTICONST2");
  }

  private void doTestExportArray() {
    doTest("something", "somethingelse", "singleconst", "multiconst", "MULTICONST2");
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
    doTest(LIBRARY_PM_FILES);
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
    doTest(REF_TYPES, LIBRARY_PACKAGES, Collections.singletonList("Foo::Bar::Bla"));
  }

  public void testMojoliciousHelper() {
    doTest("myhelper", "SUPER::");
  }

  public void testUnresolvedSubDeclaration() {
    doTest("somesubcustom");
  }

  public void testUnresolvedSubDefinition() {
    doTest("somesubcustom");
  }

  public void testConstants() {
    doTestSubs("ALONECONST1", "ALONECONST2", "ALONECONST3", "MULTI_CONST1", "MULTI_CONST2", "MULTI_CONST3");
  }

  public void testConstantsWithPackage() {
    doTest("ALONECONST1", "ALONECONST2", "ALONECONST3", "MULTI_CONST1", "MULTI_CONST2", "MULTI_CONST3");
  }

  public void testVariableInDeclaration() {
    doTest();
  }

  public void testImportSubs() {
    doTestSubs("somecode", "someothercode", "Foo::Bar::", "Foo::Bar->", "Foo::Baz::", "Foo::Baz->");
  }

  public void testImportHashes() {
    doTestHashVariables("somehash", "Foo::Bar::somehash");
  }

  public void testImportArrays() {
    doTestArrayVariables("somearray", "somehash", "Foo::Bar::somearray", "Foo::Bar::somehash", "Foo::Bar::EXPORT");
  }

  public void testImportScalars() {
    doTestScalarVariables(
      "somearray",
      "somehash",
      "somescalar",
      "Foo::Bar::EXPORT",
      "Foo::Bar::somearray",
      "Foo::Bar::somehash",
      "Foo::Bar::somescalar");
  }

  public void testImportDancer() {
    doTestSubs(PerlDancerDSL.DSL_KEYWORDS);
  }

  public void testImportDancer2() {
    ArrayList<String> dancerCopy = new ArrayList<>(PerlDancer2DSL.DSL_KEYWORDS);
    dancerCopy.remove("import");
    dancerCopy.remove("log"); // as far as we have no psi elements inside lookups, they are not duplicating
    doTestSubs(dancerCopy);
  }

  public void testImportPosixOk() {
    doTestSubs("isgreaterequal");
  }

  public void testImportPosix() {
    ArrayList<String> posixCopy = new ArrayList<>(POSIXExports.EXPORT);
    posixCopy.remove("%SIGRT");  // variable does not appear
    doTestSubs(posixCopy);
  }

  public void testImportPosixVar() {
    doTestHashVariables("SIGRT");
  }

  private void doTestSubs(String... additional) {
    doTestSubs(Arrays.asList(additional));
  }

  private void doTestSubs(List<String> additional) {
    doTest(BUILT_IN_SUBS, PACKAGES_LOOKUPS, additional);
  }

  public void testLexicalMy() {
    doTestScalarVariables("scalarname", "arrayname", "hashname");
  }

  public void testLexicalState() {
    doTestScalarVariables("scalarname", "arrayname", "hashname");
  }

  public void testLexicalOur() {
    doTestScalarVariables("scalarname", "arrayname", "hashname", "main::scalarname", "main::arrayname", "main::hashname");
  }

  private void doTestScalarVariables(String... additionalVars) {
    doTest(
      Arrays.asList(additionalVars),
      SCALAR_LOOKUPS
    );
  }

  private void doTestArrayVariables(String... additionalVars) {
    doTest(
      Arrays.asList(additionalVars),
      ARRAY_LOOKUPS
    );
  }

  private void doTestHashVariables(String... additionalVars) {
    doTest(
      Arrays.asList(additionalVars),
      HASH_LOOKUPS
    );
  }

  public void testSameStatementSimple() {
    doTestScalarVariables("normvar");
  }

  public void testSameStatementMap() {
    doTestScalarVariables("normvar");
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
    doTest(SUPER_HERE_DOC_MARKER, getLanguageMarkers());
  }

  public void testRefTypes() {
    initWithTextSmart("my $var = '<caret>'");
    List<String> strings = myFixture.getLookupElementStrings();
    assertNotNull(strings);

    UsefulTestCase.assertSameElements(strings, mergeLists(REF_TYPES, LIBRARY_PACKAGES));
  }

  public void testHashIndexBare() {
    initWithTextSmart("$$a{testindex}; $b->{<caret>}");
    List<String> strings = myFixture.getLookupElementStrings();
    assertNotNull(strings);
    UsefulTestCase.assertSameElements(strings, Arrays.asList("testindex"));
  }


  public void testAnnotation() {
    doTest("returns", "inject", "method", "override", "abstract", "deprecated", "noinspection", "type");
  }

  public void testInjectMarkers() {
    doTest(getLanguageMarkers());
  }


  public void testNextLabels() {
    doTest("LABEL1", "LABEL2", "LABEL3");
  }

  public void testGotoLabels() {
    doTest("LABEL1", "LABEL2", "LABEL3", "LABEL4", "LABEL5", "LABEL6", "LABEL8");
  }

  @Override
  public String getFileExtension() {
    return PerlFileTypeScript.EXTENSION_PL;
  }

  public void testPackageUse() {
    doTestPackageFilesAndVersions();
  }

  public void testPackageNo() {
    doTestPackageFilesAndVersions();
  }

  public void testPackageRequire() {
    doTestPackageFilesAndVersions();
  }

  public void testPackageMy() {
    doTestAllPackages();
  }

  public void testPackageOur() {
    doTestAllPackages();
  }

  public void testPackageState() {
    doTestAllPackages();
  }

  public void testTryCatch() {
    doTestAllPackages();
  }


  private void doTestPackageAndVersions(String... more) {
    doTest(BUILT_IN_VERSIONS, LIBRARY_PACKAGES, Arrays.asList(more));
  }

  private void doTestPackageFilesAndVersions(String... more) {
    doTest(BUILT_IN_VERSIONS, LIBRARY_PM_FILES, Arrays.asList(more));
  }

  private void doTestAllPackages() {
    doTest(LIBRARY_PACKAGES);
  }
}
