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


import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import unit.perl.parser.PerlParserTestBase;

import java.util.Collection;

import static base.Mason2LightTestCase.componentsExtensionsData;

@RunWith(Parameterized.class)
public class Mason2TemplatingParserTest extends PerlParserTestBase {

  public Mason2TemplatingParserTest(@NotNull String fileExt) {
    super(fileExt);
  }

  @Override
  protected String getBaseDataPath() {
    return "unit/template/parser";
  }

  @Override
  protected @NotNull String getTestLibPath() {
    return TEST_LIB_PATH_FROM_NESTED;
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

  @Override
  protected @NotNull String computeAnswerFileNameWithoutExtension(@NotNull String appendix) {
    return super.computeAnswerFileNameWithoutExtension(appendix + "." + getFileExtension());
  }

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return componentsExtensionsData();
  }
}
