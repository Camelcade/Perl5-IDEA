/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

import com.intellij.testFramework.LiveTemplateTestUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@SuppressWarnings("Junit4RunWithInspection")
@RunWith(Parameterized.class)
public final class TemplateToolkitLiveTemplateTest extends TemplateToolkitPostfixLiveTemplateTestCase {
  private final boolean myIsExpanding;

  @Override
  protected String getBaseDataPath() {
    return myIsExpanding ? "liveTemplates/expand": "liveTemplates/expandContext";
  }

  public TemplateToolkitLiveTemplateTest(@NotNull String name, boolean isExpanding) {
    myIsExpanding = isExpanding;
  }

  @Test
  public void testBlock() { doTest("bl"); }

  @Test
  public void testCall() { doTest("ca"); }

  @Test
  public void testCase() {
    doTest("cas", "liveTemplatesTestCase");
  }

  @Test
  public void testCatch() { doTest("cat", "liveTemplatesTestCatch"); }

  @Test
  public void testClear() { doTest("cl"); }

  @Test
  public void testDebug() { doTest("deb"); }

  @Test
  public void testDefault() { doTest("def"); }

  @Test
  public void testElse() { doTest("el", "liveTemplatesTestElseElsif"); }

  @Test
  public void testElsif() { doTest("eli", "liveTemplatesTestElseElsif"); }

  @Test
  public void testEnd() { doTest("en"); }

  @Test
  public void testFinal() { doTest("fin", "liveTemplatesTestCatch"); }

  @Test
  public void testGet() { doTest("ge"); }

  @Test
  public void testInclude() { doTest("inc"); }

  @Test
  public void testInsert() { doTest("ins"); }

  @Test
  public void testLast() { doTest("la"); }

  @Test
  public void testMacro() { doTest("mr"); }

  @Test
  public void testMeta() { doTest("me"); }

  @Test
  public void testNext() { doTest("ne"); }

  @Test
  public void testPerl() { doTest("pe"); }

  @Test
  public void testProcess() { doTest("pro"); }

  @Test
  public void testRawperl() { doTest("rp"); }

  @Test
  public void testReturn() { doTest("re"); }

  @Test
  public void testSet() { doTest("se"); }

  @Test
  public void testStop() { doTest("st"); }

  @Test
  public void testSwitch() { doTest("sw"); }

  @Test
  public void testTags() { doTest("ta"); }

  @Test
  public void testThrow() { doTest("th"); }

  @Test
  public void testTry() { doTest("try"); }

  @Test
  public void testUse() { doTest("us"); }

  @Override
  protected void doTest(@NotNull String templateId, @NotNull String fileName) {
    if( myIsExpanding){
      doLiveTemplateBulkTest(fileName, templateId);
    }
    else{
      initWithFileSmart(fileName);
      LiveTemplateTestUtil.doTestTemplateExpandingAvailability(myFixture, templateId, "Template Toolkit 2", getTestResultsFilePath());
    }
  }

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
      {"Expanding", true},
      {"Availability", false},
    });
  }
}
