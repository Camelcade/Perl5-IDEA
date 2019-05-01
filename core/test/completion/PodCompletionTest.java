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

package completion;

import base.PodLightTestCase;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

public class PodCompletionTest extends PodLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/completion/pod/";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerlPod();
    showLiveTemplatesInCompletion();
  }

  @Override
  protected boolean restrictFilesParsing() {
    return false;
  }

  public void testCommands() {doTest();}

  public void testItem() {doTest();}

  public void testBareName() {doTest();}

  public void testBareSection() {doTest();}

  public void testTitledName() {doTest();}

  public void testTitledNamedSection() {
    doTest();
  }

  public void testTitledSection() {doTest();}

  public void testEncoding() {doTest("=encoding <caret>", "utf-8");}

  public void testForSpace() {doTest("=for <caret>");}

  public void testBeginSpace() {doTest("=begin <caret>");}

  public void testEndSpace() {doTest("=end <caret>");}

  public void testForColon() {doTest("=for :<caret>");}

  public void testBeginColon() {doTest("=begin :<caret>");}

  public void testEndColon() {doTest("=end :<caret>");}

  private void doTest() {
    doTestCompletion();
  }

  private void doTest(@NotNull String text) {
    doTestCompletionFromText(text);
  }

  private void doTest(@NotNull String text, @NotNull String... expected) {
    doTestCompletionFromText(text, (element, presentation) -> {
      for (String string : expected) {
        if (StringUtil.equalsIgnoreCase(string, presentation.getItemText())) {
          return true;
        }
      }
      return false;
    });
  }
}
