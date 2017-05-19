/*
 * Copyright 2016 Alexandr Evstigneev
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

import base.PerlLightCodeInsightFixtureTestCase;
import com.perl5.lang.htmlmason.filetypes.HTMLMasonFileType;
import com.perl5.lang.mason2.filetypes.MasonTopLevelComponentFileType;
import com.perl5.lang.mojolicious.filetypes.MojoliciousFileType;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;

/**
 * Created by hurricup on 23.02.2016.
 */
public class PerlFoldingTestCase extends PerlLightCodeInsightFixtureTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/folding";
  }

  public void testPerlFolding() {
    testFoldingRegions("perl_folding_test", PerlFileTypeScript.INSTANCE);
  }

  public void testMason2Folding() {
    testFoldingRegions("mason2_folding_test", MasonTopLevelComponentFileType.INSTANCE);
  }

  public void testHtmlMasonFolding() {
    testFoldingRegions("html_mason_folding_test", HTMLMasonFileType.INSTANCE);
  }

  public void testMojoliciousFolding() {
    testFoldingRegions("mojolicious_folding_test", MojoliciousFileType.INSTANCE);
  }
}