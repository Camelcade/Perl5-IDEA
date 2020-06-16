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

public class PerlCompletionResultRefactoring2020Test extends PerlCompletionResultTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/completionResult/perl/refactoring2020";
  }

  @Test
  public void testArraySigilArray() {doTestInsert();}

  @Test
  public void testArraySigilArrayGlobal() {doTestInsert();}

  @Test
  public void testArraySigilArrayMid() {doTestReplace();}

  @Test
  public void testArraySigilArrayMidGlobal() {doTestReplace();}

  @Test
  public void testArraySigilArrayMidGlobalOuter() {doTestReplace();}

  @Test
  public void testArraySigilScalarGlobal() {doTestInsert();}

  @Test
  public void testArraySigilScalarGlobalMid() {doTestReplace();}

  @Test
  public void testArraySigilScalarGlobalOuter() {doTestInsert();}

  @Test
  public void testArraySigilScalarGlobalOuterMid() {doTestReplace();}

  @Test
  public void testArraySigilScalarGlobalOuterMidWithNs() {doTestReplace();}

  @Test
  public void testNoSigilScalarGlobal() {doTestInsert();}

  @Test
  public void testNoSigilScalarGlobalMid() {doTestReplace();}

  @Test
  public void testNoSigilScalarGlobalOuter() {doTestInsert();}

  @Test
  public void testNoSigilScalarGlobalOuterMid() {doTestReplace();}

  @Test
  public void testArraySigilNamespace() {doTestInsert();}

  @Test
  public void testArraySigilNamespaceContinuation() {doTestInsert();}

  @Test
  public void testArraySigilNamespaceLocal() {doTestInsert();}

  @Test
  public void testArraySigilNamespaceOuter() {doTestInsert();}

  @Test
  public void testArraySigilScalar() {doTestInsert();}

  @Test
  public void testHashSigilArray() {doTestInsert();}

  @Test
  public void testHashSigilHash() {doTestInsert();}

  @Test
  public void testHashSigilNamespace() {doTestInsert();}

  @Test
  public void testHashSigilNamespaceContinuation() {doTestInsert();}

  @Test
  public void testHashSigilNamespaceLocal() {
    disableAutoInsertion();
    doTestInsert();
  }

  @Test
  public void testHashSigilNamespaceOuter() {
    disableAutoInsertion();
    doTestInsert();
  }

  @Test
  public void testHashSigilScalar() {doTestInsert();}

  @Test
  public void testNoSigilArray() {doTestInsert();}

  @Test
  public void testNoSigilArrayMid() {doTestInsert();}

  @Test
  public void testNoSigilHash() {doTestInsert();}

  @Test
  public void testNoSigilHashMid() {doTestInsert();}

  @Test
  public void testNoSigilNamespace() {doTestInsert();}

  @Test
  public void testNoSigilNamespaceContinuation() {doTestInsert();}

  @Test
  public void testNoSigilNamespaceLocal() {doTestInsert();}

  @Test
  public void testNoSigilNamespaceOuter() {doTestInsert();}

  @Test
  public void testNoSigilScalar() {doTestInsert();}

  @Test
  public void testNoSigilScalarMid() {doTestInsert();}

  @Test
  public void testScalarSigil() {doTestInsert();}

  @Test
  public void testScalarSigilNamespace() {doTestInsert();}

  @Test
  public void testScalarSigilNamespaceContinuation() {doTestInsert();}

  @Test
  public void testScalarSigilNamespaceLocal() {
    disableAutoInsertion();
    doTestInsert();
  }

  @Test
  public void testScalarSigilNamespaceOuter() {doTestInsert();}
}
