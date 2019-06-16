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

package resolve;


import base.PerlLightTestCase;
import org.junit.Test;
public class PerlMethodResolveTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/resolve/perl/methods";
  }

  @Test
  public void testViaSelf() {
    doTest();
  }

  @Test
  public void testViaShift() {
    doTest();
  }

  @Test
  public void testViaDefault() {
    doTest();
  }

  @Test
  public void testViaShiftInParens() {
    doTest();
  }

  @Test
  public void testViaDefaultInParens() {
    doTest();
  }

  @Test
  public void testViaPackageVar() {
    doTest();
  }

  @Test
  public void testViaPackageVarInParens() {
    doTest();
  }

  @Test
  public void testViaArbitraryVar() {
    doTestResolve();
  }

  @Test
  public void testViaReturnResult() {
    doTest();
  }

  public void doTest() {
    doTestResolve();
  }
}
