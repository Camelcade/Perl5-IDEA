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
public class PerlLexicalVariableResolveTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/resolve/perl/variables";
  }

  @Test
  public void testCrossPackageGlobalVariable() {
    doTestResolve();
  }

  @Test
  public void testSimple() {
    doTestResolve();
  }

  @Test
  public void testIfCondition() {
    doTestResolve();
  }

  @Test
  public void testForIterator() {
    doTestResolve();
  }

  @Test
  public void testForIteratorIterator() {
    doTestResolve();
  }

  @Test
  public void testUseVars() {
    doTestResolve();
  }

  @Test
  public void testIfElsifElse() {
    doTestResolve();
  }

  @Test
  public void testSubSignature() {
    doTestResolve();
  }

  @Test
  public void testMethodExplicitInvocant() {
    doTestResolve();
  }

  @Test
  public void testVariableInInvocation() {
    doTestResolve();
  }

  @Test
  public void testMethodImplicitInvocant() {
    doTestResolve();
  }

  @Test
  public void testNegativeBlock() {
    doTestResolve();
  }

  @Test
  public void testNegativeIfElse() {
    doTestResolve();
  }

  @Test
  public void testSameStatementSimple() {
    doTestResolve();
  }

  @Test
  public void testSameStatementMap() {
    doTestResolve();
  }

  @Test
  public void testPackageLimitations() {
    doTestResolve();
  }
}
