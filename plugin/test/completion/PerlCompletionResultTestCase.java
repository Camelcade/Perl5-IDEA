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

import base.PerlLightTestCase;
import com.intellij.codeInsight.lookup.Lookup;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public abstract class PerlCompletionResultTestCase extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/completionResult/perl";
  }

  @Test
  public void testScalarGlobal() {doTest();}

  @Test
  public void testScalarGlobalEnd() {doTest();}

  @Test
  public void testScalarGlobalEndNotStarting() {doTest();}

  @Test
  public void testScalarGlobalEndSep() {doTest();}

  @Test
  public void testScalarGlobalMid() {doTest();}

  @Test
  public void testScalarGlobalMidNotStarting() {doTest();}

  @Test
  public void testScalarGlobalMidSep() {doTest();}

  @Test
  public void testScalar() {doTest();}

  @Test
  public void testScalarUnderscore() {doTest();}

  @Test
  public void testScalarBraced() {doTest();}

  @Test
  public void testScalarNoSigil() {doTest();}


  @Override
  public void initWithFileContent(String filename, String extension, String content) {
    super.initWithFileContent(filename, extension, processCompletionResultContent(content));
  }

  private void doTest() {
    doTestCompletionResult();
  }

  public static class NormalTest extends PerlCompletionResultTestCase {
    @Override
    protected @NotNull String getTestResultSuffix() {
      return ".normal";
    }

    @Override
    protected char getCompletionCompleteChar() {
      return Lookup.NORMAL_SELECT_CHAR;
    }
  }

  public static class ReplaceTest extends PerlCompletionResultTestCase {
    @Override
    protected @NotNull String getTestResultSuffix() {
      return ".replace";
    }

    @Override
    protected char getCompletionCompleteChar() {
      return Lookup.REPLACE_SELECT_CHAR;
    }
  }
}
