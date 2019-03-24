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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PerlStringList;
import com.perl5.lang.perl.psi.PsiPerlCommaSequenceExpr;
import com.perl5.lang.perl.psi.PsiPerlExpr;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;

public abstract class PerlIntroduceTargetsHandler {
  private static final Logger LOG = Logger.getInstance(PerlIntroduceTargetsHandler.class);
  private static final TokenSet UNINTRODUCIBLE_TOKENS = TokenSet.create(
    CONDITION_EXPR, NESTED_CALL, PARENTHESISED_EXPR,
    VARIABLE_DECLARATION_LEXICAL, VARIABLE_DECLARATION_GLOBAL, VARIABLE_DECLARATION_LOCAL,
    TR_REPLACEMENTLIST, TR_SEARCHLIST, TR_REGEX,
    PERL_REGEX, REPLACEMENT_REGEX,
    REGEX_EXPR
  );
  public static final TokenSet SEQUENTINAL_TOKENS = TokenSet.create(
    COMMA_SEQUENCE_EXPR, DEREF_EXPR, STRING_LIST,
    ADD_EXPR, MUL_EXPR, SHIFT_EXPR, BITWISE_AND_EXPR, BITWISE_OR_XOR_EXPR, AND_EXPR, OR_EXPR, LP_AND_EXPR, LP_OR_XOR_EXPR
  );
  private static final TokenSet ARRAY_CONTEXT_TOKENSET = TokenSet.create(
    ARRAY_VARIABLE, ARRAY_CAST_EXPR, ARRAY_SLICE, HASH_SLICE, COMMA_SEQUENCE_EXPR
  );
  private static final TokenSet HASH_CONTEXT_TOKENSET = TokenSet.create(
    HASH_VARIABLE, HASH_CAST_EXPR
  );

  @NotNull
  protected List<PerlIntroduceTarget> computeTargetsAtCaret(@NotNull PsiElement element, int caretOffset) {
    return isTargetableElement(element) ? Collections.singletonList(PerlIntroduceTarget.create(element)) : Collections.emptyList();
  }

  @NotNull
  protected List<PerlIntroduceTarget> computeTargetsFromSelection(@NotNull PsiElement element, @NotNull TextRange selectionRange) {
    return isTargetableElement(element) ? Collections.singletonList(PerlIntroduceTarget.create(element)) : Collections.emptyList();
  }

  /**
   * Generates a text for decaration of variable with {@code variableName} expression representing by {@code target}
   */
  @NotNull
  protected String createDeclarationStatementText(@NotNull String variableName, @NotNull PerlIntroduceTarget target) {
    return "my " + computeSigil(target) + variableName + " = " + createTargetExpressionText(target);
  }

  /**
   * @return a sigil for variable, that can represent a {@code target}
   */
  @NotNull
  protected String computeSigil(@NotNull PerlIntroduceTarget target) {
    PsiElement targetPlace = target.getPlace();
    IElementType targetType = PsiUtilCore.getElementType(targetPlace);
    String sigil;
    if (ARRAY_CONTEXT_TOKENSET.contains(targetType)) {
      sigil = "@";
    }
    else if (HASH_CONTEXT_TOKENSET.contains(targetType)) {
      sigil = "%";
    }
    else {
      sigil = "$";
    }
    return sigil;
  }

  /**
   * @return text of expression on the right side of declaration statement
   */
  @NotNull
  protected String createTargetExpressionText(@NotNull PerlIntroduceTarget target) {
    PsiElement place = target.getPlace();
    if (place == null) {
      return reportEmptyPlace();
    }

    return StringUtil.notNullize(place.getText());
  }

  @NotNull
  protected static String reportEmptyPlace() {
    LOG.error("Invalid target");
    return "'Something went wrong, please, report to developers with source sample'";
  }

  /**
   * Replaces a targets represented by {@code occurrences} with {@code replacement}
   *
   * @return list of inserted elements
   * @apiNote all occurrences should be from the same element. This api implemented for multi-replacements
   */
  @NotNull
  protected List<PsiElement> replaceTarget(@NotNull List<PerlIntroduceTarget> occurrences, @NotNull PsiElement replacement) {
    if (occurrences.size() > 1) {
      LOG.error("Unexpected multiple occurrences: " + occurrences.stream().map(it -> it.toString()).collect(Collectors.joining("; ")));
      return Collections.emptyList();
    }
    PsiElement occurrenceElement = Objects.requireNonNull(occurrences.get(0)).getPlace();
    if (occurrenceElement != null && occurrenceElement.isValid()) {
      return Collections.singletonList(occurrenceElement.replace(replacement));
    }
    else {
      LOG.error("Invalid occurrence element");
    }
    return Collections.emptyList();
  }

  /**
   * @return true iff element can be targeted for extraction
   */
  public static boolean isTargetableElement(@NotNull PsiElement element) {
    return !UNINTRODUCIBLE_TOKENS.contains(PsiUtilCore.getElementType(element)) || PerlPsiUtil.isMatchRegex(element);
  }

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
      ObjectUtils.doIfNotNull(getHandler(run), it -> targets.addAll(it.computeTargetsAtCaret(finalRun, caretOffset)));
      run = PsiTreeUtil.getParentOfType(run, PsiPerlExpr.class);
    }
    return targets;
  }

  /**
   * @return collection of names suggested for variable representing a {@code target}
   */
  @NotNull
  protected List<String> computeSuggestedNames(@NotNull PerlIntroduceTarget target) {
    String sigil = computeSigil(target);
    if ("$".equals(sigil)) {
      return Collections.singletonList("scalar");
    }
    else if ("@".equals(sigil)) {
      return Collections.singletonList("array");
    }
    return Collections.singletonList("hash");
  }

  /**
   * @return collection of names suggested for variable representing a {@code target}
   */
  @NotNull
  public static List<String> getSuggestedNames(@NotNull PerlIntroduceTarget target) {
    PsiElement place = target.getPlace();
    if (place == null) {
      LOG.error("Invalid target passed");
      return Collections.singletonList("InternalErrorReportToDevelopers");
    }
    return getHandler(place).computeSuggestedNames(target);
  }

  /**
   * Could be an extension point
   */
  @NotNull
  private static PerlIntroduceTargetsHandler getHandler(@NotNull PsiElement run) {
    IElementType elementType = PsiUtilCore.getElementType(run);

    if (run instanceof PerlStringList) {
      return PerlStringListTargetsHandler.INSTANCE;
    }
    else if (run instanceof PsiPerlCommaSequenceExpr) {
      return PerlListTargetsHandler.INSTANCE;
    }
    else if (SEQUENTINAL_TOKENS.contains(elementType)) {
      return PerlGenericSequentialElementTargetHandler.INSTANCE;
    }
    else if (run instanceof PerlString) {
      return PerlStringTargetsHandler.INSTANCE;
    }
    return PerlGenericTargetsHandler.INSTANCE;
  }

  @NotNull
  private static List<PerlIntroduceTarget> computeIntroduceTargetsFromSelection(@NotNull Editor editor, @NotNull PsiFile file) {
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
      if (isTargetableElement(wrappingExpression)) {
        break;
      }
      wrappingExpression = PsiTreeUtil.getParentOfType(wrappingExpression, PsiPerlExpr.class, true);
    }
    if (wrappingExpression == null) {
      return Collections.emptyList();
    }

    TextRange selectionRange = TextRange.create(selectionStart, selectionEnd);
    return getHandler(wrappingExpression).computeTargetsFromSelection(wrappingExpression, selectionRange);
  }

  /**
   * @return a statement to declare variable with {@code name} assigned with {@code target} or null if something went wrong
   */
  @Nullable
  public static PsiElement createTargetDeclarationStatement(@NotNull Project project,
                                                            @NotNull PerlIntroduceTarget target,
                                                            @NotNull String variableName) {
    String targetExpressionText =
      getHandler(Objects.requireNonNull(target.getPlace())).createDeclarationStatementText(variableName, target);
    PsiElement statement = PerlElementFactory.createStatement(project, targetExpressionText);
    if (statement == null) {
      LOG.error("Unable to create a statement for " + targetExpressionText + "; target was " + target);
      return null;
    }
    return statement;
  }

  /**
   * Replaces a targets represented by {@code occurrences} with {@code replacement}
   *
   * @return list of inserted elements
   * @apiNote all occurrences should be from the same element. This api implemented for multi-replacements
   */
  @NotNull
  public static List<PsiElement> replaceOccurences(@NotNull List<PerlIntroduceTarget> occurrences, @NotNull PsiElement replacement) {
    if (occurrences.isEmpty()) {
      LOG.warn("Empty occurrences passed to replacement");
    }
    PsiElement targetPlace = Objects.requireNonNull(occurrences.get(0)).getPlace();
    if (targetPlace == null) {
      return Collections.emptyList();
    }
    for (PerlIntroduceTarget it : occurrences) {
      if (!targetPlace.equals(it.getPlace())) {
        LOG.error("Invalid occurrence in the list of ocurrences");
        return Collections.emptyList();
      }
    }
    return targetPlace.isValid() ? getHandler(targetPlace).replaceTarget(occurrences, replacement) : Collections.emptyList();
  }
}
