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

package com.perl5.lang.perl.idea.intentions;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class StringToLastHeredocIntention extends PsiElementBaseIntentionAction implements IntentionAction {
  protected static String HEREDOC_MARKER = "HEREDOC";

  @Override
  public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
    PsiElement stringElement = element.getParent();
    assert stringElement instanceof PerlString;
    char quoteSymbol = '"';
    if (stringElement instanceof PsiPerlStringSq) {
      quoteSymbol = '\'';
    }
    else if (stringElement instanceof PsiPerlStringXq) {
      quoteSymbol = '`';
    }

    String contentText = ElementManipulators.getValueText(stringElement);
    List<PsiElement> heredocElements = PerlElementFactory.createHereDocElements(
      project,
      quoteSymbol,
      HEREDOC_MARKER,
      ""
    );

    PsiFile currentFile = stringElement.getContainingFile();
    int newLineIndex = StringUtil.indexOf(currentFile.getNode().getChars(), "\n", stringElement.getTextRange().getEndOffset());
    if (newLineIndex > 1) {
      PsiElement anchor = currentFile.findElementAt(newLineIndex);
      // fixme we should check here if \n in unbreakable entity - regex, qw, string, something else...
      if (anchor != null && (anchor.getParent() instanceof PerlString || anchor.getParent() instanceof PsiPerlStringList)) {
        return;
      }
    }

    final PsiDocumentManager manager = PsiDocumentManager.getInstance(element.getProject());
    final Document document = manager.getDocument(currentFile);

    if (document == null) {
      return;
    }

    newLineIndex -= stringElement.getTextLength();
    PsiElement heredocOpener = heredocElements.get(0);
    newLineIndex += heredocOpener.getTextLength();
    stringElement.replace(heredocOpener); // replace string with heredoc opener
    manager.doPostponedOperationsAndUnblockDocument(document);

    String heredocString =
      contentText +
      "\n" +
      HEREDOC_MARKER +
      "\n";

    manager.doPostponedOperationsAndUnblockDocument(document);
    document.insertString(newLineIndex + 1, heredocString);
    manager.commitDocument(document);
  }

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
    PsiElement parent = element.getParent();
    PsiElement grandParent = parent.getParent();
    return !(grandParent instanceof PerlHeredocOpener) &&
           (parent instanceof PsiPerlStringDq || parent instanceof PsiPerlStringSq || parent instanceof PsiPerlStringXq);
  }

  @Nls
  @NotNull
  @Override
  public String getFamilyName() {
    return getText();
  }

  @NotNull
  @Override
  public String getText() {
    return PerlBundle.message("perl.intention.heredoc.last.prefix") + HEREDOC_MARKER;
  }
}
