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
import com.perl5.lang.perl.idea.inspections.PerlTruthinessInspection;
import org.jetbrains.annotations.NotNull;

public class PerlTruthinessQuickfixTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/quickfix/perl/truthiness";
  }

  public void testScalarDefined() {doTestScalar("Add defined");}

  public void testScalarCastDefined() {doTestScalarCast("Add defined");}

  public void testScalarNumeric() {doTestScalar("Add zero equality check");}

  public void testScalarCastNumeric() {doTestScalarCast("Add zero equality check");}

  public void testScalarString() {doTestScalar("Add empty string equality check");}

  public void testScalarCastString() {doTestScalarCast("Add empty string equality check");}

  private void doTestScalar(@NotNull String quickFixPrefix) {
    doTestMassQuickFixes("scalar", PerlTruthinessInspection.class, quickFixPrefix);
  }

  private void doTestScalarCast(@NotNull String quickFixPrefix) {
    doTestMassQuickFixes("scalarCast", PerlTruthinessInspection.class, quickFixPrefix);
  }
}
