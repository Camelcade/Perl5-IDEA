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

package com.perl5.lang.perl.idea.refactoring.introduce.target;

import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.impl.PsiPerlPerlRegexImpl;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;

public abstract class PerlIntroduceTargetsHandler {
  private static final Logger LOG = Logger.getInstance(PerlIntroduceTargetsHandler.class);
  private static final TokenSet UNINTRODUCIBLE_TOKENS = TokenSet.create(
    CONDITION_EXPR, PARENTHESISED_EXPR,
    VARIABLE_DECLARATION_LEXICAL, VARIABLE_DECLARATION_GLOBAL, VARIABLE_DECLARATION_LOCAL,
    TR_REPLACEMENTLIST, TR_SEARCHLIST, TR_REGEX,
    PERL_REGEX, REPLACEMENT_REGEX
  );
  public static final TokenSet SEQUENTINAL_TOKENS = TokenSet.create(
    COMMA_SEQUENCE_EXPR, DEREF_EXPR, STRING_LIST,
    ADD_EXPR, MUL_EXPR, SHIFT_EXPR, BITWISE_AND_EXPR, BITWISE_OR_XOR_EXPR, AND_EXPR, OR_EXPR, LP_AND_EXPR, LP_OR_XOR_EXPR
  );
  private static final TokenSet ARRAY_CONTEXT_TOKENSET = TokenSet.create(
    ARRAY_VARIABLE, ARRAY_CAST_EXPR, ARRAY_SLICE, HASH_SLICE, COMMA_SEQUENCE_EXPR,
    SORT_EXPR, GREP_EXPR, MAP_EXPR
  );
  private static final TokenSet FAKE_SUB_EXPR_CONTAINERS = TokenSet.create(
    SORT_EXPR, EVAL_EXPR, DO_BLOCK_EXPR
  );
  private static final TokenSet HASH_CONTEXT_TOKENSET = TokenSet.create(
    HASH_VARIABLE, HASH_CAST_EXPR
  );

  private boolean isApplicable(@NotNull PsiElement element) {
    return isTargetableElement(element) && !(element instanceof PerlHeredocElementImpl) &&
           (!(element.getParent() instanceof PerlHeredocElementImpl) || isTargetableHeredocElement(element.getParent()));
  }

  protected @NotNull List<PerlIntroduceTarget> computeTargetsAtCaret(@NotNull PsiElement element, int caretOffset) {
    return isApplicable(element) ? Collections.singletonList(PerlIntroduceTarget.create(element)) : Collections.emptyList();
  }

  protected @NotNull List<PerlIntroduceTarget> computeTargetsFromSelection(@NotNull PsiElement element, @NotNull TextRange selectionRange) {
    return isApplicable(element) ? Collections.singletonList(PerlIntroduceTarget.create(element)) : Collections.emptyList();
  }

  /**
   * Generates a text for decaration of variable with {@code variableName} expression representing by {@code target}
   */
  protected @NotNull String createDeclarationStatementText(@NotNull String variableName, @NotNull PerlIntroduceTarget target) {
    return "my " + doComputeVariableType(target).getSigil() + variableName + " = " + createTargetExpressionText(target) + ";";
  }

  /**
   * @return a sigil for variable, that can represent a {@code target}
   */
  protected @NotNull PerlVariableType doComputeVariableType(@NotNull PerlIntroduceTarget target) {
    PsiElement targetPlace = target.getPlace();
    IElementType targetType = PsiUtilCore.getElementType(targetPlace);
    if (ARRAY_CONTEXT_TOKENSET.contains(targetType)) {
      return PerlVariableType.ARRAY;
    }
    else if (HASH_CONTEXT_TOKENSET.contains(targetType)) {
      return PerlVariableType.HASH;
    }

    return PerlVariableType.SCALAR;
  }

  public static @Nullable PerlVariableType computeVariableType(@NotNull PerlIntroduceTarget target) {
    PsiElement targetPlace = target.getPlace();
    if (targetPlace == null) {
      LOG.error("Unable to get target place for " + target);
      return null;
    }
    return getHandler(targetPlace).doComputeVariableType(target);
  }

  /**
   * @return text of expression on the right side of declaration statement
   */
  protected @NotNull String createTargetExpressionText(@NotNull PerlIntroduceTarget target) {
    PsiElement targetElement = target.getPlace();
    switch (targetElement) {
      case null -> {
        return reportEmptyPlace();
      }
      case PsiPerlPackageExpr packageExpr -> {
        return "'" + PerlPackageUtil.getCanonicalNamespaceName(StringUtil.notNullize(packageExpr.getText())) + "'";
      }
      case PsiPerlMatchRegex matchRegex -> {
        char openQuote = matchRegex.getOpenQuote();
        PsiPerlPerlRegex regex = matchRegex.getRegex();
        if (openQuote != 0 && regex != null) {
          char closeQuote = PerlString.getQuoteCloseChar(openQuote);
          PsiPerlPerlRegexModifiers modifiers = matchRegex.getPerlRegexModifiers();
          String regexText = "qr " + openQuote + regex.getText() + closeQuote;
          return modifiers == null ? regexText : regexText + modifiers.getText();
        }
      }
      case PsiPerlPerlRegexImpl regex -> {
        PsiElement container = regex.getParent();
        if (container instanceof PerlReplacementRegex replacementRegex) {
          char openQuote = replacementRegex.getOpenQuote();
          if (openQuote > 0) {
            char closeQuote = PerlString.getQuoteCloseChar(openQuote);
            return "qr " + openQuote + regex.getText() + closeQuote;
          }
        }
      }
      default -> {
      }
    }

    return StringUtil.notNullize(targetElement.getText());
  }

  /**
   * Replaces a targets represented by {@code occurrences} with {@code replacement}
   *
   * @return list of inserted elements
   * @apiNote all occurrences should be from the same element. This api implemented for multi-replacements
   */
  protected @NotNull List<PsiElement> replaceTarget(@NotNull List<PerlIntroduceTarget> occurrences, @NotNull PsiElement replacement) {
    if (occurrences.size() > 1) {
      LOG.error(
        "Unexpected multiple occurrences: " + occurrences.stream().map(PerlIntroduceTarget::toString).collect(Collectors.joining("; ")));
      return Collections.emptyList();
    }
    PsiElement occurrenceElement = Objects.requireNonNull(occurrences.getFirst()).getPlace();
    if (occurrenceElement != null && occurrenceElement.isValid()) {
      PsiElement occurrenceElementParent = occurrenceElement.getParent();
      if (occurrenceElementParent instanceof PerlHeredocElementImpl) {
        PsiElement nextSibling = occurrenceElement.getNextSibling();
        if (nextSibling != null && Character.isUnicodeIdentifierPart(nextSibling.getNode().getChars().charAt(0))) {
          CharSequence replacementText = PerlGenericStringTargetsHandler.braceVariableText(replacement.getText());
          PsiElement statement = PerlElementFactory.createStatement(replacement.getProject(), replacementText.toString());
          if (statement == null) {
            LOG.error("Error generating code from: " + replacementText);
            return Collections.emptyList();
          }
          replacement = statement.getFirstChild();
        }
      }
      return Collections.singletonList(occurrenceElement.replace(replacement));
    }
    else {
      LOG.error("Invalid occurrence element");
    }
    return Collections.emptyList();
  }

  static @NotNull String reportEmptyPlace() {
    LOG.error("Invalid target");
    return "'Something went wrong, please, report to developers with source sample'";
  }

  /**
   * @return true iff element can be targeted for extraction
   */
  public static boolean isTargetableElement(@NotNull PsiElement element) {
    if (isTargetableHeredocElement(element)) {
      return true;
    }
    IElementType elementType = PsiUtilCore.getElementType(element);
    if (elementType == SUB_EXPR && FAKE_SUB_EXPR_CONTAINERS.contains(PsiUtilCore.getElementType(element.getParent()))) {
      return false;
    }
    if (elementType == SUB_CALL && element.getParent() instanceof PsiPerlDerefExpr) {
      return false;
    }
    if (!UNINTRODUCIBLE_TOKENS.contains(elementType)) {
      return true;
    }
    return PerlPsiUtil.isMatchRegex(element);
  }

  /**
   * @return List of possible introduce targets for {@code file} opened in {@code editor}
   */
  public static @NotNull List<PerlIntroduceTarget> getIntroduceTargets(@NotNull Editor editor, @NotNull PsiFile file) {
    if (editor.getSelectionModel().hasSelection()) {
      return computeIntroduceTargetsFromSelection(editor, file);
    }

    List<PerlIntroduceTarget> targets = new ArrayList<>();

    int caretOffset = editor.getCaretModel().getOffset();
    PsiElement run = getElementToStart(file, caretOffset);
    if (run == null) {
      run = getElementToStart(file, TargetElementUtil.adjustOffset(file, editor.getDocument(), caretOffset));
    }

    while (run != null) {
      PsiElement finalRun = run;
      if (isHeredocOpenerInSight(run)) {
        return targets;
      }

      targets.addAll(getHandler(run).computeTargetsAtCaret(finalRun, caretOffset));
      run = PsiTreeUtil.getParentOfType(run, PsiPerlExpr.class, PerlHeredocElementImpl.class);
    }
    return targets;
  }

  private static PsiElement getElementToStart(@NotNull PsiFile file, int caretOffset) {
    PsiElement run = PsiTreeUtil.findElementOfClassAtOffset(file, caretOffset, PsiPerlExpr.class, false);
    if (run == null) {
      run = PsiTreeUtil.findElementOfClassAtOffset(file, caretOffset, PerlHeredocElementImpl.class, false);
    }
    if (run instanceof PsiPerlParenthesisedExpr) {
      run = ((PsiPerlParenthesisedExpr)run).getExpr();
    }
    return run;
  }

  /**
   * @return true iff element, it's parent or one of it's children is a {@link PerlHeredocOpener}
   */
  private static boolean isHeredocOpenerInSight(PsiElement element) {
    if (element instanceof PerlHeredocOpener || element.getParent() instanceof PerlHeredocOpener) {
      return true;
    }

    for (PsiElement runChild : element.getChildren()) {
      if (runChild instanceof PerlHeredocOpener) {
        return true;
      }
    }
    return false;
  }

  /**
   * Computes a target expression element
   */
  public static @Nullable PsiElement createTargetExpressionElement(@NotNull PerlIntroduceTarget target) {
    PsiElement targetElement = target.getPlace();
    PerlIntroduceTargetsHandler handler = getHandler(targetElement);
    if (handler == null) {
      return null;
    }

    String targetExpressionText = handler.createTargetExpressionText(target);
    if (StringUtil.isEmptyOrSpaces(targetExpressionText)) {
      return null;
    }

    PsiElement statement = PerlElementFactory.createStatement(targetElement.getProject(), targetExpressionText);
    if (statement == null) {
      return null;
    }
    return statement.getFirstChild();
  }

  /**
   * Could be an extension point
   */
  @Contract("null->null;!null->!null")
  private static PerlIntroduceTargetsHandler getHandler(@Nullable PsiElement run) {
    if (run == null) {
      return null;
    }
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
      return PerlStringsTargetsHandler.INSTANCE;
    }
    else if (isTargetableHeredocElement(run)) {
      return PerlHeredocTargetsHandler.INSTANCE;
    }
    return PerlGenericTargetsHandler.INSTANCE;
  }

  /**
   * @return true iff {@code element} is interpolatable complete heredoc body
   */
  @Contract("null->false")
  public static boolean isTargetableHeredocElement(@Nullable PsiElement element) {
    return element instanceof PerlHeredocElementImpl &&
           PsiUtilCore.getElementType(element) != HEREDOC &&
           ((PerlHeredocElementImpl)element).getHeredocOpener() != null;
  }

  private static @NotNull List<PerlIntroduceTarget> computeIntroduceTargetsFromSelection(@NotNull Editor editor, @NotNull PsiFile file) {
    SelectionModel selectionModel = editor.getSelectionModel();
    int selectionStart = selectionModel.getSelectionStart();
    int selectionEnd = selectionModel.getSelectionEnd();
    PsiElement startElement = file.findElementAt(selectionStart);
    PsiElement endElement = file.findElementAt(selectionEnd > selectionStart ? selectionEnd - 1 : selectionEnd);
    if (startElement == null || endElement == null) {
      return Collections.emptyList();
    }
    PsiElement wrappingExpression = PsiTreeUtil.findCommonParent(startElement, endElement);
    if (!(wrappingExpression instanceof PsiPerlExpr) && !(wrappingExpression instanceof PerlHeredocElementImpl)) {
      wrappingExpression = PsiTreeUtil.getParentOfType(wrappingExpression, PsiPerlExpr.class, PerlHeredocElementImpl.class);
    }
    while (wrappingExpression != null) {
      if (isTargetableElement(wrappingExpression)) {
        break;
      }
      wrappingExpression = PsiTreeUtil.getParentOfType(wrappingExpression, PsiPerlExpr.class, PerlHeredocElementImpl.class);
    }
    if (wrappingExpression == null) {
      return Collections.emptyList();
    }

    if (!PsiTreeUtil.processElements(wrappingExpression, it -> !(it instanceof PerlHeredocOpener))) {
      return Collections.emptyList();
    }

    TextRange selectionRange = TextRange.create(selectionStart, selectionEnd);
    return getHandler(wrappingExpression).computeTargetsFromSelection(wrappingExpression, selectionRange);
  }

  /**
   * @return a range of psi elements representing declaration of variable with {@code name} assigned with {@code target} or null if something went wrong
   */
  public static @Nullable Pair<PsiElement, PsiElement> createTargetDeclarationStatement(@NotNull Project project,
                                                                                        @NotNull PerlIntroduceTarget target,
                                                                                        @NotNull String variableName) {
    String targetExpressionText =
      getHandler(Objects.requireNonNull(target.getPlace())).createDeclarationStatementText(variableName, target);
    PsiElement file = PerlElementFactory.createFile(project, targetExpressionText);
    PsiElement[] children = file.getChildren();
    if (children.length == 0) {
      LOG.error("Unable to create a statement for " + targetExpressionText + "; target was " + target);
      return null;
    }
    PsiElement lastChild = children[children.length - 1];
    if (lastChild instanceof PerlHeredocElementImpl && lastChild.getNextSibling() != null) {
      // heredoc end and space are leaf elements, but they are important
      lastChild = lastChild.getNextSibling();
      if (lastChild.getNextSibling() != null) {
        lastChild = lastChild.getNextSibling();
      }
    }
    return Pair.create(children[0], lastChild);
  }

  /**
   * Replaces a targets represented by {@code occurrences} with {@code replacement}
   *
   * @return list of inserted elements
   * @apiNote all occurrences should be from the same element. This api implemented for multi-replacements
   */
  public static @NotNull List<PsiElement> replaceOccurences(@NotNull List<PerlIntroduceTarget> occurrences,
                                                            @NotNull PsiElement replacement) {
    if (occurrences.isEmpty()) {
      LOG.warn("Empty occurrences passed to replacement");
    }
    PsiElement targetPlace = Objects.requireNonNull(occurrences.getFirst()).getPlace();
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
