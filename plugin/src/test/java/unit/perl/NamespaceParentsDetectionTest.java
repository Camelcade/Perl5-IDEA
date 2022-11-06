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

package unit.perl;


import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.psi.PerlFile;
import com.perl5.lang.perl.psi.mixins.PerlNamespaceDefinitionMixin;
import org.junit.Test;

import java.util.Arrays;
public class NamespaceParentsDetectionTest extends NamespaceTestCase {

  @Override
  protected String getBaseDataPath() {
    return  "unit/perl/parents";
  }

  @Test
  public void testUseMoo() {
    withMoo();
    doTest("Moo::Object");
  }

  @Test
  public void testUseMooRole() {
    withMoo();
    doTest();
  }

  @Test
  public void testUseMoose() {
    withMoose();
    doTest("Moose::Object");
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
    withMoose();
    doTest();
  }

  @Test
  public void testUseMooseXMethodAttirbutes() {
    withMoose();
    doTest();
  }

  @Test
  public void testUseMooseXMethodAttirbutesRole() {
    withMoose();
    doTest();
  }

  @Test
  public void testUseMooseXRoleParametrized() {
    withMoose();
    doTest();
  }

  @Test
  public void testUseMooseXRoleWithOverloading() {
    withMoose();
    doTest();
  }

  @Test
  public void testUseMooseXTypesCheckedUtilExports() {
    withMoose();
    doTest();
  }

  @Test
  public void testUseRoleTiny() {
    withRoleTiny();
    doTest();
  }

  @Test
  public void testMain() { doFileTest(); }

  @Test
  public void testMainInnerParent() { doFileTest(); }

  @Test
  public void testMainMojolite() { doFileTest("Mojolicious::Lite"); }

  @Test
  public void testMainParent(){doFileTest("Foo::Bar");}

  @Test
  public void testIsaAssign() {
    doTest("superfoo1", "superbar1");
  }

  @Test
  public void testIsaAssignFullQualified() {
    doTest("superfoo1", "superbar1");
  }

  @Test
  public void testIsaAssignFullQualifiedOtherPackage() {
    doTest();
  }

  @Test
  public void testIsaDeclare() {
    doTest("superfoo2", "superbar2");
  }

  @Test
  public void testIsaDeclareParens() {
    doTest("superfoo3", "superbar3");
  }

  @Test
  public void testBase() {
    doTest("superbase", "superbase2");
  }

  @Test
  public void testParent() {
    doTest("superparent", "superparent2");
  }

  @Test
  public void testParentQQ() {
    doTest("superparent::duperparent");
  }

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

  @Test
  public void testMoo() {
    doTest("Moo::Object");
  }

  @Test
  public void testMoose() {
    doTest("Moose::Object");
  }

  @Test
  public void testRuntimeExtends() {
    doTest("someparent");
  }

  @Test
  public void testRuntimeWith() {
    doTest("someparent1", "someparent2");
  }

  @Test
  public void testRuntimeExtendsWith() {
    doTest("someparent", "someotherparent2", "someotherparent3", "someotherparent4", "someotherparent5");
  }

  @Test
  public void testRuntimeExtendsVsAll() {
    doTest("someparent");
  }

  @Test
  public void testRuntimeISAVsAll() {
    doTest("someisa");
  }

  @Test
  public void testCompileVsRuntime() {
    doTest("someisa", "someotherparent4", "someotherparent5");
  }

  @Test
  public void testParentWith() {
    doTest("someparents", "someotherparent4", "someotherparent5");
  }

  @Test
  public void testTypingISA() {
    doTestDumb();
  }

  private void doFileTest(String... parentsList) {
    initWithFileSmartWithoutErrors();
    PsiFile file = getFile();
    assert file instanceof PerlFile;
    assertEquals(Arrays.asList(parentsList), ((PerlFile)file).getParentNamespacesNames());
  }

  private void doTestDumb(String... parents) {
    initWithFileSmart();
    doTestWithoutInit(parents);
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
}
