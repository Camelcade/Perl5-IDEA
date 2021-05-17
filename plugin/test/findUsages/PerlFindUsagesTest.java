/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package findUsages;


import base.PerlLightTestCase;
import org.junit.Test;
public class PerlFindUsagesTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/findusages/perl";
  }

  @Test
  public void testMkAccessor() {doTest();}

  @Test
  public void testMkAccessorFbp() {doTest();}

  @Test
  public void testMkAccessorRo() {doTest();}

  @Test
  public void testMkAccessorRoFbp() {doTest();}

  @Test
  public void testMkAccessorWo() {doTest();}

  @Test
  public void testMkAccessorWoFbp() {doTest();}

  @Test
  public void testMooRwpAttr() {doTest();}

  @Test
  public void testMooRwpAttrAll() {doTest();}

  @Test
  public void testMooGeneratedClearer() {doTest();}

  @Test
  public void testMooGeneratedPredicate() {doTest();}

  @Test
  public void testMooCustomClearer() {doTest();}

  @Test
  public void testMooCustomClearerBase() {doTest();}

  @Test
  public void testMooCustomClearerBaseRwp() {doTest();}

  @Test
  public void testMooCustomPredicate() {doTest();}

  @Test
  public void testMooCustomPredicateBase() {doTest();}

  @Test
  public void testMooCustomPredicateBaseRwp() {doTest();}

  @Test
  public void testMooGeneratedClearerAll() {doTest();}

  @Test
  public void testMooGeneratedClearerUnderscored() {doTest();}

  @Test
  public void testMooGeneratedClearerUnderscoredAll() {doTest();}

  @Test
  public void testMooGeneratedPredicateAll() {doTest();}

  @Test
  public void testMooGeneratedPredicateUnderscored() {doTest();}

  @Test
  public void testMooGeneratedPredicateUnderscoredAll() {doTest();}

  @Test
  public void testPodIndexWithAngles() {doTest();}

  @Test
  public void testCaptureGroupsScalars() {doTest();}

  @Test
  public void testNamespace() {
    doTest();
  }

  private void doTest() {
    doTestFindUsages();
  }
}
