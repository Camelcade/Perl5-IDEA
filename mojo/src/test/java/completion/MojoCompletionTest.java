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

package completion;


import base.MojoLightTestCase;
import org.junit.Test;

public class MojoCompletionTest extends MojoLightTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    updateNamesCacheSynchronously();
  }

  @Override
  protected String getBaseDataPath() {
    return "completion/templates";
  }

  @Test
  public void testTopLevel() {doTestCompletion();}

  @Test
  public void testC() {doTestCompletion();}

  @Test
  public void testSelf() {doTestCompletion();}

  @Test
  public void testScalars() {doTestCompletion();}
}
