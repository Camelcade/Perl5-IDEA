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

public class PerlCompletionResultTest extends PerlCompletionResultTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/completionResult/perl";
  }

  @Test
  public void testPackageInHashIndexEnd() {doTestInsert();}

  @Test
  public void testPackageInHashIndexMid() {doTestReplace();}

  @Test
  public void testHashIndexScalar() {doTestInsert();}

  @Test
  public void testHashIndexArrayElement() {doTestInsert();}

  @Test
  public void testHashIndexHashElement() {doTestInsert();}

  @Test
  public void testHashIndexScalarMid() {doTestReplace();}

  @Test
  public void testHashIndexArrayElementMid() {doTestReplace();}

  @Test
  public void testHashIndexHashElementMid() {doTestReplace();}

  @Test
  public void testHashIndexScalarGlobal() {doTestInsert();}

  @Test
  public void testHashIndexArrayElementGlobal() {doTestInsert();}

  @Test
  public void testHashIndexHashElementGlobal() {doTestInsert();}

  @Test
  public void testScalarGlobal() {doTestInsert();}

  @Test
  public void testScalarGlobalEnd() {doTestInsert();}

  @Test
  public void testScalarGlobalEndNotStarting() {doTestInsert();}

  @Test
  public void testScalarGlobalEndSep() {doTestInsert();}

  @Test
  public void testScalarGlobalMid() {doTestReplace();}

  @Test
  public void testScalarGlobalMidNotStarting() {doTestReplace();}

  @Test
  public void testScalarGlobalMidSep() {doTestReplace();}

  @Test
  public void testScalar() {doTestInsert();}

  @Test
  public void testScalarUnderscore() {doTestReplace();}

  @Test
  public void testScalarBraced() {doTestInsert();}

  @Test
  public void testScalarNoSigil() {doTestInsert();}
}
