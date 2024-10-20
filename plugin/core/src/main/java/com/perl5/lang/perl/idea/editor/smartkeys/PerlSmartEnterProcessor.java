/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.editor.smartkeys;

import com.intellij.codeInsight.editorActions.smartEnter.SmartEnterProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlBlockOwner;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;

public class PerlSmartEnterProcessor extends SmartEnterProcessor {
  private static final TokenSet BLOCK_OWNER_FALLBACK_KEYWORDS = TokenSet.create(
    RESERVED_METHOD, RESERVED_FUNC, RESERVED_FUN,
    RESERVED_AROUND_FP, RESERVED_BEFORE_FP, RESERVED_AFTER_FP, RESERVED_AUGMENT_FP, RESERVED_OVERRIDE_FP
  );

  @Override
  public boolean process(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile psiFile) {
    int offset = editor.getCaretModel().getOffset();
    PsiElement element = psiFile.getViewProvider().findElementAt(offset > 0 ? offset - 1 : offset, PerlLanguage.INSTANCE);

    while (element != null && !(element instanceof PsiFile)) {
      if (element instanceof PsiPerlStatement perlStatement && !StringUtil.endsWithChar(element.getNode().getChars(), ';')) {
        if (isBlockOwnerFallback(perlStatement)) {
          addBlock(editor, element);
          return false;
        }
        else {
          addSemicolon(editor, element);
        }
      }
      else if (element instanceof PerlBlockOwner blockOwner && blockOwner.getBlock() == null) {
        addBlock(editor, element);
        return false;
      }
      element = element.getParent();
    }

    return false;
  }

  private boolean isBlockOwnerFallback(@NotNull PsiPerlStatement statement) {
    if (statement instanceof PerlSubDeclarationElement) {
      return true;
    }
    if (statement.getStatementModifier() != null) {
      return false;
    }
    PsiPerlExpr expr = statement.getExpr();
    if (!(expr instanceof PsiPerlSubCall perlSubCall)) {
      return false;
    }
    PsiPerlMethod method = perlSubCall.getMethod();
    if (method == null || method.getNamespaceElement() != null) {
      return false;
    }
    return BLOCK_OWNER_FALLBACK_KEYWORDS.contains(PsiUtilCore.getElementType(method.getFirstChild()));
  }

  private void addSemicolon(@NotNull Editor editor, @NotNull PsiElement element) {
    editor.getDocument().insertString(element.getTextRange().getEndOffset(), ";");
  }

  private void addBlock(@NotNull Editor editor, @NotNull PsiElement element) {
    editor.getDocument().insertString(element.getTextRange().getEndOffset(), "{\n}");
  }
}
