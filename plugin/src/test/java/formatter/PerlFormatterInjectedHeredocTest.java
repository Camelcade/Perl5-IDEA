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

package formatter;


import org.junit.Test;
public class PerlFormatterInjectedHeredocTest extends PerlFormatterTestCase {
  @Override
  protected String getBaseDataPath() {
    return "formatter/perl/injected_heredoc";
  }

  @Test
  public void testUnindentable() {doFormatTest();}

  @Test
  public void testUnindentableIndented() {doFormatTest();}

  @Test
  public void testIndented() {doFormatTest();}

  @Test
  public void testIndentableUnindented() {doFormatTest();}

  @Test
  public void testIndentedImproperly() {doFormatTest();}

  @Test
  public void testIndentedStatement() {doFormatTest();}

  @Test
  public void testIndentedNestedHeredoc() {doFormatTest();}

  @Test
  public void testIndentedNestedHeredocUnindentable() {doFormatTest();}

  @Test
  public void testIndentedNestedHeredocWithIndentation() {doFormatTest();}
}
