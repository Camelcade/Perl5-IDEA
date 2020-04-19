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

package parser;


import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public abstract class PerlFunctionParametersParserTestCase extends PerlParserTestBase {
  @Override
  protected final String getBaseDataPath() {
    return "testData/parser/perl/functionParameters/" + getDataDirName();
  }

  @NotNull
  protected abstract String getDataDirName();

  @Test
  public void testEmptySignature() {doTest();}

  @Test
  public void testSimpleSignature() {doTest();}

  @Test
  public void testNoSignature() {doTest();}

  @Test
  public void testNoSignatureAttrs() {doTest(false);}

  @Test
  public void testInvocant() {doTest();}

  @Test
  public void testInvocantWithNamed() {doTest();}

  @Test
  public void testInvocantWithPositional() {doTest();}

  @Test
  public void testInvocantWithPositionalNamed() {doTest();}

  public static class AfterTest extends PerlFunctionParametersParserTestCase {
    @NotNull
    @Override
    protected String getDataDirName() {
      return "after";
    }
  }

  public static class AroundTest extends PerlFunctionParametersParserTestCase {
    @NotNull
    @Override
    protected String getDataDirName() {
      return "around";
    }
  }

  public static class AugmentTest extends PerlFunctionParametersParserTestCase {
    @NotNull
    @Override
    protected String getDataDirName() {
      return "augment";
    }
  }

  public static class BeforeTest extends PerlFunctionParametersParserTestCase {
    @NotNull
    @Override
    protected String getDataDirName() {
      return "before";
    }
  }

  public static abstract class FunctionTestCase extends PerlFunctionParametersParserTestCase {
    @Test
    public void testInvocant() {doTest(false);}

    @Test
    public void testInvocantWithNamed() {doTest(false);}

    @Test
    public void testInvocantWithPositional() {doTest(false);}

    @Test
    public void testInvocantWithPositionalNamed() {doTest(false);}
  }

  public static class FunctionTest extends FunctionTestCase {
    @NotNull
    @Override
    protected String getDataDirName() {
      return "fun";
    }

    @Test
    public void testFunctionParametersFunOmited() {doTest();}

    @Test
    public void testFunctionParametersFunTyped() {doTest();}

    @Test
    public void testFunctionParametersFunTypedWithVars() {doTest();}

    @Test
    public void testFunctionParametersFunAttrs() {doTest();}

    @Test
    public void testFunctionParametersFunAttrsPrototype() {doTest();}

    @Test
    public void testFunctionParametersFunDefaultArgs() {doTest();}

    @Test
    public void testFunctionParametersFunDefaultCode() {doTest();}

    @Test
    public void testFunctionParametersFunDefaultEmpty() {doTest();}

    @Test
    public void testFunctionParametersFunDefaultUndef() {doTest();}

    @Test
    public void testFunctionParametersFunDefaultUsePreceding() {doTest();}

    @Test
    public void testFunctionParametersFunNamed() {doTest();}

    @Test
    public void testFunctionParametersFunNamedDefault() {doTest();}

    @Test
    public void testFunctionParametersFunPositionalAndNamed() {doTest();}

    @Test
    public void testFunctionParametersFunSlurpy() {doTest();}
  }

  public static class FunctionAnonTest extends FunctionTestCase {
    @NotNull
    @Override
    protected String getDataDirName() {
      return "funAnon";
    }
  }

  public static class MethodTest extends PerlFunctionParametersParserTestCase {
    @NotNull
    @Override
    protected String getDataDirName() {
      return "method";
    }

    @Test
    public void testNoSignatureAttrs() {doTest();}
  }

  public static class OverrideTest extends PerlFunctionParametersParserTestCase {
    @NotNull
    @Override
    protected String getDataDirName() {
      return "override";
    }
  }
}
