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

import base.PerlLightTestCase;

/**
 * Created by hurricup on 14.06.2016.
 */
public class PerlMethodResolveTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/resolve/perl/methods";
  }

  public void testViaSelf() {
    doTest();
  }

  public void testViaShift() {
    doTest();
  }

  public void testViaDefault() {
    doTest();
  }

  public void testViaShiftInParens() {
    doTest();
  }

  public void testViaDefaultInParens() {
    doTest();
  }

  public void testViaPackageVar() {
    doTest();
  }

  public void testViaPackageVarInParens() {
    doTest();
  }

  public void testViaArbitraryVar() {
    doTestResolve();
  }

  public void testViaReturnResult() {
    doTest();
  }

  public void doTest() {
    doTestResolve();
  }
}
