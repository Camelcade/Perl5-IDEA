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

import base.PerlLightTestCase;
import com.perl5.lang.perl.idea.inspections.PerlFancyMethodCallInspection;
import com.perl5.lang.perl.idea.inspections.PerlUseStrictInspection;
import com.perl5.lang.perl.idea.inspections.PerlUseWarningsInspection;

public class PerlQuickFixesTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/quickfixes/perl";
  }

  public void testFancyMethodCall() {
    doTestAnnotationQuickFix(PerlFancyMethodCallInspection.class, "Re-format method invocation");
  }

  public void testUseStrictSimple() {
    doTestAnnotationQuickFix(PerlUseStrictInspection.class, "Add use strict");
  }

  public void testUseWarningsSimple() {
    doTestAnnotationQuickFix(PerlUseWarningsInspection.class, "Add use warnings;");
  }

  public void testUseWarningsSimpleFatal() {
    doTestAnnotationQuickFix(PerlUseWarningsInspection.class, "Add use warnings FATAL");
  }
}
