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

package com.perl5.lang.pod.idea.surroundWith;

import com.google.common.annotations.VisibleForTesting;
import com.intellij.lang.surroundWith.Surrounder;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import com.perl5.PerlBundle;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.pod.parser.PodElementTypesGenerated.POD_SYMBOL;

@VisibleForTesting
public abstract class PodSurrounder implements Surrounder {
  @Override
  public String getTemplateDescription() {
    return PerlBundle.message("pod.intention.wrap.with", getFormatterLetter(), getFormatterDescription());
  }

  @Override
  public boolean isApplicable(@NotNull PsiElement @NotNull [] elements) {
    return true;
  }

  /**
   * @return a pod formatter letter
   */
  protected abstract char getFormatterLetter();

  /**
   * @return a pod formatter description - bold, italic, etc
   */
  protected abstract @NotNull String getFormatterDescription();


  @Override
  public @Nullable TextRange surroundElements(@NotNull Project project, @NotNull Editor editor, @NotNull PsiElement @NotNull [] elements)
    throws IncorrectOperationException {
    SelectionModel selectionModel = editor.getSelectionModel();
    if (!selectionModel.hasSelection()) {
      throw new IncorrectOperationException("No text selected");
    }
    int startOffset = selectionModel.getSelectionStart();
    int endOffset = selectionModel.getSelectionEnd();
    EditorHighlighter highlighter = editor.getHighlighter();
    HighlighterIterator highlighterIterator = highlighter.createIterator(startOffset);
    CharSequence documentChars = editor.getDocument().getCharsSequence();
    int angleSize = 1;
    int sequentialOpenAngles = 0;
    int sequentialCloseAngles = 0;
    StringBuilder sb = new StringBuilder();
    while (!highlighterIterator.atEnd() && highlighterIterator.getStart() <= endOffset) {
      IElementType tokenType = highlighterIterator.getTokenType();
      int effectiveTokenStart = Math.max(highlighterIterator.getStart(), startOffset);
      int effectiveTokenEnd = Math.min(highlighterIterator.getEnd(), endOffset);
      CharSequence text = documentChars.subSequence(effectiveTokenStart, effectiveTokenEnd);
      if (tokenType == POD_SYMBOL) {
        if (StringUtil.equals(text, "<")) {
          sequentialOpenAngles++;
          angleSize = Math.max(angleSize, sequentialOpenAngles + 1);
          sb.append(text);
        }
        else if (StringUtil.equals(text, ">")) {
          sequentialCloseAngles++;
          angleSize = Math.max(angleSize, sequentialCloseAngles + 1);
          sb.append(text);
        }
        else {
          sequentialOpenAngles = 0;
          sequentialCloseAngles = 0;
          if (StringUtil.equals(text, "|") || (StringUtil.equals(text, "/"))) {
            sb.append(PodRenderUtil.escapeTitle(text.toString()));
          }
          else {
            sb.append(text);
          }
        }
      }
      else {
        sequentialOpenAngles = 0;
        sequentialCloseAngles = 0;
        sb.append(text);
      }
      highlighterIterator.advance();
    }

    String openAngle = angleSize == 1 ? "<" : (StringUtil.repeat("<", angleSize) + " ");
    String closeAngle = angleSize == 1 ? ">" : (" " + StringUtil.repeat(">", angleSize));
    String content = sb.toString();
    String prefix = content.startsWith(" ") ? " " : "";
    String suffix = content.endsWith(" ") ? " " : "";
    String resultStart = prefix + getFormatterLetter() + openAngle + adjustContent(content);
    String result = resultStart + closeAngle + suffix;
    editor.getDocument().replaceString(startOffset, endOffset, result);
    selectionModel.removeSelection();
    return TextRange.from(startOffset + resultStart.length(), 0);
  }

  /**
   * @return make pre-insertion content adjustment, e.g. adding tailing pipe for a link
   */
  protected @NotNull String adjustContent(@NotNull String originalContent) {
    return originalContent.trim();
  }
}
