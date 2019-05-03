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

package stubs;

import base.PerlLightTestCase;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

public class PerlStubsTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/stubs/perl";
  }

  public void testNamespace_pl() {
    doTest();
  }

  public void testNamespace_pm() {
    doTest();
  }

  public void testNamespaceDeprecated_pl() {
    doTest();
  }

  public void testVariables_pl() {
    doTest();
  }

  public void testGlobs_pl() {
    doTest();
  }

  @NotNull
  @Override
  protected String computeAnswerFileName(@NotNull String appendix) {
    return StringUtil.replace(super.computeAnswerFileName(appendix), "_", ".");
  }

  private void doTest() {
    doTestStubs();
  }
}
