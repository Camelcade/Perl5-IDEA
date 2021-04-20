/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package unit.reparse;

import base.TemplateToolkitLightTestCase;
import com.perl5.lang.tt2.TemplateToolkitLanguage;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class TemplateToolkitReparseTest extends TemplateToolkitLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/unit/reparse";
  }

  @Test
  public void testTemplateMidAfterLine() {doTest("edit");}

  @Test
  public void testTemplateMidBeforeLine() {doTest("edit");}

  @Test
  public void testTemplateMidBetweenLines() {doTest("edit");}

  @Test
  public void testTemplateBottom() {doTest("edit");}

  @Test
  public void testTemplateBottomClose() {doTest("[% END %]");}

  @Test
  public void testTemplateMid() {doTest("edit");}

  @Test
  public void testTemplateMidClose() {doTest("[% END %]");}

  @Test
  public void testTemplateTop() {doTest("edit");}

  @Test
  public void testTemplateTopClose() {doTest("[% END %]");}

  private void doTest(@NotNull String toType) {
    doTestReparse(toType, TemplateToolkitLanguage.INSTANCE);
  }

  private void doTestBs() {
    doTestReparseBs(TemplateToolkitLanguage.INSTANCE);
  }
}
