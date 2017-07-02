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

import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import com.perl5.lang.perl.internals.PerlVersion;

/**
 * Created by hurricup on 04.03.2016.
 */
public class PerlCompletionTest extends PerlCompletionTestCase {


  @Override
  public String getFileExtension() {
    return PerlFileTypeScript.EXTENSION_PL;
  }

  @Override
  protected String getTestDataPath() {
    return "testData/completion/perl";
  }

  public void testMooseImports() {doTestCompletion();}

  public void testMooseXMethodAttributes() {doTestCompletion();}

  public void testMooseXMethodAttributesRole() {doTestCompletion();}

  public void testMooseXRoleWithOverloading() {doTestCompletion();}

  public void testMooseXRoleParametrized() {doTestCompletion();}

  public void testMooseXCLassAttribute() {doTestCompletion();}

  public void testMooseXTypesCheckedUtilExports() {doTestCompletion();}

  public void testMooseUtilTypeConstraints() {doTestCompletion();}

  public void testMooseRoleImports() {doTestCompletion();}

  public void testMooseAttrs() {doTestCompletion();}

  public void testMojoAttrs() {doTestCompletion();}


  public void testClassAccessor() {
    doTestCompletion();
  }

  public void testExceptionClassAliasLocal() {
    doTestCompletion();
  }

  public void testExceptionClassAliasStatic() {
    doTestCompletion();
  }

  public void testExceptionClassAliasMethod() {
    doTestCompletion();
  }

  public void testLibraryConstants() {
    doTestCompletion();
  }

  public void testUsePackageInCurrentDir() {
    addCustomPackage();
    setTargetPerlVersion(PerlVersion.V5_10);
    doTestCompletion("5_10");
    setTargetPerlVersion(PerlVersion.V5_12);
    doTestCompletion("5_10");
    setTargetPerlVersion(PerlVersion.V5_14);
    doTestCompletion("5_10");
    setTargetPerlVersion(PerlVersion.V5_16);
    doTestCompletion("5_10");
    setTargetPerlVersion(PerlVersion.V5_18);
    doTestCompletion("5_10");
    setTargetPerlVersion(PerlVersion.V5_20);
    doTestCompletion("5_10");
    setTargetPerlVersion(PerlVersion.V5_22);
    doTestCompletion("5_10");
    setTargetPerlVersion(PerlVersion.V5_24);
    doTestCompletion("5_10");
    setTargetPerlVersion(PerlVersion.V5_26);
    doTestCompletion("5_26");
  }

  public void testExceptionClass() {
    doTestCompletion();
  }

  public void testReferenceCompletion() {doTestCompletion();}

  public void testCaptureScalar() {doTestCompletion();}

  public void testCaptureArray() {doTestCompletion();}

  public void testCaptureHash() {doTestCompletion();}

  public void testAnnotationReturnsPackage() {
    doTestCompletion();
  }

  public void testAnnotationTypePackage() {
    doTestCompletion();
  }

  public void testImportSubsParam() {
    doTestCompletion();
  }

  private void doTestExportArray() {
    doTestCompletion();
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
    doTestCompletion();
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
    doTestCompletion();
  }

  public void testMojoliciousHelper() {
    doTestCompletion();
  }

  public void testUnresolvedSubDeclaration() {
    doTestCompletion();
  }

  public void testUnresolvedSubDefinition() {
    doTestCompletion();
  }

  public void testConstants() {
    doTestCompletion();
  }

  public void testConstantsWithPackage() {
    doTestCompletion();
  }

  public void testVariableInDeclaration() {
    doTestCompletion();
  }

  public void testImportSubs() {
    doTestCompletion();
  }

  public void testImportHashes() {
    doTestCompletion();
  }

  public void testImportArrays() {
    doTestCompletion();
  }

  public void testImportScalars() {
    doTestCompletion();
  }

  public void testImportDancer() {
    doTestCompletion();
  }

  public void testImportDancer2() {
    doTestCompletion();
  }

  public void testImportPosixOk() {
    doTestCompletion();
  }

  public void testImportPosix() {
    doTestCompletion();
  }

  public void testImportPosixVar() {
    doTestCompletion();
  }

  public void testLexicalMy() {
    doTestCompletion();
  }

  public void testLexicalState() {
    doTestCompletion();
  }

  public void testLexicalOur() {
    doTestCompletion();
  }

  public void testSameStatementSimple() {
    doTestCompletion();
  }

  public void testSameStatementMap() {
    doTestCompletion();
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
    doTestCompletion();
  }

  public void testRefTypes() {
    doTestCompletionFromText("my $var = '<caret>'");
  }

  public void testHashIndexBare() {
    doTestCompletionFromText("$$a{testindex}; $b->{<caret>}");
  }


  public void testAnnotation() {
    doTestCompletion();
  }

  public void testInjectMarkers() {
    doTestCompletion();
  }


  public void testNextLabels() {
    doTestCompletion();
  }

  public void testGotoLabels() {
    doTestCompletion();
  }

  public void testPackageUse() {
    doTestCompletion();
  }

  public void testPackageNo() {
    doTestCompletion();
  }

  public void testPackageRequire() {
    doTestCompletion();
  }

  public void testPackageMy() {
    doTestCompletion();
  }

  public void testPackageOur() {
    doTestCompletion();
  }

  public void testPackageState() {
    doTestCompletion();
  }

  public void testTryCatch() {
    doTestCompletion();
  }
}
