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

package com.perl5.lang.mojolicious.idea.liveTemplates;

import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.mojolicious.MojoliciousElementTypes;
import com.perl5.lang.mojolicious.MojoliciousLanguage;
import com.perl5.lang.mojolicious.psi.impl.MojoliciousFileImpl;
import com.perl5.lang.perl.idea.livetemplates.AbstractOutlineLiveTemplateProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class MojoliciousLiveTemplateProcessor extends AbstractOutlineLiveTemplateProcessor {
  protected boolean shouldAddMarkerAtLineStartingAtOffset(CharSequence buffer, int offset) {
    int bufferEnd = buffer.length();

    while (offset < bufferEnd) {
      char currentChar = buffer.charAt(offset++);
      if (currentChar == '%') {
        return false;
      }
      else if (currentChar == '\n') {
        return false;
      }
      else if (!Character.isWhitespace(currentChar)) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected boolean isMyFile(PsiFile file) {
    return file instanceof MojoliciousFileImpl;
  }

  @Override
  @NotNull
  protected Language getMyLanguage() {
    return MojoliciousLanguage.INSTANCE;
  }

  @Override
  @Nullable
  protected PsiElement getOutlineElement(PsiElement firstElement) {
    while (firstElement instanceof PsiWhiteSpace) {
      firstElement = firstElement.getNextSibling();
    }

    return PsiUtilCore.getElementType(firstElement) == MojoliciousElementTypes.MOJO_LINE_OPENER ? firstElement : null;
  }

  @Override
  @NotNull
  protected String getOutlineMarker() {
    return "% ";
  }
}
