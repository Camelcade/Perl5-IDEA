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

package com.perl5.lang.perl.idea.livetemplates;

import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.impl.TemplateContext;
import com.intellij.codeInsight.template.impl.TemplateOptionalProcessor;
import com.intellij.lang.Language;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public abstract class AbstractOutlineLiveTemplateProcessor implements TemplateOptionalProcessor {
  /**
   * Checks if psi file should be handled by processor
   */
  protected abstract boolean isMyFile(PsiFile file);

  /**
   * Checks if marker is already there or this is empty line and no outline is necessary
   */
  protected abstract boolean shouldAddMarkerAtLineStartingAtOffset(CharSequence buffer, int offset);

  /**
   * Returns base language of the file, could take from psiFile though
   */
  protected abstract @NotNull Language getMyLanguage();

  /**
   * Returns outline marker text to insert
   */
  @SuppressWarnings("SameReturnValue")
  protected @NotNull String getOutlineMarker() {
    return "% ";
  }

  /**
   * Attempts to find an outline marker by the first psi element in the line.
   *
   * @param firstElement first element of the line
   * @return outline marker or null if not found
   */
  protected abstract @Nullable PsiElement getOutlineElement(PsiElement firstElement);

  @Override
  public @Nls String getOptionName() {
    return PerlBundle.message("please.report.a.bug");
  }

  @Override
  public boolean isEnabled(Template template) {
    return true;
  }

  @Override
  public boolean isVisible(@NotNull Template template, @NotNull TemplateContext context) {
    return false;
  }

  @Override
  public void processText(Project project, Template template, Document document, RangeMarker templateRange, Editor editor) {
    PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
    if (!isMyFile(file)) {
      return;
    }

    int startOffset = templateRange.getStartOffset();
    int startLine = document.getLineNumber(startOffset);
    int endLine = document.getLineNumber(templateRange.getEndOffset());
    if (startLine == endLine) {
      return;
    }

    int startLineBeginOffset = document.getLineStartOffset(startLine);

    assert file != null;
    PsiElement firstElement = file.getViewProvider().findElementAt(startLineBeginOffset, getMyLanguage());
    firstElement = getOutlineElement(firstElement);
    if (firstElement == null) {
      return;
    }

    CharSequence charsSequence = document.getCharsSequence();
    for (int currentLine = endLine; currentLine > startLine; currentLine--) {
      int lineStartOffset = document.getLineStartOffset(currentLine);
      if (shouldAddMarkerAtLineStartingAtOffset(charsSequence, lineStartOffset)) {
        document.insertString(lineStartOffset, getOutlineMarker());
      }
    }
  }
}
