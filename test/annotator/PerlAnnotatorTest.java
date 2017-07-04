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
import com.perl5.lang.perl.idea.inspections.*;

/**
 * Created by hurricup on 09.11.2016.
 */
public class PerlAnnotatorTest extends PerlLightCodeInsightFixtureTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/annotator/perl";
  }

  public void testRedundantNamespaceInspection() {doTest(PerlRedundantNamespaceInspection.class);}

  public void testUnresolvedPackageFileInspection() {doTest(PerlUnresolvedPackageFileInspection.class);}

  public void testUnresolvedNamespaceInspection() {doTest(PerlUnresolvedNamespaceInspection.class);}

  public void testMultipleNamespaceDefinitionInspection() {doTest(PerlMultipleNamespaceDefinitionsInspection.class);}

  public void testClashedNamespacesInspection() {doTest(PerlClashedNamespacesInspection.class);}

  public void testUnresolvedLabelInspection() {doTest(PerlUnresolvedLabelInspection.class);}

  public void testIdentifierInspection() {
    initWithFileSmart();
    getFile().getVirtualFile().setCharset(CharsetToolkit.US_ASCII_CHARSET);
    myFixture.enableInspections(PerlIdentifierInspection.class);
    myFixture.checkHighlighting(true, false, false);
  }

  public void testFancyMethodCall() {doTest(PerlFancyMethodCallInspection.class);}

  public void testMooseAttributesDeprecation() {doDeprecationTest();}

  public void testNsRecursiveInheritance1() {doTest(PerlNamespaceRecursiveInheritanceInspection.class);}

  public void testNsRecursiveInheritance2() {doTest(PerlNamespaceRecursiveInheritanceInspection.class);}

  public void testNsRecursiveInheritance3() {doTest(PerlNamespaceRecursiveInheritanceInspection.class);}

  public void testNamespaceDeprecation() {doDeprecationTest();}

  public void testMojoAttrsDeprecation() {doDeprecationTest();}

  public void testMojoHelperDeprecation() {doDeprecationTest();}

  public void testConstants() {
    doTest();
  }

  public void testIncorrectConstants() { doTest();}

  public void testExceptionClass() {doTest();}

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
    doTest(PerlSubUnresolvableInspection.class);
  }

  private void doTest() {
    initWithFileSmart();
    myFixture.checkHighlighting(true, true, true);
  }

  private void doDeprecationTest() {
    doTest(PerlDeprecatedInspection.class);
  }

  private void doTest(Class clazz) {
    initWithFileSmart();
    myFixture.enableInspections(clazz);
    myFixture.checkHighlighting(true, false, false);
  }
}
