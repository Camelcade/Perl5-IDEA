/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package com.perl5.lang.perl.extensions.mojo;

import com.perl5.lang.perl.extensions.packageprocessor.PerlFeaturesProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageParentsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlUtfProvider;
import com.perl5.lang.perl.extensions.packageprocessor.impl.BaseStrictWarningsProvidingProcessor;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MojoliciousLitePackageProcessor extends BaseStrictWarningsProvidingProcessor implements
                                                                                          PerlUtfProvider,
                                                                                          PerlFeaturesProvider,
                                                                                          PerlPackageParentsProvider {
  public static final String MOJOLICIOUS_LITE = "Mojolicious::Lite";
  private static final List<String> AUTOEXPORTED_SUBS = Arrays.asList(
    "app", "new",
    "group",
    "helper", "hook", "plugin",
    "under", "del",
    "any", "get", "options", "patch", "post", "put", "websocket"
  );

  @Override
  public int getVersion() {
    return super.getVersion() + 1;
  }

  @Override
  public void changeParentsList(@NotNull PerlUseStatementElement useStatement, @NotNull List<? super String> currentList) {
    currentList.add(MOJOLICIOUS_LITE);
  }

  @Override
  public void addExports(@NotNull PerlUseStatementElement useStatement,
                         @NotNull Set<? super String> export,
                         @NotNull Set<? super String> exportOk) {
    export.addAll(AUTOEXPORTED_SUBS);
  }

  @Override
  public boolean hasPackageFilesOptions() {
    return false;
  }
}
