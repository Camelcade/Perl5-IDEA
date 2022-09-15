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

package documentation;


import base.PerlLightTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
public class PerlQuickDocCompletionTest extends PerlLightTestCase {

  @Override
  protected String getBaseDataPath() {
    return "documentation/perl/completion";
  }

  @Test
  public void testSubDefinition() {
    myFixture.copyFileToProject("test.pm");
    doTest("test::-><caret>", "somesub");
  }

  @Test
  public void testMethod() {
    myFixture.copyFileToProject("test.pm");
    doTest("test::-><caret>", "somemethod");
  }

  @Test
  public void testFunc() {
    myFixture.copyFileToProject("test.pm");
    doTest("test::-><caret>", "somefunc");
  }

  @Test
  public void testAttr() {
    myFixture.copyFileToProject("test.pm");
    doTest("test::-><caret>", "someattr");
  }

  @Test
  public void testPackage() {
    myFixture.copyFileToProject("test.pm");
    doTest("<caret>", "test");
  }

  @Test
  public void testMojoliciousController() {
    doTest("use <caret>", "Mojolicious::Controller");
  }

  private void doTest(@NotNull String initialText, @NotNull String completionItem) {
    initWithTextSmart(initialText);
    doTestCompletionQuickDoc(completionItem);
  }

}
