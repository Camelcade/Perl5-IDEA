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

package unit.parser;


import com.perl5.lang.mojolicious.filetypes.MojoliciousFileType;
import org.junit.Ignore;
import org.junit.Test;

public class MojoTemplateParserTest extends MojoliciousParserTestBase {
  public MojoTemplateParserTest() {
    super(MojoliciousFileType.MOJO_DEFAULT_EXTENSION);
  }

  @Override
  protected String getBaseDataPath() {
    return "testData/unit/templates/parser";
  }

  @Test
  public void testMojoParserTest() {
    doTest();
  }

  @Ignore(value = "Need to fix begin in expression #2265")
  @Test
  public void testBeginAfterFatComma() {
    setSkipSpaces(false);
    doTest();
  }

  @Ignore(value = "Need to fix begin in expression #2265")
  @Test
  public void testBeginAfterFatComma2() {
    setSkipSpaces(false);
    doTest();
  }
}
