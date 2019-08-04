/*
 * Copyright 2015-2019 Alexandr Evstigneev
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
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import com.perl5.lang.pod.filetypes.PodFileType;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
public class PerlFoldingTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/folding";
  }

  @Test
  public void testUseNoSequence() {
    doTest(PerlFileTypeScript.INSTANCE);
  }

  @Test
  public void testUseNoArguments() {
    doTest(PerlFileTypeScript.INSTANCE);
  }

  @Test
  public void testPerl() {
    doTest(PerlFileTypeScript.INSTANCE);
  }

  @Test
  public void testPodInPerl() {
    doTest(PerlFileTypeScript.INSTANCE);
  }

  @Test
  public void testPod() {doTest(PodFileType.INSTANCE);}

  private void doTest(@NotNull LanguageFileType fileType) {
    testFoldingRegions(getTestName(true), fileType);
  }
}