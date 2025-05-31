/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package unit.perl.parser;


import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Arrays;
import java.util.Collection;

@SuppressWarnings("Junit4RunWithInspection")
@RunWith(Parameterized.class)
public class PerlFunctionParametersParserTestCase extends PerlParserTestBase {

  @Parameter public @NotNull String myDataDirName;
  @Parameter(1) public boolean myIsFunction;

  @Override
  protected final String getBaseDataPath() {
    return "unit/perl/parser/functionParameters/" + getDataDirName();
  }

  protected @NotNull String getDataDirName() {
    return myDataDirName;
  }

  @Test
  public void testJoined() { doTest(false); }

  @Test
  public void testOmited() { doTest(); }

  @Test
  public void testOmitedArray() { doTest(); }

  @Test
  public void testTyped() {doTest();}

  @Test
  public void testTypedWithVars() {doTest();}

  @Test
  public void testAttrs() {doTest();}

  @Test
  public void testAttrsPrototype() {doTest();}

  @Test
  public void testDefaultArgs() {doTest();}

  @Test
  public void testDefaultCode() {doTest();}

  @Test
  public void testDefaultEmpty() {doTest();}

  @Test
  public void testDefaultUndef() {doTest();}

  @Test
  public void testDefaultUsePreceding() {doTest();}

  @Test
  public void testNamed() {doTest();}

  @Test
  public void testNamedDefault() {doTest();}

  @Test
  public void testPositionalAndNamed() {doTest();}

  @Test
  public void testSlurpy() {doTest();}

  @Test
  public void testEmptySignature() {doTest();}

  @Test
  public void testSimpleSignature() {doTest();}

  @Test
  public void testNoSignature() {doTest();}

  @Test
  public void testNoSignatureAttrs() {doTest(false);}

  @Test
  public void testInvocant() { doTest(!myIsFunction); }

  @Test
  public void testInvocantWithNamed() { doTest(!myIsFunction); }

  @Test
  public void testInvocantWithPositional() { doTest(!myIsFunction); }

  @Test
  public void testInvocantWithPositionalNamed() { doTest(!myIsFunction); }

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
      {"fun", true},
      {"funAnon", true},
      {"methodAnon", true},
      {"override", false},
      {"method", false},
      {"augment", false},
      {"before", false},
      {"around", false},
      {"after", false},
    });
  }
}
