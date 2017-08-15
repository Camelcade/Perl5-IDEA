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

package resolve.perl;

import base.PerlLightTestCase;

/**
 * Created by hurricup on 18.02.2016.
 */
public class PerlLexicalVariableResolveTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/resolve/perl/variables";
  }

  public void testSimple() {
    doTestResolve();
  }

  public void testIfCondition() {
    doTestResolve();
  }

  public void testForIterator() {
    doTestResolve();
  }

  public void testForIteratorIterator() {
    doTestResolve();
  }

  public void testUseVars() {
    doTestResolve();
  }

  public void testIfElsifElse() {
    doTestResolve();
  }

  public void testSubSignature() {
    doTestResolve();
  }

  public void testMethodExplicitInvocant() {
    doTestResolve();
  }

  public void testVariableInInvocation() {
    doTestResolve();
  }

  public void testMethodImplicitInvocant() {
    doTestResolve();
  }

  public void testNegativeBlock() {
    doTestResolve();
  }

  public void testNegativeIfElse() {
    doTestResolve();
  }

  public void testSameStatementSimple() {
    doTestResolve();
  }

  public void testSameStatementMap() {
    doTestResolve();
  }

  public void testPackageLimitations() {
    doTestResolve();
  }
}
