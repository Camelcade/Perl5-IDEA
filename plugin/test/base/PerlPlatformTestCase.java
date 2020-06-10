/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package base;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ModuleRootModificationUtil;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.PlatformTestCase;
import com.intellij.testFramework.TestActionEvent;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.perlbrew.PerlBrewTestUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.model.Statement;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

@RunWith(JUnit4.class)
public abstract class PerlPlatformTestCase extends PlatformTestCase {
  private static final String PERLBREW_HOME = "~/perl5/perlbrew/bin/perlbrew";
  private static final String PERL_526 = "perl-5.26.2";
  private static final String MOJO_LIB_SEPARATOR = "@";
  @Rule
  public final TestRule myBaseRule = (base, description) ->
    new Statement() {
      @Override
      public void evaluate() throws Throwable {
        setName(description.getMethodName());
        doEvaluate(description);
        runBare();
      }
    };
  private final Disposable myPerlLightTestCaseDisposable = Disposer.newDisposable();

  @Override
  protected @NotNull Module doCreateRealModule(@NotNull String moduleName) {
    Module module = super.doCreateRealModule(moduleName);
    try {
      VirtualFile moduleRoot = getProject().getBaseDir().createChildDirectory(this, moduleName);
      ModuleRootModificationUtil.addContentRoot(module, moduleRoot);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    return module;
  }

  protected @NotNull VirtualFile getMainContentRoot() {
    return ModuleRootManager.getInstance(getModule()).getContentRoots()[0];
  }

  @Override
  protected void tearDown() throws Exception {
    try {
      ApplicationManager.getApplication().invokeAndWait(() -> {
        PerlProjectManager projectManager = PerlProjectManager.getInstance(getProject());
        projectManager.setProjectSdk(null);
        projectManager.setExternalLibraries(Collections.emptyList());
      });
      Disposer.dispose(myPerlLightTestCaseDisposable);
    }
    finally {
      super.tearDown();
    }
  }

  /**
   * As far as we can't provide additional rules, because of hacky way main rule works, you should
   * put additional logic in here
   *
   * @param description test method description
   */
  protected void doEvaluate(@NotNull Description description) {
  }

  protected void addPerlBrewSdk(@NotNull String distributionId) {
    addSdk(getPerlbrewPath(), distributionId, PerlBrewTestUtil.getVersionManagerHandler());
  }

  protected void addSdk(@NotNull String pathToVersionManager,
                        @NotNull String distributionId,
                        @NotNull PerlRealVersionManagerHandler<?, ?> versionManagerHandler) {
    versionManagerHandler.createInterpreter(
      distributionId,
      versionManagerHandler.createAdapter(pathToVersionManager, PerlHostHandler.getDefaultHandler().createData()),
      this::onSdkCreation,
      getProject()
    );
  }

  private void onSdkCreation(@NotNull Sdk sdk) {
    PerlSdkTable.getInstance().addJdk(sdk, myPerlLightTestCaseDisposable);
    PerlProjectManager.getInstance(getProject()).setProjectSdk(sdk);
    PerlRunUtil.refreshSdkDirs(sdk, getProject());
  }

  protected @Nullable Sdk getSdk() {
    return PerlProjectManager.getSdk(getModule());
  }

  protected @NotNull String getPerl526DistibutionId(@Nullable String libraryName) {
    return StringUtil.isEmpty(libraryName) ? PERL_526 : PERL_526 + MOJO_LIB_SEPARATOR + libraryName;
  }

  protected void runAction(@NotNull AnAction anAction) {
    runAction(anAction, null);
  }

  protected void runAction(@NotNull AnAction anAction, @Nullable VirtualFile virtualFile) {
    TestActionEvent e = new TestActionEvent(dataId -> {
      if (LangDataKeys.MODULE.is(dataId)) {
        return getModule();
      }
      else if (CommonDataKeys.PROJECT.is(dataId)) {
        return getProject();
      }
      else if (CommonDataKeys.VIRTUAL_FILE.is(dataId)) {
        return virtualFile;
      }
      return null;
    });
    anAction.update(e);
    assertTrue("Action unavailable: " + anAction, e.getPresentation().isEnabled());
    anAction.actionPerformed(e);
  }

  protected static void assumePerlbrewAvailable() {
    Assume.assumeTrue(getPerlbrewFile() != null);
  }

  protected static @NotNull String getPerlbrewPath() {
    assumePerlbrewAvailable();
    return Objects.requireNonNull(getPerlbrewFile()).getPath();
  }

  protected static @Nullable File getPerlbrewFile() {
    String perlbrewHome = FileUtil.expandUserHome(PERLBREW_HOME);
    File perlbrewFile = new File(perlbrewHome);
    return perlbrewFile.exists() ? perlbrewFile : null;
  }
}
