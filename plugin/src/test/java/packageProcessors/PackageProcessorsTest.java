/*
 * Copyright 2015-2023 Alexandr Evstigneev
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
import com.perl5.lang.perl.extensions.packageprocessor.PerlMroProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageLoader;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessor;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import org.junit.Test;
public class PackageProcessorsTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "packageProcessors";
  }

  @Test
  public void testModernPerl() {
    initWithFileSmart("ModernPerl");
    PerlNamespaceDefinitionElement namespaceDefinition = getElementAtCaret(PerlNamespaceDefinitionElement.class);
    assertNotNull(namespaceDefinition);
    assertEquals(PerlMroType.C3, namespaceDefinition.getMroType());
    PerlUseStatementElement useStatement = getElementAtCaret(PerlUseStatementElement.class);
    assertNotNull(useStatement);
    PerlPackageProcessor packageProcessor = useStatement.getPackageProcessor();
    assertNotNull(packageProcessor);
    assertInstanceOf(packageProcessor, PerlMroProvider.class);
    assertInstanceOf(packageProcessor, PerlPackageLoader.class);
  }
}
