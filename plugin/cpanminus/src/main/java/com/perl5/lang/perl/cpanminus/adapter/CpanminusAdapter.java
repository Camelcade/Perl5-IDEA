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

package com.perl5.lang.perl.cpanminus.adapter;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.util.concurrency.annotations.RequiresReadLock;
import com.perl5.lang.perl.adapters.PackageManagerAdapter;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CpanminusAdapter extends PackageManagerAdapter {
  public static final @NlsSafe String SCRIPT_NAME = "cpanm";
  private static final @NlsSafe String NO_TEST_ARGUMENT = "--notest";

  private CpanminusAdapter(@NotNull Sdk sdk, @Nullable Project project) {
    super(sdk, project);
  }

  @Override
  public @NotNull String getPresentableName() {
    return "cpanminus";
  }

  @Override
  protected @NotNull String getManagerScriptName() {
    return SCRIPT_NAME;
  }

  @Override
  protected @NotNull String getManagerPackageName() {
    return CPANMINUS_PACKAGE_NAME;
  }

  @Override
  protected @NotNull List<String> getInstallParameters(@NotNull Collection<String> packageNames, boolean suppressTests) {
    var result = new ArrayList<String>();
    result.add("-v");
    if( suppressTests){
      result.add(NO_TEST_ARGUMENT);
    }
    result.addAll(super.getInstallParameters(packageNames, suppressTests));
    return result;
  }

  /**
   * @return true iff App::cpanminus is available in sdk classpath
   */
  @RequiresReadLock
  @Contract("null->false")
  public static boolean isAvailable(@Nullable Project project) {
    return PerlRunUtil.findScript(project, SCRIPT_NAME) != null;
  }

  @Contract("null->null")
  public static @Nullable CpanminusAdapter create(@Nullable Project project) {
    Sdk sdk = PerlProjectManager.getSdk(project);
    return isAvailable(project) && sdk != null ? new CpanminusAdapter(sdk, project) : null;
  }

  public static final class Factory implements PackageManagerAdapter.Factory<CpanminusAdapter> {
    @Override
    public @Nullable CpanminusAdapter createAdapter(@NotNull Sdk sdk, @Nullable Project project) {
      return isAvailable(project) ?  new CpanminusAdapter(sdk, project): null;
    }

    @VisibleForTesting
    public static @NotNull Factory getInstance(){
      return findInstance(Factory.class);
    }
  }
}
