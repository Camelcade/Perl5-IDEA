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
import org.junit.Test;
public class PerlStructureViewTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "structure/perl";
  }

  @Test
  public void testPerlImports() {doTest();}

  @Test
  public void testPerlInheritance() {doTest();}

  @Test
  public void testPlainPerl() {
    doTest();
  }

  @Test
  public void testClassAccessor() {doTest();}

  @Test
  public void testConstants() { doTest(); }

  @Test
  public void testExceptionClass() { doTest(); }

  @Test
  public void testMooseAttrs() { doTest(); }

  @Test
  public void testParentsAndImports() {
    var root = myFixture.copyDirectoryToProject("parentsAndImports", "");
    assertNotNull(root);
    var libDir = root.findFileByRelativePath("lib");
    assertNotNull(libDir);
    markAsLibRoot(libDir, true);
    var testFile = root.findFileByRelativePath("test.pl");
    assertNotNull(testFile);
    myFixture.configureFromExistingVirtualFile(testFile);
    doTestStructureViewWithoutInit();
  }


  private void doTest() {
    doTestStructureView();
  }
}
