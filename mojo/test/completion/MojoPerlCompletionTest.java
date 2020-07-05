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


import base.MojoLightTestCase;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import com.perl5.lang.perl.idea.project.PerlNamesCache;
import org.junit.Test;

public class MojoPerlCompletionTest extends MojoLightTestCase {
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    PerlNamesCache.getInstance(getProject()).forceCacheUpdate();
  }

  @Override
  protected String getBaseDataPath() {
    return "testData/completion/perl";
  }


  @Override
  public String getFileExtension() {
    return PerlFileTypeScript.EXTENSION_PL;
  }


  @Test
  public void testMojoliciousHelper() {
    doTestCompletion();
  }
}
