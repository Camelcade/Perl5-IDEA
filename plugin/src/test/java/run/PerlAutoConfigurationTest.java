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

package run;

import base.PerlInterpreterConfigurator;
import base.PerlPlatformTestCase;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.PerlConfig;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

@SuppressWarnings("UnconstructableJUnitTestCase")
public class PerlAutoConfigurationTest extends PerlPlatformTestCase {
  public PerlAutoConfigurationTest(@NotNull PerlInterpreterConfigurator interpreterConfigurator) {
    super(interpreterConfigurator);
  }

  @Override
  protected String getBaseDataPath() {
    return "run/autoConfiguration";
  }

  @Override
  protected String getResultsTestDataPath() {
    return super.getResultsTestDataPath() + "/answers";
  }

  @Test
  public void testCpanLocal() {
    copyDirToModule("cartonProject");
    var perlConfig = PerlConfig.from(getSdk());
    assertNotNull(perlConfig);

    var moduleRoot = getModuleRoot();
    var localRoot = moduleRoot.findChild("local");
    assertNotNull(localRoot);
    var testRoot = moduleRoot.findChild("t");
    assertNotNull(testRoot);
    var libRoot = moduleRoot.findChild("lib");
    assertNotNull(libRoot);
    var perl5Root = moduleRoot.findFileByRelativePath("local/lib/perl5");
    assertNotNull(perl5Root);
    var archname = perlConfig.getArchname();
    assertNotNull(archname);
    var versionString = perlConfig.getVersion();
    assertNotNull(versionString);
    var versionDir = createChildDirectory(perl5Root, versionString);
    assertNotNull(versionDir);
    var archDir = createChildDirectory(perl5Root, archname);
    assertNotNull(archDir);
    var versionArchDir = createChildDirectory(versionDir, archname);
    assertNotNull(versionArchDir);

    configureRoots();

    assertExcluded(localRoot);
    assertTestRoot(testRoot);
    assertLibRoot(libRoot);
    assertExternalLibRoot(perl5Root);
    assertExternalLibRoot(versionDir);
    assertExternalLibRoot(archDir);
    assertExternalLibRoot(versionArchDir);
  }

  @Test
  public void testCpanminus() {
    copyDirToModule("cpanminusProject");

    var moduleRoot = getModuleRoot();
    var testRoot = moduleRoot.findChild("t");
    assertNotNull(testRoot);
    var libRoot = moduleRoot.findChild("lib");
    assertNotNull(libRoot);

    configureRoots();

    assertTestRoot(testRoot);
    assertLibRoot(libRoot);
  }

  @Test
  public void testMakeMaker() {
    copyDirToModule("makeMakerProject");

    var moduleRoot = getModuleRoot();
    var blibRoot = moduleRoot.findChild("blib");
    assertNotNull(blibRoot);
    var testRoot = moduleRoot.findChild("t");
    assertNotNull(testRoot);
    var libRoot = moduleRoot.findChild("lib");
    assertNotNull(libRoot);
    var blibLibRoot = moduleRoot.findFileByRelativePath("blib/lib");
    assertNotNull(blibLibRoot);
    var archRoot = moduleRoot.findFileByRelativePath("blib/arch");
    assertNotNull(archRoot);

    assertExcluded(blibRoot);
    assertTestRoot(testRoot);
    assertLibRoot(libRoot);
    assertExternalLibRoot(archRoot);
    assertNotExternalLibRoot(blibLibRoot);
    assertNotLibRoot(blibLibRoot);
  }


  @Test
  public void testModuleBuild() {
    copyDirToModule("moduleBuildProject");

    var moduleRoot = getModuleRoot();
    var buildRoot = moduleRoot.findChild("_build");
    assertNotNull(buildRoot);
    var blibRoot = moduleRoot.findChild("blib");
    assertNotNull(blibRoot);
    var testRoot = moduleRoot.findChild("t");
    assertNotNull(testRoot);
    var libRoot = moduleRoot.findChild("lib");
    assertNotNull(libRoot);
    var blibLibRoot = moduleRoot.findFileByRelativePath("blib/lib");
    assertNotNull(blibLibRoot);
    var archRoot = moduleRoot.findFileByRelativePath("blib/arch");
    assertNotNull(archRoot);

    assertExcluded(buildRoot);
    assertExcluded(blibRoot);
    assertTestRoot(testRoot);
    assertLibRoot(libRoot);
    assertExternalLibRoot(archRoot);
    assertNotExternalLibRoot(blibLibRoot);
    assertNotLibRoot(blibLibRoot);
  }

  private void assertExcluded(@NotNull VirtualFile fileOrDirectory) {
    assertTrue("Expected to be excluded: " + fileOrDirectory,
               ProjectFileIndex.getInstance(getProject()).isExcluded(fileOrDirectory));
  }

  private void assertTestRoot(@NotNull VirtualFile fileOrDirectory) {
    assertTrue("Expected to be tests: " + fileOrDirectory,
               ProjectFileIndex.getInstance(getProject()).isInTestSourceContent(fileOrDirectory));
  }

  private void assertLibRoot(@NotNull VirtualFile directory) {
    var modulesLibraryRoots = PerlProjectManager.getInstance(getProject()).getModulesLibraryRoots();
    assertTrue("Expected to be perl library: " + directory + ", got " + modulesLibraryRoots,
               modulesLibraryRoots.contains(directory));
  }

  private void assertNotLibRoot(@NotNull VirtualFile directory) {
    var modulesLibraryRoots = PerlProjectManager.getInstance(getProject()).getModulesLibraryRoots();
    assertFalse("Expected NOT to be perl library: " + directory + ", got " + modulesLibraryRoots,
                modulesLibraryRoots.contains(directory));
  }

  private void assertExternalLibRoot(@NotNull VirtualFile directory) {
    var externalLibraryRoots = PerlProjectManager.getInstance(getProject()).getExternalLibraryRoots();
    assertTrue("Expected to be external library: " + directory + ", got " + externalLibraryRoots,
               externalLibraryRoots.contains(directory));
  }

  private void assertNotExternalLibRoot(@NotNull VirtualFile directory) {
    var externalLibraryRoots = PerlProjectManager.getInstance(getProject()).getExternalLibraryRoots();
    assertFalse("Expected NOT to be external library: " + directory + ", got " + externalLibraryRoots,
                externalLibraryRoots.contains(directory));
  }
}
