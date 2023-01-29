/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package unit.perl.reparse;

import base.PerlLightTestCase;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class PerlVariableNameReparseTest extends PerlLightTestCase {
  private final @NotNull String myName;
  private final @NotNull String mySigil;

  public PerlVariableNameReparseTest(@NotNull String name, @NotNull String sigil) {
    myName = name;
    mySigil = sigil;
  }

  @Override
  protected String getBaseDataPath() {
    return "unit/perl/reparse/variableName";
  }

  public @NotNull String getSigil() {
    return mySigil;
  }


  @Test
  public void testName() { doTest("<sigil>scalar_n<caret>ame"); }

  @Test
  public void testBracedName() { doTest("<sigil>{scalar_n<caret>ame}"); }

  @Test
  public void testFqnName() { doTest("<sigil>Foo::Bar::scalar_n<caret>ame"); }

  @Test
  public void testBracedFqnName() {doTest("<sigil>{Foo::Bar::scalar_n<caret>ame}");}

  @Test
  public void testNameEnd() {doTest("<sigil>scalar_name<caret>;");}

  @Test
  public void testNameBracedEnd() {doTest("<sigil>{scalar_name<caret>}");}

  @Test
  public void testFqnNameEnd() {
    doTest("<sigil>Foo::Bar::scalar_name<caret>;");
  }

  @Test
  public void testBracedFqnNameEnd() {
    doTest("<sigil>{Foo::Bar::scalar_name<caret>}");
  }

  @Test
  public void testNameInString() { doTestInterpolated("qq/test<sigil>scalar_n<caret>ame something/"); }

  @Test
  public void testBracedNameInString() { doTestInterpolated("qq/test<sigil>{scalar_n<caret>ame}something/"); }

  @Test
  public void testFqnNameInString() { doTestInterpolated("qq/test<sigil>Foo::Bar::scalar_n<caret>ame test/"); }

  @Test
  public void testBracedFqnNameInString() { doTestInterpolated("qq/test<sigil>{Foo::Bar::scalar_n<caret>ame}test/"); }

  @Test
  public void testNameInStringEnd() {
    doTestInterpolated("qq/test<sigil>scalar_name<caret> something/");
  }

  @Test
  public void testBracedNameInStringEnd() {
    doTestInterpolated("qq/test<sigil>{scalar_name<caret>}something/");
  }

  @Test
  public void testFqnNameInStringEnd() {
    doTestInterpolated("qq/test<sigil>Foo::Bar::scalar_name<caret> test/");
  }

  @Test
  public void testBracedFqnNameInStringEnd() {
    doTestInterpolated("qq/test<sigil>{Foo::Bar::scalar_name<caret>}test/");
  }

  protected void doTest(@NotNull String codeSample) {
    initWithTextSmartWithoutErrors(codeSample.replace("<sigil>", getSigil()));
    doTestReparseWithoutInit("insertion");
  }


  protected void doTestInterpolated(@NotNull String codeSample) {
    Assume.assumeTrue("Applicable only for interpalatable variables: scalars and arrays", "$@".contains(mySigil));
    doTest(codeSample);
  }

  @Override
  protected @NotNull String computeAnswerFileName(@NotNull String appendix) {
    return FileUtil.join(myName, super.computeAnswerFileName(appendix));
  }

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
      {"Scalar", "$"},
      {"Array", "@"},
      {"Hash", "%"},
      {"Glob", "*"},
      {"Code", "&"},
    });
  }
}
