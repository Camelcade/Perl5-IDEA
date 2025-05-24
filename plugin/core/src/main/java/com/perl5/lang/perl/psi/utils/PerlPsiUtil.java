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

package com.perl5.lang.perl.psi.utils;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.LazyParseableElement;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.stubs.PsiFileStub;
import com.intellij.psi.stubs.Stub;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlTokenSetsEx;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.*;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.mixins.PerlStatementMixin;
import com.perl5.lang.perl.psi.properties.PerlLabelScope;
import com.perl5.lang.perl.psi.properties.PerlLoop;
import com.perl5.lang.perl.psi.properties.PerlStatementsContainer;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementStub;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.intellij.psi.TokenType.NEW_LINE_INDENT;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.perl5.lang.perl.lexer.PerlTokenSets.*;

public final class PerlPsiUtil implements PerlElementTypes {
  private PerlPsiUtil() {
  }

  private static final TokenSet IGNORE_WHEN_COMPARING = TokenSet.orSet(
    HEREDOC_BODIES_TOKENSET,
    HEREDOC_ENDS,
    VARIABLE_OPEN_BRACES,
    VARIABLE_CLOSE_BRACES,
    TokenSet.create(WHITE_SPACE, NEW_LINE_INDENT, COMMENT_LINE, COMMENT_ANNOTATION, SEMICOLON
    ));

  public static final char SINGLE_QUOTE_CHAR = '\'';
  public static final String SINGLE_QUOTE = "" + SINGLE_QUOTE_CHAR;
  public static final char DOUBLE_QUOTE_CHAR = '"';
  public static final String DOUBLE_QUOTE = "" + DOUBLE_QUOTE_CHAR;
  public static final char BACK_TICK_CHAR = '`';
  public static final String BACK_TICK = "" + BACK_TICK_CHAR;
  public static final String QUOTE_Q = "q";
  public static final String QUOTE_QQ = "qq";
  public static final String QUOTE_QX = "qx";

  /**
   * @return list of all meaningful children of the {@code psiElement} inside the {@code rangeInElement} skipping spaces, comments and so on
   */
  public static @NotNull List<PsiElement> getMeaningfulChildrenWithLeafs(@Nullable PsiElement psiElement) {
    return psiElement == null ? Collections.emptyList() :
           getMeaningfulChildrenWithLeafs(psiElement, TextRange.from(0, psiElement.getTextLength()));
  }

  /**
   * @return list of all meaningful children of the {@code psiElement} inside the {@code rangeInElement} skipping spaces, comments and so on
   */
  public static @NotNull List<PsiElement> getMeaningfulChildrenWithLeafs(@Nullable PsiElement psiElement,
                                                                         @Nullable TextRange rangeInElement) {
    if (psiElement == null || rangeInElement == null || rangeInElement.isEmpty()) {
      return Collections.emptyList();
    }
    List<PsiElement> result = new ArrayList<>();
    PsiElement run = psiElement.getFirstChild();
    while (run != null) {
      if (rangeInElement.contains(run.getTextRangeInParent())) {
        if (!IGNORE_WHEN_COMPARING.contains(PsiUtilCore.getElementType(run))) {
          result.add(run);
        }
      }
      else if (!result.isEmpty()) {
        return result;
      }
      run = run.getNextSibling();
    }

    return result;
  }

  /**
   * Recursively searches for string content elements beginning from specified PsiElement
   *
   * @param startWith PsiElement to start from (inclusive)
   * @return list of PerlStringContentElement
   */
  public static Collection<PerlStringContentElement> collectStringElements(@NotNull PsiElement startWith) {
    final List<PerlStringContentElement> result = new ArrayList<>();
    processStringElements(startWith, perlStringContentElement -> {
      result.add(perlStringContentElement);
      return true;
    });
    return result;
  }

  /**
   * Recursive searcher for string content elements
   *
   * @param startWith element to start with (inclusive)
   */
  public static void processStringElements(PsiElement startWith, Processor<PerlStringContentElement> processor) {
    while (startWith != null) {
      if (startWith instanceof PerlStringContentElement stringContentElement) {
        processor.process(stringContentElement);
      }

      if (startWith.getFirstChild() != null) {
        processStringElements(startWith.getFirstChild(), processor);
      }

      startWith = startWith.getNextSibling();
    }
  }

  public static @NotNull List<String> collectStringContents(PsiElement startWith) {
    if (startWith == null) {
      return Collections.emptyList();
    }

    List<String> result = new ArrayList<>();
    collectStringContentsRecursively(startWith, result);
    return result;
  }

  private static void collectStringContentsRecursively(PsiElement run, List<String> result) {
    while (run != null) {
      if (run instanceof PerlString) {
        result.add(ElementManipulators.getValueText(run));
      }
      else if (run instanceof PerlStringList stringList) {
        result.addAll(stringList.getStringContents());
      }
      else {
        if (run instanceof ASTWrapperPsiElement) {
          collectStringContentsRecursively(run.getFirstChild(), result);
        }
      }
      run = run.getNextSibling();
    }
  }

  /**
   * Searching for manipulator by element
   *
   * @param element PsiElement to search manipulator for
   * @return manipulator
   */
  public static @NotNull ElementManipulator<? super PsiElement> getManipulator(PsiElement element) {
    ElementManipulator<? super PsiElement> manipulator = ElementManipulators.getManipulator(element);
    if (manipulator == null) {
      throw new IncorrectOperationException("Unable to find manipulator for " + element.getClass().getName());
    }
    return manipulator;
  }


  public static @NotNull PsiElement renameNamedElement(@NotNull PsiNameIdentifierOwner nameIdentifierOwner, @Nullable String newName) {
    renameElement(nameIdentifierOwner.getNameIdentifier(), newName);
    return nameIdentifierOwner;
  }

  /**
   * Renaming PsiElement using manipulator
   */
  @SuppressWarnings("UnusedReturnValue")
  @Contract("null, _ -> null; !null, null -> !null")
  public static @Nullable PsiElement renameElement(@Nullable PsiElement element, @Nullable String newName) {
    if (element == null || !element.isValid()) {
      return null;
    }
    if (StringUtil.isEmpty(newName)) {
      return element;
    }
    return PerlPsiUtil.getManipulator(element).handleContentChange(element, newName);
  }


  public static @Nullable PsiElement getNextSignificantSibling(PsiElement element) {
    PsiElement result = element.getNextSibling();
    while (isCommentOrSpace(result)) {
      result = result.getNextSibling();
    }
    return result;
  }

  // this one is bad. What about comments and so on?
  @Deprecated
  public static @Nullable ASTNode getNextSignificantSibling(ASTNode node) {
    ASTNode result = node.getTreeNext();
    while (result != null && result.getElementType() == WHITE_SPACE) {
      result = result.getTreeNext();
    }
    return result;
  }

  @Contract("null -> null")
  public static @Nullable PsiElement getPrevSignificantSibling(@Nullable PsiElement element) {
    if (element == null) {
      return null;
    }
    PsiElement result = element.getPrevSibling();
    while (isCommentOrSpace(result)) {
      result = result.getPrevSibling();
    }
    return result;
  }

  public static @Nullable PsiElement getParentElementOrStub(PsiElement currentElement,
                                                            final @Nullable Class<? extends StubElement<?>> stubClass,
                                                            final @NotNull Class<? extends PsiElement> psiClass
  ) {

    Stub stub = currentElement instanceof StubBasedPsiElement<?> stubBasedElement ? stubBasedElement.getStub() : null;
    if (stub != null && stubClass != null) {
      Stub parentStub = getParentStubOfType(stub, stubClass);
      return parentStub == null ? null : ((StubBase<?>)parentStub).getPsi();
    }
    else {
      return PsiTreeUtil.getParentOfType(currentElement, psiClass);
    }
  }

  public static @Nullable Stub getParentStubOfType(Stub currentStubElement, Class<? extends Stub> stubClass) {
    while (true) {
      if (currentStubElement == null) {
        return null;
      }

      if (stubClass.isInstance(currentStubElement)) {
        return currentStubElement;
      }

      if (currentStubElement instanceof PsiFileStub) {
        return null;
      }

      currentStubElement = currentStubElement.getParentStub();
    }
  }

  public static @NotNull List<PsiElement> collectNamespaceMembers(final @NotNull PsiElement rootElement,
                                                                  final @NotNull Class<? extends PsiElement> psiClass
  ) {
    final List<PsiElement> result = new ArrayList<>();

    Processor<PsiElement> processor = element -> {
      if (psiClass.isInstance(element)) {
        result.add(element);
      }
      return true;
    };

    if (rootElement instanceof StubBasedPsiElement<?> stubBasedPsiElement) {
      Stub rootElementStub = stubBasedPsiElement.getStub();

      if (rootElementStub != null) {
        processElementsFromStubs(
          rootElementStub,
          processor,
          PerlNamespaceDefinitionWithIdentifier.class
        );
        return result;
      }
    }

    processElementsFromPsi(
      rootElement,
      processor,
      PerlNamespaceDefinitionWithIdentifier.class
    );
    return result;
  }

  public static boolean processElementsFromStubs(
    @Nullable Stub stub,
    @Nullable Processor<? super PsiElement> processor,
    @Nullable Class<? extends PsiElement> avoidPsiClass
  ) {
    if (stub == null || processor == null) {
      return false;
    }

    for (Stub childStub : stub.getChildrenStubs()) {
      ProgressManager.checkCanceled();
      PsiElement childPsi = ((StubElement<?>)childStub).getPsi();
      if (!processor.process(childPsi)) {
        return false;
      }

      if (avoidPsiClass == null || !avoidPsiClass.isInstance(childPsi)) // don't enter sublcasses
      {
        if (!processElementsFromStubs(childStub, processor, avoidPsiClass)) {
          return false;
        }
      }
    }

    if (stub instanceof PerlPolyNamedElementStub<?> polyNamedElementStub) {
      for (PsiElement child : polyNamedElementStub.getPsi().getLightElements()) {
        ProgressManager.checkCanceled();
        if (!processor.process(child)) {
          return false;
        }
      }
    }

    return true;
  }


  public static boolean processElementsFromPsi(
    @Nullable PsiElement element,
    @Nullable Processor<? super PsiElement> processor,
    @Nullable Class<? extends PsiElement> avoidClass
  ) {
    if (element == null || processor == null) {
      return false;
    }

    for (PsiElement child : element.getChildren()) {
      ProgressManager.checkCanceled();
      if (!processor.process(child)) {
        return false;
      }

      if (avoidClass == null || !avoidClass.isInstance(child)) { // don't enter subclasses
        if (!processElementsFromPsi(child, processor, avoidClass)) {
          return false;
        }
      }
    }

    if (element instanceof PerlPolyNamedElement<?> perlPolyNamedElement) {
      for (PerlDelegatingLightNamedElement<?> lightNamedElement : perlPolyNamedElement.getLightElements()) {
        ProgressManager.checkCanceled();
        if (!processor.process(lightNamedElement)) {
          return false;
        }
      }
    }

    return true;
  }

  public static boolean iteratePsiElementsRight(PsiElement element, Processor<PsiElement> processor) {
    if (element == null || element instanceof PsiFile) {
      return false;
    }

    if (!processor.process(element)) {
      return false;
    }

    PsiElement run = element.getNextSibling();

    if (run == null) {
      return iteratePsiElementsRight(element.getParent(), processor);
    }

    return iteratePsiElementsRightDown(run, processor) && iteratePsiElementsRight(element.getParent(), processor);
  }

  public static boolean iteratePsiElementsRightDown(@NotNull PsiElement element, @NotNull Processor<PsiElement> processor) {
    boolean result = processor.process(element);
    if (result) {
      // checking children
      PsiElement run = element.getFirstChild();
      if (run != null) {
        result = iteratePsiElementsRightDown(run, processor);
      }

      // checking next sibling
      if (result) {
        run = element.getNextSibling();
        if (run != null) {
          result = iteratePsiElementsRightDown(run, processor);
        }
      }
    }
    return result;
  }

  public static boolean processNamespaceStatements(@NotNull PsiElement rootElement, Processor<PsiElement> processor) {
    PsiElement run = rootElement.getFirstChild();
    boolean result = true;
    while (run != null && result) {
      if (!(run instanceof PerlNamespaceDefinitionWithIdentifier)) {
        if (run instanceof PerlCompositeElement) {
          result = processor.process(run);
        }
        if (result && run instanceof PerlStatementsContainer) {
          result = processNamespaceStatements(run, processor);
        }
      }
      run = run.getNextSibling();
    }
    return result;
  }


  public static boolean isCommentOrSpace(PsiElement element) {
    return element != null && PerlTokenSetsEx.getMEANINGLESS_TOKENS().contains(PsiUtilCore.getElementType(element));
  }

  /**
   * Traversing tree according to next/last/redo labels resolution and processes all labels declarations
   *
   * @param element   element to start from
   * @param processor processor to process elements
   */
  public static void processNextRedoLastLabelDeclarations(PsiElement element, Processor<PerlLabelDeclaration> processor) {
    if (element == null || element instanceof PerlLabelScope) {
      return;
    }

    ProgressManager.checkCanceled();
    if (element instanceof PerlLoop) {
      PsiElement prevElement = getPrevSignificantSibling(element);
      if (prevElement instanceof PerlLabelDeclaration labelDeclaration) {
        if (!processor.process(labelDeclaration)) {
          return;
        }
      }
    }
    processNextRedoLastLabelDeclarations(element.getParent(), processor);
  }

  /**
   * Traversing tree according to goto labels resolution and processes all labels declarations
   *
   * @param element   element to start from
   * @param processor processor to process elements
   */
  public static void processGotoLabelDeclarations(PsiElement element, Processor<PerlLabelDeclaration> processor) {
    if (element == null) {
      return;
    }

    PsiElement run = element.getFirstChild();
    while (run != null) {
      ProgressManager.checkCanceled();
      if (run instanceof PerlLabelDeclaration perlLabelDeclaration) {
        if (!processor.process(perlLabelDeclaration)) {
          return;
        }
      }
      run = run.getNextSibling();
    }

    // next iteration
    if (!(element instanceof PsiFile)) {
      processGotoLabelDeclarations(element.getParent(), processor);
    }
  }

  /**
   * Checks if specified element is shift expression or $_[0] in the single statement of sub definition
   *
   * @param element element in question
   * @return check result
   */
  public static boolean isSelfShortcut(PsiElement element) {
    if (element instanceof PsiPerlParenthesisedExpr parenthesisedExpr) {
      return isSelfShortcut(parenthesisedExpr.getExpr());
    }

    if (element == null ||
        !(element instanceof PsiPerlArrayShiftExpr ||
          element instanceof PsiPerlArrayElementImpl && StringUtil.equals(element.getText(), "$_[0]")
        )) {
      return false;
    }

    PsiPerlStatement statement = PsiTreeUtil.getParentOfType(element, PsiPerlStatementImpl.class);
    if (statement == null) {
      return false;
    }

    PsiElement statementContainer = statement.getParent();
    if (!(statementContainer instanceof PsiPerlBlockImpl perlBlock &&
          perlBlock.getContainer() instanceof PerlSubDefinitionElement)) {
      return false;
    }

    PsiPerlStatement[] statements = PsiTreeUtil.getChildrenOfType(statementContainer, PsiPerlStatement.class);
    return statements != null && statements.length == 1;
  }

  /**
   * Checks if statement is shift/$_[0] deref shortcut
   *
   * @param statement statement
   * @return check result
   */
  public static boolean isSelfShortcutStatement(PsiPerlStatement statement) {
    if (statement == null) {
      return false;
    }

    PsiElement derefExpr = statement.getFirstChild();
    if (derefExpr == null) {
      return false;
    }

    if (derefExpr instanceof PsiPerlReturnExprImpl) {
      derefExpr = derefExpr.getLastChild();
    }

    if (!(derefExpr instanceof PsiPerlDerefExpr)) {
      return false;
    }

    return isSelfShortcut(derefExpr.getFirstChild());
  }

  public static @NotNull PsiElement getClosest(@NotNull PsiElement existingElement, @Nullable PsiElement possibleElement) {
    if (possibleElement == null) {
      return existingElement;
    }
    return PsiTreeUtil.isAncestor(existingElement, possibleElement, true) ? possibleElement : existingElement;
  }

  /**
   * True iff all elements from provided lists are semantically equals
   */
  public static boolean areElementsSame(@NotNull List<PsiElement> targetElements, @NotNull List<PsiElement> elementsToCompare) {
    if (targetElements.size() != elementsToCompare.size()) {
      return false;
    }
    for (int i = 0; i < targetElements.size(); i++) {
      if (!areElementsSame(targetElements.get(i), elementsToCompare.get(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * @return true iff {@code elementToCompare} is equal to {@code targetElement} or both nulls
   */
  public static boolean areElementsSame(@Nullable PsiElement targetElement, @Nullable PsiElement elementToCompare) {
    if (targetElement == null && elementToCompare == null) {
      return true;
    }
    else if (targetElement == null || elementToCompare == null) {
      return false;
    }
    else if (targetElement.equals(elementToCompare)) {
      return true;
    }

    return switch (targetElement) {
      case PerlString string -> areStringElementsSame(string, elementToCompare);
      case PerlSimpleRegex regex -> areMatchRegexElementsSame(regex, elementToCompare);
      case PerlCastExpression expression -> areCastElementsSame(expression, elementToCompare);
      case PsiPerlPerlRegex regex -> areRegexSame(regex, elementToCompare);
      case PerlMethodContainer container -> areNestedCallsSame(container, elementToCompare);
      case PsiPerlPackageExpr expr -> elementToCompare instanceof PerlString ? areElementsSame(elementToCompare, expr) :
        elementToCompare instanceof PsiPerlPackageExpr &&
        StringUtil.equals(PerlPackageUtil.getCanonicalName(expr.getText()),
                          PerlPackageUtil.getCanonicalName(elementToCompare.getText()));
      default -> areGenericElementsSame(targetElement, elementToCompare);
    };
  }

  /**
   * @return true iff {@code element} is matching part of replace regex
   */
  public static boolean isMatchRegex(@NotNull PsiElement element) {
    if (!(element instanceof PsiPerlPerlRegex)) {
      return false;
    }

    PsiElement elementParent = element.getParent();
    return elementParent instanceof PerlReplacementRegex regex && element.equals(regex.getMatchRegex());
  }

  /**
   * @return true iff nested calls are semantically equal
   */
  private static boolean areNestedCallsSame(@NotNull PerlMethodContainer targetElement, @NotNull PsiElement elementToCompare) {
    if (!(elementToCompare instanceof PerlMethodContainer methodContainer)) {
      return false;
    }
    if (!areElementsSame(targetElement.getMethod(), methodContainer.getMethod())) {
      return false;
    }
    return areElementsSame(targetElement.getCallArgumentsList(), methodContainer.getCallArgumentsList());
  }

  /**
   * @return true iff regexps are semantically equal
   */
  private static boolean areRegexSame(@NotNull PsiPerlPerlRegex targetElement, @NotNull PsiElement elementTocompare) {
    if (elementTocompare instanceof PerlSimpleRegex simpleRegex) {
      return areElementsSame(targetElement, simpleRegex.getRegex());
    }
    if (!(elementTocompare instanceof PsiPerlPerlRegex)) {
      return false;
    }
    PsiElement targetElementRun = targetElement.getFirstChild();
    PsiElement runToCompare = elementTocompare.getFirstChild();
    while (true) {
      StringBuilder targetBuffer = new StringBuilder();
      StringBuilder bufferToCompare = new StringBuilder();
      //noinspection Duplicates
      while (targetElementRun != null) {
        IElementType targetElementType = PsiUtilCore.getElementType(targetElementRun);
        if (targetElementType == REGEX_TOKEN) {
          targetBuffer.append(targetElementRun.getText());
        }
        else if (!IGNORE_WHEN_COMPARING.contains(targetElementType)) {
          break;
        }
        targetElementRun = targetElementRun.getNextSibling();
      }

      //noinspection Duplicates
      while (runToCompare != null) {
        IElementType elementToCompareType = PsiUtilCore.getElementType(runToCompare);
        if (elementToCompareType == REGEX_TOKEN) {
          bufferToCompare.append(runToCompare.getText());
        }
        else if (!IGNORE_WHEN_COMPARING.contains(elementToCompareType)) {
          break;
        }
        runToCompare = runToCompare.getNextSibling();
      }

      if (targetElementRun == null && runToCompare == null) {
        return StringUtil.equals(targetBuffer, bufferToCompare);
      }
      if (targetElementRun == null || runToCompare == null ||
          !StringUtil.equals(targetBuffer, bufferToCompare) ||
          !areElementsSame(targetElementRun, runToCompare)) {
        return false;
      }
      targetElementRun = targetElementRun.getNextSibling();
      runToCompare = runToCompare.getNextSibling();
    }
  }

  /**
   * @return true iff {@code targetElement} and {@code elementToCompare} are semantically equal simple regexp elements
   */
  private static boolean areMatchRegexElementsSame(@NotNull PerlSimpleRegex targetElement, @NotNull PsiElement elementToCompare) {
    if (isMatchRegex(elementToCompare)) {
      return areElementsSame(targetElement.getRegex(), elementToCompare);
    }
    if (!(elementToCompare instanceof PerlSimpleRegex simpleRegex)) {
      return false;
    }
    if (!areElementsSame(targetElement.getRegex(), simpleRegex.getRegex())) {
      return false;
    }
    PerlRegexpModifiers targetModifiers = PerlRegexpModifiers.create(targetElement.getPerlRegexModifiers());
    PerlRegexpModifiers elementModifiers = PerlRegexpModifiers.create(simpleRegex.getPerlRegexModifiers());
    return targetModifiers == null && elementModifiers == null ||
           targetModifiers != null && targetModifiers.areRegexpComparable(elementModifiers);
  }

  /**
   * @return true iff cast (deref) {@code targetElement} semantically equals to {@code elementToCompare}. Taking into account blocked and non-blocked versions
   */
  private static boolean areCastElementsSame(@NotNull PerlCastExpression targetElement, @NotNull PsiElement elementToCompare) {
    if (!targetElement.getClass().equals(elementToCompare.getClass())) {
      return false;
    }
    PsiPerlExpr targetExpr = targetElement.getExpr();
    PsiPerlExpr exprToCompare = ((PerlCastExpression)elementToCompare).getExpr();
    if (targetExpr != null && exprToCompare != null) {
      return areElementsSame(targetExpr, exprToCompare);
    }

    PsiPerlBlock targetBlock = targetElement.getBlock();
    PsiPerlBlock blockToCompare = ((PerlCastExpression)elementToCompare).getBlock();
    if (targetBlock != null && blockToCompare != null) {
      return areElementsSame(targetBlock, blockToCompare);
    }
    return targetBlock == null ?
           areElementsSame(targetExpr, getSingleBlockExpression(blockToCompare)) :
           areElementsSame(getSingleBlockExpression(targetBlock), exprToCompare);
  }

  /**
   * @return if {@code block} contains only single statement with single expression, returns it. False otherwise.
   */
  public static @Nullable PsiPerlExpr getSingleBlockExpression(@Nullable PsiPerlBlock block) {
    if (block == null) {
      return null;
    }
    PsiElement[] children = block.getChildren();
    if (children.length != 1 || !(children[0] instanceof PerlStatementMixin statementMixin) || statementMixin.getModifier() != null) {
      return null;
    }
    return statementMixin.getExpr();
  }

  /**
   * @return true iff {@code targetString} is non-qx string and represents the same string as {@code elementToCompare}
   */
  private static boolean areStringElementsSame(@NotNull PerlString targetString, @NotNull PsiElement elementToCompare) {
    boolean isTargetExecutable = targetString instanceof PsiPerlStringXq;
    if (!isTargetExecutable && elementToCompare instanceof PsiPerlPackageExpr && targetString.getChildren().length == 0) {
      return StringUtil.equals(PerlPackageUtil.getCanonicalName(ElementManipulators.getValueText(targetString)),
                               PerlPackageUtil.getCanonicalName(elementToCompare.getText()));
    }
    if ((isTargetExecutable || elementToCompare instanceof PsiPerlStringXq)
        && !targetString.getClass().equals(elementToCompare.getClass())) {
      return false;
    }
    if (!(elementToCompare instanceof PerlString perlString)) {
      return false;
    }

    // textual comparison for simple strings
    boolean isTargetSimple = targetString.getChildren().length == 0;
    boolean isElementSimple = elementToCompare.getChildren().length == 0;
    if (isTargetSimple && isElementSimple) {
      String targetText = ElementManipulators.getValueText(targetString);
      String textToCompare = ElementManipulators.getValueText(elementToCompare);
      if (StringUtil.equals(targetText, textToCompare)) {
        return true;
      }
      else if (isTargetExecutable) {
        return false;
      }
      else if (targetString.getPrevSibling() == null && targetString.getParent() instanceof PerlDerefExpression ||
               elementToCompare.getPrevSibling() == null && elementToCompare.getParent() instanceof PerlDerefExpression) {
        return StringUtil.equals(PerlPackageUtil.getCanonicalName(targetText), PerlPackageUtil.getCanonicalName(textToCompare));
      }
      return false;
    }
    else if (isTargetSimple != isElementSimple) {
      return false;
    }

    // traversal comparison
    PsiElement targetRun = targetString.getFirstContentToken();
    PsiElement runToCompare = perlString.getFirstContentToken();
    PsiElement targetCloseQuoteElement = targetString.getCloseQuoteElement();
    PsiElement elementToCompareCloseQuoteElement = perlString.getCloseQuoteElement();

    while (targetRun != null && runToCompare != null) {
      if (targetRun.equals(targetCloseQuoteElement) && runToCompare.equals(elementToCompareCloseQuoteElement)) {
        return true;
      }

      if (!areElementsSame(targetRun, runToCompare)) {
        return false;
      }
      targetRun = targetRun.getNextSibling();
      runToCompare = runToCompare.getNextSibling();
    }

    return (targetRun == null || targetRun.equals(targetCloseQuoteElement)) &&
           (runToCompare == null || runToCompare.equals(elementToCompareCloseQuoteElement));
  }

  /**
   * @return true iff {@code targetElement} equals {@code elementToCompare} in generic way (same tree)
   */
  private static boolean areGenericElementsSame(@NotNull PsiElement targetElement, @NotNull PsiElement elementToCompare) {
    if (!targetElement.getClass().equals(elementToCompare.getClass())) {
      return false;
    }
    PsiElement targetElementRun = targetElement.getFirstChild();
    PsiElement elementToCompareRun = elementToCompare.getFirstChild();
    if (targetElementRun == null) {
      return elementToCompareRun == null &&
             StringUtil.equals(targetElement.getNode().getChars(), elementToCompare.getNode().getChars());
    }
    while (true) {
      while (IGNORE_WHEN_COMPARING.contains(PsiUtilCore.getElementType(targetElementRun))) {
        targetElementRun = targetElementRun.getNextSibling();
      }
      while (IGNORE_WHEN_COMPARING.contains(PsiUtilCore.getElementType(elementToCompareRun))) {
        elementToCompareRun = elementToCompareRun.getNextSibling();
      }
      if (targetElementRun == null || elementToCompareRun == null) {
        break;
      }

      if (!areElementsSame(targetElementRun, elementToCompareRun)) {
        return false;
      }

      targetElementRun = targetElementRun.getNextSibling();
      elementToCompareRun = elementToCompareRun.getNextSibling();
    }
    return targetElementRun == null && elementToCompareRun == null;
  }

  /**
   * @return true iff element is inside the qw list
   */
  public static boolean isInStringList(@NotNull PsiElement element) {
    return PsiUtilCore.getElementType(element.getParent()) == STRING_LIST;
  }

  /**
   * @return a single quoted string with content. Attempts to select proper quotes
   */
  public static @NotNull String createSingleQuotedString(@NotNull String content) {
    char openQuoteChar = PerlString.suggestOpenQuoteChar(content, SINGLE_QUOTE_CHAR);
    return openQuoteChar == 0 ? SINGLE_QUOTE + StringUtil.escapeChar(content, SINGLE_QUOTE_CHAR) + SINGLE_QUOTE :
           openQuoteChar == SINGLE_QUOTE_CHAR ? SINGLE_QUOTE + content + SINGLE_QUOTE :
           PerlPsiUtil.QUOTE_Q + openQuoteChar + content + PerlString.getQuoteCloseChar(openQuoteChar);
  }

  /**
   * Tries to get stub from passed {@code element} if available
   */
  @Contract("null->null")
  public static @Nullable StubElement<?> getStubFromElement(@Nullable PsiElement element) {
    StubElement<?> stubElement = null;
    if (element instanceof PsiFileImpl psiFile) {
      stubElement = psiFile.getStub();
    }
    else if (element instanceof StubBasedPsiElement<?> stubBasedPsiElement) {
      stubElement = stubBasedPsiElement.getStub();
    }
    return stubElement;
  }

  public static @NotNull String dumpHierarchy(@Nullable PsiElement element) {
    if (element == null) {
      return "null";
    }
    StringBuilder sb = new StringBuilder();
    while (true) {
      sb.append(element.getClass()).append("(").append(PsiUtilCore.getElementType(element)).append(")").append(": ");
      if (element instanceof PsiFile || element.getParent() == null) {
        break;
      }
      element = element.getParent();
    }
    return sb.toString();
  }

  public abstract static class HeredocProcessor implements Processor<PsiElement> {
    protected final int lineEndOffset;

    public HeredocProcessor(int lineEndOffset) {
      this.lineEndOffset = lineEndOffset;
    }
  }

  @SuppressWarnings("UnusedReturnValue")
  public static boolean processSubElements(@Nullable PsiElement rootElement, @NotNull PsiElementProcessor<PerlSubElement> processor) {
    StubElement<?> stubElement = getStubFromElement(rootElement);
    if (stubElement != null) {
      return processElementsFromStubs(stubElement, it -> !(it instanceof PerlSubElement subElement) || processor.execute(subElement), null);
    }
    return processElementsFromPsi(rootElement, it -> !(it instanceof PerlSubElement element) || processor.execute(element), null);
  }

  /**
   * @return removes meaningless elements from the {@code source} list
   */
  public static @NotNull <T extends PsiElement> List<T> cleanupChildren(@NotNull T[] source) {
    return ContainerUtil.filter(source, it -> !PerlTokenSetsEx.getWHITE_SPACE_AND_COMMENTS().contains(PsiUtilCore.getElementType(it)));
  }

  /**
   * @return composite children of the given {@code psiElement}. If child happens to be lazy parsable, it's children collected instead of it.
   */
  public static @NotNull List<? extends PsiElement> getCompositeChildrenUnwrappingLazy(@Nullable PsiElement psiElement) {
    if (psiElement == null) {
      return Collections.emptyList();
    }
    @NotNull PsiElement[] rawChildren = psiElement.getChildren();
    if (rawChildren.length == 0) {
      return Collections.emptyList();
    }

    List<PsiElement> result = new ArrayList<>();
    for (PsiElement child : rawChildren) {
      ASTNode childNode = child.getNode();
      if (childNode instanceof LazyParseableElement) {
        result.addAll(getCompositeChildrenUnwrappingLazy(child));
      }
      else if (childNode instanceof CompositeElement) {
        result.add(child);
      }
    }

    return result;
  }

  /**
   * Looks for element of {@code clazz} in range from {@code startOffset} to the {@code endOffset}. Element should not start or end inside
   * bounds, but should be somewhere in this range.
   */
  @Contract(pure = true)
  public static @Nullable <T extends PsiElement> T findElementOfClassAtRange(@NotNull PsiFile file,
                                                                             int startOffset,
                                                                             int endOffset,
                                                                             @NotNull Class<T> clazz) {
    final FileViewProvider viewProvider = file.getViewProvider();
    for (Language lang : viewProvider.getLanguages()) {
      PsiElement run = viewProvider.findElementAt(startOffset, lang);
      while (run != null && run.getTextRange().getStartOffset() < endOffset) {
        var result = PsiTreeUtil.getParentOfType(run, clazz, false);
        if (result != null) {
          return result;
        }
        run = PsiTreeUtil.nextLeaf(run);
      }
    }
    return null;
  }
}
