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

package com.perl5.lang.perl.idea.sdk.host.local;

import com.intellij.openapi.util.NlsActions.ActionText;
import com.intellij.openapi.util.SystemInfo;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.sdk.PerlHandlerBean;
import com.perl5.lang.perl.idea.sdk.host.PerlSimpleHostHandler;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandlers.*;

class PerlLocalHostHandler extends PerlSimpleHostHandler<PerlLocalHostData, PerlLocalHostHandler> {
  private final PerlLocalHostData DATA_INSTANCE = new PerlLocalHostData(this);

  @SuppressWarnings("NonDefaultConstructor")
  public PerlLocalHostHandler(@NotNull PerlHandlerBean bean) {
    super(bean);
  }

  @Override
  public @NotNull @ActionText String getMenuItemTitle() {
    return PerlBundle.message("perl.host.handler.localhost.menu.title");
  }

  @Override
  public @NotNull String getShortName() {
    return PerlBundle.message("perl.host.handler.localhost.short.name");
  }

  @Override
  public @NotNull PerlLocalHostData createData() {
    return DATA_INSTANCE;
  }

  @Override
  protected boolean isChooseFolders() {
    return SystemInfo.isMac;
  }

  @Override
  protected @Nullable PerlLocalHostData createDataInteractively() {
    return DATA_INSTANCE;
  }

  @Override
  public boolean isApplicable() {
    return true;
  }

  @Override
  public @Nullable Icon getIcon() {
    return getOsHandler().getIcon();
  }

  @Override
  public @NotNull PerlOsHandler getOsHandler() {
    return SystemInfo.isWin10OrNewer ? WINDOWS10 :
           SystemInfo.isWindows ? WINDOWS :
           SystemInfo.isMac ? MACOS :
           SystemInfo.isLinux ? LINUX :
           SystemInfo.isFreeBSD ? FREEBSD :
           SystemInfo.isSolaris ? SOLARIS :
           UNIX;
  }
}
