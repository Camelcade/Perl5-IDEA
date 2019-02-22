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

package editor.liveTemplates;

import base.PerlLightTestCase;
import org.jetbrains.annotations.NotNull;

public abstract class PerlLiveTemplatesTestCase extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/liveTemplates/perl";
  }

  protected void doTest(@NotNull String textToType) {
    doLiveTemplateTest(textToType);
  }
}
