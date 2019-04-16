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

package editor;

import base.PerlLightTestCase;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;

public class PerlParameterInfoHandlerTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/parameterInfo";
  }

  public void testSubSignatureOptional() {doTest();}

  public void testSubSignatureSimple() {doTest();}

  public void testSubSignatureWithEmpty() {doTest();}

  public void testSubSignatureWithHash() {doTest();}

  public void testSubSignatureWithList() {doTest();}

  public void testSubSignatureWithSlurpyHash() {doTest();}

  public void testSubSignatureWithSlurpyList() {doTest();}

  public void testFuncStatic() {doTest();}

  public void testFuncObject() {doTest();}

  public void testMethodStatic() {doTest();}

  public void testMethodObject() {doTest();}

  public void testMethodWithInvocantStatic() {doTest();}

  public void testMethodWithInvocantObject() {doTest();}

  public void testConstObject() {doTest();}

  public void testConstObjectMulti() {doTest();}

  public void testConstStatic() {doTest();}

  public void testConstStaticMulti() {doTest();}

  public void testStaticNoParensAll() {doTest();}

  public void testStaticNoParensEmpty() {doTest();}

  public void testStaticParensAll() {doTest();}

  public void testStaticParensEmpty() {doTest();}

  public void testObjectParensAll() {doTest();}

  public void testObjectParensAfterFirst() {doTest();}

  public void testStaticParensAfterFirst() {doTest();}

  public void testStaticNoParensAfterFirst() {doTest();}

  public void testObjectParensEmpty() {doTest();}

  public void testObjectParensInMissingMiddle() {doTest();}

  public void testStaticParensInMissingMiddle() {doTest();}

  public void testStaticNoParensInMissingMiddle() {doTest();}

  private void doTest() {
    myFixture.copyFileToProject("definitions.pl");
    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
    doTestParameterInfo();
  }
}
