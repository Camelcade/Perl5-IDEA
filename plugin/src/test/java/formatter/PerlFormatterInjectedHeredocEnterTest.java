/*
 * Copyright 2015-2025 Alexandr Evstigneev
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


import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class PerlFormatterInjectedHeredocEnterTest extends PerlFormatterTestCase {
  @Override
  protected String getBaseDataPath() {
    return "formatter/perl/injected_heredoc_enter";
  }

  @Test
  public void testUnindentableNested() {
    assertHasHtml();
    doTestEnter();}

  @Test
  public void testUnindentableTopLevel() {    assertHasHtml();
    doTestEnter();}

  @Test
  public void testIndentableNested() {    assertHasHtml();
    doTestEnter();}

  @Test
  public void testSqlAfterWhere() {
    assertHasSql();
    doTestEnter();}

  @Test
  public void testSqlBetweenAnds() {
    assertHasSql();
    doTestEnter();}

  @Test
  public void testSqlReformat() {
    assertHasSql();
    doFormatTest();
  }
}
