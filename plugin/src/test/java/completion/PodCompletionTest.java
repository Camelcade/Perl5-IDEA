/*
 * Copyright 2015-2023 Alexandr Evstigneev
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
import org.junit.Test;
public class PodCompletionTest extends PodLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "completion/pod/";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerlPod528();
  }

  @Test
  public void testHeading2() {
    myFixture.copyFileToProject("test.pm");
    doTest("=head2 <caret>");
  }

  @Test
  public void testHeading2WithoutDocumented() {
    myFixture.copyFileToProject("test.pm");
    doTest("""
             =head2 somesub()

             =head2 <caret>""");
  }

  @Test
  public void testCommands() {doTest();}

  @Test
  public void testItem() {doTest();}

  @Test
  public void testBareName() {doTest();}

  @Test
  public void testBareSection() {doTest();}

  @Test
  public void testTitledName() {doTest();}

  @Test
  public void testTitledNamedSection() {
    doTest();
  }

  @Test
  public void testTitledSection() {doTest();}

  @Test
  public void testEncoding() {doTest("=encoding <caret>", "utf-8");}

  @Test
  public void testForSpace() {doTest("=for <caret>");}

  @Test
  public void testBeginSpace() {doTest("=begin <caret>");}

  @Test
  public void testEndSpace() {doTest("=end <caret>");}

  @Test
  public void testForColon() {doTest("=for :<caret>");}

  @Test
  public void testBeginColon() {doTest("=begin :<caret>");}

  @Test
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
