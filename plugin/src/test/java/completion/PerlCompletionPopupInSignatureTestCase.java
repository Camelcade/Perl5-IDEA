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


import base.PerlCompletionPopupTestCase;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public abstract class PerlCompletionPopupInSignatureTestCase extends PerlCompletionPopupTestCase {

  @Override
  protected String getBaseDataPath() {
    return "completionPopup/perl";
  }

  @Override
  public String getFileExtension() {
    return PerlFileTypePackage.EXTENSION; // requires for package.. test
  }

  @Test
  public void testEqualInSubSignature() {
    doTest("=");
  }

  public static class After extends PerlCompletionPopupInSignatureTestCase {

    @Override
    protected @NotNull String getPerTestCode() {
      return "after somemethod";
    }
  }

  public static class Around extends PerlCompletionPopupInSignatureTestCase {

    @Override
    protected @NotNull String getPerTestCode() {
      return "around somemethod";
    }
  }

  public static class Augment extends PerlCompletionPopupInSignatureTestCase {

    @Override
    protected @NotNull String getPerTestCode() {
      return "augment somemethod";
    }
  }

  public static class Before extends PerlCompletionPopupInSignatureTestCase {

    @Override
    protected @NotNull String getPerTestCode() {
      return "before somemethod";
    }
  }

  public static class Fun extends PerlCompletionPopupInSignatureTestCase {

    @Override
    protected @NotNull String getPerTestCode() {
      return "fun somefun";
    }
  }

  public static class Func extends PerlCompletionPopupInSignatureTestCase {

    @Override
    protected @NotNull String getPerTestCode() {
      return "func somefunc";
    }
  }

  public static class FunExpr extends PerlCompletionPopupInSignatureTestCase {

    @Override
    protected @NotNull String getPerTestCode() {
      return "fun";
    }
  }

  public static class Method extends PerlCompletionPopupInSignatureTestCase {

    @Override
    protected @NotNull String getPerTestCode() {
      return "method somemethod";
    }
  }

  public static class MethodExpr extends PerlCompletionPopupInSignatureTestCase {

    @Override
    protected @NotNull String getPerTestCode() {
      return "method";
    }
  }

  public static class OverrideKw extends PerlCompletionPopupInSignatureTestCase {

    @Override
    protected @NotNull String getPerTestCode() {
      return "override somemethod";
    }
  }

  public static class Sub extends PerlCompletionPopupInSignatureTestCase {

    @Override
    protected @NotNull String getPerTestCode() {
      return "sub somesub";
    }
  }

  public static class SubExpr extends PerlCompletionPopupInSignatureTestCase {

    @Override
    protected @NotNull String getPerTestCode() {
      return "sub";
    }
  }
}
