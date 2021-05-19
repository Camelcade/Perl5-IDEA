/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package formatter;


import base.HTMLMasonLightTestCase;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonCustomTagRole;
import org.junit.Ignore;
import org.junit.Test;
import unit.parser.HTMLMasonParserTest;

public class HTMLMasonFormatterTest extends HTMLMasonLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/formatter";
  }

  @Test
  public void testArgs() {doFormatTest();}

  @Test
  public void testAttr() {doFormatTest();}

  @Test
  public void testCalls() {doFormatTest();}

  @Test
  public void testCallsFiltering() {doFormatTest();}

  @Test
  @Ignore("We need to suppress new lines on perl lines")
  public void testCode() {doFormatTest();}

  private void addCustomTag(String text, HTMLMasonCustomTagRole role) {
    HTMLMasonParserTest.addCustomTag(text, role, getProject());
  }

  @Test
  public void testCustomArgs() {
    addCustomTag("customargs", HTMLMasonCustomTagRole.ARGS);
    doFormatTest();
  }

  @Test
  public void testCustomDef() {
    addCustomTag("customdef", HTMLMasonCustomTagRole.DEF);
    doFormatTest();
  }

  @Test
  public void testCustomMethod() {
    addCustomTag("custommethod", HTMLMasonCustomTagRole.METHOD);
    doFormatTest();
  }

  @Test
  public void testCustomPerl() {
    addCustomTag("customperl", HTMLMasonCustomTagRole.PERL);
    doFormatTest();
  }

  @Test
  public void testDef() {doFormatTest();}

  @Test
  public void testDoc() {doFormatTest();}

  @Test
  public void testErrorFilter() {doFormatTest();}

  @Test
  public void testEscapedBlock() {doFormatTest();}

  @Test
  public void testFilter() {doFormatTest();}

  @Test
  public void testFlags() {doFormatTest();}

  @Test
  public void testIncompleteCloser() {doFormatTest();}

  @Test
  public void testInit() {doFormatTest();}

  @Test
  public void testIssue1077() {doFormatTest();}

  @Test
  public void testMasonSample() {doFormatTest();}

  @Test
  public void testMethod() {doFormatTest();}

  @Test
  public void testOnce() {doFormatTest();}

  @Test
  public void testPerl() {doFormatTest();}

  @Test
  public void testPerlPod() {doFormatTest();}

  @Test
  public void testShared() {doFormatTest();}

  @Test
  public void testSpaceless() {doFormatTest();}

  @Test
  public void testText() {doFormatTest();}
}
