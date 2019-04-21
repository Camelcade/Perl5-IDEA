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

package resolve;

import base.PerlLightTestCase;
import com.perl5.lang.perl.internals.PerlVersion;

/**
 * Created by hurricup on 09.11.2016.
 */
public class PerlResolveTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/resolve/perl";
  }

  public void testLog4perl() {
    withLog4perl();
    doTest();
  }

  public void testLog4perlInFooBar() {
    withLog4perl();
    doTest();
  }

  public void testTestMoreWithPlan() {
    withTestMore();
    doTest();
  }

  public void testCpanfile() {
    addTestLibrary("cpanfile");
    initWithCpanFile();
    checkSerializedReferencesWithFile();
  }

  public void testFileSpec() {
    withFileSpec();
    doTest();
  }

  public void testPodWeaverTags() {doTest();}

  public void testReadonly() {doTest();}

  public void testTypesStandard() {doTest();}

  public void testIssue1723() {doTest();}

  public void testIssue1669() {doTest();}

  public void testIssue1646() {doTest();}

  public void testRegexpMatchesVariables() {doTest();}

  public void testWildCardReturns() {
    //doTest();
  }

  public void testProperSelfType() {doTest();}

  public void testCoreGlobal() {doTest();}

  public void testBuiltInNamespaces() {doTest();}

  public void testFalsePositiveCore() {doTest();}

  public void testSignaturesVariables() {doTest();}

  public void testFancyMethodCall() {
    doTest();
  }

  public void testMooseAttrs() {doTest();}

  public void testMooseDoesIsa() {doTest();}

  public void testMojoAttrSmartResolve() {
    //  doTest();
  }

  public void testMojoAttrs() {doTest();}

  public void testIssue1483() {doTest();}

  public void testClassAccessorLib() {doTest();}

  public void testClassAccessor() {doTest();}

  public void testExceptionClassFields() {doTestResolve(true);}

  public void testExceptionClassAlias() {doTest();}

  public void testConstantsWithCollapsedLists() {doTest();}

  public void testLibraryNamespace() {doTest();}

  public void testLibraryConstant() {doTest();}

  public void testExceptionClass() {doTest();}

  public void testReferenceResolve() {doTest();}

  public void testExportedSubs() {
    doTest();
  }

  public void testDerefWithComments() {
    doTest();
  }

  public void testStringToPackage() {
    doTest();
  }

  public void testConstant() {
    doTest();
  }

  public void testConstantEx() {
    doTest();
  }

  public void testVariableIsa() {
    doTest();
  }

  public void testSequentialHereDocs() {
    doTest();
  }

  public void testIndentableHeredocs() {
    doTest();
  }

  public void testIndentableHeredocSequential() {
    doTest();
  }

  public void testVariablesAndElements() {
    doTest();
  }

  public void testVariableBuiltInComplex() {doTest();}

  public void testMainSubs() {
    doTest();
  }

  public void testInterpolatedElements() {
    doTest();
  }

  public void testSingleWordPackages() {
    doTest();
  }

  public void testPackageRanges() {
    doTest();
  }

  public void testReturnsPackageTag() {doTest();}

  public void testPackageTagVarType() {doTest();}

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

  public void testLazyExportOkQw() {doTest();}

  private void doTest() {
    doTestResolve();
  }
}
