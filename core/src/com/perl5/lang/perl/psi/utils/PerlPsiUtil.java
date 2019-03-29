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

package com.perl5.lang.perl.psi.utils;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.stubs.PsiFileStub;
import com.intellij.psi.stubs.Stub;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.*;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.mixins.PerlStatementMixin;
import com.perl5.lang.perl.psi.properties.PerlLabelScope;
import com.perl5.lang.perl.psi.properties.PerlLoop;
import com.perl5.lang.perl.psi.properties.PerlStatementsContainer;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementStub;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.intellij.psi.TokenType.NEW_LINE_INDENT;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.perl5.lang.perl.PerlParserDefinition.MEANINGLESS_TOKENS;

/**
 * Created by hurricup on 09.08.2015.
 */
public class PerlPsiUtil implements PerlElementTypes {
  private static final TokenSet IGNORE_WHEN_COMPARING = TokenSet.create(
    WHITE_SPACE, NEW_LINE_INDENT,
    COMMENT_LINE, COMMENT_ANNOTATION,
    LEFT_BRACE_ARRAY, LEFT_BRACE_CODE, LEFT_BRACE_GLOB, LEFT_BRACE_HASH, LEFT_BRACE_SCALAR,
    RIGHT_BRACE_ARRAY, RIGHT_BRACE_CODE, RIGHT_BRACE_GLOB, RIGHT_BRACE_HASH, RIGHT_BRACE_SCALAR,
    SEMICOLON,
    HEREDOC, HEREDOC_END, HEREDOC_END_INDENTABLE, HEREDOC_QQ, HEREDOC_QX
  );

  private static final String OPEN_QUOTES = "<[{(";
  private static final String CLOSE_QUOTES = ">]})";
  private static final String ALTERNATIVE_QUOTES = "/|!~^._=?\\;";
  public static final char SINGLE_QUOTE_CHAR = '\'';
  public static final String SINGLE_QUOTE = "" + SINGLE_QUOTE_CHAR;
  public static final char DOUBLE_QUOTE_CHAR = '"';
  public static final String DOUBLE_QUOTE = "" + DOUBLE_QUOTE_CHAR;
  public static final char BACK_TICK_CHAR = '`';
  public static final String BACK_TICK = "" + BACK_TICK_CHAR;
  public static final String QUOTE_Q = "q";
  public static final String QUOTE_QQ = "qq";
  public static final String QUOTE_QX = "qx";
  public static final String QUOTE_QW = "qw";

  /**
   * @return list of all meaningful children of the {@code psiElement} inside the {@code rangeInElement} skipping spaces, comments and so on
   */
  @NotNull
  public static List<PsiElement> getMeaningfulChildrenWithLeafs(@Nullable PsiElement psiElement) {
    return psiElement == null ? Collections.emptyList() :
           getMeaningfulChildrenWithLeafs(psiElement, TextRange.from(0, psiElement.getTextLength()));
  }

  /**
   * @return list of all meaningful children of the {@code psiElement} inside the {@code rangeInElement} skipping spaces, comments and so on
   */
  @NotNull
  public static List<PsiElement> getMeaningfulChildrenWithLeafs(@Nullable PsiElement psiElement, @Nullable TextRange rangeInElement) {
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
      if (startWith instanceof PerlStringContentElement) {
        processor.process((PerlStringContentElement)startWith);
      }

      if (startWith.getFirstChild() != null) {
        processStringElements(startWith.getFirstChild(), processor);
      }

      startWith = startWith.getNextSibling();
    }
  }

  @NotNull
  public static List<String> collectStringContents(PsiElement startWith) {
    if (startWith == null) {
      return Collections.emptyList();
    }

    List<String> result = new ArrayList<>();
    collectStringContentsRecursively(startWith, result);
    return result;
  }

  private static void collectStringContentsRecursively(PsiElement run, List<String> result) {
    //noinspection ConstantConditions
    while (run != null) {
      if (run instanceof PerlString) {
        result.add(ElementManipulators.getValueText(run));
      }
      else if (run instanceof PerlStringList) {
        result.addAll(((PerlStringList)run).getStringContents());
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
  @NotNull
  public static ElementManipulator getManipulator(PsiElement element) {
    ElementManipulator manipulator = ElementManipulators.getManipulator(element);
    if (manipulator == null) {
      throw new IncorrectOperationException("Unable to find manipulator for " + element.getClass().getName());
    }
    return manipulator;
  }


  @NotNull
  public static PsiElement renameNamedElement(@NotNull PsiNameIdentifierOwner nameIdentifierOwner, @Nullable String newName) {
    renameElement(nameIdentifierOwner.getNameIdentifier(), newName);
    return nameIdentifierOwner;
  }

  /**
   * Renaming PsiElement using manipulator
   */
  @SuppressWarnings("UnusedReturnValue")
  @Nullable
  @Contract("null, _ -> null; !null, null -> !null")
  public static PsiElement renameElement(@Nullable PsiElement element, @Nullable String newName) {
    if (element == null) {
      return null;
    }
    if (StringUtil.isEmpty(newName)) {
      return element;
    }
    //noinspection unchecked
    return PerlPsiUtil.getManipulator(element).handleContentChange(element, newName);
  }


  @Nullable
  public static PsiElement getNextSignificantSibling(PsiElement element) {
    PsiElement result = element.getNextSibling();
    while (isCommentOrSpace(result)) {
      result = result.getNextSibling();
    }
    return result;
  }

  @Deprecated // this one is bad. What about comments and so on?
  @Nullable
  public static ASTNode getNextSignificantSibling(ASTNode node) {
    ASTNode result = node.getTreeNext();
    while (result != null && result.getElementType() == WHITE_SPACE) {
      result = result.getTreeNext();
    }
    return result;
  }

  @Nullable
  @Contract("null -> null")
  public static PsiElement getPrevSignificantSibling(@Nullable PsiElement element) {
    if (element == null) {
      return null;
    }
    PsiElement result = element.getPrevSibling();
    while (isCommentOrSpace(result)) {
      result = result.getPrevSibling();
    }
    return result;
  }

  @Nullable
  public static PsiElement getParentElementOrStub(PsiElement currentElement,
                                                  @Nullable final Class<? extends StubElement> stubClass,
                                                  @NotNull final Class<? extends PsiElement> psiClass
  ) {

    Stub stub = currentElement instanceof StubBasedPsiElement ? ((StubBasedPsiElement)currentElement).getStub() : null;
    if (stub != null && stubClass != null) {
      Stub parentStub = getParentStubOfType(stub, stubClass);
      return parentStub == null ? null : ((StubBase)parentStub).getPsi();
    }
    else {
      return PsiTreeUtil.getParentOfType(currentElement, psiClass);
    }
  }

  @Nullable
  public static Stub getParentStubOfType(Stub currentStubElement, Class<? extends Stub> stubClass) {
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

  public static List<PsiElement> collectUseStatements(@NotNull PerlNamespaceDefinitionElement rootElement) {
    return CachedValuesManager.getCachedValue(rootElement, () -> CachedValueProvider.Result
      .create(collectNamespaceMembers(rootElement, PerlUseStatement.class), rootElement));
  }

  @NotNull
  public static List<PsiElement> collectNamespaceMembers(@NotNull final PsiElement rootElement,
                                                         @NotNull final Class<? extends PsiElement> psiClass
  ) {
    final List<PsiElement> result = new ArrayList<>();

    Processor<PsiElement> processor = element -> {
      if (psiClass.isInstance(element)) {
        result.add(element);
      }
      return true;
    };

    if (rootElement instanceof StubBasedPsiElement) {
      Stub rootElementStub = ((StubBasedPsiElement)rootElement).getStub();

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
    @Nullable Processor<PsiElement> processor,
    @Nullable Class<? extends PsiElement> avoidPsiClass
  ) {
    if (stub == null || processor == null) {
      return false;
    }

    for (Stub childStub : stub.getChildrenStubs()) {
      ProgressManager.checkCanceled();
      PsiElement childPsi = ((StubElement)childStub).getPsi();
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

    if (stub instanceof PerlPolyNamedElementStub) {
      for (PsiElement child : ((PerlPolyNamedElementStub)stub).getPsi().getLightElements()) {
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

    if (element instanceof PerlPolyNamedElement) {
      for (PerlDelegatingLightNamedElement lightNamedElement : ((PerlPolyNamedElement)element).getLightElements()) {
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
    return element != null && MEANINGLESS_TOKENS.contains(PsiUtilCore.getElementType(element));
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
      if (prevElement instanceof PerlLabelDeclaration) {
        if (!processor.process((PerlLabelDeclaration)prevElement)) {
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
      if (run instanceof PerlLabelDeclaration) {
        if (!processor.process((PerlLabelDeclaration)run)) {
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
   * Recursively processes all elements starting element if they are in specified range
   *
   * @param element   startElement
   * @param processor processor
   * @param range     limitation range
   * @return false if need to stop
   */

  public static boolean processElementsInRange(PsiElement element,
                                               @NotNull TextRange range,
                                               @NotNull final PsiElementProcessor<PsiElement> processor) {
    if (element == null) {
      return true;
    }

    TextRange elementRange = element.getNode().getTextRange();
    if (range.contains(elementRange)) {
      if (!processor.execute(element)) {
        return false;
      }
    }
    if (range.intersects(elementRange)) {
      PsiElement run = element.getFirstChild();
      while (run != null) {
        if (!processElementsInRange(run, range, processor)) {
          return false;
        }
        run = run.getNextSibling();
      }
    }
    return true;
  }

  @NotNull
  public static List<PerlAnnotation> collectAnnotations(@Nullable PsiElement element) {
    if (element == null) {
      return Collections.emptyList();
    }

    final List<PerlAnnotation> result = new ArrayList<>();
    processElementAnnotations(element, perlAnnotation -> {
      result.add(perlAnnotation);
      return true;
    });
    return result;
  }

  /**
   * Gets annotation before element, eol or statement
   *
   * @param element         element to search for
   * @param annotationClass annotation class
   * @return annotation or null
   */
  @Nullable
  public static <T extends PerlAnnotation> T getAnyAnnotationByClass(@NotNull PsiElement element, final Class<T> annotationClass) {
    // before element
    T annotation = PerlPsiUtil.getAnnotationByClass(element, annotationClass);
    if (annotation != null) {
      return annotation;
    }

    // before statement
    PsiPerlStatement statement = PsiTreeUtil.getParentOfType(element, PsiPerlStatement.class);
    if (statement != null) {
      return PerlPsiUtil.getAnnotationByClass(statement, annotationClass);
    }
    return null;
  }

  @Nullable
  public static <T extends PerlAnnotation> T getAnnotationByClass(@NotNull PsiElement element, final Class<T> annotationClass) {
    final PerlAnnotation[] result = new PerlAnnotation[]{null};
    processElementAnnotations(element, perlAnnotation -> {
      if (annotationClass.isInstance(perlAnnotation)) {
        result[0] = perlAnnotation;
        return false;
      }
      return true;
    });
    //noinspection unchecked
    return (T)result[0];
  }

  /**
   * Checking the tree before the {@code element} and looking for annotions in code. Each annotation is processed by {@code annotationProcessor}
   */
  public static void processElementAnnotations(@NotNull PsiElement element, @NotNull Processor<PerlAnnotation> annotationProcessor) {
    if (element instanceof PsiFile) {
      return;
    }
    PsiElement run = element.getPrevSibling();

    boolean isListItem = element.getParent() instanceof PerlStringList;
    if (run == null || isListItem) {
      run = isListItem ? element.getParent() : element;
      int elementOffset = element.getNode().getStartOffset();
      while (true) {
        PsiElement parent = run.getParent();
        if (parent != null && !(parent instanceof PsiFile) && parent.getNode().getStartOffset() == elementOffset) {
          run = parent;
        }
        else {
          break;
        }
      }

      run = run.getPrevSibling();
    }

    while (run != null) {
      if (run instanceof PerlAnnotationContainer) {
        PerlAnnotation annotation = ((PerlAnnotationContainer)run).getAnnotation();
        if (annotation != null) {
          if (!annotationProcessor.process(annotation)) {
            return;
          }
        }
      }
      else if (!(run instanceof PsiComment || run instanceof PsiWhiteSpace)) {
        return;
      }
      run = run.getPrevSibling();
    }
  }

  @Nullable
  public static String getPerlExpressionNamespace(@Nullable PsiElement element) {
    if (element == null) {
      return null;
    }

    if (element instanceof PsiPerlPackageExpr) {
      return ((PerlNamespaceElement)element.getFirstChild()).getCanonicalName();
    }
    else if (element instanceof PerlString) {
      return ElementManipulators.getValueText(element);
    }
    else if (element instanceof PerlVariable) {
      return ((PerlVariable)element).guessVariableType();
    }
    else if (isSelfShortcut(element)) {
      return PerlPackageUtil.getContextPackageName(element);
    }
    else if (element instanceof PerlMethodContainer) {
      return PerlSubUtil.getMethodReturnValue((PerlMethodContainer)element);
    }
    else if (element instanceof PsiPerlParenthesisedExprImpl) {
      return getPerlExpressionNamespace(((PsiPerlParenthesisedExprImpl)element).getExpr());
    }
    else if (element instanceof PsiPerlDerefExprImpl) {
      return getPerlExpressionNamespace(element.getLastChild());
    }

    return null;
  }

  /**
   * Checks if specified element is shift expression or $_[0] in the single statement of sub definition
   *
   * @param element element in question
   * @return check result
   */
  public static boolean isSelfShortcut(PsiElement element) {
    if (element instanceof PsiPerlParenthesisedExpr) {
      return isSelfShortcut(((PsiPerlParenthesisedExpr)element).getExpr());
    }

    if (element == null ||
        !(element instanceof PsiPerlSubCallExpr && StringUtil.equals(element.getText(), "shift") ||
          element instanceof PsiPerlArrayElementImpl && StringUtil.equals(element.getText(), "$_[0]")
        )) {
      return false;
    }

    PsiPerlStatement statement = PsiTreeUtil.getParentOfType(element, PsiPerlStatementImpl.class);
    if (statement == null) {
      return false;
    }

    PsiElement statementContainer = statement.getParent();
    if (!(statementContainer instanceof PsiPerlBlockImpl && statementContainer.getParent() instanceof PerlSubDefinitionElement)) {
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

  @NotNull
  public static PsiElement getClosest(@NotNull PsiElement existingElement, @Nullable PsiElement possibleElement) {
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

    if (targetElement instanceof PerlString) {
      return areStringElementsSame((PerlString)targetElement, elementToCompare);
    }
    else if (targetElement instanceof PerlSimpleRegex) {
      return areMatchRegexElementsSame((PerlSimpleRegex)targetElement, elementToCompare);
    }
    else if (targetElement instanceof PerlCastExpression) {
      return areCastElementsSame((PerlCastExpression)targetElement, elementToCompare);
    }
    else if (targetElement instanceof PsiPerlPerlRegex) {
      return areRegexSame((PsiPerlPerlRegex)targetElement, elementToCompare);
    }
    else if (targetElement instanceof PerlMethodContainer) {
      return areNestedCallsSame((PerlMethodContainer)targetElement, elementToCompare);
    }
    else if (targetElement instanceof PsiPerlPackageExpr) {
      return elementToCompare instanceof PerlString ? areElementsSame(elementToCompare, targetElement) :
             elementToCompare instanceof PsiPerlPackageExpr &&
             StringUtil.equals(PerlPackageUtil.getCanonicalName(targetElement.getText()),
                               PerlPackageUtil.getCanonicalName(elementToCompare.getText()));
    }
    return areGenericElementsSame(targetElement, elementToCompare);
  }

  /**
   * @return true iff {@code element} is matching part of replace regex
   */
  public static boolean isMatchRegex(@NotNull PsiElement element) {
    if (!(element instanceof PsiPerlPerlRegex)) {
      return false;
    }

    PsiElement elementParent = element.getParent();
    if (PerlTokenSets.LAZY_PARSABLE_REGEXPS.contains(PsiUtilCore.getElementType(elementParent))) {
      return elementParent.getParent() instanceof PerlReplacementRegex;
    }
    return elementParent instanceof PerlReplacementRegex && element.equals(((PerlReplacementRegex)elementParent).getMatchRegex());
  }

  /**
   * @return true iff nested calls are semantically equal
   */
  private static boolean areNestedCallsSame(@NotNull PerlMethodContainer targetElement, @NotNull PsiElement elementToCompare) {
    if (!(elementToCompare instanceof PerlMethodContainer)) {
      return false;
    }
    if (!areElementsSame(targetElement.getMethod(), ((PerlMethodContainer)elementToCompare).getMethod())) {
      return false;
    }
    return areElementsSame(targetElement.getCallArgumentsList(), ((PerlMethodContainer)elementToCompare).getCallArgumentsList());
  }

  /**
   * @return true iff regexps are semantically equal
   */
  private static boolean areRegexSame(@NotNull PsiPerlPerlRegex targetElement, @NotNull PsiElement elementTocompare) {
    if (elementTocompare instanceof PerlSimpleRegex) {
      return areElementsSame(targetElement, ((PerlSimpleRegex)elementTocompare).getRegex());
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
    if (!(elementToCompare instanceof PerlSimpleRegex)) {
      return false;
    }
    if (!areElementsSame(targetElement.getRegex(), ((PerlSimpleRegex)elementToCompare).getRegex())) {
      return false;
    }
    PerlRegexpModifiers targetModifiers = PerlRegexpModifiers.create(targetElement.getPerlRegexModifiers());
    PerlRegexpModifiers elementModifiers = PerlRegexpModifiers.create(((PerlSimpleRegex)elementToCompare).getPerlRegexModifiers());
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
  @Nullable
  private static PsiPerlExpr getSingleBlockExpression(@Nullable PsiPerlBlock block) {
    if (block == null) {
      return null;
    }
    PsiElement[] children = block.getChildren();
    if (children.length != 1 || !(children[0] instanceof PerlStatementMixin) || ((PerlStatementMixin)children[0]).getModifier() != null) {
      return null;
    }
    return ((PerlStatementMixin)children[0]).getExpr();
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
    if (!(elementToCompare instanceof PerlString)) {
      return false;
    }

    // textual comparision for simple strings
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

    // traversal comparision
    PsiElement targetRun = targetString.getFirstContentToken();
    PsiElement runToCompare = ((PerlString)elementToCompare).getFirstContentToken();
    PsiElement targetCloseQuoteElement = targetString.getCloseQuoteElement();
    PsiElement elementToCompareCloseQuoteElement = ((PerlString)elementToCompare).getCloseQuoteElement();

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
    IElementType parentElementType = PsiUtilCore.getElementType(element.getParent());
    return parentElementType == LP_STRING_QW || parentElementType == STRING_LIST;
  }

  /**
   * @return a single quoted string with content. Attempts to select proper quotes
   */
  @NotNull
  public static String createSingleQuotedString(@NotNull String content) {
    char openQuoteChar = suggestOpenQuoteChar(content, SINGLE_QUOTE_CHAR);
    return openQuoteChar == 0 ? SINGLE_QUOTE + StringUtil.escapeChar(content, SINGLE_QUOTE_CHAR) + SINGLE_QUOTE :
           openQuoteChar == SINGLE_QUOTE_CHAR ? SINGLE_QUOTE + content + SINGLE_QUOTE :
           PerlPsiUtil.QUOTE_Q + openQuoteChar + content + getQuoteCloseChar(openQuoteChar);
  }

  static public abstract class HeredocProcessor implements Processor<PsiElement> {
    protected final int lineEndOffset;

    public HeredocProcessor(int lineEndOffset) {
      this.lineEndOffset = lineEndOffset;
    }
  }

  public static boolean processSubElements(@Nullable PsiElement rootElement, @NotNull PsiElementProcessor<PerlSubElement> processor) {
    return processElementsFromPsi(rootElement, it -> !(it instanceof PerlSubElement) || processor.execute((PerlSubElement)it), null);
  }

  /**
   * Suggests a quote to use for a {@code text} with a set of {@code defaultQuote} or non-default non-literal quotes
   *
   * @return a quote symbol or 0 if no quote been found
   */
  public static char suggestOpenQuoteChar(@NotNull CharSequence text, char defaultQuote) {
    if (StringUtil.indexOf(text, defaultQuote) < 0) {
      return defaultQuote;
    }
    for (int i = 0; i < OPEN_QUOTES.length(); i++) {
      if (StringUtil.indexOf(text, OPEN_QUOTES.charAt(i)) < 0 && StringUtil.indexOf(text, CLOSE_QUOTES.charAt(i)) < 0) {
        return OPEN_QUOTES.charAt(i);
      }
    }
    for (int i = 0; i < ALTERNATIVE_QUOTES.length(); i++) {
      if (StringUtil.indexOf(text, ALTERNATIVE_QUOTES.charAt(i)) < 0) {
        return ALTERNATIVE_QUOTES.charAt(i);
      }
    }
    return 0;
  }


  @NotNull
  public static String toSnakeCase(@NotNull String source) {
    return StringUtil.join(source.toLowerCase(Locale.getDefault()).split("\\s+"), "_");
  }

  /**
   * Choosing closing character by opening one
   *
   * @param charOpener - char with which sequence started
   * @return - ending char
   */
  public static char getQuoteCloseChar(char charOpener) {
    int index = OPEN_QUOTES.indexOf(charOpener);
    return index < 0 ? charOpener : CLOSE_QUOTES.charAt(index);
  }
}
