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
import org.jetbrains.annotations.NotNull;
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
    doTest("isa_assign.pl", "Foo", new String[]{"superfoo1", "superbar1"});
  }

  @Test
  public void testIsaAssignFullQualified() {
    doTest("isa_assign_fullqualified.pl", "Foo", new String[]{"superfoo1", "superbar1"});
  }

  @Test
  public void testIsaAssignFullQualifiedOtherPackage() {
    doTest("isa_assing_fullqualified_other_package.pl", "Foo", new String[]{});
  }

  @Test
  public void testIsaDeclare() {
    doTest("isa_declare.pl", "Foo", new String[]{"superfoo2", "superbar2"});
  }

  @Test
  public void testIsaDeclareParens() {
    doTest("isa_declare_parens.pl", "Foo", new String[]{"superfoo3", "superbar3"});
  }

  @Test
  public void testBase() {
    doTest("base.pl", "Foo", new String[]{"superbase", "superbase2"});
  }

  @Test
  public void testParent() {
    doTest("parent.pl", "Foo", new String[]{"superparent", "superparent2"});
  }

  @Test
  public void testParentQQ() {
    doTest("parent_qq.pl", "Foo", new String[]{"superparent::duperparent"});
  }

  @Test
  public void testMojoBase() {
    doTest("mojo_base.pl", "Foo", new String[]{"Mojo::Base"});
  }

  @Test
  public void testMojoBaseNoBase() {
    doTest("mojo_base_no_base.pl", "Foo", new String[]{});
  }

  @Test
  public void testMojoBaseSpecific() {
    doTest("mojo_base_specific.pl", "Foo", new String[]{"somebase1"});
  }

  @Test
  public void testMoo() {
    doTest("moo.pl", "Foo", new String[]{"Moo::Object"});
  }

  @Test
  public void testMoose() {
    doTest("moose.pl", "Foo", new String[]{"Moose::Object"});
  }

  @Test
  public void testRuntimeExtends() {
    doTest("runtime_extends.pl", "Foo", new String[]{"someparent"});
  }

  @Test
  public void testRuntimeWith() {
    doTest("runtime_with.pl", "Foo", new String[]{"someparent1", "someparent2"});
  }

  @Test
  public void testRuntimeExtendsWith() {
    doTest("runtime_extends_with.pl", "Foo",
           new String[]{"someparent", "someotherparent2", "someotherparent3", "someotherparent4", "someotherparent5"});
  }

  @Test
  public void testRuntimeExtendsVsAll() {
    doTest("runtime_extends_vs_all.pl", "Foo", new String[]{"someparent"});
  }

  @Test
  public void testRuntimeISAVsAll() {
    doTest("runtime_isa_vs_all.pl", "Foo", new String[]{"someisa"});
  }

  @Test
  public void testCompileVsRuntime() {
    doTest("compile_vs_runtime.pl", "Foo", new String[]{"someisa", "someotherparent4", "someotherparent5"});
  }

  @Test
  public void testParentWith() {
    doTest("parent_with.pl", "Foo", new String[]{"someparents", "someotherparent4", "someotherparent5"});
  }

  @Test
  public void testTypingISA() {
    doTest("typing_isa.pl", "Foo", new String[]{});
  }

  private void doFileTest(String... parentsList) {
    initWithFileSmartWithoutErrors();
    PsiFile file = getFile();
    assert file instanceof PerlFile;
    assertEquals(Arrays.asList(parentsList), ((PerlFile)file).getParentNamespacesNames());
  }

  /**
   * @deprecated consider using {@link #doTest(String...)}
   */
  @Deprecated
  private void doTest(String fileName, @NotNull String namespaceName, String[] parents) {
    PerlNamespaceDefinitionMixin namespaceDefinition = getNamespaceInFile(fileName, namespaceName);
    assertEquals(Arrays.asList(parents), namespaceDefinition.getParentNamespacesNames());
  }

  private void doTest(String... parents) {
    initWithFileSmartWithoutErrors();
    var namespaceDefinition = getElementAtCaret(PerlNamespaceDefinitionMixin.class);
    assertNotNull(namespaceDefinition);
    assertEquals(Arrays.asList(parents), namespaceDefinition.getParentNamespacesNames());
  }
}
