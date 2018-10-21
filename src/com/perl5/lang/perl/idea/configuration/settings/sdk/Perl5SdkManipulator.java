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

package com.perl5.lang.perl.idea.configuration.settings.sdk;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5RealSdkWrapper;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5SdkWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Perl5SdkManipulator {
  /**
   * @return list of SDK items, wrapped into {@link Perl5SdkWrapper}, may contain text items and parent item.
   * @see com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5ParentSdkWrapper
   * @see com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5TextSdkWrapper
   */
  @NotNull
  default List<Perl5SdkWrapper> getAllSdkWrappers() {
    return ContainerUtil.map(PerlSdkTable.getInstance().getAllJdks(), Perl5RealSdkWrapper::new);
  }

  /**
   * @return wrapper for current sdk for this manipulator, e.g. current project sdk
   */
  @Nullable
  Perl5SdkWrapper getCurrentSdkWrapper();

  /**
   * Changes sdk
   */
  void setSdk(@Nullable Sdk sdk);
}
