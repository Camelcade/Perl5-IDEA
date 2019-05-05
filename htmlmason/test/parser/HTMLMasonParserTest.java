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

package parser;

import com.intellij.psi.LanguageFileViewProviders;
import com.perl5.lang.htmlmason.HTMLMasonFileViewProviderFactory;
import com.perl5.lang.htmlmason.HTMLMasonLanguage;
import com.perl5.lang.htmlmason.HTMLMasonParserDefinition;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonCustomTag;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonCustomTagRole;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;


public class HTMLMasonParserTest extends PerlParserTestBase {
  public HTMLMasonParserTest() {
    super("", "mas", new HTMLMasonParserDefinition());
  }

  @Override
  protected String getTestDataPath() {
    return "testData/parser";
  }

  private void addCustomTag(String text, HTMLMasonCustomTagRole role) {
    HTMLMasonSettings settings = HTMLMasonSettings.getInstance(getProject());
    settings.customTags.add(new HTMLMasonCustomTag(text, role));
    settings.settingsUpdated();
  }

  public void testCustomArgs() {
    addCustomTag("customargs", HTMLMasonCustomTagRole.ARGS);
    doTest();
  }

  public void testCustomPerl() {
    addCustomTag("customperl", HTMLMasonCustomTagRole.PERL);
    doTest();
  }

  public void testCustomMethod() {
    addCustomTag("custommethod", HTMLMasonCustomTagRole.METHOD);
    doTest();
  }

  public void testCustomDef() {
    addCustomTag("customdef", HTMLMasonCustomTagRole.DEF);
    doTest();
  }

  public void testIncompleteCloser() {
    doTest();
  }

  public void testIncompleteOpener() {
    doTest(false);
  }

  public void testIssue1077() {
    doTest();
  }

  public void testArgs() {
    doTest();
  }

  public void testAttr() {
    doTest();
  }

  public void testCalls() {
    doTest();
  }

  public void testCallsUnclosed() {
    doTest(false);
  }

  public void testCallsUnclosedTag() {
    doTest(false);
  }

  public void testCallsFiltering() {
    doTest();
  }

  public void testCode() {
    doTest();
  }

  public void testDef() {
    doTest();
  }

  public void testDoc() {
    doTest();
  }

  public void testFilter() {
    doTest();
  }

  public void testFlags() {
    doTest();
  }

  public void testInit() {
    doTest();
  }

  public void testMethod() {
    doTest();
  }

  public void testOnce() {
    doTest();
  }

  public void testPerl() {
    doTest();
  }

  public void testShared() {
    doTest();
  }

  public void testText() {
    doTest();
  }

  public void testSpaceless() {
    doTest();
  }

  public void testEscapedBlock() {
    doTest();
  }

  public void testMasonSample() {
    doTest();
  }

  public void testErrorFilter() throws Exception {
    String name = "errorFilter";
    String text = loadFile(name + "." + myFileExt);
    myFile = createPsiFile(name, text);
    ensureParsed(myFile);
    //		List<PsiFile> allFiles = myFile.getViewProvider().getAllFiles();
    //		assertEquals(3, allFiles.size());
    // fixme this is not actually works we need to check annotations, not eror elements, they are still there, see #917
    // see https://github.com/JetBrains/intellij-plugins/blob/master/handlebars/src/com/dmarcotte/handlebars/inspections/HbErrorFilter.java
    //		assertFalse(
    //				"PsiFile contains error elements",
    //				toParseTreeText(allFiles.get(1), skipSpaces(), includeRanges()).contains("PsiErrorElement")
    //		);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    LanguageFileViewProviders.INSTANCE.addExplicitExtension(HTMLMasonLanguage.INSTANCE, new HTMLMasonFileViewProviderFactory());
    getProject().registerService(HTMLMasonSettings.class, new HTMLMasonSettings(getProject()));
  }
}
