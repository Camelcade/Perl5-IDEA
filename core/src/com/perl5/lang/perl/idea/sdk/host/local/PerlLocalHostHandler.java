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

package com.perl5.lang.perl.idea.sdk.host.local;

import com.intellij.openapi.util.SystemInfo;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.sdk.PerlHandlerBean;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandlers.*;

class PerlLocalHostHandler extends PerlHostHandler<PerlLocalHostData, PerlLocalHostHandler> {
  private final PerlLocalHostData DATA_INSTANCE = new PerlLocalHostData(this);

  public PerlLocalHostHandler(@NotNull PerlHandlerBean bean) {
    super(bean);
  }

  @NotNull
  @Override
  public String getMenuItemTitle() {
    return PerlBundle.message("perl.host.handler.localhost.menu.title");
  }

  @NotNull
  @Override
  public String getShortName() {
    return PerlBundle.message("perl.host.handler.localhost.short.name");
  }

  @NotNull
  @Override
  public PerlLocalHostData createData() {
    return DATA_INSTANCE;
  }

  @Override
  protected boolean isChooseFolders() {
    return SystemInfo.isMac;
  }

  @Nullable
  @Override
  protected PerlLocalHostData createDataInteractively() {
    return DATA_INSTANCE;
  }

  @Override
  public boolean isApplicable() {
    return true;
  }

  @NotNull
  @Override
  public PerlOsHandler getOsHandler() {
    return SystemInfo.isWin10OrNewer ? WINDOWS10 :
           SystemInfo.isWindows ? WINDOWS :
           SystemInfo.isMac ? MACOS :
           SystemInfo.isLinux ? LINUX :
           SystemInfo.isFreeBSD ? FREEBSD :
           SystemInfo.isSolaris ? SOLARIS :
           UNIX;
  }
}
