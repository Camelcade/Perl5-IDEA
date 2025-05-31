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

package completion;


import base.PerlCompletionPopupTestCase;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Collection;

@SuppressWarnings("Junit4RunWithInspection")
@RunWith(Parameterized.class)
public class PerlCompletionPopupInSignatureTest extends PerlCompletionPopupTestCase {

  @Parameter public @NotNull String myName;
  @Parameter(1) public @NotNull String myPerTestCode;

  @Override
  protected String getBaseDataPath() {
    return "completionPopup/perl";
  }

  @Override
  public String getFileExtension() {
    return PerlFileTypePackage.EXTENSION;
  }

  @Test
  public void testEqualInSubSignature() {
    doTest("=");
  }

  @Override
  protected @NotNull String getPerTestCode() {
    return myPerTestCode;
  }

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return getSubLikeTestParameters();
  }
}
