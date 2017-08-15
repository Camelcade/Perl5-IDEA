/*
 * Copyright 2015-2017 Alexandr Evstigneev
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
import com.perl5.lang.htmlmason.filetypes.HTMLMasonFileType;
import com.perl5.lang.mason2.filetypes.MasonTopLevelComponentFileType;
import com.perl5.lang.mojolicious.filetypes.MojoliciousFileType;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 23.02.2016.
 */
public class PerlFoldingTestCase extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/folding";
  }

  public void testPerl() {
    doTest(PerlFileTypeScript.INSTANCE);
  }

  public void testMason2() {
    doTest(MasonTopLevelComponentFileType.INSTANCE);
  }

  public void testHtmlMason() {
    doTest(HTMLMasonFileType.INSTANCE);
  }

  public void testMojolicious() {
    doTest(MojoliciousFileType.INSTANCE);
  }

  private void doTest(@NotNull LanguageFileType fileType) {
    testFoldingRegions(getTestName(true), fileType);
  }
}