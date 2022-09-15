/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

import com.intellij.lang.surroundWith.SurroundDescriptor;
import com.intellij.lang.surroundWith.Surrounder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.lexer.PodTokenSets;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.pod.lexer.PodElementTypesGenerated.POD_CODE;
import static com.perl5.lang.pod.lexer.PodElementTypesGenerated.POD_NEWLINE;

public class PodSurroundDescriptor implements SurroundDescriptor {
  private static final TokenSet NEGATION_TOKENS = TokenSet.orSet(
    PodTokenSets.POD_COMMANDS_TOKENSET, TokenSet.create(POD_NEWLINE, POD_CODE)
  );

  @Override
  public @NotNull PsiElement[] getElementsToSurround(PsiFile file, int startOffset, int endOffset) {
    PsiElement startElement = file.findElementAt(startOffset);
    if (startElement == null || !startElement.getLanguage().isKindOf(PodLanguage.INSTANCE)) {
      return PsiElement.EMPTY_ARRAY;
    }
    PsiElement endElement = file.findElementAt(endOffset);
    if (endElement == null && endOffset > 0 ||
        endElement != null && endElement.getTextRange().getStartOffset() == endOffset) {
      endElement = file.findElementAt(endOffset - 1);
    }
    if (endElement == null || !endElement.getLanguage().isKindOf(PodLanguage.INSTANCE)) {
      return PsiElement.EMPTY_ARRAY;
    }

    PsiElement commonParent = PsiTreeUtil.findCommonParent(startElement, endElement);
    if (commonParent == null) {
      return PsiElement.EMPTY_ARRAY;
    }

    if (!commonParent.equals(startElement) && !commonParent.equals(startElement.getParent()) ||
        !commonParent.equals(endElement) && !commonParent.equals(endElement.getParent())) {
      return PsiElement.EMPTY_ARRAY;
    }

    PsiElement run = startElement;
    while (run != null && run.getTextRange().getStartOffset() < endOffset) {
      if (!PsiTreeUtil.processElements(run, it -> !NEGATION_TOKENS.contains(PsiUtilCore.getElementType(it)))) {
        return PsiElement.EMPTY_ARRAY;
      }
      run = run.getNextSibling();
    }
    return new PsiElement[]{startElement, endElement};
  }

  @Override
  public @NotNull Surrounder[] getSurrounders() {
    return new Surrounder[]{
      new PodSurrounderBold(),
      new PodSurrounderCode(),
      new PodSurrounderFile(),
      new PodSurrounderIndex(),
      new PodSurrounderItalic(),
      new PodSurrounderLink(),
      new PodSurrounderNbsp()
    };
  }

  @Override
  public boolean isExclusive() {
    return false;
  }
}
