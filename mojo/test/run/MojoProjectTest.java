/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package run;

import base.PerlInterpreterConfigurator;
import base.PerlPlatformTestCase;
import categories.Heavy;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.TestDialogManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.PlatformTestUtil;
import com.perl5.lang.mojolicious.MojoUtil;
import com.perl5.lang.mojolicious.idea.actions.MojoGenerateAppAction;
import com.perl5.lang.mojolicious.idea.actions.MojoGenerateLiteAppAction;
import com.perl5.lang.mojolicious.idea.actions.MojoGeneratePluginAction;
import com.perl5.lang.mojolicious.model.MojoApp;
import com.perl5.lang.mojolicious.model.MojoPlugin;
import com.perl5.lang.mojolicious.model.MojoProject;
import com.perl5.lang.mojolicious.model.MojoProjectManager;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.Description;

import java.util.List;

@SuppressWarnings("UnconstructableJUnitTestCase")
@Category(Heavy.class)
public class MojoProjectTest extends PerlPlatformTestCase {
  private static final Logger LOG = Logger.getInstance(MojoProjectTest.class);

  public MojoProjectTest(@NotNull PerlInterpreterConfigurator interpreterConfigurator) {
    super(interpreterConfigurator);
  }

  @Override
  protected void doEvaluate(@NotNull Description description) {
    assumePerlbrewAvailable();
  }

  @Test
  public void testGenerateAppAction() {
    assertMojoAvailable();
    TestDialogManager.setTestInputDialog(message -> "Test::App");
    runAction(new MojoGenerateAppAction(), getMainContentRoot());
    waitForAllDescriptorsToFinish();
    List<MojoProject> projects = refreshAndRescanForProjects().getMojoProjects();
    if (projects.size() != 1) {
      fail("Expected an application, got: " + projects);
    }
    assertInstanceOf(projects.get(0), MojoApp.class);
    LOG.info("Got: " + projects.get(0));
  }

  @Test
  public void testGenerateLiteAppAction() {
    assertMojoAvailable();
    VirtualFile mainContentRoot = getMainContentRoot();
    TestDialogManager.setTestInputDialog(message -> "my_lite_app.pl");
    runAction(new MojoGenerateLiteAppAction(), mainContentRoot);
    waitForAllDescriptorsToFinish();
    refreshAndRescanForProjects().getMojoProjects();
    assertNotNull(mainContentRoot.findChild("my_lite_app.pl"));
  }

  private @NotNull MojoProjectManager refreshAndRescanForProjects() {
    getMainContentRoot().refresh(false, true);
    PlatformTestUtil.dispatchAllInvocationEventsInIdeEventQueue();
    MojoProjectManager mojoProjectManager = MojoProjectManager.getInstance(getProject());
    mojoProjectManager.updateInTestMode();
    return mojoProjectManager;
  }

  @Test
  public void testGeneratePluginAction() {
    assertMojoAvailable();
    TestDialogManager.setTestInputDialog(message -> "Test::Plugin");
    runAction(new MojoGeneratePluginAction(), getMainContentRoot());
    waitForAllDescriptorsToFinish();
    List<MojoProject> projects = refreshAndRescanForProjects().getMojoProjects();
    if (projects.size() != 1) {
      fail("Expected a plugin, got: " + projects);
    }
    assertInstanceOf(projects.get(0), MojoPlugin.class);
    LOG.info("Got: " + projects.get(0));
  }

  private void assertMojoAvailable() {
    assertTrue("Mojolicious is not available in " + getSdk(), MojoUtil.isMojoAvailable(getProject()));
  }
}
