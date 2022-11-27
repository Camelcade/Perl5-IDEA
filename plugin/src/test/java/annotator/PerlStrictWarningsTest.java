/*
 * Copyright 2015-2022 Alexandr Evstigneev
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
import com.perl5.lang.perl.idea.inspections.PerlUseStrictInspection;
import com.perl5.lang.perl.idea.inspections.PerlUseWarningsInspection;
import com.perl5.lang.perl.psi.PerlFile;
import org.junit.Test;

public class PerlStrictWarningsTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "annotator/perl/strictWarnings";
  }

  @Test
  public void testUseV535() {
    doTest();
  }

  @Test
  public void testUseV536() {
    doTest();
  }

  @Test
  public void testUseMoo() {
    withMoo();
    doTest();
  }

  @Test
  public void testUseMooRole() {
    withMoo();
    doTest();
  }

  @Test
  public void testUseMoose() {
    withMoose();
    doTest();
  }

  @Test
  public void testUseMooseRole() {
    withMoose();
    doTest();
  }

  @Test
  public void testUseMooseUtilTypeConstraints() {
    withMoose();
    doTest();
  }

  @Test
  public void testUseMooseXClassAttirubte() {
    withMoose();
    doTest();
  }

  @Test
  public void testUseMooseXMethodAttirbutes() {
    withMoose();
    doTest();
  }

  @Test
  public void testUseMooseXMethodAttirbutesRole() {
    withMoose();
    doTest();
  }

  @Test
  public void testUseMooseXRoleParametrized() {
    withMoose();
    doTest();
  }

  @Test
  public void testUseMooseXRoleWithOverloading() {
    withMoose();
    doTest();
  }

  @Test
  public void testUseMooseXTypesCheckedUtilExports() {
    withMoose();
    doTest();
  }

  @Test
  public void testUseRoleTiny() {
    withRoleTiny();
    doTest();
  }


  @Test
  public void testUseStrictures() {
    doTest();
  }

  @Test
  public void testUseTest2BundleExtended() {
    doTest();
  }

  @Test
  public void testUseTest2BundleMore() {
    doTest();
  }

  @Test
  public void testUseTest2BundleSimple() {
    doTest();
  }

  @Test
  public void testUseTest2V0() {
    doTest();
  }

  @Test
  public void testUseStrictInspection() { doInspectionTest(PerlUseStrictInspection.class); }

  @Test
  public void testUseWarningsInspection() { doInspectionTest(PerlUseWarningsInspection.class); }

  @Test
  public void testCpanfile() {
    initWithCpanFile();
    assertInstanceOf(myFixture.getFile(), PerlFile.class);
    doInspectionTestWithoutInit(PerlUseStrictInspection.class, PerlUseWarningsInspection.class);
  }


  private void doTest() {
    doInspectionTest(PerlUseStrictInspection.class, PerlUseWarningsInspection.class);
  }
}
