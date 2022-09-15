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

package documentation;


import org.junit.Test;

public class PerlFunctionParametersQuickDocTest extends PerlQuickDocTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withFunctionParameters();
  }

  @Override
  protected String getBaseDataPath() {
    return "documentation/perl/quickdoc/functionParameters";
  }

  @Test
  public void testSignatureDefault() {
    doTest();
  }

  @Test
  public void testSignatureInvocant() {
    doTest();
  }

  @Test
  public void testSignatureInvocantAround() {
    doTest();
  }

  @Test
  public void testSignatureNamed() {
    doTest();
  }

  @Test
  public void testSignatureUnnamed() {
    doTest();
  }

  @Test
  public void testKeywordAfter() {
    doTest();
  }

  @Test
  public void testKeywordAround() {
    doTest();
  }

  @Test
  public void testKeywordAugment() {
    doTest();
  }

  @Test
  public void testKeywordBefore() {
    doTest();
  }

  @Test
  public void testKeywordFun() {
    doTest();
  }

  @Test
  public void testKeywordFunAnon() {
    doTest();
  }

  @Test
  public void testKeywordMethod() {
    doTest();
  }

  @Test
  public void testKeywordMethodAnon() {
    doTest();
  }

  @Test
  public void testKeywordOverride() {
    doTest();
  }
}
