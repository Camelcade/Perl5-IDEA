/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package packageProcessors;


import base.PerlLightTestCase;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessor;
import com.perl5.lang.perl.extensions.packageprocessor.PerlStrictProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlWarningsProvider;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class Test2BundleProcessorTest extends PerlLightTestCase {

  @Parameters( name = "{0}" )
  public static Collection<String> data() {
    return Arrays.asList(
      "Test2BundleExtended",
      "Test2BundleMore",
      "Test2BundleSimple",
      "Test2V0"
    );
  }

  private String filename;

  public Test2BundleProcessorTest(String _filename) {
    filename = _filename;
  }

  @Before
  public void beforeEachTestMethod() {
    initWithFileSmart(filename);
  }

  @Test
  public void test_Namespace() {
    PerlNamespaceDefinitionElement namespaceDefinition = getElementAtCaret(PerlNamespaceDefinitionElement.class);

    assertNotNull(namespaceDefinition);
  }

  @Test
  public void test_UseStatement() {
    PerlUseStatementElement useStatement = getElementAtCaret(PerlUseStatementElement.class);

    assertNotNull(useStatement);
  }

  @Test
  public void test_PackageProcessor() {
    PerlUseStatementElement useStatement = getElementAtCaret(PerlUseStatementElement.class);
    PerlPackageProcessor packageProcessor = useStatement.getPackageProcessor();

    assertNotNull(packageProcessor);
    assertInstanceOf(packageProcessor, PerlStrictProvider.class);
    assertInstanceOf(packageProcessor, PerlWarningsProvider.class);
  }

  @Test
  public void test_StrictProvider() {
    PerlUseStatementElement useStatement = getElementAtCaret(PerlUseStatementElement.class);
    PerlPackageProcessor packageProcessor = useStatement.getPackageProcessor();

    assertInstanceOf(packageProcessor, PerlStrictProvider.class);
  }

  @Test
  public void test_WarningsProvider() {
    PerlUseStatementElement useStatement = getElementAtCaret(PerlUseStatementElement.class);
    PerlPackageProcessor packageProcessor = useStatement.getPackageProcessor();

    assertInstanceOf(packageProcessor, PerlWarningsProvider.class);
  }

  @Override
  protected String getBaseDataPath() {
    return "testData/packageProcessors/test2";
  }
}

