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

package lightElements;


import base.MojoLightTestCase;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import org.junit.Test;

public class MojoPerlLightElementsTest extends MojoLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/lightElements/perl";
  }

  @Override
  public String getFileExtension() {
    return PerlFileTypeScript.EXTENSION_PL;
  }

  @Test
  public void testMojoHelpers() {doTestLightElements();}
}
