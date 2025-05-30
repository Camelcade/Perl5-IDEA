/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package unit.perl.reparse;

import base.PerlLightTestCase;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@SuppressWarnings("Junit4RunWithInspection")
@RunWith(Parameterized.class)
public abstract class PerlReparseMultiTestCase extends PerlLightTestCase {

  private final @NotNull String myName;
  private final @NotNull String myCodeSample;

  protected PerlReparseMultiTestCase(@NotNull String name, @NotNull String codeSample) {
    myName = name;
    myCodeSample = codeSample;
  }

  protected @NotNull String buildCodeSample() {
    return myCodeSample;
  }


  protected @NotNull String getAnswersDirectory() {
    return myName;
  }

  protected void doTest(@NotNull String textToInsert) {
    initWithTextSmartWithoutErrors(buildCodeSample());
    doTestReparseWithoutInit(textToInsert);
  }

  @Override
  protected @NotNull String computeAnswerFileName(@NotNull String appendix) {
    return FileUtil.join(getAnswersDirectory(), super.computeAnswerFileName(appendix));
  }
}
