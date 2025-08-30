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

package highlighting;

import base.MojoLightTestCase;
import org.junit.Test;

public class MojoSyntaxHighlightingTest extends MojoLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "highlighting/syntax/template";
  }


  @Test
  public void testHeredoc() {
    doTest();
  }


  @Test
  public void testMixed() {
    doTest();
  }

  private void doTest() {
    doTest(true);
  }

  private void doTest(@SuppressWarnings("SameParameterValue") boolean checkErrors) {
    doTestHighlighter(checkErrors);
  }
}
