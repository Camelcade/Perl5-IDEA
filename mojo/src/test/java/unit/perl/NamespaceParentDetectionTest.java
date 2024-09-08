/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package unit.perl;

import base.PerlLightTestCase;
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.psi.PerlFile;
import com.perl5.lang.perl.psi.mixins.PerlNamespaceDefinitionMixin;
import org.junit.Test;

import java.util.Arrays;

public class NamespaceParentDetectionTest extends PerlLightTestCase {

  @Override
  protected String getBaseDataPath() {
    return "unit/perl/parents";
  }

  @Test
  public void testMainMojolite() { doFileTest("Mojolicious::Lite"); }

  @Test
  public void testMojoBase() {
    doTest("Mojo::Base");
  }

  @Test
  public void testMojoBaseNoBase() {
    doTest();
  }

  @Test
  public void testMojoBaseSpecific() {
    doTest("somebase1");
  }

  private void doTest(String... parents) {
    initWithFileSmartWithoutErrors();
    doTestWithoutInit(parents);
  }

  private void doTestWithoutInit(String[] parents) {
    var namespaceDefinition = getElementAtCaret(PerlNamespaceDefinitionMixin.class);
    assertNotNull(namespaceDefinition);
    assertEquals(Arrays.asList(parents), namespaceDefinition.getParentNamespacesNames());
  }

  private void doFileTest(String... parentsList) {
    initWithFileSmartWithoutErrors();
    PsiFile file = getFile();
    assert file instanceof PerlFile;
    assertEquals(Arrays.asList(parentsList), ((PerlFile)file).getParentNamespacesNames());
  }
}
