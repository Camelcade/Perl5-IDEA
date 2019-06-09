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

package com.perl5.lang.perl.idea.sdk.versionManager.perlbrew;

import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.sdk.PerlHandlerBean;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.versionManager.InstallPerlHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerAdapter;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PerlBrewHandler extends PerlRealVersionManagerHandler<PerlBrewData, PerlBrewHandler> {
  private static final Pattern KEY_VAL_PATTERN = Pattern.compile("\\s+([\\w_]+):\\s*(\\S+)");
  public PerlBrewHandler(@NotNull PerlHandlerBean bean) {
    super(bean);
  }

  @NotNull
  @Override
  protected String getExecutableName() {
    return "perlbrew";
  }

  @Override
  protected PerlVersionManagerAdapter createAdapter(@NotNull String pathToVersionManager, @NotNull PerlHostData hostData) {
    return new PerlBrewAdapter(pathToVersionManager, hostData);
  }

  @NotNull
  @Override
  public String getPresentableName() {
    return PerlBundle.message("perl.vm.perlbrew.presentable.name");
  }

  @NotNull
  @Override
  public PerlBrewData createData() {
    return new PerlBrewData(this);
  }

  @NotNull
  @Override
  protected PerlBrewData createData(@NotNull PerlVersionManagerAdapter vmAdapter, @NotNull String distributionId) {
    return new PerlBrewData(vmAdapter.getVersionManagerPath(), distributionId, this, computeInfoData((PerlBrewAdapter)vmAdapter));
  }

  @NotNull
  static PerlBrewHandler getInstance() {
    for (PerlVersionManagerHandler<?, ?> handler : PerlVersionManagerHandler.all()) {
      if (handler instanceof PerlBrewHandler) {
        return (PerlBrewHandler)handler;
      }
    }
    throw new NullPointerException();
  }

  /**
   * Builds a map of key-val from {@code perlbrew info}
   */
  @NotNull
  static Map<String, String> computeInfoData(@NotNull PerlBrewAdapter adapter) {
    List<String> infoOutput = adapter.getInfo();
    if (infoOutput == null || infoOutput.isEmpty()) {
      return Collections.emptyMap();
    }
    HashMap<String, String> result = new HashMap<>();
    infoOutput.forEach(it -> {
      Matcher matcher = KEY_VAL_PATTERN.matcher(it);
      if (matcher.matches()) {
        result.put(matcher.group(1), matcher.group(2));
      }
    });
    return result;
  }

  @NotNull
  @Override
  public Icon getIcon() {
    return PerlIcons.PERLBREW_ICON;
  }

  @Nullable
  @Override
  public InstallPerlHandler createInstallHandler(@NotNull String pathToVersionManager) {
    return new PerlBrewInstallPerlHandler(pathToVersionManager, this);
  }
}
