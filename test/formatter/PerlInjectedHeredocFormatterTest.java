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

package formatter;

public class PerlInjectedHeredocFormatterTest extends PerlFormatterTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/formatter/perl/injected_heredoc";
  }

  public void testUnindentable() {doFormatTest();}

  public void testUnindentableIndented() {doFormatTest();}

  public void testIndented() {doFormatTest();}

  public void testIndentableUnindented() {doFormatTest();}

  public void testIndentedImproperly() {doFormatTest();}
}
