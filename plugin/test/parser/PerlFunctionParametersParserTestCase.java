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
  public void testNoSignature() {doTest();}

  @Test
  public void testNoSignatureAttrs() {doTest(false);}

  public static class AfterTest extends PerlFunctionParametersParserTestCase {
    @NotNull
    @Override
    protected String getDataDirName() {
      return "after";
    }

    @Test
    public void testFunctionParametersAfter() {doTest();}
  }

  public static class AroundTest extends PerlFunctionParametersParserTestCase {
    @NotNull
    @Override
    protected String getDataDirName() {
      return "around";
    }

    @Test
    public void testFunctionParametersAround() {doTest();}
  }

  public static class AugmentTest extends PerlFunctionParametersParserTestCase {
    @NotNull
    @Override
    protected String getDataDirName() {
      return "augment";
    }

    @Test
    public void testFunctionParametersAugment() {doTest();}
  }

  public static class BeforeTest extends PerlFunctionParametersParserTestCase {
    @NotNull
    @Override
    protected String getDataDirName() {
      return "before";
    }

    @Test
    public void testFunctionParametersBefore() {doTest();}
  }

  public static class FunctionTest extends PerlFunctionParametersParserTestCase {
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
    public void testFunctionParametersFun() {doTest();}

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

  public static class FunctionAnonTest extends PerlFunctionParametersParserTestCase {
    @NotNull
    @Override
    protected String getDataDirName() {
      return "funAnon";
    }

    @Test
    public void testFunctionParametersFunAnon() {doTest();}
  }

  public static class MethodTest extends PerlFunctionParametersParserTestCase {
    @NotNull
    @Override
    protected String getDataDirName() {
      return "method";
    }

    @Test
    public void testNoSignatureAttrs() {doTest();}

    @Test
    public void testFunctionParametersMethod() {doTest();}

    @Test
    public void testFunctionParametersMethodInvocant() {doTest();}

    @Test
    public void testFunctionParametersMethodInvocantNoArgs() {doTest();}
  }

  public static class OverrideTest extends PerlFunctionParametersParserTestCase {
    @NotNull
    @Override
    protected String getDataDirName() {
      return "override";
    }

    @Test
    public void testFunctionParametersOverride() {doTest();}
  }
}
