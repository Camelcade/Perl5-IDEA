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

package editor;


import base.PerlLightTestCase;
import categories.Heavy;
import com.intellij.ide.util.gotoByName.FilteringGotoByModel;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;

/**
 * To implement module testing, we need a non-light test case
 */
@Category(Heavy.class)
public class PerlGotoModelTest extends PerlLightTestCase {

  @Override
  protected String getBaseDataPath() {
    return "gotoModel/perl";
  }

  @Test
  public void testProjectFile() {
    doTestProject(getGoToFileModel());
  }

  @Test
  public void testProjectClass() {
    doTestProject(getGoToClassModel());
  }

  @Test
  public void testProjectSymbol() {
    doTestProject(getGoToSymbolModel());
  }

  @Test
  public void testProjectWithLibFile() {
    doTestProjectWithLib(getGoToFileModel());
  }

  @Test
  public void testProjectWithLibClass() {
    doTestProjectWithLib(getGoToClassModel());
  }

  @Test
  public void testProjectWithLibSymbol() {
    doTestProjectWithLib(getGoToSymbolModel());
  }

  @Test
  public void testLibraryFile() {
    doTestLibrary(getGoToFileModel());
  }

  @Test
  public void testLibraryClass() {
    doTestLibrary(getGoToClassModel());
  }

  @Test
  public void testLibrarySymbol() {
    doTestLibrary(getGoToSymbolModel());
  }

  @Test
  public void testProjectSecondRootFile() {
    doTestProjectSecondRoot(getGoToFileModel());
  }

  @Test
  public void testProjectSecondRootClass() {
    doTestProjectSecondRoot(getGoToClassModel());
  }

  @Test
  public void testProjectSecondRootSymbol() {
    doTestProjectSecondRoot(getGoToSymbolModel());
  }


  @Test
  public void testEveryWhereFile() {
    doTestEveryWhere(getGoToFileModel());
  }

  @Test
  public void testEveryWhereClass() {
    doTestEveryWhere(getGoToClassModel());
  }

  @Test
  public void testEveryWhereSymbol() {
    doTestEveryWhere(getGoToSymbolModel());
  }

  private void doTestEveryWhere(@NotNull FilteringGotoByModel<?> model) {
    copyToProject();
    addSecondContentEntry();
    doTest(model, true);
  }

  private void doTestLibrary(@NotNull FilteringGotoByModel<?> model) {
    doTest(model, true);
  }

  private void doTestProject(@NotNull FilteringGotoByModel<?> model) {
    copyToProject();
    doTest(model, false);
  }

  private void doTestProjectWithLib(@NotNull FilteringGotoByModel<?> model) {
    copyToProject();
    VirtualFile libDir = myFixture.findFileInTempDir("lib");
    assertNotNull(libDir);
    markAsLibRoot(libDir, true);
    doTest(model, false);
  }

  private void doTestProjectSecondRoot(@NotNull FilteringGotoByModel<?> model) {
    addSecondContentEntry();
    doTest(model, false);
  }

  private void addSecondContentEntry() {
    VirtualFile moduleFilesRoot = VfsUtil.findFileByIoFile(new File(getTestDataPath() + "/" + "secondrootfiles"), true);
    assertNotNull(moduleFilesRoot);
    // fixme disabled for now. Models can't catch new root for some reason. Looks like a race, need investigation
    //  probably worth making non-light test
    //addContentEntry(moduleFilesRoot);
  }

  private void copyToProject() {
    myFixture.copyDirectoryToProject("maincontentfiles", "");
  }

  private void doTest(@NotNull FilteringGotoByModel<?> model, boolean includeNonProject) {
    doTestGoToByModel(model, includeNonProject, it -> true);
  }
}
