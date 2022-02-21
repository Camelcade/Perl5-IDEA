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

package editor;


import base.TemplateToolkitLightTestCase;
import com.perl5.lang.tt2.idea.settings.TemplateToolkitSettings;
import org.junit.Test;

public class TemplateToolkitTypedHandlerTestTest extends TemplateToolkitLightTestCase {

  @Test
  public void testAutocloseTag() {
    doTestTypingWithoutFiles("[<caret>", "%", "[%  %]");
  }

  @Test
  public void testAutocloseTagCustom() {
    saveSettings();
    TemplateToolkitSettings settings = TemplateToolkitSettings.getInstance(getProject());
    settings.START_TAG = "%SOME%=";
    settings.END_TAG = "/ENDTAG$";
    doTestTypingWithoutFiles("%SOME%<caret>", "=", "%SOME%=  /ENDTAG$");
    restoreSettings();
  }

  @Test
  public void testBraces() {
    doTestTypingWithoutFiles("[% <caret> %]", "{", "[% {} %]");
  }

  @Test
  public void testBrakets() {
    doTestTypingWithoutFiles("[% <caret> %]", "[", "[% [] %]");
  }

  @Test
  public void testParens() {
    doTestTypingWithoutFiles("[% <caret> %]", "(", "[% () %]");
  }

  @Test
  public void testDQ() {
    doTestTypingWithoutFiles("[% <caret> %]", "\"", "[% \"\" %]");
  }

  @Test
  public void testSQ() {
    doTestTypingWithoutFiles("[% <caret> %]", "'", "[% '' %]");
  }
}