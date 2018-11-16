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

package intellilang;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.testFramework.UsefulTestCase;
import org.jetbrains.annotations.NotNull;

public class PerlHeredocUpdaterTest extends PerlHeredocInjectionTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/intellilang/perl/heredoc_updater";
  }

  public void testUnindentedUpdate() {
    doTestUnindentedUpdate("<html>\n" +
                           "<div>\n" +
                           "    <div>\n" +
                           "    </div>\n" +
                           "</div>\n" +
                           "</html>");
  }

  public void testUnindentedUpdateWithEmpty() {
    doTestUnindentedUpdate("");
  }

  public void testUnindentedUpdateWithNewLine() {
    doTestUnindentedUpdate("some text\n");
  }

  public void testUnindentedUpdateWithoutNewLine() {
    doTestUnindentedUpdate("some text");
  }

  public void testIndentedUpdate() {
    doTestIndentedUpdate("<html>\n" +
                         "<div>\n" +
                         "    <div>\n" +
                         "    </div>\n" +
                         "</div>\n" +
                         "</html>");
  }

  public void testIndentedUpdateWithEmpty() {
    doTestIndentedUpdate("");
  }

  public void testIndentedUpdateWithNewLine() {
    doTestIndentedUpdate("some text\n");
  }

  public void testIndentedUpdateWithoutNewLine() {
    doTestIndentedUpdate("some text");
  }

  private void doTestUnindentedUpdate(@NotNull String textToUpdate) {
    initWithTextSmart("{\n" +
                      "    <<EOM;\nbla<caret>bla\n" +
                      "EOM\n" +
                      "}\n");
    doTest(textToUpdate);
  }

  private void doTestIndentedUpdate(@NotNull String textToUpdate) {
    initWithTextSmart("{\n" +
                      "    <<~EOM;\nbla<caret>bla\n" +
                      "    EOM\n" +
                      "}\n");
    doTest(textToUpdate);
  }

  private void doTest(@NotNull String textToUpdate) {
    WriteCommandAction.writeCommandAction(getProject()).run(() -> getHeredocUnderCursor().updateText(textToUpdate));
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getFile().getText());
  }
}
