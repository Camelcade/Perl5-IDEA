/*
 * Copyright 2016 Alexandr Evstigneev
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

package editor;

import base.TemplateToolkitLightCodeInsightFixtureTestCase;
import com.perl5.lang.tt2.idea.settings.TemplateToolkitSettings;

/**
 * Created by hurricup on 12.06.2016.
 */
public class TemplateToolkitEditorTest extends TemplateToolkitLightCodeInsightFixtureTestCase {

  @Override
  protected String getTestDataPath() {
    return "testData/editor/tt2";
  }

  public void testAutocloseTag() {
    testSmartKey("[<caret>", '%', "[%  %]");
  }

  public void testAutocloseTagCustom() {
    saveSettings();
    TemplateToolkitSettings settings = TemplateToolkitSettings.getInstance(getProject());
    settings.START_TAG = "%SOME%=";
    settings.END_TAG = "/ENDTAG$";
    testSmartKey("%SOME%<caret>", '=', "%SOME%=  /ENDTAG$");
    restoreSettings();
  }

  public void testBraces() {
    testSmartKey("[% <caret> %]", '{', "[% {} %]");
  }

  public void testBrakets() {
    testSmartKey("[% <caret> %]", '[', "[% [] %]");
  }

  public void testParens() {
    testSmartKey("[% <caret> %]", '(', "[% () %]");
  }

  public void testDQ() {
    testSmartKey("[% <caret> %]", '"', "[% \"\" %]");
  }

  public void testSQ() {
    testSmartKey("[% <caret> %]", '\'', "[% '' %]");
  }
}
