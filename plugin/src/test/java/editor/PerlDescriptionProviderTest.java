/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package editor;


import base.PerlLightTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
public class PerlDescriptionProviderTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "descriptionProvider/perl";
  }


  @Test
  public void testLocalScalar() {
    doTest("my $variable<caret>_name;");
  }

  @Test
  public void testLocalArray() {
    doTest("my @variable<caret>_name;");
  }

  @Test
  public void testLocalHash() {
    doTest("my %variable<caret>_name;");
  }

  @Test
  public void testGlobalScalar() {
    doTest("package Foo::Bar; our $variable<caret>_name;");
  }

  @Test
  public void testGlobalArray() {
    doTest("package Foo::Bar; our @variable<caret>_name;");
  }

  @Test
  public void testGlobalHash() {
    doTest("package Foo::Bar; our %variable<caret>_name;");
  }

  @Test
  public void testAttribute() {
    doTest("has attr_n<caret>ame => {};");
  }

  @Test
  public void testAccessor() {
    doTest("__PACKAGE__->follow_best_practices;__PACKAGE__->mk_accessors('accessor<caret>_name');");
  }

  @Test
  public void testException() {
    doTest("use Exception::Class 'Sept<caret>ion1';");
  }

  @Test
  public void testMethod() {
    doTest("method method<caret>_name{}");
  }

  @Test
  public void testSubDeclaration() {
    doTest("sub sub_<caret>name;");
  }

  @Test
  public void testHeredocMarker() {
    doTest();
  }

  @Test
  public void testFunction() {
    doTest("func func_n<caret>ame{}");
  }

  @Test
  public void testConstant() {
    doTest("use constant CONS<caret>T_NAME => 42;");
  }

  @Test
  public void testSubDefinition() {
    doTest("sub sub_n<caret>ame{}");
  }

  @Test
  public void testNamespace() {
    doTest("package Foo::Ba<caret>zzz;");
  }

  @Test
  public void testTypeglob() {
    doTest("*typ<caret>eglob_name = \\$var");
  }

  private void doTest() {
    doElementDescriptionTest();
  }

  private void doTest(@NotNull String content) {
    doElementDescriptionTest(content);
  }
}