/*
 * Copyright 2015-2017 Alexandr Evstigneev
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
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;

public class PerlInjectLanguageActionTest extends PerlLightTestCase {
  private PerlSharedSettings mySharedSettings;
  private boolean myAutomaticInjections;
  private boolean myInjectionsWithInterpolation;

  @Override
  protected String getTestDataPath() {
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

  public void testHeredocAutoInjectedQ() {
    doAutoInjectedTest();
  }


  public void testHeredocAutoInjectedQQ() {
    doAutoInjectedTest();
  }


  public void testHeredocAutoInjectedQX() {
    doAutoInjectedTest();
  }


  public void testHeredocIndentableQ() {
    doTest();
  }


  public void testHeredocIndentableQQ() {
    doTest();
  }


  public void testHeredocIndentableQQWithVariable() {
    doTestWithInterpolation();
  }


  public void testHeredocIndentableQWithVariable() {
    doTest();
  }


  public void testHeredocIndentableQX() {
    doTest();
  }


  public void testHeredocIndentableQXWithVariable() {
    doTestWithInterpolation();
  }


  public void testHeredocIndentedQ() {
    doTest();
  }


  public void testHeredocIndentedQQ() {
    doTest();
  }


  public void testHeredocIndentedQQWithVariable() {
    doTestWithInterpolation();
  }


  public void testHeredocIndentedQWithVariable() {
    doTest();
  }


  public void testHeredocIndentedQX() {
    doTest();
  }


  public void testHeredocIndentedQXWithVariable() {
    doTestWithInterpolation();
  }


  public void testHeredocQ() {
    doTest();
  }


  public void testHeredocQQ() {
    doTest();
  }


  public void testHeredocQQWithVariable() {
    doTestWithInterpolation();
  }


  public void testHeredocQWithVariable() {
    doTest();
  }


  public void testHeredocQX() {
    doTest();
  }


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
