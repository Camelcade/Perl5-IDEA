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

import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;

public class PerlHeredocEscaperTest extends PerlHeredocInjectionTestCase {
  private boolean myInjectWithInterpolation;

  @Override
  protected String getTestDataPath() {
    return "testData/intellilang/perl/escaper";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myInjectWithInterpolation = PerlSharedSettings.getInstance(getProject()).ALLOW_INJECTIONS_WITH_INTERPOLATION;
    PerlSharedSettings.getInstance(getProject()).ALLOW_INJECTIONS_WITH_INTERPOLATION = true;
  }

  @Override
  protected void tearDown() throws Exception {
    PerlSharedSettings.getInstance(getProject()).ALLOW_INJECTIONS_WITH_INTERPOLATION = myInjectWithInterpolation;
    super.tearDown();
  }

  public void testUnindentableEmpty() { doTest();}

  public void testUnindentableOneliner() { doTest();}

  public void testUnindentableMultiliner() { doTest();}

  public void testUnindentableWithInterpolation() { doTest();}

  public void testUnindentedEmpty() { doTest();}

  public void testUnindentedOneliner() { doTest();}

  public void testUnindentedMultiliner() { doTest();}

  public void testUnindentedWithInterpolation() { doTest();}

  public void testProperlyIndentedEmpty() { doTest();}

  public void testProperlyIndentedOneliner() { doTest();}

  public void testProperlyIndentedMultiliner() { doTest();}

  public void testProperlyIndentedMultilinerWithNewLines() { doTest();}

  public void testProperlyIndentedWithInterpolation() { doTest();}

  public void testImproperlyIndentedOneliner() { doTest();}

  public void testImproperlyIndentedMultiliner() { doTest();}

  public void testImproperlyIndentedMultilinerWithNewLines() { doTest();}

  public void testImproperlyIndentedWithInterpolation() { doTest();}

  private void doTest() {
    initWithFileSmartWithoutErrors();
    assertInjected();
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getHeredocDecodedText(getHeredocUnderCursor()));
  }
}
