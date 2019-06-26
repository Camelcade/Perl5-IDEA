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

package intellilang;


import base.PerlLightTestCase;
import com.intellij.util.FileContentUtil;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import org.junit.Test;
public class PerlInjectLanguageActionTest extends PerlLightTestCase {
  private PerlSharedSettings mySharedSettings;
  private boolean myAutomaticInjections;
  private boolean myInjectionsWithInterpolation;

  @Override
  protected String getBaseDataPath() {
    return "testData/intellilang/perl/inject_language_action";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    mySharedSettings = PerlSharedSettings.getInstance(getProject());
    myAutomaticInjections = mySharedSettings.AUTOMATIC_HEREDOC_INJECTIONS;
    myInjectionsWithInterpolation = mySharedSettings.ALLOW_INJECTIONS_WITH_INTERPOLATION;
  }

  @Override
  protected void tearDown() throws Exception {
    try {
      mySharedSettings.AUTOMATIC_HEREDOC_INJECTIONS = myAutomaticInjections;
      mySharedSettings.ALLOW_INJECTIONS_WITH_INTERPOLATION = myInjectionsWithInterpolation;
    }
    finally {
      super.tearDown();
    }
  }

  @Test
  public void testHeredocAutoInjectedQ() {
    doAutoInjectedTest();
  }


  @Test
  public void testHeredocAutoInjectedQQ() {
    doAutoInjectedTest();
  }


  @Test
  public void testHeredocAutoInjectedQX() {
    doAutoInjectedTest();
  }


  @Test
  public void testHeredocIndentableQ() {
    doTest();
  }


  @Test
  public void testHeredocIndentableQQ() {
    doTest();
  }


  @Test
  public void testHeredocIndentableQQWithVariable() {
    doTestWithInterpolation();
  }


  @Test
  public void testHeredocIndentableQWithVariable() {
    doTest();
  }


  @Test
  public void testHeredocIndentableQX() {
    doTest();
  }


  @Test
  public void testHeredocIndentableQXWithVariable() {
    doTestWithInterpolation();
  }


  @Test
  public void testHeredocIndentedQ() {
    doTest();
  }


  @Test
  public void testHeredocIndentedQQ() {
    doTest();
  }


  @Test
  public void testHeredocIndentedQQWithVariable() {
    doTestWithInterpolation();
  }


  @Test
  public void testHeredocIndentedQWithVariable() {
    doTest();
  }


  @Test
  public void testHeredocIndentedQX() {
    doTest();
  }


  @Test
  public void testHeredocIndentedQXWithVariable() {
    doTestWithInterpolation();
  }


  @Test
  public void testHeredocQ() {
    doTest();
  }


  @Test
  public void testHeredocQQ() {
    doTest();
  }


  @Test
  public void testHeredocQQWithVariable() {
    doTestWithInterpolation();
  }


  @Test
  public void testHeredocQWithVariable() {
    doTest();
  }


  @Test
  public void testHeredocQX() {
    doTest();
  }


  @Test
  public void testHeredocQXWithVariable() {
    doTestWithInterpolation();
  }

  private void doTestWithInterpolation() {
    mySharedSettings.ALLOW_INJECTIONS_WITH_INTERPOLATION = false;
    initWithFileSmartWithoutErrors();
    assertNotInjectable();

    mySharedSettings.ALLOW_INJECTIONS_WITH_INTERPOLATION = true;
    initWithFileSmartWithoutErrors();
    assertInjectable();
  }

  private void doAutoInjectedTest() {
    mySharedSettings.AUTOMATIC_HEREDOC_INJECTIONS = true;
    initWithFileSmartWithoutErrors();
    assertInjected();
    assertNotInjectable();

    mySharedSettings.AUTOMATIC_HEREDOC_INJECTIONS = false;
    FileContentUtil.reparseOpenedFiles();
    initWithFileSmartWithoutErrors();
    assertNotInjected();
    assertInjectable();
  }

  private void doTest() {
    mySharedSettings.ALLOW_INJECTIONS_WITH_INTERPOLATION = false;
    initWithFileSmartWithoutErrors();
    assertNotInjected();
    assertInjectable();

    mySharedSettings.ALLOW_INJECTIONS_WITH_INTERPOLATION = true;
    initWithFileSmartWithoutErrors();
    assertNotInjected();
    assertInjectable();
  }
}
