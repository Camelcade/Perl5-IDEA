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

package annotator;


import base.PerlLightTestCase;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.inspections.PerlSubSignaturesInspection;
import com.perl5.lang.perl.internals.PerlVersion;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static com.perl5.lang.perl.internals.PerlVersion.*;
public class PerlSubSignaturesInspectionTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/annotator/perl/sub.signatures";
  }

  @Test
  public void testMethod() {doTest(V5_10);}

  @Test
  public void testFunc() {doTest(V5_10);}

  @Test
  public void testV516() {doTest(V5_16);}

  @Test
  public void testV518() {doTest(V5_18);}

  @Test
  public void testV520() {doTest(V5_20);}

  @Test
  public void testV522() {doTest(V5_22);}

  @Test
  public void testV524() {doTest(V5_24);}

  @Test
  public void testV526() {doTest(V5_26);}

  @Test
  public void testV528() {doTest(V5_28);}

  private void doTest(@NotNull PerlVersion targetPerlVersion) {
    PerlSharedSettings.getInstance(getProject()).setTargetPerlVersion(targetPerlVersion);
    doInspectionTest(PerlSubSignaturesInspection.class);
  }
}
