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

import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.docker.PerlDockerTestUtil;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerHandler;
import org.jetbrains.annotations.NotNull;

public class PerlSystemDockerInterpreterConfigurator extends PerlInterpreterConfigurator {
  public static final PerlInterpreterConfigurator INSTANCE = new PerlSystemDockerInterpreterConfigurator();
  private static final String PERL_HOME = "/usr/local/bin/perl";
  private static final String DOCKER_IMAGE = "hurricup/camelcade-test:perl5-" + PerlPlatformTestCase.PERL_TEST_VERSION;

  private PerlSystemDockerInterpreterConfigurator() {
  }

  @Override
  protected @NotNull PerlHostData<?, ?> getHostData() {
    return PerlDockerTestUtil.createHostData(DOCKER_IMAGE);
  }

  @Override
  void setUpPerlInterpreter(@NotNull Project project) {
    PerlVersionManagerData<?, ?> versionManagerData = PerlVersionManagerHandler.getDefaultHandler().createData();
    PerlSdkType.createAndAddSdk(PERL_HOME, getHostData(), versionManagerData,
                                sdk -> PerlProjectManager.getInstance(project).setProjectSdk(sdk), project);
  }

  @Override
  public String toString() {
    return "docker: " + PERL_HOME + "@" + DOCKER_IMAGE;
  }

  @Override
  public boolean isStateful() {
    return false;
  }
}
