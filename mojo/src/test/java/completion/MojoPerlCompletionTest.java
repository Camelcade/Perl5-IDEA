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

package completion;


import base.MojoLightTestCase;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.project.PerlNamesCache;
import org.junit.Test;

public class MojoPerlCompletionTest extends MojoLightTestCase {
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    PerlNamesCache.getInstance(getProject()).forceCacheUpdate();
  }

  @Override
  protected String getBaseDataPath() {
    return "completion/perl";
  }

  @Override
  public String getFileExtension() {
    return PerlFileTypeScript.EXTENSION_PL;
  }


  @Test
  public void testMojoLite() {
    doTestCompletion(withType("main"));
  }

  @Test
  public void testMojoAttrs() { doTestCompletion(); }

  @Test
  public void testMainCompletionAll() {
    doTestMainCompletion(false);
  }

  @Test
  public void testMainCompletionSimple() {
    doTestMainCompletion(true);
  }

  private void doTestMainCompletion(boolean value) {
    PerlSharedSettings.getInstance(getProject()).SIMPLE_MAIN_RESOLUTION = value;
    myFixture.copyFileToProject("second_app.pl");
    initWithTextSmartWithoutErrors("use Mojolicious::Lite;\n" +
                                   "<caret>");
    doTestCompletionCheck("", withType("main"));
  }

  @Test
  public void testMojoliciousHelper() {
    doTestCompletion();
  }
}
