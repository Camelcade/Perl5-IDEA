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

package unit.perl.reparse;

import base.PerlLightTestCase;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public abstract class PerlVariableNameReparseTestCase extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "unit/perl/reparse/variableName";
  }

  protected abstract String getSigil();

  @Test
  public void testName() {doTest("<sigil>scalar_n<caret>ame");}

  @Test
  public void testBracedName() {doTest("<sigil>{scalar_n<caret>ame}");}

  @Test
  public void testFqnName() {doTest("<sigil>Foo::Bar::scalar_n<caret>ame");}

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

  protected void doTest(@NotNull String codeSample) {
    initWithTextSmartWithoutErrors(codeSample.replace("<sigil>", getSigil()));
    doTestReparseWithoutInit("insertion");
  }

  @Override
  protected @NotNull String computeAnswerFileName(@NotNull String appendix) {
    return FileUtil.join(getClass().getSimpleName(), super.computeAnswerFileName(appendix));
  }

  public static class Hash extends PerlVariableNameReparseTestCase {
    @Override
    protected String getSigil() {
      return "%";
    }
  }

  public static class Glob extends PerlVariableNameReparseTestCase {
    @Override
    protected String getSigil() {
      return "*";
    }
  }

  public static class Code extends PerlVariableNameReparseTestCase {
    @Override
    protected String getSigil() {
      return "&";
    }
  }
}
