/*
 * Copyright 2015-2022 Alexandr Evstigneev
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
import com.intellij.openapi.util.NlsSafe;
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
  protected static @NotNull @NlsSafe String ourMarker = "HEREDOC";

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
    String heredocString =
      contentText +
      "\n" +
      ourMarker +
      "\n";

    List<PsiElement> heredocElements = PerlElementFactory.createHereDocElements(project, quoteSymbol, ourMarker, "");

    PsiFile currentFile = stringElement.getContainingFile();
    int newLineIndex = StringUtil.indexOf(currentFile.getNode().getChars(), "\n", stringElement.getTextRange().getEndOffset());
    if (newLineIndex > 1) {
      PsiElement anchor = currentFile.findElementAt(newLineIndex);
      // fixme we should check here if \n in unbreakable entity - regex, qw, string, something else...
      if (anchor != null && (anchor.getParent() instanceof PerlString || anchor.getParent() instanceof PsiPerlStringList)) {
        return;
      }
    }
    else if (newLineIndex < 0) {
      heredocString = "\n" + heredocString;
      newLineIndex = currentFile.getTextLength() - 1;
    }

    final Document document = editor.getDocument();
    newLineIndex -= stringElement.getTextLength();
    PsiElement heredocOpener = heredocElements.get(0);
    newLineIndex += heredocOpener.getTextLength();
    stringElement.replace(heredocOpener); // replace string with heredoc opener
    var psiDocumentManager = PsiDocumentManager.getInstance(element.getProject());
    psiDocumentManager.doPostponedOperationsAndUnblockDocument(document);

    psiDocumentManager.doPostponedOperationsAndUnblockDocument(document);
    document.insertString(newLineIndex + 1, heredocString);
    psiDocumentManager.commitDocument(document);
  }

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
    PsiElement parent = element.getParent();
    PsiElement grandParent = parent == null ? null : parent.getParent();
    return !(grandParent instanceof PerlHeredocOpener) &&
           (parent instanceof PsiPerlStringDq || parent instanceof PsiPerlStringSq || parent instanceof PsiPerlStringXq);
  }

  @Override
  public @Nls @NotNull String getFamilyName() {
    return getText();
  }

  @Override
  public @NotNull String getText() {
    return PerlBundle.message("perl.intention.heredoc.last.prefix") + ourMarker;
  }
}
