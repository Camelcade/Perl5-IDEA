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

package com.perl5.lang.perl.idea.sdk.host;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.idea.sdk.AbstractPerlData;
import com.perl5.lang.perl.idea.sdk.PerlSdkAdditionalData;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Contains data necessary to access a perl host. E.g. local, wsl, ssh, docker
 */
public abstract class PerlHostData<Data extends PerlHostData<Data, Handler>, Handler extends PerlHostHandler<Data, Handler>>
  extends AbstractPerlData<Data, Handler> {
  public PerlHostData(@NotNull Handler handler) {
    super(handler);
  }

  /**
   * @return an OS handler for this host
   */
  @NotNull
  public abstract PerlOsHandler getOsHandler();

  @Contract("null->null")
  @Nullable
  static PerlHostData from(@Nullable Sdk sdk) {
    return ObjectUtils.doIfNotNull(PerlSdkAdditionalData.from(sdk), PerlSdkAdditionalData::getHostData);
  }
}
