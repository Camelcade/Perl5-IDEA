/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.sdk.versionManager.system;

import com.intellij.openapi.util.text.StringUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.sdk.PerlHandlerBean;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Paths;

public class PerlSystemVersionManagerHandler
  extends PerlVersionManagerHandler<PerlSystemVersionManagerData, PerlSystemVersionManagerHandler> {

  public PerlSystemVersionManagerHandler(@NotNull PerlHandlerBean bean) {
    super(bean);
  }

  @NotNull
  @Override
  public PerlSystemVersionManagerData createData() {
    return new PerlSystemVersionManagerData(this);
  }

  @NotNull
  @Override
  public String getMenuItemTitle() {
    return PerlBundle.message("perl.vm.system.menu.title");
  }

  @NotNull
  @Override
  public String getPresentableName() {
    return PerlBundle.message("perl.vm.system.presentable.name");
  }

  @Override
  public void createSdkInteractively(@NotNull PerlHostHandler<?, ?> hostHandler, @Nullable Runnable successCallback) {
    String suggestedPath = PerlSdkType.INSTANCE.suggestHomePath();
    hostHandler.chooseFileInteractively(
      PerlBundle.message("perl.vm.system.choose.interpreter"),
      suggestedPath == null ? null : Paths.get(suggestedPath),
      it -> StringUtil.contains(it, "perl"),
      it -> {
        String fileName = Paths.get(it).getFileName().toString();
        return StringUtil.contains(fileName, "perl") ? null : PerlBundle.message("perl.vm.system.perl.wrong.name", fileName);
      },
      (path, perlHostData) -> {
        if (StringUtil.isNotEmpty(path) && perlHostData != null) {
          PerlSdkType.createAndAddSdk(path, perlHostData, createData(), successCallback);
        }
      });
  }

  @Override
  public boolean isApplicable(@NotNull PerlOsHandler osHandler) {
    return true;
  }
}
