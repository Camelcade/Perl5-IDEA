/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.refactoring.introduce.target;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import com.perl5.lang.perl.psi.PerlQuoted;
import com.perl5.lang.perl.psi.PsiPerlExpr;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;

public abstract class PerlTargetsCollector {
  public static final TokenSet UNINTRODUCIBLE_TOKENS = TokenSet.create(
    CONDITION_EXPR, NESTED_CALL, PARENTHESISED_EXPR,
    VARIABLE_DECLARATION_LEXICAL, VARIABLE_DECLARATION_GLOBAL, VARIABLE_DECLARATION_LOCAL
  );
  private static final TokenSet SEQUENTINAL_TOKENS = TokenSet.create(
    COMMA_SEQUENCE_EXPR, DEREF_EXPR,
    STRING_LIST,
    ADD_EXPR, MUL_EXPR, SHIFT_EXPR, BITWISE_AND_EXPR, BITWISE_OR_XOR_EXPR, AND_EXPR, OR_EXPR, LP_AND_EXPR, LP_OR_XOR_EXPR
  );

  @NotNull
  protected abstract List<PerlIntroduceTarget> computeTargetsAtCaret(@NotNull PsiElement element, int caretOffset);

  @NotNull
  protected abstract List<PerlIntroduceTarget> computeTargetsFromSelection(@NotNull PsiElement element, @NotNull TextRange selectionRange);

  /**
   * @return List of possible introduce targets for {@code file} opened in {@code editor}
   */
  @NotNull
  public static List<PerlIntroduceTarget> getIntroduceTargets(@NotNull Editor editor, @NotNull PsiFile file) {
    if (editor.getSelectionModel().hasSelection()) {
      return computeIntroduceTargetsFromSelection(editor, file);
    }

    List<PerlIntroduceTarget> targets = new ArrayList<>();
    int caretOffset = editor.getCaretModel().getOffset();
    PsiPerlExpr run = PsiTreeUtil.findElementOfClassAtOffset(file, caretOffset, PsiPerlExpr.class, false);
    while (run != null) {
      PsiElement finalRun = run;
      ObjectUtils.doIfNotNull(getCollector(run), it -> targets.addAll(it.computeTargetsAtCaret(finalRun, caretOffset)));
      run = PsiTreeUtil.getParentOfType(run, PsiPerlExpr.class);
    }
    return targets;
  }

  /**
   * Could be an extension point
   */
  @NotNull
  private static PerlTargetsCollector getCollector(@NotNull PsiElement run) {
    IElementType elementType = PsiUtilCore.getElementType(run);
    if (SEQUENTINAL_TOKENS.contains(elementType)) {
      return PerlSequentialElementTargetCollector.INSTANCE;
    }
    else if (run instanceof PerlQuoted) {
      return PerlQuotedTargetsCollector.INSTANCE;
    }
    return PerlGenericTargetsCollector.INSTANCE;
  }

  @NotNull
  private static List<PerlIntroduceTarget> computeIntroduceTargetsFromSelection(Editor editor, PsiFile file) {
    SelectionModel selectionModel = editor.getSelectionModel();
    int selectionStart = selectionModel.getSelectionStart();
    int selectionEnd = selectionModel.getSelectionEnd();
    PsiElement startElement = file.findElementAt(selectionStart);
    PsiElement endElement = file.findElementAt(selectionEnd > selectionStart ? selectionEnd - 1 : selectionEnd);
    if (startElement == null || endElement == null) {
      return Collections.emptyList();
    }
    PsiElement commonParent = PsiTreeUtil.findCommonParent(startElement, endElement);
    PsiElement wrappingExpression = PsiTreeUtil.getParentOfType(commonParent, PsiPerlExpr.class, false);
    while (wrappingExpression != null) {
      if (!UNINTRODUCIBLE_TOKENS.contains(PsiUtilCore.getElementType(wrappingExpression))) {
        break;
      }
      wrappingExpression = PsiTreeUtil.getParentOfType(wrappingExpression, PsiPerlExpr.class, true);
    }
    if (wrappingExpression == null) {
      return Collections.emptyList();
    }

    TextRange selectionRange = TextRange.create(selectionStart, selectionEnd);
    return getCollector(wrappingExpression).computeTargetsFromSelection(wrappingExpression, selectionRange);
  }
}
