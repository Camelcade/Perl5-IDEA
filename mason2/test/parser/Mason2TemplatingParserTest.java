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

package parser;


import com.perl5.lang.mason2.filetypes.Mason2FileTypeFactory;
import org.junit.Test;
public class Mason2TemplatingParserTest extends PerlParserTestBase {
  public Mason2TemplatingParserTest() {
    super(Mason2FileTypeFactory.TOP_LEVEL_COMPONENT_EXTENSION);
  }

  @Override
  protected String getTestDataPath() {
    return "testData/parser/template";
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

}
