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

package liveTemplates;


import com.intellij.openapi.util.io.FileUtil;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import org.junit.Test;
public class PerlSwitchLiveTemplatesTest extends PerlLiveTemplatesTestCase {
  @Override
  protected String getTestDataPath() {
    return FileUtil.join(super.getTestDataPath(), "switch");
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    PerlSharedSettings settings = PerlSharedSettings.getInstance(getProject());
    boolean value = settings.PERL_SWITCH_ENABLED;
    addTearDownListener(() -> settings.PERL_SWITCH_ENABLED = value);
    settings.PERL_SWITCH_ENABLED = true;
  }

  @Test
  public void testSwitch() {doTest("sw");}

  @Test
  public void testCase() {doTest("ca");}

  @Test
  public void testElse() {doTest("el");}
}
