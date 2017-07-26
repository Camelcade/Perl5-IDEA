/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.project;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.PerlModuleExtension;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.roots.ModuleRootEvent;
import com.intellij.openapi.roots.ModuleRootListener;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.SyntheticLibrary;
import com.intellij.openapi.roots.ex.ProjectRootManagerEx;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.AtomicNullableLazyValue;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBusConnection;
import com.perl5.lang.perl.idea.configuration.settings.PerlLocalSettings;
import com.perl5.lang.perl.idea.configuration.settings.sdk.PerlSdkLibrary;
import com.perl5.lang.perl.idea.modules.JpsPerlLibrarySourceRootType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.intellij.ProjectTopics.PROJECT_ROOTS;

public class PerlProjectManager {
  @NotNull
  private final Project myProject;
  private final PerlLocalSettings myPerlSettings;
  private AtomicNullableLazyValue<Sdk> mySdkProvider;
  private AtomicNotNullLazyValue<List<VirtualFile>> myNonSdkLibraryRootsProvider;
  private AtomicNotNullLazyValue<List<VirtualFile>> mySdkLibraryRootsProvider;
  private AtomicNotNullLazyValue<List<VirtualFile>> myLibraryRootsProvider;
  private AtomicNotNullLazyValue<List<SyntheticLibrary>> myLibrariesProvider;

  public PerlProjectManager(@NotNull Project project) {
    myProject = project;
    myPerlSettings = PerlLocalSettings.getInstance(project);
    resetProjectSdk();
    MessageBusConnection connection = myProject.getMessageBus().connect();
    connection.subscribe(PerlSdkTable.PERL_TABLE_TOPIC, new ProjectJdkTable.Listener() {
      @Override
      public void jdkAdded(@NotNull Sdk jdk) {

      }

      @Override
      public void jdkRemoved(@NotNull Sdk sdk) {
        if (StringUtil.equals(sdk.getName(), myPerlSettings.getPerlInterpreter())) {
          setProjectSdk(null);
        }
      }

      @Override
      public void jdkNameChanged(@NotNull Sdk jdk, @NotNull String previousName) {
        if (StringUtil.equals(myPerlSettings.getPerlInterpreter(), previousName)) {
          setProjectSdk(jdk);
        }
      }
    });
    connection.subscribe(PROJECT_ROOTS, new ModuleRootListener() {
      @Override
      public void rootsChanged(ModuleRootEvent event) {
        resetProjectSdk();
      }
    });
  }

  private void resetProjectSdk() {
    mySdkProvider = AtomicNullableLazyValue.createValue(() -> PerlSdkTable.getInstance().findJdk(myPerlSettings.getPerlInterpreter()));
    mySdkLibraryRootsProvider = AtomicNotNullLazyValue.createValue(() -> {
      List<VirtualFile> result = new ArrayList<>();

      for (String externalPath : myPerlSettings.getExternalLibrariesPaths()) {
        VirtualFile virtualFile = VfsUtil.findFileByIoFile(new File(externalPath), true);
        if (virtualFile != null && virtualFile.isValid() && virtualFile.isDirectory()) {
          result.add(virtualFile);
        }
      }

      Sdk projectSdk = getProjectSdk();
      if (projectSdk != null) {
        result.addAll(Arrays.asList(projectSdk.getRootProvider().getFiles(OrderRootType.CLASSES)));
      }
      return result;
    });
    myNonSdkLibraryRootsProvider = AtomicNotNullLazyValue.createValue(() -> {
      List<VirtualFile> result = new ArrayList<>();
      for (Module module : ModuleManager.getInstance(myProject).getModules()) {
        result.addAll(PerlModuleExtension.getInstance(module).getRootsByType(JpsPerlLibrarySourceRootType.INSTANCE));
      }
      return result;
    });
    myLibraryRootsProvider = AtomicNotNullLazyValue.createValue(() -> {
      ArrayList<VirtualFile> result = new ArrayList<>(getNonSdkLibraryRoots());
      result.addAll(getProjectSdkLibraryRoots());
      return result;
    });
    myLibrariesProvider = AtomicNotNullLazyValue.createValue(() -> {
      List<VirtualFile> sdkLibs = getProjectSdkLibraryRoots();
      if (sdkLibs.isEmpty()) {
        return Collections.emptyList();
      }

      SyntheticLibrary sdkLibrary;
      Sdk sdk = getProjectSdk();
      if (sdk != null) {
        sdkLibrary = new PerlSdkLibrary(sdk, sdkLibs);
      }
      else {
        sdkLibrary = SyntheticLibrary.newImmutableLibrary(sdkLibs);
      }

      List<VirtualFile> libraryRoots = getNonSdkLibraryRoots();

      if (libraryRoots.isEmpty()) {
        return Collections.singletonList(sdkLibrary);
      }
      List<SyntheticLibrary> result = new ArrayList<>();
      result.add(SyntheticLibrary.newImmutableLibrary(libraryRoots));
      result.add(sdkLibrary);
      return result;
    });
  }

  public List<VirtualFile> getNonSdkLibraryRoots() {
    return myNonSdkLibraryRootsProvider.getValue();
  }

  public List<VirtualFile> getAllLibraryRoots() {
    return myLibraryRootsProvider.getValue();
  }

  public List<VirtualFile> getProjectSdkLibraryRoots() {
    return mySdkLibraryRootsProvider.getValue();
  }

  public List<SyntheticLibrary> getProjectLibraries() { return myLibrariesProvider.getValue();}

  @Nullable
  public Sdk getProjectSdk() {
    return mySdkProvider.getValue();
  }

  public void addExternalLibrary(@NotNull VirtualFile root) {
    if (!root.isValid() || !root.isDirectory()) {
      return;
    }
    WriteAction.run(() -> {
      List<String> paths = myPerlSettings.getExternalLibrariesPaths();
      String canonicalPath = root.getCanonicalPath();
      if (!paths.contains(canonicalPath)) {
        ProjectRootManagerEx.getInstanceEx(myProject).makeRootsChange(
          () -> {
            paths.add(canonicalPath);
            myPerlSettings.setExternalLibrariesPaths(paths);
          }, false, true);
      }
    });
  }

  public void setProjectSdk(@Nullable Sdk sdk) {
    WriteAction.run(
      () -> ProjectRootManagerEx.getInstanceEx(myProject).makeRootsChange(
        () -> myPerlSettings.setPerlInterpreter(sdk == null ? null : sdk.getName()), false, true)
    );
  }

  public static PerlProjectManager getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, PerlProjectManager.class);
  }

  @Nullable
  public static Sdk getSdk(@Nullable Module module) {
    if (module == null) {
      return null;
    }
    return getInstance(module.getProject()).getProjectSdk();
  }

  @Nullable
  public static Sdk getSdk(@Nullable Project project) {
    if (project == null) {
      return null;
    }
    return getInstance(project).getProjectSdk();
  }

  @Nullable
  public static String getSdkPath(@Nullable Module module) {
    Sdk sdk = getSdk(module);
    return sdk == null ? null : sdk.getHomePath();
  }

  @Nullable
  public static String getSdkPath(@Nullable Project project) {
    Sdk sdk = getSdk(project);
    return sdk == null ? null : sdk.getHomePath();
  }

  public static String getSdkPath(@NotNull Project project, @Nullable VirtualFile virtualFile) {
    if (virtualFile == null) {
      return getSdkPath(project);
    }
    Module module = ModuleUtilCore.findModuleForFile(virtualFile, project);
    return module == null ? getSdkPath(project) : getSdkPath(module);
  }
}
