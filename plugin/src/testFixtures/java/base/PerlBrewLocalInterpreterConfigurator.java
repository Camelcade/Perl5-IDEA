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

package base;

import com.intellij.openapi.util.io.FileUtil;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.perlbrew.PerlBrewTestUtil;
import org.jetbrains.annotations.NotNull;

class PerlBrewLocalInterpreterConfigurator extends PerlVersionManagerBasedConfigurator {
  public static final PerlInterpreterConfigurator INSTANCE = new PerlBrewLocalInterpreterConfigurator();
  private static final String PERLBREW_HOME = "~/perl5/perlbrew/bin/perlbrew";
  protected static final String BASE_DISTRIBUTION_ID = "perl-" + PerlPlatformTestCase.PERL_TEST_VERSION;
  protected static final String LIB_NAME = "plugin_test";
  protected static final String DISTRIBUTION_ID = String.join("@", BASE_DISTRIBUTION_ID, LIB_NAME);

  protected PerlBrewLocalInterpreterConfigurator() {
  }

  @Override
  protected @NotNull String getPathToVersionManager() {
    return FileUtil.expandUserHome(PERLBREW_HOME);
  }

  @Override
  protected @NotNull PerlRealVersionManagerHandler<?, ?> getVersionManagerHandler() {
    return PerlBrewTestUtil.getVersionManagerHandler();
  }

  @Override
  protected @NotNull String getDistributionId() {
    return DISTRIBUTION_ID;
  }

  @Override
  public String toString() {
    return "perlbrew: " + DISTRIBUTION_ID;
  }
}
