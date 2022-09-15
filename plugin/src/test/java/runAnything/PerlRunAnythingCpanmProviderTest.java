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
import org.junit.Test;
public class PerlRunAnythingCpanmProviderTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "runAnything/perl/cpanm";
  }

  @Test
  public void testHelpGroup() {doTestRunAnythingHelpGroup(new PerlRunAnythingHelpGroup());}

  @Test
  public void testCpanmTestMore() {doTest("cpanm Test::More");}

  @Test
  public void testEmpty() {doTest("");}

  @Test
  public void testCpanm() {doTest("cpanm");}

  @Test
  public void testOptionQuiet() {doTest("cpanm   --quiet");}

  @Test
  public void testOptionNotest() {doTest("cpanm   --notest");}

  @Test
  public void testOptionVerbose() {doTest("cpanm --verbose");}

  @Test
  public void testOptionInteractive() {doTest("cpanm --interactive");}

  @Test
  public void testOptionForce() {doTest("cpanm --force");}

  @Test
  public void testOptionTestOnly() {doTest("cpanm --test-only");}

  @Test
  public void testOptionSudo() {doTest("cpanm --sudo");}

  @Test
  public void testOptionInstalldeps() {doTest("cpanm --installdeps");}

  @Test
  public void testOptionShowdeps() {doTest("cpanm --showdeps");}

  @Test
  public void testOptionReinstall() {doTest("cpanm --reinstall");}

  @Test
  public void testOptionMirror() {doTest("cpanm --mirror");}

  @Test
  public void testOptionMirrorOnly() {doTest("cpanm --mirror-only");}

  @Test
  public void testOptionFrom() {doTest("cpanm --from");}

  @Test
  public void testOptionPrompt() {doTest("cpanm --prompt");}

  @Test
  public void testOptionLocalLib() {doTest("cpanm --local-lib");}

  @Test
  public void testOptionLocalLibContained() {doTest("cpanm --local-lib-contained");}

  @Test
  public void testOptionSelfContained() {doTest("cpanm --self-contained");}

  @Test
  public void testOptionAutoCleanup() {doTest("cpanm --auto-cleanup");}

  @Test
  public void testCommandUninstall() {doTest("cpanm --uninstall");}

  @Test
  public void testCommandSelfUpgrade() {doTest("cpanm --self-upgrade");}

  @Test
  public void testCommandHelp() {doTest("cpanm --help");}

  @Test
  public void testCommandHelpVersion() {doTest("cpanm --help --version");}

  @Test
  public void testCommandHelpUninstall() {doTest("cpanm --help --uninstall");}

  @Test
  public void testCommandLook() {doTest("cpanm --look");}

  @Test
  public void testCommandVersion() {doTest("cpanm --version");}

  @Test
  public void testCommandInfo() {doTest("cpanm --info");}

  @Test
  public void testCommandDot() {doTest("cpanm .");}


  private void doTest(@NotNull String pattern) {
    doTestRunAnythingProvider(pattern, new PerlRunAnythingProvider());
  }
}
