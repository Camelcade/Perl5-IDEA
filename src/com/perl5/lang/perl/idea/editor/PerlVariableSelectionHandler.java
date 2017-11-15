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

package com.perl5.lang.perl.idea.editor;

import com.intellij.codeInsight.editorActions.ExtendWordSelectionHandlerBase;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.templateLanguages.OuterLanguageElementImpl;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.impl.PerlVariableNameElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class PerlVariableSelectionHandler extends ExtendWordSelectionHandlerBase {
  @Override
  public boolean canSelect(PsiElement e) {
    return e instanceof PerlVariableNameElementImpl;
  }

  @Override
  public List<TextRange> select(PsiElement e, CharSequence editorText, int cursorOffset, Editor editor) {
    TextRange textRange = getRange(e);
    return textRange == null ? null : Collections.singletonList(textRange);
  }

  @Override
  public int getMinimalTextRangeLength(@NotNull PsiElement element, @NotNull CharSequence text, int cursorOffset) {
    TextRange range = getRange(element);
    return range == null ? super.getMinimalTextRangeLength(element, text, cursorOffset) : range.getLength();
  }

  @Nullable
  private static TextRange getRange(@NotNull PsiElement e) {
    PsiElement variable = e.getParent();
    return variable == null ? null : variable.getTextRange();
  }

  public static class OuterElementsSuppressor implements Condition<PsiElement> {
    @Override
    public boolean value(PsiElement element) {
      return !(element instanceof OuterLanguageElementImpl) ||
             !element.getContainingFile().getViewProvider().getBaseLanguage().isKindOf(PerlLanguage.INSTANCE);
    }
  }
}
