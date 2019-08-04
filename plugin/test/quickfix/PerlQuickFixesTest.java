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

package quickfix;


import base.PerlLightTestCase;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.inspections.*;
import org.junit.Test;
public class PerlQuickFixesTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/quickfix/perl";
  }

  @Test
  public void testUnresolvedPackageFileInspection() {
    initWithFileSmartWithoutErrors();
    myFixture.enableInspections(PerlUnresolvedPackageFileInspection.class);
    getSingleIntention("Install Foo::Bar24 with cpan");
  }

  @Test
  public void testFancyMethodCall() {
    doTestAnnotationQuickFix(PerlFancyMethodCallInspection.class, PerlBundle.message("perl.quickfix.fancy.method.prefix"));
  }

  @Test
  public void testUseStrictSimple() {
    doTestAddUseStrict();
  }

  private void doTestAddUseStrict() {
    doTestAnnotationQuickFix(PerlUseStrictInspection.class, PerlBundle.message("perl.quickfix.add.use.name", "strict"));
  }

  @Test
  public void testUseStrictAfterUse() {
    doTestAddUseStrict();
  }

  @Test
  public void testUseStrictAfterNo() {
    doTestAddUseStrict();
  }

  @Test
  public void testUseStrictAfterUseNo() {
    doTestAddUseStrict();
  }

  @Test
  public void testUseWarningsSimple() {
    doTestAnnotationQuickFix(PerlUseWarningsInspection.class, PerlBundle.message("perl.quickfix.add.use.name", "warnings"));
  }

  @Test
  public void testUseWarningsSimpleFatal() {
    doTestAnnotationQuickFix(PerlUseWarningsInspection.class, PerlBundle.message("perl.quickfix.add.use.name", "warnings FATAL => 'all'"));
  }

  @Test
  public void testUseVarsToEmpty() {
    doTestAnnotationQuickFix(PerlUseVarsInspection.class, PerlBundle.message("perl.remove.redundant.code"));
  }

  @Test
  public void testUseVars() {
    doTestAnnotationQuickFix(PerlUseVarsInspection.class, PerlBundle.message("perl.quickfix.use.vars"));
  }

  @Test
  public void testBreakToLast() {doTestAnnotationQuickFix(PerlLoopControlInspection.class, "Replace with '");}

  @Test
  public void testLastToBreak() {doTestAnnotationQuickFix(PerlLoopControlInspection.class, "Replace with '");}

  @Test
  public void testNextToContinue() {doTestAnnotationQuickFix(PerlLoopControlInspection.class, "Replace with '");}

  @Test
  public void testContinueToNext() {doTestAnnotationQuickFix(PerlLoopControlInspection.class, "Replace with '");}

}
