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

package unit.parser;


import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonCustomTag;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonCustomTagRole;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import unit.perl.parser.PerlParserTestBase;

public class HTMLMasonParserTest extends PerlParserTestBase {
  public HTMLMasonParserTest() {
    super("mas");
  }

  @Override
  protected String getBaseDataPath() {
    return "testData/unit/parser";
  }

  private void addCustomTag(String text, HTMLMasonCustomTagRole role) {
    HTMLMasonSettings settings = HTMLMasonSettings.getInstance(getProject());
    settings.customTags.add(new HTMLMasonCustomTag(text, role));
    settings.settingsUpdated();
  }

  @Override
  protected @NotNull String getTestLibPath() {
    return super.getTestLibPathFromNested();
  }

  @Test
  public void testCustomArgs() {
    addCustomTag("customargs", HTMLMasonCustomTagRole.ARGS);
    doTest();
  }

  @Test
  public void testCustomPerl() {
    addCustomTag("customperl", HTMLMasonCustomTagRole.PERL);
    doTest();
  }

  @Test
  public void testCustomMethod() {
    addCustomTag("custommethod", HTMLMasonCustomTagRole.METHOD);
    doTest();
  }

  @Test
  public void testCustomDef() {
    addCustomTag("customdef", HTMLMasonCustomTagRole.DEF);
    doTest();
  }

  @Test
  public void testIncompleteCloser() {
    doTest();
  }

  @Test
  public void testIncompleteOpener() {
    doTest(false);
  }

  @Test
  public void testIssue1077() {
    doTest();
  }

  @Test
  public void testArgs() {
    doTest();
  }

  @Test
  public void testAttr() {
    doTest();
  }

  @Test
  public void testCalls() {
    doTest();
  }

  @Test
  public void testCallsUnclosed() {
    doTest(false);
  }

  @Test
  public void testCallsUnclosedTag() {
    doTest(false);
  }

  @Test
  public void testCallsFiltering() {
    doTest();
  }

  @Test
  public void testCode() {
    doTest();
  }

  @Test
  public void testDef() {
    doTest();
  }

  @Test
  public void testDoc() {
    doTest();
  }

  @Test
  public void testFilter() {
    doTest();
  }

  @Test
  public void testFlags() {
    doTest();
  }

  @Test
  public void testInit() {
    doTest();
  }

  @Test
  public void testMethod() {
    doTest();
  }

  @Test
  public void testOnce() {
    doTest();
  }

  @Test
  public void testPerl() {
    doTest();
  }

  @Test
  public void testPerlPod() {
    doTest();
  }

  @Test
  public void testShared() {
    doTest();
  }

  @Test
  public void testText() {
    doTest();
  }

  @Test
  public void testSpaceless() {
    doTest();
  }

  @Test
  public void testEscapedBlock() {
    doTest();
  }

  @Test
  public void testMasonSample() {
    doTest();
  }
}

