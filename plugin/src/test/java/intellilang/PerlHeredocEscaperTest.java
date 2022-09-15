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

package intellilang;


import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import org.junit.Test;
public class PerlHeredocEscaperTest extends PerlHeredocInjectionTestCase {
  private boolean myInjectWithInterpolation;

  @Override
  protected String getBaseDataPath() {
    return "intellilang/perl/escaper";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myInjectWithInterpolation = PerlSharedSettings.getInstance(getProject()).ALLOW_INJECTIONS_WITH_INTERPOLATION;
    PerlSharedSettings.getInstance(getProject()).ALLOW_INJECTIONS_WITH_INTERPOLATION = true;
  }

  @Override
  protected void tearDown() throws Exception {
    try {
      PerlSharedSettings.getInstance(getProject()).ALLOW_INJECTIONS_WITH_INTERPOLATION = myInjectWithInterpolation;
    }
    finally {
      super.tearDown();
    }
  }

  @Test
  public void testUnindentableEmpty() { doTest();}

  @Test
  public void testUnindentableOneliner() { doTest();}

  @Test
  public void testUnindentableMultiliner() { doTest();}

  @Test
  public void testUnindentableWithInterpolation() { doTest();}

  @Test
  public void testUnindentedEmpty() { doTest();}

  @Test
  public void testUnindentedOneliner() { doTest();}

  @Test
  public void testUnindentedMultiliner() { doTest();}

  @Test
  public void testUnindentedWithInterpolation() { doTest();}

  @Test
  public void testProperlyIndentedEmpty() { doTest();}

  @Test
  public void testProperlyIndentedOneliner() { doTest();}

  @Test
  public void testProperlyIndentedMultiliner() { doTest();}

  @Test
  public void testProperlyIndentedMultilinerWithNewLines() { doTest();}

  @Test
  public void testProperlyIndentedWithInterpolation() { doTest();}

  @Test
  public void testImproperlyIndentedOneliner() { doTest();}

  @Test
  public void testImproperlyIndentedMultiliner() { doTest();}

  @Test
  public void testImproperlyIndentedMultilinerWithNewLines() { doTest();}

  @Test
  public void testImproperlyIndentedWithInterpolation() { doTest();}

  private void doTest() {
    initWithFileSmartWithoutErrors();
    assertInjected();
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getHeredocDecodedText(getHeredocUnderCursor()));
  }
}
