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


import com.perl5.lang.mason2.filetypes.MasonInternalComponentFileType;
import com.perl5.lang.mason2.filetypes.MasonTopLevelComponentFileType;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import unit.perl.parser.PerlParserTestBase;

public class Mason2TemplatingParserTest extends PerlParserTestBase {
  public Mason2TemplatingParserTest() {
    super(MasonTopLevelComponentFileType.TOP_LEVEL_COMPONENT_EXTENSION);
  }

  protected Mason2TemplatingParserTest(@NotNull String fileExt) {
    super(fileExt);
  }

  @Override
  protected String getBaseDataPath() {
    return "testData/unit/template/parser";
  }

  @Test
  public void testTestComponent() {
    doTest(true);
  }

  @Test
  public void testLiveTemplates() {
    doTest(true);
  }

  @Test
  public void testIssue1077() {
    doTest(true);
  }

  public static class InternalComponent extends Mason2TemplatingParserTest {
    public InternalComponent() {
      super(MasonInternalComponentFileType.INTERNAL_COMPONENT_EXTENSION);
    }

    @Override
    protected @NotNull String computeAnswerFileNameWithoutExtension(@NotNull String appendix) {
      return super.computeAnswerFileNameWithoutExtension(appendix + ".internal");
    }
  }
}
