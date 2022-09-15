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

public class PerlCompletionRefactoring2020Test extends PerlCompletionTestCase {

  @Override
  protected String getBaseDataPath() {
    return "completion/perl/refactoring2020";
  }

  @Test
  public void testMyTypeSpecifier() {doTest();}

  @Test
  public void testMyAfterSigilScalar() {doTest();}

  @Test
  public void testMyAfterSigilArray() {doTest();}

  @Test
  public void testMyAfterSigilHash() {doTest();}

  @Test
  public void testOurAfterSigilScalar() {doTest();}

  @Test
  public void testStateAfterSigilScalar() {doTest();}

  @Test
  public void testLocalAfterSigilScalar() {doTest();}

  @Test
  public void testNoSigilNamespaceLocal() {doTest();}

  @Test
  public void testNoSigilNamespaceOuter() {doTest();}

  @Test
  public void testScalarSigilNamespaceLocal() {doTest();}

  @Test
  public void testScalarSigilNamespaceOuter() {doTest();}

  @Test
  public void testArraySigilNamespaceLocal() {doTest();}

  @Test
  public void testArraySigilNamespaceOuter() {doTest();}

  @Test
  public void testHashSigilNamespaceLocal() {doTest();}

  @Test
  public void testHashSigilNamespaceOuter() {doTest();}

  @Test
  public void testNoSigilNamespaceContinuation() {doTest();}

  @Test
  public void testScalarSigilNamespaceContinuation() {doTest();}

  @Test
  public void testArraySigilNamespaceContinuation() {doTest();}

  @Test
  public void testHashSigilNamespaceContinuation() {doTest();}

  @Test
  public void testNoSigilNamespace() {doTest();}

  @Test
  public void testScalarSigilNamespace() {doTest();}

  @Test
  public void testArraySigilNamespace() {doTest();}

  @Test
  public void testHashSigilNamespace() {doTest();}

  @Test
  public void testNoSigilArray() {doTest();}

  @Test
  public void testNoSigilArrayMid() {doTest();}

  @Test
  public void testArraySigilArray() {doTest();}

  @Test
  public void testArraySigilScalar() {doTest();}

  @Test
  public void testNoSigilHash() {doTest();}

  @Test
  public void testNoSigilHashMid() {doTest();}

  @Test
  public void testHashSigilArray() {doTest();}

  @Test
  public void testHashSigilHash() {doTest();}

  @Test
  public void testHashSigilScalar() {doTest();}

  @Test
  public void testNoSigilScalar() {doTest();}

  @Test
  public void testNoSigilScalarMid() {doTest();}

  @Test
  public void testScalarSigil() {doTest();}


  protected void doTest() {
    doTestCompletion();
  }
}
