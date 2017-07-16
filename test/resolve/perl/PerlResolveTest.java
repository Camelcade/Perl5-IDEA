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

package resolve.perl;

import base.PerlLightCodeInsightFixtureTestCase;
import com.perl5.lang.perl.internals.PerlVersion;

/**
 * Created by hurricup on 09.11.2016.
 */
public class PerlResolveTest extends PerlLightCodeInsightFixtureTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/resolve/perl";
  }

  public void testSignaturesVariables() {doTestResolve();}

  public void testFancyMethodCall() {doTestResolve();}

  public void testMooseAttrs() {doTestResolve();}

  public void testMooseDoesIsa() {doTestResolve();}

  public void testMojoAttrSmartResolve() {doTestResolve();}

  public void testMojoAttrs() {doTestResolve();}

  public void testIssue1483() {doTestResolve();}

  public void testClassAccessorLib() {doTestResolve();}

  public void testClassAccessor() {doTestResolve();}

  public void testExceptionClassFields() {doTestResolve();}

  public void testExceptionClassAlias() {doTestResolve();}

  public void testConstantsWithCollapsedLists() {doTestResolve();}

  public void testLibraryNamespace() {doTestResolve();}

  public void testLibraryConstant() {doTestResolve();}

  public void testExceptionClass() {doTestResolve();}

  public void testReferenceResolve() {doTestResolve();}

  public void testExportedSubs() {
    doTestResolve();
  }

  public void testDerefWithComments() {
    doTestResolve();
  }

  public void testStringToPackage() {
    doTestResolve();
  }

  public void testConstant() {
    doTestResolve();
  }

  public void testConstantEx() {
    doTestResolve();
  }

  public void testMojoliciousHelper() {
    doTestResolve();
  }

  public void testMojoHelperNamespace() {
    doTestResolve();
  }

  public void testVariableIsa() {
    doTestResolve();
  }

  public void testSequentionalHereDocs() {
    doTestResolve();
  }

  public void testIndentableHeredocs() {
    doTestResolve();
  }

  public void testIndentableHeredocSequentional() {
    doTestResolve();
  }

  public void testVariablesAndElements() {
    doTestResolve();
  }

  public void testMainSubs() {
    doTestResolve();
  }

  public void testInterpolatedElements() {
    doTestResolve();
  }

  public void testSingleWordPackages() {
    doTestResolve();
  }

  public void testPackageRanges() {
    doTestResolve();
  }

  public void testUsePackage() {
    addCustomPackage();
    setTargetPerlVersion(PerlVersion.V5_10);
    doTestResolve();
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
}
