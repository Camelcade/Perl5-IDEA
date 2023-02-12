/*
 * Copyright 2015-2023 Alexandr Evstigneev
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


import com.perl5.lang.mason2.filetypes.MasonPurePerlComponentFileType;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import unit.perl.parser.PerlParserTestBase;

public class Mason2ParserTest extends PerlParserTestBase {
  public Mason2ParserTest() {
    super(MasonPurePerlComponentFileType.PURE_PERL_COMPONENT_EXTENSION);
  }

  @Override
  protected String getBaseDataPath() {
    return "unit/perl/parser";
  }

  @Override
  protected @NotNull String getTestLibPath() {
    return TEST_LIB_PATH_FROM_NESTED;
  }

  @Test
  public void testTestComponent() {
    doTest(true);
  }
}
