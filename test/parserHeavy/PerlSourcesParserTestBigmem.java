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

package parserHeavy;

/**
 * Created by hurricup on 28.02.2016.
 */

import categories.Heavy;
import org.junit.experimental.categories.Category;
import parser.PerlParserTestBase;

@Category(Heavy.class)
public class PerlSourcesParserTestBigmem extends PerlParserTestBase {
  public static final String DATA_PATH = "testDataHeavy/parser/perl5/bigmem";

  @Override
  protected String getTestDataPath() {
    return DATA_PATH;
  }

  public void testindex() {
    doTest("index");
  }

  public void testpos() {
    doTest("pos");
  }

  public void testread() {
    doTest("read");
  }

  public void testregexp() {
    doTest("regexp");
  }

  public void testsubst() {
    doTest("subst");
  }

  public void testvec() {
    doTest("vec");
  }
}
