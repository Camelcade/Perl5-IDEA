/*
 * Copyright 2015-2018 Alexandr Evstigneev
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
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.inspections.PerlSubSignaturesInspection;
import com.perl5.lang.perl.internals.PerlVersion;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.internals.PerlVersion.*;

public class PerlSubSignaturesQuickFixesTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/quickfix/perl/sub.signatures";
  }

  public void testV510to20() {
    test520VersionChange(V5_10, V5_20);
  }

  public void testV510to22() {
    test520VersionChange(V5_10, V5_22);
  }

  public void testV510to24() {
    test520VersionChange(V5_10, V5_24);
  }

  public void testV510to26() {
    test520VersionChange(V5_10, V5_26);
  }

  public void testV510to28() {
    test520VersionChange(V5_10, V5_28);
  }

  public void testV520to22() {
    test520VersionChange(V5_20, V5_22);
  }

  public void testV520to24() {
    test520VersionChange(V5_20, V5_24);
  }

  public void testV520to26() {
    test520VersionChange(V5_20, V5_26);
  }

  public void testV522to20() {
    test522VersionChange(V5_22, V5_20);
  }

  public void testV522to28() {
    test522VersionChange(V5_22, V5_28);
  }

  public void testV524to20() {
    test522VersionChange(V5_24, V5_20);
  }

  public void testV524to28() {
    test522VersionChange(V5_24, V5_28);
  }

  public void testV526to20() {
    test522VersionChange(V5_26, V5_20);
  }

  public void testV526to28() {
    test522VersionChange(V5_26, V5_28);
  }


  public void testV528to22() {
    test520VersionChange(V5_28, V5_22);
  }

  public void testV528to24() {
    test520VersionChange(V5_28, V5_24);
  }

  public void testV528to26() {
    test520VersionChange(V5_28, V5_26);
  }

  public void testFlip20to22() {
    doTest(V5_22);
  }

  public void testFlip22to20() {
    doTest(V5_20);
  }

  private void test520VersionChange(@NotNull PerlVersion initialVersion,
                                    @NotNull PerlVersion expectedVersion) {
    doTestVersionChange("v520style", initialVersion, expectedVersion);
  }

  private void test522VersionChange(@NotNull PerlVersion initialVersion,
                                    @NotNull PerlVersion expectedVersion) {
    doTestVersionChange("v522style", initialVersion, expectedVersion);
  }

  private void doTestVersionChange(@NotNull String sourceFileName,
                                   @NotNull PerlVersion initialVersion,
                                   @NotNull PerlVersion expectedVersion) {
    PerlSharedSettings.getInstance(getProject()).setTargetPerlVersion(initialVersion);
    doTestAnnotationQuickFix(sourceFileName, PerlSubSignaturesInspection.class,
                             "Set perl target version to " + expectedVersion.getStrictDottedVersion());
    PerlVersion actualVersion = PerlSharedSettings.getInstance(getProject()).getTargetPerlVersion();
    assertEquals("Expected " + expectedVersion + " got " + actualVersion + " instead", actualVersion, expectedVersion);
  }

  private void doTest(@NotNull PerlVersion version) {
    PerlSharedSettings.getInstance(getProject()).setTargetPerlVersion(version);
    doTestAnnotationQuickFix(PerlSubSignaturesInspection.class, "Flip signature with");
  }
}
