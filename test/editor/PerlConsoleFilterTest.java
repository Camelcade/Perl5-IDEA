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

package editor;

import base.PerlLightTestCase;
import com.intellij.execution.filters.Filter;
import com.intellij.execution.impl.EditorHyperlinkSupport;
import com.intellij.openapi.editor.Document;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.idea.execution.filters.PerlConsoleFileLinkFilter;

public class PerlConsoleFilterTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/consoleFilter/perl";
  }

  public void testConfess() {doTest();}

  public void testDie() {doTest();}

  public void testPythonBug() {doTest();}

  private void doTest() {
    initWithFileSmart();
    Document document = getEditor().getDocument();
    int lines = document.getLineCount();

    PerlConsoleFileLinkFilter filter = new PerlConsoleFileLinkFilter(getProject());
    String documentText = document.getText();

    StringBuilder sb = new StringBuilder();
    for (int lineNumber = 0; lineNumber < lines; lineNumber++) {
      int lineStart = document.getLineStartOffset(lineNumber);

      String lineText = EditorHyperlinkSupport.getLineText(document, lineNumber, true);
      int endOffset = lineStart + lineText.length();
      Filter.Result result = filter.applyFilter(lineText, endOffset);
      if (result == null) {
        continue;
      }
      for (Filter.ResultItem resultItem : result.getResultItems()) {
        int linkStartOffset = resultItem.getHighlightStartOffset();
        int linkEndOffset = resultItem.getHighlightEndOffset();
        sb.append(linkStartOffset)
          .append(" - ")
          .append(linkEndOffset)
          .append("; ")
          .append('[')
          .append(documentText.substring(linkStartOffset, linkEndOffset))
          .append(']')
          .append(" => ")
          .append(resultItem.getHyperlinkInfo())
          .append("\n");
      }
    }

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }
}
