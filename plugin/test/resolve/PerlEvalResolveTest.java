/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

import base.PerlLightTestCase;
import org.junit.Test;

public class PerlEvalResolveTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/resolve/perl/eval";
  }

  @Test
  public void testDirectScalarQ() {doTest();}

  @Test
  public void testDirectScalarQQ() {doTest();}

  @Test
  public void testDirectScalarQQEscaped() {doTest();}

  @Test
  public void testIndirectScalarQ() {doTest();}

  @Test
  public void testIndirectScalarQAssignedAfter() {doTest();}

  @Test
  public void testIndirectScalarQAssignedBeforeDeclaration() {doTest();}

  @Test
  public void testIndirectScalarQAssignedLater() {doTest();}

  @Test
  public void testIndirectScalarQQAssignedBeforeDeclaration() {doTest();}

  @Test
  public void testIndirectScalarQQAssignedBeforeDeclarationEscaped() {doTest();}

  @Override
  protected boolean withInjections() {
    return true;
  }

  private void doTest() {
    doTestResolve();
  }
}
