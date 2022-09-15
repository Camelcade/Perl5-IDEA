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

package completion;


import org.junit.Test;

public class PerlCompletionManualTest extends PerlCompletionTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    setCompletionInvocationCount(1);
  }

  @Override
  protected String getBaseDataPath() {
    return "completion/perl/manual";
  }

  @Test
  public void testArray() {doTest();}

  @Test
  public void testArrayNoSigil() {doTest();}

  @Test
  public void testCode() {doTest();}

  @Test
  public void testGlob() {doTest();}

  @Test
  public void testHash() {doTest();}

  @Test
  public void testHashNoSigil() {doTest();}

  @Test
  public void testScalar() {doTest();}

  @Test
  public void testScalarNoSigil() {doTest();}

  protected void doTest() {
    doTestCompletion();
  }
}
