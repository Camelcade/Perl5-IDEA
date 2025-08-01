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

package com.perl5.lang.perl.idea.completion.inserthandlers;

import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtilEx;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.LEFT_PAREN;


public class SubSelectionHandler implements InsertHandler<LookupElement> {
  @Override
  public void handleInsert(final @NotNull InsertionContext context, @NotNull LookupElement item) {
    final Editor editor = context.getEditor();
    int caretOffset = editor.getCaretModel().getOffset();
    PsiElement targetElement = context.getFile().findElementAt(caretOffset);
    if (targetElement != null && targetElement.getNode().getElementType() == LEFT_PAREN) {
      return;
    }

    PsiElement subDefitnition = item.getPsiElement();
    EditorModificationUtilEx.insertStringAtCaret(editor, "()");

    // todo we need hint with prototype here, but prototypes handling NYI
    if (!(subDefitnition instanceof PerlSubDefinitionElement subDefinitionElement &&
          subDefinitionElement.getSubArgumentsList().isEmpty())) {
      editor.getCaretModel().moveCaretRelatively(-1, 0, false, false, true);
    }
  }
}
