/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum PerlConfigurators {
  DOCKER_SYSTEM(PerlSystemDockerInterpreterConfigurator.INSTANCE),
  LOCAL_ASDF(AsdfLocalInterpreterConfigurator.INSTANCE),
  LOCAL_PERLBREW(PerlBrewLocalInterpreterConfigurator.INSTANCE),
  LOCAL_PERLBREW_WITH_LIBS(PerlBrewWithExternalLibrariesLocalInterpreterConfigurator.INSTANCE),
  LOCAL_PLENV(PlenvLocalInterpreterConfigurator.INSTANCE),
  LOCAL_SYSTEM(LocalSystemInterpreterConfigurator.INSTANCE);

  private static final String ENV_VARIABLE_NAME = "PERL_CONFIGURATORS";

  private final @NotNull PerlInterpreterConfigurator myConfigurator;

  PerlConfigurators(@NotNull PerlInterpreterConfigurator configurator) {
    myConfigurator = configurator;
  }

  public @NotNull PerlInterpreterConfigurator getConfigurator() {
    return myConfigurator;
  }

  public static @NotNull List<PerlConfigurators> getConfigurators() {
    var explicitConfigurators = System.getenv(ENV_VARIABLE_NAME);
    if (StringUtil.isEmpty(explicitConfigurators)) {
      return Arrays.asList(PerlConfigurators.values());
    }
    var requestedConfigurators = StringUtil.split(explicitConfigurators, ",");
    return ContainerUtil.map(requestedConfigurators, it -> Objects.requireNonNull(PerlConfigurators.valueOf(it)));
  }
}
