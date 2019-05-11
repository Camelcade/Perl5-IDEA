/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package runAnything;

import base.PerlLightTestCase;
import com.perl5.lang.perl.idea.actions.runAnything.PerlRunAnythingHelpGroup;
import com.perl5.lang.perl.idea.actions.runAnything.PerlRunAnythingProvider;
import org.jetbrains.annotations.NotNull;

public class PerlRunAnythingCpanmProviderTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/runAnything/perl/cpanm";
  }

  public void testHelpGroup() {doTestRunAnythingHelpGroup(new PerlRunAnythingHelpGroup());}

  public void testCpanmTestMore() {doTest("cpanm Test::More");}

  public void testEmpty() {doTest("");}

  public void testCpanm() {doTest("cpanm");}

  public void testOptionQuiet() {doTest("cpanm   --quiet");}

  public void testOptionNotest() {doTest("cpanm   --notest");}

  public void testOptionVerbose() {doTest("cpanm --verbose");}

  public void testOptionInteractive() {doTest("cpanm --interactive");}

  public void testOptionForce() {doTest("cpanm --force");}

  public void testOptionTestOnly() {doTest("cpanm --test-only");}

  public void testOptionSudo() {doTest("cpanm --sudo");}

  public void testOptionInstalldeps() {doTest("cpanm --installdeps");}

  public void testOptionShowdeps() {doTest("cpanm --showdeps");}

  public void testOptionReinstall() {doTest("cpanm --reinstall");}

  public void testOptionMirror() {doTest("cpanm --mirror");}

  public void testOptionMirrorOnly() {doTest("cpanm --mirror-only");}

  public void testOptionFrom() {doTest("cpanm --from");}

  public void testOptionPrompt() {doTest("cpanm --prompt");}

  public void testOptionLocalLib() {doTest("cpanm --local-lib");}

  public void testOptionLocalLibContained() {doTest("cpanm --local-lib-contained");}

  public void testOptionSelfContained() {doTest("cpanm --self-contained");}

  public void testOptionAutoCleanup() {doTest("cpanm --auto-cleanup");}

  public void testCommandUninstall() {doTest("cpanm --uninstall");}

  public void testCommandSelfUpgrade() {doTest("cpanm --self-upgrade");}

  public void testCommandHelp() {doTest("cpanm --help");}

  public void testCommandHelpVersion() {doTest("cpanm --help --version");}

  public void testCommandHelpUninstall() {doTest("cpanm --help --uninstall");}

  public void testCommandLook() {doTest("cpanm --look");}

  public void testCommandVersion() {doTest("cpanm --version");}

  public void testCommandInfo() {doTest("cpanm --info");}

  public void testCommandDot() {doTest("cpanm .");}


  private void doTest(@NotNull String pattern) {
    doTestRunAnythingProvider(pattern, new PerlRunAnythingProvider());
  }
}
