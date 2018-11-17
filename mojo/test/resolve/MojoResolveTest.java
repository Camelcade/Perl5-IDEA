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

package resolve;

import base.MojoLightTestCase;
import base.PerlLightTestCase;

import static com.perl5.lang.mojolicious.filetypes.MojoliciousFileType.MOJO_DEFAULT_EXTENSION;

public class MojoResolveTest extends MojoLightTestCase {
  protected String getTestDataPath() {
    return "testData/resolve/templates";
  }

  public void testImplicitVariables() {doTestResolve();}

  public void testHelpers() {doTestResolve();}
}
