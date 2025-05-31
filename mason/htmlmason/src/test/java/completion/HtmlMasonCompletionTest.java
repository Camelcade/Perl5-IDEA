/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package completion;

import base.HTMLMasonLightTestCase;
import categories.Heavy;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.Parameterized;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;

import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings("Junit4RunWithInspection")
@Category(Heavy.class)
@RunWith(Parameterized.class)
public class HtmlMasonCompletionTest extends HTMLMasonLightTestCase {

  private static final String COMPONENTS_ROOT_NAME = "components";
  @Parameter()
  public @NotNull String mySampleName;

  @Override
  protected String getBaseDataPath() {
    return "completion";
  }

  @org.junit.runners.Parameterized.Parameters(name = "{0}")
  public static Iterable<Object[]> fakeData() {
    return Collections.emptyList();
  }

  @com.intellij.testFramework.Parameterized.Parameters(name = "{0}")
  public static Iterable<Object[]> realData(Class<?> clazz) {
    return Arrays.asList(new Object[][]{
      {"include"},
      {"includeAbsolute"},
      {"includeBadSlug"},
      {"includeSelf"},
      {"includeParent"},
      {"includeRequest"},
      {"filter"},
      {"filterAbsolute"},
      {"filterBadSlug"},
      {"filterSelf"},
      {"filterParent"},
      {"filterRequest"},
      {"inherit"},
    });
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    VirtualFile componentsRoot = myFixture.copyDirectoryToProject("testproject", COMPONENTS_ROOT_NAME);
    assertNotNull(componentsRoot);
    markAsComponentRoot(componentsRoot);
  }

  @Test
  public void testTop() {
    doTest("file.mas");
  }

  @Test
  public void testZero() {
    doTest("zero/file.mas");
  }

  @Test
  public void testZeroSubdir() {
    doTest("zero/subdir/file.mas");
  }

  @Test
  public void testZeroOtherdir() {
    doTest("zero/otherdir/file.mas");
  }

  @Test
  public void testZeroNonExistingDir() {
    doTest("zero/nonexistingdir/file.mas");
  }

  private void doTest(@NotNull String relativePath) {
    var testFile = myFixture.copyFileToProject(getSampleName() + ".code", FileUtil.join(COMPONENTS_ROOT_NAME, relativePath));
    assertNotNull(testFile);
    myFixture.configureFromExistingVirtualFile(testFile);
    doTestCompletionCheck("." + getSampleName(), null);
  }

  protected @NotNull String getSampleName() {
    return mySampleName;
  }
}
