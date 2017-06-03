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

package parserHeavy;

/**
 * Created by hurricup on 28.02.2016.
 */

import categories.Heavy;
import org.junit.experimental.categories.Category;
import parser.PerlParserTestBase;

@Category(Heavy.class)
public class PerlSourcesParserTestCmd extends PerlParserTestBase {
  public static final String DATA_PATH = "testDataHeavy/parser/perl5/cmd";

  @Override
  protected String getTestDataPath() {
    return DATA_PATH;
  }

  public void testelsif() {
    doTest("elsif");
  }

  public void testfor() {
    doTest("for");
  }

  public void testmod() {
    doTest("mod");
  }

  public void testsubval() {
    doTest("subval");
  }

  public void testswitch() {
    doTest("switch");
  }
}
