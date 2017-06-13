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

package annotator;

import base.PerlLightCodeInsightFixtureTestCase;
import com.perl5.lang.perl.idea.inspections.PerlDeprecatedInspection;

/**
 * Created by hurricup on 09.11.2016.
 */
public class PerlAnnotatorTest extends PerlLightCodeInsightFixtureTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/annotator/perl";
  }

  public void testConstants() {
    doTest();
  }

  public void testIncorrectConstants() { doTest();}

  public void testExceptionClass() {doTest();}

  public void testDeprecations() {
    doDeprecationTest();
  }

  public void testExceptionClassDeprecation() {
    doDeprecationTest();
  }

  private void doTest() {
    initWithFileSmart();
    myFixture.checkHighlighting(true, true, true);
  }

  private void doDeprecationTest() {
    initWithFileSmart();
    myFixture.enableInspections(PerlDeprecatedInspection.class);
    myFixture.checkHighlighting(true, false, false);
  }
}
