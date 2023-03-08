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

package com.perl5.lang.perl.idea.sdk.host.msys2;

import com.intellij.openapi.util.NlsActions.ActionText;
import com.intellij.openapi.util.SystemInfo;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.sdk.PerlHandlerBean;
import com.perl5.lang.perl.idea.sdk.host.PerlSimpleHostHandler;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandlers.MSYS2;

class PerlMsys2Handler extends PerlSimpleHostHandler<PerlMsys2Data, PerlMsys2Handler> {
  private final PerlMsys2Data DATA_INSTANCE = new PerlMsys2Data(this);

  @SuppressWarnings("NonDefaultConstructor")
  public PerlMsys2Handler(@NotNull PerlHandlerBean bean) {
    super(bean);
  }

  @Override
  public @NotNull PerlMsys2Data createData() {
    return DATA_INSTANCE;
  }

  @Override
  protected @Nullable PerlMsys2Data createDataInteractively() {
    return DATA_INSTANCE;
  }

  @Override
  public @NotNull @ActionText String getMenuItemTitle() {
    return PerlBundle.message("perl.host.handler.msys2.menu.title");
  }

  @Override
  public @NotNull String getShortName() {
    return PerlBundle.message("perl.host.handler.msys2.short.name");
  }

  @Override
  public @Nullable Icon getIcon() {
    return PerlIcons.MSYS2_ICON;
  }

  @Override
  public boolean isApplicable() {
    return SystemInfo.isWindows;
  }

  @Override
  public @Nullable PerlOsHandler getOsHandler() {
    return MSYS2;
  }
}
