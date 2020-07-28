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

package parameterInfo;


import base.PerlLightTestCase;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import org.junit.Test;
public class PerlParameterInfoHandlerTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/parameterInfo";
  }

  @Test
  public void testPrintText() {doTest();}

  @Test
  public void testPrintHandleTextFirst() {doTest();}

  @Test
  public void testPrintHandleTextSecond() {doTest();}

  @Test
  public void testPrintHandleTextThird() {doTest();}

  @Test
  public void testPrintTextParens() {doTest();}

  @Test
  public void testPrintHandleTextFirstParens() {doTest();}

  @Test
  public void testPrintHandleTextSecondParens() {doTest();}

  @Test
  public void testPrintHandleTextThirdParens() {doTest();}

  @Test
  public void testSubSignatureCode() {doTest();}

  @Test
  public void testSubSignatureCodeParens() {doTest();}

  @Test
  public void testSubSignatureCodeSecond() {doTest();}

  @Test
  public void testSubSignatureCodeSecondNoComma() {doTest();}

  @Test
  public void testSubSignatureCodeSecondParens() {doTest();}

  @Test
  public void testSubSignatureOptional() {doTest();}

  @Test
  public void testSubSignatureSimple() {doTest();}

  @Test
  public void testSubSignatureWithEmpty() {doTest();}

  @Test
  public void testSubSignatureWithHash() {doTest();}

  @Test
  public void testSubSignatureWithList() {doTest();}

  @Test
  public void testSubSignatureWithSlurpyHash() {doTest();}

  @Test
  public void testSubSignatureWithSlurpyList() {doTest();}

  @Test
  public void testFuncStatic() {doTest();}

  @Test
  public void testFuncObject() {doTest();}

  @Test
  public void testMethodStatic() {doTest();}

  @Test
  public void testMethodObject() {doTest();}

  @Test
  public void testMethodWithInvocantStatic() {doTest();}

  @Test
  public void testMethodWithInvocantObject() {doTest();}

  @Test
  public void testConstObject() {doTest();}

  @Test
  public void testConstObjectMulti() {doTest();}

  @Test
  public void testConstStatic() {doTest();}

  @Test
  public void testConstStaticMulti() {doTest();}

  @Test
  public void testStaticNoParensAll() {doTest();}

  @Test
  public void testStaticNoParensEmpty() {doTest();}

  @Test
  public void testStaticParensAll() {doTest();}

  @Test
  public void testStaticParensEmpty() {doTest();}

  @Test
  public void testObjectParensAll() {doTest();}

  @Test
  public void testObjectParensAfterFirst() {doTest();}

  @Test
  public void testStaticParensAfterFirst() {doTest();}

  @Test
  public void testStaticNoParensAfterFirst() {doTest();}

  @Test
  public void testObjectParensEmpty() {doTest();}

  @Test
  public void testObjectParensInMissingMiddle() {doTest();}

  @Test
  public void testStaticParensInMissingMiddle() {doTest();}

  @Test
  public void testStaticNoParensInMissingMiddle() {doTest();}

  private void doTest() {
    myFixture.copyFileToProject("definitions.pl");
    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
    doTestParameterInfo();
  }
}
