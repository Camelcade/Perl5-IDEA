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
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.stubs.PsiFileStub;
import com.intellij.psi.stubs.Stub;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.*;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.properties.PerlLabelScope;
import com.perl5.lang.perl.psi.properties.PerlLoop;
import com.perl5.lang.perl.psi.properties.PerlStatementsContainer;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementStub;
import com.perl5.lang.perl.psi.stubs.imports.PerlUseStatementStub;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.lexer.PerlTokenSets.STRING_CONTENT_TOKENSET;

/**
 * Created by hurricup on 09.08.2015.
 */
public class PerlPsiUtil implements PerlElementTypes {
  /**
   * Recursively searches for string content elements beginnign from specified PsiElement
   *
   * @param startWith PsiElement to start from (inclusive)
   * @return list of PerlStringContentElement
   */
  public static Collection<PerlStringContentElement> collectStringElements(@NotNull PsiElement startWith) {
    final List<PerlStringContentElement> result = new ArrayList<PerlStringContentElement>();
    processStringElements(startWith, new Processor<PerlStringContentElement>() {
      @Override
      public boolean process(PerlStringContentElement perlStringContentElement) {
        result.add(perlStringContentElement);
        return true;
      }
    });
    return result;
  }

  /**
   * Recursive searcher for string content elements
   *
   * @param startWith element to start with (inclusive)
   */
  private static void processStringElements(PsiElement startWith, Processor<PerlStringContentElement> processor) {
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

    List<String> result = new ArrayList<String>();
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
      throw new IncorrectOperationException("Unable to find manipulator for " + element);
    }
    return manipulator;
  }


  /**
   * Renaming PsiElement using manipulator
   *
   * @param element PsiElement to rename
   * @param newName newName
   * @return manipulator return value
   */
  public static PsiElement renameElement(PsiElement element, String newName) {
    //noinspection unchecked
    return PerlPsiUtil.getManipulator(element).handleContentChange(element, newName);
  }


  /**
   * Renames file referencee element. Specific method required, cause somewhere we should put package name, and somewhere filename
   *
   * @param element        element to rename
   * @param newPackageName new package name
   */
  public static void renameFileReferencee(PsiElement element, String newPackageName) {
    if (element instanceof PerlNamespaceElement) {
      PerlPsiUtil.renameElement(element, newPackageName);
    }
  }

  @Nullable
  public static PsiElement getNextSignificantSibling(PsiElement element) {
    PsiElement result = element.getNextSibling();
    while (true) {
      if (result == null || !(result instanceof PsiComment || result instanceof PsiWhiteSpace)) {
        break;
      }
      result = result.getNextSibling();
    }
    return result;
  }

  @Nullable
  public static ASTNode getNextSignificantSibling(ASTNode node) {
    ASTNode result = node.getTreeNext();
    while (true) {
      if (result == null || result.getElementType() != TokenType.NEW_LINE_INDENT && result.getElementType() != TokenType.WHITE_SPACE) {
        break;
      }
      result = result.getTreeNext();
    }
    return result;
  }

  @Nullable
  public static PsiElement getPrevSignificantSibling(PsiElement element) {
    PsiElement result = element.getPrevSibling();
    while (true) {
      if (result == null ||
          !(result instanceof PsiComment || result instanceof PsiWhiteSpace || result.getNode().getElementType() == PerlElementTypes.POD)) {
        break;
      }
      result = result.getPrevSibling();
    }
    return result;
  }

  @Nullable
  public static PsiElement getParentElementOrStub(StubBasedPsiElement currentElement,
                                                  @Nullable final Class<? extends StubElement> stubClass,
                                                  @NotNull final Class<? extends PsiElement> psiClass
  ) {
    Stub stub = currentElement.getStub();
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

  public static List<PsiElement> collectUseStatements(@NotNull final PsiElement rootElement) {
    return CachedValuesManager.getCachedValue(rootElement, new CachedValueProvider<List<PsiElement>>() {
      @Nullable
      @Override
      public Result<List<PsiElement>> compute() {
        return Result.create(collectNamespaceMembers(rootElement, PerlUseStatementStub.class, PerlUseStatement.class), rootElement);
      }
    });
  }

  @NotNull
  public static List<PsiElement> collectNamespaceMembers(@NotNull final PsiElement rootElement,
                                                         @Nullable final Class<? extends StubElement> stubClass,
                                                         @NotNull final Class<? extends PsiElement> psiClass
  ) {
    final List<PsiElement> result = new ArrayList<PsiElement>();
    if (stubClass != null && rootElement instanceof StubBasedPsiElement) {
      Stub rootElementStub = ((StubBasedPsiElement)rootElement).getStub();

      if (rootElementStub != null) {
        processElementsFromStubs(
          rootElementStub,
          stub -> {
            if (stubClass.isInstance(stub)) {
              result.add(((StubBase)stub).getPsi());
            }

            return true;
          },
          PerlNamespaceDefinitionStub.class
        );
        return result;
      }
    }

    processElementsFromPsi(
      rootElement,
      element -> {
        if (psiClass.isInstance(element)) {
          result.add(element);
        }
        return true;
      },
      PerlNamespaceDefinitionElement.class
    );
    return result;
  }

  public static boolean processElementsFromStubs(
    @Nullable Stub stub,
    @Nullable Processor<Stub> processor,
    @Nullable Class<? extends StubElement> avoidClass
  ) {
    if (stub == null || processor == null) {
      return false;
    }

    for (Stub child : stub.getChildrenStubs()) {
      if (!processor.process(child)) {
        return false;
      }

      if (avoidClass == null || !avoidClass.isInstance(child)) // don't enter sublcasses
      {
        if (!processElementsFromStubs(child, processor, avoidClass)) {
          return false;
        }
      }
    }

    if (stub instanceof PerlPolyNamedElementStub) {
      for (StubElement child : ((PerlPolyNamedElementStub)stub).getLightNamedElementsStubs()) {
        if (!processor.process(child)) {
          return false;
        }
      }
    }

    return true;
  }


  public static boolean processElementsFromPsi(
    @Nullable PsiElement element,
    @Nullable Processor<PsiElement> processor,
    @Nullable Class<? extends PsiElement> avoidClass
  ) {
    if (element == null || processor == null) {
      return false;
    }

    for (PsiElement child : element.getChildren()) {
      if (!processor.process(child)) {
        return false;
      }

      if (avoidClass == null || !avoidClass.isInstance(child)) // don't enter subclasses
      {
        if (!processElementsFromPsi(child, processor, avoidClass)) {
          return false;
        }
      }
    }

    if (element instanceof PerlPolyNamedElement) {
      for (PerlDelegatingLightNamedElement lightNamedElement : ((PerlPolyNamedElement)element).getLightElements()) {
        if (!processor.process(lightNamedElement)) {
          return false;
        }
      }
    }

    return true;
  }

  public static boolean isHeredocAhead(PsiElement startElement, final int lineEndOffset) {
    HeredocChecker checker = new HeredocChecker(lineEndOffset);
    iteratePsiElementsRight(startElement, checker);
    return checker.getResult();
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
    return element != null && PerlParserDefinition.WHITE_SPACE_AND_COMMENTS.contains(element.getNode().getElementType());
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

    final List<PerlAnnotation> result = new ArrayList<PerlAnnotation>();
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
    processElementAnnotations(element, new Processor<PerlAnnotation>() {
      @Override
      public boolean process(PerlAnnotation perlAnnotation) {
        if (annotationClass.isInstance(perlAnnotation)) {
          result[0] = perlAnnotation;
          return false;
        }
        return true;
      }
    });
    //noinspection unchecked
    return (T)result[0];
  }


  public static void processElementAnnotations(@NotNull PsiElement element, @NotNull Processor<PerlAnnotation> annotationProcessor) {
    PsiElement run = element.getPrevSibling();

    if (run == null)    // first element, check parents
    {
      run = element;
      while (true) {
        if (run instanceof PsiFile) {
          return;
        }

        PsiElement parent = run.getParent();
        if (parent != null && parent.getNode().getStartOffset() == run.getNode().getStartOffset()) {
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
  public static String getPerlExpressionType(PsiElement element) {
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
    else if (element instanceof PsiPerlTagScalar && "__PACKAGE__".equals(element.getText())) {
      return PerlPackageUtil.getContextPackageName(element);
    }
    else if (element instanceof PsiPerlParenthesisedExprImpl) {
      return getPerlExpressionType(((PsiPerlParenthesisedExprImpl)element).getExpr());
    }
    else if (element instanceof PsiPerlDerefExprImpl) {
      return getPerlExpressionType(element.getLastChild());
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

  @Nullable
  public static PsiElement getFirstContentTokenOfString(PsiElement name) {

    if (name instanceof PsiPerlStringBare) {
      return name.getFirstChild();
    }
    else if (name instanceof PerlString) {
      PsiElement run = name.getFirstChild();
      if (run != null) {
        run = run.getNextSibling();
        if (STRING_CONTENT_TOKENSET.contains(PsiUtilCore.getElementType(run))) {
          return run;
        }
      }
    }
    return null;
  }

  static public abstract class HeredocProcessor implements Processor<PsiElement> {
    protected final int lineEndOffset;

    public HeredocProcessor(int lineEndOffset) {
      this.lineEndOffset = lineEndOffset;
    }
  }

  private static class HeredocChecker extends HeredocProcessor {
    protected boolean myResult = false;

    public HeredocChecker(int lineEndOffset) {
      super(lineEndOffset);
    }

    @Override
    public boolean process(PsiElement element) {
      if (element instanceof PerlHeredocOpener) {
        myResult = true;
        return false;
      }
      if (element != null && element.getTextRange().getStartOffset() >= lineEndOffset) {
        myResult = element instanceof PerlHeredocElementImpl;
        return false;
      }
      return true;
    }

    public boolean getResult() {
      return myResult;
    }
  }
}
